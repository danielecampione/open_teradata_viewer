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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class PasteAction extends CustomAction {

    private static final long serialVersionUID = 1283253040048584249L;

    protected PasteAction() {
        super("Paste", "paste.png", KeyStroke.getKeyStroke(KeyEvent.VK_V,
                KeyEvent.CTRL_DOWN_MASK), null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        ApplicationFrame.getInstance().getTextComponent().paste();
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}
