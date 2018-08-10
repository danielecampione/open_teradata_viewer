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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * A list of expressions, as in SELECT A FROM TAB WHERE B IN (expr1,expr2,expr3)
 * 
 * @author D. Campione
 * 
 */
public class ExpressionList implements IItemsList {

    private List<?> expressions;

    public ExpressionList() {
    }

    public ExpressionList(List<?> expressions) {
        this.expressions = expressions;
    }

    public List<?> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<?> list) {
        expressions = list;
    }

    public void accept(IItemsListVisitor iItemsListVisitor) {
        iItemsListVisitor.visit(this);
    }

    public String toString() {
        return PlainSelect.getStringList(expressions, true, true);
    }
}