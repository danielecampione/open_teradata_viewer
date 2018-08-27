/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSArrayFunctions;

/**
 * Object Array.
 * 
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 * 
 */
public abstract class JSArray implements IJSArrayFunctions {

    /**
     * Object Array().
     * 
     * @constructor
     * @extends Object
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray() {
    }

    /**
     * Object Array(size).
     * 
     * @constructor
     * @extends Object
     * @param size The desired number of elements in the array. The returned
     *        value has its <b>length</b> field set to <b><i>size</b></i>.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray(JSNumber size) {
    }

    /**
     * Object Array(element0, ... elementn).
     * 
     * @constructor
     * @extends Object
     * @param elements  An argument list of two or more values. The
     *        <b>length</b> field set to the number of arguments.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray(JSObject element0, JSObject elementn) {
    }

    /**
     * <b>property length</b>
     * 
     * @type Number
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber length;

    /**
    * <b>property prototype</b>
    * 
    * @type Array
    * @memberOf Array
    * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
    * @since Standard ECMA-262 3rd. Edition.
    * @since Level 2 Document Object Model Core Definition.
    */
    public JSArray prototype;

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;
}