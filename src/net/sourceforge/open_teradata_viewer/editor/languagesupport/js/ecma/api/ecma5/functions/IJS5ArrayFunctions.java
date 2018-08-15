/*
 * Open Teradata Viewer ( editor language support js ecma api ecma5 functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSArrayFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Function;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Object;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJS5ArrayFunctions extends IJS5ObjectFunctions,
        IJSArrayFunctions {

    /**
     * <b>function every(predicate, o)</b> - Test whether predicate is true for
     * every element.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * [1,2,3].every(function(x){return x < 5;} // =>true
     * [1,2,3].every(function(x){return x < 2;} // =>false
     * [].every(function(x){return false;} // =>true, always true for []
     * </pre> 
     * 
     * @param predicate A predicate function to test array elements.
     * @param <i>o</i> The optional <b><i>this</i></b> value for invocations of
     *        <b><i>predicate</i></b>.
     * @returns <b><i>true</i></b> if <b><i>predicate</i></b> is true for every
     *          element of the <b>array</b> or <b><i>false</i></b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #filter(JS5Function, JS5Object) filter()
     * @see #forEach(JS5Function, JS5Object) forEach()
     * @see #some(JS5Function, JS5Object) some()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JSBoolean every(JS5Function predicate, JS5Object o);

    /**
     * <b>function filter(predicate, o)</b> - Return array elements that pass a
     * predicate.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * [1,2,3].filter(function(x){return x > 1}); // Returns [2,3]
     * </pre> 
     * 
     * @param predicate The function to invoke to determine whether an element
     *        of <b>array</b> will be included in the returned array.
     * @param <i>o</i> An optional value on which <b><i>predicate</i></b> is
     *        invoked.
     * @returns A new <b>array</b> containing only those elements of
     *          <b>array</b> for which <b><i>predicate</i></b> returned
     *          <b><i>true</i></b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #every(JS5Function, JS5Object) every()
     * @see #forEach(JS5Function, JS5Object) forEach()
     * @see #indexOf(JS5Object, JSNumber) indexOf()
     * @see #map(JS5Function, JS5Object) map()
     * @see #reduce(JS5Function, JS5Object) reduce()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JS5Array filter(JS5Function predicate, JS5Object o);

    /**
     * <b>function forEach(f, o)</b> - Invoke a function for each array
     * element.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var a = [1,2,3];
     * a.forEach(function(x,i,a){a[i]++;}); // a is now [2,3,4]
     * </pre> 
     * 
     * @param f The function to invoke for each element of <b>array</b>.
     * @param <i>o</i> An optional value on which <b><i>f</b></i> is invoked.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @since Standard ECMA-262 5th. Edition.
     */
    public void forEach(JS5Function f, JS5Object o);

    /**
     * <b>function indexOf(value, start)</b> - Search an array.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * ['a','b','c'].indexOf('b'); //returns 1
     * ['a','b','c'].indexOf('d'); //returns -1
     * ['a','b','c'].indexOf('a',1); //returns -1
     * </pre> 
     * 
     * @param {Object} value The value to search <b>array</b> for.
     * @param {Number} <i>start</i> An optional array index at which to begin
     *        the search. If omitted, 0 is used.
     * @returns {Number} The <i>lowest</i> index => start of <b>array</b> at
     *          which the element matches <b><i>value</i></b>. Or -1 if no match
     *          is found.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #lastIndexOf(JS5Object, JSNumber) lastIndexOf()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JSNumber indexOf(JS5Object value, JSNumber start);

    /**
     * <b>function lastIndexOf(value, start)</b> - Search backwards through an
     * array.
     * 
     * @param value The value to search <b>array</b> for.
     * @param <i>start</i> An optional array index at which to begin the search.
     *        If omitted, the search begins at the last element. 
     * @returns The <i>highest</i> index => start of <b>array</b> at which the
     *          element matches <b><i>value</i></b>. Or -1 if no match is found.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #indexOf(JS5Object, JSNumber) indexOf()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JSNumber lastIndexOf(JS5Object value, JSNumber start);

    /**
     * <b>function map(f, o)</b> - Compute new array elements from old.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * [1,2,3].map(function(x){return x*x;}); //returns [1,4,9]
     * </pre> 
     * 
     * @param f The function to invoke for each element of <b>array</b>. Its
     *        return becomes the elements of the returned array.
     * @param <i>o</i> An optional value of which <b><i>f</i></b> is invoked.
     * @returns A new array with elements computed by function <b><i>f</i></b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #every(JS5Function, JS5Object) every()
     * @see #filter(JS5Function, JS5Object) filter()
     * @see #forEach(JS5Function, JS5Object) forEach()
     * @see #reduce(JS5Function, JS5Object) reduce()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JS5Array map(JS5Function f, JS5Object o);

    /**
     * <b>function reduce(f, initial)</b> - Compute a value from the elements of
     * an array.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * [1,2,3].reduce(function(x,y){return x*y;}); //returns 6 ((1*2(*3))
     * </pre> 
     * 
     * @param f A function that combines two values and returns a "reduced"
     *        value.
     * @param <i>initial</i> An optional initial value to see the array
     *        reduction with.
     * @returns The reduced value of an array.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #forEach(JS5Function, JS5Object) forEach()
     * @see #map(JS5Function, JS5Object) map()
     * @see #reduceRight(JS5Function, JS5Object) reduceRight()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JS5Object reduce(JS5Function f, JS5Object initial);

    /**
     * <b>function reduceRight(f, initial)</b> - Reduce an array from
     * right-to-left.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * [2,10,60].reduceRight(function(x,y){return x/y;}); //returns 3 (60/10)/2
     * </pre> 
     * 
     * @param f A function that combines two values and returns a "reduced"
     *        value.
     * @param <i>initial</i> An optional initial value to see the array
     *        reduction with. 
     * @returns The reduced value of an array.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @see #reduce(JS5Function, JS5Object) reduce()
     * @since Standard ECMA-262 5th. Edition.
     */
    public JS5Object reduceRight(JS5Function f, JS5Object initial);

    /**
     * <b>function some(predicate, o)</b> - Test whether a predicate is true for
     * any element.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * [1,2,3].some(function(x){return x > 5;} //=>false
     * [1,2,3].some(function(x){return x > 2;} //=>true
     * [].some(function(x){return true;} //=>false, always false for []
     * </pre> 
     * 
     * @param predicate A predicate function to test array elements.
     * @param <i>o</i> The optional <b><i>this</i></b> value for the invocations
     *        of  <b><i>predicate</i></b>.
     * @returns <b><i>true</i></b> if <b><i>predicate</i></b> returns
     *          <b><i>true</i></b> for at least one element of <b>array</b>,
     *          otherwise <b><i>false</i></b>
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.JS5Array
     * @since Standard ECMA-262 5th. Edition.
     */
    public JSBoolean some(JS5Function predicate, JS5Object o);
}