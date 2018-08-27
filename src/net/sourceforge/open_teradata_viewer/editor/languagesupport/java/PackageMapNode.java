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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.LibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;

/**
 * A data structure modeling all classes in a jar or directory, or on a
 * classpath. It's a recursive mapping of <code>String</code>s to either
 * <code>Map</code>s or {@link ClassFile}s (which are lazily created and may be
 * <code>null</code>). At each level of the nested map, the string key is a
 * package name if its corresponding value is a <code>Map</code>.
 * Examine that <code>Map</code>'s contents to explore the contents of that
 * package. If the corresponding value is a <code>ClassFile</code>, then the
 * string key's value is the name of that class. Finally, if the corresponding
 * value is <code>null</code>, then the string key's value is the name of a
 * class, but its contents have not yet been loaded for use by the code
 * completion library (<code>ClassFile</code>s are lazily loaded to conserve
 * memory).
 *
 * @author D. Campione
 *
 */
public class PackageMapNode {

    /**
     * A mapping of sub-package name to the sub-packages and classes under it.
     */
    private SortedMap<String, PackageMapNode> subpackages;

    /**
     * A mapping of class file names to class files in this package. The actual
     * {@link ClassFile} values are lazily instantiated, so any map entry with a
     * value of <code>null</code> simply has not been created yet.
     */
    private SortedMap<String, ClassFile> classFiles;

    public PackageMapNode() {
        subpackages = new TreeMap<String, PackageMapNode>(
                String.CASE_INSENSITIVE_ORDER);
        classFiles = new TreeMap<String, ClassFile>(
                String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Adds entries for a fully-qualified class name, of the form
     * <code>"net/sourceforge/open_teradata_viewer/util/DynamicIntArray.class"</code>.
     * This method should only be called on the "root" node of a package map.
     *
     * @param className A fully-qualified class name of the form described
     *        above.
     */
    public void add(String className) {
        String[] tokens = Util.splitOnChar(className, '/');
        PackageMapNode pmn = this;

        for (int i = 0; i < tokens.length - 1; i++) {
            String pkg = tokens[i];
            PackageMapNode child = pmn.subpackages.get(pkg);
            if (child == null) {
                child = new PackageMapNode();
                pmn.subpackages.put(pkg, child);
            }
            pmn = child;
        }

        className = tokens[tokens.length - 1];
        // Our internal map must have all entries ending in ".class", but the
        // one we pass to client code must not
        className = className.substring(0, className.length() - 6);
        pmn.classFiles.put(className, null); // Lazily set value to ClassFile later
    }

    /**
     * Gets the completions in this package map that match a given string.
     *
     * @param provider The parent completion provider.
     * @param pkgNames The text to match, split into tokens around the
     *        '<code>.</code>' character. This should be (the start of) a
     *        fully-qualified class, interface or enum name.
     * @param addTo The list to add completion choices to.
     */
    public void addCompletions(LibraryInfo info, ICompletionProvider provider,
            String[] pkgNames, Set<ICompletion> addTo) {
        PackageMapNode map = this;
        for (int i = 0; i < pkgNames.length - 1; i++) {
            map = map.subpackages.get(pkgNames[i]);
            if (map == null) {
                return;
            }
        }

        String fromKey = pkgNames[pkgNames.length - 1];
        String toKey = fromKey + '{'; // Ascii char > largest valid class char

        // First add completions for matching subpackage names
        SortedMap<String, PackageMapNode> subpackages = map.subpackages.subMap(
                fromKey, toKey);
        if (!subpackages.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < pkgNames.length - 1; j++) {
                sb.append(pkgNames[j]).append('.');
            }
            String earlierPackages = sb.toString();

            for (Map.Entry<String, PackageMapNode> entry : subpackages
                    .entrySet()) {
                String completionPackageName = entry.getKey();
                String text = earlierPackages + completionPackageName;
                addTo.add(new PackageNameCompletion(provider, text, fromKey));
            }
        }

        // Next, add completions for matching class names
        SortedMap<String, ClassFile> sm = map.classFiles.subMap(fromKey, toKey);
        for (Map.Entry<String, ClassFile> entry : sm.entrySet()) {
            String key = entry.getKey();
            ClassFile cf = entry.getValue();

            // The ClassFile may have already been loaded for this one
            if (cf != null) {
                boolean inPkg = false;
                if (inPkg
                        || net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                                .isPublic(cf.getAccessFlags())) {
                    addTo.add(new ClassCompletion(provider, cf));
                }
            }
            // If the ClassFile isn't yet cached
            else {
                String[] items = new String[pkgNames.length];
                System.arraycopy(pkgNames, 0, items, 0, pkgNames.length - 1);
                items[items.length - 1] = key;
                cf = getClassEntry(info, items);
                if (cf != null) {
                    boolean inPkg = false;
                    if (inPkg
                            || net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                                    .isPublic(cf.getAccessFlags())) {
                        addTo.add(new ClassCompletion(provider, cf));
                    }
                } else {
                    // This should never happen - class name without a ClassFile
                }
            }
        }
    }

    /**
     * Removes the cache of all <code>ClassFile</code>s from this package map.
     *
     * @return The number of class file entries removed.
     */
    public int clearClassFiles() {
        return clearClassFilesImpl(this);
    }

    private int clearClassFilesImpl(PackageMapNode pmn) {
        int clearedCount = 0;

        for (Map.Entry<String, ClassFile> entry : pmn.classFiles.entrySet()) {
            entry.setValue(null);
            clearedCount++;
        }

        for (Map.Entry<String, PackageMapNode> entry : pmn.subpackages
                .entrySet()) {
            clearedCount += clearClassFilesImpl(entry.getValue());
        }

        return clearedCount;
    }

    public boolean containsClass(String className) {
        String[] items = className.split("\\.");

        PackageMapNode pmn = this;
        for (int i = 0; i < items.length - 1; i++) {
            // "value" can be a ClassFile "too early" here if className is a
            // nested class
            pmn = pmn.subpackages.get(items[i]);
            if (pmn == null) {
                return false;
            }
        }

        return pmn.classFiles.containsKey(items[items.length - 1]);
    }

    public boolean containsPackage(String pkgName) {
        String[] items = Util.splitOnChar(pkgName, '.');

        PackageMapNode pmn = this;
        for (int i = 0; i < items.length; i++) {
            // "value" can be a ClassFile "too early" here if className is a
            // nested class
            pmn = pmn.subpackages.get(items[i]);
            if (pmn == null) {
                return false;
            }
        }

        return true;
    }

    public ClassFile getClassEntry(LibraryInfo info, String[] items) {
        PackageMapNode pmn = this;
        for (int i = 0; i < items.length - 1; i++) {
            pmn = pmn.subpackages.get(items[i]);
            if (pmn == null) {
                return null;
            }
        }

        String className = items[items.length - 1];
        if (pmn.classFiles.containsKey(className)) {
            ClassFile value = pmn.classFiles.get(className);
            if (value != null) { // Already created
                return value;
            } else { // A class, just no ClassFile cached yet
                try {
                    StringBuilder name = new StringBuilder(items[0]);
                    for (int i = 1; i < items.length; i++) {
                        name.append('/').append(items[i]);
                    }
                    name.append(".class");
                    ClassFile cf = info.createClassFile(name.toString());
                    pmn.classFiles.put(className, cf);
                    return cf;
                } catch (IOException ioe) {
                    ExceptionDialog.hideException(ioe);
                }
            }
        }

        return null;
    }

    public void getClassesInPackage(LibraryInfo info, List<ClassFile> addTo,
            String[] pkgs, boolean inPkg) {
        PackageMapNode map = this;

        for (int i = 0; i < pkgs.length; i++) {
            map = map.subpackages.get(pkgs[i]);
            if (map == null) {
                return;
            }
        }

        try {
            info.bulkClassFileCreationStart();
            try {
                for (Map.Entry<String, ClassFile> entry : map.classFiles
                        .entrySet()) {

                    ClassFile cf = entry.getValue();
                    if (cf == null) {
                        StringBuilder name = new StringBuilder(pkgs[0]);
                        for (int j = 1; j < pkgs.length; j++) {
                            name.append('/').append(pkgs[j]);
                        }
                        name.append('/');
                        name.append(entry.getKey()).append(".class");
                        cf = info.createClassFileBulk(name.toString());
                        map.classFiles.put(entry.getKey(), cf);
                        possiblyAddTo(addTo, cf, inPkg);
                    } else {
                        possiblyAddTo(addTo, cf, inPkg);
                    }
                }
            } finally {
                info.bulkClassFileCreationEnd();
            }
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }

    /**
     * Method used to recursively scan our package map for classes whose names
     * start with a given prefix, ignoring case.
     *
     * @param prefix The prefix that the unqualified class names must match
     *        (ignoring case).
     * @param currentPkg The package that <code>map</code> belongs to (i.e. all
     *        levels of packages scanned before this one), separated by
     *        '<code>/</code>'.
     * @param addTo The list to add any matching <code>ClassFile</code>s to.
     */
    void getClassesWithNamesStartingWith(LibraryInfo info, String prefix,
            String currentPkg, List<ClassFile> addTo) {
        final int prefixLen = prefix.length();

        for (Map.Entry<String, PackageMapNode> children : subpackages
                .entrySet()) {
            String key = children.getKey();
            PackageMapNode child = children.getValue();
            child.getClassesWithNamesStartingWith(info, prefix, currentPkg
                    + key + "/", addTo);
        }

        for (Map.Entry<String, ClassFile> cfEntry : classFiles.entrySet()) {
            // If value is null, we only lazily create the ClassFile if
            // necessary (i.e. if the class name does match what they've typed).
            String className = cfEntry.getKey();
            if (className.regionMatches(true, 0, prefix, 0, prefixLen)) {
                ClassFile cf = cfEntry.getValue();
                if (cf == null) {
                    String fqClassName = currentPkg + className + ".class";
                    try {
                        cf = info.createClassFile(fqClassName);
                        cfEntry.setValue(cf); // Update the map
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                if (cf != null) { // Possibly null if IOException above
                    addTo.add(cf);
                }
            }
        }
    }

    private static final void possiblyAddTo(Collection<ClassFile> addTo,
            ClassFile cf, boolean inPkg) {
        if (inPkg
                || net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                        .isPublic(cf.getAccessFlags())) {
            addTo.add(cf);
        }
    }
}