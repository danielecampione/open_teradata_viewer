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

package test.net.sourceforge.open_teradata_viewer.sqlparser.util;

import static org.junit.Assert.assertEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.LongValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Addition;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.EqualsTo;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Join;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.util.SelectUtils;

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
public class SelectUtilsTest {

    public SelectUtilsTest() {
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

    /** Test of addColumn method, of class SelectUtils. */
    @Test
    public void testAddExpr() throws SQLParserException {
        Select select = (Select) CCSqlParserUtil.parse("select a from mytable");
        SelectUtils.addExpression(select, new Column("b"));
        assertEquals("SELECT a, b FROM mytable", select.toString());

        Addition add = new Addition();
        add.setLeftExpression(new LongValue(5));
        add.setRightExpression(new LongValue(6));
        SelectUtils.addExpression(select, add);

        assertEquals("SELECT a, b, 5 + 6 FROM mytable", select.toString());
    }

    @Test
    public void testAddJoin() throws SQLParserException {
        Select select = (Select) CCSqlParserUtil.parse("select a from mytable");
        final EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("a"));
        equalsTo.setRightExpression(new Column("b"));
        Join addJoin = SelectUtils.addJoin(select, new Table("mytable2"),
                equalsTo);
        addJoin.setLeft(true);
        assertEquals("SELECT a FROM mytable LEFT JOIN mytable2 ON a = b",
                select.toString());
    }
}