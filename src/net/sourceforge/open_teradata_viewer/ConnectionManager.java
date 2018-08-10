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

package net.sourceforge.open_teradata_viewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages a connection to the database.
 * 
 * @author G. Biffi
 * 
 */
public class ConnectionManager {
    private Connection connection;
    private String connectionURL;
    private String userName;
    private String password;

    /**
     * Creates a new ConnectionManager.
     * @param driverClass the driver class name (i.e. "com.mysql.jdbc.Driver", make sure the
     *        jar of the driver is in the classpath)
     * @param connectionURL the full database connection URL
     *        (i.e. "jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=UTF-8",
     *        it is strongly recommended that the connection uses UTF-8)
     * @param userName the database user name
     * @param password the database password
     */
    public ConnectionManager(String driverClass, String connectionURL,
            String userName, String password) {
        this.connectionURL = connectionURL;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Connect to the database
     * @throws SQLException
     */
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(connectionURL, userName,
                password);
        //connection.setAutoCommit(false);
    }

    /**
     * Check if the database connection is still alive.
     * @return true if the connection is still alive, false otherwise
     * @throws Exception
     */
    public boolean checkConnection() {
        try {
            ResultSet rs;

            // Check if connection is closed
            if (connection == null || connection.isClosed()) {
                return false;
            }

            // Try a test query
            rs = connection.createStatement().executeQuery("SELECT 1");
            if (rs == null) {
                connection.close();
                return false;
            }
            if (!rs.next()) {
                connection.close();
                return false;
            }
            if (rs.getInt(1) != 1) {
                connection.close();
                return false;
            }

            // Everything seems to be fine
            ConnectionData connectionData = Context.getInstance()
                    .getConnectionData();
            connectionData.setMixedCaseQuotedIdentifiers();
            ApplicationFrame.getInstance().initializeObjectChooser(
                    ApplicationFrame.getInstance().connectionManager);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * @return the database connection
     */
    public Connection getConnection() {
        return connection;
    }
}
