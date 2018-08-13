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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;

/**
 * An expression as in "SELECT expr1 AS EXPR".
 * 
 * @author D. Campione
 * 
 */
public class SelectExpressionItem implements ISelectItem {

    private IExpression expression;
    private String alias;

    public String getAlias() {
        return alias;
    }

    public IExpression getExpression() {
        return expression;
    }

    public void setAlias(String string) {
        alias = string;
    }

    public void setExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public void accept(ISelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    @Override
    public String toString() {
        return expression + ((alias != null) ? " AS " + alias : "");
    }
}