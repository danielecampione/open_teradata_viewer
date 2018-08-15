/*
 * Open Teradata Viewer ( editor language support js ecma api client )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class Screen implements IJS5ObjectFunctions {

    /**
     * Object Screen().
     * 
     * @super Object
     * @constructor
     * @since Common Usage, no standard.
     */
    public Screen() {
    };

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf Object
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;

    /**
     * <b>property prototype</b>
     * 
     * @type Location
     * @memberOf Location
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     */
    public Screen prototype;

    /**
     * Property availHeight.
     * 
     * @type Number
     * @memberOf Screen
     */
    public JSNumber availHeight;

    /**
     * Property availWidth.
     * 
     * @type Number
     * @memberOf Screen
     */
    public JSNumber availWidth;

    /**
     * Property colorDepth.
     * 
     * @type Number
     * @memberOf Screen
     */
    public JSNumber colorDepth;

    /**
     * Property height.
     * 
     * @type Number
     * @memberOf Screen
     */
    public JSNumber height;

    /**
     * Property width.
     * 
     * @type Number
     * @memberOf Screen
     */
    public JSNumber width;
}
