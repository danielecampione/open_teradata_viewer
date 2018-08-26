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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.IExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.expression.TeradataHierarchicalExpression;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;

/**
 * The core of a "SELECT" statement (no UNION, no ORDER BY).
 *
 * @author D. Campione
 *
 */
public class PlainSelect implements ISelectBody {

    private Distinct distinct = null;
    private List<ISelectItem> selectItems;
    private List<Table> intoTables;
    private IFromItem fromItem;
    private List<Join> joins;
    private IExpression where;
    private List<IExpression> groupByColumnReferences;
    private List<OrderByElement> orderByElements;
    private IExpression having;
    private Limit limit;
    private Offset offset;
    private Fetch fetch;
    private Top top;
    private TeradataHierarchicalExpression teradataHierarchical = null;
    private boolean teradataSiblings = false;
    private boolean forUpdate = false;

    /**
     * The {@link IFromItem} in this query.
     *
     * @return the {@link IFromItem}.
     */
    public IFromItem getFromItem() {
        return fromItem;
    }

    public List<Table> getIntoTables() {
        return intoTables;
    }

    /**
     * The {@link ISelectItem}s in this query (for example the A,B,C in "SELECT
     * A,B,C").
     *
     * @return a list of {@link ISelectItem}s.
     */
    public List<ISelectItem> getSelectItems() {
        return selectItems;
    }

    public IExpression getWhere() {
        return where;
    }

    public void setFromItem(IFromItem item) {
        fromItem = item;
    }

    public void setIntoTables(List<Table> intoTables) {
        this.intoTables = intoTables;
    }

    public void setSelectItems(List<ISelectItem> list) {
        selectItems = list;
    }

    public void addSelectItems(ISelectItem... items) {
        if (selectItems == null) {
            selectItems = new ArrayList<ISelectItem>();
        }
        Collections.addAll(selectItems, items);
    }

    public void setWhere(IExpression where) {
        this.where = where;
    }

    /** @return the list of {@link Join}s. */
    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> list) {
        joins = list;
    }

    @Override
    public void accept(ISelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public Fetch getFetch() {
        return fetch;
    }

    public void setFetch(Fetch fetch) {
        this.fetch = fetch;
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

    public void setHaving(IExpression expression) {
        having = expression;
    }

    /**
     * A list of {@link IExpression}s of the GROUP BY clause. It is null in case
     * there is no GROUP BY clause.
     *
     * @return a list of {@link IExpression}s.
     */
    public List<IExpression> getGroupByColumnReferences() {
        return groupByColumnReferences;
    }

    public void setGroupByColumnReferences(List<IExpression> list) {
        groupByColumnReferences = list;
    }

    public TeradataHierarchicalExpression getTeradataHierarchical() {
        return teradataHierarchical;
    }

    public void setTeradataHierarchical(
            TeradataHierarchicalExpression teradataHierarchical) {
        this.teradataHierarchical = teradataHierarchical;
    }

    public boolean isTeradataSiblings() {
        return teradataSiblings;
    }

    public void setTeradataSiblings(boolean teradataSiblings) {
        this.teradataSiblings = teradataSiblings;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (distinct != null) {
            sql.append(distinct).append(" ");
        }
        if (top != null) {
            sql.append(top).append(" ");
        }
        sql.append(getStringList(selectItems));

        if (intoTables != null) {
            sql.append(" INTO ");
            for (Iterator<Table> iter = intoTables.iterator(); iter.hasNext();) {
                sql.append(iter.next().toString());
                if (iter.hasNext()) {
                    sql.append(", ");
                }
            }
        }

        if (fromItem != null) {
            sql.append(" FROM ").append(fromItem);
            if (joins != null) {
                Iterator<Join> it = joins.iterator();
                while (it.hasNext()) {
                    Join join = it.next();
                    if (join.isSimple()) {
                        sql.append(", ").append(join);
                    } else {
                        sql.append(" ").append(join);
                    }
                }
            }
            if (where != null) {
                sql.append(" WHERE ").append(where);
            }
            if (teradataHierarchical != null) {
                sql.append(teradataHierarchical.toString());
            }
            sql.append(getFormatedList(groupByColumnReferences, "GROUP BY"));
            if (having != null) {
                sql.append(" HAVING ").append(having);
            }
            sql.append(orderByToString(teradataSiblings, orderByElements));
            if (limit != null) {
                sql.append(limit);
            }
            if (offset != null) {
                sql.append(offset);
            }
            if (fetch != null) {
                sql.append(fetch);
            }
            if (isForUpdate()) {
                sql.append(" FOR UPDATE");
            }
        }
        return sql.toString();
    }

    public static String orderByToString(List<OrderByElement> orderByElements) {
        return orderByToString(false, orderByElements);
    }

    public static String orderByToString(boolean teradataSiblings,
            List<OrderByElement> orderByElements) {
        return getFormatedList(orderByElements,
                teradataSiblings ? "ORDER SIBLINGS BY" : "ORDER BY");
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
     * @param list list of objects with toString methods
     * @return comma separated list of the elements in the list
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