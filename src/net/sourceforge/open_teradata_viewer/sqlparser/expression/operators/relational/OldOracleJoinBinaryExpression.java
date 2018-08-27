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

package net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;

/**
 *
 *
 * @author D. Campione
 *
 */
public abstract class OldOracleJoinBinaryExpression extends BinaryExpression
        implements ISupportsOldOracleJoinSyntax {

    private int oldOracleJoinSyntax = NO_ORACLE_JOIN;

    private int oraclePriorPosition = NO_ORACLE_PRIOR;

    @Override
    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 2) {
            throw new IllegalArgumentException(
                    "unknown join type for oracle found (type="
                            + oldOracleJoinSyntax + ")");
        }
    }

    @Override
    public String toString() {
        return (isNot() ? "NOT " : "")
                + (oraclePriorPosition == ORACLE_PRIOR_START ? "PRIOR " : "")
                + getLeftExpression()
                + (oldOracleJoinSyntax == ORACLE_JOIN_RIGHT ? "(+)" : "") + " "
                + getStringExpression() + " "
                + (oraclePriorPosition == ORACLE_PRIOR_END ? "PRIOR " : "")
                + getRightExpression()
                + (oldOracleJoinSyntax == ORACLE_JOIN_LEFT ? "(+)" : "");
    }

    @Override
    public int getOldOracleJoinSyntax() {
        return oldOracleJoinSyntax;
    }

    @Override
    public int getOraclePriorPosition() {
        return oraclePriorPosition;
    }

    @Override
    public void setOraclePriorPosition(int oraclePriorPosition) {
        this.oraclePriorPosition = oraclePriorPosition;
    }
}