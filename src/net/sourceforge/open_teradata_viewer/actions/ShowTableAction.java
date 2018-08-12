/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2013, D. Campione
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
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ShowTableValidationStrategy;
import net.sourceforge.open_teradata_viewer.WaitingDialog;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 *
 * 
 * @author D. Campione
 * 
 */
public class ShowTableAction extends ShowObjectAction {

    private static final long serialVersionUID = -4577594966966231651L;

    protected ShowTableAction() {
        super("Show table");
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("NOT connected.",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        String tableName = null;
        String databaseName = null;
        boolean firstIteration = true;
        while (tableName == null) {
            if (firstIteration) {
                tableName = ApplicationFrame.getInstance().getTextComponent()
                        .getSelectedText();
                firstIteration = false;
            }
            if (tableName == null) {
                tableName = Dialog.showInputDialog("Insert the table name: ");
                if (tableName == null) {
                    return;
                }
            }
            if (!Utilities.canBeATeradataObjectName(tableName)) {
                tableName = null;
            }
        }

        tableName = tableName.trim().toUpperCase();
        int lastTokenIndex = tableName.lastIndexOf(".");
        if (lastTokenIndex != -1) {
            databaseName = tableName.substring(
                    tableName.lastIndexOf(".", lastTokenIndex - 1) == -1
                            ? 0
                            : tableName.lastIndexOf(".", lastTokenIndex - 1),
                    lastTokenIndex);
            tableName = tableName.substring(lastTokenIndex + 1,
                    tableName.length());
        }
        String sqlQuery = getSQLQueryToShowObject(
                new ShowTableValidationStrategy(),
                ((databaseName != null && databaseName.trim().length() > 0)
                        ? databaseName + "."
                        : "") + tableName);
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
                    ExceptionDialog.ignoreException(t);
                }
            }
        };
        WaitingDialog waitingDialog = null;
        try {
            waitingDialog = new WaitingDialog(onCancel);
        } catch (InterruptedException ie) {
            ExceptionDialog.ignoreException(ie);
        }
        waitingDialog.setText("Executing statement..");
        resultSet = statement.executeQuery();
        waitingDialog.hide();
        String tableBody = "";
        while (resultSet.next()) {
            Object obj = resultSet.getString(1);
            if (obj == null) {
                throw new SQLException("ER_BAD_NULL_ERROR", "SQLState 23000",
                        1048);
            } else if (obj instanceof String) {
                tableBody += obj.toString();
            }
        }
        ApplicationFrame.getInstance()
                .setText(StringUtil.conformize(tableBody));
        statement.close();
        resultSet.close();
    }
}