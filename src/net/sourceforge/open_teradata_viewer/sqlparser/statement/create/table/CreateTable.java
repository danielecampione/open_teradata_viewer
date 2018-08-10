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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table;


import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.StatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * A "CREATE TABLE" statement
 * 
 * @author D. Campione
 * 
 */
public class CreateTable implements Statement {

    private Table table;
    @SuppressWarnings("rawtypes")
    private List tableOptionsStrings;
    @SuppressWarnings("rawtypes")
    private List columnDefinitions;
    @SuppressWarnings("rawtypes")
    private List indexes;

    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    /**
     * The name of the table to be created
     */
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * A list of {@link ColumnDefinition}s of this table.
     */
    @SuppressWarnings("rawtypes")
    public List getColumnDefinitions() {
        return columnDefinitions;
    }

    @SuppressWarnings("rawtypes")
    public void setColumnDefinitions(List list) {
        columnDefinitions = list;
    }

    /**
     * A list of options (as simple strings) of this table definition, as ("TYPE", "=", "MYISAM") 
     */
    @SuppressWarnings("rawtypes")
    public List getTableOptionsStrings() {
        return tableOptionsStrings;
    }

    @SuppressWarnings("rawtypes")
    public void setTableOptionsStrings(List list) {
        tableOptionsStrings = list;
    }

    /**
     * A list of {@link Index}es (for example "PRIMARY KEY") of this table.<br>
     * Indexes created with column definitions (as in mycol INT PRIMARY KEY) are not inserted into this list.  
     */
    @SuppressWarnings("rawtypes")
    public List getIndexes() {
        return indexes;
    }

    @SuppressWarnings("rawtypes")
    public void setIndexes(List list) {
        indexes = list;
    }

    public String toString() {
        String sql = "";

        sql = "CREATE TABLE " + table + " (";

        sql += PlainSelect.getStringList(columnDefinitions, true, false);
        if (indexes != null && indexes.size() != 0) {
            sql += ", ";
            sql += PlainSelect.getStringList(indexes);
        }
        sql += ") ";
        sql += PlainSelect.getStringList(tableOptionsStrings, false, false);

        return sql;
    }
}