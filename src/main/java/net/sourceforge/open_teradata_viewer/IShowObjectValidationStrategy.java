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
 * The interface defines the validation strategy used to initialize the SQL
 * query invoked to obtain the DDL.
 * 
 * @author D. Campione
 *
 */
public interface IShowObjectValidationStrategy {

    String getSQLQueryToShowObject(String objectName);

    /**
     * Which 1-based column of the query's result set holds the DDL text.
     * Teradata's "SHOW ...", Oracle's DBMS_METADATA.GET_DDL and DB2's
     * catalog TEXT columns all return it as the only column, hence the
     * default. MySQL's "SHOW CREATE ..." statements return it alongside
     * several other descriptive columns (sql_mode, character set, etc.),
     * at a different position depending on the object type, so
     * {@link MySqlShowProcedureValidationStrategy} overrides this.
     */
    default int getResultColumnIndex() {
        return 1;
    }

}