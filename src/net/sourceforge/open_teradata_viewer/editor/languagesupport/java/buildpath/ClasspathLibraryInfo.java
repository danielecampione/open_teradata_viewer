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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;

/**
 * Information about specific classes on the current application's classpath to
 * add to the "build path". This type of container is useful if your application
 * ships with specific classes you want included in code completion but you
 * don't want to add the entire jar to the build path.<p>
 * 
 * Since there is no real way to determine all classes in a package via
 * reflection, you must explicitly enumerate all classes that are on the
 * classpath that you want on the build path. To make this easier, you can use
 * the {@link ClassEnumerationReader} class to read a list of classes from a
 * plain text file or other resource.<p>
 *
 * If you're delivering the corresponding .java source files also on the
 * classpath (i.e. you have a library "hard-coded" to be on the build path), you
 * can set the source location to be a <code>ClasspathSourceLocation</code> to
 * get the source located automatically.
 *
 * @author D. Campione
 * @see JarLibraryInfo
 * @see DirLibraryInfo
 * @see ClasspathSourceLocation
 * 
 */
public class ClasspathLibraryInfo extends LibraryInfo {

    /**
     * Mapping of class names to <code>ClassFile</code>s. This information is
     * cached even though it's also cached at the <code>JarReader</code> level
     * because the class definitions are effectively immutable since they're on
     * the classpath. This allows you to theoretically share a single
     * <code>ClasspathLibraryInfo</code> across several different jar managers.
     */
    private Map<String, ClassFile> classNameToClassFile;

    /**
     * Ctor.
     *
     * @param classes A list of fully-qualified class names for classes you want
     *        added to the build path.
     */
    public ClasspathLibraryInfo(String[] classes) {
        this(Arrays.asList(classes), null);
    }

    /**
     * Ctor.
     *
     * @param classes A list of fully-qualified class names for classes you want
     *        added to the build path.
     */
    public ClasspathLibraryInfo(List<String> classes) {
        this(classes, null);
    }

    /**
     * Ctor.
     *
     * @param classes A list of fully-qualified class names for classes you want
     *        added to the build path.
     * @param sourceLoc The location of the source files for the classes given.
     *        This may be <code>null</code>.
     */
    public ClasspathLibraryInfo(List<String> classes, ISourceLocation sourceLoc) {
        setSourceLocation(sourceLoc);
        classNameToClassFile = new HashMap<String, ClassFile>();
        int count = classes == null ? 0 : classes.size();
        for (int i = 0; i < count; i++) {
            // Our internal map must have all entries ending in ".class" but the
            // one we pass to client code must not
            String entryName = classes.get(i);
            entryName = entryName.replace('.', '/') + ".class";
            classNameToClassFile.put(entryName, null);
        }
    }

    @Override
    public void bulkClassFileCreationEnd() {
        // Do nothing
    }

    @Override
    public void bulkClassFileCreationStart() {
        // Do nothing
    }

    @Override
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        }
        int res = -1;

        if (o instanceof ClasspathLibraryInfo) {
            ClasspathLibraryInfo other = (ClasspathLibraryInfo) o;
            res = classNameToClassFile.size()
                    - other.classNameToClassFile.size();
            if (res == 0) {
                for (String key : classNameToClassFile.keySet()) {
                    if (!other.classNameToClassFile.containsKey(key)) {
                        res = -1;
                        break;
                    }
                }
            }
        }

        return res;
    }

    @Override
    public ClassFile createClassFile(String entryName) throws IOException {
        return createClassFileBulk(entryName);
    }

    @Override
    public ClassFile createClassFileBulk(String entryName) throws IOException {
        // NOTE: entryName always ends in ".class", so our map must account for
        // this
        ClassFile cf = null;
        if (classNameToClassFile.containsKey(entryName)) {
            cf = classNameToClassFile.get(entryName);
            if (cf == null) {
                cf = createClassFileImpl(entryName);
                classNameToClassFile.put(entryName, cf);
            }
        }
        return cf;
    }

    private ClassFile createClassFileImpl(String res) throws IOException {
        ClassFile cf = null;

        InputStream in = getClass().getClassLoader().getResourceAsStream(res);
        if (in != null) {
            DataInputStream din = null;
            try {
                BufferedInputStream bin = new BufferedInputStream(in);
                din = new DataInputStream(bin);
                cf = new ClassFile(din);
            } finally {
                in.close(); // DIS and BIS just delegate the close to the child
            }
        }

        return cf;
    }

    @Override
    public TreeMap createPackageMap() throws IOException {
        TreeMap packageMap = new TreeMap();
        for (String className : classNameToClassFile.keySet()) {
            String[] tokens = Util.splitOnChar(className, '/');
            TreeMap temp = packageMap;
            for (int j = 0; j < tokens.length - 1; j++) {
                String pkg = tokens[j];
                TreeMap submap = (TreeMap) temp.get(pkg);
                if (submap == null) {
                    submap = new TreeMap();
                    temp.put(pkg, submap);
                }
                temp = submap;
            }
            className = tokens[tokens.length - 1];
            // Our internal map must have all entries ending in ".class" but the
            // one we pass to client code must not
            className = className.substring(0, className.length() - 6);
            temp.put(className, null); // Lazily set value to ClassFile later
        }
        return packageMap;
    }

    /**
     * Since stuff on the current classpath never changes (we don't support
     * hotswapping), this method always returns <code>0</code>.
     *
     * @return <code>0</code> always.
     */
    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public String getLocationAsString() {
        return null;
    }

    @Override
    public int hashCode() {
        return classNameToClassFile.hashCode();
    }
}