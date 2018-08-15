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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSObjectFunctions;

/**
 * Object Boolean.
 * 
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 * 
 */
public abstract class JSBoolean implements IJSObjectFunctions {

    /**
     * Object Boolean(value).
     * 
     * @constructor
     * @extends Object
     * @param value The value to be held by the Boolean object or be converted
     *        to a boolean value.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSBoolean(JSObject value) {
    }

    /**
     * <b>property prototype</b>
     * 
     * @type Boolean
     * @memberOf Boolean
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSBoolean prototype;

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf Boolean
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;
}