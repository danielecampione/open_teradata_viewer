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

/**
 * Object JSON.
 *
 * @author D. Campione
 * @since Standard ECMA-262 5th. Edition.
 *
 */
public abstract class JS5JSON {

    /**
     * <b>function parse(s, reviver)</b> - Parse a JSON-formatted string.
     *
     * @memberOf Date
     * @param s The string to be parsed.
     * @param reviver An optional argument function that can transform parsed
     *        values.
     * @returns {Object}
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5JSON
     * @since Standard ECMA-262 5th. Edition.
     */
    public static JS5Object parse(JS5String s, JS5Function reviver) {
        return null;
    }

    /**
     * <b>function stringify(o, filter, indent)</b> - Serialize an object, array
     * or primitive value.
     *
     * @memberOf Date
     * @param o The object, array or primitive value to convert to JSON string.
     * @param filter An optional function that can replace values before
     *        stringification.
     * @param indent An optional argument that specifies am indentation string
     *        or number of spaces to use for indentation.
     * @returns A JSON formatted string representing the value <b><i>o</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5JSON
     * @since Standard ECMA-262 5th. Edition.
     */
    public static JS5Object stringify(JS5Object o, JS5Function filter,
            JS5Object indent) {
        return null;
    }
}