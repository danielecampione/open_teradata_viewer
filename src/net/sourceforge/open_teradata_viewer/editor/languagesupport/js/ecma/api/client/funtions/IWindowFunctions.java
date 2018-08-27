/*
 * Open Teradata Viewer ( editor language support js ecma api client funtions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.Window;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions;

import org.w3c.dom.Element;

/**
 *
 *
 * @author D. Campione
 *
 */
public interface IWindowFunctions extends IJS5ObjectFunctions {

    /**
     * function alert() - Displays an alert box with a message and an OK button.
     *
     * @param arg
     * @memberOf  Window
     */
    public void alert(JSString arg);

    /**
     * function blur() - Removes focus from the current window.
     *
     * @memberOf  Window
     */
    public void blur();

    /**
     * function clearInterval(arg) - Clears a timer set with setInterval().
     *
     * @memberOf  Window
     */
    public void clearInterval(JS5Object arg);

    /**
     * function clearTimeout(arg) - Clears a timer set with setTimeout().
     *
     * @memberOf  Window
     */
    public void clearTimeout(JS5Object arg);

    /**
     * function close() - Closes the current window.
     *
     * @memberOf  Window
     */
    public void close();

    /**
     * function confirm() - Displays a dialog box with a message and an OK and a
     * Cancel button.
     *
     * @param arg
     * @memberOf  Window
     * @returns Boolean
     */
    public JSBoolean confirm(JSString arg);

    /**
     * function focus() - Sets focus to the current window.
     *
     * @memberOf  Window
     */
    public void focus();

    /**
     * function getComputedStyle(arg1, arg2).
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     * @returns Object
     */
    public JS5Object getComputedStyle(Element arg1, JSString arg2);

    /**
     * function moveTo(arg1, arg2) - Moves a window to the specified position.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     */
    public void moveTo(JSNumber arg1, JSNumber arg2);

    /**
     * function moveBy(arg1, arg2) - Moves a window relative to its current
     * position.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     */
    public void moveBy(JSNumber arg1, JSNumber arg2);

    /**
     * function open(URL, name, specs, replace) - Opens a new browser window.
     *
     * @param URL
     * @param name
     * @param specs
     * @param replace
     * @memberOf  Window
     * @returns Opened Window object.
     */
    public Window open(JSString URL, JSString name, JSString specs,
            JSBoolean replace);

    /**
     * function print() - Prints the content of the current window.
     *
     * @memberOf  Window
     */
    public void print();

    /**
     * function prompt(arg1, arg2) - Displays a dialog box that prompts the
     * visitor for input.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     * @returns String
     */
    public JSString prompt();

    /**
     * function resizeTo(arg1, arg2) - Resizes the window to the specified width
     * and height.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     */
    public void resizeTo(JSNumber arg1, JSNumber arg2);

    /**
     * function resizeBy(arg1, arg2) - Resizes the window by the specified
     * pixels.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     */
    public void resizeBy(JSNumber arg1, JSNumber arg2);

    /**
     * function scrollTo(arg1, arg2) - Scrolls the content to the specified
     * coordinates.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     */
    public void scrollTo(JSNumber arg1, JSNumber arg2);

    /**
     * function scrollBy(arg1, arg2) - Scrolls the content by the specified
     * number of pixels.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     */
    public void scrollBy(JSNumber arg1, JSNumber arg2);

    /**
     * function setInterval(arg1, arg2) - Calls a function or evaluates an
     * expression at specified intervals (in milliseconds).
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     * @returns Number
     */
    public JSNumber setInterval(JSObject arg1, JSNumber arg2);

    /**
     * function setTimeout(arg1, arg2) - Calls a function or evaluates an
     * expression after a specified number of milliseconds.
     *
     * @param arg1
     * @param arg2
     * @memberOf  Window
     * @returns Number
     */
    public JSNumber setTimeout(JSObject arg1, JSNumber arg2);

    /**
     * function atob(arg) - The atob() method of window object decodes a string
     * of data which has been encoded using base-64 encoding. For example, the
     * window.btoa method takes a binary string as a parameter and returns a
     * base-64 encoded string.
     *
     * @param arg
     * @memberOf  Window
     * @returns String
     */
    public JSString atob(JSString arg);

    /**
     * function btoa(arg) - The btoa() method of window object is used to convert
     * a given string to a encoded data (using base-64 encoding) string.
     *
     * @param arg
     * @memberOf  Window
     * @returns {String}
     */
    public JSString btoa(JSString arg);

    /**
     * function setResizable(arg).
     *
     * @param arg
     * @memberOf  Window
     */
    public void setResizable(JSBoolean arg);

    public void captureEvents(JSObject arg1);

    public void releaseEvents(JSObject arg1);

    public void routeEvent(JSObject arg1);

    public void enableExternalCapture();

    public void disableExternalCapture();

    public void find();

    public void back();

    public void forward();

    public void home();

    public void stop();

    public void scroll(JSNumber arg1, JSNumber arg2);
}