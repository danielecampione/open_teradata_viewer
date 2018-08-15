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
 * Class representing a <code>CONSTANT_NameAndType_info</code> structure.
 *
 * @author D. Campione
 * 
 */
public class ConstantNameAndTypeInfo extends ConstantPoolInfo {

    private int nameIndex;
    private int descriptorIndex;

    /** Ctor. */
    public ConstantNameAndTypeInfo(int nameIndex, int descriptorIndex) {
        super(CONSTANT_NameAndType);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

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
        return "[ConstantNameAndTypeInfo: " + "descriptorIndex="
                + getDescriptorIndex() + "; nameIndex=" + getNameIndex() + "]";
    }
}