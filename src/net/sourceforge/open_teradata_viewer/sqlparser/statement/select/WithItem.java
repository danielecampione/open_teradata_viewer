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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.select;

import java.util.List;

/**
 * One of the parts of a "WITH" clause of a "SELECT" statement.
 *
 * @author D. Campione
 *
 */
public class WithItem implements ISelectBody {

    private String name;
    private List<ISelectItem> withItemList;
    private ISelectBody selectBody;
    private boolean recursive;

    /**
     * The name of this WITH item (for example, "myWITH" in "WITH myWITH AS
     * (SELECT A,B,C))".
     *
     * @return The name of this WITH.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    /**
     * The {@link ISelectBody} of this WITH item is the part after the "AS"
     * keyword.
     *
     * @return {@link ISelectBody} of this WITH item.
     */
    public ISelectBody getSelectBody() {
        return selectBody;
    }

    public void setSelectBody(ISelectBody selectBody) {
        this.selectBody = selectBody;
    }

    /**
     * The {@link ISelectItem}s in this WITH (for example the A,B,C in "WITH
     * mywith (A,B,C) AS ...").
     *
     * @return A list of {@link ISelectItem}s.
     */
    public List<ISelectItem> getWithItemList() {
        return withItemList;
    }

    public void setWithItemList(List<ISelectItem> withItemList) {
        this.withItemList = withItemList;
    }

    @Override
    public String toString() {
        return (recursive ? "RECURSIVE " : "")
                + name
                + ((withItemList != null) ? " "
                        + PlainSelect.getStringList(withItemList, true, true)
                        : "") + " AS (" + selectBody + ")";
    }

    @Override
    public void accept(ISelectVisitor visitor) {
        visitor.visit(this);
    }
}