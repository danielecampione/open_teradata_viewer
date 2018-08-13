/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import net.sourceforge.open_teradata_viewer.SizeGripIcon;

/**
 * A panel to be used as the content pane for <code>JDialog</code>s and
 * <code>JFrame</code>s that are resizable. This panel has a size grip that can
 * be dragged and cause a resize of the window, similar to that found on
 * resizable Microsoft Windows dialogs.
 *
 * @author D. Campione
 * 
 */
public class ResizableFrameContentPane extends JPanel {

    private static final long serialVersionUID = 9105556950518343073L;

    private SizeGripIcon gripIcon;

    /** Ctor. */
    public ResizableFrameContentPane() {
        gripIcon = new SizeGripIcon();
    }

    /**
     * Ctor.
     *
     * @param layout The layout manager.
     */
    public ResizableFrameContentPane(LayoutManager layout) {
        super(layout);
        gripIcon = new SizeGripIcon();
    }

    /**
     * Paints this panel.
     *
     * @param g The graphics context.
     */
    /*
     * We override paint() instead of paintComponent() as if we do the latter,
     * sometimes child panels will be painted over our size grip, rendering it
     * invisible.
     */
    public void paint(Graphics g) {
        super.paint(g);
        gripIcon.paintIcon(this, g, this.getX(), this.getY());
    }
}