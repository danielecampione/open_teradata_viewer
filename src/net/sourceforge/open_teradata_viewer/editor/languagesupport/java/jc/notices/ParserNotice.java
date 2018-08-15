/*
 * Open Teradata Viewer ( editor language support java jc notices )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.notices;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;

/**
 * A notice (e.g., a warning or error) from a parser.
 *
 * @author D. Campione
 * 
 */
public class ParserNotice {

    private int line;
    private int column;
    private int length;
    private String message;

    public ParserNotice(IJavaLexicalToken t, String msg) {
        line = t.getLine();
        column = t.getColumn();
        length = t.getLexeme().length();
        message = msg;
    }

    /**
     * Ctor.
     *
     * @param line The line of the notice.
     * @param column The column of the notice.
     * @param length The length of the code the message is concerned with.
     * @param message The message.
     */
    public ParserNotice(int line, int column, int length, String message) {
        this.line = line;
        this.column = column;
        this.length = length;
        this.message = message;
    }

    /**
     * Returns the character offset into the line of the parser notice, if any.
     *
     * @return The column.
     */
    public int getColumn() {
        return column;
    }

    /** @return The length of the code the message is concerned with. */
    public int getLength() {
        return length;
    }

    /**
     * Returns the line number the notice is about, if any.
     *
     * @return The line number.
     */
    public int getLine() {
        return line;
    }

    /** @return The message from the parser. */
    public String getMessage() {
        return message;
    }

    /**
     * Returns a string representation of this parser notice.
     *
     * @return This parser notice as a string.
     */
    @Override
    public String toString() {
        return "(" + getLine() + ", " + getColumn() + ": " + getMessage();
    }
}