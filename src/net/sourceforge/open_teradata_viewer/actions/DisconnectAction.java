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

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class DisconnectAction extends CustomAction {

    private static final long serialVersionUID = 1837760644323823359L;

    protected DisconnectAction() {
        super("Disconnect", "disconnect.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (isConnected) {
            ApplicationFrame.getInstance().destroyObjectChooser();
            try {
                if (ApplicationFrame.getInstance().connectionManager != null) {
                    Connection connection = ApplicationFrame.getInstance().connectionManager
                            .getConnection();
                    if (connection != null) {
                        if (!connection.isClosed()) {
                            connection.rollback();
                            connection.close();
                        }
                    }
                }
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
            Context.getInstance().setConnectionData(null);
            Context.getInstance().setResultSet(null);
            Actions.getInstance().validateActions();
            ApplicationFrame.getInstance().updateTitle();

            ApplicationFrame.getInstance().changeLog.append("disconnected.\n");
        }
    }
}
