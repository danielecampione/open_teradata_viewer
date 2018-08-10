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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.update;


import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Expression;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.StatementVisitor;

/**
 * The update statement.
 * 
 * @author D. Campione
 * 
 */
public class Update implements Statement {

    private Table table;
    private Expression where;
    @SuppressWarnings("rawtypes")
    private List columns;
    @SuppressWarnings("rawtypes")
    private List expressions;

    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public Expression getWhere() {
        return where;
    }

    public void setTable(Table name) {
        table = name;
    }

    public void setWhere(Expression expression) {
        where = expression;
    }

    /**
     * The {@link net.sf.jsqlparser.schema.Column}s in this update (as col1 and col2 in UPDATE col1='a', col2='b')
     * @return a list of {@link net.sf.jsqlparser.schema.Column}s
     */
    @SuppressWarnings("rawtypes")
    public List getColumns() {
        return columns;
    }

    /**
     * The {@link Expression}s in this update (as 'a' and 'b' in UPDATE col1='a', col2='b')
     * @return a list of {@link Expression}s
     */
    @SuppressWarnings("rawtypes")
    public List getExpressions() {
        return expressions;
    }

    @SuppressWarnings("rawtypes")
    public void setColumns(List list) {
        columns = list;
    }

    @SuppressWarnings("rawtypes")
    public void setExpressions(List list) {
        expressions = list;
    }

}
