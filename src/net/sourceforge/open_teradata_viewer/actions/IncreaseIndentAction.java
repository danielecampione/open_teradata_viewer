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
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextAreaEditorKit;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class IncreaseIndentAction extends CustomAction {

    private static final long serialVersionUID = 1668796223229494619L;

    protected IncreaseIndentAction() {
        super("Increase indentation", "format_increaseindent.png", KeyStroke
                .getKeyStroke(KeyEvent.VK_TAB, 0),
                "Increases the indentation amount for all selected lines.");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        new SyntaxTextAreaEditorKit.InsertTabAction().actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}