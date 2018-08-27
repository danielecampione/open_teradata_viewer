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

package test.net.sourceforge.open_teradata_viewer.sqlparser.create;

import static test.net.sourceforge.open_teradata_viewer.sqlparser.TestUtils.assertSqlCanBeParsedAndDeparsed;

import java.io.StringReader;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.view.CreateView;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 *
 *
 * @author D. Campione
 *
 */
public class CreateViewTest extends TestCase {

    CCSqlParserManager parserManager = new CCSqlParserManager();

    public CreateViewTest(String arg0) {
        super(arg0);
    }

    public void testCreateView() throws SQLParserException {
        String statement = "CREATE VIEW myview AS SELECT * FROM mytab";
        CreateView createView = (CreateView) parserManager
                .parse(new StringReader(statement));
        assertFalse(createView.isOrReplace());
        assertEquals("myview", createView.getView().getName());
        assertEquals("mytab",
                ((Table) ((PlainSelect) createView.getSelectBody())
                        .getFromItem()).getName());
        assertEquals(statement, createView.toString());
    }

    public void testCreateView2() throws SQLParserException {
        String stmt = "CREATE VIEW myview AS SELECT * FROM mytab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateView3() throws SQLParserException {
        String stmt = "CREATE OR REPLACE VIEW myview AS SELECT * FROM mytab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateView4() throws SQLParserException {
        String stmt = "CREATE OR REPLACE VIEW view2 AS SELECT a, b, c FROM testtab INNER JOIN testtab2 ON testtab.col1 = testtab2.col2";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateViewWithColumnNames1() throws SQLParserException {
        String stmt = "CREATE OR REPLACE VIEW view1(col1, col2) AS SELECT a, b FROM testtab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateView5() throws SQLParserException {
        String statement = "CREATE VIEW myview AS (SELECT * FROM mytab)";
        String statement2 = "CREATE VIEW myview AS (SELECT * FROM mytab)";
        CreateView createView = (CreateView) parserManager
                .parse(new StringReader(statement));
        assertFalse(createView.isOrReplace());
        assertEquals("myview", createView.getView().getName());
        assertEquals("mytab",
                ((Table) ((PlainSelect) createView.getSelectBody())
                        .getFromItem()).getName());
        assertEquals(statement2, createView.toString());
    }

    public void testCreateViewUnion() throws SQLParserException {
        String stmt = "CREATE VIEW view1 AS (SELECT a, b FROM testtab) UNION (SELECT b, c FROM testtab2)";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateMaterializedView() throws SQLParserException {
        String stmt = "CREATE MATERIALIZED VIEW view1 AS SELECT a, b FROM testtab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }
}