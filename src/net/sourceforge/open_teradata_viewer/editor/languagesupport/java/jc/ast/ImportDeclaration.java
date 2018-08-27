/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;

/**
 * An import declaration in a class file.
 *
 * @author D. Campione
 * 
 */
public class ImportDeclaration extends AbstractASTNode {

    private boolean isStatic;

    public ImportDeclaration(Scanner s, int offs, String info, boolean isStatic) {
        super(info, s.createOffset(offs), s.createOffset(offs + info.length()));
        setStatic(isStatic);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isWildcard() {
        return getName().endsWith(".*");
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}