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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJSArrayFunctions extends IJSObjectFunctions {

    /**
     * <b>function concat(args)</b> - Creates and returns a new <b>array</b>
     * that is the result of concatenating each of its arguments to an array.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var c = [1,2,3];
     * c.concat(4,5); // Returns [1,2,3,4,5]
     * c.concat([4,5]); // Returns [1,2,3,4,5]
     * c.concat(4,[5,[6, 7]]); // Returns [1,2,3,4,5, [6,7]]
     * </pre> 
     * 
     * @param args... Any number of values to be concatenated to an
     *        <b>array</b>.
     * @returns A new <b>array</b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray concat(JSArray args);

    /**
     * <b>function join(separator)</b> - Converts each element of the
     * <b>array</b> into a string. Uses the separator between the elements.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var a = [1,2,3];
     * var s = a.join("|"); // s is the string "1|2|3"
     * </pre> 
     * 
     * @param <i>separator</i> An optional character or string used to separate
     *        each element with the string.  
     * @returns A string representing the result of all the elements in the
     *          <b>array</b> concatenated together, separated by the separator.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString join(String seperator);

    /**
     * <b>function pop()</b> - Removes and returns the last element of an
     * <b>array</b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var stack = [1,2,3];
     * stack.pop(); // Returns 3, stack [1, 2]
     * stack.pop(); // Returns 2, stack [1]
     * stack.pop(); // Returns 1, stack []
     * </pre> 
     * 
     * @returns The last element of the <b>array</b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @see #push(JSArray) push()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSObject pop();

    /**
     * <b>function push(args)</b> - Append elements to an <b>array</b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var vals = [];
     * vals.push(1,2,3); // Returns new array [1,2,3]
     * </pre> 
     * 
     * @param args One or more values to be appended to the end of the <b>array</b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @see #pop()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public void push(JSArray array);

    /**
     * <b>function reverse()</b> - Reverse the elements of an <b>array</b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var r = [1,2,3];
     * r.reverse(); // r is now [3,2,1]
     * </pre> 
     * 
     * @returns The <b>array</b> after it has been reversed.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray reverse();

    /**
     * <b>function shift()</b> - Shift <b>array</b> elements down.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var s = [1,2,3];
     * s.shift(); // Returns 1; s = [2,3]
     * s.shift(); // Returns 2; s = [3]
     * </pre> 
     * 
     * @returns The former first element of the <b>array</b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @see #pop() pop()
     * @see #unshift(JSArray) unshift()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSObject shift();

    /**
     * <b>function slice(start, end)</b> - Return a portion of an
     * <b>array</b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var s = [1,2,3,4,5];
     * s.slice(0,3); // Returns [1,2,3]
     * s.slice(3); // Returns [4,5]
     * s.slice(1,-1); // Returns [2,3,4]
     * </pre> 
     * 
     * @param start The array index from where to begin. If negative, this
     *        argument specifies a position measured from the end of the array. 
     * @param end The array index immediately after the end of the slice. If not
     *        specified then the slice includes all the array elements from the
     *        start to the end of the array.
     * @returns A new <b>array</b> containing elements from the
     *          <b><i>start</b></i> up to, but not including the
     *          <b><i>end</i></b> of the slice.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @see #splice(JSNumber, JSNumber, JSArray) splice();
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray slice(Number start, Number end);

    /**
     * <b>function sort(function)</b> - Sort the elements of an <b>array</b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * function numbersort(a,b) {return a-b;}
     * var s = new Array(22,1111,33,4,55]);
     * s.sort(); // Alphabetical order : 1111,22,33,4,55
     * s.sort(numbersort); // Numerical order : 4, 22, 33, 55, 1111
     * </pre> 
     * 
     * @param <i>function</i> An optional function used to specify the sorting
     *        order.
     * @returns A reference to the <b>array</b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray sort(JSFunction function);

    /**
     * <b>function splice(start, deletecount, items)</b> - Insert, remove or
     * replace <b>array</b> elements.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var s = [1,2,3,4,5,6,7,8];
     * s.splice(1,2); // Returns [2,3]; s is [1,4]
     * s.splice(1,1); // Returns [4]; s is [1]
     * s.splice(1,0,2,3); // Returns []; s is [1 2 3]
     * </pre> 
     * 
     * @param start The <b>array</b> element at which the insertion and/or
     *        deletion is to begin.
     * @param <i>deletecount</i> The number of elements starting with and
     *        including <i><b>start</b></i>. 
     * @param items Zero or more items to be inserted into the <b>array</b>.
     * @returns An <b>array</b> containing the elements, if any, deleted from
     *          the <b>array</b>.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @see #shift() shift();
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSArray splice(JSNumber start, JSNumber deletecount, JSArray items);

    /**
     * <b>function unshift(items)</b> - Insert elements at the beginning of an
     * <b>array</b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var s = [];
     * s.unshift(1,2); // Returns 2; s is [1,2]
     * s.unshift(22); // Returns 3; s is [22,1,2]
     * s.shift(); // Returns 22; s is [1,2]
     * </pre> 
     * 
     * @param value One or more values to insert at the beginning of the
     *        <b>array</b>.
     * @returns The new length of the array.
     * @memberOf Array
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray
     * @see #shift() shift()
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSNumber unshift(JSArray value);
}