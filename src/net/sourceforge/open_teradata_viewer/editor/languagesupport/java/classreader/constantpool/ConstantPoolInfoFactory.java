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

import java.io.DataInputStream;
import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ConstantPoolInfoFactory implements IConstantTypes {

    /** Private constructor to prevent instantiation. */
    private ConstantPoolInfoFactory() {
    }

    public static ConstantPoolInfo readConstantPoolInfo(ClassFile cf,
            DataInputStream in) throws IOException {
        ConstantPoolInfo cpi = null;
        int tag = in.read();

        switch (tag) {

        case CONSTANT_Class:
            int nameIndex = in.readUnsignedShort();
            cpi = new ConstantClassInfo(nameIndex);
            break;

        case CONSTANT_Double:
            int highBytes = in.readInt();
            int lowBytes = in.readInt();
            cpi = new ConstantDoubleInfo(highBytes, lowBytes);
            break;

        case CONSTANT_Fieldref:
            int classIndex = in.readUnsignedShort();
            int nameAndTypeIndex = in.readUnsignedShort();
            cpi = new ConstantFieldrefInfo(classIndex, nameAndTypeIndex);
            break;

        case CONSTANT_Float:
            int bytes = in.readInt();
            cpi = new ConstantFloatInfo(bytes);
            break;

        case CONSTANT_Integer:
            bytes = in.readInt();
            cpi = new ConstantIntegerInfo(bytes);
            break;

        case CONSTANT_InterfaceMethodref:
            classIndex = in.readUnsignedShort();
            nameAndTypeIndex = in.readUnsignedShort();
            cpi = new ConstantInterfaceMethodrefInfo(classIndex,
                    nameAndTypeIndex);
            break;

        case CONSTANT_Long:
            highBytes = in.readInt();
            lowBytes = in.readInt();
            cpi = new ConstantLongInfo(highBytes, lowBytes);
            break;

        case CONSTANT_Methodref:
            classIndex = in.readUnsignedShort();
            nameAndTypeIndex = in.readUnsignedShort();
            cpi = new ConstantMethodrefInfo(classIndex, nameAndTypeIndex);
            break;

        case CONSTANT_NameAndType:
            nameIndex = in.readUnsignedShort();
            int descriptorIndex = in.readUnsignedShort();
            cpi = new ConstantNameAndTypeInfo(nameIndex, descriptorIndex);
            break;

        case CONSTANT_String:
            int stringIndex = in.readUnsignedShort();
            cpi = new ConstantStringInfo(cf, stringIndex);
            break;

        case CONSTANT_Utf8:
            int count = in.readUnsignedShort();
            byte[] byteArray = new byte[count];
            in.readFully(byteArray);
            cpi = new ConstantUtf8Info(byteArray);
            break;

        default:
            throw new IOException("Unknown tag for constant pool info: " + tag);
        }

        return cpi;
    }
}