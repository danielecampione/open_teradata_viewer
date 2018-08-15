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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSStringFunctions;

/**
 * Object String.
 * 
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 * 
 */
public abstract class JSString implements IJSStringFunctions {

    /**
     * Object String(s).
     * 
     * @constructor
     * @extends Object
     * @param s The value to be stored in a String or converted to a primitive
     *        type.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString(JSString s) {
    }

    /**
     * <b>property length</b>
     * 
     * @type Number
     * @memberOf String
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSNumber length;

    /**
     * <b>property prototype</b>
     * 
     * @type String
     * @memberOf String
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString prototype;

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf String
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;

    /**
     * <b>function fromCharCode(charCode1, ...)</b> - Create a string from
     * character encodings.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var s = String.fromCharCode(104,101,108,108,111); //returns the string hello
     * </pre>
     * 
     * @memberOf String
     * @param charCode Zero or more integers that specify Unicode encodings of
     *        the characters in the string to be created.
     * @returns A new string containing characters with the specified encoding.
     * @static
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public static JSString fromCharCode(JSNumber charCode) {
        return null;
    }
}