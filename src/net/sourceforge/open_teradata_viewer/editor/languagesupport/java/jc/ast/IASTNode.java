/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

/**
 * A node in a Java AST.
 *
 * @author D. Campione
 * 
 */
public interface IASTNode {

    /**
     * Returns the "name" of this node. This will be the name of the method, the
     * name of the member or local variable, etc... For {@link CodeBlock}s it
     * will be {@link CodeBlock#NAME}.<p>
     *
     * Note that this may not be unique. For example, a class with an overloaded
     * method will have multiple methods with the same "name", just with
     * different signatures.
     *
     * @return The "name" of this node.
     */
    public String getName();

    /**
     * Returns the end offset of the "name" of this node.
     *
     * @return The end offset.
     */
    public int getNameEndOffset();

    /**
     * Returns the start offset of the "name" of this node.
     *
     * @return The start offset.
     */
    public int getNameStartOffset();
}