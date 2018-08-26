/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

/**
 * An offset clause in the form "OFFSET offset" or in the form "OFFSET offset
 * (ROW | ROWS)".
 *
 * @author D. Campione
 *
 */
public class Offset {

    private long offset;
    private boolean offsetJdbcParameter = false;
    private String offsetParam = null;

    public long getOffset() {
        return offset;
    }

    public String getOffsetParam() {
        return offsetParam;
    }

    public void setOffset(long l) {
        offset = l;
    }

    public void setOffsetParam(String s) {
        offsetParam = s;
    }

    public boolean isOffsetJdbcParameter() {
        return offsetJdbcParameter;
    }

    public void setOffsetJdbcParameter(boolean b) {
        offsetJdbcParameter = b;
    }

    @Override
    public String toString() {
        return " OFFSET " + (offsetJdbcParameter ? "?" : offset)
                + (offsetParam != null ? " " + offsetParam : "");
    }
}