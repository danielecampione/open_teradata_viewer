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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface INavigatorFunctions extends IJS5ObjectFunctions {

    /**
     * function javaEnabled() - Specifies whether or not the browser has Java
     * enabled.
     * 
     * @returns true if Java is enabled.
     * @memberOf Navigator
     */
    public JSBoolean javaEnabled();

}