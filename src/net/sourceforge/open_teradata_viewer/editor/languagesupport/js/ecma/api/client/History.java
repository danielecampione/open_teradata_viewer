/*
 * Open Teradata Viewer ( editor language support js ecma api client )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.IHistoryFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class History implements IHistoryFunctions {

    /**
      * Object History().
      * 
      * @super Object
      * @constructor
      * @since Common Usage, no standard.
     */
    public History() {
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
     * @type History
     * @memberOf History
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public History prototype;

    /**
     * Property length.
     * 
     * @type Number
     * @memberOf History
     */
    public JSNumber length;
}