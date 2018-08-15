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

import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.alter.Alter;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.index.CreateIndex;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table.CreateTable;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.create.view.CreateView;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.delete.Delete;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.drop.Drop;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.insert.Insert;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.replace.Replace;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.WithItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.truncate.Truncate;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.update.Update;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StatementDeParser implements IStatementVisitor {

    private StringBuilder buffer;

    public StatementDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override
    public void visit(CreateIndex createIndex) {
        CreateIndexDeParser createIndexDeParser = new CreateIndexDeParser(
                buffer);
        createIndexDeParser.deParse(createIndex);
    }

    @Override
    public void visit(CreateTable createTable) {
        CreateTableDeParser createTableDeParser = new CreateTableDeParser(
                buffer);
        createTableDeParser.deParse(createTable);
    }

    @Override
    public void visit(CreateView createView) {
        CreateViewDeParser createViewDeParser = new CreateViewDeParser(buffer);
        createViewDeParser.deParse(createView);
    }

    @Override
    public void visit(Delete delete) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(
                selectDeParser, buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        DeleteDeParser deleteDeParser = new DeleteDeParser(expressionDeParser,
                buffer);
        deleteDeParser.deParse(delete);
    }

    @Override
    public void visit(Drop drop) {
    }

    @Override
    public void visit(Insert insert) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(
                selectDeParser, buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        InsertDeParser insertDeParser = new InsertDeParser(expressionDeParser,
                selectDeParser, buffer);
        insertDeParser.deParse(insert);
    }

    @Override
    public void visit(Replace replace) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(
                selectDeParser, buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        ReplaceDeParser replaceDeParser = new ReplaceDeParser(
                expressionDeParser, selectDeParser, buffer);
        replaceDeParser.deParse(replace);
    }

    @Override
    public void visit(Select select) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(
                selectDeParser, buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        if (select.getWithItemsList() != null
                && !select.getWithItemsList().isEmpty()) {
            buffer.append("WITH ");
            for (Iterator<WithItem> iter = select.getWithItemsList().iterator(); iter
                    .hasNext();) {
                WithItem withItem = iter.next();
                buffer.append(withItem);
                if (iter.hasNext()) {
                    buffer.append(",");
                }
                buffer.append(" ");
            }
        }
        select.getSelectBody().accept(selectDeParser);
    }

    @Override
    public void visit(Truncate truncate) {
    }

    @Override
    public void visit(Update update) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(
                selectDeParser, buffer);
        UpdateDeParser updateDeParser = new UpdateDeParser(expressionDeParser,
                buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        updateDeParser.deParse(update);
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override
    public void visit(Alter alter) {
    }
}