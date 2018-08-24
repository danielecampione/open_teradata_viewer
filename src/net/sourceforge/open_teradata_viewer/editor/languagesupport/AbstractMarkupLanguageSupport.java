/*
 * Open Teradata Viewer ( editor language support )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport;

import java.awt.event.ActionEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.Caret;
import javax.swing.text.Element;
import javax.swing.text.TextAction;

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;

/**
 * Base class for language supports for markup languages, such as HTML, PHP and
 * JSP. This class facilitates support for automatically adding a closing tag
 * (e.g. "<code>&lt;/foo&gt;</code>") when the user types an opening tag (e.g.
 * "<code>&lt;foo attr='val'&gt;</code>").
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractMarkupLanguageSupport extends
        AbstractLanguageSupport {

    protected static final String INSERT_CLOSING_TAG_ACTION = "HtmlLanguageSupport.InsertClosingTag";

    /**
     * Whether closing tags are automatically added when the user types opening
     * tags.  For HTML, this will only occur for tags that are allowed to have
     * closing tags, based on the doctype.  For XML, this will always occur if
     * this property is set to <code>true</code>.
     */
    private boolean autoAddClosingTags;

    protected AbstractMarkupLanguageSupport() {
        setAutoAddClosingTags(true);
    }

    /**
     * Returns whether closing tags should be automatically added when the user
     * types a (non-self-closing) start tag.  This will only be done for tags
     * where closing tags are accepted and valid, based on the doctype.
     *
     * @return Whether to automatically add closing tags.
     * @see #setAutoAddClosingTags(boolean)
     */
    public boolean getAutoAddClosingTags() {
        return autoAddClosingTags;
    }

    /**
     * Installs extra keyboard shortcuts supported by this language support.
     * The default implementation maps an action to automatically add closing
     * tags when '&gt;' is pressed; subclasses can override and add additional
     * shortcuts if desired.<p>
     *
     * Subclasses should call this method in their
     * {@link #install(SyntaxTextArea)} methods.
     *
     * @param textArea The text area to install the shortcuts into.
     * @see #uninstallKeyboardShortcuts(SyntaxTextArea)
     */
    protected void installKeyboardShortcuts(SyntaxTextArea textArea) {

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();

        im.put(KeyStroke.getKeyStroke('>'), INSERT_CLOSING_TAG_ACTION);
        am.put(INSERT_CLOSING_TAG_ACTION, new InsertClosingTagAction());

    }

    /**
     * Subclasses should override this method to return whether a specified
     * tag should have its closing tag auto-inserted.
     *
     * @param tag The name of the tag to check.
     * @return Whether the tag should have its closing tag auto-inserted.
     */
    protected abstract boolean shouldAutoCloseTag(String tag);

    /**
     * Sets whether closing tags should be automatically added when the user
     * types a (non-self-closing) start tag.  This will only be done for tags
     * where closing tags are accepted and valid, based on the doctype.
     *
     * @param autoAdd Whether to automatically add closing tags.
     * @see #getAutoAddClosingTags()
     */
    public void setAutoAddClosingTags(boolean autoAdd) {
        autoAddClosingTags = autoAdd;
    }

    /**
     * Uninstalls any keyboard shortcuts specific to this language support.<p>
     *
     * Subclasses should call this method in their
     * {@link #uninstall(SyntaxTextArea)} methods.
     *
     * @param textArea The text area to uninstall the actions from.
     * @see #installKeyboardShortcuts(SyntaxTextArea)
     */
    protected void uninstallKeyboardShortcuts(SyntaxTextArea textArea) {

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();

        im.remove(KeyStroke.getKeyStroke('>'));
        am.remove(INSERT_CLOSING_TAG_ACTION);

    }

    /**
     * Action that checks whether a closing tag should be auto-inserted into
     * the document.  Subclasses should map this action to the '&gt;'
     * key-typed event.
     * 
     * @author D. Campione
     * 
     */
    private class InsertClosingTagAction extends TextAction {

        private static final long serialVersionUID = 6258713210464986238L;

        InsertClosingTagAction() {
            super(INSERT_CLOSING_TAG_ACTION);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            SyntaxTextArea textArea = (SyntaxTextArea) getTextComponent(e);
            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            Caret c = textArea.getCaret();

            int dot = c.getDot(); // Get before "<" insertion
            boolean selection = dot != c.getMark(); // Me too
            textArea.replaceSelection(">");

            // Don't automatically complete a tag if there was a selection
            if (!selection && getAutoAddClosingTags()) {

                IToken t = doc.getTokenListForLine(textArea
                        .getCaretLineNumber());
                t = SyntaxUtilities.getTokenAtOffset(t, dot);
                if (t != null
                        && t.isSingleChar(IToken.MARKUP_TAG_DELIMITER, '>')) {
                    String tagName = discoverTagName(doc, dot);
                    if (tagName != null) {
                        textArea.replaceSelection("</" + tagName + ">");
                        textArea.setCaretPosition(dot + 1);
                    }
                }

            }

        }

        /**
         * Discovers the name of the tag just opened, if any.  Assumes standard
         * SGML-style markup tags.
         *
         * @param doc The document to parse.
         * @param dot The location of the caret.  This should be right at a
         *        "<code>&gt;</code>" character closing an HTML tag.
         * @return The name of the tag to close, or <code>null</code> if it
         *         could not be determined.
         */
        private String discoverTagName(SyntaxDocument doc, int dot) {

            String candidate = null;

            Element root = doc.getDefaultRootElement();
            int curLine = root.getElementIndex(dot);

            // For now, we only check for tags on the current line, for
            // simplicity.  Tags spanning multiple lines aren't common anyway.
            IToken t = doc.getTokenListForLine(curLine);
            while (t != null && t.isPaintable()) {
                if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                    if (t.isSingleChar('<')) {
                        t = t.getNextToken();
                        if (t != null && t.isPaintable()) {
                            candidate = t.getLexeme();
                        }
                    } else if (t.isSingleChar('>')) {
                        if (t.getOffset() == dot) {
                            if (candidate == null
                                    || shouldAutoCloseTag(candidate)) {
                                return candidate;
                            }
                            return null;
                        }
                    } else if (t.is(IToken.MARKUP_TAG_DELIMITER, "</")) {
                        candidate = null;
                    }
                }

                t = t.getNextToken();

            }

            return null; // No match found

        }

    }

}