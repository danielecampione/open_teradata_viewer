/*
 * Open Teradata Viewer ( editor language support java classreader attributes )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.MethodInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantClassInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantPoolInfo;

/**
 * Implementation of the "<code>Exceptions</code>" attribute found in {@link
 * MethodInfo}s.
 *
 * @author D. Campione
 * 
 */
public class Exceptions extends AttributeInfo {

    /** The method this attribute is describing. */
    private MethodInfo mi;

    /**
     * Indices into the constant pool of {@link ConstantClassInfo}s, each
     * representing a class type that this method is declared to throw.
     */
    private int[] exceptionIndexTable;

    /**
     * Ctor.
     *
     * @param mi The method this attribute is describing.
     */
    public Exceptions(MethodInfo mi, int[] exceptionIndexTable) {
        super(mi.getClassFile());
        this.exceptionIndexTable = exceptionIndexTable;
    }

    /**
     * Returns the fully-qualified name of the specified exception.
     *
     * @param index The index of the exception whose name to retrieve.
     * @return The name of the exception.
     */
    public String getException(int index) {
        ClassFile cf = getClassFile();
        ConstantPoolInfo cpi = cf
                .getConstantPoolInfo(exceptionIndexTable[index]);
        ConstantClassInfo cci = (ConstantClassInfo) cpi;
        int nameIndex = cci.getNameIndex();
        String name = cf.getUtf8ValueFromConstantPool(nameIndex);
        return name.replace('/', '.');
    }

    /**
     * Returns the number of exceptions this attribute knows about.
     *
     * @return The number of exceptions.
     */
    public int getExceptionCount() {
        return exceptionIndexTable == null ? 0 : exceptionIndexTable.length;
    }

    /**
     * Returns information about the method this attribute is describing.
     *
     * @return The method information.
     */
    public MethodInfo getMethodInfo() {
        return mi;
    }
}