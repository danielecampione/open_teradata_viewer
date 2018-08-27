/*
 * Open Teradata Viewer ( sql parser )
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.insert;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsList;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectExpressionItem;

/**
 * The insert statement. Every column name in <code>columnNames</code> matches
 * an item in <code>itemsList</code>.
 *
 * @author D. Campione
 *
 */
public class Insert implements IStatement {

    private Table table;
    private List<Column> columns;
    private IItemsList itemsList;
    private boolean useValues = true;
    private Select select;
    private boolean useSelectBrackets = true;

    private boolean returningAllColumns = false;

    private List<SelectExpressionItem> returningExpressionList = null;

    @Override
    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
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
     * @return a list of {@link net.sourceforge.open_teradata_viewer.sqlparser.schema.Column}.
     */
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> list) {
        columns = list;
    }

    /**
     * Get the values (as VALUES (...) or SELECT).
     *
     * @return the values of the insert.
     */
    public IItemsList getItemsList() {
        return itemsList;
    }

    public void setItemsList(IItemsList list) {
        itemsList = list;
    }

    public boolean isUseValues() {
        return useValues;
    }

    public void setUseValues(boolean useValues) {
        this.useValues = useValues;
    }

    public boolean isReturningAllColumns() {
        return returningAllColumns;
    }

    public void setReturningAllColumns(boolean returningAllColumns) {
        this.returningAllColumns = returningAllColumns;
    }

    public List<SelectExpressionItem> getReturningExpressionList() {
        return returningExpressionList;
    }

    public void setReturningExpressionList(
            List<SelectExpressionItem> returningExpressionList) {
        this.returningExpressionList = returningExpressionList;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public boolean isUseSelectBrackets() {
        return useSelectBrackets;
    }

    public void setUseSelectBrackets(boolean useSelectBrackets) {
        this.useSelectBrackets = useSelectBrackets;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO ");
        sql.append(table).append(" ");
        if (columns != null) {
            sql.append(PlainSelect.getStringList(columns, true, true)).append(
                    " ");
        }

        if (useValues) {
            sql.append("VALUES ");
        }

        if (itemsList != null) {
            sql.append(itemsList);
        }

        if (useSelectBrackets) {
            sql.append("(");
        }
        if (select != null) {
            sql.append(select);
        }
        if (useSelectBrackets) {
            sql.append(")");
        }

        if (isReturningAllColumns()) {
            sql.append(" RETURNING *");
        } else if (getReturningExpressionList() != null) {
            sql.append(" RETURNING ").append(
                    PlainSelect.getStringList(getReturningExpressionList(),
                            true, false));
        }

        return sql.toString();
    }
}