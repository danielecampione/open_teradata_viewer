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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sourceforge.open_teradata_viewer.util.Utilities;

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
        notifyException(t);
        if ("Details".equals(Dialog.show(t.getClass().getName(),
                t.getMessage() != null ? t.getMessage() : "Error",
                Dialog.ERROR_MESSAGE, new Object[] { "Close", "Details" },
                "Close"))) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(out));
            JTextArea textArea = new JTextArea(new String(out.toByteArray()));
            textArea.append("\n");
            textArea.append(Main.APPLICATION_NAME + " ");
            try {
                textArea.append(Config.getVersion());
            } catch (IOException ioe) {
                hideException(ioe);
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
            ConnectionData connectionData = Context.getInstance()
                    .getConnectionData();
            if (connectionData != null) {
                textArea.append("\n");
                textArea.append(connectionData.getUrl());
                textArea.append("\n");
                textArea.append(connectionData.getDriver().getClass().getName());
            }
            textArea.append("\n");
            textArea.append(text);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            if ("Copy to clipboard".equals(Dialog.show(t.getClass().getName(),
                    scrollPane, Dialog.ERROR_MESSAGE, new Object[] { "Close",
                            "Copy to clipboard" }, "Close"))) {
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
        showTip(t, text);
    }

    private static void showTip(Throwable t, String text) {
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
            } else if (Context.getInstance().getConnectionData() != null
                    && Context.getInstance().getConnectionData().isIbm()) {
                if (t.getMessage().contains(
                        "Invalid operation: result set is closed.")) {
                    int length = Context.getInstance().getColumnTypes().length;
                    for (int i = 0; i < length; i++) {
                        if (ResultSetTable.isLob(i)) {
                            msg.append("The IBM JDBC driver doesn't support modifying rows with BLOB's or CLOB's ");
                            msg.append("in the resultset yet.\n");
                            msg.append("Please execute the update statement manually ");
                            msg.append("or try the DataDirect DB2 driver.\n");
                            msg.append("http://www.datadirect.com/products/jdbc/");
                            break;
                        }
                    }
                }
            }
        } else if (t instanceof OutOfMemoryError) {
            msg.append(Main.APPLICATION_NAME
                    + " has a memory limit of 512 MB.\n");
        }
        if (msg.length() > 0) {
            Dialog.show("Tip", msg, Dialog.INFORMATION_MESSAGE,
                    Dialog.DEFAULT_OPTION);
        }
    }

    public static void notifyException(Throwable t) {
        String msg = t.getMessage();
        ApplicationFrame
                .getInstance()
                .getConsole()
                .println((msg == null ? Utilities.getStackTrace(t) : msg),
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
    }

    public static void hideException(Throwable t) {
        t.printStackTrace();
    }

    public static void ignoreException(Throwable t) {
    }
}