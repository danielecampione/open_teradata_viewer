/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;

/**
 *
 *
 * @author D. Campione
 *
 */
public class JScrollPopupMenu extends JPopupMenu {

    private static final long serialVersionUID = -8626216352855923687L;

    protected int maximumVisibleRows = 10;

    /**
     * Fraction of the usable height of the screen (the one left by the taskbar,
     * if any) that the popup may occupy. When it is greater than zero the
     * height of the popup depends on the screen instead of being fixed to
     * <code>maximumVisibleRows</code> rows; zero (the default) keeps the fixed
     * number of rows.
     */
    private double maximumScreenHeightRatio = 0d;

    private JScrollBar popupScrollBar;

    public JScrollPopupMenu() {
        this(null);
    }

    public JScrollPopupMenu(String label) {
        super(label);
        setLayout(new ScrollPopupMenuLayout());

        super.add(getScrollBar());
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                JScrollBar scrollBar = getScrollBar();
                int amount = (event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
                        ? event.getUnitsToScroll() * scrollBar.getUnitIncrement()
                        : (event.getWheelRotation() < 0 ? -1 : 1) * scrollBar.getBlockIncrement();

                scrollBar.setValue(scrollBar.getValue() + amount);
                event.consume();
            }
        });
    }

    protected JScrollBar getScrollBar() {
        if (popupScrollBar == null) {
            popupScrollBar = new JScrollBar(JScrollBar.VERTICAL);
            popupScrollBar.addAdjustmentListener(e -> {
                doLayout();
                repaint();
            });

            popupScrollBar.setVisible(false);
        }

        return popupScrollBar;
    }

    public int getMaximumVisibleRows() {
        return maximumVisibleRows;
    }

    public void setMaximumVisibleRows(int maximumVisibleRows) {
        this.maximumVisibleRows = maximumVisibleRows;
    }

    public double getMaximumScreenHeightRatio() {
        return maximumScreenHeightRatio;
    }

    /**
     * Makes the height of the popup depend on the height of the screen on which
     * it is shown. The popup is as tall as the whole rows that fit in the given
     * fraction of the usable height of the screen (never fewer rows than
     * <code>maximumVisibleRows</code>) and the scrollbar is shown only if the
     * items do not all fit. This allows to use the room offered by a large
     * screen without exceeding the height of a small one (for instance, with a
     * ratio of 0.9 a 1280x1024 screen gets a popup of about 920 pixels at
     * most).
     *
     * @param ratio The fraction of the usable screen height, greater than 0 and
     *        not greater than 1; 0 restores the fixed number of visible rows.
     * @throws IllegalArgumentException If <code>ratio</code> is out of range.
     */
    public void setMaximumScreenHeightRatio(double ratio) {
        if (!(ratio >= 0d && ratio <= 1d)) {
            throw new IllegalArgumentException("The ratio must be between 0 and 1: " + ratio);
        }
        this.maximumScreenHeightRatio = ratio;
    }

    /**
     * Returns the maximum height the popup may have on the screen of the given
     * invoker, in pixels, or 0 if the height of the popup does not depend on
     * the screen.
     */
    private int getScreenHeightLimit(Component invoker) {
        if (maximumScreenHeightRatio <= 0d || invoker == null || GraphicsEnvironment.isHeadless()) {
            return 0;
        }

        // The screen is picked the same way JMenu does to place its popup, so
        // that the popup is sized for the screen where it will appear
        GraphicsConfiguration gc = invoker.getGraphicsConfiguration();
        if (invoker.isShowing()) {
            Point location = invoker.getLocationOnScreen();
            for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                if (device.getType() == GraphicsDevice.TYPE_RASTER_SCREEN) {
                    GraphicsConfiguration candidate = device.getDefaultConfiguration();
                    if (candidate.getBounds().contains(location)) {
                        gc = candidate;
                        break;
                    }
                }
            }
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle bounds;
        Insets screenInsets;
        if (gc != null) {
            bounds = gc.getBounds();
            screenInsets = toolkit.getScreenInsets(gc);
        } else {
            bounds = new Rectangle(toolkit.getScreenSize());
            screenInsets = new Insets(0, 0, 0, 0);
        }

        // Height of the screen without the taskbar
        int usableHeight = bounds.height - Math.abs(screenInsets.top + screenInsets.bottom);
        return (int) (usableHeight * maximumScreenHeightRatio);
    }

    public void paintChildren(Graphics g) {
        Insets insets = getInsets();
        g.clipRect(insets.left, insets.top, getWidth(), getHeight() - insets.top - insets.bottom);
        super.paintChildren(g);
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);

        if (maximumVisibleRows < getComponentCount() - 1) {
            getScrollBar().setVisible(true);
        }
    }

    public void remove(int index) {
        // Can't remove the scrollbar
        ++index;

        super.remove(index);

        if (maximumVisibleRows >= getComponentCount() - 1) {
            getScrollBar().setVisible(false);
        }
    }

    public void show(Component invoker, int x, int y) {
        JScrollBar scrollBar = getScrollBar();

        // Height limit that depends on the screen, or 0 if the popup keeps the
        // fixed number of visible rows
        int screenLimit = getScreenHeightLimit(invoker);

        if (scrollBar.isVisible() || screenLimit > 0) {
            Insets insets = getInsets();
            int widthMargin = insets.left + insets.right;
            int heightMargin = insets.top + insets.bottom;

            // Preferred height of every item (the scrollbar is not an item)
            List<Integer> heights = new ArrayList<>();
            int width = 0;
            int max = 0;
            for (Component comp : getComponents()) {
                if (!(comp instanceof JScrollBar)) {
                    Dimension preferredSize = comp.getPreferredSize();
                    width = Math.max(width, preferredSize.width);
                    heights.add(preferredSize.height);
                    max += preferredSize.height;
                }
            }

            // Height of the first maximumVisibleRows rows
            int extent = 0;
            for (int i = 0; i < Math.min(maximumVisibleRows, heights.size()); i++) {
                extent += heights.get(i);
            }

            boolean scrollBarNeeded = true;
            if (screenLimit > 0) {
                // Take as many whole rows as fit in the height available on the
                // screen, but never fewer than maximumVisibleRows
                int fitting = 0;
                for (int height : heights) {
                    if (heightMargin + fitting + height > screenLimit) {
                        break;
                    }
                    fitting += height;
                }
                extent = Math.max(extent, fitting);

                // No scrollbar when all the items are visible; it has to be
                // set every time because the screen may be different from the
                // one of the previous time
                scrollBarNeeded = extent < max;
                scrollBar.setVisible(scrollBarNeeded);
            }

            if (scrollBarNeeded) {
                int unit = heights.isEmpty() ? -1 : heights.get(0);
                scrollBar.setUnitIncrement(unit);
                scrollBar.setBlockIncrement(extent);
                scrollBar.setValues(0, heightMargin + extent, 0, heightMargin + max);

                width += scrollBar.getPreferredSize().width;
            }

            width += widthMargin;
            int height = heightMargin + extent;

            setPopupSize(new Dimension(width, height));
        }

        super.show(invoker, x, y);
    }

    /**
     *
     *
     * @author D. Campione
     *
     */
    protected static class ScrollPopupMenuLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int visibleAmount = Integer.MAX_VALUE;
            Dimension dim = new Dimension();
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    if (comp instanceof JScrollBar) {
                        JScrollBar scrollBar = (JScrollBar) comp;
                        visibleAmount = scrollBar.getVisibleAmount();
                    } else {
                        Dimension pref = comp.getPreferredSize();
                        dim.width = Math.max(dim.width, pref.width);
                        dim.height += pref.height;
                    }
                }
            }

            Insets insets = parent.getInsets();
            dim.height = Math.min(dim.height + insets.top + insets.bottom, visibleAmount);

            return dim;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            int visibleAmount = Integer.MAX_VALUE;
            Dimension dim = new Dimension();
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    if (comp instanceof JScrollBar) {
                        JScrollBar scrollBar = (JScrollBar) comp;
                        visibleAmount = scrollBar.getVisibleAmount();
                    } else {
                        Dimension min = comp.getMinimumSize();
                        dim.width = Math.max(dim.width, min.width);
                        dim.height += min.height;
                    }
                }
            }

            Insets insets = parent.getInsets();
            dim.height = Math.min(dim.height + insets.top + insets.bottom, visibleAmount);

            return dim;
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();

            int width = parent.getWidth() - insets.left - insets.right;
            int height = parent.getHeight() - insets.top - insets.bottom;

            int x = insets.left;
            int y = insets.top;
            int position = 0;

            for (Component comp : parent.getComponents()) {
                if ((comp instanceof JScrollBar) && comp.isVisible()) {
                    JScrollBar scrollBar = (JScrollBar) comp;
                    Dimension dim = scrollBar.getPreferredSize();
                    scrollBar.setBounds(x + width - dim.width, y, dim.width, height);
                    width -= dim.width;
                    position = scrollBar.getValue();
                }
            }

            y -= position;
            for (Component comp : parent.getComponents()) {
                if (!(comp instanceof JScrollBar) && comp.isVisible()) {
                    Dimension pref = comp.getPreferredSize();
                    comp.setBounds(x, y, width, pref.height);
                    y += pref.height;
                }
            }
        }
    }
}