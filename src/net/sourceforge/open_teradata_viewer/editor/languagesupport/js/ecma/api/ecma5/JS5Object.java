/*
 * Open Teradata Viewer ( editor language support js ecma api ecma5 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

/**
 * Base JavaScript Object.
 *
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 *
 */
public abstract class JS5Object extends JSObject implements IJS5ObjectFunctions {

    /**
     * Object Object().
     *
     * <p>Creates a new object instance.</p>
     *
     * @constructor
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JS5Object() {
    }

    /**
     * Object Object(value).
     *
     * @constructor
     * @param value Optional argument specifies a primitive JavaScript value - a
     *        number, boolean etc...
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JS5Object(JSObject value) {
    }

    /**
     * <b>function create(proto, descriptors)</b> - Create an object with
     * specified prototype and properties.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * var p = Object.create({z:0}), {
     *   x: { value: 1, writable, false, enumerable:true. configurable:true},
     *   y: { value: 2, writable, false, enumerable:true. configurable:true},
     * });
     * </pre>
     *
     * @param proto The prototype of the newly created object or null.
     * @param descriptors An optional object that maps property names to
     *        property descriptors.
     * @returns A newly created object that inherits from <b><i>proto</b></i>
     *          and has properties described by <b><i>descriptors</i></b>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @see #defineProperties(JS5Object, JS5Object) defineProperties()
     * @see #getOwnPropertyDescriptor(JS5Object, JS5String) getOwnPropertyDescriptor()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object create(JS5Object proto, JS5Object descriptors) {
        return null;
    }

    /**
     * <b>function defineProperties(o, descriptors)</b> - Create or configure
     * multiple object properties.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * var p = Object.defineProperties({}), {
     *   x: { value: 1, writable, false, enumerable:true. configurable:true},
     *   y: { value: 2, writable, false, enumerable:true. configurable:true},
     * });
     * </pre>
     *
     * @param o The object on which properties are to be created or configured.
     * @param descriptors An object that maps property names to property
     *        descriptors.
     * @returns The object <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #create(JS5Object, JS5Object) create()
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @see #getOwnPropertyDescriptor(JS5Object, JS5String) getOwnPropertyDescriptor()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object defineProperties(JS5Object o, JS5Object descriptors) {
        return null;
    }

    /**
     * <b>function defineProperty(o, name, desc)</b> - Create or configure an
     * object property.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * function constant(o, n, v) { // Define a constant with value v
     *   Object.defineProperty (o, n, { value: v, writable, false, enumerable:true. configurable:true});
     * }
     * </pre>
     *
     * @param o The object on which a property is to be created or configured.
     * @param name The name of the property created or configured.
     * @param desc A property descriptor object that describes the new property.
     * @returns The object <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object defineProperty(JS5Object o, JS5String name,
            JS5Object desc) {
        return null;
    }

    /**
     * <b>function freeze(o)</b> - Make an object immutable.
     *
     * @param o The object to be frozen.
     * @returns The now-frozen argument object <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @see #isFrozen(JS5Object) isFrozen()
     * @see #preventExtensions(JS5Object) preventExtensions()
     * @see #seal(JS5Object) seal()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object freeze(JS5Object o) {
        return null;
    }

    /**
     * <b>function getOwnPropertyDescriptor(o, name)</b> - Query property
     * attributes.
     *
     * @param o The object that is to have its property attributes queried.
     * @param name The name of the property to query.
     * @returns A property descriptor object for the specified property or
     *          <b><i>undefined</b></i> if no such property exitsts.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object getOwnPropertyDescriptor(JS5Object o, JS5String name) {
        return null;
    }

    /**
     * <b>function getOwnPropertyNames(o)</b> - Return the names of
     * non-inherited properties.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * Object.getOwnPropertyNames([]); // Returns [length]: "length" is non enumerable
     * </pre>
     *
     * @param o An object
     * @returns An array that contains the names of all non-inherited properties
     *          of <b><i>o</b></i>, including non-enumerable properties.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #keys(JS5Object) keys()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Array getOwnPropertyNames(JS5Object o) {
        return null;
    }

    /**
     * <b>function getPrototypeOf(o)</b> - Return the prototype of an object.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * var p = {}; // Create object
     * Object.getPrototypeOf(p); // => Object.prototype
     * var o = Object.create(p); // An object inherited from p
     * Object.getPrototypeOf(o); // => p
     * </pre>
     *
     * @param o An object.
     * @returns The prototype of object <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #create(JS5Object, JS5Object) create()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object getPrototypeOf(JS5Object o) {
        return null;
    }

    /**
     * <b>function isExtensible(o)</b> - Can new properties be added to an
     * object?<p>
     *
     * <strong>Example</strong>
     * <pre>
     * var o = {}; // Create object
     * Object.isExtensible(o); // => true
     * Object.preventExtensions(o); // Make it non-extensible
     * Object.isExtensible(o); // => false
     * </pre>
     *
     * @param o The object to be checked for extensibility
     * @returns <b><i>true</b></i> if the object can be extended with new
     *          properties, otherwise <b><i>false</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JSBoolean isExtensible(JS5Object o) {
        return null;
    }

    /**
     * <b>function isFrozen(o)</b> - Is an object immutable?
     *
     * @param o The object to be checked.
     * @returns <b><i>true</b></i> if the object is frozen or immutable,
     *          otherwise <b><i>false</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @see #freeze(JS5Object) freeze()
     * @see #isExtensible(JS5Object) isExtensible()
     * @see #isSealed(JS5Object) isSealed()
     * @see #preventExtensions(JS5Object) preventExtensions()
     * @see #seal(JS5Object) seal()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JSBoolean isFrozen(JS5Object o) {
        return null;
    }

    /**
     * <b>function isSealed(o)</b> - Can properties be added or deleted from an
     * object?
     *
     * @param o The object to be checked.
     * @returns <b><i>true</b></i> if the object is sealed, otherwise
     *          <b><i>false</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @see #freeze(JS5Object) freeze()
     * @see #isExtensible(JS5Object) isExtensible()
     * @see #isFrozen(JS5Object) isFrozen()
     * @see #preventExtensions(JS5Object) preventExtensions()
     * @see #seal(JS5Object) seal()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JSBoolean isSealed(JS5Object o) {
        return null;
    }

    /**
     * <b>function keys(o)</b> - Return enumerable property names.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * Object.keys({x:1, y:2}); // => ["x", "y"]
     * </pre>
     *
     * @param o an object
     * @returns An array that contains the names of all enumerable own
     *          (non-inherited) properties of <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #getOwnPropertyNames(JS5Object) getOwnPropertyNames()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Array keys(JS5Object o) {
        return null;
    }

    /**
     * <b>function preventExtensions(o)</b> - Don't allow new properties on an
     * object.
     *
     * @param o The object is to have its extensibility attribute set.
     * @returns The argument <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #freeze(JS5Object) freeze()
     * @see #isExtensible(JS5Object) isExtensible()
     * @see #seal(JS5Object) seal()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object preventExtensions(JS5Object o) {
        return null;
    }

    /**
     * <b>function seal(o)</b> - Prevent the addition or deletion of properties.
     *
     * @param o The object to be sealed.
     * @returns The now-sealed argument of <b><i>o</b></i>.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object
     * @see #defineProperty(JS5Object, JS5String, JS5Object) defineProperty()
     * @see #freeze(JS5Object) freeze()
     * @see #isSealed(JS5Object) isSealed()
     * @see #preventExtensions(JS5Object) preventExtensions()
     * @since   Standard ECMA-262 5th. Edition.
     */
    public static JS5Object seal(JS5Object o) {
        return null;
    }
}