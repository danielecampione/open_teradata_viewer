/*
 * Open Teradata Viewer ( editor language support js ast type )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type;

import java.util.List;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations.JavaScriptObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.client.ClientBrowserAdditions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.client.DOMAddtions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.client.HTMLDOMAdditions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.e4x.ECMAvE4xAdditions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.v3.TypeDeclarationsECMAv3;

/**
 * TypeDeclarationFactory contains cache of TypeDeclarations for to make the
 * lookup of JavaScript types as efficient as possible.
 * 
 * @author D. Campione
 * 
 */
public class TypeDeclarationFactory {

    private TypeDeclarations ecma;

    public TypeDeclarationFactory() {
        setTypeDeclarationVersion(null, false, false);
    }

    public List<String> setTypeDeclarationVersion(String ecmaVersion,
            boolean xmlSupported, boolean client) {
        try {
            ecmaVersion = ecmaVersion == null ? getDefaultECMAVersion()
                    : ecmaVersion;
            // Try to instantiate classes
            Class<?> ecmaClass = TypeDeclarationFactory.class.getClassLoader()
                    .loadClass(ecmaVersion);
            ecma = (TypeDeclarations) ecmaClass.newInstance();
        } catch (Exception e) {
            // Ignore this
            ExceptionDialog.ignoreException(e);
            ecma = new TypeDeclarationsECMAv3();
        }

        if (xmlSupported) { // Add E4X API
            new ECMAvE4xAdditions().addAdditionalTypes(ecma);
        }

        if (client) {
            // For client we are going to add DOM, HTML DOM and Browser
            // attributes/methods
            new ClientBrowserAdditions().addAdditionalTypes(ecma);
            new DOMAddtions().addAdditionalTypes(ecma);
            new HTMLDOMAdditions().addAdditionalTypes(ecma);
        }

        return ecma.getAllClasses();
    }

    /** @return Default base ECMA implementation. */
    protected String getDefaultECMAVersion() {
        return TypeDeclarationsECMAv3.class.getName();
    }

    public List<TypeDeclaration> getAllJavaScriptTypes() {
        return ecma.getAllJavaScriptTypeDeclarations();
    }

    /**
     * Removes declaration type from type cache.
     * 
     * @param name Name of type declaration.
     */
    public void removeType(String name) {
        ecma.removeType(name);
    }

    /** Returns whether the qualified name is a built in JavaScript type. */
    public boolean isJavaScriptType(TypeDeclaration td) {
        return ecma.isJavaScriptType(td);
    }

    /**
     * @return Lookup type declaration from name. If the
     *         <code>TypeDeclaration</code> cannot be found, then lookup using
     *         reserve lookup.
     */
    public TypeDeclaration getTypeDeclaration(String name) {
        return ecma.getTypeDeclaration(name);
    }

    /**
     * @param name Name of TypeDeclaration to lookup.
     * @return Lookup <code>TypeDeclaration</code> and return the JavaScript
     *         name.
     */
    private String getJSTypeDeclarationAsString(String name) {
        TypeDeclaration dec = getTypeDeclaration(name);
        return dec != null ? dec.getJSName() : null;
    }

    /**
     * The API may have it's own types, so these need converting back to
     * JavaScript types, e.g JSString == String, JSNumber == Number.
     */
    public String convertJavaScriptType(String lookupName, boolean qualified) {
        if (lookupName != null) {
            if (TypeDeclarations.NULL_TYPE.equals(lookupName)) { // void has no type
                return null;
            }

            // Remove param descriptor type from type e.g java.util.Iterator<Object> -> java.util.Iterator
            // as JavaScript does not support this
            if (lookupName.indexOf('<') > -1) {
                lookupName = lookupName.substring(0, lookupName.indexOf('<'));
            }

            String lookup = !qualified ? getJSTypeDeclarationAsString(lookupName)
                    : lookupName;

            lookupName = lookup != null ? lookup : lookupName;
            if (!qualified) {
                if (lookupName != null && lookupName.indexOf(".") > -1) {
                    return lookupName.substring(
                            lookupName.lastIndexOf(".") + 1,
                            lookupName.length());
                }
            }
        }
        return lookupName;
    }

    /** @return Default type declaration - ANY */
    public TypeDeclaration getDefaultTypeDeclaration() {
        return getTypeDeclaration(TypeDeclarations.ANY);
    }

    public void addType(String name, TypeDeclaration dec) {
        ecma.addTypeDeclaration(name, dec);
    }

    public String getClassName(String lookup) throws RuntimeException {
        TypeDeclaration td = getTypeDeclaration(lookup);
        if (td != null) {
            return td.getQualifiedName();
        }
        // Else
        throw new RuntimeException("Error finding TypeDeclaration for: "
                + lookup);
    }

    /** @return A list of ECMA JavaScriptObjects. */
    public Set<JavaScriptObject> getECMAScriptObjects() {
        return ecma.getJavaScriptObjects();
    }

    /**
     * Answers the question whether an object can be instantiated (i.e has a
     * constructor).
     *  
     * @param name Name of class to test.
     */
    public boolean canJavaScriptBeInstantiated(String name) {
        return ecma.canECMAObjectBeInstantiated(name);
    }
}