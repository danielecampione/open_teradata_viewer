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

package net.sourceforge.open_teradata_viewer.sqlparser.util.deparser;

import java.util.Iterator;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from SqlParser hierarchy into a string)
 * an {@link net.sf.jsqlparser.statement.insert.Insert}
 * 
 * @author D. Campione
 * 
 */
public class InsertDeParser implements ItemsListVisitor {

    protected StringBuffer buffer;
    protected ExpressionVisitor expressionVisitor;
    protected SelectVisitor selectVisitor;

    public InsertDeParser() {
    }

    /**
     * @param expressionVisitor a {@link ExpressionVisitor} to de-parse {@link net.sf.jsqlparser.expression.Expression}s. It has to share the same<br>
     * StringBuffer (buffer parameter) as this object in order to work
     * @param selectVisitor a {@link SelectVisitor} to de-parse {@link net.sf.jsqlparser.statement.select.Select}s.
     * It has to share the same<br>
     * StringBuffer (buffer parameter) as this object in order to work
     * @param buffer the buffer that will be filled with the insert
     */
    public InsertDeParser(ExpressionVisitor expressionVisitor,
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

    @SuppressWarnings("rawtypes")
    public void deParse(Insert insert) {
        buffer.append("INSERT INTO ");
        buffer.append(insert.getTable().getWholeTableName());
        if (insert.getColumns() != null) {
            buffer.append("(");
            for (Iterator iter = insert.getColumns().iterator(); iter.hasNext();) {
                Column column = (Column) iter.next();
                buffer.append(column.getColumnName());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append(")");
        }

        insert.getItemsList().accept(this);

    }

    @SuppressWarnings("rawtypes")
    public void visit(ExpressionList expressionList) {
        buffer.append(" VALUES (");
        for (Iterator iter = expressionList.getExpressions().iterator(); iter
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
