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
 * Represents the understood styles of SQL formatting. This mirrors the
 * small, well known three-way shape (basic DML, DDL, no-op): only
 * {@link #BASIC} is currently used by Open Teradata Viewer
 * ({@code FormatSQLAction}), {@link #DDL} and {@link #NONE} are provided
 * for completeness and possible future menu entries (e.g. a dedicated
 * "Format DDL" action).
 *
 * @author D. Campione
 *
 */
public final class FormatStyle {

    public static final FormatStyle BASIC = new FormatStyle("basic", new IFormatter() {
        @Override
        public String format(String source) {
            return SqlFormatterSupport.formatBasic(source);
        }
    });

    public static final FormatStyle DDL = new FormatStyle("ddl", new IFormatter() {
        @Override
        public String format(String source) {
            return SqlFormatterSupport.formatDdl(source);
        }
    });

    public static final FormatStyle NONE = new FormatStyle("none", new IFormatter() {
        @Override
        public String format(String source) {
            return source == null ? "" : source;
        }
    });

    private final String name;
    private final IFormatter formatter;

    private FormatStyle(String name, IFormatter formatter) {
        this.name = name;
        this.formatter = formatter;
    }

    public String getName() {
        return name;
    }

    public IFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String toString() {
        return name;
    }
}
