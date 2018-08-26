/*
 * Open Teradata Viewer ( editor syntax folding )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * A fold parser NSIS.<p>
 *
 * Note that this class may impose somewhat of a performance penalty on large
 * source files, since it re-parses the entire document each time folds are
 * reevaluated.
 *
 * @author D. Campione
 * 
 */
public class NsisFoldParser implements IFoldParser {

    private static final char[] KEYWORD_FUNCTION = "Function".toCharArray();
    private static final char[] KEYWORD_FUNCTION_END = "FunctionEnd"
            .toCharArray();
    private static final char[] KEYWORD_SECTION = "Section".toCharArray();
    private static final char[] KEYWORD_SECTION_END = "SectionEnd"
            .toCharArray();

    protected static final char[] C_MLC_END = "*/".toCharArray();

    private static final boolean foundEndKeyword(char[] keyword, IToken t,
            Stack<char[]> endWordStack) {
        return t.is(IToken.RESERVED_WORD, keyword) && !endWordStack.isEmpty()
                && keyword == endWordStack.peek();
    }

    /** {@inheritDoc} */
    @Override
    public List<Fold> getFolds(SyntaxTextArea textArea) {
        List<Fold> folds = new ArrayList<Fold>();

        Fold currentFold = null;
        int lineCount = textArea.getLineCount();
        boolean inMLC = false;
        int mlcStart = 0;
        Stack<char[]> endWordStack = new Stack<char[]>();

        try {
            for (int line = 0; line < lineCount; line++) {
                IToken t = textArea.getTokenListForLine(line);
                while (t != null && t.isPaintable()) {
                    if (t.isComment()) {
                        if (inMLC) {
                            // If we found the end of an MLC that started on a
                            // previous line..
                            if (t.endsWith(C_MLC_END)) {
                                int mlcEnd = t.getEndOffset() - 1;
                                if (currentFold == null) {
                                    currentFold = new Fold(IFoldType.COMMENT,
                                            textArea, mlcStart);
                                    currentFold.setEndOffset(mlcEnd);
                                    folds.add(currentFold);
                                    currentFold = null;
                                } else {
                                    currentFold = currentFold.createChild(
                                            IFoldType.COMMENT, mlcStart);
                                    currentFold.setEndOffset(mlcEnd);
                                    currentFold = currentFold.getParent();
                                }
                                inMLC = false;
                                mlcStart = 0;
                            }
                            // Otherwise, this MLC is continuing on to yet
                            // another line
                        } else {
                            // If we're an MLC that ends on a later line..
                            if (t.getType() != IToken.COMMENT_EOL
                                    && !t.endsWith(C_MLC_END)) {
                                inMLC = true;
                                mlcStart = t.getOffset();
                            }
                        }
                    } else if (t.is(IToken.RESERVED_WORD, KEYWORD_SECTION)) {
                        if (currentFold == null) {
                            currentFold = new Fold(IFoldType.CODE, textArea,
                                    t.getOffset());
                            folds.add(currentFold);
                        } else {
                            currentFold = currentFold.createChild(
                                    IFoldType.CODE, t.getOffset());
                        }
                        endWordStack.push(KEYWORD_SECTION_END);
                    } else if (t.is(IToken.RESERVED_WORD, KEYWORD_FUNCTION)) {
                        if (currentFold == null) {
                            currentFold = new Fold(IFoldType.CODE, textArea,
                                    t.getOffset());
                            folds.add(currentFold);
                        } else {
                            currentFold = currentFold.createChild(
                                    IFoldType.CODE, t.getOffset());
                        }
                        endWordStack.push(KEYWORD_FUNCTION_END);
                    } else if (foundEndKeyword(KEYWORD_SECTION_END, t,
                            endWordStack)
                            || foundEndKeyword(KEYWORD_FUNCTION_END, t,
                                    endWordStack)) {
                        if (currentFold != null) {
                            currentFold.setEndOffset(t.getOffset());
                            Fold parentFold = currentFold.getParent();
                            endWordStack.pop();
                            // Don't add fold markers for single-line blocks
                            if (currentFold.isOnSingleLine()) {
                                if (!currentFold.removeFromParent()) {
                                    folds.remove(folds.size() - 1);
                                }
                            }
                            currentFold = parentFold;
                        }
                    }

                    t = t.getNextToken();
                }
            }
        } catch (BadLocationException ble) { // Should never happen
            ExceptionDialog.hideException(ble);
        }

        return folds;
    }
}