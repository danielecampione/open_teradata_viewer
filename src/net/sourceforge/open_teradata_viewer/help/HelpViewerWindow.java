/*
 * Open Teradata Viewer ( help )
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

package net.sourceforge.open_teradata_viewer.help;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ImageManager;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.util.FileWrapperFactoryImpl;
import net.sourceforge.open_teradata_viewer.util.IFileWrapper;
import net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class HelpViewerWindow extends JFrame {

    private static final long serialVersionUID = 8870795705401546865L;

    /** Tree containing a node for each help document. */
    private JTree _tree;

    /** Panel that displays the help document. */
    private HtmlViewerPanel _detailPnl;

    /** Home URL. */
    private URL _homeURL;

    /** Collection of the nodes in the tree keyed by the URL.toString(). */
    private final Map<String, DefaultMutableTreeNode> _nodes = new HashMap<String, DefaultMutableTreeNode>();

    /**
     * Factory for creating FileWrappers which insulate the application from
     * direct reference to File.
     */
    private IFileWrapperFactory iFileWrapperFactory = new FileWrapperFactoryImpl();

    /**
     * A IFileWrapper-enhanced version of HelpFiles that removes direct
     * references to File.
     */
    private IHelpFileWrappers helpFiles = new HelpFileWrappersImpl();

    /**
     * Ctor.
     *
     * @throws IllegalArgumentException Thrown if <TT>null</TT>
     *         <TT>IApplication</TT> passed.
     */
    public HelpViewerWindow() throws IOException {
        super(Main.APPLICATION_NAME + " ( Help )");
        createGUI();
    }

    /** @param iFileWrapperFactory the iFileWrapperFactory to set. */
    public void setFileWrapperFactory(IFileWrapperFactory iFileWrapperFactory) {
        Utilities.checkNull("setFileWrapperFactory", "iFileWrapperFactory",
                iFileWrapperFactory);
        this.iFileWrapperFactory = iFileWrapperFactory;
    }

    /** @param helpFiles the helpFiles to set. */
    public void setHelpFiles(IHelpFileWrappers helpFiles) {
        Utilities.checkNull("setHelpFiles", "helpFiles", helpFiles);
        this.helpFiles = helpFiles;
    }

    /**
     * Set the Document displayed to that defined by the passed URL.
     *
     * @param url URL of document to be displayed.
     */
    private void setSelectedDocument(URL url) {
        try {
            _detailPnl.gotoURL(url);
        } catch (IOException ioe) {
            ExceptionDialog.ignoreException(ioe);
        }
    }

    private void selectTreeNodeForURL(URL url) {
        // Strip local part of URL
        String key = url.toString();
        final int idx = key.lastIndexOf('#');
        if (idx > -1) {
            key = key.substring(0, idx);
        }
        DefaultMutableTreeNode node = _nodes.get(key);
        if (node != null) {
            DefaultTreeModel model = (DefaultTreeModel) _tree.getModel();
            TreePath path = new TreePath(model.getPathToRoot(node));
            if (path != null) {
                _tree.expandPath(path);
                _tree.scrollPathToVisible(path);
                _tree.setSelectionPath(path);
            }
        }
    }

    /** Create user interface. */
    private void createGUI() throws IOException {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final ImageIcon icon = ImageManager.getImage("/icons/help.png");

        if (icon != null) {
            setIconImage(icon.getImage());
        }

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.add(createContentsTree(), JSplitPane.LEFT);
        splitPane.add(createDetailsPanel(), JSplitPane.RIGHT);
        contentPane.add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(200);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screen.width * 0.8);
        int windowHeight = (int) (screen.height * 0.8);
        setSize(new Dimension(windowWidth, windowHeight));
        setLocationRelativeTo(null);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                _detailPnl.setHomeURL(_homeURL);
                _tree.expandRow(0);
                _tree.expandRow(2);
                _tree.setRootVisible(false);
            }
        });

        _detailPnl.addListener(new IHtmlViewerPanelListener() {
            public void currentURLHasChanged(HtmlViewerPanelListenerEvent evt) {
                selectTreeNodeForURL(evt.getHtmlViewerPanel().getURL());
            }
            public void homeURLHasChanged(HtmlViewerPanelListenerEvent evt) {
                // Nothing to do
            }
        });
    }

    /**
     * Create a tree each node being a link to a document.
     *
     * @return  The contents tree.
     */
    private JScrollPane createContentsTree() throws IOException {
        final FolderNode root = new FolderNode("Help");
        _tree = new JTree(new DefaultTreeModel(root));
        _tree.setShowsRootHandles(true);
        _tree.addTreeSelectionListener(new ObjectTreeSelectionListener());

        // Renderer for tree
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(ImageManager.getImage("/icons/help_topic.png"));
        renderer.setOpenIcon(ImageManager.getImage("/icons/help_toc_open.png"));
        renderer.setClosedIcon(ImageManager
                .getImage("/icons/help_toc_closed.png"));
        _tree.setCellRenderer(renderer);

        // Add Help, Licence and Change Log nodes to the tree
        final FolderNode helpRoot = new FolderNode("Help");
        root.add(helpRoot);
        _nodes.put(helpRoot.getURL().toString(), helpRoot);
        final FolderNode licenceRoot = new FolderNode("Licences");
        root.add(licenceRoot);
        _nodes.put(licenceRoot.getURL().toString(), licenceRoot);
        final FolderNode changeLogRoot = new FolderNode("ChangeLog");
        root.add(changeLogRoot);
        _nodes.put(changeLogRoot.getURL().toString(), changeLogRoot);

        // Add the Manual node
        IFileWrapper file = helpFiles.getQuickStartGuideFile();
        try {
            DocumentNode dn = new DocumentNode("Guide", file);
            helpRoot.add(dn);
            _homeURL = dn.getURL();
            _nodes.put(_homeURL.toString(), dn);
        } catch (MalformedURLException murle) {
            String msg = file.getAbsolutePath();
            String errorMsg = "Load help file not found.\n" + msg;
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(errorMsg,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(errorMsg);
        }

        // Add the Licence node
        file = helpFiles.getLicenceFile();
        try {
            DocumentNode dn = new DocumentNode(
                    "GNU General Public License (GPL)", file);
            licenceRoot.add(dn);
            _nodes.put(dn.getURL().toString(), dn);
        } catch (MalformedURLException murle) {
            String msg = file.getAbsolutePath();
            String errorMsg = "Load license file not found." + msg;
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(errorMsg,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(errorMsg);
        }

        // Add the Change Log node
        file = helpFiles.getChangeLogFile();
        try {
            DocumentNode dn = new DocumentNode("What's new?", file);
            changeLogRoot.add(dn);
            _nodes.put(dn.getURL().toString(), dn);
        } catch (MalformedURLException murle) {
            String msg = file.getAbsolutePath();
            String errorMsg = "Load change log file not found.\n" + msg;
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(errorMsg,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(errorMsg);
        }

        // FAQ
        file = helpFiles.getFAQFile();
        try {
            DocumentNode dn = new DocumentNode("FAQ", file);
            root.add(dn);
            _nodes.put(dn.getURL().toString(), dn);
        } catch (MalformedURLException murle) {
            String msg = file.getAbsolutePath();
            String errorMsg = "Load FAQ file not found.\n" + msg;
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(errorMsg,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(errorMsg);
        }

        // Generate contents file
        helpRoot.generateContentsFile();
        licenceRoot.generateContentsFile();
        changeLogRoot.generateContentsFile();

        JScrollPane sp = new JScrollPane(_tree);
        sp.setPreferredSize(new Dimension(200, 200));

        return sp;
    }
    HtmlViewerPanel createDetailsPanel() {
        _detailPnl = new HtmlViewerPanel(null);
        return _detailPnl;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class DocumentNode extends DefaultMutableTreeNode {

        private static final long serialVersionUID = -7798649742879211917L;

        private URL _url;

        DocumentNode(String title, IFileWrapper file)
                throws MalformedURLException {
            super(title, false);
            setFile(file);
        }

        DocumentNode(String title, boolean allowsChildren) {
            super(title, allowsChildren);
        }

        URL getURL() {
            return _url;
        }

        void setFile(IFileWrapper file) throws MalformedURLException {
            _url = file.toURI().toURL();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class FolderNode extends DocumentNode {

        private static final long serialVersionUID = -6574834940741510025L;

        private final List<String> _docTitles = new ArrayList<String>();
        private final List<URL> _docURLs = new ArrayList<URL>();
        private final IFileWrapper _contentsFile;

        FolderNode(String title) throws IOException {
            super(title, true);
            _contentsFile = iFileWrapperFactory.createTempFile("otvhelp",
                    "html");
            _contentsFile.deleteOnExit();
            setFile(_contentsFile);
        }

        public void add(MutableTreeNode node) {
            super.add(node);
            if (node instanceof DocumentNode) {
                final DocumentNode dn = (DocumentNode) node;
                final URL docURL = dn.getURL();
                if (docURL != null) {
                    String docTitle = dn.toString();
                    if (StringUtil.isEmpty(docTitle)) {
                        docTitle = docURL.toExternalForm();
                    }
                    _docTitles.add(docTitle);
                    _docURLs.add(docURL);
                }
            }
        }
        synchronized void generateContentsFile() {
            try {
                final PrintWriter pw = new PrintWriter(
                        _contentsFile.getFileWriter());
                try {
                    StringBuffer buf = new StringBuffer(50);
                    buf.append("<HTML><BODY bgcolor=\"#FFFFFF\"><H1>")
                            .append(toString()).append("</H1>");
                    pw.println(buf.toString());
                    for (int i = 0, limit = _docTitles.size(); i < limit; ++i) {
                        final URL docUrl = _docURLs.get(i);
                        buf = new StringBuffer(50);
                        buf.append("<A HREF=\"").append(docUrl).append("\">")
                                .append(_docTitles.get(i)).append("</A><BR>");
                        pw.println(buf.toString());
                    }
                    pw.println("</BODY></HTML");
                } finally {
                    pw.close();
                }
            } catch (IOException ioe) {
                String errorMsg = "Error: congen.";
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(errorMsg,
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                UISupport.getDialogs().showErrorMessage(errorMsg);
            }
        }
    }

    /**
     * This class listens for changes in the node selected in the tree and
     * displays the appropriate help document for the node.
     * 
     * @author D. Campione
     * 
     */
    private final class ObjectTreeSelectionListener
            implements
                TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent evt) {
            final TreePath path = evt.getNewLeadSelectionPath();
            if (path != null) {
                Object lastComp = path.getLastPathComponent();
                if (lastComp instanceof DocumentNode) {
                    setSelectedDocument(((DocumentNode) lastComp).getURL());
                }
            }
        }
    }
}