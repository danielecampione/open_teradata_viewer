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

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * The tabs holding the documents of the editor, one for each document, and the
 * document currently shown.
 * <p>
 * The window is told what happens through a {@link Host}: it is the one that
 * knows how a document is built, what has to be updated when another document
 * is shown and what to do with a document that is closed. The editor always
 * holds at least one document: closing the last one opens a new, empty, one.
 * <p>
 * All the methods but the ones reading the documents must be called from the
 * Swing event thread.
 *
 * @author D. Campione
 */
public class DocumentTabs {

    /** What the window provides to the tabs. */
    public interface Host {

        /**
         * @return A new document, ready to be shown, that is not part of the
         *         tabs yet.
         */
        EditorDocument createDocument();

        /**
         * Called after the document shown has changed, but not for the first
         * document added: the window shows it on its own when it starts.
         *
         * @param document The document now shown.
         */
        void documentShown(EditorDocument document);

        /**
         * Called before a document is closed, or before the application is
         * closed, for each of its documents.
         *
         * @param document The document to close.
         * @return <code>false</code> if the document must not be closed.
         */
        boolean confirmClose(EditorDocument document);

        /**
         * Called after a document has been closed.
         *
         * @param document The document closed.
         */
        void documentClosed(EditorDocument document);

        /**
         * Called when a document is shown whose file has been written after the
         * document read it, by saving another document in it: the text of the
         * document is not the one of the file anymore. The host asks the user
         * whether to load the file again, and does it if the answer is yes.
         *
         * @param document The document shown, which is the current one.
         * @return <code>true</code> if the document has been loaded again;
         *         <code>false</code> if its text has been kept, so that the
         *         document is shown as changed.
         */
        boolean reloadOutOfDate(EditorDocument document);

        /**
         * @return <code>true</code> if the documents cannot be changed right
         *         now, because a long running action is using the current one.
         */
        boolean isBusy();

        /**
         * @param document A document of the tabs.
         * @return The title to show in the tab of the document.
         */
        String getTitle(EditorDocument document);

        /**
         * @param document A document of the tabs.
         * @return The tool tip of the tab of the document, or
         *         <code>null</code>.
         */
        String getToolTip(EditorDocument document);

        /** @return The tool tip of the button closing a tab. */
        String getCloseToolTip();

        /**
         * @param document The document of the tab the user asked the menu of.
         * @return The menu to show, or <code>null</code> if there is none.
         */
        JPopupMenu createTabMenu(EditorDocument document);
    }

    private final Host host;
    private final JTabbedPane tabbedPane;
    // A snapshot, in the order of the tabs. It is replaced, never changed, so
    // that any thread can read it while the event thread changes the tabs
    private volatile List<EditorDocument> documents = Collections.emptyList();
    private final Map<EditorDocument, TabHeader> headers = new HashMap<>();

    private volatile EditorDocument current;
    private boolean updating;
    private boolean programmatic;
    // The host is asking what to do with a document that is going to be closed:
    // its tab is shown for that, and it must not be asked anything else
    private boolean confirmingClose;
    // The host is asking whether to load a file again
    private boolean askingToReload;

    // The title of a tab follows the state of its document
    private final PropertyChangeListener modifiedListener = event -> {
        if (EditorDocument.MODIFIED_PROPERTY.equals(event.getPropertyName())
                && event.getSource() instanceof EditorDocument) {
            final EditorDocument source = (EditorDocument) event.getSource();
            if (SwingUtilities.isEventDispatchThread()) {
                updateTitle(source);
            } else {
                SwingUtilities.invokeLater(() -> updateTitle(source));
            }
        }
    };

    /**
     * Creates empty tabs.
     *
     * @param host What the window provides to the tabs.
     */
    public DocumentTabs(Host host) {
        this.host = host;
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(e -> selectionChanged());
    }

    /** @return The component to add to the window. */
    public JComponent getComponent() {
        return tabbedPane;
    }

    /**
     * @return A snapshot of the documents, in the order of their tabs.
     */
    public List<EditorDocument> getDocuments() {
        return documents;
    }

    /**
     * Runs the given operation on every document.
     *
     * @param operation The operation to run.
     */
    public void forEach(Consumer<EditorDocument> operation) {
        for (EditorDocument document : documents) {
            operation.accept(document);
        }
    }

    /**
     * @return The document currently shown, or <code>null</code> if there is
     *         none yet.
     */
    public EditorDocument getCurrent() {
        return current;
    }

    /**
     * Adds a document, with its tab, after the existing ones. The first
     * document added is the current one, and the host is not told. A document
     * that is not the first one becomes the current one only if
     * <code>select</code> is <code>true</code>.
     *
     * @param document The document to add.
     * @param select Whether to show the document.
     */
    public void add(EditorDocument document, boolean select) {
        boolean first = documents.isEmpty();
        List<EditorDocument> added = new ArrayList<>(documents);
        added.add(document);
        documents = Collections.unmodifiableList(added);

        TabHeader header = new TabHeader(tabbedPane, component -> {
            EditorDocument toClose = EditorDocument.of(component);
            if (toClose != null) {
                close(toClose);
            }
        }, this::showTabMenu, this::dragTab, host.getTitle(document), host.getCloseToolTip());
        header.setToolTipText(host.getToolTip(document));
        headers.put(document, header);
        document.addPropertyChangeListener(modifiedListener);

        updating = first;
        try {
            tabbedPane.addTab(host.getTitle(document), document.getContentPanel());
            tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, header);
        } finally {
            updating = false;
        }
        if (first) {
            current = document;
        } else if (select) {
            select(document);
        }
    }

    /**
     * Shows a document. Unlike when the user selects a tab, this is possible
     * also while a long running action is using the current document: it is up
     * to the caller to know that it is safe.
     *
     * @param document The document to show.
     */
    public void select(EditorDocument document) {
        if (documents.contains(document)) {
            boolean previous = programmatic;
            programmatic = true;
            try {
                tabbedPane.setSelectedComponent(document.getContentPanel());
            } finally {
                programmatic = previous;
            }
        }
    }

    /**
     * @param file A file.
     * @return The document that was opened from the file, or <code>null</code>
     *         if there is none. If there are several (see
     *         {@link #findDocuments(File)}), the first one in the order of the
     *         tabs.
     */
    public EditorDocument findDocument(File file) {
        List<EditorDocument> found = findDocuments(file);
        return found.isEmpty() ? null : found.get(0);
    }

    /**
     * @param file A file.
     * @return The documents that hold the file, in the order of the tabs: more
     *         than one when the text of a document has been saved in a file
     *         that another document was already holding. The list is empty if
     *         there is none.
     */
    public List<EditorDocument> findDocuments(File file) {
        if (file == null) {
            return Collections.emptyList();
        }
        File wanted = normalize(file);
        List<EditorDocument> found = new ArrayList<>();
        for (EditorDocument document : documents) {
            File opened = document.getState().getOpenedFile();
            if (opened != null && normalize(opened).equals(wanted)) {
                found.add(document);
            }
        }
        return found;
    }

    private static File normalize(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException ioe) {
            return file.getAbsoluteFile();
        }
    }

    /**
     * Closes a document, after the host has confirmed it. If it is the only
     * one, a new empty document takes its place.
     *
     * @param document The document to close.
     * @return <code>false</code> if the document has not been closed.
     */
    public boolean close(EditorDocument document) {
        if (!documents.contains(document)) {
            return false;
        }
        if (host.isBusy()) {
            Toolkit.getDefaultToolkit().beep();
            return false;
        }
        if (!confirmClose(document)) {
            return false;
        }

        if (documents.size() == 1) {
            add(host.createDocument(), false);
        }
        int index = tabbedPane.indexOfComponent(document.getContentPanel());
        List<EditorDocument> remaining = new ArrayList<>(documents);
        remaining.remove(document);
        documents = Collections.unmodifiableList(remaining);
        headers.remove(document);
        document.removePropertyChangeListener(modifiedListener);
        // When the tab of the document shown is removed, another tab is
        // selected and the host is told, see selectionChanged()
        tabbedPane.removeTabAt(index);
        host.documentClosed(document);
        return true;
    }

    /**
     * Closes all the documents but one, asking the host about each of them. It
     * stops at the first one that is not closed.
     *
     * @param keep The document that stays, and is shown at the end.
     * @return <code>false</code> if some of the other documents have not been
     *         closed.
     */
    public boolean closeOthers(EditorDocument keep) {
        if (!documents.contains(keep)) {
            return false;
        }
        for (EditorDocument document : getDocuments()) {
            if (document != keep && !close(document)) {
                return false;
            }
        }
        select(keep);
        return true;
    }

    /**
     * Closes all the documents, asking the host about each of them. It stops at
     * the first one that is not closed. As the editor always holds a document,
     * what is left is a new empty one, unless the only document is empty
     * already.
     *
     * @return <code>false</code> if some documents have not been closed.
     */
    public boolean closeAll() {
        for (EditorDocument document : getDocuments()) {
            if (documents.size() == 1 && document.isPristine()) {
                break;
            }
            if (!close(document)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Asks the host about each document, in the order of their tabs, before the
     * application is closed. Nothing is closed here. It stops at the first one
     * the host does not agree to close.
     *
     * @return <code>false</code> if the application must not be closed.
     */
    public boolean confirmCloseAll() {
        for (EditorDocument document : getDocuments()) {
            if (!confirmClose(document)) {
                return false;
            }
        }
        return true;
    }

    private boolean confirmClose(EditorDocument document) {
        boolean previous = confirmingClose;
        confirmingClose = true;
        try {
            return host.confirmClose(document);
        } finally {
            confirmingClose = previous;
        }
    }

    /**
     * Tells that a file has been written, so that the documents holding it,
     * other than the one the text comes from, do not keep an old text
     * unnoticed: each of them is marked as {@link EditorDocument#isOutOfDate()
     * out of date}, and the host asks the user, when it is shown, whether to
     * load the file again. If one of them is the document shown, it is asked
     * at once.
     *
     * @param file The file that has been written.
     * @param source The document whose text, or part of it, has been written,
     *        or <code>null</code>. It is left as it is.
     */
    public void fileWritten(File file, EditorDocument source) {
        for (EditorDocument document : findDocuments(file)) {
            if (document != source) {
                document.setOutOfDate(true);
                if (document == current) {
                    SwingUtilities.invokeLater(() -> offerReload(document));
                }
            }
        }
    }

    /**
     * Lets the host ask whether to load again the file of a document that is
     * out of date. Nothing is asked if things have changed since the question
     * was scheduled (the document has been closed, or another one is shown) or
     * if the user is already being asked something else: the question is
     * repeated the next time the document is shown. If the user does not want
     * to load the file again, the text is kept and the document is marked as
     * changed, because it is not the one of the file anymore: closing it asks
     * whether to save it.
     */
    private void offerReload(EditorDocument document) {
        if (!document.isOutOfDate() || document != current || confirmingClose || askingToReload) {
            return;
        }
        document.setOutOfDate(false);
        boolean reloaded;
        askingToReload = true;
        try {
            reloaded = host.reloadOutOfDate(document);
        } finally {
            askingToReload = false;
        }
        if (!reloaded) {
            document.setModified(true);
        }
    }

    /**
     * Shows the document that is after the current one, or before it, going
     * round at the ends.
     *
     * @param delta 1 for the next document, -1 for the previous one.
     */
    public void selectRelative(int delta) {
        int count = tabbedPane.getTabCount();
        if (count < 2) {
            return;
        }
        if (host.isBusy()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        int index = ((tabbedPane.getSelectedIndex() + delta) % count + count) % count;
        EditorDocument document = EditorDocument.of(tabbedPane.getComponentAt(index));
        if (document != null) {
            select(document);
        }
    }

    /**
     * Moves the tab of a document to another position, which changes the order
     * of the documents too. The document that is shown stays the one shown.
     *
     * @param document The document to move.
     * @param newIndex The new position of its tab.
     * @return <code>false</code> if the tab has not been moved: the document is
     *         not in the tabs, the position does not exist or is the one it has.
     */
    public boolean move(EditorDocument document, int newIndex) {
        Component content = document.getContentPanel();
        int index = tabbedPane.indexOfComponent(content);
        if (index < 0 || newIndex < 0 || newIndex >= tabbedPane.getTabCount() || newIndex == index) {
            return false;
        }
        Component tabComponent = tabbedPane.getTabComponentAt(index);
        String title = tabbedPane.getTitleAt(index);
        String toolTip = tabbedPane.getToolTipTextAt(index);
        boolean wasSelected = tabbedPane.getSelectedIndex() == index;

        // The tabbed pane has no way to move a tab: it is removed and inserted
        // again. The selection changes meanwhile are not the user's, so they are
        // not told to the host
        updating = true;
        try {
            tabbedPane.removeTabAt(index);
            tabbedPane.insertTab(title, null, content, toolTip, newIndex);
            tabbedPane.setTabComponentAt(newIndex, tabComponent);
            EditorDocument shown = current;
            if (shown != null && tabbedPane.getSelectedComponent() != shown.getContentPanel()) {
                tabbedPane.setSelectedComponent(shown.getContentPanel());
            }
            List<EditorDocument> reordered = new ArrayList<>(documents);
            reordered.remove(document);
            reordered.add(newIndex, document);
            documents = Collections.unmodifiableList(reordered);
        } finally {
            updating = false;
        }
        if (wasSelected) {
            // Taking the tab out of the window made the text area lose the focus
            SwingUtilities.invokeLater(() -> document.getTextArea().requestFocusInWindow());
        }
        return true;
    }

    /**
     * Follows the mouse while a tab is dragged: when it gets past the middle of
     * the tab it is over, the dragged tab takes its place.
     * <p>
     * A tab is moved by taking it out of the tabbed pane and inserting it again,
     * and a component that is taken out loses the mouse it was following. So the
     * dragged tab is never moved: the tabs it goes past are moved the other way,
     * which comes to the same.
     *
     * @param content The component of the dragged tab.
     * @param point The position of the mouse, in the coordinates of the tabbed
     *        pane.
     */
    private void dragTab(Component content, Point point) {
        if (EditorDocument.of(content) == null || host.isBusy()) {
            return;
        }
        int index = tabbedPane.indexOfComponent(content);
        int over = tabbedPane.indexAtLocation(point.x, point.y);
        if (index < 0 || over < 0 || over == index) {
            return;
        }
        Rectangle bounds = tabbedPane.getBoundsAt(over);
        if (bounds == null) {
            return;
        }
        int middle = bounds.x + bounds.width / 2;
        if (over > index && point.x > middle) {
            for (int tab = index + 1; tab <= over; tab++) {
                move(EditorDocument.of(tabbedPane.getComponentAt(tab)), tab - 1);
            }
        } else if (over < index && point.x < middle) {
            for (int tab = index - 1; tab >= over; tab--) {
                move(EditorDocument.of(tabbedPane.getComponentAt(tab)), tab + 1);
            }
        }
    }

    private void showTabMenu(Component content, MouseEvent event) {
        EditorDocument document = EditorDocument.of(content);
        if (document == null) {
            return;
        }
        JPopupMenu menu = host.createTabMenu(document);
        if (menu != null) {
            menu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    /**
     * Updates the title and the tool tip of the tab of every document, for
     * instance after a file has been opened or the language has changed.
     */
    public void refreshTitles() {
        for (EditorDocument document : documents) {
            updateTitle(document);
            TabHeader header = headers.get(document);
            if (header != null) {
                header.setCloseToolTip(host.getCloseToolTip());
            }
        }
    }

    private void updateTitle(EditorDocument document) {
        TabHeader header = headers.get(document);
        if (header == null) {
            return;
        }
        String title = host.getTitle(document);
        header.setTitle(title);
        header.setToolTipText(host.getToolTip(document));
        int index = tabbedPane.indexOfComponent(document.getContentPanel());
        if (index >= 0) {
            tabbedPane.setTitleAt(index, title);
        }
    }

    private void selectionChanged() {
        if (updating) {
            return;
        }
        Component selectedComponent = tabbedPane.getSelectedComponent();
        EditorDocument selected = selectedComponent == null ? null : EditorDocument.of(selectedComponent);
        if (selected == null || selected == current) {
            return;
        }
        if (!programmatic && host.isBusy() && current != null && documents.contains(current)) {
            // The current document cannot be changed now: show it again
            Toolkit.getDefaultToolkit().beep();
            updating = true;
            try {
                tabbedPane.setSelectedComponent(current.getContentPanel());
            } finally {
                updating = false;
            }
            return;
        }
        current = selected;
        host.documentShown(selected);
        if (selected.isOutOfDate()) {
            // The question is asked once the tab has been shown completely
            SwingUtilities.invokeLater(() -> offerReload(selected));
        }
    }
}
