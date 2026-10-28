/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

import net.sourceforge.open_teradata_viewer.util.SwingUtil;

/**
 * The component shown in the tab of a document: its title and a button to close
 * it.
 * <p>
 * A tabbed pane does not forward the mouse events to a component it is given
 * for a tab, so this one selects its own tab when it is pressed, closes it on a
 * click of the middle button or of the close button, asks for the menu of the
 * tab when the popup menu button is pressed and tells when the tab is dragged,
 * to move it.
 *
 * @author D. Campione
 */
public class TabHeader extends JPanel {

    private static final long serialVersionUID = 4189271645501217309L;

    private final JTabbedPane tabbedPane;
    private final Consumer<Component> closeRequest;
    private final BiConsumer<Component, MouseEvent> popupRequest;
    private final BiConsumer<Component, Point> dragRequest;
    private final JLabel titleLabel;
    private final CloseButton closeButton;

    /**
     * Creates the header of a tab.
     *
     * @param tabbedPane The tabbed pane this header is the tab component of.
     * @param closeRequest Called, with the component of the tab, when the user
     *        asks to close the tab.
     * @param popupRequest Called, with the component of the tab and the mouse
     *        event, when the user asks for the menu of the tab.
     * @param dragRequest Called, with the component of the tab and the position
     *        of the mouse in the coordinates of the tabbed pane, while the tab
     *        is dragged with the left button.
     * @param title The title to show.
     * @param closeToolTip The tool tip of the close button.
     */
    public TabHeader(JTabbedPane tabbedPane, Consumer<Component> closeRequest,
                     BiConsumer<Component, MouseEvent> popupRequest, BiConsumer<Component, Point> dragRequest, String title,
                     String closeToolTip) {
        super(new BorderLayout(SwingUtil.scale(6), 0));
        this.tabbedPane = tabbedPane;
        this.closeRequest = closeRequest;
        this.popupRequest = popupRequest;
        this.dragRequest = dragRequest;
        setOpaque(false);

        titleLabel = new JLabel(title);
        add(titleLabel, BorderLayout.CENTER);

        closeButton = new CloseButton();
        closeButton.setToolTipText(closeToolTip);
        closeButton.addActionListener(e -> requestClose());
        add(closeButton, BorderLayout.EAST);

        // The label has no mouse listener of its own, so the mouse events on it
        // are delivered to this panel
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) || e.isPopupTrigger()) {
                    selectTab();
                }
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    requestClose();
                }
                showPopup(e);
            }
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragTab(e);
                }
            }
        });
    }

    /** @param title The title to show. */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /** @return The title shown. */
    public String getTitle() {
        return titleLabel.getText();
    }

    /** @param toolTip The tool tip of the close button. */
    public void setCloseToolTip(String toolTip) {
        closeButton.setToolTipText(toolTip);
    }

    /** @return The button closing the tab. */
    public JButton getCloseButton() {
        return closeButton;
    }

    private void selectTab() {
        int index = tabbedPane.indexOfTabComponent(this);
        if (index >= 0) {
            tabbedPane.setSelectedIndex(index);
        }
    }

    private void dragTab(MouseEvent e) {
        int index = tabbedPane.indexOfTabComponent(this);
        if (index >= 0) {
            dragRequest.accept(tabbedPane.getComponentAt(index), SwingUtilities.convertPoint(this, e.getPoint(), tabbedPane));
        }
    }

    // The popup menu button is a press or a release, depending on the system
    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int index = tabbedPane.indexOfTabComponent(this);
            if (index >= 0) {
                popupRequest.accept(tabbedPane.getComponentAt(index), e);
            }
        }
    }

    private void requestClose() {
        int index = tabbedPane.indexOfTabComponent(this);
        if (index >= 0) {
            closeRequest.accept(tabbedPane.getComponentAt(index));
        }
    }

    /** A small button drawing a cross. */
    private static class CloseButton extends JButton {

        private static final long serialVersionUID = -3128420970412360561L;

        private static final Color HOVER = new Color(128, 128, 128, 80);

        CloseButton() {
            int size = SwingUtil.scale(16);
            setPreferredSize(new Dimension(size, size));
            // Painted by this class, whatever the Look and Feel is
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
            setFocusable(false);
            setRolloverEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(HOVER);
                    g2.fillRoundRect(0, 0, w - 1, h - 1, 6, 6);
                }
                Color color = getForeground();
                g2.setColor(color != null ? color : Color.GRAY);
                g2.setStroke(new BasicStroke(1.6f));
                int margin = Math.max(4, w / 3);
                g2.drawLine(margin, margin, w - margin - 1, h - margin - 1);
                g2.drawLine(w - margin - 1, margin, margin, h - margin - 1);
            } finally {
                g2.dispose();
            }
        }
    }
}
