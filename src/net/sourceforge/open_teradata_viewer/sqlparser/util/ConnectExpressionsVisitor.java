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

package net.sourceforge.open_teradata_viewer.sqlparser.util;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Alias;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.AllColumns;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.AllTableColumns;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectItemVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectExpressionItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SetOperationList;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.WithItem;

/**
 * Connect all selected expressions with a binary expression. Out of select a,b
 * from table one gets select a || b as expr from table. The type of binary
 * expression is set by overwriting this class abstract method
 * createBinaryExpression.
 *
 * @author D. Campione
 * 
 */
public abstract class ConnectExpressionsVisitor implements ISelectVisitor,
        ISelectItemVisitor {

    private String alias = "expr";
    private List<SelectExpressionItem> itemsExpr = new LinkedList<SelectExpressionItem>();

    public ConnectExpressionsVisitor() {
    }

    public ConnectExpressionsVisitor(String alias) {
        this.alias = alias;
    }

    /**
     * Create instances of this binary expression that connects all selected
     * expressions.
     *
     * @return
     */
    protected abstract BinaryExpression createBinaryExpression();

    @Override
    public void visit(PlainSelect plainSelect) {
        for (ISelectItem item : plainSelect.getSelectItems()) {
            item.accept(this);
        }

        if (itemsExpr.size() > 1) {
            BinaryExpression binExpr = createBinaryExpression();
            binExpr.setLeftExpression(itemsExpr.get(0).getExpression());
            for (int i = 1; i < itemsExpr.size() - 1; i++) {
                binExpr.setRightExpression(itemsExpr.get(i).getExpression());
                BinaryExpression binExpr2 = createBinaryExpression();
                binExpr2.setLeftExpression(binExpr);
                binExpr = binExpr2;
            }
            binExpr.setRightExpression(itemsExpr.get(itemsExpr.size() - 1)
                    .getExpression());

            SelectExpressionItem sei = new SelectExpressionItem();
            sei.setExpression(binExpr);

            plainSelect.getSelectItems().clear();
            plainSelect.getSelectItems().add(sei);
        }

        ((SelectExpressionItem) plainSelect.getSelectItems().get(0))
                .setAlias(new Alias(alias));
    }

    @Override
    public void visit(SetOperationList setOpList) {
        for (PlainSelect select : setOpList.getPlainSelects()) {
            select.accept(this);
        }
    }

    @Override
    public void visit(WithItem withItem) {
    }

    @Override
    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        itemsExpr.add(selectExpressionItem);
    }
}