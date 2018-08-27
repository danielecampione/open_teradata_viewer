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
 * Class corresponding to the <code>CONSTANT_Long_info</code> structure.
 *
 * @author D. Campione
 * 
 */
public class ConstantLongInfo extends ConstantPoolInfo {

    private int highBytes;
    private int lowBytes;

    /** Ctor. */
    public ConstantLongInfo(int highBytes, int lowBytes) {
        super(CONSTANT_Long);
        this.highBytes = highBytes;
        this.lowBytes = lowBytes;
    }

    public int getHighBytes() {
        return highBytes;
    }

    public long getLongValue() {
        return (((long) highBytes << 32)) + lowBytes;
    }

    public int getLowBytes() {
        return lowBytes;
    }

    /**
     * Returns a string representation of this object. Useful for debugging.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "[ConstantLongInfo: " + "value=" + getLongValue() + "]";
    }
}