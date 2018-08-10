/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.WaitingDialog;

/**
*
* 
* @author D. Campione
* 
*/
public class ShowMacroAction extends CustomAction {

    private static final long serialVersionUID = 6799585250760196135L;

    protected ShowMacroAction() {
        super("Show macro..", null, null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "show macro" command can be performed altough other processes
        // are running.
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame.getInstance().changeLog.append("NOT connected.\n",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        String macroName = null;
        String databaseName = null;
        boolean firstIteration = true;
        while (macroName == null) {
            if (firstIteration) {
                macroName = ApplicationFrame.getInstance().getTextComponent()
                        .getSelectedText();
                firstIteration = false;
            }
            if (macroName == null) {
                macroName = Dialog.showInputDialog("Insert the table name: ");
                if (macroName == null) {
                    return;
                }
            }
            if (!canBeAnObjectName(macroName)) {
                macroName = null;
            }
        }

        macroName = macroName.trim().toUpperCase();
        int lastTokenIndex = macroName.lastIndexOf(".");
        if (lastTokenIndex != -1) {
            databaseName = macroName.substring(
                    macroName.lastIndexOf(".", lastTokenIndex - 1) == -1
                            ? 0
                            : macroName.lastIndexOf(".", lastTokenIndex - 1),
                    lastTokenIndex);
            macroName = macroName.substring(lastTokenIndex + 1,
                    macroName.length());
        }
        String sqlQuery = "SHOW MACRO "
                + ((databaseName != null && databaseName.trim().length() > 0)
                        ? databaseName + "."
                        : "") + macroName;
        ResultSet resultSet = null;
        Connection connection = Context.getInstance().getConnectionData()
                .getConnection();
        final PreparedStatement statement = connection
                .prepareStatement(sqlQuery);
        Runnable onCancel = new Runnable() {
            @Override
            public void run() {
                try {
                    statement.cancel();
                } catch (Throwable t) {
                }
            }
        };
        WaitingDialog waitingDialog = null;
        try {
            waitingDialog = new WaitingDialog(onCancel);
        } catch (InterruptedException ie) {
        }
        waitingDialog.setText("Executing statement..");
        resultSet = statement.executeQuery();
        waitingDialog.hide();
        String macroBody = "";
        while (resultSet.next()) {
            Object obj = resultSet.getString(1);
            if (obj == null) {
                throw new SQLException("ER_BAD_NULL_ERROR", "SQLState 23000",
                        1048);
            } else if (obj instanceof String) {
                macroBody += obj.toString().trim();
            }
        }
        ApplicationFrame.getInstance().setText(macroBody);
        statement.close();
        resultSet.close();
    }
}