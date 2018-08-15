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

import java.io.DataInputStream;
import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class AttributeInfo {

    private ClassFile cf;
    public int attributeNameIndex;

    protected AttributeInfo(ClassFile cf) {
        this.cf = cf;
    }

    public ClassFile getClassFile() {
        return cf;
    }

    /** @return The name of this attribute. */
    public String getName() {
        return cf.getUtf8ValueFromConstantPool(attributeNameIndex);
    }

    /**
     * Reads an unknown/unsupported attribute from an input stream.
     *
     * @param cf The class file containing the attribute.
     * @param in The input stream to read from.
     * @param attrName The name of the attribute.
     * @param attrLength The length of the data to read from <code>in</code>, in
     *        bytes.
     * @return The attribute.
     * @throws IOException If an IO error occurs.
     */
    public static UnsupportedAttribute readUnsupportedAttribute(ClassFile cf,
            DataInputStream in, String attrName, int attrLength)
            throws IOException {
        Util.skipBytes(in, attrLength);
        return new UnsupportedAttribute(cf, attrName);
    }
}