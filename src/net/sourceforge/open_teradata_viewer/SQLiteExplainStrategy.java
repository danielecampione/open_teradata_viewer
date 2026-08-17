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
 * Plain "EXPLAIN" in SQLite dumps the internal virtual-machine bytecode
 * (opcodes/operands) - not something a human would read to understand a
 * query's performance. <code>EXPLAIN QUERY PLAN</code> is the readable
 * equivalent of Teradata's EXPLAIN/Oracle's DBMS_XPLAN.DISPLAY: one row per
 * plan step (e.g. "SCAN TABLE x", "SEARCH TABLE x USING INDEX ..."). Per
 * the SQLite documentation its result always has four columns -
 * <code>selectid</code>, <code>order</code>, <code>from</code>,
 * <code>detail</code> - with the readable text in the last one, so this
 * reads column 4 rather than column 1.
 *
 * @author D. Campione
 *
 */
public class SQLiteExplainStrategy implements IExplainStrategy {

    private volatile Statement currentStatement;

    public SQLiteExplainStrategy() {
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
            ResultSet resultSet = statement.executeQuery("EXPLAIN QUERY PLAN " + queryToExplain);
            try {
                while (resultSet.next()) {
                    lines.add(resultSet.getString(4));
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
