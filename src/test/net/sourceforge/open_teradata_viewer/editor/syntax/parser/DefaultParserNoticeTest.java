/*
 * Open Teradata Viewer ( editor syntax parser )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import java.awt.Color;
import java.net.URL;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IExtendedHyperlinkListener;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice.Level;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link DefaultParserNotice} class.
 *
 * @author D. Campione
 *
 */
public class DefaultParserNoticeTest {

    private MockParser parser;
    private DefaultParserNotice notice;

    @Before
    public void setUp() {
        parser = new MockParser();
    }

    @Test
    public void testDefaultParserNotice_3ArgConstructor() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertEquals("Foo", notice.getMessage());
        Assert.assertEquals(5, notice.getLine());
        Assert.assertEquals(-1, notice.getOffset());
        Assert.assertEquals(-1, notice.getLength());
        Assert.assertFalse(notice.getKnowsOffsetAndLength());
    }

    @Test
    public void testDefaultParserNoticeParserNotice_5ArgConstructor() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertEquals("Foo", notice.getMessage());
        Assert.assertEquals(5, notice.getLine());
        Assert.assertEquals(4, notice.getOffset());
        Assert.assertEquals(7, notice.getLength());
        Assert.assertTrue(notice.getKnowsOffsetAndLength());
    }

    @Test
    public void testCompareTo_null() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertTrue(notice.compareTo(null) < 0);
    }

    @Test
    public void testCompareTo_differentLevels() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.ERROR);
        DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 5);
        notice2.setLevel(Level.INFO);
        Assert.assertTrue(notice.compareTo(notice2) < 0);
        Assert.assertTrue(notice2.compareTo(notice) > 0);
    }

    @Test
    public void testCompareTo_differentLines() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.INFO);

        DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 6);
        notice2.setLevel(Level.INFO);

        Assert.assertTrue(notice.compareTo(notice2) < 0);
        Assert.assertTrue(notice2.compareTo(notice) > 0);
    }

    @Test
    public void testCompareTo_differentMessages() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.INFO);

        DefaultParserNotice notice2 = new DefaultParserNotice(parser, "FooBar",
                5);
        notice2.setLevel(Level.INFO);

        Assert.assertTrue(notice.compareTo(notice2) < 0);
        Assert.assertTrue(notice2.compareTo(notice) > 0);
    }

    @Test
    public void testCompareTo_equal() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.INFO);
        Assert.assertTrue(notice.compareTo(notice) == 0);

        DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 5);
        notice2.setLevel(Level.INFO);

        Assert.assertTrue(notice.compareTo(notice2) == 0);
        Assert.assertTrue(notice2.compareTo(notice) == 0);
    }

    @Test
    public void testContainsPosition() {
        int offs = 4;
        int len = 7;
        notice = new DefaultParserNotice(parser, "Foo", 5, offs, len);

        Assert.assertFalse(notice.containsPosition(offs - 1));
        for (int i = offs; i < offs + len; i++) {
            Assert.assertTrue(notice.containsPosition(i));
        }
        Assert.assertFalse(notice.containsPosition(offs + len));
    }

    @Test
    public void testEquals_null() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertFalse(notice.equals(null));
    }

    @Test
    public void testEquals_differentObjectType() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertFalse(notice.equals("Hello world"));
    }

    @Test
    public void testEquals_unequalParserNotice() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.INFO);

        DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Bar", 6);
        notice2.setLevel(Level.INFO);

        Assert.assertFalse(notice.equals(notice2));
        Assert.assertFalse(notice2.equals(notice));
    }

    @Test
    public void testEquals_equalParserNotice() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.INFO);

        DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 5);
        notice2.setLevel(Level.INFO);

        Assert.assertTrue(notice.equals(notice));
        Assert.assertTrue(notice.equals(notice2));
        Assert.assertTrue(notice2.equals(notice));
    }

    @Test
    public void testGetColor() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setColor(Color.yellow);
        Assert.assertEquals(Color.yellow, notice.getColor());
        notice.setColor(Color.orange);
        Assert.assertEquals(Color.orange, notice.getColor());
    }

    @Test
    public void testGetKnowsOffsetAndLength() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertFalse(notice.getKnowsOffsetAndLength());

        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertTrue(notice.getKnowsOffsetAndLength());
    }

    @Test
    public void testGetLength() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertEquals(-1, notice.getLength());

        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertEquals(7, notice.getLength());
    }

    @Test
    public void testGetLevel() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertEquals(Level.ERROR, notice.getLevel());
        notice.setLevel(Level.INFO);
        Assert.assertEquals(Level.INFO, notice.getLevel());
        notice.setLevel(Level.WARNING);
        Assert.assertEquals(Level.WARNING, notice.getLevel());
    }

    @Test
    public void testGetLine() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertEquals(5, notice.getLine());
    }

    @Test
    public void testGetMessage() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertEquals("Foo", notice.getMessage());
    }

    @Test
    public void testGetOffset() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertEquals(4, notice.getOffset());
    }

    @Test
    public void testGetParser() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertTrue(parser == notice.getParser());
    }

    @Test
    public void testGetShowInEditor() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertTrue(notice.getShowInEditor());
        notice.setShowInEditor(false);
        Assert.assertFalse(notice.getShowInEditor());
    }

    @Test
    public void testGetToolTipText() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertEquals("Foo", notice.getToolTipText()); // Defaults to message

        final String TIP = "Hello world";
        notice.setToolTipText(TIP);
        Assert.assertEquals(TIP, notice.getToolTipText());
    }

    @Test
    public void testHashCode() {
        int line = 5;
        DefaultParserNotice notice = new DefaultParserNotice(parser, "Foo",
                line);
        Assert.assertEquals(line << 16 | notice.getOffset(), notice.hashCode());

        int offs = 3;
        notice = new DefaultParserNotice(parser, "Foo", line, offs, 4);
        Assert.assertEquals(line << 16 | notice.getOffset(), notice.hashCode());
    }

    @Test
    public void testSetColor() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setColor(Color.yellow);
        Assert.assertEquals(Color.yellow, notice.getColor());
        notice.setColor(Color.orange);
        Assert.assertEquals(Color.orange, notice.getColor());
    }

    @Test
    public void testSetLevel() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        Assert.assertEquals(Level.ERROR, notice.getLevel());
        notice.setLevel(Level.INFO);
        Assert.assertEquals(Level.INFO, notice.getLevel());
        notice.setLevel(Level.WARNING);
        Assert.assertEquals(Level.WARNING, notice.getLevel());
    }

    @Test
    public void testSetLevel_null() {
        notice = new DefaultParserNotice(parser, "Foo", 5);
        notice.setLevel(Level.INFO);
        Assert.assertEquals(Level.INFO, notice.getLevel());
        notice.setLevel(null);
        Assert.assertEquals(Level.ERROR, notice.getLevel());
    }

    @Test
    public void testSetShowInEditor() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertTrue(notice.getShowInEditor());
        notice.setShowInEditor(false);
        Assert.assertFalse(notice.getShowInEditor());
    }

    @Test
    public void testSetToolTipText() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertEquals("Foo", notice.getToolTipText()); // Defaults to message

        final String TIP = "Hello world";
        notice.setToolTipText(TIP);
        Assert.assertEquals(TIP, notice.getToolTipText());
    }

    @Test
    public void testToString() {
        notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
        Assert.assertEquals("Line 5: Foo", notice.toString());
    }

    /**
     * A mock parser implementation for use in unit tests.
     *
     * @author D. Campione
     *
     */
    private static class MockParser implements IParser {

        private DefaultParseResult result;

        public MockParser() {
            result = new DefaultParseResult(this);
        }

        @Override
        public IExtendedHyperlinkListener getHyperlinkListener() {
            return null;
        }

        @Override
        public URL getImageBase() {
            return null;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public IParseResult parse(SyntaxDocument doc, String style) {
            return result;
        }
    }
}