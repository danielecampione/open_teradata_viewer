/*
 * Open Teradata Viewer ( sql formatter )
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

package net.sourceforge.open_teradata_viewer.util.sql_formatter;

/**
 * Minimal contract for pretty-printing a SQL statement.
 *
 * @author D. Campione
 *
 */
public interface IFormatter {

    /**
     * Formats the given SQL source.
     *
     * @param source The raw SQL statement (or script) to format.
     * @return The formatted SQL, never <code>null</code> (an empty or
     *         <code>null</code> input yields an empty string).
     */
    String format(String source);
}
