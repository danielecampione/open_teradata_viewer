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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.LibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;

/**
 * Reads entries from a source of class files, such as a jar or a "bin/"
 * directory. This class acts as an intermediary between a raw
 * <code>LibraryInfo</code> and the higher level Java completion classes.
 * It caches information about classes and refreshes that cache when
 * appropriate.
 *
 * @author D. Campione
 *
 */
class JarReader {

    /** Information about the jar or directory we're reading classes from. */
    private LibraryInfo info;

    /** Data structure that caches {@link ClassFile}s. */
    private PackageMapNode packageMap;

    private long lastModified;

    /**
     * Ctor.
     *
     * @param info The jar file to read from. This cannot be <code>null</code>.
     * @throws IOException If an IO error occurs reading from the jar file.
     */
    public JarReader(LibraryInfo info) throws IOException {
        this.info = info;
        packageMap = new PackageMapNode();
        loadCompletions();
    }

    /**
     * Gets the completions in this jar that match a given string.
     *
     * @param provider The parent completion provider.
     * @param pkgNames The text to match, split into tokens around the
     *        '<code>.</code>' character. This should be (the start of) a
     *        fully-qualified class, interface or enum name.
     * @param addTo The list to add completion choices to.
     */
    public void addCompletions(ICompletionProvider provider, String[] pkgNames,
            Set<ICompletion> addTo) {
        checkLastModified();
        packageMap.addCompletions(info, provider, pkgNames, addTo);
    }

    /**
     * Checks whether the jar or class file directory has been modified since
     * the last use of this reader. If it has, then any cached
     * <code>ClassFile</code>s are cleared, in case any classes have been
     * updated.
     */
    private void checkLastModified() {
        long newLastModified = info.getLastModified();
        if (newLastModified != 0 && newLastModified != lastModified) {
            int count = 0;
            count = packageMap.clearClassFiles();
            System.out
                    .println("DEBUG: Cleared " + count + " cached ClassFiles");
            lastModified = newLastModified;
        }
    }

    public boolean containsClass(String className) {
        return packageMap.containsClass(className);
    }

    public boolean containsPackage(String pkgName) {
        return packageMap.containsPackage(pkgName);
    }

    public ClassFile getClassEntry(String[] items) {
        return packageMap.getClassEntry(info, items);
    }

    public void getClassesInPackage(List<ClassFile> addTo, String[] pkgs,
            boolean inPkg) {
        packageMap.getClassesInPackage(info, addTo, pkgs, inPkg);
    }

    /**
     * Looks through all classes in this jar or directory, trying to find any
     * whose unqualified names start with a given prefix.
     *
     * @param prefix The prefix of the class names. Case is ignored on this
     *        parameter.
     * @return A list of {@link ClassFile}s representing classes in this jar or
     *         directory whose unqualified names start with the prefix.
     *         This will never be <code>null</code> but may of course be empty.
     */
    public List<ClassFile> getClassesWithNamesStartingWith(String prefix) {
        List<ClassFile> res = new ArrayList<ClassFile>();
        String currentPkg = ""; // Don't use null; we're appending to it
        packageMap.getClassesWithNamesStartingWith(info, prefix, currentPkg,
                res);
        return res;
    }

    /**
     * Returns the physical file on disk.<p>
     *
     * Modifying the returned object will <em>not</em> have any effect on code
     * completion; e.g. changing the source location will not have any effect.
     *
     * @return The info.
     */
    public LibraryInfo getLibraryInfo() {
        return (LibraryInfo) info.clone();
    }

    private void loadCompletions() throws IOException {
        packageMap = info.createPackageMap();
        lastModified = info.getLastModified();
    }

    @Override
    public String toString() {
        return "[JarReader: " + getLibraryInfo() + "]";
    }
}