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
 * Derby equivalent of Teradata's "SHOW VIEW", via
 * <code>SYS.SYSVIEWS.VIEWDEFINITION</code>. SYSVIEWS itself only carries a
 * TABLEID foreign key (no name column), so this joins it with
 * SYS.SYSTABLES (for the name) and SYS.SYSSCHEMAS (for the schema),
 * mirroring how Derby's own catalog is structured.<p/>
 *
 * There is deliberately no Show Table/Procedure/Explain strategy for
 * Derby:
 * <ul>
 * <li>Tables: Derby has no equivalent of VIEWDEFINITION for tables, and
 * reconstructing full DDL (columns, constraints, indexes) from the
 * catalog reliably would mean writing a parser this project cannot verify
 * against a real Derby instance.</li>
 * <li>Procedures: Derby stored procedures are always backed by an
 * external Java method (there is no SQL-bodied procedure support), so
 * there is no SQL source to show.</li>
 * <li>Explain: Derby's only human-readable mechanism, RUNTIMESTATISTICS,
 * requires actually <em>executing</em> the statement to collect its
 * numbers - unlike every other "Explain request" implementation in this
 * project, which only previews the plan. Wiring that up here would make
 * "Explain request" silently run (and, for an UPDATE/DELETE, silently
 * apply) the statement on Derby only. The non-executing alternative,
 * XPLAIN-only mode, stores its output across several SYSXPLAIN_* tables
 * that Derby's own documentation describes as too dense to be readable
 * without dedicated tooling.</li>
 * </ul>
 *
 * @author D. Campione
 *
 */
public class DerbyShowViewValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public DerbyShowViewValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String viewName) {
        String schema = null;
        String name = viewName;
        int dotIndex = viewName.lastIndexOf('.');
        if (dotIndex != -1) {
            schema = viewName.substring(0, dotIndex);
            name = viewName.substring(dotIndex + 1);
        }
        StringBuilder sql = new StringBuilder(
                "SELECT V.VIEWDEFINITION FROM SYS.SYSVIEWS V, SYS.SYSTABLES T")
                .append(schema != null && schema.trim().length() > 0 ? ", SYS.SYSSCHEMAS S" : "")
                .append(" WHERE V.TABLEID = T.TABLEID AND T.TABLENAME = '").append(name).append("'");
        if (schema != null && schema.trim().length() > 0) {
            sql.append(" AND T.SCHEMAID = S.SCHEMAID AND S.SCHEMANAME = '").append(schema).append("'");
        }
        return sql.toString();
    }
}
