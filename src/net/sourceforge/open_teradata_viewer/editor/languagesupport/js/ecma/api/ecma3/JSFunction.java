/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSFunctionFunctions;

/**
 * Object Function.
 * 
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 * 
 */
public abstract class JSFunction implements IJSFunctionFunctions {

    /**
     * Object Function(argument_names..., body).
     * 
     * @constructor
     * @extends Object
     * @param argument_names Any number of string arguments, each naming one or
     *        more arguments of the Function object to be created.
     * @param body A string that represents the body of the function. It may
     *        contain an number of JavaScript statements, separated by
     *        semicolons.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSFunction(JSString argument_names, JSString body) {
    }

    /**
     * <b>property length</b>
     * 
     * @type  Number
     * @see   net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.    
     */
    protected JSNumber length;

    /**
     * <b>property prototype</b>
     * 
     * @type Function
     * @memberOf Function
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSFunction prototype;

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf Function
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;
}