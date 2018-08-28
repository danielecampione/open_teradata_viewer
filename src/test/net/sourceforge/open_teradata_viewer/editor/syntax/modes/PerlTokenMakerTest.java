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
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.PerlTokenMaker;

/**
 * Unit tests for the {@link PerlTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class PerlTokenMakerTest extends AbstractTokenMakerTest {

    /**
     * Returns a new instance of the <code>ITokenMaker</code> to test.
     *
     * @return The <code>ITokenMaker</code> to test.
     */
    private ITokenMaker createTokenMaker() {
        return new PerlTokenMaker();
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
    public void testEolComments() {
        String[] eolCommentLiterals = { "# Hello world", };

        for (String code : eolCommentLiterals) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.COMMENT_EOL, token.getType());
        }
    }

    @Test
    public void testFloatingPointLiterals() {
        String code =
        // Basic doubles
        "3.0 4.2 3.0 4.2 .111 " +
                // Basic floats ending in f, F, d, or D
                "3.f 3.F 3.0f 3.0F .111f .111F " +
                // lower-case exponent, no sign
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
    public void testStandardFunctions() {
        String[] functions = { "abs", "accept", "alarm", "atan2", "bind",
                "binmode", "bless", "caller", "chdir", "chmod", "chomp", "chop",
                "chown", "chr", "chroot", "close", "closedir", "connect", "cos",
                "crypt", "dbmclose", "dbmopen", "defined", "delete", "die",
                "dump", "each", "endgrent", "endhostent", "endnetent",
                "endprotoent", "endpwent", "endservent", "eof", "eval", "exec",
                "exists", "exit", "exp", "fcntl", "fileno", "flock", "fork",
                "formline", "getc", "getgrent", "getgrgid", "getgrnam",
                "gethostbyaddr", "gethostbyname", "gethostent", "getlogin",
                "getnetbyaddr", "getnetbyname", "getnetent", "getpeername",
                "getpgrp", "getppid", "getpriority", "getprotobyname",
                "getprotobynumber", "getprotoent", "getpwent", "getpwnam",
                "getpwuid", "getservbyname", "getservbyport", "getservent",
                "getsockname", "getsockopt", "glob", "gmtime", "goto", "grep",
                "hex", "index", "int", "ioctl", "join", "keys", "kill",
                //"last",
                "lc", "lcfirst", "length", "link", "listen", "local",
                "localtime", "log", "lstat", "map", "mkdir", "msgctl", "msgget",
                "msgrcv", "msgsnd", "my",
                //"next",
                "no", "oct", "open", "opendir", "ord", "our", "pack", "package",
                "pipe", "pop", "pos", "print", "printf", "prototype", "push",
                "quotemeta", "rand", "read", "readdir", "readline", "readlink",
                "readpipe", "recv",
                //"redo",
                "ref", "rename", "require", "reset", "return", "reverse",
                "rewinddir", "rindex", "rmdir", "scalar", "seek", "seekdir",
                "select", "semctl", "semget", "semop", "send", "sethostent",
                "setgrent", "setnetent", "setpgrp", "setpriority",
                "setprotoent", "setpwent", "setservent", "setsockopt", "shift",
                "shmctl", "shmget", "shmread", "shmwrite", "shutdown", "sin",
                "sleep", "socket", "socketpair", "sort", "splice", "split",
                "sprintf", "sqrt", "srand", "stat", "study",
                //"sub",
                "substr", "symlink", "syscall", "sysopen", "sysread", "sysseek",
                "system", "syswrite", "tell", "telldir", "tie", "tied", "time",
                "times", "truncate", "uc", "ucfirst", "umask", "undef",
                "unlink", "unpack", "unshift", "untie", "use", "utime",
                "values", "vec", "wait", "waitpid", "wantarray", "warn",
                "write", };

        for (String code : functions) {
            Segment segment = createSegment(code);
            ITokenMaker tm = createTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals("Not a function: " + token,
                    ITokenTypes.FUNCTION, token.getType());
        }
    }

    @Test
    public void testKeywords() {
        String code = "and cmp continue do else elsif eq esac for foreach ge "
                + "if last le ne next not or redo sub unless until while xor";

        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        String[] keywords = code.split(" +");
        for (int i = 0; i < keywords.length; i++) {
            Assert.assertEquals(keywords[i], token.getLexeme());
            Assert.assertEquals("Not a keyword: " + token,
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
    }

    @Test
    public void testOperators() {
        String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == >> ~ | &&";
        String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>=";
        String code = assignmentOperators + " " + nonAssignmentOperators;

        Segment segment = createSegment(code);
        ITokenMaker tm = createTokenMaker();
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