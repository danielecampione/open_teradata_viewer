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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;

/**
 * A TypeParameter.
 *
 * <pre>
 * TypeParameter:
 *    Identifier ['extends' Bound]
 * 
 * Bound:
 *    Type { '&' Type }
 * </pre>
 *
 * @author D. Campione
 * 
 */
public class TypeParameter {

    private IJavaLexicalToken name;
    private List<Type> bounds;

    public TypeParameter(IJavaLexicalToken name) {
        this.name = name;
    }

    public void addBound(Type bound) {
        if (bounds == null) {
            bounds = new ArrayList<Type>(1); // Usually just 1
        }
        bounds.add(bound);
    }

    public String getName() {
        return name.getLexeme();
    }
}