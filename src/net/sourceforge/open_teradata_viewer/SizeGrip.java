/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * A component that allows its parent window to be resizable, similar to the
 * size grip seen on status bars.
 *
 * @author D. Campione
 * 
 */
public class SizeGrip extends StatusBarPanel {

    private static final long serialVersionUID = 9015344617609968566L;

    private Window window;
    private ComponentAdapter maximizeWindow;
    private SizeGripIcon sizeGripIcon;

    /**
     * Whether or not to paint the grip. This is set to <code>false</code> when
     * the parent window is maximized.
     */
    private boolean paintGrip;

    /** Ctor. */
    public SizeGrip() {
        MouseHandler adapter = new MouseHandler();
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        possiblyFixCursor(ComponentOrientation.getOrientation(getLocale()));

        sizeGripIcon = new SizeGripIcon();

        // Listens for the parent window being maximized. When it is, the size
        // grip doesn't paint itself to visually show that the window cannot be
        // stretched anymore
        maximizeWindow = new ComponentAdapter() {

            long prevTime;
            boolean isLastResize;

            @Override
            public void componentResized(ComponentEvent e) {
                checkMaximize(true);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                checkMaximize(false);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // Must do this to display size grip on first display
                checkMaximize(!isLastResize);
            }

            public void checkMaximize(boolean resize) {
                long currTime = System.currentTimeMillis();
                if ((isLastResize != resize) && (prevTime + 100 > currTime)) {
                    paintGrip = getShouldPaintGrip();
                    repaint();
                }
                isLastResize = resize;
                prevTime = currTime;
            }
        };
    }

    /**
     * Called when this component receives a parent. This method is overridden
     * so we can add a component listener to the parent window to know when it
     * is maximized/de-maximized.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        window = (Window) SwingUtilities.getRoot(this);
        window.addComponentListener(maximizeWindow);
    }

    /**
     * Overridden to ensure that the cursor for this component is appropriate
     * for the orientation.
     *
     * @param o The new orientation.
     */
    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        possiblyFixCursor(o);
        super.applyComponentOrientation(o);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(20, 20);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    private boolean getShouldPaintGrip() {
        Rectangle rect = window.getBounds();
        Dimension dim = getToolkit().getScreenSize();
        if ((rect.x == rect.y) && (rect.x <= 0) && (rect.x > -20)
                && (dim.width - 20 < rect.width)
                && (dim.width + 20 > rect.width)) {
            return false;
        }
        return true;
    }

    public SizeGripIcon getSizeGripIcon() {
        return sizeGripIcon;
    }

    /**
     * Paints this panel.
     *
     * @param g The graphics context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // If the window isn't maximized, paint the grip part
        if (paintGrip) {
            sizeGripIcon.paintIcon(this, g, 0, 0);
        }
    }

    /**
     * Ensures that the cursor for this component is appropriate for the
     * orientation.
     *
     * @param o The new orientation.
     */
    protected void possiblyFixCursor(ComponentOrientation o) {
        int cursor = Cursor.NE_RESIZE_CURSOR;
        if (o.isLeftToRight()) {
            cursor = Cursor.NW_RESIZE_CURSOR;
        }
        if (cursor != getCursor().getType()) {
            setCursor(Cursor.getPredefinedCursor(cursor));
        }
    }

    /**
     * Called when this component loses its parent. This method is overridden so
     * we can remove the component listener we added to the parent.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        window.removeComponentListener(maximizeWindow);
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
     * started happening somewhere - our window would grow way too large.
     */
    private class MouseHandler extends MouseInputAdapter {

        private Point origPos;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (origPos == null) {
                // Happens, for example, when a menu is open, and the user then
                // clicks on and drags the size grip. Swing doesn't send a
                // mousePressed event to the grip, but does send the drag events
                // (on Windows and OS X, at least)
                mousePressed(e);
            }
            Point newPos = e.getPoint();
            SwingUtilities.convertPointToScreen(newPos, SizeGrip.this);
            int xDelta = newPos.x - origPos.x;
            int yDelta = newPos.y - origPos.y;
            if (getComponentOrientation().isLeftToRight()) {
                int w = window.getWidth();
                if (newPos.x >= window.getX()) {
                    w += xDelta;
                }
                int h = window.getHeight();
                if (newPos.y >= window.getY()) {
                    h += yDelta;
                }
                window.setSize(w, h);
            } else { // RTL
                Rectangle newBounds = window.getBounds();
                if (newPos.x <= window.getX() + window.getWidth()) {
                    newBounds.width -= xDelta;
                    newBounds.x += xDelta;
                }
                if (newPos.y >= window.getY()) {
                    newBounds.height += yDelta;
                }
                window.setBounds(newBounds.x, newBounds.y, newBounds.width,
                        newBounds.height);
            }
            // invalidate()/revalidate() needed pre-1.6.
            window.invalidate();
            window.validate();
            origPos.setLocation(newPos);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            origPos = e.getPoint();
            SwingUtilities.convertPointToScreen(origPos, SizeGrip.this);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            origPos = null;
        }
    }
}