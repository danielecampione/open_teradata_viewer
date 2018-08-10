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
import java.awt.event.WindowListener;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FullScreenAction extends CustomAction {

    private static final long serialVersionUID = 1052503682296759923L;

    public FullScreenAction() {
        super("Full Screen", null, KeyStroke.getKeyStroke(KeyEvent.VK_F11,
                KeyEvent.VK_UNDEFINED), null);
        setEnabled(true);
    }
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        final WindowListener[] wl = ApplicationFrame.getInstance()
                .getWindowListeners();
        for (WindowListener w : wl) {
            ApplicationFrame.getInstance().removeWindowListener(w);
        }
        boolean fullScreenMode = ApplicationFrame.getInstance()
                .isFullScreenModeActive();
        fullScreenMode = !fullScreenMode;
        ApplicationFrame.getInstance().setFullScreenMode(fullScreenMode);
        ApplicationFrame.getInstance().repaint();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (WindowListener w : wl) {
                    ApplicationFrame.getInstance().addWindowListener(w);
                }
            }
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "switch to full screen mode" action can be performed only if
        // other processes are NOT running. No ThreadAction object must be
        // instantiated.
        if (!inProgress) {
            inProgress = true;
            try {
                performThreaded(e);
            } catch (Throwable t) {
                ApplicationFrame.getInstance().printStackTraceOnGUI(t);
                ExceptionDialog.showException(t);
            } finally {
                CustomAction.inProgress = false;
            }
        } else {
            ApplicationFrame.getInstance().changeLog.append(
                    "Another process is already running..\n",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
    }
}
