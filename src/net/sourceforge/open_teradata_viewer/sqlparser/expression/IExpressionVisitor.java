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
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.GreaterThan;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.GreaterThanEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.InExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IsNullExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.LikeExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.Matches;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MinorThan;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.MinorThanEquals;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.NotEqualsTo;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;

/**
 *
 *
 * @author D. Campione
 *
 */
public interface IExpressionVisitor {

    void visit(NullValue nullValue);

    void visit(Function function);

    void visit(SignedExpression signedExpression);

    void visit(JdbcParameter jdbcParameter);

    void visit(JdbcNamedParameter jdbcNamedParameter);

    void visit(DoubleValue doubleValue);

    void visit(LongValue longValue);

    void visit(HexValue hexValue);

    void visit(DateValue dateValue);

    void visit(TimeValue timeValue);

    void visit(TimestampValue timestampValue);

    void visit(Parenthesis parenthesis);

    void visit(StringValue stringValue);

    void visit(Addition addition);

    void visit(Division division);

    void visit(Multiplication multiplication);

    void visit(Subtraction subtraction);

    void visit(AndExpression andExpression);

    void visit(OrExpression orExpression);

    void visit(Between between);

    void visit(EqualsTo equalsTo);

    void visit(GreaterThan greaterThan);

    void visit(GreaterThanEquals greaterThanEquals);

    void visit(InExpression inExpression);

    void visit(IsNullExpression isNullExpression);

    void visit(LikeExpression likeExpression);

    void visit(MinorThan minorThan);

    void visit(MinorThanEquals minorThanEquals);

    void visit(NotEqualsTo notEqualsTo);

    void visit(Column tableColumn);

    void visit(SubSelect subSelect);

    void visit(CaseExpression caseExpression);

    void visit(WhenClause whenClause);

    void visit(ExistsExpression existsExpression);

    void visit(AllComparisonExpression allComparisonExpression);

    void visit(AnyComparisonExpression anyComparisonExpression);

    void visit(Concat concat);

    void visit(Matches matches);

    void visit(BitwiseAnd bitwiseAnd);

    void visit(BitwiseOr bitwiseOr);

    void visit(BitwiseXor bitwiseXor);

    void visit(CastExpression cast);

    void visit(Modulo modulo);

    void visit(AnalyticExpression aexpr);

    void visit(WithinGroupExpression wgexpr);

    void visit(ExtractExpression eexpr);

    void visit(IntervalExpression iexpr);

    void visit(OracleHierarchicalExpression texpr);

    void visit(RegExpMatchOperator rexpr);

    void visit(JsonExpression jsonExpr);

    void visit(RegExpMySQLOperator regExpMySQLOperator);

    void visit(UserVariable var);

    void visit(NumericBind bind);

    void visit(KeepExpression aexpr);

    void visit(MySQLGroupConcat groupConcat);

    void visit(RowConstructor rowConstructor);
}