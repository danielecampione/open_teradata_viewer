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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.execute.Execute;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 *
 *
 * @author D. Campione
 *
 */
public class ExecuteDeParser {

    private StringBuilder buffer;
    private IExpressionVisitor expressionVisitor;

    /**
     * @param expressionVisitor A {@link IExpressionVisitor} to de-parse
     * expressions. It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work.
     * @param buffer The buffer that will be filled with the select.
     */
    public ExecuteDeParser(IExpressionVisitor expressionVisitor,
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

    public void deParse(Execute execute) {
        buffer.append("EXECUTE ").append(execute.getName());
        buffer.append(" ").append(
                PlainSelect.getStringList(execute.getExprList()
                        .getExpressions(), true, false));
    }

    public IExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(IExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }
}