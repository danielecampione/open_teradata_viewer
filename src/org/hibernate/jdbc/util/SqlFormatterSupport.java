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

package org.hibernate.jdbc.util;

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
 * logical operators, parentheses and commas.
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

    private SqlFormatterSupport() {
        // Utility class.
    }

    static String formatBasic(String source) {
        return layout(source, true);
    }

    static String formatDdl(String source) {
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

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String upper = isPlainWord(token) ? token.toUpperCase() : "";

            // Try to recognize multi-word keyword phrases (longest match first).
            String phrase3 = lookaheadPhrase(tokens, i, 3);
            String phrase2 = lookaheadPhrase(tokens, i, 2);

            if (recognizeClauses && phrase2 != null && CLAUSE_STARTERS_2.contains(phrase2)) {
                newLine(out, parenDepth);
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
                out.append(tokens.get(i)).append(' ').append(tokens.get(i + 1)).append(' ').append(tokens.get(i + 2));
                i += 2;
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && phrase2 != null && contains(JOIN_2, phrase2)) {
                newLine(out, parenDepth);
                out.append(tokens.get(i)).append(' ').append(tokens.get(i + 1));
                i++;
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && CLAUSE_STARTERS_1.contains(upper)) {
                newLine(out, parenDepth);
                clauseParenDepth = parenDepth;
                continuationIndent = parenDepth + 1;
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && JOIN_1.equals(upper)) {
                newLine(out, parenDepth);
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && "ON".equals(upper)) {
                newLine(out, parenDepth + 1);
                out.append(token);
                atLineStart = false;
                noSpaceBeforeNext = false;
                continue;
            }
            if (recognizeClauses && ("AND".equals(upper) || "OR".equals(upper))) {
                newLine(out, parenDepth + 1);
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
                    atLineStart = true;
                } else {
                    atLineStart = false;
                    noSpaceBeforeNext = true;
                }
                continue;
            }
            if (")".equals(token)) {
                if (!recognizeClauses && parenDepth == 1 && ddlInDefinitionList) {
                    ddlInDefinitionList = false;
                    newLine(out, 0);
                    atLineStart = true;
                }
                if (parenDepth > 0) {
                    parenDepth--;
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
