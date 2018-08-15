/*
 * Open Teradata Viewer ( editor spell event )
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

package net.sourceforge.open_teradata_viewer.editor.spell.event;

/**
 * <p>An interface for objects which take a text-based media as input and
 * iterate through the words in the text stored in that media. Examples of such
 * media could be Strings, Documents, Files, TextComponents etc..</p>
 *
 * <p>When the object is instantiated, and before the first call to
 * <code>next()</code> is made, the following methods should throw a
 * <code>WordNotFoundException</code>:<br>
 * <code>getCurrentWordEnd()</code>, <code>getCurrentWordPosition()</code>,
 *  <code>isNewSentence()</code> and <code>replaceWord()</code>.</p>
 *
 * <p>A call to <code>next()</code> when <code>hasMoreWords()</code> returns
 * false should throw a <code>WordNotFoundException</code>.</p>
 * 
 * @author D. Campione
 * 
 */
public interface IWordTokenizer {

    //~ Methods .................................................................

    /**
     * Returns the context text that is being tokenized (should include any
     * changes that have been made).
     * 
     * @return The text being searched.
     */
    public String getContext();

    /**
     * Returns the number of word tokens that have been processed thus far.
     * 
     * @return The number of words found so far.
     */
    public int getCurrentWordCount();

    /**
     * Returns an index representing the end location of the current word in the
     * text.
     * 
     * @return Index of the end of the current word in the text.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public int getCurrentWordEnd();

    /**
     * Returns an index representing the start location of the current word in
     * the text.
     * 
     * @return Index of the start of the current word in the text.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public int getCurrentWordPosition();

    /**
     * Returns true if the current word is at the start of a sentence.
     * 
     * @return true if the current word starts a sentence.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public boolean isNewSentence();

    /**
     * Indicates if there are more words left.
     * 
     * @return true if more words can be found in the text.
     */
    public boolean hasMoreWords();

    /**
     * This returns the next word in the iteration. Note that any implementation
     * should return the current word and then replace the current word with
     * the next word found in the input text (if one exists).
     * 
     * @return The next word in the iteration.
     * @throws WordNotFoundException Search string contains no more words.
     */
    public String nextWord();

    /**
     * Replaces the current word token.<p/>
     * 
     * When a word is replaced care should be taken that the IWordTokenizer
     * repositions itself such that the words that were added aren't rechecked.
     * Of course this is not mandatory, maybe there is a case when an
     * application doesn't need to do this.<p/>
     * 
     * @param newWord The string which should replace the current word.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    public void replaceWord(String newWord);
}