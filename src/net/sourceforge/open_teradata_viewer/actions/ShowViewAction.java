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
import net.sourceforge.open_teradata_viewer.ShowViewValidationStrategy;
import net.sourceforge.open_teradata_viewer.WaitingDialog;

/**
 *
 * 
 * @author D. Campione
 * 
 */
public class ShowViewAction extends ShowObjectAction {

    private static final long serialVersionUID = -8555161081550563065L;

    protected ShowViewAction() {
        super("Show view");
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
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
        String sqlQuery = getSQLQueryToShowObject(
                new ShowViewValidationStrategy(),
                ((databaseName != null && databaseName.trim().length() > 0)
                        ? databaseName + "."
                        : "") + viewName);
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
        String viewBody = "";
        while (resultSet.next()) {
            Object obj = resultSet.getString(1);
            if (obj == null) {
                throw new SQLException("ER_BAD_NULL_ERROR", "SQLState 23000",
                        1048);
            } else if (obj instanceof String) {
                viewBody += obj.toString();
            }
        }
        ApplicationFrame.getInstance().setText(viewBody);
        statement.close();
        resultSet.close();
    }
}