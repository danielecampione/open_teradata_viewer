/*
 * Open Teradata Viewer ( editor language support java buildpath )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath;

import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;

/**
 * Represents the location of Java source, either in a zip file (src.zip), a
 * flat file (source in a project's source folder) or in some other location.
 *
 * @author D. Campione
 * @see DirSourceLocation
 * @see ZipSourceLocation
 * @see ClasspathSourceLocation
 * 
 */
public interface ISourceLocation {

    /**
     * Returns an AST for the specified class file.
     *
     * @param cf The class file to grab the AST for.
     * @return The AST or <code>null</code> if it cannot be found.
     * @throws IOException If an IO error occurs.
     */
    CompilationUnit getCompilationUnit(ClassFile cf) throws IOException;

    /**
     * Returns a string representation of this source location. For locations on
     * disk such as zip files or directories, this should be the full path to
     * the resource.
     *
     * @return The location of this source as a string or <code>null</code> if
     *         it is not an accessible location.
     */
    public String getLocationAsString();
}