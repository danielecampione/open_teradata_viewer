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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSRegExpFunctions;

/**
 * Object RegExp.
 *
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 *
 */
public abstract class JSRegExp implements IJSRegExpFunctions {

    /**
    * Object RegExp(pattern, attributes).
    *
    * @super Object
    * @constructor
    * @memberOf RegExp
    * @param pattern A string that specifies the pattern of the regular
    *        expression.
    * @param attributes An optional string containing and of the "g", "i" an "m"
    *        attributes that specify global, case-insensitive and multiline
    *        matches respectively.
    * @since Standard ECMA-262 3rd. Edition.
    * @since Level 2 Document Object Model Core Definition.
    */
    public JSRegExp(JSString pattern, JSString attributes) {
    };

    /**
    * <b>property prototype</b>
    *
    * @type RegExp
    * @memberOf RegExp
    * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSRegExp
    * @since Standard ECMA-262 3rd. Edition.
    * @since Level 2 Document Object Model Core Definition.
    */
    public JSRegExp prototype;

    /**
    * <b>property constructor</b>
    *
    * @type Function
    * @memberOf RegExp
    * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
    * @since Standard ECMA-262 3rd. Edition.
    * @since Level 2 Document Object Model Core Definition.
    */
    protected JSFunction constructor;

    /**
     * <b>property source</b>
     *
     * @type String
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSString source;

    /**
     * <b>property global</b>
     *
     * @type Boolean
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSBoolean global;

    /**
     * <b>property ignoreCase</b>
     *
     * @type Boolean
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSBoolean ignoreCase;

    /**
     * <b>property multiline</b>
     *
     * @type Boolean
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSBoolean multiline;

    /**
     * <b>property lastIndex</b>
     *
     * @type Number
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSNumber lastIndex;
}