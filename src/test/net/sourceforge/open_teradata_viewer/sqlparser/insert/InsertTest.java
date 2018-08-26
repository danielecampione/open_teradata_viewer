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

package test.net.sourceforge.open_teradata_viewer.sqlparser.insert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static test.net.sourceforge.open_teradata_viewer.sqlparser.TestUtils.assertSqlCanBeParsedAndDeparsed;

import java.io.StringReader;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.DoubleValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.JdbcParameter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.LongValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.StringValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.insert.Insert;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

import org.junit.Test;

/**
 *
 *
 * @author D. Campione
 *
 */
public class InsertTest {

    CCSqlParserManager parserManager = new CCSqlParserManager();

    @Test
    public void testRegularInsert() throws SQLParserException {
        String statement = "INSERT INTO mytable (col1, col2, col3) VALUES (?, 'sadfsd', 234)";
        Insert insert = (Insert) parserManager
                .parse(new StringReader(statement));
        assertEquals("mytable", insert.getTable().getName());
        assertEquals(3, insert.getColumns().size());
        assertEquals("col1",
                ((Column) insert.getColumns().get(0)).getColumnName());
        assertEquals("col2",
                ((Column) insert.getColumns().get(1)).getColumnName());
        assertEquals("col3",
                ((Column) insert.getColumns().get(2)).getColumnName());
        assertEquals(3, ((ExpressionList) insert.getItemsList())
                .getExpressions().size());
        assertTrue(((ExpressionList) insert.getItemsList()).getExpressions()
                .get(0) instanceof JdbcParameter);
        assertEquals("sadfsd",
                ((StringValue) ((ExpressionList) insert.getItemsList())
                        .getExpressions().get(1)).getValue());
        assertEquals(234, ((LongValue) ((ExpressionList) insert.getItemsList())
                .getExpressions().get(2)).getValue());
        assertEquals(statement, "" + insert);

        statement = "INSERT INTO myschema.mytable VALUES (?, ?, 2.3)";
        insert = (Insert) parserManager.parse(new StringReader(statement));
        assertEquals("myschema.mytable", insert.getTable()
                .getFullyQualifiedName());
        assertEquals(3, ((ExpressionList) insert.getItemsList())
                .getExpressions().size());
        assertTrue(((ExpressionList) insert.getItemsList()).getExpressions()
                .get(0) instanceof JdbcParameter);
        assertEquals(2.3,
                ((DoubleValue) ((ExpressionList) insert.getItemsList())
                        .getExpressions().get(2)).getValue(), 0.0);
        assertEquals(statement, "" + insert);

    }

    @Test
    public void testInsertWithKeywordValue() throws SQLParserException {
        String statement = "INSERT INTO mytable (col1) VALUE ('val1')";
        Insert insert = (Insert) parserManager
                .parse(new StringReader(statement));
        assertEquals("mytable", insert.getTable().getName());
        assertEquals(1, insert.getColumns().size());
        assertEquals("col1",
                ((Column) insert.getColumns().get(0)).getColumnName());
        assertEquals("val1",
                ((StringValue) ((ExpressionList) insert.getItemsList())
                        .getExpressions().get(0)).getValue());
        assertEquals("INSERT INTO mytable (col1) VALUES ('val1')",
                insert.toString());
    }

    @Test
    public void testInsertFromSelect() throws SQLParserException {
        String statement = "INSERT INTO mytable (col1, col2, col3) SELECT * FROM mytable2";
        Insert insert = (Insert) parserManager
                .parse(new StringReader(statement));
        assertEquals("mytable", insert.getTable().getName());
        assertEquals(3, insert.getColumns().size());
        assertEquals("col1",
                ((Column) insert.getColumns().get(0)).getColumnName());
        assertEquals("col2",
                ((Column) insert.getColumns().get(1)).getColumnName());
        assertEquals("col3",
                ((Column) insert.getColumns().get(2)).getColumnName());
        assertNull(insert.getItemsList());
        assertNotNull(insert.getSelect());
        assertEquals("mytable2", ((Table) ((PlainSelect) insert.getSelect()
                .getSelectBody()).getFromItem()).getName());

        // toString uses brakets
        String statementToString = "INSERT INTO mytable (col1, col2, col3) SELECT * FROM mytable2";
        assertEquals(statementToString, "" + insert);
    }

    @Test
    public void testInsertMultiRowValue() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (col1, col2) VALUES (a, b), (d, e)");
    }

    @Test
    public void testInsertMultiRowValueDifferent() throws SQLParserException {
        try {
            assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (col1, col2) VALUES (a, b), (d, e, c)");
        } catch (Exception e) {
            return;
        }

        fail("should not work");
    }

    @Test
    public void testSimpleInsert() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO example (num, name, address, tel) VALUES (1, 'name', 'test ', '1234-1234')");
    }

    @Test
    public void testInsertWithReturning() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) VALUES ('1') RETURNING id");
    }

    @Test
    public void testInsertWithReturning2() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) VALUES ('1') RETURNING *");
    }

    @Test
    public void testInsertWithReturning3() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) VALUES ('1') RETURNING id AS a1, id2 AS a2");
    }

    @Test
    public void testInsertSelect() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) SELECT mycolumn FROM mytable");
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) (SELECT mycolumn FROM mytable)");
    }

    @Test
    public void testInsertWithSelect() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) WITH a AS (SELECT mycolumn FROM mytable) SELECT mycolumn FROM a");
        assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (mycolumn) (WITH a AS (SELECT mycolumn FROM mytable) SELECT mycolumn FROM a)");
    }

    @Test
    public void testInsertWithKeywords() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("INSERT INTO kvPair (value, key) VALUES (?, ?)");
    }
}