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

package test.net.sourceforge.open_teradata_viewer.sqlparser.statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statements;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;

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
public class StatementsTest {

    public StatementsTest() {
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

    /** Test of toString method, of class Statements. */
    @Test
    public void testStatements() throws SQLParserException {
        String sqls = "select * from mytable; select * from mytable2;";
        Statements parseStatements = CCSqlParserUtil.parseStatements(sqls);

        assertEquals("SELECT * FROM mytable;\nSELECT * FROM mytable2;\n",
                parseStatements.toString());

        assertTrue(parseStatements.getStatements().get(0) instanceof Select);
        assertTrue(parseStatements.getStatements().get(1) instanceof Select);
    }
}