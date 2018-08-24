/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.JarLibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.LibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ImportDeclaration;

/**
 * Manages a list of jars and gets completions from them. This can be shared
 * amongst multiple {@link JavaCompletionProvider} instances.
 *
 * @author D. Campione
 * 
 */
public class JarManager {

    /** Locations of class files to get completions from. */
    private List<JarReader> classFileSources;

    /**
     * Whether to check datestamps on jars/directories when completion
     * information is requested.
     */
    private static boolean checkModified;

    /** Ctor. */
    public JarManager() {
        classFileSources = new ArrayList<JarReader>();
        setCheckModifiedDatestamps(true);
    }

    /**
     * Adds completions matching the specified text to a list.
     *
     * @param p The parent completion provider.
     * @param text The text to match.
     * @param addTo The list to add completion choices to.
     */
    public void addCompletions(ICompletionProvider p, String text,
            Set<ICompletion> addTo) {
        if (text.length() == 0) {
            return;
        }

        // If what they've typed is qualified, add qualified completions
        if (text.indexOf('.') > -1) {
            String[] pkgNames = Util.splitOnChar(text, '.');
            for (int i = 0; i < classFileSources.size(); i++) {
                JarReader jar = classFileSources.get(i);
                jar.addCompletions(p, pkgNames, addTo);
            }
        }

        // If they are (possibly) typing an unqualified class name, see if what
        // they're typing matches any classes in any of jar jars and if so, add
        // completions for them too. This allows the user to get completions for
        // classes not in their import statements
        else {//if (text.indexOf('.')==-1) {
            String lowerCaseText = text.toLowerCase();
            for (int i = 0; i < classFileSources.size(); i++) {
                JarReader jar = classFileSources.get(i);
                List<ClassFile> classFiles = jar
                        .getClassesWithNamesStartingWith(lowerCaseText);
                if (classFiles != null) {
                    for (ClassFile cf : classFiles) {
                        if (net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                                .isPublic(cf.getAccessFlags())) {
                            addTo.add(new ClassCompletion(p, cf));
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds a jar to read from. This is a convenience method for folks only
     * reading classes from jar files.
     *
     * @param jarFile The jar to add. This cannot be <code>null</code>.
     * @return Whether this jar was added (e.g. it wasn't already loaded or it
     *         has a new source path).
     * @throws IOException If an IO error occurs.
     * @see #addClassFileSource(LibraryInfo)
     * @see #addCurrentJreClassFileSource()
     * @see #getClassFileSources()
     * @see #removeClassFileSource(File)
     */
    public boolean addClassFileSource(File jarFile) throws IOException {
        if (jarFile == null) {
            throw new IllegalArgumentException("jarFile cannot be null");
        }
        return addClassFileSource(new JarLibraryInfo(jarFile));
    }

    /**
     * Adds a class file source to read from.
     *
     * @param info The source to add. If this is <code>null</code>, then the
     *        current JVM's main JRE jar (rt.jar or classes.jar on OS X) will be
     *        added. If this source has already been added, adding it again
     *        will do nothing (except possibly update its attached source
     *        location).
     * @return Whether this source was added (e.g. it wasn't already loaded or
     *         it has a new source path).
     * @throws IOException If an IO error occurs.
     * @see #addClassFileSource(File)
     * @see #addCurrentJreClassFileSource()
     * @see #getClassFileSources()
     * @see #removeClassFileSource(LibraryInfo)
     */
    public boolean addClassFileSource(LibraryInfo info) throws IOException {
        if (info == null) {
            throw new IllegalArgumentException("info cannot be null");
        }

        // First see if this jar is already on the "build path"
        for (int i = 0; i < classFileSources.size(); i++) {
            JarReader jar = classFileSources.get(i);
            LibraryInfo info2 = jar.getLibraryInfo();
            if (info2.equals(info)) {
                // Only update if the source location is different
                ISourceLocation source = info.getSourceLocation();
                ISourceLocation source2 = info2.getSourceLocation();
                if ((source == null && source2 != null)
                        || (source != null && !source.equals(source2))) {
                    classFileSources.set(i,
                            new JarReader((LibraryInfo) info.clone()));
                    return true;
                }
                return false;
            }
        }

        // If it isn't on the build path, add it now
        classFileSources.add(new JarReader(info));
        return true;
    }

    /**
     * Adds the current JVM's rt.jar (or class.jar if on OS X) to the list of
     * class file sources. If the application is running in a JDK, the
     * associated source zip is also located and used.
     *
     * @throws IOException If an IO error occurs.
     * @see #addClassFileSource(LibraryInfo)
     */
    public void addCurrentJreClassFileSource() throws IOException {
        addClassFileSource(LibraryInfo.getMainJreJarInfo());
    }

    /**
     * Removes all class file sources from the "build path".
     *
     * @see #removeClassFileSource(LibraryInfo)
     * @see #removeClassFileSource(File)
     * @see #addClassFileSource(LibraryInfo)
     * @see #getClassFileSources()
     */
    public void clearClassFileSources() {
        classFileSources.clear();
    }

    /**
     * Returns whether the "last modified" time stamp on jars and class
     * directories should be checked whenever completions are requested and if
     * the jar/directory has been modified since the last time, reload any
     * cached class file data. This allows for code completion to update
     * whenever dependencies are rebuilt but has the side effect of increased
     * file I/O. By default this option is enabled; if you somehow find the file
     * I/O to be a bottleneck (perhaps accessing jars over a slow NFS mount),
     * you can disable this option.
     *
     * @return Whether jars/directories are checked for modification since the
     *         last access and clear any cached completion information if so.
     * @see #setCheckModifiedDatestamps(boolean)
     */
    public static boolean getCheckModifiedDatestamps() {
        return checkModified;
    }

    public ClassFile getClassEntry(String className) {
        String[] items = Util.splitOnChar(className, '.');

        for (int i = 0; i < classFileSources.size(); i++) {
            JarReader jar = classFileSources.get(i);
            ClassFile cf = jar.getClassEntry(items);
            if (cf != null) {
                return cf;
            }
        }

        return null;
    }

    /**
     * Returns a list of all classes/interfaces/enums with a given (unqualified)
     * name. There may be several, since the name is unqualified.
     *
     * @param name The unqualified name of a type declaration.
     * @param importDeclarations The imports of the compilation unit, if any.
     * @return A list of type declarations with the given name or
     *         <code>null</code> if there are none.
     */
    public List<ClassFile> getClassesWithUnqualifiedName(String name,
            List<ImportDeclaration> importDeclarations) {
        // Might be more than one class/interface/enum with the same name
        List<ClassFile> result = null;

        // Loop through all of our imports
        for (ImportDeclaration idec : importDeclarations) {
            // Static imports are for fields/methods, not classes
            if (!idec.isStatic()) {
                // Wildcard => See if package contains a class with this name
                if (idec.isWildcard()) {
                    String qualified = idec.getName();
                    qualified = qualified.substring(0, qualified.indexOf('*'));
                    qualified += name;
                    ClassFile entry = getClassEntry(qualified);
                    if (entry != null) {
                        if (result == null) {
                            result = new ArrayList<ClassFile>(1); // Usually small
                        }
                        result.add(entry);
                    }
                }
                // Not wildcard => fully-qualified class/interface name
                else {
                    String name2 = idec.getName();
                    String unqualifiedName2 = name2.substring(name2
                            .lastIndexOf('.') + 1);
                    if (unqualifiedName2.equals(name)) {
                        ClassFile entry = getClassEntry(name2);
                        if (entry != null) { // Should always be true
                            if (result == null) {
                                result = new ArrayList<ClassFile>(1); // Usually small
                            }
                            result.add(entry);
                        } else {
                            System.err.println("ERROR: Class not found! - "
                                    + name2);
                        }
                    }
                }
            }
        }

        // Also check java.lang
        String qualified = "java.lang." + name;
        ClassFile entry = getClassEntry(qualified);
        if (entry != null) {
            if (result == null) {
                result = new ArrayList<ClassFile>(1); // Usually small
            }
            result.add(entry);
        }

        return result;
    }

    /**
     * @param pkgName A package name.
     * @return A list of all classes in that package.
     */
    public List<ClassFile> getClassesInPackage(String pkgName, boolean inPkg) {
        List<ClassFile> list = new ArrayList<ClassFile>();
        String[] pkgs = Util.splitOnChar(pkgName, '.');

        for (int i = 0; i < classFileSources.size(); i++) {
            JarReader jar = classFileSources.get(i);
            jar.getClassesInPackage(list, pkgs, inPkg);
        }

        return list;
    }

    /**
     * Returns the jars on the "build path".
     *
     * @return A list of {@link LibraryInfo}s. Modifying a
     *         <code>LibraryInfo</code> in this list will have no effect on this
     *         completion provider; in order to do that, you must re-add the jar
     *         via {@link #addClassFileSource(LibraryInfo)}. If there are no
     *         jars on the "build path", this will be an empty list.
     * @see #addClassFileSource(LibraryInfo)
     */
    public List<LibraryInfo> getClassFileSources() {
        List<LibraryInfo> jarList = new ArrayList<LibraryInfo>(
                classFileSources.size());
        for (JarReader reader : classFileSources) {
            jarList.add(reader.getLibraryInfo()); // Already cloned
        }
        return jarList;
    }

    public ISourceLocation getSourceLocForClass(String className) {
        ISourceLocation sourceLoc = null;
        for (int i = 0; i < classFileSources.size(); i++) {
            JarReader jar = classFileSources.get(i);
            if (jar.containsClass(className)) {
                sourceLoc = jar.getLibraryInfo().getSourceLocation();
                break;
            }
        }
        return sourceLoc;
    }

    /**
     * Removes a jar from the "build path". This is a convenience method for
     * folks only adding and removing jar sources.
     *
     * @param jar The jar to remove.
     * @return Whether the jar was removed. This will be <code>false</code> if
     *         the jar was not on the build path.
     * @see #removeClassFileSource(LibraryInfo)
     * @see #addClassFileSource(LibraryInfo)
     * @see #getClassFileSources()
     */
    public boolean removeClassFileSource(File jar) {
        return removeClassFileSource(new JarLibraryInfo(jar));
    }

    /**
     * Removes a class file source from the "build path".
     *
     * @param toRemove The source to remove.
     * @return Whether source jar was removed. This will be <code>false</code>
     *         if the source was not on the build path.
     * @see #removeClassFileSource(File)
     * @see #addClassFileSource(LibraryInfo)
     * @see #getClassFileSources()
     */
    public boolean removeClassFileSource(LibraryInfo toRemove) {
        for (Iterator<JarReader> i = classFileSources.iterator(); i.hasNext();) {
            JarReader reader = i.next();
            LibraryInfo info = reader.getLibraryInfo();
            if (info.equals(toRemove)) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Sets whether the "last modified" time stamp on jars and class directories
     * should be checked whenever completions are requested and if the
     * jar/directory has been modified since the last time, reload any cached
     * class file data. This allows for code completion to update whenever
     * dependencies are rebuilt but has the side effect of increased file I/O.
     * By default this option is enabled; if you somehow find the file I/O to be
     * a bottleneck (perhaps accessing jars over a slow NFS mount), you can
     * disable this option.
     *
     * @param check Whether to check if any jars/directories have been modified
     *        since the last access and clear any cached completion information
     *        if so.
     * @see #getCheckModifiedDatestamps()
     */
    public static void setCheckModifiedDatestamps(boolean check) {
        checkModified = check;
    }
}