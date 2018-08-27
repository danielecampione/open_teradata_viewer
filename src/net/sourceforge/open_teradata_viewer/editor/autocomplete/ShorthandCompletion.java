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

/**
 * A completion where the input text is shorthand for (really, just different
 * than) the actual text to be inserted. For example, the input text
 * "<code>sysout</code>" could be associated with the completion
 * "<code>System.out.println(</code>" in Java.
 *
 * @author D. Campione
 * 
 */
public class ShorthandCompletion extends BasicCompletion {

    /** The text the user can start typing that will match this completion. */
    private String inputText;

    /**
     * Ctor.
     *
     * @param provider The provider that returns this completion.
     * @param inputText The text the user inputs to get this completion.
     * @param replacementText The replacement text of the completion.
     */
    public ShorthandCompletion(ICompletionProvider provider, String inputText,
            String replacementText) {
        super(provider, replacementText);
        this.inputText = inputText;
    }

    /**
     * Ctor.
     *
     * @param provider The provider that returns this completion.
     * @param inputText The text the user inputs to get this completion.
     * @param replacementText The replacement text of the completion.
     * @param shortDesc A short description of the completion. This will be
     *        displayed in the completion list. This may be <code>null</code>.
     */
    public ShorthandCompletion(ICompletionProvider provider, String inputText,
            String replacementText, String shortDesc) {
        super(provider, replacementText, shortDesc);
        this.inputText = inputText;
    }

    /**
     * Ctor.
     *
     * @param provider The provider that returns this completion.
     * @param inputText The text the user inputs to get this completion.
     * @param replacementText The replacement text of the completion.
     * @param shortDesc A short description of the completion. This will be
     *        displayed in the completion list. This may be <code>null</code>.
     * @param summary The summary of this completion. This should be HTML. This
     *        may be <code>null</code>.
     */
    public ShorthandCompletion(ICompletionProvider provider, String inputText,
            String replacementText, String shortDesc, String summary) {
        super(provider, replacementText, shortDesc, summary);
        this.inputText = inputText;
    }

    /**
     * Returns the text the user must start typing to get this completion.
     *
     * @return The text the user must start to input.
     */
    @Override
    public String getInputText() {
        return inputText;
    }

    /**
     * If a summary has been set, that summary is returned. Otherwise, the
     * replacement text is returned.
     *
     * @return A description of this completion (the text that will be
     *         inserted).
     * @see #getReplacementText()
     */
    @Override
    public String getSummary() {
        String summary = super.getSummary();
        return summary != null ? summary : ("<html><body>" + getSummaryBody());
    }

    /**
     * Returns the "body" of the HTML returned by {@link #getSummary()} when no
     * summary text has been set. This is defined to return the replacement text
     * in a monospaced font.
     *
     * @return The summary text's body, if no other summary has been defined.
     * @see #getReplacementText()
     */
    protected String getSummaryBody() {
        return "<code>" + getReplacementText();
    }
}