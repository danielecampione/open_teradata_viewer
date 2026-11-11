/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import net.sourceforge.open_teradata_viewer.editor.EditorDocument;
import net.sourceforge.open_teradata_viewer.editor.FileSnapshot;
import net.sourceforge.open_teradata_viewer.editor.FileSyntaxStyle;
import net.sourceforge.open_teradata_viewer.editor.TextFileCodec;
import net.sourceforge.open_teradata_viewer.editor.WebPageBuilder;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 *
 *
 * @author D. Campione
 *
 */
public class FileIO {

    private static JFileChooser fileChooser;

    private static AcceptAllFileFilter acceptAllFileFilter;

    static {
        acceptAllFileFilter = new AcceptAllFileFilter();
    }

    private FileIO() {
    }

    /**
     * Saves some bytes in a file chosen by the user and then asks whether to
     * open it with the application associated to it. The tabs holding the file
     * are told that it has been written (see
     * {@link #fileWritten(File, EditorDocument)}) after that question, so that
     * the two do not ask the user something at the same time.
     *
     * @param fileName The name suggested to the user.
     * @param bytes The content of the file.
     * @throws Exception If the file cannot be written.
     */
    public static void saveAndOpenFile(String fileName, byte[] bytes) throws Exception {
        File file = chooseAndWriteFile(fileName, bytes);
        if (file == null) {
            return;
        }
        try {
            LanguageManager langManager = LanguageManager.getInstance();
            if (Dialog.YES_OPTION == Dialog.show(langManager.getString("dialog.open_file"),
                    langManager.getString("dialog.open_file.associated_application"), Dialog.QUESTION_MESSAGE,
                    Dialog.YES_NO_OPTION)) {
                FileIO.openFile(file, true);
            }
        } finally {
            fileWritten(file, null);
        }
    }

    /**
     * Saves some bytes in a file chosen by the user, exactly as they are, also
     * when the file is a web page: the bytes are not the text of a document,
     * see {@link #saveText(String, EditorDocument, boolean)}. The tabs holding
     * the file are told that it has been written, see
     * {@link #fileWritten(File, EditorDocument)}.
     *
     * @param fileName The name suggested to the user.
     * @param bytes The content of the file.
     * @return The file that has been written, or <code>null</code> if the user
     *         gave up.
     * @throws Exception If the file cannot be written.
     */
    public static File saveFile(String fileName, byte[] bytes) throws Exception {
        File file = chooseAndWriteFile(fileName, bytes);
        if (file != null) {
            fileWritten(file, null);
        }
        return file;
    }

    /**
     * What {@link #saveText(String, EditorDocument, boolean)} has written, and
     * where.
     */
    public static final class SavedText {

        private final File file;
        private final String encoding;
        private final boolean bom;

        private SavedText(File file, String encoding, boolean bom) {
            this.file = file;
            this.encoding = encoding;
            this.bom = bom;
        }

        /** @return The file that has been written. */
        public File getFile() {
            return file;
        }

        /**
         * @return The name of the encoding the user chose for the file. It is
         *         not the one of the text that is written when the file is a
         *         web page (see {@link FileIO#isWebPage(File)}): a web page is
         *         always written in UTF-8.
         */
        public String getEncoding() {
            return encoding;
        }

        /** @return <code>true</code> if the file starts with a BOM. */
        public boolean hasBom() {
            return bom;
        }
    }

    /**
     * Saves the text of a document, or only the selected part of it, in a file
     * chosen by the user, who also chooses the encoding of the file: the one of
     * the document is the one that is suggested. If the file is a web page (see
     * {@link #isWebPage(File)}), what is written is not the text but a page
     * that shows it with the colors of its syntax highlighting, see
     * {@link WebPageBuilder}, and it is in UTF-8 whatever the user chose. The
     * tabs holding the file are told that it has been written, see
     * {@link #fileWritten(File, EditorDocument)}: the document the text comes
     * from is left as it is when the whole text has been saved, because the
     * text is already its own, but not when only a part of it has been saved,
     * because the file then holds something else than the text of the document.
     * The encoding and the BOM of the document are left as they are too: it is
     * up to the caller to give them what has been written, if the file becomes
     * the one of the document. Can be called from any thread.
     *
     * @param fileName The name suggested to the user.
     * @param document The document whose text is saved.
     * @param selectionOnly <code>true</code> to save only what is selected in
     *        the document, <code>false</code> to save all of its text.
     * @return What has been written, or <code>null</code> if the user gave up,
     *         or if only the selection had to be saved and nothing is
     *         selected.
     * @throws Exception If the file cannot be written, or if the text has
     *         characters that cannot be represented in the encoding the user
     *         chose (nothing is written then).
     */
    public static SavedText saveText(String fileName, EditorDocument document, boolean selectionOnly) throws Exception {
        // The text is read, and the selection and the encoding with it, at the
        // same time and on the thread that owns the text area
        final AtomicReference<TextRange> rangeRef = new AtomicReference<>();
        final AtomicReference<String> textRef = new AtomicReference<>();
        final String[] documentEncoding = new String[1];
        final boolean[] documentBom = new boolean[1];
        runOnEventThread(() -> {
            RSyntaxTextArea textArea = document.getTextArea();
            int start = selectionOnly ? textArea.getSelectionStart() : 0;
            int end = selectionOnly ? textArea.getSelectionEnd() : textArea.getDocument().getLength();
            if (selectionOnly && start == end) {
                return;
            }
            try {
                textRef.set(textArea.getDocument().getText(start, end - start));
            } catch (BadLocationException ble) {
                // The range comes from the text area itself
                throw new IllegalStateException(ble);
            }
            rangeRef.set(new TextRange(document, start, end));
            documentEncoding[0] = document.getState().getEncoding();
            documentBom[0] = document.getState().hasBom();
        });
        if (rangeRef.get() == null) {
            return null;
        }

        // The BOM of the file the text comes from is kept, as long as the
        // encoding is the same
        final String text = textRef.get();
        final Chosen chosen = chooseAndWrite(fileName, documentEncoding[0], rangeRef.get(),
                encoding -> encodeText(text, encoding, writesBom(encoding, documentEncoding[0], documentBom[0])));
        if (chosen == null) {
            return null;
        }
        fileWritten(chosen.file, selectionOnly ? null : document);
        return new SavedText(chosen.file, chosen.encoding,
                writesBom(chosen.encoding, documentEncoding[0], documentBom[0]));
    }

    private static boolean writesBom(String encoding, String documentEncoding, boolean documentBom) {
        return TextFileCodec.writesBom(encoding, documentBom && encoding.equals(documentEncoding));
    }

    /**
     * Turns some text into the bytes of a file.
     *
     * @param text The text.
     * @param encoding The name of the encoding.
     * @param bom <code>true</code> to write the BOM of the encoding first, if
     *        it has one.
     * @return The bytes.
     * @throws IOException If the text has characters that cannot be represented
     *         in the encoding: its message tells it to the user, and nothing is
     *         written in place of those characters.
     */
    public static byte[] encodeText(String text, String encoding, boolean bom) throws IOException {
        try {
            return TextFileCodec.encode(text, encoding, bom);
        } catch (CharacterCodingException cce) {
            throw new IOException(MessageFormat.format(
                    LanguageManager.getInstance().getString("message.cannot_encode"), encoding), cce);
        }
    }

    /**
     * @param file A file.
     * @return <code>true</code> if the name of the file is the one of a web
     *         page (<code>.htm</code> or <code>.html</code>).
     */
    public static boolean isWebPage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".htm") || name.endsWith(".html");
    }

    /**
     * Tells the editor that a file has been written, so that the tabs holding
     * it, other than the one the text comes from, do not keep an old text
     * unnoticed: each of them asks the user, when it is shown, whether to load
     * the file again. The writes done by
     * {@link #saveFile(String, byte[])}, {@link #saveText(String, EditorDocument, boolean)}
     * and {@link #saveAndOpenFile(String, byte[])} tell it on their own; whoever
     * else writes a file that the user can have open in a tab, with
     * {@link #writeFile(File, byte[])} or in some other way, has to call this
     * method once it is done. Can be called from any thread. If the editor
     * cannot be told, the write is not a failure: the reason is only logged.
     *
     * @param file The file that has been written.
     * @param source The document whose text, or part of it, has been written,
     *        or <code>null</code>. It is left as it is.
     */
    public static void fileWritten(File file, EditorDocument source) {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        if (file == null || applicationFrame == null) {
            return;
        }
        try {
            applicationFrame.fileWritten(file, source);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    /**
     * Lets the user choose a file and writes in it, asking before it overwrites
     * an existing one. Nobody is told about the write.
     *
     * @param bytes What is written, unless the file is a web page and a range
     *        of the text of a document is given.
     * @param range The text, or the part of the text, of a document that the
     *        bytes are, or <code>null</code> if the bytes are not the text of a
     *        document: they are then written as they are, whatever the name of
     *        the file.
     * @return The file that has been written, or <code>null</code> if the user
     *         gave up.
     */
    private static File chooseAndWriteFile(String fileName, byte[] bytes) throws Exception {
        Chosen chosen = chooseAndWrite(fileName, null, null, encoding -> bytes);
        return chosen == null ? null : chosen.file;
    }

    /** What is written in the file the user chose, given its encoding. */
    private interface BytesProvider {
        byte[] bytesFor(String encoding) throws Exception;
    }

    private static final class Chosen {

        private final File file;
        private final String encoding;

        private Chosen(File file, String encoding) {
            this.file = file;
            this.encoding = encoding;
        }
    }

    /**
     * Asks the user for a file, and an encoding if the text is saved, and
     * writes it, after asking whether to replace it if it exists.
     *
     * @param fileName The name suggested to the user.
     * @param initialEncoding The encoding that is suggested, or
     *        <code>null</code> if the user is not asked for one.
     * @param range What is written if the file is a web page, instead of what
     *        <code>provider</code> gives, or <code>null</code>.
     * @param provider What is written, when the encoding is known.
     * @return The file and its encoding (<code>null</code> if the user was not
     *         asked), or <code>null</code> if the user gave up.
     */
    private static Chosen chooseAndWrite(String fileName, String initialEncoding, TextRange range,
                                         BytesProvider provider) throws Exception {
        JFileChooser fileChooser = getFileChooser();
        File file = new File(fileName);
        try {
            fileChooser.setSelectedFile(file);
        } catch (IndexOutOfBoundsException ioobe) {
            // Do nothing.
        }

        // Ensure JFileChooser.showSaveDialog is called on EDT
        final java.util.concurrent.atomic.AtomicInteger resultRef = new java.util.concurrent.atomic.AtomicInteger();
        final java.util.concurrent.atomic.AtomicReference<File> selectedFileRef = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<String> currentDirRef = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<String> encodingRef = new java.util.concurrent.atomic.AtomicReference<>();

        try {
            runOnEventThread(() -> {
                final JComboBox<String> encodingCombo = initialEncoding == null ? null
                        : createEncodingCombo(initialEncoding);
                if (encodingCombo != null) {
                    fileChooser.setAccessory(createEncodingAccessory(encodingCombo));
                }
                try {
                    int result = fileChooser.showSaveDialog(ApplicationFrame.getInstance());
                    resultRef.set(result);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFileRef.set(fileChooser.getSelectedFile());
                        try {
                            currentDirRef.set(fileChooser.getCurrentDirectory().getCanonicalPath());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        if (encodingCombo != null) {
                            encodingRef.set((String) encodingCombo.getSelectedItem());
                        }
                    }
                } finally {
                    // The chooser is shared with the commands that do not ask
                    // for an encoding
                    fileChooser.setAccessory(null);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (JFileChooser.APPROVE_OPTION == resultRef.get()) {
            LanguageManager langManager = LanguageManager.getInstance();
            Config.saveLastUsedDir(currentDirRef.get());
            File selectedFile = selectedFileRef.get();
            String chosenFilePath = selectedFile.getAbsolutePath().trim();
            if (!new File(chosenFilePath).exists() || Dialog.YES_OPTION == Dialog.show(langManager.getString("dialog.file_exists"),
                    langManager.getString("dialog.file_exists.overwrite_existing_file"), Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
                if (range != null && isWebPage(selectedFile)) {
                    writeFile(selectedFile, buildWebPage(chosenFilePath, range));
                } else {
                    writeFile(selectedFile, provider.bytesFor(encodingRef.get()));
                }
                return new Chosen(selectedFile, encodingRef.get());
            }
        }
        return null;
    }

    private static JComboBox<String> createEncodingCombo(String selected) {
        List<String> encodings = new ArrayList<>(TextFileCodec.getEncodings());
        if (!encodings.contains(selected)) {
            encodings.add(selected);
        }
        JComboBox<String> combo = new JComboBox<>(encodings.toArray(new String[0]));
        combo.setSelectedItem(selected);
        return combo;
    }

    private static JComponent createEncodingAccessory(JComboBox<String> combo) {
        JLabel label = new JLabel(LanguageManager.getInstance().getString("dialog.encoding"));
        label.setLabelFor(combo);
        JPanel form = new JPanel(new BorderLayout(0, 4));
        form.add(label, BorderLayout.NORTH);
        form.add(combo, BorderLayout.CENTER);
        // The chooser gives the accessory all the height there is
        JPanel accessory = new JPanel(new BorderLayout());
        accessory.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        accessory.add(form, BorderLayout.NORTH);
        return accessory;
    }

    /**
     * Writes some bytes in a file, replacing its content. Unlike
     * {@link #saveFile(String, byte[])}, it does not tell the editor that the
     * file has been written: see {@link #fileWritten(File, EditorDocument)}.
     *
     * @param file The file to write.
     * @param bytes The content of the file.
     * @throws Exception If the file cannot be written.
     */
    public static void writeFile(File file, byte[] bytes) throws Exception {
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(bytes);
        }
    }

    public static void openFile(File file, boolean openExternally) throws Exception {
        if (openExternally) {
            Desktop.getDesktop().open(file);
        } else {
            FileIO.openFile(file);
        }
    }

    public static void openFile(File file) throws Exception {
        if (file == null) {
            return;
        }
        // The content read here must be handed over to the EDT before it
        // touches the text area's Document: this method runs on the
        // background thread started by ThreadedAction (see performThreaded()
        // callers), and Swing text components/Documents are not thread-safe.
        // The previous implementation mutated the text area directly from
        // this background thread (and, for the initial clear, queued an
        // *asynchronous* SwingUtilities.invokeLater() via
        // ApplicationFrame.setText() that raced against it) - depending on
        // scheduling, the EDT could clear the text area right after this
        // thread had just loaded the real content into it, leaving the
        // editor empty, or the two threads could corrupt the Document by
        // touching it at the same time; once corrupted, the same shared
        // RSyntaxTextArea/Document instance stayed broken for the rest of
        // that run, which is why every subsequent open (including of a file
        // that had opened fine moments before) kept failing too
        // The encoding is the one of the BOM, or it is guessed, but it is never
        // the platform default blindly, see TextFileCodec.decode(byte[]): that
        // would silently corrupt any non-ASCII character (e.g. accented Italian
        // letters) whenever the file was written on a different machine or
        // with a different editor, and what is written back would not be the
        // file that was read
        // What the file is like is noted down too, to know later whether it has
        // been changed by someone else: its date is got before reading it, see
        // FileSnapshot.lastModifiedOf(File)
        final FileTime lastModified = FileSnapshot.lastModifiedOf(file);
        final byte[] bytes = FileIO.readFile(file);
        final FileSnapshot snapshot = FileSnapshot.of(lastModified, bytes);
        final TextFileCodec.Decoded decoded = TextFileCodec.decode(bytes);
        final String content = decoded.getText();
        // The name of the file tells how its text is highlighted
        final String syntaxStyle = FileSyntaxStyle.forFile(file);
        SwingUtilities.invokeAndWait(() -> {
            ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
            // The file is opened in a tab of its own, or in the empty one that
            // is shown, unless it is already open: then its tab is shown
            EditorDocument document = applicationFrame.getDocumentForOpening(file);
            if (document == null) {
                return;
            }
            document.getState().setOpenedFile(file);
            document.getState().setFileSnapshot(snapshot);
            document.getState().setEncoding(decoded.getEncoding());
            document.getState().setBom(decoded.hasBom());
            RSyntaxTextArea textArea = document.getTextArea();
            // The style is set before the text, so that the text is tokenized
            // once with the right style and not again when the style changes
            textArea.setSyntaxEditingStyle(syntaxStyle);
            textArea.setText(content);
            textArea.setCaretPosition(0);
            // The text of the file is where the document starts from: it must
            // not be possible to undo it
            textArea.discardAllEdits();
            document.setModified(false);
            // Only the documents of some syntax styles have a source tree
            document.refreshSourceTree();
            // The menu shows the syntax style of the document that is shown
            ApplicationMenuBar menuBar = applicationFrame.getApplicationMenuBar();
            if (menuBar != null && document == applicationFrame.getCurrentDocument()) {
                menuBar.selectSyntaxStyle(syntaxStyle);
            }
            applicationFrame.refreshTabTitles();
        });
    }

    /**
     * Replaces the text of a document with the one of its file, for instance
     * because the file has been written after the document read it. Unlike
     * {@link #openFile(File)}, the syntax style of the document is kept, and so
     * is the position of the caret as far as the new text allows. As when a file
     * is opened, the new text cannot be undone and the document is not shown as
     * changed. The file is read on the calling thread, which must be the Swing
     * event thread, as the text area is changed too. What the document knows of
     * its file is brought up to date, as it was with the text just read.
     *
     * @param document The document to reload. Nothing is done if it is not the
     *        document of a file.
     * @throws Exception If the file cannot be read: the document is left as it
     *         is.
     */
    public static void reloadFile(EditorDocument document) throws Exception {
        File file = document.getState().getOpenedFile();
        if (file == null) {
            return;
        }
        // The date comes before the content, see openFile(File)
        FileTime lastModified = FileSnapshot.lastModifiedOf(file);
        byte[] bytes = readFile(file);
        // The encoding the document had is kept, see TextFileCodec.decodeAgain()
        TextFileCodec.Decoded decoded = TextFileCodec.decodeAgain(bytes, document.getState().getEncoding());
        String content = decoded.getText();
        RSyntaxTextArea textArea = document.getTextArea();
        int caretPosition = textArea.getCaretPosition();
        textArea.setText(content);
        textArea.setCaretPosition(Math.min(caretPosition, textArea.getDocument().getLength()));
        textArea.discardAllEdits();
        document.getState().setFileSnapshot(FileSnapshot.of(lastModified, bytes));
        document.getState().setEncoding(decoded.getEncoding());
        document.getState().setBom(decoded.hasBom());
        document.setModified(false);
        // Only the documents of some syntax styles have a source tree
        document.refreshSourceTree();
    }

    /**
     * Opens some files one after the other, as if the user had chosen them
     * with the Open command: each one in a tab of its own, the first one in
     * the empty tab that is shown, if there is one. A file that does not
     * exist, or that cannot be opened, is reported and does not stop the
     * others. When all of them are open, the first one is the one that is
     * shown, as in RText. Like {@link #openFile(File)}, it must not be called
     * from the Swing event thread.
     *
     * @param files The files to open, in the order they have to be opened.
     */
    public static void openFiles(List<File> files) {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        LanguageManager langManager = LanguageManager.getInstance();
        for (File file : files) {
            if (!file.isFile()) {
                applicationFrame.getConsole().println(
                        MessageFormat.format(langManager.getString("message.not_an_existing_file"), file.getPath()),
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                continue;
            }
            try {
                applicationFrame.openFile(FileLocation.create(file));
            } catch (Exception e) {
                ExceptionDialog.showException(e);
            }
        }
        if (files.size() > 1) {
            // A file that has not been opened (it was too large and the user
            // gave up, or it could not be read) has no document to show
            try {
                runOnEventThread(() -> {
                    for (File file : files) {
                        if (applicationFrame.showOpenedFile(file)) {
                            break;
                        }
                    }
                });
            } catch (Exception e) {
                ExceptionDialog.showException(e);
            }
        }
    }

    /**
     * Runs a task on the Swing event thread and waits for it: at once if the
     * caller already is the event thread, which cannot wait for itself.
     *
     * @param task The task to run.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws InvocationTargetException If the task throws an exception.
     */
    static void runOnEventThread(Runnable task) throws InterruptedException, InvocationTargetException {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeAndWait(task);
        }
    }

    public static File chooseFile() throws Exception {
        JFileChooser fileChooser = getFileChooser();

        // Ensure JFileChooser.showOpenDialog is called on EDT
        final AtomicInteger resultRef = new AtomicInteger();
        final AtomicReference<File> selectedFileRef = new AtomicReference<>();
        final AtomicReference<String> currentDirRef = new AtomicReference<>();

        try {
            SwingUtilities.invokeAndWait(() -> {
                int result = fileChooser.showOpenDialog(ApplicationFrame.getInstance());
                resultRef.set(result);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFileRef.set(fileChooser.getSelectedFile());
                    try {
                        currentDirRef.set(fileChooser.getCurrentDirectory().getCanonicalPath());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (JFileChooser.APPROVE_OPTION == resultRef.get()) {
            Config.saveLastUsedDir(currentDirRef.get());
            return selectedFileRef.get();
        }
        return null;
    }

    public static byte[] readFile(File file) throws Exception {
        return Files.readAllBytes(file.toPath());
    }

    protected static JFileChooser getFileChooser() throws Exception {
        // Ensure JFileChooser creation happens on EDT. The caller can already be
        // the event thread: closing a tab that has been changed asks to save
        // its text there, and invokeAndWait() cannot be called from the event
        // thread
        final java.util.concurrent.atomic.AtomicReference<JFileChooser> chooserRef = new java.util.concurrent.atomic.AtomicReference<>();

        try {
            runOnEventThread(() -> {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.addChoosableFileFilter(acceptAllFileFilter);
                    fileChooser.setFileFilter(acceptAllFileFilter);
                }
                try {
                    Optional.ofNullable(Config.getLastUsedDir()).map(File::new)
                            .ifPresent(fileChooser::setCurrentDirectory);
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
                chooserRef.set(fileChooser);
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return chooserRef.get();
    }

    /**
     * Builds the web page of a range of the text of a document. The text area
     * is read on the Swing event thread.
     *
     * @param title The title of the page.
     * @param range What the page shows.
     * @return The page, encoded as it declares itself: UTF-8.
     */
    private static byte[] buildWebPage(String title, TextRange range) throws Exception {
        final AtomicReference<String> pageRef = new AtomicReference<>();
        runOnEventThread(() -> {
            try {
                pageRef.set(WebPageBuilder.build(title, range.document.getTextArea(), range.start, range.end));
            } catch (BadLocationException ble) {
                // The range comes from the text area itself
                throw new IllegalStateException(ble);
            }
        });
        return pageRef.get().getBytes(StandardCharsets.UTF_8);
    }

    /** A range of the text of a document: the one that is being saved. */
    private static final class TextRange {

        private final EditorDocument document;
        private final int start;
        private final int end;

        private TextRange(EditorDocument document, int start, int end) {
            this.document = document;
            this.start = start;
            this.end = end;
        }
    }
}
