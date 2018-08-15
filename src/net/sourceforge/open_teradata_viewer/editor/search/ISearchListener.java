/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

import java.util.EventListener;

import net.sourceforge.open_teradata_viewer.editor.SearchEngine;

/**
 * Listens for events fired from a Find or Replace dialog/tool bar. 
 * Applications can implement this class to listen for the user searching for
 * text, and actually perform the operation via {@link SearchEngine} in
 * response.
 *
 * @author D. Campione
 * 
 */
public interface ISearchListener extends EventListener {

    /**
     * Callback called whenever a search event occurs.
     *
     * @param e The event.
     */
    void searchEvent(SearchEvent e);

}