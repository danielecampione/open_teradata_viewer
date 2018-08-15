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

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Pivot {

    private List<FunctionItem> functionItems;

    private List<Column> forColumns;

    private List<SelectExpressionItem> singleInItems;

    private List<ExpressionListItem> multiInItems;

    public void accept(IPivotVisitor iPivotVisitor) {
        iPivotVisitor.visit(this);
    }

    public List<SelectExpressionItem> getSingleInItems() {
        return singleInItems;
    }

    public void setSingleInItems(List<SelectExpressionItem> singleInItems) {
        this.singleInItems = singleInItems;
    }

    public List<ExpressionListItem> getMultiInItems() {
        return multiInItems;
    }

    public void setMultiInItems(List<ExpressionListItem> multiInItems) {
        this.multiInItems = multiInItems;
    }

    public List<FunctionItem> getFunctionItems() {
        return functionItems;
    }

    public void setFunctionItems(List<FunctionItem> functionItems) {
        this.functionItems = functionItems;
    }

    public List<Column> getForColumns() {
        return forColumns;
    }

    public void setForColumns(List<Column> forColumns) {
        this.forColumns = forColumns;
    }

    public List<?> getInItems() {
        return singleInItems == null ? multiInItems : singleInItems;
    }

    @Override
    public String toString() {
        return "PIVOT ("
                + PlainSelect.getStringList(functionItems)
                + " FOR "
                + PlainSelect.getStringList(forColumns, true,
                        forColumns != null && forColumns.size() > 1) + " IN "
                + PlainSelect.getStringList(getInItems(), true, true) + ")";
    }
}