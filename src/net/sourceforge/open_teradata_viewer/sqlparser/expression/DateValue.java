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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

import java.sql.Date;

/**
 * A Date in the form {d 'yyyy-mm-dd'}
 * 
 * @author D. Campione
 * 
 */
public class DateValue implements IExpression {

    private Date value;

    public DateValue(String value) {
        this.value = Date.valueOf(value.substring(1, value.length() - 1));
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date d) {
        value = d;
    }
}