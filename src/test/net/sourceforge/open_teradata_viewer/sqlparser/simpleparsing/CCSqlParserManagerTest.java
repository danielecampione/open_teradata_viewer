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

package test.net.sourceforge.open_teradata_viewer.sqlparser.simpleparsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import test.net.sourceforge.open_teradata_viewer.sqlparser.TestException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CCSqlParserManagerTest extends TestCase {

    public CCSqlParserManagerTest(String arg0) {
        super(arg0);
    }

    public void testParse() throws Exception {
        CCSqlParserManager parserManager = new CCSqlParserManager();
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass()
                .getResourceAsStream(
                        "/res/testfiles/sqlparser/simple_parsing.txt")));

        String statement = "";
        while (true) {
            try {
                statement = CCSqlParserManagerTest.getStatement(in);
                if (statement == null) {
                    break;
                }

                parserManager.parse(new StringReader(statement));
            } catch (SQLParserException e) {
                throw new TestException("impossible to parse statement: "
                        + statement, e);
            }
        }
    }

    public static String getStatement(BufferedReader in) throws Exception {
        StringBuilder buf = new StringBuilder();
        String line = null;
        while ((line = CCSqlParserManagerTest.getLine(in)) != null) {

            if (line.length() == 0) {
                break;
            }

            buf.append(line);
            buf.append("\n");

        }

        if (buf.length() > 0) {
            return buf.toString();
        } else {
            return null;
        }

    }

    public static String getLine(BufferedReader in) throws Exception {
        String line = null;
        while (true) {
            line = in.readLine();
            if (line != null) {
                line.trim();
                // if ((line.length() != 0) && ((line.length() < 2) || (line.length() >= 2) && !(line.charAt(0) == '/'
                // && line.charAt(1) == '/')))
                if (((line.length() < 2) || (line.length() >= 2)
                        && !(line.charAt(0) == '/' && line.charAt(1) == '/'))) {
                    break;
                }
            } else {
                break;
            }

        }

        return line;
    }
}