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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ConnectionData.DatabaseType;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.IShowObjectValidationStrategy;
import net.sourceforge.open_teradata_viewer.ShowObjectType;
import net.sourceforge.open_teradata_viewer.ShowObjectValidationStrategyFactory;
import net.sourceforge.open_teradata_viewer.WaitingDialog;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
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
        super(LanguageManager.getInstance().getString("action.show.table"));
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.show.table"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame.getInstance().getConsole().println("NOT connected.",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        // Checked here, before asking for the table name below, because
        // the answer never depends on that name - it would be wasted
        // effort to make the user type one only to be told afterwards
        // that this isn't supported for the connected database type.
        DatabaseType databaseType = ApplicationFrame.getInstance().getDatabaseType();
        IShowObjectValidationStrategy showObjectValidationStrategy = ShowObjectValidationStrategyFactory
                .getStrategy(ShowObjectType.TABLE, databaseType);
        if (showObjectValidationStrategy == null) {
            ApplicationFrame.getInstance().getConsole().println(
                    LanguageManager.getInstance().getString("message.action_not_supported_for_database"),
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        String tableName = null;
        String databaseName = null;
        boolean firstIteration = true;
        while (tableName == null) {
            if (firstIteration) {
                tableName = ApplicationFrame.getInstance().getTextComponent().getSelectedText();
                firstIteration = false;
            }
            if (tableName == null) {
                tableName = Dialog.showInputDialog(LanguageManager.getInstance().getString("message.insert_table_name"));
                if (tableName == null) {
                    return;
                }
            }
            if (!Utilities.canBeAValidObjectName(tableName)) {
                tableName = null;
            }
        }

        tableName = tableName.trim().toUpperCase();
        int lastTokenIndex = tableName.lastIndexOf(".");
        if (lastTokenIndex != -1) {
            databaseName = tableName.substring(
                    tableName.lastIndexOf(".", lastTokenIndex - 1) == -1 ? 0
                            : tableName.lastIndexOf(".", lastTokenIndex - 1),
                    lastTokenIndex);
            tableName = tableName.substring(lastTokenIndex + 1, tableName.length());
        }
        String sqlQuery = getSQLQueryToShowObject(
                showObjectValidationStrategy,
                ((databaseName != null && databaseName.trim().length() > 0)
                        ? databaseName + "." : "")
                        + tableName);
        ResultSet resultSet = null;
        Connection connection = Context.getInstance().getConnectionData().getConnection();
        final PreparedStatement statement = connection.prepareStatement(sqlQuery);
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
        WaitingDialog waitingDialog;
        try {
            waitingDialog = new WaitingDialog(onCancel);
        } catch (InterruptedException ie) {
            ExceptionDialog.ignoreException(ie);
            return;
        }
        waitingDialog.setText(LanguageManager.getInstance().getString("message.executing_statement"));
        try {
            resultSet = statement.executeQuery();
            StringBuilder tableBody = new StringBuilder();
            while (resultSet.next()) {
                Object obj = resultSet.getString(showObjectValidationStrategy.getResultColumnIndex());
                if (obj == null) {
                    throw new SQLException("ER_BAD_NULL_ERROR", "SQLState 23000", 1048);
                } else if (obj instanceof String) {
                    tableBody.append(obj.toString());
                }
            }
            ApplicationFrame.getInstance().setText(StringUtil.conformize(tableBody.toString()));
        } finally {
            waitingDialog.hide();
            if (resultSet != null) {
                resultSet.close();
            }
            statement.close();
        }
    }
}