/*
 * Open Teradata Viewer ( test util sql formatter )
 * Copyright (C), D. Campione
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

package test.net.sourceforge.open_teradata_viewer.util.sql_formatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.util.sql_formatter.FormatStyle;
import net.sourceforge.open_teradata_viewer.util.sql_formatter.IFormatter;

/**
 * Tests for {@link FormatStyle#BASIC}, the formatter behind the "Format SQL
 * code" menu action - i.e. everyday SELECT/INSERT/UPDATE/DELETE statements.
 *
 * <p>Unlike the two existing test classes in this package (which mostly
 * assert on a prefix or use {@code assertTrue(expected.equals(actual))}),
 * these use {@link org.junit.jupiter.api.Assertions#assertEquals(Object,
 * Object)} throughout: the formatter's whole output is exact and
 * deterministic, and a multi-line SQL diff is far easier to read from
 * JUnit's own expected/actual report than from a bare boolean failure.</p>
 *
 * <p>The formatter always emits '\n' as its line separator, regardless of
 * platform (it never goes through a system-dependent writer), so the
 * expected strings below use a literal "\n" rather than
 * {@code System.lineSeparator()}.</p>
 *
 * @author D. Campione
 *
 */
public class TestFormatStyleBasic {

    private static String formatBasic(String sql) {
        IFormatter formatter = FormatStyle.BASIC.getFormatter();
        return formatter.format(sql);
    }

    @Test
    public void test_formatBasic_selectJoinGroupByOrderBy() {
        String sql = "select a.id, a.name, count(*) from customers a inner join orders b on a.id = b.customer_id "
                + "where a.status in (1,2) group by a.id, a.name order by a.id";
        String expected = "select a.id,\n" +
                "    a.name,\n" +
                "    count(*)\n" +
                "from customers a\n" +
                "inner join orders b\n" +
                "    on a.id = b.customer_id\n" +
                "where a.status in (1, 2)\n" +
                "group by a.id,\n" +
                "    a.name\n" +
                "order by a.id";
        assertEquals(expected, formatBasic(sql));
    }

    /** Regression: the AND that belongs to a BETWEEN must stay on the same
     *  line as the BETWEEN, unlike a plain, independent AND. */
    @Test
    public void test_formatBasic_betweenAndDoesNotBreak() {
        String sql = "select * from employees where hire_date between '2020-01-01' and '2020-12-31' and dept_id = 5";
        String expected = "select *\n" +
                "from employees\n" +
                "where hire_date between '2020-01-01' and '2020-12-31'\n" +
                "    and dept_id = 5";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_caseWhenElseEnd() {
        String sql = "select id, case when salary > 5000 then 'high' when salary > 2000 then 'medium' else 'low' end "
                + "as bracket from employees order by id";
        String expected = "select id,\n" +
                "    case\n" +
                "        when salary > 5000 then 'high'\n" +
                "        when salary > 2000 then 'medium'\n" +
                "        else 'low'\n" +
                "    end as bracket\n" +
                "from employees\n" +
                "order by id";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_caseWithoutElse() {
        String sql = "select case when x > 0 then 1 end from t";
        String expected = "select case\n" +
                "    when x > 0 then 1\n" +
                "end\n" +
                "from t";
        assertEquals(expected, formatBasic(sql));
    }

    /** A CASE nested inside another CASE's WHEN: the inner WHEN/ELSE must
     *  indent one level deeper than the outer one, and the inner END must
     *  return to the outer WHEN's own level, not the outer CASE's. */
    @Test
    public void test_formatBasic_nestedCase() {
        String sql = "select id, case when dept_id = 1 then case when salary > 1000 then 'A' else 'B' end "
                + "else 'C' end as grp from employees";
        String expected = "select id,\n" +
                "    case\n" +
                "        when dept_id = 1 then case\n" +
                "            when salary > 1000 then 'A'\n" +
                "            else 'B'\n" +
                "        end\n" +
                "        else 'C'\n" +
                "    end as grp\n" +
                "from employees";
        assertEquals(expected, formatBasic(sql));
    }

    /** A CASE that is just one argument of a function call must not force
     *  the enclosing, "attached" parentheses onto their own line: only a
     *  standalone paren (a subquery, a group, ...) gets that treatment. */
    @Test
    public void test_formatBasic_caseInsideFunctionCallStaysCompact() {
        String sql = "select coalesce(nullif(case when status = 'A' then 1 when status = 'B' then 2 else null end, 0), "
                + "-1) as code from employees";
        String expected = "select coalesce(nullif(case\n" +
                "    when status = 'A' then 1\n" +
                "    when status = 'B' then 2\n" +
                "    else null\n" +
                "end, 0), -1) as code\n" +
                "from employees";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_caseAsJoinCondition() {
        String sql = "select a.id from a join b on a.id = b.id and case when a.type = 1 then a.x else a.y end = b.z";
        String expected = "select a.id\n" +
                "from a\n" +
                "join b\n" +
                "    on a.id = b.id\n" +
                "    and case\n" +
                "        when a.type = 1 then a.x\n" +
                "        else a.y\n" +
                "    end = b.z";
        assertEquals(expected, formatBasic(sql));
    }

    /** Regression: a line comment must force a newline afterwards, or the
     *  next token visually (and misleadingly) reads as part of the comment. */
    @Test
    public void test_formatBasic_lineCommentForcesNewline() {
        String sql = "select id, -- primary key\nname from employees";
        String expected = "select id,\n" +
                "    -- primary key\n" +
                "    name\n" +
                "from employees";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_blockCommentStaysInline() {
        String sql = "select id, /* important */ name from employees where id = 1";
        String expected = "select id,\n" +
                "    /* important */ name\n" +
                "from employees\n" +
                "where id = 1";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_multipleStatementsWithComment() {
        String sql = "select * from t1; -- first query\nselect * from t2 where x = 1";
        String expected = "select *\n" +
                "from t1;\n" +
                "-- first query\n" +
                "select *\n" +
                "from t2\n" +
                "where x = 1";
        assertEquals(expected, formatBasic(sql));
    }

    /** A doubled single quote ('') is SQL's own escape for a literal quote,
     *  not a token boundary: it must stay inside the same string token. */
    @Test
    public void test_formatBasic_escapedQuoteInLiteral() {
        String sql = "select * from employees where name = 'O''Brien'";
        String expected = "select *\n" +
                "from employees\n" +
                "where name = 'O''Brien'";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_unionAll() {
        String sql = "select id, name from active_employees union all select id, name from archived_employees "
                + "order by id";
        String expected = "select id,\n" +
                "    name\n" +
                "from active_employees\n" +
                "union all\n" +
                "select id,\n" +
                "    name\n" +
                "from archived_employees\n" +
                "order by id";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_plainUnion() {
        String sql = "select id from a union select id from b";
        String expected = "select id\n" +
                "from a\n" +
                "union\n" +
                "select id\n" +
                "from b";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_existsAndNotExists() {
        String sql = "select e.id, e.name from employees e where exists (select 1 from bonuses b where b.emp_id = e.id "
                + "and b.year = 2025) and not exists (select 1 from terminations t where t.emp_id = e.id)";
        String expected = "select e.id,\n" +
                "    e.name\n" +
                "from employees e\n" +
                "where exists (\n" +
                "    select 1\n" +
                "    from bonuses b\n" +
                "    where b.emp_id = e.id\n" +
                "        and b.year = 2025\n" +
                ")\n" +
                "    and not exists (\n" +
                "    select 1\n" +
                "    from terminations t\n" +
                "    where t.emp_id = e.id\n" +
                "    )";
        assertEquals(expected, formatBasic(sql));
    }

    /** Regression: after a subquery paren closes, an unrelated IN value list
     *  later in the same statement must not inherit its comma-break context. */
    @Test
    public void test_formatBasic_inSubqueryThenInValueListStaysCompact() {
        String sql = "select * from orders where customer_id in (select id from customers where country = 'IT') "
                + "and status in ('OPEN','PENDING')";
        String expected = "select *\n" +
                "from orders\n" +
                "where customer_id in (\n" +
                "    select id\n" +
                "    from customers\n" +
                "    where country = 'IT'\n" +
                ")\n" +
                "    and status in ('OPEN', 'PENDING')";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_subqueryInFromWithAlias() {
        String sql = "select e.id, d.name from (select * from employees where active = 1) e "
                + "left join departments d on e.dept_id = d.id where e.salary between 1000 and 5000";
        String expected = "select e.id,\n" +
                "    d.name\n" +
                "from (\n" +
                "    select *\n" +
                "    from employees\n" +
                "    where active = 1\n" +
                ") e\n" +
                "left join departments d\n" +
                "    on e.dept_id = d.id\n" +
                "where e.salary between 1000 and 5000";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_singleCte() {
        String sql = "with recent as (select id, amount from payments where year = 2025) select id from recent";
        String expected = "with recent as (\n" +
                "    select id,\n" +
                "        amount\n" +
                "    from payments\n" +
                "    where year = 2025\n" +
                ")\n" +
                "select id\n" +
                "from recent";
        assertEquals(expected, formatBasic(sql));
    }

    /** Regression: sibling CTE definitions line up at column 0 (WITH's own
     *  level), they do not indent as if they were items in a value list. */
    @Test
    public void test_formatBasic_multipleCtesAlignAtColumnZero() {
        String sql = "with dept_totals as (select dept_id, sum(salary) as total from employees group by dept_id), "
                + "top_depts as (select dept_id from dept_totals where total > 100000) "
                + "select e.id, e.name, d.total from employees e join dept_totals d on e.dept_id = d.dept_id "
                + "where e.dept_id in (select dept_id from top_depts) order by d.total desc";
        String expected = "with dept_totals as (\n" +
                "    select dept_id,\n" +
                "        sum(salary) as total\n" +
                "    from employees\n" +
                "    group by dept_id\n" +
                "),\n" +
                "top_depts as (\n" +
                "    select dept_id\n" +
                "    from dept_totals\n" +
                "    where total > 100000\n" +
                ")\n" +
                "select e.id,\n" +
                "    e.name,\n" +
                "    d.total\n" +
                "from employees e\n" +
                "join dept_totals d\n" +
                "    on e.dept_id = d.dept_id\n" +
                "where e.dept_id in (\n" +
                "    select dept_id\n" +
                "    from top_depts\n" +
                ")\n" +
                "order by d.total desc";
        assertEquals(expected, formatBasic(sql));
    }

    /** Regression: PARTITION BY / ORDER BY inside a window function's OVER
     *  (...) describe the window, not the outer query - they must stay on
     *  one line instead of being treated as top-level query clauses. */
    @Test
    public void test_formatBasic_windowFunctionOverStaysCompact() {
        String sql = "select id, salary, row_number() over (partition by dept_id order by salary desc) as rnk, "
                + "sum(salary) over (partition by dept_id) as dept_total from employees";
        String expected = "select id,\n" +
                "    salary,\n" +
                "    row_number() over (partition by dept_id order by salary desc) as rnk,\n" +
                "    sum(salary) over (partition by dept_id) as dept_total\n" +
                "from employees";
        assertEquals(expected, formatBasic(sql));
    }

    /** Regression: a forced closing-paren line aligns with the indent of the
     *  line that opened it, even when that open happened mid-line (here, one
     *  level deeper than WHERE, right after "and"). */
    @Test
    public void test_formatBasic_parenthesizedOrGroupClosingParenAlignment() {
        String sql = "select a.id from a where a.active = 1 and (a.dept_id in (select id from departments "
                + "where region = 'EU') or a.dept_id is null) and a.hire_date between '2015-01-01' and '2025-01-01'";
        String expected = "select a.id\n" +
                "from a\n" +
                "where a.active = 1\n" +
                "    and (a.dept_id in (\n" +
                "        select id\n" +
                "        from departments\n" +
                "        where region = 'EU'\n" +
                "    )\n" +
                "        or a.dept_id is null\n" +
                "    )\n" +
                "    and a.hire_date between '2015-01-01' and '2025-01-01'";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_distinctAndCountStar() {
        String sql = "select distinct count(*) from employees where dept_id in (1,2,3)";
        String expected = "select distinct count(*)\n" +
                "from employees\n" +
                "where dept_id in (1, 2, 3)";
        assertEquals(expected, formatBasic(sql));
    }

    /** Plain arithmetic grouping has no clause content, so none of these
     *  nested parens should ever break, however deep. */
    @Test
    public void test_formatBasic_deeplyNestedArithmeticStaysCompact() {
        String sql = "select ((a + b) * (c - d)) / nullif((e + (f * g)), 0) as result from t";
        String expected = "select ((a + b) * (c - d)) / nullif((e + (f * g)), 0) as result\n" +
                "from t";
        assertEquals(expected, formatBasic(sql));
    }

    @Test
    public void test_formatBasic_emptyString() {
        assertEquals("", formatBasic(""));
    }

    @Test
    public void test_formatBasic_blankString() {
        assertEquals("", formatBasic("   "));
    }

    @Test
    public void test_formatBasic_nullInput() {
        assertEquals("", formatBasic(null));
    }

    @Test
    public void test_formatBasic_unbalancedOpenParenDoesNotThrow() {
        String result = assertDoesNotThrow(() -> formatBasic("select * from t where (a = 1"));
        assertTrue(result.contains("where (a = 1"));
    }

    @Test
    public void test_formatBasic_unmatchedCloseParenDoesNotThrow() {
        String result = assertDoesNotThrow(() -> formatBasic("select * from t where a = 1)"));
        assertTrue(result.contains("a = 1)"));
    }

    @Test
    public void test_formatBasic_onlyACommentDoesNotThrow() {
        assertEquals("-- just a comment", formatBasic("-- just a comment"));
    }
}
