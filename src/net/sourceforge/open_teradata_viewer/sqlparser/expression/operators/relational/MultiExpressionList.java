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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;

/**
 * A list of ExpressionList items. e.g. multi values of insert statements. This
 * one allows only equally sized ExpressionList.
 *
 * @author D. Campione
 * 
 */
public class MultiExpressionList implements IItemsList {

    private List<ExpressionList> exprList;

    public MultiExpressionList() {
        this.exprList = new ArrayList<ExpressionList>();
    }

    @Override
    public void accept(IItemsListVisitor iItemsListVisitor) {
        iItemsListVisitor.visit(this);
    }

    public List<ExpressionList> getExprList() {
        return exprList;
    }

    public void addExpressionList(ExpressionList el) {
        if (!exprList.isEmpty()
                && exprList.get(0).getExpressions().size() != el
                        .getExpressions().size()) {
            throw new IllegalArgumentException("different count of parameters");
        }
        exprList.add(el);
    }

    public void addExpressionList(List<IExpression> list) {
        addExpressionList(new ExpressionList(list));
    }

    public void addExpressionList(IExpression expr) {
        addExpressionList(new ExpressionList(Arrays.asList(expr)));
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Iterator<ExpressionList> it = exprList.iterator(); it.hasNext();) {
            b.append(it.next().toString());
            if (it.hasNext()) {
                b.append(", ");
            }
        }
        return b.toString();
    }
}