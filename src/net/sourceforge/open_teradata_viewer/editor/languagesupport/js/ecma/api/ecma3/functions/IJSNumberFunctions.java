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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJSNumberFunctions extends IJSObjectFunctions {

    /**
     * <b>function toFixed(fractionDigits)</b> - Format a number using
     * fixed-point notation.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var n = 12345.6789;
     * n.toFixed(); // Returns 12346: note rounding up
     * n.toFixed(1); // Returns 12345.7: note rounding up
     * n.toFixed(6); // Returns 12345.678900: note zeros
     * (1.23e+20).toFixed(2); // Returns 123000000000000000000.00
     * </pre>
     * 
     * @memberOf Number
     * @param fractionDigits The number of digits to appear after the decimal
     *        point. If omitted it is treated as 0. 
     * @returns A string representation of <b><i>number</i></b> that does not
     *          use exponential notation and has exactly the digits applied.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toFixed(JSNumber fractionDigits);

    /**
     * <b>function toExponential(fractionDigits)</b> - Format a number using
     * exponential notation.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var n = 12345.6789;
     * n.toExponential(1); // Returns 1.2e+4
     * n.toExponential(5); // Returns 1.23457e+4
     * n.toExponential(10); // Returns 1.2345678900e+4
     * n.toExponential(); // Returns 1.23456789e+4
     * </pre> 
     * 
     * @memberOf Number
     * @param fractionDigits The number of digits that appear after the decimal
     *        point.
     * @returns A string representation of <b><i>number</i></b> in exponential
     *          notation.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toExponential(JSNumber fractionDigits);

    /**
     * <b>function toPrecision(precision)</b> - Format the significant digits of
     * a number.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var n = 12345.6789;
     * n.toPrecision(1); // Returns 1e+4
     * n.toPrecision(3); // Returns 1.23e+4
     * n.toPrecision(5); // Returns 12346
     * n.toPrecision(10); // Returns 12345.67890
     * </pre>
     * 
     * @memberOf Number
     * @param fractionDigits The number of significant digits to appear in the
     *        returned string.
     * @returns A string representation of <b><i>number</i></b> that contains
     *          the number significant digits.
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber
     * @since Standard ECMA-262 3rd. Edition.
     * @since Level 2 Document Object Model Core Definition.
     */
    public JSString toPrecision(JSNumber fractionDigits);
}