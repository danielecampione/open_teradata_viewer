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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.insert;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsList;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * The insert statement.
 * Every column name in <code>columnNames</code> matches an item in <code>iItemsList</code>
 * 
 * @author D. Campione
 * 
 */
public class Insert implements IStatement {

    private Table table;
    private List<?> columns;
    private IItemsList iItemsList;
    private boolean useValues = true;

    public void accept(IStatementVisitor iStatementVisitor) {
        iStatementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table name) {
        table = name;
    }

    /**
     * Get the columns (found in "INSERT INTO (col1,col2..) [...]" ).
     * 
     * @return a list of {@link net.sf.jsqlparser.schema.Column}
     */

    public List<?> getColumns() {
        return columns;
    }

    public void setColumns(List<?> list) {
        columns = list;
    }

    /**
     * Get the values (as VALUES (...) or SELECT).
     *  
     * @return the values of the insert
     */
    public IItemsList getItemsList() {
        return iItemsList;
    }

    public void setItemsList(IItemsList list) {
        iItemsList = list;
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
            sql += "VALUES " + iItemsList + "";
        } else {
            sql += "" + iItemsList + "";
        }

        return sql;
    }
}