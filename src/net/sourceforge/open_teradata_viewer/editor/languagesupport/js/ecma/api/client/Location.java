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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.ILocationFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class Location implements ILocationFunctions {

    /**
      * Object Location().
      * 
      * @super Object
      * @constructor
      * @since Common Usage, no standard.
     */
    public Location() {
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
     * @type Location
     * @memberOf Location
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject Object
     */
    public Location prototype;

    /**
     * <b>property location</b>
     * 
     * @type Location
     * @memberOf Location
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     */
    public Location location;

    /**
     * Property hash.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString hash;

    /**
     * Property host.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString host;

    /**
     * Property hostname.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString hostname;

    /**
     * Property href.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString href;

    /**
     * Property pathname.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString pathname;

    /**
     * Property port.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString port;

    /**
     * Property protocol.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString protocol;

    /**
     * Property search.
     * 
     * @type String
     * @memberOf Location
     */
    public JSString search;
}