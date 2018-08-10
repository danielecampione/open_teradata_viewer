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


/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface SettingsKeys {

    /** The JDBC driver */
    public static final String JDBC_DRIVER_KEY = "jdbc_driver";
    public static final String JDBC_DRIVER_DEFAULT_VALUE = "com.teradata.jdbc.TeraDriver";

    /** The Server name */
    public static final String SERVER_NAME_KEY = "server_name";
    public static final String SERVER_NAME_DEFAULT_VALUE = "your server";

    /** The database port */
    public static final String DATABASE_PORT_KEY = "database_port";
    public static final String DATABASE_PORT_DEFAULT_VALUE = "1025";

    /** The database */
    public static final String DATABASE_KEY = "database";
    public static final String DATABASE_DEFAULT_VALUE = "your database";

    /** The charset configuration */
    public static final String CHARSET_ENCODING_KEY = "charset";
    public static final String CHARSET_ENCODING_DEFAULT_VALUE = "UTF8";

    /** The log mechanism */
    public static final String LOGMECH_KEY = "log_mechanism";
    public static final String LOGMECH_DEFAULT_VALUE = "LDAP";

    /** Authentication credentials */
    public static final String ACTUAL_USERID_KEY = "userID";
    public static final String ACTUAL_USERID_DEFAULT_VALUE = "your user";
    public static final String ACTUAL_PASSWORD_KEY = "password";
    public static final String ACTUAL_PASSWORD_DEFAULT_VALUE = "your password";

    /** The last used directory selected by each JFileChooser */
    public static final String LAST_USED_DIR_KEY = "last_used_dir";
    public static final String LAST_USED_DIR_DEFAULT_VALUE = ".";
}
