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

package net.sourceforge.open_teradata_viewer.sqlparser.statement;

import java.util.List;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class Statements {

    private List<IStatement> statements;

    public List<IStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<IStatement> statements) {
        this.statements = statements;
    }

    public void accept(IStatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (IStatement stmt : statements) {
            b.append(stmt.toString()).append(";\n");
        }
        return b.toString();
    }
}