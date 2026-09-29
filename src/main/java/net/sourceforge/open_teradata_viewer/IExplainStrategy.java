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
import java.sql.SQLException;
import java.util.List;

/**
 * Obtains the execution plan for a SQL statement. Unlike the "Show"
 * strategies, this cannot always be reduced to a single SQL string: some
 * databases (Oracle) require more than one statement to produce a plan, so
 * implementations run whatever JDBC calls they need themselves.
 *
 * @author D. Campione
 *
 */
public interface IExplainStrategy {

    /**
     * @return The execution plan, one line per result row - already
     *         trimmed, ready to be printed to the console.
     */
    List<String> explain(Connection connection, String sqlToExplain) throws SQLException;

    /**
     * Cancels the statement currently being executed by {@link #explain},
     * if one is in flight. Mirrors {@link java.sql.Statement#cancel()} and
     * is safe to call from another thread (e.g. from a "Cancel" button).
     */
    void cancel();
}
