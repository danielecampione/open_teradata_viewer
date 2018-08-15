/*
 * Open Teradata Viewer ( formatter )
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

package org.hibernate.jdbc.util;

import java.util.StringTokenizer;

/**
 * Performs formatting of DDL SQL statements.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * 
 */
public class DDLFormatterImpl implements IFormatter {

    /**
     * Format an SQL statement using simple rules<ul>
     * <li>Insert newline after each comma</li>
     * <li>Indent three spaces after each inserted newline</li>
     * </ul>
     * If the statement contains single/double quotes return unchanged, it is
     * too complex and could be broken by simple formatting.
     * 
     * @param sql The statement to be fornmatted.
     */
    @Override
    public String format(String sql) {
        if (sql.toLowerCase().startsWith("create table")) {
            return formatCreateTable(sql);
        } else if (sql.toLowerCase().startsWith("alter table")) {
            return formatAlterTable(sql);
        } else if (sql.toLowerCase().startsWith("comment on")) {
            return formatCommentOn(sql);
        } else {
            return "\n    " + sql;
        }
    }

    private String formatCommentOn(String sql) {
        StringBuilder result = new StringBuilder(60).append("\n    ");
        StringTokenizer tokens = new StringTokenizer(sql, " '[]\"", true);

        boolean quoted = false;
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            result.append(token);
            if (isQuote(token)) {
                quoted = !quoted;
            } else if (!quoted) {
                if ("is".equals(token)) {
                    result.append("\n       ");
                }
            }
        }

        return result.toString();
    }

    private String formatAlterTable(String sql) {
        StringBuilder result = new StringBuilder(60).append("\n    ");
        StringTokenizer tokens = new StringTokenizer(sql, " (,)'[]\"", true);

        boolean quoted = false;
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (isQuote(token)) {
                quoted = !quoted;
            } else if (!quoted) {
                if (isBreak(token)) {
                    result.append("\n        ");
                }
            }
            result.append(token);
        }

        return result.toString();
    }

    private String formatCreateTable(String sql) {
        StringBuilder result = new StringBuilder(60).append("\n    ");
        StringTokenizer tokens = new StringTokenizer(sql, "(,)'[]\"", true);

        int depth = 0;
        boolean quoted = false;
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (isQuote(token)) {
                quoted = !quoted;
                result.append(token);
            } else if (quoted) {
                result.append(token);
            } else {
                if (")".equals(token)) {
                    depth--;
                    if (depth == 0) {
                        result.append("\n    ");
                    }
                }
                result.append(token);
                if (",".equals(token) && depth == 1) {
                    result.append("\n       ");
                }
                if ("(".equals(token)) {
                    depth++;
                    if (depth == 1) {
                        result.append("\n        ");
                    }
                }
            }
        }

        return result.toString();
    }

    private static boolean isBreak(String token) {
        return "drop".equals(token) || "add".equals(token)
                || "references".equals(token) || "foreign".equals(token)
                || "on".equals(token);
    }

    private static boolean isQuote(String tok) {
        return "\"".equals(tok) || "`".equals(tok) || "]".equals(tok)
                || "[".equals(tok) || "'".equals(tok);
    }
}