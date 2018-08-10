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

import java.sql.ResultSet;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ConnectionData {

    private ResultSet resultSet;

    private String name;
    private String url;
    private String user;
    private String password;
    private String driver;

    private String identifierQuoteString;

    public ConnectionData() {
    }

    public ConnectionData(String newName, String newUrl, String newUser,
            String newPassword, String newDriver) {
        this.name = newName;
        this.url = newUrl;
        this.user = newUser;
        this.password = newPassword;
        this.driver = newDriver;
        setMixedCaseQuotedIdentifiers();
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet newResultSet) {
        this.resultSet = newResultSet;
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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String newDriver) {
        this.driver = newDriver;
    }

    protected void setMixedCaseQuotedIdentifiers() {
        try {
            if (!ApplicationFrame.getInstance().connectionManager
                    .getConnection().getMetaData()
                    .supportsMixedCaseIdentifiers()
                    && ApplicationFrame.getInstance().connectionManager
                            .getConnection().getMetaData()
                            .supportsMixedCaseQuotedIdentifiers()) {
                identifierQuoteString = ApplicationFrame.getInstance().connectionManager
                        .getConnection().getMetaData()
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
}
