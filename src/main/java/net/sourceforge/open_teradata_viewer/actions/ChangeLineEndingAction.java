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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.editor.EditorDocument;
import net.sourceforge.open_teradata_viewer.editor.LineEnding;

/**
 * Changes the line ending the text of the current document is written with, see
 * {@link EditorDocument#convertLineEnding(LineEnding)}. It is the action of the
 * items of the menu that shows the line ending of the document.
 *
 * @author D. Campione
 *
 */
public class ChangeLineEndingAction extends AbstractAction {

    private static final long serialVersionUID = -4888790066690495216L;

    private final LineEnding lineEnding;

    /**
     * @param name The name of the action, as the menu item shows it.
     * @param lineEnding The line ending the action gives to the document.
     */
    public ChangeLineEndingAction(String name, LineEnding lineEnding) {
        putValue(NAME, name);
        this.lineEnding = lineEnding;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorDocument document = ApplicationFrame.getInstance().getCurrentDocument();
        if (document != null) {
            document.convertLineEnding(lineEnding);
        }
    }
}
