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
 * Shared helper used by the HSQLDB "Show" strategies
 * ({@link HSQLDBShowViewValidationStrategy}, {@link HSQLDBShowProcedureValidationStrategy})
 * to build queries against the standard SQL:2003 <code>INFORMATION_SCHEMA</code>,
 * which HyperSQL advertises broad, correct support for. Unlike the DB2 and
 * MySQL equivalents (confirmed against columns explicitly documented for
 * those products), this relies on HSQLDB's general standard-compliance
 * claim rather than a column list confirmed specifically for HSQLDB - if
 * the exact column name ever turns out to differ, the query fails with a
 * clear "column/object not found" error rather than returning something
 * silently wrong.
 * <p/>
 * There is deliberately no equivalent for tables: HSQLDB has no
 * INFORMATION_SCHEMA column (or any other single-query mechanism this
 * could confirm) that returns a table's full DDL as text.
 *
 * @author D. Campione
 *
 */
final class HSQLDBInformationSchemaQueryBuilder {

    private HSQLDBInformationSchemaQueryBuilder() {
    }

    static String buildViewDefinitionQuery(String qualifiedName) {
        String[] schemaAndName = split(qualifiedName);
        StringBuilder sql = new StringBuilder(
                "SELECT VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = '")
                .append(schemaAndName[1]).append("'");
        if (schemaAndName[0] != null) {
            sql.append(" AND TABLE_SCHEMA = '").append(schemaAndName[0]).append("'");
        }
        return sql.toString();
    }

    static String buildRoutineDefinitionQuery(String qualifiedName) {
        String[] schemaAndName = split(qualifiedName);
        StringBuilder sql = new StringBuilder("SELECT ROUTINE_DEFINITION FROM INFORMATION_SCHEMA.ROUTINES "
                + "WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_NAME = '").append(schemaAndName[1]).append("'");
        if (schemaAndName[0] != null) {
            sql.append(" AND ROUTINE_SCHEMA = '").append(schemaAndName[0]).append("'");
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
