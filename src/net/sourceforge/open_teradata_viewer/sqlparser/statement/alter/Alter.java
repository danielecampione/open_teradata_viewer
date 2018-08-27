/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2015, D. Campione
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

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.ColDataType;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

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
    private List<String> pkColumns;
    private List<String> ukColumns;
    private String ukName;

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

    public List<String> getPkColumns() {
        return pkColumns;
    }

    public void setPkColumns(List<String> pkColumns) {
        this.pkColumns = pkColumns;
    }

    public List<String> getUkColumns() {
        return ukColumns;
    }

    public void setUkColumns(List<String> ukColumns) {
        this.ukColumns = ukColumns;
    }

    public String getUkName() {
        return ukName;
    }

    public void setUkName(String ukName) {
        this.ukName = ukName;
    }

    @Override
    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("ALTER TABLE ").append(table.getFullyQualifiedName())
                .append(" ADD ");
        if (columnName != null) {
            b.append("COLUMN ").append(columnName).append(" ")
                    .append(dataType.toString());
        } else if (pkColumns != null) {
            b.append("PRIMARY KEY (")
                    .append(PlainSelect.getStringList(pkColumns)).append(")");
        } else if (ukColumns != null) {
            b.append("UNIQUE KEY ").append(ukName).append(" (")
                    .append(PlainSelect.getStringList(ukColumns)).append(")");
        }
        return b.toString();
    }
}