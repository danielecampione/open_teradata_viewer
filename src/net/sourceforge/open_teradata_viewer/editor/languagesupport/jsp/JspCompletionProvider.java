/*
 * Open Teradata Viewer ( editor language support jsp )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.jsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion.Parameter;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.MarkupTagCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.AttributeCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlCompletionProvider;

/**
 * Completion provider for JSP.
 *
 * @author D. Campione
 * 
 */
public class JspCompletionProvider extends HtmlCompletionProvider {

    /** Mapping of prefixes to TLD's. */
    private Map<String, TldFile> prefixToTld;

    public JspCompletionProvider() {
        prefixToTld = new HashMap<String, TldFile>();
        setAutoActivationRules(false, "<:");
    }

    /** Overridden to handle JSP tags on top of standard HTML tags. */
    @Override
    protected List<AttributeCompletion> getAttributeCompletionsForTag(
            String tagName) {
        List<AttributeCompletion> list = super
                .getAttributeCompletionsForTag(tagName);

        if (list == null) {
            int colon = tagName.indexOf(':');
            if (colon > -1) {
                String prefix = tagName.substring(0, colon);
                tagName = tagName.substring(colon + 1);

                TldFile tldFile = prefixToTld.get(prefix);
                if (tldFile != null) {
                    List<Parameter> attrs = tldFile
                            .getAttributesForTag(tagName);
                    if (attrs != null && attrs.size() > -1) {
                        list = new ArrayList<AttributeCompletion>();
                        for (Parameter param : attrs) {
                            list.add(new AttributeCompletion(this, param));
                        }
                    }
                }
            }
        }

        return list;
    }

    /**
     * Overridden to include JSP-specific tags in addition to the standard HTML
     * tags.
     *
     * @return The list of tags.
     */
    @Override
    protected List<ICompletion> getTagCompletions() {
        List<ICompletion> completions = new ArrayList<ICompletion>(
                super.getTagCompletions());

        for (Map.Entry<String, TldFile> entry : prefixToTld.entrySet()) {
            String prefix = entry.getKey();
            TldFile tld = entry.getValue();
            for (int j = 0; j < tld.getElementCount(); j++) {
                TldElement elem = tld.getElement(j);
                MarkupTagCompletion mtc = new MarkupTagCompletion(this, prefix
                        + ":" + elem.getName());
                mtc.setDescription(elem.getDescription());
                completions.add(mtc);
            }
        }

        Collections.sort(completions);
        return completions;
    }

    /** Overridden to load <code>jsp:*</code> tags also. */
    @Override
    @SuppressWarnings("unchecked")
    protected void initCompletions() {
        super.initCompletions();

        // Load our JSP completions but remember the basic HTML ones too
        try {
            loadFromXML("res/jsp.xml");
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }

        // The completions array is expected to be sorted alphabetically.
        // We must re-sort since we added to it
        Collections.sort(completions, comparator);
    }

    @Override
    protected boolean isValidChar(char ch) {
        return super.isValidChar(ch) || ch == ':';
    }
}