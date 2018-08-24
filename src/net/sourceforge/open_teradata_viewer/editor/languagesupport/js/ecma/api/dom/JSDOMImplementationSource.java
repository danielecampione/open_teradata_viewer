/*
 * Open Teradata Viewer ( editor language support js ecma api dom html )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

import org.w3c.dom.DOMImplementationList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class JSDOMImplementationSource implements
        DOMImplementationList, IJS5ObjectFunctions {

    /**
     * Object DOMImplementationSource().
     * http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html
     * 
     * @constructor
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSDOMImplementationSource() {
    }

    /**
     * <b>property prototype</b>
     * 
     * @type DOMImplementationSource
     * @memberOf DOMImplementationSource
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom.JSDOMImplementationSource
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public DOMImplementationList protype;

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;
}