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

import net.sourceforge.open_teradata_viewer.sqlparser.expression.JdbcParameter;

/**
 * A skip clause in the form [SKIP row_count]
 *
 * Initial implementation was done for informix special syntax:
 * http://www-01.ibm.com/support/knowledgecenter/SSGU8G_12.1.0/com.ibm.sqls.doc/ids_sqs_0156.htm
 *
 * @author D. Campione
 *
 */
public class Skip {

    private Long rowCount;
    private JdbcParameter jdbcParameter;
    private String variable;

    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        this.rowCount = rowCount;
    }

    public JdbcParameter getJdbcParameter() {
        return jdbcParameter;
    }

    public void setJdbcParameter(JdbcParameter jdbcParameter) {
        this.jdbcParameter = jdbcParameter;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        String result = "SKIP ";

        if (rowCount != null) {
            result += rowCount;
        } else if (jdbcParameter != null) {
            result += jdbcParameter.toString();
        } else if (variable != null) {
            result += variable;
        }

        return result;
    }
}