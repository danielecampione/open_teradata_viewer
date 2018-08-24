/*
 * Open Teradata Viewer ( editor language support xml tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractSourceTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.LanguageSupportFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.XmlLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.XmlParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * A tree view showing the outline of XML, similar to the "Outline" view in
 * Eclipse. It also uses Eclipse's icons, just like the rest of this code
 * completion library.<p>
 *
 * You can get this tree automatically updating in response to edits in an
 * <code>SyntaxTextArea</code> with {@link XmlLanguageSupport} installed by
 * calling {@link #listenTo(SyntaxTextArea)}. Note that an instance of this
 * class can only listen to a single editor at a time, so if your application
 * contains multiple instances of SyntaxTextArea, you'll either need a separate
 * <code>XmlOutlineTree</code> for each one or call <code>uninstall()</code> and
 * <code>listenTo(SyntaxTextArea)</code> each time a new STA receives focus.
 * 
 * @author D. Campione
 * 
 */
public class XmlOutlineTree extends AbstractSourceTree {

    private static final long serialVersionUID = 6789560460515003310L;

    private XmlParser parser;
    private XmlEditorListener listener;
    private DefaultTreeModel model;
    private XmlTreeCellRenderer xmlTreeCellRenderer;

    /**
     * Ctor. The tree created will not have its elements sorted alphabetically.
     */
    public XmlOutlineTree() {
        this(false);
    }

    /**
     * Ctor.
     *
     * @param sorted Whether the tree should sort its elements alphabetically.
     *        Note that outline trees will likely group nodes by type before
     *        sorting (i.e. methods will be sorted in one group, fields in
     *        another group, etc..).
     */
    public XmlOutlineTree(boolean sorted) {
        setSorted(sorted);
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        setRootVisible(false);
        xmlTreeCellRenderer = new XmlTreeCellRenderer();
        setCellRenderer(xmlTreeCellRenderer);
        model = new DefaultTreeModel(new XmlTreeNode("Nothing"));
        setModel(model);
        listener = new XmlEditorListener();
        addTreeSelectionListener(listener);
    }

    /** Refreshes listeners on the text area when its syntax style changes. */
    private void checkForXmlParsing() {
        // Remove possible listener on old Java parser (in case they're just
        // changing syntax style AWAY from Java)
        if (parser != null) {
            parser.removePropertyChangeListener(XmlParser.PROPERTY_AST,
                    listener);
            parser = null;
        }

        // Get the Java language support (shared by all STA instances editing
        // Java that were registered with the LanguageSupportFactory)
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        ILanguageSupport support = lsf
                .getSupportFor(ISyntaxConstants.SYNTAX_STYLE_XML);
        XmlLanguageSupport xls = (XmlLanguageSupport) support;

        // Listen for re-parsing of the editor and update the tree accordingly
        parser = xls.getParser(textArea);
        if (parser != null) { // Should always be true
            parser.addPropertyChangeListener(XmlParser.PROPERTY_AST, listener);
            // Populate with any already-existing AST
            XmlTreeNode root = parser.getAst();
            update(root);
        } else {
            update((XmlTreeNode) null); // Clear the tree
        }
    }

    /** {@inheritDoc} */
    @Override
    public void expandInitialNodes() {
        fastExpandAll(new TreePath(getModel().getRoot()), true);
    }

    private void gotoElementAtPath(TreePath path) {
        Object node = path.getLastPathComponent();
        if (node instanceof XmlTreeNode) {
            XmlTreeNode xtn = (XmlTreeNode) node;
            textArea.select(xtn.getStartOffset(), xtn.getEndOffset());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean gotoSelectedElement() {
        TreePath path = getLeadSelectionPath();
        if (path != null) {
            gotoElementAtPath(path);
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void listenTo(SyntaxTextArea textArea) {
        if (this.textArea != null) {
            uninstall();
        }

        // Nothing new to listen to
        if (textArea == null) {
            return;
        }

        // Listen for future language changes in the text editor
        this.textArea = textArea;
        textArea.addPropertyChangeListener(
                SyntaxTextArea.SYNTAX_STYLE_PROPERTY, listener);

        checkForXmlParsing();
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall() {
        if (parser != null) {
            parser.removePropertyChangeListener(XmlParser.PROPERTY_AST,
                    listener);
            parser = null;
        }

        if (textArea != null) {
            textArea.removePropertyChangeListener(
                    SyntaxTextArea.SYNTAX_STYLE_PROPERTY, listener);
            textArea = null;
        }
    }

    private void update(XmlTreeNode root) {
        if (root != null) {
            root = (XmlTreeNode) root.cloneWithChildren();
        }
        model.setRoot(root);
        if (root != null) {
            root.setSorted(isSorted());
        }
        refresh();
    }

    /** Overridden to update the cell renderer. */
    @Override
    public void updateUI() {
        super.updateUI();
        xmlTreeCellRenderer = new XmlTreeCellRenderer();
        setCellRenderer(xmlTreeCellRenderer); // So it picks up new LAF's properties
    }

    /**
     * Listens for events this tree is interested in (events in the associated
     * editor, for example), as well as events in this tree.
     * 
     * @author D. Campione
     * 
     */
    private class XmlEditorListener implements PropertyChangeListener,
            TreeSelectionListener {

        /**
         * Called whenever the text area's syntax style changes, as well as when
         * it is re-parsed.
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();

            // If the text area is changing the syntax style it is editing
            if (SyntaxTextArea.SYNTAX_STYLE_PROPERTY.equals(name)) {
                checkForXmlParsing();
            } else if (XmlParser.PROPERTY_AST.equals(name)) {
                XmlTreeNode root = (XmlTreeNode) e.getNewValue();
                update(root);
            }
        }

        /**
         * Selects the corresponding element in the text editor when a user
         * clicks on a node in this tree.
         */
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if (getGotoSelectedElementOnClick()) {
                TreePath newPath = e.getNewLeadSelectionPath();
                if (newPath != null) {
                    gotoElementAtPath(newPath);
                }
            }
        }
    }
}