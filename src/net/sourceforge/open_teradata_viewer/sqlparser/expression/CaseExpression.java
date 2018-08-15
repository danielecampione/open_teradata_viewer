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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * CASE/WHEN expression.
 *
 * Syntax:
 * <code><pre>
 * CASE
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </pre></code>
 *
 * <br/>
 * or <br/>
 * <br/>
 *
 * <code><pre>
 * CASE expression
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </pre></code>
 *
 * See also: https://aurora.vcu.edu/db2help/db2s0/frame3.htm#casexp
 * http://sybooks.sybase.com/onlinebooks/group-as/asg1251e /commands/
 *
 * @ebt-link;pt=5954?target=%25N%15_52628_START_RESTART_N%25
 *
 *
 * @author D. Campione
 * 
 */
public class CaseExpression implements IExpression {

    private IExpression switchExpression;
    private List<IExpression> whenClauses;
    private IExpression elseExpression;

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression#accept(net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor)
     */
    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    /** @return Returns the switchExpression. */
    public IExpression getSwitchExpression() {
        return switchExpression;
    }

    /** @param switchExpression The switchExpression to set. */
    public void setSwitchExpression(IExpression switchExpression) {
        this.switchExpression = switchExpression;
    }

    /** @return Returns the elseExpression. */
    public IExpression getElseExpression() {
        return elseExpression;
    }

    /** @param elseExpression The elseExpression to set. */
    public void setElseExpression(IExpression elseExpression) {
        this.elseExpression = elseExpression;
    }

    /** @return Returns the whenClauses. */
    public List<IExpression> getWhenClauses() {
        return whenClauses;
    }

    /** @param whenClauses The whenClauses to set. */
    public void setWhenClauses(List<IExpression> whenClauses) {
        this.whenClauses = whenClauses;
    }

    @Override
    public String toString() {
        return "CASE "
                + ((switchExpression != null) ? switchExpression + " " : "")
                + PlainSelect.getStringList(whenClauses, false, false)
                + " "
                + ((elseExpression != null)
                        ? "ELSE " + elseExpression + " "
                        : "") + "END";
    }
}