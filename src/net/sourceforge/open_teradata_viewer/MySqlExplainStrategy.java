/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL's default "EXPLAIN" output is tabular, spread across several
 * columns (id, select_type, table, type, key, rows, Extra, ...) with one
 * row per table/step - not a single readable text block like Teradata's
 * EXPLAIN or Oracle's DBMS_XPLAN.DISPLAY. <code>EXPLAIN FORMAT=TREE</code>
 * (MySQL 8.0.16+) is the closest match: it returns the plan as a single,
 * human-readable, indented text column - the whole plan typically comes
 * back as one row whose value contains embedded newlines, which this
 * splits into separate lines for the console.<p/>
 *
 * On MySQL versions older than 8.0.16, <code>FORMAT=TREE</code> is not
 * recognized and this fails with a clear syntax error from the server
 * rather than silently falling back to something else.
 *
 * @author D. Campione
 *
 */
public class MySqlExplainStrategy implements IExplainStrategy {

    private volatile Statement currentStatement;

    public MySqlExplainStrategy() {
    }

    @Override
    public List<String> explain(Connection connection, String sqlToExplain) throws SQLException {
        String queryToExplain = sqlToExplain.trim();
        if (queryToExplain.endsWith(";")) {
            queryToExplain = queryToExplain.substring(0, queryToExplain.length() - 1);
        }

        List<String> lines = new ArrayList<>();
        Statement statement = connection.createStatement();
        currentStatement = statement;
        try {
            ResultSet resultSet = statement.executeQuery("EXPLAIN FORMAT=TREE " + queryToExplain);
            try {
                while (resultSet.next()) {
                    String value = resultSet.getString(1);
                    if (value != null) {
                        for (String line : value.split("\r?\n")) {
                            lines.add(line);
                        }
                    }
                }
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
            currentStatement = null;
        }
        return lines;
    }

    @Override
    public void cancel() {
        Statement statement = currentStatement;
        if (statement != null) {
            try {
                statement.cancel();
            } catch (SQLException sqle) {
                ExceptionDialog.ignoreException(sqle);
            }
        }
    }
}
