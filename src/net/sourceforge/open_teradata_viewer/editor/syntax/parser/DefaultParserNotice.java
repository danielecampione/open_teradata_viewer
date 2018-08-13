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
 * Base implementation of a parser notice. Most <code>IParser</code>
 * implementations can return instances of this in their parse result.
 *
 * @author D. Campione
 * @see IParser
 * @see IParseResult
 */
public class DefaultParserNotice implements IParserNotice {

    private IParser parser;
    private int level;
    private int line;
    private int offset;
    private int length;
    private boolean showInEditor;
    private Color color;
    private String message;
    private String toolTipText;

    private static final Color[] DEFAULT_COLORS = {new Color(255, 0, 128), // Error
            new Color(244, 200, 45), // Warning
            Color.gray, // Info
    };

    /**
     * Ctor.
     *
     * @param parser The parser that created this notice.
     * @param msg The text of the message.
     * @param line The line number for the message.
     */
    public DefaultParserNotice(IParser parser, String msg, int line) {
        this(parser, msg, line, -1, -1);
    }

    /**
     * Ctor.
     *
     * @param parser The parser that created this notice.
     * @param message The message.
     * @param line The line number corresponding to the message.
     * @param offset The offset in the input stream of the code the
     *               message is concerned with, or <code>-1</code> if unknown.
     * @param length The length of the code the message is concerned with,
     *        or <code>-1</code> if unknown.
     */
    public DefaultParserNotice(IParser parser, String message, int line,
            int offset, int length) {
        this.parser = parser;
        this.message = message;
        this.line = line;
        this.offset = offset;
        this.length = length;
        setLevel(ERROR);
        setShowInEditor(true);
    }

    /**
     * Compares this parser notice to another.
     *
     * @param other Another parser notice.
     * @return How the two parser notices should be sorted relative to one
     *         another.
     */
    public int compareTo(IParserNotice other) {
        int diff = -1;
        if (other != null) {
            diff = level - other.getLevel();
            if (diff == 0) {
                diff = line - other.getLine();
                if (diff == 0) {
                    diff = message.compareTo(other.getMessage());
                }
            }
        }
        return diff;
    }

    /** {@inheritDoc} */
    public boolean containsPosition(int pos) {
        return offset <= pos && pos < (offset + length);
    }

    /**
     * Returns whether this parser notice is equal to another one.
     *
     * @param obj Another parser notice.
     * @return Whether the two notices are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IParserNotice)) {
            return false;
        }
        return compareTo((IParserNotice) obj) == 0;
    }

    /** {@inheritDoc} */
    public Color getColor() {
        Color c = color; // User-defined
        if (c == null) {
            c = DEFAULT_COLORS[getLevel()];
        }
        return c;
    }

    /** {@inheritDoc} */
    public int getLength() {
        return length;
    }

    /** {@inheritDoc} */
    public int getLevel() {
        return level;
    }

    /** {@inheritDoc} */
    public int getLine() {
        return line;
    }

    /** {@inheritDoc} */
    public String getMessage() {
        return message;
    }

    /** {@inheritDoc} */
    public int getOffset() {
        return offset;
    }

    /** {@inheritDoc} */
    public IParser getParser() {
        return parser;
    }

    /** {@inheritDoc} */
    public boolean getShowInEditor() {
        return showInEditor;
    }

    /** {@inheritDoc} */
    public String getToolTipText() {
        return toolTipText != null ? toolTipText : getMessage();
    }

    /**
     * Returns the hash code for this notice.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return (line << 16) | offset;
    }

    /**
     * Sets the color to use when painting this notice.
     *
     * @param color The color to use.
     * @see #getColor()
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the level of this notice.
     *
     * @param level The new level.
     * @see #getLevel()
     */
    public void setLevel(int level) {
        if (level > INFO) {
            level = INFO;
        } else if (level < ERROR) {
            level = ERROR;
        }
        this.level = level;
    }

    /**
     * Sets whether a squiggle underline should be drawn in the editor for this
     * notice.
     *
     * @param show Whether to draw a squiggle underline.
     * @see #getShowInEditor()
     */
    public void setShowInEditor(boolean show) {
        showInEditor = show;
    }

    /**
     * Sets the tooltip text to display for this notice.
     *
     * @param text The new tooltip text. This can be HTML. If this is
     *        <code>null</code>, then tooltips will return the same text as
     *        {@link #getMessage()}.
     * @see #getToolTipText()
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;
    }

    /**
     * Returns a string representation of this parser notice.
     *
     * @return This parser notice as a string.
     */
    @Override
    public String toString() {
        return "Line " + getLine() + ": " + getMessage();
    }
}