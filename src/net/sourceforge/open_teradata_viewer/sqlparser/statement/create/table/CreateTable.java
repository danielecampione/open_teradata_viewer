/*
 * Open Teradata Viewer ( sql parser )
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;

/**
 * A "CREATE TABLE" statement.
 *
 * @author D. Campione
 *
 */
public class CreateTable implements IStatement {

    private Table table;
    private boolean unlogged = false;
    private List<String> createOptionsStrings;
    private List<String> tableOptionsStrings;
    private List<ColumnDefinition> columnDefinitions;
    private List<Index> indexes;
    private Select select;

    @Override
    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    /** The name of the table to be created. */
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    /** Whether the table is unlogged or not. */
    public boolean isUnlogged() {
        return unlogged;
    }

    public void setUnlogged(boolean unlogged) {
        this.unlogged = unlogged;
    }

    /** A list of {@link ColumnDefinition}s of this table. */
    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> list) {
        columnDefinitions = list;
    }

    /**
     * A list of options (as simple strings) of this table definition, as
     * ("TYPE", "=", "MYISAM").
     */
    public List<?> getTableOptionsStrings() {
        return tableOptionsStrings;
    }

    public void setTableOptionsStrings(List<String> list) {
        tableOptionsStrings = list;
    }

    public List<String> getCreateOptionsStrings() {
        return createOptionsStrings;
    }

    public void setCreateOptionsStrings(List<String> createOptionsStrings) {
        this.createOptionsStrings = createOptionsStrings;
    }

    /**
     * A list of {@link Index}es (for example "PRIMARY KEY") of this table.<br>
     * Indexes created with column definitions (as in mycol INT PRIMARY KEY) are
     * not inserted into this list.
     */
    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> list) {
        indexes = list;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    @Override
    public String toString() {
        String sql = "";
        String createOps = PlainSelect.getStringList(createOptionsStrings,
                false, false);

        sql = "CREATE " + (unlogged ? "UNLOGGED " : "")
                + (!"".equals(createOps) ? createOps + " " : "") + "TABLE "
                + table;

        if (select != null) {
            sql += " AS " + select.toString();
        } else {
            sql += " (";

            sql += PlainSelect.getStringList(columnDefinitions, true, false);
            if (indexes != null && indexes.size() != 0) {
                sql += ", ";
                sql += PlainSelect.getStringList(indexes);
            }
            sql += ")";
            String options = PlainSelect.getStringList(tableOptionsStrings,
                    false, false);
            if (options != null && options.length() > 0) {
                sql += " " + options;
            }
        }

        return sql;
    }
}