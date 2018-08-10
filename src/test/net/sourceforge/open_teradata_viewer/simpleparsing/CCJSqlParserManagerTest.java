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

package test.net.sourceforge.open_teradata_viewer.simpleparsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import test.net.sourceforge.open_teradata_viewer.TestException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CCJSqlParserManagerTest extends TestCase {

    public CCJSqlParserManagerTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        TestRunner.run(CCJSqlParserManagerTest.class);
    }

    @SuppressWarnings("unused")
    public void testParse() throws Exception {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        BufferedReader in = new BufferedReader(new FileReader("testfiles"
                + File.separator + "simple_parsing.txt"));
        String statement = "";
        while (true) {
            try {
                statement = CCJSqlParserManagerTest.getStatement(in);
                if (statement == null)
                    break;

                Statement parsedStm = parserManager.parse(new StringReader(
                        statement));
                //System.out.println(statement);
            } catch (JSQLParserException e) {
                throw new TestException("impossible to parse statement: "
                        + statement, e);
            }
        }
    }

    public static String getStatement(BufferedReader in) throws Exception {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while ((line = CCJSqlParserManagerTest.getLine(in)) != null) {

            if (line.length() == 0)
                break;

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
                //				if ((line.length() != 0) && ((line.length() < 2) ||  (line.length() >= 2) && !(line.charAt(0) == '/' && line.charAt(1) == '/')))
                if (((line.length() < 2) || (line.length() >= 2)
                        && !(line.charAt(0) == '/' && line.charAt(1) == '/')))
                    break;
            } else {
                break;
            }

        }

        return line;
    }

}
