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

/**
 * Class representing a <code>CONSTANT_MethodHandle</code> structure.
 *
 * @author D. Campione
 *
 */
public class ConstantMethodHandleInfo extends ConstantPoolInfo {

    private int referenceKind;

    private int referenceIndex;

    /** Ctor. */
    public ConstantMethodHandleInfo(int referenceKind, int referenceIndex) {
        super(CONSTANT_MethodHandle);
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    public int getReferenceKind() {
        return referenceKind;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }

    /**
     * Returns a string representation of this object. Useful for debugging.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "[ConstantMethodHandleInfo: " + "referenceKind="
                + getReferenceKind() + "; referenceIndex="
                + getReferenceIndex() + "]";
    }
}