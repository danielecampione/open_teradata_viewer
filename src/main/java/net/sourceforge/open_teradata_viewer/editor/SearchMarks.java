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

import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

/**
 * The occurrences marked by a search ("Mark All") in the documents of the
 * editor.
 * <p>
 * The search, and so its marks, is shared by all the documents but only the
 * document shown is searched: when another one is shown, the marks have to
 * follow it, otherwise the document that was left would keep marks that no
 * longer match the search, and the one shown would have none.
 *
 * @author D. Campione
 */
public final class SearchMarks {

    private SearchMarks() {
    }

    /**
     * Removes the marks from the document that has been left and, if the
     * search marks the occurrences, marks them in the document now shown.
     *
     * @param from The document that was shown, or <code>null</code>.
     * @param to The document now shown, or <code>null</code>.
     * @param context The search shared by the documents, or <code>null</code>
     *        if there is none yet.
     */
    public static void move(EditorDocument from, EditorDocument to, SearchContext context) {
        if (from != null && from != to) {
            from.getTextArea().clearMarkAllHighlights();
        }
        if (to != null && context != null && context.getMarkAll()) {
            String searchFor = context.getSearchFor();
            if (searchFor != null && !searchFor.isEmpty()) {
                SearchEngine.markAll(to.getTextArea(), context);
            }
        }
    }
}
