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
 * Represents a completion choice. A {@link ICompletionProvider} returns lists of
 * objects implementing this interface. A <tt>ICompletion</tt> contains the
 * following information:
 * 
 * <ul>
 *   <li>The text the user must (begin to) input for this to be a completion
 *       choice.
 *   <li>The text that will be filled in if the user chooses this completion.
 *       Note that often, this is the same as the text the user must (begin to)
 *       enter, but this doesn't have to be the case.
 *   <li>Summary HTML that describes this completion. This is information that
 *       can be displayed in a helper "tooltip"-style window beside the
 *       completion list. This may be <code>null</code>. It may also be lazily
 *       generated to cut down on memory usage.
 *   <li>The <tt>ICompletionProvider</tt> that returned this completion.
 *   <li>Tool tip text that can be displayed when a mouse hovers over this
 *       completion in a text component.
 * </ul>
 *
 * @author D. Campione
 * @see AbstractCompletion
 */
public interface ICompletion extends Comparable {

    /**
     * Compares this completion to another one lexicographically, ignoring
     * case.
     *
     * @param o Another completion instance.
     * @return How this completion compares to the other one.
     */
    public int compareTo(Object o);

    /**
     * Returns the portion of this completion that has already been entered
     * into the text component. The match is case-insensitive.<p>
     *
     * This is a convenience method for:
     * <code>getProvider().getAlreadyEnteredText(comp)</code>.
     *
     * @param comp The text component.
     * @return The already-entered portion of this completion.
     */
    public String getAlreadyEntered(JTextComponent comp);

    /**
     * Returns the icon to use for this completion.
     *
     * @return The icon, or <code>null</code> for none.
     */
    public Icon getIcon();

    /**
     * Returns the text that the user has to (start) typing for this completion
     * to be offered. Note that this will usually be the same value as {@link
     * #getReplacementText()}, but not always (a completion could be a way to
     * implement shorthand, for example, "<code>sysout</code>" mapping to
     * "<code>System.out.println(</code>").
     *
     * @return The text the user has to (start) typing for this completion to be
     *         offered.
     * @see #getReplacementText()
     */
    public String getInputText();

    /**
     * Returns the provider that returned this completion.
     *
     * @return The provider.
     */
    public ICompletionProvider getProvider();

    /**
     * Returns the "relevance" of this completion. This is used when sorting
     * completions by their relevance. It is an abstract concept that may mean
     * different things to different languages, and may depend on the context of
     * the completion.<p>
     *
     * By default, all completions have a relevance of <code>0</code>. The
     * higher the value returned by this method, the higher up in the list this
     * completion will be; the lower the value returned, the lower it will be.
     * <code>ICompletion</code>s with equal relevance values will be sorted
     * alphabetically.
     *
     * @return The relevance of this completion.
     */
    public int getRelevance();

    /**
     * Returns the text to insert as the result of this auto-completion. This is
     * the "complete" text, including any text that replaces what the user has
     * already typed.
     *
     * @return The replacement text.
     * @see #getInputText()
     */
    public String getReplacementText();

    /**
     * Returns the description of this auto-complete choice. This can be used in
     * a popup "description window."
     *
     * @return This item's description. This should be HTML. It may be
     *         <code>null</code> if there is no description for this completion.
     */
    public String getSummary();

    /**
     * Returns the tool tip text to display for mouse hovers over this
     * completion.<p>
     *
     * Note that for this functionality to be enabled, a <tt>JTextComponent</tt>
     * must be registered with the <tt>ToolTipManager</tt>, and the text
     * component must know to search for this value. In the case of an
     * <tt>OTVSyntaxTextArea</tt>, this can be done with a
     * <tt>net.sourceforge.open_teradata_viewer.editor.IToolTipSupplier</tt>
     * that calls into {@link
     * ICompletionProvider#getCompletionsAt(JTextComponent, java.awt.Point)}.
     *
     * @return The tool tip text for this completion, or <code>null</code> if
     *         none.
     */
    public String getToolTipText();
}