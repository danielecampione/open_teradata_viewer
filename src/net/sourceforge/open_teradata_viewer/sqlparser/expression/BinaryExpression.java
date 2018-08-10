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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

/**
 * A basic class for binary expressions, that is expressions having a left member and a right member
 * which are in turn expressions. 
 * 
 * @author D. Campione
 * 
 */
public abstract class BinaryExpression implements IExpression {

    private IExpression leftExpression;
    private IExpression rightExpression;
    private boolean not = false;

    public BinaryExpression() {
    }

    public IExpression getLeftExpression() {
        return leftExpression;
    }

    public IExpression getRightExpression() {
        return rightExpression;
    }

    public void setLeftExpression(IExpression iExpression) {
        leftExpression = iExpression;
    }

    public void setRightExpression(IExpression iExpression) {
        rightExpression = iExpression;
    }

    public void setNot() {
        not = true;
    }

    public boolean isNot() {
        return not;
    }

    public String toString() {
        return (not ? "NOT " : "") + getLeftExpression() + " "
                + getStringExpression() + " " + getRightExpression();
    }

    public abstract String getStringExpression();
}