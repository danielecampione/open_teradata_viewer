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

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 *
 * @author D. Campione
 *
 */

public class ConnectionData implements Comparable<Object>, Cloneable {

    public enum DatabaseType {
        TERADATA, ORACLE, DB2, MYSQL, SQLITE, HSQLDB, H2, APACHE_DERBY, SQL_SERVER, UNKNOWN
    }

    private String name;
    private String url;
    private String user;
    private String password;
    private String transientPassword;

    private Driver driver;
    private Connection connection;

    private String defaultOwner;

    private String identifierQuoteString;

    public ConnectionData() {
    }

    public ConnectionData(String newName, String newUrl, String newUser, String newPassword, String newDefaultOwner) {
        this.name = newName;
        this.url = newUrl;
        this.user = newUser;
        this.password = newPassword;
        this.defaultOwner = newDefaultOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String newUrl) {
        this.url = newUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String newUser) {
        this.user = newUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getTransientPassword() {
        return transientPassword;
    }

    public void setTransientPassword(String newTransientPassword) {
        this.transientPassword = newTransientPassword;
    }

    public Driver getDriver() {
        return driver;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDefaultOwner() {
        return defaultOwner;
    }

    public void setDefaultOwner(String newDefaultOwner) {
        this.defaultOwner = newDefaultOwner;
    }

    public void connect() throws Exception {
        try {
            driver = DriverManager.getDriver(url);
        } catch (Exception e) {
            ExceptionDialog.showException(new Exception(String.format("No suitable driver for URL \"%s\"", url), e));
            int response = Drivers.editDrivers();
            if (Dialog.OK_OPTION == response) {
                try {
                    driver = DriverManager.getDriver(url);
                } catch (SQLException sqle) {
                    throw new Exception(String.format("No suitable driver for URL \"%s\"", url), sqle);
                }
            } else if (Dialog.CANCEL_OPTION == response || Dialog.CLOSED_OPTION == response) {
                return;
            }
        }

        Properties properties = new Properties();
        if (user.length() > 0) {
            properties.setProperty("user", user);
            properties.setProperty("password", password.isEmpty() ? transientPassword : password);
        }
        addExtraProperties(properties);
        connection = driver.connect(url, properties);

        if (connection == null) {
            throw new Exception(
                    String.format("Unable to connect.\nURL = %s\nDriver = %s", url, driver.getClass().getName()));
        }
        connection.setAutoCommit(false);
        setMixedCaseQuotedIdentifiers();
    }

    private void addExtraProperties(Properties properties) {
        if (isOracle()) {
            addProperty(properties, "v$session.program", Main.APPLICATION_NAME);
        } else if (isIbm()) {
            addProperty(properties, "clientProgramName", Main.APPLICATION_NAME);
            addProperty(properties, "retrieveMessagesFromServerOnGetMessage", "true");
        } else if (isDataDirect()) {
            addProperty(properties, "applicationName", Main.APPLICATION_NAME);
            addProperty(properties, "connectionRetryCount", "0");
        } else if (isHSQLDB()) {
            addProperty(properties, "shutdown", "true");
        }
    }

    private void addProperty(Properties properties, String name, String value) {
        if (!url.toLowerCase().contains(name.toLowerCase())) {
            properties.setProperty(name, value);
        }
    }

    public boolean isOracle() {
        return url.toLowerCase().trim().startsWith("jdbc:oracle");
    }

    public boolean isIbm() {
        return url.toLowerCase().trim().startsWith("jdbc:db2");
    }

    public boolean isDataDirect() {
        return url.toLowerCase().trim().startsWith("jdbc:datadirect");
    }

    public boolean isHSQLDB() {
        return url.toLowerCase().trim().startsWith("jdbc:hsqldb");
    }

    private void setMixedCaseQuotedIdentifiers() {
        try {
            if (!connection.getMetaData().supportsMixedCaseIdentifiers()
                    && connection.getMetaData().supportsMixedCaseQuotedIdentifiers()) {
                identifierQuoteString = connection.getMetaData().getIdentifierQuoteString();
            } else {
                identifierQuoteString = "";
            }
        } catch (Exception e) {
            identifierQuoteString = "";
            ExceptionDialog.hideException(e);
        }
    }

    public String checkMixedCaseQuotedIdentifier(String s) {
        boolean hasLowerCase = !s.equals(s.toUpperCase());
        if (hasLowerCase) {
            s = String.format("%s%s%s", identifierQuoteString, s, identifierQuoteString);
        }
        return s;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        ConnectionData connectionData = (ConnectionData) o;
        return name.compareTo(connectionData.name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static final OutputStream DERBY_LOG_FILE_ELIMINATOR = new OutputStream() {
        {
            System.setProperty("derby.stream.error.field",
                    "net.sourceforge.open_teradata_viewer.ConnectionData.DERBY_LOG_FILE_ELIMINATOR");
        }

        @Override
        public void write(int b) {
        }
    };
}