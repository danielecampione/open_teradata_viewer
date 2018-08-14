/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.util;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.AllComparisonExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.AnalyticExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.AnyComparisonExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.BinaryExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.CaseExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.CastExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.DateValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.DoubleValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.ExtractExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.Function;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.IntervalExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.InverseExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.JdbcNamedParameter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.JdbcParameter;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.LongValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.NullValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.Parenthesis;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.StringValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.TeradataHierarchicalExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.TimeValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.TimestampValue;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.WhenClause;
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
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.delete.Delete;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.insert.Insert;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.replace.Replace;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.IFromItemVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Join;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.LateralSubSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SetOperationList;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubJoin;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ValuesList;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.WithItem;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.update.Update;

/**
 * Find all used tables within an select statement.
 * 
 * @author D. Campione
 * 
 */
public class TablesNamesFinder implements ISelectVisitor, IFromItemVisitor,
        IExpressionVisitor, IItemsListVisitor {

    private List<String> tables;
    /**
     * There are special names, that are not table names but are parsed as
     * tables. These names are collected here and are not included in the tables
     * - names anymore.
     */
    private List<String> otherItemNames;

    /** Main entry for this Tool class. A list of found tables is returned. */
    public List<String> getTableList(Delete delete) {
        init();
        tables.add(delete.getTable().getName());
        delete.getWhere().accept(this);

        return tables;
    }

    /** Main entry for this Tool class. A list of found tables is returned. */
    public List<String> getTableList(Insert insert) {
        init();
        tables.add(insert.getTable().getName());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }

        return tables;
    }

    /** Main entry for this Tool class. A list of found tables is returned. */
    public List<String> getTableList(Replace replace) {
        init();
        tables.add(replace.getTable().getName());
        if (replace.getExpressions() != null) {
            for (IExpression expression : replace.getExpressions()) {
                expression.accept(this);
            }
        }
        if (replace.getItemsList() != null) {
            replace.getItemsList().accept(this);
        }

        return tables;
    }

    /** Main entry for this Tool class. A list of found tables is returned. */
    public List<String> getTableList(Select select) {
        init();
        if (select.getWithItemsList() != null) {
            for (WithItem withItem : select.getWithItemsList()) {
                withItem.accept(this);
            }
        }
        select.getSelectBody().accept(this);

        return tables;
    }

    /** Main entry for this Tool class. A list of found tables is returned. */
    public List<String> getTableList(Update update) {
        init();
        tables.add(update.getTable().getName());
        if (update.getExpressions() != null) {
            for (IExpression expression : update.getExpressions()) {
                expression.accept(this);
            }
        }

        if (update.getFromItem() != null) {
            update.getFromItem().accept(this);
        }

        if (update.getJoins() != null) {
            for (Join join : update.getJoins()) {
                join.getRightItem().accept(this);
            }
        }

        if (update.getWhere() != null) {
            update.getWhere().accept(this);
        }

        return tables;
    }

    @Override
    public void visit(WithItem withItem) {
        otherItemNames.add(withItem.getName().toLowerCase());
        withItem.getSelectBody().accept(this);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        plainSelect.getFromItem().accept(this);

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                join.getRightItem().accept(this);
            }
        }
        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(this);
        }

    }

    @Override
    public void visit(Table tableName) {
        String tableWholeName = tableName.getWholeTableName();
        if (!otherItemNames.contains(tableWholeName.toLowerCase())
                && !tables.contains(tableWholeName)) {
            tables.add(tableWholeName);
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(this);
    }

    @Override
    public void visit(Addition addition) {
        visitBinaryExpression(addition);
    }

    @Override
    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression);
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        between.getBetweenExpressionStart().accept(this);
        between.getBetweenExpressionEnd().accept(this);
    }

    @Override
    public void visit(Column tableColumn) {
    }

    @Override
    public void visit(Division division) {
        visitBinaryExpression(division);
    }

    @Override
    public void visit(DoubleValue doubleValue) {
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        visitBinaryExpression(equalsTo);
    }

    @Override
    public void visit(Function function) {
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        visitBinaryExpression(greaterThan);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        visitBinaryExpression(greaterThanEquals);
    }

    @Override
    public void visit(InExpression inExpression) {
        inExpression.getLeftExpression().accept(this);
        inExpression.getRightItemsList().accept(this);
    }

    @Override
    public void visit(InverseExpression inverseExpression) {
        inverseExpression.getExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression);
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        existsExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(LongValue longValue) {
    }

    @Override
    public void visit(MinorThan minorThan) {
        visitBinaryExpression(minorThan);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        visitBinaryExpression(minorThanEquals);
    }

    @Override
    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        visitBinaryExpression(notEqualsTo);
    }

    @Override
    public void visit(NullValue nullValue) {
    }

    @Override
    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression);
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override
    public void visit(StringValue stringValue) {
    }

    @Override
    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction);
    }

    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        binaryExpression.getLeftExpression().accept(this);
        binaryExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        for (IExpression expression : expressionList.getExpressions()) {
            expression.accept(this);
        }

    }

    @Override
    public void visit(DateValue dateValue) {
    }

    @Override
    public void visit(TimestampValue timestampValue) {
    }

    @Override
    public void visit(TimeValue timeValue) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor#visit(net.sourceforge.open_teradata_viewer.sqlparser.expression.CaseExpression)
     */
    @Override
    public void visit(CaseExpression caseExpression) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpressionVisitor#visit(net.sourceforge.open_teradata_viewer.sqlparser.expression.WhenClause)
     */
    @Override
    public void visit(WhenClause whenClause) {
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        allComparisonExpression.getSubSelect().getSelectBody().accept(this);
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
    }

    @Override
    public void visit(SubJoin subjoin) {
        subjoin.getLeft().accept(this);
        subjoin.getJoin().getRightItem().accept(this);
    }

    @Override
    public void visit(Concat concat) {
        visitBinaryExpression(concat);
    }

    @Override
    public void visit(Matches matches) {
        visitBinaryExpression(matches);
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd);
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr);
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor);
    }

    @Override
    public void visit(CastExpression cast) {
        cast.getLeftExpression().accept(this);
    }

    @Override
    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo);
    }

    @Override
    public void visit(AnalyticExpression analytic) {
    }

    @Override
    public void visit(SetOperationList list) {
        for (PlainSelect plainSelect : list.getPlainSelects()) {
            visit(plainSelect);
        }
    }

    @Override
    public void visit(ExtractExpression eexpr) {
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        lateralSubSelect.getSubSelect().getSelectBody().accept(this);
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList exprList : multiExprList.getExprList()) {
            exprList.accept(this);
        }
    }

    @Override
    public void visit(ValuesList valuesList) {
    }

    private void init() {
        otherItemNames = new ArrayList<String>();
        tables = new ArrayList<String>();
    }

    @Override
    public void visit(IntervalExpression iexpr) {
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
    }

    @Override
    public void visit(TeradataHierarchicalExpression texpr) {
    }
}