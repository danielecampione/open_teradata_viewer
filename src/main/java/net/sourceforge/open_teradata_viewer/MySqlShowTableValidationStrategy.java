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

/**
 * MySQL equivalent of Teradata's "SHOW TABLE": <code>SHOW CREATE TABLE</code>.
 * Unlike Teradata/Oracle/DB2, the result has two columns
 * (<code>Table</code>, <code>Create Table</code>) - the DDL is in the
 * second one, hence the {@link #getResultColumnIndex()} override. MySQL
 * accepts a schema-qualified <code>db.table</code> name directly, so no
 * splitting is needed here (unlike the Oracle/DB2 equivalents).
 *
 * @author D. Campione
 *
 */
public class MySqlShowTableValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public MySqlShowTableValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String tableName) {
        return "SHOW CREATE TABLE " + tableName;
    }

    @Override
    public int getResultColumnIndex() {
        return 2;
    }
}
