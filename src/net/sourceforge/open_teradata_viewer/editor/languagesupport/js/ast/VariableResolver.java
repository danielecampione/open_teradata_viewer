/*
 * Open Teradata Viewer ( editor language support js ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast;

import java.util.HashMap;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;

/**
 * Cache Local and System scope variables. Local scope variables are cleared each
 * time the <code>SourceCompletionProvider</code> finishes parsing the script.
 * System scope variables will not be cleared.
 *
 * @author D. Campione
 *
 */
public class VariableResolver {

    // HashMap of local variables mapped Name -> JSVariableDeclaration
    private HashMap<String, JavaScriptVariableDeclaration> localVariables = new HashMap<String, JavaScriptVariableDeclaration>();
    // Pre processing variables - these are set when pre-processing
    private HashMap<String, JavaScriptVariableDeclaration> preProcessedVariables = new HashMap<String, JavaScriptVariableDeclaration>();
    // HashMap of system variables mapped Name -> JSVariableDeclaration.
    // System variables do not get cleared as they are always available to the
    // system
    private HashMap<String, JavaScriptVariableDeclaration> systemVariables = new HashMap<String, JavaScriptVariableDeclaration>();

    private HashMap<String, JavaScriptFunctionDeclaration> localFunctions = new HashMap<String, JavaScriptFunctionDeclaration>();
    private HashMap<String, JavaScriptFunctionDeclaration> preProcessedFunctions = new HashMap<String, JavaScriptFunctionDeclaration>();

    /**
     * Add local scope variable to cache.
     *
     * @param declaration Variable to add.
     */
    public void addLocalVariable(JavaScriptVariableDeclaration declaration) {
        localVariables.put(declaration.getName(), declaration);
    }

    /**
     * Add pre-processing scope variable to cache.
     *
     * @param declaration Variable to add.
     */
    public void addPreProcessingVariable(
            JavaScriptVariableDeclaration declaration) {
        preProcessedVariables.put(declaration.getName(), declaration);
    }

    /**
     * Add system scope variable to cache.
     *
     * @param declaration Variable to add.
     */
    public void addSystemVariable(JavaScriptVariableDeclaration declaration) {
        systemVariables.put(declaration.getName(), declaration);
    }

    /**
     * Remove pre-processing variable from system variable cache.
     *
     * @param name Name of the system variable to remove.
     */
    public void removePreProcessingVariable(String name) {
        preProcessedVariables.remove(name);
    }

    /**
     * Remove system variable from system variable cache.
     *
     * @param name Name of the system variable to remove.
     */
    public void removeSystemVariable(String name) {
        systemVariables.remove(name);
    }

    /**
     * Find JSVariableDeclaration for name against all variable types and check
     * is in scope of caret position.
     *
     * @return JSVariableDeclaration from the name.
     */
    public JavaScriptVariableDeclaration findDeclaration(String name, int dot) {
        JavaScriptVariableDeclaration findDeclaration = findDeclaration(
                localVariables, name, dot);
        // Test whether this was found and then try pre-processing variable
        findDeclaration = findDeclaration == null ? findDeclaration(
                preProcessedVariables, name, dot) : findDeclaration;
        // Last chance.. look in system variables
        return findDeclaration == null ? findDeclaration(systemVariables, name,
                dot) : findDeclaration;
    }

    public JavaScriptVariableDeclaration findDeclaration(String name, int dot,
            boolean local, boolean preProcessed, boolean system) {
        JavaScriptVariableDeclaration findDeclaration = local ? findDeclaration(
                localVariables, name, dot) : null;
        // Test whether this was found and then try pre-processing variable
        findDeclaration = findDeclaration == null ? preProcessed ? findDeclaration(
                preProcessedVariables, name, dot) : null
                : findDeclaration;
        // Last chance.. look in system variables
        return findDeclaration == null ? system ? findDeclaration(
                systemVariables, name, dot) : null : findDeclaration;
    }

    /**
     * Find JSVariableDeclaration within pre-processed and system variable only.
     * Also check is in scope of caret position.
     *
     * @param name Name of variable to resolve.
     * @param dot Position in text document.
     * @return JSVariableDeclaration from the name.
     */
    public JavaScriptVariableDeclaration findNonLocalDeclaration(String name,
            int dot) {
        // Try pre-processing variable
        JavaScriptVariableDeclaration findDeclaration = findDeclaration(
                preProcessedVariables, name, dot);
        // Last chance.. look in system variables
        return findDeclaration == null ? findDeclaration(systemVariables, name,
                dot) : findDeclaration;
    }

    /**
     * Find JSVariableDeclaration and check the scope of the caret position
     *
     * @return JSVariableDeclaration from the name.
     */
    private JavaScriptVariableDeclaration findDeclaration(
            HashMap<String, JavaScriptVariableDeclaration> variables,
            String name, int dot) {
        JavaScriptVariableDeclaration dec = variables.get(name);

        if (dec != null) {
            if (dec.getCodeBlock() == null || dec.getCodeBlock().contains(dot)) {
                int decOffs = dec.getOffset();
                if (dot <= decOffs) {
                    return dec;
                }
            }
        }
        return null;
    }

    /**
     * Find the <code>TypeDeclaration</code> for the variable and check the
     * scope of the caret position.
     *
     * @param name Name of variable.
     * @param dot Position.
     * @return TypeDeclaration from the name.
     */
    public TypeDeclaration getTypeDeclarationForVariable(String name, int dot) {
        JavaScriptVariableDeclaration dec = findDeclaration(name, dot);
        return dec != null ? dec.getTypeDeclaration() : null;
    }

    /** Clear all local scope variables. */
    public void resetLocalVariables() {
        localVariables.clear();
        localFunctions.clear();
    }

    public void resetPreProcessingVariables(boolean clear) {
        if (clear) {
            preProcessedVariables.clear();
            preProcessedFunctions.clear();
        } else {
            for (JavaScriptVariableDeclaration dec : preProcessedVariables
                    .values()) {
                dec.resetVariableToOriginalType();
            }
        }
    }

    public void resetSystemVariables() {
        systemVariables.clear();
    }

    /**
     * Resolve the entered text by chopping up the text and working from left to
     * right, resolving each type in turn.
     *
     * @return TypeDeclaration for variable name.
     */
    public TypeDeclaration resolveType(String varName, int dot) {
        // Just look up variable
        return getTypeDeclarationForVariable(varName, dot);
    }

    public void addLocalFunction(JavaScriptFunctionDeclaration func) {
        localFunctions.put(func.getName(), func);
    }

    public JavaScriptFunctionDeclaration findFunctionDeclaration(String name) {
        JavaScriptFunctionDeclaration dec = localFunctions.get(name);
        if (dec == null) {
            dec = preProcessedFunctions.get(name);
        }
        return dec;
    }

    public JavaScriptFunctionDeclaration findFunctionDeclaration(String name,
            boolean local, boolean preProcessed) {
        JavaScriptFunctionDeclaration dec = local ? (JavaScriptFunctionDeclaration) localFunctions
                .get(name) : null;
        if (dec == null) {
            dec = preProcessed ? (JavaScriptFunctionDeclaration) preProcessedFunctions
                    .get(name) : null;
        }
        return dec;
    }

    public JavaScriptFunctionDeclaration findFunctionDeclarationByFunctionName(
            String name, boolean local, boolean preprocessed) {
        JavaScriptFunctionDeclaration func = local ? findFirstFunction(name,
                localFunctions) : null;
        if (func == null) {
            func = preprocessed ? findFirstFunction(name, preProcessedFunctions)
                    : null;
        }
        return func;
    }

    private JavaScriptFunctionDeclaration findFirstFunction(String name,
            HashMap<String, JavaScriptFunctionDeclaration> functions) {
        for (JavaScriptFunctionDeclaration func : functions.values()) {
            if (name.equals(func.getFunctionName())) {
                return func;
            }
        }
        return null;
    }

    /**
     * Add pre-processing scope function to cache.
     *
     * @param func Variable to add.
     */
    public void addPreProcessingFunction(JavaScriptFunctionDeclaration func) {
        preProcessedFunctions.put(func.getName(), func);
    }
}