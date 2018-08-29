/*
 * Open Teradata Viewer ( editor macros )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.macros;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * A macro; it consists of a script to run in the Open Teradata Viewer JVM.
 *
 * @author D. Campione
 * 
 */
public class Macro implements Comparable<Macro>, Cloneable {

    /**
     * Name of the macro. Usually the same as the name of the file, without
     * extension.
     */
    private String name;

    /** A short (1-line) description of the macro. */
    private String desc;

    /**
     * Full path to the file. Not a <code>java.io.File</code> to support
     * encoding via XMLEncoder/XMLDecoder.
     */
    private String file;

    /** Keyboard shortcut to execute the macro. */
    private String accelerator;

    /** Ctor used to support serialization. */
    public Macro() {
    }

    /** @return A clone of this macro. */
    @Override
    public Object clone() {
        Macro m = null;
        try {
            m = (Macro) super.clone();
        } catch (CloneNotSupportedException cnse) { // Never happens
            ExceptionDialog.hideException(cnse);
        }
        return m;
    }

    /**
     * Compares this macro to another by name, lexicographically.
     *
     * @param o The other macro.
     * @return The sort order of this macro, compared to another.
     */
    @Override
    public int compareTo(Macro o) {
        int val = -1;
        if (o == this) {
            return 0;
        } else if (o != null) {
            val = String.CASE_INSENSITIVE_ORDER.compare(getName(), o.getName());
        }
        return val;
    }

    /** @return Whether this macro and another have the same name. */
    @Override
    public boolean equals(Object o) {
        return o instanceof Macro && compareTo((Macro) o) == 0;
    }

    public String getAccelerator() {
        return accelerator;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Returns the full path to the file for this macro.
     *
     * @return The full path to the file.
     * @see #setFile(String)
     */
    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Sets the full path to the file for this macro.
     *
     * @param file The full path to the file.
     * @see #getFile()
     */
    public void setFile(String file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Overridden to return the name of this macro. Used by the Macro options
     * panel.
     *
     * @return The name of this macro.
     */
    @Override
    public String toString() {
        return getName();
    }
}