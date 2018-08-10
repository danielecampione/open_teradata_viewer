/*
 * Open Teradata Viewer ( editor syntax focusabletip )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;

/**
 * A component that allows its parent window to be resizable, similar to the
 * size grip seen on status bars.
 *
 * @author D. Campione
 * 
 */
class SizeGrip extends JPanel {

    private static final long serialVersionUID = 5871803464041965865L;

    /** The size grip to use if we're on OS X. */
    private Image osxSizeGrip;

    public SizeGrip() {
        MouseHandler adapter = new MouseHandler();
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        setPreferredSize(new Dimension(16, 16));
    }

    /**
     * Overridden to ensure that the cursor for this component is appropriate
     * for the orientation.
     *
     * @param o The new orientation.
     */
    public void applyComponentOrientation(ComponentOrientation o) {
        possiblyFixCursor(o.isLeftToRight());
        super.applyComponentOrientation(o);
    }

    /**
     * Paints this panel.
     *
     * @param g The graphics context.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dim = getSize();

        if (osxSizeGrip != null) {
            g.drawImage(osxSizeGrip, dim.width - 16, dim.height - 16, null);
            return;
        }

        Color c1 = UIManager.getColor("Label.disabledShadow");
        Color c2 = UIManager.getColor("Label.disabledForeground");
        ComponentOrientation orientation = getComponentOrientation();

        if (orientation.isLeftToRight()) {
            int width = dim.width -= 3;
            int height = dim.height -= 3;
            g.setColor(c1);
            g.fillRect(width - 9, height - 1, 3, 3);
            g.fillRect(width - 5, height - 1, 3, 3);
            g.fillRect(width - 1, height - 1, 3, 3);
            g.fillRect(width - 5, height - 5, 3, 3);
            g.fillRect(width - 1, height - 5, 3, 3);
            g.fillRect(width - 1, height - 9, 3, 3);
            g.setColor(c2);
            g.fillRect(width - 9, height - 1, 2, 2);
            g.fillRect(width - 5, height - 1, 2, 2);
            g.fillRect(width - 1, height - 1, 2, 2);
            g.fillRect(width - 5, height - 5, 2, 2);
            g.fillRect(width - 1, height - 5, 2, 2);
            g.fillRect(width - 1, height - 9, 2, 2);
        } else {
            int height = dim.height -= 3;
            g.setColor(c1);
            g.fillRect(10, height - 1, 3, 3);
            g.fillRect(6, height - 1, 3, 3);
            g.fillRect(2, height - 1, 3, 3);
            g.fillRect(6, height - 5, 3, 3);
            g.fillRect(2, height - 5, 3, 3);
            g.fillRect(2, height - 9, 3, 3);
            g.setColor(c2);
            g.fillRect(10, height - 1, 2, 2);
            g.fillRect(6, height - 1, 2, 2);
            g.fillRect(2, height - 1, 2, 2);
            g.fillRect(6, height - 5, 2, 2);
            g.fillRect(2, height - 5, 2, 2);
            g.fillRect(2, height - 9, 2, 2);
        }
    }

    /**
     * Ensures that the cursor for this component is appropriate for the
     * orientation.
     *
     * @param ltr Whether the current component orientation is LTR.
     */
    protected void possiblyFixCursor(boolean ltr) {
        int cursor = Cursor.NE_RESIZE_CURSOR;
        if (ltr) {
            cursor = Cursor.NW_RESIZE_CURSOR;
        }
        if (cursor != getCursor().getType()) {
            setCursor(Cursor.getPredefinedCursor(cursor));
        }
    }

    /**
     * Listens for mouse events on this panel and resizes the parent window
     * appropriately.
     *
     * @author D. Campione
     * 
     */
    /*
     * NOTE: We use SwingUtilities.convertPointToScreen() instead of just using
     * the locations relative to the corner component because the latter proved
     * buggy - stretch the window too wide and some kind of arithmetic error
     * started happening somewhere - our window would grow way too large
     */
    private class MouseHandler extends MouseInputAdapter {

        private Point origPos;

        public void mouseDragged(MouseEvent e) {
            Point newPos = e.getPoint();
            SwingUtilities.convertPointToScreen(newPos, SizeGrip.this);
            int xDelta = newPos.x - origPos.x;
            int yDelta = newPos.y - origPos.y;
            Window wind = SwingUtilities.getWindowAncestor(SizeGrip.this);
            if (wind != null) { // Should always be true
                if (getComponentOrientation().isLeftToRight()) {
                    int w = wind.getWidth();
                    if (newPos.x >= wind.getX()) {
                        w += xDelta;
                    }
                    int h = wind.getHeight();
                    if (newPos.y >= wind.getY()) {
                        h += yDelta;
                    }
                    wind.setSize(w, h);
                } else { // RTL
                    int newW = Math.max(1, wind.getWidth() - xDelta);
                    int newH = Math.max(1, wind.getHeight() + yDelta);
                    wind.setBounds(newPos.x, wind.getY(), newW, newH);
                }
                wind.invalidate();
                wind.validate();
            }
            origPos.setLocation(newPos);
        }

        public void mousePressed(MouseEvent e) {
            origPos = e.getPoint();
            SwingUtilities.convertPointToScreen(origPos, SizeGrip.this);
        }

        public void mouseReleased(MouseEvent e) {
            origPos = null;
        }
    }
}