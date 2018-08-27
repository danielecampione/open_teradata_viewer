/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

/**
 * Utility classes for the JavaScript code completion.
 *
 * @author D. Campione
 * 
 */
public class Util {

    /** Private constructor to prevent instantiation. */
    private Util() {
    }

    /**
     * Generates an HTML summary from a JSDoc comment.
     *
     * @param jsDoc The JSDoc comment.
     * @return The HTML version.
     */
    public static String jsDocToHtml(String jsDoc) {
        return net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                .docCommentToHtml(jsDoc);
    }
}