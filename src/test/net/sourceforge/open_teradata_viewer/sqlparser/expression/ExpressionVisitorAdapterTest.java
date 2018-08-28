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

package test.net.sourceforge.open_teradata_viewer.sqlparser.expression;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.ExpressionVisitorAdapter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.InExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;

/**
 *
 *
 * @author D. Campione
 *
 */
public class ExpressionVisitorAdapterTest {

    public ExpressionVisitorAdapterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInExpressionProblem() throws SQLParserException {
        final List exprList = new ArrayList();
        Select select = (Select) CCSqlParserUtil
                .parse("select * from foo where x in (?,?,?)");
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        IExpression where = plainSelect.getWhere();
        where.accept(new ExpressionVisitorAdapter() {

            @Override
            public void visit(InExpression expr) {
                super.visit(expr);
                exprList.add(expr.getLeftExpression());
                exprList.add(expr.getLeftItemsList());
                exprList.add(expr.getRightItemsList());
            }
        });

        assertTrue(exprList.get(0) instanceof IExpression);
        assertNull(exprList.get(1));
        assertTrue(exprList.get(2) instanceof IItemsList);
    }

    @Test
    public void testInExpression() throws SQLParserException {
        final List exprList = new ArrayList();
        Select select = (Select) CCSqlParserUtil.parse(
                "select * from foo where (a,b) in (select a,b from foo2)");
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        IExpression where = plainSelect.getWhere();
        where.accept(new ExpressionVisitorAdapter() {

            @Override
            public void visit(InExpression expr) {
                super.visit(expr);
                exprList.add(expr.getLeftExpression());
                exprList.add(expr.getLeftItemsList());
                exprList.add(expr.getRightItemsList());
            }
        });

        assertNull(exprList.get(0));
        assertTrue(exprList.get(1) instanceof IItemsList);
        assertTrue(exprList.get(2) instanceof IItemsList);
    }
}