/*
 * Open Teradata Viewer ( look and feel )
 * Copyright (C) 2011, D. Campione
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

package com.incors.plaf.kunststoff;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;

import net.sourceforge.open_teradata_viewer.ImageManager;

/**
 * 
 * 
 * @author Aljoscha Rittner
 * @author C.J. Kent
 * @author Christian Peter
 * @author Christoph Wilhelms
 * @author Eric Georges
 * @author Gerald Bauer
 * @author Ingo Kegel
 * @author Jamie LaScolea
 * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
 * @author Jerason Banes
 * @author Jim Wissner
 * @author Johannes Ernst
 * @author Jonas Kilian
 * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
 * @author Karsten Lentzsch
 * @author Matthew Philips
 * @author Romain Guy
 * @author Sebastian Ferreyra
 * @author Steve Varghese
 * @author Taoufik Romdhane
 * @author Timo Haberkern
 * 
 */
public class KunststoffTreeUI extends BasicTreeUI {

    protected static ImageIcon m_iconExpanded;
    protected static ImageIcon m_iconCollapsed;

    public KunststoffTreeUI(JComponent tree) {
        try {
            m_iconExpanded = ImageManager.getImage("icons/treeex.gif");
            m_iconCollapsed = ImageManager.getImage("icons/treecol.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ComponentUI createUI(JComponent tree) {
        return new KunststoffTreeUI(tree);
    }

    // This method replaces the metal expand-/collaps-icons with some nicer ones.
    protected void paintExpandControl(Graphics g, Rectangle clipBounds,
            Insets insets, Rectangle bounds, TreePath path, int row,
            boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        //super.paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
        if (isExpanded == true) {
            if (null != m_iconExpanded) {
                //g.drawImage(m_iconExpanded.getImage(), (int)bounds.x-15, (int)bounds.y+5, null);
                g.drawImage(m_iconExpanded.getImage(), (int) bounds.x - 17,
                        (int) bounds.y + 4, null);
            }
        } else {
            if (null != m_iconCollapsed) {
                //g.drawImage(m_iconCollapsed.getImage(), (int)bounds.x-15, (int)bounds.y+5, null);
                g.drawImage(m_iconCollapsed.getImage(), (int) bounds.x - 17,
                        (int) bounds.y + 4, null);
            }
        }
    }

}