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

import javax.swing.text.Position;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.SourceTreeNode;

/**
 * The tree node in <code>XmlOutlineTree</code>s.
 *
 * @author D. Campione
 * @see XmlOutlineTree
 * 
 */
public class XmlTreeNode extends SourceTreeNode {

    private static final long serialVersionUID = -7143402416930902427L;

    private String mainAttr;
    private Position offset;
    private Position endOffset;

    public XmlTreeNode(String name) {
        super(name);
    }

    public boolean containsOffset(int offs) {
        return offset != null && endOffset != null
                && offs >= offset.getOffset() && offs <= endOffset.getOffset();
    }

    public String getElement() {
        return (String) getUserObject();
    }

    public int getEndOffset() {
        return endOffset != null ? endOffset.getOffset() : Integer.MAX_VALUE;
    }

    public String getMainAttr() {
        return mainAttr;
    }

    public int getStartOffset() {
        return offset != null ? offset.getOffset() : -1;
    }

    public void setEndOffset(Position pos) {
        this.endOffset = pos;
    }

    public void setMainAttribute(String attr) {
        this.mainAttr = attr;
    }

    public void setStartOffset(Position pos) {
        this.offset = pos;
    }

    /** @return A string representation of this tree node. */
    @Override
    public String toString() {
        String text = getElement();
        if (mainAttr != null) {
            text += " " + mainAttr;
        }
        return text;
    }
}