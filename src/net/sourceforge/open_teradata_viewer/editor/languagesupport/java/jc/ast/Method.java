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

import java.util.Iterator;
import java.util.List;

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
public class Method extends AbstractMember {

    private Modifiers modifiers;
    private Type type;
    private List<FormalParameter> parameters;
    private List<String> thrownTypeNames;
    private CodeBlock body;
    private boolean deprecated;
    private String docComment;

    public Method(Scanner s, Modifiers modifiers, Type type,
            IJavaLexicalToken nameToken, List<FormalParameter> params,
            List<String> thrownTypeNames) {
        super(nameToken.getLexeme(), s.createOffset(nameToken.getOffset()), s
                .createOffset(nameToken.getOffset() + nameToken.getLength()));
        if (modifiers == null) {
            modifiers = new Modifiers();
        }
        this.modifiers = modifiers;
        this.type = type;
        this.parameters = params;
        this.thrownTypeNames = thrownTypeNames;
    }

    public CodeBlock getBody() {
        return body;
    }

    public boolean getBodyContainsOffset(int offs) {
        return offs >= getBodyStartOffset() && offs < getBodyEndOffset();
    }

    public int getBodyEndOffset() {
        return body == null ? Integer.MAX_VALUE : body.getNameEndOffset();
    }

    public int getBodyStartOffset() {
        return getNameStartOffset();
    }

    @Override
    public String getDocComment() {
        return docComment;
    }

    @Override
    public Modifiers getModifiers() {
        return modifiers;
    }

    public String getNameAndParameters() {
        StringBuilder sb = new StringBuilder(getName());
        sb.append('(');
        int count = getParameterCount();
        for (int i = 0; i < count; i++) {
            FormalParameter fp = getParameter(i);
            sb.append(fp.getType().getName(false));
            sb.append(' ');
            sb.append(fp.getName());
            if (i < count - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public FormalParameter getParameter(int index) {
        return parameters.get(index);
    }

    public int getParameterCount() {
        return parameters.size();
    }

    public Iterator<FormalParameter> getParameterIterator() {
        return parameters.iterator();
    }

    public int getThrownTypeNameCount() {
        return thrownTypeNames == null ? 0 : thrownTypeNames.size();
    }

    @Override
    public Type getType() {
        return type;
    }

    public boolean isConstructor() {
        return type == null;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    public void setBody(CodeBlock body) {
        this.body = body;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public void setDocComment(String comment) {
        docComment = comment;
    }
}