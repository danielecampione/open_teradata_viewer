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
 * One of the partecipants belonging to the factory method that has been adopted
 * to implement the initialization of the columns name discoverer statement; the
 * class represents the ConcreteProduct of the specified design pattern.<p/>
 * 
 * It implements the concrete columns name discoverer appropriate to the
 * MySQL syntax, querying <code>information_schema.columns</code> rather
 * than folding the name to a fixed case first: unlike Oracle/Teradata,
 * MySQL running on Linux (and most other Unix-likes) treats unquoted
 * table/schema identifiers as case-sensitive, so a name must be looked up
 * exactly as it is really stored.<p/>
 * 
 * <code>relationName</code> may optionally be schema-qualified
 * (<code>schema.table</code>); when it isn't, the lookup is scoped to the
 * database currently in use on the connection (MySQL's <code>DATABASE()</code>),
 * matching what an unqualified table reference in a plain
 * <code>SELECT * FROM table</code> would resolve to.
 * 
 * @author D. Campione
 *
 */
public class MySqlColumnsNameDiscoverer implements IColumnsNameDiscovererElement {

    private String relationName;

    @Override
    public void setSQLQuery(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public String getSQLQuery() {
        String schemaName = null;
        String tableName = relationName;
        int lastTokenIndex = relationName.lastIndexOf(".");
        if (lastTokenIndex != -1) {
            schemaName = relationName.substring(0, lastTokenIndex);
            tableName = relationName.substring(lastTokenIndex + 1);
        }

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT column_name FROM information_schema.columns WHERE table_schema = ");
        if (schemaName != null) {
            sqlQuery.append('\'').append(escape(schemaName)).append('\'');
        } else {
            sqlQuery.append("DATABASE()");
        }
        sqlQuery.append(" AND table_name = '").append(escape(tableName)).append('\'')
                .append(" ORDER BY ordinal_position");
        return sqlQuery.toString();
    }

    /** Doubles up any single quote, so a stray one in the identifier can't break out of the string literal. */
    private static String escape(String identifier) {
        return identifier.replace("'", "''");
    }
}
