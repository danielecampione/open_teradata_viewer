/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.sql.SQLException;

/**
 * One of the partecipants belonging to the template method that has been
 * adopted to implement the initialization of the "select from" statement; the
 * class represents one of the ConcreteClasses of the specified design
 * pattern.<p/>
 * 
 * A ConcreteClass consists of a class that implements the primitive operations
 * for executing the steps related to the algorithm.<p/>
 * 
 * The class <code>GenericSelectFromStatement</code> implements the code of the
 * SQL query that consists of a "select from" statement that uses the '*' jolly
 * character to select all the fields. Notice that the method
 * <code>initSQLQuery()</code> has been defined.
 * 
 * @author D. Campione
 *
 */
public class GenericSelectFromStatement extends
        SelectFromStatementTemplateMethod {

    public GenericSelectFromStatement() {
    }

    @Override
    public void initSQLQuery() throws SQLException {
        sqlQuery = DEFAULT_STATEMENT;
    }
}