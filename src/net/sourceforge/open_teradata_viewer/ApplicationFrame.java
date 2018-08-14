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
import java.net.URL;
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.AnimatedAssistantAction;
import net.sourceforge.open_teradata_viewer.actions.SchemaBrowserAction;
import net.sourceforge.open_teradata_viewer.animated_assistant.AnimatedAssistant;
import net.sourceforge.open_teradata_viewer.editor.CollapsibleSectionPanel;
import net.sourceforge.open_teradata_viewer.editor.Gutter;
import net.sourceforge.open_teradata_viewer.editor.IToolTipSupplier;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.SearchContext;
import net.sourceforge.open_teradata_viewer.editor.SearchEngine;
import net.sourceforge.open_teradata_viewer.editor.SearchResult;
import net.sourceforge.open_teradata_viewer.editor.TextScrollPane;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.LanguageAwareCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.SQLCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.search.FindDialog;
import net.sourceforge.open_teradata_viewer.editor.search.FindToolBar;
import net.sourceforge.open_teradata_viewer.editor.search.ISearchListener;
import net.sourceforge.open_teradata_viewer.editor.search.OTVGoToDialog;
import net.sourceforge.open_teradata_viewer.editor.search.ReplaceDialog;
import net.sourceforge.open_teradata_viewer.editor.search.ReplaceToolBar;
import net.sourceforge.open_teradata_viewer.editor.search.SearchEvent;
import net.sourceforge.open_teradata_viewer.editor.spell.SpellingParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.ErrorStrip;
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
import net.sourceforge.open_teradata_viewer.util.UIUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * The main frame.
 * 
 * @author D. Campione
 *
 */
public class ApplicationFrame extends JFrame implements ISyntaxConstants,
        ISearchListener, HyperlinkListener {

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

    private FindDialog findDialog;
    private ReplaceDialog replaceDialog;
    private OTVGoToDialog _OTVGoToDialog;
    private CaretListenerLabel caretListenerLabel;
    private CollapsibleSectionPanel csp;
    private FindToolBar findToolBar;
    private ReplaceToolBar replaceToolBar;

    /** Used to dynamically load 3rd-party LookAndFeels. */
    private ThirdPartyLookAndFeelManager lafManager;

    private SpellingParser spellingParser;

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
            IPluginEntry pluginEntry = (IPluginEntry) pluginFactory
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
        JPanel contentPane = new JPanel(new BorderLayout());
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
        csp.add(textScrollPane);
        ErrorStrip errorStrip = new ErrorStrip(textArea);
        contentPane.add(csp, BorderLayout.CENTER);
        contentPane.add(errorStrip, BorderLayout.LINE_END);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contentPane,
                null);

        globalQueryEditorPanel.add(splitPane, BorderLayout.CENTER);

        // Create the status area
        JPanel statusPane = new JPanel(new GridLayout(1, 1));
        caretListenerLabel = new CaretListenerLabel("Ready.");
        statusPane.add(caretListenerLabel);
        globalQueryEditorPanel.add(statusPane, BorderLayout.PAGE_END);

        textArea.addCaretListener(caretListenerLabel);

        addSyntaxHighlighting();

        return globalQueryEditorPanel;
    }

    /** Creates our Find and Replace dialogs. */
    public void initSearchDialogs() {
        setFindDialog(new FindDialog(this, this));
        setReplaceDialog(new ReplaceDialog(this, this));

        // This ties the properties of the two dialogs together (match case,
        // regex, etc..)
        SearchContext context = findDialog.getSearchContext();
        replaceDialog.setSearchContext(context);

        // Create tool bars and tie their search contexts together also
        setFindToolBar(new FindToolBar(this));
        findToolBar.setSearchContext(context);
        setReplaceToolBar(new ReplaceToolBar(this));
        replaceToolBar.setSearchContext(context);
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

        textArea.addHyperlinkListener(this);
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

        csp.hideBottomComponent();
        setCollapsibleSectionPanel(null);
        if (findToolBar != null) {
            setFindToolBar(null);
        }
        if (replaceToolBar != null) {
            setReplaceToolBar(null);
        }
        if (replaceDialog != null) {
            if (replaceDialog.isVisible()) {
                replaceDialog.setVisible(false);
            }
            replaceDialog.dispose();
            setReplaceDialog(null);
        }
        if (findDialog != null) {
            if (findDialog.isVisible()) {
                findDialog.setVisible(false);
            }
            findDialog.dispose();
            setFindDialog(null);
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

    /**
     * Listens for events from our search dialogs and actually does the dirty
     * work.
     */
    @Override
    public void searchEvent(SearchEvent e) {
        SearchEvent.Type type = e.getType();
        SearchContext context = e.getSearchContext();
        SearchResult result = null;

        switch (type) {
        case MARK_ALL:
            result = SearchEngine.markAll(textArea, context);
            break;
        case FIND:
            result = SearchEngine.find(textArea, context);
            if (!result.wasFound()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
            }
            break;
        case REPLACE:
            result = SearchEngine.replace(textArea, context);
            if (!result.wasFound()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
            }
            break;
        case REPLACE_ALL:
            result = SearchEngine.replaceAll(textArea, context);
            UISupport.getDialogs().showInfoMessage(
                    result.getCount() + " occurrences replaced.");
            break;
        }

        if (result.getMarkedCount() == 0) {
            caretListenerLabel.setText("Text not found");
        }
    }

    /**
     * Starts a thread to load the spell checker when the app is made visible,
     * since the dictionary is somewhat large (takes 0.9 seconds to load on
     * a 3.0 GHz Core 2 Duo).<p/>
     *
     * This assumes the application will only be made visible once, which is certainly
     * true for our program.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        new Thread() {
            @Override
            public void run() {
                spellingParser = createSpellingParser();
                if (spellingParser != null) {
                    try {
                        File userDict = File
                                .createTempFile("spellDemo", ".txt");
                        spellingParser.setUserDictionary(userDict);
                        System.out.println("User dictionary: "
                                + userDict.getAbsolutePath());
                    } catch (IOException ioe) { // Applets, IO errors
                        System.err.println("Can't open user dictionary: "
                                + ioe.getMessage());
                    } catch (SecurityException se) { // Applets
                        System.err.println("Can't open user dictionary: "
                                + se.getMessage());
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            textArea.addParser(spellingParser);
                            Actions.TOGGLE_SPELLING_PARSER.setEnabled(true);
                        }
                    });
                }
            }
        }.start();
    }

    private SpellingParser createSpellingParser() {
        File zip = new File("english_dic.zip");
        try {
            return SpellingParser.createEnglishSpellingParser(zip, true);
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
        return null;
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            URL url = e.getURL();
            if (url == null) {
                UIManager.getLookAndFeel().provideErrorFeedback(null);
            } else {
                Utilities.openURLWithDefaultBrowser(url.toString());
            }
        }
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
                Class<?> c = cl.loadClass(lnfClassName);
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

    /** @return The goto dialog. */
    public OTVGoToDialog getGoToDialog() {
        return _OTVGoToDialog;
    }

    /** @param _OTVGoToDialog The goto dialog to set. */
    private void setGoToDialog(OTVGoToDialog _OTVGoToDialog) {
        this._OTVGoToDialog = _OTVGoToDialog;
    }

    /**
     * Returns the application's "collapsible section panel"; that is, the panel
     * containing the main view and possible find/replace tool bars.
     *
     * @return The collapsible section panel.
     */
    public CollapsibleSectionPanel getCollapsibleSectionPanel() {
        return csp;
    }

    private void setCollapsibleSectionPanel(CollapsibleSectionPanel csp) {
        this.csp = csp;
    }

    public FindToolBar getFindToolBar() {
        return findToolBar;
    }

    private void setFindToolBar(FindToolBar findToolBar) {
        this.findToolBar = findToolBar;
    }

    public ReplaceToolBar getReplaceToolBar() {
        return replaceToolBar;
    }

    private void setReplaceToolBar(ReplaceToolBar replaceToolBar) {
        this.replaceToolBar = replaceToolBar;
    }

    public FindDialog getFindDialog() {
        return findDialog;
    }

    private void setFindDialog(FindDialog findDialog) {
        this.findDialog = findDialog;
    }

    public ReplaceDialog getReplaceDialog() {
        return replaceDialog;
    }

    private void setReplaceDialog(ReplaceDialog replaceDialog) {
        this.replaceDialog = replaceDialog;
    }

    public SpellingParser getSpellingParser() {
        return spellingParser;
    }

    private void setSpellingParser(SpellingParser spellingParser) {
        this.spellingParser = spellingParser;
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

            splashScreen.updateStatus("Initializing the goto dialog..", 25);
            setGoToDialog(new OTVGoToDialog(ApplicationFrame.this));
            splashScreen
                    .updateStatus("Initializing the search tool bars..", 35);
            initSearchDialogs();
            setCollapsibleSectionPanel(new CollapsibleSectionPanel());

            splashScreen.updateStatus("Initializing the work area..", 40);
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
            splashScreen.updateStatus("Initializing the console..", 45);
            setConsole(new Console(5, 30, MAX_CHARACTERS_LOG));
            getConsole().setEditable(true);
            getConsole().setCaretPosition(
                    getConsole().getDocument().getLength());
            DefaultCaret caret = (DefaultCaret) getConsole().getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            JScrollPane scrollPaneConsole = new JScrollPane(getConsole());

            splashScreen.updateStatus("Initializing the work panels..", 50);
            JSplitPane mainSplitPane = new JSplitPane(
                    JSplitPane.VERTICAL_SPLIT, globalPanel, scrollPaneConsole);
            mainSplitPane.setOneTouchExpandable(true);
            mainSplitPane.setDividerSize(4);
            mainSplitPane.setDividerLocation(510);
            getContentPane().add(mainSplitPane, BorderLayout.CENTER);

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
                String animatedAssistantProperty = "animated_assistant_activated";
                String strAnimatedAssistantActivated = Config
                        .getSetting(animatedAssistantProperty);
                if (StringUtil.isEmpty(strAnimatedAssistantActivated)) {
                    Config.saveSetting(animatedAssistantProperty, "false");
                    ((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                            .setAnimatedAssistantActivated(false);
                } else {
                    ((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                            .setAnimatedAssistantActivated(strAnimatedAssistantActivated
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