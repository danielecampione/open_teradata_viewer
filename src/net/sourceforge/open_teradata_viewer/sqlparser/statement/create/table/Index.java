/*
 * Open Teradata Viewer ( sql parser )
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * An index (unique, primary etc.) in a CREATE TABLE statement
 * 
 * @author D. Campione
 * 
 */
public class Index {

    private String type;

    private List<?> columnsNames;
    private String name;

    /**
     * A list of strings of all the columns regarding this index  
     */

    public List<?> getColumnsNames() {
        return columnsNames;
    }

    public String getName() {
        return name;
    }

    /**
     * The type of this index: "PRIMARY KEY", "UNIQUE", "INDEX"
     */
    public String getType() {
        return type;
    }

    public void setColumnsNames(List<?> list) {
        columnsNames = list;
    }

    public void setName(String string) {
        name = string;
    }

    public void setType(String string) {
        type = string;
    }

    public String toString() {
        return type + " " + PlainSelect.getStringList(columnsNames, true, true)
                + (name != null ? " " + name : "");
    }
}