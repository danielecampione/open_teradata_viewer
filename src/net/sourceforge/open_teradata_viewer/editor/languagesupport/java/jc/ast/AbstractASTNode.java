/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IOffset;

/**
 * Base implementation of an AST node.
 *
 * @author D. Campione
 * 
 */
abstract class AbstractASTNode implements IASTNode {

    private String name;
    private IOffset startOffs;
    private IOffset endOffs;

    protected AbstractASTNode(String name, IOffset start) {
        this(name, start, null);
    }

    protected AbstractASTNode(String name, IOffset start, IOffset end) {
        this.name = name;
        startOffs = start;
        endOffs = end;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public int getNameEndOffset() {
        return endOffs != null ? endOffs.getOffset() : Integer.MAX_VALUE;
    }

    /** {@inheritDoc} */
    @Override
    public int getNameStartOffset() {
        return startOffs != null ? startOffs.getOffset() : 0;
    }

    public void setDeclarationEndOffset(IOffset end) {
        endOffs = end;
    }

    /**
     * Sets the start and end offsets of this node.
     *
     * @param start The start offset.
     * @param end The end offset.
     */
    protected void setDeclarationOffsets(IOffset start, IOffset end) {
        startOffs = start;
        endOffs = end;
    }

    /**
     * Returns the name of this node (e.g. the value of {@link #getName()}.
     * Subclasses can override this method if appropriate.
     *
     * @return A string representation of this node.
     */
    @Override
    public String toString() {
        return getName();
    }
}