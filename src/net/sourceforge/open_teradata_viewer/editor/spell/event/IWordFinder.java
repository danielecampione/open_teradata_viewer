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
 * <p>An interface for objects which take a String as input and iterates through
 * the words in the string.</p>
 *
 * <p>When the object is instantiated, and before the first call to
 * <code>next()</code> is made, the following methods should throw a
 * <code>WordNotFoundException</code>:<br>
 * <code>current()</code>,
 *  <code>startsSentence()</code> and <code>replace()</code>.</p>
 *
 * <p>A call to <code>next()</code> when <code>hasMoreWords()</code> returns
 * false should throw a <code>WordNotFoundException</code>.</p>
 * 
 * @author D. Campione
 * 
 */
public interface IWordFinder {

    //~ Methods .................................................................

    /**
     * This method returns the text through which the IWordFinder is iterating.
     * The text may have been modified through calls to replace().
     *
     * @return The (possibly modified) text being searched.
     */
    public String getText();

    /**
     * This method resets the text through which the IWordFinder iterates.
     * It must also re-initialize the IWordFinder.
     *
     * @param newText the new text to search.
     */
    public void setText(String newText);

    /**
     * This method should return the Word object representing the current word
     * in the iteration.
     * This method should not affect the state of the  IWordFinder object.
     *
     * @return The current Word object.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public Word current();

    /**
     * Tests the finder to see if any more words are available.
     *
     * @return true if more words are available.
     */
    public boolean hasNext();

    /**
     * This method should return the Word object representing the next word in
     * the iteration (the first word if next() has not yet been called).
     *
     * @return The next Word in the iteration.
     * @throws WordNotFoundException Search string contains no more words.
     */
    public Word next();

    /**
     * This method should replace the current Word object with a Word object
     * representing the String newWord.
     *
     * @param newWord The word to replace the current word with.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public void replace(String newWord);

    /**
     * Indicates if the current word starts a new sentence.
     * 
     * @return true if the current word starts a new sentence.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public boolean startsSentence();
}