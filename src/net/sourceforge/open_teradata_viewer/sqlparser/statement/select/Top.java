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

/**
 * A top clause in the form [TOP (row_count) or TOP row_count].
 * 
 * @author D. Campione
 * 
 */
public class Top {

    private long rowCount;
    private boolean rowCountJdbcParameter = false;
    private boolean hasParenthesis = false;
    private boolean isPercentage = false;

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isRowCountJdbcParameter() {
        return rowCountJdbcParameter;
    }

    public void setRowCountJdbcParameter(boolean rowCountJdbcParameter) {
        this.rowCountJdbcParameter = rowCountJdbcParameter;
    }

    public boolean hasParenthesis() {
        return hasParenthesis;
    }

    public void setParenthesis(boolean hasParenthesis) {
        this.hasParenthesis = hasParenthesis;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        this.isPercentage = percentage;
    }

    @Override
    public String toString() {
        String result = "TOP ";

        if (hasParenthesis) {
            result += "(";
        }

        result += rowCountJdbcParameter ? "?" : rowCount;

        if (hasParenthesis) {
            result += ")";
        }

        if (isPercentage) {
            result += " PERCENT";
        }

        return result;
    }
}