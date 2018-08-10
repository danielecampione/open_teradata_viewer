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

package net.sf.jsqlparser.schema;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

/**
 * A column. It can have the table name it belongs to.
 * 
 * @author L. Francalanci
 * 
 */
public class Column implements Expression {

    private String columnName = "";
    private Table table;

    public Column() {
    }

    public Column(Table table, String columnName) {
        this.table = table;
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public Table getTable() {
        return table;
    }

    public void setColumnName(String string) {
        columnName = string;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return the name of the column, prefixed with 'tableName' and '.' 
     */
    public String getWholeColumnName() {

        String columnWholeName = null;
        String tableWholeName = table.getWholeTableName();

        if (tableWholeName != null && tableWholeName.length() != 0) {
            columnWholeName = tableWholeName + "." + columnName;
        } else {
            columnWholeName = columnName;
        }

        return columnWholeName;

    }

    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return getWholeColumnName();
    }
}