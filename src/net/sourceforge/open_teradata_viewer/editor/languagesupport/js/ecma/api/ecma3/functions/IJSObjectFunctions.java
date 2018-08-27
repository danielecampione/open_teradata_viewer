/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJSObjectFunctions {

    /**
     * <b>function toString()</b> - Define an objects string representation.
     * 
     * @memberOf Object
     * @returns A string representing the object.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @see #toLocaleString() toLocalString()
     * @see #valueOf() valueOf()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    @Override
    public String toString();

    /**
     * <b>function toLocaleString()</b> - Return an object localized string
     * representation.
     * 
     * @memberOf Object
     * @returns A string representing the object.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @see #toString() toString()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toLocaleString();

    /**
     * <b>function valueOf()</b> - The primitive value of a specified object.
     * 
     * @memberOf Object
     * @returns The primitive value associated with the <b><i>object</i></b>, if
     *          any. 
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @see #toString() toString()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSObject valueOf();

    /**
     * <b>function hasOwnProperty(name)</b> - Check whether a property is
     * inherited.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var o = new Object();
     * o.x = 3.14;
     * o.hasOwnProperty("x"); // Return true; o has property x.
     * o.hasOwnProperty("y"); // Return false; o does not have property y.
     * o.hasOwnProperty("toString"); // Return false; o inherits toString.
     * </pre> 
     * 
     * @memberOf Object
     * @param name A string that contains the name of a property of
     *        <b><i>object</i></b>.
     * @returns <b><i>true</i></b> if <b><i>object</i></b> has a noninherited
     *          property with the name specified by <b><i>name</i></b>. 
     *          <b><i>false</i></b> if <b><i>object</i></b> does not contain the
     *          property with the specified name or if it inherits the property
     *          from its prototype object. 
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @see #propertyIsEnumerable(JSObject) propertyIsEnumerable()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSBoolean hasOwnProperty();

    /**
     * <b>function isPrototypeOf(o)</b> - Is an object the prototype of
     * another?<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var o = new Object();
     * Object.prototype.isPrototypeOf(o); // true: o is an object.
     * Function.prototype.isPrototypeOf(o.toString(); // Return true: toString is a function.
     * Array.prototype.isPrototypeOf([1,2,3]; // Return true: [1,2,3] is an Array.
     * </pre> 
     * 
     * @memberOf Object
     * @param o Any object.
     * @returns <b><i>true</i></b> if <b><i>object</i></b> is prototype of o.
     *          <b><i>false</i></b> is not an object or if <b><i>object</i></b>
     *          is not prototype of o.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSBoolean isPrototypeOf(JSObject o);

    /**
     * <b>function propertyIsEnumerable(name)</b> - Will property be seen by
     * for/in loop?<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var o = new Object();
     * o.x = 3.14;
     * o.propertyIsEnumerable("x"); // Return true; property x is local and enumerable.
     * o.propertyIsEnumerable("y"); // Return false; o does not have property y.
     * o.propertyIsEnumerable("toString"); // Return false; o inherits toString.
     * </pre> 
     * 
     * @memberOf Object
     * @param name A string that contains the name of a property of
     *        <b><i>object</i></b>.
     * @returns <b><i>true</i></b> if <b><i>object</i></b> has a noninherited
     *          property with the name specified by <b><i>name</i></b> and if
     *          that name is enumerable.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @see #hasOwnProperty() hasOwnProperty()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSBoolean propertyIsEnumerable(JSObject name);
}