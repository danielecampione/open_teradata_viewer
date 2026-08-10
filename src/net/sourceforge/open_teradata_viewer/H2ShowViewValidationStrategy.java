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
 * H2 equivalent of Teradata's "SHOW VIEW". Perhaps surprisingly, this is
 * the exact same statement as {@link H2ShowTableValidationStrategy}: in
 * H2's own SQL grammar, the <code>TABLE</code> clause of
 * <code>SCRIPT</code> accepts a view name just as well as a table name
 * (confirmed from H2's parser source: both resolve through the same
 * <code>readTableOrView()</code> method), and correctly produces a
 * "CREATE VIEW ..." statement instead of "CREATE TABLE ...".
 *
 * @author D. Campione
 *
 */
public class H2ShowViewValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public H2ShowViewValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String viewName) {
        return "SCRIPT NODATA NOPASSWORDS NOSETTINGS NOVERSION TABLE " + viewName;
    }
}
