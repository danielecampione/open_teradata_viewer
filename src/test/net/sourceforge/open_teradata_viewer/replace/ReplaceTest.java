/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2011, D. Campione
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

package test.net.sourceforge.open_teradata_viewer.replace;

import java.io.StringReader;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ReplaceTest extends TestCase {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public ReplaceTest(String arg0) {
        super(arg0);
    }

    public void testReplaceSyntax1() throws JSQLParserException {
        String statement = "REPLACE mytable SET col1='as', col2=?, col3=565";
        Replace replace = (Replace) parserManager.parse(new StringReader(
                statement));
        assertEquals("mytable", replace.getTable().getName());
        assertEquals(3, replace.getColumns().size());
        assertEquals("col1",
                ((Column) replace.getColumns().get(0)).getColumnName());
        assertEquals("col2",
                ((Column) replace.getColumns().get(1)).getColumnName());
        assertEquals("col3",
                ((Column) replace.getColumns().get(2)).getColumnName());
        assertEquals("as",
                ((StringValue) replace.getExpressions().get(0)).getValue());
        assertTrue(replace.getExpressions().get(1) instanceof JdbcParameter);
        assertEquals(565,
                ((LongValue) replace.getExpressions().get(2)).getValue());
        assertEquals(statement, "" + replace);

    }

    public void testReplaceSyntax2() throws JSQLParserException {
        String statement = "REPLACE mytable (col1, col2, col3) VALUES ('as', ?, 565)";
        Replace replace = (Replace) parserManager.parse(new StringReader(
                statement));
        assertEquals("mytable", replace.getTable().getName());
        assertEquals(3, replace.getColumns().size());
        assertEquals("col1",
                ((Column) replace.getColumns().get(0)).getColumnName());
        assertEquals("col2",
                ((Column) replace.getColumns().get(1)).getColumnName());
        assertEquals("col3",
                ((Column) replace.getColumns().get(2)).getColumnName());
        assertEquals("as",
                ((StringValue) ((ExpressionList) replace.getItemsList())
                        .getExpressions().get(0)).getValue());
        assertTrue(((ExpressionList) replace.getItemsList()).getExpressions()
                .get(1) instanceof JdbcParameter);
        assertEquals(565,
                ((LongValue) ((ExpressionList) replace.getItemsList())
                        .getExpressions().get(2)).getValue());
        assertEquals(statement, "" + replace);
    }

    public void testReplaceSyntax3() throws JSQLParserException {
        String statement = "REPLACE mytable (col1, col2, col3) SELECT * FROM mytable3";
        Replace replace = (Replace) parserManager.parse(new StringReader(
                statement));
        assertEquals("mytable", replace.getTable().getName());
        assertEquals(3, replace.getColumns().size());
        assertEquals("col1",
                ((Column) replace.getColumns().get(0)).getColumnName());
        assertEquals("col2",
                ((Column) replace.getColumns().get(1)).getColumnName());
        assertEquals("col3",
                ((Column) replace.getColumns().get(2)).getColumnName());
        assertTrue(replace.getItemsList() instanceof SubSelect);
        //TODO:
        //assertEquals(statement, ""+replace);
    }

    public static void main(String[] args) {
        TestRunner.run(ReplaceTest.class);
    }

}
