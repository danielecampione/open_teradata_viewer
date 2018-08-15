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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class OldTeradataJoinBinaryExpression extends BinaryExpression
        implements ISupportsOldTeradataJoinSyntax {

    private int oldTeradataJoinSyntax = NO_TERADATA_JOIN;

    private int teradataPriorPosition = NO_TERADATA_PRIOR;

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
        return (isNot() ? "NOT " : "")
                + (teradataPriorPosition == TERADATA_PRIOR_START ? "PRIOR "
                        : "") + getLeftExpression()
                + (oldTeradataJoinSyntax == TERADATA_JOIN_RIGHT ? "(+)" : "")
                + " " + getStringExpression() + " "
                + (teradataPriorPosition == TERADATA_PRIOR_END ? "PRIOR " : "")
                + getRightExpression()
                + (oldTeradataJoinSyntax == TERADATA_JOIN_LEFT ? "(+)" : "");
    }

    @Override
    public int getOldTeradataJoinSyntax() {
        return oldTeradataJoinSyntax;
    }

    @Override
    public int getTeradataPriorPosition() {
        return teradataPriorPosition;
    }

    @Override
    public void setTeradataPriorPosition(int teradataPriorPosition) {
        this.teradataPriorPosition = teradataPriorPosition;
    }
}