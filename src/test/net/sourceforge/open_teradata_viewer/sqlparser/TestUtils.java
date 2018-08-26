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

package test.net.sourceforge.open_teradata_viewer.sqlparser;

import static junit.framework.Assert.assertEquals;

import java.io.StringReader;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.util.deparser.ExpressionDeParser;
import net.sourceforge.open_teradata_viewer.sqlparser.util.deparser.SelectDeParser;
import net.sourceforge.open_teradata_viewer.sqlparser.util.deparser.StatementDeParser;

import org.junit.Test;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TestUtils {

    public static void assertSqlCanBeParsedAndDeparsed(String statement)
            throws SQLParserException {
        assertSqlCanBeParsedAndDeparsed(statement, false);
    }

    /**
     * Tries to parse and deparse the given statement.
     *
     * @param laxDeparsingCheck Removes all linefeeds from the original and
     *        removes all double spaces. The check is case-insensitive.
     */
    public static void assertSqlCanBeParsedAndDeparsed(String statement,
            boolean laxDeparsingCheck) throws SQLParserException {
        IStatement parsed = CCSqlParserUtil.parse(new StringReader(statement));
        assertStatementCanBeDeparsedAs(parsed, statement, laxDeparsingCheck);
    }

    public static void assertStatementCanBeDeparsedAs(IStatement parsed,
            String statement) {
        assertStatementCanBeDeparsedAs(parsed, statement, false);
    }

    public static void assertStatementCanBeDeparsedAs(IStatement parsed,
            String statement, boolean laxDeparsingCheck) {
        assertEquals(buildSqlString(statement, laxDeparsingCheck),
                buildSqlString(parsed.toString(), laxDeparsingCheck));

        StatementDeParser deParser = new StatementDeParser(new StringBuilder());
        parsed.accept(deParser);
        assertEquals(
                buildSqlString(statement, laxDeparsingCheck),
                buildSqlString(deParser.getBuffer().toString(),
                        laxDeparsingCheck));
    }

    public static String buildSqlString(String sql, boolean laxDeparsingCheck) {
        if (laxDeparsingCheck) {
            return sql.replaceAll("\\s", " ").replaceAll("\\s+", " ")
                    .replaceAll("\\s*([/,()=+\\-*|\\]<>])\\s*", "$1")
                    .toLowerCase().trim();
        } else {
            return sql;
        }
    }

    @Test
    public void testBuildSqlString() {
        assertEquals("select col from test",
                buildSqlString("   SELECT   col FROM  \r\n \t  TEST \n", true));
        assertEquals("select  col  from test",
                buildSqlString("select  col  from test", false));
    }

    public static void assertExpressionCanBeDeparsedAs(
            final IExpression parsed, String expression) {
        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        StringBuilder stringBuilder = new StringBuilder();
        expressionDeParser.setBuffer(stringBuilder);
        SelectDeParser selectDeParser = new SelectDeParser(expressionDeParser,
                stringBuilder);
        expressionDeParser.setSelectVisitor(selectDeParser);
        parsed.accept(expressionDeParser);

        assertEquals(expression, stringBuilder.toString());
    }
}