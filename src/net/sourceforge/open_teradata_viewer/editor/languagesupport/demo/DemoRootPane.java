/*
 * Open Teradata Viewer ( editor language support demo )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreeNode;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.TextScrollPane;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractSourceTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.LanguageSupportFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JavaLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.tree.JavaOutlineTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.tree.JavaScriptOutlineTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.tree.XmlOutlineTree;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * The root pane used by the demos. This allows both the applet and the
 * stand-alone application to share the same UI.
 *
 * @author D. Campione
 *
 */
class DemoRootPane extends JRootPane implements HyperlinkListener,
        ISyntaxConstants, IActions {

    private static final long serialVersionUID = 7642909140270118508L;

    private JScrollPane treeSP;
    private AbstractSourceTree tree;
    private TextScrollPane scrollPane;
    private SyntaxTextArea textArea;

    public DemoRootPane() {
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        ILanguageSupport support = lsf.getSupportFor(SYNTAX_STYLE_JAVA);
        JavaLanguageSupport jls = (JavaLanguageSupport) support;
        try {
            jls.getJarManager().addCurrentJreClassFileSource();
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }

        // Dummy tree keeps JViewport's "background" looking right initially
        JTree dummy = new JTree((TreeNode) null);
        treeSP = new JScrollPane(dummy);

        textArea = createTextArea();
        setText("CExample.txt", SYNTAX_STYLE_C);
        scrollPane = new TextScrollPane(textArea, true);
        scrollPane.setIconRowHeaderEnabled(true);
        scrollPane.getGutter().setBookmarkingEnabled(true);

        final JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeSP, scrollPane);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sp.setDividerLocation(0.25);
            }
        });
        sp.setContinuousLayout(true);
        setContentPane(sp);

        setJMenuBar(createMenuBar());
    }

    private void addItem(Action a, ButtonGroup bg, JMenu menu) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(a);
        bg.add(item);
        menu.add(item);
    }

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu menu = new JMenu("File");
        menu.add(new JMenuItem(new OpenAction(this)));
        menu.addSeparator();
        menu.add(new JMenuItem(new ExitAction()));
        mb.add(menu);

        menu = new JMenu("Language");
        ButtonGroup bg = new ButtonGroup();
        addItem(new StyleAction(this, "C", "CExample.txt", SYNTAX_STYLE_C), bg,
                menu);
        addItem(new StyleAction(this, "CSS", "CssExample.txt", SYNTAX_STYLE_CSS),
                bg, menu);
        addItem(new StyleAction(this, "Groovy", "GroovyExample.txt",
                SYNTAX_STYLE_GROOVY), bg, menu);
        addItem(new StyleAction(this, "Java", "JavaExample.txt",
                SYNTAX_STYLE_JAVA), bg, menu);
        addItem(new StyleAction(this, "JavaScript", "JSExample.txt",
                SYNTAX_STYLE_JAVASCRIPT), bg, menu);
        addItem(new StyleAction(this, "JSP", "JspExample.txt", SYNTAX_STYLE_JSP),
                bg, menu);
        addItem(new StyleAction(this, "Perl", "PerlExample.txt",
                SYNTAX_STYLE_PERL), bg, menu);
        addItem(new StyleAction(this, "HTML", "HtmlExample.txt",
                SYNTAX_STYLE_HTML), bg, menu);
        addItem(new StyleAction(this, "PHP", "PhpExample.txt", SYNTAX_STYLE_PHP),
                bg, menu);
        addItem(new StyleAction(this, "sh", "ShellExample.txt",
                SYNTAX_STYLE_UNIX_SHELL), bg, menu);
        addItem(new StyleAction(this, "XML", "XMLExample.txt", SYNTAX_STYLE_XML),
                bg, menu);
        menu.getItem(0).setSelected(true);
        mb.add(menu);

        menu = new JMenu("LookAndFeel");
        bg = new ButtonGroup();
        LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < infos.length; i++) {
            addItem(new LookAndFeelAction(this, infos[i]), bg, menu);
        }
        mb.add(menu);

        menu = new JMenu("View");
        menu.add(new JCheckBoxMenuItem(new ToggleLayeredHighlightsAction(this)));
        mb.add(menu);

        menu = new JMenu("Help");
        menu.add(new JMenuItem(new AboutAction(this)));
        mb.add(menu);

        return mb;
    }

    /**
     * Creates the text area for this application.
     *
     * @return The text area.
     */
    private SyntaxTextArea createTextArea() {
        SyntaxTextArea textArea = new SyntaxTextArea(25, 80);
        LanguageSupportFactory.get().register(textArea);
        textArea.setCaretPosition(0);
        textArea.addHyperlinkListener(this);
        textArea.requestFocusInWindow();
        textArea.setMarkOccurrences(true);
        textArea.setCodeFoldingEnabled(true);
        textArea.setTabsEmulated(true);
        textArea.setTabSize(3);
        ToolTipManager.sharedInstance().registerComponent(textArea);
        return textArea;
    }

    /** Focuses the text area. */
    void focusTextArea() {
        textArea.requestFocusInWindow();
    }

    SyntaxTextArea getTextArea() {
        return textArea;
    }

    /**
     * Called when a hyperlink is clicked in the text area.
     *
     * @param e The event.
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            URL url = e.getURL();
            if (url == null) {
                UIManager.getLookAndFeel().provideErrorFeedback(null);
            } else {
                JOptionPane.showMessageDialog(this,
                        "URL clicked:\n" + url.toString());
            }
        }
    }

    /**
     * Opens a file in the editor (as opposed to one of the pre-defined code
     * examples).
     *
     * @param file The file to open.
     */
    public void openFile(File file) {
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            textArea.read(r, null);
            textArea.setCaretPosition(0);
            r.close();
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
            UIManager.getLookAndFeel().provideErrorFeedback(this);
            return;
        }
    }

    /**
     * Displays a tree view of the current source code, if available for the
     * current programming language.
     */
    private void refreshSourceTree() {
        if (tree != null) {
            tree.uninstall();
        }

        String language = textArea.getSyntaxEditingStyle();
        if (ISyntaxConstants.SYNTAX_STYLE_JAVA.equals(language)) {
            tree = new JavaOutlineTree();
        } else if (ISyntaxConstants.SYNTAX_STYLE_JAVASCRIPT.equals(language)) {
            tree = new JavaScriptOutlineTree();
        } else if (ISyntaxConstants.SYNTAX_STYLE_XML.equals(language)) {
            tree = new XmlOutlineTree();
        } else {
            tree = null;
        }

        if (tree != null) {
            tree.listenTo(textArea);
            treeSP.setViewportView(tree);
        } else {
            JTree dummy = new JTree((TreeNode) null);
            treeSP.setViewportView(dummy);
        }
        treeSP.revalidate();
    }

    /**
     * Sets the content in the text area to that in the specified resource.
     *
     * @param resource The resource to load.
     * @param style The syntax style to use when highlighting the text.
     */
    void setText(String resource, String style) {
        textArea.setSyntaxEditingStyle(style);

        ClassLoader cl = getClass().getClassLoader();
        BufferedReader r = null;
        try {
            r = new BufferedReader(
                    new InputStreamReader(
                            cl.getResourceAsStream("res/examples/" + resource),
                            "UTF-8"));
            textArea.read(r, null);
            r.close();
            textArea.setCaretPosition(0);
            textArea.discardAllEdits();

            refreshSourceTree();
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            textArea.setText("Type here to see syntax highlighting");
        }
    }
}