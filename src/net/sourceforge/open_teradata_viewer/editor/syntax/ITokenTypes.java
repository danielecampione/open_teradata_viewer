/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

/**
 * All token types supported by SyntaxTextArea.<p>
 *
 * Note that all valid token types are &gt;= 0, so extensions of the ITokenMaker
 * class are free to internally use all ints &lt; 0 ONLY for "end-of-line" style
 * markers; they are ignored by painting implementations.
 *
 * @author D. Campione
 * 
 */
public interface ITokenTypes {

    /**
     * Tokens of type <code>NULL</code> mark the end of lines with no multi-line
     * token at the end (such as a block comment in C++).
     */
    public static final int NULL = 0;

    public static final int COMMENT_EOL = 1;
    public static final int COMMENT_MULTILINE = 2;
    public static final int COMMENT_DOCUMENTATION = 3;
    public static final int COMMENT_KEYWORD = 4;
    public static final int COMMENT_MARKUP = 5;

    public static final int RESERVED_WORD = 6;
    public static final int RESERVED_WORD_2 = 7;

    public static final int FUNCTION = 8;

    public static final int LITERAL_BOOLEAN = 9;
    public static final int LITERAL_NUMBER_DECIMAL_INT = 10;
    public static final int LITERAL_NUMBER_FLOAT = 11;
    public static final int LITERAL_NUMBER_HEXADECIMAL = 12;
    public static final int LITERAL_STRING_DOUBLE_QUOTE = 13;
    public static final int LITERAL_CHAR = 14;
    public static final int LITERAL_BACKQUOTE = 15;

    public static final int DATA_TYPE = 16;

    public static final int VARIABLE = 17;

    public static final int REGEX = 18;

    public static final int ANNOTATION = 19;

    public static final int IDENTIFIER = 20;

    public static final int WHITESPACE = 21;

    public static final int SEPARATOR = 22;

    public static final int OPERATOR = 23;

    public static final int PREPROCESSOR = 24;

    public static final int MARKUP_TAG_DELIMITER = 25;
    public static final int MARKUP_TAG_NAME = 26;
    public static final int MARKUP_TAG_ATTRIBUTE = 27;
    public static final int MARKUP_TAG_ATTRIBUTE_VALUE = 28;
    public static final int MARKUP_PROCESSING_INSTRUCTION = 29;
    public static final int MARKUP_CDATA = 30;

    public static final int ERROR_IDENTIFIER = 31;
    public static final int ERROR_NUMBER_FORMAT = 32;
    public static final int ERROR_STRING_DOUBLE = 33;
    public static final int ERROR_CHAR = 34;

    public static final int NUM_TOKEN_TYPES = 35;
}