/*
 * Open Teradata Viewer ( sql parser )
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

package net.sourceforge.open_teradata_viewer.sqlparser.schema;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Alias;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IFromItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IFromItemVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IIntoTableVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Pivot;

/**
 * A table. It can have an alias and the schema name it belongs to.
 * 
 * @author D. Campione
 * 
 */
public class Table implements IFromItem {

    private String schemaName;
    private String name;
    private Alias alias;
    private Pivot pivot;

    public Table() {
    }

    public Table(String name) {
        this.name = name;
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

    @Override
    public Alias getAlias() {
        return alias;
    }

    @Override
    public void setAlias(Alias alias) {
        this.alias = alias;
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

    @Override
    public void accept(IFromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public void accept(IIntoTableVisitor iIntoTableVisitor) {
        iIntoTableVisitor.visit(this);
    }

    @Override
    public Pivot getPivot() {
        return pivot;
    }

    @Override
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    @Override
    public String toString() {
        return getWholeTableName() + ((pivot != null) ? " " + pivot : "")
                + ((alias != null) ? alias.toString() : "");
    }
}