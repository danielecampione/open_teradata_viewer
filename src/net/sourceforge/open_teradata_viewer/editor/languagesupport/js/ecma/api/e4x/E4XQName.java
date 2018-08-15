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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSObjectFunctions;

/**
 * Object QName.
 * 
 * @author D. Campione
 * @since Standard ECMA-357 2nd. Edition.
 * 
 */
public abstract class E4XQName implements IJSObjectFunctions {

    /**
     * Object QName().
     * 
     * @constructor
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XQName() {
    }

    /**
     * Object QName(name).
     * 
     * @constructor
     * @param name Localname of the QName.
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XQName(JSString name) {
    }

    /**
     * Object QName(namespace, name).
     * 
     * @constructor
     * @param <i>namespace</i> Optional namespace part of QName.
     * @param name Localname of the QName.
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XQName(E4XNamespace namespace, JSString name) {
    }

    /**
     * <b>property prototype</b>
     * 
     * @type QName
     * @memberOf QName
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XQName
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XQName protype;

    /**
     * <b>property constructor</b>
     * 
     * @type Function
     * @memberOf QName
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XQName
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSFunction constructor;

    /**
     * <b>property localName</b> local name part of QName.
     * 
     * @type String
     * @memberOf QName
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XQName
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSString localName;

    /**
     * <b>property uri</b> - Namespace uri part of QName.
     * 
     * @type String
     * @memberOf QName
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XQName
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XNamespace
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSString uri;
}