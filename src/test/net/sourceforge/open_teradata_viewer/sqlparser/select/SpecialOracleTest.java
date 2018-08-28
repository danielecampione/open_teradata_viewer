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

package test.net.sourceforge.open_teradata_viewer.sqlparser.select;

import static org.junit.Assert.assertTrue;
import static test.net.sourceforge.open_teradata_viewer.sqlparser.TestUtils.assertSqlCanBeParsedAndDeparsed;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;

/**
 * Tries to parse and deparse all statments in
 * "src\res\testfiles\sqlparser\oracle-tests".
 *
 * As a matter of fact that there are a lot of files that cannot yet be
 * processed.
 * A step by step improvement is the way to go.
 *
 * The test ensures that the successfully parsed files count doesn't diminish.
 *
 * @author D. Campione
 *
 */
public class SpecialOracleTest {

    private static final File SQLS_DIR = new File(
            "./src/res/testfiles/sqlparser/oracle-tests/");
    private static final Logger LOG = Logger
            .getLogger(SpecialOracleTest.class.getName());

    @Test
    public void testAllSqlsParseDeparse() throws IOException {
        int count = 0;
        int success = 0;
        File[] sqlTestFiles = SQLS_DIR.listFiles();

        for (File file : sqlTestFiles) {
            if (file.isFile()) {
                count++;
                LOG.log(Level.INFO, "testing {0}", file.getName());
                String sql = FileUtils.readFileToString(file);
                try {
                    assertSqlCanBeParsedAndDeparsed(sql, true);
                    success++;
                    LOG.info("   -> SUCCESS");
                } catch (SQLParserException ex) {
                    LOG.log(Level.INFO, "   -> PROBLEM {0}", ex.toString());
                } catch (Exception ex) {
                    LOG.log(Level.INFO, "   -> PROBLEM {0}", ex.toString());
                }
            }
        }

        LOG.log(Level.INFO, "tested {0} files. got {1} correct parse results",
                new Object[] { count, success });
        assertTrue(success >= 129);
    }

    @Test
    public void testAllSqlsOnlyParse() throws IOException {
        File[] sqlTestFiles = new File(SQLS_DIR, "only-parse-test").listFiles();

        for (File file : sqlTestFiles) {
            LOG.log(Level.INFO, "testing {0}", file.getName());
            String sql = FileUtils.readFileToString(file);
            try {
                CCSqlParserUtil.parse(sql);

                LOG.info("   -> SUCCESS");
            } catch (SQLParserException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}