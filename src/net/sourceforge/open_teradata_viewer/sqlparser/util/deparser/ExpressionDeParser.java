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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.Alias;
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
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.ISupportsOldTeradataJoinSyntax;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.InExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IsNullExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.LikeExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.Matches;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MinorThan;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MinorThanEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MultiExpressionList;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.NotEqualsTo;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.OldTeradataJoinBinaryExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.ISelectVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from ISqlParser hierarchy into a
 * string) an {@link net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression}.
 * 
 * @author D. Campione
 * 
 */
public class ExpressionDeParser implements IExpressionVisitor,
        IItemsListVisitor {

    private StringBuilder buffer;
    private ISelectVisitor selectVisitor;
    private boolean useBracketsInExprList = true;

    public ExpressionDeParser() {
    }

    /**
     * @param selectVisitor a ISelectVisitor to de-parse SubSelects. It has to
     * share the same<br> StringBuilder as this object in order to work, as:
     *
     * <pre>
     * <code>
     * StringBuilder myBuf = new StringBuilder();
     * MySelectDeparser selectDeparser = new  MySelectDeparser();
     * selectDeparser.setBuffer(myBuf);
     * ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeparser, myBuf);
     * </code>
     * </pre>
     * 
     * @param buffer the buffer that will be filled with the expression.
     */
    public ExpressionDeParser(ISelectVisitor selectVisitor, StringBuilder buffer) {
        this.selectVisitor = selectVisitor;
        this.buffer = buffer;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override
    public void visit(Addition addition) {
        visitBinaryExpression(addition, " + ");
    }

    @Override
    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression, " AND ");
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        if (between.isNot()) {
            buffer.append(" NOT");
        }

        buffer.append(" BETWEEN ");
        between.getBetweenExpressionStart().accept(this);
        buffer.append(" AND ");
        between.getBetweenExpressionEnd().accept(this);

    }

    @Override
    public void visit(EqualsTo equalsTo) {
        visitOldTeradataJoinBinaryExpression(equalsTo, " = ");
    }

    @Override
    public void visit(Division division) {
        visitBinaryExpression(division, " / ");

    }

    @Override
    public void visit(DoubleValue doubleValue) {
        buffer.append(doubleValue.toString());

    }

    public void visitOldTeradataJoinBinaryExpression(
            OldTeradataJoinBinaryExpression expression, String operator) {
        if (expression.isNot()) {
            buffer.append(" NOT ");
        }
        expression.getLeftExpression().accept(this);
        if (expression.getOldTeradataJoinSyntax() == EqualsTo.TERADATA_JOIN_RIGHT) {
            buffer.append("(+)");
        }
        buffer.append(operator);
        expression.getRightExpression().accept(this);
        if (expression.getOldTeradataJoinSyntax() == EqualsTo.TERADATA_JOIN_LEFT) {
            buffer.append("(+)");
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        visitOldTeradataJoinBinaryExpression(greaterThan, " > ");
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        visitOldTeradataJoinBinaryExpression(greaterThanEquals, " >= ");

    }

    @Override
    public void visit(InExpression inExpression) {
        if (inExpression.getLeftExpression() == null) {
            inExpression.getLeftItemsList().accept(this);
        } else {
            inExpression.getLeftExpression().accept(this);
            if (inExpression.getOldTeradataJoinSyntax() == ISupportsOldTeradataJoinSyntax.TERADATA_JOIN_RIGHT) {
                buffer.append("(+)");
            }
        }
        if (inExpression.isNot()) {
            buffer.append(" NOT");
        }
        buffer.append(" IN ");

        inExpression.getRightItemsList().accept(this);
    }

    @Override
    public void visit(InverseExpression inverseExpression) {
        buffer.append("-");
        inverseExpression.getExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        isNullExpression.getLeftExpression().accept(this);
        if (isNullExpression.isNot()) {
            buffer.append(" IS NOT NULL");
        } else {
            buffer.append(" IS NULL");
        }
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        buffer.append("?");

    }

    @Override
    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression, " LIKE ");
        String escape = likeExpression.getEscape();
        if (escape != null) {
            buffer.append(" ESCAPE '").append(escape).append('\'');
        }
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        if (existsExpression.isNot()) {
            buffer.append("NOT EXISTS ");
        } else {
            buffer.append("EXISTS ");
        }
        existsExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(LongValue longValue) {
        buffer.append(longValue.getStringValue());

    }

    @Override
    public void visit(MinorThan minorThan) {
        visitOldTeradataJoinBinaryExpression(minorThan, " < ");

    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        visitOldTeradataJoinBinaryExpression(minorThanEquals, " <= ");

    }

    @Override
    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication, " * ");

    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        visitOldTeradataJoinBinaryExpression(notEqualsTo, " <> ");

    }

    @Override
    public void visit(NullValue nullValue) {
        buffer.append("NULL");

    }

    @Override
    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression, " OR ");

    }

    @Override
    public void visit(Parenthesis parenthesis) {
        if (parenthesis.isNot()) {
            buffer.append(" NOT ");
        }

        buffer.append("(");
        parenthesis.getExpression().accept(this);
        buffer.append(")");

    }

    @Override
    public void visit(StringValue stringValue) {
        buffer.append("'").append(stringValue.getValue()).append("'");

    }

    @Override
    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction, " - ");

    }

    private void visitBinaryExpression(BinaryExpression binaryExpression,
            String operator) {
        if (binaryExpression.isNot()) {
            buffer.append(" NOT ");
        }
        binaryExpression.getLeftExpression().accept(this);
        buffer.append(operator);
        binaryExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(SubSelect subSelect) {
        buffer.append("(");
        subSelect.getSelectBody().accept(selectVisitor);
        buffer.append(")");
    }

    @Override
    public void visit(Column tableColumn) {
        final Alias alias = tableColumn.getTable().getAlias();
        String tableName = null;
        if (alias != null) {
            tableName = alias.getName();
        } else if (tableName == null) {
            tableName = tableColumn.getTable().getWholeTableName();
        }
        if (tableName != null) {
            buffer.append(tableName).append(".");
        }

        buffer.append(tableColumn.getColumnName());
    }

    @Override
    public void visit(Function function) {
        if (function.isEscaped()) {
            buffer.append("{fn ");
        }

        buffer.append(function.getName());
        if (function.isAllColumns()) {
            buffer.append("(*)");
        } else if (function.getParameters() == null) {
            buffer.append("()");
        } else {
            boolean oldUseBracketsInExprList = useBracketsInExprList;
            if (function.isDistinct()) {
                useBracketsInExprList = false;
                buffer.append("(DISTINCT ");
            }
            visit(function.getParameters());
            useBracketsInExprList = oldUseBracketsInExprList;
            if (function.isDistinct()) {
                buffer.append(")");
            }
        }

        if (function.isEscaped()) {
            buffer.append("}");
        }
    }

    @Override
    public void visit(ExpressionList expressionList) {
        if (useBracketsInExprList) {
            buffer.append("(");
        }
        for (Iterator<IExpression> iter = expressionList.getExpressions()
                .iterator(); iter.hasNext();) {
            IExpression expression = iter.next();
            expression.accept(this);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        if (useBracketsInExprList) {
            buffer.append(")");
        }
    }

    public ISelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setSelectVisitor(ISelectVisitor visitor) {
        selectVisitor = visitor;
    }

    @Override
    public void visit(DateValue dateValue) {
        buffer.append("{d '").append(dateValue.getValue().toString())
                .append("'}");
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        buffer.append("{ts '").append(timestampValue.getValue().toString())
                .append("'}");
    }

    @Override
    public void visit(TimeValue timeValue) {
        buffer.append("{t '").append(timeValue.getValue().toString())
                .append("'}");
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        buffer.append("CASE ");
        IExpression switchExp = caseExpression.getSwitchExpression();
        if (switchExp != null) {
            switchExp.accept(this);
            buffer.append(" ");
        }

        for (IExpression exp : caseExpression.getWhenClauses()) {
            exp.accept(this);
        }

        IExpression elseExp = caseExpression.getElseExpression();
        if (elseExp != null) {
            buffer.append("ELSE ");
            elseExp.accept(this);
            buffer.append(" ");
        }

        buffer.append("END");
    }

    @Override
    public void visit(WhenClause whenClause) {
        buffer.append("WHEN ");
        whenClause.getWhenExpression().accept(this);
        buffer.append(" THEN ");
        whenClause.getThenExpression().accept(this);
        buffer.append(" ");
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        buffer.append(" ALL ");
        allComparisonExpression.getSubSelect()
                .accept((IExpressionVisitor) this);
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        buffer.append(" ANY ");
        anyComparisonExpression.getSubSelect()
                .accept((IExpressionVisitor) this);
    }

    @Override
    public void visit(Concat concat) {
        visitBinaryExpression(concat, " || ");
    }

    @Override
    public void visit(Matches matches) {
        visitOldTeradataJoinBinaryExpression(matches, " @@ ");
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd, " & ");
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr, " | ");
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor, " ^ ");
    }

    @Override
    public void visit(CastExpression cast) {
        if (cast.isUseCastKeyword()) {
            buffer.append("CAST(");
            buffer.append(cast.getLeftExpression());
            buffer.append(" AS ");
            buffer.append(cast.getType());
            buffer.append(")");
        } else {
            buffer.append(cast.getLeftExpression());
            buffer.append("::");
            buffer.append(cast.getType());
        }
    }

    @Override
    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo, " % ");
    }

    @Override
    public void visit(AnalyticExpression aexpr) {
        buffer.append(aexpr.toString());
    }

    @Override
    public void visit(ExtractExpression eexpr) {
        buffer.append(eexpr.toString());
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        for (Iterator<ExpressionList> it = multiExprList.getExprList()
                .iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
    }

    @Override
    public void visit(IntervalExpression iexpr) {
        buffer.append(iexpr.toString());
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        buffer.append(jdbcNamedParameter.toString());
    }

    @Override
    public void visit(TeradataHierarchicalExpression texpr) {
        buffer.append(texpr.toString());
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
        visitBinaryExpression(rexpr, " " + rexpr.getStringExpression() + " ");
    }
}