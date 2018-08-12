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

import java.util.Iterator;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) a {@link net.sf.jsqlparser.statement.replace.Replace}.
 * 
 * @author D. Campione
 * 
 */
public class ReplaceDeParser implements ItemsListVisitor {

    protected StringBuffer buffer;
    protected ExpressionVisitor expressionVisitor;
    protected SelectVisitor selectVisitor;

    public ReplaceDeParser() {
    }

    /**
     * @param expressionVisitor a {@link IExpressionVisitor} to de-parse
     *                          expressions. It has to share the same<br>
     *                          StringBuffer (buffer parameter) as this object
     *                          in order to work.
     * @param selectVisitor a {@link ISelectVisitor} to de-parse {@link
     *                      net.sf.jsqlparser.statement.select.Select}s. It has
     *                      to share the same<br> StringBuffer (buffer
     *                      parameter) as this object in order to work.
     * @param buffer the buffer that will be filled with the select
     */
    public ReplaceDeParser(ExpressionVisitor expressionVisitor,
            SelectVisitor selectVisitor, StringBuffer buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
        this.selectVisitor = selectVisitor;
    }

    public StringBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuffer buffer) {
        this.buffer = buffer;
    }

    public void deParse(Replace replace) {
        buffer.append("REPLACE " + replace.getTable().getWholeTableName());
        if (replace.getItemsList() != null) {
            if (replace.getColumns() != null) {
                buffer.append(" (");
                for (int i = 0; i < replace.getColumns().size(); i++) {
                    Column column = (Column) replace.getColumns().get(i);
                    buffer.append(column.getWholeColumnName());
                    if (i < replace.getColumns().size() - 1) {
                        buffer.append(", ");
                    }
                }
                buffer.append(") ");
            } else {
                buffer.append(" ");
            }
        } else {
            buffer.append(" SET ");
            for (int i = 0; i < replace.getColumns().size(); i++) {
                Column column = (Column) replace.getColumns().get(i);
                buffer.append(column.getWholeColumnName() + "=");

                Expression expression = (Expression) replace.getExpressions()
                        .get(i);
                expression.accept(expressionVisitor);
                if (i < replace.getColumns().size() - 1) {
                    buffer.append(", ");
                }
            }
        }
    }

    public void visit(ExpressionList expressionList) {
        buffer.append(" VALUES (");
        for (Iterator<?> iter = expressionList.getExpressions().iterator(); iter
                .hasNext();) {
            Expression expression = (Expression) iter.next();
            expression.accept(expressionVisitor);
            if (iter.hasNext())
                buffer.append(", ");
        }
        buffer.append(")");
    }

    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(selectVisitor);
    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public SelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }

    public void setSelectVisitor(SelectVisitor visitor) {
        selectVisitor = visitor;
    }
}