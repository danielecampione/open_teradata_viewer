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

import java.sql.Time;

/**
 * A Time in the form {t 'hh:mm:ss'}.
 * 
 * @author D. Campione
 * 
 */
public class TimeValue implements IExpression {

    private Time value;

    public TimeValue(String value) {
        this.value = Time.valueOf(value.substring(1, value.length() - 1));
    }

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Time getValue() {
        return value;
    }

    public void setValue(Time d) {
        value = d;
    }

    @Override
    public String toString() {
        return "{t '" + value + "'}";
    }
}