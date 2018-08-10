/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;
import javax.swing.text.PlainDocument;

/**
 * The document implementation used by instances of <code>TextArea</code>.
 *
 * @author D. Campione
 * 
 */
public class OTVDocument extends PlainDocument {

    private static final long serialVersionUID = -7088950354258624396L;

    /** Ctor. */
    public OTVDocument() {
        super(new OTVGapContent());
    }

    /**
     * Returns the character in the document at the specified offset.
     *
     * @param offset The offset of the character.
     * @return The character.
     * @throws BadLocationException If the offset is invalid.
     */
    public char charAt(int offset) throws BadLocationException {
        return ((OTVGapContent) getContent()).charAt(offset);
    }

    /** Document content that provides fast access to individual characters. */
    private static class OTVGapContent extends GapContent {

        private static final long serialVersionUID = -2709015317096581646L;

        public char charAt(int offset) throws BadLocationException {
            if (offset < 0 || offset >= length()) {
                throw new BadLocationException("Invalid offset", offset);
            }
            int g0 = getGapStart();
            char[] array = (char[]) getArray();
            if (offset < g0) { // Below gap
                return array[offset];
            }
            return array[getGapEnd() + offset - g0]; // Above gap
        }
    }
}