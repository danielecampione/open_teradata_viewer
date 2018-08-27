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
 *
 *
 * @author D. Campione
 *
 */
public class JavaScriptFunctionTypeDeclaration extends
        JavaScriptVariableDeclaration {

    private AstNode typeNode;

    public JavaScriptFunctionTypeDeclaration(String name, int offset,
            SourceCompletionProvider provider, CodeBlock block) {
        super(name, offset, provider, block);
    }

    @Override
    public void setTypeDeclaration(AstNode typeNode) {
        this.typeNode = typeNode;
    }

    @Override
    public TypeDeclaration getTypeDeclaration() {
        return provider.getJavaScriptEngine().getJavaScriptResolver(provider)
                .resolveNode(typeNode);
    }
}