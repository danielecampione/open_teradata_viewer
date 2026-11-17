/*
 * Open Teradata Viewer ( editor )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.fife.ui.rsyntaxtextarea.FileTypeUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 * Chooses the syntax style of the text of a file from the name of the file
 * and, in some cases, from the text.
 * <p>
 * The name is looked up in the file types known by RSyntaxTextArea, with those
 * of the syntax styles the editor offers and the library does not know the
 * files of. A file that is not recognized, such as a <code>.txt</code> file, a
 * file with any other extension or a file without extension, is not
 * highlighted.
 * <p>
 * A file whose name has no extension and does not tell either can still be
 * recognized from the first line of its text: a <code>#!</code> line, an XML
 * declaration or an HTML document type.
 *
 * @author D. Campione
 */
public final class FileSyntaxStyle {

    /** The file types looked up: the ones of the library plus the ones added. */
    private static final Map<String, List<String>> FILTERS = createFilters();

    /** The most of the first line of a text that is looked at. */
    private static final int MAX_FIRST_LINE_LENGTH = 512;

    /**
     * The name of an interpreter followed by its version in a <code>#!</code>
     * line (python3, python3.11, ...), the library knows the names without it.
     */
    private static final Pattern INTERPRETER_VERSION = Pattern
            .compile("(?<=[/\\s])(python|perl|php|lua|ruby)[0-9][0-9.]*(?=\\s|$)");

    private FileSyntaxStyle() {
    }

    /**
     * Returns the syntax style to use for the text of a file. The extension is
     * compared ignoring the case and the extensions of the backup copies
     * (<code>.bak</code>, <code>.old</code> and <code>.orig</code>) are
     * ignored.
     *
     * @param file The file, which may be <code>null</code>.
     * @return One of {@link SyntaxConstants}: the style that matches the name
     *         of the file, or {@link SyntaxConstants#SYNTAX_STYLE_NONE} if the
     *         name does not tell. It is never <code>null</code>.
     */
    public static String forFile(File file) {
        return FileTypeUtil.get().guessContentType(file, FILTERS);
    }

    /**
     * Returns the syntax style to use for the text of a file that has been
     * read: the one of its name, see {@link #forFile(File)} or, if the name
     * does not tell and has no extension (the extension of a backup copy does
     * not count), the one of the first line of the text, see
     * {@link #forContent(String)}. A name with an extension is trusted, so a
     * <code>.txt</code> file that starts with a <code>#!</code> line is not
     * highlighted.
     *
     * @param file The file, which may be <code>null</code>.
     * @param text The text of the file, which may be <code>null</code>.
     * @return One of {@link SyntaxConstants}: the style that the name or the
     *         text tells, or {@link SyntaxConstants#SYNTAX_STYLE_NONE} if
     *         nothing does. It is never <code>null</code>.
     */
    public static String forFileAndContent(File file, String text) {
        String style = forFile(file);
        if (SyntaxConstants.SYNTAX_STYLE_NONE.equals(style) && hasNoExtension(file)) {
            style = forContent(text);
        }
        return style;
    }

    /**
     * Returns the syntax style that the first line of a text tells: a shell
     * script or another script with a <code>#!</code> line that names its
     * interpreter, an XML declaration (<code>&lt;?xml ... ?&gt;</code>) or an
     * HTML document type (<code>&lt;!DOCTYPE html&gt;</code>). The decision is
     * the one of {@link FileTypeUtil#guessContentType(RSyntaxTextArea)}, taken
     * on the first line once it is cleaned up: the line break and the spaces
     * at the end are removed (a file written on Windows would not be
     * recognized otherwise), the case is ignored (the library only knows
     * <code>&lt;!doctype html</code> in lower case) and the version of the
     * interpreter is removed (<code>python3</code> is the one the library
     * knows as <code>python</code>). Must be called on the Swing event thread.
     *
     * @param text The text, which may be <code>null</code>.
     * @return One of {@link SyntaxConstants}: the style that the first line
     *         tells, or {@link SyntaxConstants#SYNTAX_STYLE_NONE} if it does
     *         not. It is never <code>null</code>.
     */
    public static String forContent(String text) {
        String line = firstLine(text);
        if (line.isEmpty()) {
            return SyntaxConstants.SYNTAX_STYLE_NONE;
        }
        // The library reads the first line of a text area
        return FileTypeUtil.get().guessContentType(new RSyntaxTextArea(line));
    }

    /**
     * Tells how the text of a document is highlighted after it has been saved
     * in a file that becomes the file of the document. The style follows the
     * file as it does when a file is opened, see
     * {@link #forFileAndContent(File, String)}, but only if the name of the new
     * file tells another kind of file than the one of the document before: a
     * text that is saved again under the same name, for instance to change its
     * encoding, or in another file of the same kind, keeps the style it has,
     * which may have been chosen by the user, and its text does not count. The
     * names that do not tell the kind of file count as one kind: a text that is
     * saved from one of them in another one keeps its style too. Must be called
     * on the Swing event thread, as the text may be looked at, see
     * {@link #forContent(String)}.
     *
     * @param previous The file the document had, or <code>null</code> if it had
     *        none.
     * @param file The file the text has been saved in.
     * @param text The text that has been saved, which may be <code>null</code>.
     * @return The style the text gets, one of {@link SyntaxConstants}, or
     *         <code>null</code> if the text keeps the style it has.
     */
    public static String forSavedAs(File previous, File file, String text) {
        if (previous != null && forFile(file).equals(forFile(previous))) {
            return null;
        }
        return forFileAndContent(file, text);
    }

    /**
     * Tells whether the name of a file has no extension, as for the files that
     * the content is looked at for. The extension of a backup copy and the
     * case are ignored, as {@link #forFile(File)} does.
     */
    private static boolean hasNoExtension(File file) {
        if (file == null) {
            return false;
        }
        String name = FileTypeUtil.stripBackupExtensions(file.getName().toLowerCase(Locale.ROOT));
        return name.indexOf('.') == -1;
    }

    /**
     * Returns the first line of a text the way the library expects it: without
     * the line break or the spaces at the end, in lower case and, if it is a
     * <code>#!</code> line, without the version of the interpreter. A very
     * long line is cut: what the library looks for is at its start or end, and
     * only when it is short.
     */
    private static String firstLine(String text) {
        if (text == null) {
            return "";
        }
        int limit = Math.min(text.length(), MAX_FIRST_LINE_LENGTH);
        int end = 0;
        while (end < limit && text.charAt(end) != '\n' && text.charAt(end) != '\r') {
            end++;
        }
        String line = text.substring(0, end).stripTrailing().toLowerCase(Locale.ROOT);
        if (line.startsWith("#!")) {
            line = INTERPRETER_VERSION.matcher(line).replaceAll("$1");
        }
        return line;
    }

    private static Map<String, List<String>> createFilters() {
        Map<String, List<String>> filters = FileTypeUtil.get().getDefaultContentTypeToFilterMap();

        // Syntax styles of the editor whose files the library does not know
        addFilters(filters, SyntaxConstants.SYNTAX_STYLE_POWERSHELL, "*.ps1", "*.psm1", "*.psd1");
        addFilters(filters, SyntaxConstants.SYNTAX_STYLE_VHDL, "*.vhd", "*.vhdl");

        // The names are compared in lower case: the library lists these two
        // with capitals, so on a case sensitive system they would never match
        addFilters(filters, SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, "dockerfile");
        addFilters(filters, SyntaxConstants.SYNTAX_STYLE_RUBY, "vagrantfile");

        return filters;
    }

    private static void addFilters(Map<String, List<String>> filters, String style, String... patterns) {
        filters.computeIfAbsent(style, key -> new ArrayList<>()).addAll(Arrays.asList(patterns));
    }
}
