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

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.JshintrcTokenMaker;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link JshintrcTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class JshintrcTokenMakerTest {

    @Test
    public void testBooleanLiterals() {
        String code = "true false";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JshintrcTokenMaker tm = new JshintrcTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals(ITokenTypes.LITERAL_BOOLEAN, token.getType());
            if (i < keywords.length - 1) {
                token = token.getNextToken();
                Assert.assertTrue("Not a whitespace token: " + token,
                        token.isWhitespace());
                Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
            }
            token = token.getNextToken();
        }

        Assert.assertTrue(token.getType() == ITokenTypes.NULL);
    }

    @Test
    public void testFloatingPointLiterals() {
        String code =
        // Basic doubles
        "3.0 4.2 3.000 4.2 0.111 " +
        // lower-case exponent, no sign
                "3e7 3.0e7 0.111e7 -3e7 -3.0e7 -0.111e7 " +
                // Upper-case exponent, no sign
                "3E7 3.0E7 0.111E7 -3E7 -3.0E7 -0.111E7 " +
                // Lower-case exponent, positive
                "3e+7 3.0e+7 0.111e+7 -3e+7 -3.0e+7 -0.111e+7 " +
                // Upper-case exponent, positive
                "3E+7 3.0E+7 0.111E+7 -3E+7 -3.0E+7 -0.111E+7 " +
                // Lower-case exponent, negative
                "3e-7 3.0e-7 0.111e-7 -3e-7 -3.0e-7 -0.111e-7 " +
                // Upper-case exponent, negative
                "3E-7 3.0E-7 0.111E-7 -3E-7 -3.0E-7 -0.111E-7";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JshintrcTokenMaker tm = new JshintrcTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals("Unexpected number for token " + i,
                    keywords[i], token.getLexeme());
            Assert.assertEquals("Invalid float: " + token,
                    ITokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
            if (i < keywords.length - 1) {
                token = token.getNextToken();
                Assert.assertTrue("Not a whitespace token: " + token,
                        token.isWhitespace());
                Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
            }
            token = token.getNextToken();
        }

        Assert.assertTrue(token.getType() == ITokenTypes.NULL);
    }

    @Test
    public void testIntegerLiterals() {
        String code = "1 42 0 -1 -42";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JshintrcTokenMaker tm = new JshintrcTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals("Unexpected number for token " + i,
                    keywords[i], token.getLexeme());
            Assert.assertEquals(ITokenTypes.LITERAL_NUMBER_DECIMAL_INT,
                    token.getType());
            if (i < keywords.length - 1) {
                token = token.getNextToken();
                Assert.assertTrue("Not a whitespace token: " + token,
                        token.isWhitespace());
                Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
            }
            token = token.getNextToken();
        }

        Assert.assertTrue(token.getType() == ITokenTypes.NULL);
    }

    @Test
    public void testMultiLineComments() {
        String code = "// Hello world";
        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JshintrcTokenMaker tm = new JshintrcTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertTrue(token.is(IToken.COMMENT_EOL, code));
    }

    @Test
    public void testNullLiterals() {
        String code = "null";
        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JshintrcTokenMaker tm = new JshintrcTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertTrue(token.is(ITokenTypes.RESERVED_WORD, "null"));
    }

    @Test
    public void testSeparators() {
        String code = "[ ] { }";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JshintrcTokenMaker tm = new JshintrcTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] separators = code.split(" +");
        for (int i = 0; i < separators.length; i++) {
            Assert.assertEquals(separators[i], token.getLexeme());
            Assert.assertEquals(ITokenTypes.SEPARATOR, token.getType());
            // Just one extra test here
            Assert.assertTrue(token.isSingleChar(ITokenTypes.SEPARATOR,
                    separators[i].charAt(0)));
            if (i < separators.length - 1) {
                token = token.getNextToken();
                Assert.assertTrue("Not a whitespace token: " + token,
                        token.isWhitespace());
                Assert.assertTrue("Not a single space: " + token,
                        token.is(ITokenTypes.WHITESPACE, " "));
            }
            token = token.getNextToken();
        }

        Assert.assertTrue(token.getType() == ITokenTypes.NULL);
    }

    @Test
    public void testStringLiterals() {
        String[] stringLiterals = { "\"\"", "\"hi\"", "\"\\u00fe\"",
                "\"\\\"\"", "\"\\\\/\\b\\f\\n\\r\\t\"", };

        for (String code : stringLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JshintrcTokenMaker tm = new JshintrcTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals("Invalid string: " + token,
                    ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
        }
    }

    @Test
    public void testStringLiterals_errors() {
        String[] stringLiterals = { "\"foo \\x bar\"",
                "\"foo unterminated string", };

        for (String code : stringLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JshintrcTokenMaker tm = new JshintrcTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals("Invalid error-string: " + token,
                    ITokenTypes.ERROR_STRING_DOUBLE, token.getType());
        }
    }
}