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
 * MySQL equivalent of Teradata's "SHOW VIEW": <code>SHOW CREATE VIEW</code>.
 * The result has four columns (<code>View</code>, <code>Create View</code>,
 * <code>character_set_client</code>, <code>collation_connection</code>) -
 * the DDL is in the second one.
 *
 * @author D. Campione
 *
 */
public class MySqlShowViewValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public MySqlShowViewValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String viewName) {
        return "SHOW CREATE VIEW " + viewName;
    }

    @Override
    public int getResultColumnIndex() {
        return 2;
    }
}
