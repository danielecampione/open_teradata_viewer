/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.actions.GoToMemberAction;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ClasspathLibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ClasspathSourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.LibraryInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JavaScriptShorthandCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.tree.JavaScriptOutlineTree;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.JavaScriptTokenMaker;
import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.Token;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.AstRoot;
import sun.org.mozilla.javascript.internal.ast.NodeVisitor;

/**
 * Language support for JavaScript. This requires Rhino, which is included with
 * this library.
 * 
 * @author D. Campione
 * 
 */
public class JavaScriptLanguageSupport extends AbstractLanguageSupport {

    /**
     * Maps <code>JavaScriptParser</code>s to <code>Info</code> instances
     * about them.
     */
    private Map<JavaScriptParser, Info> parserToInfoMap;
    private JarManager jarManager;
    private boolean xmlAvailable;
    private boolean client;
    private boolean strictMode;
    private int languageVersion;
    private JsErrorParser errorParser;
    private JavaScriptParser parser;
    private JavaScriptCompletionProvider provider;
    private File jshintrc;

    /** Client property installed on text areas that points to a listener. */
    private static final String PROPERTY_LISTENER = "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptLanguageSupport.Listener";

    public JavaScriptLanguageSupport() {
        parserToInfoMap = new HashMap<JavaScriptParser, Info>();
        jarManager = createJarManager();
        provider = createJavaScriptCompletionProvider();
        setErrorParser(JsErrorParser.RHINO);
        setECMAVersion(null, jarManager); // Load default ECMA 
        setDefaultCompletionCellRenderer(new JavaScriptCellRenderer());
        setAutoActivationEnabled(true);
        setParameterAssistanceEnabled(true);
        setShowDescWindow(true);
        setLanguageVersion(Integer.MIN_VALUE); // Take Rhino's default
    }

    /**
     * Creates a jar manager instance for used in JS language support.
     *
     * @return The jar manager instance.
     */
    protected JarManager createJarManager() {
        JarManager jarManager = new JarManager();

        return jarManager;
    }

    public void setECMAVersion(String version, JarManager jarManager) {
        // Load classes
        try {
            List<String> classes = provider
                    .getProvider()
                    .getTypesFactory()
                    .setTypeDeclarationVersion(version, isXmlAvailable(),
                            isClient());
            provider.getProvider().setXMLSupported(isXmlAvailable());
            if (classes != null) {
                LibraryInfo info = new ClasspathLibraryInfo(classes,
                        new ClasspathSourceLocation());
                jarManager.addClassFileSource(info);
            }
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }

    /**
     * Creates the provider to use for an STA instance editing JavaScript.
     * Subclasses can override to return custom subclasses of
     * <code>JavaScriptCompletionProvider</code>.
     * 
     * @return The provider.
     */
    protected JavaScriptCompletionProvider createJavaScriptCompletionProvider() {
        return new JavaScriptCompletionProvider(jarManager, this);
    }

    /**
     * Returns the engine to use for checking for syntax errors in JavaScript
     * files. Note that regardless of the value specified to this method, Rhino
     * is always used for code completion and the outline tree.
     *
     * @return The engine.
     * @see #setErrorParser(JsErrorParser)
     */
    public JsErrorParser getErrorParser() {
        return errorParser;
    }

    public JarManager getJarManager() {
        return jarManager;
    }

    public JavaScriptParser getJavaScriptParser() {
        return parser;
    }

    /**
     * Returns the location of the <code>.jshintrc</code> file to use if using
     * JsHint as your error parser. This property is ignored if {@link
     * #getErrorParser()} does not return {@link JsErrorParser#JSHINT}.
     *
     * @return The <code>.jshintrc</code> file or <code>null</code> if none; in
     *         that case, the JsHint defaults will be used.
     * @see #setJsHintRCFile(File)
     * @see #setErrorParser(JsErrorParser)
     */
    public File getJsHintRCFile() {
        return jshintrc;
    }

    public int getJsHintIndent() {
        final int DEFAULT = 4;
        return DEFAULT;
    }

    /**
     * Sets the JS version to use when parsing the code.
     *
     * @return The JS version. This should be one of the
     *        <code>VERSION_xxx</code> constants in Rhino's {@link Context}
     *        class. If this is set to a value unknown to Rhino, then Rhino's
     *        default value is used (<code>VERSION_DEFAULT</code>).
     * @see #setLanguageVersion(int)
     */
    public int getLanguageVersion() {
        return languageVersion;
    }

    /**
     * Returns the JS parser running on a text area with this JavaScript
     * language support installed.
     * 
     * @param textArea The text area.
     * @return The JS parser. This will be <code>null</code> if the text area
     *         does not have this <code>JavaScriptLanguageSupport</code>
     *         installed.
     */
    public JavaScriptParser getParser(SyntaxTextArea textArea) {
        // Could be a parser for another language
        Object parser = textArea.getClientProperty(PROPERTY_LANGUAGE_PARSER);
        if (parser instanceof JavaScriptParser) {
            return (JavaScriptParser) parser;
        }
        return null;
    }

    @Override
    public void install(SyntaxTextArea textArea) {
        // We use a custom auto-completion
        // AutoCompletion ac = createAutoCompletion(p);
        AutoCompletion ac = new JavaScriptAutoCompletion(provider, textArea);
        ac.setListCellRenderer(getDefaultCompletionCellRenderer());
        ac.setAutoCompleteEnabled(isAutoCompleteEnabled());
        ac.setAutoActivationEnabled(isAutoActivationEnabled());
        ac.setAutoActivationDelay(getAutoActivationDelay());
        ac.setParameterAssistanceEnabled(isParameterAssistanceEnabled());
        ac.setExternalURLHandler(new JavaScriptDocUrlhandler(this));
        ac.setShowDescWindow(getShowDescWindow());
        ac.install(textArea);
        installImpl(textArea, ac);

        Listener listener = new Listener(textArea);
        textArea.putClientProperty(PROPERTY_LISTENER, listener);

        parser = new JavaScriptParser(this, textArea);
        textArea.putClientProperty(PROPERTY_LANGUAGE_PARSER, parser);
        textArea.addParser(parser);

        Info info = new Info(provider, parser);
        parserToInfoMap.put(parser, info);

        installKeyboardShortcuts(textArea);

        // Set XML on JavaScriptTokenMaker
        JavaScriptTokenMaker.setE4xSupported(isXmlAvailable());

        textArea.setLinkGenerator(new JavaScriptLinkGenerator(this));
    }

    /**
     * Installs extra keyboard shortcuts supported by this language support.
     * 
     * @param textArea The text area to install the shortcuts into.
     */
    private void installKeyboardShortcuts(SyntaxTextArea textArea) {
        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        int c = textArea.getToolkit().getMenuShortcutKeyMask();
        int shift = InputEvent.SHIFT_MASK;

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, c | shift), "GoToType");
        am.put("GoToType", new GoToMemberAction(JavaScriptOutlineTree.class));
    }

    /**
     * Returns whether strict mode (more warnings are detected) is enabled.
     * 
     * @return Whether strict mode is enabled.
     * @see #setStrictMode(boolean)
     */
    public boolean isStrictMode() {
        return strictMode;
    }

    /**
     * Returns whether E4X is supported in parsed JavaScript.
     * 
     * @return Whether E4X is supported.
     * @see #setXmlAvailable(boolean)
     */
    public boolean isXmlAvailable() {
        return xmlAvailable;
    }

    /**
     * @return Whether the JavaScript support supports client/browser objects.
     */
    public boolean isClient() {
        return client;
    }

    protected void reparseDocument(int offset) {
        provider.reparseDocument(offset);
    }

    /**
     * Set whether the JavaScript support supports client/browser objects.
     *
     * @param client - true if client mode is supported.
     */
    public void setClient(boolean client) {
        this.client = client;
    }

    /**
     * Sets the engine to use for identifying syntax errors in JavaScript files.
     * Note that regardless of the value specified to this method, Rhino is
     * always used for code completion and the outline tree.
     *
     * @param errorParser The engine to use. This cannot be <code>null</code>.
     * @return Whether this was actually a new error parser.
     * @see #getErrorParser()
     */
    public boolean setErrorParser(JsErrorParser errorParser) {
        if (errorParser == null) {
            throw new IllegalArgumentException("errorParser cannot be null");
        }
        if (errorParser != this.errorParser) {
            this.errorParser = errorParser;
            return true;
        }
        return false;
    }

    /**
     * Sets the location of the <code>.jshintrc</code> file to use if using
     * JsHint as your error parser. This property is ignored if {@link
     * #getErrorParser()} does not return {@link JsErrorParser#JSHINT}.
     *
     * @param file The <code>.jshintrc</code> file or <code>null</code> if none;
     *        in that case, the JsHint defaults will be used.
     * @return Whether the new .jshintrc file is different than the original
     *         one.
     * @see #getJsHintRCFile()
     * @see #setErrorParser(JsErrorParser)
     */
    public boolean setJsHintRCFile(File file) {
        if ((file == null && jshintrc != null)
                || (file != null && jshintrc == null)
                || (file != null && !file.equals(jshintrc))) {
            jshintrc = file;
            return true;
        }
        return false;
    }

    /**
     * Sets the JS version to use when parsing the code.
     *
     * @param languageVersion The JS version. This should be one of the
     *        <code>VERSION_xxx</code> constants in Rhino's {@link Context}
     *        class. If this is set to a value unknown to Rhino, then Rhino's
     *        default value is used (<code>VERSION_DEFAULT</code>).
     * @see #getLanguageVersion()
     */
    public void setLanguageVersion(int languageVersion) {
        if (languageVersion < 0) {
            languageVersion = Context.VERSION_UNKNOWN;
        }
        this.languageVersion = languageVersion;
    }

    /**
     * Sets whether strict mode (more warnings are detected) is enabled.
     * 
     * @param strict Whether strict mode is enabled.
     * @return Whether a new value was actually set for this property.
     * @see #isStrictMode()
     */
    public boolean setStrictMode(boolean strict) {
        if (strict != strictMode) {
            strictMode = strict;
            return true;
        }
        return false;
    }

    /**
     * Sets whether E4X is supported in parsed JavaScript.
     * 
     * @param available Whether E4X is supported.
     * @return Whether a new value was actually set for this property.
     * @see #isXmlAvailable()
     */
    public boolean setXmlAvailable(boolean available) {
        if (available != this.xmlAvailable) {
            this.xmlAvailable = available;
            return true;
        }
        return false;
    }

    @Override
    public void uninstall(SyntaxTextArea textArea) {
        uninstallImpl(textArea);

        JavaScriptParser parser = getParser(textArea);
        Info info = parserToInfoMap.remove(parser);
        if (info != null) { // Should always be true
            parser.removePropertyChangeListener(JavaScriptParser.PROPERTY_AST,
                    info);
        }
        textArea.removeParser(parser);
        textArea.putClientProperty(PROPERTY_LANGUAGE_PARSER, null);
        textArea.setToolTipSupplier(null);

        Object listener = textArea.getClientProperty(PROPERTY_LISTENER);
        if (listener instanceof Listener) { // Should always be true
            ((Listener) listener).uninstall();
            textArea.putClientProperty(PROPERTY_LISTENER, null);
        }

        uninstallKeyboardShortcuts(textArea);
    }

    /**
     * Uninstalls any keyboard shortcuts specific to this language support.
     * 
     * @param textArea The text area to uninstall the actions from.
     */
    private void uninstallKeyboardShortcuts(SyntaxTextArea textArea) {
        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        int c = textArea.getToolkit().getMenuShortcutKeyMask();
        int shift = InputEvent.SHIFT_MASK;

        im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_O, c | shift));
        am.remove("GoToType");
    }

    /**
     * Manages information about the parsing/auto-completion for a single text
     * area. Unlike many simpler language supports,
     * <code>JavaScriptLanguageSupport</code> cannot share any information
     * amongst instances of <code>SyntaxTextArea</code>.
     * 
     * @author D. Campione
     * 
     */
    private static class Info implements PropertyChangeListener {

        public JavaScriptCompletionProvider provider;

        public Info(JavaScriptCompletionProvider provider,
                JavaScriptParser parser) {
            this.provider = provider;
            parser.addPropertyChangeListener(JavaScriptParser.PROPERTY_AST,
                    this);
        }

        /**
         * Called when a text area is re-parsed.
         * 
         * @param e The event.
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();

            if (JavaScriptParser.PROPERTY_AST.equals(name)) {
                AstRoot root = (AstRoot) e.getNewValue();
                provider.setASTRoot(root);
            }
        }
    }

    /**
     * A hack of <code>AutoCompletion</code> that forces the parser to re-parse
     * the document when the user presses Ctrl+space.
     * 
     * @author D. Campione
     * 
     */
    private class JavaScriptAutoCompletion extends AutoCompletion {

        private SyntaxTextArea textArea;

        public JavaScriptAutoCompletion(JavaScriptCompletionProvider provider,
                SyntaxTextArea textArea) {
            super(provider);
            this.textArea = textArea;
        }

        @Override
        protected String getReplacementText(ICompletion c, Document doc,
                int start, int len) {
            String replacement = super.getReplacementText(c, doc, start, len);
            if (c instanceof JavaScriptShorthandCompletion) {
                try {
                    int caret = textArea.getCaretPosition();
                    String leadingWS = SyntaxUtilities.getLeadingWhitespace(
                            doc, caret);
                    if (replacement.indexOf('\n') > -1) {
                        replacement = replacement.replaceAll("\n", "\n"
                                + leadingWS);
                    }

                } catch (BadLocationException ble) {
                    ExceptionDialog.ignoreException(ble);
                }
            }
            return replacement;
        }

        @Override
        protected int refreshPopupWindow() {
            // Force the parser to re-parse
            JavaScriptParser parser = getParser(textArea);
            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            String style = textArea.getSyntaxEditingStyle();
            parser.parse(doc, style);
            return super.refreshPopupWindow();
        }
    }

    /**
     * Listens for various events in a text area editing Java (in particular,
     * caret events, so we can track the "active" code block).
     * 
     * @author D. Campione
     * 
     */
    private class Listener implements CaretListener, ActionListener {

        private SyntaxTextArea textArea;
        private Timer t;
        private DeepestScopeVisitor visitor;

        public Listener(SyntaxTextArea textArea) {
            this.textArea = textArea;
            textArea.addCaretListener(this);
            t = new Timer(650, this);
            t.setRepeats(false);
            visitor = new DeepestScopeVisitor();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JavaScriptParser parser = getParser(textArea);
            if (parser == null) {
                return; // Shouldn't happen
            }
            AstRoot astRoot = parser.getAstRoot();

            if (astRoot != null) {
                int dot = textArea.getCaretPosition();
                visitor.reset(dot);
                astRoot.visit(visitor);
                AstNode scope = visitor.getDeepestScope();
                if (scope != null && scope != astRoot) {
                    int start = scope.getAbsolutePosition();
                    int end = Math.min(start + scope.getLength() - 1, textArea
                            .getDocument().getLength());
                    try {
                        int startLine = textArea.getLineOfOffset(start);
                        int endLine = end < 0 ? textArea.getLineCount()
                                : textArea.getLineOfOffset(end);
                        textArea.setActiveLineRange(startLine, endLine);
                    } catch (BadLocationException ble) {
                        ExceptionDialog.hideException(ble); // Never happens
                    }
                } else {
                    textArea.setActiveLineRange(-1, -1);
                }
            }
        }

        @Override
        public void caretUpdate(CaretEvent e) {
            t.restart();
        }

        /**
         * Should be called whenever Java language support is removed from a
         * text area.
         */
        public void uninstall() {
            textArea.removeCaretListener(this);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class DeepestScopeVisitor implements NodeVisitor {

        private int offs;
        private AstNode deepestScope;

        private boolean containsOffs(AstNode node) {
            int start = node.getAbsolutePosition();
            return start <= offs && start + node.getLength() > offs;
        }

        public AstNode getDeepestScope() {
            return deepestScope;
        }

        public void reset(int offs) {
            this.offs = offs;
            deepestScope = null;
        }

        @Override
        public boolean visit(AstNode node) {
            switch (node.getType()) {
            case Token.FUNCTION:
                if (containsOffs(node)) {
                    deepestScope = node;
                    return true;
                }
                return false;
            default:
                return true;
            case Token.BLOCK: // Get scope starting at e.g. "function", not "{"
                return true;
            }
        }
    }
}