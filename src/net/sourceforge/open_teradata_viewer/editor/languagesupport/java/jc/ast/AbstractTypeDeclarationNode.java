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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IOffset;

public abstract class AbstractTypeDeclarationNode extends AbstractASTNode
        implements ITypeDeclaration {

    private Package pkg;
    private Modifiers modifiers;
    private ITypeDeclaration parentType;
    private List<ITypeDeclaration> childTypes;
    private IOffset bodyStartOffs;
    private IOffset bodyEndOffs;
    private boolean deprecated;
    private String docComment;

    // --- "ClassBody"/"InterfaceBody"/EnumConstant fields ---
    private List<IMember> memberList;

    public AbstractTypeDeclarationNode(String name, IOffset start) {
        super(name, start);
        init();
    }

    public AbstractTypeDeclarationNode(String name, IOffset start, IOffset end) {
        super(name, start, end);
        init();
    }

    public void addMember(IMember member) {
        member.setParentTypeDeclaration(this);
        memberList.add(member);
    }

    @Override
    public void addTypeDeclaration(ITypeDeclaration type) {
        if (childTypes == null) {
            childTypes = new ArrayList<ITypeDeclaration>(1); // Usually small
        }
        type.setParentType(this);
        childTypes.add(type);
    }

    @Override
    public boolean getBodyContainsOffset(int offs) {
        return offs >= getBodyStartOffset() && offs < getBodyEndOffset();
    }

    @Override
    public int getBodyEndOffset() {
        return bodyEndOffs != null ? bodyEndOffs.getOffset()
                : Integer.MAX_VALUE;
    }

    @Override
    public int getBodyStartOffset() {
        return bodyStartOffs == null ? 0 : bodyStartOffs.getOffset();
    }

    @Override
    public ITypeDeclaration getChildType(int index) {
        return childTypes.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public ITypeDeclaration getChildTypeAtOffset(int offs) {
        ITypeDeclaration typeDec = null;

        for (int i = 0; i < getChildTypeCount(); i++) {
            ITypeDeclaration td = getChildType(i);
            if (td.getBodyContainsOffset(offs)) {
                typeDec = td;
                break;
            }
        }

        return typeDec;
    }

    @Override
    public int getChildTypeCount() {
        return childTypes == null ? 0 : childTypes.size();
    }

    @Override
    public String getDocComment() {
        return docComment;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Field> getFieldIterator() {
        List<Field> fields = new ArrayList<Field>();
        for (Iterator<IMember> i = getMemberIterator(); i.hasNext();) {
            IMember member = i.next();
            if (member instanceof Field) {
                fields.add((Field) member);
            }
        }
        return fields.iterator();
    }

    @Override
    public IMember getMember(int index) {
        return memberList.get(index);
    }

    @Override
    public int getMemberCount() {
        return memberList.size();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<IMember> getMemberIterator() {
        return memberList.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Method> getMethodIterator() {
        List<Method> methods = new ArrayList<Method>();
        for (Iterator<IMember> i = getMemberIterator(); i.hasNext();) {
            IMember member = i.next();
            if (member instanceof Method) {
                methods.add((Method) member);
            }
        }
        return methods.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public List<Method> getMethodsByName(String name) {
        List<Method> methods = new ArrayList<Method>();
        for (Iterator<IMember> i = getMemberIterator(); i.hasNext();) {
            IMember member = i.next();
            if (member instanceof Method && name.equals(member.getName())) {
                methods.add((Method) member);
            }
        }
        return methods;
    }

    @Override
    public Modifiers getModifiers() {
        return modifiers;
    }

    /** {@inheritDoc} */
    @Override
    public String getName(boolean fullyQualified) {
        String name = getName();
        if (fullyQualified) {
            Package pkg = getPackage();
            if (pkg != null) {
                name = pkg.getName() + "." + name;
            }
        }
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public Package getPackage() {
        return pkg;
    }

    /** {@inheritDoc} */
    @Override
    public ITypeDeclaration getParentType() {
        return parentType;
    }

    private void init() {
        memberList = new ArrayList<IMember>();
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStatic() {
        return modifiers == null ? false : modifiers.isStatic();
    }

    public void setBodyEndOffset(IOffset end) {
        bodyEndOffs = end;
    }

    public void setBodyStartOffset(IOffset start) {
        bodyStartOffs = start;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public void setDocComment(String comment) {
        docComment = comment;
    }

    public void setModifiers(Modifiers modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * Sets the package this type is in.
     *
     * @param pkg The package or <code>null</code> if this is in the default
     *        package.
     * @see #getPackage()
     */
    public void setPackage(Package pkg) {
        this.pkg = pkg;
    }

    /** {@inheritDoc} */
    @Override
    public void setParentType(ITypeDeclaration parentType) {
        this.parentType = parentType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (modifiers != null) {
            sb.append(modifiers.toString()).append(' ');
        }
        sb.append(getTypeString()).append(' ');
        sb.append(getName());
        return sb.toString();
    }
}