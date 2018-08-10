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

package test.net.sourceforge.open_teradata_viewer.update;

import java.io.StringReader;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class UpdateTest extends TestCase {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public UpdateTest(String arg0) {
        super(arg0);
    }
    public void testUpdate() throws JSQLParserException {
        String statement = "UPDATE mytable set col1='as', col2=?, col3=565 Where o >= 3";
        Update update = (Update) parserManager
                .parse(new StringReader(statement));
        assertEquals("mytable", update.getTable().getName());
        assertEquals(3, update.getColumns().size());
        assertEquals("col1",
                ((Column) update.getColumns().get(0)).getColumnName());
        assertEquals("col2",
                ((Column) update.getColumns().get(1)).getColumnName());
        assertEquals("col3",
                ((Column) update.getColumns().get(2)).getColumnName());
        assertEquals("as",
                ((StringValue) update.getExpressions().get(0)).getValue());
        assertTrue(update.getExpressions().get(1) instanceof JdbcParameter);
        assertEquals(565,
                ((LongValue) update.getExpressions().get(2)).getValue());

        assertTrue(update.getWhere() instanceof GreaterThanEquals);
    }

    @SuppressWarnings("unused")
    public void testUpdateWAlias() throws JSQLParserException {
        String statement = "UPDATE table1 A SET A.column = 'XXX' WHERE A.cod_table = 'YYY'";
        Update update = (Update) parserManager
                .parse(new StringReader(statement));
    }

    public static void main(String[] args) {
        TestRunner.run(UpdateTest.class);
    }

}
