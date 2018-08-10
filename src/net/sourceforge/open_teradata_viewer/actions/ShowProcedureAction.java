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
public class ShowProcedureAction extends CustomAction {

    private static final long serialVersionUID = -603744266487784355L;

    protected ShowProcedureAction() {
        super("Show procedure..", null, null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "show procedure" command can be performed altough other processes
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

        String procedureName = null;
        boolean firstIteration = true;
        while (procedureName == null) {
            if (firstIteration) {
                procedureName = ApplicationFrame.getInstance()
                        .getTextComponent().getSelectedText();
                firstIteration = false;
            }
            if (procedureName == null) {
                procedureName = Dialog
                        .showInputDialog("Insert the procedure name: ");
                if (procedureName == null) {
                    return;
                }
            }
            if (!canBeAnObjectName(procedureName)) {
                procedureName = null;
            }
        }

        procedureName = procedureName.trim().toUpperCase();
        String querySQL = "SHOW PROCEDURE " + procedureName;
        ResultSet resultSet = null;
        Connection connection = ApplicationFrame.getInstance().connectionManager
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
        String procedureBody = "";
        while (resultSet.next()) {
            procedureBody += resultSet.getString(1).trim();
        }
        // Do not use the ApplicationFrame.setText(String s) method 
        ApplicationFrame.getInstance().getTextComponent()
                .setText(procedureBody);
        statement.close();
        resultSet.close();
    }
}
