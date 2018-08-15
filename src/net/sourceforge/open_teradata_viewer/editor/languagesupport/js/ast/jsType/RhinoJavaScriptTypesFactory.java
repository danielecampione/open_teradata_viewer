/*
 * Open Teradata Viewer ( editor language support js ast jsType )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser.RhinoJavaScriptAstParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclarationFactory;

/**
 * Rhino Specific JavaScriptTypesFactory.
 * 
 * Supports: importPackage and importClass.
 * 
 * importPackage(java.util)
 * importClass(java.util.HashSet)
 * 
 * Clears the cache every time document is parsed for importPackage and
 * importClass to work properly.
 * 
 * @author D. Campione
 * 
 */
public class RhinoJavaScriptTypesFactory extends JSR223JavaScriptTypesFactory {

    private LinkedHashSet<String> importClasses = new LinkedHashSet<String>();
    private LinkedHashSet<String> importPackages = new LinkedHashSet<String>();

    public RhinoJavaScriptTypesFactory(TypeDeclarationFactory typesFactory) {
        super(typesFactory);
    }

    public void addImportClass(String qualifiedClass) {
        importClasses.add(qualifiedClass);
    }

    public void addImportPackage(String packageName) {
        importPackages.add(packageName);
    }

    public void mergeImports(HashSet<String> packages, HashSet<String> classes) {
        mergeImports(packages, importPackages, true);
        mergeImports(classes, importClasses, false);
    }

    private void mergeImports(HashSet<String> newImports,
            LinkedHashSet<String> oldImports, boolean packages) {
        // Iterate through the old imports and check whether the import exists
        // in new. If not then add to remove and remove all types for that
        // package/class
        HashSet<String> remove = new HashSet<String>();
        for (String obj : oldImports) {
            if (!newImports.contains(obj)) {
                remove.add(obj);
            }
        }

        // Now iterate through the remove list and remove imports not needed
        if (!remove.isEmpty()) {
            HashSet<TypeDeclaration> removeTypes = new HashSet<TypeDeclaration>();
            for (String name : remove) {
                for (TypeDeclaration dec : cachedTypes.keySet()) {
                    if ((packages && dec.getQualifiedName().startsWith(name))
                            || (!packages && dec.getQualifiedName()
                                    .equals(name))) {
                        removeAllTypes(cachedTypes.get(dec));
                        removeTypes.add(dec);
                    }
                }
            }
            cachedTypes.keySet().removeAll(removeTypes);
        }

        if (canClearCache(newImports, oldImports)) {
            // Now clear and swap
            oldImports.clear();
            // Remove types
            clearAllImportTypes();
            // Add all imports to cached imports
            oldImports.addAll(newImports);
        }
    }

    /**
     * Validate whether the newImports and old imports contain the same values.
     */
    private boolean canClearCache(HashSet<String> newImports,
            LinkedHashSet<String> oldImports) {
        if (newImports.size() != oldImports.size()) {
            return true;
        }

        for (String im : oldImports) {
            if (!newImports.contains(im)) {
                return true;
            }
        }

        return false;
    }

    public void clearImportCache() {
        importClasses.clear();
        importPackages.clear();
        clearAllImportTypes();
    }

    private void clearAllImportTypes() {
        HashSet<TypeDeclaration> removeTypes = new HashSet<TypeDeclaration>();
        // Clear all non ECMA (JavaScript types) for importPackage and
        // importClass to work properly
        for (Iterator<TypeDeclaration> i = cachedTypes.keySet().iterator(); i
                .hasNext();) {
            TypeDeclaration dec = i.next();
            if (!typesFactory.isJavaScriptType(dec)
                    && !dec.equals(typesFactory.getDefaultTypeDeclaration())) {
                removeAllTypes(cachedTypes.get(dec));
                removeTypes.add(dec);
            }
        }
        cachedTypes.keySet().removeAll(removeTypes);
    }

    /**
     * Remove all TypeDeclarations from the TypeDeclarationFactory from the
     * JavaScriptType and all it's extended classes.
     */
    private void removeAllTypes(JavaScriptType type) {
        if (type != null) {
            typesFactory.removeType(type.getType().getQualifiedName());
            if (type.getExtendedClasses().size() > 0) {
                for (Iterator<JavaScriptType> i = type.getExtendedClasses()
                        .iterator(); i.hasNext();) {
                    JavaScriptType extendedType = i.next();
                    removeAllTypes(extendedType);
                }
            }
        }
    }

    /**
     * Override getClassFile that checks the imported packages and classnames
     * based on the TypeDeclaration.getAPITypeName().
     */
    @Override
    public ClassFile getClassFile(JarManager manager, TypeDeclaration type) {
        String qName = removePackagesFromType(type.getQualifiedName());
        ClassFile file = super.getClassFile(manager,
                JavaScriptHelper.createNewTypeDeclaration(qName));
        if (file == null) {
            file = findFromClasses(manager, qName);
            if (file == null) {
                file = findFromImport(manager, qName);
            }
        }
        return file;
    }

    private String removePackagesFromType(String type) {
        if (type.startsWith(RhinoJavaScriptAstParser.PACKAGES)) {
            return RhinoJavaScriptAstParser.removePackages(type);
        }
        return type;
    }

    /** Look for class file using imported classes. */
    private ClassFile findFromClasses(JarManager manager, String name) {
        ClassFile file = null;
        for (String cls : importClasses) {
            if (cls.endsWith(name)) {
                file = manager.getClassEntry(cls);
                if (file != null) {
                    break;
                }
            }
        }
        return file;
    }

    /** Look for class file using imported packages. */
    private ClassFile findFromImport(JarManager manager, String name) {
        ClassFile file = null;
        for (String packageName : importPackages) {
            String cls = name.startsWith(".") ? (packageName + name)
                    : (packageName + "." + name);
            file = manager.getClassEntry(cls);
            if (file != null) {
                break;
            }
        }
        return file;
    }
}