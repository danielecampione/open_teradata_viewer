/*
 * Open Teradata Viewer ( editor spell engine )
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

package net.sourceforge.open_teradata_viewer.editor.spell.engine;

import java.util.Comparator;

/**
 * The Word object holds information for one suggested spelling.
 * It contains both the suggested word string and the distance cost, which
 * represents how different the suggested word is from the misspelling.
 *
 * <p>This class is immutable.</p>
 * 
 * @author D. Campione
 * 
 */
public class Word implements Comparator {

    private String word;
    private int score;

    /**
     * Constructs a new Word.
     * 
     * @param word The text of a word.
     * @param score The word's distance cost.
     */
    public Word(String word, int score) {
        this.word = word;
        this.score = score;
    }

    /** Constructs a new Word. */
    public Word() {
        this.word = "";
        this.score = 0;
    }

    /**
     * Compares two words, mostly for the purpose of sorting words.
     * 
     * @param o1 The first word.
     * @param o2 The second word.
     * @return -1 if the first word is more similar to the misspelled word.<br/>
     *         1 if the second word is more similar to the misspelled word.<br/>
     *         0 if both words are equally similar.
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (((Word) o1).getCost() < ((Word) o2).getCost()) {
            return -1;
        }
        if (((Word) o1).getCost() == ((Word) o2).getCost()) {
            return 0;
        }
        return 1;
    }

    /**
     * Indicates if this word is equal to another one.
     * 
     * @param o The other word to compare.
     * @return The indication of equality.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Word) {
            return (((Word) o).getWord().equals(getWord()));
        }
        return false;
    }

    /**
     * Gets suggested spelling.
     * @return The actual text of the suggest spelling.
     */
    public String getWord() {
        return word;
    }

    /**
     * Overridden since {@link #equals(Object)} is overridden.
     *
     * @return The hash code for this word.
     */
    @Override
    public int hashCode() {
        return word.hashCode();
    }

    /**
     * Sets suggested spelling.
     * 
     * @param word The text to set for suggested spelling.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * A cost measures how close a match this word was to the original word.
     * 
     * @return 0 if an exact match. Higher numbers are worse matches.
     * @see EditDistance
     */
    public int getCost() {
        return score;
    }

    /**
     * Returns the suggested spelling.
     * 
     * @return The word's text.
     */
    @Override
    public String toString() {
        return word;
    }
}