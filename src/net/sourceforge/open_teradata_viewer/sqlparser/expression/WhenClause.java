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

/**
 * A clause of following syntax: WHEN condition THEN expression. Which is part
 * of a CaseExpression.
 *
 * @author D. Campione
 * 
 */
public class WhenClause implements IExpression {

    private IExpression whenExpression;
    private IExpression thenExpression;

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression#accept(net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor)
     */
    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    /** @return Returns the thenExpression. */
    public IExpression getThenExpression() {
        return thenExpression;
    }

    /** @param thenExpression The thenExpression to set. */
    public void setThenExpression(IExpression thenExpression) {
        this.thenExpression = thenExpression;
    }

    /** @return Returns the whenExpression. */
    public IExpression getWhenExpression() {
        return whenExpression;
    }

    /** @param whenExpression The whenExpression to set. */
    public void setWhenExpression(IExpression whenExpression) {
        this.whenExpression = whenExpression;
    }

    @Override
    public String toString() {
        return "WHEN " + whenExpression + " THEN " + thenExpression;
    }
}