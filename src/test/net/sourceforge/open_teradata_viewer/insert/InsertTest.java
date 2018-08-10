/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2012, D. Campione
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

package test.net.sourceforge.open_teradata_viewer.insert;

import java.io.StringReader;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class InsertTest extends TestCase {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public InsertTest(String arg0) {
        super(arg0);
    }

    public void testRegularInsert() throws JSQLParserException {
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
        assertEquals("myschema.mytable", insert.getTable().getWholeTableName());
        assertEquals(3, ((ExpressionList) insert.getItemsList())
                .getExpressions().size());
        assertTrue(((ExpressionList) insert.getItemsList()).getExpressions()
                .get(0) instanceof JdbcParameter);
        assertEquals(2.3,
                ((DoubleValue) ((ExpressionList) insert.getItemsList())
                        .getExpressions().get(2)).getValue(), 0.0);
        assertEquals(statement, "" + insert);

    }

    public void testInsertFromSelect() throws JSQLParserException {
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
        assertTrue(insert.getItemsList() instanceof SubSelect);
        assertEquals("mytable2",
                ((Table) ((PlainSelect) ((SubSelect) insert.getItemsList())
                        .getSelectBody()).getFromItem()).getName());

        //toString uses brakets
        String statementToString = "INSERT INTO mytable (col1, col2, col3) (SELECT * FROM mytable2)";
        assertEquals(statementToString, "" + insert);
    }

    public static void main(String[] args) {
        TestRunner.run(InsertTest.class);
    }

}
