/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.AnimatedAssistantAction;
import net.sourceforge.open_teradata_viewer.actions.SchemaBrowserAction;
import net.sourceforge.open_teradata_viewer.animated_assistant.AnimatedAssistant;
import net.sourceforge.open_teradata_viewer.editor.Gutter;
import net.sourceforge.open_teradata_viewer.editor.IToolTipSupplier;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.TextScrollPane;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.LanguageAwareCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.SQLCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.search.OTVFindDialog;
import net.sourceforge.open_teradata_viewer.editor.search.OTVGoToDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewer;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphic_viewer.UndoMgr;
import net.sourceforge.open_teradata_viewer.help.HelpViewerWindow;
import net.sourceforge.open_teradata_viewer.plugin.EntryDescriptor;
import net.sourceforge.open_teradata_viewer.plugin.IPluginEntry;
import net.sourceforge.open_teradata_viewer.plugin.PluginFactory;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.SubstanceUtil;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;
import net.sourceforge.open_teradata_viewer.util.TranslucencyUtil;
import net.sourceforge.open_teradata_viewer.util.UIUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * The main frame.
 * 
 * @author D. Campione
 *
 */
public class ApplicationFrame extends JFrame implements ISyntaxConstants {

    private static final long serialVersionUID = -8572855678886323789L;

    private static ApplicationFrame APPLICATION_FRAME;
    public static final Color DEFAULT_FOREGROUND_COLOR_LOG = Color.DARK_GRAY;
    public static final Color WARNING_FOREGROUND_COLOR_LOG = Color.RED;
    public static final int MAX_CHARACTERS_LOG = 100000;
    /** Shared variable used by LookAndFeelAction and ApplicationMenuBar. */
    public static final String LAF_MENU_LABEL = "Look & Feel";

    /** The output console. */
    private Console console;

    private OTVSyntaxTextArea textArea;
    private AutoCompletion autoCompletion;
    private HelpViewerWindow helpFrame;
    private GraphicViewer graphicViewer;
    private ApplicationMenuBar menubar;
    private ApplicationToolBar toolbar;

    /** The schema browser toggle button. */
    private JToggleButton schemaBrowserToggleButton = new JToggleButton();

    /** The split pane that shows or hides the schema browser. */
    private JSplitPane splitPane;

    /** The scrollpane containing the schema browser. */
    private JScrollPane rightComponent;

    private TextScrollPane textScrollPane;

    private boolean fullScreenMode;
    private DisplayChanger displayChanger;

    private GlassPane glassPane;

    private OTVFindDialog _OTVFindDialog;
    private OTVGoToDialog _OTVGoToDialog;

    /** Used to dynamically load 3rd-party LookAndFeels. */
    private ThirdPartyLookAndFeelManager lafManager;

    /**
     * Whether <code>findWindowOpacityListener</code> has been attempted to be
     * created yet. This is kept in a variable instead of checking for
     * <code>null</code> because the creation is done via reflection, so it is a
     * fairly common case that creation is attempted but fails.
     */
    private boolean windowListenersInited;

    /**
     * Listens for focus events of certain child windows (those that can be made
     * translucent on focus lost).
     */
    private ChildWindowListener findWindowOpacityListener;

    /** Whether the Find dialog can have its opacity changed. */
    private boolean findWindowOpacityEnabled;

    /**
     * The opacity with which to render unfocused child windows that support
     * opacity changes.
     */
    private float findWindowOpacity;

    /** The rule used for making certain unfocused child windows translucent. */
    private int findWindowOpacityRule;

    public ApplicationFrame() {
        super(Main.APPLICATION_NAME);
        APPLICATION_FRAME = this;
    }

    public void drawIt() {
        // Create the splash screen, if this application has one
        final SplashScreen splashScreen = createSplashScreen();
        if (splashScreen != null) {
            splashScreen.setVisible(true);
        }

        // Do the rest of this stuff "later", so that the EDT has time to
        // actually display the splash screen and update it
        SwingUtilities.invokeLater(new StartupRunnable(splashScreen));
    }

    public AnimatedAssistant startAnimatedAssistant(
            AnimatedAssistant animatedAssistant) {
        getGlassPane().addAnimatedAssistant(animatedAssistant);
        return animatedAssistant;
    }

    public void stopAnimatedAssistant(AnimatedAssistant animatedAssistant) {
        getGlassPane().removeAnimatedAssistant(animatedAssistant);
    }

    public void installPlugins() {
        Drivers.setInitialized(false);
        try {
            Drivers.initialize();
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }

        String pluginsPath = "./";
        // The PluginFactory is instantiated
        PluginFactory pluginFactory = new PluginFactory(pluginsPath);
        // The application obtains the loaded EntryDescriptors
        Collection<EntryDescriptor> pluginsCollection = pluginFactory
                .getAllEntryDescriptor();
        if (pluginsCollection == null) { // No JAR file in the plugins path 
            return;
        }
        Iterator<EntryDescriptor> pluginsIterator = pluginsCollection
                .iterator();
        // Searching for plugins.. 
        while (pluginsIterator.hasNext()) {
            EntryDescriptor entryDescriptor = (EntryDescriptor) pluginsIterator
                    .next();
            IPluginEntry iPluginEntry = (IPluginEntry) pluginFactory
                    .getPluginEntry(entryDescriptor.getId());
        }
    }

    /**
     * Updates the look and feel for all components and windows in this
     * <code>ApplicationFrame</code> instance. This method assumes that
     * <code>setLookAndFeel(lnf)</code> has already been called.
     *
     * @param lnf The new look and feel.
     */
    public void updateLookAndFeel(LookAndFeel lnf) {
        try {
            Dimension size = getSize();

            // Update all components in this frame
            SwingUtilities.updateComponentTreeUI(this);
            pack();
            setSize(size);

            for (Frame f : ApplicationFrame.getFrames()) {
                SwingUtilities.updateComponentTreeUI(f);
                for (Window w : f.getOwnedWindows()) {
                    SwingUtilities.updateComponentTreeUI(w);
                    size = w.getSize();
                    w.pack();
                    w.setSize(size);
                }
            }
        } catch (Exception e) {
            ExceptionDialog.showException(e);
        }
    }

    private JSplitPane createWorkArea() {
        JSplitPane queryArea = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                createQueryEditor(), createQueryTable());
        queryArea.setOneTouchExpandable(true);
        queryArea.setDividerSize(4);
        queryArea.setDividerLocation(200);
        return queryArea;
    }

    private OTVSyntaxTextArea createTextArea() {
        OTVSyntaxTextArea textArea = new OTVSyntaxTextArea();
        textArea.setTabSize(4);
        textArea.setCaretPosition(0);
        textArea.requestFocusInWindow();
        textArea.setMarkOccurrences(true);
        return textArea;
    }

    private JPanel createQueryEditor() {
        JPanel globalQueryEditorPanel = new JPanel(new BorderLayout());
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        Actions.SCHEMA_BROWSER.setEnabled(isConnected);
        setToolbar(new ApplicationToolBar(schemaBrowserToggleButton));
        globalQueryEditorPanel.add(getToolbar(), BorderLayout.NORTH);
        textArea = createTextArea();

        textScrollPane = new TextScrollPane(textArea, true);
        Gutter gutter = textScrollPane.getGutter();
        gutter.setBookmarkingEnabled(true);
        gutter.setBookmarkIcon(ImageManager.getImage("/icons/bookmark.png"));
        textScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane,
                null);
        globalQueryEditorPanel.add(splitPane, BorderLayout.CENTER);

        // Create the status area
        JPanel statusPane = new JPanel(new GridLayout(1, 1));
        CaretListenerLabel caretListenerLabel = new CaretListenerLabel("Ready.");
        statusPane.add(caretListenerLabel);
        globalQueryEditorPanel.add(statusPane, BorderLayout.PAGE_END);

        textArea.addCaretListener(caretListenerLabel);

        addSyntaxHighlighting();

        return globalQueryEditorPanel;
    }

    private void addSyntaxHighlighting() {
        //      Syntax highlighting solutions tried so far:

        //      1 jEdit Syntax Package
        //        http://syntax.jedit.org/
        //        Not pluggable on JTextComponent, needed to use custom JEditTextArea, too heavy and all the wrong features

        //        syntax.SyntaxDocument doc = new SyntaxDocument();
        //        doc.setTokenMarker(new TSQLTokenMarker());
        //        text.setDocument(doc);

        //      2 http://www.discoverteenergy.com/files/SyntaxDocument.java
        //        Highlighting comments doesn't work well

        //        text = new JEditorPane();
        //        ((JEditorPane) text).setEditorKit(new StyledEditorKit() {
        //            public Document createDefaultDocument() {
        //                return new SyntaxDocument();
        //            }
        //        });

        //      3 com.Ostermiller.Syntax
        //        http://ostermiller.org/syntax/
        //        Strings in quotes aren't highlighted in real-time

        //        HighlightedDocument doc = new HighlightedDocument();
        //        doc.setHighlightStyle(HighlightedDocument.SQL_STYLE);
        //        text.setDocument(doc);

        //      4 JSyntaxColor
        //        http://www.japisoft.com/syntaxcolor/
        //        Not free and not open source

        //      5 Colorer
        //        http://colorer.sourceforge.net/
        //        OS dependant (dll files and stuff)

        //      6 CodeDocument
        //        http://forum.java.sun.com/thread.jspa?forumID=57&threadID=607646
        //        Lot of bugs

        //        text.setDocument(new CodeDocument());

        //      7 SyntaxHighlighter
        //        http://www.cs.bris.ac.uk/Teaching/Resources/COMS30122/tools/
        //        stops recolouring after a while

        //        Scanner scanner = new JavaScanner();
        //        text = new SyntaxHighlighter(24, 80, scanner);

        //      8 jsyntaxpane
        //        http://code.google.com/p/jsyntaxpane/
        //        Good work, but a customizable editor is better

        //      9 OTVSyntaxTextArea
        //        @see net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea
        //        Looks great, let's use it!
        textArea.setSyntaxEditingStyle(SYNTAX_STYLE_SQL);
        textArea.setCodeFoldingEnabled(true);

        ICompletionProvider provider = createCompletionProvider();
        // Install auto-completion onto our text area
        autoCompletion = new AutoCompletion(provider);
        autoCompletion.setListCellRenderer(new SQLCellRenderer());
        autoCompletion.setShowDescWindow(true);
        autoCompletion.setParameterAssistanceEnabled(true);
        autoCompletion.install(textArea);

        textArea.setToolTipSupplier((IToolTipSupplier) provider);
        ToolTipManager.sharedInstance().registerComponent(textArea);
    }

    private JScrollPane createQueryTable() {
        return new JScrollPane(ResultSetTable.getInstance());
    }

    public void updateTitle() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setTitle(isConnected ? Context.getInstance().getConnectionData()
                .getName()
                + " - " + Main.APPLICATION_NAME : Main.APPLICATION_NAME);
    }

    /** Handles the window closing. */
    public void handleWindowClose() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (isConnected) {
            Actions.DISCONNECT.actionPerformed(new ActionEvent(this, 0, null));
        }

        if (_OTVFindDialog != null) {
            _OTVFindDialog.dispose();
            _OTVFindDialog = null;
        }
        if (_OTVGoToDialog != null) {
            _OTVGoToDialog.dispose();
            _OTVGoToDialog = null;
        }

        if (helpFrame != null) {
            helpFrame.dispose();
            helpFrame = null;
        }
        if (graphicViewer != null) {
            graphicViewer.dispose();
            graphicViewer = null;
        }

        deleteHelpFiles();
        try {
            if (getConsole().bw != null) {
                getConsole().bw.close();
            }
        } catch (IOException ioe) {
            ExceptionDialog.ignoreException(ioe);
        }

        dispose();
        AWTExceptionHandler.shutdown();
    }

    private void deleteHelpFiles() {
        String tmpDir = Utilities.conformizePath(System
                .getProperty("java.io.tmpdir"));
        String dirToDelete[] = {
                tmpDir + "help" + File.separator + "images" + File.separator,
                tmpDir + "help" + File.separator };

        for (String element : dirToDelete) {
            File[] files = Utilities.listFiles(new File(element));
            if (files != null) {
                for (File file : files) {
                    Utilities.removeFile(file);
                }
                Utilities.removeFile(new File(element));
            }
        }
    }

    public void destroyObjectChooser() {
        if (rightComponent != null) {
            rightComponent = null;
            splitPane.setRightComponent(rightComponent);
            splitPane.setDividerSize(0);
            schemaBrowserToggleButton.setSelected(false);
            Actions.SCHEMA_BROWSER.setEnabled(false);
        }
    }

    public void initializeObjectChooser(final ConnectionData connectionData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SchemaBrowser schemaBrowser = new SchemaBrowser(
                            connectionData);
                    if (Actions.SCHEMA_BROWSER instanceof SchemaBrowserAction) {
                        SchemaBrowserAction schemaBrowserAction = (SchemaBrowserAction) Actions.SCHEMA_BROWSER;
                        schemaBrowser.addKeyListener(schemaBrowserAction);
                        schemaBrowser.addMouseListener(schemaBrowserAction);
                    }
                    rightComponent = new JScrollPane(schemaBrowser);
                    schemaBrowser.expand(new String[] {
                            connectionData.getName(), "username", "TABLES" });
                    Actions.SCHEMA_BROWSER.setEnabled(true);
                } catch (IllegalStateException ise) {
                    // Ignore: connection has been closed
                    ExceptionDialog.ignoreException(ise);
                }
            }
        }).start();
    }

    public void showHideObjectChooser() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            getConsole()
                    .println(
                            "Connect to a Teradata database to use the Schema Browser functionality.",
                            WARNING_FOREGROUND_COLOR_LOG);
            splitPane.setRightComponent(null);
            splitPane.setDividerSize(0);
            schemaBrowserToggleButton.setSelected(false);
            focusTextArea();
            return;
        }
        if (splitPane.getRightComponent() == null) {
            splitPane.setDividerLocation(.5);
            splitPane.setDividerSize(4);
            splitPane.setRightComponent(rightComponent);
            schemaBrowserToggleButton.setSelected(true);
            getObjectChooser().requestFocus();
        } else {
            splitPane.setRightComponent(null);
            splitPane.setDividerSize(0);
            schemaBrowserToggleButton.setSelected(false);
            focusTextArea();
        }
    }

    public void replaceText(String t) {
        textArea.replaceSelection(t);
    }

    /**
     * Sets the utility used to dynamically load 3rd-party LookAndFeels.
     *
     * @param manager The utility, or <code>null</code> for none.
     * @see #getLookAndFeelManager()
     */
    public void initLookAndFeelManager(ThirdPartyLookAndFeelManager manager) {
        lafManager = manager;

        if (lafManager != null) {
            // We must add our class loader capable of loading 3rd party LaF
            // jars as this property as it is used internally by Swing when
            // loading classes
            ClassLoader cl = lafManager.getLAFClassLoader();
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
        }
    }

    /**
     * Returns the splash screen to display while this GUI application is
     * loading.
     *
     * @return The splash screen. If <code>null</code> is returned, no splash
     *         screen is displayed.
     */
    protected SplashScreen createSplashScreen() {
        String img = "icons/logo.png";
        return new SplashScreen(img, "Initializing..");
    }

    public void focusTextArea() {
        textArea.requestFocusInWindow();
    }

    public void registerChildWindowListeners(Window w) {
        if (!windowListenersInited) {
            windowListenersInited = true;
            if (TranslucencyUtil.get().isTranslucencySupported(false)) {
                findWindowOpacityListener = new ChildWindowListener(this);
                findWindowOpacityListener
                        .setTranslucencyRule(findWindowOpacityRule);
            }
        }

        if (findWindowOpacityListener != null) {
            w.addWindowFocusListener(findWindowOpacityListener);
            w.addComponentListener(findWindowOpacityListener);
        }
    }

    /**
     * Creates the completion provider for a SQL editor. This provider can be
     * shared among multiple editors.
     *
     * @return The provider.
     */
    private ICompletionProvider createCompletionProvider() {
        // Create the provider used when typing code
        ICompletionProvider codeCP = createCodeCompletionProvider();

        // The provider used when typing a string
        ICompletionProvider stringCP = createStringCompletionProvider();

        // The provider used when typing a comment
        ICompletionProvider commentCP = createCommentCompletionProvider();

        // Create the "parent" completion provider
        LanguageAwareCompletionProvider provider = new LanguageAwareCompletionProvider(
                codeCP);
        provider.setStringCompletionProvider(stringCP);
        provider.setCommentCompletionProvider(commentCP);

        return provider;
    }

    /**
     * Returns the provider to use when editing code.
     *
     * @return The provider.
     * @see #createCommentCompletionProvider()
     * @see #createStringCompletionProvider()
     */
    private ICompletionProvider createCodeCompletionProvider() {
        // Add completions for the SQL standard library
        DefaultCompletionProvider cp = new DefaultCompletionProvider();

        // First try loading resource (running from demo jar), then try
        // accessing file (debugging in Eclipse)
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream("res/sql.xml");
        try {
            if (in != null) {
                cp.loadFromXML(in);
                in.close();
            } else {
                cp.loadFromXML(new File("res/sql.xml"));
            }
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }

        return cp;
    }

    /**
     * Returns the completion provider to use when the caret is in a string.
     *
     * @return The provider.
     * @see #createCodeCompletionProvider()
     * @see #createCommentCompletionProvider()
     */
    private ICompletionProvider createStringCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        cp.addCompletion(new BasicCompletion(cp, "%c", "char",
                "Prints a character"));
        cp.addCompletion(new BasicCompletion(cp, "%i", "signed int",
                "Prints a signed integer"));
        cp.addCompletion(new BasicCompletion(cp, "%f", "float",
                "Prints a float"));
        cp.addCompletion(new BasicCompletion(cp, "%s", "string",
                "Prints a string"));
        cp.addCompletion(new BasicCompletion(cp, "%u", "unsigned int",
                "Prints an unsigned integer"));
        cp.addCompletion(new BasicCompletion(cp, "\\n", "Newline",
                "Prints a newline"));
        return cp;
    }

    /**
     * Returns the provider to use when in a comment.
     *
     * @return The provider.
     * @see #createCodeCompletionProvider()
     * @see #createStringCompletionProvider()
     */
    private ICompletionProvider createCommentCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        cp.addCompletion(new BasicCompletion(cp, "TODO:", "A to-do reminder"));
        cp.addCompletion(new BasicCompletion(cp, "FIXME:",
                "A bug that needs to be fixed"));
        return cp;
    }

    public static ApplicationFrame getInstance() {
        return APPLICATION_FRAME;
    }

    public String getText() {
        return textArea.getSelectedText() != null ? textArea.getSelectedText()
                : textArea.getText();
    }

    public AutoCompletion getAutoCompletion() {
        return autoCompletion;
    }

    /**
     * Sets the opacity with which to render unfocused child windows, if this
     * option is enabled.
     *
     * @param opacity The opacity. This should be between <code>0</code> and
     *        <code>1</code>.
     * @see #getFindWindowOpacity()
     * @see #setFindWindowOpacityRule(int)
     */
    public void setFindWindowOpacity(float opacity) {
        findWindowOpacity = Math.max(0, Math.min(opacity, 1));
        if (windowListenersInited && isFindWindowOpacityEnabled()) {
            findWindowOpacityListener.refreshTranslucencies();
        }
    }

    /**
     * Toggles whether find window opacity is enabled.
     *
     * @param enabled Whether find window opacity should be enabled.
     * @see #isFindWindowOpacityEnabled()
     */
    public void setFindWindowOpacityEnabled(boolean enabled) {
        if (enabled != findWindowOpacityEnabled) {
            findWindowOpacityEnabled = enabled;
            if (windowListenersInited && findWindowOpacityListener != null) {
                findWindowOpacityListener.refreshTranslucencies();
            }
        }
    }

    /**
     * Toggles whether certain child windows should be made translucent.
     *
     * @param rule The new opacity rule.
     * @see #getFindWindowOpacityRule()
     * @see #setFindWindowOpacity(float)
     */
    public void setFindWindowOpacityRule(int rule) {
        if (rule != findWindowOpacityRule) {
            findWindowOpacityRule = rule;
            if (windowListenersInited) {
                findWindowOpacityListener.setTranslucencyRule(rule);
            }
        }
    }

    /**
     * Returns whether find window opacity is enabled.
     *
     * @return Whether find window opacity is enabled.
     * @see #setFindWindowOpacityEnabled(boolean)
     */
    public boolean isFindWindowOpacityEnabled() {
        return findWindowOpacityEnabled;
    }

    @Override
    public GlassPane getGlassPane() {
        return glassPane;
    }

    /**
     * Sets the Look and Feel for the opened application instance.
     *
     * @param lnfClassName The class name of the Look and Feel to set.
     */
    public void setLookAndFeel(String lnfClassName) {
        // Only set the Look and Feel if we're not already using that Look.
        // Compare against currently active one, not the one we want to change
        // to on restart, seems more logical to the end-user
        String current = UIManager.getLookAndFeel().getClass().getName();

        if (lnfClassName != null && !current.equals(lnfClassName)) {
            try {
                // Use application's LaF class loader, not a system one, as it
                // can access any additional 3rd-party LaF jars that weren't on
                // the classpath when the application started. Also, don't
                // necessarily trust UIDefaults.get("ClassLoader") to be this
                // class loader, as on Windows if the user changes the UxTheme
                // the LaF is updated outside of this call, and the property
                // value is reset to null
                ClassLoader cl = getLookAndFeelManager().getLAFClassLoader();
                // Set these properties before instantiating WebLookAndFeel
                if (WebLookAndFeelUtil.isWebLookAndFeel(lnfClassName)) {
                    WebLookAndFeelUtil.installWebLookAndFeelProperties(cl);
                }

                // Load the Look and Feel class. Note that we cannot simply use
                // its name for some reason (Exceptions are thrown)
                Class c = cl.loadClass(lnfClassName);
                final LookAndFeel lnf = (LookAndFeel) c.newInstance();

                // If we're changing to a LAF that supports window decorations
                // and our current one doesn't, or vice versa, inform the user
                // that this change will occur on restart. Substance seems to be
                // the only troublemaker here (Metal, for example, supports
                // window decorations, but works fine without special logic)
                boolean curSubstance = SubstanceUtil.isSubstanceInstalled();
                boolean nextSubstance = SubstanceUtil
                        .isASubstanceLookAndFeel(lnf);
                if (curSubstance != nextSubstance) {
                    String startupLookAndFeelProperty = "startup_lookandfeel_class";
                    try {
                        Config.saveSetting(startupLookAndFeelProperty,
                                lnfClassName);
                    } catch (Exception e) {
                        ExceptionDialog.ignoreException(e);
                    }

                    String message = "The Look And Feel will be loaded on next startup.";
                    String title = "Look And Feel";
                    UISupport.getDialogs().showInfoMessage(message, title);
                    return;
                }

                UIManager.setLookAndFeel(lnf);
                // Re-save the class loader BEFORE calling updateLookAndFeels(),
                // as the UIManager.setLookAndFeel() call above resets this
                // property to null, and we need this class loader to be set as
                // it's the one that's aware of our 3rd party JARs. Swing uses
                // this property (if non-null) to load classes from, and if we
                // don't set it, exceptions will be thrown
                UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
                UIUtil.installOsSpecificLafTweaks();
                Runnable updateUIRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateLookAndFeel(lnf);
                    }
                };

                // Ensure we update Look and Feels on event dispatch thread
                SwingUtilities.invokeLater(updateUIRunnable);
            } catch (Exception e) {
                ExceptionDialog.showException(e);
            }

            String startupLookAndFeelProperty = "startup_lookandfeel_class";
            try {
                Config.saveSetting(startupLookAndFeelProperty, lnfClassName);
            } catch (Exception e) {
                ExceptionDialog.ignoreException(e);
            }
        }
    }

    /**
     * Returns the opacity with which to render unfocused child windows, if this
     * option is enabled.
     *
     * @return The opacity.
     * @see #setFindWindowOpacity(float)
     */
    public float getFindWindowOpacity() {
        return findWindowOpacity;
    }

    /**
     * Returns the rule used for making certain child windows translucent.
     *
     * @return The rule.
     * @see #setFindWindowOpacityRule(int)
     * @see #getFindWindowOpacity()
     */
    public int getFindWindowOpacityRule() {
        return findWindowOpacityRule;
    }

    /**
     * Returns the manager in charge of any 3rd-party LookAndFeels this
     * application is aware of.
     *
     * @return The manager, or <code>null</code> if there is none.
     * @see #initLookAndFeelManager(ThirdPartyLookAndFeelManager)
     */
    public ThirdPartyLookAndFeelManager getLookAndFeelManager() {
        return lafManager;
    }

    /**
     * Returns an array of info. for JAR files containing 3rd party Look and
     * Feels. These JAR files will be added to the <code>UIManager</code>'s
     * classpath so that these LnFs can be used in this GUI application.<p>
     *
     * For this method to return anything, you must install a {@link
     * ThirdPartyLookAndFeelManager}.
     *
     * @return An array of information on JAR files containing Look and Feels.
     * @see #initLookAndFeelManager(ThirdPartyLookAndFeelManager)
     */
    public ExtendedLookAndFeelInfo[] get3rdPartyLookAndFeelInfo() {
        return lafManager != null ? lafManager.get3rdPartyLookAndFeelInfo()
                : null;
    }

    public SchemaBrowser getObjectChooser() {
        return rightComponent == null ? null : (SchemaBrowser) rightComponent
                .getViewport().getComponent(0);
    }

    public void setText(final String t) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.setText(t);
            }
        });
    }

    public boolean isFullScreenModeActive() {
        return fullScreenMode;
    }

    public void setFullScreenMode(boolean fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
        displayChanger.setDisplayMode(this.fullScreenMode);
    }

    /** @return The SQL editor. */
    public OTVSyntaxTextArea getTextComponent() {
        return textArea;
    }

    /** @return The help frame. */
    public HelpViewerWindow getHelpFrame() {
        return helpFrame;
    }

    /** @param helpFrame The help frame to set. */
    public void setHelpFrame(HelpViewerWindow helpFrame) {
        this.helpFrame = helpFrame;
    }

    /** @return The graphic viewer. */
    public GraphicViewer getGraphicViewer() {
        return graphicViewer;
    }

    /** @param graphicViewer The graphic viewer to set. */
    public void setGraphicViewer(GraphicViewer graphicViewer) {
        this.graphicViewer = graphicViewer;
    }

    /** @return The toolbar. */
    public ApplicationToolBar getToolbar() {
        return toolbar;
    }

    /** @param toolbar The toolbar to set. */
    public void setToolbar(ApplicationToolBar toolbar) {
        this.toolbar = toolbar;
    }

    /** @return The textScrollPane */
    public TextScrollPane getTextScrollPane() {
        return textScrollPane;
    }

    /** @return The menubar. */
    public ApplicationMenuBar getApplicationMenuBar() {
        return menubar;
    }

    /** @param menubar The menubar to set. */
    public void setApplicationMenuBar(ApplicationMenuBar menubar) {
        this.menubar = menubar;
        setJMenuBar(this.menubar);
    }

    /** @return The output console. */
    public Console getConsole() {
        return console;
    }

    /** @param console The output console to set. */
    private void setConsole(Console console) {
        this.console = console;
    }

    /** @return The find dialog. */
    public OTVFindDialog getFindDialog() {
        return _OTVFindDialog;
    }

    /** @param _OTVFindDialog The find dialog to set. */
    public void setFindDialog(OTVFindDialog _OTVFindDialog) {
        this._OTVFindDialog = _OTVFindDialog;
    }

    /** @return The goto dialog. */
    public OTVGoToDialog getGoToDialog() {
        return _OTVGoToDialog;
    }

    /** @param _OTVGoToDialog The goto dialog to set. */
    public void setGoToDialog(OTVGoToDialog _OTVGoToDialog) {
        this._OTVGoToDialog = _OTVGoToDialog;
    }

    /**
     * Actually creates the GUI. This is called after the splash screen is
     * displayed via <code>SwingUtilities#invokeLater()</code>.
     *
     * @author D. Campione
     * 
     */
    private class StartupRunnable implements Runnable {

        private SplashScreen splashScreen;

        public StartupRunnable(SplashScreen splashScreen) {
            this.splashScreen = splashScreen;
        }

        @Override
        public void run() {
            splashScreen.updateStatus(
                    "The LookAndFeel has been installed successfully.", 10);

            setIconImage(ImageManager.getImage("/icons/logo32.png").getImage());
            getRootPane().setGlassPane(glassPane = new GlassPane());

            splashScreen.updateStatus("Initializing the main window..", 20);
            getContentPane().setLayout(new BorderLayout());
            UISupport.setMainFrame(getInstance());

            splashScreen.updateStatus("Initializing the work area..", 30);
            JPanel globalPanel = new JPanel(new BorderLayout());
            globalPanel.add(createWorkArea(), BorderLayout.CENTER);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    System.exit(0);
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    handleWindowClose();
                }
            });

            // Create the console and configure it
            splashScreen.updateStatus("Initializing the console..", 40);
            setConsole(new Console(5, 30, MAX_CHARACTERS_LOG));
            getConsole().setEditable(true);
            getConsole().setCaretPosition(
                    getConsole().getDocument().getLength());
            DefaultCaret caret = (DefaultCaret) getConsole().getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            JScrollPane scrollPaneConsole = new JScrollPane(getConsole());

            splashScreen.updateStatus("Initializing the work panels..", 45);
            JSplitPane mainSplitPane = new JSplitPane(
                    JSplitPane.VERTICAL_SPLIT, globalPanel, scrollPaneConsole);
            mainSplitPane.setOneTouchExpandable(true);
            mainSplitPane.setDividerSize(4);
            mainSplitPane.setDividerLocation(510);
            getContentPane().add(mainSplitPane, BorderLayout.CENTER);

            splashScreen.updateStatus("Initializing the find dialog..", 50);
            setFindWindowOpacityEnabled(true);
            setFindWindowOpacity(0.6f);
            setFindWindowOpacityRule(ChildWindowListener.TRANSLUCENT_WHEN_OVERLAPPING_APP);
            setFindDialog(new OTVFindDialog(ApplicationFrame.this));

            splashScreen
                    .updateStatus("Initializing the goto line dialog..", 55);
            setGoToDialog(new OTVGoToDialog(ApplicationFrame.this));

            splashScreen.updateStatus("Initializing the graphic viewer..", 60);
            graphicViewer = new GraphicViewer();

            splashScreen.updateStatus(
                    "Initializing the graphic viewer undo manager..", 65);
            GraphicViewerDocument graphicViewerDocument = graphicViewer.myView
                    .getDocument();
            graphicViewerDocument.addDocumentListener(graphicViewer);
            graphicViewerDocument.setUndoManager(new UndoMgr());

            splashScreen.updateStatus("Initializing the status bar..", 70);
            getContentPane().add(new ApplicationStatusBar(),
                    BorderLayout.PAGE_END);

            try {
                if (StringUtil.isEmpty(Config.getDrivers())) {
                    Config.saveDrivers("com.teradata.jdbc.TeraDriver");
                }

                splashScreen.updateStatus(
                        "Initializing the animated assistant..", 75);
                String animatedAssistantProperty = "animated_assistant_actived";
                String strAnimatedAssistantActived = Config
                        .getSetting(animatedAssistantProperty);
                if (StringUtil.isEmpty(strAnimatedAssistantActived)) {
                    Config.saveSetting(animatedAssistantProperty, "false");
                    ((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                            .setAnimatedAssistantActived(false);
                } else {
                    ((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                            .setAnimatedAssistantActived(strAnimatedAssistantActived
                                    .equalsIgnoreCase("true"));
                }
            } catch (Exception e) {
                ExceptionDialog.ignoreException(e);
            }

            splashScreen.updateStatus("Initializing the display changer..", 80);
            displayChanger = new DisplayChanger(ApplicationFrame.this);
            displayChanger.setExclusiveMode(false);

            splashScreen.updateStatus("Initializing the menu bar..", 85);
            setApplicationMenuBar(new ApplicationMenuBar());

            splashScreen.updateStatus("Installing the plugins..", 90);
            installPlugins();

            splashScreen.updateStatus("Configuring the main window..", 98);
            pack();
            double screenWidth = Toolkit.getDefaultToolkit().getScreenSize()
                    .getWidth();
            double screenHeight = Toolkit.getDefaultToolkit().getScreenSize()
                    .getHeight();
            setSize(Math.min(1150, (int) (screenWidth * .8)),
                    Math.min(720, (int) (screenHeight * .8)));
            setMinimumSize(new Dimension((int) (screenWidth * .2),
                    (int) (screenHeight * .2)));
            SwingUtil.centerWithinScreen(ApplicationFrame.this);
            splashScreen.updateStatus("Done.", 100);

            // Clean up the splash screen, if necessary
            if (splashScreen != null) {
                splashScreen.setVisible(false);
                splashScreen.dispose();
            }
            setVisible(true);

            Actions.CONNECT.actionPerformed(new ActionEvent(this, 0, null));
        }
    }
}