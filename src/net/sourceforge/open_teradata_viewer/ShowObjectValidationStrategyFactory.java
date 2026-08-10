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
 * Picks the {@link IShowObjectValidationStrategy} appropriate to the
 * currently connected database's SQL dialect.<p/>
 *
 * This is a plain static dispatcher rather than a classic GoF Factory
 * Method (like {@link ColumnsNameDiscovererHandlerFactoryMethod}): with two
 * independent axes (database type x object type) and several combinations
 * that are simply not supported, one small switch here is easier to read
 * and to extend than a matrix of creator subclasses.
 *
 * @author D. Campione
 *
 */
public abstract class ShowObjectValidationStrategyFactory {

    /**
     * @return The strategy to use, or <code>null</code> if this
     *         combination of object type and database type is not (yet)
     *         supported - callers must handle this by informing the user
     *         instead of running a query.
     */
    public static IShowObjectValidationStrategy getStrategy(ShowObjectType objectType, DatabaseType databaseType) {
        if (databaseType == null) {
            return null;
        }
        switch (databaseType) {
        case TERADATA:
            switch (objectType) {
            case TABLE:
                return new TeradataShowTableValidationStrategy();
            case VIEW:
                return new TeradataShowViewValidationStrategy();
            case PROCEDURE:
                return new TeradataShowProcedureValidationStrategy();
            case MACRO:
                return new TeradataShowMacroValidationStrategy();
            }
            break;
        case ORACLE:
            switch (objectType) {
            case TABLE:
                return new OracleShowTableValidationStrategy();
            case VIEW:
                return new OracleShowViewValidationStrategy();
            case PROCEDURE:
                return new OracleShowProcedureValidationStrategy();
            case MACRO:
                return null; // Oracle has no macro object type
            }
            break;
        case DB2:
            switch (objectType) {
            case VIEW:
                return new DB2ShowViewValidationStrategy();
            case PROCEDURE:
                return new DB2ShowProcedureValidationStrategy();
            case TABLE:
                // DB2 has no single built-in function that returns full
                // table DDL as text (unlike Oracle's DBMS_METADATA.GET_DDL).
                // The official tool, db2look, is command-line only - see
                // Db2CatalogQueryBuilder for details.
                return null;
            case MACRO:
                return null; // DB2 has no macro object type
            }
            break;
        case MYSQL:
            switch (objectType) {
            case TABLE:
                return new MySqlShowTableValidationStrategy();
            case VIEW:
                return new MySqlShowViewValidationStrategy();
            case PROCEDURE:
                return new MySqlShowProcedureValidationStrategy();
            case MACRO:
                return null; // MySQL has no macro object type
            }
            break;
        case SQLITE:
            switch (objectType) {
            case TABLE:
                return new SQLiteShowTableValidationStrategy();
            case VIEW:
                return new SQLiteShowViewValidationStrategy();
            case PROCEDURE:
                // SQLite has no server-side stored procedures at all - it
                // is a deliberately minimal embedded engine with no
                // procedural language support, not a missing feature here.
                return null;
            case MACRO:
                return null; // SQLite has no macro object type
            }
            break;
        case HSQLDB:
            switch (objectType) {
            case VIEW:
                return new HSQLDBShowViewValidationStrategy();
            case PROCEDURE:
                return new HSQLDBShowProcedureValidationStrategy();
            case TABLE:
                // No confirmed single-query mechanism for full table DDL
                // in HSQLDB - see HSQLDBInformationSchemaQueryBuilder.
                return null;
            case MACRO:
                return null; // HSQLDB has no macro object type
            }
            break;
        case H2:
            switch (objectType) {
            case TABLE:
                return new H2ShowTableValidationStrategy();
            case VIEW:
                return new H2ShowViewValidationStrategy();
            case PROCEDURE:
                // H2 has no native SQL stored procedures - only
                // Java-backed "CREATE ALIAS" functions, which have no SQL
                // body to show (confirmed: no PROCEDURE keyword in H2's
                // parser at all).
                return null;
            case MACRO:
                return null; // H2 has no macro object type
            }
            break;
        case APACHE_DERBY:
            switch (objectType) {
            case VIEW:
                return new DerbyShowViewValidationStrategy();
            case TABLE:
                // No reliable single-query DDL reconstruction for tables
                // in Derby (confirmed independently by third-party Derby
                // tooling too) - see DerbyShowViewValidationStrategy.
                return null;
            case PROCEDURE:
                // Derby stored procedures are always backed by an
                // external Java method - there is no SQL source to show.
                return null;
            case MACRO:
                return null; // Derby has no macro object type
            }
            break;
        case SQL_SERVER:
            switch (objectType) {
            case VIEW:
                return new SQLServerShowViewValidationStrategy();
            case PROCEDURE:
                return new SQLServerShowProcedureValidationStrategy();
            case TABLE:
                // No single built-in function returns full table DDL as
                // text in SQL Server either (same situation as DB2/HSQLDB/
                // Derby) - SSMS's "Script Table as CREATE" is a GUI/tool
                // feature, not something callable in plain T-SQL.
                return null;
            case MACRO:
                return null; // SQL Server has no macro object type
            }
            break;
        default:
            // Unknown: not implemented (there is nothing to implement -
            // by definition the database type could not be determined).
            // Returning null (rather than guessing at untested syntax)
            // makes the caller show a clear message instead of running a
            // query that would silently return the wrong thing or blow
            // up with a cryptic driver error.
            break;
        }
        return null;
    }
}
