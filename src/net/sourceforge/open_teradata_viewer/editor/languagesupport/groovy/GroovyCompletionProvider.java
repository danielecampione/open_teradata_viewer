/*
 * Open Teradata Viewer ( editor language support groovy )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.groovy;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.LanguageAwareCompletionProvider;

/**
 * A completion provider for Groovy.
 *
 * @author D. Campione
 * 
 */
public class GroovyCompletionProvider extends LanguageAwareCompletionProvider {

    /** Ctor. */
    public GroovyCompletionProvider() {
        setDefaultCompletionProvider(createCodeCompletionProvider());
        setStringCompletionProvider(createStringCompletionProvider());
        setCommentCompletionProvider(createCommentCompletionProvider());
    }

    /**
     * Adds shorthand completions to the code completion provider.
     *
     * @param codeCP The code completion provider.
     */
    protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {
    }

    /**
     * Returns the provider to use when editing code.
     *
     * @return The provider.
     * @see #createCommentCompletionProvider()
     * @see #createStringCompletionProvider()
     * @see #addShorthandCompletions(DefaultCompletionProvider)
     */
    protected ICompletionProvider createCodeCompletionProvider() {
        ICompletionProvider cp = new GroovySourceCompletionProvider();
        return cp;
    }

    /**
     * Returns the provider to use when in a comment.
     *
     * @return The provider.
     * @see #createCodeCompletionProvider()
     * @see #createStringCompletionProvider()
     */
    protected ICompletionProvider createCommentCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        cp.addCompletion(new BasicCompletion(cp, "TODO:", "A to-do reminder"));
        cp.addCompletion(new BasicCompletion(cp, "FIXME:",
                "A bug that needs to be fixed"));
        return cp;
    }

    /**
     * Returns the completion provider to use when the caret is in a string.
     *
     * @return The provider.
     * @see #createCodeCompletionProvider()
     * @see #createCommentCompletionProvider()
     */
    protected ICompletionProvider createStringCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        cp.addCompletion(new BasicCompletion(cp, "%c", "char",
                "Prints a character"));
        cp.addCompletion(new BasicCompletion(cp, "%i", "signed int",
                "Prints a signed integer"));
        cp.addCompletion(new BasicCompletion(cp, "%f", "float",
                "Prints a float"));
        cp.addCompletion(new BasicCompletion(cp, "%s", "string",
                "Prints a string"));
        cp.addCompletion(new BasicCompletion(cp, "%u", "unsigned int",
                "Prints an unsigned integer"));
        cp.addCompletion(new BasicCompletion(cp, "\\n", "Newline",
                "Prints a newline"));
        return cp;
    }
}