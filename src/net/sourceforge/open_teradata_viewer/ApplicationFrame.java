/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import jsyntaxpane.DefaultSyntaxKit;
import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.SchemaBrowserAction;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewer;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphicviewer.UndoMgr;
import net.sourceforge.open_teradata_viewer.help.HelpViewerWindow;
import net.sourceforge.open_teradata_viewer.plugin.Plugin;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import com.incors.plaf.kunststoff.KunststoffTheme;

/**
 * The main frame.
 * 
 * @author D. Campione
 */
public class ApplicationFrame extends JFrame {

    private static final long serialVersionUID = -8572855678886323789L;
    private static ApplicationFrame APPLICATION_FRAME;
    public static final Color DEFAULT_FOREGROUND_COLOR_LOG = Color.DARK_GRAY;
    public static final Color WARNING_FOREGROUND_COLOR_LOG = Color.RED;
    public static final int MAX_CHARACTERS_LOG = 100000;
    /**
     * Shared variable used by ChangeLookAndFeelAction and
     * ApplicationMenuBar
     */
    public static final String LAF_MENU_LABEL = "Look & Feel";
    public Plugin PLUGIN;

    public ChangeLog changeLog;
    private JEditorPane text;
    public HelpViewerWindow helpFrame;
    public GraphicViewer graphicViewer;

    /** The schema browser toggle button. */
    private JToggleButton schemaBrowserToggleButton = new JToggleButton();

    /** The split pane that shows or hides the schema browser. */
    private JSplitPane splitPane;

    /** The scollpane containing the schema browser. */
    private JScrollPane rightComponent;

    public ApplicationFrame() {
        super(Main.APPLICATION_NAME);
        APPLICATION_FRAME = this;
    }

    private void setUI() {
        try {
            KunststoffLookAndFeel kunststoffLnF = new KunststoffLookAndFeel();
            KunststoffLookAndFeel.setCurrentTheme(new KunststoffTheme());
            UIManager.setLookAndFeel(kunststoffLnF);
        } catch (UnsupportedLookAndFeelException ex) {
            // handle exception or not, whatever you prefer
        }
        // this line needs to be implemented in order to make JWS work properly
        UIManager.getLookAndFeelDefaults().put("ClassLoader",
                getClass().getClassLoader());
    }

    public void drawIt(SplashScreen splashScreen) {
        setUI();
        splashScreen.progress(9);

        getContentPane().setLayout(new BorderLayout());
        UISupport.setMainFrame(getInstance());
        splashScreen.progress(20);
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
        splashScreen.progress(20);

        // Create the text area for the status log and configure it.
        changeLog = new ChangeLog(5, 30, MAX_CHARACTERS_LOG);
        changeLog.setEditable(true);
        changeLog.setCaretPosition(changeLog.getDocument().getLength());
        DefaultCaret caret = (DefaultCaret) changeLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPaneForLog = new JScrollPane(changeLog);
        splashScreen.progress(10);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerSize(7);
        mainSplitPane.setDividerLocation(470);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setLeftComponent(globalPanel);
        mainSplitPane.setRightComponent(scrollPaneForLog);
        getContentPane().add(mainSplitPane, BorderLayout.CENTER);
        splashScreen.progress(10);

        //Add some key bindings.
        addBindings();
        splashScreen.progress(10);

        graphicViewer = new GraphicViewer();
        splashScreen.progress(10);
        GraphicViewerDocument graphicViewerDocument = graphicViewer.myView
                .getDocument();
        graphicViewerDocument.addDocumentListener(graphicViewer);
        graphicViewerDocument.setUndoManager(new UndoMgr());
        getContentPane().add(new SystemStatusBar(), BorderLayout.PAGE_END);

        try {
            if (Config.getDrivers().trim().length() == 0) {
                Config.saveDrivers("com.teradata.jdbc.TeraDriver");
            }
        } catch (Exception e) {
            // ignore
        }

        splashScreen.progress(10);
    }
    public void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (UnsupportedLookAndFeelException ulafe) {
            ulafe.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        for (Frame f : ApplicationFrame.getFrames()) {
            SwingUtilities.updateComponentTreeUI(f);
            for (Window w : f.getOwnedWindows()) {
                SwingUtilities.updateComponentTreeUI(w);
            }
        }
    }

    private JSplitPane createWorkArea() {
        JSplitPane queryArea = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                createQueryEditor(), createQueryTable());
        queryArea.setOneTouchExpandable(true);
        queryArea.setDividerSize(7);
        queryArea.setDividerLocation(200);
        return queryArea;
    }

    private JPanel createQueryEditor() {
        JPanel globalQueryEditorPanel = new JPanel(new BorderLayout());
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        Actions.SCHEMA_BROWSER.setEnabled(isConnected);
        globalQueryEditorPanel.add(new ApplicationToolBar(
                schemaBrowserToggleButton), BorderLayout.NORTH);
        text = new JEditorPane();

        JScrollPane scrollPaneQueryEditor = new JScrollPane(text);
        scrollPaneQueryEditor
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneQueryEditor.getViewport().setPreferredSize(
                new Dimension(0, 100));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPaneQueryEditor, null);
        globalQueryEditorPanel.add(splitPane, BorderLayout.CENTER);

        // Create the status area
        JPanel statusPane = new JPanel(new GridLayout(1, 1));
        CaretListenerLabel caretListenerLabel = new CaretListenerLabel("Ready.");
        statusPane.add(caretListenerLabel);
        globalQueryEditorPanel.add(statusPane, BorderLayout.PAGE_END);

        text.addCaretListener(caretListenerLabel);

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
        //        Looks great, let's use it!

        DefaultSyntaxKit.initKit();
        text.setContentType("text/sql");

        // Rearrange the menu a bit
        JPopupMenu menu = text.getComponentPopupMenu();
        menu.remove(17); // "Goto line" defined twice
        menu.remove(15); // "Show abbreviations", not implemented for text/sql
        menu.remove(12); // "Jump to Pair", not implemented for text/sql
        menu.add(text.getActionMap().get("toggle-lines"));

        // Set proper menu texts
        DefaultSyntaxKit kit = (DefaultSyntaxKit) text.getEditorKit();
        for (Object key : text.getActionMap().keys()) {
            Action action = text.getActionMap().get(key);
            action.putValue(
                    Action.NAME,
                    kit.getProperty(String.format("Action.%s.MenuText",
                            action.getValue(Action.NAME))));
        }
    }

    private JScrollPane createQueryTable() {
        return new JScrollPane(ResultSetTable.getInstance());
    }

    protected void addBindings() {
        InputMap inputMap = text.getInputMap();

        KeyStroke key = KeyStroke.getKeyStroke("F5");
        inputMap.put(key, Actions.RUN);
    }

    public void updateTitle() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (isConnected) {
            setTitle(Context.getInstance().getConnectionData().getName()
                    + " - " + Main.APPLICATION_NAME);
        } else {
            setTitle(Main.APPLICATION_NAME);
        }
    }

    /**
     * Handles the window closing.
     */
    public void handleWindowClose() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (isConnected) {
            Actions.DISCONNECT.actionPerformed(new ActionEvent(this, 0, null));
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
            if (changeLog.bw != null) {
                changeLog.bw.close();
            }
        } catch (IOException e) {
        }
        dispose();
    }

    public static ApplicationFrame getInstance() {
        return APPLICATION_FRAME;
    }

    public String getText() {
        return text.getSelectedText() != null ? text.getSelectedText() : text
                .getText();
    }

    public void printStackTraceOnGUI(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(out));
        changeLog.append(new String(out.toByteArray()),
                WARNING_FOREGROUND_COLOR_LOG);
    }

    private void deleteHelpFiles() {
        String directoryPath = Tools.conformizePath(System
                .getProperty("java.io.tmpdir"));

        File file = new File(directoryPath + "help" + File.separator
                + "manual.html");
        if (file.exists()) {
            file.delete();
        }
        file = new File(directoryPath + File.separator + "help"
                + File.separator + "FAQ.html");
        if (file.exists()) {
            file.delete();
        }

        directoryPath = Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + "help"
                + File.separator
                + "manual_file" + File.separator;
        File[] files = Tools.listFiles(new File(directoryPath));
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        file = new File(directoryPath);
        if (file.exists()) {
            file.delete();
        }
        file = new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir")) + "help");
        if (file.exists()) {
            file.delete();
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
                    ConnectionData connectionData = Context.getInstance()
                            .getConnectionData();
                    final SchemaBrowser schemaBrowser = new SchemaBrowser(
                            connectionData);
                    if (Actions.SCHEMA_BROWSER instanceof SchemaBrowserAction) {
                        SchemaBrowserAction schemaBrowserAction = (SchemaBrowserAction) Actions.SCHEMA_BROWSER;
                        schemaBrowser.addKeyListener(schemaBrowserAction);
                        schemaBrowser.addMouseListener(schemaBrowserAction);
                    }
                    rightComponent = new JScrollPane(schemaBrowser);
                    schemaBrowser.expand(new String[]{connectionData.getName(),
                            "username", "TABLES"});
                    Actions.SCHEMA_BROWSER.setEnabled(true);
                } catch (IllegalStateException e) {
                    // ignore: connection has been closed
                    ExceptionDialog.ignoreException(e);
                }
            }
        }).start();
    }
    public void showHideObjectChooser() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            changeLog
                    .append("Connect to a Teradata database to use the Schema Browser functionality.\n",
                            WARNING_FOREGROUND_COLOR_LOG);
            splitPane.setRightComponent(null);
            splitPane.setDividerSize(0);
            schemaBrowserToggleButton.setSelected(false);
            text.requestFocus();
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
            text.requestFocus();
        }
    }
    public SchemaBrowser getObjectChooser() {
        return rightComponent == null ? null : (SchemaBrowser) rightComponent
                .getViewport().getComponent(0);
    }

    public void replaceText(String t) {
        text.replaceSelection(t);
    }

    public void setText(final String t) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                text.setText(t);
            }
        });
    }

    public JEditorPane getTextComponent() {
        return text;
    }
}
