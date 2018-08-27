/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AbstractCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.LanguageAwareCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ShorthandCompletionCache;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.LibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;

/**
 * Completion provider for the Java programming language.
 *
 * @author D. Campione
 * 
 */
public class JavaCompletionProvider extends LanguageAwareCompletionProvider {

    /** The provider used for source code, kept here since it's used so much. */
    private SourceCompletionProvider sourceProvider;

    private CompilationUnit cu;

    /** Ctor. */
    public JavaCompletionProvider() {
        this(null);
    }

    /**
     * Ctor.
     *
     * @param jarManager The jar manager to use when looking up completion
     *        choices. This can be passed in to share a single jar manager
     *        across multiple <tt>SyntaxTextArea</tt>s. This may also be
     *        <code>null</code>, in which case this completion provider will
     *        have a unique <tt>JarManager</tt>.
     */
    public JavaCompletionProvider(JarManager jarManager) {
        super(new SourceCompletionProvider(jarManager));
        this.sourceProvider = (SourceCompletionProvider) getDefaultCompletionProvider();
        sourceProvider.setJavaProvider(this);
        setShorthandCompletionCache(new JavaShorthandCompletionCache(
                sourceProvider, new DefaultCompletionProvider()));
        setDocCommentCompletionProvider(new DocCommentCompletionProvider());
    }

    /**
     * Adds a jar to the "build path".
     *
     * @param info The jar to add. If this is <code>null</code>, then the
     *        current JVM's main JRE jar (rt.jar, or classes.jar on OS X) will
     *        be added. If this jar has already been added, adding it again will
     *        do nothing (except possibly update its attached source location).
     * @throws IOException If an IO error occurs.
     * @see #removeJar(File)
     * @see #getJars()
     */
    public void addJar(LibraryInfo info) throws IOException {
        sourceProvider.addJar(info);
    }

    /**
     * Removes all jars from the "build path".
     *
     * @see #removeJar(File)
     * @see #addJar(LibraryInfo)
     * @see #getJars()
     */
    public void clearJars() {
        sourceProvider.clearJars();
    }

    /**
     * Defers to the source-analyzing completion provider.
     *
     * @return The already entered text.
     */
    @Override
    public String getAlreadyEnteredText(JTextComponent comp) {
        return sourceProvider.getAlreadyEnteredText(comp);
    }

    public synchronized CompilationUnit getCompilationUnit() {
        return cu;
    }

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> getCompletionsAt(JTextComponent tc, Point p) {
        return sourceProvider.getCompletionsAt(tc, p);
    }

    /**
     * Returns the jars on the "build path".
     *
     * @return A list of {@link LibraryInfo}s. Modifying a
     *         <code>LibraryInfo</code> in this list will have no effect on this
     *         completion provider; in order to do that, you must re-add the jar
     *         via {@link #addJar(LibraryInfo)}. If there are no jars on the
     *         "build path", this will be an empty list.
     * @see #addJar(LibraryInfo)
     */
    public List<LibraryInfo> getJars() {
        return sourceProvider.getJars();
    }

    /** {@inheritDoc} */
    @Override
    public List<IParameterizedCompletion> getParameterizedCompletions(
            JTextComponent tc) {
        return null;
    }

    /**
     * Removes a jar from the "build path".
     *
     * @param jar The jar to remove.
     * @return Whether the jar was removed. This will be <code>false</code> if
     *         the jar was not on the build path.
     * @see #addJar(LibraryInfo)
     */
    public boolean removeJar(File jar) {
        return sourceProvider.removeJar(jar);
    }

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

    public synchronized void setCompilationUnit(CompilationUnit cu) {
        this.cu = cu;
    }

    /** Set short hand completion cache (template and comment completions). */
    public void setShorthandCompletionCache(ShorthandCompletionCache cache) {
        sourceProvider.setShorthandCache(cache);
        // Reset comment completions too
        setCommentCompletions(cache);
    }
}