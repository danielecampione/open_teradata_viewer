/*
 * Open Teradata Viewer ( editor language support c )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.c;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.LanguageAwareCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ShorthandCompletion;

/**
 * A completion provider for the C programming language. It provides code
 * completion support and parameter assistance for the C Standard Library.
 * This information is read from an XML file.
 *
 * @author D. Campione
 * 
 */
public class CCompletionProvider extends LanguageAwareCompletionProvider {

    /** Ctor. */
    public CCompletionProvider() {
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
        codeCP.addCompletion(new ShorthandCompletion(codeCP, "main",
                "int main(int argc, char **argv)"));
    }

    /**
     * Returns the provider to use when editing code.
     *
     * @return The provider.
     * @see #createCommentCompletionProvider()
     * @see #createStringCompletionProvider()
     * @see #loadCodeCompletionsFromXml(DefaultCompletionProvider)
     * @see #addShorthandCompletions(DefaultCompletionProvider)
     */
    protected ICompletionProvider createCodeCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        loadCodeCompletionsFromXml(cp);
        addShorthandCompletions(cp);
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

    /**
     * Returns the name of the XML resource to load (on classpath or a file).
     *
     * @return The resource to load.
     */
    protected String getXmlResource() {
        return "res/c.xml";
    }

    /**
     * Called from {@link #createCodeCompletionProvider()} to actually load the
     * completions from XML. Subclasses that override that method will want to
     * call this one.
     *
     * @param cp The code completion provider.
     */
    protected void loadCodeCompletionsFromXml(DefaultCompletionProvider cp) {
        // First try loading resource (running from executable jar), then try
        // accessing file (debugging in Eclipse)
        ClassLoader cl = getClass().getClassLoader();
        String res = getXmlResource();
        if (res != null) { // Subclasses may specify a null value
            InputStream in = cl.getResourceAsStream(res);
            try {
                if (in != null) {
                    cp.loadFromXML(in);
                    in.close();
                } else {
                    cp.loadFromXML(new File(res));
                }
            } catch (IOException ioe) {
                ExceptionDialog.hideException(ioe);
            }
        }
    }
}