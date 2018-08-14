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

/**
 * A word finder Java source files, which searches text for sequences of letters
 * formated as Java comments.
 *
 * @author D. Campione
 * 
 */
public class JavaWordFinder extends AbstractWordFinder {

    //~ Instance/static variables ...............................................

    private boolean inComment;

    //~ Constructors ............................................................

    /**
     * Creates a new JavaWordFinder object.
     *
     * @param inText the String to search.
     */
    public JavaWordFinder(String inText) {
        super(inText);
    }

    /** Creates a new JavaWordFinder object. */
    public JavaWordFinder() {
        super();
    }

    //~ Methods .................................................................

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
        boolean started = false;

        search: while (i < text.length() && !finished) {
            i = ignore(i, '@');
            i = ignore(i, "<code>", "</code>");
            i = ignore(i, "<CODE>", "</CODE>");
            i = ignore(i, '<', '>');

            if (i >= text.length()) {
                break search;
            }

            char currentLetter = text.charAt(i);
            if (inComment) {
                // Reset on new line
                if (currentLetter == '\n') {
                    inComment = false;
                    i++;
                    continue search;
                } else if (!isWordChar(i)) {
                    i++;
                    continue search;
                }
                // Find words
                while (i < text.length() - 1) {
                    if (!started && isWordChar(i)) {
                        nextWord.setStart(i);
                        started = true;
                    } else if (started && !isWordChar(i)) {
                        nextWord.setText(text.substring(nextWord.getStart(), i));
                        finished = true;
                        break search;
                    }

                    currentLetter = text.charAt(++i);
                }
            } else if (currentLetter == '*') {
                inComment = true;
                i++;
            } else {
                i++;
            }
        }

        if (!started) {
            nextWord = null;
        } else if (!finished) {
            nextWord.setText(text.substring(nextWord.getStart(), i));
        }

        return currentWord;
    }

    /** Initializes this word finder. */
    @Override
    protected void init() {
        super.init();
        inComment = false;
    }
}