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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFileChooser;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;

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

    public static void saveAndOpenFile(String fileName, byte[] bytes)
            throws Exception {
        File file = saveFile(fileName, bytes);
        if (file != null
                && Dialog.YES_OPTION == Dialog.show("Open file",
                        "Open the file with the associated application?",
                        Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
            openFile(file);
        }
    }

    public static File saveFile(String fileName, byte[] bytes) throws Exception {
        JFileChooser fileChooser = getFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        if (JFileChooser.APPROVE_OPTION == fileChooser
                .showSaveDialog(ApplicationFrame.getInstance())) {
            Config.saveLastUsedDir(fileChooser.getCurrentDirectory()
                    .getCanonicalPath());
            File selectedFile = fileChooser.getSelectedFile();
            String chosenFilePath = selectedFile.getAbsolutePath().trim();
            if (!new File(chosenFilePath).exists()
                    || Dialog.YES_OPTION == Dialog.show("File exists",
                            "Overwrite existing file?",
                            Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
                if (chosenFilePath.toLowerCase().endsWith(".htm")
                        || chosenFilePath.toLowerCase().endsWith(".html")) {
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
        FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
    }

    public static void openFile(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    public static File openFile() throws Exception {
        JFileChooser fileChooser = getFileChooser();
        if (JFileChooser.APPROVE_OPTION == fileChooser
                .showOpenDialog(ApplicationFrame.getInstance())) {
            Config.saveLastUsedDir(fileChooser.getCurrentDirectory()
                    .getCanonicalPath());
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static byte[] readFile(File file) throws Exception {
        FileInputStream in = new FileInputStream(file);
        byte[] b = new byte[in.available()];
        in.read(b);
        in.close();
        return b;
    }

    protected static JFileChooser getFileChooser() throws Exception {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(acceptAllFileFilter);
            fileChooser.setFileFilter(acceptAllFileFilter);
        }
        String dir = Config.getLastUsedDir();
        if (dir != null) {
            fileChooser.setCurrentDirectory(new File(dir));
        }
        return fileChooser;
    }

    private static void writeFileAsWebPage(String path) throws IOException {
        String[] styles = new String[Token.DEFAULT_NUM_TOKEN_TYPES];
        StringBuilder temp = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), "UTF-8")));
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<!-- Generated by " + Main.APPLICATION_NAME + " -->");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
        out.println("<title>" + path + "</title>");

        RSyntaxTextArea textArea = ApplicationFrame.getInstance()
                .getTextComponent();
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
                    temp.append("color: ")
                            .append(UIUtil.getHTMLFormatForColor(c))
                            .append(";\n");
                    temp.append("}");
                    styles[token.getType()] = temp.toString();
                }
                sb.append("<span class=\"s" + token.getType() + "\">");
                sb.append(StringUtil.escapeForHTML(token.getLexeme(), "\n",
                        true));
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

        out.close();
    }
}