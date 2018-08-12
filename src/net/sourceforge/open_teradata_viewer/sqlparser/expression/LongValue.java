/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2013, D. Campione
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
 * Every number without a point or an exponential format is a LongValue
 * 
 * @author D. Campione
 * 
 */
public class LongValue implements IExpression {

    private long value;
    private String stringValue;

    public LongValue(String value) {
        if (value.charAt(0) == '+') {
            value = value.substring(1);
        }
        this.value = Long.parseLong(value);
        setStringValue(value);
    }

    public void accept(IExpressionVisitor iExpressionVisitor) {
        iExpressionVisitor.visit(this);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long d) {
        value = d;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String string) {
        stringValue = string;
    }

    public String toString() {
        return getStringValue();
    }
}