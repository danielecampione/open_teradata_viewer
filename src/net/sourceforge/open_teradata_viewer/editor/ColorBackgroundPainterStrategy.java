/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A strategy for painting the background of an <code>TextAreaBase</code> as a
 * solid color. The default background for <code>TextAreaBase</code>s is this
 * strategy using the color white.
 *
 * @author D. Campione
 * @see net.sourceforge.open_teradata_viewer.editor.ImageBackgroundPainterStrategy
 * 
 */
public class ColorBackgroundPainterStrategy implements
        IBackgroundPainterStrategy {

    private Color color;

    /**
     * Ctor.
     *
     * @param color The color to use when painting the background.
     */
    public ColorBackgroundPainterStrategy(Color color) {
        setColor(color);
    }

    /**
     * Returns whether or not the specified object is equivalent to this one.
     *
     * @param o2 The object to which to compare.
     * @return Whether <code>o2</code> is another
     *         <code>ColorBackgroundPainterStrategy</code> representing the same
     *         color as this one.
     */
    @Override
    public boolean equals(Object o2) {
        return o2 != null
                && (o2 instanceof ColorBackgroundPainterStrategy)
                && this.color.equals(((ColorBackgroundPainterStrategy) o2)
                        .getColor());
    }

    /**
     * Returns the color used to paint the background.
     *
     * @return The color.
     * @see #setColor
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the hash code to use when placing an object of this type into
     * hash maps. This method is implemented since we overrode {@link
     * #equals(Object)}.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return color.hashCode();
    }

    /**
     * Paints the background.
     *
     * @param g The graphics context.
     * @param bounds The bounds of the object whose backgrouns we're painting.
     */
    @Override
    public void paint(Graphics g, Rectangle bounds) {
        Color temp = g.getColor();
        g.setColor(color);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(temp);
    }

    /**
     * Sets the color used to paint the background.
     *
     * @param color The color to use.
     * @see #getColor
     */
    public void setColor(Color color) {
        this.color = color;
    }
}