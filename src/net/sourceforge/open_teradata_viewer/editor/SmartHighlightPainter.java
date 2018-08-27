/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * A "smart" highlight painter designed for use in SyntaxTextArea. Adds the
 * following features:
 * 
 * <ul>
 *    <li>Rendered highlights don't "grow" when users append text to the "end"
 *        of them. This is implemented by assuming that the highlights
 *        themselves specify their end offset as one offset "too short". This
 *        behavior is baked into various STA highlights (mark all, mark
 *        occurrences, etc..).
 *    <li>Ability to paint a border line around highlights.
 * </ul>
 *
 * @author D. Campione
 * 
 */
public class SmartHighlightPainter extends ChangeableHighlightPainter {

    private static final long serialVersionUID = -2264634995786004000L;

    private Color borderColor;
    private boolean paintBorder;

    /** Creates a highlight painter that defaults to blue. */
    public SmartHighlightPainter() {
        super(Color.BLUE);
    }

    /**
     * Ctor.
     *
     * @param paint The color or paint to use for this painter.
     */
    public SmartHighlightPainter(Paint paint) {
        super(paint);
    }

    /**
     * Returns whether a border is painted around marked occurrences.
     *
     * @return Whether a border is painted.
     * @see #setPaintBorder(boolean)
     * @see #getPaint()
     */
    public boolean getPaintBorder() {
        return paintBorder;
    }

    /** {@inheritDoc} */
    @Override
    public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds,
            JTextComponent c, View view) {
        g.setColor((Color) getPaint());

        // This special case isn't needed for most standard Swing Views (which
        // always return a width of 1 for modelToView() calls), but it is needed
        // for STA views, which actually return the width of chars for
        // modelToView calls. But this should be faster anyway, as we
        // short-circuit and do only one modelToView() for one offset
        if (p0 == p1) {
            try {
                Shape s = view.modelToView(p0, viewBounds,
                        Position.Bias.Forward);
                Rectangle r = s.getBounds();
                g.drawLine(r.x, r.y, r.x, r.y + r.height);
                return r;
            } catch (BadLocationException ble) { // Never happens
                ExceptionDialog.hideException(ble);
                return null;
            }
        }

        if (p0 == view.getStartOffset() && p1 == view.getEndOffset()) {
            // Contained in view, can just use bounds
            Rectangle alloc;
            if (viewBounds instanceof Rectangle) {
                alloc = (Rectangle) viewBounds;
            } else {
                alloc = viewBounds.getBounds();
            }
            g.fillRect(alloc.x, alloc.y, alloc.width, alloc.height);
            return alloc;
        }

        // Should only render part of View
        try {
            // --- determine locations ---
            Shape shape = view.modelToView(p0, Position.Bias.Forward, p1,
                    Position.Bias.Backward, viewBounds);
            Rectangle r = (shape instanceof Rectangle) ? (Rectangle) shape
                    : shape.getBounds();
            g.fillRect(r.x, r.y, r.width, r.height);
            if (paintBorder) {
                g.setColor(borderColor);
                g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
            }
            return r;
        } catch (BadLocationException e) { // Never happens
            ExceptionDialog.hideException(e);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setPaint(Paint paint) {
        super.setPaint(paint);
        if (paint instanceof Color) {
            borderColor = ((Color) paint).darker();
        }
    }

    /**
     * Toggles whether a border is painted around highlights.
     *
     * @param paint Whether to paint a border.
     * @see #getPaintBorder()
     * @see #setPaint(Paint)
     */
    public void setPaintBorder(boolean paint) {
        this.paintBorder = paint;
    }
}