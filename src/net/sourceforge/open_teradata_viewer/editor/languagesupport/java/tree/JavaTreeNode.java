/*
 * Open Teradata Viewer ( editor language support java tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.tree;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.SourceTreeNode;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IASTNode;

/**
 * Base class for nodes in the Java outline tree.
 *
 * @author D. Campione
 * 
 */
class JavaTreeNode extends SourceTreeNode {

    private static final long serialVersionUID = 8400556838662942789L;

    private IASTNode astNode;
    private Icon icon;

    protected static final int PRIORITY_TYPE = 0;
    protected static final int PRIORITY_FIELD = 1;
    protected static final int PRIORITY_CONSTRUCTOR = 2;
    protected static final int PRIORITY_METHOD = 3;
    protected static final int PRIORITY_LOCAL_VAR = 4;
    protected static final int PRIORITY_BOOST_STATIC = -16;

    protected JavaTreeNode(IASTNode node) {
        this(node, null);
    }

    protected JavaTreeNode(IASTNode node, String iconName) {
        this(node, iconName, false);
    }

    protected JavaTreeNode(IASTNode node, String iconName, boolean sorted) {
        super(node, sorted);
        this.astNode = node;
        if (iconName != null) {
            setIcon(IconFactory.get().getIcon(iconName));
        }
    }

    public JavaTreeNode(String text, String iconName) {
        this(text, iconName, false);
    }

    public JavaTreeNode(String text, String iconName, boolean sorted) {
        super(text, sorted);
        if (iconName != null) {
            this.icon = IconFactory.get().getIcon(iconName);
        }
    }

    /** Overridden to compare tree text without HTML. */
    @Override
    public int compareTo(SourceTreeNode obj) {
        int res = -1;
        if (obj instanceof JavaTreeNode) {
            JavaTreeNode jtn2 = (JavaTreeNode) obj;
            res = getSortPriority() - jtn2.getSortPriority();
            if (res == 0 && ((SourceTreeNode) getParent()).isSorted()) {
                res = getText(false).compareToIgnoreCase(jtn2.getText(false));
            }
        }
        return res;
    }

    public IASTNode getASTNode() {
        return astNode;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getText(boolean selected) {
        Object obj = getUserObject();
        return obj != null ? obj.toString() : null;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * Overridden to return the same thing as <tt>getText(false)</tt>, so we
     * look nice with <tt>ToolTipTree</tt>s.
     *
     * @return A string representation of this tree node.
     */
    @Override
    public String toString() {
        return getText(false);
    }
}