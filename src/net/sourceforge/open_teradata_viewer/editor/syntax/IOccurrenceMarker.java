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

import net.sourceforge.open_teradata_viewer.editor.SmartHighlightPainter;

/**
 * IOccurrenceMarker marks occurrences of the current token.
 * 
 * An <code>IOccurrenceMarker</code> is called when the caret stops moving after
 * a short period. If the current {@link ITokenMaker} returns an instance of
 * this class, it is told to mark all occurrences of the identifier at the caret
 * position.
 *
 * @author D. Campione
 * 
 */
public interface IOccurrenceMarker {

    /**
     * Returns the token to mark occurrences, of, provided it matches the
     * criteria put forth by {@link #isValidType(SyntaxTextArea, IToken)}.
     * For most languages, this method should return the token at the caret
     * position.
     *
     * @param textArea The text area.
     * @return The token to (possibly) mark occurrences of, or <code>null</code>
     *         if none.
     */
    public IToken getTokenToMark(SyntaxTextArea textArea);

    /**
     * Returns whether the specified token is a type that we can do a "mark
     * occurrences" of. Typically, this will delegate to {@link
     * SyntaxTextArea#getMarkOccurrencesOfTokenType(int)}.
     *
     * @param textArea The text area.
     * @param t The token.
     * @return Whether we should mark all occurrences of this token.
     */
    public boolean isValidType(SyntaxTextArea textArea, IToken t);

    /**
     * Called when occurrences of a token should be marked.
     *
     * @param doc The document.
     * @param t The document whose relevant occurrences should be marked.
     * @param h The highlighter to add the highlights to.
     * @param p The painter for the highlights.
     */
    public void markOccurrences(SyntaxDocument doc, IToken t,
            SyntaxTextAreaHighlighter h, SmartHighlightPainter p);
}