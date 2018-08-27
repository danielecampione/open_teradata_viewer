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

import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.ColDataType;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CastExpression implements IExpression {

    private IExpression leftExpression;
    private ColDataType type;
    private boolean useCastKeyword = true;

    public ColDataType getType() {
        return type;
    }

    public void setType(ColDataType type) {
        this.type = type;
    }

    public IExpression getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(IExpression expression) {
        leftExpression = expression;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public boolean isUseCastKeyword() {
        return useCastKeyword;
    }

    public void setUseCastKeyword(boolean useCastKeyword) {
        this.useCastKeyword = useCastKeyword;
    }

    @Override
    public String toString() {
        if (useCastKeyword) {
            return "CAST(" + leftExpression + " AS " + type.toString() + ")";
        } else {
            return leftExpression + "::" + type.toString();
        }
    }
}