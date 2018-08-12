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

package net.sourceforge.open_teradata_viewer.sqlparser.util.deparser;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) a {@link net.sf.jsqlparser.statement.delete.Delete}.
 * 
 * @author D. Campione
 * 
 */
public class DeleteDeParser {

    protected StringBuffer buffer;
    protected ExpressionVisitor expressionVisitor;

    public DeleteDeParser() {
    }

    /**
     * @param expressionVisitor a {@link IExpressionVisitor} to de-parse
     *        expressions. It has to share the same<br>
     *        StringBuffer (buffer parameter) as this object in order to work.
     * @param buffer the buffer that will be filled with the select.
     */
    public DeleteDeParser(ExpressionVisitor expressionVisitor,
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

    public void deParse(Delete delete) {
        buffer.append("DELETE FROM " + delete.getTable().getWholeTableName());
        if (delete.getWhere() != null) {
            buffer.append(" WHERE ");
            delete.getWhere().accept(expressionVisitor);
        }

    }
    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }
}