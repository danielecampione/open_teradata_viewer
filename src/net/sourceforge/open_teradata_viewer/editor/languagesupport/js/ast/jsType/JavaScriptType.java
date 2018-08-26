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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.IJSCompletion;

/**
 * Cached Type Tree Node with pointer to a list of super classes to make it easy
 * to walk through Completion hierarchy. Contains a HashMap of lookup keys to
 * <code>IJSCompletion</code>
 *
 * @author D. Campione
 *
 */
public class JavaScriptType {

    // Base type
    protected TypeDeclaration type;
    // Completions for base type String -> IJSCompletion
    protected HashMap<String, IJSCompletion> methodFieldCompletions;
    // Constructor completions
    protected HashMap<String, IJSCompletion> constructors;
    // Class type
    protected IJSCompletion classType;

    // Extended cached types
    private ArrayList<JavaScriptType> extended;

    public JavaScriptType(TypeDeclaration type) {
        this.type = type;
        methodFieldCompletions = new HashMap<String, IJSCompletion>();
        constructors = new HashMap<String, IJSCompletion>();
        extended = new ArrayList<JavaScriptType>();
    }

    /**
     * Add method or field completion to CachedType.
     *
     * @see IJSCompletion
     */
    public void addCompletion(IJSCompletion completion) {
        methodFieldCompletions.put(completion.getLookupName(), completion);
    }

    public IJSCompletion removeCompletion(String completionLookup,
            SourceCompletionProvider provider) {
        IJSCompletion completion = getCompletion(completionLookup, provider);
        if (completion != null) {
            removeCompletion(this, completion);
        }
        return completion;
    }

    /**
     * Recursively walk through completions for this and extended classes to
     * remove completion for this lookup name.
     */
    private void removeCompletion(JavaScriptType type, IJSCompletion completion) {
        if (type.methodFieldCompletions.containsKey(completion.getLookupName())) {
            type.methodFieldCompletions.remove(completion.getLookupName());
        }
        // Get extended classes and recursively remove method from them
        for (JavaScriptType extendedType : type.extended) {
            removeCompletion(extendedType, completion);
        }
    }

    /** Adds a constructor completion to CachedType object type. */
    public void addConstructor(IJSCompletion completion) {
        constructors.put(completion.getLookupName(), completion);
    }

    public void removeConstructor(IJSCompletion completion) {
        constructors.remove(completion.getLookupName());
    }

    /**
     * Set the class type completion, e.g String, Number.
     *
     * @param classType Completion to format the class.
     */
    public void setClassTypeCompletion(IJSCompletion classType) {
        this.classType = classType;
    }

    /** @return The class type completion for the JavaScript type. */
    public IJSCompletion getClassTypeCompletion() {
        return classType;
    }

    /**
     * @return IJSCompletion using lookup name.
     * @see IJSCompletion
     */
    public IJSCompletion getCompletion(String completionLookup,
            SourceCompletionProvider provider) {
        return getCompletion(this, completionLookup, provider);
    }

    /**
     * @return IJSCompletion using lookup name.
     * @see IJSCompletion
     */
    protected IJSCompletion _getCompletion(String completionLookup,
            SourceCompletionProvider provider) {
        return methodFieldCompletions.get(completionLookup);
    }

    /**
     * @return Completion searches this typeCompletions first and if not found,
     *         then tries the extended type. Recursive routine to drill down for
     *         IJSCompletion.
     * @see IJSCompletion
     */
    private static IJSCompletion getCompletion(JavaScriptType cachedType,
            String completionLookup, SourceCompletionProvider provider) {
        IJSCompletion completion = cachedType._getCompletion(completionLookup,
                provider);
        if (completion == null) {
            // Try the extended types
            for (Iterator<JavaScriptType> i = cachedType.getExtendedClasses()
                    .iterator(); i.hasNext();) {
                completion = getCompletion(i.next(), completionLookup, provider);
                if (completion != null) {
                    break;
                }
            }
        }
        return completion;
    }

    /**
     * @return Map of completions String -> IJSCompletion.
     * @see IJSCompletion
     */
    public HashMap<String, IJSCompletion> getMethodFieldCompletions() {
        return methodFieldCompletions;
    }

    public HashMap<String, IJSCompletion> getConstructorCompletions() {
        return constructors;
    }

    /**
     * @return Get type declaration for CachedType.
     * @see TypeDeclaration
     */
    public TypeDeclaration getType() {
        return type;
    }

    /**
     * Add Cached Type extension.
     *
     * @see JavaScriptType
     */
    public void addExtension(JavaScriptType type) {
        extended.add(type);
    }

    /** @return List of CachedType extended classes. */
    public List<JavaScriptType> getExtendedClasses() {
        return extended;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof JavaScriptType) {
            JavaScriptType ct = (JavaScriptType) o;
            return ct.getType().equals(getType());
        }

        if (o instanceof TypeDeclaration) {
            TypeDeclaration dec = (TypeDeclaration) o;
            return dec.equals(getType());
        }

        return false;
    }

    /**
     * Overridden since {@link #equals(Object)} is overridden.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return getType().hashCode();
    }
}