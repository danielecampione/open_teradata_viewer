/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.parser;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Token Manager.
 * 
 * @author D. Campione
 * 
 */
public class CCSqlParserTokenManager implements ICCSqlParserConstants {

    /** Debug output. */
    public PrintStream debugStream = System.out;

    /** Set debug output. */
    public void setDebugStream(PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0,
            long active1) {
        switch (pos) {
            case 0 :
                if ((active0 & 0xffffffffffffffe0L) != 0L
                        || (active1 & 0x3L) != 0L) {
                    jjmatchedKind = 71;
                    return 36;
                }
                if ((active1 & 0x100000000L) != 0L)
                    return 8;
                if ((active1 & 0x20000L) != 0L)
                    return 1;
                if ((active1 & 0x80000000L) != 0L)
                    return 5;
                return -1;
            case 1 :
                if ((active0 & 0xff7f67ffffbbf000L) != 0L
                        || (active1 & 0x1L) != 0L) {
                    if (jjmatchedPos != 1) {
                        jjmatchedKind = 71;
                        jjmatchedPos = 1;
                    }
                    return 36;
                }
                if ((active0 & 0x80980000440fe0L) != 0L
                        || (active1 & 0x2L) != 0L)
                    return 36;
                return -1;
            case 2 :
                if ((active0 & 0xffffffffffe00000L) != 0L
                        || (active1 & 0x3L) != 0L) {
                    jjmatchedKind = 71;
                    jjmatchedPos = 2;
                    return 36;
                }
                if ((active0 & 0x1ff000L) != 0L)
                    return 36;
                return -1;
            case 3 :
                if ((active0 & 0xffffffe000000000L) != 0L
                        || (active1 & 0x3L) != 0L) {
                    jjmatchedKind = 71;
                    jjmatchedPos = 3;
                    return 36;
                }
                if ((active0 & 0x1fffe00000L) != 0L)
                    return 36;
                return -1;
            case 4 :
                if ((active0 & 0xfffe000000000000L) != 0L
                        || (active1 & 0x3L) != 0L) {
                    jjmatchedKind = 71;
                    jjmatchedPos = 4;
                    return 36;
                }
                if ((active0 & 0x1ffe000000000L) != 0L)
                    return 36;
                return -1;
            case 5 :
                if ((active0 & 0xf800000000000000L) != 0L
                        || (active1 & 0x3L) != 0L) {
                    jjmatchedKind = 71;
                    jjmatchedPos = 5;
                    return 36;
                }
                if ((active0 & 0x7fe000000000000L) != 0L)
                    return 36;
                return -1;
            case 6 :
                if ((active0 & 0x8000000000000000L) != 0L
                        || (active1 & 0x3L) != 0L) {
                    jjmatchedKind = 71;
                    jjmatchedPos = 6;
                    return 36;
                }
                if ((active0 & 0x7800000000000000L) != 0L)
                    return 36;
                return -1;
            case 7 :
                if ((active1 & 0x2L) != 0L) {
                    jjmatchedKind = 71;
                    jjmatchedPos = 7;
                    return 36;
                }
                if ((active0 & 0x8000000000000000L) != 0L
                        || (active1 & 0x1L) != 0L)
                    return 36;
                return -1;
            default :
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0, long active1) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1),
                pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (curChar) {
            case 33 :
                return jjMoveStringLiteralDfa1_0(0x0L, 0x2000000L);
            case 38 :
                return jjStopAtPos(0, 93);
            case 40 :
                return jjStopAtPos(0, 79);
            case 41 :
                return jjStopAtPos(0, 80);
            case 42 :
                return jjStopAtPos(0, 82);
            case 43 :
                return jjStopAtPos(0, 94);
            case 44 :
                return jjStopAtPos(0, 78);
            case 45 :
                return jjStartNfaWithStates_0(0, 95, 5);
            case 46 :
                return jjStartNfaWithStates_0(0, 81, 1);
            case 47 :
                return jjStartNfaWithStates_0(0, 96, 8);
            case 59 :
                return jjStopAtPos(0, 76);
            case 60 :
                jjmatchedKind = 85;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x1800000L);
            case 61 :
                return jjStopAtPos(0, 77);
            case 62 :
                jjmatchedKind = 84;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x400000L);
            case 63 :
                return jjStopAtPos(0, 83);
            case 64 :
                return jjMoveStringLiteralDfa1_0(0x0L, 0x4000000L);
            case 94 :
                return jjStopAtPos(0, 97);
            case 65 :
            case 97 :
                return jjMoveStringLiteralDfa1_0(0x47020L, 0x0L);
            case 66 :
            case 98 :
                return jjMoveStringLiteralDfa1_0(0x4000040000000040L, 0x0L);
            case 67 :
            case 99 :
                return jjMoveStringLiteralDfa1_0(0x4000040000000L, 0x0L);
            case 68 :
            case 100 :
                return jjMoveStringLiteralDfa1_0(0x2000002200080L, 0x1L);
            case 69 :
            case 101 :
                return jjMoveStringLiteralDfa1_0(0x420000200100000L, 0x0L);
            case 70 :
            case 102 :
                return jjMoveStringLiteralDfa1_0(0x810000000L, 0x0L);
            case 71 :
            case 103 :
                return jjMoveStringLiteralDfa1_0(0x20000000000L, 0x0L);
            case 72 :
            case 104 :
                return jjMoveStringLiteralDfa1_0(0x40000000000000L, 0x0L);
            case 73 :
            case 105 :
                return jjMoveStringLiteralDfa1_0(0x80180000400300L, 0x2L);
            case 74 :
            case 106 :
                return jjMoveStringLiteralDfa1_0(0x4000000L, 0x0L);
            case 75 :
            case 107 :
                return jjMoveStringLiteralDfa1_0(0x8000L, 0x0L);
            case 76 :
            case 108 :
                return jjMoveStringLiteralDfa1_0(0x200009000000L, 0x0L);
            case 78 :
            case 110 :
                return jjMoveStringLiteralDfa1_0(0x1000000000810000L, 0x0L);
            case 79 :
            case 111 :
                return jjMoveStringLiteralDfa1_0(0x10c00020000c00L, 0x0L);
            case 80 :
            case 112 :
                return jjMoveStringLiteralDfa1_0(0x800000000000000L, 0x0L);
            case 82 :
            case 114 :
                return jjMoveStringLiteralDfa1_0(0x2001000000000000L, 0x0L);
            case 83 :
            case 115 :
                return jjMoveStringLiteralDfa1_0(0x8000400020000L, 0x0L);
            case 84 :
            case 116 :
                return jjMoveStringLiteralDfa1_0(0x8000002100080000L, 0x0L);
            case 85 :
            case 117 :
                return jjMoveStringLiteralDfa1_0(0x100018000000000L, 0x0L);
            case 86 :
            case 118 :
                return jjMoveStringLiteralDfa1_0(0x200000000000000L, 0x0L);
            case 87 :
            case 119 :
                return jjMoveStringLiteralDfa1_0(0x5080000000L, 0x0L);
            case 123 :
                return jjMoveStringLiteralDfa1_0(0x0L, 0x7400000000L);
            case 124 :
                jjmatchedKind = 92;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x8000000L);
            case 125 :
                return jjStopAtPos(0, 99);
            default :
                return jjMoveNfa_0(7, 0);
        }
    }

    private int jjMoveStringLiteralDfa1_0(long active0, long active1) {
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(0, active0, active1);
            return 1;
        }
        switch (curChar) {
            case 61 :
                if ((active1 & 0x400000L) != 0L)
                    return jjStopAtPos(1, 86);
                else if ((active1 & 0x800000L) != 0L)
                    return jjStopAtPos(1, 87);
                else if ((active1 & 0x2000000L) != 0L)
                    return jjStopAtPos(1, 89);
                break;
            case 62 :
                if ((active1 & 0x1000000L) != 0L)
                    return jjStopAtPos(1, 88);
                break;
            case 64 :
                if ((active1 & 0x4000000L) != 0L)
                    return jjStopAtPos(1, 90);
                break;
            case 65 :
            case 97 :
                return jjMoveStringLiteralDfa2_0(active0, 0x1240002040000000L,
                        active1, 0L);
            case 68 :
            case 100 :
                if ((active1 & 0x400000000L) != 0L)
                    return jjStopAtPos(1, 98);
                break;
            case 69 :
            case 101 :
                return jjMoveStringLiteralDfa2_0(active0, 0x600a040008228000L,
                        active1, 0L);
            case 70 :
            case 102 :
                return jjMoveStringLiteralDfa2_0(active0, 0x10000000000000L,
                        active1, 0x4000000000L);
            case 72 :
            case 104 :
                return jjMoveStringLiteralDfa2_0(active0, 0x4180000000L,
                        active1, 0L);
            case 73 :
            case 105 :
                return jjMoveStringLiteralDfa2_0(active0, 0x1201001000000L,
                        active1, 0x1L);
            case 76 :
            case 108 :
                return jjMoveStringLiteralDfa2_0(active0, 0x200001000L,
                        active1, 0L);
            case 78 :
            case 110 :
                if ((active0 & 0x200L) != 0L) {
                    jjmatchedKind = 9;
                    jjmatchedPos = 1;
                } else if ((active0 & 0x800L) != 0L)
                    return jjStartNfaWithStates_0(1, 11, 36);
                return jjMoveStringLiteralDfa2_0(active0, 0x80190000506000L,
                        active1, 0x2L);
            case 79 :
            case 111 :
                if ((active0 & 0x80L) != 0L)
                    return jjStartNfaWithStates_0(1, 7, 36);
                return jjMoveStringLiteralDfa2_0(active0, 0x404090000L,
                        active1, 0L);
            case 80 :
            case 112 :
                return jjMoveStringLiteralDfa2_0(active0, 0x100000020000000L,
                        active1, 0L);
            case 82 :
            case 114 :
                if ((active0 & 0x400L) != 0L) {
                    jjmatchedKind = 10;
                    jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 0x8804820012000000L,
                        active1, 0L);
            case 83 :
            case 115 :
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 5;
                    jjmatchedPos = 1;
                } else if ((active0 & 0x100L) != 0L)
                    return jjStartNfaWithStates_0(1, 8, 36);
                return jjMoveStringLiteralDfa2_0(active0, 0x400008000040000L,
                        active1, 0L);
            case 84 :
            case 116 :
                if ((active1 & 0x1000000000L) != 0L) {
                    jjmatchedKind = 100;
                    jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1,
                        0x2000000000L);
            case 85 :
            case 117 :
                return jjMoveStringLiteralDfa2_0(active0, 0x400800800000L,
                        active1, 0L);
            case 88 :
            case 120 :
                return jjMoveStringLiteralDfa2_0(active0, 0x20000000000000L,
                        active1, 0L);
            case 89 :
            case 121 :
                if ((active0 & 0x40L) != 0L)
                    return jjStartNfaWithStates_0(1, 6, 36);
                break;
            case 124 :
                if ((active1 & 0x8000000L) != 0L)
                    return jjStopAtPos(1, 91);
                break;
            default :
                break;
        }
        return jjStartNfa_0(0, active0, active1);
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(0, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(1, active0, active1);
            return 2;
        }
        switch (curChar) {
            case 66 :
            case 98 :
                return jjMoveStringLiteralDfa3_0(active0, 0x2000000000L,
                        active1, 0L);
            case 67 :
            case 99 :
                if ((active0 & 0x40000L) != 0L)
                    return jjStartNfaWithStates_0(2, 18, 36);
                return jjMoveStringLiteralDfa3_0(active0, 0x400000000000000L,
                        active1, 0L);
            case 68 :
            case 100 :
                if ((active0 & 0x2000L) != 0L)
                    return jjStartNfaWithStates_0(2, 13, 36);
                else if ((active0 & 0x100000L) != 0L)
                    return jjStartNfaWithStates_0(2, 20, 36);
                return jjMoveStringLiteralDfa3_0(active0, 0x100880000000000L,
                        active1, 0L);
            case 69 :
            case 101 :
                return jjMoveStringLiteralDfa3_0(active0, 0x40041a0000000L,
                        active1, 0L);
            case 70 :
            case 102 :
                return jjMoveStringLiteralDfa3_0(active0, 0x10000008000000L,
                        active1, 0L);
            case 71 :
            case 103 :
                return jjMoveStringLiteralDfa3_0(active0, 0x1040000000000L,
                        active1, 0L);
            case 73 :
            case 105 :
                return jjMoveStringLiteralDfa3_0(active0, 0x820018004000000L,
                        active1, 0L);
            case 75 :
            case 107 :
                return jjMoveStringLiteralDfa3_0(active0, 0x1000000L, active1,
                        0L);
            case 76 :
            case 108 :
                if ((active0 & 0x1000L) != 0L)
                    return jjStartNfaWithStates_0(2, 12, 36);
                return jjMoveStringLiteralDfa3_0(active0, 0x20a000800800000L,
                        active1, 0L);
            case 77 :
            case 109 :
                return jjMoveStringLiteralDfa3_0(active0, 0x200400000000L,
                        active1, 0L);
            case 78 :
            case 110 :
                if ((active1 & 0x4000000000L) != 0L)
                    return jjStopAtPos(2, 102);
                return jjMoveStringLiteralDfa3_0(active0, 0x100000000000L,
                        active1, 0L);
            case 79 :
            case 111 :
                return jjMoveStringLiteralDfa3_0(active0, 0x20012000000L,
                        active1, 0L);
            case 80 :
            case 112 :
                if ((active0 & 0x80000L) != 0L)
                    return jjStartNfaWithStates_0(2, 19, 36);
                return jjMoveStringLiteralDfa3_0(active0, 0x2000000000000000L,
                        active1, 0L);
            case 83 :
            case 115 :
                if ((active1 & 0x2000000000L) != 0L)
                    return jjStopAtPos(2, 101);
                return jjMoveStringLiteralDfa3_0(active0, 0x80000240200000L,
                        active1, 0x1L);
            case 84 :
            case 116 :
                if ((active0 & 0x10000L) != 0L)
                    return jjStartNfaWithStates_0(2, 16, 36);
                else if ((active0 & 0x20000L) != 0L)
                    return jjStartNfaWithStates_0(2, 17, 36);
                return jjMoveStringLiteralDfa3_0(active0, 0x5000401000400000L,
                        active1, 0x2L);
            case 85 :
            case 117 :
                return jjMoveStringLiteralDfa3_0(active0, 0x8000000000000000L,
                        active1, 0L);
            case 86 :
            case 118 :
                return jjMoveStringLiteralDfa3_0(active0, 0x40000000000000L,
                        active1, 0L);
            case 89 :
            case 121 :
                if ((active0 & 0x4000L) != 0L)
                    return jjStartNfaWithStates_0(2, 14, 36);
                else if ((active0 & 0x8000L) != 0L)
                    return jjStartNfaWithStates_0(2, 15, 36);
                break;
            default :
                break;
        }
        return jjStartNfa_0(1, active0, active1);
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(1, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(2, active0, active1);
            return 3;
        }
        switch (curChar) {
            case 65 :
            case 97 :
                return jjMoveStringLiteralDfa4_0(active0, 0x504000000000000L,
                        active1, 0L);
            case 67 :
            case 99 :
                if ((active0 & 0x200000L) != 0L)
                    return jjStartNfaWithStates_0(3, 21, 36);
                break;
            case 69 :
            case 101 :
                if ((active0 & 0x1000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 24, 36);
                else if ((active0 & 0x40000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 30, 36);
                else if ((active0 & 0x200000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 33, 36);
                else if ((active0 & 0x400000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 34, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0x8ad80000000000L,
                        active1, 0x2L);
            case 72 :
            case 104 :
                if ((active0 & 0x1000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 36, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0x1000000000000L,
                        active1, 0L);
            case 73 :
            case 105 :
                return jjMoveStringLiteralDfa4_0(active0, 0x40240000000000L,
                        active1, 0L);
            case 76 :
            case 108 :
                if ((active0 & 0x800000L) != 0L)
                    return jjStartNfaWithStates_0(3, 23, 36);
                else if ((active0 & 0x800000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 35, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0x2000002000000000L,
                        active1, 0L);
            case 77 :
            case 109 :
                if ((active0 & 0x10000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 28, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0x800000000000000L,
                        active1, 0L);
            case 78 :
            case 110 :
                if ((active0 & 0x4000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 26, 36);
                else if ((active0 & 0x20000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 29, 36);
                else if ((active0 & 0x80000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 31, 36);
                else if ((active0 & 0x100000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 32, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0x8000008000000000L,
                        active1, 0L);
            case 79 :
            case 111 :
                if ((active0 & 0x400000L) != 0L)
                    return jjStartNfaWithStates_0(3, 22, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0x10000000000L,
                        active1, 0L);
            case 80 :
            case 112 :
                if ((active0 & 0x2000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 25, 36);
                break;
            case 82 :
            case 114 :
                return jjMoveStringLiteralDfa4_0(active0, 0x4000000000L,
                        active1, 0L);
            case 83 :
            case 115 :
                return jjMoveStringLiteralDfa4_0(active0, 0x30000000000000L,
                        active1, 0L);
            case 84 :
            case 116 :
                if ((active0 & 0x8000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 27, 36);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x1L);
            case 85 :
            case 117 :
                return jjMoveStringLiteralDfa4_0(active0, 0x1200020000000000L,
                        active1, 0L);
            case 87 :
            case 119 :
                return jjMoveStringLiteralDfa4_0(active0, 0x4000000000000000L,
                        active1, 0L);
            default :
                break;
        }
        return jjStartNfa_0(2, active0, active1);
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(2, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(3, active0, active1);
            return 4;
        }
        switch (curChar) {
            case 65 :
            case 97 :
                return jjMoveStringLiteralDfa5_0(active0, 0x2800000000000000L,
                        active1, 0L);
            case 67 :
            case 99 :
                return jjMoveStringLiteralDfa5_0(active0, 0x8008000000000000L,
                        active1, 0L);
            case 69 :
            case 101 :
                if ((active0 & 0x2000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 37, 36);
                else if ((active0 & 0x4000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 38, 36);
                return jjMoveStringLiteralDfa5_0(active0, 0x4210000000000000L,
                        active1, 0L);
            case 71 :
            case 103 :
                if ((active0 & 0x8000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 39, 36);
                break;
            case 73 :
            case 105 :
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x1L);
            case 78 :
            case 110 :
                if ((active0 & 0x10000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 40, 36);
                else if ((active0 & 0x40000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 42, 36);
                return jjMoveStringLiteralDfa5_0(active0, 0x40000000000000L,
                        active1, 0L);
            case 80 :
            case 112 :
                if ((active0 & 0x20000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 41, 36);
                return jjMoveStringLiteralDfa5_0(active0, 0x400000000000000L,
                        active1, 0L);
            case 82 :
            case 114 :
                if ((active0 & 0x100000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 44, 36);
                else if ((active0 & 0x400000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 46, 36);
                else if ((active0 & 0x800000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 47, 36);
                return jjMoveStringLiteralDfa5_0(active0, 0x1080000000000000L,
                        active1, 0x2L);
            case 84 :
            case 116 :
                if ((active0 & 0x200000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 45, 36);
                else if ((active0 & 0x1000000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 48, 36);
                return jjMoveStringLiteralDfa5_0(active0, 0x126000000000000L,
                        active1, 0L);
            case 88 :
            case 120 :
                if ((active0 & 0x80000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 43, 36);
                break;
            default :
                break;
        }
        return jjStartNfa_0(3, active0, active1);
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(3, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(4, active0, active1);
            return 5;
        }
        switch (curChar) {
            case 65 :
            case 97 :
                return jjMoveStringLiteralDfa6_0(active0, 0x9000000000000000L,
                        active1, 0L);
            case 67 :
            case 99 :
                return jjMoveStringLiteralDfa6_0(active0, 0x2000000000000000L,
                        active1, 0L);
            case 69 :
            case 101 :
                if ((active0 & 0x2000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 49, 36);
                else if ((active0 & 0x4000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 50, 36);
                else if ((active0 & 0x100000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 56, 36);
                else if ((active0 & 0x400000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 58, 36);
                return jjMoveStringLiteralDfa6_0(active0, 0x4000000000000000L,
                        active1, 0L);
            case 71 :
            case 103 :
                if ((active0 & 0x40000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 54, 36);
                break;
            case 78 :
            case 110 :
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x1L);
            case 82 :
            case 114 :
                return jjMoveStringLiteralDfa6_0(active0, 0x800000000000000L,
                        active1, 0L);
            case 83 :
            case 115 :
                if ((active0 & 0x20000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 53, 36);
                else if ((active0 & 0x200000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 57, 36);
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x2L);
            case 84 :
            case 116 :
                if ((active0 & 0x8000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 51, 36);
                else if ((active0 & 0x10000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 52, 36);
                else if ((active0 & 0x80000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 55, 36);
                break;
            default :
                break;
        }
        return jjStartNfa_0(4, active0, active1);
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(4, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(5, active0, active1);
            return 6;
        }
        switch (curChar) {
            case 67 :
            case 99 :
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x1L);
            case 69 :
            case 101 :
                if ((active0 & 0x2000000000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 61, 36);
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x2L);
            case 76 :
            case 108 :
                if ((active0 & 0x1000000000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 60, 36);
                break;
            case 78 :
            case 110 :
                if ((active0 & 0x4000000000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 62, 36);
                break;
            case 84 :
            case 116 :
                return jjMoveStringLiteralDfa7_0(active0, 0x8000000000000000L,
                        active1, 0L);
            case 89 :
            case 121 :
                if ((active0 & 0x800000000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 59, 36);
                break;
            default :
                break;
        }
        return jjStartNfa_0(5, active0, active1);
    }

    private int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(5, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(6, active0, active1);
            return 7;
        }
        switch (curChar) {
            case 67 :
            case 99 :
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x2L);
            case 69 :
            case 101 :
                if ((active0 & 0x8000000000000000L) != 0L)
                    return jjStartNfaWithStates_0(7, 63, 36);
                break;
            case 84 :
            case 116 :
                if ((active1 & 0x1L) != 0L)
                    return jjStartNfaWithStates_0(7, 64, 36);
                break;
            default :
                break;
        }
        return jjStartNfa_0(6, active0, active1);
    }

    private int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1,
            long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L)
            return jjStartNfa_0(6, old0, old1);
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            jjStopStringLiteralDfa_0(7, 0L, active1);
            return 8;
        }
        switch (curChar) {
            case 84 :
            case 116 :
                if ((active1 & 0x2L) != 0L)
                    return jjStartNfaWithStates_0(8, 65, 36);
                break;
            default :
                break;
        }
        return jjStartNfa_0(7, 0L, active1);
    }

    private int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (IOException ioe) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    static final long[] jjbitVec0 = {0x0L, 0x0L, 0xffffffffffffffffL,
            0xffffffffffffffffL};

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 36;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (;;) {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch (jjstateSet[--i]) {
                        case 36 :
                        case 16 :
                            if ((0x3ff001000000000L & l) == 0L)
                                break;
                            if (kind > 71)
                                kind = 71;
                            jjCheckNAdd(16);
                            break;
                        case 7 :
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 67)
                                    kind = 67;
                                jjCheckNAddStates(0, 5);
                            } else if (curChar == 34)
                                jjCheckNAddTwoStates(23, 24);
                            else if (curChar == 39)
                                jjCheckNAddTwoStates(18, 19);
                            else if (curChar == 47)
                                jjstateSet[jjnewStateCnt++] = 8;
                            else if (curChar == 45)
                                jjstateSet[jjnewStateCnt++] = 5;
                            else if (curChar == 46)
                                jjCheckNAdd(1);
                            break;
                        case 0 :
                            if (curChar == 46)
                                jjCheckNAdd(1);
                            break;
                        case 1 :
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 66)
                                kind = 66;
                            jjCheckNAddTwoStates(1, 2);
                            break;
                        case 3 :
                            if ((0x280000000000L & l) != 0L)
                                jjCheckNAdd(4);
                            break;
                        case 4 :
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 66)
                                kind = 66;
                            jjCheckNAdd(4);
                            break;
                        case 5 :
                            if (curChar != 45)
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjCheckNAdd(6);
                            break;
                        case 6 :
                            if ((0xffffffffffffdbffL & l) == 0L)
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjCheckNAdd(6);
                            break;
                        case 8 :
                            if (curChar == 42)
                                jjCheckNAddTwoStates(9, 10);
                            break;
                        case 9 :
                            if ((0xfffffbffffffffffL & l) != 0L)
                                jjCheckNAddTwoStates(9, 10);
                            break;
                        case 10 :
                            if (curChar == 42)
                                jjCheckNAddStates(6, 8);
                            break;
                        case 11 :
                            if ((0xffff7bffffffffffL & l) != 0L)
                                jjCheckNAddTwoStates(12, 10);
                            break;
                        case 12 :
                            if ((0xfffffbffffffffffL & l) != 0L)
                                jjCheckNAddTwoStates(12, 10);
                            break;
                        case 13 :
                            if (curChar == 47 && kind > 70)
                                kind = 70;
                            break;
                        case 14 :
                            if (curChar == 47)
                                jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 17 :
                            if (curChar == 39)
                                jjCheckNAddTwoStates(18, 19);
                            break;
                        case 18 :
                            if ((0xffffff7fffffffffL & l) != 0L)
                                jjCheckNAddTwoStates(18, 19);
                            break;
                        case 19 :
                            if (curChar != 39)
                                break;
                            if (kind > 74)
                                kind = 74;
                            jjstateSet[jjnewStateCnt++] = 20;
                            break;
                        case 20 :
                            if (curChar == 39)
                                jjCheckNAddTwoStates(21, 19);
                            break;
                        case 21 :
                            if ((0xffffff7fffffffffL & l) != 0L)
                                jjCheckNAddTwoStates(21, 19);
                            break;
                        case 22 :
                            if (curChar == 34)
                                jjCheckNAddTwoStates(23, 24);
                            break;
                        case 23 :
                            if ((0xfffffffbffffdbffL & l) != 0L)
                                jjCheckNAddTwoStates(23, 24);
                            break;
                        case 24 :
                            if (curChar == 34 && kind > 75)
                                kind = 75;
                            break;
                        case 26 :
                            if ((0xffffffffffffdbffL & l) != 0L)
                                jjAddStates(9, 10);
                            break;
                        case 28 :
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 67)
                                kind = 67;
                            jjCheckNAddStates(0, 5);
                            break;
                        case 29 :
                            if ((0x3ff000000000000L & l) != 0L)
                                jjCheckNAddTwoStates(29, 0);
                            break;
                        case 30 :
                            if ((0x3ff000000000000L & l) != 0L)
                                jjCheckNAddStates(11, 13);
                            break;
                        case 31 :
                            if (curChar == 46)
                                jjCheckNAdd(32);
                            break;
                        case 33 :
                            if ((0x280000000000L & l) != 0L)
                                jjCheckNAdd(34);
                            break;
                        case 34 :
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 66)
                                kind = 66;
                            jjCheckNAdd(34);
                            break;
                        case 35 :
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 67)
                                kind = 67;
                            jjCheckNAdd(35);
                            break;
                        default :
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        case 36 :
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 71)
                                    kind = 71;
                                jjCheckNAdd(16);
                            }
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 71)
                                    kind = 71;
                                jjCheckNAddTwoStates(15, 16);
                            }
                            break;
                        case 7 :
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 71)
                                    kind = 71;
                                jjCheckNAddTwoStates(15, 16);
                            } else if (curChar == 96)
                                jjCheckNAddTwoStates(26, 27);
                            break;
                        case 2 :
                            if ((0x2000000020L & l) != 0L)
                                jjAddStates(14, 15);
                            break;
                        case 6 :
                            if (kind > 69)
                                kind = 69;
                            jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 9 :
                            jjCheckNAddTwoStates(9, 10);
                            break;
                        case 11 :
                        case 12 :
                            jjCheckNAddTwoStates(12, 10);
                            break;
                        case 15 :
                            if ((0x7fffffe87fffffeL & l) == 0L)
                                break;
                            if (kind > 71)
                                kind = 71;
                            jjCheckNAddTwoStates(15, 16);
                            break;
                        case 16 :
                            if ((0x7fffffe87fffffeL & l) == 0L)
                                break;
                            if (kind > 71)
                                kind = 71;
                            jjCheckNAdd(16);
                            break;
                        case 18 :
                            jjCheckNAddTwoStates(18, 19);
                            break;
                        case 21 :
                            jjCheckNAddTwoStates(21, 19);
                            break;
                        case 23 :
                            jjAddStates(16, 17);
                            break;
                        case 25 :
                            if (curChar == 96)
                                jjCheckNAddTwoStates(26, 27);
                            break;
                        case 26 :
                            if ((0xfffffffeffffffffL & l) != 0L)
                                jjCheckNAddTwoStates(26, 27);
                            break;
                        case 27 :
                            if (curChar == 96 && kind > 75)
                                kind = 75;
                            break;
                        case 32 :
                            if ((0x2000000020L & l) != 0L)
                                jjAddStates(18, 19);
                            break;
                        default :
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        case 6 :
                            if ((jjbitVec0[i2] & l2) == 0L)
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 9 :
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjCheckNAddTwoStates(9, 10);
                            break;
                        case 11 :
                        case 12 :
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjCheckNAddTwoStates(12, 10);
                            break;
                        case 18 :
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjCheckNAddTwoStates(18, 19);
                            break;
                        case 21 :
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjCheckNAddTwoStates(21, 19);
                            break;
                        case 23 :
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjAddStates(16, 17);
                            break;
                        case 26 :
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjAddStates(9, 10);
                            break;
                        default :
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 36 - (jjnewStateCnt = startsAt)))
                return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (IOException ioe) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = {29, 0, 30, 31, 32, 35, 10, 11, 13, 26,
            27, 30, 31, 32, 3, 4, 23, 24, 33, 34,};

    /** Token literal values. */
    public static final String[] jjstrLiteralImages = {"", null, null, null,
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, "\73", "\75", "\54", "\50",
            "\51", "\56", "\52", "\77", "\76", "\74", "\76\75", "\74\75",
            "\74\76", "\41\75", "\100\100", "\174\174", "\174", "\46", "\53",
            "\55", "\57", "\136", null, "\175", null, null, null,};

    /** Lexer state names. */
    public static final String[] lexStateNames = {"DEFAULT",};
    static final long[] jjtoToken = {0xffffffffffffffe1L, 0x7ffffffc8fL,};
    static final long[] jjtoSkip = {0x1eL, 0x60L,};
    static final long[] jjtoSpecial = {0x0L, 0x60L,};
    protected SimpleCharStream input_stream;
    private final int[] jjrounds = new int[36];
    private final int[] jjstateSet = new int[72];
    protected char curChar;

    /** Ctor. */
    public CCSqlParserTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag)
            throw new Error(
                    "ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    /** Ctor. */
    public CCSqlParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    /** Reinitialise parser. */
    public void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 36; i-- > 0;)
            jjrounds[i] = 0x80000000;
    }

    /** Reinitialise parser. */
    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    /** Switch to specified lex state. */
    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0)
            throw new TokenMgrError("Error: Ignoring invalid lexical state : "
                    + lexState + ". State unchanged.",
                    TokenMgrError.INVALID_LEXICAL_STATE);
        else
            curLexState = lexState;
    }

    protected Token jjFillToken() {
        final Token t;
        final String curTokenImage;
        final int beginLine;
        final int endLine;
        final int beginColumn;
        final int endColumn;
        String im = jjstrLiteralImages[jjmatchedKind];
        curTokenImage = (im == null) ? input_stream.GetImage() : im;
        beginLine = input_stream.getBeginLine();
        beginColumn = input_stream.getBeginColumn();
        endLine = input_stream.getEndLine();
        endColumn = input_stream.getEndColumn();
        t = Token.newToken(jjmatchedKind, curTokenImage);

        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;

        return t;
    }

    int curLexState = 0;
    int defaultLexState = 0;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

    /** Get the next Token. */
    public Token getNextToken() {
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;

        EOFLoop : for (;;) {
            try {
                curChar = input_stream.BeginToken();
            } catch (IOException ioe) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }

            try {
                input_stream.backup(0);
                while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
                    curChar = input_stream.BeginToken();
            } catch (IOException ioe) {
                continue EOFLoop;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos)
                    input_stream.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    matchedToken.specialToken = specialToken;
                    return matchedToken;
                } else {
                    if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        if (specialToken == null)
                            specialToken = matchedToken;
                        else {
                            matchedToken.specialToken = specialToken;
                            specialToken = (specialToken.next = matchedToken);
                        }
                    }
                    continue EOFLoop;
                }
            }
            int error_line = input_stream.getEndLine();
            int error_column = input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;
            try {
                input_stream.readChar();
                input_stream.backup(1);
            } catch (IOException ioe) {
                EOFSeen = true;
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
                if (curChar == '\n' || curChar == '\r') {
                    error_line++;
                    error_column = 0;
                } else
                    error_column++;
            }
            if (!EOFSeen) {
                input_stream.backup(1);
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
            }
            throw new TokenMgrError(EOFSeen, curLexState, error_line,
                    error_column, error_after, curChar,
                    TokenMgrError.LEXICAL_ERROR);
        }
    }

    private void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }
}