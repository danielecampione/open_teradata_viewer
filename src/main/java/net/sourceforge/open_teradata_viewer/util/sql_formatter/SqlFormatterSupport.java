/*
 * Open Teradata Viewer ( sql formatter )
 * Copyright (C), D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.open_teradata_viewer.util.sql_formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tokenizes a SQL statement and lays it out on multiple, indented lines.
 * This is an original, from-scratch implementation (not a port of any
 * third-party formatter): it keeps quoted literals, quoted identifiers and
 * comments intact, and breaks the statement into readable lines by
 * recognizing a small set of well known clause keywords, join keywords,
 * logical operators, {@code CASE} expressions, parentheses and commas.
 * {@code ALTER TABLE} and {@code COMMENT ON} statements get their own
 * dedicated, keyword-driven layout instead of the generic one, since a
 * plain paren-depth layout does not read well for either of them.
 *
 * @author D. Campione
 *
 */
final class SqlFormatterSupport {

    private static final String INDENT_UNIT = "    ";

    /** Sentinel token for an open-paren directly attached to the previous
     *  token in the source (e.g. a function call: {@code COUNT(*)}), as
     *  opposed to one separated by whitespace (e.g. {@code WHERE id IN (1, 2)}).
     *  This distinction is only knowable at tokenize time, when the original
     *  character offsets are still available. */
    private static final String OPEN_PAREN_ATTACHED = "\u0001";

    /** Keywords that start a brand new top-level clause. */
    private static final Set<String> CLAUSE_STARTERS_1 = new HashSet<String>(Arrays.asList(
            "SELECT", "FROM", "WHERE", "SET", "VALUES", "HAVING", "RETURNING",
            "LIMIT", "OFFSET", "UNION", "INTERSECT", "MINUS", "EXCEPT"));

    /** Two-word clause starters, checked as a whole against the next token pair. */
    private static final Set<String> CLAUSE_STARTERS_2 = new HashSet<String>(Arrays.asList(
            "GROUP BY", "ORDER BY", "UNION ALL", "START WITH", "CONNECT BY",
            "INSERT INTO"));

    /** Single-word JOIN. */
    private static final String JOIN_1 = "JOIN";

    /** Multi-word JOIN variants, longest match first. */
    private static final String[] JOIN_3 = { "LEFT OUTER JOIN", "RIGHT OUTER JOIN", "FULL OUTER JOIN" };
    private static final String[] JOIN_2 = { "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL JOIN", "CROSS JOIN" };

    /** Top-level keywords that, inside an {@code ALTER TABLE} statement, each
     *  start a brand new sub-action and therefore deserve their own line.
     *  Whatever qualifies the action itself ({@code CONSTRAINT name},
     *  {@code CHECK (...)}, {@code REFERENCES ...}, ...) stays attached to
     *  that same line: splitting a single action across several lines reads
     *  worse than keeping it together. */
    private static final Set<String> ALTER_TABLE_KEYWORDS = new HashSet<String>(Arrays.asList(
            "ADD", "DROP", "ALTER", "MODIFY", "RENAME"));

    private SqlFormatterSupport() {
        // Utility class.
    }

    static String formatBasic(String source) {
        return layout(source, true);
    }

    static String formatDdl(String source) {
        if (source == null || source.trim().length() == 0) {
            return "";
        }
        String leading = leadingKeywords(source, 2);
        if ("ALTER TABLE".equals(leading)) {
            return formatAlterTable(source);
        }
        if ("COMMENT ON".equals(leading)) {
            return formatCommentOn(source);
        }
        // CREATE TABLE (and anything else not specifically handled above)
        // already reads well under the generic, paren-depth-aware layout:
        // the outer "(column, column, ...)" list is what drives it, and
        // there is no other top-level keyword that needs its own line.
        return layout(source, false);
    }

    private static String layout(String source, boolean recognizeClauses) {
        if (source == null || source.trim().length() == 0) {
            return "";
        }

        List<String> tokens = tokenize(source);
        StringBuilder out = new StringBuilder(source.length() + 64);

        int parenDepth = 0;
        int clauseParenDepth = 0;
        // Indent level used for the line right after a comma break; kept as an
        // explicit variable because BASIC mode (list under a clause keyword)
        // and DDL mode (list inside a definition paren) compute it differently.
        int continuationIndent = 1;
        boolean atLineStart = true;
        boolean noSpaceBeforeNext = false;
        // DDL mode only: true once we are inside the outer "(column, column, ...)"
        // definition list of the statement currently being laid out.
        boolean ddlInDefinitionList = false;
        // The indent level of the line currently being written, kept in sync
        // with every newLine() call below. A CASE expression can start in the
        // middle of an already-indented line (e.g. a SELECT column list one
        // level in), so WHEN/ELSE/END need this rather than just parenDepth
        // to line themselves up correctly relative to their own CASE.
        int currentIndent = 0;
        // BASIC mode only: one entry per currently open CASE, holding the
        // indent level its WHEN/ELSE lines should use (its END then closes
        // one level shallower than that, back at the CASE's own line).
        List<Integer> caseIndents = new ArrayList<Integer>();
        // BASIC mode only: set right after a BETWEEN keyword so the AND that
        // belongs to it (as in "x BETWEEN 1 AND 10") is treated as a plain,
        // same-line token instead of triggering the usual AND/OR line break.
        boolean suppressNextAnd = false;
        // BASIC mode only: the position in "out" right after each currently
        // open paren's "(" character, one entry per open paren, together with
        // whether that paren was "attached" (a function call, e.g. COUNT(...))
        // as opposed to a standalone group (a subquery, a CTE definition, a
        // parenthesized condition, ...). Used when a paren closes to decide
        // whether it should get a line of its own: only a standalone group
        // whose content actually broke across lines does; a function call
        // stays closed on whatever line its last argument ended on, even if
        // one of its arguments (say, a CASE) spans several lines itself.
        List<Integer> parenOpenLen = new ArrayList<Integer>();
        List<Boolean> parenAttached = new ArrayList<Boolean>();
        // BASIC mode only: clauseParenDepth/continuationIndent describe which
        // comma-separated list (if any) is currently active; they are saved
        // here on entering a paren and restored on leaving it, so that once a
        // subquery or a function call's argument list closes, an outer list
        // that happens to resume at the same paren depth does not keep using
        // a comma-break context left over from inside that closed paren.
        List<Integer> clauseParenDepthStack = new ArrayList<Integer>();
        List<Integer> continuationIndentStack = new ArrayList<Integer>();
        // BASIC mode only: the indent level of the line each currently open
        // paren was itself opened on, one entry per open paren. A forced
        // closing-paren line uses this (not the raw paren depth) so it lines
        // up with the text that opened it, even when that opening happened
        // mid-line at an indent one or more levels deeper than its own depth
        // (e.g. a parenthesized "AND (... OR ...)" group under a WHERE).
        List<Integer> parenOpenIndent = new ArrayList<Integer>();
        // BASIC mode only: paren depth of an OVER (...) window specification
        // currently being written, or -1 when not inside one. PARTITION BY /
        // ORDER BY read differently there than as top-level query clauses, so
        // everything between OVER's "(" and its matching ")" is emitted as
        // plain, same-line tokens instead of going through clause recognition.
        int opaqueDepth = -1;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String upper = isPlainWord(token) ? token.toUpperCase() : "";

            // A line comment runs to the end of the physical source line, so
            // whatever follows it must start on a fresh line: otherwise it
            // would visually (and misleadingly) appear to be part of the
            // comment once the original line breaks are gone.
            if (token.startsWith("--")) {
                appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
                newLine(out, currentIndent);
                atLineStart = true;
                noSpaceBeforeNext = true;
                continue;
            }

            // The "(" right after OVER opens a window specification: PARTITION
            // BY / ORDER BY in there describe the window, not a new top-level
            // clause, so it is kept opaque to clause recognition below and
            // stays on one line like any other function argument list.
            if (recognizeClauses && opaqueDepth < 0 && i > 0
                    && ("(".equals(token) || OPEN_PAREN_ATTACHED.equals(token))
                    && "OVER".equalsIgnoreCase(tokens.get(i - 1))) {
                appendWithSpacing(out, "(", atLineStart, OPEN_PAREN_ATTACHED.equals(token));
                parenDepth++;
                opaqueDepth = parenDepth;
                atLineStart = false;
                noSpaceBeforeNext = true;
                continue;
            }
            if (opaqueDepth >= 0) {
                if ("(".equals(token) || OPEN_PAREN_ATTACHED.equals(token)) {
                    appendWithSpacing(out, "(", atLineStart, OPEN_PAREN_ATTACHED.equals(token));
                    parenDepth++;
                    atLineStart = false;
                    noSpaceBeforeNext = true;
                    continue;
                }
                if (")".equals(token)) {
                    if (parenDepth > 0) {
                        parenDepth--;
                    }
                    appendWithSpacing(out, token, atLineStart, true);
                    atLineStart = false;
                    noSpaceBeforeNext = false;
                    if (parenDepth < opaqueDepth) {
                        opaqueDepth = -1;
                    }
                    continue;
                }
                appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }

            // Try to recognize multi-word keyword phrases (longest match first).
            String phrase3 = lookaheadPhrase(tokens, i, 3);
            String phrase2 = lookaheadPhrase(tokens, i, 2);

            if (recognizeClauses && "WITH".equals(upper)) {
                // A CTE list ("WITH a AS (...), b AS (...)") is a list of
                // sibling definitions, not values under a clause: each one
                // should line up with WITH itself, not indent a level deeper.
                newLine(out, parenDepth);
                currentIndent = parenDepth;
                clauseParenDepth = parenDepth;
                continuationIndent = parenDepth;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && phrase2 != null && CLAUSE_STARTERS_2.contains(phrase2)) {
                newLine(out, parenDepth);
                currentIndent = parenDepth;
                clauseParenDepth = parenDepth;
                continuationIndent = parenDepth + 1;
                out.append(tokens.get(i)).append(' ').append(tokens.get(i + 1));
                i++;
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && phrase3 != null && contains(JOIN_3, phrase3)) {
                newLine(out, parenDepth);
                currentIndent = parenDepth;
                out.append(tokens.get(i)).append(' ').append(tokens.get(i + 1)).append(' ').append(tokens.get(i + 2));
                i += 2;
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && phrase2 != null && contains(JOIN_2, phrase2)) {
                newLine(out, parenDepth);
                currentIndent = parenDepth;
                out.append(tokens.get(i)).append(' ').append(tokens.get(i + 1));
                i++;
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && CLAUSE_STARTERS_1.contains(upper)) {
                newLine(out, parenDepth);
                currentIndent = parenDepth;
                clauseParenDepth = parenDepth;
                continuationIndent = parenDepth + 1;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && JOIN_1.equals(upper)) {
                newLine(out, parenDepth);
                currentIndent = parenDepth;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && "ON".equals(upper)) {
                newLine(out, parenDepth + 1);
                currentIndent = parenDepth + 1;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && "BETWEEN".equals(upper)) {
                appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
                atLineStart = false;
                noSpaceBeforeNext = false;
                suppressNextAnd = true;
                continue;
            }
            if (recognizeClauses && ("AND".equals(upper) || "OR".equals(upper))) {
                if (suppressNextAnd && "AND".equals(upper)) {
                    suppressNextAnd = false;
                    appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
                    atLineStart = false;
                    noSpaceBeforeNext = false;
                    continue;
                }
                newLine(out, parenDepth + 1);
                currentIndent = parenDepth + 1;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && "CASE".equals(upper)) {
                // CASE keeps flowing on the current line (it is normally part
                // of a larger expression); only its WHEN/ELSE/END need a line
                // of their own, one level deeper than wherever CASE itself sits.
                appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
                atLineStart = false;
                noSpaceBeforeNext = false;
                caseIndents.add(currentIndent + 1);
                continue;
            }
            if (recognizeClauses && ("WHEN".equals(upper) || "ELSE".equals(upper))) {
                int level = caseIndents.isEmpty() ? currentIndent + 1
                        : caseIndents.get(caseIndents.size() - 1);
                newLine(out, level);
                currentIndent = level;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && "END".equals(upper) && !caseIndents.isEmpty()) {
                int level = caseIndents.remove(caseIndents.size() - 1) - 1;
                newLine(out, level);
                currentIndent = level;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }

            if ("(".equals(token) || OPEN_PAREN_ATTACHED.equals(token)) {
                boolean attached = OPEN_PAREN_ATTACHED.equals(token);
                appendWithSpacing(out, "(", atLineStart, attached);
                parenDepth++;
                if (!recognizeClauses && parenDepth == 1 && !ddlInDefinitionList) {
                    ddlInDefinitionList = true;
                    clauseParenDepth = 1;
                    continuationIndent = 1;
                    newLine(out, 1);
                    currentIndent = 1;
                    atLineStart = true;
                } else {
                    if (recognizeClauses) {
                        parenOpenLen.add(out.length());
                        parenAttached.add(attached);
                        clauseParenDepthStack.add(clauseParenDepth);
                        continuationIndentStack.add(continuationIndent);
                        parenOpenIndent.add(currentIndent);
                    }
                    atLineStart = false;
                    noSpaceBeforeNext = true;
                }
                continue;
            }
            if (")".equals(token)) {
                boolean forceOwnLine = false;
                int closeIndent = parenDepth > 0 ? parenDepth - 1 : 0;
                if (!recognizeClauses && parenDepth == 1 && ddlInDefinitionList) {
                    ddlInDefinitionList = false;
                    forceOwnLine = true;
                    closeIndent = 0;
                } else if (recognizeClauses && !parenOpenLen.isEmpty()) {
                    int openLen = parenOpenLen.remove(parenOpenLen.size() - 1);
                    boolean wasAttached = parenAttached.remove(parenAttached.size() - 1);
                    forceOwnLine = !wasAttached && out.indexOf("\n", openLen) >= 0;
                    closeIndent = parenOpenIndent.remove(parenOpenIndent.size() - 1);
                    clauseParenDepth = clauseParenDepthStack.remove(clauseParenDepthStack.size() - 1);
                    continuationIndent = continuationIndentStack.remove(continuationIndentStack.size() - 1);
                }
                if (parenDepth > 0) {
                    parenDepth--;
                }
                if (forceOwnLine) {
                    newLine(out, closeIndent);
                    currentIndent = closeIndent;
                    atLineStart = true;
                }
                appendWithSpacing(out, token, atLineStart, true);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (",".equals(token)) {
                out.append(token);
                if (parenDepth == clauseParenDepth) {
                    newLine(out, continuationIndent);
                    currentIndent = continuationIndent;
                    atLineStart = true;
                } else {
                    noSpaceBeforeNext = false;
                    atLineStart = false;
                }
                continue;
            }
            if (";".equals(token)) {
                out.append(token);
                out.append('\n');
                atLineStart = true;
                noSpaceBeforeNext = true;
                clauseParenDepth = parenDepth;
                currentIndent = 0;
                continue;
            }

            appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
            atLineStart = false;
            noSpaceBeforeNext = false;
        }

        return out.toString().trim();
    }

    /**
     * Lays out an {@code ALTER TABLE} statement by starting a new,
     * one-level-indented line at every top-level (paren depth 0) sub-action
     * keyword ({@code ADD}, {@code DROP}, {@code MODIFY}, ...) and at every
     * top-level comma, so that a multi-action statement reads as one action
     * per line. Everything that qualifies the action ({@code CONSTRAINT} name,
     * {@code CHECK (...)}, {@code REFERENCES ...}, a column's type arguments,
     * ...) is left untouched on that same line: only the outer structure of
     * the statement is broken up.
     */
    private static String formatAlterTable(String source) {
        List<String> tokens = tokenize(source);
        StringBuilder out = new StringBuilder(source.length() + 64);

        int depth = 0;
        boolean atLineStart = true;
        boolean noSpaceBeforeNext = false;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String upper = isPlainWord(token) ? token.toUpperCase() : "";

            if (token.startsWith("--")) {
                appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
                newLine(out, 1);
                atLineStart = true;
                noSpaceBeforeNext = true;
                continue;
            }
            if ("(".equals(token) || OPEN_PAREN_ATTACHED.equals(token)) {
                boolean attached = OPEN_PAREN_ATTACHED.equals(token);
                appendWithSpacing(out, "(", atLineStart, attached);
                depth++;
                atLineStart = false;
                noSpaceBeforeNext = true;
                continue;
            }
            if (")".equals(token)) {
                if (depth > 0) {
                    depth--;
                }
                appendWithSpacing(out, token, atLineStart, true);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (",".equals(token)) {
                // Unconditional append, exactly like the generic layout()
                // method: a comma never gets a leading space, regardless of
                // depth, only appendWithSpacing() would introduce one.
                out.append(token);
                if (depth == 0) {
                    newLine(out, 1);
                    atLineStart = true;
                } else {
                    atLineStart = false;
                }
                noSpaceBeforeNext = false;
                continue;
            }
            // Index 0 is always the statement's own "ALTER" keyword, which
            // must stay on the header line together with "TABLE" and the
            // table name; only a *later* occurrence is a real sub-action
            // (e.g. the second ALTER in "... ALTER COLUMN x SET DEFAULT 1").
            // atLineStart is also checked so a keyword right after a comma
            // break (which already opened this line) does not open a second,
            // blank one.
            if (depth == 0 && i > 0 && !atLineStart && ALTER_TABLE_KEYWORDS.contains(upper)) {
                newLine(out, 1);
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }

            appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
            atLineStart = false;
            noSpaceBeforeNext = false;
        }

        return out.toString().trim();
    }

    /**
     * Lays out a {@code COMMENT ON ... IS '...'} statement by breaking the
     * line right before {@code IS}, which is the one place a long comment
     * statement benefits from a break.
     */
    private static String formatCommentOn(String source) {
        List<String> tokens = tokenize(source);
        StringBuilder out = new StringBuilder(source.length() + 32);

        boolean atLineStart = true;
        boolean noSpaceBeforeNext = false;

        for (String token : tokens) {
            String upper = isPlainWord(token) ? token.toUpperCase() : "";

            if ("IS".equals(upper)) {
                newLine(out, 1);
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }

            appendWithSpacing(out, token, atLineStart, noSpaceBeforeNext);
            atLineStart = false;
            noSpaceBeforeNext = false;
        }

        return out.toString().trim();
    }

    private static void appendWithSpacing(StringBuilder out, String token, boolean atLineStart, boolean noSpaceBefore) {
        if (!atLineStart && !noSpaceBefore && out.length() > 0) {
            char last = out.charAt(out.length() - 1);
            if (last != '\n' && last != '(') {
                out.append(' ');
            }
        }
        out.append(token);
    }

    private static void newLine(StringBuilder out, int indentLevel) {
        if (out.length() > 0) {
            // Avoid emitting a blank line if we are already at the start of one.
            char last = out.charAt(out.length() - 1);
            if (last != '\n') {
                out.append('\n');
            } else {
                // Already at line start: drop the trailing newline we are about to
                // duplicate, the indent below will re-open the same fresh line.
            }
        }
        for (int i = 0; i < indentLevel; i++) {
            out.append(INDENT_UNIT);
        }
    }

    private static String lookaheadPhrase(List<String> tokens, int i, int wordCount) {
        if (i + wordCount > tokens.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < wordCount; k++) {
            String t = tokens.get(i + k);
            if (!isPlainWord(t)) {
                return null;
            }
            if (k > 0) {
                sb.append(' ');
            }
            sb.append(t.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * Uppercases the first {@code wordCount} whitespace-separated words of
     * {@code source}, ignoring any leading whitespace. Used to dispatch a
     * DDL statement to its dedicated layout method by its opening keywords,
     * without the cost of a full tokenize() pass.
     */
    private static String leadingKeywords(String source, int wordCount) {
        String trimmed = source.trim();
        StringBuilder sb = new StringBuilder();
        int n = trimmed.length();
        int i = 0;
        int word = 0;
        while (i < n && word < wordCount) {
            while (i < n && Character.isWhitespace(trimmed.charAt(i))) {
                i++;
            }
            int start = i;
            while (i < n && !Character.isWhitespace(trimmed.charAt(i))) {
                i++;
            }
            if (i > start) {
                if (word > 0) {
                    sb.append(' ');
                }
                sb.append(trimmed.substring(start, i).toUpperCase());
                word++;
            }
        }
        return sb.toString();
    }

    private static boolean contains(String[] arr, String value) {
        for (String s : arr) {
            if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAttachingChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '\'' || c == '"';
    }

    private static boolean isPlainWord(String token) {
        if (token.length() == 0) {
            return false;
        }
        char c0 = token.charAt(0);
        if (c0 == '\'' || c0 == '"' || c0 == '(' || c0 == ')' || c0 == ',' || c0 == ';') {
            return false;
        }
        if (token.startsWith("--") || token.startsWith("/*")) {
            return false;
        }
        return Character.isLetter(c0) || c0 == '_';
    }

    private static List<String> tokenize(String sql) {
        List<String> tokens = new ArrayList<String>();
        int i = 0;
        int n = sql.length();
        while (i < n) {
            char c = sql.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (c == '\'') {
                int start = i;
                i++;
                while (i < n) {
                    if (sql.charAt(i) == '\'') {
                        if (i + 1 < n && sql.charAt(i + 1) == '\'') {
                            i += 2;
                            continue;
                        }
                        i++;
                        break;
                    }
                    i++;
                }
                tokens.add(sql.substring(start, Math.min(i, n)));
                continue;
            }

            if (c == '"') {
                int start = i;
                i++;
                while (i < n && sql.charAt(i) != '"') {
                    i++;
                }
                if (i < n) {
                    i++;
                }
                tokens.add(sql.substring(start, i));
                continue;
            }

            if (c == '-' && i + 1 < n && sql.charAt(i + 1) == '-') {
                int start = i;
                while (i < n && sql.charAt(i) != '\n') {
                    i++;
                }
                tokens.add(sql.substring(start, i));
                continue;
            }

            if (c == '/' && i + 1 < n && sql.charAt(i + 1) == '*') {
                int start = i;
                i += 2;
                while (i + 1 < n && !(sql.charAt(i) == '*' && sql.charAt(i + 1) == '/')) {
                    i++;
                }
                i = Math.min(i + 2, n);
                tokens.add(sql.substring(start, i));
                continue;
            }

            if (c == '(') {
                boolean attached = i > 0 && isAttachingChar(sql.charAt(i - 1));
                tokens.add(attached ? OPEN_PAREN_ATTACHED : "(");
                i++;
                continue;
            }
            if (c == ')' || c == ',' || c == ';') {
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }

            int start = i;
            while (i < n) {
                char cc = sql.charAt(i);
                if (Character.isWhitespace(cc) || cc == '\'' || cc == '"' || cc == '(' || cc == ')'
                        || cc == ',' || cc == ';') {
                    break;
                }
                if (cc == '-' && i + 1 < n && sql.charAt(i + 1) == '-') {
                    break;
                }
                if (cc == '/' && i + 1 < n && sql.charAt(i + 1) == '*') {
                    break;
                }
                i++;
            }
            tokens.add(sql.substring(start, i));
        }
        return tokens;
    }
}
