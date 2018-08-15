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

package test.net.sourceforge.open_teradata_viewer.sqlparser.alter;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.alter.Alter;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AlterTest extends TestCase {

    public AlterTest(String arg0) {
        super(arg0);
    }

    public void testAlterTableAddColumn() throws SQLParserException {
        IStatement stmt = CCSqlParserUtil
                .parse("ALTER TABLE mytable ADD COLUMN mycolumn varchar (255)");
        assertTrue(stmt instanceof Alter);
        Alter alter = (Alter) stmt;
        assertEquals("mytable", alter.getTable().getWholeTableName());
        assertEquals("mycolumn", alter.getColumnName());
        assertEquals("varchar (255)", alter.getDataType().toString());
    }
}