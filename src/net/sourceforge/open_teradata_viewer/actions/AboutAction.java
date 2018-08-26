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

import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.AboutDialog;
import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * Displays an "About" dialog.
 *
 * @author D. Campione
 *
 */
public class AboutAction extends CustomAction implements MouseListener {

    private static final long serialVersionUID = -4235652606704763545L;

    protected AboutAction() {
        super("About..");
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        AboutDialog ad = new AboutDialog(
                (ApplicationFrame) SwingUtilities
                        .getWindowAncestor(applicationFrame));
        ad.setLocationRelativeTo(applicationFrame);
        ad.setVisible(true);
    }
}