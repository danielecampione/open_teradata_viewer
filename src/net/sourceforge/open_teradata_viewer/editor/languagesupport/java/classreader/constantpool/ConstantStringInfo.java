/*
 * Open Teradata Viewer ( editor language support java classreader constantpool )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;

/**
 * Class corresponding to the <code>CONSTANT_String_info</code> structure.
 *
 * @author D. Campione
 * 
 */
public class ConstantStringInfo extends ConstantPoolInfo {

    private ClassFile cf;
    private int stringIndex;

    /** Ctor. */
    public ConstantStringInfo(ClassFile cf, int stringIndex) {
        super(CONSTANT_String);
        this.cf = cf;
        this.stringIndex = stringIndex;
    }

    public int getStringIndex() {
        return stringIndex;
    }

    /**
     * Returns the string represented by this constant.
     *
     * @return The string value represented.
     */
    public String getStringValue() {
        return '"' + cf.getUtf8ValueFromConstantPool(getStringIndex()) + '"';
    }

    /**
     * Returns a string representation of this object. Useful for debugging.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "[ConstantStringInfo: " + "stringIndex=" + getStringIndex()
                + "]";
    }
}