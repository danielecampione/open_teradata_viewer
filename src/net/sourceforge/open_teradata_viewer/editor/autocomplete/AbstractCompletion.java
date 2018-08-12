/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

/**
 * Base class for possible completions. Most, if not all, {@link ICompletion}
 * implementations can extend this class. It remembers the
 * <tt>ICompletionProvider</tt> that returns this completion, and also implements
 * <tt>Comparable</tt>, allowing such completions to be compared
 * lexicographically (ignoring case).<p>
 *
 * This implementation assumes the input text and replacement text are the same
 * value. It also returns the input text from its {@link #toString()} method
 * (which is what <code>DefaultListCellRenderer</code> uses to render objects).
 * Subclasses that wish to override any of this behavior can simply override the
 * corresponding method(s) needed to do so.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractCompletion implements ICompletion {

    /** The provider that created this completion. */
    private ICompletionProvider provider;

    /**
     * The relevance of this completion. ICompletion instances with higher
     * "relevance" values are inserted higher into the list of possible
     * completions than those with lower values. ICompletion instances with
     * equal relevance values are sorted alphabetically.
     */
    private int relevance;

    /**
     * Ctor.
     *
     * @param provider The provider that created this completion.
     */
    public AbstractCompletion(ICompletionProvider provider) {
        this.provider = provider;
    }

    /** {@inheritDoc} */
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        } else if (o instanceof ICompletion) {
            ICompletion c2 = (ICompletion) o;
            return toString().compareToIgnoreCase(c2.toString());
        }
        return -1;
    }

    /** {@inheritDoc} */
    public String getAlreadyEntered(JTextComponent comp) {
        return provider.getAlreadyEnteredText(comp);
    }

    /**
     * The default implementation returns <code>null</code>. Subclasses who wish
     * to display an icon can override this method.
     *
     * @return The icon for this completion.
     */
    public Icon getIcon() {
        return null;
    }

    /**
     * Returns the text the user has to (start) typing for this completion to be
     * offered. The default implementation simply returns {@link
     * #getReplacementText()}.
     *
     * @return The text the user has to (start) typing for this completion.
     * @see #getReplacementText()
     */
    public String getInputText() {
        return getReplacementText();
    }

    /** {@inheritDoc} */
    public ICompletionProvider getProvider() {
        return provider;
    }

    /** {@inheritDoc} */
    public int getRelevance() {
        return relevance;
    }

    /**
     * The default implementation returns <code>null</code>. Subclasses can
     * override this method.
     *
     * @return The tool tip text.
     */
    public String getToolTipText() {
        return null;
    }

    /**
     * Sets the relevance of this completion.
     *
     * @param relevance The new relevance of this completion.
     * @see #getRelevance()
     */
    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    /**
     * Returns a string representation of this completion. The default
     * implementation returns {@link #getInputText()}.
     *
     * @return A string representation of this completion.
     */
    public String toString() {
        return getInputText();
    }
}