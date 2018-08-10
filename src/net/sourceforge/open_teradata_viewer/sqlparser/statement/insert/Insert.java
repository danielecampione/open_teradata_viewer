/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.insert;


import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ItemsList;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.StatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * The insert statement.
 * Every column name in <code>columnNames</code> matches an item in <code>itemsList</code>
 * 
 * @author D. Campione
 * 
 */
public class Insert implements Statement {

    private Table table;
    @SuppressWarnings("rawtypes")
    private List columns;
    private ItemsList itemsList;
    private boolean useValues = true;

    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table name) {
        table = name;
    }

    /**
     * Get the columns (found in "INSERT INTO (col1,col2..) [...]" )
     * @return a list of {@link net.sf.jsqlparser.schema.Column}
     */
    @SuppressWarnings("rawtypes")
    public List getColumns() {
        return columns;
    }

    @SuppressWarnings("rawtypes")
    public void setColumns(List list) {
        columns = list;
    }

    /**
     * Get the values (as VALUES (...) or SELECT) 
     * @return the values of the insert
     */
    public ItemsList getItemsList() {
        return itemsList;
    }

    public void setItemsList(ItemsList list) {
        itemsList = list;
    }

    public boolean isUseValues() {
        return useValues;
    }

    public void setUseValues(boolean useValues) {
        this.useValues = useValues;
    }

    public String toString() {
        String sql = "";

        sql = "INSERT INTO ";
        sql += table + " ";
        sql += ((columns != null) ? PlainSelect.getStringList(columns, true,
                true) + " " : "");

        if (useValues) {
            sql += "VALUES " + itemsList + "";
        } else {
            sql += "" + itemsList + "";
        }

        return sql;
    }

}
