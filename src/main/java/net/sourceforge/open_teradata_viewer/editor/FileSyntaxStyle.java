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
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.FileTypeUtil;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 * Chooses the syntax style of the text of a file from the name of the file.
 * <p>
 * The name is looked up in the file types known by RSyntaxTextArea, with those
 * of the syntax styles the editor offers and the library does not know the
 * files of. A file that is not recognized, such as a <code>.txt</code> file, a
 * file with any other extension or a file without extension, is not
 * highlighted.
 *
 * @author D. Campione
 */
public final class FileSyntaxStyle {

    /** The file types looked up: the ones of the library plus the ones added. */
    private static final Map<String, List<String>> FILTERS = createFilters();

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
     * Tells how the text of a document is highlighted after it has been saved
     * in a file that becomes the file of the document. The style follows the
     * name of the new file, as it does when a file is opened, but only if that
     * name tells another kind of file than the one of the document before: a
     * text that is saved again under the same name, for instance to change its
     * encoding, or in another file of the same kind, keeps the style it has,
     * which may have been chosen by the user. The names that do not tell the
     * kind of file count as one kind: a text that is saved from one of them in
     * another one keeps its style too.
     *
     * @param previous The file the document had, or <code>null</code> if it had
     *        none.
     * @param file The file the text has been saved in.
     * @return The style the text gets, one of {@link SyntaxConstants}, or
     *         <code>null</code> if the text keeps the style it has.
     */
    public static String forSavedAs(File previous, File file) {
        String style = forFile(file);
        if (previous != null && style.equals(forFile(previous))) {
            return null;
        }
        return style;
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
