/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;

/**
 *
 *
 * @author D. Campione
 *
 */
public interface IJSFunctionFunctions extends IJSObjectFunctions {

    /**
     * <b>function apply (thisObject, argArray)</b> - Invoke a function as a
     * method of an object.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * // Apply the default  Object.toString() method to an object that
     * // overrides it with its own version of the method
     * Object.prototype.toString().apply(o);
     * </pre>
     *
     * @param thisObject The object to which the <b><i>function</i></b> is
     *        applied.
     * @param argArray An array of arguments to be passed to
     *        <b><i>function</i></b>.
     * @returns Whatever value is returned by <b><i>function</i></b>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSObject apply(JSObject thisObject, JSArray argArray);

    /**
     * <b>function call (thisObject, args)</b> - Invoke a function as a method
     * of an object.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * // Call the default  Object.toString() method to an object that
     * // overrides it with its own version of the method
     * Object.prototype.toString().call(o);
     * </pre>
     *
     * @param thisObject The object to which the <b><i>function</i></b> is
     *        applied.
     * @param args An array of arguments to be passed to <b><i>function</i></b>.
     * @returns Whatever value is returned by <b><i>function</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSObject call(JSObject thisObject, JSObject args);
}