/*
 * Open Teradata Viewer ( editor language support js ecma api client funtions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

/**
 *
 *
 * @author D. Campione
 *
 */
public interface ILocationFunctions extends IJS5ObjectFunctions {

    /**
     * function assign(newURL) - Method loads a new document.
     *
     * @param newURL
     * @memberOf Location
     */
    public void assign(JSString newURL);

    /**
     * function reload(optionalArg) - Reload the current document.
     *
     * @param optionalArg - Default <i><b>false</b></i> which reloads the page
     *        from the cache. Set this paramter to true if you want to force the
     *        browser to get the page from the Server.
     * @memberOf Location
     */
    public void reload(JSBoolean optionalArg);

    /**
     * function replace(newURL) - Method replaces the current document with a
     * new one.
     *
     * @param newURL
     * @memberOf Location
     */
    public void replace(JSString arg);
}