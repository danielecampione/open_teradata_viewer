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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.AllColumns;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Join;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectExpressionItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SetOperationList;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.WithItem;

/**
 * Utility function for select statements.
 *
 * @author D. Campione
 *
 */
public final class SelectUtils {

    private SelectUtils() {
    }

    /** Builds select expr1, expr2 from table. */
    public static Select buildSelectFromTableAndExpressions(Table table,
            IExpression... expr) {
        ISelectItem[] list = new ISelectItem[expr.length];
        for (int i = 0; i < expr.length; i++) {
            list[i] = new SelectExpressionItem(expr[i]);
        }
        return buildSelectFromTableAndSelectItems(table, list);
    }

    /** Builds select expr1, expr2 from table. */
    public static Select buildSelectFromTableAndExpressions(Table table,
            String... expr) throws SQLParserException {
        ISelectItem[] list = new ISelectItem[expr.length];
        for (int i = 0; i < expr.length; i++) {
            list[i] = new SelectExpressionItem(
                    CCSqlParserUtil.parseExpression(expr[i]));
        }
        return buildSelectFromTableAndSelectItems(table, list);
    }

    public static Select buildSelectFromTableAndSelectItems(Table table,
            ISelectItem... selectItems) {
        Select select = new Select();
        PlainSelect body = new PlainSelect();
        body.addSelectItems(selectItems);
        body.setFromItem(table);
        select.setSelectBody(body);
        return select;
    }

    /** Builds select * from table. */
    public static Select buildSelectFromTable(Table table) {
        return buildSelectFromTableAndSelectItems(table, new AllColumns());
    }

    /**
     * Adds an expression to select statements. E.g. a simple column is an
     * expression.
     */
    public static void addExpression(Select select, final IExpression expr) {
        select.getSelectBody().accept(new ISelectVisitor() {

            @Override
            public void visit(PlainSelect plainSelect) {
                plainSelect.getSelectItems()
                        .add(new SelectExpressionItem(expr));
            }

            @Override
            public void visit(SetOperationList setOpList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void visit(WithItem withItem) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    /**
     * Adds a simple join to a select statement. The introduced join is returned
     * for more configuration settings on it (e.g. left join, right join).
     */
    public static Join addJoin(Select select, final Table table,
            final IExpression onExpression) {
        if (select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            List<Join> joins = plainSelect.getJoins();
            if (joins == null) {
                joins = new ArrayList<Join>();
                plainSelect.setJoins(joins);
            }
            Join join = new Join();
            join.setRightItem(table);
            join.setOnExpression(onExpression);
            joins.add(join);
            return join;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Adds group by to a plain select statement. */
    public static void addGroupBy(Select select, final IExpression expr) {
        select.getSelectBody().accept(new ISelectVisitor() {

            @Override
            public void visit(PlainSelect plainSelect) {
                plainSelect.addGroupByColumnReference(expr);
            }

            @Override
            public void visit(SetOperationList setOpList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void visit(WithItem withItem) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
}