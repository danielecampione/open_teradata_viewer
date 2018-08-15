/*
 * Open Teradata Viewer ( editor language support js ecma api ecma5 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ArrayFunctions;

/**
 * Object Array.
 * 
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 * 
 */
public abstract class JS5Array extends JSArray implements IJS5ArrayFunctions {

    /**
     * Object Array().
     * 
     * @constructor
     * @extends Object
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JS5Array() {
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
    public JS5Array(JSNumber size) {
    }

    /**
     * Object Array(element0, ... elementn).
     * 
     * @constructor
     * @extends Object
     * @param elements An argument list of two or more values. The
     *        <b>length</b> field set to the number of arguments.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JS5Array(JSObject element0, JSObject elementn) {
    }

    /**
     * <b>function isArray(o)</b> - Test whether argument is an array.
     * 
     * @param o Object to test.
     * @returns <b><i>true</b></i> if object is of type array, otherwise
     *          <b><i>false</b></i>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @since Standard ECMA-262 5th. Edition.
     */
    public static JSBoolean isArray(JS5Object o) {
        return null;
    }
}