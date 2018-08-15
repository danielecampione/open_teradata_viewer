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

/**
 * An interface for all ITransformators - which take a dictionary word and
 * converts into its phonetic hash. These phonetic hashes are useful for
 * determining what other words are similar to it and then list those words as
 * suggestions.
 *
 * @author D. Campione
 * 
 */
public interface ITransformator {

    /**
     * Take the given word and return the best phonetic hash for it.
     * 
     * @param word The word to transform.
     * @return The phonetic transformation of the word.
     */
    public String transform(String word);

    /**
     * Gets the list of characters that should be swapped in to the misspelled
     * word in order to try to find more suggestions.
     * In general, this list represents all of the unique phonetic characters
     * for this ITransformator.<p/>
     *
     * The replace list is used in the getSuggestions method.
     * All of the letters in the misspelled word are replaced with the
     * characters from this list to try and generate more suggestions, which
     * implies l*n tries, if l is the size of the string, and n is the size of
     * this list.<p/>
     *
     * In addition to that, each of these letters is added to the misspelled
     * word.<p/>
     * 
     * @return char[] Misspelled words should try replacing with these
     *         characters to get more suggestions.
     */
    public char[] getReplaceList();
}