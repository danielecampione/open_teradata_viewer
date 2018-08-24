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

/**
 * It represents a "-" or "+" before an expression.
 * 
 * @author D. Campione
 * 
 */
public class SignedExpression implements IExpression {

    private char sign;
    private IExpression expression;

    public SignedExpression(char sign, IExpression expression) {
        setSign(sign);
        setExpression(expression);
    }

    public char getSign() {
        return sign;
    }

    public final void setSign(char sign) {
        this.sign = sign;
        if (sign != '+' && sign != '-') {
            throw new IllegalArgumentException(
                    "illegal sign character, only + - allowed");
        }
    }

    public IExpression getExpression() {
        return expression;
    }

    public final void setExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return getSign() + expression.toString();
    }
}