/*
 * Open Teradata Viewer ( editor language support java classreader constantpool )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool;

/**
 * Represents a class or interface.
 *
 * @author D. Campione
 * 
 */
public class ConstantClassInfo extends ConstantPoolInfo {

    /**
     * An index into the constant_pool table. The entry at this index must be a
     * <code>CONSTANT_Utf8_info</code> structure representing a valid,
     * fully-qualified class or interface name encoded in internal form.
     */
    private int nameIndex;

    /**
     * Ctor.
     *
     * @param nameIndex The index into the constant pool containing a {@link
     *        ConstantUtf8Info} representing the fully-qualified class or
     *        interface name, encoded in internal form.
     */
    public ConstantClassInfo(int nameIndex) {
        super(CONSTANT_Class);
        this.nameIndex = nameIndex;
    }

    /**
     * Returns the index into the constant pool table for a
     * <code>ConstantUtf8Info</code> structure representing a valid,
     * fully-qualified class or interface name, encoded in internal form.
     *
     * @return The index into the constant pool table.
     */
    public int getNameIndex() {
        return nameIndex;
    }

    /**
     * Returns a string representation of this object. Useful for debugging.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "[ConstantClassInfo: " + "nameIndex=" + getNameIndex() + "]";
    }
}