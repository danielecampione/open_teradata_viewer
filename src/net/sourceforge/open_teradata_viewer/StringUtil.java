/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.text.BreakIterator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StringUtil {

    public static final long BYTE = 1L;
    public static final long KILOBYTE = 1024;
    public static final long MEGABYTE = KILOBYTE * 1024;
    public static final long GIGABYTE = MEGABYTE * 1024;
    public static final long TERABYTE = GIGABYTE * 1024;
    public static final long PETABYTE = TERABYTE * 1024;

    public static final long KILOBYTE_1000 = 1000;
    public static final long MEGABYTE_1000 = KILOBYTE_1000 * 1000;
    public static final long GIGABYTE_1000 = MEGABYTE_1000 * 1000;
    public static final long TERABYTE_1000 = GIGABYTE_1000 * 1000;
    public static final long PETABYTE_1000 = TERABYTE_1000 * 1000;

    public static String addStringChar(String text, String addText, String chr) {
        if (!text.equals("")) {
            text = text + chr;
        }
        return text + addText;
    }

    public static String addStringEol(String text, String addText) {
        return addStringChar(text, addText, "\n");
    }

    public static String addStringHtmlBr(String text, String addText) {
        return addStringChar(text, addText, "<br>");
    }

    public static String replaceString(String text, String oldStr, String newStr) {
        if (oldStr == null || newStr == null) {
            throw new IllegalArgumentException(
                    "oldStr == null or newStr == null");
        }
        if (oldStr.equals(newStr) || isEmpty(text)) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text);
        int pos = sb.lastIndexOf(oldStr);
        while (pos >= 0) {
            sb.replace(pos, pos + oldStr.length(), newStr);
            pos = sb.lastIndexOf(oldStr, pos - 1);
        }
        return sb.toString();
    }

    public static String replaceString(String text, String oldStr, int newStr) {
        return replaceString(text, oldStr, String.valueOf(newStr));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(String str, boolean trim) {
        return str == null || str.length() == 0
                || (str.trim().length() == 0 && trim);
    }

    public static boolean hasText(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalAnyOfString(String str, String[] strs) {
        return anyOfString(str, strs) >= 0;
    }

    public static int anyOfString(String str, String[] strs) {
        if (strs == null || str == null) {
            return -1;
        }
        for (int i = 0; i < strs.length; i++) {
            if (str.equals(strs[i])) {
                return i;
            }
        }
        return -1;
    }

    public static boolean equalAnyOfString(String str, String[] strs,
            boolean ignoreCase) {
        return anyOfString(str, strs, ignoreCase) >= 0;
    }

    public static int anyOfString(String str, String[] strs, boolean ignoreCase) {
        if (!ignoreCase) {
            return anyOfString(str, strs);
        }
        if (strs == null || str == null) {
            return -1;
        }
        for (int i = 0; i < strs.length; i++) {
            if (str.compareToIgnoreCase(strs[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static boolean equals(String str1, String str2) {
        return nvl(str1, "").equals(nvl(str2, ""));
    }

    public static boolean equals(Object str1, Object str2) {
        return nvl(str1, "").equals(nvl(str2, ""));
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return nvl(str1, "").equalsIgnoreCase(nvl(str2, ""));
    }

    public static String nvl(String obj, String nullValue) {
        return obj == null ? nullValue : obj;
    }

    public static Object nvl(Object obj, Object nullValue) {
        return obj == null ? nullValue : obj;
    }

    public static boolean containsChar(char ch, String chars) {
        for (int i = 0; i < chars.length(); i++) {
            if (ch == chars.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    public static String evl(String obj, String emptyValue) {
        return isEmpty(obj) ? emptyValue : obj;
    }

    public static String formatSize(long longSize) {
        return formatSize(longSize, -1);
    }

    public static String formatSize(long longSize, int decimalPos) {
        NumberFormat fmt = NumberFormat.getNumberInstance();
        if (decimalPos >= 0) {
            fmt.setMaximumFractionDigits(decimalPos);
        }
        final double size = longSize;
        double val = size / PETABYTE;
        if (val >= 1) {
            return fmt.format(val).concat(" PB");
        }
        val = size / TERABYTE;
        if (val >= 1) {
            return fmt.format(val).concat(" TB");
        }
        val = size / GIGABYTE;
        if (val >= 1) {
            return fmt.format(val).concat(" GB");
        }
        val = size / MEGABYTE;
        if (val >= 1) {
            return fmt.format(val).concat(" MB");
        }
        val = size / KILOBYTE;
        if (val >= 1) {
            return fmt.format(val).concat(" KB");
        }
        return fmt.format(size).concat(" bytes");
    }
    /**
     * <pre>
     *       : bytes
     * b     : bytes
     * bytes : bytes
     * k     : kilobytes
     * kb    : kilobytes
     * Ki    : kilobytes
     * m     : megabytes
     * mb    : megabytes
     * Mi    : megabytes
     * g     : gigabytes
     * gb    : gigabytes
     * Gi    : gigabytes
     * t     : terabytes
     * tb    : terabytes
     * Ti    : terabytes
     * p     : petabytes
     * pb    : petabytes
     * Pi    : petabytes
     * </pre>
     */
    public static long toBytes(String bytes) {
        if (bytes == null)
            return -1;

        long value = 0;
        long sign = 1;
        int i = 0;
        int length = bytes.length();

        if (length == 0)
            return -1;

        if (bytes.charAt(i) == '-') {
            sign = -1;
            i++;
        } else if (bytes.charAt(i) == '+') {
            i++;
        }

        if (length <= i)
            return -1;

        int ch;
        for (; i < length && (ch = bytes.charAt(i)) >= '0' && ch <= '9'; i++) {
            value = 10 * value + ch - '0';
        }

        value = sign * value;

        if (bytes.endsWith("pb") || bytes.endsWith("p") || bytes.endsWith("P")
                || bytes.endsWith("PB") || bytes.endsWith("Pi")) {
            return value * PETABYTE;
        }
        if (bytes.endsWith("tb") || bytes.endsWith("t") || bytes.endsWith("T")
                || bytes.endsWith("TB") || bytes.endsWith("Ti")) {
            return value * TERABYTE;
        }
        if (bytes.endsWith("gb") || bytes.endsWith("g") || bytes.endsWith("G")
                || bytes.endsWith("GB") || bytes.endsWith("Gi")) {
            return value * GIGABYTE;
        } else if (bytes.endsWith("mb") || bytes.endsWith("m")
                || bytes.endsWith("M") || bytes.endsWith("MB")
                || bytes.endsWith("Mi")) {
            return value * MEGABYTE;
        } else if (bytes.endsWith("kb") || bytes.endsWith("k")
                || bytes.endsWith("K") || bytes.endsWith("KB")
                || bytes.endsWith("Ki")) {
            return value * KILOBYTE;
        } else if (bytes.endsWith("b") || bytes.endsWith("B")
                || bytes.endsWith("bytes") || bytes.endsWith("BYTES")) {
            return value;
        } else if (value < 0)
            return value;
        else {
            throw new IllegalArgumentException(
                    "byte-valued expression must have units.");
        }
    }

    public static String formatTime(long time) {
        long opentime = time / 1000000;
        String jt = "ms";
        if (opentime > 1000) {
            opentime = opentime / 1000;
            jt = "sec";
            if (opentime >= 60) {
                String st = String.format("%02d", opentime % 60);
                opentime = opentime / 60;
                st = String.format("%02d", opentime % 60) + ":" + st;
                opentime = opentime / 60;
                st = Long.toString(opentime) + ":" + st;
                return st;
            }
        }
        return opentime + jt;
    }

    public static int nlineCount(String text) {
        return charCount(text, '\n');
    }

    public static int charCount(String text, char ch) {
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ch) {
                result++;
            }
        }
        return result;
    }

    public static String initCap(String strIn) {
        StringBuilder strBuf = new StringBuilder();

        int i = 0;
        while (i < strIn.length()) {
            while (i < strIn.length()
                    && Character.isWhitespace(strIn.charAt(i))) {
                strBuf.append(strIn.charAt(i));
                i++;
            }
            if (i < strIn.length()) {
                strBuf.append(Character.toUpperCase(strIn.charAt(i)));
                i++;
                if (i < strIn.length()) {
                    while (i < strIn.length()
                            && !Character.isWhitespace(strIn.charAt(i))) {
                        strBuf.append(Character.toLowerCase(strIn.charAt(i)));
                        i++;
                    }
                }
            }
        }

        return strBuf.toString();
    }

    public static boolean toBoolean(String value) {
        if (isEmpty(value)) {
            return false;
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("tak")
                || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1")
                || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("y")
                || value.equalsIgnoreCase("+")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        if (isEmpty(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if ((value.charAt(i) < '0' || value.charAt(i) > '9')
                    && value.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static String removeChars(String text, String chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (chars.indexOf(text.charAt(i)) == -1) {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }

    public static int lastIndexOfChars(String text, char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            int index = text.lastIndexOf(chars[i]);
            if (index >= 0) {
                return index;
            }
        }
        return -1;
    }

    public static int lastIndexOfChars(String text, String chars) {
        for (int i = 0; i < chars.length(); i++) {
            int index = text.lastIndexOf(chars.charAt(i));
            if (index >= 0) {
                return index;
            }
        }
        return -1;
    }

    public static String argName(String text) {
        if (!isEmpty(text, true)) {
            StringBuilder sb = new StringBuilder();
            if (text.charAt(0) == '/' || text.charAt(0) == '-') {
                text = text.substring(1);
            }
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) != ':' && text.charAt(i) != '=') {
                    sb.append(text.charAt(i));
                } else {
                    break;
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static String argValue(String text) {
        if (!isEmpty(text, true)) {
            if (text.charAt(0) == '/' || text.charAt(0) == '-') {
                text = text.substring(1);
            }
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == ':' || text.charAt(i) == '=') {
                    return text.substring(i + 1);
                }
            }
            return "";
        }
        return null;
    }

    public static String[] unionList(String[] l1, String[] l2) {
        HashSet<String> list = new HashSet<String>();

        for (String t : l1) {
            list.add(t.trim());
        }

        for (String t : l2) {
            list.add(t.trim());
        }

        return list.toArray(new String[list.size()]);
    }

    public static String[] tokenizeList(String[] list, String delims) {
        if (list == null) {
            return null;
        }

        HashSet<String> tl = new HashSet<String>(100);
        for (String l : list) {
            StringTokenizer st = new StringTokenizer(l, delims);
            while (st.hasMoreTokens()) {
                tl.add(st.nextToken().trim());
            }
        }

        return tl.toArray(new String[tl.size()]);
    }

    public static String[] tokenizeList(String list, String delims) {
        StringTokenizer st = new StringTokenizer(list, delims);
        HashSet<String> tl = new HashSet<String>(100);
        while (st.hasMoreTokens()) {
            tl.add(st.nextToken().trim());
        }
        return tl.toArray(new String[tl.size()]);
    }

    public static String[] wordWrap(String text, int maxLineLength) {
        if (text == null) {
            return null;
        }
        if (maxLineLength <= 10) {
            return new String[]{text};
        }
        BreakIterator boundary = BreakIterator.getLineInstance();
        boundary.setText(text);
        ArrayList<String> strings = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
                .next()) {
            String word = text.substring(start, end);
            if (sb.length() + word.length() > maxLineLength) {
                if (sb.length() == 0) {
                    strings.add(word);
                } else {
                    strings.add(sb.toString());
                    sb.setLength(0);
                }
            }
            sb.append(word);
        }
        if (sb.length() > 0) {
            strings.add(sb.toString());
        }
        return strings.toArray(new String[strings.size()]);
    }

    public static String toString(String[] array, String lineBreak) {
        if (array == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String line : array) {
            if (sb.length() > 0) {
                sb.append(lineBreak == null ? "\n" : lineBreak);
            }
            sb.append(line);
        }
        return sb.toString();
    }

}
