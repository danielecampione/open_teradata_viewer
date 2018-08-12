/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Highlight painter that draws an outline around the text. This is used to draw
 * bounds around function/method parameters.
 *
 * @author D. Campione
 * 
 */
/*
 * NOTE: Whenever you see text like "Workaround for Java Highlight issues",
 * this is because highlighted text in a JTextComponent gets "pushed" forward
 * when the caret is at the Highlight's start, when we need it to instead get
 * prepended to. For this reason, the auto-complete package adds its Highlights
 * one char too long (one char earlier than where it should really start), but
 * only paint the Highlight from the 2nd char on.
 */
class OutlineHighlightPainter
        extends
            DefaultHighlighter.DefaultHighlightPainter {

    /**
     * DefaultHighlightPainter doesn't allow changing color, so we must cache
     * ours here.
     */
    private Color color;

    /**
     * Ctor.
     *
     * @param color The color to draw the bounding boxes with. This cannot be
     *        <code>null</code>.
     */
    public OutlineHighlightPainter(Color color) {
        super(color);
        setColor(color);
    }

    /**
     * Returns the color to paint bounding boxes with.
     *
     * @return The color.
     * @see #setColor(Color)
     */
    public Color getColor() {
        return color;
    }

    /** {@inheritDoc} */
    public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds,
            JTextComponent c, View view) {
        g.setColor(getColor());
        p0++; // Workaround for Java Highlight issues

        // This special case isn't needed for most standard Swing Views (which
        // always return a width of 1 for modelToView() calls), but it is needed
        // for OTVSyntaxTextArea views, which actually return the width of chars
        // for modelToView calls. But this should be faster anyway, as we
        // short-circuit and do only one modelToView() for one offset
        if (p0 == p1) {
            try {
                Shape s = view.modelToView(p0, viewBounds,
                        Position.Bias.Forward);
                Rectangle r = s.getBounds();
                g.drawLine(r.x, r.y, r.x, r.y + r.height);
                return r;
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble); // Never happens
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
            g.drawRect(alloc.x, alloc.y, alloc.width - 1, alloc.height - 1);
            return alloc;
        }

        // Should only render part of View
        try {
            // --- determine locations ---
            Shape shape = view.modelToView(p0, Position.Bias.Forward, p1,
                    Position.Bias.Backward, viewBounds);
            Rectangle r = (shape instanceof Rectangle)
                    ? (Rectangle) shape
                    : shape.getBounds();
            g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
            return r;
        } catch (BadLocationException e) { // Never happens
            ExceptionDialog.hideException(e);
            return null;
        }

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