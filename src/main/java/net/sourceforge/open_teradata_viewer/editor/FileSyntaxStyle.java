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
 * files of. A file that is not recognized, such as a <code>.txt</code> file or
 * a script of the database with any other extension, gets the style every
 * document of the editor starts with: SQL.
 *
 * @author D. Campione
 */
public final class FileSyntaxStyle {

    /**
     * The style of a text that is not known to be of any other kind. It is the
     * style every document of the editor starts with.
     */
    public static final String DEFAULT_STYLE = SyntaxConstants.SYNTAX_STYLE_SQL;

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
     *         of the file, or {@link #DEFAULT_STYLE} if the name does not tell.
     *         It is never <code>null</code>.
     */
    public static String forFile(File file) {
        String style = FileTypeUtil.get().guessContentType(file, FILTERS);
        return SyntaxConstants.SYNTAX_STYLE_NONE.equals(style) ? DEFAULT_STYLE : style;
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
