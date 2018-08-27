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

import java.awt.Color;

/**
 * Manages the colors shared across the library.
 *
 * @author D. Campione
 * 
 */
public class AutoCompletionStyleContext {

    /**
     * The color used to denote the ending caret position for parameterized
     * completions.
     */
    private Color parameterizedCompletionCursorPositionColor;

    /**
     * The color used to highlight copies of editable parameters in
     * parameterized completions.
     */
    private Color parameterCopyColor;

    /**
     * The color of the outline highlight used to denote editable parameters in
     * parameterized completions.
     */
    private Color parameterOutlineColor;

    public AutoCompletionStyleContext() {
        setParameterOutlineColor(Color.gray);
        setParameterCopyColor(new Color(0xb4d7ff));
        setParameterizedCompletionCursorPositionColor(new Color(0x00b400));
    }

    /**
     * Returns the color of the highlight painted on copies of editable
     * parameters in parameterized completions.
     *
     * @return The color used.
     * @see #setParameterCopyColor(Color)
     */
    public Color getParameterCopyColor() {
        return parameterCopyColor;
    }

    /**
     * Returns the color used to denote the ending caret position for
     * parameterized completions.
     *
     * @return The color used.
     * @see #setParameterizedCompletionCursorPositionColor(Color)
     */
    public Color getParameterizedCompletionCursorPositionColor() {
        return parameterizedCompletionCursorPositionColor;
    }

    /**
     * Returns the color of the outline highlight used to denote editable
     * parameters in parameterized completions.
     *
     * @return The color used.
     * @see #setParameterOutlineColor(Color)
     */
    public Color getParameterOutlineColor() {
        return parameterOutlineColor;
    }

    /**
     * Sets the color of the highlight painted on copies of editable
     * parameters in parameterized completions.
     *
     * @param color The color to use.
     * @see #setParameterCopyColor(Color)
     */
    public void setParameterCopyColor(Color color) {
        this.parameterCopyColor = color;
    }

    /**
     * Sets the color used to denote the ending caret position for parameterized
     * completions.
     *
     * @param color The color to use.
     * @see #getParameterizedCompletionCursorPositionColor()
     */
    public void setParameterizedCompletionCursorPositionColor(Color color) {
        this.parameterizedCompletionCursorPositionColor = color;
    }

    /**
     * Sets the color of the outline highlight used to denote editable
     * parameters in parameterized completions.
     *
     * @param color The color to use.
     * @see #getParameterOutlineColor()
     */
    public void setParameterOutlineColor(Color color) {
        this.parameterOutlineColor = color;
    }
}