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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.OrderByElement;

/**
 *
 *
 * @author D. Campione
 *
 */
public class WithinGroupExpression implements IExpression {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<OrderByElement> orderByElements;

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    private ExpressionList exprList;

    public ExpressionList getExprList() {
        return exprList;
    }

    public void setExprList(ExpressionList exprList) {
        this.exprList = exprList;
    }

    @Override
    public void accept(IExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append(name);
        b.append(exprList.toString());
        b.append(" WITHIN GROUP (");

        b.append("ORDER BY ");
        for (int i = 0; i < orderByElements.size(); i++) {
            if (i > 0) {
                b.append(", ");
            }
            b.append(orderByElements.get(i).toString());
        }

        b.append(")");

        return b.toString();
    }
}