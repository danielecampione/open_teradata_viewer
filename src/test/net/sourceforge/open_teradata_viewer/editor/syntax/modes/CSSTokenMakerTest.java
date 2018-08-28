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
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenMaker;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.CSSTokenMaker;

/**
 * Unit tests for the {@link CSSTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class CSSTokenMakerTest extends AbstractTokenMakerTest {

    /**
     * The last token type on the previous line for this token maker to start
     * parsing a new line as CSS. This constant is only here so we can copy and
     * paste tests from this class into others, such as HTML, PHP and JSP token
     * maker tests, with as little change as possible.
     */
    private static final int CSS_PREV_TOKEN_TYPE = ITokenTypes.NULL;

    /**
     * Returns a new instance of the <code>ITokenMaker</code> to test.
     *
     * @return The <code>ITokenMaker</code> to test.
     */
    private ITokenMaker createTokenMaker() {
        return new CSSTokenMaker();
    }

    @Test
    public void testCss_comment() {
        String[] commentLiterals = { "/* Hello world */", };

        for (String code : commentLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_MULTILINE, token.getType());
        }
    }

    @Test
    public void testCss_comment_URL() {
        String code = "/* Hello world http://www.google.com */";
        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

        Assert.assertFalse(token.isHyperlink());
        Assert.assertTrue(
                token.is(ITokenTypes.COMMENT_MULTILINE, "/* Hello world "));
        token = token.getNextToken();
        Assert.assertTrue(token.isHyperlink());
        Assert.assertTrue(token.is(ITokenTypes.COMMENT_MULTILINE,
                "http://www.google.com"));
        token = token.getNextToken();
        Assert.assertFalse(token.isHyperlink());
        Assert.assertTrue(token.is(ITokenTypes.COMMENT_MULTILINE, " */"));
    }

    @Test
    public void testCss_getCurlyBracesDenoteCodeBlocks() {
        ITokenMaker tm = createTokenMaker();
        Assert.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(0));
    }

    @Test
    public void testCss_getLineCommentStartAndEnd() {
        ITokenMaker tm = createTokenMaker();
        Assert.assertNull(tm.getLineCommentStartAndEnd(0));
    }

    @Test
    public void testCss_getMarkOccurrencesOfTokenType() {
        ITokenMaker tm = createTokenMaker();
        Assert.assertTrue(
                tm.getMarkOccurrencesOfTokenType(ITokenTypes.RESERVED_WORD));
        Assert.assertFalse(
                tm.getMarkOccurrencesOfTokenType(ITokenTypes.VARIABLE));
    }

    @Test
    public void testCss_happyPath_simpleSelector() {
        String code = "body { padding: 0; }";
        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

        Assert.assertTrue(token.is(ITokenTypes.DATA_TYPE, "body"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.SEPARATOR, "{"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.RESERVED_WORD, "padding"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.OPERATOR, ":"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(
                token.is(ITokenTypes.LITERAL_NUMBER_DECIMAL_INT, "0"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.OPERATOR, ";"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.SEPARATOR, "}"));
    }

    @Test
    public void testCss_id() {
        String code = "#mainContent";
        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

        Assert.assertTrue(token.is(ITokenTypes.VARIABLE, "#mainContent"));
    }

    @Test
    public void testCss_isIdentifierChar() {
        ITokenMaker tm = createTokenMaker();
        for (int ch = 'A'; ch <= 'Z'; ch++) {
            Assert.assertTrue(tm.isIdentifierChar(0, (char) ch));
            Assert.assertTrue(
                    tm.isIdentifierChar(0, (char) (ch + ('a' - 'A'))));
        }
        Assert.assertTrue(tm.isIdentifierChar(0, '-'));
        Assert.assertTrue(tm.isIdentifierChar(0, '_'));
        Assert.assertTrue(tm.isIdentifierChar(0, '.'));
    }

    @Test
    public void testCss_propertyValue_function() {
        String code = "background-image: url(\"test.png\");";
        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment,
                CSSTokenMaker.INTERNAL_CSS_PROPERTY, 0);

        Assert.assertTrue(
                token.is(ITokenTypes.RESERVED_WORD, "background-image"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.OPERATOR, ":"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.FUNCTION, "url"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.SEPARATOR, "("));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                "\"test.png\""));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.SEPARATOR, ")"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.OPERATOR, ";"));

        code = "background-image: url('test.png');";
        segment = createSegment(code);
        tm = createTokenMaker();
        token = tm.getTokenList(segment, CSSTokenMaker.INTERNAL_CSS_PROPERTY,
                0);

        Assert.assertTrue(
                token.is(ITokenTypes.RESERVED_WORD, "background-image"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.OPERATOR, ":"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.FUNCTION, "url"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.SEPARATOR, "("));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.LITERAL_CHAR, "'test.png'"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.SEPARATOR, ")"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.OPERATOR, ";"));
    }
}