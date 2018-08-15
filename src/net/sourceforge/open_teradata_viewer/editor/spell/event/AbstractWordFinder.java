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

import java.text.BreakIterator;

/**
 * Defines common methods and behaviour for the various word finding
 * subclasses.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractWordFinder implements IWordFinder {

    //~ Instance/static variables .............................................

    /** The word being analyzed. */
    protected Word currentWord;

    /** The word following the current one. */
    protected Word nextWord;

    /** Indicate if the current word starts a new sentence. */
    protected boolean startsSentence;

    /** Holds the text to analyze. */
    protected String text;

    /** An iterator to work through the sentence. */
    protected BreakIterator sentenceIterator;

    //~ Constructors ..........................................................

    /**
     * Creates a new AbstractWordFinder object.
     *
     * @param inText the String to iterate through.
     */
    public AbstractWordFinder(String inText) {
        text = inText;
        setup();
    }

    /** Creates a new default AbstractWordFinder object. */
    public AbstractWordFinder() {
        text = "";
        setup();
    }

    //~ Methods ...............................................................

    /**
     * This method scans the text from the end of the last word and returns a
     * new Word object corresponding to the next word.
     *
     * @return The following word.
     */
    @Override
    public abstract Word next();

    /**
     * Return the text being searched. May have changed since first set through
     * calls to replace.
     *
     * @return The text being searched.
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * Defines the text to search.
     * 
     * @param newText The text to be analyzed.
     */
    @Override
    public void setText(String newText) {
        text = newText;
        setup();
    }

    /**
     * Returns the current word in the iteration.
     *
     * @return The current word.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public Word current() {
        if (currentWord == null) {
            throw new WordNotFoundException("No Words in current String");
        }

        return currentWord;
    }

    /**
     * Indicates if there is some more word to analyze.
     * 
     * @return true if there are further words in the string.
     */
    @Override
    public boolean hasNext() {
        return nextWord != null;
    }

    /**
     * Replace the current word in the search with a replacement string.
     *
     * @param newWord The replacement string.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public void replace(String newWord) {
        if (currentWord == null) {
            throw new WordNotFoundException("No Words in current String");
        }

        StringBuilder sb = new StringBuilder(text.substring(0,
                currentWord.getStart()));
        sb.append(newWord);
        sb.append(text.substring(currentWord.getEnd()));
        int diff = newWord.length() - currentWord.getText().length();
        currentWord.setText(newWord);
        /* Conditional to ensure a NPE is avoided */
        if (nextWord != null) {
            nextWord.setStart(nextWord.getStart() + diff);
        }
        text = sb.toString();

        sentenceIterator.setText(text);
        int start = currentWord.getStart();
        sentenceIterator.following(start);
        startsSentence = sentenceIterator.current() == start;
    }

    /**
     * @return true if the current word starts a new sentence.
     * @throws WordNotFoundException Current word has not yet been set.
     */
    @Override
    public boolean startsSentence() {
        if (currentWord == null) {
            throw new WordNotFoundException("No Words in current String");
        }

        return startsSentence;
    }

    /**
     * Return the text being searched. May have changed since first set through
     * calls to replace.
     *
     * @return The text being searched.
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Adjusts the sentence iterator and the startSentence flag according to the
     * currentWord.
     * 
     * @param wd The wd parameter is not presently used.
     */
    protected void setSentenceIterator(Word wd) {
        int current = sentenceIterator.current();

        if (current == currentWord.getStart()) {
            startsSentence = true;
        } else {
            startsSentence = false;

            if (currentWord.getEnd() > current) {
                sentenceIterator.next();
            }
        }
    }

    /**
     * Indicates if the character at the specified position is acceptable as
     * part of a word. To be acceptable, the character need to be a letter or a
     * digit. It is also acceptable if the character is one of ''', '@', '.' or
     * '_' and is preceded and followed by letter or digit.
     * 
     * @param posn The character position to analyze.
     * @return true if the character is a letter or digit.
     */
    protected boolean isWordChar(int posn) {
        boolean out = false;

        char curr = text.charAt(posn);

        if ((posn == 0) || (posn == text.length() - 1)) {
            return Character.isLetterOrDigit(curr);
        }

        char prev = text.charAt(posn - 1);
        char next = text.charAt(posn + 1);

        switch (curr) {
        case '\'':
        case '@':
        case '.':
        case '_':
            out = (Character.isLetterOrDigit(prev) && Character
                    .isLetterOrDigit(next));
            break;
        default:
            out = Character.isLetterOrDigit(curr);
        }

        return out;
    }

    /**
     * Ignores or skip over text starting from the index position specified 
     * if it contains the <code>startIgnore</code>, and until the first non
     * letter or digit character is encountered or end of text is detected.
     * 
     * @param index The start position in text.
     * @param startIgnore The character that should be at <code>index</code> 
     *        position to start skipping through.
     * @return The index position pointing after the skipped characters or the
     *         original index if the ignore condition could not be met.
     */
    protected int ignore(int index, char startIgnore) {
        return ignore(index, new Character(startIgnore), null);
    }

    /**
     * Ignores or skip over text starting from the index position specified if
     * it contains the <code>startIgnore</code> and until the
     * <code>endIgnore</code> character is encountered or end of text is
     * detected.
     * 
     * @param index The start position in text.
     * @param startIgnore The character that should be at <code>index</code> 
     *        position to start skipping through.
     * @param endIgnore The character which mark the end of skipping through. If
     *        the value of endIgnore is <code>null</code>, skipping characters
     *        stop at first non letter or digit character.
     * @return The index position pointing after the skipped characters or the
     *         original index if the ignore condition could not be met.
     */
    protected int ignore(int index, char startIgnore, char endIgnore) {
        return ignore(index, new Character(startIgnore), new Character(
                endIgnore));
    }

    /**
     * Ignores or skip over text starting from the index position specified if
     * it contains the <code>startIgnore</code> and until the
     * <code>endIgnore</code> character is encountered or end of text is
     * detected.
     * 
     * @param index The start position in text.
     * @param startIgnore The character that should be at <code>index</code> 
     *        position to start skipping through.
     * @param endIgnore The character which mark the end of skipping through. If
     *        the value of endIgnore is <code>null</code>, skipping characters
     *        stop at first non letter or digit character.
     * @return The index position pointing after the skipped characters or the
     *         original index if the ignore condition could not be met.
     */
    protected int ignore(int index, Character startIgnore, Character endIgnore) {
        int newIndex = index;

        if (newIndex < text.length()) {
            Character curChar = new Character(text.charAt(newIndex));

            if (curChar.equals(startIgnore)) {
                newIndex++;
                while (newIndex < text.length()) {
                    curChar = new Character(text.charAt(newIndex));
                    if (endIgnore != null && curChar.equals(endIgnore)) {
                        newIndex++;
                        break;
                    } else if (endIgnore == null
                            && !Character.isLetterOrDigit(curChar.charValue())) {
                        break;
                    }
                    newIndex++;
                }
            }
        }

        return newIndex;
    }

    /**
     * Ignores or skip over text starting from the index position specified 
     * if it contains the <code>startIgnore</code> string and until the 
     * <code>endIgnore</code> string is encountered or end of text is detected.
     * 
     * @param index The start position in text.
     * @param startIgnore The string that should be at <code>index</code> 
     *        position to start skipping through.
     * @param endIgnore The string which mark the end of skipping through.
     * @return The index position pointing after the skipped characters or the
     *         original index if the ignore condition could not be met.
     */
    protected int ignore(int index, String startIgnore, String endIgnore) {
        int newIndex = index;
        int len = text.length();
        int slen = startIgnore.length();
        int elen = endIgnore.length();

        if (!((newIndex + slen) >= len)) {
            String seg = text.substring(newIndex, newIndex + slen);

            if (seg.equals(startIgnore)) {
                newIndex += slen;
                cycle: while (true) {

                    if (newIndex == (text.length() - elen)) {
                        break cycle;
                    }

                    String ss = text.substring(newIndex, newIndex + elen);

                    if (ss.equals(endIgnore)) {
                        newIndex += elen;

                        break cycle;
                    } else {
                        newIndex++;
                    }
                }
            }
        }

        return newIndex;
    }

    /** Initializes the sentenceIterator. */
    protected void init() {
        sentenceIterator = BreakIterator.getSentenceInstance();
        sentenceIterator.setText(text);
    }

    /** Defines the starting positions for text analysis. */
    private void setup() {
        currentWord = new Word("", 0);
        nextWord = new Word("", 0);
        startsSentence = true;

        init();

        try {
            next();
        } catch (WordNotFoundException e) {
            currentWord = null;
            nextWord = null;
        }
    }
}