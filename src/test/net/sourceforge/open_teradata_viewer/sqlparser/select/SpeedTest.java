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

package test.net.sourceforge.open_teradata_viewer.sqlparser.select;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.util.TablesNamesFinder;
import test.net.sourceforge.open_teradata_viewer.sqlparser.TestException;
import test.net.sourceforge.open_teradata_viewer.sqlparser.simpleparsing.CCSqlParserManagerTest;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SpeedTest extends TestCase {

    private final static int NUM_REPS = 500;
    private CCSqlParserManager parserManager = new CCSqlParserManager();

    public SpeedTest(String arg0) {
        super(arg0);
    }

    public void testSpeed() throws Exception {
        // all the statements in testfiles/simple_parsing.txt
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        SpeedTest.class
                                .getResourceAsStream("/res/testfiles/sqlparser/simple_parsing.txt")));
        CCSqlParserManagerTest d;
        ArrayList statementsList = new ArrayList();

        while (true) {
            String statement = CCSqlParserManagerTest.getStatement(in);
            if (statement == null) {
                break;
            }
            statementsList.add(statement);
        }
        in.close();
        in = new BufferedReader(
                new InputStreamReader(
                        SpeedTest.class
                                .getResourceAsStream("/res/testfiles/sqlparser/RUBiS-select-requests.txt")));

        // all the statements in testfiles/RUBiS-select-requests.txt
        while (true) {
            String line = CCSqlParserManagerTest.getLine(in);
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }

            if (!line.equals("#begin")) {
                break;
            }
            line = CCSqlParserManagerTest.getLine(in);
            StringBuilder buf = new StringBuilder(line);
            while (true) {
                line = CCSqlParserManagerTest.getLine(in);
                if (line.equals("#end")) {
                    break;
                }
                buf.append("\n");
                buf.append(line);
            }
            if (!CCSqlParserManagerTest.getLine(in).equals("true")) {
                continue;
            }

            statementsList.add(buf.toString());

            String cols = CCSqlParserManagerTest.getLine(in);
            String tables = CCSqlParserManagerTest.getLine(in);
            String whereCols = CCSqlParserManagerTest.getLine(in);
            String type = CCSqlParserManagerTest.getLine(in);

        }
        in.close();

        String statement = "";
        int numTests = 0;
        // it seems that the very first parsing takes a while, so I put it aside
        IStatement parsedStm = parserManager.parse(new StringReader(
                statement = (String) statementsList.get(0)));
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        ArrayList parsedSelects = new ArrayList(NUM_REPS
                * statementsList.size());
        long time = System.currentTimeMillis();

        // measure the time to parse NUM_REPS times all statements in the 2 files
        for (int i = 0; i < NUM_REPS; i++) {
            try {
                int j = 0;
                for (Iterator iter = statementsList.iterator(); iter.hasNext(); j++) {
                    statement = (String) iter.next();
                    parsedStm = parserManager
                            .parse(new StringReader(statement));
                    numTests++;
                    if (parsedStm instanceof Select) {
                        parsedSelects.add(parsedStm);
                    }

                }
            } catch (SQLParserException e) {
                throw new TestException("impossible to parse statement: "
                        + statement, e);
            }
        }
        long elapsedTime = System.currentTimeMillis() - time;
        long statementsPerSecond = numTests * 1000 / elapsedTime;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(7);
        df.setMinimumFractionDigits(4);
        System.out.println(numTests + " statements parsed in " + elapsedTime
                + " milliseconds");
        System.out.println(" (" + statementsPerSecond
                + " statements per second,  "
                + df.format(1.0 / statementsPerSecond)
                + " seconds per statement )");

        numTests = 0;
        time = System.currentTimeMillis();
        // measure the time to get the tables names from all the SELECTs parsed before
        for (Iterator iter = parsedSelects.iterator(); iter.hasNext();) {
            Select select = (Select) iter.next();
            if (select != null) {
                numTests++;
                List tableListRetr = tablesNamesFinder.getTableList(select);
            }
        }
        elapsedTime = System.currentTimeMillis() - time;
        statementsPerSecond = numTests * 1000 / elapsedTime;
        System.out.println(numTests
                + " select scans for table name executed in " + elapsedTime
                + " milliseconds");
        System.out.println(" (" + statementsPerSecond
                + " select scans for table name per second,  "
                + df.format(1.0 / statementsPerSecond)
                + " seconds per select scans for table name)");
    }
}