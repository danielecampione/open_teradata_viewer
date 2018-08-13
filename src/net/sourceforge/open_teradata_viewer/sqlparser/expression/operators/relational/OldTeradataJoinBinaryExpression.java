/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class OldTeradataJoinBinaryExpression extends BinaryExpression
        implements
            ISupportsOldTeradataJoinSyntax {

    private int oldTeradataJoinSyntax = NO_TERADATA_JOIN;

    @Override
    public void setOldTeradataJoinSyntax(int oldTeradataJoinSyntax) {
        this.oldTeradataJoinSyntax = oldTeradataJoinSyntax;
        if (oldTeradataJoinSyntax < 0 || oldTeradataJoinSyntax > 2) {
            throw new IllegalArgumentException(
                    "unknown join type for Teradata found (type="
                            + oldTeradataJoinSyntax + ")");
        }
    }

    @Override
    public String toString() {
        return (isNot() ? "NOT " : "") + getLeftExpression()
                + (oldTeradataJoinSyntax == TERADATA_JOIN_RIGHT ? "(+)" : "")
                + " " + getStringExpression() + " " + getRightExpression()
                + (oldTeradataJoinSyntax == TERADATA_JOIN_LEFT ? "(+)" : "");
    }

    @Override
    public int getOldTeradataJoinSyntax() {
        return oldTeradataJoinSyntax;
    }
}