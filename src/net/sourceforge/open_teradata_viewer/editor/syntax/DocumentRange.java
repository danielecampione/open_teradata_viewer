/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

/**
 * A range of text in a document.
 *
 * @author D. Campione
 * 
 */
public class DocumentRange {

    private int startOffs;
    private int endOffs;

    public DocumentRange(int startOffs, int endOffs) {
        this.startOffs = startOffs;
        this.endOffs = endOffs;
    }

    /**
     * Gets the end offset of the range.
     *
     * @return The end offset.
     * @see #getStartOffset()
     */
    public int getEndOffset() {
        return endOffs;
    }

    /**
     * Gets the starting offset of the range.
     *
     * @return The starting offset.
     * @see #getEndOffset()
     */
    public int getStartOffset() {
        return startOffs;
    }

    /** @return A string representation of this object. */
    public String toString() {
        return "[DocumentRange: " + startOffs + "-" + endOffs + "]";
    }
}