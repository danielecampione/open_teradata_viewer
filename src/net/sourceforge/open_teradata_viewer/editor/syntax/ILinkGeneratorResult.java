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

import javax.swing.event.HyperlinkEvent;

/**
 * A result object from a {@link ILinkGenerator}. Implementations of this class
 * specify what action to execute when the user clicks on the "link" specified
 * by the <code>ILinkGenerator</code>. Typically, this will do something like
 * select another region of text in the document (the declaration of the
 * variable at the mouse position), or open another file in the parent
 * application, etc..
 *
 * @author D. Campione
 * @see SelectRegionLinkGeneratorResult
 * 
 */
public interface ILinkGeneratorResult {

    /**
     * Executes the action associated with this object. If the result is a URL
     * to open, a standard hyperlink event can be returned. Alternatively,
     * <code>null</code> can be returned and the action performed in this method
     * itself.
     *
     * @return The hyperlink event to broadcast from the text area, or
     *         <code>null</code> if the action's behavior occurs in this method
     *         directly.
     */
    public HyperlinkEvent execute();

    /**
     * Returns the starting offset of the link specified by the parent
     * <code>ILinkGenerator</code>.
     *
     * @return The offset.
     */
    public int getSourceOffset();
}