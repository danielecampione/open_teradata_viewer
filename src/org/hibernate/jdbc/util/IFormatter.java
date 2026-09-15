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

package org.hibernate.jdbc.util;

/**
 * Minimal contract for pretty-printing a SQL statement. This package is
 * <b>not</b> a real Hibernate dependency: it is a small, self-contained,
 * dependency-free formatter maintained directly inside Open Teradata Viewer
 * under this package name only to stay a drop-in replacement for the type
 * originally referenced by {@code FormatSQLAction}, without pulling in the
 * whole Hibernate ORM just to reformat a SQL string.
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
