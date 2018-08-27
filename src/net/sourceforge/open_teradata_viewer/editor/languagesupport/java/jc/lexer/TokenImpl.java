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
 * Implementation of a token in a Java source file.
 *
 * @author D. Campione
 * 
 */
class TokenImpl implements IJavaLexicalToken {

    private int type;

    /** The token's text. */
    private String lexeme;

    /** The line the token is on. */
    private int line;

    /** The column the token is on. */
    private int column;

    /** The absolute offset into the source of the token. */
    private int offset;

    /** Whether the token is invalid (e.g. an invalid char of String). */
    private boolean invalid;

    public TokenImpl(int type, String lexeme, int line, int column, int offs) {
        this(type, lexeme, line, column, offs, false);
    }

    public TokenImpl(int type, String lexeme, int line, int column, int offs,
            boolean invalid) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.offset = offs;
        this.invalid = invalid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IJavaLexicalToken) {
            IJavaLexicalToken t2 = (IJavaLexicalToken) obj;
            return type == t2.getType() && lexeme.equals(t2.getLexeme())
                    && line == t2.getLine() && column == t2.getColumn()
                    && invalid == t2.isInvalid();
        }
        return false;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getLength() {
        return lexeme.length();
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return lexeme.hashCode();
    }

    @Override
    public boolean isBasicType() {
        switch (getType()) {
        case KEYWORD_BYTE:
        case KEYWORD_SHORT:
        case KEYWORD_CHAR:
        case KEYWORD_INT:
        case KEYWORD_LONG:
        case KEYWORD_FLOAT:
        case KEYWORD_DOUBLE:
        case KEYWORD_BOOLEAN:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isIdentifier() {
        return (getType() & IDENTIFIER) > 0;
    }

    @Override
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public boolean isOperator() {
        return (getType() & OPERATOR) > 0;
    }

    @Override
    public boolean isType(int type) {
        return this.type == type;
    }

    @Override
    public String toString() {
        return "[TokenImpl: " + "type=" + type + "; lexeme=\"" + lexeme + "\""
                + "; line=" + getLine() + "; col=" + getColumn() + "; offs="
                + getOffset() + "; invalid=" + isInvalid() + "]";
    }
}