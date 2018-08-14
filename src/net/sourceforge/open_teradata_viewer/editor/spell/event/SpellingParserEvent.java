/*
 * Open Teradata Viewer ( editor spell event )
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

package net.sourceforge.open_teradata_viewer.editor.spell.event;

import java.util.EventObject;

import net.sourceforge.open_teradata_viewer.editor.spell.SpellingParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * An event fired by the spelling parser.
 *
 * @author D. Campione
 * 
 */
public class SpellingParserEvent extends EventObject {

    /** Event type specifying that a word was added to the user's dictionary. */
    public static final int WORD_ADDED = 0;

    /**
     * Event type specifying that a word will be ignored for the rest of this
     * JVM session.
     */
    public static final int WORD_IGNORED = 1;

    private SyntaxTextArea textArea;
    private int type;
    private String word;

    /**
     * Ctor.
     *
     * @param source The source parser.
     * @param textArea The text area that was parsed.
     * @param type The type of event.
     * @param word The word being added or ignored.
     * @throws IllegalArgumentException If <code>type</code> is invalid.
     */
    public SpellingParserEvent(SpellingParser source, SyntaxTextArea textArea,
            int type, String word) {
        super(source);
        this.textArea = textArea;
        setType(type);
        this.word = word;
    }

    /**
     * Returns the parser that fired this event. This is a wrapper for
     * <code>(SpellingParser)getSource()</code>.
     *
     * @return The parser.
     */
    public SpellingParser getParser() {
        return (SpellingParser) getSource();
    }

    /**
     * Returns the text area that was parsed.
     *
     * @return The text area.
     */
    public SyntaxTextArea getTextArea() {
        return textArea;
    }

    /**
     * Returns the type of this event.
     *
     * @return Either {@link #WORD_ADDED} or {@link #WORD_IGNORED}.
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the word being added or ignored.
     *
     * @return The word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the type of event being fired.
     *
     * @param type The type of event being fired.
     */
    private void setType(int type) {
        if (type != WORD_ADDED && type != WORD_IGNORED) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        this.type = type;
    }
}