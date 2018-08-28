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
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.DTokenMaker;

/**
 * Unit tests for the {@link DTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class DTokenMakerTest extends AbstractTokenMakerTest {

    /**
     * Returns a new instance of the <code>ITokenMaker</code> to test.
     *
     * @return The <code>ITokenMaker</code> to test.
     */
    private ITokenMaker createTokenMaker() {
        return new DTokenMaker();
    }

    @Test
    public void testBooleanLiterals() {
        String[] booleans = { "true", "false" };

        for (String code : booleans) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_BOOLEAN, code));
        }
    }

    @Test
    public void testCharLiterals() {
        String[] chars = { "'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'",
                "'\\111'", "'\\222'", "'\\333'", "'\\11'", "'\\22'", "'\\33'",
                "'\\1'", };

        for (String code : chars) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals("Invalid char literal: " + token,
                    ITokenTypes.LITERAL_CHAR, token.getType());
        }
    }

    @Test
    public void testDataTypes() {
        String[] dataTypes = { "string", "wstring", "dstring", "size_t",
                "ptrdiff_t", "bool", "byte", "cdouble", "cent", "cfloat",
                "char", "creal", "dchar", "double", "float", "idouble",
                "ifloat", "ireal", "int", "long", "real", "short", "ubyte",
                "ucent", "uint", "ulong", "ushort", "wchar", };

        for (String code : dataTypes) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.DATA_TYPE, code));
        }
    }

    @Test
    public void testEolComments() {
        String[] eolCommentLiterals = { "// Hello world", };

        for (String code : eolCommentLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_EOL, token.getType());
        }
    }

    @Test
    public void testEolComments_URL() {
        String[] eolCommentLiterals = { "// Hello world http://www.sas.com", };

        for (String code : eolCommentLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();

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
        "3.0 4.2 3.0 4.2 .111 " +
                // Basic floats ending in f, F, d, or D
                "3.f 3.F 3.0f 3.0F .111f .111F " +
                // Lower-case exponent, no sign
                "3.e7f 3.e7F 3.0e7f 3.0e7F .111e7f .111e7F " +
                // Upper-case exponent, no sign
                "3.E7f 3.E7F 3.0E7f 3.0E7F .111E7f .111E7F " +
                // Lower-case exponent, positive
                "3.e+7f 3.e+7F 3.0e+7f 3.0e+7F .111e+7f .111e+7F " +
                // Upper-case exponent, positive
                "3.E+7f 3.E+7F 3.0E+7f 3.0E+7F .111E+7f .111E+7F " +
                // Lower-case exponent, negative
                "3.e-7f 3.e-7F 3.0e-7f 3.0e-7F .111e-7f .111e-7F " +
                // Upper-case exponent, negative
                "3.E-7f 3.E-7F 3.0E-7f 3.0E-7F .111E-7f .111E-7F";

        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals("Invalid floating point: " + token,
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
    public void testHexLiterals() {
        String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE "
                + "0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl "
                + "0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL "
                + "0x1u 0xfeu 0x333333333333u 0X1u 0Xfeu 0X33333333333u 0xFEu 0XFEu "
                + "0x1U 0xfeU 0x333333333333U 0X1U 0XfeU 0X33333333333U 0xFEU 0XFEU "
                + "0x1lu 0xfelu 0x333333333333lu 0X1lu 0Xfelu 0X33333333333lu 0xFElu 0XFElu "
                + "0x1LU 0xfeLU 0x333333333333LU 0X1LU 0XfeLU 0X33333333333LU 0xFELU 0XFELU "
                + "0x1ul 0xfeul 0x333333333333ul 0X1ul 0Xfeul 0X33333333333ul 0xFEul 0XFEul "
                + "0x1UL 0xfeUL 0x333333333333UL 0X1UL 0XfeUL 0X33333333333UL 0xFEUL 0XFEUL";

        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals("Invalid hex literal: " + token,
                    ITokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
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
    public void testStandardFunctions() {
        String[] functions = {
                // Currently none
        };

        for (String code : functions) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals("Not a standard function: " + token,
                    ITokenTypes.FUNCTION, token.getType());
        }
    }

    @Test
    public void testKeywords() {
        String[] keywords = { "abstract", "alias", "align", "asm", "assert",
                "auto", "body", "break", "case", "cast", "catch", "class",
                "const", "continue", "debug", "default", "delegate", "delete",
                "deprecated", "do", "else", "enum", "export", "extern", "final",
                "finally", "for", "foreach", "foreach_reverse", "function",
                "goto", "if", "immutable", "import", "in", "inout", "interface",
                "invariant", "is", "lazy", "macro", "mixin", "module", "new",
                "nothrow", "null", "out", "override", "package", "pragma",
                "private", "protected", "public", "pure", "ref", "scope",
                "shared", "static", "struct", "super", "switch", "synchronized",
                "template", "this", "throw", "try", "typedef", "typeid",
                "typeof", "union", "unittest", "version", "void", "volatile",
                "while", "with", "__FILE__", "__MODULE__", "__LINE__",
                "__FUNCTION__", "__PRETTY_FUNCTION__", "__gshared", "__traits",
                "__vector", "__parameters" };

        for (String code : keywords) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.RESERVED_WORD, code));
        }

        Segment segment = createSegment("return");
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
        Assert.assertTrue(token.is(ITokenTypes.RESERVED_WORD_2, "return"));
    }

    @Test
    public void testMultiLineComments() {
        String[] mlcLiterals = { "/* Hello world */", };

        for (String code : mlcLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_MULTILINE, token.getType());
        }
    }

    @Test
    public void testMultiLineComments_URL() {
        String[] mlcLiterals = { "/* Hello world http://www.sas.com */", };

        for (String code : mlcLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();

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
        String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | &&";
        String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>=";
        String code = assignmentOperators + " " + nonAssignmentOperators;

        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals(ITokenTypes.OPERATOR, token.getType());
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

        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
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
        String[] stringLiterals = { "\"\"", "\"hi\"", "\"\\\"\"", };

        for (String code : stringLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    token.getType());
        }
    }
}