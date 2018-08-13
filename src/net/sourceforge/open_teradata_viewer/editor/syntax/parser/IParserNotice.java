/*
 * Open Teradata Viewer ( editor syntax parser )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import java.awt.Color;

/**
 * A notice (e.g., a warning or error) from a parser.
 *
 * @author D. Campione
 * @see DefaultParserNotice
 * 
 */
public interface IParserNotice extends Comparable<IParserNotice> {

    /** Indicates an info notice. */
    public static final int INFO = 2;

    /** Indicates a warning notice. */
    public static final int WARNING = 1;

    /** Indicates an error notice. */
    public static final int ERROR = 0;

    /**
     * Returns whether this parser notice contains the specified location in the
     * document.
     *
     * @param pos The position in the document.
     * @return Whether the position is contained. This will always return
     *         <code>false</code> if {@link #getOffset()} returns
     *         <code>-1</code>.
     */
    public boolean containsPosition(int pos);

    /**
     * Returns the color to use when painting this notice.
     *
     * @return The color.
     */
    public Color getColor();

    /**
     * Returns the length of the code the message is concerned with.
     *
     * @return The length of the code the message is concerned with, or
     *         <code>-1</code> if unknown.
     * @see #getOffset()
     * @see #getLine()
     */
    public int getLength();

    /**
     * Returns the level of this notice.
     *
     * @return One of {@link #INFO}, {@link #WARNING} OR {@link #ERROR}.
     */
    public int getLevel();

    /**
     * Returns the line number the notice is about.
     *
     * @return The line number.
     */
    public int getLine();

    /** @return The message from the parser. */
    public String getMessage();

    /**
     * Returns the offset of the code the message is concerned with.
     *
     * @return The offset, or <code>-1</code> if unknown.
     * @see #getLength()
     * @see #getLine()
     */
    public int getOffset();

    /** @return The parser that created this message. */
    public IParser getParser();

    /**
     * Whether a squiggle underline should be drawn in the editor for this
     * notice.
     *
     * @return Whether a squiggle underline should be drawn.
     */
    public boolean getShowInEditor();

    /**
     * Returns the tooltip text to display for this notice.
     *
     * @return The tool tip text. If none has been explicitly set, this method
     *         returns the same text as {@link #getMessage()}.
     */
    public String getToolTipText();
}