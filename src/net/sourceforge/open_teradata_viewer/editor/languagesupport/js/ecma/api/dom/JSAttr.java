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

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * Object Attr.<p>
 * 
 * See also the <a href='http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407'>Document
 * Object Model (DOM) Level 3 Core Specification</a>.
 * 
 * @author D. Campione
 * 
 */
public abstract class JSAttr implements Attr, IJS5ObjectFunctions {

    /**
     * Object Attr().
     * http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html
     * 
     * @augments Node
     * @constructor
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     * @see Node
     */
    public JSAttr() {
    }

    /**
     * <b>property prototype</b>
     * http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html
     * 
     * @type Attr
     * @memberOf Attr
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom.JSAttr
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSAttr protype;

    /**
     * <b>property constructor</b>
     * http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html
     * 
     * @type Function
     * @memberOf Attr
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;
}