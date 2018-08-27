/*
 * Open Teradata Viewer ( editor spell )
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

package net.sourceforge.open_teradata_viewer.editor.spell;

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;

/**
 * Identifies tokens that contain spell-checkable text.
 *
 * @author D. Campione
 * 
 */
public interface ISpellCheckableTokenIdentifier {

    /**
     * Called before each parsing of the document for tokens to spell check.
     *
     * @see #end()
     */
    void begin();

    /**
     * Called when each parsing of the document completes.
     *
     * @see #begin()
     */
    void end();

    /**
     * Returns whether a particular token should be spell-checked.
     *
     * @param t The token.
     * @return Whether that token should be spell checked.
     */
    boolean isSpellCheckable(IToken t);
}