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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;

/**
 * A marker for a member of a class or interface.
 *
 * @author D. Campione
 * 
 */
public interface IMember extends IASTNode {

    public String getDocComment();

    @Override
    public int getNameEndOffset();

    @Override
    public int getNameStartOffset();

    public Modifiers getModifiers();

    @Override
    public String getName();

    public ITypeDeclaration getParentTypeDeclaration();

    public Type getType();

    public boolean isDeprecated();

    /**
     * Shortcut for <code>getModifiers().isStatic()</code>; useful since
     * <code>getModifiers()</code> may return <code>null</code>.
     *
     * @return Whether this member is static.
     * @see #getModifiers()
     */
    public boolean isStatic();

    public void setParentTypeDeclaration(ITypeDeclaration dec);
}