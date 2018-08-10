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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.History;
import net.sourceforge.open_teradata_viewer.ResultSetTable;
import net.sourceforge.open_teradata_viewer.WaitingDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RunAction extends CustomAction {

    private static final long serialVersionUID = -502870390309110470L;

    protected RunAction() {
        super("Run", "run.png", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                KeyEvent.CTRL_DOWN_MASK), null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
    }

    @Override
    protected void performThreaded(ActionEvent ae) throws Exception {
        String sql = ApplicationFrame.getInstance().getText();
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("NOT connected.",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        if (sql.trim().length() == 0) {
            return;
        }
        if (sql.trim().endsWith(";")) {
            String sqlWithoutSemicolon = sql.trim().substring(0,
                    sql.trim().length() - 1);
            if (!sqlWithoutSemicolon.trim().toLowerCase().endsWith("end")) {
                sql = sqlWithoutSemicolon;
            }
        }
        String originalSql = sql;
        History.getInstance().add(sql);
        Actions.getInstance().validateTextActions();
        Vector<String> columnIdentifiers = new Vector<String>();
        Vector<Vector> dataVector = new Vector<Vector>();
        int[] columnTypes;
        String[] columnTypeNames;
        PreparedStatement statement = createStatement(Context.getInstance()
                .getConnectionData().getConnection(), sql);
        statement.setMaxRows(Context.getInstance().getFetchLimit());

        String[] bindVariables = handleBindVariables(statement);

        final Statement[] statements = new Statement[]{statement};
        final boolean[] executed = {false};
        Runnable onCancel = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!executed[0]) {
                        // Don't cancel when we're already going through the
                        // result set
                        statements[0].cancel();
                    }
                } catch (Throwable t) {
                    ExceptionDialog.hideException(t);
                }
            }
        };
        WaitingDialog waitingDialog = new WaitingDialog(onCancel);
        waitingDialog.setText("Executing statement");
        try {
            boolean hasResultSet;
            try {
                hasResultSet = statement.execute();
            } catch (SQLException sqle) {
                if (statement.getResultSetConcurrency() != ResultSet.CONCUR_READ_ONLY) {
                    // Try read-only and without modifications
                    statement = Context.getInstance().getConnectionData()
                            .getConnection().prepareStatement(originalSql);
                    // Bind variables
                    handleBindVariables(statement, bindVariables);
                    statements[0] = statement;
                    hasResultSet = statement.execute();
                } else {
                    throw sqle;
                }
            }
            executed[0] = true;
            if (hasResultSet) {
                ResultSet resultSet = statement.getResultSet();
                Context.getInstance().setResultSet(resultSet);
                int columnCount = resultSet.getMetaData().getColumnCount();
                columnTypes = new int[columnCount];
                columnTypeNames = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnIdentifiers.add(resultSet.getMetaData()
                            .getColumnName(i + 1));
                    columnTypes[i] = resultSet.getMetaData().getColumnType(
                            i + 1);
                    columnTypeNames[i] = resultSet.getMetaData()
                            .getColumnTypeName(i + 1);
                }
                while (waitingDialog.isVisible() && resultSet.next()) {
                    Vector<Object> row = new Vector<Object>(columnCount + 1);
                    for (int i = 0; i < columnCount; i++) {
                        try {
                            Object object = resultSet.getObject(i + 1);
                            // System.out.println((i + 1) + " "
                            //     + resultSet.getMetaData().getColumnName(i+1) + " - "
                            //     + resultSet.getMetaData().getColumnType(i+1) + " - "
                            //     + resultSet.getMetaData().getColumnTypeName(i+1) + " - "
                            //     + resultSet.getMetaData().getColumnClassName(i+1) + " - \"" + object + "\"");
                            row.add(object);
                        } catch (Exception e) {
                            row.add("###");
                            System.err
                                    .format("Unable to retrieve value for row %s col %s",
                                            dataVector.size() + 1, i + 1);
                            ExceptionDialog.hideException(e);
                        }
                    }
                    dataVector.add(row);
                    waitingDialog.setText(String.format("%d rows retrieved",
                            dataVector.size()));
                }
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                String.format("[%d rows retrieved]",
                                        dataVector.size()));
            } else {
                Context.getInstance().setResultSet(null);
                int updateCount = statement.getUpdateCount();
                if (updateCount != -1) {
                    Vector<Object> row = new Vector<Object>(1);
                    row.add(Integer.toString(updateCount));
                    dataVector.add(row);
                    columnIdentifiers.add("Rows updated");
                    columnTypes = new int[]{Types.INTEGER};
                    columnTypeNames = new String[1];
                } else if (statement instanceof CallableStatement) {
                    for (int i = 0; i < bindVariables.length; i++) {
                        try {
                            Object o = ((CallableStatement) statement)
                                    .getObject(i + 1);
                            Vector<Object> row = new Vector<Object>(1);
                            row.add(o);
                            dataVector.add(row);
                        } catch (SQLException sqle) {
                            // Not an output parameter
                            ExceptionDialog.ignoreException(sqle);
                        }
                    }
                    columnIdentifiers.add("Statement executed");
                    columnTypes = new int[]{Types.VARCHAR};
                    columnTypeNames = new String[1];
                } else {
                    columnIdentifiers.add("Statement executed");
                    columnTypes = new int[]{Types.INTEGER};
                    columnTypeNames = new String[1];
                }
            }
        } finally {
            statement.close();
            waitingDialog.hide();
        }
        Context.getInstance().setQuery(originalSql);
        Context.getInstance().setColumnTypes(columnTypes);
        Context.getInstance().setColumnTypeNames(columnTypeNames);
        ResultSetTable.getInstance().setDataVector(dataVector,
                columnIdentifiers, waitingDialog.getExecutionTime());
        Actions.getInstance().validateActions();
    }

    private PreparedStatement createStatement(Connection connection, String sql)
            throws SQLException {
        boolean query = sql.trim().toLowerCase().startsWith("sel")
                || sql.trim().toLowerCase().startsWith("with");
        boolean call = sql.trim().toLowerCase().startsWith("call");
        PreparedStatement statement;
        if (query) {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.supportsResultSetConcurrency(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                statement = connection.prepareStatement(sql,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
            } else if (metaData
                    .supportsResultSetConcurrency(
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE)) {
                if (metaData
                        .supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
                    try {
                        statement = connection.prepareStatement(sql,
                                ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE,
                                ResultSet.CLOSE_CURSORS_AT_COMMIT);
                    } catch (SQLException sqle) {
                        statement = connection.prepareStatement(sql,
                                ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                    }
                } else {
                    statement = connection.prepareStatement(sql,
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                }
            } else {
                statement = connection.prepareStatement(sql);
            }
        } else if (call) {
            statement = connection.prepareCall(sql);
        } else {
            statement = connection.prepareStatement(sql);
        }
        return statement;
    }

    private String[] handleBindVariables(PreparedStatement statement) {
        try {
            ParameterMetaData metaData = statement.getParameterMetaData();
            String[] bindVariables = new String[metaData.getParameterCount()];
            for (int i = 0; i < metaData.getParameterCount(); i++) {
                bindVariables[i] = JOptionPane.showInputDialog(String.format(
                        "Bind variable %d", i + 1));
            }
            handleBindVariables(statement, bindVariables);
            return bindVariables;
        } catch (Exception e) {
            return new String[0];
        }
    }

    private void handleBindVariables(PreparedStatement statement,
            String[] bindVariables) throws SQLException {
        for (int i = 0; i < bindVariables.length; i++) {
            statement.setObject(i + 1, bindVariables[i]);
        }
    }
}