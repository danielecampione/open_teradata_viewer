/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.BoundedRangeModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 * An extension of <code>JScrollPane</code> that adds popup menus on its
 * scrollbars, allowing the user to scroll, page over, etc..
 *
 * @author D. Campione
 * 
 */
public class ScrollPane extends JScrollPane
        implements
            ActionListener,
            Serializable {

    private static final long serialVersionUID = 4168177668435926163L;

    private JPopupMenu vertSBMenu;
    private JPopupMenu horizSBMenu;

    // Mouse position relative to the scrollbar when the user opens a
    // scrollbar's popup menu
    private int mouseX, mouseY;

    /**
     * Creates a <code>ScrollPane</code> with no view. Note that if you use this
     * constructor, you should then call <code>setViewportView</code>.
     */
    public ScrollPane() {
        initialize();
    }

    /**
     * Creates a <code>ScrollPane</code> with the specified view.
     *
     * @param view The component this scrollpane contains.
     */
    public ScrollPane(Component view) {
        super(view);
        initialize();
    }

    /**
     * Creates a <code>ScrollPane</code> with the specified size and specified
     * view.
     *
     * @param width The preferred width of the scrollpane.
     * @param height The preferred height of the scrollpane.
     * @param view The component this scrollpane contains.
     */
    public ScrollPane(int width, int height, Component view) {
        super(view);
        setPreferredSize(new Dimension(width, height));
        initialize();
    }

    /**
     * Listens for scrollbars' popup menus' actions.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Scroll to the position the mouse points to horizontally.
        if ("ScrollHereHorizontal".equals(command)) {
            int width = getHorizontalScrollBar().getWidth();
            float howFarIn = ((float) mouseX) / ((float) width);

            BoundedRangeModel brm = getHorizontalScrollBar().getModel();
            int newPosition = java.lang.Math.round(howFarIn
                    * (brm.getMaximum() - brm.getMinimum()))
                    - brm.getExtent() / 2;
            brm.setValue(newPosition);
        }
        // Scroll to the position the mouse points to vertically
        else if ("ScrollHereVertical".equals(command)) {
            int height = getVerticalScrollBar().getHeight();
            float howFarIn = ((float) mouseY) / ((float) height);

            BoundedRangeModel brm = getVerticalScrollBar().getModel();
            int newPosition = java.lang.Math.round(howFarIn
                    * (brm.getMaximum() - brm.getMinimum()))
                    - brm.getExtent() / 2;
            brm.setValue(newPosition);
        }
        // Scroll to the top
        else if ("Top".equals(command)) {
            BoundedRangeModel brm = getVerticalScrollBar().getModel();
            brm.setValue(0);
        }
        // Scroll to the bottom
        else if ("Bottom".equals(command)) {
            BoundedRangeModel brm = getVerticalScrollBar().getModel();
            brm.setValue(brm.getMaximum());
        }
        // Scroll one page up in the document
        else if ("PageUp".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int viewportHeight = viewport.getExtentSize().height;
            p.translate(0, -viewportHeight);
            if (p.getY() < 0)
                p.setLocation(p.getX(), 0);
            viewport.setViewPosition(p);
        }
        // Scroll one page down in the document
        else if ("PageDown".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int viewportHeight = viewport.getExtentSize().height;
            Component view = viewport.getView();
            double tempY = p.getY() + viewportHeight;
            if (view.getHeight() >= tempY + viewportHeight)
                p.setLocation(p.getX(), tempY);
            else
                p.setLocation(p.getX(), view.getHeight() - viewportHeight);
            viewport.setViewPosition(p);
        }
        // Scroll one unit up in the document
        else if ("ScrollUp".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int unitIncrement = getVerticalScrollBar().getUnitIncrement(-1);
            if (p.getY() > unitIncrement) {
                p.translate(0, -unitIncrement);
                viewport.setViewPosition(p);
            } else {
                p.setLocation(p.getX(), 0);
                viewport.setViewPosition(p);
            }
        }
        // Scroll one unit down in the document
        else if ("ScrollDown".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int unitIncrement = getVerticalScrollBar().getUnitIncrement(1);
            Component view = viewport.getView();
            if (p.getY() < view.getHeight() - viewport.getHeight()
                    - unitIncrement) {
                p.translate(0, unitIncrement);
                viewport.setViewPosition(p);
            } else {
                p.setLocation(p.getX(), view.getHeight() - viewport.getHeight());
                viewport.setViewPosition(p);
            }
        }
        // Scroll all the way to the left
        else if ("LeftEdge".equals(command)) {
            BoundedRangeModel brm = getHorizontalScrollBar().getModel();
            brm.setValue(0);
        }
        // Scroll all the way to the right
        else if ("RightEdge".equals(command)) {
            BoundedRangeModel brm = getHorizontalScrollBar().getModel();
            brm.setValue(brm.getMaximum());
        }
        // Scroll one page to the left
        else if ("PageLeft".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int viewportWidth = viewport.getExtentSize().width;
            p.translate(-viewportWidth, 0);
            if (p.getX() < 0) {
                p.setLocation(0, p.getY());
            }
            viewport.setViewPosition(p);
        }
        // Scroll one page to the right
        else if ("PageRight".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            Component view = viewport.getView();
            int viewportWidth = viewport.getExtentSize().width;
            p.translate(viewportWidth, 0);
            if (p.getX() > view.getWidth() - viewportWidth) {
                p.setLocation(view.getWidth() - viewportWidth, p.getY());
            }
            viewport.setViewPosition(p);
        }
        // Scroll one element (pixel) to the left
        else if ("ScrollLeft".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int unitIncrement = getHorizontalScrollBar().getUnitIncrement(-1);
            if (p.getX() > unitIncrement) {
                p.translate(-unitIncrement, 0);
                viewport.setViewPosition(p);
            } else {
                p.setLocation(0, p.getY());
                viewport.setViewPosition(p);
            }
        }
        // Scroll one element (pixel) to the right
        else if ("ScrollRight".equals(command)) {
            JViewport viewport = getViewport();
            Point p = viewport.getViewPosition();
            int unitIncrement = getHorizontalScrollBar().getUnitIncrement(1);
            Component view = viewport.getView();
            if (p.getX() < view.getWidth() - viewport.getWidth()
                    - unitIncrement) {
                p.translate(unitIncrement, 0);
                viewport.setViewPosition(p);
            } else {
                p.setLocation(view.getWidth() - viewport.getWidth(), p.getY());
                viewport.setViewPosition(p);
            }
        }
    }

    /**
     * Adds a menu item to a menu.
     *
     * @param actionCommand The command sent to the listener (the scroll pane).
     * @param menu The popup menu to which to add the item.
     * @param text The text of the menu item.
     */
    private final void addMenuItem(String actionCommand, JPopupMenu menu,
            String text) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(actionCommand);
        item.addActionListener(this);
        menu.add(item);
    }

    /** Sets up horizontal scrollbar's popup menu. */
    private void createHorizontalScrollBarMenu() {
        horizSBMenu = new JPopupMenu();

        addMenuItem("ScrollHereHorizontal", horizSBMenu, "Scroll Here");
        horizSBMenu.addSeparator();
        addMenuItem("LeftEdge", horizSBMenu, "Left Edge");
        addMenuItem("RightEdge", horizSBMenu, "Right Edge");
        horizSBMenu.addSeparator();
        addMenuItem("PageLeft", horizSBMenu, "Page Left");
        addMenuItem("PageRight", horizSBMenu, "Page Right");
        horizSBMenu.addSeparator();
        addMenuItem("ScrollLeft", horizSBMenu, "Scroll Left");
        addMenuItem("ScrollRight", horizSBMenu, "Scroll Right");

        horizSBMenu.applyComponentOrientation(getComponentOrientation());
    }

    /** Sets up vertical scrollbar's popup menu. */
    private void createVerticalScrollBarMenu() {
        vertSBMenu = new JPopupMenu();

        addMenuItem("ScrollHereVertical", vertSBMenu, "Scroll Here");
        vertSBMenu.addSeparator();
        addMenuItem("Top", vertSBMenu, "Top");
        addMenuItem("Bottom", vertSBMenu, "Bottom");
        vertSBMenu.addSeparator();
        addMenuItem("PageUp", vertSBMenu, "Page Up");
        addMenuItem("PageDown", vertSBMenu, "Page Down");
        vertSBMenu.addSeparator();
        addMenuItem("ScrollUp", vertSBMenu, "Scroll Up");
        addMenuItem("ScrollDown", vertSBMenu, "Scroll Down");

        vertSBMenu.applyComponentOrientation(getComponentOrientation());
    }
    private void initialize() {
        // Create scrollbars' popup menus
        MouseListener popupListener = new PopupListener();
        getVerticalScrollBar().addMouseListener(popupListener);
        getHorizontalScrollBar().addMouseListener(popupListener);
    }

    /**
     * Resets the UI property with a value from the current look and feel. This
     * overrides <code>JComponent</code>'s <code>updateUI</code> method, so that
     * the popup menus are updated as well.
     */
    public void updateUI() {
        super.updateUI();
        if (vertSBMenu != null) {
            SwingUtilities.updateComponentTreeUI(vertSBMenu);
        }
        if (horizSBMenu != null) {
            SwingUtilities.updateComponentTreeUI(horizSBMenu);
        }
    }

    /**
     * Class to listen for, and respond do, popup menu requests.
     * 
     * @author D. Campione
     * 
     */
    class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                mouseX = e.getX();
                mouseY = e.getY();
                if (e.getComponent().equals(getVerticalScrollBar())) {
                    if (vertSBMenu == null)
                        createVerticalScrollBarMenu();
                    vertSBMenu.show(getVerticalScrollBar(), mouseX, mouseY);
                } else {
                    if (horizSBMenu == null)
                        createHorizontalScrollBarMenu();
                    horizSBMenu.show(getHorizontalScrollBar(), mouseX, mouseY);
                }
            }
        }
    }
}