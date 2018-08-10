/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer;


import java.awt.Component;
import java.awt.Cursor;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseAdapter;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.actions.CustomAction;

/**
 * 
 *
 * @author D. Campione
 * 
 */
public abstract class ThreadedAction implements Runnable {

    public ThreadedAction() {
        new Thread(this).start();
    }

    @Override
    public final void run() {
        final Component focusOwner = KeyboardFocusManager
                .getCurrentKeyboardFocusManager().getFocusOwner();
        final Component glassPane = ApplicationFrame.getInstance()
                .getRootPane().getGlassPane();
        try {
            if (!glassPane.isVisible()) {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        if (glassPane.getMouseListeners().length == 0) {
                            glassPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            glassPane.addMouseListener(new MouseAdapter() {
                            });
                        }
                        glassPane.setVisible(true);
                        glassPane.requestFocus();
                    }
                });
            }
            execute();
        } catch (Throwable t) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(t);
            ExceptionDialog.showException(t);
        } finally {
            CustomAction.inProgress = false;
            if (glassPane.isVisible()) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            glassPane.setVisible(false);
                            if (focusOwner != null) {
                                focusOwner.requestFocus();
                            }
                        }
                    });
                } catch (Throwable t) {
                    ExceptionDialog.hideException(t);
                }
            }
        }
    }
    protected abstract void execute() throws Exception;
}