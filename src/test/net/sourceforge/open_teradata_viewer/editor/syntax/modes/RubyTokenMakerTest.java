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

import java.util.Arrays;

import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenMaker;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.RubyTokenMaker;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link RubyTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class RubyTokenMakerTest {

    /**
     * Returns a new instance of the <code>ITokenMaker</code> to test.
     *
     * @return The <code>ITokenMaker</code> to test.
     */
    private ITokenMaker createTokenMaker() {
        return new RubyTokenMaker();
    }

    @Test
    public void test_api_getLineCommentStartAndEnd() {
        ITokenMaker tm = createTokenMaker();
        String[] startAndEnd = tm.getLineCommentStartAndEnd(0);
        Assert.assertEquals("#", startAndEnd[0]);
        Assert.assertEquals(null, startAndEnd[1]);
    }

    @Test
    public void test_api_getMarkOccurrencesOfTokenType() {
        ITokenMaker tm = createTokenMaker();
        // NOTE: This array must be sorted for this test to work
        int[] expected = { ITokenTypes.VARIABLE, ITokenTypes.IDENTIFIER, };

        for (int i = 0; i < ITokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
            boolean shouldMark = Arrays.binarySearch(expected, i) >= 0;
            Assert.assertEquals(shouldMark, tm.getMarkOccurrencesOfTokenType(i));
        }
    }

    @Test
    public void testBacktickLiterals() {
        String[] chars = { "`Hello world`", "`Hello world", // Unterminated string literals not flagged as errors yet
                "`Hello \\q world`", // Any escapes are ignored
                "``", };

        for (String code : chars) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.LITERAL_BACKQUOTE, token.getType());
        }
    }

    @Test
    public void testBooleanLiterals() {
        String[] booleans = { "true", "false" };

        for (String code : booleans) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_BOOLEAN, code));
        }
    }

    @Test
    public void testCharLiterals() {
        String[] chars = { "'Hello world'", "'Hello world", // Unterminated char literals not flagged as errors yet
                "'Hello \\q world'", // Any escapes are ignored
                "''", };

        for (String code : chars) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.LITERAL_CHAR, token.getType());
        }
    }

    @Test
    public void testDocComments() {
        String[] docCommentLiterals = { "=begin Hello world =end" };

        for (String code : docCommentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_DOCUMENTATION,
                    token.getType());
        }
    }

    @Test
    public void testEolComments() {
        String[] eolCommentLiterals = { "# Hello world", };

        for (String code : eolCommentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_EOL, token.getType());
        }
    }

    @Test
    public void testFloatingPointLiterals() {
        String[] floats = { "3e10", "3e+10", "3e-10", "3E10", "3E+10", "3E-10",
                "3e1_0", "3E1_0", };

        for (String code : floats) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue("Nope: " + token,
                    token.is(ITokenTypes.LITERAL_NUMBER_FLOAT, code));
        }
    }

    @Test
    public void testFunctions() {
        String[] functions = { "Array", "Float", "Integer", "String",
                "at_exit", "autoload", "binding", "caller", "catch", "chop",
                "chop!", "chomp", "chomp!", "eval", "exec", "exit", "exit!",
                "fail", "fork", "format", "gets", "global_variables", "gsub",
                "gsub!", "iterator?", "lambda", "load", "local_variables",
                "loop", "open", "p", "print", "printf", "proc", "putc", "puts",
                "raise", "rand", "readline", "readlines", "require", "select",
                "sleep", "split", "sprintf", "srand", "sub", "sub!", "syscall",
                "system", "test", "trace_var", "trap", "untrace_var", };

        for (String code : functions) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.FUNCTION, token.getType());
        }
    }

    @Test
    public void testHeredoc_EOF() {
        // Note that the terminating "EOF" should be on another line in real
        // Ruby scripts, but our lexer does not discern that
        String[] eofs = { "<<EOF Hello world EOF",
                "<< \"EOF\" Hello world EOF", "<<   \t\"EOF\" Hello world EOF",
                "<< 'EOF' Hello world EOF", "<<   \t'EOF' Hello world EOF", };

        for (String code : eofs) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.PREPROCESSOR, code));
        }
    }

    @Test
    public void testHeredoc_EOT() {
        // Note that the terminating "EOT" should be on another line in real
        // Ruby scripts, but our lexer does not discern that
        String[] eofs = { "<<EOT Hello world EOT",
                "<< \"EOT\" Hello world EOT", "<<   \t\"EOT\" Hello world EOT",
                "<< 'EOT' Hello world EOT", "<<   \t'EOT' Hello world EOT", };

        for (String code : eofs) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.PREPROCESSOR, code));
        }
    }

    @Test
    public void testHexLiterals() {
        String[] hexLiterals = { "0x1", "0xfe", "0x333333333333 ", "0xf_e",
                "0x333_33_3", // Underscores
        };

        for (String code : hexLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals("Invalid hex literal: " + token,
                    ITokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
        }
    }

    @Test
    public void testIdentifiers() {
        String[] identifiers = { "foo", "_foo", "foo9", "_foo9", };

        for (String code : identifiers) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.IDENTIFIER, token.getType());
        }
    }

    @Test
    public void testIntegerLiterals() {
        String[] binaryInts = { "0b0", "0b111", "0b001", "0b10_01", };
        for (String code : binaryInts) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue("Nope: " + token,
                    token.is(ITokenTypes.LITERAL_NUMBER_DECIMAL_INT, code));
        }

        String[] octalInts = { "0", "0777", "017", "0_54", "07_7____7", };
        for (String code : octalInts) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue("Nope: " + token,
                    token.is(ITokenTypes.LITERAL_NUMBER_DECIMAL_INT, code));
        }

        String[] decimalInts = { "1", "333", "3_3____3", "0d1", "0d333",
                "0d3_3___3", };
        for (String code : decimalInts) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue("Nope: " + token,
                    token.is(ITokenTypes.LITERAL_NUMBER_DECIMAL_INT, code));
        }
    }

    @Test
    public void testKeywords() {
        String[] keywords = { "alias", "BEGIN", "begin", "break", "case",
                "class", "def", "defined", "do", "else", "elsif", "END", "end",
                "ensure", "for", "if", "in", "module", "next", "nil", "redo",
                "rescue", "retry", "return", "self", "super", "then", "undef",
                "unless", "until", "when", "while", "yield", };

        for (String code : keywords) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.RESERVED_WORD, token.getType());
        }
    }

    @Test
    public void testOperators() {
        String[] operators = { "and", "or", "not", "::", ".", "[", "]", "-",
                "+", "!", "~", "*", "/", "%", "<<", ">>", "&", "|", "^", ">",
                ">=", "<", "<=", "<=>", "==", "===", "!=", "=~", "!~", "&&",
                "||", "..", "...", "=", "+=", "-=", "*=", "/=", "%=", };

        for (String code : operators) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.OPERATOR, token.getType());
        }
    }

    @Test
    public void testPredefinedVariables() {
        // ("$"([!@&`\'+0-9~=/\,;.<>_*$?:\"]|"DEBUG"|"FILENAME"|"LOAD_PATH"|"stderr"|"stdin"|"stdout"|"VERBOSE"|([\-][0adFiIlpwv])))
        String[] predefinedVars = { "$!", "$@", "$DEBUG", "$FILENAME",
                "$LOAD_PATH", "$stderr", "$stdin", "$stdout", "$VERBOSE", };

        for (String code : predefinedVars) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.VARIABLE, token.getType());
        }
    }

    @Test
    public void testSeparators() {
        String[] separators = { "(", ")", "{", "}", };

        for (String code : separators) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.SEPARATOR, token.getType());
        }
    }

    @Test
    public void testStringLiterals() {
        String[] strings = { "\"Hello world\"", "\"Hello world", // Unterminated string literals not flagged as errors yet
                "\"Hello \\q world\"", // Any escapes are ignored
                "\"\"", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }

        strings = new String[] {
                "%(\"Hello world\")",
                "%(\"Hello world", // Unterminated not yet flagged as errors
                "%Q(\"Hello world\")", "%q(\"Hello world\")",
                "%W(\"Hello world\")", "%w(\"Hello world\")",
                "%x(\"Hello world\")", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }

        strings = new String[] {
                "%{\"Hello world\"}",
                "%{\"Hello world", // Unterminated not yet flagged as errors
                "%Q{\"Hello world\"}", "%q{\"Hello world\"}",
                "%W{\"Hello world\"}", "%w{\"Hello world\"}",
                "%x{\"Hello world\"}", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }

        strings = new String[] {
                "%[\"Hello world\"]",
                "%[\"Hello world", // Unterminated not yet flagged as errors
                "%Q[\"Hello world\"]", "%q[\"Hello world\"]",
                "%W[\"Hello world\"]", "%w[\"Hello world\"]",
                "%x[\"Hello world\"]", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }

        strings = new String[] {
                "%<\"Hello world\">",
                "%<\"Hello world", // Unterminated not yet flagged as errors
                "%Q<\"Hello world\">", "%q<\"Hello world\">",
                "%W<\"Hello world\">", "%w<\"Hello world\">",
                "%x<\"Hello world\">", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }

        strings = new String[] {
                "%!\"Hello world\"!",
                "%!\"Hello world", // Unterminated not yet flagged as errors
                "%Q!\"Hello world\"!", "%q!\"Hello world\"!",
                "%W!\"Hello world\"!", "%w!\"Hello world\"!",
                "%x!\"Hello world\"!", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }

        strings = new String[] {
                "%/\"Hello world\"/",
                "%/\"Hello world", // Unterminated not yet flagged as errors
                "%Q/\"Hello world\"/", "%q/\"Hello world\"/",
                "%W/\"Hello world\"/", "%w/\"Hello world\"/",
                "%x/\"Hello world\"/", };
        for (String code : strings) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertTrue(token.is(ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
                    code));
        }
    }

    @Test
    public void testVariables() {
        String[] vars = { "$foo", "@foo", "@@foo", };

        for (String code : vars) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.VARIABLE, token.getType());
        }
    }
}