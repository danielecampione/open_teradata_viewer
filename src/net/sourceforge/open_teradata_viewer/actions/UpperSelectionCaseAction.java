/*
 * Open Teradata Viewer ( kernel )
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
public class UpperSelectionCaseAction extends CustomAction {

    private static final long serialVersionUID = 5694474278593302679L;

    private Action upperSelectionCase;

    protected UpperSelectionCaseAction() {
        super("To upper case", null, null,
                "Converts all letters in the current selection to upper case.");
        upperSelectionCase = new TextAreaEditorKit.UpperSelectionCaseAction();
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        upperSelectionCase.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}