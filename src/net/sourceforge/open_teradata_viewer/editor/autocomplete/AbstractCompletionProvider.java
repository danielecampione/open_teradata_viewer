/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * A base class for completion providers. {@link ICompletion}s are kept in a
 * sorted list. To get the list of completions that match a given input, a
 * binary search is done to find the first matching completion, then all
 * succeeding completions that also match are also returned.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractCompletionProvider extends CompletionProviderBase {

    /**
     * The completions this provider is aware of. Subclasses should ensure that
     * this list is sorted alphabetically (case-insensitively).
     */
    protected List<ICompletion> completions;

    /** Compares a {@link ICompletion} against a String. */
    protected CaseInsensitiveComparator comparator;

    /** Ctor. */
    public AbstractCompletionProvider() {
        comparator = new CaseInsensitiveComparator();
        clearParameterizedCompletionParams();
    }

    /**
     * Adds a single completion to this provider. If you are adding multiple
     * completions to this provider, for efficiency reasons please consider
     * using {@link #addCompletions(List)} instead.
     *
     * @param c The completion to add.
     * @throws IllegalArgumentException If the completion's provider isn't this
     *         <tt>ICompletionProvider</tt>.
     * @see #addCompletions(List)
     * @see #removeCompletion(ICompletion)
     * @see #clear()
     */
    public void addCompletion(ICompletion c) {
        checkProviderAndAdd(c);
        Collections.sort(completions);
    }

    /**
     * Adds {@link ICompletion}s to this provider.
     *
     * @param completions The completions to add. This cannot be
     *        <code>null</code>.
     * @throws IllegalArgumentException If a completion's provider isn't this
     *         <tt>ICompletionProvider</tt>.
     * @see #addCompletion(ICompletion)
     * @see #removeCompletion(ICompletion)
     * @see #clear()
     */
    public void addCompletions(List<ICompletion> completions) {
        for (ICompletion c : completions) {
            checkProviderAndAdd(c);
        }
        Collections.sort(this.completions);
    }

    /**
     * Adds simple completions for a list of words.
     *
     * @param words The words.
     * @see BasicCompletion
     */
    protected void addWordCompletions(String[] words) {
        int count = words == null ? 0 : words.length;
        for (int i = 0; i < count; i++) {
            completions.add(new BasicCompletion(this, words[i]));
        }
        Collections.sort(completions);
    }

    protected void checkProviderAndAdd(ICompletion c) {
        if (c.getProvider() != this) {
            throw new IllegalArgumentException("Invalid ICompletionProvider");
        }
        completions.add(c);
    }

    /**
     * Removes all completions from this provider. This does not affect the
     * parent <tt>ICompletionProvider</tt>, if there is one.
     *
     * @see #addCompletion(ICompletion)
     * @see #addCompletions(List)
     * @see #removeCompletion(ICompletion)
     */
    public void clear() {
        completions.clear();
    }

    /**
     * Returns a list of <tt>ICompletion</tt>s in this provider with the
     * specified input text.
     *
     * @param inputText The input text to search for.
     * @return A list of {@link ICompletion}s, or <code>null</code> if there are
     *         no matching <tt>ICompletion</tt>s.
     */
    @SuppressWarnings("unchecked")
    public List<ICompletion> getCompletionByInputText(String inputText) {
        // Find any entry that matches this input text (there may be > 1)
        int end = Collections.binarySearch(completions, inputText, comparator);
        if (end < 0) {
            return null;
        }

        // There might be multiple entries with the same input text
        int start = end;
        while (start > 0
                && comparator.compare(completions.get(start - 1), inputText) == 0) {
            start--;
        }
        int count = completions.size();
        while (++end < count
                && comparator.compare(completions.get(end), inputText) == 0) {
            ;
        }

        return completions.subList(start, end); // (inclusive, exclusive)
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    protected List getCompletionsImpl(JTextComponent comp) {
        List<ICompletion> retVal = new ArrayList<ICompletion>();
        String text = getAlreadyEnteredText(comp);

        if (text != null) {
            int index = Collections.binarySearch(completions, text, comparator);
            if (index < 0) { // No exact match
                index = -index - 1;
            } else {
                // If there are several overloads for the function being
                // completed, Collections.binarySearch() will return the index
                // of one of those overloads, but we must return all of them, so
                // search backward until we find the first one
                int pos = index - 1;
                while (pos > 0
                        && comparator.compare(completions.get(pos), text) == 0) {
                    retVal.add(completions.get(pos));
                    pos--;
                }
            }

            while (index < completions.size()) {
                ICompletion c = completions.get(index);
                if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
                    retVal.add(c);
                    index++;
                } else {
                    break;
                }
            }
        }

        return retVal;
    }

    /**
     * Removes the specified completion from this provider. This method will not
     * remove completions from the parent provider, if there is one.
     *
     * @param c The completion to remove.
     * @return <code>true</code> if this provider contained the specified
     *         completion.
     * @see #clear()
     * @see #addCompletion(ICompletion)
     * @see #addCompletions(List)
     */
    public boolean removeCompletion(ICompletion c) {
        // Don't just call completions.remove(c) as it'll be a linear search
        int index = Collections.binarySearch(completions, c);
        if (index < 0) {
            return false;
        }
        completions.remove(index);
        return true;
    }

    /**
     * A comparator that compares the input text of a {@link ICompletion}
     * against a String lexicographically, ignoring case.
     * 
     * @author D. Campione
     * 
     */
    @SuppressWarnings("rawtypes")
    protected static class CaseInsensitiveComparator implements Comparator,
            Serializable {

        private static final long serialVersionUID = 5231161608905723621L;

        @Override
        public int compare(Object o1, Object o2) {
            ICompletion c = (ICompletion) o1;
            // o2.toString() needed to help compile with 1.6+
            return String.CASE_INSENSITIVE_ORDER.compare(c.getInputText(),
                    o2.toString());
        }
    }
}