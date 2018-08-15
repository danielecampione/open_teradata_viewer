/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2014, D. Campione
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
import java.io.File;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.FileIO;
import net.sourceforge.open_teradata_viewer.ResultSetTable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LobImportAction extends CustomAction {

    private static final long serialVersionUID = 574573571483820092L;

    protected LobImportAction() {
        super("Import Lob..", "import.png", null, null);
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
        File file = FileIO.openFile();
        if (file != null) {
            ResultSetTable.getInstance().setTableValue(FileIO.readFile(file));
            ResultSetTable.getInstance().editingStopped(null);
        }
    }
}