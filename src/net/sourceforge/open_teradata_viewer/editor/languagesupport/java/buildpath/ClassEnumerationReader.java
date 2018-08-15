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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads plain text files enumerating classes to take from the classpath and add
 * to a {@link ClasspathLibraryInfo}. Files should have a format similar to the
 * following:
 * 
 * <pre>
 * - com.mycompany.pkg1.Class1
 * Class2
 * Class3
 * - com.mycompany.pkg2.Foo
 * Bar
 * - another.pkg.Utils
 * ...
 * </pre>
 * 
 * Such files are expected to be UTF-8. The exact file structure is as follows:
 * <ul>
 *    <li>Lines that start with a "<code>-</code>" denote a fully-qualified
 *        class, interface or enum name.
 *    <li>Lines following a line starting with "<code>-</code>" are simply a
 *        class, interface or enum name and are assumed to be in the same
 *        package as the previous class on the "<code>-</code>" line.
 *    <li>Blank lines and lines starting with "<code>#</code>" are ignored.
 * </ul>
 *
 * @author D. Campione
 * 
 */
public class ClassEnumerationReader {

    /** Private constructor to prevent instantiation. */
    private ClassEnumerationReader() {
    }

    /**
     * Returns the list of classes specified in the given stream.
     *
     * @param in The input stream to read from. This will be closed when this
     *        method returns.
     * @return The list of class names read.
     * @throws IOException If an IO error occurs.
     */
    public static List<String> getClassNames(InputStream in) throws IOException {
        String lastPkg = null;
        String line = null;
        List<String> classNames = new ArrayList<String>();

        BufferedReader r = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));
        try {
            while ((line = r.readLine()) != null) {
                // Skip blank lines and comments
                line = line.trim();
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }

                // A new fully-qualified class name
                if (line.charAt(0) == '-') {
                    line = line.substring(1).trim();
                    classNames.add(line);
                    int lastDot = line.lastIndexOf('.');
                    lastPkg = line.substring(0, lastDot + 1);
                }
                // Just a class name
                else {
                    String className = line;
                    if (lastPkg != null) {
                        className = lastPkg + className;
                    }
                    classNames.add(className);
                }
            }
        } finally {
            r.close();
        }

        return classNames;
    }
}