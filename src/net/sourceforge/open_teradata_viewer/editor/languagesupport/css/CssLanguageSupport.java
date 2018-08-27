/*
 * Open Teradata Viewer ( editor language support css )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.css;

import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for CSS files.
 *
 * @author D. Campione
 * 
 */
public class CssLanguageSupport extends AbstractLanguageSupport {

    /** The completion provider, shared amongst all text areas editing CSS. */
    private CssCompletionProvider provider;

    /** Ctor. */
    public CssLanguageSupport() {
        setAutoActivationEnabled(true);
        setAutoActivationDelay(500);
        setParameterAssistanceEnabled(true);
    }

    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new CssCellRenderer();
    }

    private CssCompletionProvider getProvider() {
        if (provider == null) {
            provider = new CssCompletionProvider();
        }
        return provider;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        CssCompletionProvider provider = getProvider();
        AutoCompletion ac = createAutoCompletion(provider);
        ac.install(textArea);
        installImpl(textArea, ac);

        textArea.setToolTipSupplier(provider);
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall(SyntaxTextArea textArea) {
        uninstallImpl(textArea);
        textArea.setToolTipSupplier(null);
    }
}