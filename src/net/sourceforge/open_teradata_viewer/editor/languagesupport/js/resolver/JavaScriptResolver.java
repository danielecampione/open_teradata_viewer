/*
 * Open Teradata Viewer ( editor language support js resolver )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver;

import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptType;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JSMethodData;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.FunctionCall;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class JavaScriptResolver {

    protected SourceCompletionProvider provider;

    /**
     * Base JavaScriptResolver.
     * 
     * @param provider SourceCompletionProvider.
     */
    public JavaScriptResolver(SourceCompletionProvider provider) {
        this.provider = provider;
    }

    /**
     * Resolve node type to TypeDeclaration. Called instead of
     * #compileText(String text) when document is already parsed.
     * 
     * @param node AstNode to resolve.
     * @return TypeDeclaration for node or null if not found.
     */
    public abstract TypeDeclaration resolveNode(AstNode node);

    /**
     * Resolve node type to TypeDeclaration. Called instead of
     * #compileText(String text) when document is already parsed.
     * 
     * @param node AstNode to resolve.
     * @return TypeDeclaration for node or null if not found.
     */
    public abstract TypeDeclaration resolveParamNode(String text)
            throws IOException;

    /**
     * Compiles Text and resolves the type, e.g
     * "Hello World".length; // Resolve as a Number
     * 
     * @param text To compile and resolve.
     */
    public abstract JavaScriptType compileText(String text) throws IOException;

    /**
     * Resolve node type to TypeDeclaration.
     * 
     * @param node AstNode to resolve.
     * @return TypeDeclaration for node or null if not found.
     */
    protected abstract TypeDeclaration resolveNativeType(AstNode node);

    /**
     * Get lookup string for function completions.
     * 
     * @param method JSMethodData holding method information.
     * @param name Name of method.
     */
    public abstract String getLookupText(JSMethodData method, String name);

    /**
     * Returns same string format as {@link #getLookupText(JSMethodData,
     * String)} but from AstNode Function.
     */
    public abstract String getFunctionNameLookup(FunctionCall call,
            SourceCompletionProvider provider);
}