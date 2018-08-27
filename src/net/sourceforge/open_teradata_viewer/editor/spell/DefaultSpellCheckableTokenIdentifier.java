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
 * The spell-checkable token identifier used by {@link SpellingParser} if none
 * is explicitly identified. It causes all comment tokens to be spell checked.
 *
 * @author D. Campione
 * 
 */
public class DefaultSpellCheckableTokenIdentifier implements
        ISpellCheckableTokenIdentifier {

    /**
     * The default implementation of this method does nothing; this token
     * identifier does not have state.
     */
    @Override
    public void begin() {
    }

    /**
     * The default implementation of this method does nothing; this token
     * identifier does not have state.
     */
    @Override
    public void end() {
    }

    /**
     * Returns <code>true</code> if the token is a comment.
     *
     * @return <code>true</code> only if the token is a comment.
     */
    @Override
    public boolean isSpellCheckable(IToken t) {
        return t.isComment();
    }
}