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

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;

/**
 * Information about a folder containing a set of classes to add to the "build
 * path". This type of library info could be used, for example, to add sibling
 * projects in a workspace, not yet built into jars, to another project's build
 * path.
 *
 * @author D. Campione
 * @see JarLibraryInfo
 * @see ClasspathLibraryInfo
 * 
 */
public class DirLibraryInfo extends LibraryInfo {

    private File dir;

    public DirLibraryInfo(File dir) {
        this(dir, null);
    }

    public DirLibraryInfo(String dir) {
        this(new File(dir));
    }

    public DirLibraryInfo(File dir, ISourceLocation sourceLoc) {
        setDirectory(dir);
        setSourceLocation(sourceLoc);
    }

    public DirLibraryInfo(String dir, ISourceLocation sourceLoc) {
        this(new File(dir), sourceLoc);
    }

    @Override
    public void bulkClassFileCreationEnd() {
        // Do nothing
    }

    @Override
    public void bulkClassFileCreationStart() {
        // Do nothing
    }

    /**
     * Compares this <code>LibraryInfo</code> to another one. Two instances of
     * this class are only considered equal if they represent the same class
     * file location. Source attachment is irrelevant.
     *
     * @return The sort order of these two library infos.
     */
    @Override
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        }
        int result = -1;
        if (o instanceof DirLibraryInfo) {
            return dir.compareTo(((DirLibraryInfo) o).dir);
        }
        return result;
    }

    @Override
    public ClassFile createClassFile(String entryName) throws IOException {
        return createClassFileBulk(entryName);
    }

    @Override
    public ClassFile createClassFileBulk(String entryName) throws IOException {
        File file = new File(dir, entryName);
        if (!file.isFile()) {
            System.err.println("ERROR: Invalid class file: "
                    + file.getAbsolutePath());
            return null;
        }
        return new ClassFile(file);
    }

    @Override
    public TreeMap createPackageMap() throws IOException {
        TreeMap map = new TreeMap();
        getPackageMapImpl(dir, null, map);
        return map;
    }

    @Override
    public long getLastModified() {
        return dir.lastModified();
    }

    @Override
    public String getLocationAsString() {
        return dir.getAbsolutePath();
    }

    /**
     * Does the dirty-work of finding all class files in a directory tree.
     *
     * @param dir The directory to scan.
     * @param pkg The package name scanned so far, in the form
     *        "<code>com/company/pkgname</code>".
     * @throws IOException If an IO error occurs.
     */
    private void getPackageMapImpl(File dir, String pkg, TreeMap retVal)
            throws IOException {
        File[] children = dir.listFiles();
        TreeMap m = retVal;
        boolean firstTimeThrough = true;

        for (int i = 0; i < children.length; i++) {
            File child = children[i];
            if (child.isFile() && child.getName().endsWith(".class")) {
                if (pkg != null) { // Will be null the first time through
                    if (firstTimeThrough) { // Lazily drill down to pkg map node
                        firstTimeThrough = false;
                        String[] items = Util.splitOnChar(pkg, '/');
                        for (int j = 0; j < items.length; j++) {
                            Object temp = m.get(items[j]);
                            if (temp instanceof TreeMap) {
                                m = (TreeMap) temp;
                            } else if (temp == null) {
                                TreeMap submap = new TreeMap();
                                m.put(items[j], submap);
                                m = submap;
                            } else { // e.g. a ClassFile
                                     // A class with the same name as a package
                                     // name - very unlikely, but could happen.
                                     // In this case, all peer
                                     // classes/directories will share this
                                     // package/class name conflict, so might as
                                     // well bail now
                                return;
                            }
                        }
                    }
                }
                String className = child.getName().substring(0,
                        child.getName().length() - 6);
                m.put(className, null);
            } else if (child.isDirectory()) {
                String subpkg = pkg == null ? child.getName()
                        : (pkg + "/" + child.getName());
                getPackageMapImpl(child, subpkg, retVal);
            }
        }

    }

    @Override
    public int hashCode() {
        return dir.hashCode();
    }

    /**
     * Sets the directory containing the classes.
     *
     * @param dir The directory. This cannot be <code>null</code>.
     */
    private void setDirectory(File dir) {
        if (dir == null || !dir.isDirectory()) {
            String name = dir == null ? "null" : dir.getAbsolutePath();
            throw new IllegalArgumentException("Directory does not exist: "
                    + name);
        }
        this.dir = dir;
    }

    /**
     * Returns a string representation of this jar information. Useful for
     * debugging.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "[DirLibraryInfo: " + "jar=" + dir.getAbsolutePath()
                + "; source=" + getSourceLocation() + "]";
    }
}