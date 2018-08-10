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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.replace;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.expression.operators.relational.IItemsList;
import net.sourceforge.open_teradata_viewer.sqlparser.schema.Table;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatementVisitor;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * The replace statement.
 * 
 * @author D. Campione
 * 
 */
public class Replace implements IStatement {

    private Table table;
    private List<?> columns;
    private IItemsList iItemsList;
    private List<?> expressions;
    private boolean useValues = true;

    public void accept(IStatementVisitor iStatementVisitor) {
        iStatementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table name) {
        table = name;
    }

    /**
     * A list of {@link net.sf.jsqlparser.schema.Column}s either from a "REPLACE
     * mytab (col1, col2) [...]" or a "REPLACE mytab SET col1=exp1, col2=exp2". 
     * 
     * @return a list of {@link net.sf.jsqlparser.schema.Column}s
     */
    public List<?> getColumns() {
        return columns;
    }

    /**
     * An {@link IItemsList} (either from a "REPLACE mytab VALUES (exp1,exp2)"
     * or a "REPLACE mytab SELECT * FROM mytab2") it is null in case of a
     * "REPLACE mytab SET col1=exp1, col2=exp2".  
     */
    public IItemsList getItemsList() {
        return iItemsList;
    }

    public void setColumns(List<?> list) {
        columns = list;
    }

    public void setItemsList(IItemsList list) {
        iItemsList = list;
    }

    /**
     * A list of {@link net.sf.jsqlparser.expression.Expression}s (from a
     * "REPLACE mytab SET col1=exp1, col2=exp2"). <br> it is null in case of a
     * "REPLACE mytab (col1, col2) [...]".  
     */
    public List<?> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<?> list) {
        expressions = list;
    }

    public boolean isUseValues() {
        return useValues;
    }

    public void setUseValues(boolean useValues) {
        this.useValues = useValues;
    }

    public String toString() {
        String sql = "REPLACE " + table;

        if (expressions != null && columns != null) {
            // The SET col1=exp1, col2=exp2 case
            sql += " SET ";
            // Each element from expressions match up with a column from columns
            for (int i = 0, s = columns.size(); i < s; i++) {
                sql += "" + columns.get(i) + "=" + expressions.get(i);
                sql += (i < s - 1) ? ", " : "";
            }
        } else if (columns != null) {
            // The REPLACE mytab (col1, col2) [...] case 
            sql += " " + PlainSelect.getStringList(columns, true, true);
        }

        if (iItemsList != null) {
            //REPLACE mytab SELECT * FROM mytab2
            //or VALUES ('as', ?, 565)

            if (useValues) {
                sql += " VALUES";
            }

            sql += " " + iItemsList;
        }

        return sql;
    }
}