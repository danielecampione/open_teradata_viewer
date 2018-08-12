/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;

/**
 * An element (column reference) in an "ORDER BY" clause.
 * 
 * @author D. Campione
 * 
 */
public class OrderByElement {

    private IExpression iExpression;
    private boolean asc = true;

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean b) {
        asc = b;
    }

    public void accept(IOrderByVisitor iOrderByVisitor) {
        iOrderByVisitor.visit(this);
    }

    public IExpression getExpression() {
        return iExpression;
    }

    public void setExpression(IExpression iExpression) {
        this.iExpression = iExpression;
    }

    public String toString() {
        return "" + iExpression + ((asc) ? "" : " DESC");
    }
}