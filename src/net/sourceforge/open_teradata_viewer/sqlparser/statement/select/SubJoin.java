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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

/**
 * A table created by "(tab1 join tab2)".
 * 
 * @author D. Campione
 * 
 */
public class SubJoin implements IFromItem {

    private IFromItem left;
    private Join join;
    private String alias;

    public void accept(IFromItemVisitor iFromItemVisitor) {
        iFromItemVisitor.visit(this);
    }

    public IFromItem getLeft() {
        return left;
    }

    public void setLeft(IFromItem l) {
        left = l;
    }

    public Join getJoin() {
        return join;
    }

    public void setJoin(Join j) {
        join = j;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String string) {
        alias = string;
    }

    public String toString() {
        return "(" + left + " " + join + ")"
                + ((alias != null) ? " AS " + alias : "");
    }
}