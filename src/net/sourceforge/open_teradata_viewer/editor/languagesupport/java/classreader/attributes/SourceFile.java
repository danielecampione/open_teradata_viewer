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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantUtf8Info;

/**
 * The <code>SourceFile</code> attribute, an optional fixed-length attribute in
 * the attributes table of a {@link ClassFile}. There can be no more than one
 * <code>SourceFile</code> attribute for a given <code>ClassFile</code>.
 *
 * @author D. Campione
 * 
 */
public class SourceFile extends AttributeInfo {

    /**
     * Index into the constant pool of a {@link ConstantUtf8Info} structure
     * representing the name of the source file from which this class file was
     * compiled.
     */
    private int sourceFileIndex;

    /**
     * Ctor.
     *
     * @param cf The class file.
     * @param sourceFileIndex Index into the constant pool of a {@link
     *        ConstantUtf8Info} structure representing the source file name.
     */
    public SourceFile(ClassFile cf, int sourceFileIndex) {
        super(cf);
        this.sourceFileIndex = sourceFileIndex;
    }

    /**
     * Returns the name of the source file that was compiled to create this
     * class file.
     *
     * @return The name of the source file.
     */
    public String getSourceFileName() {
        return getClassFile().getUtf8ValueFromConstantPool(sourceFileIndex);
    }

    /**
     * Returns a string representation of this attribute. Useful for debugging.
     *
     * @return A string representation of this attribute.
     */
    @Override
    public String toString() {
        return "[SourceFile: " + "file=" + getSourceFileName() + "]";
    }
}