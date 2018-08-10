/*
 * Open Teradata Viewer ( help )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.help;

import java.util.EventListener;

/**
 * This interface defines a listener to listen to a HTML Viewer Panel.
 *
 * @author D. Campione
 * 
 */
public interface IHtmlViewerPanelListener extends EventListener {

    /**
     * URL has changed.
     *
     * @param   evt event object.
     */
    void currentURLHasChanged(HtmlViewerPanelListenerEvent evt);

    /**
     * Home URL has changed.
     *
     * @param   evt event object.
     */
    void homeURLHasChanged(HtmlViewerPanelListenerEvent evt);
}