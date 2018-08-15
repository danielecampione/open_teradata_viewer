/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * A dialog that closes itself when the Escape key is pressed. Subclasses can
 * extend the {@link #escapePressed()} method and provide custom handling logic
 * (parameter validation, custom closing, etc.). 
 * 
 * @author D. Campione
 *
 */
public abstract class EscapableDialog extends JDialog {

    private static final long serialVersionUID = 4872864678085416122L;

    /** The key in an <code>InputMap</code> for the Escape key action. */
    private static final String ESCAPE_KEY = "OnEsc";

    /** Ctor. */
    public EscapableDialog() {
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent dialog.
     */
    public EscapableDialog(java.awt.Dialog owner) {
        super(owner);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent dialog.
     */
    public EscapableDialog(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent dialog.
     */
    public EscapableDialog(java.awt.Dialog owner, String title) {
        super(owner, title);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent dialog.
     */
    public EscapableDialog(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent frame.
     */
    public EscapableDialog(Frame owner) {
        super(owner);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent frame.
     */
    public EscapableDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent frame.
     */
    public EscapableDialog(Frame owner, String title) {
        super(owner, title);
        init();
    }

    /**
     * Ctor.
     *
     * @param owner The parent frame.
     */
    public EscapableDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    /**
     * Called when the Escape key is pressed in this dialog. Subclasses can
     * override to handle any custom "Cancel" logic. The default implementation
     * hides the dialog (via <code>setVisible(false);</code>).
     */
    protected void escapePressed() {
        setVisible(false);
    }

    /** Initializes this dialog. */
    private void init() {
        setEscapeClosesDialog(true);
    }

    /**
     * Toggles whether the Escape key closes this dialog.
     *
     * @param closes Whether Escape should close this dialog (actually, whether
     *        {@link #escapePressed()} should be called when Escape is pressed).
     */
    public void setEscapeClosesDialog(boolean closes) {
        JRootPane rootPane = getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        if (closes) {
            im.put(ks, ESCAPE_KEY);
            actionMap.put(ESCAPE_KEY, new AbstractAction() {

                private static final long serialVersionUID = -1572858731057374772L;

                public void actionPerformed(ActionEvent e) {
                    escapePressed();
                }
            });
        } else {
            im.remove(ks);
            actionMap.remove(ESCAPE_KEY);
        }
    }
}