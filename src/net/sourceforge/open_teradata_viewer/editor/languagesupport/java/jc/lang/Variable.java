/*
 * Open Teradata Viewer ( editor language support java jc lang )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;

/**
 * Base class for variable type (local variables, formal parameters..).
 *
 * @author D. Campione
 * 
 */
public abstract class Variable {

    private boolean isFinal;
    private Type type;
    private IJavaLexicalToken name;

    public Variable(boolean isFinal, Type type, IJavaLexicalToken name) {
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name.getLexeme();
    }

    public Type getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }
}