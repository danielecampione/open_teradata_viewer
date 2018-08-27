/*
 * Open Teradata Viewer ( editor language support java classreader )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader;

import java.io.DataInputStream;
import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes.AttributeInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes.Signature;

/**
 * Base class for information about members (fields and methods).
 *
 * @author D. Campione
 * @see FieldInfo
 * @see MethodInfo
 * 
 */
public abstract class MemberInfo {

    /** The class file in which this method is defined. */
    protected ClassFile cf;

    /**
     * A mask of flags used to denote access permission to and properties of
     * this method.
     */
    private int accessFlags;

    /** Whether this member is deprecated. */
    private boolean deprecated;

    /** Attribute marking a member as deprecated. */
    public static final String DEPRECATED = "Deprecated";

    /** Attribute containing index of the member's signature. */
    public static final String SIGNATURE = "Signature";

    /** Attribute containing runtime-visible annotations. */
    public static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";

    /**
     * Ctor.
     *
     * @param cf The class file defining this member.
     */
    protected MemberInfo(ClassFile cf, int accessFlags) {
        this.cf = cf;
        this.accessFlags = accessFlags;
    }

    /**
     * Returns the access flags for this field.
     *
     * @return The access flags, as a bit field.
     * @see IAccessFlags
     */
    public int getAccessFlags() {
        return accessFlags;
    }

    /** @return The parent class file. */
    public ClassFile getClassFile() {
        return cf;
    }

    /** @return The name of this member. */
    public abstract String getName();

    /** @return Whether this member is deprecated. */
    public boolean isDeprecated() {
        return deprecated;
    }

    /** @return The descriptor of this member. */
    public abstract String getDescriptor();

    /** @return Whether this member is final. */
    public boolean isFinal() {
        return (getAccessFlags() & IAccessFlags.ACC_FINAL) > 0;
    }

    /** @return Whether this member is static. */
    public boolean isStatic() {
        return (getAccessFlags() & IAccessFlags.ACC_STATIC) > 0;
    }

    /**
     * Reads attributes common to all members. If the specified attribute is not
     * common to members, the attribute returned is an "unsupported" attribute.
     *
     * @return The attribute or <code>null</code> if it was purposely skipped
     *         for some reason (known to be useless for our purposes, etc..).
     * @throws IOException
     */
    protected AttributeInfo readAttribute(DataInputStream in, String attrName,
            int attrLength) throws IOException {
        AttributeInfo ai = null;

        if (DEPRECATED.equals(attrName)) { // 4.7.10
            // No need to read anything else, attributeLength==0
            deprecated = true;
        } else if (SIGNATURE.equals(attrName)) { // 4.8.8
            int signatureIndex = in.readUnsignedShort();
            String typeSig = cf.getUtf8ValueFromConstantPool(signatureIndex);
            ai = new Signature(cf, typeSig);
        } else if (RUNTIME_VISIBLE_ANNOTATIONS.equals(attrName)) { // 4.8.15
            Util.skipBytes(in, attrLength);
        } else {
            ai = AttributeInfo.readUnsupportedAttribute(cf, in, attrName,
                    attrLength);
        }

        return ai;
    }
}