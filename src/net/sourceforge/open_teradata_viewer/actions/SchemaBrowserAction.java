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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.SchemaBrowser;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SchemaBrowserAction extends CustomAction implements KeyListener {

    private static final long serialVersionUID = -2426894619024711099L;

    protected SchemaBrowserAction() {
        super("Schema Browser", "schema.png", KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK), null);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        ApplicationFrame.getInstance().showHideObjectChooser();
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (e.getClickCount() == 2) {
            selectFromObjectChooser((SchemaBrowser) e.getSource());
            ApplicationFrame.getInstance().showHideObjectChooser();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() instanceof SchemaBrowser) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isAltDown()) {
                selectFromObjectChooser((SchemaBrowser) e.getSource());
                ApplicationFrame.getInstance().showHideObjectChooser();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                ApplicationFrame.getInstance().showHideObjectChooser();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void selectFromObjectChooser(SchemaBrowser schemaBrowser) {
        String s = Arrays.asList(schemaBrowser.getSelectedItems()).toString();
        s = s.substring(1, s.length() - 1);
        ApplicationFrame.getInstance().replaceText(s);
    }
}