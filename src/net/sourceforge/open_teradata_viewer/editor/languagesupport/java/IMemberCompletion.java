/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IconFactory.IIconData;

/**
 * Extra methods defined by a completion for a Java member (fields and methods).
 *
 * @author D. Campione
 * 
 */
interface IMemberCompletion extends IJavaSourceCompletion {

    /**
     * Returns the name of the enclosing class.
     *
     * @param fullyQualified Whether the name returned should be fully
     *        qualified.
     * @return The class name.
     */
    public String getEnclosingClassName(boolean fullyQualified);

    /**
     * Returns the signature of this member.
     *
     * @return The signature.
     */
    public String getSignature();

    /**
     * Returns the type of this member (the return type for methods).
     *
     * @return The type of this member.
     */
    public String getType();

    /** @return Whether this member is deprecated. */
    public boolean isDeprecated();

    /**
     * Meta data about the member. IMember completions will be constructed from
     * a concrete instance of this interface. This is because there are two
     * sources that member completions come from - parsing Java source files and
     * parsing compiled class files (in libraries).
     * 
     * @author D. Campione
     * 
     */
    public static interface IData extends IIconData {

        /**
         * Returns the name of the enclosing class.
         *
         * @param fullyQualified Whether the name returned should be fully
         *        qualified.
         * @return The class name.
         */
        public String getEnclosingClassName(boolean fullyQualified);

        /**
         * Returns the signature of this member.
         *
         * @return The signature.
         * @see IMemberCompletion#getSignature()
         */
        public String getSignature();

        /**
         * Returns the summary description (should be HTML) for this member.
         *
         * @return The summary description or <code>null</code> if there is
         *         none.
         * @see IMemberCompletion#getSummary()
         */
        public String getSummary();

        /**
         * Returns the type of this member (the return type for methods).
         *
         * @return The type of this member.
         * @see IMemberCompletion#getType()
         */
        public String getType();

        /** @return Whether this member is a constructor. */
        public boolean isConstructor();
    }
}