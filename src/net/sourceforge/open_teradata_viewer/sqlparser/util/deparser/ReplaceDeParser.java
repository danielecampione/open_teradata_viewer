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

package net.sourceforge.open_teradata_viewer.sqlparser.util.deparser;

import java.util.Iterator;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsListVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MultiExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.replace.Replace;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) a {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.replace.Replace}.
 * 
 * @author D. Campione
 * 
 */
public class ReplaceDeParser implements IItemsListVisitor {

    private StringBuilder buffer;
    private IExpressionVisitor expressionVisitor;
    private ISelectVisitor selectVisitor;

    public ReplaceDeParser() {
    }

    /**
     * @param expressionVisitor a {@link IExpressionVisitor} to de-parse
     * expressions. It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work.
     * @param selectVisitor a {@link ISelectVisitor} to de-parse
     * {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select}s.
     * It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work.
     * @param buffer the buffer that will be filled with the select.
     */
    public ReplaceDeParser(IExpressionVisitor expressionVisitor,
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

    public void deParse(Replace replace) {
        buffer.append("REPLACE ")
                .append(replace.getTable().getWholeTableName());
        if (replace.getItemsList() != null) {
            if (replace.getColumns() != null) {
                buffer.append(" (");
                for (int i = 0; i < replace.getColumns().size(); i++) {
                    Column column = replace.getColumns().get(i);
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
                Column column = replace.getColumns().get(i);
                buffer.append(column.getWholeColumnName()).append("=");

                IExpression expression = replace.getExpressions().get(i);
                expression.accept(expressionVisitor);
                if (i < replace.getColumns().size() - 1) {
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

    @Override
    public void visit(MultiExpressionList multiExprList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}