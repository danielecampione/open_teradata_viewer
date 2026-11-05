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

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.DocumentState;

/**
 * Looks whether the file of a document has been changed by another program
 * since the document read it or wrote it, see
 * {@link FileSnapshot#compare(File, FileSnapshot)}.
 * <p>
 * The files are accessed on a thread of its own, never on the Swing event
 * thread: a file on a network drive that cannot be reached would otherwise
 * freeze the interface every time a look is asked for. There is only one such
 * thread, and the requests that come while it is busy are merged instead of
 * queued up: a document waits for at most one look, the last one asked for, and
 * a request that is the same as the look being done is dropped.
 * <p>
 * What is found out is applied on the event thread, and only if it still holds.
 * The questions to the user are not asked here: the {@link Client} does it.
 *
 * @author D. Campione
 */
final class ExternalChangeChecker {

    /** What the checker needs from the tabs. Always called on the event thread. */
    interface Client {

        /**
         * @return <code>true</code> if the documents cannot be dealt with right
         *         now, because a long running action is using them.
         */
        boolean isBusy();

        /**
         * @param document A document.
         * @return <code>true</code> if the document is still in the tabs.
         */
        boolean isOpen(EditorDocument document);

        /**
         * Called when the file of a document has been found out to be changed.
         * The document has already taken note of what the file is like now, and
         * is marked as {@link EditorDocument#isOutOfDate() out of date}.
         *
         * @param document The document whose file is changed.
         */
        void changedOutside(EditorDocument document);
    }

    /** A look at the file of a document, as it was when it has been asked for. */
    private static final class Job {

        private final EditorDocument document;
        private final File file;
        private final FileSnapshot baseline;

        private Job(EditorDocument document, File file, FileSnapshot baseline) {
            this.document = document;
            this.file = file;
            this.baseline = baseline;
        }

        private boolean isSameAs(Job other) {
            return other != null && document == other.document && file.equals(other.file)
                    && baseline == other.baseline;
        }
    }

    private final Client client;

    private final Object lock = new Object();
    // Everything below is guarded by the lock
    private final Map<EditorDocument, Job> pending = new LinkedHashMap<>();
    // The look being done by the thread
    private Job running;
    // Whether the thread is started, and will go through what is pending
    private boolean scheduled;

    /**
     * @param client What the checker needs from the tabs.
     */
    ExternalChangeChecker(Client client) {
        this.client = client;
    }

    /**
     * Asks for a look at the file of a document, with what the document knows
     * of it right now. Nothing is done if the document has no file. Must be
     * called from the Swing event thread, which is the one that reads what the
     * document knows of its file; the look is done later, on another thread.
     *
     * @param document The document whose file is to be looked at.
     */
    void request(EditorDocument document) {
        DocumentState state = document.getState();
        File file = state.getOpenedFile();
        if (file == null) {
            return;
        }
        Job job = new Job(document, file, state.getFileSnapshot());
        boolean start = false;
        synchronized (lock) {
            if (job.isSameAs(running)) {
                // What is being done now is what is asked for
                return;
            }
            pending.put(document, job);
            if (!scheduled) {
                scheduled = true;
                start = true;
            }
        }
        if (start) {
            Thread thread = new Thread(this::drain, "File change check");
            thread.setDaemon(true);
            thread.start();
        }
    }

    /** Looks at the files asked for, until there are no more. */
    private void drain() {
        Job job = null;
        try {
            while ((job = next()) != null) {
                check(job);
            }
        } finally {
            if (job != null) {
                // The look has failed in a way that is not an unreadable file:
                // later requests have to be able to start the thread again
                synchronized (lock) {
                    running = null;
                    scheduled = false;
                }
            }
        }
    }

    private Job next() {
        synchronized (lock) {
            Iterator<Job> iterator = pending.values().iterator();
            if (!iterator.hasNext()) {
                running = null;
                scheduled = false;
                return null;
            }
            running = iterator.next();
            iterator.remove();
            return running;
        }
    }

    private void check(Job job) {
        final FileSnapshot.Comparison comparison = FileSnapshot.compare(job.file, job.baseline);
        FileSnapshot.Outcome outcome = comparison.getOutcome();
        if (outcome == FileSnapshot.Outcome.UNCHANGED || outcome == FileSnapshot.Outcome.UNAVAILABLE) {
            // Nothing to do: if the file cannot be read now, it is looked at
            // again the next time it is asked for
            return;
        }
        SwingUtilities.invokeLater(() -> apply(job, comparison));
    }

    /**
     * Applies what a look found out. It runs on the event thread, some time
     * after the look was asked for, and it does nothing if
     * <ul>
     * <li>the document is not the one that was asked for anymore: it has been
     * closed, or it holds another file, or what it knows of its file is not
     * what it was (the text has been loaded again or saved, or the file has been
     * written by the application), so what has been found is out of date;</li>
     * <li>an action is running: it may have written the file of the document
     * without having told it yet, and that would be taken for a change made by
     * another program. The look is just asked for again the next time.</li>
     * </ul>
     * A file that has been touched, or that the document knew nothing about, is
     * taken note of without telling anyone. A file with a different content is
     * the case the user has to be asked about.
     */
    private void apply(Job job, FileSnapshot.Comparison comparison) {
        EditorDocument document = job.document;
        DocumentState state = document.getState();
        if (!client.isOpen(document) || !job.file.equals(state.getOpenedFile())
                || state.getFileSnapshot() != job.baseline || client.isBusy()) {
            return;
        }
        // The document takes note at once, so that the same change is not found
        // again, whatever the user answers
        state.setFileSnapshot(comparison.getCurrent());
        if (comparison.getOutcome() == FileSnapshot.Outcome.MODIFIED) {
            document.setOutOfDate(true);
            client.changedOutside(document);
        }
    }
}
