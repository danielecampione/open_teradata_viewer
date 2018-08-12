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
 * A string as in 'example_string'
 * 
 * @author D. Campione
 * 
 */
public class StringValue implements IExpression {

    private String value = "";

    public StringValue(String escapedValue) {
        // Removing "'" at the start and at the end 
        value = escapedValue.substring(1, escapedValue.length() - 1);
    }

    public String getValue() {
        return value;
    }

    public String getNotExcapedValue() {
        StringBuffer buffer = new StringBuffer(value);
        int index = 0;
        int deletesNum = 0;
        while ((index = value.indexOf("''", index)) != -1) {
            buffer.deleteCharAt(index - deletesNum);
            index += 2;
            deletesNum++;
        }
        return buffer.toString();
    }

    public void setValue(String string) {
        value = string;
    }

    public void accept(IExpressionVisitor iExpressionVisitor) {
        iExpressionVisitor.visit(this);
    }

    public String toString() {
        return "'" + value + "'";
    }
}