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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextAreaEditorKit;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class DecreaseIndentAction extends CustomAction {

    private static final long serialVersionUID = -1690909378881683266L;

    private Action decreaseIndent;

    protected DecreaseIndentAction() {
        super("Decrease indentation", "format_decreaseindent.png", KeyStroke
                .getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK),
                "Decreases the indentation amount for all selected lines.");
        decreaseIndent = new SyntaxTextAreaEditorKit.DecreaseIndentAction();
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        decreaseIndent.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}