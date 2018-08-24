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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
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
public class AnalyticExpression implements IExpression {

    private List<Column> partitionByColumns;
    private List<OrderByElement> orderByElements;
    private String name;
    private IExpression expression;
    private IExpression offset;
    private IExpression defaultValue;
    private boolean allColumns = false;
    private WindowElement windowElement;

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

    public List<Column> getPartitionByColumns() {
        return partitionByColumns;
    }

    public void setPartitionByColumns(List<Column> partitionByColumns) {
        this.partitionByColumns = partitionByColumns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IExpression getExpression() {
        return expression;
    }

    public void setExpression(IExpression expression) {
        this.expression = expression;
    }

    public IExpression getOffset() {
        return offset;
    }

    public void setOffset(IExpression offset) {
        this.offset = offset;
    }

    public IExpression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(IExpression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public WindowElement getWindowElement() {
        return windowElement;
    }

    public void setWindowElement(WindowElement windowElement) {
        this.windowElement = windowElement;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append(name).append("(");
        if (expression != null) {
            b.append(expression.toString());
            if (offset != null) {
                b.append(", ").append(offset.toString());
                if (defaultValue != null) {
                    b.append(", ").append(defaultValue.toString());
                }
            }
        } else if (isAllColumns()) {
            b.append("*");
        }
        b.append(") OVER (");

        toStringPartitionBy(b);
        toStringOrderByElements(b);

        b.append(")");

        return b.toString();
    }

    public boolean isAllColumns() {
        return allColumns;
    }

    public void setAllColumns(boolean allColumns) {
        this.allColumns = allColumns;
    }

    private void toStringPartitionBy(StringBuilder b) {
        if (partitionByColumns != null && !partitionByColumns.isEmpty()) {
            b.append("PARTITION BY ");
            for (int i = 0; i < partitionByColumns.size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(partitionByColumns.get(i).toString());
            }
            b.append(" ");
        }
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

            if (windowElement != null) {
                b.append(' ');
                b.append(windowElement);
            }
        }
    }
}