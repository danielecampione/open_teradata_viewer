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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Expression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.ExpressionVisitor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class InExpression implements Expression {

    private Expression leftExpression;
    private ItemsList itemsList;
    private boolean not = false;

    public InExpression() {
    }

    public InExpression(Expression leftExpression, ItemsList itemsList) {
        setLeftExpression(leftExpression);
        setItemsList(itemsList);
    }

    public ItemsList getItemsList() {
        return itemsList;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public void setItemsList(ItemsList list) {
        itemsList = list;
    }

    public void setLeftExpression(Expression expression) {
        leftExpression = expression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return leftExpression + " " + ((not) ? "NOT " : "") + "IN " + itemsList
                + "";
    }
}