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

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * An icon that looks like a Windows 98/XP-style size grip.
 *
 * @author D. Campione
 * 
 */
public class SizeGripIcon implements Icon {

    private static final int SIZE = 20;

    private int style;

    /** Ctor. */
    public SizeGripIcon() {
        this(-1); // Important for ApplicationStatusBarPanel
    }

    /**
     * Ctor.
     *
     * @param style The style of the size grip.
     */
    public SizeGripIcon(int style) {
        setStyle(style);
    }

    /**
     * Returns the height of this icon.
     *
     * @return This icon's height.
     */
    public int getIconHeight() {
        return SIZE;
    }

    /**
     * Returns the width of this icon.
     *
     * @return This icon's width.
     */
    public int getIconWidth() {
        return SIZE;
    }

    /**
     * Returns the current style of this icon.
     *
     * @return The icon's style.
     * @see #setStyle(int)
     */
    public int getStyle() {
        return style;
    }

    /**
     * Paints this icon.
     *
     * @param c The component to paint on.
     * @param g The graphics context.
     * @param x The x-coordinate at which to paint.
     * @param y The y-coordinate at which to paint.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Dimension dim = c.getSize();
        Color c1 = UIManager.getColor("Label.disabledShadow");
        Color c2 = UIManager.getColor("Label.disabledForeground");

        ComponentOrientation orientation = c.getComponentOrientation();

        switch (style) {
            case StatusBar.WINDOWS_98_STYLE :
                int width = dim.width;
                int height = dim.height;
                g.setColor(c1.brighter());
                g.drawLine(7, height, width, 7);
                g.drawLine(11, height, width, 11);
                g.drawLine(15, height, width, 15);
                g.setColor(c2.darker());
                g.drawLine(8, height, width, 8);
                g.drawLine(12, height, width, 12);
                g.drawLine(16, height, width, 16);
                break;

            default :// StatusBar.WINDOWS_XP_STYLE:
                if (orientation.isLeftToRight()) {
                    width = dim.width -= 3;
                    height = dim.height -= 3;
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
                    height = dim.height -= 3;
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
                break;
        }
    }

    /**
     * Sets the style of this icon.
     *
     * @param style This icon's style.
     * @see #getStyle()
     */
    public void setStyle(int style) {
        this.style = style;
    }
}