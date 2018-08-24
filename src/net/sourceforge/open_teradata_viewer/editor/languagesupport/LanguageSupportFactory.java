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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Provides language support (code completion, etc..) for programming languages
 * in SyntaxTextArea. Different languages may have varying levels of "support".
 *
 * @author D. Campione
 * 
 */
public class LanguageSupportFactory implements PropertyChangeListener {

    private static final LanguageSupportFactory INSTANCE = new LanguageSupportFactory();

    /**
     * Maps styles to class-names-for-language-supports; this way we can lazily
     * create the <code>ILanguageSupports</code> when necessary.
     */
    private Map<String, String> styleToSupportClass;

    /** Maps syntax styles to language supports for them. */
    private Map<String, ILanguageSupport> styleToSupport;

    /**
     * Client property set on SyntaxTextAreas that points to the current
     * language support for that text area.
     */
    private static final String LANGUAGE_SUPPORT_PROPERTY = "net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport";

    /** Ctor. */
    private LanguageSupportFactory() {
        createSupportMap();
    }

    /**
     * Adds language support for a language. This is a hook for applications
     * using this library to add language support for custom languages.
     *
     * @param style The language to add support for. This should be one of the
     *        values defined in {@link ISyntaxConstants}. Any previous language
     *        support for this language is removed. 
     * @param lsClassName The class name of the <code>ILanguageSupport</code>.
     */
    public void addLanguageSupport(String style, String lsClassName) {
        styleToSupportClass.put(style, lsClassName);
    }

    /** Creates the mapping of syntax styles to language supports. */
    private void createSupportMap() {
        styleToSupport = new HashMap<String, ILanguageSupport>();
        styleToSupportClass = new HashMap<String, String>();

        String prefix = "net.sourceforge.open_teradata_viewer.editor.languagesupport.";

        addLanguageSupport(ISyntaxConstants.SYNTAX_STYLE_C, prefix
                + "c.CLanguageSupport");
        addLanguageSupport(ISyntaxConstants.SYNTAX_STYLE_CSS, prefix
                + "css.CssLanguageSupport");
        addLanguageSupport(ISyntaxConstants.SYNTAX_STYLE_JAVA, prefix
                + "java.JavaLanguageSupport");
        addLanguageSupport(ISyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, prefix
                + "js.JavaScriptLanguageSupport");
        addLanguageSupport(ISyntaxConstants.SYNTAX_STYLE_XML, prefix
                + "xml.XmlLanguageSupport");
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance.
     */
    public static LanguageSupportFactory get() {
        return INSTANCE;
    }

    /**
     * Returns the language support for a programming language.
     *
     * @param style The language. This should be one of the constants defined in
     *        {@link ISyntaxConstants}.
     * @return The language support or <code>null</code> if none is registered
     *         for the language specified.
     */
    public ILanguageSupport getSupportFor(String style) {
        ILanguageSupport support = styleToSupport.get(style);

        if (support == null) {
            String supportClazz = styleToSupportClass.get(style);
            if (supportClazz != null) {
                try {
                    Class<?> clazz = Class.forName(supportClazz);
                    support = (ILanguageSupport) clazz.newInstance();
                } catch (RuntimeException re) {
                    throw re;
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
                styleToSupport.put(style, support);
                // Always remove from classes to load, so we don't try again
                styleToSupportClass.remove(style);
            }
        }

        return support;
    }

    /**
     * Installs language support on a STA depending on its syntax style.
     *
     * @param textArea The text area to install language support on.
     * @see #uninstallSupport(SyntaxTextArea)
     */
    private void installSupport(SyntaxTextArea textArea) {
        String style = textArea.getSyntaxEditingStyle();
        ILanguageSupport support = getSupportFor(style);
        if (support != null) {
            support.install(textArea);
        }
        textArea.putClientProperty(LANGUAGE_SUPPORT_PROPERTY, support);
    }

    /**
     * Listens for SyntaxTextAreas to change what language they're highlighting,
     * so language support can be updated appropriately.
     *
     * @param e The event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        SyntaxTextArea source = (SyntaxTextArea) e.getSource();
        String name = e.getPropertyName();
        if (SyntaxTextArea.SYNTAX_STYLE_PROPERTY.equals(name)) {
            uninstallSupport(source);
            installSupport(source);
        }
    }

    /**
     * Registers a SyntaxTextArea to receive language support. The text area
     * will get support for the currently highlighted language and if it changes
     * what language it is highlighting, the support will change as appropriate.
     *
     * @param textArea The text area to register.
     */
    public void register(SyntaxTextArea textArea) {
        installSupport(textArea);
        textArea.addPropertyChangeListener(
                SyntaxTextArea.SYNTAX_STYLE_PROPERTY, this);
    }

    /**
     * Uninstalls the language support on a SyntaxTextArea, if any.
     *
     * @param textArea The text area.
     * @see #installSupport(SyntaxTextArea)
     */
    private void uninstallSupport(SyntaxTextArea textArea) {
        ILanguageSupport support = (ILanguageSupport) textArea
                .getClientProperty(LANGUAGE_SUPPORT_PROPERTY);
        if (support != null) {
            support.uninstall(textArea);
        }
    }

    /**
     * Un-registers a SyntaxTextArea. This removes any language support on it.
     *
     * @param textArea The text area.
     * @see #register(SyntaxTextArea)
     */
    public void unregister(SyntaxTextArea textArea) {
        uninstallSupport(textArea);
        textArea.removePropertyChangeListener(
                SyntaxTextArea.SYNTAX_STYLE_PROPERTY, this);
    }
}