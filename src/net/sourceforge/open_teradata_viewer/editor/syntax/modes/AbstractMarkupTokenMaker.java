/*
 * Open Teradata Viewer ( editor syntax modes )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.modes;

import net.sourceforge.open_teradata_viewer.editor.syntax.AbstractJFlexTokenMaker;

/**
 * Base class for token makers for markup languages.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractMarkupTokenMaker extends AbstractJFlexTokenMaker {

    /**
     * Returns whether markup close tags should be completed.
     *
     * @return Whether closing markup tags are to be completed.
     */
    public abstract boolean getCompleteCloseTags();

    /**
     * Returns the text to place at the beginning and end of a line to "comment"
     * it in a this programming language.
     *
     * @return The start and end strings to add to a line to "comment" it
     *         out.
     */
    @Override
    public String[] getLineCommentStartAndEnd() {
        return new String[]{"<!--", "-->"};
    }

    /**
     * Overridden to return <code>true</code>.
     *
     * @return <code>true</code> always.
     */
    @Override
    public final boolean isMarkupLanguage() {
        return true;
    }
}