/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2013, D. Campione
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

import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Select implements IStatement {

    private ISelectBody iSelectBody;
    private List<?> withItemsList;

    public void accept(IStatementVisitor iStatementVisitor) {
        iStatementVisitor.visit(this);
    }

    public ISelectBody getSelectBody() {
        return iSelectBody;
    }

    public void setSelectBody(ISelectBody body) {
        iSelectBody = body;
    }

    public String toString() {
        StringBuffer retval = new StringBuffer();
        if (withItemsList != null && !withItemsList.isEmpty()) {
            retval.append("WITH ");
            for (Iterator<?> iter = withItemsList.iterator(); iter.hasNext();) {
                WithItem withItem = (WithItem) iter.next();
                retval.append(withItem);
                if (iter.hasNext())
                    retval.append(",");
                retval.append(" ");
            }
        }
        retval.append(iSelectBody);
        return retval.toString();
    }

    public List<?> getWithItemsList() {
        return withItemsList;
    }

    public void setWithItemsList(List<?> withItemsList) {
        this.withItemsList = withItemsList;
    }
}