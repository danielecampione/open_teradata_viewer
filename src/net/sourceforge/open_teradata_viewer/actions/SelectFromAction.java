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
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.Tools;
import net.sourceforge.open_teradata_viewer.WaitingDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SelectFromAction extends CustomAction {

    private static final long serialVersionUID = 5489248195539100092L;

    protected SelectFromAction() {
        super("SELECT * FROM ..", null, KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK), null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "select from" process can be performed altough other processes
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

        String relationName = null;
        boolean firstIteration = true;
        while (relationName == null) {
            if (firstIteration) {
                relationName = ApplicationFrame.getInstance()
                        .getTextComponent().getSelectedText();
                firstIteration = false;
            }
            if (relationName == null) {
                relationName = Dialog
                        .showInputDialog("Insert the table name: ");
                if (relationName == null) {
                    return;
                }
            }
            if (!canBeAnObjectName(relationName)) {
                relationName = null;
            }
        }

        relationName = relationName.toUpperCase();
        String sqlQuery = "HELP TABLE " + relationName;
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
        String text = "SELECT ";
        while (resultSet.next()) {
            String columnName = resultSet.getString(1);
            if (Tools.isEmpty(columnName) || columnName.trim().length() == 0) {
                columnName = "";
            }
            text += columnName.toUpperCase().trim();
            if (!resultSet.isLast()) {
                text += ", ";
            }
        }
        text += " FROM " + relationName;
        // Do not use the ApplicationFrame.setText(String s) method 
        ApplicationFrame.getInstance().getTextComponent().setText(text);
        statement.close();
        resultSet.close();
        Actions.FORMAT_SQL.actionPerformed(new ActionEvent(this, 0, null));
    }
}
