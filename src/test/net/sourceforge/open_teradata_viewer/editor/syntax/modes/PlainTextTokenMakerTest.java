/*
 * Open Teradata Viewer ( editor syntax modes )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax.modes;

import javax.swing.text.Segment;

import org.junit.Assert;
import org.junit.Test;

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.PlainTextTokenMaker;

/**
 * Unit tests for the {@link PlainTextTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class PlainTextTokenMakerTest extends AbstractTokenMakerTest {

    @Test
    public void testIdentifiers() {
        String code = "   foo bar\t\tbas\t  \tbaz ";
        PlainTextTokenMaker tm = new PlainTextTokenMaker();

        Segment segment = createSegment(code);

        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertTrue(token.isWhitespace());
        token = token.getNextToken();

        while (token != null && token.isPaintable()) {
            Assert.assertEquals("Not an identifier: " + token,
                    ITokenTypes.IDENTIFIER, token.getType());
            token = token.getNextToken();
            Assert.assertTrue(token.isWhitespace());
            token = token.getNextToken();
        }
    }

    @Test
    public void testUrls() {
        String code = "http://www.sas.com foo ftp://ftp.microsoft.com bar https://google.com goo www.yahoo.com ber file://test.txt";
        PlainTextTokenMaker tm = new PlainTextTokenMaker();

        Segment segment = createSegment(code);

        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertTrue(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        Assert.assertEquals("http://www.sas.com", token.getLexeme());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertFalse(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertTrue(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        Assert.assertEquals("ftp://ftp.microsoft.com", token.getLexeme());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertFalse(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertTrue(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        Assert.assertEquals("https://google.com", token.getLexeme());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertFalse(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertTrue(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        Assert.assertEquals("www.yahoo.com", token.getLexeme());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertFalse(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        token = token.getNextToken();
        Assert.assertTrue(token.isWhitespace());

        token = token.getNextToken();
        Assert.assertTrue(token.isHyperlink());
        Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        Assert.assertEquals("file://test.txt", token.getLexeme());
    }

    @Test
    public void testWhitespace() {
        String code = "   foo bar\t\tbas\t  \tbaz ";
        PlainTextTokenMaker tm = new PlainTextTokenMaker();

        Segment segment = createSegment(code);

        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertTrue(token.isWhitespace());
        token = token.getNextToken();

        while (token != null && token.isPaintable()) {
            Assert.assertEquals("Not an identifier: " + token,
                    ITokenTypes.IDENTIFIER, token.getType());
            token = token.getNextToken();
            Assert.assertTrue(token.isWhitespace());
            token = token.getNextToken();
        }
    }
}