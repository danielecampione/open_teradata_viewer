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
 * SQL Server equivalent of Teradata's "SHOW PROCEDURE". Same mechanism as
 * {@link SQLServerShowViewValidationStrategy}: <code>OBJECT_DEFINITION</code>
 * does not need to be told what kind of object it is looking at, so this
 * is the exact same query. Returns <code>NULL</code> for an encrypted
 * procedure (<code>WITH ENCRYPTION</code>) or one implemented as a CLR
 * assembly rather than T-SQL.
 *
 * @author D. Campione
 *
 */
public class SQLServerShowProcedureValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public SQLServerShowProcedureValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String procedureName) {
        return "SELECT OBJECT_DEFINITION(OBJECT_ID('" + procedureName + "'))";
    }
}
