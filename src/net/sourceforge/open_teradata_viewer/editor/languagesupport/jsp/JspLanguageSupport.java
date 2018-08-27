/*
 * Open Teradata Viewer ( editor language support jsp )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.jsp;

import java.util.HashSet;
import java.util.Set;

import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractMarkupLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for JSP. Features currently include:
 *
 * <ul>
 *    <li>Code completion for HTML5 tags and attributes.</li>
 *    <li>Code completion for JSTL.</li>
 *    <li>Automatic creation of closing tags for non-self-closing tags.</li>
 * </ul>
 * 
 * @author D. Campione
 * 
 */
public class JspLanguageSupport extends AbstractMarkupLanguageSupport {

    /** The completion provider. This is shared amongst all JSP text areas. */
    private JspCompletionProvider provider;

    /** A cached set of tags that require closing tags. */
    private static Set<String> tagsToClose = new HashSet<String>();

    /** Ctor. */
    public JspLanguageSupport() {
        setAutoActivationEnabled(true);
        setParameterAssistanceEnabled(false);
        setShowDescWindow(true);
        tagsToClose = HtmlLanguageSupport.getTagsToClose();
    }

    /** {@inheritDoc} */
    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new HtmlCellRenderer();
    }

    private JspCompletionProvider getProvider() {
        if (provider == null) {
            provider = new JspCompletionProvider();
        }
        return provider;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        HtmlCompletionProvider provider = getProvider();
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