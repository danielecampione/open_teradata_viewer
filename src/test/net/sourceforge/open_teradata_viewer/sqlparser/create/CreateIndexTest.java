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

package test.net.sourceforge.open_teradata_viewer.sqlparser.create;

import static test.net.sourceforge.open_teradata_viewer.sqlparser.TestUtils.assertSqlCanBeParsedAndDeparsed;

import java.io.StringReader;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.index.CreateIndex;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CreateIndexTest extends TestCase {

    CCSqlParserManager parserManager = new CCSqlParserManager();

    public CreateIndexTest(String arg0) {
        super(arg0);
    }

    public void testCreateIndex() throws SQLParserException {
        String statement = "CREATE INDEX myindex ON mytab (mycol, mycol2)";
        CreateIndex createIndex = (CreateIndex) parserManager
                .parse(new StringReader(statement));
        assertEquals(2, createIndex.getIndex().getColumnsNames().size());
        assertEquals("myindex", createIndex.getIndex().getName());
        assertNull(createIndex.getIndex().getType());
        assertEquals("mytab", createIndex.getTable().getWholeTableName());
        assertEquals("mycol", createIndex.getIndex().getColumnsNames().get(0));
        assertEquals(statement, "" + createIndex);
    }

    public void testCreateIndex2() throws SQLParserException {
        String statement = "CREATE mytype INDEX myindex ON mytab (mycol, mycol2)";
        CreateIndex createIndex = (CreateIndex) parserManager
                .parse(new StringReader(statement));
        assertEquals(2, createIndex.getIndex().getColumnsNames().size());
        assertEquals("myindex", createIndex.getIndex().getName());
        assertEquals("mytype", createIndex.getIndex().getType());
        assertEquals("mytab", createIndex.getTable().getWholeTableName());
        assertEquals("mycol2", createIndex.getIndex().getColumnsNames().get(1));
        assertEquals(statement, "" + createIndex);
    }

    public void testCreateIndex3() throws SQLParserException {
        String statement = "CREATE mytype INDEX myindex ON mytab (mycol ASC, mycol2, mycol3)";
        CreateIndex createIndex = (CreateIndex) parserManager
                .parse(new StringReader(statement));
        assertEquals(3, createIndex.getIndex().getColumnsNames().size());
        assertEquals("myindex", createIndex.getIndex().getName());
        assertEquals("mytype", createIndex.getIndex().getType());
        assertEquals("mytab", createIndex.getTable().getWholeTableName());
        assertEquals("mycol3", createIndex.getIndex().getColumnsNames().get(2));
    }

    public void testCreateIndex4() throws SQLParserException {
        String statement = "CREATE mytype INDEX myindex ON mytab (mycol ASC, mycol2 (75), mycol3)";
        CreateIndex createIndex = (CreateIndex) parserManager
                .parse(new StringReader(statement));
        assertEquals(3, createIndex.getIndex().getColumnsNames().size());
        assertEquals("myindex", createIndex.getIndex().getName());
        assertEquals("mytype", createIndex.getIndex().getType());
        assertEquals("mytab", createIndex.getTable().getWholeTableName());
        assertEquals("mycol3", createIndex.getIndex().getColumnsNames().get(2));
    }

    public void testCreateIndex5() throws SQLParserException {
        String statement = "CREATE mytype INDEX myindex ON mytab (mycol ASC, mycol2 (75), mycol3) mymodifiers";
        CreateIndex createIndex = (CreateIndex) parserManager
                .parse(new StringReader(statement));
        assertEquals(3, createIndex.getIndex().getColumnsNames().size());
        assertEquals("myindex", createIndex.getIndex().getName());
        assertEquals("mytype", createIndex.getIndex().getType());
        assertEquals("mytab", createIndex.getTable().getWholeTableName());
        assertEquals("mycol3", createIndex.getIndex().getColumnsNames().get(2));
    }

    public void testCreateIndex6() throws SQLParserException {
        String stmt = "CREATE INDEX myindex ON mytab (mycol, mycol2)";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }
}