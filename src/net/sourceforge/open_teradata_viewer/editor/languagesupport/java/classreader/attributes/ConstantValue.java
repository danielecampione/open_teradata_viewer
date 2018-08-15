/*
 * Open Teradata Viewer ( editor language support java classreader attributes )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantDoubleInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantFloatInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantIntegerInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantLongInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantPoolInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantStringInfo;

/**
 * The "<code>ConstantValue</code>" attribute, as defined by 4.7.2 of the JVM
 * specification.
 *
 * @author D. Campione
 * 
 */
public class ConstantValue extends AttributeInfo {

    /**
     * An index into the constant pool that gives the constant value represented
     * by this attribute.
     */
    private int constantValueIndex;

    /**
     * Ctor.
     *
     * @param cf The class file.
     * @param constantValueIndex The index into the constant pool that gives the
     *        constant value represented by this attribute.
     */
    public ConstantValue(ClassFile cf, int constantValueIndex) {
        super(cf);
        this.constantValueIndex = constantValueIndex;
    }

    /**
     * Returns the index into the constant pool that gives the constant value
     * represented by this attribute.
     *
     * @return The index.
     */
    public int getConstantValueIndex() {
        return constantValueIndex;
    }

    /** @return The constant's value, as a string. */
    public String getConstantValueAsString() {
        ClassFile cf = getClassFile();
        ConstantPoolInfo cpi = cf.getConstantPoolInfo(getConstantValueIndex());

        if (cpi instanceof ConstantDoubleInfo) {
            ConstantDoubleInfo cdi = (ConstantDoubleInfo) cpi;
            double value = cdi.getDoubleValue();
            return Double.toString(value);
        } else if (cpi instanceof ConstantFloatInfo) {
            ConstantFloatInfo cfi = (ConstantFloatInfo) cpi;
            float value = cfi.getFloatValue();
            return Float.toString(value);
        } else if (cpi instanceof ConstantIntegerInfo) {
            ConstantIntegerInfo cii = (ConstantIntegerInfo) cpi;
            int value = cii.getIntValue();
            return Integer.toString(value);
        } else if (cpi instanceof ConstantLongInfo) {
            ConstantLongInfo cli = (ConstantLongInfo) cpi;
            long value = cli.getLongValue();
            return Long.toString(value);
        } else if (cpi instanceof ConstantStringInfo) {
            ConstantStringInfo csi = (ConstantStringInfo) cpi;
            return csi.getStringValue();
        } else {
            return "INVALID_CONSTANT_TYPE_" + cpi.toString();
        }
    }
}