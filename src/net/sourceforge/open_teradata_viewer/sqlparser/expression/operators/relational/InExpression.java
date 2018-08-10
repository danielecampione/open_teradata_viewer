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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class InExpression implements IExpression {

    private IExpression leftExpression;
    private IItemsList iItemsList;
    private boolean not = false;

    public InExpression() {
    }

    public InExpression(IExpression leftExpression, IItemsList iItemsList) {
        setLeftExpression(leftExpression);
        setItemsList(iItemsList);
    }

    public IItemsList getItemsList() {
        return iItemsList;
    }

    public IExpression getLeftExpression() {
        return leftExpression;
    }

    public void setItemsList(IItemsList list) {
        iItemsList = list;
    }

    public void setLeftExpression(IExpression iExpression) {
        leftExpression = iExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public void accept(IExpressionVisitor iExpressionVisitor) {
        iExpressionVisitor.visit(this);
    }

    public String toString() {
        return leftExpression + " " + ((not) ? "NOT " : "") + "IN "
                + iItemsList + "";
    }
}