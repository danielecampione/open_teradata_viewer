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

package net.sourceforge.open_teradata_viewer.sqlparser.util.deparser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;

/**
 * A class to de-parse (that is, tranform from SqlParser hierarchy into a string)
 * an {@link net.sf.jsqlparser.statement.update.Update}
 * 
 * @author D. Campione
 * 
 */
public class UpdateDeParser {

    protected StringBuffer buffer;
    protected ExpressionVisitor expressionVisitor;

    public UpdateDeParser() {
    }

    /**
     * @param expressionVisitor a {@link ExpressionVisitor} to de-parse expressions. It has to share the same<br>
     * StringBuffer (buffer parameter) as this object in order to work
     * @param buffer the buffer that will be filled with the select
     */
    public UpdateDeParser(ExpressionVisitor expressionVisitor,
            StringBuffer buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuffer buffer) {
        this.buffer = buffer;
    }

    public void deParse(Update update) {
        buffer.append("UPDATE " + update.getTable().getWholeTableName()
                + " SET ");
        for (int i = 0; i < update.getColumns().size(); i++) {
            Column column = (Column) update.getColumns().get(i);
            buffer.append(column.getWholeColumnName() + "=");

            Expression expression = (Expression) update.getExpressions().get(i);
            expression.accept(expressionVisitor);
            if (i < update.getColumns().size() - 1) {
                buffer.append(", ");
            }

        }

        if (update.getWhere() != null) {
            buffer.append(" WHERE ");
            update.getWhere().accept(expressionVisitor);
        }

    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }

}
