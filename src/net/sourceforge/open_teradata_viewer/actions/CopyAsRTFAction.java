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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
public class CopyAsRTFAction extends CustomAction {

    private static final long serialVersionUID = -804158889626235212L;

    private Action copyAsRTF;

    protected CopyAsRTFAction() {
        super(
                "Copy as RTF",
                "copy.png",
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_DOWN_MASK
                        + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                "Copies the current selection to the clipboard as RTF.");
        copyAsRTF = new SyntaxTextAreaEditorKit.CopyAsRtfAction();
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        copyAsRTF.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}