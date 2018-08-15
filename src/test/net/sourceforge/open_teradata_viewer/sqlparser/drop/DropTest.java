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

package test.net.sourceforge.open_teradata_viewer.sqlparser.drop;

import java.io.StringReader;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.drop.Drop;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DropTest extends TestCase {

    CCSqlParserManager parserManager = new CCSqlParserManager();

    public DropTest(String arg0) {
        super(arg0);
    }

    public void testDrop() throws SQLParserException {
        String statement = "DROP TABLE mytab";
        Drop drop = (Drop) parserManager.parse(new StringReader(statement));
        assertEquals("TABLE", drop.getType());
        assertEquals("mytab", drop.getName());
        assertEquals(statement, "" + drop);

        statement = "DROP INDEX myindex CASCADE";
        drop = (Drop) parserManager.parse(new StringReader(statement));
        assertEquals("INDEX", drop.getType());
        assertEquals("myindex", drop.getName());
        assertEquals("CASCADE", drop.getParameters().get(0));
        assertEquals(statement, "" + drop);
    }
}