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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLWarning;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.ConnectionData;
import net.sourceforge.open_teradata_viewer.ConnectionData.DatabaseType;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

public class ConnectAction extends CustomAction {

    private static final long serialVersionUID = -1992828047874871010L;

    protected ConnectAction() {
        super("Connect", "connect.png", null, null);
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ((DisconnectAction) Actions.DISCONNECT).saveDefaultOwner();
        Vector<ConnectionData> connectionDatas = Config.getDatabases();
        final JList<?> list = new JList<Object>(connectionDatas);
        list.addMouseListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Object value = Dialog.show("Connections", new JScrollPane(list), Dialog.PLAIN_MESSAGE,
                new Object[] { "Connect", "Cancel", "Add", "Edit", "Duplicate", "Delete" }, "Connect");
        if ("Connect".equals(value)) {
            if (!list.isSelectionEmpty()) {
                Actions.DISCONNECT.performThreaded(e);
                ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                boolean connected = false;
                while (!connected) {
                    try {
                        ApplicationFrame app = ApplicationFrame.getInstance();
                        app.getConsole().println("connecting..");
                        connectionData.connect();
                        if (connectionData.getConnection() == null) {
                            performThreaded(e);
                            return;
                        }
                        app.getConsole().println("connected.");

                        String url = connectionData.getUrl().trim().toLowerCase();
                        if (url.startsWith("jdbc:teradata:")) { // Teradata
                            app.setDatabaseType(DatabaseType.TERADATA);
                        } else if (url.startsWith("jdbc:oracle:")) { // ORACLE
                            app.setDatabaseType(DatabaseType.ORACLE);
                        } else {
                            app.setDatabaseType(DatabaseType.UNKNOWN);
                        }

                        SQLWarning warnings = connectionData.getConnection().getWarnings();
                        while (warnings != null) {
                            Dialog.show("Warning", warnings.getMessage(), Dialog.WARNING_MESSAGE,
                                    Dialog.DEFAULT_OPTION);
                            warnings = warnings.getNextWarning();
                        }

                        Context.getInstance().setConnectionData(connectionData);
                        app.updateTitle();
                        Actions.getInstance().validateActions();
                        app.initializeObjectChooser(connectionData);
                        connected = true;
                    } catch (Throwable t) {
                        ExceptionDialog.showException(t);
                        if (editConnection(connectionData)) {
                            Config.saveDatabases(connectionDatas);
                        } else {
                            performThreaded(e);
                            return;
                        }
                    }
                }
            }
        } else if ("Add".equals(value)) {
            ConnectionData connectionData = newConnectionWizard();
            if (editConnection(connectionData)) {
                connectionDatas.add(connectionData);
                Config.saveDatabases(connectionDatas);
            }
            performThreaded(e);
        } else if ("Edit".equals(value)) {
            if (!list.isSelectionEmpty()) {
                ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                if (editConnection(connectionData)) {
                    Config.saveDatabases(connectionDatas);
                }
            }
            performThreaded(e);
        } else if ("Duplicate".equals(value)) {
            if (!list.isSelectionEmpty()) {
                ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                connectionData = (ConnectionData) connectionData.clone();
                if (editConnection(connectionData)) {
                    connectionDatas.add(connectionData);
                    Config.saveDatabases(connectionDatas);
                }
            }
            performThreaded(e);
        } else if ("Delete".equals(value)) {
            if (!list.isSelectionEmpty()) {
                if (Dialog.YES_OPTION == Dialog.show("Delete connection", "Are you sure?", Dialog.WARNING_MESSAGE,
                        Dialog.YES_NO_OPTION)) {
                    ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                    connectionDatas.remove(connectionData);
                    Config.saveDatabases(connectionDatas);
                }
            }
            performThreaded(e);
        }
    }

    private ConnectionData newConnectionWizard() throws IOException {
        ConnectionData connectionData = new ConnectionData();
        Object db = Dialog.show("New Connection", "Choose database", Dialog.PLAIN_MESSAGE, new Object[] { "Teradata",
                "Oracle", "DB2", "MySQL", "SQLite", "HSQLDB", "H2", "Derby", "SQL Server", "Other" }, null);
        if ("Teradata".equals(db)) {
            String serverName = checkString(JOptionPane.showInputDialog("Server name"));
            String databaseName = checkString(JOptionPane.showInputDialog("Database name"));
            connectionData.setName(databaseName);
            connectionData.setUrl(String.format(
                    "jdbc:teradata://%s/database=%s,TMODE=ANSI,DBS_PORT=1025,CHARSET=UTF8,LOGMECH=LDAP,LOGDATA=<user>@@<password>",
                    serverName, databaseName));
        } else if ("Oracle".equals(db)) {
            String serverName = checkString(JOptionPane.showInputDialog("Server name"));
            String databaseName = checkString(JOptionPane.showInputDialog("Database name"));
            connectionData.setName(databaseName);
            connectionData.setUrl(String.format("jdbc:oracle:thin:@%s:1521:%s", serverName, databaseName));
        } else if ("DB2".equals(db)) {
            String serverName = checkString(JOptionPane.showInputDialog("Server name"));
            String databaseName = checkString(JOptionPane.showInputDialog("Database name"));
            String portNumber = checkString(JOptionPane.showInputDialog("Port number", "50000"));
            connectionData.setName(databaseName);
            connectionData.setUrl(String.format("jdbc:db2://%s:%s/%s", serverName, portNumber, databaseName));
        } else if ("MySQL".equals(db)) {
            String serverName = checkString(JOptionPane.showInputDialog("Server name"));
            String databaseName = checkString(JOptionPane.showInputDialog("Database name"));
            connectionData.setName(databaseName);
            connectionData.setUrl(String.format("jdbc:mysql://%s/%s", serverName, databaseName));
        } else if ("SQLite".equals(db)) {
            String fileName = checkString(
                    JOptionPane.showInputDialog("File name", new File("/sqlite.db").getCanonicalPath()));
            connectionData.setName(new File(fileName).getName());
            connectionData.setUrl(String.format("jdbc:sqlite:%s", fileName));
        } else if ("HSQLDB".equals(db)) {
            String fileName = checkString(
                    JOptionPane.showInputDialog("File name", new File("/hsqldb").getCanonicalPath()));
            connectionData.setName(new File(fileName).getName());
            connectionData.setUrl(String.format("jdbc:hsqldb:%s", fileName));
            connectionData.setUser("sa");
        } else if ("H2".equals(db)) {
            String fileName = checkString(
                    JOptionPane.showInputDialog("File name", new File("/h2db").getCanonicalPath()));
            connectionData.setName(new File(fileName).getName());
            connectionData.setUrl(String.format("jdbc:h2:%s", fileName));
        } else if ("Derby".equals(db)) {
            String fileName = checkString(
                    JOptionPane.showInputDialog("File name", new File("/derbydb").getCanonicalPath()));
            connectionData.setName(new File(fileName).getName());
            connectionData.setUrl(String.format("jdbc:derby:%s", fileName));
        } else if ("SQL Server".equals(db)) {
            String serverName = checkString(JOptionPane.showInputDialog("Server name"));
            String databaseName = checkString(JOptionPane.showInputDialog("Database name"));
            connectionData.setName(databaseName);
            connectionData.setUrl(String.format("jdbc:jtds:sqlserver://%s:1433/%s", serverName, databaseName));
        }
        return connectionData;
    }

    private String checkString(String s) {
        return s == null ? "" : s;
    }

    private boolean editConnection(ConnectionData connectionData) throws Exception {
        return editConnection(connectionData, false);
    }

    private boolean editConnection(ConnectionData connectionData, boolean nested) throws Exception {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridy++;
        panel.add(new JLabel("Name"), c);
        JTextField name = new JTextField(connectionData.getName(), 50);
        panel.add(name, c);
        c.gridy++;
        panel.add(new JLabel("URL"), c);
        final JTextField url = new JTextField(connectionData.getUrl());
        panel.add(url, c);
        c.gridy++;
        panel.add(new JLabel("User"), c);
        JTextField user = new JTextField(connectionData.getUser());
        panel.add(user, c);
        c.gridy++;
        panel.add(new JLabel("Password"), c);
        JTextField password = new JPasswordField(connectionData.getPassword());
        panel.add(password, c);
        int i = Dialog.show("Connection", panel, Dialog.PLAIN_MESSAGE, Dialog.OK_CANCEL_OPTION);
        connectionData.setName(name.getText());
        connectionData.setUrl(url.getText());
        connectionData.setUser(user.getText().trim());
        connectionData.setPassword(password.getText());
        if (Dialog.OK_OPTION == i && connectionData.getName().trim().isEmpty()) {
            Dialog.show("Empty name", "Why would you want an empty name?", Dialog.ERROR_MESSAGE,
                    new Object[] { "OK, I'm sorry, I will give it a name." }, null);
            boolean okay = editConnection(connectionData, true);
            if (!nested) {
                if (okay) {
                    Dialog.show(null, "That's more like it!", Dialog.INFORMATION_MESSAGE,
                            new Object[] { "You were right, it's better to give it a name." }, null);
                } else {
                    Dialog.show(null, "So you won't give it a name, won't you?", Dialog.QUESTION_MESSAGE,
                            new Object[] {
                                    "No, If I can't have a nameless connection, I rather have no connection at all!" },
                            null);
                }
            }
            return okay;
        } else {
            return Dialog.OK_OPTION == i;
        }
    }
}