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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;

/**
 * Base class for local variables and formal parameters.
 *
 * @author D. Campione
 * 
 */
public class LocalVariable extends AbstractASTNode {

    private boolean isFinal;
    private Type type;

    public LocalVariable(Scanner s, boolean isFinal, Type type, int offs,
            String name) {
        super(name, s.createOffset(offs), s.createOffset(offs + name.length()));
        this.isFinal = isFinal;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }
}