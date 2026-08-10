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

import java.awt.Toolkit;
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
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 *
 *
 * @author D. Campione
 *
 */
public class RunAction extends CustomAction {

    private static final long serialVersionUID = -502870390309110470L;

    protected RunAction() {
        super(LanguageManager.getInstance().getString("action.run"), "run.png",
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.run"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent ae) throws Exception {
        String sql = ApplicationFrame.getInstance().getText();
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame.getInstance().getConsole().println("NOT connected.",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        if (sql.trim().length() == 0) {
            return;
        }
        sql = stripTrailingSlashTerminator(sql);
        if (sql.trim().endsWith(";")) {
            String sqlWithoutSemicolon = sql.trim().substring(0, sql.trim().length() - 1);
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
        PreparedStatement statement = createStatement(Context.getInstance().getConnectionData().getConnection(), sql);
        statement.setMaxRows(Context.getInstance().getFetchLimit());

        String[] bindVariables = handleBindVariables(statement);

        final Statement[] statements = new Statement[] { statement };
        final boolean[] executed = { false };
        Runnable onCancel = () -> {
            try {
                if (!executed[0]) {
                    statements[0].cancel();
                }
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
        };
        WaitingDialog waitingDialog = new WaitingDialog(onCancel);
        waitingDialog.setText(LanguageManager.getInstance().getString("message.executing_statement"));
        try {
            boolean hasResultSet;
            try {
                hasResultSet = statement.execute();
            } catch (SQLException sqle) {
                if (statement.getResultSetConcurrency() != ResultSet.CONCUR_READ_ONLY) {
                    statement.close();
                    statement = Context.getInstance().getConnectionData().getConnection().prepareStatement(originalSql);
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
                    columnIdentifiers.add(resultSet.getMetaData().getColumnName(i + 1));
                    columnTypes[i] = resultSet.getMetaData().getColumnType(i + 1);
                    columnTypeNames[i] = resultSet.getMetaData().getColumnTypeName(i + 1);
                }
                while (waitingDialog.isVisible() && resultSet.next()) {
                    Vector<Object> row = new Vector<Object>(columnCount + 1);
                    for (int i = 0; i < columnCount; i++) {
                        try {
                            Object object = resultSet.getObject(i + 1);
                            row.add(object);
                        } catch (Exception e) {
                            row.add("###");
                            System.err.format("Unable to retrieve value for row %s col %s", dataVector.size() + 1,
                                    i + 1);
                            ExceptionDialog.hideException(e);
                        }
                    }
                    dataVector.add(row);
                    waitingDialog.setText(String.format("%d rows retrieved", dataVector.size()));
                }
                ApplicationFrame.getInstance().getConsole()
                        .println(String.format("[%d rows retrieved]", dataVector.size()));
            } else {
                Context.getInstance().setResultSet(null);
                int updateCount = statement.getUpdateCount();
                if (updateCount != -1) {
                    Vector<Object> row = new Vector<Object>(1);
                    row.add(Integer.toString(updateCount));
                    dataVector.add(row);
                    columnIdentifiers.add(LanguageManager.getInstance().getString("label.rows_updated"));
                    columnTypes = new int[] { Types.INTEGER };
                    columnTypeNames = new String[1];
                } else if (statement instanceof CallableStatement) {
                    for (int i = 0; i < bindVariables.length; i++) {
                        try {
                            Object o = ((CallableStatement) statement).getObject(i + 1);
                            Vector<Object> row = new Vector<Object>(1);
                            row.add(o);
                            dataVector.add(row);
                        } catch (SQLException sqle) {
                            ExceptionDialog.ignoreException(sqle);
                        }
                    }
                    columnIdentifiers.add(LanguageManager.getInstance().getString("label.statement_executed"));
                    columnTypes = new int[] { Types.VARCHAR };
                    columnTypeNames = new String[1];
                } else {
                    columnIdentifiers.add(LanguageManager.getInstance().getString("label.statement_executed"));
                    columnTypes = new int[] { Types.INTEGER };
                    columnTypeNames = new String[1];
                }
            }
        } finally {
            waitingDialog.hide();
        }
        Context.getInstance().setQuery(originalSql);
        Context.getInstance().setColumnTypes(columnTypes);
        Context.getInstance().setColumnTypeNames(columnTypeNames);
        ResultSetTable.getInstance().setDataVector(dataVector, columnIdentifiers, waitingDialog.getExecutionTime());
        Actions.getInstance().validateActions();
    }

    private PreparedStatement createStatement(Connection connection, String sql) throws SQLException {
        boolean query = sql.trim().toLowerCase().startsWith("sel") || sql.trim().toLowerCase().startsWith("with");
        boolean call = sql.trim().toLowerCase().startsWith("call");
        PreparedStatement statement;
        if (query) {
            if (Context.getInstance().getConnectionData().isOracle()) {
                // http://download.oracle.com/docs/cd/B19306_01/java.102/b14355/resltset.htm#CIHEJHJI
                sql = String.format("select x.* from (%s) x where 1 = 1", sql);
            }
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                // Oracle, MySQL, DataDirect DB2, HSQLDB, H2, Apache Derby
                statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
            } else if (metaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                if (metaData.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
                    // IBM DB2
                    try {
                        statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                    } catch (SQLException sqle) {
                        // Microsoft (obviously)
                        statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                    }
                } else {
                    statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                }
            } else {
                // SQLite
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
                bindVariables[i] = JOptionPane.showInputDialog(String.format(LanguageManager.getInstance().getString("message.bind_variable"), i + 1));
            }
            handleBindVariables(statement, bindVariables);
            return bindVariables;
        } catch (Exception e) {
            return new String[0];
        }
    }

    private void handleBindVariables(PreparedStatement statement, String[] bindVariables) throws SQLException {
        for (int i = 0; i < bindVariables.length; i++) {
            statement.setObject(i + 1, bindVariables[i]);
        }
    }

    /**
     * Strips a trailing "/" placed alone on its own line, if present.<p/>
     *
     * A PL/SQL block (CREATE PROCEDURE/FUNCTION/PACKAGE/TRIGGER/TYPE, or a
     * bare BEGIN/DECLARE block) is conventionally terminated this way in
     * SQL*Plus/SQLcl-style scripts - it is a client-side directive
     * ("execute this block now"), not part of the SQL text itself. Sending
     * it to the JDBC driver as literal trailing content makes the database
     * report a syntax error even though the CREATE/REPLACE right before it
     * already succeeded, which looks like the whole statement failed when
     * it didn't.
     */
    private static String stripTrailingSlashTerminator(String sql) {
        sql = sql.trim();
        int lastNewline = Math.max(sql.lastIndexOf('\n'), sql.lastIndexOf('\r'));
        String lastLine = lastNewline == -1 ? sql : sql.substring(lastNewline + 1);
        if (lastLine.trim().equals("/")) {
            sql = (lastNewline == -1 ? "" : sql.substring(0, lastNewline)).trim();
        }
        return sql;
    }
}