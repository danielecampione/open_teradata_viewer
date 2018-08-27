/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.TemplateCompletion;

/**
 * Completion provider for JSDoc.
 *
 * @author D. Campione
 * 
 */
class JsDocCompletionProvider extends DefaultCompletionProvider {

    public JsDocCompletionProvider() {

        // Simple tags
        String[] simpleTags = { "abstract", "access", "alias", "augments",
                "author", "borrows", "callback", "classdesc", "constant",
                "constructor", "constructs", "copyright", "default",
                "deprecated", "desc", "enum", "event", "example", "exports",
                "external", "file", "fires", "global", "ignore", "inner",
                "instance", "kind", "lends", "license", "member", "memberof",
                "method", "mixes", "mixin", "module", "name", "namespace",
                "private", "property", "protected", "public", "readonly",
                "requires", "see", "since", "static", "summary", "this",
                "throws", "todo", "type", "typedef", "variation", "version", };
        for (int i = 0; i < simpleTags.length; i++) {
            addCompletion(new JsDocCompletion(this, "@" + simpleTags[i]));
        }

        // Parameterized (simple) tags
        addCompletion(new JsDocParameterizedCompletion(this, "@param",
                "@param {type} varName", "@param {${}} ${varName} ${cursor}"));
        addCompletion(new JsDocParameterizedCompletion(this, "@return",
                "@return {type} description", "@return {${type}} ${}"));
        addCompletion(new JsDocParameterizedCompletion(this, "@returns",
                "@returns {type} description", "@returns {${type}} ${}"));

        // Inline tags
        addCompletion(new JsDocParameterizedCompletion(this, "{@link}",
                "{@link}", "{@link ${}}${cursor}"));
        addCompletion(new JsDocParameterizedCompletion(this, "{@linkplain}",
                "{@linkplain}", "{@linkplain ${}}${cursor}"));
        addCompletion(new JsDocParameterizedCompletion(this, "{@linkcode}",
                "{@linkcode}", "{@linkcode ${}}${cursor}"));
        addCompletion(new JsDocParameterizedCompletion(this, "{@tutorial}",
                "{@tutorial}", "{@tutorial ${tutorialID}}${cursor}"));

        // Other common stuff
        addCompletion(new JsDocCompletion(this, "null", "<code>null</code>",
                "&lt;code>null&lt;/code>", IconFactory.TEMPLATE_ICON));
        addCompletion(new JsDocCompletion(this, "true", "<code>true</code>",
                "&lt;code>true&lt;/code>", IconFactory.TEMPLATE_ICON));
        addCompletion(new JsDocCompletion(this, "false", "<code>false</code>",
                "&lt;code>false&lt;/code>", IconFactory.TEMPLATE_ICON));

        setAutoActivationRules(false, "{@");
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isValidChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '@'
                || ch == '{' || ch == '}';
    }

    /**
     * A Javadoc completion.
     * 
     * @author D. Campione
     * 
     */
    private static class JsDocCompletion extends BasicCompletion {

        private String inputText;
        private String icon;

        public JsDocCompletion(ICompletionProvider provider,
                String replacementText) {
            super(provider, replacementText);
            this.inputText = replacementText;
            this.icon = IconFactory.JSDOC_ITEM_ICON;
        }

        public JsDocCompletion(ICompletionProvider provider, String inputText,
                String replacementText, String shortDesc, String icon) {
            super(provider, replacementText, shortDesc, shortDesc);
            this.inputText = inputText;
            this.icon = icon;
        }

        @Override
        public Icon getIcon() {
            return IconFactory.getIcon(icon);
        }

        @Override
        public String getInputText() {
            return inputText;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private static class JsDocParameterizedCompletion extends
            TemplateCompletion {

        private String icon;

        public JsDocParameterizedCompletion(ICompletionProvider provider,
                String inputText, String definitionString, String template) {
            this(provider, inputText, definitionString, template,
                    IconFactory.JSDOC_ITEM_ICON);
        }

        public JsDocParameterizedCompletion(ICompletionProvider provider,
                String inputText, String definitionString, String template,
                String icon) {
            super(provider, inputText, definitionString, template);
            setIcon(icon);
        }

        @Override
        public Icon getIcon() {
            return IconFactory.getIcon(icon);
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}