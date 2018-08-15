/*
 * Open Teradata Viewer ( editor language support java classreader )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes.AttributeInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes.ConstantValue;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantUtf8Info;

/**
 * Represents a "field_info" structure as defined by the Java VM spec.
 *
 * @author D. Campione
 * 
 */
public class FieldInfo extends MemberInfo {

    /**
     * Index into the constant pool of a {@link ConstantUtf8Info} structure
     * representing the field name, as a simple name.
     */
    private int nameIndex;

    /**
     * Index into the constant pool of a {@link ConstantUtf8Info} structure
     * representing a valid field descriptor.
     */
    private int descriptorIndex;

    /** An array of attributes of this field. */
    private List<AttributeInfo> attributes;

    public static final String CONSTANT_VALUE = "ConstantValue";

    /** Ctor. */
    public FieldInfo(ClassFile cf, int accessFlags, int nameIndex,
            int descriptorIndex) {
        super(cf, accessFlags);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        attributes = new ArrayList<AttributeInfo>(1);
    }

    /**
     * Adds the specified attribute to this field.
     *
     * @param info Information about the attribute.
     */
    public void addAttribute(AttributeInfo info) {
        attributes.add(info);
    }

    /**
     * Returns the specified attribute.
     *
     * @param index The index of the attribute.
     * @return The attribute.
     */
    public AttributeInfo getAttribute(int index) {
        return attributes.get(index);
    }

    /**
     * Returns the number of attributes of this field.
     *
     * @return The number of attributes.
     */
    public int getAttributeCount() {
        return attributes.size();
    }

    public String getConstantValueAsString() {
        ConstantValue cv = getConstantValueAttributeInfo();
        return cv == null ? null : cv.getConstantValueAsString();
    }

    /**
     * Returns the {@link ConstantValue} attribute info for this field, if any.
     *
     * @return The <code>ConstantValue</code> attribute or <code>null</code> if
     *         there isn't one.
     */
    private ConstantValue getConstantValueAttributeInfo() {
        for (int i = 0; i < getAttributeCount(); i++) {
            AttributeInfo ai = attributes.get(i);
            if (ai instanceof ConstantValue) {
                return (ConstantValue) ai;
            }
        }
        return null;
    }

    /** @return The field descriptor of this field. */
    @Override
    public String getDescriptor() {
        return cf.getUtf8ValueFromConstantPool(descriptorIndex);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return cf.getUtf8ValueFromConstantPool(nameIndex);
    }

    /**
     * Returns the index into the constant pool of a {@link ConstantUtf8Info}
     * structure representing the field name, as a simple name.
     *
     * @return The index into the constant pool.
     */
    public int getNameIndex() {
        return nameIndex;
    }

    /**
     * Returns the type of this field, as determined from its field descriptor.
     *
     * @return The type of this field.
     */
    public String getTypeString(boolean qualified) {
        StringBuilder sb = new StringBuilder();

        String descriptor = getDescriptor();
        int braceCount = descriptor.lastIndexOf('[') + 1;

        switch (descriptor.charAt(braceCount)) {
        // BaseType
        case 'B':
            sb.append("byte");
            break;
        case 'C':
            sb.append("char");
            break;
        case 'D':
            sb.append("double");
            break;
        case 'F':
            sb.append("float");
            break;
        case 'I':
            sb.append("int");
            break;
        case 'J':
            sb.append("long");
            break;
        case 'S':
            sb.append("short");
            break;
        case 'Z':
            sb.append("boolean");
            break;

        // ObjectType
        case 'L':
            String clazz = descriptor.substring(braceCount + 1,
                    descriptor.length() - 1);
            if (qualified) {
                clazz = clazz.replace('/', '.');
            } else {
                clazz = clazz.substring(clazz.lastIndexOf('/') + 1);
            }
            sb.append(clazz);
            break;

        // Invalid field descriptor
        default:
            sb.append("UNSUPPORTED_TYPE_").append(descriptor);
            break;

        }

        for (int i = 0; i < braceCount; i++) {
            sb.append("[]");
        }

        return sb.toString();
    }

    public boolean isConstant() {
        return getConstantValueAttributeInfo() != null;
    }

    /**
     * Reads a <code>FieldInfo</code> structure from the specified input
     * stream.
     *
     * @param cf The class file containing this field.
     * @param in The input stream to read from.
     * @return The field information read.
     * @throws IOException If an IO error occurs.
     */
    public static FieldInfo read(ClassFile cf, DataInputStream in)
            throws IOException {
        FieldInfo info = new FieldInfo(cf, in.readUnsignedShort(),
                in.readUnsignedShort(), in.readUnsignedShort());
        int attrCount = in.readUnsignedShort();
        for (int i = 0; i < attrCount; i++) {
            AttributeInfo ai = info.readAttribute(in);
            if (ai != null) {
                info.addAttribute(ai);
            }
        }
        return info;
    }

    /**
     * Reads an attribute for this field from an input stream.
     *
     * @param in The input stream to read from.
     * @return The attribute read, possibly <code>null</code> if it was known to
     *         be unimportant for our purposes.
     * @throws IOException If an IO error occurs.
     */
    private AttributeInfo readAttribute(DataInputStream in) throws IOException {
        AttributeInfo ai = null;

        int attributeNameIndex = in.readUnsignedShort();
        int attributeLength = in.readInt();

        String attrName = cf.getUtf8ValueFromConstantPool(attributeNameIndex);

        if (CONSTANT_VALUE.equals(attrName)) { // 4.7.2
            int constantValueIndex = in.readUnsignedShort();
            ConstantValue cv = new ConstantValue(cf, constantValueIndex);
            ai = cv;
        }
        // Attributes common to all members, or unhandled attributes.
        else {
            ai = super.readAttribute(in, attrName, attributeLength);
        }

        return ai;
    }
}