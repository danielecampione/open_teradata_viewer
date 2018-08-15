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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SetOperationList.SetOperationType;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class UnionOp extends SetOperation {

    private boolean distinct;
    private boolean all;

    public UnionOp() {
        super(SetOperationType.UNION);
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public String toString() {
        String allDistinct = "";
        if (isAll()) {
            allDistinct = " ALL";
        } else if (isDistinct()) {
            allDistinct = " DISTINCT";
        }
        return super.toString() + allDistinct;
    }
}