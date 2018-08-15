/*
 * Open Teradata Viewer ( editor autocomplete )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import javax.swing.event.HyperlinkEvent;

/**
 * A callback for when an external URL is clicked in the description window. If
 * no handler is installed, and if running in Java 6, the system default Web
 * browser is used to open the URL. If not running Java 6, nothing will
 * happen.<p>
 *
 * Alternatively, folks implementing robust code completion support for a
 * language might install an <code>IExternalURLHandler</code> to handle
 * navigating through linked documentation of objects, functions, etc..
 *
 * @author D. Campione
 * @see AutoCompletion#setExternalURLHandler(IExternalURLHandler)
 */
public interface IExternalURLHandler {

    /**
     * Called when an external URL is clicked in the description window.
     *
     * @param e The event containing the hyperlink clicked.
     * @param c The completion currently being displayed.
     * @param callback Allows you to display new content in the description
     *        window.
     */
    public void urlClicked(HyperlinkEvent e, ICompletion c,
            IDescWindowCallback callback);

}