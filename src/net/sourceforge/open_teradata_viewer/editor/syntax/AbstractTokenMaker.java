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

/**
 * An abstract implementation of the {@link
 * net.sourceforge.open_teradata_viewer.editor.syntax.ITokenMaker} interface. It
 * should be overridden for every language for which you want to provide syntax
 * highlighting.<p>
 *
 * @author D. Campione
 * @see IToken
 * 
 */
public abstract class AbstractTokenMaker extends TokenMakerBase {

    /**
     * Hash table of words to highlight and what token type they are. The keys
     * are the words to highlight, and their values are the token types, for
     * example, <code>IToken.RESERVED_WORD</code> or <code>IToken.FUNCTION</code>.
     */
    protected TokenMap wordsToHighlight;

    /** Ctor. */
    public AbstractTokenMaker() {
        wordsToHighlight = getWordsToHighlight();
    }

    /**
     * Returns the words to highlight for this programming language.
     *
     * @return A <code>TokenMap</code> containing the words to highlight for
     *         this programming language.
     */
    public abstract TokenMap getWordsToHighlight();

    /**
     * Removes the token last added from the linked list of tokens. The
     * programmer should never have to call this directly; it can be called by
     * subclasses of <code>ITokenMaker</code> if necessary.
     */
    public void removeLastToken() {
        if (previousToken == null) {
            firstToken = currentToken = null;
        } else {
            currentToken = previousToken;
            currentToken.setNextToken(null);
        }
    }
}