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

import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.view.CreateView;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) a {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.create.view.CreateView}.
 * 
 * @author D. Campione
 * 
 */
public class CreateViewDeParser {

    private StringBuilder buffer;

    /** @param buffer the buffer that will be filled with the select. */
    public CreateViewDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(CreateView createView) {
        buffer.append("CREATE ");
        if (createView.isOrReplace()) {
            buffer.append("OR REPLACE ");
        }
        if (createView.isMaterialized()) {
            buffer.append("MATERIALIZED ");
        }
        buffer.append("VIEW ").append(
                createView.getView().getFullyQualifiedName());
        if (createView.getColumnNames() != null) {
            buffer.append(PlainSelect.getStringList(
                    createView.getColumnNames(), true, true));
        }
        buffer.append(" AS ");
        buffer.append(createView.getSelectBody().toString());
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }
}