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
 * This class tokenizes a input string.
 *
 * <p>It also allows for the string to be altered by calls to replaceWord(). The
 * result after the spell checking is completed is available to the call to
 * getContext.</p>
 *
 * @author D. Campione
 * 
 */
public class StringWordTokenizer extends AbstractWordTokenizer {

    //~ Constructors ............................................................

    /**
     * Creates a new StringWordTokenizer object.
     *
     * @param s The string to tokenize.
     */
    public StringWordTokenizer(String s) {
        super(s);
    }

    /**
     * Creates a new StringWordTokenizer object.
     *
     * @param wf The custom IWordFinder to use in tokenizing. Note that the
     *        string to tokenize will be encapsulated within the IWordFinder.
     */
    public StringWordTokenizer(IWordFinder wf) {
        super(wf);
    }

    /**
     * Creates a new StringWordTokenizer object.
     * 
     * @param s The string to work on.
     * @param finder The custom IWordFinder to use in tokenizing. Note that the
     *        string to tokenize will be encapsulated within the IWordFinder.
     */
    public StringWordTokenizer(String s, IWordFinder finder) {
        super(finder);
        finder.setText(s);
    }

    //~ Methods .................................................................

    /**
     * @deprecated Use getContext() instead as per the IWordTokenizer interface
     *             specification.
     * @return The final text.
     */
    @Deprecated
    public String getFinalText() {
        return getContext();
    }

    /**
     * Replace the current word in the iteration with the String s.
     *
     * @param s The String to replace the current word.
     * @throws WordNotFoundException current word not yet set.
     */
    @Override
    public void replaceWord(String s) {
        finder.replace(s);
    }
}