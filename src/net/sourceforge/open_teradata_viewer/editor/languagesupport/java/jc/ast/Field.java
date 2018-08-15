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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Field extends AbstractMember {

    private Modifiers modifiers;
    private Type type;
    private boolean deprecated;
    private String docComment;

    public Field(Scanner s, Modifiers modifiers, Type type, IJavaLexicalToken t) {
        super(t.getLexeme(), s.createOffset(t.getOffset()));
        setDeclarationEndOffset(s.createOffset(t.getOffset() + t.getLength()));
        if (modifiers == null) {
            modifiers = new Modifiers();
        }
        this.modifiers = modifiers;
        this.type = type;
    }

    @Override
    public String getDocComment() {
        return docComment;
    }

    @Override
    public Modifiers getModifiers() {
        return modifiers;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public void setDocComment(String comment) {
        docComment = comment;
    }
}