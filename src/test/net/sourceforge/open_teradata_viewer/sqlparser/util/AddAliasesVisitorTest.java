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

package test.net.sourceforge.open_teradata_viewer.sqlparser.util;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.util.AddAliasesVisitor;

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
public class AddAliasesVisitorTest {

    public AddAliasesVisitorTest() {
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

    /** Test of visit method, of class AddAliasesVisitor. */
    @Test
    public void testVisit_PlainSelect() throws SQLParserException {
        String sql = "select a,b,c from test";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        select.getSelectBody().accept(instance);

        assertEquals("SELECT a AS A1, b AS A2, c AS A3 FROM test",
                select.toString());
    }

    @Test
    public void testVisit_PlainSelect_duplicates() throws SQLParserException {
        String sql = "select a,b as a1,c from test";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        select.getSelectBody().accept(instance);

        assertEquals("SELECT a AS A2, b AS a1, c AS A3 FROM test",
                select.toString());
    }

    @Test
    public void testVisit_PlainSelect_expression() throws SQLParserException {
        String sql = "select 3+4 from test";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        select.getSelectBody().accept(instance);

        assertEquals("SELECT 3 + 4 AS A1 FROM test", select.toString());
    }

    /** Test of visit method, of class AddAliasesVisitor. */
    @Test
    public void testVisit_SetOperationList() throws SQLParserException {
        String sql = "select 3+4 from test union select 7+8 from test2";
        Select setOpList = (Select) parserManager.parse(new StringReader(sql));
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        setOpList.getSelectBody().accept(instance);

        assertEquals(
                "(SELECT 3 + 4 AS A1 FROM test) UNION (SELECT 7 + 8 AS A1 FROM test2)",
                setOpList.toString());
    }
}