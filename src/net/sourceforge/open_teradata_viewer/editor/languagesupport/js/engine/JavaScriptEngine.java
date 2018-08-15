/*
 * Open Teradata Viewer ( editor language support js engine )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.engine;

import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptTypesFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser.JavaScriptParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclarationFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver.JavaScriptResolver;

/**
 * JavaScript Engine Interface used for resolving Types.
 * 
 * @author D. Campione
 * @see JavaScriptResolver
 * @see JavaScriptTypesFactory
 * @see JavaScriptParser
 * 
 */
public abstract class JavaScriptEngine {

    private TypeDeclarationFactory typesFactory = new TypeDeclarationFactory();

    protected JavaScriptTypesFactory jsFactory;

    public List<String> setTypeDeclarationVersion(String ecmaVersion,
            boolean xmlSupported, boolean client) {
        return typesFactory.setTypeDeclarationVersion(ecmaVersion,
                xmlSupported, client);
    }

    public TypeDeclarationFactory getTypesFactory() {
        return typesFactory;
    }

    /**
     * @param provider SourceCompletionProvider.
     * @return JavaScriptResolver used to resolve JavaScriptType and
     *         TypeDeclaration.
     */
    public abstract JavaScriptResolver getJavaScriptResolver(
            SourceCompletionProvider provider);

    /**
     * @param provider SourceCompletionProvider.
     * @return JavaScriptTypesFactory that holds a cache of JavaScriptType.
     */
    public abstract JavaScriptTypesFactory getJavaScriptTypesFactory(
            SourceCompletionProvider provider);

    /**
     * @param provider SourceCompletionProvider.
     * @param dot Caret position.
     * @param options TypeDeclationsOption to allow configuration options for
     *        processing script before JTextComponent's text within
     *        SourceCompletionProvider.
     * @return JavaScriptParser that converts AstRoot to CodeBlock.
     */
    public abstract JavaScriptParser getParser(
            SourceCompletionProvider provider, int dot,
            TypeDeclarationOptions options);
}