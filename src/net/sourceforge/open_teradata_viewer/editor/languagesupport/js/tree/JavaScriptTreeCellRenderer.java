/*
 * Open Teradata Viewer ( editor language support js tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Renderer for JavaScript outline trees.
 *
 * @author D. Campione
 * 
 */
class JavaScriptTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -429792622783608L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);
        if (value instanceof JavaScriptTreeNode) { // Should always be true
            JavaScriptTreeNode node = (JavaScriptTreeNode) value;
            setText(node.getText(sel));
            setIcon(node.getIcon());
        }
        return this;
    }

}