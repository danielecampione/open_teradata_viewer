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

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public abstract class GroupAction extends CustomAction {

    private static final long serialVersionUID = -5061201540156365096L;

    private JPopupMenu popupMenu;

    protected GroupAction(String name) {
        super(name, "arrow.png", null, null);
        popupMenu = new JPopupMenu();
    }

    protected void addAction(Action action) {
        popupMenu.add(action);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        popupMenu.show((Component) e.getSource(), 0,
                ((Component) e.getSource()).getHeight());
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                JToggleButton toggleButton = (JToggleButton) popupMenu
                        .getInvoker();
                toggleButton.setSelected(false);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }
        });
    }
}