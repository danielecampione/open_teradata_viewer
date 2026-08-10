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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Teradata's native "EXPLAIN" statement, which directly returns the
 * execution plan as a result set (one text column, one row per plan line).
 *
 * @author D. Campione
 *
 */
public class TeradataExplainStrategy implements IExplainStrategy {

    private volatile PreparedStatement currentStatement;

    public TeradataExplainStrategy() {
    }

    @Override
    public List<String> explain(Connection connection, String sqlToExplain) throws SQLException {
        String sqlQuery = "EXPLAIN " + sqlToExplain + ";";
        List<String> lines = new ArrayList<String>();
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        currentStatement = statement;
        try {
            ResultSet resultSet = statement.executeQuery();
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
        PreparedStatement statement = currentStatement;
        if (statement != null) {
            try {
                statement.cancel();
            } catch (SQLException sqle) {
                ExceptionDialog.ignoreException(sqle);
            }
        }
    }
}
