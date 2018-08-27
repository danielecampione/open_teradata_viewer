/*
 * Open Teradata Viewer ( editor spell event )
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

package net.sourceforge.open_teradata_viewer.editor.spell.event;

/**
 * Offers basic methods to manipulate a text string representing a word.
 * 
 * @author D. Campione
 * 
 */
public class Word {

    //~ Instance/static variables ...............................................

    private int end;
    private int start;
    private String text;

    //~ Constructors ............................................................

    /**
     * Creates a new Word object.
     *
     * @param text The String representing the word.
     * @param start The start index of the word.
     */
    public Word(String text, int start) {
        this.text = text;
        this.start = start;
        setEnd();
    }

    /**
     * Creates a new Word object by cloning an existing Word object.
     *
     * @param w The word object to clone.
     */
    public Word(Word w) {
        this.copy(w);
    }

    //~ Methods .................................................................

    /**
     * Evaluate the end of word position.
     * 
     * @return The end index of the word.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Set the start index of the word.
     *
     * @param s The start index.
     */
    public void setStart(int s) {
        start = s;
        setEnd();
    }

    /**
     * Evaluate the start of word position.
     * 
     * @return The start index.
     */
    public int getStart() {
        return start;
    }

    /**
     * Set the text to a new string value.
     *
     * @param s The new text.
     */
    public void setText(String s) {
        text = s;
        setEnd();
    }

    /**
     * Supply the text string representing the word.
     * 
     * @return The String representing the word.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of this Word to be a copy of another.
     *
     * @param w The Word to copy.
     */
    public void copy(Word w) {
        text = w.toString();
        start = w.getStart();
        setEnd();
    }

    /**
     * Evaluate the length of the word.
     * 
     * @return The length of the word.
     */
    public int length() {
        return text.length();
    }

    /**
     * Supply the text representing the word.
     * 
     * @return The text representing the word.
     */
    @Override
    public String toString() {
        return text;
    }

    /** Set the end index of the word. */
    private void setEnd() {
        end = start + text.length();
    }
}