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

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

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
    @Override
    public void markOccurrences(SyntaxDocument doc, IToken t,
            SyntaxTextAreaHighlighter h, MarkOccurrencesHighlightPainter p) {
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