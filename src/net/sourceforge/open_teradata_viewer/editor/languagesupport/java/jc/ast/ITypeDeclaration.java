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

import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface ITypeDeclaration extends IASTNode, ITypeDeclarationContainer {

    public boolean getBodyContainsOffset(int offs);

    public int getBodyEndOffset();

    public int getBodyStartOffset();

    public ITypeDeclaration getChildType(int index);

    /**
     * Returns the child type declaration of this one that contains the
     * specified offset, if any.
     *
     * @param offs The offset.
     * @return The type declaration or <code>null</code> if the offset is
     *         outside of any child type declaration.
     */
    public ITypeDeclaration getChildTypeAtOffset(int offs);

    public int getChildTypeCount();

    public String getDocComment();

    /**
     * Returns an iterator over all fields defined in this type.
     *
     * @return The iterator.
     * @see #getMethodIterator()
     * @see #getMemberIterator()
     */
    public Iterator<Field> getFieldIterator();

    public IMember getMember(int index);

    public int getMemberCount();

    /**
     * Returns an iterator over all members of this type. Note that an exception
     * may be thrown if a method is added to this type while this iterator is
     * being used.
     *
     * @return The iterator.
     * @see #getMethodIterator()
     */
    public Iterator<IMember> getMemberIterator();

    /**
     * Returns an iterator over all methods defined in this type.
     *
     * @return The iterator.
     * @see #getFieldIterator()
     * @see #getMemberIterator()
     */
    public Iterator<Method> getMethodIterator();

    /**
     * Returns all methods declared in this type with the given name. Does not
     * check for methods with this name in subclasses.
     *
     * @param name The name to check for.
     * @return Any method overloads with that name or an empty list if none.
     */
    public List<Method> getMethodsByName(String name);

    /**
     * Returns the modifiers of this type declaration.
     *
     * @return The modifier list. This may be <code>null</code> if no modifiers
     *         were specified.
     */
    public Modifiers getModifiers();

    /**
     * Returns the name of this type, unqualified.
     *
     * @return The name of this type.
     * @see #getName(boolean)
     */
    @Override
    public String getName();

    /**
     * Returns the name of this type.
     *
     * @param fullyQualified Whether the name returned should be fully
     *        qualified.
     * @return The type's name.
     * @see #getName()
     */
    public String getName(boolean fullyQualified);

    /**
     * Returns the package this type is in.
     *
     * @return The package or <code>null</code> if it's in the default package.
     */
    public Package getPackage();

    /**
     * Returns the parent type declaration.
     *
     * @return The parent type declaration or <code>null</code> if there isn't
     *             one.
     * @see #setParentType(ITypeDeclaration)
     */
    public ITypeDeclaration getParentType();

    public String getTypeString();

    public boolean isDeprecated();

    /**
     * Shortcut for <code>getModifiers().isStatic()</code>; useful since
     * <code>getModifiers()</code> may return <code>null</code>.
     *
     * @return Whether this type declaration is static.
     * @see #getModifiers()
     */
    public boolean isStatic();

    public void setDocComment(String comment);

    /**
     * Sets the parent type declaration for this type declaration.
     *
     * @param parentType The parent type declaration.
     * @see #getParentType()
     */
    public void setParentType(ITypeDeclaration parentType);
}