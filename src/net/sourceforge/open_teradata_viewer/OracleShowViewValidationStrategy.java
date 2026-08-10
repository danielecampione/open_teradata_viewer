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
 * Oracle equivalent of Teradata's "SHOW VIEW", implemented via
 * <code>DBMS_METADATA.GET_DDL</code>.
 *
 * @author D. Campione
 *
 */
public class OracleShowViewValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public OracleShowViewValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String viewName) {
        return OracleDdlQueryBuilder.buildGetDdlQuery("VIEW", viewName);
    }
}
