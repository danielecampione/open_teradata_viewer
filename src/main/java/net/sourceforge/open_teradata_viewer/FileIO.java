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
import net.sourceforge.open_teradata_viewer.editor.EncodingChoice;
import net.sourceforge.open_teradata_viewer.editor.FileSnapshot;
import net.sourceforge.open_teradata_viewer.editor.FileSyntaxStyle;
import net.sourceforge.open_teradata_viewer.editor.LineEnding;
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
     * Saves some bytes in a file chosen by the user, exactly as they are,
     * whatever the name of the file is: the bytes are not the text of a
     * document, so the user is not asked for an encoding, see
     * {@link #saveText(String, EditorDocument, boolean)}. The tabs holding the
     * file are told that it has been written, see
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
        private final String text;

        private SavedText(File file, String encoding, boolean bom, String text) {
            this.file = file;
            this.encoding = encoding;
            this.bom = bom;
            this.text = text;
        }

        /** @return The file that has been written. */
        public File getFile() {
            return file;
        }

        /** @return The name of the encoding the user chose for the file. */
        public String getEncoding() {
            return encoding;
        }

        /**
         * @return <code>true</code> if the file starts with a BOM: the user
         *         chose UTF-8 with a BOM, or an encoding that always has one.
         */
        public boolean hasBom() {
            return bom;
        }

        /**
         * @return The text that has been saved, as it was when it was read from
         *         the document: all of it, or only the selected part.
         */
        public String getText() {
            return text;
        }
    }

    /**
     * Saves the text of a document, or only the selected part of it, in a file
     * chosen by the user, who also chooses the encoding of the file and, for
     * UTF-8, whether it starts with a BOM (see {@link EncodingChoice}): the ones
     * of the document are the ones that are suggested. The text is written as it
     * is, whatever the name of the file is: a web page that shows it with the
     * colors of its syntax highlighting is only made by
     * {@link #exportHtml(String, EditorDocument, boolean)}. The tabs holding
     * the file are told that it has been written, see
     * {@link #fileWritten(File, EditorDocument)}: the document the text comes
     * from is left as it is when the whole text has been saved, because the
     * text is already its own, but not when only a part of it has been saved,
     * because the file then holds something else than the text of the document.
     * The encoding and the BOM of the document are left as they are too: it is
     * up to the caller to give them what has been written, if the file becomes
     * the one of the document. The lines of the text end as the ones of the
     * file the document comes from do, see {@link LineEnding}. Can be called
     * from any thread.
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
        final AtomicReference<String> textRef = new AtomicReference<>();
        final AtomicReference<EncodingChoice> documentChoiceRef = new AtomicReference<>();
        final AtomicReference<LineEnding> lineEndingRef = new AtomicReference<>();
        runOnEventThread(() -> {
            TextRange toSave = rangeToSave(document, selectionOnly);
            if (toSave == null) {
                return;
            }
            try {
                textRef.set(document.getTextArea().getDocument().getText(toSave.start, toSave.end - toSave.start));
            } catch (BadLocationException ble) {
                // The range comes from the text area itself
                throw new IllegalStateException(ble);
            }
            documentChoiceRef.set(EncodingChoice.of(document.getState().getEncoding(), document.getState().hasBom()));
            lineEndingRef.set(document.getState().getLineEnding());
        });
        // Nothing has been read if only the selection had to be saved and
        // nothing is selected
        final String text = textRef.get();
        if (text == null) {
            return null;
        }

        // The user chooses the BOM together with the encoding, starting from
        // the ones of the document
        final Chosen chosen = chooseAndWrite(fileName, documentChoiceRef.get(),
                (file, choice) -> encodeText(text, lineEndingRef.get(), choice.getEncoding(), choice.writesBom()));
        if (chosen == null) {
            return null;
        }
        fileWritten(chosen.file, selectionOnly ? null : document);
        return new SavedText(chosen.file, chosen.choice.getEncoding(), chosen.choice.writesBom(), text);
    }

    /**
     * Exports the text of a document, or only the selected part of it, as a web
     * page that shows it with the colors and the font styles of its syntax
     * highlighting, see {@link WebPageBuilder}, in a file chosen by the user.
     * The page is written whatever the name of the file is, and it is in UTF-8,
     * as it declares itself, so the user is not asked for an encoding. A page
     * is not the text of the document: the document is left as it is, and the
     * tabs holding the file, the one the text comes from included, are told
     * that it has been written, see {@link #fileWritten(File, EditorDocument)}.
     * Can be called from any thread.
     *
     * @param fileName The name suggested to the user.
     * @param document The document whose text is exported.
     * @param selectionOnly <code>true</code> to export only what is selected in
     *        the document, <code>false</code> to export all of its text.
     * @return The file that has been written, or <code>null</code> if the user
     *         gave up, or if only the selection had to be exported and nothing
     *         is selected.
     * @throws Exception If the file cannot be written.
     */
    public static File exportHtml(String fileName, EditorDocument document, boolean selectionOnly) throws Exception {
        // The selection is read on the thread that owns the text area
        final AtomicReference<TextRange> rangeRef = new AtomicReference<>();
        runOnEventThread(() -> rangeRef.set(rangeToSave(document, selectionOnly)));
        final TextRange range = rangeRef.get();
        if (range == null) {
            return null;
        }
        // The user is not asked for an encoding, and what is written is the
        // page, not the text
        final Chosen chosen = chooseAndWrite(fileName, null, (file, choice) -> buildWebPage(file.getName(), range));
        if (chosen == null) {
            return null;
        }
        fileWritten(chosen.file, null);
        return chosen.file;
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
     * Turns some text of a document into the bytes of a file: its lines end as
     * the ones of a file that has the given line ending do, see
     * {@link LineEnding#toFile(String)}, and then it is encoded, see
     * {@link #encodeText(String, String, boolean)}.
     *
     * @param text The text, or a part of it.
     * @param lineEnding The line ending of the file.
     * @param encoding The name of the encoding.
     * @param bom <code>true</code> to write the BOM of the encoding first, if
     *        it has one.
     * @return The bytes.
     * @throws IOException If the text has characters that cannot be represented
     *         in the encoding, see {@link #encodeText(String, String, boolean)}.
     */
    public static byte[] encodeText(String text, LineEnding lineEnding, String encoding, boolean bom)
            throws IOException {
        return encodeText(lineEnding.toFile(text), encoding, bom);
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
     * Lets the user choose a file and writes some bytes in it, exactly as they
     * are whatever the name of the file is, asking before it overwrites an
     * existing one. Nobody is told about the write.
     *
     * @param fileName The name suggested to the user.
     * @param bytes What is written.
     * @return The file that has been written, or <code>null</code> if the user
     *         gave up.
     */
    private static File chooseAndWriteFile(String fileName, byte[] bytes) throws Exception {
        Chosen chosen = chooseAndWrite(fileName, null, (file, choice) -> bytes);
        return chosen == null ? null : chosen.file;
    }

    /**
     * What is written in the file the user chose, given the file, and the
     * encoding and the BOM the user chose.
     */
    private interface BytesProvider {

        /**
         * @param file The file the user chose.
         * @param choice The encoding and the BOM the user chose, or
         *        <code>null</code> if the user was not asked for them.
         * @return What is written in the file.
         */
        byte[] bytesFor(File file, EncodingChoice choice) throws Exception;
    }

    private static final class Chosen {

        private final File file;
        private final EncodingChoice choice;

        private Chosen(File file, EncodingChoice choice) {
            this.file = file;
            this.choice = choice;
        }
    }

    /**
     * Asks the user for a file, and an encoding (with its BOM) if the text is
     * saved, and writes it, after asking whether to replace it if it exists.
     *
     * @param fileName The name suggested to the user.
     * @param initialChoice The encoding and the BOM that are suggested, or
     *        <code>null</code> if the user is not asked for them.
     * @param provider What is written, when the file and the encoding are
     *        known.
     * @return The file and the encoding the user chose (<code>null</code> if
     *         the user was not asked), or <code>null</code> if the user gave
     *         up.
     */
    private static Chosen chooseAndWrite(String fileName, EncodingChoice initialChoice, BytesProvider provider)
            throws Exception {
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
        final java.util.concurrent.atomic.AtomicReference<EncodingChoice> encodingRef = new java.util.concurrent.atomic.AtomicReference<>();

        try {
            runOnEventThread(() -> {
                final JComboBox<EncodingItem> encodingCombo = initialChoice == null ? null
                        : createEncodingCombo(initialChoice);
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
                            encodingRef.set(getSelectedEncoding(encodingCombo));
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
                writeFile(selectedFile, provider.bytesFor(selectedFile, encodingRef.get()));
                return new Chosen(selectedFile, encodingRef.get());
            }
        }
        return null;
    }

    /**
     * An item of the list of the encodings a text can be saved in: the choice
     * of the user, and the words that tell it in the language of the user (the
     * name of the encoding, but for UTF-8 with a BOM). It is what the list
     * shows and what it gives back.
     */
    public static final class EncodingItem {

        private final EncodingChoice choice;
        private final String label;

        private EncodingItem(EncodingChoice choice, String label) {
            this.choice = choice;
            this.label = label;
        }

        /** @return The encoding, and the BOM if it is UTF-8. */
        public EncodingChoice getChoice() {
            return choice;
        }

        @Override
        public String toString() {
            return label;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof EncodingItem && choice.equals(((EncodingItem) obj).choice);
        }

        @Override
        public int hashCode() {
            return choice.hashCode();
        }
    }

    /**
     * @param selected The encoding (and BOM) that is selected, which is added
     *        to the list if it is not one of the encodings that can be used.
     * @return The list of the encodings that can be used to write a text, see
     *         {@link EncodingChoice#getChoices()}: UTF-8 with a BOM is an item
     *         of its own, right after UTF-8 without it. Must be called from the
     *         Swing event thread.
     */
    public static JComboBox<EncodingItem> createEncodingCombo(EncodingChoice selected) {
        List<EncodingChoice> choices = new ArrayList<>(EncodingChoice.getChoices());
        if (!choices.contains(selected)) {
            choices.add(selected);
        }
        List<EncodingItem> items = new ArrayList<>();
        EncodingItem selectedItem = null;
        for (EncodingChoice choice : choices) {
            EncodingItem item = new EncodingItem(choice, EncodingChoice.UTF_8_WITH_BOM.equals(choice)
                    ? LanguageManager.getInstance().getString("dialog.encoding.utf8_with_bom")
                    : choice.getEncoding());
            items.add(item);
            if (choice.equals(selected)) {
                selectedItem = item;
            }
        }
        JComboBox<EncodingItem> combo = new JComboBox<>(items.toArray(new EncodingItem[0]));
        combo.setSelectedItem(selectedItem);
        return combo;
    }

    /**
     * @param combo A list made by {@link #createEncodingCombo(EncodingChoice)}.
     * @return The encoding (and BOM) that is selected in it.
     */
    public static EncodingChoice getSelectedEncoding(JComboBox<EncodingItem> combo) {
        return ((EncodingItem) combo.getSelectedItem()).getChoice();
    }

    /**
     * @return The list of encodings of the dialog to open a file: "Automatic"
     *         first, and selected, then the encodings that can be used, see
     *         {@link TextFileCodec#getEncodings()}.
     */
    private static JComboBox<String> createOpeningEncodingCombo() {
        List<String> items = new ArrayList<>();
        items.add(LanguageManager.getInstance().getString("dialog.encoding.automatic"));
        items.addAll(TextFileCodec.getEncodings());
        JComboBox<String> combo = new JComboBox<>(items.toArray(new String[0]));
        combo.setSelectedIndex(0);
        return combo;
    }

    private static JComponent createEncodingAccessory(JComboBox<?> combo) {
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

    /**
     * Opens a file as a document of the editor, reading it in the encoding that
     * is guessed, see {@link TextFileCodec#decode(byte[])}.
     *
     * @param file The file to open.
     * @throws Exception If the file cannot be read.
     * @see #openFile(File, String)
     */
    public static void openFile(File file) throws Exception {
        openFile(file, (String) null);
    }

    /**
     * Opens a file as a document of the editor, in the tab of the file if it is
     * already open (it is shown, and nothing is read again, so the encoding
     * does not matter then), otherwise in the empty tab that is shown or in a
     * new one. Must not be called from the Swing event thread.
     *
     * @param file The file to open.
     * @param encoding The name of the encoding the file is read in, or
     *        <code>null</code> to guess it, see
     *        {@link TextFileCodec#decode(byte[])}. A BOM, if the file has one,
     *        tells the encoding without doubt and wins over it.
     * @throws IOException If the file is not valid in the encoding: nothing is
     *         replaced by a substitute character, as it would be lost saving
     *         the file again, and no tab is opened or changed. The message
     *         tells it to the user.
     * @throws Exception If the file cannot be read.
     */
    public static void openFile(File file, String encoding) throws Exception {
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
        // The encoding is the one of the BOM, or the one the user chose, or it
        // is guessed, but it is never the platform default blindly, see
        // TextFileCodec.decode(byte[]): that would silently corrupt any
        // non-ASCII character (e.g. accented Italian letters) whenever the file
        // was written on a different machine or with a different editor, and
        // what is written back would not be the file that was read
        // What the file is like is noted down too, to know later whether it has
        // been changed by someone else: its date is got before reading it, see
        // FileSnapshot.lastModifiedOf(File)
        final FileTime lastModified = FileSnapshot.lastModifiedOf(file);
        final byte[] bytes = FileIO.readFile(file);
        final FileSnapshot snapshot = FileSnapshot.of(lastModified, bytes);
        final TextFileCodec.Decoded decoded = decodeFile(file, bytes, encoding);
        // The lines of the text of a document all end with a line feed: the line
        // ending of the file is kept apart, and given back when the text is
        // written. A file that has no line ending (a single line, or nothing at
        // all) gets the one of the files that are written on this platform
        final LineEnding detectedLineEnding = LineEnding.detect(decoded.getText());
        final LineEnding lineEnding = detectedLineEnding != null ? detectedLineEnding : LineEnding.getDefault();
        final String content = LineEnding.normalize(decoded.getText());
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
            document.getState().setLineEnding(lineEnding);
            // The name of the file tells how its text is highlighted or, if it
            // has no extension, the first line of the text may
            String syntaxStyle = FileSyntaxStyle.forFileAndContent(file, content);
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
     * Reads the text of a file, in the encoding the user chose if there is
     * one, otherwise in the one that is guessed.
     *
     * @param file The file the bytes come from (only to name it to the user).
     * @param bytes The content of the file.
     * @param encoding The name of the encoding the user chose, or
     *        <code>null</code> if the encoding is to be guessed.
     * @return The text, and how it was encoded.
     * @throws IOException If the file is not valid in the chosen encoding.
     */
    private static TextFileCodec.Decoded decodeFile(File file, byte[] bytes, String encoding) throws IOException {
        if (encoding == null) {
            return TextFileCodec.decode(bytes);
        }
        try {
            return TextFileCodec.decode(bytes, encoding);
        } catch (CharacterCodingException cce) {
            throw new IOException(MessageFormat.format(
                    LanguageManager.getInstance().getString("message.cannot_decode"), file.getPath(), encoding), cce);
        }
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
        // The file may have been written with another line ending, but if it
        // has no line ending at all the document keeps the one it had
        LineEnding lineEnding = LineEnding.detect(decoded.getText());
        String content = LineEnding.normalize(decoded.getText());
        RSyntaxTextArea textArea = document.getTextArea();
        int caretPosition = textArea.getCaretPosition();
        textArea.setText(content);
        textArea.setCaretPosition(Math.min(caretPosition, textArea.getDocument().getLength()));
        textArea.discardAllEdits();
        document.getState().setFileSnapshot(FileSnapshot.of(lastModified, bytes));
        document.getState().setEncoding(decoded.getEncoding());
        document.getState().setBom(decoded.hasBom());
        if (lineEnding != null) {
            document.getState().setLineEnding(lineEnding);
        }
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

    /**
     * Asks the user for a file, for the commands that do something else than
     * opening it as text (checksum, import of a LOB, ...).
     *
     * @return The file, or <code>null</code> if the user gave up.
     */
    public static File chooseFile() throws Exception {
        ChosenTextFile chosen = chooseForOpening(false);
        return chosen == null ? null : chosen.getFile();
    }

    /**
     * Asks the user for a file to open as text, and for the encoding to read it
     * in: the dialog has a list of encodings, which starts with "Automatic"
     * (the encoding is guessed, see {@link TextFileCodec#decode(byte[])}) every
     * time it is shown, so that what was chosen for a file does not apply to
     * the next one. Can be called from any thread.
     *
     * @return The file and the encoding, or <code>null</code> if the user gave
     *         up.
     */
    public static ChosenTextFile chooseTextFile() throws Exception {
        return chooseForOpening(true);
    }

    /** A file the user chose to open as text, and how to read it. */
    public static final class ChosenTextFile {

        private final File file;
        private final String encoding;

        private ChosenTextFile(File file, String encoding) {
            this.file = file;
            this.encoding = encoding;
        }

        /** @return The file. */
        public File getFile() {
            return file;
        }

        /**
         * @return The name of the encoding the user chose to read the file in,
         *         or <code>null</code> if the user left the encoding to be
         *         guessed.
         */
        public String getEncoding() {
            return encoding;
        }
    }

    /**
     * Shows the dialog to open a file and remembers the folder it was left in.
     *
     * @param askEncoding <code>true</code> to let the user choose the encoding
     *        too, in a list the dialog gets for the occasion: the dialog is
     *        shared with the commands that do not need it, so it is taken away
     *        when the dialog is closed.
     * @return The file and, if the user chose one, the encoding, or
     *         <code>null</code> if the user gave up.
     */
    private static ChosenTextFile chooseForOpening(boolean askEncoding) throws Exception {
        JFileChooser fileChooser = getFileChooser();

        // Ensure JFileChooser.showOpenDialog is called on EDT
        final AtomicInteger resultRef = new AtomicInteger();
        final AtomicReference<File> selectedFileRef = new AtomicReference<>();
        final AtomicReference<String> currentDirRef = new AtomicReference<>();
        final AtomicReference<String> encodingRef = new AtomicReference<>();

        try {
            runOnEventThread(() -> {
                final JComboBox<String> encodingCombo = askEncoding ? createOpeningEncodingCombo() : null;
                if (encodingCombo != null) {
                    fileChooser.setAccessory(createEncodingAccessory(encodingCombo));
                }
                try {
                    int result = fileChooser.showOpenDialog(ApplicationFrame.getInstance());
                    resultRef.set(result);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFileRef.set(fileChooser.getSelectedFile());
                        try {
                            currentDirRef.set(fileChooser.getCurrentDirectory().getCanonicalPath());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        // The first choice is "Automatic": no encoding
                        if (encodingCombo != null && encodingCombo.getSelectedIndex() > 0) {
                            encodingRef.set((String) encodingCombo.getSelectedItem());
                        }
                    }
                } finally {
                    fileChooser.setAccessory(null);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (JFileChooser.APPROVE_OPTION == resultRef.get()) {
            Config.saveLastUsedDir(currentDirRef.get());
            return new ChosenTextFile(selectedFileRef.get(), encodingRef.get());
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

    /**
     * Works out what has to be saved of a document. Must be called from the
     * Swing event thread, which owns the text area.
     *
     * @param document The document whose text is saved.
     * @param selectionOnly <code>true</code> to save only what is selected,
     *        <code>false</code> to save all the text.
     * @return What is saved, or <code>null</code> if only the selection has to
     *         be saved and nothing is selected.
     */
    private static TextRange rangeToSave(EditorDocument document, boolean selectionOnly) {
        RSyntaxTextArea textArea = document.getTextArea();
        int start = selectionOnly ? textArea.getSelectionStart() : 0;
        int end = selectionOnly ? textArea.getSelectionEnd() : textArea.getDocument().getLength();
        if (selectionOnly && start == end) {
            return null;
        }
        return new TextRange(document, start, end);
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
