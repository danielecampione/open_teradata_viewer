/*
 * Open Teradata Viewer ( editor language support js ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.VariableDeclaration;

/**
 * A block of code. This can be used to implement <em>very</em> simple parsing
 * for languages that have some concept of code blocks, such as C, Perl, Java,
 * etc.. Currently, using <code>CodeBlock</code>s provides a means of
 * remembering where variables are defined, as well as their scopes.
 *
 * @author D. Campione
 * @see VariableDeclaration
 *
 */
public class CodeBlock {

    private int start;
    private int end;
    private CodeBlock parent;
    private List<CodeBlock> children;
    private List<JavaScriptVariableDeclaration> varDecs;

    /**
     * Ctor.
     *
     * @param start The starting offset of the code block.
     */
    public CodeBlock(int start) {
        this.start = start;
        end = Integer.MAX_VALUE;
    }

    /**
     * Creates and returns a child (nested) code block.
     *
     * @param start The starting offset of the nested code block.
     * @return The code block.
     */
    public CodeBlock addChildCodeBlock(int start) {
        CodeBlock child = new CodeBlock(start);
        child.parent = this;
        if (children == null) {
            children = new ArrayList<CodeBlock>();
        }
        children.add(child);
        return child;
    }

    /**
     * Adds a variable declaration.
     *
     * @param varDec The variable declaration.
     */
    public void addVariable(JavaScriptVariableDeclaration varDec) {
        if (varDecs == null) {
            varDecs = new ArrayList<JavaScriptVariableDeclaration>();
        }
        varDecs.add(varDec);
    }

    /**
     * Returns whether this code block contains a given offset.
     *
     * @param offset The offset.
     * @return Whether this code block contains that offset.
     */
    public boolean contains(int offset) {
        return offset >= start && offset < end;
    }

    /**
     * Returns a child code block.
     *
     * @param index The index of the child code block.
     * @return The child code block.
     * @see #getChildCodeBlockCount()
     */
    public CodeBlock getChildCodeBlock(int index) {
        return children.get(index);
    }

    /**
     * Returns the number of child code blocks.
     *
     * @return The child code block count.
     * @see #getChildCodeBlock(int)
     */
    public int getChildCodeBlockCount() {
        return children == null ? 0 : children.size();
    }

    /**
     * Returns the end offset of this code block.
     *
     * @return The end offset.
     * @see #getStartOffset()
     * @see #setEndOffset(int)
     */
    public int getEndOffset() {
        return end;
    }

    /**
     * Returns the parent code block.
     *
     * @return The parent code block or <code>null</code> if there isn't one.
     */
    public CodeBlock getParent() {
        return parent;
    }

    /**
     * Returns the start offset of this code block.
     *
     * @return The start offset.
     * @see #getEndOffset()
     */
    public int getStartOffset() {
        return start;
    }

    /**
     * Returns a variable declaration.
     *
     * @param index The index of the declaration.
     * @return The declaration.
     * @see #getVariableDeclarationCount()
     */
    public JavaScriptVariableDeclaration getVariableDeclaration(int index) {
        return varDecs.get(index);
    }

    /**
     * Returns the number of variable declarations in this code block.
     *
     * @return The number of variable declarations.
     * @see #getVariableDeclaration(int)
     */
    public int getVariableDeclarationCount() {
        return varDecs == null ? 0 : varDecs.size();
    }

    /**
     * Sets the end offset of this code block.
     *
     * @param end The end offset.
     * @see #getEndOffset()
     */
    public void setEndOffset(int end) {
        this.end = end;
    }

    /**
     * Sets the start offset of this code block.
     *
     * @param start The start offset.
     * @see #getStartOffset()
     */
    public void setStartOffSet(int start) {
        this.start = start;
    }

    public void debug() {
        StringBuilder sb = new StringBuilder();
        outputChild(sb, this, 0);
        System.out.println(sb.toString());
    }

    private void outputChild(StringBuilder sb, CodeBlock block, int tab) {
        String tabs = "";
        for (int i = 0; i < tab; i++) {
            tabs = tabs + "\t";
        }
        sb.append(tabs);
        sb.append("start: " + block.getStartOffset() + "\n");
        sb.append(tabs);
        sb.append("end: " + block.getEndOffset() + "\n");
        sb.append(tabs);
        sb.append("var count: " + block.getVariableDeclarationCount() + "\n"
                + "\n");
        for (int i = 0; i < block.getChildCodeBlockCount(); i++) {
            CodeBlock childBlock = block.getChildCodeBlock(i);
            outputChild(sb, childBlock, tab++);
        }
    }
}