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
 * H2's "EXPLAIN &lt;query&gt;" (confirmed from H2's own source,
 * <code>org.h2.command.dml.Explain</code>) returns a single row with a
 * single column named "PLAN", containing the whole plan as one text value
 * with embedded newlines - which this splits into separate lines for the
 * console.
 *
 * @author D. Campione
 *
 */
public class H2ExplainStrategy implements IExplainStrategy {

    private volatile Statement currentStatement;

    public H2ExplainStrategy() {
    }

    @Override
    public List<String> explain(Connection connection, String sqlToExplain) throws SQLException {
        String queryToExplain = sqlToExplain.trim();
        if (queryToExplain.endsWith(";")) {
            queryToExplain = queryToExplain.substring(0, queryToExplain.length() - 1);
        }

        List<String> lines = new ArrayList<String>();
        Statement statement = connection.createStatement();
        currentStatement = statement;
        try {
            ResultSet resultSet = statement.executeQuery("EXPLAIN " + queryToExplain);
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
