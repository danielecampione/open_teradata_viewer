/*
 * Open Teradata Viewer ( editor spell engine )
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

package net.sourceforge.open_teradata_viewer.editor.spell.engine;

/**
 * This class calculates how similar two words are.
 * If the words are identical, then the distance is 0. The more that the words
 * have in common, the lower the distance value.
 * The distance value is based on how many operations it takes to get from one
 * word to the other. Possible operations are swapping characters, adding a
 * character, deleting a character and substituting a character.
 * The resulting distance is the sum of these operations weighted by their cost,
 * which can be set in the Configuration object.
 * When there are multiple ways to convert one word into the other, the lowest
 * cost distance is returned.<br/>
 *
 * Another way to think about this: what are the cheapest operations that would
 * have to be done on the "original" word to end up with the "similar" word?
 * Each operation has a cost, and these are added up to get the distance.<br/>
 *
 * @author D. Campione
 * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#COST_REMOVE_CHAR
 * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#COST_INSERT_CHAR
 * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#COST_SUBST_CHARS
 * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#COST_SWAP_CHARS
 *
 */
public class EditDistance {

    /** Fetches the spell engine configuration properties. */
    public static final Configuration config = Configuration.getConfiguration();

    /** Get the weights for each possible operation. */
    static final int costOfDeletingSourceCharacter = config
            .getInteger(Configuration.COST_REMOVE_CHAR);
    static final int costOfInsertingSourceCharacter = config
            .getInteger(Configuration.COST_INSERT_CHAR);
    static final int costOfSubstitutingLetters = config
            .getInteger(Configuration.COST_SUBST_CHARS);
    static final int costOfSwappingLetters = config
            .getInteger(Configuration.COST_SWAP_CHARS);
    static final int costOfChangingCase = config
            .getInteger(Configuration.COST_CHANGE_CASE);

    /**
     * Evaluates the distance between two words.
     * 
     * @param word One word to evaluates.
     * @param similar The other word to evaluates.
     * @return A number representing how easy or complex it is to transform one
     *         word into a similar one.
     */
    public static final int getDistance(String word, String similar) {
        return getDistance(word, similar, null);
    }

    /**
     * Evaluates the distance between two words.
     * 
     * @param word One word to evaluates.
     * @param similar The other word to evaluates.
     * @return A number representing how easy or complex it is to transform one
     *         word into a similar one.
     */
    public static final int getDistance(String word, String similar,
            int[][] matrix) {
        // Allocate memory outside of the loops 
        int i;
        int j;
        int costOfSubst;
        int costOfSwap;
        int costOfDelete;
        int costOfInsertion;
        int costOfCaseChange;

        boolean isSwap;
        char sourceChar = 0;
        char otherChar = 0;

        int a_size = word.length() + 1;
        int b_size = similar.length() + 1;

        // Only allocate new memory if we need a bigger matrix 
        if (matrix == null || matrix.length < a_size
                || matrix[0].length < b_size) {
            matrix = new int[a_size][b_size];
        }

        matrix[0][0] = 0;

        for (i = 1; i != a_size; ++i) {
            matrix[i][0] = matrix[i - 1][0] + costOfInsertingSourceCharacter; // Initialize the first column
        }

        for (j = 1; j != b_size; ++j) {
            matrix[0][j] = matrix[0][j - 1] + costOfDeletingSourceCharacter; // Initalize the first row
        }

        for (i = 1; i != a_size; ++i) {
            sourceChar = word.charAt(i - 1);
            for (j = 1; j != b_size; ++j) {

                otherChar = similar.charAt(j - 1);
                if (sourceChar == otherChar) {
                    matrix[i][j] = matrix[i - 1][j - 1]; // No change required, so just carry the current cost up
                    continue;
                }

                costOfSubst = costOfSubstitutingLetters + matrix[i - 1][j - 1];
                // If needed, add up the cost of doing a swap
                costOfSwap = Integer.MAX_VALUE;

                isSwap = (i != 1) && (j != 1)
                        && sourceChar == similar.charAt(j - 2)
                        && word.charAt(i - 2) == otherChar;
                if (isSwap) {
                    costOfSwap = costOfSwappingLetters + matrix[i - 2][j - 2];
                }

                costOfDelete = costOfDeletingSourceCharacter + matrix[i][j - 1];
                costOfInsertion = costOfInsertingSourceCharacter
                        + matrix[i - 1][j];

                costOfCaseChange = Integer.MAX_VALUE;

                if (equalIgnoreCase(sourceChar, otherChar)) {
                    costOfCaseChange = costOfChangingCase
                            + matrix[i - 1][j - 1];
                }

                matrix[i][j] = minimum(costOfSubst, costOfSwap, costOfDelete,
                        costOfInsertion, costOfCaseChange);
            }
        }

        return matrix[a_size - 1][b_size - 1];
    }

    /** Checks to see if the two characters are equal ignoring case. */
    private static boolean equalIgnoreCase(char ch1, char ch2) {
        if (ch1 == ch2) {
            return true;
        } else {
            return (Character.toLowerCase(ch1) == Character.toLowerCase(ch2));
        }
    }

    static private int minimum(int a, int b, int c, int d, int e) {
        int mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        if (d < mi) {
            mi = d;
        }
        if (e < mi) {
            mi = e;
        }

        return mi;
    }
}