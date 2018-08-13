/*
 * Open Teradata Viewer ( editor syntax folding )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Locates folds in a document. If you are implementing a language that has
 * sections of source code that can be logically "folded," you can create an
 * instance of this interface that locates those regions and represents them as
 * {@link Fold}s. <code>SyntaxTextArea</code> knows how to take it from there
 * and implement code folding in the editor.
 *
 * @author D. Campione
 * @see CurlyFoldParser
 * @see XmlFoldParser
 * 
 */
public interface IFoldParser {

    /**
     * Returns a list of all folds in the text area.
     *
     * @param textArea The text area whose contents should be analyzed.
     * @return The list of folds.  If this method returns <code>null</code>, it
     *         is treated as if no folds were found.
     */
    public List<Fold> getFolds(SyntaxTextArea textArea);

}