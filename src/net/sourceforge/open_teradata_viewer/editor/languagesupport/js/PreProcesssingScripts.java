/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;

/**
 * Scripts to be processed before parsing main script text.
 *
 * Useful for includes within JavaScript client.
 *
 * Caches the completions so they do not have to parsed every single time the
 * main script text is parsed.
 *
 * @author D. Campione
 *
 */
public class PreProcesssingScripts {

    private SourceCompletionProvider provider;

    private Set<ICompletion> preProcessingCompletions = new HashSet<ICompletion>();

    public PreProcesssingScripts(SourceCompletionProvider provider) {
        this.provider = provider;
    }

    public void parseScript(String scriptText, TypeDeclarationOptions options) {
        if (scriptText != null && scriptText.length() > 0) {
            CompilerEnvirons env = JavaScriptParser.createCompilerEnvironment(
                    new JavaScriptParser.JSErrorReporter(),
                    provider.getLanguageSupport());
            Parser parser = new Parser(env);
            StringReader r = new StringReader(scriptText);
            try {
                AstRoot root = parser.parse(r, null, 0);
                CodeBlock block = provider.iterateAstRoot(root,
                        preProcessingCompletions, "", Integer.MAX_VALUE,
                        options);
                provider.recursivelyAddLocalVars(preProcessingCompletions,
                        block, 0, null, false, true);
            } catch (IOException io) {
                // Ignore this
                ExceptionDialog.ignoreException(io);
            }
        }
    }

    public void reset() {
        preProcessingCompletions.clear();
        // Remove all preProcessing Variables
        provider.getVariableResolver().resetPreProcessingVariables(true);
    }

    public Set<ICompletion> getCompletions() {
        return preProcessingCompletions;
    }
}