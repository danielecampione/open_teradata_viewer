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
 * Oracle has no single statement that returns an execution plan directly
 * like Teradata's "EXPLAIN": it takes two steps -
 * <code>EXPLAIN PLAN FOR &lt;query&gt;</code> populates <code>PLAN_TABLE</code>
 * and returns no result set, then a separate
 * <code>SELECT ... FROM TABLE(DBMS_XPLAN.DISPLAY)</code> reads the plan
 * back as text. Both steps run on the same {@link Statement} so a single
 * {@link #cancel()} call covers whichever one is in flight.
 *
 * @author D. Campione
 *
 */
public class OracleExplainStrategy implements IExplainStrategy {

    private volatile Statement currentStatement;

    public OracleExplainStrategy() {
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
            statement.execute("EXPLAIN PLAN FOR " + queryToExplain);
            ResultSet resultSet = statement.executeQuery("SELECT PLAN_TABLE_OUTPUT FROM TABLE(DBMS_XPLAN.DISPLAY)");
            try {
                while (resultSet.next()) {
                    lines.add(resultSet.getString(1).trim());
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
