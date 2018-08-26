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

package net.sourceforge.open_teradata_viewer.sqlparser.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statements;

/**
 * Toolfunctions to start and use ISqlParser.
 * 
 * @author D. Campione
 * 
 */
public final class CCSqlParserUtil {
    public static IStatement parse(Reader statementReader)
            throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(statementReader);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    public static IStatement parse(String sql) throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(new StringReader(sql));
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    public static IStatement parse(InputStream is) throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(is);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    public static IStatement parse(InputStream is, String encoding)
            throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(is, encoding);
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    /** Parse an expression. */
    public static IExpression parseExpression(String expression)
            throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(new StringReader(expression));
        try {
            return parser.SimpleExpression();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    /**
     * Parse a conditional expression. This is the expression after a where
     * clause. 
     */
    public static IExpression parseCondExpression(String condExpr)
            throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(new StringReader(condExpr));
        try {
            return parser.Expression();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    /** Parse a statement list. */
    public static Statements parseStatements(String sqls)
            throws SQLParserException {
        CCSqlParser parser = new CCSqlParser(new StringReader(sqls));
        try {
            return parser.Statements();
        } catch (Exception ex) {
            throw new SQLParserException(ex);
        }
    }

    private CCSqlParserUtil() {
    }
}