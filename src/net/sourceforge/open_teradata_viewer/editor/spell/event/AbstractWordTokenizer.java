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

import java.text.BreakIterator;

/**
 * This class tokenizes a input string.
 *
 * <p>It also allows for the string to be mutated. The result after the spell
 * checking is completed is available to the call to getFinalText.</p>
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractWordTokenizer implements IWordTokenizer {

    //~ Instance/static variables ...............................................

    /** The word being analyzed. */
    protected Word currentWord;

    /**
     * The word finder used to filter out words which are non pertinent to spell
     * checking.
     */
    protected IWordFinder finder;

    /** An iterator to work through the sentence. */
    protected BreakIterator sentenceIterator;

    /** The cumulative word count that have been processed. */
    protected int wordCount = 0;

    //~ Constructors ............................................................

    /**
     * Creates a new AbstractWordTokenizer object.
     *
     * @param text the text to process.
     */
    public AbstractWordTokenizer(String text) {
        this(new DefaultWordFinder(text));
    }

    /**
     * Creates a new AbstractWordTokenizer object.
     *
     * @param wf The custom IWordFinder to use in searching for words.
     */
    public AbstractWordTokenizer(IWordFinder wf) {
        this.finder = wf;
    }

    //~ Methods .................................................................

    /**
     * Returns the current number of words that have been processed.
     *
     * @return Number of words so far iterated.
     */
    @Override
    public int getCurrentWordCount() {
        return wordCount;
    }

    /**
     * Returns the end of the current word in the text.
     *
     * @return Index in string of the end of the current word.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public int getCurrentWordEnd() {
        if (currentWord == null) {
            throw new WordNotFoundException("No Words in current String");
        }

        return currentWord.getEnd();
    }

    /**
     * Returns the index of the start of the current word in the text.
     *
     * @return Index in string of the start of the current word.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public int getCurrentWordPosition() {
        if (currentWord == null) {
            throw new WordNotFoundException("No Words in current String");
        }

        return currentWord.getStart();
    }

    /**
     * Returns true if there are more words that can be processed in the string.
     *
     * @return true if there are further words in the text.
     */
    @Override
    public boolean hasMoreWords() {
        return finder.hasNext();
    }

    /**
     * Returns searches for the next word in the text and returns that word.
     *
     * @return The string representing the current word.
     * @throws WordNotFoundException Search string contains no more words.
     */
    @Override
    public String nextWord() {
        currentWord = finder.next();

        return currentWord.getText();
    }

    /**
     * Replaces the current word token.
     *
     * @param newWord Replacement word.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public abstract void replaceWord(String newWord);

    /**
     * Returns the current text that is being tokenized (includes any changes
     * that have been made).
     *
     * @return The text being tokenized.
     */
    @Override
    public String getContext() {
        return finder.toString();
    }

    /**
     * Returns true if the current word is at the start of a sentence.
     *
     * @return true if the current word starts a sentence.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public boolean isNewSentence() {
        return finder.startsSentence();
    }
}