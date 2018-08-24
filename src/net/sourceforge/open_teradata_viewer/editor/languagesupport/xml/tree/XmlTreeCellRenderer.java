/*
 * Open Teradata Viewer ( editor language support xml tree )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URL;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;

/**
 * Renders nodes in the XML tree.
 *
 * @author D. Campione
 * 
 */
class XmlTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 2590409492315612997L;

    private Icon elemIcon;
    private String elem;
    private String attr;
    private boolean selected;

    private static final XmlTreeCellUI UI = new XmlTreeCellUI();
    private static final Color ATTR_COLOR = new Color(0x808080);

    public XmlTreeCellRenderer() {
        URL url = getClass().getResource("tag.png");
        if (url != null) { // Always true
            elemIcon = new ImageIcon(url);
        }
        setUI(UI);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean focused) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, focused);
        this.selected = sel;
        if (value instanceof XmlTreeNode) {
            // Don't modify setText() since it determines width of tree node
            XmlTreeNode node = (XmlTreeNode) value;
            elem = node.getElement();
            attr = node.getMainAttr();
        } else {
            elem = attr = null;
        }
        setIcon(elemIcon);
        return this;
    }

    @Override
    public void updateUI() {
        // We must call super.updateUI() since, as of Java 7, that's where
        // DefaultTreeCellRenderer caches its fonts, colors, etc..
        super.updateUI(); // Get "real" new defaults
        setUI(UI); // Doesn't update colors, border, etc.., just paint method
    }

    /**
     * Custom UI for our renderer. This is basically a performance hack to avoid
     * using HTML for our rendering. Swing's HTML rendering engine is very slow,
     * making tree views many thousands of nodes large using HTML very slow for
     * expand operations (our expandInitialNodes() method). This is caused by
     * calls to get the preferred size of each HTML view. A "plain text"
     * renderer that can paint the different colors itself is much faster (~ 4x
     * faster) but still doesn't eliminate the issue for huge trees.
     * 
     * @author D. Campione
     * 
     */
    private static class XmlTreeCellUI extends BasicLabelUI {

        @Override
        protected void installDefaults(JLabel label) {
            // Do nothing
        }

        @Override
        protected void paintEnabledText(JLabel l, Graphics g, String s,
                int textX, int textY) {
            XmlTreeCellRenderer r = (XmlTreeCellRenderer) l;
            Graphics2D g2d = (Graphics2D) g;
            Map<?, ?> hints = SyntaxUtilities.getDesktopAntiAliasHints();
            if (hints != null) {
                g2d.addRenderingHints(hints);
            }
            g2d.setColor(l.getForeground());
            g2d.drawString(r.elem, textX, textY);
            if (r.attr != null) {
                textX += g2d.getFontMetrics().stringWidth(r.elem + " ");
                if (!r.selected) {
                    g2d.setColor(ATTR_COLOR);
                }
                g2d.drawString(r.attr, textX, textY);
            }
            g2d.dispose();
        }

        @Override
        protected void uninstallDefaults(JLabel label) {
            // Do nothing
        }
    }
}