/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2015, D. Campione
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
public class WindowElement {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Type {
        ROWS, RANGE
    }

    private Type type;
    private WindowOffset offset;
    private WindowRange range;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WindowOffset getOffset() {
        return offset;
    }

    public void setOffset(WindowOffset offset) {
        this.offset = offset;
    }

    public WindowRange getRange() {
        return range;
    }

    public void setRange(WindowRange range) {
        this.range = range;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(type.toString());

        if (offset != null) {
            buffer.append(offset.toString());
        } else if (range != null) {
            buffer.append(range.toString());
        }

        return buffer.toString();
    }
}