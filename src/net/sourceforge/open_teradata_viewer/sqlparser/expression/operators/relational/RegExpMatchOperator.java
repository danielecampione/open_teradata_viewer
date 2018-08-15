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
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RegExpMatchOperator extends BinaryExpression {

    private RegExpMatchOperatorType operatorType;

    public RegExpMatchOperator(RegExpMatchOperatorType operatorType) {
        if (operatorType == null) {
            throw new NullPointerException();
        }
        this.operatorType = operatorType;
    }

    public RegExpMatchOperatorType getOperatorType() {
        return operatorType;
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        switch (operatorType) {
        case MATCH_CASESENSITIVE:
            return "~";
        case MATCH_CASEINSENSITIVE:
            return "~*";
        case NOT_MATCH_CASESENSITIVE:
            return "!~";
        case NOT_MATCH_CASEINSENSITIVE:
            return "!~*";
        }
        return null;
    }
}