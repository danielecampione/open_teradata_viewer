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

/** @author D. Campione */
public class ShowViewAction extends CustomAction {

    private static final long serialVersionUID = -8555161081550563065L;

    protected ShowViewAction() {
        super("Show view..", null, null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "show view" command can be performed altough other processes
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

        String viewName = null;
        String databaseName = null;
        boolean firstIteration = true;
        while (viewName == null) {
            if (firstIteration) {
                viewName = ApplicationFrame.getInstance().getTextComponent()
                        .getSelectedText();
                firstIteration = false;
            }
            if (viewName == null) {
                viewName = Dialog.showInputDialog("Insert the view name: ");
                if (viewName == null) {
                    return;
                }
            }
            if (!canBeAnObjectName(viewName)) {
                viewName = null;
            }
        }

        viewName = viewName.trim().toUpperCase();
        int lastTokenIndex = viewName.lastIndexOf(".");
        if (lastTokenIndex != -1) {
            databaseName = viewName.substring(
                    viewName.lastIndexOf(".", lastTokenIndex - 1) == -1
                            ? 0
                            : viewName.lastIndexOf(".", lastTokenIndex - 1),
                    lastTokenIndex);
            viewName = viewName
                    .substring(lastTokenIndex + 1, viewName.length());
        }
        String querySQL = "SELECT TOP 1 RequestText FROM DBC.TABLESV WHERE TableName = '"
                + viewName
                + "'"
                + ((databaseName != null && databaseName.trim().length() > 0)
                        ? " AND DatabaseName='" + databaseName + "'"
                        : "");
        ResultSet resultSet = null;
        Connection connection = Context.getInstance().getConnectionData()
                .getConnection();
        final PreparedStatement statement = connection
                .prepareStatement(querySQL);
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
        if (resultSet.next()) {
            Object obj = resultSet.getString(1);
            if (obj == null) {
                throw new SQLException("ER_BAD_NULL_ERROR", "SQLState 23000",
                        1048);
            } else if (obj instanceof String) {
                String viewBody = obj.toString().trim();
                ApplicationFrame.getInstance().setText(viewBody);
            }
        } else {
            throw new SQLException(
                    "Object '"
                            + ((databaseName != null && databaseName.trim()
                                    .length() > 0) ? databaseName + "." : "")
                            + viewName + "' does not exist.", "SQLState 42S02",
                    3807);
        }
        statement.close();
        resultSet.close();
    }
}
