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
 * SQL Server has no single "EXPLAIN &lt;query&gt;" statement: the
 * non-executing text plan is obtained via <code>SET SHOWPLAN_TEXT
 * ON</code>, which changes how <em>every subsequent statement on the
 * connection</em> behaves (each one returns its plan instead of running)
 * until it is turned back <code>OFF</code> - confirmed from Microsoft's
 * own documentation, which explicitly warns that a statement executed
 * while it is on does not actually run.<p/>
 *
 * Since OTV keeps its connection open and reuses it for everything else
 * the user does afterwards, leaving this stuck ON would silently turn
 * every later query into a plan preview instead of running it. The
 * <code>OFF</code> statement is therefore always sent from a
 * <code>finally</code> block, independently of whether explaining the
 * query itself succeeded.<p/>
 *
 * Per SQL Server's own restriction, <code>SET SHOWPLAN_TEXT ON/OFF</code>
 * must each be the only statement in their batch - that's satisfied
 * naturally here since each is sent as its own separate JDBC call.
 *
 * @author D. Campione
 *
 */
public class SQLServerExplainStrategy implements IExplainStrategy {

    private volatile Statement currentStatement;

    public SQLServerExplainStrategy() {
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
            statement.execute("SET SHOWPLAN_TEXT ON");
            try {
                ResultSet resultSet = statement.executeQuery(queryToExplain);
                try {
                    while (resultSet.next()) {
                        lines.add(resultSet.getString(1));
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                try {
                    statement.execute("SET SHOWPLAN_TEXT OFF");
                } catch (SQLException sqle) {
                    ExceptionDialog.ignoreException(sqle);
                }
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
