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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IOffset;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.ITokenTypes;

/**
 * A block of code in curly braces in a class.<p>
 * 
 * This class implements the <code>IMember</code> interface because a block can
 * be a member (say, a static block in a class declaration) but usually it's
 * not actually a <code>IMember</code> but something else, e.g. the body of a
 * method or the content of an <code>if</code>-statement, etc..
 *
 * @author D. Campione
 * 
 */
public class CodeBlock extends AbstractMember {

    /** The name of all <code>CodeBlock</code>s. */
    public static final String NAME = "{...}";

    private CodeBlock parent;
    private List<CodeBlock> children;
    private List<LocalVariable> localVars;
    private boolean isStatic;

    public CodeBlock(boolean isStatic, IOffset startOffs) {
        super(NAME, startOffs);
        this.isStatic = isStatic;
    }

    public void add(CodeBlock child) {
        if (children == null) {
            children = new ArrayList<CodeBlock>();
        }
        children.add(child);
        child.setParent(this);
    }

    public void addLocalVariable(LocalVariable localVar) {
        if (localVars == null) {
            localVars = new ArrayList<LocalVariable>();
        }
        localVars.add(localVar);
    }

    public boolean containsOffset(int offs) {
        // Do endOffset first since we'll often iterate from first CodeBlock to
        // last, so checking it first should be faster
        return getNameEndOffset() >= offs && getNameStartOffset() <= offs;
    }

    public CodeBlock getChildBlock(int index) {
        return children.get(index);
    }

    public int getChildBlockCount() {
        return children == null ? 0 : children.size();
    }

    /**
     * Returns the deepest code block nested under this one (or this one itself)
     * containing a given offset.
     *
     * @param offs The offset to look for.
     * @return The deepest-nested code block containing the offset or
     *         <code>null</code> if this code block and none of its children
     *         contain the offset.
     */
    public CodeBlock getDeepestCodeBlockContaining(int offs) {
        if (!containsOffset(offs)) {
            return null;
        }
        for (int i = 0; i < getChildBlockCount(); i++) {
            CodeBlock child = getChildBlock(i);
            if (child.containsOffset(offs)) {
                return child.getDeepestCodeBlockContaining(offs);
            }
        }
        return this;
    }

    @Override
    public String getDocComment() {
        return null;
    }

    public LocalVariable getLocalVar(int index) {
        return localVars.get(index);
    }

    public int getLocalVarCount() {
        return localVars == null ? 0 : localVars.size();
    }

    /**
     * Returns all local variables declared before a given offset, both in this
     * code block and in all parent blocks.
     *
     * @param offs The offset.
     * @return The {@link LocalVariable}s or an empty list of none were declared
     *         before the offset.
     */
    public List<LocalVariable> getLocalVarsBefore(int offs) {
        List<LocalVariable> vars = new ArrayList<LocalVariable>();

        if (localVars != null) {
            for (int i = 0; i < getLocalVarCount(); i++) {
                LocalVariable localVar = getLocalVar(i);
                if (localVar.getNameStartOffset() < offs) {
                    vars.add(localVar);
                } else {
                    break;
                }
            }
        }

        if (parent != null) {
            vars.addAll(parent.getLocalVarsBefore(offs));
        }

        return vars;
    }

    @Override
    public Modifiers getModifiers() {
        Modifiers modifiers = new Modifiers();
        if (isStatic) {
            modifiers.addModifier(ITokenTypes.KEYWORD_STATIC);
        }
        return modifiers;
    }

    public CodeBlock getParent() {
        return parent;
    }

    /**
     * Returns <code>null</code>, since blocks don't have types.
     *
     * @return <code>null</code> always.
     */
    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }

    /**
     * Returns whether this block is a static block (in a class declaration).
     *
     * @return Whether this is a static code block.
     */
    @Override
    public boolean isStatic() {
        return isStatic;
    }

    public void setParent(CodeBlock parent) {
        this.parent = parent;
    }
}