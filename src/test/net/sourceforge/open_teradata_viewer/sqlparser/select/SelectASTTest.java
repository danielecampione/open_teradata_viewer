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

package test.net.sourceforge.open_teradata_viewer.sqlparser.select;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserTreeConstants;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.SimpleNode;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.OrderByElement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectExpressionItem;

/**
 *
 *
 * @author D. Campione
 *
 */
public class SelectASTTest extends TestCase {

    public SelectASTTest(String name) {
        super(name);
    }

    public void testSelectASTColumn() throws SQLParserException {
        String sql = "SELECT  a,  b FROM  mytable  order by   b,  c";
        StringBuilder b = new StringBuilder(sql);
        IStatement stmt = CCSqlParserUtil.parse(sql);
        Select select = (Select) stmt;
        PlainSelect ps = (PlainSelect) select.getSelectBody();
        for (ISelectItem item : ps.getSelectItems()) {
            SelectExpressionItem sei = (SelectExpressionItem) item;
            Column c = (Column) sei.getExpression();
            SimpleNode astNode = c.getASTNode();
            assertNotNull(astNode);
            b.setCharAt(astNode.jjtGetFirstToken().beginColumn - 1, '*');
        }
        for (OrderByElement item : ps.getOrderByElements()) {
            Column c = (Column) item.getExpression();
            SimpleNode astNode = c.getASTNode();
            assertNotNull(astNode);
            b.setCharAt(astNode.jjtGetFirstToken().beginColumn - 1, '#');
        }
        assertEquals("SELECT  *,  * FROM  mytable  order by   #,  #",
                b.toString());
    }

    public void testSelectASTNode() throws SQLParserException {
        String sql = "SELECT  a,  b FROM  mytable  order by   b,  c";
        StringBuilder b = new StringBuilder(sql);
        SimpleNode node = (SimpleNode) CCSqlParserUtil.parseAST(sql);
        node.dump("*");
        assertEquals(CCSqlParserTreeConstants.JJTSTATEMENT, 0 /*node.getId()*/);
    }
}