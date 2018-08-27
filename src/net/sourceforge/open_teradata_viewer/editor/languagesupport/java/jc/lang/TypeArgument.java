/*
 * Open Teradata Viewer ( editor language support java jc lang )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TypeArgument {

    public static final int NOTHING = 0;
    public static final int EXTENDS = 1;
    public static final int SUPER = 2;

    private Type type;
    private int doesExtend;
    private Type otherType;

    public TypeArgument(Type type) {
        this.type = type;
    }

    public TypeArgument(Type type, int doesExtend, Type otherType) {
        if (doesExtend < 0 || doesExtend > 2) {
            throw new IllegalArgumentException("Illegal doesExtend: "
                    + doesExtend);
        }
        this.type = type;
        this.doesExtend = doesExtend;
        this.otherType = otherType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == null) {
            sb.append('?');
        } else {
            sb.append(type.toString());
        }
        if (doesExtend == EXTENDS) {
            sb.append(" extends ");
            sb.append(otherType.toString());
        } else if (doesExtend == SUPER) {
            sb.append(" super ");
            sb.append(otherType.toString());
        }
        return sb.toString();
    }
}