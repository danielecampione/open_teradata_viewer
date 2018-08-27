/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * A base class for all standard completion providers. This class implements
 * functionality that should be sharable across all <tt>ICompletionProvider</tt>
 * implementations.
 *
 * @author D. Campione
 * @see AbstractCompletionProvider
 */
public abstract class CompletionProviderBase implements ICompletionProvider {

    /** The parent completion provider. */
    private ICompletionProvider parent;

    /**
     * The renderer to use for completions from this provider. If this is
     * <code>null</code>, a default renderer is used.
     */
    private ListCellRenderer listCellRenderer;

    /** Text that marks the beginning of a parameter list, for example, '('. */
    private char paramListStart;

    /** Text that marks the end of a parameter list, for example, ')'. */
    private char paramListEnd;

    /** Text that separates items in a parameter list, for example, ", ". */
    private String paramListSeparator;

    /** Whether auto-activation should occur after letters. */
    private boolean autoActivateAfterLetters;

    /** Non-letter chars that should cause auto-activation to occur. */
    private String autoActivateChars;

    /**
     * Provides completion choices for a parameterized completion's parameters.
     */
    private IParameterChoicesProvider paramChoicesProvider;

    /** A segment to use for fast char access. */
    private Segment s = new Segment();

    protected static final String EMPTY_STRING = "";

    /**
     * Comparator used to sort completions by their relevance before sorting
     * them lexicographically.
     */
    private static final Comparator<ICompletion> sortByRelevanceComparator = new SortByRelevanceComparator();

    /** {@inheritDoc} */
    @Override
    public void clearParameterizedCompletionParams() {
        paramListEnd = paramListStart = 0;
        paramListSeparator = null;
    }

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> getCompletions(JTextComponent comp) {
        List<ICompletion> completions = getCompletionsImpl(comp);
        if (parent != null) {
            List<ICompletion> parentCompletions = parent.getCompletions(comp);
            if (parentCompletions != null) {
                completions.addAll(parentCompletions);
                Collections.sort(completions);
            }
        }

        // NOTE: We can't sort by relevance prior to this; we need to have
        // things alphabetical so we can easily narrow down completions to those
        // starting with what was already typed
        if (/*sortByRelevance*/true) {
            Collections.sort(completions, sortByRelevanceComparator);
        }

        return completions;
    }

    /**
     * Does the dirty work of creating a list of completions.
     *
     * @param comp The text component to look in.
     * @return The list of possible completions, or an empty list if there
     *         are none.
     */
    protected abstract List<ICompletion> getCompletionsImpl(JTextComponent comp);

    /** {@inheritDoc} */
    @Override
    public ListCellRenderer getListCellRenderer() {
        return listCellRenderer;
    }

    /** {@inheritDoc} */
    @Override
    public IParameterChoicesProvider getParameterChoicesProvider() {
        return paramChoicesProvider;
    }

    /** {@inheritDoc} */
    @Override
    public char getParameterListEnd() {
        return paramListEnd;
    }

    /** {@inheritDoc} */
    @Override
    public String getParameterListSeparator() {
        return paramListSeparator;
    }

    /** {@inheritDoc} */
    @Override
    public char getParameterListStart() {
        return paramListStart;
    }

    /** {@inheritDoc} */
    @Override
    public ICompletionProvider getParent() {
        return parent;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAutoActivateOkay(JTextComponent tc) {
        Document doc = tc.getDocument();
        char ch = 0;
        try {
            doc.getText(tc.getCaretPosition(), 1, s);
            ch = s.first();
        } catch (BadLocationException ble) { // Never happens
            ExceptionDialog.hideException(ble);
        }
        return (autoActivateAfterLetters && Character.isLetter(ch))
                || (autoActivateChars != null && autoActivateChars.indexOf(ch) > -1);
    }

    /**
     * Sets the characters that auto-activation should occur after. A Java
     * completion provider, for example, might want to set <code>others</code>
     * to "<code>.</code>", to allow auto-activation for members of an object.
     *
     * @param letters Whether auto-activation should occur after any letter.
     * @param others A string of (non-letter) chars that auto-activation should
     *        occur after. This may be <code>null</code>.
     */
    public void setAutoActivationRules(boolean letters, String others) {
        autoActivateAfterLetters = letters;
        autoActivateChars = others;
    }

    /**
     * Sets the param choices provider. This is used when a user code-completes
     * a parameterized completion, such as a function or method. For any
     * parameter to the function/method, this object can return possible
     * completions.
     *
     * @param pcp The parameter choices provider, or <code>null</code> for none.
     * @see #getParameterChoicesProvider()
     */
    public void setParameterChoicesProvider(IParameterChoicesProvider pcp) {
        paramChoicesProvider = pcp;
    }

    /** {@inheritDoc} */
    @Override
    public void setListCellRenderer(ListCellRenderer r) {
        listCellRenderer = r;
    }

    /** {@inheritDoc} */
    @Override
    public void setParameterizedCompletionParams(char listStart,
            String separator, char listEnd) {
        if (listStart < 0x20 || listStart == 0x7F) {
            throw new IllegalArgumentException("Invalid listStart");
        }
        if (listEnd < 0x20 || listEnd == 0x7F) {
            throw new IllegalArgumentException("Invalid listEnd");
        }
        if (separator == null || separator.length() == 0) {
            throw new IllegalArgumentException("Invalid separator");
        }
        paramListStart = listStart;
        paramListSeparator = separator;
        paramListEnd = listEnd;
    }

    /** {@inheritDoc} */
    @Override
    public void setParent(ICompletionProvider parent) {
        this.parent = parent;
    }
}