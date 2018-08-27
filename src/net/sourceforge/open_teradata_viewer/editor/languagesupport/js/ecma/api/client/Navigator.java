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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.INavigatorFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class Navigator implements INavigatorFunctions {

    /**
     * Object Navigator().
     * 
     * @super Object
     * @constructor
     * @since Common Usage, no standard.
    */
    public Navigator() {
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
     * @type Navigator
     * @memberOf Navigator
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     */
    public Navigator prototype;

    /**
     * Property appCodeName.
     * 
     * @type String
     * @memberOf Navigator
     */
    public JSString appCodeName;

    /**
     * Property appName.
     * 
     * @type String
     * @memberOf Navigator
     */
    public JSString appName;

    /**
     * Property appVersion.
     * 
     * @type String
     * @memberOf Navigator
     */
    public JSString appVersion;

    /**
     * Property cookieEnabled.
     * 
     * @type Boolean
     * @memberOf Navigator
     */
    public JSBoolean cookieEnabled;

    /**
     * Property mimeTypes.
     * 
     * @type Array
     * @memberOf Navigator
     */
    public JS5Array mimeTypes;

    /**
     * Property platform.
     * 
     * @type String
     * @memberOf Navigator
     */
    public JSString platform;

    /**
     * Property plugins.
     * 
     * @type Array
     * @memberOf Navigator
     */
    public JS5Array plugins;

    /**
     * Property userAgent.
     * 
     * @type String
     * @memberOf Navigator
     */
    public JSString userAgent;
}