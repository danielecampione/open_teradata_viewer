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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.alter;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.ColDataType;

/**
 *
 *
 * @author D. Campione
 *
 */
public class Alter implements IStatement {

    private Table table;
    private String columnName;
    private ColDataType dataType;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ColDataType getDataType() {
        return dataType;
    }

    public void setDataType(ColDataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "ALTER TABLE " + table.getFullyQualifiedName() + " ADD COLUMN "
                + columnName + " " + dataType.toString();
    }
}