/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2013, D. Campione
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
 * class represents the AbstractClass of the specified design pattern.<p/>
 * 
 * The class specifies the abstract operations that the concrete sub-classes
 * may define to implement the steps of the algorithm.<br/>
 * It implements the template method by creating the structure of an
 * algorithm.<br/>
 * The template method invokes the primitive operations declared in the
 * AbstractClass or the primitive operations defined in other objects.<p/>
 * 
 * The abstract class <code>SelectFromStatementTemplateMethod</code> defines the
 * general logic of the getSQLQuery method. The primitive operation invoked by
 * this method is declared such as an abstract method (initSQLQuery).
 * 
 * @author D. Campione
 *
 */
public abstract class SelectFromStatementTemplateMethod {

    protected String sqlQuery;

    public static final String DEFAULT_STATEMENT = "SELECT * FROM ";

    public String returnSQLQuery() throws SQLException {
        initSQLQuery();
        return sqlQuery == null ? DEFAULT_STATEMENT : sqlQuery;
    }

    public abstract void initSQLQuery() throws SQLException;
}