/*
 * Open Teradata Viewer ( editor syntax modes )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax.modes;

import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.editor.syntax.AbstractTokenMaker;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.editor.syntax.TokenMap;

/**
 * Scanner for Windows batch files.
 *
 * A token maker that turns text into a linked list of <code>IToken</code>s for
 * syntax highlighting Microsoft Windows batch files.
 *
 * @author D. Campione
 *
 */
public class WindowsBatchTokenMaker extends AbstractTokenMaker {

    protected final String operators = "@:*<>=?";

    private int currentTokenStart;
    private int currentTokenType;

    private VariableType varType;

    /** Ctor. */
    public WindowsBatchTokenMaker() {
        super(); // Initializes tokensToHighlight
    }

    /**
     * Checks the token to give it the exact ID it deserves before being passed
     * up to the super method.
     *
     * @param segment <code>Segment</code> to get text from.
     * @param start Start offset in <code>segment</code> of token.
     * @param end End offset in <code>segment</code> of token.
     * @param tokenType The token's type.
     * @param startOffset The offset in the document at which the token occurs.
     */
    @Override
    public void addToken(Segment segment, int start, int end, int tokenType,
            int startOffset) {
        switch (tokenType) {
        // Since reserved words, functions and data types are all passed into
        // here as "identifiers", we have to see what the token really is..
        case IToken.IDENTIFIER:
            int value = wordsToHighlight.get(segment, start, end);
            if (value != -1) {
                tokenType = value;
            }
            break;
        }

        super.addToken(segment, start, end, tokenType, startOffset);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getLineCommentStartAndEnd(int languageIndex) {
        return new String[] { "rem ", null };
    }

    /**
     * Returns whether tokens of the specified type should have "mark
     * occurrences" enabled for the current programming language.
     *
     * @param type The token type.
     * @return Whether tokens of this type should have "mark occurrences"
     *         enabled.
     */
    @Override
    public boolean getMarkOccurrencesOfTokenType(int type) {
        return type == IToken.IDENTIFIER || type == IToken.VARIABLE;
    }

    /**
     * Returns the words to highlight for Windows batch files.
     *
     * @return A <code>TokenMap</code> containing the words to highlight for
     *         Windows batch files.
     * @see net.sourceforge.open_teradata_viewer.editor.syntax.AbstractTokenMaker#getWordsToHighlight
     */
    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap(true); // Ignore case
        int reservedWord = IToken.RESERVED_WORD;

        // Batch-file specific stuff
        tokenMap.put("goto", reservedWord);
        tokenMap.put("if", reservedWord);
        tokenMap.put("shift", reservedWord);
        tokenMap.put("start", reservedWord);

        // General command line stuff
        tokenMap.put("ansi.sys", reservedWord);
        tokenMap.put("append", reservedWord);
        tokenMap.put("arp", reservedWord);
        tokenMap.put("assign", reservedWord);
        tokenMap.put("assoc", reservedWord);
        tokenMap.put("at", reservedWord);
        tokenMap.put("attrib", reservedWord);
        tokenMap.put("break", reservedWord);
        tokenMap.put("cacls", reservedWord);
        tokenMap.put("call", reservedWord);
        tokenMap.put("cd", reservedWord);
        tokenMap.put("chcp", reservedWord);
        tokenMap.put("chdir", reservedWord);
        tokenMap.put("chkdsk", reservedWord);
        tokenMap.put("chknfts", reservedWord);
        tokenMap.put("choice", reservedWord);
        tokenMap.put("cls", reservedWord);
        tokenMap.put("cmd", reservedWord);
        tokenMap.put("color", reservedWord);
        tokenMap.put("comp", reservedWord);
        tokenMap.put("compact", reservedWord);
        tokenMap.put("control", reservedWord);
        tokenMap.put("convert", reservedWord);
        tokenMap.put("copy", reservedWord);
        tokenMap.put("ctty", reservedWord);
        tokenMap.put("date", reservedWord);
        tokenMap.put("debug", reservedWord);
        tokenMap.put("defrag", reservedWord);
        tokenMap.put("del", reservedWord);
        tokenMap.put("deltree", reservedWord);
        tokenMap.put("dir", reservedWord);
        tokenMap.put("diskcomp", reservedWord);
        tokenMap.put("diskcopy", reservedWord);
        tokenMap.put("do", reservedWord);
        tokenMap.put("doskey", reservedWord);
        tokenMap.put("dosshell", reservedWord);
        tokenMap.put("drivparm", reservedWord);
        tokenMap.put("echo", reservedWord);
        tokenMap.put("edit", reservedWord);
        tokenMap.put("edlin", reservedWord);
        tokenMap.put("emm386", reservedWord);
        tokenMap.put("erase", reservedWord);
        tokenMap.put("exist", reservedWord);
        tokenMap.put("exit", reservedWord);
        tokenMap.put("expand", reservedWord);
        tokenMap.put("extract", reservedWord);
        tokenMap.put("fasthelp", reservedWord);
        tokenMap.put("fc", reservedWord);
        tokenMap.put("fdisk", reservedWord);
        tokenMap.put("find", reservedWord);
        tokenMap.put("for", reservedWord);
        tokenMap.put("format", reservedWord);
        tokenMap.put("ftp", reservedWord);
        tokenMap.put("graftabl", reservedWord);
        tokenMap.put("help", reservedWord);
        tokenMap.put("ifshlp.sys", reservedWord);
        tokenMap.put("in", reservedWord);
        tokenMap.put("ipconfig", reservedWord);
        tokenMap.put("keyb", reservedWord);
        tokenMap.put("kill", reservedWord);
        tokenMap.put("label", reservedWord);
        tokenMap.put("lh", reservedWord);
        tokenMap.put("loadfix", reservedWord);
        tokenMap.put("loadhigh", reservedWord);
        tokenMap.put("lock", reservedWord);
        tokenMap.put("md", reservedWord);
        tokenMap.put("mem", reservedWord);
        tokenMap.put("mkdir", reservedWord);
        tokenMap.put("mklink", reservedWord);
        tokenMap.put("mode", reservedWord);
        tokenMap.put("more", reservedWord);
        tokenMap.put("move", reservedWord);
        tokenMap.put("msav", reservedWord);
        tokenMap.put("msd", reservedWord);
        tokenMap.put("mscdex", reservedWord);
        tokenMap.put("nbtstat", reservedWord);
        tokenMap.put("net", reservedWord);
        tokenMap.put("netstat", reservedWord);
        tokenMap.put("nlsfunc", reservedWord);
        tokenMap.put("not", reservedWord);
        tokenMap.put("nslookup", reservedWord);
        tokenMap.put("path", reservedWord);
        tokenMap.put("pathping", reservedWord);
        tokenMap.put("pause", reservedWord);
        tokenMap.put("ping", reservedWord);
        tokenMap.put("power", reservedWord);
        tokenMap.put("print", reservedWord);
        tokenMap.put("prompt", reservedWord);
        tokenMap.put("pushd", reservedWord);
        tokenMap.put("popd", reservedWord);
        tokenMap.put("qbasic", reservedWord);
        tokenMap.put("rd", reservedWord);
        tokenMap.put("ren", reservedWord);
        tokenMap.put("rename", reservedWord);
        tokenMap.put("rmdir", reservedWord);
        tokenMap.put("route", reservedWord);
        tokenMap.put("sc", reservedWord);
        tokenMap.put("scandisk", reservedWord);
        tokenMap.put("scandreg", reservedWord);
        tokenMap.put("set", reservedWord);
        tokenMap.put("setx", reservedWord);
        tokenMap.put("setver", reservedWord);
        tokenMap.put("share", reservedWord);
        tokenMap.put("shutdown", reservedWord);
        tokenMap.put("smartdrv", reservedWord);
        tokenMap.put("sort", reservedWord);
        tokenMap.put("subset", reservedWord);
        tokenMap.put("switches", reservedWord);
        tokenMap.put("sys", reservedWord);
        tokenMap.put("time", reservedWord);
        tokenMap.put("tracert", reservedWord);
        tokenMap.put("tree", reservedWord);
        tokenMap.put("type", reservedWord);
        tokenMap.put("undelete", reservedWord);
        tokenMap.put("unformat", reservedWord);
        tokenMap.put("unlock", reservedWord);
        tokenMap.put("ver", reservedWord);
        tokenMap.put("verify", reservedWord);
        tokenMap.put("vol", reservedWord);
        tokenMap.put("xcopy", reservedWord);

        return tokenMap;
    }

    /**
     * Returns a list of tokens representing the given text.
     *
     * @param text The text to break into tokens.
     * @param startTokenType The token with which to start tokenizing.
     * @param startOffset The offset at which the line of tokens begins.
     * @return A linked list of tokens representing <code>text</code>.
     */
    @Override
    public IToken getTokenList(Segment text, int startTokenType,
            final int startOffset) {
        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        // See, when we find a token, its starting position is always of the
        // form:
        // 'startOffset + (currentTokenStart-offset)'; but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart' for one less subtraction
        // operation
        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType = startTokenType;

        // beginning:
        for (int i = offset; i < end; i++) {
            char c = array[i];

            switch (currentTokenType) {
            case IToken.NULL:
                currentTokenStart = i; // Starting a new token here

                switch (c) {
                case ' ':
                case '\t':
                    currentTokenType = IToken.WHITESPACE;
                    break;

                case '"':
                    currentTokenType = IToken.ERROR_STRING_DOUBLE;
                    break;

                case '%':
                    currentTokenType = IToken.VARIABLE;
                    break;

                // The "separators"
                case '(':
                case ')':
                    addToken(text, currentTokenStart, i, IToken.SEPARATOR,
                            newStartOffset + currentTokenStart);
                    currentTokenType = IToken.NULL;
                    break;

                // The "separators2"
                case ',':
                case ';':
                    addToken(text, currentTokenStart, i, IToken.IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    currentTokenType = IToken.NULL;
                    break;

                // Newer version of EOL comments, or a label
                case ':':
                    // If this will be the first token added, it is a new-style
                    // comment or a label
                    if (firstToken == null) {
                        if (i < end - 1 && array[i + 1] == ':') { // new-style comment
                            currentTokenType = IToken.COMMENT_EOL;
                        } else { // Label
                            currentTokenType = IToken.PREPROCESSOR;
                        }
                    } else { // Just a colon
                        currentTokenType = IToken.IDENTIFIER;
                    }
                    break;

                default:
                    // Just to speed things up a tad, as this will usually be
                    // the case (if spaces above failed)
                    if (SyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
                        currentTokenType = IToken.IDENTIFIER;
                        break;
                    }

                    int indexOf = operators.indexOf(c, 0);
                    if (indexOf > -1) {
                        addToken(text, currentTokenStart, i, IToken.OPERATOR,
                                newStartOffset + currentTokenStart);
                        currentTokenType = IToken.NULL;
                        break;
                    } else {
                        currentTokenType = IToken.IDENTIFIER;
                        break;
                    }
                } // End of switch (c)

                break;

            case IToken.WHITESPACE:
                switch (c) {
                case ' ':
                case '\t':
                    break; // Still whitespace

                case '"':
                    addToken(text, currentTokenStart, i - 1, IToken.WHITESPACE,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = IToken.ERROR_STRING_DOUBLE;
                    break;

                case '%':
                    addToken(text, currentTokenStart, i - 1, IToken.WHITESPACE,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = IToken.VARIABLE;
                    break;

                // The "separators"
                case '(':
                case ')':
                    addToken(text, currentTokenStart, i - 1, IToken.WHITESPACE,
                            newStartOffset + currentTokenStart);
                    addToken(text, i, i, IToken.SEPARATOR, newStartOffset + i);
                    currentTokenType = IToken.NULL;
                    break;

                // The "separators2"
                case ',':
                case ';':
                    addToken(text, currentTokenStart, i - 1, IToken.WHITESPACE,
                            newStartOffset + currentTokenStart);
                    addToken(text, i, i, IToken.IDENTIFIER, newStartOffset + i);
                    currentTokenType = IToken.NULL;
                    break;

                // Newer version of EOL comments, or a label
                case ':':
                    addToken(text, currentTokenStart, i - 1, IToken.WHITESPACE,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    // If the previous (whitespace) token was the first token
                    // added, this is a new-style comment or a label
                    if (firstToken.getNextToken() == null) {
                        if (i < end - 1 && array[i + 1] == ':') { // new-style comment
                            currentTokenType = IToken.COMMENT_EOL;
                        } else { // Label
                            currentTokenType = IToken.PREPROCESSOR;
                        }
                    } else { // Just a colon
                        currentTokenType = IToken.IDENTIFIER;
                    }
                    break;

                default: // Add the whitespace token and start anew
                    addToken(text, currentTokenStart, i - 1, IToken.WHITESPACE,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;

                    // Just to speed things up a tad, as this will usually be
                    // the case (if spaces above failed)
                    if (SyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
                        currentTokenType = IToken.IDENTIFIER;
                        break;
                    }

                    int indexOf = operators.indexOf(c, 0);
                    if (indexOf > -1) {
                        addToken(text, currentTokenStart, i, IToken.OPERATOR,
                                newStartOffset + currentTokenStart);
                        currentTokenType = IToken.NULL;
                        break;
                    } else {
                        currentTokenType = IToken.IDENTIFIER;
                    }
                } // End of switch (c)

                break;

            default: // Should never happen
            case IToken.IDENTIFIER:
                switch (c) {
                case ' ':
                case '\t':
                    // Check for REM comments
                    if (i - currentTokenStart == 3
                            && (array[i - 3] == 'r' || array[i - 3] == 'R')
                            && (array[i - 2] == 'e' || array[i - 2] == 'E')
                            && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
                        currentTokenType = IToken.COMMENT_EOL;
                        break;
                    }
                    addToken(text, currentTokenStart, i - 1, IToken.IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = IToken.WHITESPACE;
                    break;

                case '"':
                    addToken(text, currentTokenStart, i - 1, IToken.IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = IToken.ERROR_STRING_DOUBLE;
                    break;

                case '%':
                    addToken(text, currentTokenStart, i - 1, IToken.IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = IToken.VARIABLE;
                    break;

                // Should be part of identifiers but not at end of "REM"
                case '\\':
                    // Check for REM comments
                    if (i - currentTokenStart == 3
                            && (array[i - 3] == 'r' || array[i - 3] == 'R')
                            && (array[i - 2] == 'e' || array[i - 2] == 'E')
                            && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
                        currentTokenType = IToken.COMMENT_EOL;
                    }
                    break;

                case '.':
                case '_':
                    break; // Characters good for identifiers

                // The "separators"
                case '(':
                case ')':
                    addToken(text, currentTokenStart, i - 1, IToken.IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    addToken(text, i, i, IToken.SEPARATOR, newStartOffset + i);
                    currentTokenType = IToken.NULL;
                    break;

                // The "separators2"
                case ',':
                case ';':
                    addToken(text, currentTokenStart, i - 1, IToken.IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    addToken(text, i, i, IToken.IDENTIFIER, newStartOffset + i);
                    currentTokenType = IToken.NULL;
                    break;

                default:
                    // Just to speed things up a tad, as this will usually be
                    // the case
                    if (SyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
                        break;
                    }

                    int indexOf = operators.indexOf(c);
                    if (indexOf > -1) {
                        addToken(text, currentTokenStart, i - 1,
                                IToken.IDENTIFIER, newStartOffset
                                        + currentTokenStart);
                        addToken(text, i, i, IToken.OPERATOR, newStartOffset
                                + i);
                        currentTokenType = IToken.NULL;
                        break;
                    }

                    // Otherwise, fall through and assume we're still okay as an
                    // IDENTIFIER..
                } // End of switch (c)

                break;

            case IToken.COMMENT_EOL:
                i = end - 1;
                addToken(text, currentTokenStart, i, IToken.COMMENT_EOL,
                        newStartOffset + currentTokenStart);
                // We need to set token type to null so at the bottom we don't
                // add one more token
                currentTokenType = IToken.NULL;
                break;

            case IToken.PREPROCESSOR: // Used for labels
                i = end - 1;
                addToken(text, currentTokenStart, i, IToken.PREPROCESSOR,
                        newStartOffset + currentTokenStart);
                // We need to set token type to null so at the bottom we don't
                // add one more token
                currentTokenType = IToken.NULL;
                break;

            case IToken.ERROR_STRING_DOUBLE:
                if (c == '"') {
                    addToken(text, currentTokenStart, i,
                            IToken.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset
                                    + currentTokenStart);
                    currentTokenStart = i + 1;
                    currentTokenType = IToken.NULL;
                }
                // Otherwise, we're still an unclosed string..

                break;

            case IToken.VARIABLE:
                if (i == currentTokenStart + 1) { // first character after '%'
                    varType = VariableType.NORMAL_VAR;
                    switch (c) {
                    case '{':
                        varType = VariableType.BRACKET_VAR;
                        break;
                    case '~':
                        varType = VariableType.TILDE_VAR;
                        break;
                    case '%':
                        varType = VariableType.DOUBLE_PERCENT_VAR;
                        break;
                    default:
                        if (SyntaxUtilities.isLetter(c) || c == '_' || c == ' ') { // No tab, just space; spaces are okay in variable names
                            break;
                        } else if (SyntaxUtilities.isDigit(c)) { // Single-digit command-line argument ("%1")
                            addToken(text, currentTokenStart, i,
                                    IToken.VARIABLE, newStartOffset
                                            + currentTokenStart);
                            currentTokenType = IToken.NULL;
                            break;
                        } else { // Anything else
                            addToken(text, currentTokenStart, i - 1,
                                    IToken.VARIABLE, newStartOffset
                                            + currentTokenStart);
                            i--;
                            currentTokenType = IToken.NULL;
                            break;
                        }
                    } // End of switch (c)
                } else { // Character other than first after the '%'
                    switch (varType) {
                    case BRACKET_VAR:
                        if (c == '}') {
                            addToken(text, currentTokenStart, i,
                                    IToken.VARIABLE, newStartOffset
                                            + currentTokenStart);
                            currentTokenType = IToken.NULL;
                        }
                        break;
                    case TILDE_VAR:
                        if (!SyntaxUtilities.isLetterOrDigit(c)) {
                            addToken(text, currentTokenStart, i - 1,
                                    IToken.VARIABLE, newStartOffset
                                            + currentTokenStart);
                            i--;
                            currentTokenType = IToken.NULL;
                        }
                        break;
                    case DOUBLE_PERCENT_VAR:
                        // Can be terminated with "%%" or (essentially) a space.
                        // substring chars are valid
                        if (c == '%') {
                            if (i < end - 1 && array[i + 1] == '%') {
                                i++;
                                addToken(text, currentTokenStart, i,
                                        IToken.VARIABLE, newStartOffset
                                                + currentTokenStart);
                                currentTokenType = IToken.NULL;
                            }
                        } else if (!SyntaxUtilities.isLetterOrDigit(c)
                                && c != ':' && c != '~' && c != ',' && c != '-') {
                            addToken(text, currentTokenStart, i - 1,
                                    IToken.VARIABLE, newStartOffset
                                            + currentTokenStart);
                            currentTokenType = IToken.NULL;
                            i--;
                        }
                        break;
                    default:
                        if (c == '%') {
                            addToken(text, currentTokenStart, i,
                                    IToken.VARIABLE, newStartOffset
                                            + currentTokenStart);
                            currentTokenType = IToken.NULL;
                        }
                        break;
                    }
                }
                break;
            } // End of switch (currentTokenType)
        } // End of for (int i=offset; i<end; i++)

        // Deal with the (possibly there) last token
        if (currentTokenType != IToken.NULL) {
            // Check for REM comments
            if (end - currentTokenStart == 3
                    && (array[end - 3] == 'r' || array[end - 3] == 'R')
                    && (array[end - 2] == 'e' || array[end - 2] == 'E')
                    && (array[end - 1] == 'm' || array[end - 1] == 'M')) {
                currentTokenType = IToken.COMMENT_EOL;
            }

            addToken(text, currentTokenStart, end - 1, currentTokenType,
                    newStartOffset + currentTokenStart);
        }

        addNullToken();

        // Return the first token in our linked list
        return firstToken;
    }

    /**
     *
     *
     * @author D. Campione
     *
     */
    private enum VariableType {
        BRACKET_VAR, TILDE_VAR, NORMAL_VAR, DOUBLE_PERCENT_VAR; // Escaped '%' var, special highlighting rules
    }
}