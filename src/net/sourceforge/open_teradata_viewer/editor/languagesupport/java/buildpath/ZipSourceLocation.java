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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser.ASTFactory;

/**
 * Represents source inside a zip or jar file. The source can be either in a
 * "<code>src/</code>" subfolder or at the root level of the archive. This class
 * is useful for the JDK or other libraries that come with a
 * <code>src.zip</code> file (<code>src.jar</code> on OS X).
 *
 * @author D. Campione
 * 
 */
public class ZipSourceLocation implements ISourceLocation {

    private File archive;

    /**
     * Ctor.
     *
     * @param archive The archive containing the source. This should be an
     *        absolute path to ensure correctness.
     */
    public ZipSourceLocation(String archive) {
        this(new File(archive));
    }

    /**
     * Ctor.
     *
     * @param archive The archive containing the source.
     */
    public ZipSourceLocation(File archive) {
        this.archive = archive;
    }

    /** {@inheritDoc} */
    @Override
    public CompilationUnit getCompilationUnit(ClassFile cf) throws IOException {
        CompilationUnit cu = null;

        ZipFile zipFile = new ZipFile(archive);

        try {
            String entryName = cf.getClassName(true).replaceAll("\\.", "/");
            entryName += ".java";
            ZipEntry entry = zipFile.getEntry(entryName);
            if (entry == null) {
                // Seen in some src.jar files, for example OS X's src.jar
                entry = zipFile.getEntry("src/" + entryName);
            }

            if (entry != null) {
                InputStream in = zipFile.getInputStream(entry);
                Scanner s = new Scanner(new InputStreamReader(in));
                cu = new ASTFactory().getCompilationUnit(entryName, s);
            }
        } finally {
            zipFile.close(); // Closes the input stream too
        }

        return cu;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocationAsString() {
        return archive.getAbsolutePath();
    }
}