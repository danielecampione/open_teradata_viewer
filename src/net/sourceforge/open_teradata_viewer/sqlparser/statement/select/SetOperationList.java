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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import java.util.List;

/**
 * A database set operation. This operation consists of a list of plainSelects
 * connected by set operations (UNION,INTERSECT,MINUS,EXCEPT). All these
 * operations have the same priority.
 *
 * @author D. Campione
 * 
 */
public class SetOperationList implements ISelectBody {

    private List<PlainSelect> plainSelects;
    private List<SetOperation> operations;
    private List<OrderByElement> orderByElements;
    private Limit limit;

    @Override
    public void accept(ISelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public List<PlainSelect> getPlainSelects() {
        return plainSelects;
    }

    public List<SetOperation> getOperations() {
        return operations;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public void setOpsAndSelects(List<PlainSelect> select,
            List<SetOperation> ops) {
        plainSelects = select;
        operations = ops;

        if (select.size() - 1 != ops.size()) {
            throw new IllegalArgumentException("list sizes are not valid");
        }
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < plainSelects.size(); i++) {
            if (i != 0) {
                buffer.append(" ").append(operations.get(i - 1).toString())
                        .append(" ");
            }
            buffer.append("(").append(plainSelects.get(i).toString())
                    .append(")");
        }

        if (orderByElements != null) {
            buffer.append(PlainSelect.orderByToString(orderByElements));
        }
        if (limit != null) {
            buffer.append(limit.toString());
        }
        return buffer.toString();
    }

    /**
     * List of set operations.
     * 
     * @author D. Campione
     * 
     */
    public enum SetOperationType {

        INTERSECT, EXCEPT, MINUS, UNION

    }
}