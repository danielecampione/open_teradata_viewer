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
public interface IStatementVisitor {

    void visit(Select select);

    void visit(Delete delete);

    void visit(Update update);

    void visit(Insert insert);

    void visit(Replace replace);

    void visit(Drop drop);

    void visit(Truncate truncate);

    void visit(CreateIndex createIndex);

    void visit(CreateTable createTable);

    void visit(CreateView createView);

    void visit(Alter alter);

    void visit(Statements stmts);
}