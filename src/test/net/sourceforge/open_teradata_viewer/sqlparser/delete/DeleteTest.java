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

package test.net.sourceforge.open_teradata_viewer.sqlparser.delete;

import java.io.StringReader;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DeleteTest extends TestCase {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public DeleteTest(String arg0) {
        super(arg0);
    }

    public void testDelete() throws JSQLParserException {
        String statement = "DELETE FROM mytable WHERE mytable.col = 9";

        Delete delete = (Delete) parserManager
                .parse(new StringReader(statement));
        assertEquals("mytable", delete.getTable().getName());
        assertEquals(statement, "" + delete);
    }

    public static void main(String[] args) {
        TestRunner.run(DeleteTest.class);
    }
}