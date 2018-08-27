/*
 * Open Teradata Viewer ( editor language support js ecma api ecma5 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSDateFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5String;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJS5DateFunctions extends IJS5ObjectFunctions,
        IJSDateFunctions {

    /**
     * <b>function toISOString()</b> - Converts a Date to ISO8601-formatted
     * string.
     * 
     * @memberOf Date
     * @returns A string representation of <b><i>date</i></b>, formatted
     *          according to ISO-8601 - yyyy-mm-ddThh:mm:ss.sssZ.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Date
     * @since Standard ECMA-262 5th. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JS5String toISOString();

    /**
     * <b>function toJSON(key)</b> - JSON-serialize a Date object.
     * 
     * @memberOf Date
     * @param key JSON.stringify() passes this argument.
     * @returns A string representation of the date, obtained by calling the
     *          toISOString() method. 
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Date
     * @see #toISOString() toISOString()
     * @since Standard ECMA-262 5th. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JS5String toJSON(JS5String key);
}