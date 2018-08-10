/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class Tools {

    /**
     * <p>An array of <code>String</code>s used for padding.</p>
     *
     * <p>Used for efficient space padding. The length of each String expands as needed.</p>
     */
    private static final String[] PADDING = new String[Character.MAX_VALUE];

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;

    static {
        // space padding is most common, start with 64 chars
        PADDING[32] = " ";
    }

    /**
     * <p>Left pad a String with spaces (' ').</p>
     *
     * <p>The String is padded to the size of <code>size<code>.</p>
     *
     * <pre>
     * StringUtils.leftPad(null, *) = null
     * StringUtils.leftPad("", 3) = " "
     * StringUtils.leftPad("bat", 3) = "bat"
     * StringUtils.leftPad("bat", 5) = " bat"
     * StringUtils.leftPad("bat", 1) = "bat"
     * StringUtils.leftPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str the String to pad out, may be null
     * @param size the size to pad to
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * <p>Left pad a String with a specified character.</p>
     *
     * <p>Pad to a size of <code>size</code>.</p>
     *
     * <pre>
     * StringUtils.leftPad(null, *, *) = null
     * StringUtils.leftPad("", 3, 'z') = "zzz"
     * StringUtils.leftPad("bat", 3, 'z') = "bat"
     * StringUtils.leftPad("bat", 5, 'z') = "zzbat"
     * StringUtils.leftPad("bat", 1, 'z') = "bat"
     * StringUtils.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str the String to pad out, may be null
     * @param size the size to pad to
     * @param padChar the character to pad with
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input.
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    /**
     * <p>Left pad a String with a specified String.</p>
     *
     * <p>Pad to a size of <code>size</code>.</p>
     *
     * <pre>
     * StringUtils.leftPad(null, *, *) = null
     * StringUtils.leftPad("", 3, "z") = "zzz"
     * StringUtils.leftPad("bat", 3, "yz") = "bat"
     * StringUtils.leftPad("bat", 5, "yz") = "yzbat"
     * StringUtils.leftPad("bat", 8, "yz") = "yzyzybat"
     * StringUtils.leftPad("bat", 1, "yz") = "bat"
     * StringUtils.leftPad("bat", -1, "yz") = "bat"
     * StringUtils.leftPad("bat", 5, null) = " bat"
     * StringUtils.leftPad("bat", 5, "") = " bat"
     * </pre>
     *
     * @param str the String to pad out, may be null
     * @param size the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String leftPadOfMonth(String str) {
        if (str == null) {
            return null;
        }
        String formatted = str;

        try {
            formatted = String.format("%07d", str);
        } catch (Throwable t) {
            try {
                DecimalFormat df = new DecimalFormat("00");
                formatted = df.format(Integer.parseInt(str));
            } catch (Throwable t1) {
                formatted = "NaN";
            }
        }

        return formatted;
    }

    /**
     * <p>Returns padding using the specified delimiter repeated
     * to a given length.</p>
     *
     * <pre>
     * StringUtils.padding(0, 'e') = ""
     * StringUtils.padding(3, 'e') = "eee"
     * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
     * </pre>
     *
     * @param repeat number of times to repeat delim
     * @param padChar character to repeat
     * @return String with repeated character
     * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
     */
    private static String padding(int repeat, char padChar) {
        // be careful of synchronization in this method
        // we are assuming that get and set from an array index is atomic
        String pad = PADDING[padChar];
        if (pad == null) {
            pad = String.valueOf(padChar);
        }
        while (pad.length() < repeat) {
            pad = pad.concat(pad);
        }
        PADDING[padChar] = pad;
        return pad.substring(0, repeat);
    }

    /**
     * The method eliminates the differences between the path obtained by the
     * system property invocation on Linux family os and on Windows.
     * 
     * @param path.
     * @return path in line with expectations.
     */
    public static String conformizePath(String path) {
        if (path == null) {
            return null;
        }
        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.lastIndexOf(File.separator));
        }
        path += File.separator;
        return path;
    }

    public static int getRowCount(ResultSet rs) throws SQLException {
        int current = rs.getRow();
        rs.last();
        int count = rs.getRow();
        if (count == -1)
            count = 0;
        if (current == 0)
            rs.beforeFirst();
        else
            rs.absolute(current);
        return count;
    }

    /** Check the installed JDK version */
    public static boolean isJDK16OrAbove() {
        String vmVer = System.getProperty("java.vm.version").substring(0, 3);

        return (vmVer.compareTo("1.6") >= 0);
    }

    public static void openURLWithDefaultBrowser(String url) {
        final String[] browsers = {"google-chrome", "firefox", "opera",
                "epiphany", "konqueror", "conkeror", "midori", "kazehakase",
                "mozilla"};

        try {
            // Trying to use the Desktop library, available from the JDK
            // versions up to 1.6
            Class<?> d = Class.forName("java.awt.Desktop");
            d.getDeclaredMethod("browse", new Class[]{java.net.URI.class})
                    .invoke(d.getDeclaredMethod("getDesktop").invoke(null),
                            new Object[]{java.net.URI.create(url)});
            // The above code performs the same operations carried out by
            // invoking java.awt.Desktop.getDesktop().browse()
        } catch (Exception e) {
            // Library not available or failed attempt
            String osName = System.getProperty("os.name").toLowerCase(
                    java.util.Locale.ENGLISH);
            try {
                if (osName.startsWith("mac os")) {
                    Class.forName("com.apple.eio.FileManager")
                            .getDeclaredMethod("openURL",
                                    new Class[]{String.class})
                            .invoke(null, new Object[]{url});
                } else if (osName.startsWith("windows")) {
                    Runtime.getRuntime().exec(
                            "rundll32 url.dll,FileProtocolHandler " + url);
                } else {
                    // Otherwise it is assumed it is a Unix or Linux
                    String browser = null;
                    for (String b : browsers) {
                        if ((browser == null)
                                && (Runtime.getRuntime()
                                        .exec(new String[]{"which", b})
                                        .getInputStream().read() != -1)) {
                            Runtime.getRuntime().exec(
                                    new String[]{browser = b, url});
                        }
                    }
                    if (browser == null) {
                        throw new Exception(Arrays.toString(browsers));
                    }
                }
            } catch (Exception e1) {
                ExceptionDialog.showException(e1);
            }
        }
    }

    public static void writeLocallyJARInternalFile(String relativeFileName) {
        if (relativeFileName.contains(File.separator)) {
            String suppRelativeFileName = File.separator + relativeFileName;
            StringTokenizer relativePath = new StringTokenizer(
                    suppRelativeFileName, File.separator);
            File folder = new File(Tools.conformizePath(System
                    .getProperty("java.io.tmpdir"))
                    + relativePath.nextElement().toString());
            if (!folder.exists()) {
                folder.mkdir();
                folder.deleteOnExit();
            }
            for (int i = 0; i < relativePath.countTokens() - 1; i++) {
                folder = new File(folder.getAbsolutePath() + File.separator
                        + relativePath.nextElement().toString());
                if (!folder.exists()) {
                    folder.mkdir();
                    folder.deleteOnExit();
                }
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = Tools.class.getResourceAsStream("/"
                + relativeFileName.replaceAll("\\\\", "/"));
        byte[] bytes = new byte[1024];
        int length;
        try {
            length = in.read(bytes);
            while (length != -1) {
                out.write(bytes, 0, length);
                length = in.read(bytes);
            }
            in.close();
        } catch (IOException e) {
            UISupport.getDialogs().showErrorMessage(e.getMessage());
        }

        File localLicenseFile = new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir")) + relativeFileName);
        localLicenseFile.deleteOnExit();
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(localLicenseFile);
            fileOutputStream.write(out.toByteArray());
        } catch (FileNotFoundException fnfe) {
            UISupport.getDialogs().showErrorMessage(fnfe.getMessage());
        } catch (IOException ioe) {
            UISupport.getDialogs().showErrorMessage(ioe.getMessage());
        }
    }

    public static File[] listFiles(File directory) {
        // This filter only returns directories
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        };
        File[] files = directory.listFiles(fileFilter);
        return files;
    }

    public static int getOccurancesCount(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex + 1);
            if (lastIndex != -1) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if the file is empty.
     * 
     * @return true if the file is empty
     */
    public static boolean isAnEmptyText(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = null;
        scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine() + "\n");
        }
        scanner.close();
        if (stringBuilder.toString().trim().equals("")) {
            return true;
        }
        return false;
    }
}