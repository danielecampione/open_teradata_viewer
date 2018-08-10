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

package test.net.sourceforge.open_teradata_viewer.truncate;

import java.io.StringReader;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.truncate.Truncate;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TruncateTest extends TestCase {

    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public TruncateTest(String arg0) {
        super(arg0);
    }

    public void testTruncate() throws Exception {
        String statement = "TRUncATE TABLE myschema.mytab";
        Truncate truncate = (Truncate) parserManager.parse(new StringReader(
                statement));
        assertEquals("myschema", truncate.getTable().getSchemaName());
        assertEquals("myschema.mytab", truncate.getTable().getWholeTableName());
        assertEquals(statement.toUpperCase(), truncate.toString().toUpperCase());

        statement = "TRUncATE   TABLE    mytab";
        String toStringStatement = "TRUncATE TABLE mytab";
        truncate = (Truncate) parserManager.parse(new StringReader(statement));
        assertEquals("mytab", truncate.getTable().getName());
        assertEquals(toStringStatement.toUpperCase(), truncate.toString()
                .toUpperCase());
    }

    public static void main(String[] args) {
        TestRunner.run(TruncateTest.class);
    }

}
