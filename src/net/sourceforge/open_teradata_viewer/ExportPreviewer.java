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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sourceforge.open_teradata_viewer.editor.xml_tools.XMLBeautifier;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ExportPreviewer {
	
    private ExportPreviewer() {
    }

    public static void preview(final String text, final byte[] bytes) throws Exception {
        final java.util.concurrent.atomic.AtomicReference<String> chosenValue =
                new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicBoolean isXmlRef =
                new java.util.concurrent.atomic.AtomicBoolean(false);

        try {
            javax.swing.SwingUtilities.invokeAndWait(() -> {
                try {
                    JTextArea textArea = new JTextArea(text);
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    boolean isXml = text.startsWith("<?xml");
                    isXmlRef.set(isXml);
                    Object[] options = isXml
                            ? new Object[]{
                                "button.save_to_file",
                                "button.save_to_file_and_open",
                                "button.copy_to_clipboard",
                                "button.pretty_print_xml",
                                "button.cancel"}
                            : new Object[]{
                                "button.save_to_file",
                                "button.save_to_file_and_open",
                                "button.copy_to_clipboard",
                                "button.cancel"};
                    LanguageManager langManager = LanguageManager.getInstance();
                    Object value = Dialog.show(langManager.getString("dialog.preview"),
                            scrollPane, Dialog.PLAIN_MESSAGE, options, "button.save_to_file");
                    chosenValue.set(value != null ? value.toString() : null);
                } catch (Exception ex) {
                    ExceptionDialog.showException(ex);
                }
            });
        } catch (java.lang.reflect.InvocationTargetException ite) {
            throw new Exception("Error in ExportPreviewer.preview", ite);
        }

        // File operations happen here, outside the EDT
        LanguageManager langManager = LanguageManager.getInstance();
        String value = chosenValue.get();
        boolean isXml = isXmlRef.get();

        if (langManager.getString("button.save_to_file").equals(value)) {
            String fileName = isXml ? "export.xml" : "export.txt";
            FileIO.saveFile(fileName, bytes != null ? bytes : text.getBytes());
        } else if (langManager.getString("button.save_to_file_and_open").equals(value)) {
            String fileName = isXml ? "export.xml" : "export.txt";
            File file = FileIO.saveFile(fileName, bytes != null ? bytes : text.getBytes());
            if (file != null) {
                FileIO.openFile(file, true);
            }
        } else if (langManager.getString("button.copy_to_clipboard").equals(value)) {
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(text), null);
            } catch (Throwable t2) {
                ExceptionDialog.hideException(t2);
            }
        } else if (langManager.getString("button.pretty_print_xml").equals(value)) {
            XMLBeautifier xmlBeautifier = new XMLBeautifier(XMLBeautifier.DEFAULT_TAB_SIZE);
            String formatted = xmlBeautifier.indentXML(text);
            preview(formatted, bytes);
        }
    }
}
