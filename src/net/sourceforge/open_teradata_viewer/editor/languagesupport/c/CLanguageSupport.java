/*
 * Open Teradata Viewer ( editor language support c )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.c;

import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for C.
 *
 * @author D. Campione
 * 
 */
public class CLanguageSupport extends AbstractLanguageSupport {

    /** The completion provider, shared amongst all text areas editing C. */
    private CCompletionProvider provider;

    /** Ctor. */
    public CLanguageSupport() {
        setParameterAssistanceEnabled(true);
        setShowDescWindow(true);
    }

    /** {@inheritDoc} */
    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new CCellRenderer();
    }

    private CCompletionProvider getProvider() {
        if (provider == null) {
            provider = new CCompletionProvider();
        }
        return provider;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        CCompletionProvider provider = getProvider();
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