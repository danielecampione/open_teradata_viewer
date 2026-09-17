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

import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.util.sql_formatter.FormatStyle;
import net.sourceforge.open_teradata_viewer.util.sql_formatter.IFormatter;

/**
 * Tests for {@link FormatStyle#DDL}, the formatter behind the "Format DDL
 * code" menu action. {@code CREATE TABLE} falls back to the generic,
 * paren-depth-aware layout (its own tests live here too, since it is still
 * part of what {@code formatDdl()} produces); {@code ALTER TABLE} and
 * {@code COMMENT ON} get their own dedicated, keyword-driven layout; every
 * other DDL statement type (DROP TABLE, CREATE INDEX, TRUNCATE, ...) falls
 * back to the same generic layout as CREATE TABLE.
 *
 * @author D. Campione
 *
 */
public class TestFormatStyleDdl {

    private static String formatDdl(String sql) {
        IFormatter formatter = FormatStyle.DDL.getFormatter();
        return formatter.format(sql);
    }

    // ------------------------------------------------------------------
    // CREATE TABLE (generic definition-list layout)
    // ------------------------------------------------------------------

    @Test
    public void test_formatDdl_createTableSimple() {
        String sql = "create table departments (id integer not null, name varchar(100))";
        String expected = "create table departments (\n" +
                "    id integer not null,\n" +
                "    name varchar(100)\n" +
                ")";
        assertEquals(expected, formatDdl(sql));
    }

    /** A column type's own arguments (DECIMAL(10,2)) and an inline FOREIGN
     *  KEY's target column list must stay on their column's own line: only
     *  the outermost, table-level comma list breaks one column per line. */
    @Test
    public void test_formatDdl_createTableWithForeignKeyAndCheck() {
        String sql = "create table employees (id integer not null, name varchar(100), salary decimal(10,2) default 0, "
                + "dept_id integer, constraint fk_dept foreign key (dept_id) references departments(id), "
                + "constraint ck_salary check (salary >= 0))";
        String expected = "create table employees (\n" +
                "    id integer not null,\n" +
                "    name varchar(100),\n" +
                "    salary decimal(10, 2) default 0,\n" +
                "    dept_id integer,\n" +
                "    constraint fk_dept foreign key (dept_id) references departments(id),\n" +
                "    constraint ck_salary check (salary >= 0)\n" +
                ")";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_createTableCompositePrimaryKeyAndUniqueColumn() {
        String sql = "create table accounts (id integer not null, email varchar(255) unique, region_id integer, "
                + "code varchar(10), primary key (id, region_id), "
                + "constraint fk_region foreign key (region_id) references regions(id))";
        String expected = "create table accounts (\n" +
                "    id integer not null,\n" +
                "    email varchar(255) unique,\n" +
                "    region_id integer,\n" +
                "    code varchar(10),\n" +
                "    primary key (id, region_id),\n" +
                "    constraint fk_region foreign key (region_id) references regions(id)\n" +
                ")";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_createTableMultipleForeignKeys() {
        String sql = "create table order_items (id integer not null, order_id integer, product_id integer, "
                + "qty integer default 1, constraint fk_order foreign key (order_id) references orders(id), "
                + "constraint fk_product foreign key (product_id) references products(id))";
        String expected = "create table order_items (\n" +
                "    id integer not null,\n" +
                "    order_id integer,\n" +
                "    product_id integer,\n" +
                "    qty integer default 1,\n" +
                "    constraint fk_order foreign key (order_id) references orders(id),\n" +
                "    constraint fk_product foreign key (product_id) references products(id)\n" +
                ")";
        assertEquals(expected, formatDdl(sql));
    }

    // ------------------------------------------------------------------
    // ALTER TABLE (dedicated layout)
    // ------------------------------------------------------------------

    /** Regression: everything that qualifies a single action (CONSTRAINT
     *  name, CHECK (...), ...) must stay on ADD's own line: only ADD itself
     *  starts a new line, not CONSTRAINT/CHECK/REFERENCES individually. */
    @Test
    public void test_formatDdl_alterTableAddConstraintCheckStaysOnOneLine() {
        String sql = "alter table employees add constraint ck_salary check (salary >= 0)";
        String expected = "alter table employees\n" +
                "    add constraint ck_salary check (salary >= 0)";
        assertEquals(expected, formatDdl(sql));
    }

    /** Regression: a comma inside a column's own type arguments must not get
     *  a leading space ("10, 2", not "10 , 2") the way a top-level,
     *  action-separating comma would. */
    @Test
    public void test_formatDdl_alterTableAddColumnWithTypeArguments() {
        String sql = "alter table employees add column bonus decimal(10,2) default 0";
        String expected = "alter table employees\n" +
                "    add column bonus decimal(10, 2) default 0";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_alterTableDropColumn() {
        String sql = "alter table employees drop column bonus";
        String expected = "alter table employees\n" +
                "    drop column bonus";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_alterTableDropConstraint() {
        String sql = "alter table employees drop constraint ck_salary";
        String expected = "alter table employees\n" +
                "    drop constraint ck_salary";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_alterTableModifyColumn() {
        String sql = "alter table employees modify column salary decimal(12,2)";
        String expected = "alter table employees\n" +
                "    modify column salary decimal(12, 2)";
        assertEquals(expected, formatDdl(sql));
    }

    /** Regression: the statement's own leading ALTER (part of "ALTER TABLE")
     *  must never itself be treated as a sub-action; only a later one is
     *  (SQL Server-style "ALTER TABLE x ALTER COLUMN y ..."). */
    @Test
    public void test_formatDdl_alterTableAlterColumnSecondAlterIsTheBreakPoint() {
        String sql = "alter table employees alter column salary set default 0";
        String expected = "alter table employees\n" +
                "    alter column salary set default 0";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_alterTableRenameTo() {
        String sql = "alter table employees rename to staff";
        String expected = "alter table employees\n" +
                "    rename to staff";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_alterTableAddNotNullDefault() {
        String sql = "alter table employees add column status varchar(20) not null default 'ACTIVE'";
        String expected = "alter table employees\n" +
                "    add column status varchar(20) not null default 'ACTIVE'";
        assertEquals(expected, formatDdl(sql));
    }

    /** A single ALTER TABLE with several comma-separated actions: each one
     *  starts its own line, same indent level, no blank line in between. */
    @Test
    public void test_formatDdl_alterTableMultipleCommaSeparatedActions() {
        String sql = "alter table employees add column a int, add column b int, drop column c";
        String expected = "alter table employees\n" +
                "    add column a int,\n" +
                "    add column b int,\n" +
                "    drop column c";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_alterTableAddForeignKeyReferencesOnDelete() {
        String sql = "alter table orders add constraint fk_customer foreign key (customer_id) "
                + "references customers (id) on delete cascade";
        String expected = "alter table orders\n" +
                "    add constraint fk_customer foreign key (customer_id) references customers (id) on delete cascade";
        assertEquals(expected, formatDdl(sql));
    }

    /** ALTER TABLE's own layout does not do clause/operator recognition
     *  inside parentheses: AND/OR in a CHECK expression stay exactly as
     *  written, on the same line, whatever their number. */
    @Test
    public void test_formatDdl_alterTableCheckWithAndOrStaysInline() {
        String sql = "alter table employees add constraint ck_range check (salary > 0 and (bonus is null or bonus >= 0))";
        String expected = "alter table employees\n" +
                "    add constraint ck_range check (salary > 0 and (bonus is null or bonus >= 0))";
        assertEquals(expected, formatDdl(sql));
    }

    /** Keyword detection is case-insensitive, but the original casing of
     *  every token is preserved in the output (the formatter never
     *  normalizes case itself). */
    @Test
    public void test_formatDdl_alterTableMixedCaseKeywordsPreserveOriginalCasing() {
        String sql = "Alter Table employees Add Column x int";
        String expected = "Alter Table employees\n" +
                "    Add Column x int";
        assertEquals(expected, formatDdl(sql));
    }

    // ------------------------------------------------------------------
    // COMMENT ON (dedicated layout)
    // ------------------------------------------------------------------

    @Test
    public void test_formatDdl_commentOnTable() {
        String sql = "comment on table employees is 'staff registry'";
        String expected = "comment on table employees\n" +
                "    is 'staff registry'";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_commentOnColumn() {
        String sql = "comment on column employees.salary is 'monthly salary in EUR'";
        String expected = "comment on column employees.salary\n" +
                "    is 'monthly salary in EUR'";
        assertEquals(expected, formatDdl(sql));
    }

    @Test
    public void test_formatDdl_commentOnTableLongText() {
        String sql = "comment on table employees is 'Registry of all active and former staff members, "
                + "including contractors'";
        String expected = "comment on table employees\n" +
                "    is 'Registry of all active and former staff members, including contractors'";
        assertEquals(expected, formatDdl(sql));
    }

    /** Dispatch to COMMENT ON's dedicated layout must tolerate leading
     *  whitespace/blank lines before the statement itself. */
    @Test
    public void test_formatDdl_commentOnWithLeadingWhitespace() {
        String sql = "   \n  comment on table employees is 'x'";
        String expected = "comment on table employees\n" +
                "    is 'x'";
        assertEquals(expected, formatDdl(sql));
    }

    // ------------------------------------------------------------------
    // Statement types with no dedicated layout: generic fallback
    // ------------------------------------------------------------------

    @Test
    public void test_formatDdl_dropTableFallsBackToPlainOutput() {
        String sql = "drop table employees";
        assertEquals("drop table employees", formatDdl(sql));
    }

    @Test
    public void test_formatDdl_truncateTableFallsBackToPlainOutput() {
        String sql = "truncate table employees";
        assertEquals("truncate table employees", formatDdl(sql));
    }

    @Test
    public void test_formatDdl_createIndexUsesGenericParenLayout() {
        String sql = "create index idx_emp_dept on employees (dept_id)";
        String expected = "create index idx_emp_dept on employees (\n" +
                "    dept_id\n" +
                ")";
        assertEquals(expected, formatDdl(sql));
    }

    // ------------------------------------------------------------------
    // Defensive / edge cases
    // ------------------------------------------------------------------

    @Test
    public void test_formatDdl_emptyString() {
        assertEquals("", formatDdl(""));
    }

    @Test
    public void test_formatDdl_blankString() {
        assertEquals("", formatDdl("   \n\t  "));
    }

    @Test
    public void test_formatDdl_nullInput() {
        assertEquals("", formatDdl(null));
    }

    @Test
    public void test_formatDdl_singleWordStatementDoesNotThrow() {
        assertDoesNotThrow(() -> formatDdl("VACUUM"));
    }
}
