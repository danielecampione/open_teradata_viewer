/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.event.MouseEvent;

/**
 * A <tt>IToolTipSupplier</tt> can create tool tip text for an <tt>TextArea</tt>
 * on its behalf. A text area will check its <tt>IToolTipSupplier</tt> for a
 * tool tip before calling the super class's implementation of {@link
 * TextArea#getToolTipText()}. This allows applications to intercept tool tip
 * events and provide the text for a tool tip without subclassing
 * <tt>TextArea</tt>.
 *
 * @author D. Campione
 * 
 */
public interface IToolTipSupplier {

    /**
     * Returns the tool tip text to display for a given mouse event.
     *
     * @param textArea The text area.
     * @param e The mouse event.
     * @return The tool tip, or <code>null</code> if none.
     */
    public String getToolTipText(TextArea textArea, MouseEvent e);

}