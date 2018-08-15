/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IMemberCompletion.IData;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;

/**
 * Metadata about a method as read from a Java source file. This class is used
 * by instances of {@link MethodCompletion}.
 *
 * @author D. Campione
 * 
 */
class MethodData implements IData {

    private Method method;

    public MethodData(Method method) {
        this.method = method;
    }

    /** {@inheritDoc} */
    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        ITypeDeclaration td = method.getParentTypeDeclaration();
        if (td == null) {
            ExceptionDialog.hideException(new Exception(
                    "No parent type declaration for: " + getSignature()));
            return "";
        }
        return td.getName(fullyQualified);
    }

    @Override
    public String getIcon() {
        String key = null;

        Modifiers mod = method.getModifiers();
        if (mod == null) {
            key = IconFactory.METHOD_DEFAULT_ICON;
        } else if (mod.isPrivate()) {
            key = IconFactory.METHOD_PRIVATE_ICON;
        } else if (mod.isProtected()) {
            key = IconFactory.METHOD_PROTECTED_ICON;
        } else if (mod.isPublic()) {
            key = IconFactory.METHOD_PUBLIC_ICON;
        } else {
            key = IconFactory.METHOD_DEFAULT_ICON;
        }

        return key;
    }

    @Override
    public String getSignature() {
        return method.getNameAndParameters();
    }

    @Override
    public String getSummary() {
        String docComment = method.getDocComment();
        return docComment != null ? docComment : method.toString();
    }

    @Override
    public String getType() {
        Type type = method.getType();
        return type == null ? "void" : type.toString();
    }

    @Override
    public boolean isAbstract() {
        return method.getModifiers().isAbstract();
    }

    @Override
    public boolean isConstructor() {
        return method.isConstructor();
    }

    @Override
    public boolean isDeprecated() {
        return method.isDeprecated();
    }

    @Override
    public boolean isFinal() {
        return method.getModifiers().isFinal();
    }

    @Override
    public boolean isStatic() {
        return method.getModifiers().isStatic();
    }
}