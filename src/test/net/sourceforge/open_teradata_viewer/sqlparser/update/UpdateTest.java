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

package test.net.sourceforge.open_teradata_viewer.sqlparser.update;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static test.net.sourceforge.open_teradata_viewer.sqlparser.TestUtils.assertSqlCanBeParsedAndDeparsed;

import java.io.StringReader;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.JdbcParameter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.LongValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.StringValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.GreaterThanEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.update.Update;

import org.junit.Test;

/**
 *
 *
 * @author D. Campione
 *
 */
public class UpdateTest {

    static CCSqlParserManager parserManager = new CCSqlParserManager();

    @Test
    public void testUpdate() throws SQLParserException {
        String statement = "UPDATE mytable set col1='as', col2=?, col3=565 Where o >= 3";
        Update update = (Update) parserManager
                .parse(new StringReader(statement));
        assertEquals("mytable", update.getTables().get(0).getName());
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

    @Test
    public void testUpdateWAlias() throws SQLParserException {
        String statement = "UPDATE table1 A SET A.columna = 'XXX' WHERE A.cod_table = 'YYY'";
        Update update = (Update) parserManager
                .parse(new StringReader(statement));
    }

    @Test
    public void testUpdateWithDeparser() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("UPDATE table1 AS A SET A.columna = 'XXX' WHERE A.cod_table = 'YYY'");
    }

    @Test
    public void testUpdateWithFrom() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("UPDATE table1 SET columna = 5 FROM table1 LEFT JOIN table2 ON col1 = col2");
    }

    @Test
    public void testUpdateMultiTable() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("UPDATE T1, T2 SET T1.C2 = T2.C2, T2.C3 = 'UPDATED' WHERE T1.C1 = T2.C1 AND T1.C2 < 10");
    }

    @Test
    public void testUpdateWithSelect() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("UPDATE NATION SET (N_NATIONKEY) = (SELECT ? FROM SYSIBM.SYSDUMMY1)");
    }

    @Test
    public void testUpdateWithSelect2() throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed("UPDATE mytable SET (col1, col2, col3) = (SELECT a, b, c FROM mytable2)");
    }
}