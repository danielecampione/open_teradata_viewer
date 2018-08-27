/*
 * Open Teradata Viewer ( editor language support js ecma api client funtions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IHistoryFunctions extends IJS5ObjectFunctions {

    /**
     * function back - Loads the previous URL in the history list.
     * 
     * @memberOf History
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.History
     */
    public void back();

    /**
     * function forward - Loads the next URL in the history list.
     * 
     * @memberOf History
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.History
     */
    public void forward();

    /**
     * function go - Loads a specific URL from the history list. 
     * 
     * @memberOf History
     * @param arg Goes to the URL within the specific position (-1 goes back one
     *        page, 1 goes forward one page).
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.History
     */
    public void go(JSNumber arg);

    /**
     * function go - Loads a specific URL from the history list. 
     * 
     * @memberOf History
     * @param arg The string must be a partial or full URL and the function will
     *        go to the first URL that matches the string.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.History
     */
    public void go(JSString arg);
}