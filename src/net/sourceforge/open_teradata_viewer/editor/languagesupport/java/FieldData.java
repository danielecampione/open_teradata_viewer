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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;

/**
 * Metadata about a field as read from a Java source file. This class is used by
 * instances of {@link FieldCompletion}.
 *
 * @author D. Campione
 * 
 */
class FieldData implements IData {

    private Field field;

    public FieldData(Field field) {
        this.field = field;
    }

    /** {@inheritDoc} */
    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        ITypeDeclaration td = field.getParentTypeDeclaration();
        if (td == null) {
            ExceptionDialog.hideException(new Exception(
                    "No parent type declaration for: " + getSignature()));
            return "";
        }
        return td.getName(fullyQualified);
    }

    /** {@inheritDoc} */
    @Override
    public String getIcon() {
        String key = null;

        Modifiers mod = field.getModifiers();
        if (mod == null) {
            key = IconFactory.FIELD_DEFAULT_ICON;
        } else if (mod.isPrivate()) {
            key = IconFactory.FIELD_PRIVATE_ICON;
        } else if (mod.isProtected()) {
            key = IconFactory.FIELD_PROTECTED_ICON;
        } else if (mod.isPublic()) {
            key = IconFactory.FIELD_PUBLIC_ICON;
        } else {
            key = IconFactory.FIELD_DEFAULT_ICON;
        }

        return key;
    }

    /** {@inheritDoc} */
    @Override
    public String getSignature() {
        return field.getName();
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        String docComment = field.getDocComment();
        return docComment != null ? docComment : field.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return field.getType().toString();
    }

    @Override
    public boolean isAbstract() {
        return field.getModifiers().isAbstract();
    }

    /**
     * Always returns <code>false</code>, fields cannot be constructors.
     *
     * @return <code>false</code> always.
     */
    @Override
    public boolean isConstructor() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDeprecated() {
        return field.isDeprecated();
    }

    @Override
    public boolean isFinal() {
        return field.getModifiers().isFinal();
    }

    @Override
    public boolean isStatic() {
        return field.getModifiers().isStatic();
    }
}