/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.util.Iterator;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IMemberCompletion.IData;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.FieldInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;

/**
 * Metadata about a field as read from a class file. This class is used by
 * instances of {@link FieldCompletion}.
 *
 * @author D. Campione
 * 
 */
class FieldInfoData implements IData {

    private FieldInfo info;
    private SourceCompletionProvider provider;

    public FieldInfoData(FieldInfo info, SourceCompletionProvider provider) {
        this.info = info;
        this.provider = provider;
    }

    /** {@inheritDoc} */
    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return info.getClassFile().getClassName(fullyQualified);
    }

    /** {@inheritDoc} */
    @Override
    public String getIcon() {
        String key = null;
        int flags = info.getAccessFlags();

        if (net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                .isDefault(flags)) {
            key = IconFactory.FIELD_DEFAULT_ICON;
        } else if (net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                .isPrivate(flags)) {
            key = IconFactory.FIELD_PRIVATE_ICON;
        } else if (net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                .isProtected(flags)) {
            key = IconFactory.FIELD_PROTECTED_ICON;
        } else if (net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                .isPublic(flags)) {
            key = IconFactory.FIELD_PUBLIC_ICON;
        } else {
            key = IconFactory.FIELD_DEFAULT_ICON;
        }

        return key;
    }

    /** {@inheritDoc} */
    @Override
    public String getSignature() {
        return info.getName();
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        ClassFile cf = info.getClassFile();
        ;
        ISourceLocation loc = provider.getSourceLocForClass(cf
                .getClassName(true));
        String summary = null;

        // First, try to parse the Javadoc for this method from the attached
        // source
        if (loc != null) {
            summary = getSummaryFromSourceLoc(loc, cf);
        }

        // Default to the field name
        if (summary == null) {
            summary = info.getName();
        }
        return summary;
    }

    /**
     * Scours the source in a location (zip file, directory), looking for a
     * particular class's source. If it is found, it is parsed and the Javadoc
     * for this field (if any) is returned.
     *
     * @param loc The zip file, jar file or directory to look in.
     * @param cf The {@link ClassFile} representing the class of this field.
     * @return The summary or <code>null</code> if the field has no javadoc, the
     *         class's source was not found or an IO error occurred.
     */
    private String getSummaryFromSourceLoc(ISourceLocation loc, ClassFile cf) {
        String summary = null;
        CompilationUnit cu = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                .getCompilationUnitFromDisk(loc, cf);

        // If the class's source was found and successfully parsed, look for
        // this method
        if (cu != null) {
            Iterator<ITypeDeclaration> i = cu.getTypeDeclarationIterator();
            while (i.hasNext()) {
                ITypeDeclaration td = i.next();
                String typeName = td.getName();

                // Avoid inner classes, etc..
                if (typeName.equals(cf.getClassName(false))) {
                    // Locate our field
                    Iterator<IMember> j = td.getMemberIterator();
                    while (j.hasNext()) {
                        IMember member = j.next();
                        if (member instanceof Field
                                && member.getName().equals(info.getName())) {
                            Field f2 = (Field) member;
                            summary = f2.getDocComment();
                            break;
                        }
                    }
                } // if (typeName.equals(cf.getClassName(false)))
            } // for (Iterator i=cu.getTypeDeclarationIterator(); i.hasNext(); )
        } // if (cu!=null)

        return summary;
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return info.getTypeString(false);
    }

    /**
     * Always returns <code>false</code> since fields cannot be abstract.
     *
     * @return <code>false</code> always.
     */
    @Override
    public boolean isAbstract() {
        return false; // Fields cannot be abstract
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
        return info.isDeprecated();
    }

    @Override
    public boolean isFinal() {
        return info.isFinal();
    }

    @Override
    public boolean isStatic() {
        return info.isStatic();
    }
}