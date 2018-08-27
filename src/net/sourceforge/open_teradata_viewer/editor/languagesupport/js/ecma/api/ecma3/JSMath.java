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

/**
 * Object Math.
 * 
 * @author D. Campione
 * @since Standard ECMA-262 3rd. Edition.
 * 
 */
public abstract class JSMath {

    /**
     * <b>property E</b> - The constant e, the base of the natural logarithm.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber E;

    /**
     * <b>property LN10</b> - The natural logarithm of 10.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber LN10;

    /**
     * <b>property LN2</b> - The natural logarithm of 2.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.
     */
    public static JSNumber LN2;

    /**
     * <b>property LOG2E</b> - The base-2 logarithm of e.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber LOG2E;

    /**
     * <b>property LOG10E</b> - The base-10 logarithm of e.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition. 
     */
    public static JSNumber LOG10E;

    /**
     * <b>property PI</b> - The constant PI.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.  
     */
    public static JSNumber PI;

    /**
     * <b>property SQRT1_2</b> - The number 1 divided by the square root of 2.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber SQRT1_2;

    /**
     * <b>property SQRT2</b> - The square root of 2.
     * 
     * @memberOf Math
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition. 
     */
    public static JSNumber SQRT2;

    /**
     * <b>function abs(x)</b> - Computes an absolute value.
     * 
     * @memberOf Math
     * @param x Any number.
     * @type Number
     * @returns The absolute value of x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.     
     */
    public static JSNumber abs(JSNumber x) {
        return null;
    }

    /**
     * <b>function acos(x)</b> - Compute an arccosine.
     * 
     * @memberOf Math
     * @param x A number between -1.0 and 1.0.
     * @type Number
     * @returns The arccosine, or inverse cosine, of the specified value x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.
     */
    public static JSNumber acos(JSNumber x) {
        return null;
    }

    /**
     * <b>function asin(x)</b> - Compute an arcsine.
     * 
     * @memberOf Math
     * @param x A number between -1.0 and 1.0.
     * @type Number
     * @returns The arcsine of the specified value x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.  
     */
    public static JSNumber asin(JSNumber x) {
        return null;
    }

    /**
      * <b>function atan(x)</b> - Compute an arctangent.
      * 
      * @memberOf Math
      * @param x Any number.
      * @type Number
      * @returns The arc tangent of the specified value x.
      * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
      * @since   Standard ECMA-262 3rd. Edition.
      * @since   Level 2 Document Object Model Core Definition.
      */
    public static JSNumber atan(JSNumber x) {
        return null;
    }

    /**
      * <b>function atan2(x,y)</b> - Compute the angle from the X axis to a
      * point.
      * 
      * @memberOf Math
      * @param y The Y coordinate of the point.
      * @param x The X coordinate of the point.
      * @type Number
      * @returns The arctangent of the quotient of its arguments.
      * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber 
      * @since   Standard ECMA-262 3rd. Edition.
      * @since   Level 2 Document Object Model Core Definition.  
      */
    public static JSNumber atan2(JSNumber y, JSNumber x) {
        return null;
    }

    /**
     * <b>function ceil(x)</b> - Round a number up.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * a = Math.ceil(1.99); // Returns 2.0
     * b = Math.ceil(1.01); // Returns 2.0
     * c = Math.ceil(1.0) // Returns 1.0
     * d = Math.ceil(-1.99); // Returns -1.0
     * </pre>
     * 
     * @memberOf Math 
     * @param x Any number or numeric value.
     * @type Number
     * @returns The closest integer greater to or equal to x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber ceil(JSNumber x) {
        return null;
    }

    /**
     * <b>function cos(x)</b> - Compute a cosine.
     * 
     * @memberOf Math
     * @param x An angle, measured in radians.
     * @type Number
     * @returns The cosine of the specified value.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.  
     */
    public static JSNumber cos(JSNumber x) {
        return null;
    }

    /**
     * <b>function exp(x)</b> - Compute E<sup>x</sup>.
     * 
     * @memberOf Math
     * @param x A numeric value or expression.  
     * @type Number
     * @returns E<sup>x</sup>, e raised to the power of the specified exponent
     *          x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition. 
     */
    public static JSNumber exp(JSNumber x) {
        return null;
    }

    /**
     * <b>function floor(x)</b> - Round a number down.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * a = Math.floor(1.99); // Returns 1.0
     * b = Math.floor(1.01); // Returns 1.0
     * c = Math.floor(1.0) // Returns 1.0
     * d = Math.floor(-1.99); // Returns -2.0
     * </pre>
     * 
     * @memberOf Math
     * @param x Any number or numeric value.
     * @type Number
     * @returns The closest integer less than or equal to x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.  
     */
    public static JSNumber floor(JSNumber x) {
        return null;
    }

    /**
     * <b>function log(x)</b> - Compute a natural logarithm.
     * 
     * @memberOf Math
     * @param x Any number or numeric value greater than 0.
     * @type Number
     * @returns The natural logarithm of <b><i>x</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber log(JSNumber x) {
        return null;
    }

    /**
     * <b>function max(args)</b> - Return the largest argument.
     * 
     * @memberOf Math
     * @param args Zero or more values.
     * @type Number
     * @returns The largest of the arguments. Returns -Infinity if there are no
     *          arguments.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.  
     */
    public static JSNumber max(JSNumber args) {
        return null;
    }

    /**
     * <b>function min(args)</b> - Return the smallest argument.
     * 
     * @memberOf Math
     * @param args Any number of arguments
     * @type Number
     * @returns The smallest of the arguments. Returns -Infinity if there are no
     *          arguments.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber min(JSNumber args) {
        return null;
    }

    /**
     * <b>function pow(x,y)</b> - Compute X<sub>y</sub>
     * 
     * @memberOf Math
     * @param x The number to be raised to a power.
     * @param y The power that x to be raised to.
     * @type Number
     * @returns x to the power of y.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber pow(JSNumber x, JSNumber y) {
        return null;
    }

    /**
     * function random() - Return a pseudorandom number.
     * 
     * @memberOf Math
     * @type Number
     * @returns A pseudorandom number greater or equal to 0.0 and less than 1.0.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.     
     */
    public static JSNumber random() {
        return null;
    }

    /**
     * <b>function round(x)</b> - Round to the nearest integer.
     * 
     * @memberOf Math
     * @param x Any number.
     * @type Number
     * @returns The integer closest to x.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.   
     */
    public static JSNumber round(JSNumber x) {
        return null;
    }

    /**
     * <b>function sin(x)</b> - Compute a sine.
     * 
     * @memberOf Math
     * @param x An angle, in radians.
     * @type Number
     * @returns The sine of x. The return value is between -1.0 and 1.0.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber sin(JSNumber x) {
        return null;
    }

    /**
     * <b>function sqrt(x)</b> - Compute a square root.
     * 
     * @memberOf Math
     * @param x A numeric value greater than or equal to zero.
     * @type Number
     * @returns the square root of x. Returns Nan if x is less than 0.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.     
     */
    public static JSNumber sqrt(JSNumber x) {
        return null;
    }

    /**
     * <b>function tan(x)</b> - Compute a tangent.
     * 
     * @memberOf Math
     * @param x An angle, in radians.
     * @type Number
     * @returns The tangent of the specified angle <b><i>x</i></b>.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since   Standard ECMA-262 3rd. Edition.
     * @since   Level 2 Document Object Model Core Definition.    
     */
    public static JSNumber tan(JSNumber x) {
        return null;
    }
}