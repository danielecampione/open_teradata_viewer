/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class SizeGripIcon implements Icon {

    private int style;

    public SizeGripIcon() {
        this(-1);
    }

    public SizeGripIcon(int style) {
        setStyle(style);
    }

    public int getIconHeight() {
        return 20;
    }

    public int getIconWidth() {
        return 20;
    }

    public int getStyle() {
        return this.style;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Dimension dim = c.getSize();
        Color c1 = UIManager.getColor("Label.disabledShadow");
        Color c2 = UIManager.getColor("Label.disabledForeground");

        ComponentOrientation orientation = c.getComponentOrientation();
        int width;
        int height;
        switch (this.style) {
            case 0 :
                width = dim.width;
                height = dim.height;
                g.setColor(c1.brighter());
                g.drawLine(7, height, width, 7);
                g.drawLine(11, height, width, 11);
                g.drawLine(15, height, width, 15);
                g.setColor(c2.darker());
                g.drawLine(8, height, width, 8);
                g.drawLine(12, height, width, 12);
                g.drawLine(16, height, width, 16);
                break;
            default :
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
        }
    }

    public void setStyle(int style) {
        this.style = style;
    }
}