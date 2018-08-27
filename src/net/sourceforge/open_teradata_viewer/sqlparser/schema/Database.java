/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.schema;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public final class Database implements IMultiPartName {

    private Server server;
    private String databaseName;

    public Database(String databaseName) {
        setDatabaseName(databaseName);
    }

    public Database(Server server, String databaseName) {
        setServer(server);
        setDatabaseName(databaseName);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String getFullyQualifiedName() {
        String fqn = "";

        if (server != null) {
            fqn += server.getFullyQualifiedName();
        }
        if (!fqn.isEmpty()) {
            fqn += ".";
        }

        if (databaseName != null) {
            fqn += databaseName;
        }

        return fqn;
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }
}