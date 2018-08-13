/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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

import java.awt.Rectangle;

import javax.swing.text.TabExpander;

/**
 * A generic token that functions as a node in a linked list of syntax
 * highlighted tokens for some language.<p>
 *
 * A <code>IToken</code> is a piece of text representing some logical token in
 * source code for a programming language. For example, the line of C code:<p>
 * <pre>
 * int i = 0;
 * </pre>
 * would be broken into 8 <code>IToken</code>s: the first representing
 * <code>int</code>, the second whitespace, the third <code>i</code>, the fourth
 * whitespace, the fifth <code>=</code>, etc..<p>
 *
 * <b>Note:</b> The tokens returned by {@link SyntaxDocument}s are pooled and
 * should always be treated as immutable. Modifying tokens you did not create
 * yourself can and will result in rendering issues and/or runtime exceptions.
 * You have been warned.
 * 
 * @author D. Campione
 * 
 */
public interface IToken extends ITokenTypes {

    /**
     * Appends HTML code for painting this token, using the given text area's
     * color scheme.
     *
     * @param sb The buffer to append to.
     * @param textArea The text area whose color scheme to use.
     * @param fontFamily Whether to include the font family in the HTML for
     *        this token. You can pass <code>false</code> for this parameter
     *        if, for example, you are making all your HTML be monospaced,
     *        and don't want any crazy fonts being used in the editor to be
     *        reflected in your HTML.
     * @return The buffer appended to.
     * @see #getHTMLRepresentation(SyntaxTextArea)
     */
    public StringBuilder appendHTMLRepresentation(StringBuilder sb,
            SyntaxTextArea textArea, boolean fontFamily);

    /**
     * Appends HTML code for painting this token, using the given text area's
     * color scheme.
     *
     * @param sb The buffer to append to.
     * @param textArea The text area whose color scheme to use.
     * @param fontFamily Whether to include the font family in the HTML for
     *        this token. You can pass <code>false</code> for this parameter
     *        if, for example, you are making all your HTML be monospaced,
     *        and don't want any crazy fonts being used in the editor to be
     *        reflected in your HTML.
     * @param tabsToSpaces Whether to convert tabs into spaces.
     * @return The buffer appended to.
     * @see #getHTMLRepresentation(SyntaxTextArea)
     */
    public StringBuilder appendHTMLRepresentation(StringBuilder sb,
            SyntaxTextArea textArea, boolean fontFamily, boolean tabsToSpaces);

    /**
     * Returns the character at the specified offset in the token.
     *
     * @param index The index. This should be in the range
     *        <code>0-({@link #length()}-1)</code>.
     * @return The character.
     * @see #length()
     */
    public char charAt(int index);

    /**
     * Returns whether the token straddles the specified position in the
     * document.
     *
     * @param pos The position in the document to check.
     * @return Whether the specified position is straddled by this token.
     */
    public boolean containsPosition(int pos);

    /**
     * Returns the position in the token's internal char array corresponding
     * to the specified document position.<p>
     * Note that this method does NOT do any bounds checking; you can pass in
     * a document position that does not correspond to a position in this
     * token, and you will not receive an Exception or any other notification;
     * it is up to the caller to ensure valid input.
     *
     * @param pos A position in the document that is represented by this token.
     * @return The corresponding token position >= <code>textOffset</code> and
     *         < <code>textOffset+textCount</code>.
     * @see #tokenToDocument
     */
    public int documentToToken(int pos);

    /**
     * Returns whether this token's lexeme ends with the specified characters.
     *
     * @param ch The characters.
     * @return Whether this token's lexeme ends with the specified characters.
     * @see #startsWith(char[])
     */
    public boolean endsWith(char[] ch);

    /**
     * Returns the end offset of this token in the document (exclusive). In
     * other words, the token ranges from
     * <code>[getOffset(), getEndOffset())</code>.
     * 
     * @return The end offset of this token.
     * @see #getOffset()
     */
    public int getEndOffset();

    /**
     * Returns a <code>String</code> containing HTML code for painting this
     * token, using the given text area's color scheme.
     *
     * @param textArea The text area whose color scheme to use.
     * @return The HTML representation of the token.
     * @see #appendHTMLRepresentation(StringBuilder, SyntaxTextArea, boolean)
     */
    public String getHTMLRepresentation(SyntaxTextArea textArea);

    /**
     * Returns the language index of this token.
     *
     * @return The language index. A value of <code>0</code> denotes the
     *        "main" language, any positive value denotes a specific secondary
     *        language.
     * @see #setLanguageIndex(int)
     */
    public int getLanguageIndex();

    /**
     * Returns the last token in this list that is not whitespace or a
     * comment.
     *
     * @return The last non-comment, non-whitespace token, or <code>null</code>
     *         if there isn't one.
     */
    public IToken getLastNonCommentNonWhitespaceToken();

    /**
     * Returns the last paintable token in this token list, or <code>null</code>
     * if there is no paintable token.
     *
     * @return The last paintable token in this token list.
     */
    public IToken getLastPaintableToken();

    /**
     * Returns the text of this token, as a string.<p>
     *
     * Note that this method isn't used much by the
     * <code>syntaxtextarea</code> package internally, as it tries to limit
     * memory allocation.
     *
     * @return The text of this token.
     */
    public String getLexeme();

    /**
     * Determines the offset into this token list (i.e., into the
     * document) that covers pixel location <code>x</code> if the token list
     * starts at pixel location <code>x0</code><p>.
     * This method will return the document position "closest" to the
     * x-coordinate (i.e., if they click on the "right-half" of the
     * <code>w</code> in <code>awe</code>, the caret will be placed in
     * between the <code>w</code> and <code>e</code>; similarly, clicking on
     * the left-half places the caret between the <code>a</code> and
     * <code>w</code>). This makes it useful for methods such as
     * <code>viewToModel</code> found in <code>javax.swing.text.View</code>
     * subclasses.
     *
     * @param textArea The text area from which the token list was derived.
     * @param e How to expand tabs.
     * @param x0 The pixel x-location that is the beginning of
     *        <code>tokenList</code>.
     * @param x The pixel-position for which you want to get the corresponding
     *        offset.
     * @return The position (in the document, NOT into the token list) that
     *         covers the pixel location. If <code>tokenList</code> is
     *         <code>null</code> or has type <code>IToken.NULL</code>, then
     *         <code>-1</code is returned; the caller should recognize this and
     *         return the actual end position of the (empty) line.
     */
    public int getListOffset(SyntaxTextArea textArea, TabExpander e, float x0,
            float x);

    /**
     * Returns the token after this one in the linked list.
     *
     * @return The next token.
     */
    public IToken getNextToken();

    /**
     * Returns the offset into the document at which this token resides.
     *
     * @return The offset into the document.
     * @see #getEndOffset()
     */
    public int getOffset();

    /**
     * Returns the position in the document that represents the last character
     * in the token that will fit into <code>endBeforeX-startX</code> pixels.
     * For example, if you're using a monospaced 8-pixel-per-character font,
     * have the token "while" and <code>startX</code> is <code>0</code> and
     * <code>endBeforeX</code> is <code>30</code>, this method will return the
     * document position of the "i" in "while", because the "i" ends at pixel
     * <code>24</code>, while the "l" ends at <code>32</code>. If not even the
     * first character fits in <code>endBeforeX-startX</code>, the first
     * character's position is still returned so calling methods don't go into
     * infinite loops.
     *
     * @param textArea The text area in which this token is being painted.
     * @param e How to expand tabs.
     * @param startX The x-coordinate at which the token will be painted. This
     *        is needed because of tabs.
     * @param endBeforeX The x-coordinate for which you want to find the last
     *        character of <code>t</code> which comes before it.
     * @return The last document position that will fit in the specified amount
     *         of pixels.
     */
    public int getOffsetBeforeX(SyntaxTextArea textArea, TabExpander e,
            float startX, float endBeforeX);

    /**
     * Returns the character array backing the lexeme of this token. This
     * value should be treated as read-only.
     *
     * @return A character array containing the lexeme of this token.
     * @see #getTextOffset()
     * @see #length()
     */
    public char[] getTextArray();

    /**
     * Returns the offset into the character array of the lexeme in
     * {@link #getTextArray()}.
     *
     * @return The offset of the lexeme in the character array.
     * @see #getTextArray()
     */
    public int getTextOffset();

    /**
     * Returns the type of this token.
     *
     * @return The type of this token.
     * @see ITokenTypes
     * @see #setType(int)
     */
    public int getType();

    /**
     * Returns the width of this token given the specified parameters.
     *
     * @param textArea The text area in which the token is being painted.
     * @param e Describes how to expand tabs. This parameter cannot be
     *        <code>null</code>.
     * @param x0 The pixel-location at which the token begins. This is needed
     *        because of tabs.
     * @return The width of the token, in pixels.
     * @see #getWidthUpTo
     */
    public float getWidth(SyntaxTextArea textArea, TabExpander e, float x0);

    /**
     * Returns the width of a specified number of characters in this token.
     * For example, for the token "while", specifying a value of <code>3</code>
     * here returns the width of the "whi" portion of the token.
     *
     * @param numChars The number of characters for which to get the width.
     * @param textArea The text area in which the token is being painted.
     * @param e How to expand tabs. This value cannot be <code>null</code>.
     * @param x0 The pixel-location at which this token begins. This is needed
     *        because of tabs.
     * @return The width of the specified number of characters in this token.
     * @see #getWidth
     */
    public float getWidthUpTo(int numChars, SyntaxTextArea textArea,
            TabExpander e, float x0);

    /**
     * Returns whether this token's lexeme matches a specific character array.
     *
     * @param lexeme The lexeme to check for.
     * @return Whether this token has that lexeme.
     * @see #is(int, char[])
     * @see #is(int, String)
     * @see #isSingleChar(int, char)
     * @see #startsWith(char[])
     */
    public boolean is(char[] lexeme);

    /**
     * Returns whether this token is of the specified type, with the specified
     * lexeme.<p>
     * This method is preferred over the other overload in performance-critical
     * code where this operation may be called frequently, since it does not
     * involve any String allocations.
     *
     * @param type The type to check for.
     * @param lexeme The lexeme to check for.
     * @return Whether this token has that type and lexeme.
     * @see #is(int, String)
     * @see #is(char[])
     * @see #isSingleChar(int, char)
     * @see #startsWith(char[])
     */
    public boolean is(int type, char[] lexeme);

    /**
     * Returns whether this token is of the specified type, with the specified
     * lexeme.<p>
     * The other overload of this method is preferred over this one in
     * performance-critical code, as this one involves a String allocation
     * while the other does not.
     *
     * @param type The type to check for.
     * @param lexeme The lexeme to check for.
     * @return Whether this token has that type and lexeme.
     * @see #is(int, char[])
     * @see #isSingleChar(int, char)
     * @see #startsWith(char[])
     */
    public boolean is(int type, String lexeme);

    /**
     * @return Whether this token is a comment.
     * @see #isWhitespace()
     * @see #isCommentOrWhitespace()
     */
    public boolean isComment();

    /**
     * @return Whether this token is a comment or whitespace.
     * @see #isComment()
     * @see #isWhitespace()
     */
    public boolean isCommentOrWhitespace();

    /**
     * @return Whether this token is a hyperlink.
     * @see #setHyperlink(boolean)
     */
    public boolean isHyperlink();

    /** @return Whether this token is an identifier. */
    public boolean isIdentifier();

    /**
     * Returns whether this token is a {@link #SEPARATOR} representing a single
     * left curly brace.
     *
     * @return Whether this token is a left curly brace.
     * @see #isRightCurly()
     */
    public boolean isLeftCurly();

    /**
     * Returns whether this token is a {@link #SEPARATOR} representing a single
     * right curly brace.
     *
     * @return Whether this token is a right curly brace.
     * @see #isLeftCurly()
     */
    public boolean isRightCurly();

    /**
     * Returns whether or not this token is "paintable;" i.e., whether or not
     * the type of this token is one such that it has an associated syntax
     * style. What this boils down to is whether the token type is greater
     * than <code>IToken.NULL</code>.
     *
     * @return Whether or not this token is paintable.
     */
    public boolean isPaintable();

    /**
     * Returns whether this token is the specified single character.
     *
     * @param ch The character to check for.
     * @return Whether this token's lexeme is the single character specified.
     * @see #isSingleChar(int, char)
     */
    public boolean isSingleChar(char ch);

    /**
     * Returns whether this token is the specified single character, and of a
     * specific type.
     *
     * @param type The token type.
     * @param ch The character to check for.
     * @return Whether this token is of the specified type, and with a lexeme
     *         Equaling the single character specified.
     * @see #isSingleChar(char)
     */
    public boolean isSingleChar(int type, char ch);

    /**
     * Returns whether or not this token is whitespace.
     *
     * @return <code>true</code> if this token is whitespace.
     * @see #isComment()
     * @see #isCommentOrWhitespace()
     */
    public boolean isWhitespace();

    /**
     * @return The length of this token.
     * @see #getOffset()
     */
    public int length();

    /**
     * Returns the bounding box for the specified document location. The
     * location must be in the specified token list; if it isn't,
     * <code>null</code> is returned.
     *
     * @param textArea The text area from which the token list was derived.
     * @param e How to expand tabs.
     * @param pos The position in the document for which to get the bounding
     *        box in the view.
     * @param x0 The pixel x-location that is the beginning of
     *        <code>tokenList</code>.
     * @param rect The rectangle in which we'll be returning the results. This
     *        object is reused to keep from frequent memory allocations.
     * @return The bounding box for the specified position in the model.
     */
    public Rectangle listOffsetToView(SyntaxTextArea textArea, TabExpander e,
            int pos, int x0, Rectangle rect);

    /**
     * Sets whether this token is a hyperlink.
     *
     * @param hyperlink Whether this token is a hyperlink.
     * @see #isHyperlink()
     */
    public void setHyperlink(boolean hyperlink);

    /**
     * Sets the language index for this token. If this value is positive, it
     * denotes a specific "secondary" language this token represents (such as
     * JavaScript code or CSS embedded in an HTML file). If this value is
     * <code>0</code>, this token is in the "main" language being edited.
     * Negative values are invalid and treated as <code>0</code>.
     *
     * @param languageIndex The new language index. A value of
     *        <code>0</code> denotes the "main" language, any positive value
     *        denotes a specific secondary language. Negative values will
     *        be treated as <code>0</code>.
     * @see #getLanguageIndex()
     */
    public void setLanguageIndex(int languageIndex);

    /**
     * Sets the type of this token.
     *
     * @param type The new token type.
     * @see ITokenTypes
     * @see #getType()
     */
    public void setType(int type);

    /**
     * Returns whether this token starts with the specified characters.
     *
     * @param chars The characters.
     * @return Whether this token starts with those characters.
     * @see #endsWith(char[])
     * @see #is(int, char[])
     */
    public boolean startsWith(char[] chars);

    /**
     * Returns the position in the document corresponding to the specified
     * position in this token's internal char array (<code>textOffset</code> -
     * <code>textOffset+textCount-1</code>).<p>
     * Note that this method does NOT do any bounds checking; you can pass in
     * an invalid token position, and you will not receive an Exception or any
     * other indication that the returned document position is invalid. It is
     * up to the user to ensure valid input.
     *
     * @param pos A position in the token's internal char array
     *        (<code>textOffset</code> - <code>textOffset+textCount</code>).
     * @return The corresponding position in the document.
     * @see #documentToToken(int)
     */
    public int tokenToDocument(int pos);
}