/*
 * Open Teradata Viewer ( editor syntax parser )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import java.awt.Color;

/**
 * A notice (e.g., a warning or error) from a parser.<p>
 *
 * Since different parsers have different levels of precision when it comes to
 * identifying errors in code, this class supports marking parser notices on
 * either a per-line basis or arbitrary regions of a document. For any
 * <code>IParserNotice</code>, {@link #getLine()} is guaranteed to return the
 * (primary) line containing the notice but {@link #getOffset()} and {@link
 * #getLength()} are allowed to return <code>-1</code> if that particular notice
 * isn't mapped to a specific region of code.
 *
 * @author D. Campione
 * @see DefaultParserNotice
 */
public interface IParserNotice extends Comparable<IParserNotice> {

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
     * @return A value from the {@link Level} enumeration.
     */
    public Level getLevel();

    /**
     * Returns the line number the notice is about.
     *
     * @return The line number.
     */
    public int getLine();

    /**
     * Returns whether this parser notice has offset and length information (as
     * opposed to just what line number to mark).
     *
     * @return Whether the offset and length of the notice are specified.
     * @see #getLine()
     * @see #getOffset()
     * @see #getLength()
     */
    public boolean getKnowsOffsetAndLength();

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

    /**
     * Denotes the severity of a parser notice.
     * 
     * @author D. Campione
     * 
     */
    public static enum Level {

        /** Indicates an informational notice. */
        INFO(2),

        /** Indicates a warning notice. */
        WARNING(1),

        /** Indicates an error notice. */
        ERROR(0);

        private int value;

        private Level(int value) {
            this.value = value;
        }

        /**
         * Returns the value of this notice level, as an integer.
         *
         * @return A numeric value for this notice level.
         */
        public int getNumericValue() {
            return value;
        }

        /**
         * Returns whether this level is as sever as, or worse than, another
         * level.
         *
         * @param other The other level.
         * @return Whether this level is equal to or more severe.
         */
        public boolean isEqualToOrWorseThan(Level other) {
            return value <= other.getNumericValue();
        }
    }
}