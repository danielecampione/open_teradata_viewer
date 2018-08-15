/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Annotation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;

/**
 * A parameter to a method.
 *
 * @author D. Campione
 * 
 */
/*
 * FormalParameter:
 *    ['final'] [Annotations] Type VariableDeclaratorId
 *   
 * VariableDeclaratorId:
 *    Identifier { "[" "]" }
 */
public class FormalParameter extends LocalVariable {

    private List<Annotation> annotations;

    public FormalParameter(Scanner s, boolean isFinal, Type type, int offs,
            String name, List<Annotation> annotations) {
        super(s, isFinal, type, offs, name);
        this.annotations = annotations;
    }

    public int getAnnotationCount() {
        return annotations == null ? 0 : annotations.size();
    }

    /**
     * Overridden to return "<code>getType() getName()</code>".
     *
     * @return This parameter, as a string.
     */
    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}