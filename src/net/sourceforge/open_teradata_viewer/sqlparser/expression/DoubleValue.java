/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

/**
 * Every number with a point or a exponential format is a DoubleValue
 * 
 * @author D. Campione
 * 
 */
public class DoubleValue implements Expression {

    private double value;
    private String stringValue;

    public DoubleValue(String value) {
        if (value.charAt(0) == '+') {
            value = value.substring(1);
        }
        this.value = Double.parseDouble(value);
        this.stringValue = value;
    }

    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double d) {
        value = d;
    }

    public String toString() {
        return stringValue;
    }
}
