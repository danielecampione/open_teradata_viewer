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

package test.net.sourceforge.open_teradata_viewer.sqlparser.statement;

import static org.junit.Assert.assertEquals;

import java.util.Stack;

import net.sourceforge.open_teradata_viewer.sqlparser.SQLParserException;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.ExpressionVisitorAdapter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.JdbcNamedParameter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.conditional.AndExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserUtil;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.StatementVisitorAdapter;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectVisitorAdapter;

import org.junit.Test;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class AdaptersTest {

    /** Test extracting JDBC named parameters using adapters. */
    @Test
    public void testAdapters() throws SQLParserException {
        String sql = "SELECT * FROM MYTABLE WHERE COLUMN_A = :paramA AND COLUMN_B <> :paramB";
        IStatement stmnt = CCSqlParserUtil.parse(sql);

        final Stack<Pair<String, String>> params = new Stack<Pair<String, String>>();
        stmnt.accept(new StatementVisitorAdapter() {
            @Override
            public void visit(Select select) {
                select.getSelectBody().accept(new SelectVisitorAdapter() {
                    @Override
                    public void visit(PlainSelect plainSelect) {
                        plainSelect.getWhere().accept(
                                new ExpressionVisitorAdapter() {
                                    @Override
                                    protected void visitBinaryExpression(
                                            BinaryExpression expr) {
                                        if (!(expr instanceof AndExpression)) {
                                            params.push(new Pair<String, String>(
                                                    null, null));
                                        }
                                        super.visitBinaryExpression(expr);
                                    }

                                    @Override
                                    public void visit(Column column) {
                                        params.push(new Pair<String, String>(
                                                column.getColumnName(), params
                                                        .pop().getRight()));
                                    }

                                    @Override
                                    public void visit(
                                            JdbcNamedParameter parameter) {
                                        params.push(new Pair<String, String>(
                                                params.pop().getLeft(),
                                                parameter.getName()));
                                    }
                                });
                    }
                });
            }
        });

        assertEquals(2, params.size());
        Pair<String, String> param2 = params.pop();
        assertEquals("COLUMN_B", param2.getLeft());
        assertEquals("paramB", param2.getRight());
        Pair<String, String> param1 = params.pop();
        assertEquals("COLUMN_A", param1.getLeft());
        assertEquals("paramA", param1.getRight());
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private static class Pair<L, R> {

        private final L left;
        private final R right;

        private Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        public boolean isEmpty() {
            return left == null && right == null;
        }

        public boolean isFull() {
            return left != null && right != null;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Pair{");
            sb.append("left=").append(left);
            sb.append(", right=").append(right);
            sb.append('}');
            return sb.toString();
        }
    }
}