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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;

/**
 * A "BETWEEN" expr1 expr2 statement.
 * 
 * @author D. Campione
 * 
 */
public class Between implements IExpression {

    private IExpression leftExpression;
    private boolean not = false;
    private IExpression betweenExpressionStart;
    private IExpression betweenExpressionEnd;

    public IExpression getBetweenExpressionEnd() {
        return betweenExpressionEnd;
    }

    public IExpression getBetweenExpressionStart() {
        return betweenExpressionStart;
    }

    public IExpression getLeftExpression() {
        return leftExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setBetweenExpressionEnd(IExpression expression) {
        betweenExpressionEnd = expression;
    }

    public void setBetweenExpressionStart(IExpression expression) {
        betweenExpressionStart = expression;
    }

    public void setLeftExpression(IExpression expression) {
        leftExpression = expression;
    }

    public void setNot(boolean b) {
        not = b;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return leftExpression + " " + (not ? "NOT " : "") + "BETWEEN "
                + betweenExpressionStart + " AND " + betweenExpressionEnd;
    }
}