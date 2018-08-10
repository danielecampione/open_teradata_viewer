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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ThreadedAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ChangeLookAndFeelAction extends CustomAction {

    private static final long serialVersionUID = 1504750231015434193L;

    private String laf;

    public ChangeLookAndFeelAction(String laf) {
        super(laf);
        this.laf = laf;
    }

    /**
     * The method overrides the original one to hide the exception stacktrace. 
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (!inProgress) {
            inProgress = true;
            new ThreadedAction() {
                @Override
                protected void execute() throws Exception {
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        public void uncaughtException(Thread t, Throwable e) {
                            // Ignore all general errors relative to the L&F setup.
                        }
                    });
                    performThreaded(e);
                }
            };
        } else {
            ApplicationFrame.getInstance().changeLog.append(
                    "Another process is already running..\n",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            selectPrecedingLookAndFeelMenuItem();
        }
    }
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        if (UIManager.getLookAndFeel().getClass().toString()
                .substring("class ".length()).equals(laf)) {
            return;
        }
        ApplicationFrame.getInstance().setLookAndFeel(laf);
    }

    private void selectPrecedingLookAndFeelMenuItem() {
        StringTokenizer stringTokenizer = new StringTokenizer(UIManager
                .getLookAndFeel().getClass().toString()
                .substring("class ".length()), ".");
        String strSelectedLookAndFeel = "";
        while (stringTokenizer.hasMoreElements()) {
            strSelectedLookAndFeel = (String) stringTokenizer.nextElement();
        }

        // Searching for the look & feel menu
        JMenuBar menuBar = ApplicationFrame.getInstance().getJMenuBar();
        int lookAndFeelMenuPosition = -1;
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu currentMenu = menuBar.getMenu(i);
            if (currentMenu.getActionCommand().equals(
                    ApplicationFrame.LAF_MENU_LABEL)) {
                lookAndFeelMenuPosition = i;
                break;
            }
        }

        JMenu lookAndFeelMenu = menuBar.getMenu(lookAndFeelMenuPosition);
        for (int i = 0; i < lookAndFeelMenu.getItemCount(); i++) {
            JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) lookAndFeelMenu
                    .getItem(i);
            if (strSelectedLookAndFeel.equals(menuItem.getActionCommand())) {
                menuItem.setSelected(true);
                break;
            }
        }
    }
}
