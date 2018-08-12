/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

/**
 * This listens for and reports caret movements.
 * 
 * @author D. Campione
 *
 */
public class CaretListenerLabel extends JLabel implements CaretListener {

    private static final long serialVersionUID = -6946478396948185540L;

    public CaretListenerLabel(String label) {
        super(label);
    }

    /** Might not be invoked from the event dispatch thread */
    public void caretUpdate(CaretEvent e) {
        displaySelectionInfo(e.getDot(), e.getMark());
    }

    // This method can be invoked from any thread. It invokes the setText and
    // modelToView methods, which must run on the event dispatch thread. We use
    // invokeLater to schedule the code for execution on the event dispatch
    // thread.
    protected void displaySelectionInfo(final int dot, final int mark) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (dot == mark) { // No selection
                    try {
                        Rectangle caretCoords = ApplicationFrame.getInstance()
                                .getTextComponent().modelToView(dot);
                        // Convert it to view coordinates
                        setText(dot + ", [" + caretCoords.x + ", "
                                + caretCoords.y + "]\n");
                    } catch (BadLocationException ble) {
                        setText(dot + "\n");
                    }
                } else {
                    int selectedCharacters;
                    if (dot < mark) {
                        selectedCharacters = mark - dot;
                        setText("selection from: " + dot + " to " + mark + " ("
                                + selectedCharacters
                                + " selected characters)\n");
                    } else {
                        selectedCharacters = dot - mark;
                        setText("selection from: " + mark + " to " + dot + " ("
                                + selectedCharacters
                                + " selected characters)\n");
                    }
                }
            }
        });
    }
}
