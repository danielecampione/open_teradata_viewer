/*
 * Open Teradata Viewer ( editor syntax modes )
 * Copyright (C) 2015, D. Campione
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
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Fold parser for JSON.
 *
 * The fold parser for JSON. Objects (<code>"{ ... }</code>") and arrays
 * (<code>"[ ... ]"</code>) that span multiple lines are considered fold
 * regions.
 *
 * @author D. Campione
 *
 */
public class JsonFoldParser implements IFoldParser {

    private static final Object OBJECT_BLOCK = new Object();
    private static final Object ARRAY_BLOCK = new Object();

    /** {@inheritDoc} */
    @Override
    public List<Fold> getFolds(SyntaxTextArea textArea) {
        Stack<Object> blocks = new Stack<Object>();
        List<Fold> folds = new ArrayList<Fold>();

        Fold currentFold = null;
        int lineCount = textArea.getLineCount();

        try {
            for (int line = 0; line < lineCount; line++) {
                IToken t = textArea.getTokenListForLine(line);
                while (t != null && t.isPaintable()) {

                    if (t.isLeftCurly()) {
                        if (currentFold == null) {
                            currentFold = new Fold(IFoldType.CODE, textArea,
                                    t.getOffset());
                            folds.add(currentFold);
                        } else {
                            currentFold = currentFold.createChild(
                                    IFoldType.CODE, t.getOffset());
                        }
                        blocks.push(OBJECT_BLOCK);
                    }

                    else if (t.isRightCurly()
                            && popOffTop(blocks, OBJECT_BLOCK)) {
                        if (currentFold != null) {
                            currentFold.setEndOffset(t.getOffset());
                            Fold parentFold = currentFold.getParent();
                            // Don't add fold markers for single-line blocks
                            if (currentFold.isOnSingleLine()) {
                                if (!currentFold.removeFromParent()) {
                                    folds.remove(folds.size() - 1);
                                }
                            }
                            currentFold = parentFold;
                        }
                    } else if (isLeftBracket(t)) {
                        if (currentFold == null) {
                            currentFold = new Fold(IFoldType.CODE, textArea,
                                    t.getOffset());
                            folds.add(currentFold);
                        } else {
                            currentFold = currentFold.createChild(
                                    IFoldType.CODE, t.getOffset());
                        }
                        blocks.push(ARRAY_BLOCK);
                    } else if (isRightBracket(t)
                            && popOffTop(blocks, ARRAY_BLOCK)) {
                        if (currentFold != null) {
                            currentFold.setEndOffset(t.getOffset());
                            Fold parentFold = currentFold.getParent();
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

    /**
     * Returns whether a token is the left bracket token.
     *
     * @param t The token.
     * @return Whether the token is the left bracket token.
     * @see #isRightBracket(IToken)
     */
    private static final boolean isLeftBracket(IToken t) {
        return t.getType() == ITokenTypes.SEPARATOR && t.isSingleChar('[');
    }

    /**
     * Returns whether a token is the right bracket token.
     *
     * @param t The token.
     * @return Whether the token is the right bracket token.
     * @see #isLeftBracket(IToken)
     */
    private static final boolean isRightBracket(IToken t) {
        return t.getType() == ITokenTypes.SEPARATOR && t.isSingleChar(']');
    }

    /**
     * If the specified value is on top of the stack, pop it off and return
     * <code>true</code>. Otherwise, return <code>false</code>.
     *
     * @param stack The stack.
     * @param value The value to check for.
     * @return Whether the value was found on top of the stack.
     */
    private static final boolean popOffTop(Stack<Object> stack, Object value) {
        if (stack.size() > 0 && stack.peek() == value) {
            stack.pop();
            return true;
        }
        return false;
    }
}