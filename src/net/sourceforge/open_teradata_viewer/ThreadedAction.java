/*
 * DBEdit 2
 * Copyright (C) 2006-2012 Jef Van Den Ouweland
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

public abstract class ThreadedAction implements Runnable {

    public ThreadedAction() {
        new Thread(this).start();
    }

    @Override
    public final void run() {
        final Component[] focusOwnerHolder = {null};
        final Component glassPane = ApplicationFrame.getInstance().getRootPane()
                .getGlassPane();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    focusOwnerHolder[0] = KeyboardFocusManager
                            .getCurrentKeyboardFocusManager().getFocusOwner();
                    if (!glassPane.isVisible()) {
                        if (glassPane.getMouseListeners().length == 0) {
                            glassPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            glassPane.addMouseListener(new MouseAdapter() {
                            });
                        }
                        glassPane.setVisible(true);
                        glassPane.requestFocus();
                    }
                }
            });
            execute();
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        } finally {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        if (glassPane.isVisible()) {
                            glassPane.setVisible(false);
                            if (focusOwnerHolder[0] != null) {
                                focusOwnerHolder[0].requestFocus();
                            }
                        }
                    }
                });
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
        }
    }

    protected abstract void execute() throws Exception;
}