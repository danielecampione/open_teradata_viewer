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

/**
 * An attribute that is unknown/unsupported by this decompiler.
 *
 * @author D. Campione
 * 
 */
public class UnsupportedAttribute extends AttributeInfo {

    private String name;

    /**
     * Ctor.
     *
     * @param cf The class file.
     */
    public UnsupportedAttribute(ClassFile cf, String name) {
        super(cf);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of this attribute. Useful for debugging.
     *
     * @return A string representation of this attribute.
     */
    @Override
    public String toString() {
        return "[UnsupportedAttribute: " + "name=" + getName() + "]";
    }
}