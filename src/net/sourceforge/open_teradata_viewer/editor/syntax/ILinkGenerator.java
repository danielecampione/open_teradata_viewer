/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

/**
 * Generates hyperlinks in a document. If one of these is installed on an
 * <code>SyntaxTextArea</code> it is queried when the mouse is moved and
 * hyperlinks are enabled. If the user is not hovering over a "real" hyperlink
 * (e.g. "http://www.google.com"), the link generator is asked if a text region
 * at the mouse position should be considered a hyperlink. If so, a result
 * object is returned, describing exactly what region of text is the link, and
 * where it goes to.<p>
 * 
 * This interface is typically used by applications providing advanced support
 * for programming languages, such as IDEs. For example, an implementation of
 * this class could identify the token under the mouse position as a "variable,"
 * and the hyperlink returned would select the variable's declaration in the
 * document.
 *
 * @author D. Campione
 * 
 */
public interface ILinkGenerator {

    /**
     * If a region of text under the mouse position should be considered a
     * hyperlink, a result object is returned. This object describes what region
     * of text is the link, and what action to perform if the link is clicked.
     *
     * @param textArea The text component.
     * @param offs The offset in the document under the mouse position.
     * @return The link information, or <code>null</code> if no link is at the
     *         specified offset.
     */
    public ILinkGeneratorResult isLinkAtOffset(SyntaxTextArea textArea, int offs);

}