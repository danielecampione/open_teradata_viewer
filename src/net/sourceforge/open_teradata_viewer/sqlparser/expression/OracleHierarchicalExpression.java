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
 *
 *
 * @author D. Campione
 * 
 */
public class OracleHierarchicalExpression implements IExpression {

    private IExpression startExpression;
    private IExpression connectExpression;
    private boolean noCycle = false;

    public IExpression getStartExpression() {
        return startExpression;
    }

    public void setStartExpression(IExpression startExpression) {
        this.startExpression = startExpression;
    }

    public IExpression getConnectExpression() {
        return connectExpression;
    }

    public void setConnectExpression(IExpression connectExpression) {
        this.connectExpression = connectExpression;
    }

    public boolean isNoCycle() {
        return noCycle;
    }

    public void setNoCycle(boolean noCycle) {
        this.noCycle = noCycle;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (startExpression != null) {
            b.append(" START WITH ").append(startExpression.toString());
        }
        b.append(" CONNECT BY ");
        if (isNoCycle()) {
            b.append("NOCYCLE ");
        }
        b.append(connectExpression.toString());
        return b.toString();
    }
}