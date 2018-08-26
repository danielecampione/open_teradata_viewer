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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Alias;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsListVisitor;

/**
 * A subselect followed by an optional alias.
 * 
 * @author D. Campione
 * 
 */
public class SubSelect implements IFromItem, IExpression, IItemsList {

    private ISelectBody selectBody;
    private Alias alias;

    private Pivot pivot;

    @Override
    public void accept(IFromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public ISelectBody getSelectBody() {
        return selectBody;
    }

    public void setSelectBody(ISelectBody body) {
        selectBody = body;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public Alias getAlias() {
        return alias;
    }

    @Override
    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override
    public Pivot getPivot() {
        return pivot;
    }

    @Override
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    @Override
    public void accept(IItemsListVisitor iItemsListVisitor) {
        iItemsListVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + selectBody + ")" + ((pivot != null) ? " " + pivot : "")
                + ((alias != null) ? alias.toString() : "");
    }
}