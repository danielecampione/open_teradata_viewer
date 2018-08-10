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
import java.sql.SQLException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class ConnectAction extends CustomAction {

    private static final long serialVersionUID = 604086939550655893L;

    protected ConnectAction() {
        super("Connect", "connect.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(!isConnected);
    }

    @Override
    protected void performThreaded(ActionEvent ae) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            try {
                ApplicationFrame.getInstance().initConnectionManager();
            } catch (ClassNotFoundException e) {
                throw e;
            } catch (InstantiationException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw e;
            } catch (SQLException e) {
                throw e;
            }
            if (ApplicationFrame.getInstance().connectionManager
                    .checkConnection()) {
                ApplicationFrame.getInstance().changeLog.append("connected.\n");
            } else {
                Context.getInstance().setConnectionData(null);
            }
            Actions.getInstance().validateActions();
            ApplicationFrame.getInstance().updateTitle();
        }
    }
}
