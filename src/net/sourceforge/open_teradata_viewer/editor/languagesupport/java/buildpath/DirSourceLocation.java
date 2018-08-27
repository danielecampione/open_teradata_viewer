/*
 * Open Teradata Viewer ( editor language support java buildpath )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser.ASTFactory;

/**
 * Represents Java source in a directory, such as in a project's source folder.
 * 
 * @author D. Campione
 * 
 */
public class DirSourceLocation implements ISourceLocation {

    private File dir;

    /**
     * Ctor.
     *
     * @param dir The directory containing the source files.
     */
    public DirSourceLocation(String dir) {
        this(new File(dir));
    }

    /**
     * Ctor.
     *
     * @param dir The directory containing the source files.
     */
    public DirSourceLocation(File dir) {
        this.dir = dir;
    }

    /** {@inheritDoc} */
    @Override
    public CompilationUnit getCompilationUnit(ClassFile cf) throws IOException {
        CompilationUnit cu = null;

        String entryName = cf.getClassName(true);
        entryName = entryName.replace('.', '/');
        entryName += ".java";
        File file = new File(dir, entryName);
        if (!file.isFile()) {
            // Be nice and check for "src/" subdirectory
            file = new File(dir, "src/" + entryName);
        }

        if (file.isFile()) {
            BufferedReader r = new BufferedReader(new FileReader(file));
            try {
                Scanner s = new Scanner(r);
                cu = new ASTFactory().getCompilationUnit(entryName, s);
            } finally {
                r.close();
            }
        }

        return cu;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocationAsString() {
        return dir.getAbsolutePath();
    }
}