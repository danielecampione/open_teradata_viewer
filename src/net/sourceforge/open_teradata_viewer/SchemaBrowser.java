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

package net.sourceforge.open_teradata_viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SchemaBrowser extends JTree {

    private static final long serialVersionUID = -6971889243415387652L;

    public SchemaBrowser(ConnectionData connectionData) {
        super(new ObjectNode(connectionData));
        ((DefaultTreeCellRenderer) getCellRenderer()).setLeafIcon(null);
        ((DefaultTreeCellRenderer) getCellRenderer()).setOpenIcon(null);
        ((DefaultTreeCellRenderer) getCellRenderer()).setClosedIcon(null);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private static class ObjectNode extends DynamicUtilTreeNode {

        private static final long serialVersionUID = -7209470034361905060L;
        private ConnectionData connectionData;

        public ObjectNode(ConnectionData newConnectionData) {
            this(newConnectionData.getName(), new Object[0], newConnectionData);
        }

        public ObjectNode(Object value, Object children,
                ConnectionData newConnectionData) {
            super(value, children);
            this.connectionData = newConnectionData;
        }

        @Override
        protected void loadChildren() {
            try {
                loadedChildren = true;
                if (allowsChildren) {
                    addChildren();
                }
            } catch (Throwable t) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(t.getMessage(),
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                String relationName = toString().toUpperCase();
                String sqlQuery = "HELP TABLE " + relationName;
                ResultSet resultSet = null;
                Connection connection = connectionData.getConnection();
                try {
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
                    resultSet = statement.executeQuery();
                    waitingDialog.hide();

                    while (resultSet.next()) {
                        String columnName = resultSet.getString(1).trim();
                        if (Utilities.isEmpty(columnName)
                                || columnName.trim().length() == 0) {
                            columnName = "";
                        }
                        add(columnName, false);
                    }
                    statement.close();
                    resultSet.close();
                } catch (SQLException sqle) {
                    ExceptionDialog.ignoreException(sqle);
                }
            }
        }

        private void addChildren() throws SQLException {
            switch (getLevel()) {
                case 0 :
                    if (connectionData.getConnection().getMetaData()
                            .supportsSchemasInTableDefinitions()) {
                        addQuery(connectionData.getConnection().getMetaData()
                                .getSchemas(), true, 1);
                    } else if (connectionData.getConnection().getMetaData()
                            .supportsCatalogsInTableDefinitions()) {
                        addQuery(connectionData.getConnection().getMetaData()
                                .getCatalogs(), true, 1);
                    } else {
                        add("Schema", true);
                    }
                    break;
                case 1 :
                    add("TABLES", true);
                    add("VIEWS", true);
                    add("PROCEDURES", true);
                    break;
                case 2 :
                    String owner = getParent().toString();
                    String type = toString();
                    if ("TABLES".equals(type)) {
                        if (connectionData.getConnection().getMetaData()
                                .supportsCatalogsInTableDefinitions()) {
                            addQuery(
                                    connectionData
                                            .getConnection()
                                            .getMetaData()
                                            .getTables(
                                                    owner,
                                                    null,
                                                    null,
                                                    new String[]{"TABLE",
                                                            "SYSTEM TABLE"}),
                                    true, 3);
                        } else {
                            addQuery(
                                    connectionData
                                            .getConnection()
                                            .getMetaData()
                                            .getTables(
                                                    null,
                                                    owner,
                                                    null,
                                                    new String[]{"TABLE",
                                                            "SYSTEM TABLE"}),
                                    true, 3);
                        }
                    } else if ("VIEWS".equals(type)) {
                        if (connectionData.getConnection().getMetaData()
                                .supportsCatalogsInTableDefinitions()) {
                            addQuery(
                                    connectionData
                                            .getConnection()
                                            .getMetaData()
                                            .getTables(owner, null, null,
                                                    new String[]{"VIEW"}),
                                    true, 3);
                        } else {
                            addQuery(
                                    connectionData
                                            .getConnection()
                                            .getMetaData()
                                            .getTables(null, owner, null,
                                                    new String[]{"VIEW"}),
                                    true, 3);
                        }
                    } else if ("PROCEDURES".equals(type)) {
                        if (connectionData.getConnection().getMetaData()
                                .supportsCatalogsInTableDefinitions()) {
                            addQuery(
                                    connectionData.getConnection()
                                            .getMetaData()
                                            .getProcedures(owner, null, null),
                                    true, 3);
                        } else {
                            addQuery(
                                    connectionData.getConnection()
                                            .getMetaData()
                                            .getProcedures(null, owner, null),
                                    true, 3);
                        }
                    }
                    break;
                case 3 :
                    String table = toString();
                    if (connectionData.getConnection().getMetaData()
                            .supportsCatalogsInTableDefinitions()) {
                        addQuery(
                                connectionData
                                        .getConnection()
                                        .getMetaData()
                                        .getColumns(
                                                getParent().getParent()
                                                        .toString(), null,
                                                table, null), false, 4);
                    } else {
                        addQuery(
                                connectionData
                                        .getConnection()
                                        .getMetaData()
                                        .getColumns(
                                                null,
                                                getParent().getParent()
                                                        .toString(), table,
                                                null), false, 4);
                    }
                    break;
                default :
            }
        }

        private void addQuery(ResultSet resultSet, boolean children,
                int columnIndex) throws SQLException {
            while (resultSet.next()) {
                add(resultSet.getString(columnIndex), children);
            }
        }

        private void add(String s, boolean children) {
            add(new ObjectNode(s, children ? new Object[0] : null,
                    connectionData));
        }
    }

    public String[] getSelectedItems() {
        TreePath[] selectionPaths = getSelectionPaths();
        String[] selectedItems = new String[selectionPaths.length];
        for (int i = 0; i < selectionPaths.length; i++) {
            TreePath selectionPath = selectionPaths[i];
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) selectionPath
                    .getLastPathComponent();
            String s = Context.getInstance().getConnectionData()
                    .checkMixedCaseQuotedIdentifier(treeNode.toString());
            if (treeNode.getLevel() == 3) {
                TreeNode parent = treeNode.getParent().getParent();
                if (!"Schema".equals(parent.toString())) {
                    s = parent + "." + s;
                }
            }
            selectedItems[i] = s;
        }
        return selectedItems;
    }

    public String getSelectedOwner() {
        TreePath selectionPath = getSelectionPath();
        if (selectionPath != null) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) selectionPath
                    .getLastPathComponent();
            if (treeNode.getLevel() > 1) {
                while (treeNode.getLevel() > 1) {
                    treeNode = (DefaultMutableTreeNode) treeNode.getParent();
                }
                return treeNode.toString();
            }
        }
        return null;
    }

    public void expand(String[] path) {
        TreeNode node = (TreeNode) getModel().getRoot();
        int row = 0;
        for (String aPath : path) {
            for (int j = 0; j < node.getChildCount(); j++) {
                TreeNode child = node.getChildAt(j);
                if (aPath.equals(child.toString())) {
                    row += j + 1;
                    expandRow(row);
                    node = child;
                    break;
                }
            }
        }
        setSelectionRow(row + 1);
        scrollRowToVisible(row - 1);
    }
}