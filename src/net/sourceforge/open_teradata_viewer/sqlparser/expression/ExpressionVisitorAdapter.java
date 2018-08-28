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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Addition;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Concat;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Division;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Modulo;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Multiplication;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Subtraction;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.conditional.AndExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.conditional.OrExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.Between;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.EqualsTo;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExistsExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.GreaterThan;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.GreaterThanEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsListVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.InExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IsNullExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.LikeExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.Matches;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MinorThan;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MinorThanEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MultiExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.NotEqualsTo;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.AllColumns;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.AllTableColumns;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ExpressionListItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.FunctionItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IPivotVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectItemVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.OrderByElement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Pivot;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PivotXml;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SelectExpressionItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.WithItem;

/**
 *
 *
 * @author D. Campione
 *
 */
public class ExpressionVisitorAdapter implements IExpressionVisitor,
        IItemsListVisitor, IPivotVisitor, ISelectItemVisitor {

    private ISelectVisitor selectVisitor;

    public ISelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setSelectVisitor(ISelectVisitor selectVisitor) {
        this.selectVisitor = selectVisitor;
    }

    @Override
    public void visit(NullValue value) {
    }

    @Override
    public void visit(Function function) {
        if (function.getParameters() != null) {
            function.getParameters().accept(this);
        }
        if (function.getKeep() != null) {
            function.getKeep().accept(this);
        }
    }

    @Override
    public void visit(SignedExpression expr) {
        expr.getExpression().accept(this);
    }

    @Override
    public void visit(JdbcParameter parameter) {
    }

    @Override
    public void visit(JdbcNamedParameter parameter) {
    }

    @Override
    public void visit(DoubleValue value) {
    }

    @Override
    public void visit(LongValue value) {
    }

    @Override
    public void visit(DateValue value) {
    }

    @Override
    public void visit(TimeValue value) {
    }

    @Override
    public void visit(TimestampValue value) {
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override
    public void visit(StringValue value) {
    }

    @Override
    public void visit(Addition expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Division expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Multiplication expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Subtraction expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AndExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(OrExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Between expr) {
        expr.getLeftExpression().accept(this);
        expr.getBetweenExpressionStart().accept(this);
        expr.getBetweenExpressionEnd().accept(this);
    }

    @Override
    public void visit(EqualsTo expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThan expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThanEquals expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(InExpression expr) {
        if (expr.getLeftExpression() != null) {
            expr.getLeftExpression().accept(this);
        } else if (expr.getLeftItemsList() != null) {
            expr.getLeftItemsList().accept(this);
        }
        expr.getRightItemsList().accept(this);
    }

    @Override
    public void visit(IsNullExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(LikeExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThan expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThanEquals expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(NotEqualsTo expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Column column) {
    }

    @Override
    public void visit(SubSelect subSelect) {
        if (selectVisitor != null) {
            for (WithItem item : subSelect.getWithItemsList()) {
                item.accept(selectVisitor);
            }
            subSelect.getSelectBody().accept(selectVisitor);
        }
        if (subSelect.getPivot() != null) {
            subSelect.getPivot().accept(this);
        }
    }

    @Override
    public void visit(CaseExpression expr) {
        expr.getSwitchExpression().accept(this);
        for (IExpression x : expr.getWhenClauses()) {
            x.accept(this);
        }
        expr.getElseExpression().accept(this);
    }

    @Override
    public void visit(WhenClause expr) {
        expr.getWhenExpression().accept(this);
        expr.getThenExpression().accept(this);
    }

    @Override
    public void visit(ExistsExpression expr) {
        expr.getRightExpression().accept(this);
    }

    @Override
    public void visit(AllComparisonExpression expr) {
    }

    @Override
    public void visit(AnyComparisonExpression expr) {
    }

    @Override
    public void visit(Concat expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Matches expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseAnd expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseOr expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseXor expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(CastExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(Modulo expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AnalyticExpression expr) {
        expr.getExpression().accept(this);
        expr.getDefaultValue().accept(this);
        expr.getOffset().accept(this);
        if (expr.getKeep() != null) {
            expr.getKeep().accept(this);
        }
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
        }

        expr.getWindowElement().getRange().getStart().getExpression()
                .accept(this);
        expr.getWindowElement().getRange().getEnd().getExpression()
                .accept(this);
        expr.getWindowElement().getOffset().getExpression().accept(this);
    }

    @Override
    public void visit(ExtractExpression expr) {
        expr.getExpression().accept(this);
    }

    @Override
    public void visit(IntervalExpression expr) {
    }

    @Override
    public void visit(OracleHierarchicalExpression expr) {
        expr.getConnectExpression().accept(this);
        expr.getStartExpression().accept(this);
    }

    @Override
    public void visit(RegExpMatchOperator expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        for (IExpression expr : expressionList.getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list);
        }
    }

    protected void visitBinaryExpression(BinaryExpression expr) {
        expr.getLeftExpression().accept(this);
        expr.getRightExpression().accept(this);
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        visit(jsonExpr.getColumn());
    }

    @Override
    public void visit(RegExpMySQLOperator expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(WithinGroupExpression wgexpr) {
        wgexpr.getExprList().accept(this);
        for (OrderByElement element : wgexpr.getOrderByElements()) {
            element.getExpression().accept(this);
        }
    }

    @Override
    public void visit(UserVariable var) {
    }

    @Override
    public void visit(NumericBind bind) {
    }

    @Override
    public void visit(KeepExpression expr) {
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
        }
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
        for (IExpression expr : groupConcat.getExpressionList()
                .getExpressions()) {
            expr.accept(this);
        }
        if (groupConcat.getOrderByElements() != null) {
            for (OrderByElement element : groupConcat.getOrderByElements()) {
                element.getExpression().accept(this);
            }
        }
    }

    @Override
    public void visit(Pivot pivot) {
        for (FunctionItem item : pivot.getFunctionItems()) {
            item.getFunction().accept(this);
        }
        for (Column col : pivot.getForColumns()) {
            col.accept(this);
        }
        if (pivot.getSingleInItems() != null) {
            for (SelectExpressionItem item : pivot.getSingleInItems()) {
                item.accept(this);
            }
        }

        if (pivot.getMultiInItems() != null) {
            for (ExpressionListItem item : pivot.getMultiInItems()) {
                item.getExpressionList().accept(this);
            }
        }
    }

    @Override
    public void visit(PivotXml pivot) {
        for (FunctionItem item : pivot.getFunctionItems()) {
            item.getFunction().accept(this);
        }
        for (Column col : pivot.getForColumns()) {
            col.accept(this);
        }
        if (pivot.getInSelect() != null) {
            if (selectVisitor != null) {
                pivot.getInSelect().accept(selectVisitor);
            }
        }
    }

    @Override
    public void visit(AllColumns allColumns) {

    }

    @Override
    public void visit(AllTableColumns allTableColumns) {

    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        selectExpressionItem.getExpression().accept(this);
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        for (IExpression expr : rowConstructor.getExprList().getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(HexValue hexValue) {
    }
}