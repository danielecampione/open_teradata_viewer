/*
 * Open Teradata Viewer ( editor language support html )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractMarkupLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for HTML. This currently provides the following:
 * 
 * <ul>
 *    <li>Code completion for HTML5 tags and attributes.</li>
 *    <li>Automatic creation of closing tags for non-self-closing tags.</li>
 * </ul>
 *
 * @author D. Campione
 * 
 */
public class HtmlLanguageSupport extends AbstractMarkupLanguageSupport {

    /** The completion provider. This is shared amongst all HTML text areas. */
    private HtmlCompletionProvider provider;

    /** A cached set of tags that require closing tags. */
    private static Set<String> tagsToClose = new HashSet<String>();

    /** Ctor. */
    public HtmlLanguageSupport() {
        setAutoActivationEnabled(true);
        setParameterAssistanceEnabled(false);
        setShowDescWindow(true);
    }

    /** {@inheritDoc} */
    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new HtmlCellRenderer();
    }

    private HtmlCompletionProvider getProvider() {
        if (provider == null) {
            provider = new HtmlCompletionProvider();
        }
        return provider;
    }

    /**
     * Hack to share this with others, such as PHP and JSP supports.
     * Note that we should be passing doctype information here.
     * 
     * @return The set of tags to close.
     */
    public static Set<String> getTagsToClose() {
        return tagsToClose;
    }

    /**
     * Returns a set of tags that require a closing tag, based on a resource in
     * this class's package.
     *
     * @param res The resource.
     * @return The set of tags that require closing.
     */
    private static final Set<String> getTagsToClose(String res) {
        Set<String> tags = new HashSet<String>();
        InputStream in = HtmlLanguageSupport.class.getResourceAsStream(res);
        if (in != null) { // Never happens
            String line = null;
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                while ((line = r.readLine()) != null) {
                    if (line.length() > 0 && line.charAt(0) != '#') {
                        tags.add(line.trim());
                    }
                }
                r.close();
            } catch (IOException ioe) { // Never happens
                ExceptionDialog.hideException(ioe);
            }
        }
        return tags;
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

    static {
        tagsToClose = getTagsToClose("html5_close_tags.txt");
    }
}