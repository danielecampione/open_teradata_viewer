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

import javax.swing.text.Segment;

/**
 * Interface for a class that generates tokens somehow.
 *
 * @author D. Campione
 * 
 */
interface ITokenFactory {

    /** @return A null token. */
    public Token createToken();

    /**
     * Returns a token.
     *
     * @param line The segment from which to get the token's text.
     * @param beg The starting offset of the token's text in the segment.
     * @param end The ending offset of the token's text in the segment.
     * @param startOffset The offset in the document of the token.
     * @param type The type of token.
     * @return The token.
     */
    public Token createToken(final Segment line, final int beg, final int end,
            final int startOffset, final int type);

    /**
     * Returns a token.
     *
     * @param line The char array from which to get the token's text.
     * @param beg The starting offset of the token's text in the char array.
     * @param end The ending offset of the token's text in the char array.
     * @param startOffset The offset in the document of the token.
     * @param type The type of token.
     * @return The token.
     */
    public Token createToken(final char[] line, final int beg, final int end,
            final int startOffset, final int type);

    /** Resets the state of this token maker, if necessary. */
    public void resetAllTokens();
}