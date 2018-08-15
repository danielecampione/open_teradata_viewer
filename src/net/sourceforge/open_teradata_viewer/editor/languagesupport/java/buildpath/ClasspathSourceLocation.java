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
import java.io.InputStream;
import java.io.InputStreamReader;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser.ASTFactory;

/**
 * Represents Java source files somewhere on the classpath. This might be
 * somewhat of a unique situation, since often source isn't on the classpath,
 * only class files are. However, there may be times when you want to ship both
 * the classes and source for a library and put them on your classpath for
 * simplicity of integrating with this code completion library. In such a case,
 * you would use a <code>ClasspathLibraryInfo</code> and use this class for the
 * source location.<p>
 *
 * This class has no state; any classes it's asked about, it assumes it can find
 * the corresponding .java file somewhere on the classpath using the class's
 * ClassLoader.
 *
 * @author D. Campione
 * @see ClasspathLibraryInfo
 * 
 */
public class ClasspathSourceLocation implements ISourceLocation {

    /** {@inheritDoc} */
    @Override
    public CompilationUnit getCompilationUnit(ClassFile cf) throws IOException {
        CompilationUnit cu = null;

        String res = cf.getClassName(true).replace('.', '/') + ".java";
        InputStream in = getClass().getClassLoader().getResourceAsStream(res);
        if (in != null) {
            Scanner s = new Scanner(new InputStreamReader(in));
            cu = new ASTFactory().getCompilationUnit(res, s);
        }

        return cu;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocationAsString() {
        return null;
    }
}