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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;

/**
 *
 * @author D. Campione
 *
 */
public class JsonExpression implements IExpression {

    private Column column;

    private List<String> idents = new ArrayList<String>();

    @Override
    public void accept(IExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public List<String> getIdents() {
        return idents;
    }

    public void setIdents(List<String> idents) {
        this.idents = idents;
    }

    public void addIdent(String ident) {
        idents.add(ident);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(column.toString());
        for (String ident : idents) {
            b.append("->").append(ident);
        }
        return b.toString();
    }
}