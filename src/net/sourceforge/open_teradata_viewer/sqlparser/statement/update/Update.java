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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.update;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;

/**
 * The update statement.
 * 
 * @author D. Campione
 * 
 */
public class Update implements IStatement {

    private Table table;
    private IExpression where;
    private List<?> columns;
    private List<?> expressions;

    public void accept(IStatementVisitor iStatementVisitor) {
        iStatementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public IExpression getWhere() {
        return where;
    }

    public void setTable(Table name) {
        table = name;
    }

    public void setWhere(IExpression iExpression) {
        where = iExpression;
    }

    /**
     * The {@link net.sf.jsqlparser.schema.Column}s in this update (as col1 and
     * col2 in UPDATE col1='a', col2='b').
     * 
     * @return a list of {@link net.sf.jsqlparser.schema.Column}s.
     */
    public List<?> getColumns() {
        return columns;
    }

    /**
     * The {@link IExpression}s in this update (as 'a' and 'b' in UPDATE
     * col1='a', col2='b').
     * 
     * @return a list of {@link IExpression}s.
     */
    public List<?> getExpressions() {
        return expressions;
    }

    public void setColumns(List<?> list) {
        columns = list;
    }

    public void setExpressions(List<?> list) {
        expressions = list;
    }
}