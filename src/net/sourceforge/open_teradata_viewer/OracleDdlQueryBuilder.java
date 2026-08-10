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
 * Shared helper used by the Oracle "Show" strategies
 * ({@link OracleShowTableValidationStrategy},
 * {@link OracleShowViewValidationStrategy},
 * {@link OracleShowProcedureValidationStrategy}) to build a
 * <code>DBMS_METADATA.GET_DDL</code> query - Oracle's standard, idiomatic
 * way to retrieve an object's DDL as text, and the closest equivalent of
 * Teradata's "SHOW &lt;TYPE&gt;" statements. Like those, it returns exactly
 * one row/one column, so it plugs into the existing Show*Action result
 * reading loop unchanged.
 *
 * @author D. Campione
 *
 */
final class OracleDdlQueryBuilder {

    private OracleDdlQueryBuilder() {
    }

    /**
     * @param objectType One of Oracle's DBMS_METADATA object types, e.g.
     *        <code>"TABLE"</code>, <code>"VIEW"</code>,
     *        <code>"PROCEDURE"</code>.
     * @param qualifiedName The object name, optionally schema-qualified
     *        (<code>"SCHEMA.OBJECT"</code>) the same way it already arrives
     *        from the Show*Action classes for Teradata.
     */
    static String buildGetDdlQuery(String objectType, String qualifiedName) {
        String schema = null;
        String name = qualifiedName;
        int dotIndex = qualifiedName.lastIndexOf('.');
        if (dotIndex != -1) {
            schema = qualifiedName.substring(0, dotIndex);
            name = qualifiedName.substring(dotIndex + 1);
        }
        StringBuilder sql = new StringBuilder("SELECT DBMS_METADATA.GET_DDL('").append(objectType).append("', '")
                .append(name).append("'");
        if (schema != null && schema.trim().length() > 0) {
            sql.append(", '").append(schema).append("'");
        }
        sql.append(") FROM DUAL");
        return sql.toString();
    }
}
