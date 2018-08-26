/*
 * Open Teradata Viewer ( editor language support js ast parser )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser;

import java.util.Set;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;

import org.mozilla.javascript.ast.AstRoot;

/**
 *
 *
 * @author D. Campione
 *
 */
public abstract class JavaScriptParser {

    protected SourceCompletionProvider provider;
    protected int dot;
    protected TypeDeclarationOptions options;

    /** JavaScriptParser constructor. */
    public JavaScriptParser(SourceCompletionProvider provider, int dot,
            TypeDeclarationOptions options) {
        this.provider = provider;
        this.dot = dot;
        this.options = options;
    }

    /**
     * Converts AstRoot to CodeBlock.
     *
     * @param root AstRoot to iterate.
     * @param set Completions set.
     * @param entered Text entered by user.
     * @return CodeBlock tree.
     */
    public abstract CodeBlock convertAstNodeToCodeBlock(AstRoot root,
            Set<ICompletion> set, String entered);

    /**
     * If options are null, then it is assumed that the main editor text is
     * being parsed.
     *
     * @return Whether options is not null and is in pre-processing mode.
     */
    public boolean isPreProcessing() {
        return options != null && options.isPreProcessing();
    }
}