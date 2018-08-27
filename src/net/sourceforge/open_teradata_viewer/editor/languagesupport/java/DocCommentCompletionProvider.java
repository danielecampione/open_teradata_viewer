/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Graphics;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * Completion provider for documentation comments.
 *
 * @author D. Campione
 * 
 */
class DocCommentCompletionProvider extends DefaultCompletionProvider {

    public DocCommentCompletionProvider() {

        // Block tags
        addCompletion(new JavadocCompletion(this, "@author"));
        addCompletion(new JavadocCompletion(this, "@deprecated"));
        addCompletion(new JavadocCompletion(this, "@exception"));
        addCompletion(new JavadocCompletion(this, "@param"));
        addCompletion(new JavadocCompletion(this, "@return"));
        addCompletion(new JavadocCompletion(this, "@see"));
        addCompletion(new JavadocCompletion(this, "@serial"));
        addCompletion(new JavadocCompletion(this, "@serialData"));
        addCompletion(new JavadocCompletion(this, "@serialField"));
        addCompletion(new JavadocCompletion(this, "@since"));
        addCompletion(new JavadocCompletion(this, "@throws"));
        addCompletion(new JavadocCompletion(this, "@version"));

        // Proposed block tags
        addCompletion(new JavadocCompletion(this, "@category"));
        addCompletion(new JavadocCompletion(this, "@example"));
        addCompletion(new JavadocCompletion(this, "@tutorial"));
        addCompletion(new JavadocCompletion(this, "@index"));
        addCompletion(new JavadocCompletion(this, "@exclude"));
        addCompletion(new JavadocCompletion(this, "@todo"));
        addCompletion(new JavadocCompletion(this, "@internal"));
        addCompletion(new JavadocCompletion(this, "@obsolete"));
        addCompletion(new JavadocCompletion(this, "@threadsafety"));

        // Inline tags
        addCompletion(new JavadocTemplateCompletion(this, "{@code}", "{@code}",
                "{@code ${}}${cursor}"));
        addCompletion(new JavadocTemplateCompletion(this, "{@docRoot}",
                "{@docRoot}", "{@docRoot ${}}${cursor}"));
        addCompletion(new JavadocTemplateCompletion(this, "{@inheritDoc}",
                "{@inheritDoc}", "{@inheritDoc ${}}${cursor}"));
        addCompletion(new JavadocTemplateCompletion(this, "{@link}", "{@link}",
                "{@link ${}}${cursor}"));
        addCompletion(new JavadocTemplateCompletion(this, "{@linkplain}",
                "{@linkplain}", "{@linkplain ${}}${cursor}"));
        addCompletion(new JavadocTemplateCompletion(this, "{@literal}",
                "{@literal}", "{@literal ${}}${cursor}"));
        addCompletion(new JavadocTemplateCompletion(this, "{@value}",
                "{@value}", "{@value ${}}${cursor}"));

        // Other common stuff
        addCompletion(new JavaShorthandCompletion(this, "null",
                "<code>null</code>", "<code>null</code>"));
        addCompletion(new JavaShorthandCompletion(this, "true",
                "<code>true</code>", "<code>true</code>"));
        addCompletion(new JavaShorthandCompletion(this, "false",
                "<code>false</code>", "<code>false</code>"));

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
    private static class JavadocCompletion extends BasicCompletion implements
            IJavaSourceCompletion {
        public JavadocCompletion(ICompletionProvider provider,
                String replacementText) {
            super(provider, replacementText);
        }

        @Override
        public Icon getIcon() {
            return IconFactory.get().getIcon(IconFactory.JAVADOC_ITEM_ICON);
        }

        @Override
        public void rendererText(Graphics g, int x, int y, boolean selected) {
            g.drawString(getReplacementText(), x, y);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private static class JavadocTemplateCompletion extends
            JavaTemplateCompletion {

        public JavadocTemplateCompletion(ICompletionProvider provider,
                String inputText, String definitionString, String template) {
            super(provider, inputText, definitionString, template);
            setIcon(IconFactory.JAVADOC_ITEM_ICON);
        }

    }
}