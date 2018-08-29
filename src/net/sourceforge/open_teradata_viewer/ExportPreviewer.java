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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ExportPreviewer {
    private ExportPreviewer() {
    }

    public static void preview(String text, byte[] bytes) throws Exception {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        boolean isXml = text.startsWith("<?xml");
        Object[] options = isXml
                ? new Object[] { "Save to file", "Save to file and open", "Copy to clipboard", "Pretty print XML",
                        "Cancel" }
                : new Object[] { "Save to file", "Save to file and open", "Copy to clipboard", "Cancel" };
        Object value = Dialog.show("Preview", scrollPane, Dialog.PLAIN_MESSAGE, options, "Save to file");
        if ("Save to file".equals(value)) {
            String fileName = isXml ? "export.xml" : "export.txt";
            FileIO.saveFile(fileName, bytes != null ? bytes : text.getBytes());
        } else if ("Save to file and open".equals(value)) {
            String fileName = isXml ? "export.xml" : "export.txt";
            File file = FileIO.saveFile(fileName, bytes != null ? bytes : text.getBytes());
            if (file != null) {
                FileIO.openFile(file, true);
            }
        } else if ("Copy to clipboard".equals(value)) {
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
            } catch (Throwable t2) {
                ExceptionDialog.hideException(t2);
            }
        } else if ("Pretty print XML".equals(value)) {
            XMLBeautifier xmlBeautifier = new XMLBeautifier(XMLBeautifier.DEFAULT_TAB_SIZE);
            text = xmlBeautifier.indentXML(text);
            preview(text, bytes);
        }
    }
}
