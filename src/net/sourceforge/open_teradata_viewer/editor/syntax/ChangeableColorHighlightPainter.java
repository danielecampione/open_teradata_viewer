/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.Color;

import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

/**
 * A highlighter whose color you can change.
 *
 * @author D. Campione
 * 
 */
class ChangeableColorHighlightPainter extends DefaultHighlightPainter {

    /**
     * DefaultHighlightPainter doesn't allow changing color, so we must cache
     * ours here.
     */
    private Color color;

    /**
     * Ctor.
     *
     * @param color The initial color to use.  This cannot be <code>null</code>.
     */
    public ChangeableColorHighlightPainter(Color color) {
        super(color);
        setColor(color);
    }

    /**
     * Returns the color to paint with.
     *
     * @return The color.
     * @see #setColor(Color)
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color to paint the bounding boxes with.
     *
     * @param color The new color. This cannot be <code>null</code>.
     * @see #getColor()
     */
    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null");
        }
        this.color = color;
    }
}