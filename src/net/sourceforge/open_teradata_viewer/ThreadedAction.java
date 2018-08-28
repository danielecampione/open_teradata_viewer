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

package net.sourceforge.open_teradata_viewer;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseAdapter;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.actions.CustomAction;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;
import net.sourceforge.open_teradata_viewer.util.task.Task;
import net.sourceforge.open_teradata_viewer.util.task.TaskPool;

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
            if (!SwingUtil.isVisible(glassPane)) {
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
            TaskPool.getTaskPool().addTask(new Task(Main.APPLICATION_NAME) {
                public void run() {
                    try {
                        execute();
                    } catch (final Exception e) {
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                ExceptionDialog.showException(e);
                            }
                        });
                    }
                }
            });
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        } finally {
            CustomAction.inProgress = false;

            if (SwingUtil.isVisible(glassPane)) {
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