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

package net.sourceforge.open_teradata_viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.open_teradata_viewer.ConnectionData.DatabaseType;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * One of the partecipants belonging to the template method that has been
 * adopted to implement the initialization of the "select from" statement; the
 * class represents one of the ConcreteClasses of the specified design
 * pattern.<p/>
 * 
 * A ConcreteClass consists of a class that implements the primitive operations
 * for executing the steps related to the algorithm.<p/>
 * 
 * The class <code>CustomSelectFromStatement</code> implements the code of the
 * custom SQL query having the fields listed in the select statement. This class
 * has one attribute: relationName. Notice that method the
 * <code>initSQLQuery()</code> has been defined.
 * 
 * @author D. Campione
 *
 */
public class CustomSelectFromStatement extends SelectFromStatementTemplateMethod {

    private String relationName;

    public CustomSelectFromStatement(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public void initSQLQuery() throws SQLException {
        if (relationName == null) {
            return;
        }

        ApplicationFrame app = ApplicationFrame.getInstance();
        DatabaseType databaseType = app.getDatabaseType();
        ColumnsNameDiscovererHandlerFactoryMethod handler;
        IColumnsNameDiscovererElement columnsNameDiscoverer;

        if (databaseType == DatabaseType.TERADATA) {
            handler = new TeradataColumnsNameDiscovererHandler();
            columnsNameDiscoverer = (TeradataColumnsNameDiscoverer) handler.createElement(relationName);
        } else if (databaseType == DatabaseType.ORACLE) {
            handler = new ORACLEColumnsNameDiscovererHandler();
            columnsNameDiscoverer = (ORACLEColumnsNameDiscoverer) handler.createElement(relationName);
        } else {
            handler = new GenericColumnsNameDiscovererHandler();
            columnsNameDiscoverer = (GenericColumnsNameDiscoverer) handler.createElement(relationName);
        }
        String sqlQuery = columnsNameDiscoverer.getSQLQuery();

        if (databaseType != DatabaseType.UNKNOWN) {
            ResultSet resultSet = null;
            Connection connection = Context.getInstance().getConnectionData().getConnection();
            final PreparedStatement statement = connection.prepareStatement(sqlQuery);
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
            try {
                resultSet = statement.executeQuery();
            } finally {
                waitingDialog.hide();
            }
            final String COLUMN_SEPARATOR = ", ";
            String text = "SELECT ";
            while (resultSet.next()) {
                String columnName = resultSet.getString(1);
                if (Utilities.isEmpty(columnName) || columnName.trim().length() == 0) {
                    columnName = "";
                }
                text += columnName.toUpperCase().trim() + COLUMN_SEPARATOR;
            }
            if (text.equals("SELECT ")) {
                text += "* ";
            } else if (text.endsWith(COLUMN_SEPARATOR)) {
                text = text.substring(0, text.length() - COLUMN_SEPARATOR.length());
            }
            text += " FROM " + relationName;
            this.sqlQuery = text;
            statement.close();
            resultSet.close();
        } else {
            this.sqlQuery = sqlQuery;
        }
    }

    public String getRelationName() {
        return relationName;
    }
}