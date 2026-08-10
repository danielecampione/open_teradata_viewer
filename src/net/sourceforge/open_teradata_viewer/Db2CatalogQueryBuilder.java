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

/**
 * Shared helper used by the DB2 "Show" strategies
 * ({@link DB2ShowViewValidationStrategy}, {@link DB2ShowProcedureValidationStrategy})
 * to build queries against DB2's system catalog. Unlike Oracle, DB2 has no
 * single built-in function that returns an object's full DDL as text for
 * every object type: the official tool for that is <code>db2look</code>,
 * a command-line utility that cannot be invoked over JDBC (this is why
 * there is no <code>DB2ShowTableValidationStrategy</code>: table DDL is
 * assembled by db2look from several catalog tables - columns, keys,
 * indexes, tablespaces - and reconstructing that reliably without a real
 * DB2 instance to verify against would risk producing subtly wrong DDL).
 * <p/>
 * Views and (SQL-bodied) procedures are simpler: DB2 stores their defining
 * SQL text verbatim in the catalog, so it can be read back directly.
 *
 * @author D. Campione
 *
 */
final class Db2CatalogQueryBuilder {

    private Db2CatalogQueryBuilder() {
    }

    /**
     * SYSCAT.VIEWS.TEXT holds the view's defining SQL text. Long
     * definitions are split across several rows (one per SEQNO), which is
     * why this is ordered rather than expected to be a single row - the
     * existing Show*Action result-reading loop already concatenates every
     * row it gets back, so this plugs in unchanged.
     */
    static String buildViewTextQuery(String qualifiedName) {
        String[] schemaAndName = split(qualifiedName);
        StringBuilder sql = new StringBuilder("SELECT TEXT FROM SYSCAT.VIEWS WHERE VIEWNAME = '")
                .append(schemaAndName[1]).append("'");
        if (schemaAndName[0] != null) {
            sql.append(" AND VIEWSCHEMA = '").append(schemaAndName[0]).append("'");
        }
        sql.append(" ORDER BY SEQNO");
        return sql.toString();
    }

    /**
     * SYSCAT.ROUTINES.TEXT holds the full "CREATE PROCEDURE" text, but
     * only for native SQL PL procedures - it is <code>NULL</code> for
     * external (Java/C) procedures, and contains an encoded string rather
     * than readable SQL for procedures created with an obfuscated body.
     * Both are DB2 catalog limitations, not something this query can work
     * around.
     */
    static String buildProcedureTextQuery(String qualifiedName) {
        String[] schemaAndName = split(qualifiedName);
        StringBuilder sql = new StringBuilder(
                "SELECT TEXT FROM SYSCAT.ROUTINES WHERE ROUTINETYPE = 'P' AND ROUTINENAME = '")
                .append(schemaAndName[1]).append("'");
        if (schemaAndName[0] != null) {
            sql.append(" AND ROUTINESCHEMA = '").append(schemaAndName[0]).append("'");
        }
        return sql.toString();
    }

    private static String[] split(String qualifiedName) {
        int dotIndex = qualifiedName.lastIndexOf('.');
        if (dotIndex == -1) {
            return new String[]{null, qualifiedName};
        }
        return new String[]{qualifiedName.substring(0, dotIndex), qualifiedName.substring(dotIndex + 1)};
    }
}
