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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.delete;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Expression;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.StatementVisitor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Delete implements Statement {

    private Table table;
    private Expression where;

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

    public String toString() {
        return "DELETE FROM " + table
                + ((where != null) ? " WHERE " + where : "");
    }
}
