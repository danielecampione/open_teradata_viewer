/*
 * Open Teradata Viewer ( editor language support java jc lexer )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer;

/**
 * A lexical token in a Java file.
 *
 * @author D. Campione
 * 
 */
public interface IJavaLexicalToken extends ITokenTypes {

    public int getColumn();

    public String getLexeme();

    public int getLength();

    public int getLine();

    public int getOffset();

    public int getType();

    public boolean isBasicType();

    public boolean isIdentifier();

    public boolean isInvalid();

    public boolean isOperator();

    public boolean isType(int type);
}