/*
 * Open Teradata Viewer ( editor language support xml )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.xml;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;

import net.sourceforge.open_teradata_viewer.actions.GoToMemberAction;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractMarkupLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.tree.XmlOutlineTree;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Language support for XML. Currently supported features include:
 *
 * <ul>
 *    <li>Squiggle underlining of basic XML structure errors.</li>
 *    <li>Usage of {@link XmlOutlineTree}, a tree view modeling the XML in the
 *        <code>SyntaxTextArea</code>.</li>
 * </ul>
 *
 * @author D. Campione
 * @see XmlOutlineTree
 *
 */
public class XmlLanguageSupport extends AbstractMarkupLanguageSupport {

    /** The shared completion provider instance for all XML editors. */
    private XmlCompletionProvider provider;

    /** Whether syntax errors are squiggle-underlined in the editor. */
    private boolean showSyntaxErrors;

    /** Ctor. */
    public XmlLanguageSupport() {
        setAutoActivationEnabled(true);
        setParameterAssistanceEnabled(false);
        setShowDescWindow(false);
        setShowSyntaxErrors(true);
    }

    /** {@inheritDoc} */
    @Override
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new HtmlCellRenderer();
    }

    /**
     * Lazily creates the shared completion provider instance for XML.
     *
     * @return The completion provider.
     */
    private XmlCompletionProvider getProvider() {
        if (provider == null) {
            provider = new XmlCompletionProvider();
        }
        return provider;
    }

    /**
     * Returns the XML parser running on a text area with this XML language
     * support installed.
     *
     * @param textArea The text area.
     * @return The XML parser. This will be <code>null</code> if the text area
     *         does not have this <tt>XmlLanguageSupport</tt> installed.
     */
    public XmlParser getParser(SyntaxTextArea textArea) {
        // Could be a parser for another language
        Object parser = textArea.getClientProperty(PROPERTY_LANGUAGE_PARSER);
        if (parser instanceof XmlParser) {
            return (XmlParser) parser;
        }
        return null;
    }

    /**
     * Returns whether syntax errors are squiggle-underlined in the editor.
     *
     * @return Whether errors are squiggle-underlined.
     * @see #setShowSyntaxErrors(boolean)
     */
    public boolean getShowSyntaxErrors() {
        return showSyntaxErrors;
    }

    /** {@inheritDoc} */
    @Override
    public void install(SyntaxTextArea textArea) {
        // Code completion currently only completes words found elsewhere in the
        // editor
        XmlCompletionProvider provider = getProvider();
        AutoCompletion ac = createAutoCompletion(provider);
        ac.install(textArea);
        installImpl(textArea, ac);

        XmlParser parser = new XmlParser(this);
        textArea.addParser(parser);
        textArea.putClientProperty(PROPERTY_LANGUAGE_PARSER, parser);

        installKeyboardShortcuts(textArea);
    }

    /** {@inheritDoc} */
    @Override
    protected void installKeyboardShortcuts(SyntaxTextArea textArea) {
        super.installKeyboardShortcuts(textArea);

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        int c = textArea.getToolkit().getMenuShortcutKeyMask();
        int shift = InputEvent.SHIFT_MASK;

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, c | shift), "GoToType");
        am.put("GoToType", new GoToMemberAction(XmlOutlineTree.class));
    }

    /**
     * Sets whether syntax errors are squiggle-underlined in the editor.
     *
     * @param show Whether syntax errors are squiggle-underlined.
     * @see #getShowSyntaxErrors()
     */
    public void setShowSyntaxErrors(boolean show) {
        showSyntaxErrors = show;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean shouldAutoCloseTag(String tag) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall(SyntaxTextArea textArea) {
        uninstallImpl(textArea);

        XmlParser parser = getParser(textArea);
        if (parser != null) {
            textArea.removeParser(parser);
        }

        uninstallKeyboardShortcuts(textArea);
    }

    /** {@inheritDoc} */
    @Override
    protected void uninstallKeyboardShortcuts(SyntaxTextArea textArea) {
        super.uninstallKeyboardShortcuts(textArea);

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        int c = textArea.getToolkit().getMenuShortcutKeyMask();
        int shift = InputEvent.SHIFT_MASK;

        im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_O, c | shift));
        am.remove("GoToType");
    }
}