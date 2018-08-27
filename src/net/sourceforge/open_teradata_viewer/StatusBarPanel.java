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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * A panel used internally by <code>ApplicationStatusBarPanel</code>; it can have a "shadow"
 * effect displayed at its top and bottom. This shadow can be toggled on and
 * off. This class is used to mimic the status bar look found in Microsoft
 * Windows XP-style status bars.
 *
 * @author D. Campione
 * 
 */
public class StatusBarPanel extends JPanel {

    private static final long serialVersionUID = -4044684910342685600L;

    /** Whether or not the "shadow" effect is on. */
    private boolean shadowEnabled;

    /** Ctor. */
    public StatusBarPanel() {
        shadowEnabled = true;
    }

    /**
     * Ctor.
     *
     * @param layout The layout for the panel.
     */
    public StatusBarPanel(LayoutManager layout) {
        super(layout);
        shadowEnabled = true;
    }

    /**
     * Creates a <code>StatusBarPanel</code> containing a label. This is a
     * convenience constructor, since many status bar panels contain merely a
     * label.
     *
     * @param layout The layout to use for the panel.
     * @param label The label this panels hould contain.
     */
    public StatusBarPanel(LayoutManager layout, JLabel label) {
        super(layout);
        add(label);
        shadowEnabled = true;
    }

    /**
     * @return Whether the shadow is enabled.
     * @see #setShadowEnabled
     */
    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    /**
     * Paints this panel with the (optional) shadow effect.
     *
     * @param g The graphics context with which to paint.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Fill in background

        // Draw the shadow
        if (isShadowEnabled() == true) {
            int width = getWidth();
            int height = getHeight();
            Color bg = getBackground();
            // Top
            Color color = bg;
            color = Utilities.deriveColor(color, 15);
            g.setColor(color);
            g.drawLine(0, 2, width, 2);
            color = Utilities.deriveColor(color, 24);
            g.setColor(color);
            g.drawLine(0, 1, width, 1);
            color = Utilities.deriveColor(color, 40);
            g.setColor(color);
            g.drawLine(0, 0, width, 0);

            // Bottom
            color = Utilities.deriveColor(bg, 8);
            g.setColor(color);
            g.drawLine(0, height - 1, width, height - 1);
            color = Utilities.deriveColor(color, 4);
            g.setColor(color);
            g.drawLine(0, height - 2, width, height - 2);
            color = Utilities.deriveColor(color, -3);
            g.setColor(color);
            g.drawLine(0, height - 3, width, height - 3);
            color = Utilities.deriveColor(color, -5);
            g.setColor(color);
            g.drawLine(0, height - 4, width, height - 4);
        }
    }

    /**
     * Sets whether or not the shadow effect is enabled.
     *
     * @param enabled Whether the shadow effect should be enabled.
     * @see #isShadowEnabled
     */
    public void setShadowEnabled(boolean enabled) {
        if (this.shadowEnabled != enabled) {
            this.shadowEnabled = enabled;
            repaint();
        }
    }
}