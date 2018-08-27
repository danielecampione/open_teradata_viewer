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

package test.net.sourceforge.open_teradata_viewer.sqlparser.util;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Addition;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Concat;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.util.ConnectExpressionsVisitor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ConnectExpressionsVisitorTest {

    public ConnectExpressionsVisitorTest() {
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
    CCSqlParserManager parserManager = new CCSqlParserManager();

    @Test
    public void testVisit_PlainSelect_concat() throws SQLParserException {
        String sql = "select a,b,c from test";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        ConnectExpressionsVisitor instance = new ConnectExpressionsVisitor() {
            @Override
            protected BinaryExpression createBinaryExpression() {
                return new Concat();
            }
        };
        select.getSelectBody().accept(instance);

        assertEquals("SELECT a || b || c AS expr FROM test", select.toString());
    }

    @Test
    public void testVisit_PlainSelect_addition() throws SQLParserException {
        String sql = "select a,b,c from test";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        ConnectExpressionsVisitor instance = new ConnectExpressionsVisitor(
                "testexpr") {
            @Override
            protected BinaryExpression createBinaryExpression() {
                return new Addition();
            }
        };
        select.getSelectBody().accept(instance);

        assertEquals("SELECT a + b + c AS testexpr FROM test",
                select.toString());
    }
}