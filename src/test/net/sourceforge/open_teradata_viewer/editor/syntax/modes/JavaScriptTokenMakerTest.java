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
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.JavaScriptTokenMaker;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link JavaScriptTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class JavaScriptTokenMakerTest {

    @Test
    public void testBooleanLiterals() {
        String code = "true false";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
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
    public void testCharLiterals() {
        String[] chars = { "'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'",
                "'\\u00fe'", "'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
                "'\\11'", "'\\22'", "'\\33'", "'\\1'", };

        for (String code : chars) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.LITERAL_CHAR, token.getType());
        }
    }

    @Test
    public void testDataTypes() {
        String code = "boolean byte char double float int long short";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals(ITokenTypes.DATA_TYPE, token.getType());
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
    public void testDocComments() {
        String[] docCommentLiterals = { "/** Hello world */", };

        for (String code : docCommentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_DOCUMENTATION,
                    token.getType());
        }
    }

    @Test
    public void testDocComments_URL() {
        String[] docCommentLiterals = { "/** Hello world http://www.sas.com */", };

        for (String code : docCommentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();

            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_DOCUMENTATION,
                    token.getType());

            token = token.getNextToken();
            Assert.assertTrue(token.isHyperlink());
            Assert.assertEquals(ITokenTypes.COMMENT_DOCUMENTATION,
                    token.getType());
            Assert.assertEquals("http://www.sas.com", token.getLexeme());

            token = token.getNextToken();
            Assert.assertEquals(ITokenTypes.COMMENT_DOCUMENTATION,
                    token.getType());
            Assert.assertEquals(" */", token.getLexeme());
        }
    }

    @Test
    public void testEolComments() {
        String[] eolCommentLiterals = { "// Hello world", };

        for (String code : eolCommentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_EOL, token.getType());
        }
    }

    @Test
    public void testEolComments_URL() {
        String[] eolCommentLiterals = { "// Hello world http://www.sas.com", };

        for (String code : eolCommentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();

            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_EOL, token.getType());

            token = token.getNextToken();
            Assert.assertTrue(token.isHyperlink());
            Assert.assertEquals(ITokenTypes.COMMENT_EOL, token.getType());
            Assert.assertEquals("http://www.sas.com", token.getLexeme());
        }
    }

    @Test
    public void testFloatingPointLiterals() {
        String code =
        // Basic doubles
        "3.0 4.2 3.0 4.2 .111 "
                +
                // Basic floats ending in f, F, d, or D
                "3f 3F 3d 3D 3.f 3.F 3.d 3.D 3.0f 3.0F 3.0d 3.0D .111f .111F .111d .111D "
                +
                // Lower-case exponent, no sign
                "3e7f 3e7F 3e7d 3e7D 3.e7f 3.e7F 3.e7d 3.e7D 3.0e7f 3.0e7F 3.0e7d 3.0e7D .111e7f .111e7F .111e7d .111e7D "
                +
                // Upper-case exponent, no sign
                "3E7f 3E7F 3E7d 3E7D 3.E7f 3.E7F 3.E7d 3.E7D 3.0E7f 3.0E7F 3.0E7d 3.0E7D .111E7f .111E7F .111E7d .111E7D "
                +
                // Lower-case exponent, positive
                "3e+7f 3e+7F 3e+7d 3e+7D 3.e+7f 3.e+7F 3.e+7d 3.e+7D 3.0e+7f 3.0e+7F 3.0e+7d 3.0e+7D .111e+7f .111e+7F .111e+7d .111e+7D "
                +
                // Upper-case exponent, positive
                "3E+7f 3E+7F 3E+7d 3E+7D 3.E+7f 3.E+7F 3.E+7d 3.E+7D 3.0E+7f 3.0E+7F 3.0E+7d 3.0E+7D .111E+7f .111E+7F .111E+7d .111E+7D "
                +
                // Lower-case exponent, negative
                "3e-7f 3e-7F 3e-7d 3e-7D 3.e-7f 3.e-7F 3.e-7d 3.e-7D 3.0e-7f 3.0e-7F 3.0e-7d 3.0e-7D .111e-7f .111e-7F .111e-7d .111e-7D "
                +
                // Upper-case exponent, negative
                "3E-7f 3E-7F 3E-7d 3E-7D 3.E-7f 3.E-7F 3.E-7d 3.E-7D 3.0E-7f 3.0E-7F 3.0E-7d 3.0E-7D .111E-7f .111E-7F .111E-7d .111E-7D";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals(ITokenTypes.LITERAL_NUMBER_FLOAT,
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
    public void testFunctions() {
        String code = "eval parseInt parseFloat escape unescape isNaN isFinite";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] functions = code.split(" +");
        for (int i = 0; i < functions.length; i++) {
            Assert.assertEquals(functions[i], token.getLexeme());
            Assert.assertEquals("Not a function token: " + token,
                    ITokenTypes.FUNCTION, token.getType());
            if (i < functions.length - 1) {
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
    public void testHexLiterals() {
        String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE "
                + "0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl "
                + "0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL ";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] literals = code.split(" +");
        for (int i = 0; i < literals.length; i++) {
            Assert.assertEquals(literals[i], token.getLexeme());
            Assert.assertEquals("Not a hex number: " + token,
                    ITokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
            if (i < literals.length - 1) {
                token = token.getNextToken();
                Assert.assertTrue("Not a whitespace token: " + token,
                        token.isWhitespace());
                Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
            }
            token = token.getNextToken();
        }
    }

    @Test
    public void testKeywords() {
        String code = "break case catch class const continue "
                + "debugger default delete do else export extends finally for function if "
                + "import in instanceof let new super switch "
                + "this throw try typeof void while with " + "NaN Infinity";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals("Not a keyword token: " + token,
                    ITokenTypes.RESERVED_WORD, token.getType());
            if (i < keywords.length - 1) {
                token = token.getNextToken();
                Assert.assertTrue("Not a whitespace token: " + token,
                        token.isWhitespace());
                Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
            }
            token = token.getNextToken();
        }

        Assert.assertTrue(token.getType() == ITokenTypes.NULL);

        segment = new Segment("return".toCharArray(), 0, "return".length());
        token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertEquals("return", token.getLexeme());
        Assert.assertEquals(ITokenTypes.RESERVED_WORD_2, token.getType());
        token = token.getNextToken();
        Assert.assertTrue(token.getType() == ITokenTypes.NULL);
    }

    @Test
    public void testMultiLineComments() {
        String[] mlcLiterals = { "/* Hello world */", };

        for (String code : mlcLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_MULTILINE, token.getType());
        }
    }

    @Test
    public void testMultiLineComments_URL() {
        String[] mlcLiterals = { "/* Hello world http://www.sas.com */", };

        for (String code : mlcLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();

            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_MULTILINE, token.getType());

            token = token.getNextToken();
            Assert.assertTrue(token.isHyperlink());
            Assert.assertEquals(ITokenTypes.COMMENT_MULTILINE, token.getType());
            Assert.assertEquals("http://www.sas.com", token.getLexeme());

            token = token.getNextToken();
            Assert.assertEquals(ITokenTypes.COMMENT_MULTILINE, token.getType());
            Assert.assertEquals(" */", token.getLexeme());
        }
    }

    @Test
    public void testOperators() {
        String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
        String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
        String code = assignmentOperators + " " + nonAssignmentOperators;

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals("Not an operator: " + token,
                    ITokenTypes.OPERATOR, token.getType());
            if (i < keywords.length - 1) {
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
    public void testSeparators() {
        String code = "( ) [ ] { }";

        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
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
                "\"\\\"\"", };

        for (String code : stringLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    token.getType());
        }
    }
}