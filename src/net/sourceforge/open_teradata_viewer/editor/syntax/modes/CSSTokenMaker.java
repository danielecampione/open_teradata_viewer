/*
 * Open Teradata Viewer ( editor syntax modes )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax.modes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.AbstractJFlexCTokenMaker;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.TokenImpl;

/**
 * This class splits up text into tokens representing a CSS 3 file. It's written
 * with a few extra internal states, so that it can easily be copy and pasted
 * into HTML, PHP and JSP TokenMakers when it is updated.<p>
 *
 * This implementation was created using <a
 * href="http://www.jflex.de/">JFlex</a> 1.4.1; however, the generated file was
 * modified for performance. Memory allocation needs to be almost completely
 * removed to be competitive with the handwritten lexers (subclasses of
 * <code>AbstractTokenMaker</code>, so this class has been modified so that
 * Strings are never allocated (via yytext()) and the scanner never has to worry
 * about refilling its buffer (needlessly copying chars around).
 * We can achieve this because Open Teradata Viewer always scans exactly 1 line
 * of tokens at a time and hands the scanner this line as an array of characters
 * (a Segment really). Since tokens contain pointers to char arrays instead of
 * Strings holding their contents, there is no need for allocating new memory
 * for Strings.<p>
 *
 * The actual algorithm generated for scanning has, of course, not been
 * modified.<p>
 *
 * If you wish to regenerate this file yourself, keep in mind the following:
 * <ul>
 *   <li>The generated CSSTokenMaker.java</code> file will contain 2 definitions
 *       of both <code>zzRefill</code> and <code>yyreset</code>.
 *       You should hand-delete the second of each definition (the ones
 *       generated by the lexer), as these generated methods modify the input
 *       buffer, which we'll never have to do.</li>
 *   <li>You should also change the declaration/definition of zzBuffer to NOT be
 *       initialized. This is a needless memory allocation for us since we will
 *       be pointing the array somewhere else anyway.</li>
 *   <li>You should NOT call <code>yylex()</code> on the generated scanner
 *       directly; rather, you should use <code>getTokenList</code> as you would
 *       with any other <code>ITokenMaker</code> instance.</li>
 * </ul>
 *
 * @author D. Campione
 *
 */
public class CSSTokenMaker extends AbstractJFlexCTokenMaker {

    /** This character denotes the end of file. */
    public static final int YYEOF = -1;

    /** Lexical states. */
    public static final int CSS_C_STYLE_COMMENT = 5;
    public static final int YYINITIAL = 0;
    public static final int CSS_STRING = 3;
    public static final int CSS_VALUE = 2;
    public static final int CSS_PROPERTY = 1;
    public static final int CSS_CHAR_LITERAL = 4;

    /** Translates characters to character classes. */
    private static final String ZZ_CMAP_PACKED = "\11\0\1\40\1\61\25\0\1\40\1\63\1\56\1\36\1\52\1\45"
            + "\1\47\1\57\1\43\1\64\1\5\1\51\1\55\1\4\1\6\1\41"
            + "\12\1\1\7\1\37\1\0\1\51\1\60\1\47\1\35\6\46\24\2"
            + "\1\50\1\42\1\50\1\60\1\3\1\0\1\21\1\34\1\15\1\20"
            + "\1\26\1\23\1\33\1\14\1\16\1\2\1\30\1\17\1\27\1\13"
            + "\1\11\1\25\1\2\1\10\1\22\1\12\1\32\1\31\1\53\1\44"
            + "\1\24\1\2\1\54\1\60\1\62\1\51\uff81\0";

    /** Translates characters to character classes. */
    private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

    /** Translates DFA states to action switch labels. */
    private static final int[] ZZ_ACTION = zzUnpackAction();

    private static final String ZZ_ACTION_PACKED_0 = "\2\0\1\1\3\0\1\2\1\3\1\4\2\2\1\5"
            + "\1\6\1\2\1\7\1\10\1\1\1\11\1\12\1\13"
            + "\1\14\1\15\1\14\1\16\1\14\1\17\1\20\1\21"
            + "\1\22\1\23\2\1\1\22\1\24\1\1\1\25\1\26"
            + "\1\22\1\27\1\30\1\31\1\32\1\33\1\30\1\34"
            + "\1\35\5\30\1\36\15\0\1\37\1\40\1\41\2\0"
            + "\1\23\3\0\1\23\1\0\1\31\1\42\41\0\1\15" + "\17\0\1\43\47\0\1\44";

    private static int[] zzUnpackAction() {
        int[] result = new int[168];
        int offset = 0;
        offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAction(String packed, int offset, int[] result) {
        int i = 0; /* Index in packed string */
        int j = offset; /* Index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }

    /** Translates a state to a row index in the transition table. */
    private static final int[] ZZ_ROWMAP = zzUnpackRowMap();

    private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\65\0\152\0\237\0\324\0\u0109\0\u013e\0\u0173"
            + "\0\u01a8\0\u01dd\0\u0212\0\u013e\0\u0247\0\u027c\0\u013e\0\u013e"
            + "\0\u013e\0\u013e\0\u013e\0\u013e\0\u013e\0\u02b1\0\u02e6\0\u013e"
            + "\0\u027c\0\u013e\0\u013e\0\u013e\0\u013e\0\u031b\0\u0350\0\u0385"
            + "\0\u03ba\0\u013e\0\u03ef\0\u013e\0\u013e\0\u0424\0\u013e\0\u0459"
            + "\0\u048e\0\u013e\0\u013e\0\u04c3\0\u013e\0\u013e\0\u04f8\0\u052d"
            + "\0\u0562\0\u0597\0\u05cc\0\u013e\0\u0601\0\u0636\0\u066b\0\u06a0"
            + "\0\u06d5\0\u070a\0\u073f\0\u0774\0\u07a9\0\u07de\0\u0813\0\u0848"
            + "\0\u087d\0\u08b2\0\u08e7\0\u013e\0\u091c\0\u0951\0\u013e\0\u0986"
            + "\0\u09bb\0\u09f0\0\u03ba\0\u0a25\0\u013e\0\u013e\0\u0a5a\0\u0a8f"
            + "\0\u0ac4\0\u0af9\0\u0b2e\0\u0b63\0\u0b98\0\u0bcd\0\u0c02\0\u0c37"
            + "\0\u0c6c\0\u0ca1\0\u0cd6\0\u0d0b\0\u0d40\0\u0d75\0\u0daa\0\u0ddf"
            + "\0\u0e14\0\u0e49\0\u0e7e\0\u0eb3\0\u0ee8\0\u0f1d\0\u0f52\0\u0f87"
            + "\0\u0fbc\0\u0ff1\0\u1026\0\u105b\0\u1090\0\u10c5\0\u10fa\0\u013e"
            + "\0\u112f\0\u1164\0\u1199\0\u11ce\0\u1203\0\u1238\0\u126d\0\u12a2"
            + "\0\u12d7\0\u130c\0\u1341\0\u1376\0\u13ab\0\u13e0\0\u1415\0\u144a"
            + "\0\u147f\0\u14b4\0\u14e9\0\u151e\0\u1553\0\u1588\0\u15bd\0\u15f2"
            + "\0\u1627\0\u165c\0\u1691\0\u16c6\0\u16fb\0\u1730\0\u144a\0\u1765"
            + "\0\u179a\0\u17cf\0\u1804\0\u1839\0\u186e\0\u18a3\0\u18d8\0\u190d"
            + "\0\u1942\0\u1977\0\u19ac\0\u19e1\0\u1a16\0\u1a4b\0\u1a80\0\u1ab5"
            + "\0\u1aea\0\u1b1f\0\u1b54\0\u1b89\0\u1bbe\0\u1bf3\0\u1c28\0\u013e";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[168];
        int offset = 0;
        offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackRowMap(String packed, int offset, int[] result) {
        int i = 0; /* Index in packed string */
        int j = offset; /* Index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int high = packed.charAt(i++) << 16;
            result[j++] = high | packed.charAt(i++);
        }
        return j;
    }

    /** The transition table of the DFA. */
    private static final int[] ZZ_TRANS = zzUnpackTrans();

    private static final String ZZ_TRANS_PACKED_0 = "\2\7\5\10\1\11\25\10\1\12\1\13\1\14\1\15"
            + "\1\16\1\7\1\14\1\10\1\7\1\10\1\7\1\14"
            + "\2\17\1\10\1\20\1\21\1\22\1\23\1\17\1\24"
            + "\2\7\1\14\2\25\3\26\1\27\1\25\1\30\25\26"
            + "\3\25\1\15\1\31\2\25\1\26\1\25\1\26\4\25"
            + "\1\26\1\32\4\25\1\33\1\34\2\25\1\35\1\36"
            + "\2\37\1\40\1\35\1\21\1\35\25\37\1\35\1\41"
            + "\1\42\1\15\1\43\1\37\1\44\1\37\1\35\1\37"
            + "\4\35\1\37\1\35\1\21\1\22\1\23\1\35\1\45"
            + "\1\34\1\46\1\47\42\50\1\51\13\50\1\52\2\50"
            + "\1\53\3\50\42\54\1\51\14\54\1\55\1\54\1\56"
            + "\3\54\5\57\1\60\6\57\1\61\6\57\1\62\27\57"
            + "\1\63\5\57\1\64\3\57\66\0\4\10\1\0\1\10"
            + "\1\0\25\10\7\0\1\10\1\0\1\10\4\0\1\10"
            + "\20\0\1\65\1\66\1\67\1\70\1\71\1\72\1\73"
            + "\1\0\1\74\1\75\1\76\1\0\1\77\2\0\1\100"
            + "\2\0\1\101\35\0\5\102\1\0\25\102\7\0\1\102"
            + "\1\0\1\102\4\0\1\102\13\0\5\103\1\0\25\103"
            + "\7\0\1\103\1\0\1\103\4\0\1\103\51\0\1\15"
            + "\31\0\1\104\60\0\4\26\3\0\25\26\7\0\1\26"
            + "\1\0\1\26\4\0\1\26\13\0\3\26\3\0\25\26"
            + "\7\0\1\26\1\0\1\26\4\0\1\26\12\0\1\36"
            + "\4\0\1\36\6\0\1\105\1\106\3\0\1\107\2\0"
            + "\1\110\1\111\1\112\15\0\1\107\21\0\3\37\3\0"
            + "\25\37\4\0\2\37\1\44\1\37\1\0\1\37\4\0"
            + "\1\37\12\0\1\36\3\37\3\0\25\37\4\0\2\37"
            + "\1\44\1\37\1\0\1\37\4\0\1\37\12\0\1\113"
            + "\13\0\1\113\2\0\2\113\1\0\1\113\2\0\1\113"
            + "\5\0\1\113\11\0\1\113\20\0\3\37\1\104\2\0"
            + "\25\37\4\0\2\37\1\44\1\37\1\0\1\37\4\0"
            + "\1\37\27\0\1\114\46\0\42\50\1\0\13\50\1\0"
            + "\2\50\1\0\3\50\61\115\1\0\3\115\42\54\1\0"
            + "\14\54\1\0\1\54\1\0\3\54\5\57\1\0\6\57"
            + "\1\0\6\57\1\0\27\57\1\0\5\57\1\0\3\57"
            + "\41\0\1\116\35\0\1\117\64\0\1\120\3\0\1\121"
            + "\121\0\1\122\32\0\1\123\1\0\1\124\10\0\1\125"
            + "\41\0\1\126\66\0\1\127\72\0\1\130\54\0\1\131"
            + "\1\132\63\0\1\133\67\0\1\134\66\0\1\135\2\0"
            + "\1\136\61\0\1\137\63\0\1\140\60\0\1\141\4\0"
            + "\1\142\61\0\1\143\13\0\1\144\53\0\1\145\47\0"
            + "\4\102\1\0\1\102\1\0\25\102\7\0\1\102\1\0"
            + "\1\102\4\0\1\102\12\0\4\103\1\0\1\103\1\0"
            + "\25\103\7\0\1\103\1\0\1\103\4\0\1\103\40\0"
            + "\1\107\50\0\1\107\63\0\1\107\2\0\1\107\26\0"
            + "\1\107\47\0\1\107\14\0\1\107\42\0\1\107\4\0"
            + "\1\107\64\0\1\146\47\0\1\147\77\0\1\150\56\0"
            + "\1\151\120\0\1\152\34\0\1\153\57\0\1\154\74\0"
            + "\1\155\47\0\1\131\72\0\1\156\55\0\1\157\66\0"
            + "\1\160\66\0\1\161\101\0\1\162\61\0\1\163\51\0"
            + "\1\164\64\0\1\165\6\0\1\166\64\0\1\143\54\0"
            + "\1\167\67\0\1\170\57\0\1\171\75\0\1\172\70\0"
            + "\1\173\61\0\1\174\67\0\1\175\64\0\1\176\46\0"
            + "\1\177\103\0\1\150\44\0\1\200\70\0\1\162\62\0"
            + "\1\201\77\0\1\202\65\0\1\203\73\0\1\204\35\0"
            + "\1\205\106\0\1\206\53\0\1\207\77\0\1\160\67\0"
            + "\1\160\43\0\1\203\70\0\1\210\100\0\1\211\54\0"
            + "\1\166\76\0\1\212\42\0\1\213\70\0\1\214\57\0"
            + "\1\215\62\0\1\177\12\0\1\150\103\0\1\216\24\0"
            + "\2\200\5\217\25\200\3\217\1\0\1\200\1\0\1\217"
            + "\1\200\1\217\1\200\3\217\2\200\1\0\1\217\1\0"
            + "\1\217\3\0\2\217\22\0\1\220\53\0\1\221\57\0"
            + "\1\222\106\0\1\131\47\0\1\223\3\0\1\224\1\0"
            + "\1\225\55\0\1\160\104\0\1\226\65\0\1\227\55\0"
            + "\1\160\61\0\1\226\71\0\1\160\52\0\1\226\62\0"
            + "\1\230\115\0\1\200\35\0\1\231\62\0\1\227\65\0"
            + "\1\223\3\0\1\224\72\0\1\232\55\0\1\233\71\0"
            + "\1\171\71\0\1\234\64\0\1\160\50\0\1\235\56\0"
            + "\1\236\64\0\1\237\76\0\1\240\66\0\1\160\65\0"
            + "\1\241\62\0\1\242\57\0\1\243\71\0\1\234\60\0"
            + "\1\244\67\0\1\245\7\0\1\246\62\0\1\247\52\0"
            + "\1\250\65\0\1\227\63\0\1\153\77\0\1\227\37\0";

    private static int[] zzUnpackTrans() {
        int[] result = new int[7261];
        int offset = 0;
        offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackTrans(String packed, int offset, int[] result) {
        int i = 0; /* Index in packed string */
        int j = offset; /* Index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            value--;
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }

    /* Error codes */
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;

    /* Error messages for the codes above */
    private static final String ZZ_ERROR_MSG[] = {
            "Unkown internal scanner error", "Error: could not match input",
            "Error: pushback value was too large" };

    /**
     * ZZ_ATTRIBUTE[aState] contains the attributes of state
     * <code>aState</code>.
     */
    private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\2\0\1\1\3\0\1\11\4\1\1\11\2\1\7\11"
            + "\2\1\1\11\1\1\4\11\4\1\1\11\1\1\2\11"
            + "\1\1\1\11\2\1\2\11\1\1\2\11\5\1\1\11"
            + "\15\0\2\1\1\11\2\0\1\11\3\0\1\1\1\0"
            + "\2\11\41\0\1\11\17\0\1\1\47\0\1\11";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[168];
        int offset = 0;
        offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAttribute(String packed, int offset, int[] result) {
        int i = 0; /* Index in packed string */
        int j = offset; /* Index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }

    /** The input device. */
    private Reader zzReader;

    /** The current state of the DFA. */
    private int zzState;

    /** The current lexical state. */
    private int zzLexicalState = YYINITIAL;

    /**
     * This buffer contains the current text to be matched and is the source of
     * the yytext() string.
     */
    private char zzBuffer[];

    /** The text position at the last accepting state. */
    private int zzMarkedPos;

    /** The current text position in the buffer. */
    private int zzCurrentPos;

    /** startRead marks the beginning of the yytext() string in the buffer. */
    private int zzStartRead;

    /**
     * endRead marks the last character in the buffer, that has been read from
     * input.
     */
    private int zzEndRead;

    /** zzAtEOF == true <=> the scanner is at the EOF. */
    private boolean zzAtEOF;

    /* User code: */

    /** Internal type denoting a line ending in a CSS property. */
    public static final int INTERNAL_CSS_PROPERTY = -1;

    /** Internal type denoting a line ending in a CSS property value. */
    public static final int INTERNAL_CSS_VALUE = -2;

    /**
     * Internal type denoting line ending in a CSS double-quote string.
     * The state to return to is embedded in the actual end token type.
     */
    public static final int INTERNAL_CSS_STRING = -(1 << 11);

    /**
     * Internal type denoting line ending in a CSS single-quote string.
     * The state to return to is embedded in the actual end token type.
     */
    public static final int INTERNAL_CSS_CHAR = -(2 << 11);

    /**
     * Internal type denoting line ending in a CSS multi-line comment.
     * The state to return to is embedded in the actual end token type.
     */
    public static final int INTERNAL_CSS_MLC = -(3 << 11);

    /**
     * The state previous CSS-related state we were in before going into a CSS
     * string, multi-line comment, etc..
     */
    private int cssPrevState;

    /**
     * Ctor. This must be here because JFlex does not generate a no-parameter
     * constructor.
     */
    public CSSTokenMaker() {
        super();
    }

    /**
     * Adds the token specified to the current linked list of tokens as an "end
     * token"; that is, at <code>zzMarkedPos</code>.
     *
     * @param tokenType The token's type.
     */
    private void addEndToken(int tokenType) {
        addToken(zzMarkedPos, zzMarkedPos, tokenType);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param tokenType The token's type.
     * @see #addToken(int, int, int)
     */
    private void addHyperlinkToken(int start, int end, int tokenType) {
        int so = start + offsetShift;
        addToken(zzBuffer, start, end, tokenType, so, true);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param tokenType The token's type.
     */
    private void addToken(int tokenType) {
        addToken(zzStartRead, zzMarkedPos - 1, tokenType);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param tokenType The token's type.
     */
    private void addToken(int start, int end, int tokenType) {
        int so = start + offsetShift;
        addToken(zzBuffer, start, end, tokenType, so);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param array The character array.
     * @param start The starting offset in the array.
     * @param end The ending offset in the array.
     * @param tokenType The token's type.
     * @param startOffset The offset in the document at which this token occurs.
     */
    @Override
    public void addToken(char[] array, int start, int end, int tokenType,
            int startOffset) {
        super.addToken(array, start, end, tokenType, startOffset);
        zzStartRead = zzMarkedPos;
    }

    /**
     * Returns the closest {@link ITokenTypes "standard" token type} for a given
     * "internal" token type (e.g. one whose value is <code>&lt; 0</code>).
     */
    @Override
    public int getClosestStandardTokenTypeForInternalType(int type) {
        switch (type) {
        case INTERNAL_CSS_STRING:
        case INTERNAL_CSS_CHAR:
            return ITokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
        case INTERNAL_CSS_MLC:
            return ITokenTypes.COMMENT_MULTILINE;
        }
        return type;
    }

    /**
     * Returns <code>true</code> since CSS uses curly braces.
     *
     * @return <code>true</code> always.
     */
    public boolean getCurlyBracesDenoteCodeBlocks() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean getMarkOccurrencesOfTokenType(int type) {
        return type == IToken.RESERVED_WORD; // Used for CSS keys
    }

    /** {@inheritDoc} */
    @Override
    public boolean getShouldIndentNextLineAfter(IToken t) {
        if (t != null && t.length() == 1) {
            char ch = t.charAt(0);
            return ch == '{' || ch == '(';
        }
        return false;
    }

    /**
     * Returns the first token in the linked list of tokens generated from
     * <code>text</code>. This method must be implemented by subclasses so they
     * can correctly implement syntax highlighting.
     *
     * @param text The text from which to get tokens.
     * @param initialTokenType The token type we should start with.
     * @param startOffset The offset into the document at which
     *        <code>text</code> starts.
     * @return The first <code>IToken</code> in a linked list representing the
     *         syntax highlighted text.
     */
    @Override
    public IToken getTokenList(Segment text, int initialTokenType,
            int startOffset) {
        resetTokenList();
        this.offsetShift = -text.offset + startOffset;
        cssPrevState = YYINITIAL; // Shouldn't be necessary

        // Start off in the proper state
        int state = IToken.NULL;
        switch (initialTokenType) {
        case IToken.LITERAL_STRING_DOUBLE_QUOTE:
            state = CSS_STRING;
            break;
        case IToken.LITERAL_CHAR:
            state = CSS_CHAR_LITERAL;
            break;
        case IToken.COMMENT_MULTILINE:
            state = CSS_C_STYLE_COMMENT;
            break;
        case INTERNAL_CSS_PROPERTY:
            state = CSS_PROPERTY;
            break;
        case INTERNAL_CSS_VALUE:
            state = CSS_VALUE;
            break;
        default:
            if (initialTokenType < -1024) {
                int main = -(-initialTokenType & 0xffffff00);
                switch (main) {
                default: // Should never happen
                case INTERNAL_CSS_STRING:
                    state = CSS_STRING;
                    break;
                case INTERNAL_CSS_CHAR:
                    state = CSS_CHAR_LITERAL;
                    break;
                case INTERNAL_CSS_MLC:
                    state = CSS_C_STYLE_COMMENT;
                    break;
                }
                cssPrevState = -initialTokenType & 0xff;
            } else {
                state = IToken.NULL;
            }
        }

        start = text.offset;
        s = text;
        try {
            yyreset(zzReader);
            yybegin(state);
            return yylex();
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
            return new TokenImpl();
        }
    }

    /** Overridden to accept letters, digits, underscores and hyphens. */
    @Override
    public boolean isIdentifierChar(int languageIndex, char ch) {
        return Character.isLetterOrDigit(ch) || ch == '-' || ch == '.'
                || ch == '_';
    }

    /**
     * Refills the input buffer.
     *
     * @return      <code>true</code> if EOF was reached, otherwise
     *              <code>false</code>.
     */
    private boolean zzRefill() {
        return zzCurrentPos >= s.offset + s.count;
    }

    /**
     * Resets the scanner to read from a new input stream.
     * Does not close the old reader.
     *
     * All internal variables are reset, the old input stream <b>cannot</b> be
     * reused (internal buffer is discarded and lost).
     * Lexical state is set to <tt>YY_INITIAL</tt>.
     *
     * @param reader   The new input stream.
     */
    public final void yyreset(Reader reader) {
        // 's' has been updated
        zzBuffer = s.array;
        /*
         * We replaced the line below with the two below it because zzRefill no
         * longer "refills" the buffer (since the way we do it, it's always
         * "full" the first time through, since it points to the segment's
         * array). So, we assign zzEndRead here
         */
        //zzStartRead = zzEndRead = s.offset;
        zzStartRead = s.offset;
        zzEndRead = zzStartRead + s.count - 1;
        zzCurrentPos = zzMarkedPos = s.offset;
        zzLexicalState = YYINITIAL;
        zzReader = reader;
        zzAtEOF = false;
    }

    /**
     * Creates a new scanner.
     * There is also a java.io.InputStream version of this constructor.
     *
     * @param   in  The java.io.Reader to read input from.
     */
    public CSSTokenMaker(Reader in) {
        this.zzReader = in;
    }

    /**
     * Creates a new scanner.
     * There is also java.io.Reader version of this constructor.
     *
     * @param   in  The java.io.Inputstream to read input from.
     */
    public CSSTokenMaker(InputStream in) {
        this(new InputStreamReader(in));
    }

    /**
     * Unpacks the compressed character translation table.
     *
     * @param packed   The packed character translation table.
     * @return         The unpacked character translation table.
     */
    private static char[] zzUnpackCMap(String packed) {
        char[] map = new char[0x10000];
        int i = 0; /* Index in packed string */
        int j = 0; /* Index in unpacked array */
        while (i < 134) {
            int count = packed.charAt(i++);
            char value = packed.charAt(i++);
            do {
                map[j++] = value;
            } while (--count > 0);
        }
        return map;
    }

    /** Closes the input stream. */
    public final void yyclose() throws IOException {
        zzAtEOF = true; /* Indicate end of file */
        zzEndRead = zzStartRead; /* Invalidate buffer */

        if (zzReader != null) {
            zzReader.close();
        }
    }

    /** Returns the current lexical state. */
    public final int yystate() {
        return zzLexicalState;
    }

    /**
     * Enters a new lexical state.
     *
     * @param newState The new lexical state.
     */
    @Override
    public final void yybegin(int newState) {
        zzLexicalState = newState;
    }

    /** Returns the text matched by the current regular expression. */
    public final String yytext() {
        return new String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
    }

    /**
     * Returns the character at position <tt>pos</tt> from the matched text.
     *
     * It is equivalent to yytext().charAt(pos), but faster.
     *
     * @param pos The position of the character to fetch.
     *            A value from 0 to yylength()-1.
     *
     * @return The character at position pos.
     */
    public final char yycharat(int pos) {
        return zzBuffer[zzStartRead + pos];
    }

    /** Returns the length of the matched text region. */
    public final int yylength() {
        return zzMarkedPos - zzStartRead;
    }

    /**
     * Reports an error that occured while scanning.
     *
     * In a wellformed scanner (no or only correct usage of yypushback(int) and
     * a match-all fallback rule) this method will only be called with things
     * that "Can't Possibly Happen".
     * If this method is called, something is seriously wrong (e.g. a JFlex bug
     * producing a faulty scanner etc..).
     *
     * Usual syntax/scanner level error handling should be done in error
     * fallback rules.
     *
     * @param   errorCode  The code of the error message to display.
     */
    private void zzScanError(int errorCode) {
        String message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
        }

        throw new Error(message);
    }

    /**
     * Pushes the specified amount of characters back into the input stream.
     *
     * They will be read again by then next call of the scanning method.
     *
     * @param number  The number of characters to be read again.
     *                This number must not be greater than yylength().
     */
    public void yypushback(int number) {
        if (number > yylength()) {
            zzScanError(ZZ_PUSHBACK_2BIG);
        }

        zzMarkedPos -= number;
    }

    /**
     * Resumes scanning until the next regular expression is matched, the end of
     * input is encountered or an I/O-Error occurs.
     *
     * @return      The next token.
     * @exception   java.io.IOException  if any I/O-Error occurs.
     */
    public IToken yylex() throws IOException {
        int zzInput;
        int zzAction;

        // Cached fields:
        int zzCurrentPosL;
        int zzMarkedPosL;
        int zzEndReadL = zzEndRead;
        char[] zzBufferL = zzBuffer;
        char[] zzCMapL = ZZ_CMAP;

        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;

        while (true) {
            zzMarkedPosL = zzMarkedPos;

            zzAction = -1;

            zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

            zzState = zzLexicalState;

            zzForAction: {
                while (true) {
                    if (zzCurrentPosL < zzEndReadL) {
                        zzInput = zzBufferL[zzCurrentPosL++];
                    } else if (zzAtEOF) {
                        zzInput = YYEOF;
                        break zzForAction;
                    } else {
                        // Store back cached positions
                        zzCurrentPos = zzCurrentPosL;
                        zzMarkedPos = zzMarkedPosL;
                        boolean eof = zzRefill();
                        // Get translated positions and possibly new buffer
                        zzCurrentPosL = zzCurrentPos;
                        zzMarkedPosL = zzMarkedPos;
                        zzBufferL = zzBuffer;
                        zzEndReadL = zzEndRead;
                        if (eof) {
                            zzInput = YYEOF;
                            break zzForAction;
                        } else {
                            zzInput = zzBufferL[zzCurrentPosL++];
                        }
                    }
                    int zzNext = zzTransL[zzRowMapL[zzState] + zzCMapL[zzInput]];
                    if (zzNext == -1) {
                        break zzForAction;
                    }
                    zzState = zzNext;

                    int zzAttributes = zzAttrL[zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction = zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) == 8) {
                            break zzForAction;
                        }
                    }
                }
            }

            // Store back cached position
            zzMarkedPos = zzMarkedPosL;

            switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
            case 1: {
                addToken(IToken.IDENTIFIER);
            }
            case 37:
                break;
            case 2: {
                addToken(IToken.IDENTIFIER);
            }
            case 38:
                break;
            case 22: {
                addEndToken(INTERNAL_CSS_VALUE);
                return firstToken;
            }
            case 39:
                break;
            case 36: {
                addToken(IToken.ANNOTATION);
            }
            case 40:
                break;
            case 32: {
                addToken(IToken.VARIABLE);
            }
            case 41:
                break;
            case 8: {
                addToken(IToken.SEPARATOR);
                yybegin(CSS_PROPERTY);
            }
            case 42:
                break;
            case 26: {
                addToken(start, zzStartRead, IToken.LITERAL_STRING_DOUBLE_QUOTE);
                yybegin(cssPrevState);
            }
            case 43:
                break;
            case 29: {
                addToken(start, zzStartRead - 1, IToken.LITERAL_CHAR);
                addEndToken(INTERNAL_CSS_CHAR - cssPrevState);
                return firstToken;
            }
            case 44:
                break;
            case 33: {
                start = zzMarkedPos - 2;
                cssPrevState = zzLexicalState;
                yybegin(CSS_C_STYLE_COMMENT);
            }
            case 45:
                break;
            case 28: {
                addToken(start, zzStartRead, IToken.LITERAL_CHAR);
                yybegin(cssPrevState);
            }
            case 46:
                break;
            case 34: {
                addToken(start, zzStartRead + 1, IToken.COMMENT_MULTILINE);
                yybegin(cssPrevState);
            }
            case 47:
                break;
            case 9: {
                start = zzMarkedPos - 1;
                cssPrevState = zzLexicalState;
                yybegin(CSS_STRING);
            }
            case 48:
                break;
            case 14: {
                addToken(IToken.OPERATOR);
                yybegin(CSS_VALUE);
            }
            case 49:
                break;
            case 4: { /* Unknown pseudo class */
                addToken(IToken.DATA_TYPE);
            }
            case 50:
                break;
            case 20: {
                addToken(IToken.OPERATOR);
                yybegin(CSS_PROPERTY);
            }
            case 51:
                break;
            case 25: { /* Skip escaped chars */
            }
            case 52:
                break;
            case 31: {
                addToken(IToken.REGEX);
            }
            case 53:
                break;
            case 16: {
                addEndToken(INTERNAL_CSS_PROPERTY);
                return firstToken;
            }
            case 54:
                break;
            case 21: {
                int temp = zzMarkedPos - 2;
                addToken(zzStartRead, temp, IToken.FUNCTION);
                addToken(zzMarkedPos - 1, zzMarkedPos - 1, IToken.SEPARATOR);
                zzStartRead = zzCurrentPos = zzMarkedPos;
            }
            case 55:
                break;
            case 6: {
                addToken(IToken.WHITESPACE);
            }
            case 56:
                break;
            case 17: {
                addToken(IToken.SEPARATOR);
                yybegin(YYINITIAL);
            }
            case 57:
                break;
            case 3: {
                addToken(IToken.DATA_TYPE);
            }
            case 58:
                break;
            case 15: {
                addToken(IToken.SEPARATOR); /* Helps with auto-closing curlies when editing CSS */
            }
            case 59:
                break;
            case 19: {
                addToken(IToken.LITERAL_NUMBER_DECIMAL_INT);
            }
            case 60:
                break;
            case 27: {
                addToken(start, zzStartRead - 1,
                        IToken.LITERAL_STRING_DOUBLE_QUOTE);
                addEndToken(INTERNAL_CSS_STRING - cssPrevState);
                return firstToken;
            }
            case 61:
                break;
            case 30: {
                addToken(start, zzStartRead - 1, IToken.COMMENT_MULTILINE);
                addEndToken(INTERNAL_CSS_MLC - cssPrevState);
                return firstToken;
            }
            case 62:
                break;
            case 23: { /* End of a function */
                addToken(IToken.SEPARATOR);
            }
            case 63:
                break;
            case 12: {
                addToken(IToken.IDENTIFIER);
            }
            case 64:
                break;
            case 13: {
                addToken(IToken.RESERVED_WORD);
            }
            case 65:
                break;
            case 35: {
                int temp = zzStartRead;
                addToken(start, zzStartRead - 1, IToken.COMMENT_MULTILINE);
                addHyperlinkToken(temp, zzMarkedPos - 1,
                        IToken.COMMENT_MULTILINE);
                start = zzMarkedPos;
            }
            case 66:
                break;
            case 10: {
                start = zzMarkedPos - 1;
                cssPrevState = zzLexicalState;
                yybegin(CSS_CHAR_LITERAL);
            }
            case 67:
                break;
            case 5: {
                addToken(IToken.SEPARATOR);
            }
            case 68:
                break;
            case 11: {
                addNullToken();
                return firstToken;
            }
            case 69:
                break;
            case 7: {
                addToken(IToken.OPERATOR);
            }
            case 70:
                break;
            case 18: {
                addToken(IToken.IDENTIFIER);
            }
            case 71:
                break;
            case 24: {
            }
            case 72:
                break;
            default:
                if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                    zzAtEOF = true;
                    switch (zzLexicalState) {
                    case CSS_C_STYLE_COMMENT: {
                        addToken(start, zzStartRead - 1,
                                IToken.COMMENT_MULTILINE);
                        addEndToken(INTERNAL_CSS_MLC - cssPrevState);
                        return firstToken;
                    }
                    case 169:
                        break;
                    case YYINITIAL: {
                        addNullToken();
                        return firstToken;
                    }
                    case 170:
                        break;
                    case CSS_STRING: {
                        addToken(start, zzStartRead - 1,
                                IToken.LITERAL_STRING_DOUBLE_QUOTE);
                        addEndToken(INTERNAL_CSS_STRING - cssPrevState);
                        return firstToken;
                    }
                    case 171:
                        break;
                    case CSS_VALUE: {
                        addEndToken(INTERNAL_CSS_VALUE);
                        return firstToken;
                    }
                    case 172:
                        break;
                    case CSS_PROPERTY: {
                        addEndToken(INTERNAL_CSS_PROPERTY);
                        return firstToken;
                    }
                    case 173:
                        break;
                    case CSS_CHAR_LITERAL: {
                        addToken(start, zzStartRead - 1, IToken.LITERAL_CHAR);
                        addEndToken(INTERNAL_CSS_CHAR - cssPrevState);
                        return firstToken;
                    }
                    case 174:
                        break;
                    default:
                        return null;
                    }
                } else {
                    zzScanError(ZZ_NO_MATCH);
                }
            }
        }
    }
}