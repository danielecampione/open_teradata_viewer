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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class PivotXml extends Pivot {

    private ISelectBody inSelect;
    private boolean inAny = false;

    @Override
    public void accept(IPivotVisitor iPivotVisitor) {
        iPivotVisitor.visit(this);
    }

    public ISelectBody getInSelect() {
        return inSelect;
    }

    public void setInSelect(ISelectBody inSelect) {
        this.inSelect = inSelect;
    }

    public boolean isInAny() {
        return inAny;
    }

    public void setInAny(boolean inAny) {
        this.inAny = inAny;
    }

    @Override
    public String toString() {
        List<Column> forColumns = getForColumns();
        String in = inAny ? "ANY" : inSelect == null ? PlainSelect
                .getStringList(getInItems()) : inSelect.toString();
        return "PIVOT XML ("
                + PlainSelect.getStringList(getFunctionItems())
                + " FOR "
                + PlainSelect.getStringList(forColumns, true,
                        forColumns != null && forColumns.size() > 1) + " IN ("
                + in + "))";
    }
}