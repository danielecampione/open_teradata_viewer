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

import javax.swing.text.JTextComponent;

/**
 * A completion for a variable (or constant) in a programming language.<p>
 *
 * This completion type uses its <tt>shortDescription</tt> property as part of
 * its summary returned by {@link #getSummary()}; for this reason, it may be a
 * little longer (even much longer), if desired, than what is recommended for
 * <tt>BasicCompletion</tt>s (where the <tt>shortDescription</tt> is used in
 * {@link #toString()} for <tt>ListCellRenderers</tt>).
 *
 * @author D. Campione
 * 
 */
public class VariableCompletion extends BasicCompletion {

    /** The variable's type. */
    private String type;

    /** What library (for example) this variable is defined in. */
    private String definedIn;

    /**
     * Ctor.
     *
     * @param provider The parent provider.
     * @param name The name of this variable.
     * @param type The type of this variable (e.g. "<code>int</code>",
     *        "<code>String</code>", etc..).
     */
    public VariableCompletion(ICompletionProvider provider, String name,
            String type) {
        super(provider, name);
        this.type = type;
    }

    protected void addDefinitionString(StringBuilder sb) {
        sb.append("<html><b>").append(getDefinitionString()).append("</b>");
    }

    public String getDefinitionString() {
        StringBuilder sb = new StringBuilder();

        // Add the return type if applicable (C macros like NULL have no type)
        if (type != null) {
            sb.append(type).append(' ');
        }

        // Add the item being described's name
        sb.append(getName());

        return sb.toString();
    }

    /**
     * Returns where this variable is defined.
     *
     * @return Where this variable is defined.
     * @see #setDefinedIn(String)
     */
    public String getDefinedIn() {
        return definedIn;
    }

    /** @return The name of this variable. */
    public String getName() {
        return getReplacementText();
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        addDefinitionString(sb);
        possiblyAddDescription(sb);
        possiblyAddDefinedIn(sb);
        return sb.toString();
    }

    /**
     * Returns the tool tip text to display for mouse hovers over this
     * completion.<p>
     *
     * Note that for this functionality to be enabled, a <tt>JTextComponent</tt>
     * must be registered with the <tt>ToolTipManager</tt>, and the text
     * component must know to search for this value. In the case of an
     * <tt>OTVSyntaxTextArea</tt>, this can be done with a
     * <tt>net.sourceforge.open_teradata_viewer.editor.IToolTipSupplier</tt>
     * that calls into
     * {@link ICompletionProvider#getCompletionsAt(JTextComponent,
     * java.awt.Point)}.
     *
     * @return The tool tip text for this completion, or <code>null</code> if
     *         none.
     */
    @Override
    public String getToolTipText() {
        return getDefinitionString();
    }

    /**
     * Returns the type of this variable.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Adds some HTML describing where this variable is defined, if this
     * information is known.
     *
     * @param sb The buffer to append to.
     */
    protected void possiblyAddDefinedIn(StringBuilder sb) {
        if (definedIn != null) {
            sb.append("<hr>Defined in:");
            sb.append(" <em>").append(definedIn).append("</em>");
        }
    }

    /**
     * Adds the description text as HTML to a buffer, if a description is
     * defined.
     *
     * @param sb The buffer to append to.
     * @return Whether there was a description to add.
     */
    protected boolean possiblyAddDescription(StringBuilder sb) {
        if (getShortDescription() != null) {
            sb.append("<hr><br>");
            sb.append(getShortDescription());
            sb.append("<br><br><br>");
            return true;
        }
        return false;
    }

    /**
     * Sets where this variable is defined.
     *
     * @param definedIn Where this variable is defined.
     * @see #getDefinedIn()
     */
    public void setDefinedIn(String definedIn) {
        this.definedIn = definedIn;
    }

    /**
     * Overridden to return the name of the variable being completed.
     *
     * @return A string representation of this completion.
     */
    @Override
    public String toString() {
        return getName();
    }
}