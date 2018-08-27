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

import java.util.List;

/**
 * This event is fired off by the SpellChecker and is passed to the registered
 * ISpellCheckListeners.
 *
 * @author D. Campione
 * 
 */
class BasicSpellCheckEvent implements ISpellCheckEvent {

    /** The list holding the suggested Word objects for the misspelt word. */
    private List suggestions;

    /** The misspelt word. */
    private String invalidWord;

    /** The action to be done when the event returns. */
    private short action = INITIAL;

    /**
     * Contains the word to be replaced if the action is REPLACE or REPLACEALL.
     */
    private String replaceWord = null;

    private int startPosition;

    /**
     * Constructs the ISpellCheckEvent.
     * 
     * @param invalidWord The word that is misspelt.
     * @param suggestions A list of Word objects that are suggested to replace
     *        the currently misspelt word.
     * @param tokenizer The reference to the tokenizer that caused this event to
     *        fire.
     */
    public BasicSpellCheckEvent(String invalidWord, List suggestions,
            IWordTokenizer tokenizer) {
        this.invalidWord = invalidWord;
        this.suggestions = suggestions;
        this.startPosition = tokenizer.getCurrentWordPosition();
    }

    /**
     * Returns the list of suggested Word objects.
     * 
     * @return A list of words phonetically close to the misspelt word.
     */
    @Override
    public List getSuggestions() {
        return suggestions;
    }

    /**
     * Returns the currently misspelt word.
     * 
     * @return The text misspelt.
     */
    @Override
    public String getInvalidWord() {
        return invalidWord;
    }

    /**
     * Returns the context in which the misspelt word is used.
     * 
     * @return The text containing the context.
     */
    @Override
    public String getWordContext() {
        return null;
    }

    /**
     * Returns the start position of the misspelt word in the context.
     * 
     * @return The position of the word.
     */
    @Override
    public int getWordContextPosition() {
        return startPosition;
    }

    /**
     * Returns the action type the user has to handle.
     * 
     * @return The type of action the event is carrying.
     */
    @Override
    public short getAction() {
        return action;
    }

    /**
     * Returns the text to replace.
     * 
     * @return The text of the word to replace.
     */
    @Override
    public String getReplaceWord() {
        return replaceWord;
    }

    /**
     * Set the action to replace the currently misspelt word with the new word.
     * 
     * @param newWord The word to replace the currently misspelt word.
     * @param replaceAll If set to true, the SpellChecker will replace all
     *        further occurrences of the misspelt word without firing a
     *        ISpellCheckEvent.
     */
    @Override
    public void replaceWord(String newWord, boolean replaceAll) {
        if (action != INITIAL) {
            throw new IllegalStateException(
                    "The action can can only be set once");
        }
        if (replaceAll) {
            action = REPLACEALL;
        } else {
            action = REPLACE;
        }
        replaceWord = newWord;
    }

    /**
     * Set the action it ignore the currently misspelt word.
     * 
     * @param ignoreAll If set to true, the SpellChecker will replace all
     *        further occurrences of the misspelt word without firing a
     *        ISpellCheckEvent.
     */
    @Override
    public void ignoreWord(boolean ignoreAll) {
        if (action != INITIAL) {
            throw new IllegalStateException(
                    "The action can can only be set once");
        }
        if (ignoreAll) {
            action = IGNOREALL;
        } else {
            action = IGNORE;
        }
    }

    /**
     * Set the action to add a new word into the dictionary. This will also
     * replace the currently misspelt word.
     * 
     * @param newWord The new word to add to the dictionary.
     */
    @Override
    public void addToDictionary(String newWord) {
        if (action != INITIAL) {
            throw new IllegalStateException(
                    "The action can can only be set once");
        }
        action = ADDTODICT;
        replaceWord = newWord;
    }

    /** Set the action to terminate processing of the spellchecker. */
    @Override
    public void cancel() {
        if (action != INITIAL) {
            throw new IllegalStateException(
                    "The action can can only be set once");
        }
        action = CANCEL;
    }
}