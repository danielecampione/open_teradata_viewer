/*
 * Open Teradata Viewer ( editor language support js completion )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion;

import java.util.Iterator;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.FieldInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;

/**
 *
 *
 * @author D. Campione
 *
 */
public class JSFieldData {

    private FieldInfo info;
    private JarManager jarManager;

    public JSFieldData(FieldInfo info, JarManager jarManager) {
        this.info = info;
        this.jarManager = jarManager;
    }

    public Field getField() {
        ClassFile cf = info.getClassFile();
        ISourceLocation loc = jarManager.getSourceLocForClass(cf
                .getClassName(true));
        return getFieldFromSourceLoc(loc, cf);
    }

    /**
     * Scours the source in a location (zip file, directory), looking for a
     * particular class's source. If it is found, it is parsed and the {@link
     * Method} for this method (if any) is returned.
     *
     * @param loc The zip file, jar file or directory to look in.
     * @param cf The {@link ClassFile} representing the class of this method.
     * @return The method or <code>null</code> if it cannot be found or an
     *         IO error occurred.
     */
    private Field getFieldFromSourceLoc(ISourceLocation loc, ClassFile cf) {
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
                    // Get all overloads of this method with the number of
                    // parameters we're looking for. 99% of the time, there will
                    // only be 1, the method we're looking for
                    Iterator<IMember> j = td.getMemberIterator();
                    while (j.hasNext()) {
                        IMember member = j.next();
                        if (member instanceof Field
                                && member.getName().equals(info.getName())) {
                            return (Field) member;
                        }
                    }
                } // if (typeName.equals(cf.getClassName(false)))
            } // for (Iterator i=cu.getTypeDeclarationIterator(); i.hasNext(); )
        } // if (cu!=null)

        return null;
    }

    public String getType(boolean qualified) {
        return info.getTypeString(qualified);
    }

    public boolean isStatic() {
        return info.isStatic();
    }

    public boolean isPublic() {
        int access = info.getAccessFlags();
        return net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                .isPublic(access);
    }

    public String getEnclosingClassName(boolean fullyQualified) {
        return info.getClassFile().getClassName(fullyQualified);
    }
}