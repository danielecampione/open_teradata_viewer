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
 * A basic word finder, which searches text for sequences of letters.
 * 
 * @author D. Campione
 * 
 */
public class DefaultWordFinder extends AbstractWordFinder {

    //~ Instance/static variables ..............................................

    //~ Constructors ...........................................................

    /**
     * Creates a new DefaultWordFinder object.
     * 
     * @param inText The String to search.
     */
    public DefaultWordFinder(String inText) {
        super(inText);
    }

    /** Creates a new DefaultWordFinder object. */
    public DefaultWordFinder() {
        super();
    }

    //~ Methods ................................................................

    /**
     * This method scans the text from the end of the last word and returns a
     * new Word object corresponding to the next word.
     * 
     * @return The next word.
     * @throws WordNotFoundException Search string contains no more words.
     */
    @Override
    public Word next() {
        if (nextWord == null) {
            throw new WordNotFoundException("No more words found.");
        }
        currentWord.copy(nextWord);
        setSentenceIterator(currentWord);

        int i = currentWord.getEnd();
        boolean finished = false;

        while (i < text.length() && !finished) {
            if (isWordChar(i)) {
                nextWord.setStart(i);
                int end = getNextWordEnd(text, i);
                nextWord.setText(text.substring(i, end));
                finished = true;
            }
            i++;
        }
        if (!finished) {
            nextWord = null;
        }

        return currentWord;
    }

    /**
     * Returns the position in the string <em>after</em> the end of the next
     * word.
     * Note that this return value should not be used as an index into the
     * string without checking first that it is in range, since it is possible
     * for the value <code>text.length()</code> to be returned by this method.
     */
    private int getNextWordEnd(String text, int startPos) {
        // If we're dealing with a possible 'internet word' we need to provide
        // some special handling
        if (SpellChecker.beginsAsINETWord(text.substring(startPos))) {
            for (int i = startPos; i < text.length(); i++) {
                char ch = text.charAt(i);
                if (Character.isLetterOrDigit(ch)) {
                    continue;
                }

                if (ch == '\r' || ch == '\n') {
                    return i;
                }
                // Chop off any characters that might be enclosing the 'internet word'. eg ',",),]
                if (Character.isSpaceChar(ch)) {
                    if (i > 0 && Character.isLetterOrDigit(text.charAt(i - 1))) {
                        return i;
                    } else {
                        return i - 1;
                    }
                }
            }
            return text.length();
        } else {
            for (int i = startPos; i < text.length(); i++) {
                if (!isWordChar(i)) {
                    return i;
                }
            }
            return text.length();
        }
    }
}