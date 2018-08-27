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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import java.util.Iterator;
import java.util.List;

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
    private boolean useBrackets = true;
    private List<WithItem> withItemsList;

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

    public boolean isUseBrackets() {
        return useBrackets;
    }

    public void setUseBrackets(boolean useBrackets) {
        this.useBrackets = useBrackets;
    }

    public List<WithItem> getWithItemsList() {
        return withItemsList;
    }

    public void setWithItemsList(List<WithItem> withItemsList) {
        this.withItemsList = withItemsList;
    }

    @Override
    public void accept(IItemsListVisitor iItemsListVisitor) {
        iItemsListVisitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder retval = new StringBuilder();
        if (useBrackets) {
            retval.append("(");
        }
        if (withItemsList != null && !withItemsList.isEmpty()) {
            retval.append("WITH ");
            for (Iterator<WithItem> iter = withItemsList.iterator(); iter
                    .hasNext();) {
                WithItem withItem = (WithItem) iter.next();
                retval.append(withItem);
                if (iter.hasNext()) {
                    retval.append(",");
                }
                retval.append(" ");
            }
        }
        retval.append(selectBody);
        if (useBrackets) {
            retval.append(")");
        }

        if (pivot != null) {
            retval.append(" ").append(pivot);
        }
        if (alias != null) {
            retval.append(alias.toString());
        }

        return retval.toString();
    }
}