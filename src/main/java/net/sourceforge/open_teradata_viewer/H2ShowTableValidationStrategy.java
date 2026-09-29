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
 * H2 equivalent of Teradata's "SHOW TABLE": <code>SCRIPT ... TABLE
 * &lt;name&gt;</code>, H2's own SQL-level statement for generating the DDL
 * needed to recreate one or more tables - confirmed directly from H2's
 * parser source (<code>Parser.parseScript()</code>), not just third-party
 * documentation. <code>NODATA</code> excludes the INSERT statements (only
 * the structure is wanted here); <code>NOPASSWORDS</code>/
 * <code>NOSETTINGS</code>/<code>NOVERSION</code> keep the output to just
 * the DDL, without the extra housekeeping statements SCRIPT normally
 * includes for a full database dump. The result is a single VARCHAR
 * column, one row per line of generated SQL.
 *
 * @author D. Campione
 *
 */
public class H2ShowTableValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public H2ShowTableValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String tableName) {
        return "SCRIPT NODATA NOPASSWORDS NOSETTINGS NOVERSION TABLE " + tableName;
    }
}
