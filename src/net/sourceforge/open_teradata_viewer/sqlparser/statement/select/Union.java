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

import java.util.List;

/**
 * A UNION statement
 * 
 * @author D. Campione
 */
public class Union implements SelectBody {

    @SuppressWarnings("rawtypes")
    private List plainSelects;
    @SuppressWarnings("rawtypes")
    private List orderByElements;
    private Limit limit;
    private boolean distinct;
    private boolean all;

    public void accept(SelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }

    @SuppressWarnings("rawtypes")
    public List getOrderByElements() {
        return orderByElements;
    }

    /**
     * the list of {@link PlainSelect}s in this UNION
     * @return the list of {@link PlainSelect}s
     */
    @SuppressWarnings("rawtypes")
    public List getPlainSelects() {
        return plainSelects;
    }

    @SuppressWarnings("rawtypes")
    public void setOrderByElements(List orderByElements) {
        this.orderByElements = orderByElements;
    }

    @SuppressWarnings("rawtypes")
    public void setPlainSelects(List list) {
        plainSelects = list;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    /**
     * This is not 100% right; every UNION should have their own All/Distinct clause...
     */
    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    /**
     * This is not 100% right; every UNION should have their own All/Distinct clause...
     */
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public String toString() {

        String selects = "";
        String allDistinct = "";
        if (isAll()) {
            allDistinct = "ALL ";
        } else if (isDistinct()) {
            allDistinct = "DISTINCT ";
        }

        for (int i = 0; i < plainSelects.size(); i++) {
            selects += "("
                    + plainSelects.get(i)
                    + ((i < plainSelects.size() - 1)
                            ? ") UNION " + allDistinct
                            : ")");
        }

        return selects
                + ((orderByElements != null) ? PlainSelect
                        .orderByToString(orderByElements) : "")
                + ((limit != null) ? limit + "" : "");
    }

}
