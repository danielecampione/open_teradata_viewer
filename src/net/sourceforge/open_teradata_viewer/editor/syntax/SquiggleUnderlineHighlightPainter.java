/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2012, D. Campione
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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Highlight painter that paints a squiggly underline underneath text, similar
 * to what popular IDE's such as Visual Studio and Eclipse do to indicate
 * errors, warnings, etc.<p>
 *
 * This class must be used as a <code>LayerPainter</code>.
 *
 * @author D. Campione
 * 
 */
public class SquiggleUnderlineHighlightPainter
        extends
            ChangeableColorHighlightPainter {

    private static final int AMT = 2;

    /**
     * Ctor.
     *
     * @param color The color of the squiggle. This cannot be <code>null</code>.
     */
    public SquiggleUnderlineHighlightPainter(Color color) {
        super(color);
        setColor(color);
    }

    /**
     * Paints a portion of a highlight.
     *
     * @param g the graphics context
     * @param offs0 the starting model offset >= 0
     * @param offs1 the ending model offset >= offs1
     * @param bounds the bounding box of the view, which is not necessarily the
     *               region to paint.
     * @param c the editor
     * @param view View painting for
     * @return region drawing occurred in
     */
    public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds,
            JTextComponent c, View view) {
        g.setColor(getColor());

        if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
            // Contained in view, can just use bounds
            Rectangle alloc;
            if (bounds instanceof Rectangle)
                alloc = (Rectangle) bounds;
            else
                alloc = bounds.getBounds();
            paintSquiggle(g, alloc);
            return alloc;
        }

        // Otherwise, should only render part of View
        try {
            // Determine locations
            Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1,
                    Position.Bias.Backward, bounds);
            Rectangle r = (shape instanceof Rectangle)
                    ? (Rectangle) shape
                    : shape.getBounds();
            paintSquiggle(g, r);
            return r;
        } catch (BadLocationException ble) {
            ExceptionDialog.notifyException(ble); // Can't render
        }

        // Only if exception
        return null;
    }

    /**
     * Paints a squiggle underneath text in the specified rectangle.
     *
     * @param g The graphics context with which to paint.
     * @param r The rectangle containing the text.
     */
    protected void paintSquiggle(Graphics g, Rectangle r) {
        int x = r.x;
        int y = r.y + r.height - 1;
        int delta = -AMT;
        while (x < r.x + r.width) {
            g.drawLine(x, y, x + AMT, y + delta);
            y += delta;
            delta = -delta;
            x += AMT;
        }
    }
}