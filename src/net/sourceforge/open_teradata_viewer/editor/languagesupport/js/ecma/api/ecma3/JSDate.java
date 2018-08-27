/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSDateFunctions;

/**
 * Object Date.
 *
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 *
 */
public abstract class JSDate implements IJSDateFunctions {

    /**
     * Object Date().
     *
     * <p>Creates a Date object set to the current date and time.</p>
     *
     * @constructor
     * @extends Object
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSDate() {
    }

    /**
     * Object Date(milliseconds).
     *
     * @constructor
     * @extends Object
     * @param milliseconds The number of milliseconds between the desired date
     *        and midnight January 1, 1970 (UTC).
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSDate(JSNumber milliseconds) {
    }

    /**
     * Object Date(datestring).
     *
     * @constructor
     * @extends Object
     * @param datestring A single argument that specifies date and optionally,
     *        the time as a string. The string should be in a format accepted by
     *        <b>Date.parse()</b>.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSDate(JSString datestring) {
    }

    /**
     * Object Date(year, month, day, hours, minutes, seconds, ms).
     *
     * @constructor
     * @extends Object
     * @param year The year in a four digit format. e.g 2011 for the year 2011.
     * @param month The month specified as a single integer from 0 (January) to
     *        11 (December).
     * @param day The day of the month as an integer between 1 to 31.
     * @param hours Optional hour value, specified as an integer from 0
     *        (midnight) to 23 (11pm).
     * @param minutes Optional minute value, specified as an integer from 0 to
     *        59.
     * @param seconds Optional second value, specified as an integer from 0 to
     *        59.
     * @param ms Optional milliseconds value, specified as an integer from 0 to
     *        999.
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSDate(JSNumber year, JSNumber month, JSNumber day, JSNumber hours,
            JSNumber minutes, JSNumber seconds, JSNumber ms) {
    }

    /**
     * <b>property prototype</b>
     *
     * @type Date
     * @memberOf Date
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSDate
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSDate prototype;

    /**
     * <b>property constructor</b>
     *
     * @type Function
     * @memberOf Date
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    protected JSFunction constructor;

    /**
     * <b>function UTC(year,month,day,hour,min,sec,ms)</b> - Converts a Date
     * specification to milliseconds.
     *
     * @memberOf Date
     * @param year The year in four digit format. If the year is added between 0
     *        and 99 --> 1900 is added to it.
     * @param month The month specified from 0 (January) to 11 (December).
     * @param day The day in the month between 1 and 31.
     * @param hour The hour specified from 0 (midnight) and 23 (11 p.m).
     * @param min The minutes in the hour, specified from 0 to 59.
     * @param sec The seconds in the minute, specified from 0 to 59.
     * @param ms The milliseconds within the second, specified from 0 to 999.
     * @returns The millisecond representation of the specified universal time
     *          (between 1st January 1970 and the specified time).
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSDate
     * @see #parse(JSString) parse()
     * @static
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public static JSNumber UTC(JSNumber year, JSNumber month, JSNumber day,
            JSNumber hour, JSNumber min, JSNumber sec, JSNumber ms) {
        return null;
    }

    /**
     * <b>function parse(string)</b> - Parse a date/time string.
     *
     * @memberOf Date
     * @param string A string containing the date and time to be parsed.
     * @returns The millisecond between 1st January 1970 and the specified date and time.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSDate
     * @static
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public static JSNumber parse(JSString string) {
        return null;
    }
}