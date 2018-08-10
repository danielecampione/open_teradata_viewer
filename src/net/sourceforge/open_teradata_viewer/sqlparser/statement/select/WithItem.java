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
 * One of the parts of a "WITH" clause of a "SELECT" statement
 * 
 * @author D. Campione
 * 
 */
public class WithItem {

    private String name;
    @SuppressWarnings("rawtypes")
    private List withItemList;
    private SelectBody selectBody;

    /**
     * The name of this WITH item (for example, "myWITH" in "WITH myWITH AS (SELECT A,B,C))"
     * @return the name of this WITH
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The {@link SelectBody} of this WITH item is the part after the "AS" keyword
     * @return {@link SelectBody} of this WITH item
     */
    public SelectBody getSelectBody() {
        return selectBody;
    }
    public void setSelectBody(SelectBody selectBody) {
        this.selectBody = selectBody;
    }

    /**
     * The {@link SelectItem}s in this WITH (for example the A,B,C in "WITH mywith (A,B,C) AS ...")
     * @return a list of {@link SelectItem}s
     */
    @SuppressWarnings("rawtypes")
    public List getWithItemList() {
        return withItemList;
    }
    @SuppressWarnings("rawtypes")
    public void setWithItemList(List withItemList) {
        this.withItemList = withItemList;
    }

    public String toString() {
        return name
                + ((withItemList != null)
                        ? " "
                                + PlainSelect.getStringList(withItemList, true,
                                        true)
                        : "") + " AS (" + selectBody + ")";
    }

}
