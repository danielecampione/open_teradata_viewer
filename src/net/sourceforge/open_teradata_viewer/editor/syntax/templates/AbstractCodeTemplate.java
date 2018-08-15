/*
 * Open Teradata Viewer ( editor syntax templates )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.templates;

/**
 * A base class to build code templates on top of.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractCodeTemplate implements ICodeTemplate {

    private static final long serialVersionUID = 5669573028034679094L;

    /** The ID of this template. */
    private String id;

    /** This no-arg constructor is required for serialization purposes. */
    public AbstractCodeTemplate() {
    }

    /**
     * Creates a new template.
     *
     * @param id The ID for this template.
     * @throws IllegalArgumentException If <code>id</code> is <code>null</code>.
     */
    public AbstractCodeTemplate(String id) {
        setID(id);
    }

    /**
     * Creates a deep copy of this template.
     *
     * @return A deep copy of this template.
     */
    @Override
    public Object clone() {
        // This method can't be abstract as compilers don't like concrete
        // subclassses calling super.clone() on an abstract super
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new InternalError(
                    "ICodeTemplate implementation not Cloneable: "
                            + getClass().getName());
        }
    }

    /**
     * Compares the <code>StaticCodeTemplate</code> to another.
     *
     * @param o Another <code>StaticCodeTemplate</code> object.
     * @return A negative integer, zero, or a positive integer as this object is
     *         less than, equal-to, or greater than the passed-in object.
     * @throws ClassCastException If <code>o</code> is not an instance of
     *         <code>ICodeTemplate</code>.
     */
    public int compareTo(ICodeTemplate o) {
        if (o == null) {
            return -1;
        }
        return getID().compareTo(o.getID());
    }

    /**
     * Overridden to return "<code>true</code>" if
     * {@link #compareTo(ICodeTemplate)} returns <code>0</code>.
     *
     * @return Whether this code template is equal to another.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ICodeTemplate) {
            return compareTo(((ICodeTemplate) obj)) == 0;
        }
        return false;
    }

    /**
     * Returns the ID of this code template.
     *
     * @return The template's ID.
     * @see #setID(String)
     */
    public String getID() {
        return id;
    }

    /** @return The hash code for this template. */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Sets the ID for this template.
     *
     * @param id The ID for this template.
     * @throws IllegalArgumentException If <code>id</code> is <code>null</code>.
     * @see #getID()
     */
    public void setID(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        this.id = id;
    }
}