/*
 * Open Teradata Viewer ( editor language support js ecma api ecma5 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSFunctionFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Function;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJS5FunctionFunctions extends IJS5ObjectFunctions,
        IJSFunctionFunctions {

    /**
     * <b>function bind (thisObject, argArray)</b> - Return a function that
     * invokes this as a method.
     * 
     * @param thisObject The object to which the function should be bound.
     * @param argArray Zero or more argument values that will also be bound.
     * @returns A new function which invokes this function as a method of
     *          <b><i>thisObject</i></b> and passes arguments
     *          <b><i>argArray</i></b>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Function
     * @since   Standard ECMA-262 5th. Edition.
     * @since   Level 2 Document Object Model Core Definition.
     */
    public JS5Function bind(JS5Object thisObject, JS5Array argArray);

}