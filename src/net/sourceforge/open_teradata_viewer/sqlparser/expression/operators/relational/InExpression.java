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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class InExpression implements IExpression,
        ISupportsOldTeradataJoinSyntax {

    private IExpression leftExpression;
    private IItemsList leftItemsList;
    private IItemsList rightItemsList;
    private boolean not = false;

    private int oldTeradataJoinSyntax = NO_TERADATA_JOIN;

    public InExpression() {
    }

    public InExpression(IExpression leftExpression, IItemsList iItemsList) {
        setLeftExpression(leftExpression);
        setRightItemsList(iItemsList);
    }

    @Override
    public void setOldTeradataJoinSyntax(int oldTeradataJoinSyntax) {
        this.oldTeradataJoinSyntax = oldTeradataJoinSyntax;
        if (oldTeradataJoinSyntax < 0 || oldTeradataJoinSyntax > 1) {
            throw new IllegalArgumentException(
                    "unexpected join type for Teradata found with IN (type="
                            + oldTeradataJoinSyntax + ")");
        }
    }

    @Override
    public int getOldTeradataJoinSyntax() {
        return oldTeradataJoinSyntax;
    }

    public IItemsList getRightItemsList() {
        return rightItemsList;
    }

    public IExpression getLeftExpression() {
        return leftExpression;
    }

    public final void setRightItemsList(IItemsList list) {
        rightItemsList = list;
    }

    public final void setLeftExpression(IExpression expression) {
        leftExpression = expression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public IItemsList getLeftItemsList() {
        return leftItemsList;
    }

    public void setLeftItemsList(IItemsList leftItemsList) {
        this.leftItemsList = leftItemsList;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    private String getLeftExpressionString() {
        return leftExpression
                + (oldTeradataJoinSyntax == TERADATA_JOIN_RIGHT ? "(+)" : "");
    }

    @Override
    public String toString() {
        return (leftExpression == null ? leftItemsList
                : getLeftExpressionString())
                + " "
                + ((not) ? "NOT " : "")
                + "IN " + rightItemsList + "";
    }

    @Override
    public int getTeradataPriorPosition() {
        return ISupportsOldTeradataJoinSyntax.NO_TERADATA_PRIOR;
    }

    @Override
    public void setTeradataPriorPosition(int priorPosition) {
        if (priorPosition != ISupportsOldTeradataJoinSyntax.NO_TERADATA_PRIOR) {
            throw new IllegalArgumentException(
                    "unexpected prior for Teradata found");
        }
    }
}