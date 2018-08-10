/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2011, D. Campione
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
 * See also:
 * https://aurora.vcu.edu/db2help/db2s0/frame3.htm#casexp
 * http://sybooks.sybase.com/onlinebooks/group-as/asg1251e/commands/@ebt-link;pt=5954?target=%25N%15_52628_START_RESTART_N%25
 *  
 * @author Havard Rast Blok
 * 
 */
public class CaseExpression implements Expression {

    private Expression switchExpression;

    @SuppressWarnings("rawtypes")
    private List whenClauses;

    private Expression elseExpression;

    /* (non-Javadoc)
     * @see net.sf.jsqlparser.expression.Expression#accept(net.sf.jsqlparser.expression.ExpressionVisitor)
     */
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    /**
     * @return Returns the switchExpression.
     */
    public Expression getSwitchExpression() {
        return switchExpression;
    }
    /**
     * @param switchExpression The switchExpression to set.
     */
    public void setSwitchExpression(Expression switchExpression) {
        this.switchExpression = switchExpression;
    }

    /**
     * @return Returns the elseExpression.
     */
    public Expression getElseExpression() {
        return elseExpression;
    }
    /**
     * @param elseExpression The elseExpression to set.
     */
    public void setElseExpression(Expression elseExpression) {
        this.elseExpression = elseExpression;
    }
    /**
     * @return Returns the whenClauses.
     */
    @SuppressWarnings("rawtypes")
    public List getWhenClauses() {
        return whenClauses;
    }

    /**
     * @param whenClauses The whenClauses to set.
     */
    @SuppressWarnings("rawtypes")
    public void setWhenClauses(List whenClauses) {
        this.whenClauses = whenClauses;
    }

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