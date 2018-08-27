/*
 * Open Teradata Viewer ( editor language support groovy )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.groovy;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for Groovy.
 *
 * @author D. Campione
 * 
 */
public class GroovyLanguageSupport extends AbstractLanguageSupport {

    /** The completion provider, shared amongst all text areas. */
    private GroovyCompletionProvider provider;

    /** Ctor. */
    public GroovyLanguageSupport() {
        setParameterAssistanceEnabled(true);
        setShowDescWindow(true);
    }

    private GroovyCompletionProvider getProvider() {
        if (provider == null) {
            provider = new GroovyCompletionProvider();
        }
        return provider;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        GroovyCompletionProvider provider = getProvider();
        AutoCompletion ac = createAutoCompletion(provider);
        ac.install(textArea);
        installImpl(textArea, ac);

        textArea.setToolTipSupplier(provider);
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall(SyntaxTextArea textArea) {
        uninstallImpl(textArea);
    }
}