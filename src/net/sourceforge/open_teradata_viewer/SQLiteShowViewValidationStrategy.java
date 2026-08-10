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
 * SQLite equivalent of Teradata's "SHOW VIEW". Same mechanism as
 * {@link SQLiteShowTableValidationStrategy}: the original "CREATE VIEW ..."
 * text is stored verbatim in <code>sqlite_master</code>.
 *
 * @author D. Campione
 *
 */
public class SQLiteShowViewValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public SQLiteShowViewValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String viewName) {
        String schema = null;
        String name = viewName;
        int dotIndex = viewName.lastIndexOf('.');
        if (dotIndex != -1) {
            schema = viewName.substring(0, dotIndex);
            name = viewName.substring(dotIndex + 1);
        }
        String master = (schema != null && schema.trim().length() > 0) ? schema + ".sqlite_master" : "sqlite_master";
        return "SELECT sql FROM " + master + " WHERE type = 'view' AND name = '" + name + "'";
    }
}
