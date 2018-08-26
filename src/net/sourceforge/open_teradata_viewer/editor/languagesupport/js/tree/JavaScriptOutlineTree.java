/*
 * Open Teradata Viewer ( editor language support js tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractSourceTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.LanguageSupportFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.DocumentRange;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;

import org.mozilla.javascript.ast.AstRoot;

/**
 * A tree view showing the outline of JavaScript source, similar to the
 * "Outline" view in the Eclipse JDT. It also uses Eclipse's icons, just like
 * the rest of this code completion library.<p>
 *
 * You can get this tree automatically updating in response to edits in an
 * <code>SyntaxTextArea</code> with {@link JavaScriptLanguageSupport} installed
 * by calling {@link #listenTo(SyntaxTextArea)}. Note that, if you have an
 * application with multiple STA editors, you would want to call this method
 * each time a new editor is focused.
 *
 * @author D. Campione
 *
 */
public class JavaScriptOutlineTree extends AbstractSourceTree {

    private static final long serialVersionUID = -5419628317051527956L;

    private DefaultTreeModel model;
    private SyntaxTextArea textArea;
    private JavaScriptParser parser;
    private Listener listener;

    static final int PRIORITY_FUNCTION = 1;
    static final int PRIORITY_VARIABLE = 2;

    /**
     * Ctor. The tree created will not have its elements sorted alphabetically.
     */
    public JavaScriptOutlineTree() {
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
    public JavaScriptOutlineTree(boolean sorted) {
        setSorted(sorted);
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        setRootVisible(false);
        setCellRenderer(new JavaScriptTreeCellRenderer());
        model = new DefaultTreeModel(new DefaultMutableTreeNode("Nothing"));
        setModel(model);
        listener = new Listener();
        addTreeSelectionListener(listener);
    }

    /** Refreshes listeners on the text area when its syntax style changes. */
    private void checkForJavaScriptParsing() {
        // Remove possible listener on old Java parser (in case they're just
        // changing syntax style away from Java)
        if (parser != null) {
            parser.removePropertyChangeListener(JavaScriptParser.PROPERTY_AST,
                    listener);
            parser = null;
        }

        // Get the Java language support (shared by all STA instances editing
        // Java that were registered with the LanguageSupportFactory)
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        ILanguageSupport support = lsf
                .getSupportFor(ISyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        JavaScriptLanguageSupport jls = (JavaScriptLanguageSupport) support;

        // Listen for re-parsing of the editor and update the tree accordingly
        parser = jls.getParser(textArea);
        if (parser != null) { // Should always be true
            parser.addPropertyChangeListener(JavaScriptParser.PROPERTY_AST,
                    listener);
            // Populate with any already-existing AST
            AstRoot ast = parser.getAstRoot();
            update(ast);
        } else {
            update((AstRoot) null); // Clear the tree
        }
    }

    /** {@inheritDoc} */
    @Override
    public void expandInitialNodes() {
        // First, collapse all rows
        int j = 0;
        while (j < getRowCount()) {
            collapseRow(j++);
        }

        // Expand only functions
        expandRow(0);
        j = 1;
        while (j < getRowCount()) {
            TreePath path = getPathForRow(j);
            expandPath(path);
            j++;
        }
    }

    private void gotoElementAtPath(TreePath path) {
        Object node = path.getLastPathComponent();
        if (node instanceof JavaScriptTreeNode) {
            JavaScriptTreeNode jstn = (JavaScriptTreeNode) node;
            int len = jstn.getLength();
            if (len > -1) { // Should always be true
                int offs = jstn.getOffset();
                DocumentRange range = new DocumentRange(offs, offs + len);
                SyntaxUtilities.selectAndPossiblyCenter(textArea, range, true);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean gotoSelectedElement() {
        TreePath path = getLeadSelectionPath(); // e.getNewLeadSelectionPath();
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

        // Check whether we're currently editing JavaScript
        checkForJavaScriptParsing();
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall() {
        if (parser != null) {
            parser.removePropertyChangeListener(JavaScriptParser.PROPERTY_AST,
                    listener);
            parser = null;
        }

        if (textArea != null) {
            textArea.removePropertyChangeListener(
                    SyntaxTextArea.SYNTAX_STYLE_PROPERTY, listener);
            textArea = null;
        }
    }

    /**
     * Refreshes this tree.
     *
     * @param ast The AST. If this is <code>null</code> then the tree is
     *        cleared.
     */
    private void update(AstRoot ast) {
        JavaScriptOutlineTreeGenerator generator = new JavaScriptOutlineTreeGenerator(
                textArea, ast);
        JavaScriptTreeNode root = generator.getTreeRoot();
        model.setRoot(root);
        root.setSorted(isSorted());
        refresh();
    }

    /** Overridden to also update the UI of the child cell renderer. */
    @Override
    public void updateUI() {
        super.updateUI();
        // DefaultTreeCellRenderer caches colors, so we can't just call
        // ((JComponent)getCellRenderer()).updateUI()..
        setCellRenderer(new JavaScriptTreeCellRenderer());
    }

    /**
     * Listens for events this tree is interested in (events in the associated
     * editor, for example), as well as events in this tree.
     *
     * @author D. Campione
     *
     */
    private class Listener implements PropertyChangeListener,
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
                checkForJavaScriptParsing();
            } else if (JavaScriptParser.PROPERTY_AST.equals(name)) {
                AstRoot ast = (AstRoot) e.getNewValue();
                update(ast);
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