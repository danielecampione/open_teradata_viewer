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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.sourceforge.open_teradata_viewer.ConnectionData;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.Settings;
import net.sourceforge.open_teradata_viewer.SettingsException;
import net.sourceforge.open_teradata_viewer.SettingsKeys;
import net.sourceforge.open_teradata_viewer.ThreadedAction;

/**
 * The process thread relative to the modify of the connection data
 * 
 * @author D. Campione
 * 
 */
public class EditConnectionDataAction extends CustomAction {

    private static final long serialVersionUID = 7332349382230597272L;

    public EditConnectionDataAction() {
        super("Edit connection data..", "connect.png", null,
                "Edit the JDBC driver, the Server, the database port and the "
                        + "credentials to access the Teradata database.");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "edit connection data" process can be performed altough other
        // processes are running.
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
        if (isConnected) {
            Actions.DISCONNECT.actionPerformed(new ActionEvent(this, 0, null));
        }
        ConnectionData connectionData = Context.getInstance()
                .getConnectionData();
        if (connectionData == null) {
            connectionData = new ConnectionData();
            String actualUserID = Settings.load(SettingsKeys.ACTUAL_USERID_KEY,
                    SettingsKeys.ACTUAL_USERID_DEFAULT_VALUE);
            String database = Settings.load(SettingsKeys.DATABASE_KEY,
                    SettingsKeys.DATABASE_DEFAULT_VALUE);
            connectionData.setName(actualUserID + "@" + database);
        }

        Vector<String> connectionDatas = new Vector<String>(1, 1);
        String connectionName = connectionData.getName();
        connectionDatas.add(connectionName);
        final JList list = new JList(connectionDatas);
        Object value = Dialog.show("Connections", new JScrollPane(list),
                Dialog.PLAIN_MESSAGE, new Object[]{"Edit"}, "Edit");
        list.setSelectedIndex(0);
        if ("Edit".equals(value)) {
            if (!list.isSelectionEmpty()) {
                editConnection(connectionName);
            }
        }
    }

    private void editConnection(String connectionData) {
        String driverClass = Settings.load(SettingsKeys.JDBC_DRIVER_KEY,
                SettingsKeys.JDBC_DRIVER_DEFAULT_VALUE);
        String serverName = Settings.load(SettingsKeys.SERVER_NAME_KEY,
                SettingsKeys.SERVER_NAME_DEFAULT_VALUE);
        String databasePort = Settings.load(SettingsKeys.DATABASE_PORT_KEY,
                SettingsKeys.DATABASE_PORT_DEFAULT_VALUE);
        String database = Settings.load(SettingsKeys.DATABASE_KEY,
                SettingsKeys.DATABASE_DEFAULT_VALUE);
        String charsetEncoding = Settings.load(
                SettingsKeys.CHARSET_ENCODING_KEY,
                SettingsKeys.CHARSET_ENCODING_DEFAULT_VALUE);
        String logMechanism = Settings.load(SettingsKeys.LOGMECH_KEY,
                SettingsKeys.LOGMECH_DEFAULT_VALUE);
        String actualUserID = Settings.load(SettingsKeys.ACTUAL_USERID_KEY,
                SettingsKeys.ACTUAL_USERID_DEFAULT_VALUE);
        String actualPassword = Settings.load(SettingsKeys.ACTUAL_PASSWORD_KEY,
                SettingsKeys.ACTUAL_PASSWORD_DEFAULT_VALUE, true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridy++;
        panel.add(new JLabel("Name"), c);
        JTextField txtConnectionData = new JTextField(connectionData, 50);
        txtConnectionData.setEditable(false);
        panel.add(txtConnectionData, c);
        c.gridy++;
        panel.add(new JLabel("Server"), c);
        final JTextField txtServerName = new JTextField(serverName);
        panel.add(txtServerName, c);
        c.gridy++;
        panel.add(new JLabel("Port"), c);
        final JTextField txtDatabasePort = new JTextField(databasePort);
        panel.add(txtDatabasePort, c);
        c.gridy++;
        panel.add(new JLabel("Database"), c);
        final JTextField txtDatabase = new JTextField(database);
        panel.add(txtDatabase, c);
        c.gridy++;
        panel.add(new JLabel("User"), c);
        JTextField txtActualUserID = new JTextField(actualUserID);
        panel.add(txtActualUserID, c);
        c.gridy++;
        panel.add(new JLabel("Password"), c);
        JTextField txtActualPassword = new JPasswordField(actualPassword);
        panel.add(txtActualPassword, c);
        c.gridy++;
        panel.add(new JLabel("Driver"), c);
        JComboBox cmbBoxDriver = new JComboBox(new Object[]{driverClass});
        cmbBoxDriver.setEditable(false);
        cmbBoxDriver.setSelectedItem(driverClass);
        panel.add(cmbBoxDriver, c);
        c.gridy++;
        panel.add(new JLabel("Log mechanism"), c);
        JTextField txtLogMechanism = new JTextField(logMechanism);
        panel.add(txtLogMechanism, c);
        c.gridy++;
        panel.add(new JLabel("Charset"), c);
        JTextField txtCharsetEncoding = new JTextField(charsetEncoding);
        panel.add(txtCharsetEncoding, c);
        if (Dialog.OK_OPTION == (Dialog.show("Connection", panel,
                Dialog.PLAIN_MESSAGE, Dialog.OK_CANCEL_OPTION))) {
            try {
                Settings.write(SettingsKeys.JDBC_DRIVER_KEY,
                        cmbBoxDriver.getSelectedItem() == null
                                ? ""
                                : (String) cmbBoxDriver.getSelectedItem());
                Settings.write(SettingsKeys.SERVER_NAME_KEY,
                        txtServerName.getText());
                Settings.write(SettingsKeys.DATABASE_PORT_KEY,
                        txtDatabasePort.getText());
                Settings.write(SettingsKeys.DATABASE_KEY, txtDatabase.getText());
                Settings.write(SettingsKeys.CHARSET_ENCODING_KEY,
                        txtCharsetEncoding.getText());
                Settings.write(SettingsKeys.LOGMECH_KEY,
                        txtLogMechanism.getText());
                Settings.write(SettingsKeys.ACTUAL_USERID_KEY,
                        txtActualUserID.getText());
                Settings.write(SettingsKeys.ACTUAL_PASSWORD_KEY,
                        txtActualPassword.getText(), true);
                Actions.CONNECT.actionPerformed(new ActionEvent(this, 0, null));
            } catch (SettingsException e) {
            }
        } else {
            Context.getInstance().setConnectionData(null);
        }
    }
}
