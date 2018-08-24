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

import java.io.UnsupportedEncodingException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Class representing a <code>CONSTANT_Utf8_info</code> structure.
 *
 * @author D. Campione
 * 
 */
public class ConstantUtf8Info extends ConstantPoolInfo {

    private String representedString;

    /** Ctor. */
    public ConstantUtf8Info(byte[] bytes) {
        super(CONSTANT_Utf8);
        representedString = createRepresentedString(bytes);
    }

    private String createRepresentedString(byte[] bytes) {
        try {
            representedString = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException uee) { // Never happens
            ExceptionDialog.hideException(uee);
            System.exit(0);
        }
        return representedString;
    }

    /**
     * Returns the string represented by this info.
     *
     * @param quoted Whether to add enclosing quotation marks and "escape" any
     *        quotation marks in the represented string.
     * @return The string represented.
     */
    public String getRepresentedString(boolean quoted) {
        if (!quoted) {
            return representedString;
        }
        String temp = "\"" + representedString.replaceAll("\"", "\\\"") + "\"";
        return temp;
    }

    /**
     * Returns a string representation of this object. Useful for debugging.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "[ConstantUtf8Info: " + representedString + "]";
    }
}