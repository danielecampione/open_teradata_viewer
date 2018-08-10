/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2011, D. Campione
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

import net.sf.jsqlparser.schema.Column;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Addition;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Concat;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.arithmetic.Division;
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
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.SubSelect;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface ExpressionVisitor {

    public void visit(NullValue nullValue);
    public void visit(Function function);
    public void visit(InverseExpression inverseExpression);
    public void visit(JdbcParameter jdbcParameter);
    public void visit(DoubleValue doubleValue);
    public void visit(LongValue longValue);
    public void visit(DateValue dateValue);
    public void visit(TimeValue timeValue);
    public void visit(TimestampValue timestampValue);
    public void visit(Parenthesis parenthesis);
    public void visit(StringValue stringValue);
    public void visit(Addition addition);
    public void visit(Division division);
    public void visit(Multiplication multiplication);
    public void visit(Subtraction subtraction);
    public void visit(AndExpression andExpression);
    public void visit(OrExpression orExpression);
    public void visit(Between between);
    public void visit(EqualsTo equalsTo);
    public void visit(GreaterThan greaterThan);
    public void visit(GreaterThanEquals greaterThanEquals);
    public void visit(InExpression inExpression);
    public void visit(IsNullExpression isNullExpression);
    public void visit(LikeExpression likeExpression);
    public void visit(MinorThan minorThan);
    public void visit(MinorThanEquals minorThanEquals);
    public void visit(NotEqualsTo notEqualsTo);
    public void visit(Column tableColumn);
    public void visit(SubSelect subSelect);
    public void visit(CaseExpression caseExpression);
    public void visit(WhenClause whenClause);
    public void visit(ExistsExpression existsExpression);
    public void visit(AllComparisonExpression allComparisonExpression);
    public void visit(AnyComparisonExpression anyComparisonExpression);
    public void visit(Concat concat);
    public void visit(Matches matches);
    public void visit(BitwiseAnd bitwiseAnd);
    public void visit(BitwiseOr bitwiseOr);
    public void visit(BitwiseXor bitwiseXor);

}
