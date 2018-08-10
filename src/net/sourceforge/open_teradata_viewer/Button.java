/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Cursor;
import java.io.Serializable;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * An extension of <code>javax.swing.JButton</code> that displays a hand cursor
 * when the mouse is over it.
 *
 * @author D. Campione
 * 
 */
public class Button extends JButton implements Serializable {

    private static final long serialVersionUID = 3100058375834941621L;

    /**
     * If this property is defined, <code>Button</code>s will use the hand
     * cursor.
     */
    private static final boolean USE_HAND_CURSOR = !Boolean
            .getBoolean("normalButtonCursors");

    /** Creates a button with no set text or icon. */
    public Button() {
        init();
    }

    /**
     * Creates a button where properties are taken from the <code>Action</code>
     * supplied.
     *
     * @param a The <code>Action</code> used to specify the new button.
     */
    public Button(Action a) {
        super(a);
        init();
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon The <code>Icon</code> image to display on the button.
     */
    public Button(Icon icon) {
        super(icon);
        init();
    }

    /**
     * Creates a button with text.
     *
     * @param text The text of the button.
     */
    public Button(String text) {
        super(text);
        init();
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text The text of the button.
     * @param icon The <code>Icon</code> image to display on the button.
     */
    public Button(String text, Icon icon) {
        super(text, icon);
        init();
    }

    /** Does initialization common to all constructors. */
    private void init() {
        if (USE_HAND_CURSOR) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
}