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
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.History;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class HistoryNextAction extends CustomAction {

    private static final long serialVersionUID = -633917757421880055L;

    protected HistoryNextAction() {
        super("History - Next", "next.png", KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK), null);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (History.getInstance().hasNext()) {
            ApplicationFrame.getInstance()
                    .setText(History.getInstance().next());
        }
        Actions.getInstance().validateTextActions();
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}
