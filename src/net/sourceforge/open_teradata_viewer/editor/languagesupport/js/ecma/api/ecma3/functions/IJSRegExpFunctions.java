/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJSRegExpFunctions extends IJSObjectFunctions {

    /**
     * <b>function exec(string)</b> - General purpose pattern matching.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var r = new RegExp("/\bJava\w*\b/g");
     * var text = "JavaScript is not the same as Java";
     * while((result = e.exec(text)) != null)
     * {
     *   alert("Matched: " + result[0]);
     * }
     * </pre> 
     * 
     * @param string The string to be searched.
     * @returns An array containing results on the match or <b><i>null</i></b>
     *          if no match is found.
     * @type Array
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSRegExp
     * @see #test(String) test()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray exec(String string);

    /**
     * <b>function test(string)</b> - Test whether a string matches a
     * pattern.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var r = new RegExp("/java/i");
     * r.test("JavaScript"); // Returns true
     * r.test("ECMAScript"); // Returns false
     * </pre> 
     * 
     * @param string The string to be tested
     * @returns <b><i>true</i></b> if <b><i>string</i></b> contains text that
     *          matches <b><i>regexp</i></b>, otherwise <b><i>false</i></b>.
     * @type Boolean
     * @memberOf RegExp
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSRegExp
     * @see #exec(String) exec()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSBoolean test(String string);
}