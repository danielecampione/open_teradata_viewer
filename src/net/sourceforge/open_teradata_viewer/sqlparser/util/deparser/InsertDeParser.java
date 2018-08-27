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

package net.sourceforge.open_teradata_viewer.sqlparser.util.deparser;

import java.util.Iterator;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsListVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MultiExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.insert.Insert;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectExpressionItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.WithItem;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) an {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.insert.Insert}.
 *
 * @author D. Campione
 *
 */
public class InsertDeParser implements IItemsListVisitor {

    private StringBuilder buffer;
    private IExpressionVisitor expressionVisitor;
    private ISelectVisitor selectVisitor;

    public InsertDeParser() {
    }

    /**
     * @param expressionVisitor a {@link IExpressionVisitor} to de-parse
     * {@link net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression}s.
     * It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work.
     * @param selectVisitor a {@link ISelectVisitor} to de-parse
     * {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select}s.
     * It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work.
     * @param buffer the buffer that will be filled with the insert.
     */
    public InsertDeParser(IExpressionVisitor expressionVisitor,
            ISelectVisitor selectVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
        this.selectVisitor = selectVisitor;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Insert insert) {
        buffer.append("INSERT INTO ");
        buffer.append(insert.getTable().getFullyQualifiedName());
        if (insert.getColumns() != null) {
            buffer.append(" (");
            for (Iterator<Column> iter = insert.getColumns().iterator(); iter
                    .hasNext();) {
                Column column = iter.next();
                buffer.append(column.getColumnName());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append(")");
        }

        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }

        if (insert.getSelect() != null) {
            buffer.append(" ");
            if (insert.isUseSelectBrackets()) {
                buffer.append("(");
            }
            if (insert.getSelect().getWithItemsList() != null) {
                buffer.append("WITH ");
                for (WithItem with : insert.getSelect().getWithItemsList()) {
                    with.accept(selectVisitor);
                }
                buffer.append(" ");
            }
            insert.getSelect().getSelectBody().accept(selectVisitor);
            if (insert.isUseSelectBrackets()) {
                buffer.append(")");
            }
        }

        if (insert.isReturningAllColumns()) {
            buffer.append(" RETURNING *");
        } else if (insert.getReturningExpressionList() != null) {
            buffer.append(" RETURNING ");
            for (Iterator<SelectExpressionItem> iter = insert
                    .getReturningExpressionList().iterator(); iter.hasNext();) {
                buffer.append(iter.next().toString());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
    }

    @Override
    public void visit(ExpressionList expressionList) {
        buffer.append(" VALUES (");
        for (Iterator<IExpression> iter = expressionList.getExpressions()
                .iterator(); iter.hasNext();) {
            IExpression expression = iter.next();
            expression.accept(expressionVisitor);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        buffer.append(" VALUES ");
        for (Iterator<ExpressionList> it = multiExprList.getExprList()
                .iterator(); it.hasNext();) {
            buffer.append("(");
            for (Iterator<IExpression> iter = it.next().getExpressions()
                    .iterator(); iter.hasNext();) {
                IExpression expression = iter.next();
                expression.accept(expressionVisitor);
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append(")");
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(selectVisitor);
    }

    public IExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public ISelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setExpressionVisitor(IExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }

    public void setSelectVisitor(ISelectVisitor visitor) {
        selectVisitor = visitor;
    }
}