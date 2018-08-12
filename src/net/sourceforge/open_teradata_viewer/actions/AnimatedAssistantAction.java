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

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AnimatedAssistantAction extends CustomAction implements Runnable {

    private static final long serialVersionUID = -4623999245908017490L;

    private boolean animatedAssistantActived;

    protected AnimatedAssistantAction() {
        super("Animated assistant", null, null,
                "Shows an animated assistant while an operation is "
                        + "in progress.");
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        animatedAssistantActived = !animatedAssistantActived;

        String animatedAssistantProperty = "animated_assistant_actived";
        Config.saveSetting(animatedAssistantProperty,
                String.format("%b", animatedAssistantActived));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "animated assistant" process can be performed only if other
        // processes are NOT running. No ThreadAction object must be
        // instantiated
        if (!inProgress) {
            inProgress = true;
            try {
                performThreaded(e);
            } catch (Throwable t) {
                ExceptionDialog.showException(t);
            } finally {
                CustomAction.inProgress = false;
            }
        } else {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("Another process is already running..",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(200L);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ApplicationFrame.getInstance().repaint();
                    }
                });
            }
        } catch (InterruptedException ie) {
            return;
        }
    }

    public void setAnimatedAssistantActived(boolean animatedAssistantActived) {
        this.animatedAssistantActived = animatedAssistantActived;
    }
    public boolean isAnimatedAssistantActived() {
        return animatedAssistantActived;
    }
}