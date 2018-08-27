/*
 * Open Teradata Viewer ( editor language support php )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.php;

import java.util.HashSet;
import java.util.Set;

import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractMarkupLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for PHP. Features currently include:
 *
 * <ul>
 *    <li>Code completion for PHP functions.</li>
 *    <li>Code completion for HTML5 tags and attributes.</li>
 *    <li>Automatic creation of closing tags for non-self-closing tags.</li>
 * </ul>
 * 
 * @author D. Campione
 * 
 */
public class PhpLanguageSupport extends AbstractMarkupLanguageSupport {

    /** The completion provider. This is shared amongst all PHP text areas. */
    private PhpCompletionProvider provider;

    /** A cached set of tags that require closing tags. */
    private static Set<String> tagsToClose = new HashSet<String>();

    /** Ctor. */
    public PhpLanguageSupport() {
        setAutoActivationEnabled(true);
        setParameterAssistanceEnabled(true);
        setShowDescWindow(true);
        tagsToClose = HtmlLanguageSupport.getTagsToClose();
    }

    /** {@inheritDoc} */
    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new HtmlCellRenderer();
    }

    /**
     * Lazily creates the shared completion provider instance for PHP.
     *
     * @return The completion provider.
     */
    private PhpCompletionProvider getProvider() {
        if (provider == null) {
            provider = new PhpCompletionProvider();
        }
        return provider;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        PhpCompletionProvider provider = getProvider();
        AutoCompletion ac = createAutoCompletion(provider);
        ac.install(textArea);
        installImpl(textArea, ac);
        installKeyboardShortcuts(textArea);

        textArea.setToolTipSupplier(null);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean shouldAutoCloseTag(String tag) {
        return tagsToClose.contains(tag.toLowerCase());
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall(SyntaxTextArea textArea) {
        uninstallImpl(textArea);
        uninstallKeyboardShortcuts(textArea);
    }
}