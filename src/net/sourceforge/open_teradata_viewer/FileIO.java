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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

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

    public static void saveAndOpenFile(String fileName, byte[] bytes) throws Exception {
        File file = saveFile(fileName, bytes);
        LanguageManager langManager = LanguageManager.getInstance();
        if (file != null && Dialog.YES_OPTION == Dialog.show(langManager.getString("dialog.open_file"),
        		langManager.getString("dialog.open_file.associated_application"), Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
            FileIO.openFile(file, true);
        }
    }

    public static File saveFile(String fileName, byte[] bytes) throws Exception {
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

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    int result = fileChooser.showSaveDialog(ApplicationFrame.getInstance());
                    resultRef.set(result);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFileRef.set(fileChooser.getSelectedFile());
                        try {
                            currentDirRef.set(fileChooser.getCurrentDirectory().getCanonicalPath());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
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
                if (chosenFilePath.toLowerCase().endsWith(".htm") || chosenFilePath.toLowerCase().endsWith(".html")) {
                    // Write output to the current filename
                    writeFileAsWebPage(chosenFilePath);
                } else {
                    writeFile(selectedFile, bytes);
                }
                return selectedFile;
            }
        }
        return null;
    }

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
        if (file != null) {
            ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
            applicationFrame.setText("");
            Context.getInstance().setOpenedFile(file);
            RSyntaxTextArea textArea = applicationFrame.getTextComponent();
            TransferHandler transferHandler = textArea.getTransferHandler();
            // An explicit charset is required here: relying on the
            // platform default (as new String(byte[]) does) makes the
            // decoded text depend on the OS/locale the JVM happens to run
            // under, silently corrupting any non-ASCII character (e.g.
            // accented Italian letters) whenever the file was written on a
            // different machine or with a different editor
            StringSelection stringSelection = new StringSelection(
                    new String(FileIO.readFile(file), StandardCharsets.UTF_8));
            transferHandler.importData(new TransferSupport(textArea, stringSelection));
            textArea.setCaretPosition(0);
        }
    }

    public static File chooseFile() throws Exception {
        JFileChooser fileChooser = getFileChooser();

        // Ensure JFileChooser.showOpenDialog is called on EDT
        final java.util.concurrent.atomic.AtomicInteger resultRef = new java.util.concurrent.atomic.AtomicInteger();
        final java.util.concurrent.atomic.AtomicReference<File> selectedFileRef = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<String> currentDirRef = new java.util.concurrent.atomic.AtomicReference<>();

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
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
        // Ensure JFileChooser creation happens on EDT
        final java.util.concurrent.atomic.AtomicReference<JFileChooser> chooserRef = new java.util.concurrent.atomic.AtomicReference<>();

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
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
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return chooserRef.get();
    }

    private static void writeFileAsWebPage(String path) throws IOException {
        String[] styles = new String[Token.DEFAULT_NUM_TOKEN_TYPES];
        StringBuilder temp = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8")))) {
            out.println(
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<head>");
            out.println("<!-- Generated by " + Main.APPLICATION_NAME + " -->");
            out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
            out.println("<title>" + path + "</title>");

            RSyntaxTextArea textArea = ApplicationFrame.getInstance().getTextComponent();
            int lineCount = textArea.getLineCount();
            for (int i = 0; i < lineCount; i++) {
                Token token = textArea.getTokenListForLine(i);
                while (token != null && token.isPaintable()) {
                    if (styles[token.getType()] == null) {
                        temp.setLength(0);
                        temp.append(".s").append(token.getType()).append(" {\n");
                        Font font = textArea.getFontForTokenType(token.getType());
                        if (font.isBold()) {
                            temp.append("font-weight: bold;\n");
                        }
                        if (font.isItalic()) {
                            temp.append("font-style: italic;\n");
                        }
                        Color c = textArea.getForegroundForToken(token);
                        temp.append("color: ").append(UIUtil.getHTMLFormatForColor(c)).append(";\n");
                        temp.append("}");
                        styles[token.getType()] = temp.toString();
                    }
                    sb.append("<span class=\"s" + token.getType() + "\">");
                    sb.append(StringUtil.escapeForHTML(token.getLexeme(), "\n", true));
                    sb.append("</span>");
                    token = token.getNextToken();
                }
                sb.append('\n');
            }

            // Print CSS styles
            out.println("<style type=\"text/css\">");
            for (int i = 0; i < styles.length; i++) {
                if (styles[i] != null) {
                    out.println(styles[i]);
                }
            }
            out.println("</style>");

            // Print the body
            out.println("</head>");
            out.println("<body>\n<pre>");
            out.println(sb.toString());
            out.println("</pre>\n</body>\n</html>");
        }
    }
}