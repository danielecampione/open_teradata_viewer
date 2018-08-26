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

package net.sourceforge.open_teradata_viewer.sqlparser.util.deparser;

import java.util.Iterator;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.ColumnDefinition;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.CreateTable;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.Index;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) a {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.CreateTable}.
 *
 * @author D. Campione
 *
 */
public class CreateTableDeParser {

    private StringBuilder buffer;

    /** @param buffer the buffer that will be filled with the select. */
    public CreateTableDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(CreateTable createTable) {
        buffer.append("CREATE ");
        if (createTable.isUnlogged()) {
            buffer.append("UNLOGGED ");
        }
        String params = PlainSelect.getStringList(
                createTable.getCreateOptionsStrings(), false, false);
        if (!"".equals(params)) {
            buffer.append(params).append(' ');
        }

        buffer.append("TABLE ").append(
                createTable.getTable().getFullyQualifiedName());
        if (createTable.getSelect() != null) {
            buffer.append(" AS ").append(createTable.getSelect().toString());
        } else {
            if (createTable.getColumnDefinitions() != null) {
                buffer.append(" (");
                for (Iterator<ColumnDefinition> iter = createTable
                        .getColumnDefinitions().iterator(); iter.hasNext();) {
                    ColumnDefinition columnDefinition = iter.next();
                    buffer.append(columnDefinition.getColumnName());
                    buffer.append(" ");
                    buffer.append(columnDefinition.getColDataType().toString());
                    if (columnDefinition.getColumnSpecStrings() != null) {
                        for (String s : columnDefinition.getColumnSpecStrings()) {
                            buffer.append(" ");
                            buffer.append(s);
                        }
                    }

                    if (iter.hasNext()) {
                        buffer.append(", ");
                    }
                }

                if (createTable.getIndexes() != null) {
                    for (Iterator<Index> iter = createTable.getIndexes()
                            .iterator(); iter.hasNext();) {
                        buffer.append(", ");
                        Index index = iter.next();
                        buffer.append(index.toString());
                    }
                }

                buffer.append(")");
            }
        }

        params = PlainSelect.getStringList(
                createTable.getTableOptionsStrings(), false, false);
        if (!"".equals(params)) {
            buffer.append(' ').append(params);
        }
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }
}