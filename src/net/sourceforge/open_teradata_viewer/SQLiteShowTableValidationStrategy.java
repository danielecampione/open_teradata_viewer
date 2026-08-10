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
 * SQLite equivalent of Teradata's "SHOW TABLE". Unlike every other
 * database handled so far, this needs no reconstruction at all: SQLite
 * stores the exact original "CREATE TABLE ..." text, verbatim, in its own
 * <code>sqlite_master</code> catalog table.
 *
 * @author D. Campione
 *
 */
public class SQLiteShowTableValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public SQLiteShowTableValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String tableName) {
        String schema = null;
        String name = tableName;
        int dotIndex = tableName.lastIndexOf('.');
        if (dotIndex != -1) {
            schema = tableName.substring(0, dotIndex);
            name = tableName.substring(dotIndex + 1);
        }
        // A schema prefix here means an ATTACHed database alias (SQLite has
        // no multi-schema concept within a single database file), each of
        // which has its own sqlite_master.
        String master = (schema != null && schema.trim().length() > 0) ? schema + ".sqlite_master" : "sqlite_master";
        return "SELECT sql FROM " + master + " WHERE type = 'table' AND name = '" + name + "'";
    }
}
