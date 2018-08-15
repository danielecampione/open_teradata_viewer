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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Container for various methods that any <code>ISpellDictionary</code> will
 * use.
 * This class is based on the original Jazzy aspell port.<p/>
 *
 * Derived classes will need words list files as spell checking reference. 
 * Words list file is a dictionary with one word per line. There are many open
 * source dictionary files.<p/>
 *
 * You can choose words lists form <a href="http://aspell.net/">aspell</a> 
 * many differents languages dictionaries. To grab some, install
 * <code>aspell</code> and the dictionaries you require. Then run aspell
 * specifying the name of the dictionary and the words list file to dump it
 * into, for example:
 * <pre>
 * aspell --master=fr-40 dump master > fr-40.txt
 * </pre>
 * Note: the number following the language is the size indicator. A bigger
 * number gives a more extensive language coverage. Size 40 is more than
 * adequate for many usages.<p/>
 *
 * For some languages, Aspell can also supply you with the phonetic file. 
 * On Windows, go into aspell <code>data</code> directory and copy the  phonetic
 * file corresponding to your language, for example the
 * <code>fr_phonet.dat</code> for the <code>fr</code> language. The phonetic
 * file should be in directory <code>/usr/share/aspell</code> on Unix.
 *
 * @author D. Campione
 * @see GenericTransformator GenericTransformator for information on phonetic
 *      files.
 */
public abstract class SpellDictionaryASpell implements ISpellDictionary {

    /**
     * The reference to a ITransformator, used to transform a word into it's
     * phonetic code.
     */
    protected ITransformator tf;

    /**
     * Constructs a new SpellDictionaryASpell.
     * 
     * @param phonetic The file to use for phonetic transformation of the words
     *        list. If <code>phonetic</code> is null, the transformation uses
     *        {@link DoubleMeta} transformation.
     * @throws java.io.IOException Indicates problems reading the phonetic
     *         information.
     */
    public SpellDictionaryASpell(File phonetic) throws IOException {
        if (phonetic == null) {
            tf = new DoubleMeta();
        } else {
            tf = new GenericTransformator(phonetic);
        }
    }

    /**
     * Constructs a new SpellDictionaryASpell.
     * 
     * @param phonetic The file to use for phonetic transformation of the words
     *        list. If <code>phonetic</code> is null, the transformation uses
     *        {@link DoubleMeta} transformation.
     * @param encoding Uses the character set encoding specified.
     * @throws java.io.IOException Indicates problems reading the phonetic 
     *         information.
     */
    public SpellDictionaryASpell(File phonetic, String encoding)
            throws IOException {
        if (phonetic == null) {
            tf = new DoubleMeta();
        } else {
            tf = new GenericTransformator(phonetic, encoding);
        }
    }

    /**
     * Constructs a new SpellDictionaryASpell.
     * 
     * @param phonetic The Reader to use for phonetic transformation of the
     *        words list. If <code>phonetic</code> is null, the transformation
     *        uses {@link DoubleMeta} transformation.
     * @throws java.io.IOException Indicates problems reading the phonetic
     *         information.
     */
    public SpellDictionaryASpell(Reader phonetic) throws IOException {
        if (phonetic == null) {
            tf = new DoubleMeta();
        } else {
            tf = new GenericTransformator(phonetic);
        }
    }

    /**
     * Returns a list of Word objects that are the suggestions to an incorrect
     * word.<p/>
     *
     * This method is only needed to provide backward compatibility.
     * 
     * @see #getSuggestions(String, int, int[][])
     * @param word Suggestions for given misspelt word.
     * @param threshold The lower boundary of similarity to misspelt word.
     * @return List A List of suggestions.
     */
    @Override
    public List getSuggestions(String word, int threshold) {
        return getSuggestions(word, threshold, null);
    }

    /**
     * Returns a list of Word objects that are the suggestions to an incorrect
     * word.<p/>
     * 
     * @param word Suggestions for given misspelt word.
     * @param threshold The lower boundary of similarity to misspelt word.
     * @param matrix Two dimensional int array used to calculate edit distance.
     *        Allocating this memory outside of the function will greatly
     *        improve efficiency.
     * @return List A List of suggestions.
     */
    @Override
    public List getSuggestions(String word, int threshold, int[][] matrix) {
        int i;
        int j;

        if (matrix == null) {
            matrix = new int[0][0];
        }

        Hashtable nearmisscodes = new Hashtable();
        String code = getCode(word);

        // Add all words that have the same phonetics
        nearmisscodes.put(code, code);
        Vector phoneticList = getWordsFromCode(word, nearmisscodes);

        // Do some transformations to pick up more results.
        // Interchange
        nearmisscodes = new Hashtable();
        char[] charArray = word.toCharArray();
        char a;
        char b;

        for (i = 0; i < word.length() - 1; i++) {
            a = charArray[i];
            b = charArray[i + 1];
            charArray[i] = b;
            charArray[i + 1] = a;
            String s = getCode(new String(charArray));
            nearmisscodes.put(s, s);
            charArray[i] = a;
            charArray[i + 1] = b;
        }

        char[] replacelist = tf.getReplaceList();

        // Change
        charArray = word.toCharArray();
        char original;
        for (i = 0; i < word.length(); i++) {
            original = charArray[i];
            for (j = 0; j < replacelist.length; j++) {
                charArray[i] = replacelist[j];
                String s = getCode(new String(charArray));
                nearmisscodes.put(s, s);
            }
            charArray[i] = original;
        }

        // Add
        charArray = (word += " ").toCharArray();
        int iy = charArray.length - 1;
        while (true) {
            for (j = 0; j < replacelist.length; j++) {
                charArray[iy] = replacelist[j];
                String s = getCode(new String(charArray));
                nearmisscodes.put(s, s);
            }
            if (iy == 0) {
                break;
            }
            charArray[iy] = charArray[iy - 1];
            --iy;
        }

        // Delete
        word = word.trim();
        charArray = word.toCharArray();
        char[] charArray2 = new char[charArray.length - 1];
        for (int ix = 0; ix < charArray2.length; ix++) {
            charArray2[ix] = charArray[ix];
        }

        a = charArray[charArray.length - 1];
        int ii = charArray2.length;
        while (true) {
            String s = getCode(new String(charArray));
            nearmisscodes.put(s, s);
            if (ii == 0) {
                break;
            }
            b = a;
            a = charArray2[ii - 1];
            charArray2[ii - 1] = b;
            --ii;
        }

        nearmisscodes.remove(code); // Already accounted for in phoneticList

        Vector wordlist = getWordsFromCode(word, nearmisscodes);

        if (wordlist.size() == 0 && phoneticList.size() == 0) {
            addBestGuess(word, phoneticList, matrix);
        }

        // We sort a Vector at the end instead of maintaining a continuously
        // sorted TreeSet because every time you add a collection to a tree set
        // it has to be resorted. It's better to do this operation once at the
        // end

        Collections.sort(phoneticList, new Word()); // Always sort phonetic matches along the top
        Collections.sort(wordlist, new Word()); // The non-phonetic matches can be listed below

        phoneticList.addAll(wordlist);
        return phoneticList;
    }

    /**
     * When we don't come up with any suggestions (probably because the
     * threshold was too strict), then pick the best guesses from the those
     * words that have the same phonetic code.
     * 
     * @param word The word we are trying spell correct. 
     * @param wordList The linked list that will get the best guess.
     * @param matrix Two dimensional array of int used to calculate edit
     *        distance. Allocating this memory outside of the function will
     *        greatly improve efficiency.
     */
    private void addBestGuess(String word, Vector wordList, int[][] matrix) {
        if (matrix == null) {
            matrix = new int[0][0];
        }

        if (wordList.size() != 0) {
            throw new InvalidParameterException(
                    "the wordList vector must be empty");
        }

        int bestScore = Integer.MAX_VALUE;

        String code = getCode(word);
        List simwordlist = getWords(code);

        LinkedList candidates = new LinkedList();

        for (Iterator j = simwordlist.iterator(); j.hasNext();) {
            String similar = (String) j.next();
            int distance = EditDistance.getDistance(word, similar, matrix);
            if (distance <= bestScore) {
                bestScore = distance;
                Word goodGuess = new Word(similar, distance);
                candidates.add(goodGuess);
            }
        }

        // Now, only pull out the guesses that had the best score
        for (Iterator iter = candidates.iterator(); iter.hasNext();) {
            Word candidate = (Word) iter.next();
            if (candidate.getCost() == bestScore) {
                wordList.add(candidate);
            }
        }
    }

    private Vector getWordsFromCode(String word, Hashtable codes) {
        Configuration config = Configuration.getConfiguration();
        Vector result = new Vector();
        int[][] matrix = new int[0][0];
        final int configDistance = config
                .getInteger(Configuration.SPELL_THRESHOLD);

        for (Enumeration i = codes.keys(); i.hasMoreElements();) {
            String code = (String) i.nextElement();

            List simwordlist = getWords(code);
            for (Iterator iter = simwordlist.iterator(); iter.hasNext();) {
                String similar = (String) iter.next();
                int distance = EditDistance.getDistance(word, similar, matrix);
                if (distance < configDistance) {
                    Word w = new Word(similar, distance);
                    result.addElement(w);
                }
            }
        }
        return result;
    }

    /**
     * Returns the phonetic code representing the word.
     * 
     * @param word The word we want the phonetic code.
     * @return The value of the phonetic code for the word.
     */
    public String getCode(String word) {
        return tf.transform(word);
    }

    /**
     * Returns a list of words that have the same phonetic code.
     * 
     * @param phoneticCode The phonetic code common to the list of words.
     * @return A list of words having the same phonetic code.
     */
    protected abstract List getWords(String phoneticCode);

    /**
     * Returns true if the word is correctly spelled against the current word
     * list.
     */
    @Override
    public boolean isCorrect(String word) {
        List possible = getWords(getCode(word));
        if (possible.contains(word)) {
            return true;
        } else if (possible.contains(word.toLowerCase())) {
            return true;
        }
        return false;
    }
}