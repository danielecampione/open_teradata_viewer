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

import javax.swing.Action;

import org.fife.ui.rtextarea.RTextAreaEditorKit;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class LowerSelectionCaseAction extends CustomAction {

    private static final long serialVersionUID = -3177141864988162422L;

    private Action lowerSelectionCase;

    protected LowerSelectionCaseAction() {
        super("To lower case", null, null,
                "Converts all letters in the current selection to lower case.");
        lowerSelectionCase = new RTextAreaEditorKit.LowerSelectionCaseAction();
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        lowerSelectionCase.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}