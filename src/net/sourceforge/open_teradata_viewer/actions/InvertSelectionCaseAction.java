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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import net.sourceforge.open_teradata_viewer.editor.TextAreaEditorKit;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class InvertSelectionCaseAction extends CustomAction {

    private static final long serialVersionUID = 3152464367809905410L;

    private Action invertSelectionCase;

    protected InvertSelectionCaseAction() {
        super("Invert case", null, null,
                "Inverts the case of all letters in the current selection.");
        invertSelectionCase = new TextAreaEditorKit.InvertSelectionCaseAction();
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        invertSelectionCase.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}