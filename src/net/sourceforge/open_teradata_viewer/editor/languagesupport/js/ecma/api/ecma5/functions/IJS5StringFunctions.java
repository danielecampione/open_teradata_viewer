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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5String;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJS5StringFunctions extends IJS5ObjectFunctions {

    /**
     * <b>function trim ()</b> - String leading and trailing whitespace.
     * 
     * @returns A copy of <b><i>string</i></b>, with all leading and trailing
     *          whitespace removed.
     * @see  net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5String
     * @since   Standard ECMA-262 5th. Edition.
     */
    public JS5String trim();

}