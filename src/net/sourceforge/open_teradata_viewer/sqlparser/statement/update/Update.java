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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.update;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IFromItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Join;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;

/**
 * The update statement.
 *
 * @author D. Campione
 *
 */
public class Update implements IStatement {

    private List<Table> tables;
    private IExpression where;
    private List<Column> columns;
    private List<IExpression> expressions;
    private IFromItem fromItem;
    private List<Join> joins;
    private Select select;
    private boolean useColumnsBrackets = true;
    private boolean useSelect = false;

    @Override
    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public List<Table> getTables() {
        return tables;
    }

    public IExpression getWhere() {
        return where;
    }

    public void setTables(List<Table> list) {
        tables = list;
    }

    public void setWhere(IExpression expression) {
        where = expression;
    }

    /**
     * The {@link net.sourceforge.open_teradata_viewer.sqlparser.schema.Column}s
     * in this update (as col1 and col2 in UPDATE col1='a', col2='b').
     *
     * @return a list of {@link net.sourceforge.open_teradata_viewer.sqlparser.schema.Column}s.
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * The {@link IExpression}s in this update (as 'a' and 'b' in UPDATE
     * col1='a', col2='b').
     *
     * @return a list of {@link IExpression}s.
     */
    public List<IExpression> getExpressions() {
        return expressions;
    }

    public void setColumns(List<Column> list) {
        columns = list;
    }

    public void setExpressions(List<IExpression> list) {
        expressions = list;
    }

    public IFromItem getFromItem() {
        return fromItem;
    }

    public void setFromItem(IFromItem fromItem) {
        this.fromItem = fromItem;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public boolean isUseColumnsBrackets() {
        return useColumnsBrackets;
    }

    public void setUseColumnsBrackets(boolean useColumnsBrackets) {
        this.useColumnsBrackets = useColumnsBrackets;
    }

    public boolean isUseSelect() {
        return useSelect;
    }

    public void setUseSelect(boolean useSelect) {
        this.useSelect = useSelect;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("UPDATE ");
        b.append(PlainSelect.getStringList(getTables(), true, false)).append(
                " SET ");

        if (!useSelect) {
            for (int i = 0; i < getColumns().size(); i++) {
                if (i != 0) {
                    b.append(", ");
                }
                b.append(columns.get(i)).append(" = ");
                b.append(expressions.get(i));
            }
        } else {
            if (useColumnsBrackets) {
                b.append("(");
            }
            for (int i = 0; i < getColumns().size(); i++) {
                if (i != 0) {
                    b.append(", ");
                }
                b.append(columns.get(i));
            }
            if (useColumnsBrackets) {
                b.append(")");
            }
            b.append(" = ");
            b.append("(").append(select).append(")");
        }

        if (fromItem != null) {
            b.append(" FROM ").append(fromItem);
            if (joins != null) {
                for (Join join : joins) {
                    if (join.isSimple()) {
                        b.append(", ").append(join);
                    } else {
                        b.append(" ").append(join);
                    }
                }
            }
        }

        if (where != null) {
            b.append(" WHERE ");
            b.append(where);
        }
        return b.toString();
    }
}