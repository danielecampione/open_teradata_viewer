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
 * HSQLDB equivalent of Teradata's "SHOW PROCEDURE", via the standard
 * <code>INFORMATION_SCHEMA.ROUTINES.ROUTINE_DEFINITION</code> column.
 * Only covers routines written in SQL (the common case); a routine backed
 * by an external Java method has no SQL body to show.
 *
 * @author D. Campione
 *
 */
public class HSQLDBShowProcedureValidationStrategy
        implements
            IShowObjectValidationStrategy {

    public HSQLDBShowProcedureValidationStrategy() {
    }

    public String getSQLQueryToShowObject(String procedureName) {
        return HSQLDBInformationSchemaQueryBuilder.buildRoutineDefinitionQuery(procedureName);
    }
}
