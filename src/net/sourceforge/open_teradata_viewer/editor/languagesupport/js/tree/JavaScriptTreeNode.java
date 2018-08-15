/*
 * Open Teradata Viewer ( editor language support js tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.tree;

import javax.swing.Icon;
import javax.swing.text.Position;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.SourceTreeNode;
import sun.org.mozilla.javascript.internal.ast.AstNode;

/**
 * Tree node for JavaScript outline trees.
 *
 * @author D. Campione
 * 
 */
public class JavaScriptTreeNode extends SourceTreeNode {

    private static final long serialVersionUID = 9059590844140116343L;

    /** The location of this source element in the document. */
    private Position pos;

    /** The text to display in the tree. */
    private String text;

    /** The icon this node displays in the tree. */
    private Icon icon;

    public JavaScriptTreeNode(AstNode userObject) {
        super(userObject);
    }

    public JavaScriptTreeNode(AstNode userObject, boolean sorted) {
        super(userObject, sorted);
    }

    public Icon getIcon() {
        return icon;
    }

    /**
     * Returns the length in the document of this source element.
     *
     * @return The length of this element.
     * @see #getOffset()
     */
    public int getLength() {
        return ((AstNode) getUserObject()).getLength();
    }

    /**
     * Returns the offset into the document of this source element. This offset
     * tracks modifications in the document and has been updated accordingly.
     *
     * @return The offset.
     * @see #getLength()
     */
    public int getOffset() {
        return pos.getOffset();
    }

    public String getText(boolean selected) {
        return text;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * Sets the absolute offset of this element in the document.
     *
     * @param offs The offset.
     * @see #getOffset()
     */
    public void setOffset(Position offs) {
        this.pos = offs;
    }

    /**
     * Sets the text to display in the tree for this node.
     *
     * @param text The text to display.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Overridden to return the textual representation displayed in the tree
     * view.
     *
     * @return The text of this tree node.
     */
    @Override
    public String toString() {
        return getText(false);
    }
}