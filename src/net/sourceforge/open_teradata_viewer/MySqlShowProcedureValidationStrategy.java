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
 * MySQL equivalent of Teradata's "SHOW PROCEDURE": <code>SHOW CREATE
 * PROCEDURE</code>. The result has six columns (<code>Procedure</code>,
 * <code>sql_mode</code>, <code>Create Procedure</code>,
 * <code>character_set_client</code>, <code>collation_connection</code>,
 * <code>Database Collation</code>) - the DDL is the <em>third</em> one
 * here, unlike Table/View where it is the second.<p/>
 *
 * Note: MySQL returns <code>NULL</code> for this column if the connected
 * user lacks privileges on the routine itself (SELECT on
 * <code>mysql.proc</code>/routine metadata is not enough on its own) -
 * a MySQL permission model detail, not something this query can work
 * around.
 *
 * @author D. Campione
 *
 */
public class MySqlShowProcedureValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public MySqlShowProcedureValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String procedureName) {
        return "SHOW CREATE PROCEDURE " + procedureName;
    }

    @Override
    public int getResultColumnIndex() {
        return 3;
    }
}
