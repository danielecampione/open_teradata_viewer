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
@SuppressWarnings("rawtypes")
public class ConnectionData implements Comparable, Cloneable {

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

    public ConnectionData(String newName, String newUrl, String newUser,
            String newPassword, String newDefaultOwner) {
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
            ExceptionDialog.showException(new Exception(String.format(
                    "No suitable driver for URL \"%s\"", url), e));
            int response = Drivers.editDrivers();
            if (Dialog.OK_OPTION == response) {
                try {
                    driver = DriverManager.getDriver(url);
                } catch (SQLException e1) {
                    throw new Exception(String.format(
                            "No suitable driver for URL \"%s\"", url), e1);
                }
            } else if (Dialog.CANCEL_OPTION == response
                    || Dialog.CLOSED_OPTION == response) {
                return;
            }
        }

        Properties properties = new Properties();
        if (user.length() > 0) {
            properties.setProperty("user", user);
            properties.setProperty("password", password.isEmpty()
                    ? transientPassword
                    : password);
        }
        addExtraProperties(properties);
        connection = driver.connect(url, properties);

        if (connection == null) {
            throw new Exception(String.format(
                    "Unable to connect.\nURL = %s\nDriver = %s", url, driver
                            .getClass().getName()));
        }
        connection.setAutoCommit(false);
        setMixedCaseQuotedIdentifiers();
    }

    private void addExtraProperties(Properties properties) {
    }

    @SuppressWarnings("unused")
    private void addProperty(Properties properties, String name, String value) {
        if (!url.toLowerCase().contains(name.toLowerCase())) {
            properties.setProperty(name, value);
        }
    }

    private void setMixedCaseQuotedIdentifiers() {
        try {
            if (!connection.getMetaData().supportsMixedCaseIdentifiers()
                    && connection.getMetaData()
                            .supportsMixedCaseQuotedIdentifiers()) {
                identifierQuoteString = connection.getMetaData()
                        .getIdentifierQuoteString();
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
            s = String.format("%s%s%s", identifierQuoteString, s,
                    identifierQuoteString);
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
}