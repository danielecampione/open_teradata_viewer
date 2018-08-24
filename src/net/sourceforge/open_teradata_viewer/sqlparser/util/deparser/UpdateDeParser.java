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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Join;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.update.Update;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) an {@link net.sourceforge.open_teradata_viewer.sqlparser.statement.update.Update}.
 * 
 * @author D. Campione
 * 
 */
public class UpdateDeParser {

    private StringBuilder buffer;
    private IExpressionVisitor expressionVisitor;

    /**
     * @param expressionVisitor a {@link IExpressionVisitor} to de-parse
     * expressions. It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work.
     * @param buffer the buffer that will be filled with the select.
     */
    public UpdateDeParser(IExpressionVisitor expressionVisitor,
            StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Update update) {
        buffer.append("UPDATE ").append(update.getTable()).append(" SET ");
        for (int i = 0; i < update.getColumns().size(); i++) {
            Column column = update.getColumns().get(i);
            buffer.append(column.getFullyQualifiedName()).append(" = ");

            IExpression expression = update.getExpressions().get(i);
            expression.accept(expressionVisitor);
            if (i < update.getColumns().size() - 1) {
                buffer.append(", ");
            }
        }

        if (update.getFromItem() != null) {
            buffer.append(" FROM ").append(update.getFromItem());
            if (update.getJoins() != null) {
                for (Join join : update.getJoins()) {
                    if (join.isSimple()) {
                        buffer.append(", ").append(join);
                    } else {
                        buffer.append(" ").append(join);
                    }
                }
            }
        }

        if (update.getWhere() != null) {
            buffer.append(" WHERE ");
            update.getWhere().accept(expressionVisitor);
        }
    }

    public IExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(IExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }
}