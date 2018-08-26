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
import net.sourceforge.open_teradata_viewer.editor.syntax.AbstractJFlexTokenMaker;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.TokenImpl;

/**
 * Scanner for the Python programming language.
 *
 * @author D. Campione
 * 
 */
public class PythonTokenMaker extends AbstractJFlexTokenMaker {

    /** This character denotes the end of file. */
    public static final int YYEOF = -1;

    /** Lexical states. */
    public static final int YYINITIAL = 0;
    public static final int LONG_STRING_2 = 2;
    public static final int LONG_STRING_1 = 1;

    /** Translates characters to character classes. */
    private static final String ZZ_CMAP_PACKED = "\11\0\1\34\1\12\1\0\1\34\1\32\22\0\1\34\1\64\1\11"
            + "\1\33\1\0\1\63\1\67\1\10\1\57\1\57\1\62\1\61\1\64"
            + "\1\27\1\24\1\63\1\16\7\21\2\3\1\64\1\72\1\65\1\60"
            + "\1\66\1\64\1\71\4\23\1\26\1\23\3\2\1\31\1\2\1\15"
            + "\5\2\1\6\2\2\1\7\2\2\1\20\2\2\1\57\1\13\1\57"
            + "\1\64\1\1\1\0\1\35\1\42\1\44\1\37\1\25\1\22\1\52"
            + "\1\54\1\46\1\30\1\43\1\14\1\51\1\36\1\45\1\47\1\2"
            + "\1\4\1\40\1\41\1\5\1\55\1\53\1\17\1\50\1\56\1\57"
            + "\1\70\1\57\1\64\uff81\0";

    /** Translates characters to character classes. */
    private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

    /** Translates DFA states to action switch labels. */
    private static final int[] ZZ_ACTION = zzUnpackAction();

    private static final String ZZ_ACTION_PACKED_0 = "\3\0\1\1\1\2\1\3\4\2\2\4\1\5\1\2"
            + "\1\3\2\2\1\6\1\2\1\6\1\7\1\10\21\2"
            + "\1\11\10\6\1\2\2\12\2\4\1\13\1\3\1\14"
            + "\1\13\1\14\4\2\2\4\1\0\2\4\1\0\4\2"
            + "\1\13\1\3\1\13\12\2\1\15\27\2\1\15\3\2"
            + "\2\15\1\16\1\15\15\2\3\0\2\14\1\0\12\2"
            + "\1\4\1\17\1\20\3\2\1\3\15\2\1\15\22\2"
            + "\1\21\14\2\1\22\1\23\1\14\6\2\1\21\3\2"
            + "\1\15\36\2\1\15\17\2\1\15\6\2";

    private static int[] zzUnpackAction() {
        int[] result = new int[265];
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

    private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\73\0\166\0\261\0\354\0\u0127\0\u0162\0\u019d"
            + "\0\u01d8\0\u0213\0\u024e\0\u0289\0\261\0\u02c4\0\u02ff\0\u033a"
            + "\0\u0375\0\u03b0\0\u03eb\0\u0426\0\u0461\0\u049c\0\u04d7\0\u0512"
            + "\0\u054d\0\u0588\0\u05c3\0\u05fe\0\u0639\0\u0674\0\u06af\0\u06ea"
            + "\0\u0725\0\u0760\0\u079b\0\u07d6\0\u0811\0\u084c\0\u0887\0\261"
            + "\0\u08c2\0\u08fd\0\u0938\0\261\0\u0973\0\u09ae\0\u09e9\0\u0a24"
            + "\0\261\0\u0a5f\0\u0a9a\0\u0ad5\0\u0b10\0\u0b4b\0\u0b4b\0\u0b86"
            + "\0\u0bc1\0\u0b4b\0\u0bfc\0\u0c37\0\u0c72\0\u0cad\0\u0ce8\0\u0d23"
            + "\0\u0d5e\0\u0d99\0\u0dd4\0\u0e0f\0\u0e4a\0\u0e85\0\u0ec0\0\u0efb"
            + "\0\u0f36\0\u0f71\0\u0fac\0\u0fe7\0\u1022\0\u105d\0\u1098\0\u10d3"
            + "\0\u110e\0\u1149\0\u1184\0\u11bf\0\u11fa\0\u1235\0\u1270\0\u12ab"
            + "\0\u12e6\0\u1321\0\u135c\0\u1397\0\u13d2\0\u140d\0\u1448\0\u1483"
            + "\0\u14be\0\u14f9\0\u1534\0\u156f\0\u15aa\0\u15e5\0\u1620\0\u165b"
            + "\0\u1696\0\u16d1\0\u170c\0\u1747\0\u1782\0\u17bd\0\u17f8\0\u1833"
            + "\0\u186e\0\354\0\u18a9\0\354\0\u18e4\0\u191f\0\u195a\0\u1995"
            + "\0\u19d0\0\u1a0b\0\u1a46\0\u1a81\0\u1abc\0\u1af7\0\u1b32\0\u1b6d"
            + "\0\u1ba8\0\u1be3\0\u1c1e\0\u1c59\0\u1c94\0\261\0\u1ccf\0\u1d0a"
            + "\0\u1d45\0\u1d80\0\u1dbb\0\u1df6\0\u1e31\0\u1e6c\0\u1ea7\0\u1ee2"
            + "\0\u1f1d\0\u1f58\0\261\0\261\0\261\0\u1f93\0\u1fce\0\u2009"
            + "\0\u2044\0\u207f\0\u20ba\0\u20f5\0\u2130\0\u216b\0\u21a6\0\u21e1"
            + "\0\u221c\0\u2257\0\u2292\0\u22cd\0\u2308\0\u2343\0\u237e\0\u23b9"
            + "\0\u23f4\0\u242f\0\u237e\0\u246a\0\u24a5\0\u24e0\0\u251b\0\u2556"
            + "\0\u2591\0\u25cc\0\u2607\0\u2642\0\u267d\0\u26b8\0\u26f3\0\u272e"
            + "\0\u2769\0\u27a4\0\u27df\0\u281a\0\u2855\0\u2890\0\u28cb\0\u2906"
            + "\0\u2941\0\u297c\0\u29b7\0\u29f2\0\u2a2d\0\u2a68\0\261\0\261"
            + "\0\u2aa3\0\u2ade\0\u2b19\0\u2b54\0\u17bd\0\u2b8f\0\u2bca\0\354"
            + "\0\u2c05\0\u2c40\0\u2c7b\0\u2cb6\0\u2cf1\0\u2d2c\0\u2d67\0\u2da2"
            + "\0\u2ddd\0\u2e18\0\u2e53\0\u2e8e\0\u2ec9\0\u2f04\0\u2f3f\0\u2f7a"
            + "\0\u2fb5\0\u2ff0\0\u302b\0\u3066\0\u30a1\0\u30dc\0\u3117\0\u3152"
            + "\0\u318d\0\u31c8\0\u3203\0\u323e\0\u3279\0\u32b4\0\u32ef\0\u332a"
            + "\0\u3365\0\u33a0\0\u33db\0\u3416\0\u3451\0\u348c\0\u34c7\0\u3502"
            + "\0\u353d\0\u3578\0\u35b3\0\u35ee\0\u33db\0\u3629\0\u3664\0\u369f"
            + "\0\u36da\0\u3715\0\u1270\0\u3750\0\u378b\0\u37c6\0\u3801\0\u383c"
            + "\0\u3877";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[265];
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

    private static final String ZZ_TRANS_PACKED_0 = "\1\4\2\5\1\6\1\7\1\10\1\11\1\12\1\13"
            + "\1\14\1\15\1\4\1\16\1\5\1\17\1\20\1\5"
            + "\1\6\1\21\1\5\1\22\1\23\1\5\1\24\2\5"
            + "\1\4\1\25\1\26\1\27\1\30\1\31\1\32\1\33"
            + "\1\34\1\5\1\35\1\36\1\37\1\40\1\41\1\42"
            + "\1\43\1\44\1\45\1\46\1\47\1\50\1\51\1\52"
            + "\1\53\1\51\1\54\1\55\1\56\1\57\1\60\2\61"
            + "\10\62\1\63\62\62\11\64\1\65\61\64\74\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\22\5"
            + "\14\0\3\66\1\6\4\66\3\0\1\66\2\67\1\6"
            + "\2\66\1\6\2\66\1\70\2\71\1\0\2\72\1\0"
            + "\1\66\1\0\22\66\12\0\1\66\2\0\7\5\1\13"
            + "\1\14\2\0\10\5\1\0\1\73\1\5\1\0\2\5"
            + "\3\0\1\74\7\5\1\75\11\5\15\0\3\5\1\11"
            + "\1\5\1\11\1\5\1\13\1\14\2\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\1\5\1\76\20\5\15\0"
            + "\7\5\1\13\1\14\2\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\15\0\3\5\1\11\1\5\1\11"
            + "\1\5\1\13\1\14\2\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\14\0\10\77\1\100\1\77\1\0"
            + "\1\101\57\77\11\102\1\103\1\0\1\104\57\102\1\0"
            + "\7\5\4\0\10\5\1\0\1\105\1\5\1\0\2\5"
            + "\3\0\1\106\7\5\1\107\1\110\10\5\14\0\3\66"
            + "\1\111\4\66\3\0\1\66\2\67\1\112\2\113\1\112"
            + "\2\66\1\70\2\71\1\0\2\72\1\0\1\66\1\0"
            + "\22\66\12\0\1\66\2\0\3\5\1\114\3\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\22\5\15\0"
            + "\3\5\1\115\3\5\4\0\1\116\7\5\1\0\2\5"
            + "\1\0\2\5\3\0\10\5\1\117\1\120\10\5\17\0"
            + "\1\70\12\0\1\70\2\0\1\70\52\0\7\5\4\0"
            + "\1\121\2\5\1\122\4\5\1\0\2\5\1\0\2\5"
            + "\3\0\1\5\1\123\16\5\1\124\1\5\43\0\1\54"
            + "\30\0\1\54\12\0\12\25\1\0\60\25\34\0\1\26"
            + "\37\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\1\5\1\125\1\5\1\126\1\5\1\127\4\5"
            + "\1\130\7\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\10\5\1\131\11\5\15\0\7\5"
            + "\4\0\10\5\1\0\1\132\1\5\1\0\2\5\3\0"
            + "\10\5\1\133\1\134\10\5\15\0\4\5\1\135\2\5"
            + "\4\0\1\136\7\5\1\0\1\137\1\5\1\0\2\5"
            + "\3\0\4\5\1\140\4\5\1\141\5\5\1\142\2\5"
            + "\15\0\3\5\1\143\1\144\2\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\13\5\1\145\6\5\15\0"
            + "\3\5\1\146\1\147\2\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\10\5\1\150\11\5\15\0\7\5"
            + "\4\0\1\151\7\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\152\7\5\1\153\3\5\1\154\2\5\1\155\2\5"
            + "\15\0\3\5\1\156\3\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\5\5\1\157\1\5\1\160\2\5"
            + "\1\161\7\5\15\0\7\5\4\0\6\5\1\162\1\5"
            + "\1\0\2\5\1\0\2\5\3\0\1\5\1\163\1\164"
            + "\1\165\1\166\7\5\1\167\5\5\15\0\3\5\1\170"
            + "\3\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\171\7\5\1\172\11\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\11\5\1\173\10\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\1\174\10\5\1\105\10\5\15\0\7\5\4\0"
            + "\1\175\7\5\1\0\1\137\1\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\17\5\1\176\2\5\15\0\7\5\4\0"
            + "\10\5\1\0\1\177\1\5\1\0\2\5\3\0\1\200"
            + "\21\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\1\201\7\5\1\202\11\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\11\5"
            + "\1\154\10\5\74\0\1\54\72\0\2\54\71\0\1\54"
            + "\1\0\1\54\75\0\1\51\73\0\1\51\73\0\1\54"
            + "\73\0\1\54\2\0\10\62\1\0\62\62\10\0\1\203"
            + "\62\0\11\64\1\0\61\64\11\0\1\204\61\0\10\66"
            + "\3\0\11\66\1\0\2\66\1\0\2\66\1\0\1\66"
            + "\1\0\22\66\12\0\1\66\4\0\1\70\12\0\1\70"
            + "\2\0\1\70\3\0\2\205\1\0\2\206\41\0\3\66"
            + "\1\207\4\66\3\0\3\66\1\207\2\66\1\207\2\66"
            + "\1\0\2\66\1\210\2\66\1\0\1\66\1\0\22\66"
            + "\2\0\1\210\7\0\1\66\2\0\7\5\4\0\1\211"
            + "\7\5\1\0\2\5\1\0\2\5\3\0\2\5\1\212"
            + "\1\5\1\213\5\5\1\214\7\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\1\5\1\215"
            + "\7\5\1\216\4\5\1\217\3\5\15\0\4\5\1\220"
            + "\2\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\3\5\1\221\5\5\1\222\10\5\14\0"
            + "\10\77\1\223\1\77\1\0\1\101\57\77\10\0\1\224"
            + "\62\0\12\77\1\0\60\77\11\102\1\223\1\0\1\104"
            + "\57\102\11\0\1\225\61\0\12\102\1\0\60\102\1\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\5\1\164\20\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\14\5\1\226\5\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\5\1\227\5\5\1\230\12\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\3\5\1\160"
            + "\16\5\14\0\3\66\1\111\4\66\3\0\3\66\1\111"
            + "\2\66\1\111\2\66\1\70\2\71\1\0\2\72\1\0"
            + "\1\66\1\0\22\66\12\0\1\66\1\0\3\66\1\111"
            + "\4\66\3\0\1\66\2\67\1\112\2\66\1\112\2\66"
            + "\1\70\2\71\1\0\2\72\1\0\1\66\1\0\22\66"
            + "\12\0\1\66\1\0\3\66\1\231\4\66\3\0\3\66"
            + "\1\231\2\66\3\231\1\0\2\231\1\0\2\66\1\0"
            + "\1\66\1\0\1\231\1\66\1\231\2\66\1\231\1\66"
            + "\1\231\12\66\12\0\1\66\2\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\1\232\21\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\10\5\1\233\11\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\10\5\1\234\11\5\15\0"
            + "\3\5\1\162\3\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\15\0\7\5\4\0\1\235\7\5"
            + "\1\0\2\5\1\0\2\5\3\0\1\5\1\236\20\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\3\5\1\237\5\5\1\240\10\5\15\0\7\5"
            + "\4\0\10\5\1\0\1\241\1\5\1\0\2\5\3\0"
            + "\7\5\1\242\12\5\15\0\4\5\1\243\2\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\22\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\244\21\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\2\5\1\162\17\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\3\5"
            + "\1\245\16\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\3\5\1\164\16\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\12\5"
            + "\1\246\7\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\4\5\1\162\15\5\15\0\7\5"
            + "\4\0\1\247\5\5\1\162\1\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\15\0\4\5\1\250\2\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\22\5\15\0"
            + "\3\5\1\164\3\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\7\5\1\160\10\5\1\251\1\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\12\5\1\166\1\5\1\164\5\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\11\5\1\252"
            + "\10\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\4\5\1\253\15\5\15\0\3\5\1\164"
            + "\3\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\254\21\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\15\5\1\255\4\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\10\5"
            + "\1\256\11\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\13\5\1\162\6\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\12\5"
            + "\1\257\7\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\12\5\1\260\7\5\15\0\7\5"
            + "\4\0\10\5\1\0\1\261\1\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\6\5\1\262\1\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\10\5\1\244"
            + "\11\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\1\263\21\5\15\0\7\5\4\0\1\264"
            + "\7\5\1\0\2\5\1\0\2\5\3\0\22\5\15\0"
            + "\7\5\4\0\10\5\1\0\1\265\1\5\1\0\2\5"
            + "\3\0\1\5\1\266\12\5\1\267\5\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\12\5"
            + "\1\164\7\5\15\0\3\5\1\164\3\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\1\270\21\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\2\5\1\164\17\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\1\271\1\5\3\0\22\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\4\5"
            + "\1\164\15\5\15\0\7\5\4\0\10\5\1\0\1\105"
            + "\1\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\4\5\1\272"
            + "\5\5\1\273\7\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\3\5\1\274\5\5\1\275"
            + "\10\5\15\0\7\5\4\0\10\5\1\0\1\214\1\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\12\5\1\276\7\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\10\5\1\277\1\300\10\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\3\5\1\301"
            + "\16\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\16\5\1\164\3\5\15\0\7\5\4\0"
            + "\10\5\1\0\1\302\1\5\1\0\2\5\3\0\22\5"
            + "\15\0\7\5\4\0\3\5\1\164\4\5\1\0\2\5"
            + "\1\0\2\5\3\0\12\5\1\164\7\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\10\5"
            + "\1\303\11\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\11\5\1\304\10\5\15\0\7\5"
            + "\4\0\3\5\1\164\4\5\1\0\2\5\1\0\2\5"
            + "\3\0\22\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\3\5\1\305\16\5\15\0\3\5"
            + "\1\127\3\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\22\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\11\5\1\306\10\5\24\0\1\307"
            + "\73\0\1\310\64\0\1\311\12\0\1\311\2\0\1\311"
            + "\5\0\1\210\31\0\1\210\11\0\3\66\1\207\4\66"
            + "\3\0\3\66\1\207\2\66\1\207\2\66\1\0\2\66"
            + "\1\0\2\72\1\0\1\66\1\0\22\66\12\0\1\66"
            + "\4\0\1\311\12\0\1\311\2\0\1\311\52\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\10\5"
            + "\1\312\11\5\15\0\4\5\1\252\2\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\22\5\15\0\4\5"
            + "\1\313\2\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\22\5\15\0\3\5\1\164\3\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\22\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\15\5"
            + "\1\260\4\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\3\5\1\237\16\5\15\0\1\314"
            + "\6\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\1\5\1\315\20\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\11\5\1\141"
            + "\10\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\7\5\1\316\12\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\5\5\1\317"
            + "\14\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\15\5\1\320\4\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\1\321\21\5"
            + "\14\0\3\66\1\231\4\66\3\0\1\66\2\67\1\231"
            + "\2\66\3\231\1\0\2\231\1\0\2\66\1\0\1\66"
            + "\1\0\1\231\1\66\1\231\2\66\1\231\1\66\1\231"
            + "\12\66\12\0\1\66\2\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\1\5\1\215\20\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\14\5\1\162\5\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\1\322\21\5\15\0\7\5"
            + "\4\0\10\5\1\0\1\164\1\5\1\0\2\5\3\0"
            + "\4\5\1\166\15\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\1\323\21\5\15\0\7\5"
            + "\4\0\10\5\1\0\1\162\1\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\6\5\1\162\1\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\7\5\1\324"
            + "\12\5\15\0\7\5\4\0\10\5\1\0\1\325\1\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\14\5\1\326\5\5"
            + "\15\0\7\5\4\0\1\164\7\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\15\0\7\5\4\0\10\5\1\0"
            + "\1\327\1\5\1\0\2\5\3\0\22\5\15\0\7\5"
            + "\4\0\1\330\7\5\1\0\2\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\1\331\21\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\5\5\1\332\14\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\14\5\1\333\5\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\7\5\1\260\12\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\4\5\1\334\15\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\1\5\1\335\20\5"
            + "\15\0\3\5\1\322\3\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\1\260"
            + "\7\5\1\0\2\5\1\0\2\5\3\0\22\5\15\0"
            + "\7\5\4\0\10\5\1\0\1\164\1\5\1\0\2\5"
            + "\3\0\22\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\1\336\21\5\15\0\7\5\4\0"
            + "\6\5\1\166\1\5\1\0\2\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\3\5\1\337\16\5\15\0\7\5\4\0"
            + "\1\340\7\5\1\0\2\5\1\0\2\5\3\0\22\5"
            + "\15\0\3\5\1\252\3\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\4\5\1\341\15\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\12\5\1\342\7\5\15\0\3\5\1\320\3\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\22\5"
            + "\15\0\7\5\4\0\10\5\1\0\1\343\1\5\1\0"
            + "\2\5\3\0\22\5\15\0\7\5\4\0\10\5\1\0"
            + "\1\344\1\5\1\0\2\5\3\0\22\5\15\0\4\5"
            + "\1\160\2\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\22\5\15\0\4\5\1\345\2\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\22\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\1\5"
            + "\1\346\20\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\10\5\1\327\11\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\12\5"
            + "\1\347\7\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\1\5\1\131\20\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\3\5"
            + "\1\162\16\5\15\0\7\5\4\0\1\125\7\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\5\5\1\350"
            + "\14\5\15\0\7\5\4\0\1\237\7\5\1\0\2\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\1\331\16\5\1\164"
            + "\2\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\2\5\1\320\17\5\17\0\1\311\12\0"
            + "\1\311\2\0\1\311\6\0\2\206\42\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\1\315\21\5"
            + "\15\0\3\5\1\351\3\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\11\5\1\352\10\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\10\5\1\353\6\5\1\214\2\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\2\5"
            + "\1\354\17\5\15\0\7\5\4\0\1\127\7\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\4\5\1\320"
            + "\15\5\15\0\7\5\4\0\1\355\7\5\1\0\2\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\6\5"
            + "\1\356\1\5\1\0\2\5\1\0\2\5\3\0\22\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\12\5\1\131\7\5\15\0\7\5\4\0\10\5"
            + "\1\0\1\357\1\5\1\0\2\5\3\0\22\5\15\0"
            + "\3\5\1\131\3\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\13\5\1\164\6\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\4\5\1\360\15\5\15\0\7\5\4\0\1\361\7\5"
            + "\1\0\2\5\1\0\2\5\3\0\22\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\10\5"
            + "\1\315\11\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\11\5\1\362\10\5\15\0\7\5"
            + "\4\0\10\5\1\0\1\306\1\5\1\0\2\5\3\0"
            + "\22\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\6\5\1\162\13\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\3\5\1\363"
            + "\16\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\1\364\21\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\11\5\1\365\10\5"
            + "\15\0\7\5\4\0\1\366\7\5\1\0\2\5\1\0"
            + "\2\5\3\0\11\5\1\257\10\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\7\5\1\160"
            + "\12\5\15\0\3\5\1\105\3\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\5\5\1\367"
            + "\14\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\3\5\1\370\16\5\15\0\7\5\4\0"
            + "\10\5\1\0\1\371\1\5\1\0\2\5\3\0\22\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\1\372\21\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\1\5\1\162\20\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\5\1\373\20\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\2\5\1\260\17\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\162\21\5\15\0\7\5\4\0\1\143\7\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\11\5\1\257"
            + "\10\5\15\0\3\5\1\374\3\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\22\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\4\5\1\214"
            + "\15\5\15\0\7\5\4\0\10\5\1\0\1\320\1\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\7\5\1\375\12\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\14\5\1\376\5\5\15\0\7\5\4\0\10\5"
            + "\1\0\2\5\1\0\2\5\3\0\5\5\1\257\14\5"
            + "\15\0\7\5\4\0\10\5\1\0\2\5\1\0\2\5"
            + "\3\0\1\5\1\377\20\5\15\0\7\5\4\0\10\5"
            + "\1\0\1\177\1\5\1\0\2\5\3\0\22\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\7\5\1\u0100\12\5\15\0\7\5\4\0\10\5\1\0"
            + "\2\5\1\0\2\5\3\0\4\5\1\u0101\15\5\15\0"
            + "\3\5\1\u0102\3\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\22\5\15\0\7\5\4\0\1\u0103\7\5"
            + "\1\0\2\5\1\0\2\5\3\0\22\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\12\5"
            + "\1\273\7\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\1\u0104\21\5\15\0\7\5\4\0"
            + "\10\5\1\0\1\u0105\1\5\1\0\2\5\3\0\22\5"
            + "\15\0\4\5\1\237\2\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\22\5\15\0\7\5\4\0\1\u0106"
            + "\7\5\1\0\2\5\1\0\2\5\3\0\22\5\15\0"
            + "\7\5\4\0\10\5\1\0\2\5\1\0\2\5\3\0"
            + "\1\u0107\21\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\4\5\1\330\15\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\4\5"
            + "\1\260\15\5\15\0\7\5\4\0\10\5\1\0\2\5"
            + "\1\0\2\5\3\0\4\5\1\u0108\15\5\15\0\7\5"
            + "\4\0\10\5\1\0\2\5\1\0\2\5\3\0\1\u0109"
            + "\21\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\1\5\1\252\20\5\15\0\7\5\4\0"
            + "\10\5\1\0\2\5\1\0\2\5\3\0\17\5\1\333"
            + "\2\5\15\0\7\5\4\0\10\5\1\0\2\5\1\0"
            + "\2\5\3\0\3\5\1\127\16\5\14\0";

    private static int[] zzUnpackTrans() {
        int[] result = new int[14514];
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

    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\3\0\1\11\10\1\1\11\32\1\1\11\3\1\1\11"
            + "\4\1\1\11\17\1\1\0\2\1\1\0\76\1\3\0"
            + "\1\11\1\1\1\0\12\1\3\11\61\1\2\11\101\1";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[265];
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

    /**
     * Ctor. This must be here because JFlex does not generate a no-parameter
     * constructor.
     */
    public PythonTokenMaker() {
        super();
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

    /** {@inheritDoc} */
    @Override
    public String[] getLineCommentStartAndEnd(int languageIndex) {
        return new String[] { "#", null };
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
     * @return The first <code>Token</code> in a linked list representing the
     *         syntax highlighted text.
     */
    @Override
    public IToken getTokenList(Segment text, int initialTokenType,
            int startOffset) {
        resetTokenList();
        this.offsetShift = -text.offset + startOffset;

        // Start off in the proper state
        int state = IToken.NULL;
        switch (initialTokenType) {
        case IToken.LITERAL_STRING_DOUBLE_QUOTE:
            state = LONG_STRING_2;
            break;
        case IToken.LITERAL_CHAR:
            state = LONG_STRING_1;
            break;
        default:
            state = IToken.NULL;
        }

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

    /**
     * Resets the scanner to read from a new input stream.
     * Does not close the old reader.
     *
     * All internal variables are reset, the old input stream 
     * <b>cannot</b> be reused (internal buffer is discarded and lost).
     * Lexical state is set to <tt>YY_INITIAL</tt>.
     *
     * @param reader   The new input stream.
     */
    public final void yyreset(Reader reader) throws IOException {
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
     * Refills the input buffer.
     *
     * @return      <code>true</code> if EOF was reached, otherwise
     *              <code>false</code>.
     * @exception   IOException  if any I/O-Error occurs.
     */
    private boolean zzRefill() throws IOException {
        return zzCurrentPos >= s.offset + s.count;
    }

    /**
     * Creates a new scanner.
     * There is also a java.io.InputStream version of this constructor.
     *
     * @param   in  The java.io.Reader to read input from.
     */
    public PythonTokenMaker(Reader in) {
        this.zzReader = in;
    }

    /**
     * Creates a new scanner.
     * There is also java.io.Reader version of this constructor.
     *
     * @param   in  The java.io.Inputstream to read input from.
     */
    public PythonTokenMaker(InputStream in) {
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
        while (i < 168) {
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
     * It is equivalent to yytext().charAt(pos) but faster.
     *
     * @param pos The position of the character to fetch. 
     *            A value from 0 to yylength()-1.
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
            case 5: {
                addNullToken();
                return firstToken;
            }
            case 20:
                break;
            case 10: {
                addToken(IToken.LITERAL_CHAR);
            }
            case 21:
                break;
            case 8: {
                addToken(IToken.WHITESPACE);
            }
            case 22:
                break;
            case 12: {
                addToken(IToken.LITERAL_NUMBER_FLOAT);
            }
            case 23:
                break;
            case 13: {
                addToken(IToken.RESERVED_WORD);
            }
            case 24:
                break;
            case 9: {
                addToken(IToken.SEPARATOR);
            }
            case 25:
                break;
            case 15: {
                yybegin(LONG_STRING_1);
                addToken(IToken.LITERAL_CHAR);
            }
            case 26:
                break;
            case 2: {
                addToken(IToken.IDENTIFIER);
            }
            case 27:
                break;
            case 14: {
                addToken(IToken.FUNCTION);
            }
            case 28:
                break;
            case 1: {
                addToken(IToken.ERROR_IDENTIFIER);
            }
            case 29:
                break;
            case 17: {
                addToken(IToken.DATA_TYPE);
            }
            case 30:
                break;
            case 4: {
                addToken(IToken.LITERAL_STRING_DOUBLE_QUOTE);
            }
            case 31:
                break;
            case 7: {
                addToken(IToken.COMMENT_EOL);
            }
            case 32:
                break;
            case 11: {
                addToken(IToken.ERROR_NUMBER_FORMAT);
            }
            case 33:
                break;
            case 3: {
                addToken(IToken.LITERAL_NUMBER_DECIMAL_INT);
            }
            case 34:
                break;
            case 6: {
                addToken(IToken.OPERATOR);
            }
            case 35:
                break;
            case 19: {
                yybegin(YYINITIAL);
                addToken(IToken.LITERAL_STRING_DOUBLE_QUOTE);
            }
            case 36:
                break;
            case 16: {
                yybegin(LONG_STRING_2);
                addToken(IToken.LITERAL_STRING_DOUBLE_QUOTE);
            }
            case 37:
                break;
            case 18: {
                yybegin(YYINITIAL);
                addToken(IToken.LITERAL_CHAR);
            }
            case 38:
                break;
            default:
                if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                    zzAtEOF = true;
                    switch (zzLexicalState) {
                    case YYINITIAL: {
                        addNullToken();
                        return firstToken;
                    }
                    case 266:
                        break;
                    case LONG_STRING_2: {
                        if (firstToken == null) {
                            addToken(IToken.LITERAL_STRING_DOUBLE_QUOTE);
                        }
                        return firstToken;
                    }
                    case 267:
                        break;
                    case LONG_STRING_1: {
                        if (firstToken == null) {
                            addToken(IToken.LITERAL_CHAR);
                        }
                        return firstToken;
                    }
                    case 268:
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