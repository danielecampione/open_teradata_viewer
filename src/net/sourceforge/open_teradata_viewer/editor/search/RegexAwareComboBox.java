/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

import javax.swing.ComboBoxModel;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.IContentAssistable;
import net.sourceforge.open_teradata_viewer.editor.MaxWidthComboBox;
import net.sourceforge.open_teradata_viewer.editor.OTVComboBoxModel;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * A combo box that offers content assistance for regular expressions.
 *
 * @author D. Campione
 * 
 */
public class RegexAwareComboBox extends MaxWidthComboBox implements
        IContentAssistable {

    private static final long serialVersionUID = -2535017274279608060L;

    private boolean enabled;
    private boolean replace;
    private AutoCompletion ac;
    private RegexAwareProvider provider;

    /**
     * Ctor.
     *
     * @param replace Whether this is a "replace" combo box (as opposed to a
     *        "find" combo box). This dictates what auto-complete choices the
     *        user is offered.
     */
    public RegexAwareComboBox(boolean replace) {
        this(new OTVComboBoxModel(), 200, replace);
    }

    /** Ctor. */
    public RegexAwareComboBox(ComboBoxModel model, int maxWidth, boolean replace) {
        super(model, maxWidth);
        setEditable(true);
        this.replace = replace;
    }

    /**
     * Adds the completion choices for regexes in a "find" text field.
     *
     * @param p The completion provider to add to.
     * @see #addReplaceFieldCompletions(RegexAwareProvider)
     */
    private void addFindFieldCompletions(RegexAwareProvider p) {
        // Characters
        p.addCompletion(new RegexCompletion(p, "\\\\", "\\\\",
                "\\\\ - Backslash"));
        p.addCompletion(new RegexCompletion(p, "\\t", "\\t", "\\t - Tab"));
        p.addCompletion(new RegexCompletion(p, "\\n", "\\n", "\\n - Newline"));

        // Character classes
        p.addCompletion(new RegexCompletion(p, "[", "[",
                "[abc] - Any of a, b, or c"));
        p.addCompletion(new RegexCompletion(p, "[^", "[^",
                "[^abc] - Any character except a, b, or c"));

        // Predefined character classes
        p.addCompletion(new RegexCompletion(p, ".", ".", ". - Any character"));
        p.addCompletion(new RegexCompletion(p, "\\d", "\\d", "\\d - A digit"));
        p.addCompletion(new RegexCompletion(p, "\\D", "\\D",
                "\\D - Not a digit"));
        p.addCompletion(new RegexCompletion(p, "\\s", "\\s",
                "\\s - A whitespace"));
        p.addCompletion(new RegexCompletion(p, "\\S", "\\S",
                "\\S - Not a whitespace"));
        p.addCompletion(new RegexCompletion(p, "\\w", "\\w",
                "\\w - An alphanumeric (word character)"));
        p.addCompletion(new RegexCompletion(p, "\\W", "\\W",
                "\\W - Not an alphanumeric"));

        // Boundary matchers
        p.addCompletion(new RegexCompletion(p, "^", "^", "^ - Line Start"));
        p.addCompletion(new RegexCompletion(p, "$", "$", "$ - Line End"));
        p.addCompletion(new RegexCompletion(p, "\\b", "\b",
                "\\b - Word beginning or end"));
        p.addCompletion(new RegexCompletion(p, "\\B", "\\B",
                "\\B - Not a word beginning or end"));

        // Greedy, reluctant and possessive quantifiers
        p.addCompletion(new RegexCompletion(p, "?", "?",
                "X? - Greedy match, 0 or 1 times"));
        p.addCompletion(new RegexCompletion(p, "*", "*",
                "X* - Greedy match, 0 or more times"));
        p.addCompletion(new RegexCompletion(p, "+", "+",
                "X+ - Greedy match, 1 or more times"));
        p.addCompletion(new RegexCompletion(p, "{", "{",
                "X{n} - Greedy match, exactly n times"));
        p.addCompletion(new RegexCompletion(p, "{", "{",
                "X{n,} - Greedy match, at least n times"));
        p.addCompletion(new RegexCompletion(p, "{", "{",
                "X{n,m} - Greedy match, at least n but no more than m times"));
        p.addCompletion(new RegexCompletion(p, "??", "??",
                "X?? - Lazy match, 0 or 1 times"));
        p.addCompletion(new RegexCompletion(p, "*?", "*?",
                "X*? - Lazy match, 0 or more times"));
        p.addCompletion(new RegexCompletion(p, "+?", "+?",
                "X+? - Lazy match, 1 or more times"));
        p.addCompletion(new RegexCompletion(p, "?+", "?+",
                "X?+ - Possessive match, 0 or 1 times"));
        p.addCompletion(new RegexCompletion(p, "*+", "*+",
                "X*+ - Possessive match, 0 or more times"));
        p.addCompletion(new RegexCompletion(p, "++", "++",
                "X++ - Possessive match, 0 or more times"));

        // Back references
        p.addCompletion(new RegexCompletion(p, "\\i", "\\i",
                "\\i - Match of the capturing group i"));

        // Capturing groups
        p.addCompletion(new RegexCompletion(p, "(", "(",
                "(Expr) - Mark Expr as capturing group"));
        p.addCompletion(new RegexCompletion(p, "(?:", "(?:",
                "(?:Expr) - Non-capturing group"));
    }

    /**
     * Adds the completion choices for regexes in a "replace with" text field.
     *
     * @param p The completion provider to add to.
     * @see #addFindFieldCompletions(RegexAwareProvider)
     */
    private void addReplaceFieldCompletions(RegexAwareProvider p) {
        p.addCompletion(new RegexCompletion(p, "$", "$",
                "$i - Match of the capturing group i"));
        p.addCompletion(new RegexCompletion(p, "\\", "\\",
                "\\ - Quote next character"));
        p.addCompletion(new RegexCompletion(p, "\\t", "\\t", "\\t - Tab"));
        p.addCompletion(new RegexCompletion(p, "\\n", "\\n", "\\n - Newline"));
    }

    /**
     * Lazily creates the AutoCompletion instance this combo box uses.
     *
     * @return The auto-completion instance.
     */
    private AutoCompletion getAutoCompletion() {
        if (ac == null) {
            ac = new AutoCompletion(getCompletionProvider());
        }
        return ac;
    }

    /**
     * Creates the shared completion provider instance.
     *
     * @return The completion provider.
     */
    protected synchronized ICompletionProvider getCompletionProvider() {
        if (provider == null) {
            provider = new RegexAwareProvider();
            if (replace) {
                addReplaceFieldCompletions(provider);
            } else {
                addFindFieldCompletions(provider);
            }
        }
        return provider;
    }

    /**
     * Hides any auto-complete windows that are visible.
     *
     * @return Whether any windows were visible.
     */
    public boolean hideAutoCompletePopups() {
        return ac == null ? false : ac.hideChildWindows();
    }

    /**
     * @return Whether regex auto-complete is enabled.
     * @see #setAutoCompleteEnabled(boolean)
     */
    public boolean isAutoCompleteEnabled() {
        return enabled;
    }

    /**
     * Toggles whether regex auto-complete is enabled. This method will fire a
     * property change event of type {@link IContentAssistable#ASSISTANCE_IMAGE}.
     * 
     * @param enabled Whether regex auto complete should be enabled.
     * @see #isAutoCompleteEnabled()
     */
    public void setAutoCompleteEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                AutoCompletion ac = getAutoCompletion();
                JTextComponent tc = (JTextComponent) getEditor()
                        .getEditorComponent();
                ac.install(tc);
            } else {
                ac.uninstall();
            }
            String prop = IContentAssistable.ASSISTANCE_IMAGE;
            // Must take care how we fire the property event, as Swing property
            // change support won't fire a notice if old and new are both
            // non-null and old.equals(new)
            if (enabled) {
                firePropertyChange(prop, null,
                        AbstractSearchDialog.getContentAssistImage());
            } else {
                firePropertyChange(prop, null, null);
            }
        }
    }

    /**
     * A completion provider for regular expressions.
     * 
     * @author D. Campione
     * 
     */
    private static class RegexAwareProvider extends DefaultCompletionProvider {

        @Override
        protected boolean isValidChar(char ch) {
            switch (ch) {
            case '\\':
            case '(':
            case '*':
            case '.':
            case '?':
            case '[':
            case '^':
            case ':':
            case '{':
            case '$':
            case '+':
                return true;
            default:
                return false;
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private static class RegexCompletion extends BasicCompletion {

        private String inputText;

        /**
         * Ctor.
         *
         * @param provider The parent completion provider.
         * @param inputText The text the user must input.
         * @param replacementText The text to replace.
         * @param shortDesc A short description of the completion. This will be
         *        displayed in the completion list.
         */
        public RegexCompletion(ICompletionProvider provider, String inputText,
                String replacementText, String shortDesc) {
            super(provider, replacementText, shortDesc);
            this.inputText = inputText;
        }

        /** {@inheritDoc} */
        @Override
        public String getInputText() {
            return inputText;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return getShortDescription();
        }
    }
}