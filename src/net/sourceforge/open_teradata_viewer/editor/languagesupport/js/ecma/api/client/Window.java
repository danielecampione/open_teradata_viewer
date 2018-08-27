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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.IWindowFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom.html.JSHTMLDocument;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSGlobal;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class Window extends JSGlobal implements IWindowFunctions {

    /**
     * Object Window().
     * 
     * @constructor
     * @extends Global
     */
    public Window() {
    }

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
     * @type Window
     * @memberOf Window
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public Window prototype;

    /**
     * Property closed.
     * 
     * @type Boolean
     * @memberOf Window
     */
    public JSBoolean closed;

    /**
     * Property window.
     * 
     * @type Window
     * @memberOf Window
     */
    public Window window;

    /**
     * Property frames.
     * 
     * @type Array
     * @memberOf Window
     */
    public JSArray frames;

    /**
     * Property defaultStatus.
     * 
     * @type String
     * @memberOf Window
     */
    public JSString defaultStatus;

    /**
     * Property document.
     * 
     * @type Document
     * @memberOf Window
     */
    public JSHTMLDocument document;

    /**
     * Property history.
     * 
     * @type History
     * @memberOf Window
     */
    public History history;

    /**
     * Property location.
     * 
     * @type Location
     * @memberOf Window
     */
    public Location location;

    /**
     * Property name.
     * 
     * @type String
     * @memberOf Window
     */
    public JSString name;

    /**
     * Property navigator.
     * 
     * @type Navigator
     * @memberOf Window
     */
    public Navigator navigator;

    /**
     * Property opener.
     * 
     * @type Window
     * @memberOf Window
     */
    public Window opener;

    /**
     * Property outerWidth.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber outerWidth;

    /**
     * Property outerHeight.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber outerHeight;

    /**
     * Property pageXOffset.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber pageXOffset;

    /**
     * Property pageYOffset.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber pageYOffset;

    /**
     * Property parent.
     * 
     * @type Window
     * @memberOf Window
     */
    public Window parent;

    /**
     * Property screen.
     * 
     * @type Screen
     * @memberOf Window
     */
    public Screen screen;

    /**
     * Property status.
     * 
     * @type String
     * @memberOf Window
     */
    public JSString status;

    /**
     * Property top.
     * 
     * @type Window
     * @memberOf Window
     */
    public Window top;

    /**
    * Property innerWidth.
    * 
    * @type Number
    * @memberOf Window
    */
    public JSNumber innerWidth;

    /**
     * Property innerHeight.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber innerHeight;

    /**
     * Property screenX.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber screenX;

    /**
     * Property screenY.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber screenY;

    /**
     * Property screenLeft.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber screenLeft;

    /**
     * Property screenTop.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber screenTop;

    /**
     * Property length.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber length;

    /**
     * Property scrollbars.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp scrollbars;

    /**
     * Property scrollX.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber scrollX;

    /**
     * Property scrollY.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber scrollY;

    /**
     * Property content.
     * 
     * @type Window
     * @memberOf Window
     */
    public Window content;

    /**
     * Property menubar.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp menubar;

    /**
     * Property toolbar.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp toolbar;

    /**
     * Property locationbar.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp locationbar;

    /**
     * Property personalbar.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp personalbar;

    /**
     * Property statusbar.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp statusbar;

    /**
     * Property directories.
     * 
     * @type BarProp
     * @memberOf Window
     */
    public BarProp directories;

    /**
     * Property scrollMaxX.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber scrollMaxX;

    /**
     * Property scrollMaxY.
     * 
     * @type Number
     * @memberOf Window
     */
    public JSNumber scrollMaxY;

    /**
     * Property fullScreen.
     * 
     * @type String
     * @memberOf Window
     */
    public JSString fullScreen;

    /**
     * Property frameElement.
     * 
     * @type String
     * @memberOf Window
     */
    public JSString frameElement;

    /**
     * Property sessionStorage.
     * 
     * @type String
     * @memberOf Window
     */
    public JSString sessionStorage;
}