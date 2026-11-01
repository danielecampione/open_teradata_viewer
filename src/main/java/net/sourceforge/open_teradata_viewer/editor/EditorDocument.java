/*
 * Open Teradata Viewer ( editor )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreeNode;

import net.sourceforge.open_teradata_viewer.DocumentState;
import net.sourceforge.open_teradata_viewer.ResultSetTable;

import org.fife.rsta.ac.AbstractSourceTree;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.tree.JavaOutlineTree;
import org.fife.rsta.ac.js.tree.JavaScriptOutlineTree;
import org.fife.rsta.ac.xml.tree.XmlOutlineTree;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.ErrorStrip;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * A document of the editor: groups everything that belongs to one open text,
 * that is its text area, the scroll pane with the gutter, the area showing the
 * source tree, the error strip and the auto-completion installed on it, plus
 * its {@link DocumentState state} and the table showing its results.
 * <p>
 * The window creates the documents and keeps track of the one currently shown.
 * Keeping these parts together, instead of as separate members of the window,
 * is what allows the editor to hold more than one document.
 *
 * @author D. Campione
 */
public class EditorDocument {

    /**
     * The name of the property that tells whether the text of the document has
     * been changed, see {@link #isModified()}.
     */
    public static final String MODIFIED_PROPERTY = "modified";

    private final OTVSyntaxTextArea textArea;
    private final RTextScrollPane scrollPane;
    private final JScrollPane treeScrollPane;
    private final JComponent component;
    private final ErrorStrip errorStrip;
    private final DocumentState state;
    private final ResultSetTable resultSetTable;
    private final JScrollPane resultScrollPane;
    private final JPanel contentPanel;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private volatile boolean modified;
    private volatile boolean outOfDate;
    private int number;
    private AutoCompletion autoCompletion;
    private AbstractSourceTree tree;

    /**
     * Creates a document out of its already built parts.
     *
     * @param textArea The text area holding the text.
     * @param scrollPane The scroll pane containing the text area.
     * @param treeScrollPane The scroll pane showing the source tree.
     * @param component The component to add to the window, which lays out the
     *        source tree area next to the scroll pane of the text area.
     * @param errorStrip The error strip showing the markers of the text area.
     * @param state The file, the query and the results held by the document.
     * @param resultSetTable The table showing the results of the document.
     * @param resultScrollPane The scroll pane containing the results table.
     */
    public EditorDocument(OTVSyntaxTextArea textArea, RTextScrollPane scrollPane, JScrollPane treeScrollPane,
                          JComponent component, ErrorStrip errorStrip, DocumentState state, ResultSetTable resultSetTable,
                          JScrollPane resultScrollPane) {
        this.textArea = textArea;
        this.scrollPane = scrollPane;
        this.treeScrollPane = treeScrollPane;
        this.component = component;
        this.errorStrip = errorStrip;
        this.state = state;
        this.resultSetTable = resultSetTable;
        this.resultScrollPane = resultScrollPane;

        // What is shown in the tab of the document: the text next to its error
        // strip
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.add(errorStrip, BorderLayout.LINE_END);
        contentPanel.putClientProperty(EditorDocument.class, this);

        // Any change of the text, typed, pasted or set by the application, makes
        // the document modified
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Only the attributes of the text changed, not the text
            }
        });
    }

    private void textChanged() {
        if (!modified) {
            setModified(true);
        }
    }

    /**
     * @param component A component, possibly <code>null</code>.
     * @return The document whose {@link #getContentPanel() content} is the
     *         given component, or <code>null</code> if it is not the content of
     *         a document.
     */
    public static EditorDocument of(Component component) {
        if (component instanceof JComponent) {
            Object document = ((JComponent) component).getClientProperty(EditorDocument.class);
            if (document instanceof EditorDocument) {
                return (EditorDocument) document;
            }
        }
        return null;
    }

    /** @return The text area holding the text of this document. */
    public OTVSyntaxTextArea getTextArea() {
        return textArea;
    }

    /** @return The scroll pane containing the text area. */
    public RTextScrollPane getScrollPane() {
        return scrollPane;
    }

    /** @return The scroll pane showing the source tree. */
    public JScrollPane getTreeScrollPane() {
        return treeScrollPane;
    }

    /**
     * @return The component to add to the window: the source tree area laid
     *         out next to the scroll pane of the text area.
     */
    public JComponent getComponent() {
        return component;
    }

    /** @return The error strip showing the markers of the text area. */
    public ErrorStrip getErrorStrip() {
        return errorStrip;
    }

    /**
     * @return What is shown in the tab of the document: the text, with the
     *         source tree next to it, and its error strip.
     */
    public JPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * @return The number of the document: it tells apart the documents that are
     *         not saved in a file.
     */
    public int getNumber() {
        return number;
    }

    /** @param number The number of the document, see {@link #getNumber()}. */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return <code>true</code> if the text of the document has been changed
     *         since the document was created, or since it was last opened from
     *         a file or saved in one.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Sets whether the text of the document has been changed, for instance to
     * clear it after the text has been saved or loaded from a file. The
     * listeners are told when the value changes.
     *
     * @param modified The new value.
     */
    public void setModified(boolean modified) {
        boolean old = this.modified;
        this.modified = modified;
        propertyChangeSupport.firePropertyChange(MODIFIED_PROPERTY, old, modified);
    }

    /**
     * @return <code>true</code> if the file of the document has been written
     *         after the document read it or wrote it, because the text of
     *         another document has been saved in it: the text of this document
     *         is not the one of the file anymore.
     */
    public boolean isOutOfDate() {
        return outOfDate;
    }

    /**
     * @param outOfDate Whether the file of the document has been written after
     *        the document read it or wrote it, see {@link #isOutOfDate()}.
     */
    public void setOutOfDate(boolean outOfDate) {
        this.outOfDate = outOfDate;
    }

    /**
     * @return <code>true</code> if the document has nothing in it: it has no
     *         text, it was not opened from a file and it has not been changed.
     *         A file can be opened in such a document, instead of in a new one.
     */
    public boolean isPristine() {
        return !modified && state.getOpenedFile() == null && textArea.getDocument().getLength() == 0;
    }

    /** @param listener A listener of the changes of {@link #MODIFIED_PROPERTY}. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /** @param listener The listener to remove. */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /** @return The file, the query and the results held by this document. */
    public DocumentState getState() {
        return state;
    }

    /** @return The table showing the results of this document. */
    public ResultSetTable getResultSetTable() {
        return resultSetTable;
    }

    /** @return The scroll pane containing the results table. */
    public JScrollPane getResultScrollPane() {
        return resultScrollPane;
    }

    /**
     * @return The auto-completion installed on the text area, or
     *         <code>null</code> if none has been installed yet.
     */
    public AutoCompletion getAutoCompletion() {
        return autoCompletion;
    }

    /**
     * Remembers the auto-completion installed on the text area.
     *
     * @param autoCompletion The auto-completion installed on the text area.
     */
    public void setAutoCompletion(AutoCompletion autoCompletion) {
        this.autoCompletion = autoCompletion;
    }

    /**
     * Displays a tree view of the source code of this document, if available
     * for its current programming language.
     */
    public void refreshSourceTree() {
        if (tree != null) {
            tree.uninstall();
        }

        String language = textArea.getSyntaxEditingStyle();
        if (SyntaxConstants.SYNTAX_STYLE_JAVA.equals(language)) {
            tree = new JavaOutlineTree();
        } else if (SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT.equals(language)) {
            tree = new JavaScriptOutlineTree();
        } else if (SyntaxConstants.SYNTAX_STYLE_XML.equals(language)) {
            tree = new XmlOutlineTree();
        } else {
            tree = null;
        }

        if (tree != null) {
            tree.listenTo(textArea);
            treeScrollPane.setViewportView(tree);
        } else {
            JTree dummy = new JTree((TreeNode) null);
            treeScrollPane.setViewportView(dummy);
        }
        treeScrollPane.revalidate();
    }

    /**
     * Releases what the document holds, when it is closed: the source tree, the
     * auto-completion, the language support of the text area and the results of
     * the last query, which closes the statement that produced them.
     */
    public void dispose() {
        disposeSourceTree();
        if (autoCompletion != null) {
            autoCompletion.uninstall();
        }
        LanguageSupportFactory.get().unregister(textArea);
        ToolTipManager.sharedInstance().unregisterComponent(textArea);
        state.setResultSet(null);
    }

    /** Detaches the source tree, if any, from the text area. */
    public void disposeSourceTree() {
        if (tree != null) {
            tree.uninstall();
            treeScrollPane.remove(tree);
        }
    }
}
