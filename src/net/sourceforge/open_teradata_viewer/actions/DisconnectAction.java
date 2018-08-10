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
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.ConnectionData;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class DisconnectAction extends CustomAction implements AncestorListener {

    private static final long serialVersionUID = 7202373336183800439L;

    protected DisconnectAction() {
        super("Disconnect", "disconnect.png", null, null);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        if (Context.getInstance().getConnectionData() != null) {
            saveDefaultOwner();
            ApplicationFrame.getInstance().destroyObjectChooser();
            try {
                Connection connection = Context.getInstance()
                        .getConnectionData().getConnection();
                if (!connection.isClosed()) {
                    connection.rollback();
                    connection.close();
                }
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
            Context.getInstance().setConnectionData(null);
            Context.getInstance().setResultSet(null);
            ApplicationFrame.getInstance().updateTitle();
            Actions.getInstance().validateActions();
            ApplicationFrame.getInstance().changeLog.append("disconnected.\n");
        }
    }

    /**
     * Remember last selected schema
     */
    public void saveDefaultOwner() throws Exception {
        ConnectionData thisConnectionData = Context.getInstance()
                .getConnectionData();
        if (thisConnectionData != null
                && ApplicationFrame.getInstance().getObjectChooser() != null) {
            String selectedOwner = ApplicationFrame.getInstance()
                    .getObjectChooser().getSelectedOwner();
            if (selectedOwner != null
                    && !selectedOwner.equals(thisConnectionData
                            .getDefaultOwner())) {
                thisConnectionData.setDefaultOwner(selectedOwner);
                Vector<ConnectionData> connectionDatas = Config.getDatabases();
                for (ConnectionData connectionData : connectionDatas) {
                    if (connectionData.getName().equals(
                            thisConnectionData.getName())) {
                        connectionData.setDefaultOwner(selectedOwner);
                    }
                }
                Config.saveDatabases(connectionDatas);
            }
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        // Kills the application in 10 seconds in case disconnecting will hang
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 10000);
        try {
            performThreaded(null);
        } catch (Throwable t) {
            ExceptionDialog.hideException(t);
        }
        System.exit(0);
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }
}
