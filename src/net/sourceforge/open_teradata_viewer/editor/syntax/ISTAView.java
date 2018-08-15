/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2014, D. Campione
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

import java.awt.Rectangle;

import javax.swing.text.BadLocationException;

/**
 * Utility methods for SyntaxTextArea's views.
 *
 * @author D. Campione
 * 
 */
interface ISTAView {

    /**
     * Returns the y-coordinate of the specified line.<p>
     *
     * This method is quicker than using traditional
     * <code>modelToView(int)</code> calls, as the entire bounding box isn't
     * computed.
     *
     * @param alloc The area the text area can render into.
     * @param line The line number.
     * @return The y-coordinate of the top of the line, or <code>-1</code> if
     *         this text area doesn't yet have a positive size or the line is
     *         hidden (i.e. from folding).
     * @throws BadLocationException If <code>line</code> isn't a valid line
     *         number for this document.
     */
    public int yForLine(Rectangle alloc, int line) throws BadLocationException;

    /**
     * Returns the y-coordinate of the line containing a specified offset.<p>
     *
     * This method is quicker than using traditional
     * <code>modelToView(int)</code> calls, as the entire bounding box isn't
     * computed.
     *
     * @param alloc The area the text area can render into.
     * @param offs The offset info the document.
     * @return The y-coordinate of the top of the offset, or <code>-1</code> if
     *         this text area doesn't yet have a positive size or the line is
     *         hidden (i.e. from folding).
     * @throws BadLocationException If <code>offs</code> isn't a valid offset
     *         into the document.
     */
    public int yForLineContaining(Rectangle alloc, int offs)
            throws BadLocationException;
}