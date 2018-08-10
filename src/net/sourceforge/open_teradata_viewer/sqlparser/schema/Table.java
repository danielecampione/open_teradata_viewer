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

package net.sourceforge.open_teradata_viewer.sqlparser.schema;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.FromItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.FromItemVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IntoTableVisitor;

/**
 * A table. It can have an alias and the schema name it belongs to.
 * 
 * @author D. Campione
 * 
 */
public class Table implements FromItem {

    private String schemaName;
    private String name;
    private String alias;

    public Table() {
    }

    public Table(String schemaName, String name) {
        this.schemaName = schemaName;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setName(String string) {
        name = string;
    }

    public void setSchemaName(String string) {
        schemaName = string;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String string) {
        alias = string;
    }

    public String getWholeTableName() {

        String tableWholeName = null;
        if (name == null) {
            return null;
        }
        if (schemaName != null) {
            tableWholeName = schemaName + "." + name;
        } else {
            tableWholeName = name;
        }

        return tableWholeName;

    }

    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public void accept(IntoTableVisitor intoTableVisitor) {
        intoTableVisitor.visit(this);
    }

    public String toString() {
        return getWholeTableName() + ((alias != null) ? " AS " + alias : "");
    }
}
