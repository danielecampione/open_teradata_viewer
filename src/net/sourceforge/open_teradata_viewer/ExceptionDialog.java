/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public final class ExceptionDialog {

    private ExceptionDialog() {
    }

    public static void showException(Throwable t) {
        ApplicationFrame applicationFrame;
        String text = "";
        try {
            applicationFrame = ApplicationFrame.getInstance();
            text = applicationFrame.getText();
        } catch (Throwable e) {
            ExceptionDialog.hideException(e);
        }
        if ("Details"
                .equals(Dialog.show(t.getClass().getName(),
                        t.getMessage() != null ? t.toString() : "Error",
                        Dialog.ERROR_MESSAGE, new Object[]{"Close", "Details"},
                        "Close"))) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(out));
            JTextArea textArea = new JTextArea(new String(out.toByteArray()));
            textArea.append("\n");
            textArea.append(Main.APPLICATION_NAME + " ");
            try {
                textArea.append(Config.getVersion());
            } catch (IOException e) {
                hideException(e);
            }
            textArea.append("\n");
            textArea.append(System.getProperty("os.name"));
            textArea.append(" ");
            textArea.append(System.getProperty("os.version"));
            textArea.append(" ");
            textArea.append(System.getProperty("sun.os.patch.level"));
            textArea.append(" ");
            textArea.append(System.getProperty("os.arch"));
            textArea.append("\n");
            textArea.append(System.getProperty("java.runtime.name"));
            textArea.append(" ");
            textArea.append(System.getProperty("java.runtime.version"));
            textArea.append("\n" + text);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            if ("Copy to clipboard".equals(Dialog.show(t.getClass().getName(),
                    scrollPane, Dialog.ERROR_MESSAGE, new Object[]{"Close",
                            "Copy to clipboard"}, "Close"))) {
                try {
                    Toolkit.getDefaultToolkit()
                            .getSystemClipboard()
                            .setContents(
                                    new StringSelection(textArea.getText()
                                            .replace('\r', ' ')), null);
                } catch (Throwable t2) {
                    ExceptionDialog.hideException(t2);
                }
            }
        }

        //some tips
        StringBuilder msg = new StringBuilder();
        if (t instanceof SQLException) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(out));
            String exception = new String(out.toByteArray());
            if (t.getMessage().contains("Unsupported VM encoding")
                    || t.getCause() instanceof UnsupportedEncodingException) {
                msg.append("This can be resolved by reinstalling your JRE and enabling ");
                msg.append("\"Support for additional languages\" during setup.");
            } else if (((SQLException) t).getErrorCode() == 907) {
                if (text.toLowerCase().contains("order")
                        && (exception.contains("updateRow")
                                || exception.contains("insertRow") || exception
                                .contains("deleteRow"))) {
                    msg.append("Updating a resultset mostly fails when the ORDER BY clause is used.\n");
                    msg.append("Try the select without ORDER BY and sort the column afterwards.");
                }
            }
        }
    }

    public static void hideException(Throwable t) {
        t.printStackTrace();
    }

    public static void ignoreException(Throwable t) {
    }
}
