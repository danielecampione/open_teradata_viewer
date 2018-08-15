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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IOffset;

/**
 * Code shared amongst all {@link IMember} nodes.
 *
 * @author D. Campione
 * 
 */
abstract class AbstractMember extends AbstractASTNode implements IMember {

    private ITypeDeclaration parentTypeDec;

    protected AbstractMember(String name, IOffset start) {
        super(name, start);
    }

    protected AbstractMember(String name, IOffset start, IOffset end) {
        super(name, start, end);
    }

    @Override
    public ITypeDeclaration getParentTypeDeclaration() {
        return parentTypeDec;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStatic() {
        Modifiers modifiers = getModifiers();
        return modifiers != null && modifiers.isStatic();
    }

    /** {@inheritDoc} */
    @Override
    public void setParentTypeDeclaration(ITypeDeclaration dec) {
        if (dec == null) {
            throw new InternalError("Parent ITypeDeclaration cannot be null");
        }
        parentTypeDec = dec;
    }
}