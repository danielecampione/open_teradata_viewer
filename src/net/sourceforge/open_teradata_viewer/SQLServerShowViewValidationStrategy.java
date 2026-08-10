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
 * SQL Server equivalent of Teradata's "SHOW VIEW", via
 * <code>OBJECT_DEFINITION(OBJECT_ID(...))</code> - a single scalar
 * function call that returns the object's full T-SQL source as one value
 * (unlike the older <code>sp_helptext</code>, which splits it across
 * several ~4000-character rows). <code>OBJECT_ID</code> accepts a
 * schema-qualified <code>schema.name</code> string directly, so no
 * splitting is needed here.<p/>
 *
 * Returns <code>NULL</code> for an encrypted view (<code>WITH
 * ENCRYPTION</code>) - there is no source to retrieve in that case.
 *
 * @author D. Campione
 *
 */
public class SQLServerShowViewValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public SQLServerShowViewValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String viewName) {
        return "SELECT OBJECT_DEFINITION(OBJECT_ID('" + viewName + "'))";
    }
}
