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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.execute;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 *
 *
 * @author D. Campione
 *
 */
public class Execute implements IStatement {

    private String name;
    private ExpressionList exprList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpressionList getExprList() {
        return exprList;
    }

    public void setExprList(ExpressionList exprList) {
        this.exprList = exprList;
    }

    @Override
    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "EXECUTE "
                + name
                + " "
                + PlainSelect.getStringList(exprList.getExpressions(), true,
                        false);
    }
}