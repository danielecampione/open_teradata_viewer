/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

/**
 * A range of text in a document.
 *
 * @author D. Campione
 * 
 */
public class DocumentRange implements Comparable<DocumentRange> {

    private int startOffs;
    private int endOffs;

    /**
     * Ctor.
     *
     * @param startOffs The starting offset in the document, inclusive.
     * @param endOffs The ending offset in the document, exclusive.
     * @throws IllegalArgumentException If <code>endOffs</code> is less than
     *         <code>startOffs</code>, or either argument is less than zero.
     */
    public DocumentRange(int startOffs, int endOffs) {
        set(startOffs, endOffs);
    }

    /**
     * Compares this document range to another.
     *
     * @param other Another document range.
     * @return How the two should be sorted relative to each other.
     */
    @Override
    public int compareTo(DocumentRange other) {
        if (other == null) {
            return 1;
        }
        int diff = startOffs - other.startOffs;
        if (diff != 0) {
            return diff;
        }
        return endOffs - other.endOffs;
    }

    /**
     * Returns whether this document range is equal to another one.
     *
     * @param other Another object, presumably a document range.
     * @return Whether <code>other</code> is also a document range, and equal to
     *         this one.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof DocumentRange) {
            return this.compareTo((DocumentRange) other) == 0;
        }
        return false;
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

    /**
     * Overridden simply as a best practice, since {@link #equals(Object)} is
     * overridden.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return startOffs + endOffs;
    }

    /**
     * Returns whether this document range has zero length. This can happen, for
     * example, with regex searches of forms like <code>"foo|"</code>, where the
     * right-hand sub-expression matches empty strings.
     *
     * @return Whether this document range has zero length.
     */
    public boolean isZeroLength() {
        return startOffs == endOffs;
    }

    /**
     * Sets the document range.
     *
     * @param start The new start value, inclusive.
     * @param end The new end value, exclusive.
     * @throws IllegalArgumentException If <code>end</code> is less than
     *         <code>start</code>, or either argument is less than zero.
     */
    public void set(int start, int end) {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("start and end must be >= 0");
        }
        if (end < start) {
            throw new IllegalArgumentException(
                    "'end' cannot be less than 'start'");
        }
        this.startOffs = start;
        this.endOffs = end;
    }

    /** @return A string representation of this object. */
    @Override
    public String toString() {
        return "[DocumentRange: " + startOffs + "-" + endOffs + "]";
    }

    /**
     * Translates this document range by a given amount.
     *
     * @param amount The amount to translate this range by.
     * @return This (modified) range.
     */
    public DocumentRange translate(int amount) {
        startOffs += amount;
        endOffs += amount;
        return this;
    }
}