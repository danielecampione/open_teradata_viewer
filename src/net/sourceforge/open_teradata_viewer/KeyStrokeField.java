/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * A text field that lets a user enter a <code>KeyStroke</code>.
 *
 * @author D. Campione
 * 
 */
public class KeyStrokeField extends JTextField {

    private static final long serialVersionUID = -7337476433678386928L;

    private KeyStroke stroke;
    private FocusAdapter listener;

    public KeyStrokeField() {
        super(20);
        listener = new FocusHandler();
        addFocusListener(listener);
    }

    /**
     * Returns the key stroke they've entered.
     *
     * @return The key stroke, or <code>null</code> if nothing is
     *         entered.
     * @see #setKeyStroke(KeyStroke)
     */
    public KeyStroke getKeyStroke() {
        return stroke;
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (e.getID() == KeyEvent.KEY_PRESSED && keyCode != KeyEvent.VK_ENTER && keyCode != KeyEvent.VK_BACK_SPACE) {
            int modifiers = e.getModifiers();
            setKeyStroke(KeyStroke.getKeyStroke(keyCode, modifiers));
            return;
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            stroke = null; // Not necessary; sanity check
            setText(null);
        }
    }

    /**
     * Sets the key stroke currently displayed.
     *
     * @param ks The key stroke to display. This may be <code>null</code>.
     * @see #getKeyStroke()
     */
    public void setKeyStroke(KeyStroke ks) {
        stroke = ks;
        setText(UIUtil.getPrettyStringFor(stroke));
    }

    /**
     * Listens for focus events in this component.
     * 
     * @author D. Campione
     * 
     */
    private class FocusHandler extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent e) {
            selectAll();
        }

    }
}