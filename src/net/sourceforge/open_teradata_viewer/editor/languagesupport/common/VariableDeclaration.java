/*
 * Open Teradata Viewer ( editor language support common )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.common;

/**
 * A marker for a variable declaration. This can be used by {@link
 * LanguageSupport}s to mark variables, and is especially helpful when used in
 * conjunction with {@link CodeBlock}.
 *
 * @author D. Campione
 * @see CodeBlock
 * 
 */
public class VariableDeclaration {

    private String type;
    private String name;
    private int offset;

    public VariableDeclaration(String name, int offset) {
        this(null, name, offset);
    }

    public VariableDeclaration(String type, String name, int offset) {
        this.type = type;
        this.name = name;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Returns the type of this variable.
     *
     * @return The variable's type, or <code>null</code> if none.
     */
    public String getType() {
        return type;
    }
}