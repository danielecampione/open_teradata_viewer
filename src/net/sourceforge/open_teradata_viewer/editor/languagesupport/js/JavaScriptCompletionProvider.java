/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AbstractCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.LanguageAwareCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ShorthandCompletionCache;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;

import org.mozilla.javascript.ast.AstRoot;

/**
 * Completion provider for JavaScript.
 *
 * @author D. Campione
 *
 */
public class JavaScriptCompletionProvider extends
        LanguageAwareCompletionProvider {

    /** The AST for the JS. */
    private AstRoot astRoot;

    /** The provider used for source code, kept here since it's used so much. */
    private SourceCompletionProvider sourceProvider;

    private JavaScriptLanguageSupport languageSupport;

    public JavaScriptCompletionProvider(JarManager jarManager,
            JavaScriptLanguageSupport languageSupport) {
        this(new SourceCompletionProvider(languageSupport.isXmlAvailable()),
                jarManager, languageSupport);
    }

    public JavaScriptCompletionProvider(SourceCompletionProvider provider,
            JarManager jarManager, JavaScriptLanguageSupport ls) {
        super(provider);
        this.sourceProvider = (SourceCompletionProvider) getDefaultCompletionProvider();
        this.sourceProvider.setJarManager(jarManager);
        this.languageSupport = ls;

        setShorthandCompletionCache(new JavaScriptShorthandCompletionCache(
                sourceProvider, new DefaultCompletionProvider(),
                ls.isXmlAvailable()));
        sourceProvider.setParent(this);

        setDocCommentCompletionProvider(new JsDocCompletionProvider());
    }

    /**
     * Returns the AST for the JavaScript in the editor.
     *
     * @return The AST.
     */
    public synchronized AstRoot getASTRoot() {
        return astRoot;
    }

    public JarManager getJarManager() {
        return ((SourceCompletionProvider) getDefaultCompletionProvider())
                .getJarManager();
    }

    public JavaScriptLanguageSupport getLanguageSupport() {
        return languageSupport;
    }

    public SourceCompletionProvider getProvider() {
        return sourceProvider;
    }

    /** Set short hand completion cache. */
    public void setShorthandCompletionCache(
            ShorthandCompletionCache shorthandCache) {
        sourceProvider.setShorthandCache(shorthandCache);
        // Reset comment completions too
        setCommentCompletions(shorthandCache);
    }

    /** Load the comment completions from the short hand cache. */
    private void setCommentCompletions(ShorthandCompletionCache shorthandCache) {
        AbstractCompletionProvider provider = shorthandCache
                .getCommentProvider();
        if (provider != null) {
            for (ICompletion c : shorthandCache.getCommentCompletions()) {
                provider.addCompletion(c);
            }
            setCommentCompletionProvider(provider);
        }
    }

    /**
     * Sets the AST for the JavaScript in this editor.
     *
     * @param root The AST.
     */
    public synchronized void setASTRoot(AstRoot root) {
        this.astRoot = root;
    }

    protected synchronized void reparseDocument(int offset) {
        sourceProvider.parseDocument(offset);
    }
}