/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Interface for classes that paint the background of an
 * <code>TextAreaBase</code>. The Strategy pattern is used for this object
 * because the background can be painted as a solid color, as an image, and
 * possibly other ways (gradients, animated images, etc..). When a method to
 * change the background of an <code>TextAreaBase</code> instance is called
 * (such as <code>setBackground</code>, <code>setBackgroundImage</code> or
 * <code>setBackgoundObject</code>), the correct strategy is then created and
 * used to paint its background.
 *
 * @author D. Campione
 * @see net.sourceforge.open_teradata_viewer.editor.ImageBackgroundPainterStrategy
 * @see net.sourceforge.open_teradata_viewer.editor.ColorBackgroundPainterStrategy
 * 
 */
public interface IBackgroundPainterStrategy {

    /**
     * Paints the background.
     *
     * @param g The graphics context.
     * @param bounds The bounds of the object whose backgrouns we're
     *               painting.
     */
    public void paint(Graphics g, Rectangle bounds);

}