/*
 * Open Teradata Viewer ( editor language support js engine )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.engine;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JSR223JavaScriptTypesFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptTypesFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser.JavaScriptAstParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser.JavaScriptParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver.JSR223JavaScriptCompletionResolver;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver.JavaScriptResolver;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSR223JavaScriptEngine extends JavaScriptEngine {

    public static final String JSR223_ENGINE = "JSR223";

    @Override
    public JavaScriptResolver getJavaScriptResolver(
            SourceCompletionProvider provider) {
        return new JSR223JavaScriptCompletionResolver(provider);
    }

    @Override
    public JavaScriptTypesFactory getJavaScriptTypesFactory(
            SourceCompletionProvider provider) {
        if (jsFactory == null) {
            jsFactory = new JSR223JavaScriptTypesFactory(
                    provider.getTypesFactory());
        }
        return jsFactory;
    }

    @Override
    public JavaScriptParser getParser(SourceCompletionProvider provider,
            int dot, TypeDeclarationOptions options) {
        return new JavaScriptAstParser(provider, dot, options);
    }
}