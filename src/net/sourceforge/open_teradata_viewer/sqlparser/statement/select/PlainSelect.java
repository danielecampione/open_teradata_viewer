/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;

/**
 * The core of a "SELECT" statement (no UNION, no ORDER BY).
 * 
 * @author D. Campione
 */
public class PlainSelect implements ISelectBody {

    private Distinct distinct = null;
    private List<?> selectItems;
    private Table into;
    private IFromItem iFromItem;
    private List<?> joins;
    private IExpression where;
    private List<?> groupByColumnReferences;
    private List<?> orderByElements;
    private IExpression having;
    private Limit limit;
    private Top top;

    /**
     * The {@link IFromItem} in this query.
     * 
     * @return the {@link IFromItem}.
     */
    public IFromItem getFromItem() {
        return iFromItem;
    }

    public Table getInto() {
        return into;
    }

    /**
     * The {@link ISelectItem}s in this query (for example the A,B,C in "SELECT
     * A,B,C").
     * 
     * @return a list of {@link ISelectItem}s.
     */
    public List<?> getSelectItems() {
        return selectItems;
    }

    public IExpression getWhere() {
        return where;
    }

    public void setFromItem(IFromItem item) {
        iFromItem = item;
    }

    public void setInto(Table table) {
        into = table;
    }

    public void setSelectItems(List<?> list) {
        selectItems = list;
    }

    public void setWhere(IExpression where) {
        this.where = where;
    }

    /** @return the list of {@link Join}s. */
    public List<?> getJoins() {
        return joins;
    }

    public void setJoins(List<?> list) {
        joins = list;
    }

    public void accept(ISelectVisitor iSelectVisitor) {
        iSelectVisitor.visit(this);
    }

    public List<?> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<?> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Top getTop() {
        return top;
    }

    public void setTop(Top top) {
        this.top = top;
    }

    public Distinct getDistinct() {
        return distinct;
    }

    public void setDistinct(Distinct distinct) {
        this.distinct = distinct;
    }

    public IExpression getHaving() {
        return having;
    }

    public void setHaving(IExpression iExpression) {
        having = iExpression;
    }

    /**
     * A list of {@link IExpression}s of the GROUP BY clause. It is null in case
     * there is no GROUP BY clause.
     * 
     * @return a list of {@link IExpression}s. 
     */
    public List<?> getGroupByColumnReferences() {
        return groupByColumnReferences;
    }

    public void setGroupByColumnReferences(List<?> list) {
        groupByColumnReferences = list;
    }

    public String toString() {
        String sql = "";

        sql = "SELECT ";
        sql += ((distinct != null) ? "" + distinct + " " : "");
        sql += ((top != null) ? "" + top + " " : "");
        sql += getStringList(selectItems);
        sql += " FROM " + iFromItem;
        if (joins != null) {
            Iterator<?> it = joins.iterator();
            while (it.hasNext()) {
                Join join = (Join) it.next();
                if (join.isSimple()) {
                    sql += ", " + join;
                } else {
                    sql += " " + join;
                }
            }
        }
        sql += ((where != null) ? " WHERE " + where : "");
        sql += getFormatedList(groupByColumnReferences, "GROUP BY");
        sql += ((having != null) ? " HAVING " + having : "");
        sql += orderByToString(orderByElements);
        sql += ((limit != null) ? limit + "" : "");

        return sql;
    }

    public static String orderByToString(List<?> orderByElements) {
        return getFormatedList(orderByElements, "ORDER BY");
    }

    public static String getFormatedList(List<?> list, String expression) {
        return getFormatedList(list, expression, true, false);
    }

    public static String getFormatedList(List<?> list, String expression,
            boolean useComma, boolean useBrackets) {
        String sql = getStringList(list, useComma, useBrackets);

        if (sql.length() > 0) {
            if (expression.length() > 0) {
                sql = " " + expression + " " + sql;
            } else {
                sql = " " + sql;
            }
        }

        return sql;
    }

    /**
     * List the toString out put of the objects in the List comma separated. If
     * the List is null or empty an empty string is returned.
     * 
     * The same as getStringList(list, true, false).
     * 
     * @see #getStringList(List, boolean, boolean)
     * @param list List of objects with toString methods.
     * @return comma separated list of the elements in the list.
     */
    public static String getStringList(List<?> list) {
        return getStringList(list, true, false);
    }

    /**
     * List the toString out put of the objects in the List that can be comma
     * separated. If the List is null or empty an empty string is returned.
     * 
     * @param list list of objects with toString methods.
     * @param useComma true if the list has to be comma separated.
     * @param useBrackets true if the list has to be enclosed in brackets.
     * @return comma separated list of the elements in the list.
     */
    public static String getStringList(List<?> list, boolean useComma,
            boolean useBrackets) {
        String ans = "";
        String comma = ",";
        if (!useComma) {
            comma = "";
        }
        if (list != null) {
            if (useBrackets) {
                ans += "(";
            }

            for (int i = 0; i < list.size(); i++) {
                ans += "" + list.get(i)
                        + ((i < list.size() - 1) ? comma + " " : "");
            }

            if (useBrackets) {
                ans += ")";
            }
        }

        return ans;
    }
}