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

package test.net.sourceforge.open_teradata_viewer.sqlparser.schema;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Server;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ServerTest extends TestCase {

    public void testServerNameParsing() throws Exception {
        final String serverName = "LOCALHOST";

        final String fullServerName = String.format("[%s]", serverName);
        final Server server = new Server(fullServerName);

        Assert.assertEquals(serverName, server.getServerName());
        Assert.assertEquals(fullServerName, server.toString());
    }

    public void testServerNameAndInstanceParsing() throws Exception {
        final String serverName = "LOCALHOST";
        final String serverInstanceName = "SQLSERVER";

        final String fullServerName = String.format("[%s\\%s]", serverName,
                serverInstanceName);
        final Server server = new Server(fullServerName);

        Assert.assertEquals(serverName, server.getServerName());
        Assert.assertEquals(serverInstanceName, server.getInstanceName());
        Assert.assertEquals(fullServerName, server.toString());
    }
}