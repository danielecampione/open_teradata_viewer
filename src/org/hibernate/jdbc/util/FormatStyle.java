/*
 * Open Teradata Viewer ( formatter )
 * Copyright (C) 2012, D. Campione
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
 * Represents the the understood types or styles of formatting. 
 *
 * @author Steve Ebersole
 * 
 */
public class FormatStyle {
    public static final FormatStyle BASIC = new FormatStyle("basic",
            new BasicFormatterImpl());
    public static final FormatStyle DDL = new FormatStyle("ddl",
            new DDLFormatterImpl());
    public static final FormatStyle NONE = new FormatStyle("none",
            new NoFormatImpl());

    private final String name;
    private final Formatter formatter;

    private FormatStyle(String name, Formatter formatter) {
        this.name = name;
        this.formatter = formatter;
    }

    public String getName() {
        return name;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FormatStyle that = (FormatStyle) o;

        return name.equals(that.name);

    }

    public int hashCode() {
        return name.hashCode();
    }

    private static class NoFormatImpl implements Formatter {
        public String format(String source) {
            return source;
        }
    }
}
