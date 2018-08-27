/*
 * Open Teradata Viewer ( editor )
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

package test.net.sourceforge.open_teradata_viewer.editor;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.editor.OTVDocument;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link OTVDocument}.
 *
 * @author D. Campione
 *
 */
public class OTVDocumentTest {

    @Test
    public void testCharAt_Simple() throws Exception {
        String text = "Hello world";

        OTVDocument doc = new OTVDocument();
        doc.insertString(0, text, null);

        for (int i = 0; i < text.length(); i++) {
            Assert.assertEquals(text.charAt(i), doc.charAt(i));
        }
    }

    @Test
    public void testCharAt_ModifiedGapChange() throws Exception {
        String text = "Hello world";

        OTVDocument doc = new OTVDocument();
        doc.insertString(0, text, null);
        doc.insertString(6, "there ", null);

        String expected = "Hello there world";
        for (int i = 0; i < expected.length(); i++) {
            Assert.assertEquals(expected.charAt(i), doc.charAt(i));
        }
    }

    @Test(expected = BadLocationException.class)
    public void testCharAt_Invalid_NegativeOffset() throws Exception {
        String text = "Hello world";

        OTVDocument doc = new OTVDocument();
        doc.insertString(0, text, null);

        Assert.assertEquals('a', doc.charAt(-1));
    }

    @Test(expected = BadLocationException.class)
    public void testCharAt_Invalid_OffsetTooLarge() throws Exception {
        String text = "Hello world";

        OTVDocument doc = new OTVDocument();
        doc.insertString(0, text, null);

        Assert.assertEquals('a', doc.charAt(text.length() + 1));
    }
}