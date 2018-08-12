/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Allows iterating over a portion of an <code>OTVDocument</code>. This is of
 * course not thread-safe, so should only be used on the EDT or with external
 * synchronization.
 * 
 * @author D. Campione
 * 
 */
public class OTVDocumentCharSequence implements CharSequence {

    private OTVDocument doc;
    private int start;
    private int end;

    /**
     * Creates a <code>CharSequence</code> representing the text in a document
     * from the specified offset to the end of that document.
     *
     * @param doc The document.
     * @param start The starting offset in the document, inclusive.
     */
    public OTVDocumentCharSequence(OTVDocument doc, int start) {
        this(doc, start, doc.getLength());
    }

    /**
     * Ctor.
     *
     * @param doc The document.
     * @param start The starting offset in the document, inclusive.
     * @param end the ending offset in the document, exclusive.
     */
    public OTVDocumentCharSequence(OTVDocument doc, int start, int end) {
        this.doc = doc;
        this.start = start;
        this.end = end;
    }

    /** {@inheritDoc} */
    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Index " + index
                    + " is not in range [0-" + length() + ")");
        }
        try {
            return doc.charAt(start + index);
        } catch (BadLocationException ble) {
            throw new IndexOutOfBoundsException(ble.toString());
        }
    }

    /** {@inheritDoc} */
    public int length() {
        return end - start;
    }

    /** {@inheritDoc} */
    public CharSequence subSequence(int start, int end) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start must be >= 0 (" + start
                    + ")");
        } else if (end < 0) {
            throw new IndexOutOfBoundsException("end must be >= 0 (" + end
                    + ")");
        } else if (end > length()) {
            throw new IndexOutOfBoundsException("end must be <= " + length()
                    + " (" + end + ")");
        } else if (start > end) {
            throw new IndexOutOfBoundsException("start (" + start
                    + ") cannot be > end (" + end + ")");
        }
        int newStart = this.start + start;
        int newEnd = this.start + end;
        return new OTVDocumentCharSequence(doc, newStart, newEnd);
    }

    /** {@inheritDoc} */
    public String toString() {
        try {
            return doc.getText(start, length());
        } catch (BadLocationException ble) { // Never happens
            ExceptionDialog.hideException(ble);
            return "";
        }
    }
}