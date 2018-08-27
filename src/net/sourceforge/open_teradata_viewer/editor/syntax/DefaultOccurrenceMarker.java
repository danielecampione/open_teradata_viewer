/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.SmartHighlightPainter;

/**
 * DefaultOccurrenceMarker marks occurrences of the current token for most
 * languages.
 * 
 * The default implementation of {@link IOccurrenceMarker}. It goes through the
 * document and marks all instances of the specified token.
 *
 * @author D. Campione
 * 
 */
class DefaultOccurrenceMarker implements IOccurrenceMarker {

    /** {@inheritDoc} */
    public IToken getTokenToMark(SyntaxTextArea textArea) {
        // Get the token at the caret position
        int line = textArea.getCaretLineNumber();
        IToken tokenList = textArea.getTokenListForLine(line);
        Caret c = textArea.getCaret();
        int dot = c.getDot();

        IToken t = SyntaxUtilities.getTokenAtOffset(tokenList, dot);
        if (t == null /* EOL */|| !isValidType(textArea, t)
                || SyntaxUtilities.isNonWordChar(t)) {
            // Try to the "left" of the caret
            dot--;
            try {
                if (dot >= textArea.getLineStartOffset(line)) {
                    t = SyntaxUtilities.getTokenAtOffset(tokenList, dot);
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble);
            }
        }

        return t;
    }

    /** {@inheritDoc} */
    public boolean isValidType(SyntaxTextArea textArea, IToken t) {
        return textArea.getMarkOccurrencesOfTokenType(t.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void markOccurrences(SyntaxDocument doc, IToken t,
            SyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
        markOccurrencesOfToken(doc, t, h, p);
    }

    /**
     * Highlights all instances of tokens identical to <code>t</code> in the
     * specified document.
     *
     * @param doc The document.
     * @param t The document whose relevant occurrences should be marked.
     * @param h The highlighter to add the highlights to.
     * @param p The painter for the highlights.
     */
    public static final void markOccurrencesOfToken(SyntaxDocument doc,
            IToken t, SyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
        char[] lexeme = t.getLexeme().toCharArray();
        int type = t.getType();
        int lineCount = doc.getDefaultRootElement().getElementCount();

        for (int i = 0; i < lineCount; i++) {
            IToken temp = doc.getTokenListForLine(i);
            while (temp != null && temp.isPaintable()) {
                if (temp.is(type, lexeme)) {
                    try {
                        int end = temp.getEndOffset();
                        h.addMarkedOccurrenceHighlight(temp.getOffset(), end, p);
                    } catch (BadLocationException ble) {
                        ExceptionDialog.hideException(ble); // Never happens
                    }
                }
                temp = temp.getNextToken();
            }
        }
    }
}