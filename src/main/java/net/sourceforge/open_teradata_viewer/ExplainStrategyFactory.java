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

import net.sourceforge.open_teradata_viewer.ConnectionData.DatabaseType;

/**
 * Picks the {@link IExplainStrategy} appropriate to the currently
 * connected database's SQL dialect. See
 * {@link ShowObjectValidationStrategyFactory} for why this is a plain
 * static dispatcher rather than a classic GoF Factory Method.
 *
 * @author D. Campione
 *
 */
public abstract class ExplainStrategyFactory {

    /**
     * @return The strategy to use, or <code>null</code> if explaining a
     *         query is not (yet) supported for this database type -
     *         callers must handle this by informing the user instead of
     *         running a query.
     */
    public static IExplainStrategy getStrategy(DatabaseType databaseType) {
        if (databaseType == null) {
            return null;
        }
        switch (databaseType) {
        case TERADATA:
            return new TeradataExplainStrategy();
        case ORACLE:
            return new OracleExplainStrategy();
        case MYSQL:
            return new MySqlExplainStrategy();
        case SQLITE:
            return new SQLiteExplainStrategy();
        case HSQLDB:
            return new HSQLDBExplainStrategy();
        case H2:
            return new H2ExplainStrategy();
        case DB2:
            // DB2 has no single built-in function that returns a readable
            // execution plan like Oracle's DBMS_XPLAN.DISPLAY: the
            // official tools (db2exfmt/db2expln) are command-line only,
            // and the underlying EXPLAIN_* tables are a complex, version-
            // dependent multi-table structure not worth guessing at
            // without a real DB2 instance to verify against.
            return null;
        case APACHE_DERBY:
            // Deliberately not implemented, not just missing: Derby's only
            // human-readable mechanism (RUNTIMESTATISTICS) requires
            // actually executing the statement to collect its numbers -
            // unlike every other database here, where "Explain request"
            // only previews the plan. Wiring it up would make this action
            // silently run (and, for an UPDATE/DELETE, silently apply) the
            // statement on Derby only. See DerbyShowViewValidationStrategy
            // for the full reasoning, including why the non-executing
            // alternative (XPLAIN-only mode) isn't a good substitute either.
            return null;
        case SQL_SERVER:
            return new SQLServerExplainStrategy();
        default:
            // Unknown: not implemented (there is nothing to implement -
            // by definition the database type could not be determined).
            return null;
        }
    }
}
