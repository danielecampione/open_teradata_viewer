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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Alias;

/**
 * An item in a "SELECT [...] FROM item1" statement. (for example a table or a
 * sub-select).
 * 
 * @author D. Campione
 * 
 */
public interface IFromItem {

    void accept(IFromItemVisitor fromItemVisitor);

    Alias getAlias();

    void setAlias(Alias alias);

    Pivot getPivot();

    void setPivot(Pivot pivot);
}