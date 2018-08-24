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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WindowRange {

    private WindowOffset start;
    private WindowOffset end;

    public WindowOffset getEnd() {
        return end;
    }

    public void setEnd(WindowOffset end) {
        this.end = end;
    }

    public WindowOffset getStart() {
        return start;
    }

    public void setStart(WindowOffset start) {
        this.start = start;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" BETWEEN ");
        buffer.append(start);
        buffer.append(" AND ");
        buffer.append(end);
        return buffer.toString();
    }
}