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

package net.sourceforge.open_teradata_viewer.sqlparser.statement;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.alter.Alter;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.index.CreateIndex;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.CreateTable;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.view.CreateView;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.delete.Delete;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.drop.Drop;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.execute.Execute;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.insert.Insert;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.replace.Replace;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.truncate.Truncate;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.update.Update;

/**
 *
 *
 * @author D. Campione
 *
 */
public class StatementVisitorAdapter implements IStatementVisitor {

    @Override
    public void visit(Select select) {
    }

    @Override
    public void visit(Delete delete) {
    }

    @Override
    public void visit(Update update) {
    }

    @Override
    public void visit(Insert insert) {
    }

    @Override
    public void visit(Replace replace) {
    }

    @Override
    public void visit(Drop drop) {
    }

    @Override
    public void visit(Truncate truncate) {
    }

    @Override
    public void visit(CreateIndex createIndex) {
    }

    @Override
    public void visit(CreateTable createTable) {
    }

    @Override
    public void visit(CreateView createView) {
    }

    @Override
    public void visit(Alter alter) {
    }

    @Override
    public void visit(Statements stmts) {
    }

    @Override
    public void visit(Execute execute) {
    }
}