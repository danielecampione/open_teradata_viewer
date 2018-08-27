/*
 * Open Teradata Viewer ( editor language support js ecma api ecma3 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSRegExp;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;

/**
 *
 *
 * @author D. Campione
 *
 */
public interface IJSStringFunctions extends IJSObjectFunctions {

    /**
     * <b>function charAt(position)</b> - Get the nth character from a string.
     *
     * @memberOf String
     * @param position The index of the character that should be returned from
     *        <b><i>string</i></b>.
     * @returns The <i>nth</i> character of <b><i>string</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @see #charCodeAt(JSNumber) charCodeAt()
     * @see #indexOf(JSString, JSNumber) indexOf()
     * @see #lastIndexOf(JSString, JSNumber) lastIndexOf()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString charAt(JSNumber position);

    /**
     * <b>function charCodeAt(position)</b> - Get the nth character code from a
     * string.
     *
     * @memberOf String
     * @param position The index of the character whose encoding is to be
     *        returned.
     * @returns The Unicode encoding of the i>nth</i> character within
     *          <b><i>string</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @see #charAt(JSNumber) charAt()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber charCodeAt(JSNumber position);

    /**
     * <b>function concat(value1, ...)</b> - Concatenate strings.
     *
     * @memberOf String
     * @param value One or more values to be concatenated to
     *        <b><i>string</i></b>.
     * @returns A new string that results from concatenating each argument to a
     *          <b><i>string</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString concat(JSString value);

    /**
     * <b>function indexOf(searchString, startPosition)</b> - Search a string.
     *
     * @memberOf String
     * @param searchString The substring to be search within
     *        <b><i>string</i></b>.
     * @param startPosition Optional start index.
     * @returns The position of the first occurrence of
     *          <b><i>searchString</i></b>. -1 if not found.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber indexOf(JSString searchString, JSNumber startPosition);

    /**
     * <b>function lastIndexOf(searchString, startPosition)</b> - Search a
     * string backward.
     *
     * @memberOf String
     * @param searchString The substring to be search within
     *        <b><i>string</i></b>.
     * @param startPosition Optional start index.
     * @returns The position of the last occurrence of
     *          <b><i>searchString</i></b>. -1 if not found.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber lastIndexOf(JSString searchString, JSNumber startPosition);

    /**
     * <b>function localeCompare(otherString)</b> - Compare one string to
     * another, using locale-specific ordering.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * var string;//array of string initialised somewhere
     * strings.sort(function(a,b){return a.localCompare(b);});
     * </pre>
     *
     * @memberOf String
     * @param otherString A <b><i>string</i></b> to be compared, in a
     *        locale-sensitive fashion, with <b><i>string</i></b>.
     * @returns A number that indicates the result of the comparison.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber localeCompare(JSString otherString);

    /**
     * <b>function match(regexp)</b> - Find one or more regular-expression
     * matches.
     *
     * @memberOf String
     * @param regexp A RegExp object that specifies the pattern to be matched.
     * @returns An Array containing results of the match.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString match(JSRegExp regexp);

    /**
     * <b>function replace(regexp, replaceValue)</b> - Replace substring(s)
     * matching a regular expression.
     *
     * @memberOf String
     * @param regexp A RegExp object that specifies the pattern to be replaced.
     * @param replaceValue A string that specifies the replacement text.
     * @returns {String}
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString replace(JSRegExp regexp, JSString replaceValue);

    /**
     * <b>function search(regexp)</b> - Search for a regular expression.
     *
     * @memberOf String
     * @param regexp A RegExp object that specifies the pattern to be searched.
     * @returns The position of the start of the first substring of
     *          <b><i>string</i></b>. -1 if no match is found.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber search(JSRegExp regexp);

    /**
     * <b>function slice(start, end)</b> - Extract a substring.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * var s = "abcdefg";
     * s.slice(0,4); // Returns "abcd"
     * s.slice(2,4); // Returns "cd"
     * s.slice(4); // Returns "efg"
     * s.slice(3, -1); // Returns "def"
     * s.slice(3,-2); // Returns "de"
     * </pre>
     *
     * @memberOf String
     * @param start The start index where the slice if to begin.
     * @param end Optional end index where the slice is to end.
     * @returns A new string that contains all the characters of
     *          <b><i>string</i></b> from and including <b><i>start</i></b> up
     *          to the <b><i>end</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString slice(JSNumber start, JSNumber end);

    /**
     * <b>function split(separator, limit)</b> - Break a string into an array of
     * strings.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * "1|2|3|4".split("|"); // Returns ["1","2","3","4"]
     * "%1%2%3%4%".split("%"); // Returns ["","1","2","3","4",""]
     * </pre>
     *
     * @memberOf String
     * @param separator The string or regular expression at which the
     *        <b><i>string</i></b> splits.
     * @param limit Optional value that specifies the maximum length of the
     *        returned array.
     * @returns An array of strings, created by splitting <b><i>string</i></b>
     *          using the separator.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray split(JSString separator, JSNumber limit);

    /**
     * <b>function substring(from, to)</b> - Return a substring of a string.
     *
     * @memberOf String
     * @param from The index where to start the extraction. First character is
     *        at index 0.
     * @param to Optional. The index where to stop the extraction. If omitted,
     *        it extracts the rest of the string.
     * @returns A new string of length <b><i>from-to</i></b> which contains a
     *          substring of <b><i>string</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString substring(JSNumber from, JSNumber to);

    /**
     * <b>function toLowerCase()</b> - Converts a string to lower case.
     *
     * @memberOf String
     * @returns A copy of <b><i>string</i></b> converted to lower case.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @see #charAt(JSNumber) charAt()
     * @see #indexOf(JSString, JSNumber) indexOf()
     * @see #lastIndexOf(JSString, JSNumber) lastIndexOf()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toLowerCase();

    /**
     * <b>function toLocaleLowerCase()</b> - Converts a string to lower case.
     *
     * @memberOf String
     * @returns A copy of <b><i>string</i></b> converted to lower case a
     *          locale-specific way.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @see #toLocaleUpperCase()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toLocaleLowerCase();

    /**
     * <b>function toUpperCase()</b> - Converts a string to upper case.
     *
     * @memberOf String
     * @returns A copy of <b><i>string</i></b> converted to upper case.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toUpperCase();

    /**
     * <b>function toLocaleUpperCase()</b> - Converts a string to upper case.
     *
     * @memberOf String
     * @returns A copy of <b><i>string</i></b> converted to upper case a
     *          locale-specific way.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toLocaleUpperCase();
}