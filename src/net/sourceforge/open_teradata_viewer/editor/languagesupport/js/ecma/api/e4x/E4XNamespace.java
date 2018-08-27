/*
 * Open Teradata Viewer ( editor language support js ecma api e4x )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSObjectFunctions;

/**
 * Object Namespace.
 *
 * @author D. Campione
 * @since Standard ECMA-357 2nd. Edition.
 *
 */
public abstract class E4XNamespace implements IJSObjectFunctions {

    /**
     * Object Namespace().
     *
     * @constructor
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XNamespace() {
    }

    /**
     * Object Namespace(uriValue).
     *
     * @constructor
     * @param uriValue uri part of Namespace.
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XNamespace(JSString uriValue) {
    }

    /**
     * Object Namespace(prefixValue, uriValue).
     *
     * @constructor
     * @param prefixValue Optional prefix part of Namespace.
     * @param uriValue uri part of Namespace.
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XNamespace(JSString prefixValue, JSString uriValue) {
    }

    /**
     * <b>property prototype</b>
     *
     * @type Namespace
     * @memberOf Namespace
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XNamespace
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XNamespace protype;

    /**
     * <b>property constructor</b>
     *
     * @type Function
     * @memberOf Namespace
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XNamespace
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSFunction constructor;

    /**
     * <b>property prefix</b>
     *
     * @type String
     * @memberOf Namespace
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XNamespace
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSString prefix;

    /**
     * <b>property uri</b>
     *
     * @type String
     * @memberOf Namespace
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XNamespace
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSString uri;
}