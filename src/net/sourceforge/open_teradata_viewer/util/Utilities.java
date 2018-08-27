/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessControlException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.util.array.StringList;

/**
 * General purpose utilities functions.
 *
 * @author D. Campione
 */
public class Utilities {

    /**
     * A Teradata object name can contain the letters A through Z, the digits 0
     * through 9, the underscore (_), $, and #. A name in double quotation marks
     * can contain any characters except double quotation marks.
     */
    public static String[] teradataLegalChars = { "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "_", "$", "#", "." };

    /** An object name cannot be a Teradata reserved word. */
    public static StringList teradataReservedWords = new StringList();

    /** Used to indicate a &quot;Start Of Line&quot; comment in SQL. */
    public final static String START_OF_LINE_COMMENT = "--";

    public final static String LINE_SEPARATOR = "=======================================";

    /**
     * <p>An array of <code>String</code>s used for padding.</p>
     *
     * <p>Used for efficient space padding. The length of each String expands as needed.</p>
     */
    private static final String[] PADDING = new String[Character.MAX_VALUE];

    /** <p>The maximum size to which the padding constant(s) can expand.</p> */
    private static final int PAD_LIMIT = 8192;

    /**
     * If a system property is defined with this name and set, ignoring case, to
     * <code>true</code>, this library will not attempt to use Substance
     * renderers. Otherwise, if a Substance Look and Feel is installed, we will
     * attempt to use Substance cell renderers in all of our dropdowns.<p>
     *
     * Note that we do not have a build dependency on Substance, so all access
     * to Substance stuff is done via reflection. We will fall back onto default
     * renderers if something goes wrong.
     */
    public static final String PROPERTY_DONT_USE_SUBSTANCE_RENDERERS = "net.sourceforge.open_teradata_viewer.editor.autocomplete.DontUseSubstanceRenderers";

    /**
     * Used for the color of hyperlinks when a LookAndFeel uses light text
     * against a dark background.
     */
    private static final Color LIGHT_HYPERLINK_FG = new Color(0xd8ffff);

    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]*>");

    private static final boolean useSubstanceRenderers;

    static {
        try {
            teradataReservedWords
                    .setText(StreamUtil.stream2String(Utilities.class
                            .getResourceAsStream("/res/teradata_reserved_words.list")));
            for (int i = 0; i < teradataReservedWords.size(); i++) {
                if (StringUtil.isEmpty((String) teradataReservedWords.get(i))) {
                    continue;
                }
            }
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }

        // Space padding is most common, start with 64 chars
        PADDING[32] = " ";

        boolean use = true;
        try {
            use = !Boolean.getBoolean(PROPERTY_DONT_USE_SUBSTANCE_RENDERERS);
        } catch (AccessControlException ace) { // We're in an applet
            use = true;
        }
        useSubstanceRenderers = use;
    }

    /** Ctor. <TT>private</TT> as all methods are static. */
    private Utilities() {
        super();
    }

    /**
     * Print the current stack trace to <TT>ps</TT>.
     *
     * @param ps The <TT>PrintStream</TT> to print stack trace to.
     * @throws IllegalArgumentException If a null <TT>ps</TT> passed.
     */
    public static void printStackTrace(Throwable t, PrintStream ps) {
        if (ps == null) {
            throw new IllegalArgumentException("PrintStream == null");
        }

        ps.println(getStackTrace(t));
    }

    /**
     * Return the stack trace from the passed exception as a string.
     *
     * @param t The exception to retrieve stack trace for.
     */
    public static String getStackTrace(Throwable t) {
        if (t == null) {
            throw new IllegalArgumentException("Throwable == null");
        }

        StringWriter sw = new StringWriter();
        try {
            PrintWriter pw = new PrintWriter(sw);
            try {
                t.printStackTrace(pw);
                return sw.toString();
            } finally {
                pw.close();
            }
        } finally {
            try {
                sw.close();
            } catch (IOException ioe) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println("Unexpected error closing StringWriter.",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            }
        }
    }

    public static Throwable getDeepestThrowable(Throwable t) {
        Throwable parent = t;
        Throwable child = t.getCause();
        while (null != child) {
            parent = child;
            child = parent.getCause();
        }
        return parent;
    }

    /**
     * Change the passed class name to its corresponding file name. E.G. change
     * &quot;Utilities&quot; to &quot;Utilities.class&quot;.
     *
     * @param name Class name to be changed.
     * @throws IllegalArgumentException If a null <TT>name</TT> passed.
     */
    public static String changeClassNameToFileName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Class Name == null");
        }
        return name.replace('.', '/').concat(".class");
    }

    /**
     * Change the passed file name to its corresponding class name. E.G.
     * change &quot;Utilities.class&quot; to &quot;Utilities&quot;.
     *
     * @param name Class name to be changed. If this does not represent a Java
     *             class then <TT>null</TT> is returned.
     * @throws IllegalArgumentException  If a null <TT>name</TT> passed.
     */
    public static String changeFileNameToClassName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("File Name == null");
        }
        String className = null;
        if (name.toLowerCase().endsWith(".class")) {
            className = name.replace('/', '.');
            className = className.replace('\\', '.');
            className = className.substring(0, className.length() - 6);
        }
        return className;
    }

    /**
     * Clean the passed string. Replace whitespace characters with a single
     * space. If a <TT>null</TT> string passed return an empty string. E.G.
     * replace.
     *
     * [pre]
     * \t\tselect\t* from\t\ttab01
     * [/pre]
     *
     * with
     *
     * [pre]
     * select * from tab01
     * [/pre]
     *
     * @deprecated Use <tt>StringUtil.cleanString(String)</tt> instead.
     * @param str String to be cleaned.
     * @return Cleaned string.
     */
    @Deprecated
    public static String cleanString(String str) {
        return StringUtil.cleanString(str);
    }

    /**
     * Return the suffix of the passed file name.
     *
     * @param fileName File name to retrieve suffix for.
     * @return Suffix for <TT>fileName</TT> or an empty string if unable to get
     *         the suffix.
     * @throws IllegalArgumentException if <TT>null</TT> file name passed.
     */
    public static String getFileNameSuffix(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("file name == null");
        }
        int pos = fileName.lastIndexOf('.');
        if (pos > 0 && pos < fileName.length() - 1) {
            return fileName.substring(pos + 1);
        }
        return "";
    }

    public static boolean equalsRespectNull(Object o1, Object o2) {
        if (null == o1 && null == o2) {
            return true;
        } else if (null == o1 || null == o2) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    /**
     * Remove the suffix from the passed file name.
     *
     * @param fileName File name to remove suffix from.
     * @return <TT>fileName</TT> without a suffix.
     * @throws IllegalArgumentException If <TT>null</TT> file name passed.
     */
    public static String removeFileNameSuffix(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("file name == null");
        }
        int pos = fileName.lastIndexOf('.');
        if (pos > 0 && pos < fileName.length() - 1) {
            return fileName.substring(0, pos);
        }
        return fileName;
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
        double val = size / (1024 * 1024);
        if (val > 1) {
            return fmt.format(val).concat(" MB");
        }
        val = size / 1024;
        if (val > 10) {
            return fmt.format(val).concat(" KB");
        }
        return fmt.format(val).concat(" bytes");
    }

    /**
     * Split a string based on the given delimiter, but don't remove
     * empty elements.
     *
     * @deprecated Use <tt>StringUtil.split(String, char)</tt> instead.
     * @param str The string to be split.
     * @param delimiter Split string based on this delimiter.
     * @return Array of split strings. Guaranteeded to be not null.
     */
    @Deprecated
    public static String[] splitString(String str, char delimiter) {
        return StringUtil.split(str, delimiter);
    }

    /**
     * Split a string based on the given delimiter, optionally removing empty
     * elements.
     *
     * @deprecated Use <tt>StringUtil.split(String, char, boolean)</tt>
     *             instead.
     * @param str The string to be split.
     * @param delimiter Split string based on this delimiter.
     * @param removeEmpty If <tt>true</tt> then remove empty elements.
     * @return Array of split strings. Guaranteeded to be not null.
     */
    @Deprecated
    public static String[] splitString(String str, char delimiter,
            boolean removeEmpty) {
        return StringUtil.split(str, delimiter, removeEmpty);
    }

    /**
     * Creates a clone of any serializable object. Collections and arrays may be
     * cloned if the entries are serializable.
     *
     * Caution super class members are not cloned if a super class is not
     * serializable.
     */
    public static Object cloneObject(Object toClone,
            final ClassLoader classLoader) {
        if (null == toClone) {
            return null;
        } else {
            try {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream oOut = new ObjectOutputStream(bOut);
                oOut.writeObject(toClone);
                oOut.close();
                ByteArrayInputStream bIn = new ByteArrayInputStream(
                        bOut.toByteArray());
                bOut.close();
                ObjectInputStream oIn = new ObjectInputStream(bIn) {
                    @Override
                    protected Class<?> resolveClass(ObjectStreamClass desc)
                            throws IOException, ClassNotFoundException {
                        return Class
                                .forName(desc.getName(), false, classLoader);
                    }
                };
                bIn.close();
                Object copy = oIn.readObject();
                oIn.close();

                return copy;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
      * This prevents you from having to place SuppressWarnings throughout your
      * code.
      *
      * @param <T> The return type to cast the object to.
      * @param x The object to cast.
      * @return a type-casted version of the specified object.
      */
    public static <T> T cast(Object x) {
        return (T) x;
    }

    /**
     * Checks the specified list of argument to see if any are null and throws a
     * runtime exception if one is.
     *
     * @param methodName The name of the method checking it's arguments.
     * @param arguments The arguments - these should be in name/value pairs.
     */
    public static void checkNull(String methodName, Object... arguments) {
        if (arguments.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Args must be specified in name/value pairs");
        }
        for (int i = 0; i < arguments.length - 1; i += 2) {
            String name = (String) arguments[i];
            Object value = arguments[i + 1];
            if (value == null) {
                throw new IllegalArgumentException(methodName + ": Argument "
                        + name + " cannot be null");
            }
        }
    }

    /**
     * Cause the current thread to sleep for the specified number of
     * milliseconds. Exceptions logged.
     *
     * @param millis number of milliseconds to sleep.
     */
    public static void sleep(long millis) {
        if (millis == 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(ie.getMessage(),
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
    }

    /**
     * Run the garbage collector. We may eventually want this to execute in an
     * app thread and serialize many requests using a queue to avoid a
     * performance hit for too many simultaneous calls.
     */
    public static void garbageCollect() {
        System.gc();
    }

    public static boolean canBeATeradataObjectName(String text) {
        if (!isEmpty(text) && text.trim().length() > 0) {
            text = text.trim();
            // A Teradata object name must be from 1 to 30 characters long
            int lastTokenIndex = text.lastIndexOf(".");
            if (text.substring(lastTokenIndex == -1 ? 0 : lastTokenIndex + 1,
                    text.length()).length() > 30) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                "A Teradata object name must be from 1 to 30 characters long.",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                return false;
            }
            int numRetries;
            for (int i = 0; i < text.length(); i++) {
                numRetries = 0;
                for (int j = 0; j < teradataLegalChars.length; j++) {
                    if (text.toUpperCase().charAt(i) != teradataLegalChars[j]
                            .charAt(0)) {
                        numRetries++;
                    } else {
                        break;
                    }
                }
                if (numRetries == teradataLegalChars.length) {
                    ApplicationFrame
                            .getInstance()
                            .getConsole()
                            .println(
                                    "You're trying to perform a SQL command using an illegal character.",
                                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                    return false;
                }
            }
            for (int i = 0; i < teradataReservedWords.size(); i++) {
                if (text.equalsIgnoreCase(teradataReservedWords.get(i))) {
                    ApplicationFrame
                            .getInstance()
                            .getConsole()
                            .println(
                                    "You're trying to perform a SQL command using a Teradata reserved word: \""
                                            + teradataReservedWords.get(i)
                                            + "\".",
                                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                    return false;
                }
            }
            // A Teradata object name cannot be a number
            try {
                Double.parseDouble(text);
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println("A Teradata object name cannot be a number.",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                return false;
            } catch (NumberFormatException nfe) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the location of the specified jar file in the currently-running
     * application's classpath. This can be useful if you wish to know the
     * location of the installation of the currently-running application.<p> For
     * example, a Java program running from the executable jar
     * <code>Foo.jar</code> can call this method with <code>Foo.jar</code> as
     * the parameter, and the location of the jar file would be returned. With
     * this knowledge, along with knowledge of the directory layout of the
     * application, the programmer can access other files in the installation.
     *
     * @param jarFileName The name of the jar file for which to search.
     * @return The directory in which the jar file resides.
     */
    public static String getLocationOfJar(String jarFileName) {
        String classPath = System.getProperty("java.class.path");
        int index = classPath.indexOf(jarFileName);

        // A jar file on a classpath must be explicitly given; a jar file in a
        // directory, for example, will not be picked up by specifying
        // "-classpath /my/directory/". So, we can simply search for the jar
        // name in the classpath string, and if it isn't there, it must be in
        // the current directory
        if (index > -1) {
            int pathBeginning = classPath.lastIndexOf(File.pathSeparator,
                    index - 1) + 1;
            String loc = classPath.substring(pathBeginning, index);
            File file = new File(loc);
            return file.getAbsolutePath();
        }

        // Otherwise, it must be in the current directory
        return System.getProperty("user.dir");
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
        if (count == -1) {
            count = 0;
        }
        if (current == 0) {
            rs.beforeFirst();
        } else {
            rs.absolute(current);
        }
        return count;
    }

    /** Check the installed JDK version */
    public static boolean isJDK16OrAbove() {
        String vmVer = System.getProperty("java.vm.version").substring(0, 3);

        return (vmVer.compareTo("1.6") >= 0);
    }

    public static void openURLWithDefaultBrowser(String url) {
        final String[] browsers = { "google-chrome", "firefox", "opera",
                "epiphany", "konqueror", "conkeror", "midori", "kazehakase",
                "mozilla" };

        try {
            // Trying to use the Desktop library, available from the JDK
            // versions up to 1.6
            Class<?> d = Class.forName("java.awt.Desktop");
            d.getDeclaredMethod("browse", new Class[] { java.net.URI.class })
                    .invoke(d.getDeclaredMethod("getDesktop").invoke(null),
                            new Object[] { java.net.URI.create(url) });
            // The above code performs the same operations carried out by
            // invoking java.awt.Desktop.getDesktop().browse()
        } catch (Exception e) {
            // Library not available or failed attempt
            int os = SyntaxUtilities.getOS();
            try {
                if (os == SyntaxUtilities.OS_MAC_OSX) {
                    Class.forName("com.apple.eio.FileManager")
                            .getDeclaredMethod("openURL",
                                    new Class[] { String.class })
                            .invoke(null, new Object[] { url });
                } else if (os == SyntaxUtilities.OS_WINDOWS) {
                    Runtime.getRuntime().exec(
                            "rundll32 url.dll,FileProtocolHandler " + url);
                } else {
                    // Otherwise it is assumed it is a Unix or Linux
                    String browser = null;
                    for (String b : browsers) {
                        if ((browser == null)
                                && (Runtime.getRuntime()
                                        .exec(new String[] { "which", b })
                                        .getInputStream().read() != -1)) {
                            Runtime.getRuntime().exec(
                                    new String[] { browser = b, url });
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
            File folder = new File(
                    conformizePath(System.getProperty("java.io.tmpdir"))
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
        InputStream in = Utilities.class.getResourceAsStream("/"
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

        File localLicenseFile = new File(
                conformizePath(System.getProperty("java.io.tmpdir"))
                        + relativeFileName);
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
            @Override
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

    public static String getRootDir() {
        String rootDir = null;
        String javaClassPath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");
        if (javaClassPath.contains(pathSeparator)) {
            rootDir = System.getProperty("user.dir");
        } else {
            File executableFile = new File(javaClassPath);
            rootDir = getLocationOfJar(executableFile.getName());
        }
        return conformizePath(rootDir);
    }

    /**
     * Derives a color from another color by linearly shifting its blue, green,
     * and blue values.
     *
     * @param orig The original color.
     * @param darker The amount by which to decrease its r, g, and b values.
     *        Note that you can use negative values for making a color component
     *        "brighter". If this makes any of the three values less than zero,
     *        zero is used for that component value; similarly, if it makes any
     *        value greater than 255, 255 is used for that component's value.
     */
    public static final Color deriveColor(Color orig, int darker) {
        int red = orig.getRed() - darker;
        int green = orig.getGreen() - darker;
        int blue = orig.getBlue() - darker;

        if (red < 0) {
            red = 0;
        } else if (red > 255) {
            red = 255;
        }
        if (green < 0) {
            green = 0;
        } else if (green > 255) {
            green = 255;
        }
        if (blue < 0) {
            blue = 0;
        } else if (blue > 255) {
            blue = 255;
        }

        return new Color(red, green, blue);
    }

    /**
     * Fixes the orientation of the renderer of a combo box. It seems that Swing
     * standard LaFs don't handle this on their own.
     *
     * @param combo The combo box.
     */
    public static void fixComboOrientation(JComboBox combo) {
        ListCellRenderer r = combo.getRenderer();
        if (r instanceof Component) {
            ComponentOrientation o = ComponentOrientation.getOrientation(Locale
                    .getDefault());
            ((Component) r).setComponentOrientation(o);
        }
    }

    /**
     * Returns the extension of a file name.
     *
     * @param fileName The file name.
     * @return The extension, or <code>null</code> if the file name has no
     *         extension.
     */
    public static final String getExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > -1 ? fileName.substring(lastDot + 1) : null;
    }

    /**
     * Returns a hex string for the specified color, suitable for HTML.
     *
     * @param c The color.
     * @return The string representation, in the form "<code>#rrggbb</code>", or
     *         <code>null</code> if <code>c</code> is <code>null</code>.
     */
    public static String getHexString(Color c) {
        if (c == null) {
            return null;
        }

        // Don't assume 0xff alpha
        //return "#" + Integer.toHexString(c.getRGB()&0xffffff).substring(2);

        StringBuilder sb = new StringBuilder("#");
        int r = c.getRed();
        if (r < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(r));
        int g = c.getGreen();
        if (g < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(g));
        int b = c.getBlue();
        if (b < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(b));

        return sb.toString();
    }

    /**
     * Returns whether we should attempt to use Substance cell renderers and
     * styles for things such as completion choices, if a Substance Look and
     * Feel is installed. If this is <code>false</code>, we'll use our standard
     * rendering for completions, even when Substance is being used.
     *
     * @return Whether to use Substance renderers if Substance is installed.
     */
    public static boolean getUseSubstanceRenderers() {
        return useSubstanceRenderers;
    }

    /**
     * Strips any HTML from a string. The string must start with
     * "<code>&lt;html&gt;</code>" for markup tags to be stripped.
     *
     * @param text The string.
     * @return The string, with any HTML stripped.
     */
    public static String stripHtml(String text) {
        if (text == null || !text.startsWith("<html>")) {
            return text;
        }
        return TAG_PATTERN.matcher(text).replaceAll("");
    }

    public static void removeFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}