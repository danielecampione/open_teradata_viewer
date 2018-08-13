/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.TextArea;

/**
 * Base class for JFlex-based token makers using C-style syntax. This class
 * knows how to auto-indent after opening braces and parens.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractJFlexCTokenMaker extends AbstractJFlexTokenMaker {

    protected static final Action INSERT_BREAK_ACTION = new InsertBreakAction();

    /**
     * Returns <code>true</code> always as C-style languages use curly braces to
     * denote code blocks.
     *
     * @return <code>true</code> always.
     */
    @Override
    public boolean getCurlyBracesDenoteCodeBlocks() {
        return true;
    }

    /**
     * Returns an action to handle "insert break" key presses (i.e. Enter). An
     * action is returned that handles newlines differently in multi-line
     * comments.
     *
     * @return The action.
     */
    @Override
    public Action getInsertBreakAction() {
        return INSERT_BREAK_ACTION;
    }

    /** {@inheritDoc} */
    @Override
    public boolean getMarkOccurrencesOfTokenType(int type) {
        return type == IToken.IDENTIFIER || type == IToken.FUNCTION;
    }

    /** {@inheritDoc} */
    @Override
    public boolean getShouldIndentNextLineAfter(IToken t) {
        if (t != null && t.length() == 1) {
            char ch = t.charAt(0);
            return ch == '{' || ch == '(';
        }
        return false;
    }

    /**
     * Action that knows how to special-case inserting a newline in a multi-line
     * comment for languages like C and Java.
     * 
     * @author D. Campione
     * 
     */
    private static class InsertBreakAction extends
            SyntaxTextAreaEditorKit.InsertBreakAction {

        private static final long serialVersionUID = 7740382686532033128L;

        private static final Pattern p = Pattern
                .compile("([ \\t]*)(/?[\\*]+)([ \\t]*)");

        @Override
        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            if (!textArea.isEditable() || !textArea.isEnabled()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            SyntaxTextArea sta = (SyntaxTextArea) getTextComponent(e);
            SyntaxDocument doc = (SyntaxDocument) sta.getDocument();

            int line = textArea.getCaretLineNumber();
            int type = doc.getLastTokenTypeOnLine(line);
            if (type < 0) {
                type = doc.getClosestStandardTokenTypeForInternalType(type);
            }

            // Only in MLC's should we try this
            if (type == IToken.COMMENT_DOCUMENTATION
                    || type == IToken.COMMENT_MULTILINE) {
                insertBreakInMLC(e, sta, line);
            } else {
                handleInsertBreak(sta, true);
            }
        }

        /**
         * Returns whether the MLC token containing <code>offs</code> appears to
         * have a "nested" comment (i.e., contains "<code>/*</code>" somewhere
         * inside of it). This implies that it is likely a "new" MLC and needs
         * to be closed. While not foolproof, this is usually good enough of a
         * sign.
         *
         * @param textArea.
         * @param line.
         * @param offs.
         * @return Whether a comment appears to be nested inside this one.
         */
        private boolean appearsNested(SyntaxTextArea textArea, int line,
                int offs) {
            final int firstLine = line; // Remember the line we start at

            while (line < textArea.getLineCount()) {
                IToken t = textArea.getTokenListForLine(line);
                int i = 0;
                // If examining the first line, start at offs
                if (line++ == firstLine) {
                    t = SyntaxUtilities.getTokenAtOffset(t, offs);
                    if (t == null) { // offs was at end of the line
                        continue;
                    }
                    i = t.documentToToken(offs);
                } else {
                    i = t.getTextOffset();
                }
                int textOffset = t.getTextOffset();
                while (i < textOffset + t.length() - 1) {
                    if (t.charAt(i - textOffset) == '/'
                            && t.charAt(i - textOffset + 1) == '*') {
                        return true;
                    }
                    i++;
                }
                // If tokens come after this one on this line, our MLC ended
                if (t.getNextToken() != null) {
                    return false;
                }
            }

            return true; // No match - MLC goes to the end of the file
        }

        private void insertBreakInMLC(ActionEvent e, SyntaxTextArea textArea,
                int line) {
            Matcher m = null;
            int start = -1;
            int end = -1;
            try {
                start = textArea.getLineStartOffset(line);
                end = textArea.getLineEndOffset(line);
                String text = textArea.getText(start, end - start);
                m = p.matcher(text);
            } catch (BadLocationException ble) { // Never happens
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                ExceptionDialog.notifyException(ble);
                return;
            }

            if (m.lookingAt()) {
                String leadingWS = m.group(1);
                String mlcMarker = m.group(2);

                // If the caret is "inside" any leading whitespace or MLC
                // marker, move it to the end of the line
                int dot = textArea.getCaretPosition();
                if (dot >= start
                        && dot < start + leadingWS.length()
                                + mlcMarker.length()) {
                    // If we're in the whitespace before the very start of the
                    // MLC though, just insert a normal newline
                    if (mlcMarker.charAt(0) == '/') {
                        handleInsertBreak(textArea, true);
                        return;
                    }
                    textArea.setCaretPosition(end - 1);
                }

                boolean firstMlcLine = mlcMarker.charAt(0) == '/';
                boolean nested = appearsNested(textArea, line, start
                        + leadingWS.length() + 2);
                String header = leadingWS + (firstMlcLine ? " * " : "*")
                        + m.group(3);
                textArea.replaceSelection("\n" + header);
                if (nested) {
                    dot = textArea.getCaretPosition(); // Has changed
                    textArea.insert("\n" + leadingWS + " */", dot);
                    textArea.setCaretPosition(dot);
                }

            } else {
                handleInsertBreak(textArea, true);
            }
        }
    }
}