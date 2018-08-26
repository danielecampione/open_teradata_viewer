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

package net.sourceforge.open_teradata_viewer.sqlparser.expression;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WindowOffset {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Type {
        PRECEDING, FOLLOWING, CURRENT, EXPR
    }

    private IExpression expression;
    private Type type;

    public IExpression getExpression() {
        return expression;
    }

    public void setExpression(IExpression expression) {
        this.expression = expression;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (expression != null) {
            buffer.append(' ').append(expression);
            if (type != null) {
                buffer.append(' ');
                buffer.append(type);
            }
        } else {
            switch (type) {
            case PRECEDING:
                buffer.append(" UNBOUNDED PRECEDING");
                break;
            case FOLLOWING:
                buffer.append(" UNBOUNDED FOLLOWING");
                break;
            case CURRENT:
                buffer.append(" CURRENT ROW");
                break;
            default:
                break;
            }
        }
        return buffer.toString();
    }
}