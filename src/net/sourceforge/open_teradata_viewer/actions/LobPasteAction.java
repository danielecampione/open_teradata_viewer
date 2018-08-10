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

import javax.swing.JTable;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.ResultSetTable;
import net.sourceforge.open_teradata_viewer.WaitingDialog;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class LobPasteAction extends CustomAction {

    private static final long serialVersionUID = 3197672902100995655L;

    protected LobPasteAction() {
        super("Paste Lob", "paste.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        boolean hasResultSet = isConnected
                && Context.getInstance().getResultSet() != null;
        boolean isLobSelected = hasResultSet
                && ResultSetTable.isLob(ResultSetTable.getInstance()
                        .getSelectedColumn());
        setEnabled(isLobSelected);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        boolean hasResultSet = isConnected
                && Context.getInstance().getResultSet() != null;
        boolean isLobSelected = hasResultSet
                && ResultSetTable.isLob(ResultSetTable.getInstance()
                        .getSelectedColumn());
        if (!isLobSelected) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("The field is NOT a Lob.",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        JTable table = ResultSetTable.getInstance();
        if (table.getSelectedRowCount() == 1) {
            ResultSetTable.getInstance().setTableValue(
                    Context.getInstance().getSavedLobs()[0]);
            ResultSetTable.getInstance().editingStopped(null);
        } else {
            WaitingDialog waitingDialog = new WaitingDialog(null);
            try {
                int[] selectedRows = table.getSelectedRows();
                waitingDialog.setText(String
                        .format("0/%d", selectedRows.length));
                for (int i = 0; i < selectedRows.length
                        && waitingDialog.isVisible(); i++) {
                    int selectedRow = selectedRows[i];
                    table.getSelectionModel().setSelectionInterval(selectedRow,
                            selectedRow);
                    ResultSetTable.getInstance().setTableValue(
                            Context.getInstance().getSavedLobs()[i]);
                    ResultSetTable.getInstance().editingStopped(null);
                    waitingDialog.setText(String.format("%d/%d", i + 1,
                            selectedRows.length));
                }
            } finally {
                waitingDialog.hide();
            }
        }
    }
}