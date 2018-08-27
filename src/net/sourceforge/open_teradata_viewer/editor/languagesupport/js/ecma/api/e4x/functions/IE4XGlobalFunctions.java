/*
 * Open Teradata Viewer ( editor language support js ecma api e4x functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IE4XGlobalFunctions extends IJS5ObjectFunctions {

    /**
     * <b>function isXMLName(name)</b> - Determines whether name is a valid XML
     * name.
     * 
     * @param name The name to be tested. 
     * @returns Examines the given value and determines whether it is a valid
     *          XML <b></i>name</i></b> that can be used as an XML element or
     *          attribute name. If so, it returns <b>true</b>, otherwise it
     *          returns <b>false</b>.
     * @memberOf Global
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XGlobal
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean isXMLName(JSString name);

}