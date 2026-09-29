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
 * DB2 equivalent of Teradata's "SHOW PROCEDURE": reads the procedure's
 * defining SQL text straight from <code>SYSCAT.ROUTINES</code>.<p/>
 *
 * Note: this only works for native SQL PL procedures. It returns nothing
 * for external (Java/C) procedures - DB2 does not store their source in
 * the catalog at all - and returns an encoded, unreadable string for
 * procedures that were deliberately created with an obfuscated body.
 *
 * @author D. Campione
 *
 */
public class DB2ShowProcedureValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public DB2ShowProcedureValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String procedureName) {
        return Db2CatalogQueryBuilder.buildProcedureTextQuery(procedureName);
    }
}
