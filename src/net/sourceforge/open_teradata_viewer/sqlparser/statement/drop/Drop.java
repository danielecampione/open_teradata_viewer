/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.drop;


import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.Statement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.StatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Drop implements Statement {

    private String type;
    private String name;
    @SuppressWarnings("rawtypes")
    private List parameters;

    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("rawtypes")
    public List getParameters() {
        return parameters;
    }

    public String getType() {
        return type;
    }

    public void setName(String string) {
        name = string;
    }

    @SuppressWarnings("rawtypes")
    public void setParameters(List list) {
        parameters = list;
    }

    public void setType(String string) {
        type = string;
    }

    public String toString() {
        String sql = "DROP " + type + " " + name;

        if (parameters != null && parameters.size() > 0) {
            sql += " " + PlainSelect.getStringList(parameters);
        }

        return sql;
    }
}
