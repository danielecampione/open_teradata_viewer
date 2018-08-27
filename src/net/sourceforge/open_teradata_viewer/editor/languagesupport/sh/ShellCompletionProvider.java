/*
 * Open Teradata Viewer ( editor language support sh )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.sh;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.c.CCompletionProvider;

/**
 * A completion provider for Unix shell scripts.
 *
 * @author D. Campione
 * 
 */
public class ShellCompletionProvider extends CCompletionProvider {

    /** Whether local man pages should be used for function descriptions. */
    private static boolean useLocalManPages;

    /** Ctor. */
    public ShellCompletionProvider() {
    }

    /** {@inheritDoc} */
    @Override
    protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {
    }

    /** {@inheritDoc} */
    @Override
    protected ICompletionProvider createStringCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        return cp;
    }

    /** {@inheritDoc} */
    @Override
    public char getParameterListEnd() {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public char getParameterListStart() {
        return 0;
    }

    /**
     * Returns whether the local system's man pages should be used for
     * descriptions of functions. If this returns <tt>false</tt>, or man cannot
     * be found (e.g. if this is Windows), a shorter description will be used
     * instead.
     *
     * @return Whether to use the local man pages in function descriptions.
     * @see #setUseLocalManPages(boolean)
     */
    public static boolean getUseLocalManPages() {
        return useLocalManPages;
    }

    /** {@inheritDoc} */
    @Override
    protected String getXmlResource() {
        return "res/sh.xml";
    }

    /**
     * Sets whether the local system's man pages should be used for descriptions
     * of functions. If this is set to <tt>false</tt>, or man cannot be found
     * (e.g. if this is Windows), a shorter description will be used instead.
     *
     * @param use Whether to use the local man pages in function descriptions.
     * @see #getUseLocalManPages()
     */
    public static void setUseLocalManPages(boolean use) {
        useLocalManPages = use;
    }
}