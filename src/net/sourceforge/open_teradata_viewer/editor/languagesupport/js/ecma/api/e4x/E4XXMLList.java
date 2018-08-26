/*
 * Open Teradata Viewer ( editor language support js ecma api e4x )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLListFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;

/**
 * Object XMLList.
 *
 * @author D. Campione
 * @since Standard ECMA-357 2nd. Edition.
 *
 */
public abstract class E4XXMLList implements IE4XXMLListFunctions {

    /**
     * Object E4XXMLList(xml).
     *
     * @constructor
     * @param xml The XML definition.
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XXMLList(JSObject xml) {
    }

    /**
     * <b>property prototype</b>
     *
     * @type XMLList
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XXMLList protype;

    /**
     * <b>property constructor</b>
     *
     * @type Function
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSFunction constructor;
}