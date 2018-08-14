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

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.WaitingDialog;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 *
 * 
 * @author D. Campione
 * 
 */
public class ExplainRequestAction extends CustomAction {

    private static final long serialVersionUID = -8555161081550563065L;

    protected ExplainRequestAction() {
        super("Explain request..", null, null, "<html>The request may be a "
                + "multi-statement request or any single-statement<BR>"
                + "request except a DDL statement, or an explicit "
                + "transaction.</html>");
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "explain request" command can be performed altough other
        // processes are running
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
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("NOT connected.",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        String request = null;
        boolean firstIteration = true;
        while (request == null) {
            if (firstIteration) {
                request = ApplicationFrame.getInstance().getTextComponent()
                        .getSelectedText();
                firstIteration = false;
            }
            if (request == null) {
                request = ApplicationFrame.getInstance().getTextComponent()
                        .getText();
                if (request.trim().length() == 0) {
                    request = Dialog
                            .showInputDialog("Insert the request to analyze: ");
                    if (request == null) {
                        return;
                    }
                }
            }
        }

        String sqlQuery = "EXPLAIN " + request + ";";
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
        try {
            resultSet = statement.executeQuery();
        } finally {
            waitingDialog.hide();
        }
        ApplicationFrame
                .getInstance()
                .getConsole()
                .println(
                        Utilities.LINE_SEPARATOR + "\nExplanation\n"
                                + Utilities.LINE_SEPARATOR);
        while (resultSet.next()) {
            Object obj = resultSet.getString(1);
            String executionPlan = obj.toString().trim();
            ApplicationFrame.getInstance().getConsole().println(executionPlan);
        }
        statement.close();
        resultSet.close();
    }
}