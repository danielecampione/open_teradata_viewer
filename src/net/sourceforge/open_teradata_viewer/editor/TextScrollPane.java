/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollPane;

/**
 * An extension of <code>javax.swing.JScrollPane</code> that will only take
 * <code>TextArea</code>s for its view. This class has the ability to show:
 * <ul>
 *    <li>Line numbers
 *    <li>Per-line icons (for bookmarks, debugging breakpoints, error markers,
 *        etc..).
 *    <li>+/- icons to denote code folding regions.
 * </ul>
 *
 * The actual "meat" of these extras is contained in the {@link Gutter} class.
 * Each <code>TextScrollPane</code> has a <code>Gutter</code> instance that it
 * uses as its row header. The gutter is only made visible when one of its
 * features is being used (line numbering, folding, and/or icons).
 *
 * @author D. Campione
 * 
 */
public class TextScrollPane extends JScrollPane {

    private static final long serialVersionUID = -1501799895052465321L;

    private TextArea textArea;
    private Gutter gutter;

    /**
     * Ctor. If you use this constructor, you must call {@link
     * #setViewportView(Component)} and pass in an {@link TextArea} for this
     * scroll pane to render line numbers properly.
     */
    public TextScrollPane() {
        this(null, true);
    }

    /**
     * Creates a scroll pane. A default value will be used for line number color
     * (gray), and the current line's line number will be highlighted.
     *
     * @param textArea The text area this scroll pane will contain.
     */
    public TextScrollPane(TextArea textArea) {
        this(textArea, true);
    }

    /**
     * Creates a scroll pane. A default value will be used for line number color
     * (gray), and the current line's line number will be highlighted.
     *
     * @param textArea The text area this scroll pane will contain. If this is
     *                 <code>null</code>, you must call {@link
     *                 #setViewportView(Component)}, passing in an {@link
     *                 TextArea}.
     * @param lineNumbers Whether line numbers should be enabled.
     */
    public TextScrollPane(TextArea textArea, boolean lineNumbers) {
        this(textArea, lineNumbers, Color.GRAY);
    }

    /**
     * Creates a scroll pane with preferred size (width, height).
     *
     * @param area The text area this scroll pane will contain. If this is
     *             <code>null</code>, you must call {@link
     *             #setViewportView(Component)}, passing in an {@link TextArea}.
     * @param lineNumbers Whether line numbers are initially enabled.
     * @param lineNumberColor The color to use for line numbers.
     */
    public TextScrollPane(TextArea area, boolean lineNumbers,
            Color lineNumberColor) {
        super(area);

        // Create the text area and set it inside this scroll bar area
        textArea = area;

        // Create the gutter for this document
        Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);
        gutter = new Gutter(textArea);
        gutter.setLineNumberFont(defaultFont);
        gutter.setLineNumberColor(lineNumberColor);
        setLineNumbersEnabled(lineNumbers);

        // Set miscellaneous properties
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /** Ensures the gutter is visible if it's showing anything. */
    private void checkGutterVisibility() {
        int count = gutter.getComponentCount();
        if (count == 0) {
            if (getRowHeader() != null && getRowHeader().getView() == gutter) {
                setRowHeaderView(null);
            }
        } else {
            if (getRowHeader() == null || getRowHeader().getView() == null) {
                setRowHeaderView(gutter);
            }
        }
    }

    /** @return The gutter. */
    public Gutter getGutter() {
        return gutter;
    }

    /**
     * Returns <code>true</code> if the line numbers are enabled and visible.
     *
     * @return Whether or not line numbers are visible.
     * @see #setLineNumbersEnabled(boolean)
     */
    public boolean getLineNumbersEnabled() {
        return gutter.getLineNumbersEnabled();
    }

    /**
     * Returns the text area being displayed.
     *
     * @return The text area.
     * @see #setViewportView(Component)
     */
    public TextArea getTextArea() {
        return (TextArea) getViewport().getView();
    }

    /**
     * @return Whether the fold indicator is enabled.
     * @see #setFoldIndicatorEnabled(boolean)
     */
    public boolean isFoldIndicatorEnabled() {
        return gutter.isFoldIndicatorEnabled();
    }

    /**
     * @return Whether the icon row header is enabled.
     * @see #setIconRowHeaderEnabled(boolean)
     */
    public boolean isIconRowHeaderEnabled() {
        return gutter.isIconRowHeaderEnabled();
    }

    /**
     * Toggles whether the fold indicator is enabled.
     *
     * @param enabled Whether the fold indicator should be enabled.
     * @see #isFoldIndicatorEnabled()
     */
    public void setFoldIndicatorEnabled(boolean enabled) {
        gutter.setFoldIndicatorEnabled(enabled);
        checkGutterVisibility();
    }

    /**
     * Toggles whether the icon row header (used for breakpoints, bookmarks,
     * etc..) is enabled.
     *
     * @param enabled Whether the icon row header is enabled.
     * @see #isIconRowHeaderEnabled()
     */
    public void setIconRowHeaderEnabled(boolean enabled) {
        gutter.setIconRowHeaderEnabled(enabled);
        checkGutterVisibility();
    }

    /**
     * Toggles whether or not line numbers are visible.
     *
     * @param enabled Whether or not line numbers should be visible.
     * @see #getLineNumbersEnabled()
     */
    public void setLineNumbersEnabled(boolean enabled) {
        gutter.setLineNumbersEnabled(enabled);
        checkGutterVisibility();
    }

    /**
     * Sets the view for this scroll pane. This must be an {@link TextArea}.
     *
     * @param view The new view.
     * @see #getTextArea()
     */
    @Override
    public void setViewportView(Component view) {
        if (!(view instanceof TextArea)) {
            throw new IllegalArgumentException("view must be an TextArea");
        }
        super.setViewportView(view);
        textArea = (TextArea) view;
        if (gutter != null) {
            gutter.setTextArea(textArea);
        }
    }
}