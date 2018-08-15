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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * This class tokenizes a swing document model. It also allows for the document
 * model to be changed when corrections occur.
 *
 * @author D. Campione
 * 
 */
public class DocumentWordTokenizer implements IWordTokenizer {

    /** Holds the start character position of the current word. */
    private int currentWordPos = 0;

    /** Holds the end character position of the current word. */
    private int currentWordEnd = 0;

    /** Holds the start character position of the next word. */
    private int nextWordPos = -1;

    /** The actual text that is being tokenized. */
    private Document document;

    /** The character iterator over the document. */
    private Segment text;

    /** The cumulative word count that have been processed. */
    private int wordCount = 0;

    /** Flag indicating if there are any more tokens (words) left. */
    private boolean moreTokens = true;

    /**
     * Is this a special case where the currentWordStart, currntWordEnd and
     * nextWordPos have already been calculated (see nextWord).
     */
    private boolean first = true;
    private BreakIterator sentenceIterator;
    private boolean startsSentence = true;

    /**
     * Creates a new DocumentWordTokenizer to work on a document.
     * 
     * @param document The document to spell check.
     */
    public DocumentWordTokenizer(Document document) {
        this.document = document;
        // Create a text segment over the entire document
        text = new Segment();
        sentenceIterator = BreakIterator.getSentenceInstance();
        try {
            document.getText(0, document.getLength(), text);
            sentenceIterator.setText(text);
            currentWordPos = getNextWordStart(text, text.getBeginIndex());
            // If the current word pos is -1 then the string was all white space
            if (currentWordPos != -1) {
                currentWordEnd = getNextWordEnd(text, currentWordPos);
                nextWordPos = getNextWordStart(text, currentWordEnd);
            } else {
                moreTokens = false;
            }
        } catch (BadLocationException ex) {
            moreTokens = false;
        }
    }

    /**
     * This helper method will return the start character of the next word in
     * the buffer from the start position.
     */
    private static int getNextWordStart(Segment text, int startPos) {
        if (startPos <= text.getEndIndex()) {
            for (char ch = text.setIndex(startPos); ch != Segment.DONE; ch = text
                    .next()) {
                if (Character.isLetterOrDigit(ch)) {
                    return text.getIndex();
                }
            }
        }
        return -1;
    }

    /**
     * This helper method will return the end of the next word in the buffer.
     */
    private static int getNextWordEnd(Segment text, int startPos) {
        for (char ch = text.setIndex(startPos); ch != Segment.DONE; ch = text
                .next()) {
            if (!Character.isLetterOrDigit(ch)) {
                if (ch == '-' || ch == '\'') { // Handle ' and - inside words
                    char ch2 = text.next();
                    text.previous();
                    if (ch2 != Segment.DONE && Character.isLetterOrDigit(ch2)) {
                        continue;
                    }
                }
                return text.getIndex();
            }
        }
        return text.getEndIndex();
    }

    /**
     * Indicates if there are more words left.
     * 
     * @return true if more words can be found in the text.
     */
    @Override
    public boolean hasMoreWords() {
        return moreTokens;
    }

    /**
     * Sets the current word position at the start of the word containing the
     * char at position pos. This way a call to nextWord() will return this
     * word.
     * 
     * @param pos Position in the word we want to set as current.
     */
    public void posStartFullWordFrom(int pos) {
        currentWordPos = text.getBeginIndex();
        if (pos > text.getEndIndex()) {
            pos = text.getEndIndex();
        }
        for (char ch = text.setIndex(pos); ch != Segment.DONE; ch = text
                .previous()) {
            if (!Character.isLetterOrDigit(ch)) {
                if (ch == '-' || ch == '\'') { // Handle ' and - inside words
                    char ch2 = text.previous();
                    text.next();
                    if (ch2 != Segment.DONE && Character.isLetterOrDigit(ch2)) {
                        continue;
                    }
                }
                currentWordPos = text.getIndex() + 1;
                break;
            }
        }
        if (currentWordPos == 0) {
            first = true;
        }
        moreTokens = true;
        currentWordEnd = getNextWordEnd(text, currentWordPos);
        nextWordPos = getNextWordStart(text, currentWordEnd + 1);
    }

    /**
     * Returns the number of word tokens that have been processed thus far.
     * 
     * @return The number of words found so far.
     */
    @Override
    public int getCurrentWordPosition() {
        return currentWordPos - text.offset;
    }

    /**
     * Returns an index representing the end location of the current word in the
     * text.
     * 
     * @return Index of the end of the current word in the text.
     */
    @Override
    public int getCurrentWordEnd() {
        return currentWordEnd - text.offset;
    }

    /**
     * This returns the next word in the iteration. Note that any implementation
     * should return the current word and then replace the current word with the
     * next word found in the input text (if one exists).
     * 
     * @return The next word in the iteration.
     */
    @Override
    public String nextWord() {
        if (!first) {
            currentWordPos = nextWordPos;
            currentWordEnd = getNextWordEnd(text, currentWordPos);
            nextWordPos = getNextWordStart(text, currentWordEnd + 1);
        }
        int current = sentenceIterator.current();
        if (current == currentWordPos) {
            startsSentence = true;
        } else {
            startsSentence = false;
            if (currentWordEnd > current) {
                sentenceIterator.next();
            }
        }
        // The nextWordPos has already been populated
        String word = null;
        try {
            int offs = currentWordPos - text.offset;
            word = document.getText(offs, currentWordEnd - currentWordPos);
        } catch (BadLocationException ex) {
            ExceptionDialog.hideException(ex);
            moreTokens = false;
        }
        wordCount++;
        first = false;
        if (nextWordPos == -1) {
            moreTokens = false;
        }
        return word;
    }

    /**
     * Returns the number of word tokens that have been processed thus far.
     * 
     * @return The number of words found so far.
     */
    @Override
    public int getCurrentWordCount() {
        return wordCount;
    }

    /**
     * Replaces the current word token.
     * 
     * @param newWord The new word to replace the misspelled one.
     */
    @Override
    public void replaceWord(String newWord) {
        if (currentWordPos != -1) {
            try {
                document.remove(currentWordPos, currentWordEnd - currentWordPos);
                document.insertString(currentWordPos, newWord, null);
                // Need to reset the segment
                document.getText(0, document.getLength(), text);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex.getMessage());
            }
            // Position after the newly replaced word(s)
            first = true;
            currentWordPos = getNextWordStart(text,
                    currentWordPos + newWord.length());
            if (currentWordPos != -1) {
                currentWordEnd = getNextWordEnd(text, currentWordPos);
                nextWordPos = getNextWordStart(text, currentWordEnd);
                sentenceIterator.setText(text);
                sentenceIterator.following(currentWordPos);
            } else {
                moreTokens = false;
            }
        }
    }

    /**
     * Returns the current text that is being tokenized (includes any changes
     * that have been made).
     * 
     * @return The text, including changes.
     */
    @Override
    public String getContext() {
        return text.toString();
    }

    /** @return true if the current word is at the start of a sentence. */
    @Override
    public boolean isNewSentence() {
        // BreakIterator doesn't work when the first word in a sentence is not
        // capitalized, but we need to check for capitalization
        if (startsSentence || currentWordPos < 2) {
            return (true);
        }

        String textBefore = null;
        try {
            textBefore = document.getText(currentWordPos - 2, 2);
        } catch (BadLocationException ex) {
            return (false);
        }
        return (textBefore != null && ".".equals(textBefore.trim()));
    }
}