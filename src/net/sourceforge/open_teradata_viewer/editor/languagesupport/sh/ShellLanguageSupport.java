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

import java.io.File;

import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.CompletionCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for Unix shell scripts.
 *
 * @author D. Campione
 * 
 */
public class ShellLanguageSupport extends AbstractLanguageSupport {

    /** The completion provider. This is shared amongst all sh text areas. */
    private ShellCompletionProvider provider;

    /** Whether local man pages should be used. */
    private boolean useLocalManPages;

    /** Ctor. */
    public ShellLanguageSupport() {
        setParameterAssistanceEnabled(false);
        setShowDescWindow(true);
        useLocalManPages = File.separatorChar == '/';
    }

    /** {@inheritDoc} */
    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new CompletionCellRenderer();
    }

    /**
     * Lazily creates the shared completion provider instance for sh scripts.
     *
     * @return The completion provider.
     */
    private ShellCompletionProvider getProvider() {
        if (provider == null) {
            provider = new ShellCompletionProvider();
            ShellCompletionProvider.setUseLocalManPages(getUseLocalManPages());
        }
        return provider;
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
    public boolean getUseLocalManPages() {
        return useLocalManPages;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        ShellCompletionProvider provider = getProvider();
        AutoCompletion ac = createAutoCompletion(provider);
        ac.install(textArea);
        installImpl(textArea, ac);

        textArea.setToolTipSupplier(provider);
    }

    /**
     * Sets whether the local system's man pages should be used for descriptions
     * of functions. If this is set to <tt>false</tt>, or man cannot be found
     * (e.g. if this is Windows), a shorter description will be used instead.
     *
     * @param use Whether to use the local man pages in function descriptions.
     * @see #getUseLocalManPages()
     */
    public void setUseLocalManPages(boolean use) {
        if (use != useLocalManPages) {
            useLocalManPages = use;
            if (provider != null) {
                ShellCompletionProvider.setUseLocalManPages(useLocalManPages);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall(SyntaxTextArea textArea) {
        uninstallImpl(textArea);
    }
}