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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;

import org.mozilla.javascript.ast.AstNode;

/**
 * JavaScript Variable Declaration class <code>TypeDeclarations</code>.
 *
 * @author D. Campione
 *
 */
public class JavaScriptVariableDeclaration extends JavaScriptDeclaration {

    protected TypeDeclaration typeDec;
    protected SourceCompletionProvider provider;

    private boolean reassigned;
    private TypeDeclaration originalTypeDec;

    /**
     * @param name Name of the variable.
     * @param offset Position within script.
     * @param provider JavaScript source provider.
     */
    public JavaScriptVariableDeclaration(String name, int offset,
            SourceCompletionProvider provider, CodeBlock block) {
        super(name, offset, block);
        this.provider = provider;
    }

    /**
     * Lookup TypeDeclaration from the Rhino <code>AstNode</code>.
     *
     * @param typeNode Rhino AstNode linked to this variable.
     */
    public void setTypeDeclaration(AstNode typeNode) {
        typeDec = provider.getJavaScriptEngine()
                .getJavaScriptResolver(provider).resolveNode(typeNode);
    }

    /**
     * Set the TypeDeclaration for the AstNode. Stores the original value so it
     * can be reset.
     *
     * @see #resetVariableToOriginalType()
     */
    public void setTypeDeclaration(AstNode typeNode, boolean overrideOriginal) {
        // Check whether the variable has been reassigned already
        if (!reassigned) {
            originalTypeDec = typeDec;
        }

        setTypeDeclaration(typeNode);

        if (overrideOriginal) {
            originalTypeDec = typeDec;
        }
        reassigned = true;
    }

    /** Resets the TypeDeclaration to the original value. */
    public void resetVariableToOriginalType() {
        if (reassigned) {
            reassigned = false;
            typeDec = originalTypeDec;
        }
        originalTypeDec = null;
    }

    /** Set TypeDeclaration. */
    public void setTypeDeclaration(TypeDeclaration typeDec) {
        this.typeDec = typeDec;
    }

    /** @return TypeDeclaration for the variable. */
    public TypeDeclaration getTypeDeclaration() {
        return typeDec;
    }

    /**
     * @return JavaScript name for the type declaration, e.g String, Number,
     *         etc..
     */
    public String getJavaScriptTypeName() {
        TypeDeclaration dec = getTypeDeclaration();
        return dec != null ? dec.getJSName() : provider.getTypesFactory()
                .getDefaultTypeDeclaration().getJSName();
    }
}