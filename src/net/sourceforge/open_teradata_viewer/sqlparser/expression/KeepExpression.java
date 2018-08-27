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

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.OrderByElement;

/**
 * Analytic function. The name of the function is variable but the parameters
 * following the special analytic function path. e.g. row_number() over (order
 * by test). Additional there can be an expression for an analytical aggregate
 * like sum(col) or the "all collumns" wildcard like count(*).
 *
 * @author D. Campione
 *
 */
public class KeepExpression implements IExpression {

    private String name;
    private List<OrderByElement> orderByElements;
    private boolean first = false;

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append("KEEP (").append(name);

        b.append(" ").append(first ? "FIRST" : "LAST").append(" ");
        toStringOrderByElements(b);

        b.append(")");

        return b.toString();
    }

    private void toStringOrderByElements(StringBuilder b) {
        if (orderByElements != null && !orderByElements.isEmpty()) {
            b.append("ORDER BY ");
            for (int i = 0; i < orderByElements.size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(orderByElements.get(i).toString());
            }
        }
    }
}