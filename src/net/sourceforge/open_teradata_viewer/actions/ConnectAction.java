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
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

public class ConnectAction extends CustomAction {

    private static final long serialVersionUID = -1992828047874871010L;
    
    private final LanguageManager langManager = LanguageManager.getInstance();

    protected ConnectAction() {
        super(LanguageManager.getInstance().getString("action.connect"), "connect.png", null, null);
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        langManager.addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.connect"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {     
        ((DisconnectAction) Actions.DISCONNECT).saveDefaultOwner();
        Vector<ConnectionData> connectionDatas = Config.getDatabases();
        
        // Create UI components on EDT
        final java.util.concurrent.atomic.AtomicReference<JList<?>> listRef = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<JScrollPane> scrollPaneRef = new java.util.concurrent.atomic.AtomicReference<>();
        
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JList<?> tempList = new JList<Object>(connectionDatas);
                    tempList.addMouseListener(ConnectAction.this);
                    tempList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    JScrollPane tempScrollPane = new JScrollPane(tempList);
                    
                    listRef.set(tempList);
                    scrollPaneRef.set(tempScrollPane);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        final JList<?> list = listRef.get();
        final JScrollPane scrollPane = scrollPaneRef.get();
        
        // Create dialog with fresh localized strings
        Object value = Dialog.show(langManager.getString("dialog.connections.title"), scrollPane, Dialog.PLAIN_MESSAGE,
                new Object[] { "button.connect", "button.cancel", 
                              "button.add", "button.edit", 
                              "button.duplicate", "button.delete" }, 
                "button.connect");
        if (langManager.getString("button.connect").equals(value)) {
            if (!list.isSelectionEmpty()) {
                Actions.DISCONNECT.performThreaded(e);
                ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                boolean connected = false;
                while (!connected) {
                    try {
                        ApplicationFrame app = ApplicationFrame.getInstance();
                        app.getConsole().println(langManager.getString("message.connecting"));
                        connectionData.connect();
                        if (connectionData.getConnection() == null) {
                            performThreaded(e);
                            return;
                        }
                        app.getConsole().println(langManager.getString("message.connected"));

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
                            Dialog.show(langManager.getString("dialog.warning.title"), warnings.getMessage(), Dialog.WARNING_MESSAGE,
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
        } else if (langManager.getString("button.add").equals(value)) {
            ConnectionData connectionData = newConnectionWizard();
            if (editConnection(connectionData)) {
                connectionDatas.add(connectionData);
                Config.saveDatabases(connectionDatas);
            }
            performThreaded(e);
        } else if (langManager.getString("button.edit").equals(value)) {
            if (!list.isSelectionEmpty()) {
                ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                if (editConnection(connectionData)) {
                    Config.saveDatabases(connectionDatas);
                }
            }
            performThreaded(e);
        } else if (langManager.getString("button.duplicate").equals(value)) {
            if (!list.isSelectionEmpty()) {
                ConnectionData connectionData = (ConnectionData) list.getSelectedValue();
                connectionData = (ConnectionData) connectionData.clone();
                if (editConnection(connectionData)) {
                    connectionDatas.add(connectionData);
                    Config.saveDatabases(connectionDatas);
                }
            }
            performThreaded(e);
        } else if (langManager.getString("button.delete").equals(value)) {
            if (!list.isSelectionEmpty()) {
                if (Dialog.YES_OPTION == Dialog.show(langManager.getString("dialog.delete_connection.title"), 
                        langManager.getString("dialog.confirm_delete"), Dialog.WARNING_MESSAGE,
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
        final ConnectionData connectionData = new ConnectionData();
        final IOException[] ioExceptionHolder = new IOException[1];

        try {
            // Synchronously dispatch dialog creation and user inputs to the Event
        	// Dispatch Thread (EDT) to satisfy look-and-feel thread-safety
        	// requirements
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object db = Dialog.show(langManager.getString("dialog.new_connection.title"), langManager.getString("dialog.choose_database"), Dialog.PLAIN_MESSAGE, 
                                new Object[] { "database.teradata", "database.oracle", 
                                              "database.db2", "database.mysql",
                                              "database.sqlite", "database.hsqldb",
                                              "database.h2", "database.derby",
                                              "database.sqlserver", "database.other" }, null);
                        if (langManager.getString("database.teradata").equals(db)) {
                            String serverName = checkString(JOptionPane.showInputDialog(langManager.getString("message.server_name")));
                            String databaseName = checkString(JOptionPane.showInputDialog(langManager.getString("message.database_name")));
                            connectionData.setName(databaseName);
                            connectionData.setUrl(String.format(
                                    "jdbc:teradata://%s/database=%s,TMODE=ANSI,DBS_PORT=1025,CHARSET=UTF8,LOGMECH=LDAP,LOGDATA=<user>@@<password>",
                                    serverName, databaseName));
                        } else if (langManager.getString("database.oracle").equals(db)) {
                            String serverName = checkString(JOptionPane.showInputDialog(langManager.getString("message.server_name")));
                            String databaseName = checkString(JOptionPane.showInputDialog(langManager.getString("message.database_name")));
                            connectionData.setName(databaseName);
                            connectionData.setUrl(String.format("jdbc:oracle:thin:@%s:1521:%s", serverName, databaseName));
                        } else if (langManager.getString("database.db2").equals(db)) {
                            String serverName = checkString(JOptionPane.showInputDialog(langManager.getString("message.server_name")));
                            String databaseName = checkString(JOptionPane.showInputDialog(langManager.getString("message.database_name")));
                            String portNumber = checkString(JOptionPane.showInputDialog(langManager.getString("message.port_number"), "50000"));
                            connectionData.setName(databaseName);
                            connectionData.setUrl(String.format("jdbc:db2://%s:%s/%s", serverName, portNumber, databaseName));
                        } else if (langManager.getString("database.mysql").equals(db)) {
                            String serverName = checkString(JOptionPane.showInputDialog(langManager.getString("message.server_name")));
                            String databaseName = checkString(JOptionPane.showInputDialog(langManager.getString("message.database_name")));
                            connectionData.setName(databaseName);
                            connectionData.setUrl(String.format("jdbc:mysql://%s/%s", serverName, databaseName));
                        } else if (langManager.getString("database.sqlite").equals(db)) {
                            String fileName = checkString(
                                    JOptionPane.showInputDialog(langManager.getString("message.file_name"), new File("/sqlite.db").getCanonicalPath()));
                            connectionData.setName(new File(fileName).getName());
                            connectionData.setUrl(String.format("jdbc:sqlite:%s", fileName));
                        } else if (langManager.getString("database.hsqldb").equals(db)) {
                            String fileName = checkString(
                                    JOptionPane.showInputDialog(langManager.getString("message.file_name"), new File("/hsqldb").getCanonicalPath()));
                            connectionData.setName(new File(fileName).getName());
                            connectionData.setUrl(String.format("jdbc:hsqldb:%s", fileName));
                            connectionData.setUser("sa");
                        } else if (langManager.getString("database.h2").equals(db)) {
                            String fileName = checkString(
                                    JOptionPane.showInputDialog(langManager.getString("message.file_name"), new File("/h2db").getCanonicalPath()));
                            connectionData.setName(new File(fileName).getName());
                            connectionData.setUrl(String.format("jdbc:h2:%s", fileName));
                        } else if (langManager.getString("database.derby").equals(db)) {
                            String fileName = checkString(
                                    JOptionPane.showInputDialog(langManager.getString("message.file_name"), new File("/derbydb").getCanonicalPath()));
                            connectionData.setName(new File(fileName).getName());
                            connectionData.setUrl(String.format("jdbc:derby:%s", fileName));
                        } else if (langManager.getString("database.sqlserver").equals(db)) {
                            String serverName = checkString(JOptionPane.showInputDialog(langManager.getString("message.server_name")));
                            String databaseName = checkString(JOptionPane.showInputDialog(langManager.getString("message.database_name")));
                            connectionData.setName(databaseName);
                            connectionData.setUrl(String.format("jdbc:jtds:sqlserver://%s:1433/%s", serverName, databaseName));
                        }
                    } catch (IOException e) {
                        ioExceptionHolder[0] = e;
                    }
                }
            });
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new RuntimeException(cause);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread execution was interrupted while waiting for user interaction.", e);
        }

        // Rethrow the caught checked exception if an I/O error occurred inside the EDT block.
        if (ioExceptionHolder[0] != null) {
            throw ioExceptionHolder[0];
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
        final java.util.concurrent.atomic.AtomicBoolean result = new java.util.concurrent.atomic.AtomicBoolean(false);

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JPanel panel = new JPanel(new GridBagLayout());
                    GridBagConstraints c = new GridBagConstraints();
                    c.anchor = GridBagConstraints.WEST;
                    c.fill = GridBagConstraints.BOTH;
                    c.insets = new Insets(2, 2, 2, 2);
                    c.gridy++;
                    panel.add(new JLabel(langManager.getString("label.name")), c);
                    JTextField name = new JTextField(connectionData.getName(), 50);
                    panel.add(name, c);
                    c.gridy++;
                    panel.add(new JLabel(langManager.getString("label.url")), c);
                    final JTextField url = new JTextField(connectionData.getUrl());
                    panel.add(url, c);
                    c.gridy++;
                    panel.add(new JLabel(langManager.getString("label.username")), c);
                    JTextField user = new JTextField(connectionData.getUser());
                    panel.add(user, c);
                    c.gridy++;
                    panel.add(new JLabel(langManager.getString("label.password")), c);
                    JPasswordField password = new JPasswordField(connectionData.getPassword());
                    panel.add(password, c);
                    Object i = Dialog.show(langManager.getString("dialog.connect.title"), panel, Dialog.PLAIN_MESSAGE,
                            new Object[]{"button.ok", "button.cancel"},
                            "button.ok");
                    connectionData.setName(name.getText());
                    connectionData.setUrl(url.getText());
                    connectionData.setUser(user.getText().trim());
                    // getPassword() (char[]) is used instead of getText()
                    // (String): a String lingers in JVM memory until GC,
                    // while a char[] can be wiped right after use - the
                    // standard, documented way to handle a JPasswordField's
                    // value.
                    connectionData.setPassword(new String(password.getPassword()));
                    if (langManager.getString("button.ok").equals(i) && connectionData.getName().trim().isEmpty()) {
                        try {
                            Dialog.show(langManager.getString("dialog.empty_name.title"),
                                    langManager.getString("dialog.empty_name.message"), Dialog.ERROR_MESSAGE,
                                    new Object[]{"dialog.empty_name.button"}, null);
                            boolean okay;
                            try {
                                okay = editConnection(connectionData, true);
                            } catch (Exception ex) {
                                okay = false;
                            }
                            if (!nested) {
                                if (okay) {
                                    Dialog.show(null, langManager.getString("dialog.name_added.message"),
                                            Dialog.INFORMATION_MESSAGE,
                                            new Object[]{"dialog.name_added.button"}, null);
                                } else {
                                    Dialog.show(null, langManager.getString("dialog.no_name.message"),
                                            Dialog.QUESTION_MESSAGE,
                                            new Object[]{"dialog.no_name.button"}, null);
                                }
                            }
                            result.set(okay);
                        } catch (Exception ex) {
                            result.set(false);
                        }
                    } else {
                        result.set(langManager.getString("button.ok").equals(i));
                    }
                }
            });
        } catch (java.lang.reflect.InvocationTargetException ite) {
            throw new Exception("Error in editConnection", ite);
        }

        return result.get();
    }
}