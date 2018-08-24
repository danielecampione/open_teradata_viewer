/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2014, D. Campione
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public final class Server implements IMultiPartName {

    public static final Pattern SERVER_PATTERN = Pattern
            .compile("\\[([^\\]]+?)(?:\\\\([^\\]]+))?\\]");

    private String serverName;
    private String instanceName;

    public Server(String serverAndInstanceName) {
        if (serverAndInstanceName != null) {
            final Matcher matcher = SERVER_PATTERN
                    .matcher(serverAndInstanceName);
            if (!matcher.find()) {
                throw new IllegalArgumentException(String.format(
                        "%s is not a valid database reference",
                        serverAndInstanceName));
            }
            setServerName(matcher.group(1));
            setInstanceName(matcher.group(2));
        }
    }

    public Server(String serverName, String instanceName) {
        setServerName(serverName);
        setInstanceName(instanceName);
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public String getFullyQualifiedName() {
        if (serverName != null && !serverName.isEmpty() && instanceName != null
                && !instanceName.isEmpty()) {
            return String.format("[%s\\%s]", serverName, instanceName);
        } else if (serverName != null && !serverName.isEmpty()) {
            return String.format("[%s]", serverName);
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }
}