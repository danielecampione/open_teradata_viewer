/*
 * Open Teradata Viewer ( editor )
 * Copyright (C), D. Campione
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
package net.sourceforge.open_teradata_viewer.editor;

/**
 * The way the lines of a text file are separated: with a carriage return
 * followed by a line feed (the files written on Windows), with a line feed only
 * (Unix) or with a carriage return only (the old Macintosh).
 * <p>
 * The text of an editor document never holds carriage returns: every line ends
 * with a line feed, whatever the file the text comes from was like, so that the
 * caret can only be before or after a line ending and never in the middle of
 * it. A document remembers the line ending of its file instead, see
 * {@link #detect(String)}, and the text is given that line ending again when it
 * is written, see {@link #toFile(String)}.
 *
 * @author D. Campione
 *
 */
public enum LineEnding {

    /** A carriage return followed by a line feed. */
    WINDOWS("\r\n"),

    /** A line feed. */
    UNIX("\n"),

    /** A carriage return. */
    MAC("\r");

    private final String sequence;

    private LineEnding(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return The characters that separate two lines in a file that has this
     *         line ending.
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @return The line ending of the files that are written on the platform
     *         the application runs on: the one of a file that has just been
     *         created.
     */
    public static LineEnding getDefault() {
        return getDefault(System.lineSeparator());
    }

    /**
     * @param lineSeparator The line separator of a platform, as
     *        {@link System#lineSeparator()} gives it.
     * @return The line ending that goes with it: the one with a carriage
     *         return and a line feed if that is the separator, otherwise the
     *         one with a line feed.
     */
    public static LineEnding getDefault(String lineSeparator) {
        return WINDOWS.sequence.equals(lineSeparator) ? WINDOWS : UNIX;
    }

    /**
     * Tells the line ending of a text from the first line ending it has, as an
     * editor of text files does to tell what a file is like: the lines that
     * end differently later are taken for ending as the first one.
     *
     * @param text The text of a file.
     * @return The line ending of the text, or <code>null</code> if the text has
     *         no line ending at all.
     */
    public static LineEnding detect(String text) {
        for (int i = 0, length = text.length(); i < length; i++) {
            char c = text.charAt(i);
            if (c == '\r') {
                return i + 1 < length && text.charAt(i + 1) == '\n' ? WINDOWS : MAC;
            }
            if (c == '\n') {
                return UNIX;
            }
        }
        return null;
    }

    /**
     * Gives a text the line ending that the text of a document has: every line
     * ends with a line feed. A carriage return followed by a line feed, and a
     * carriage return alone, are line endings like a line feed alone.
     *
     * @param text The text of a file.
     * @return The text with a line feed at the end of every line, which is the
     *         text itself if it has no carriage return.
     */
    public static String normalize(String text) {
        if (text.indexOf('\r') < 0) {
            return text;
        }
        StringBuilder normalized = new StringBuilder(text.length());
        for (int i = 0, length = text.length(); i < length; i++) {
            char c = text.charAt(i);
            if (c == '\r') {
                normalized.append('\n');
                // A carriage return followed by a line feed is one line ending
                if (i + 1 < length && text.charAt(i + 1) == '\n') {
                    i++;
                }
            } else {
                normalized.append(c);
            }
        }
        return normalized.toString();
    }

    /**
     * Gives the text of a document the line ending of a file, so that the text
     * can be written in it.
     *
     * @param text The text of a document, or a part of it.
     * @return The text with this line ending at the end of every line.
     */
    public String toFile(String text) {
        String normalized = normalize(text);
        return this == UNIX ? normalized : normalized.replace("\n", sequence);
    }
}
