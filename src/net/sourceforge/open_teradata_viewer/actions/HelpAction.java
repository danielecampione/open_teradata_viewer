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
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.help.HelpFiles;
import net.sourceforge.open_teradata_viewer.help.HelpViewerWindow;
import net.sourceforge.open_teradata_viewer.util.StreamUtil;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;
import net.sourceforge.open_teradata_viewer.util.array.StringList;

/**
 * 
 * @author D. Campione
 * 
 */
public class HelpAction extends CustomAction {

    private static final long serialVersionUID = 1572333979959917847L;

    public HelpAction() {
        super("Help", "help.png", KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                KeyEvent.VK_UNDEFINED), "Shows the user manual.");
        setEnabled(true);
    }

    public void actionPerformed(final ActionEvent e) {
        // The "help" process can be performed altough other processes are
        // running. No ThreadAction object must be instantiated because the
        // focus must still remains on the guide frame
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        if (ApplicationFrame.getInstance().getHelpFrame() != null
                && SwingUtil.isVisible(ApplicationFrame.getInstance()
                        .getHelpFrame())) {
            return;
        }
        Utilities.writeLocallyJARInternalFile("license.txt");
        Utilities.writeLocallyJARInternalFile("changes.txt");

        // Guide files
        Utilities.writeLocallyJARInternalFile(HelpFiles.helpFolder
                + File.separator + "manual.html");

        StringList sl = new StringList();
        sl.setText(StreamUtil.stream2String(getClass().getResourceAsStream(
                "/res/help_files.list")));
        for (int i = 0; i < sl.size(); i++) {
            if (StringUtil.isEmpty((String) sl.get(i))) {
                continue;
            }
            try {
                Utilities.writeLocallyJARInternalFile(HelpFiles.helpFolder
                        + File.separator + (String) sl.get(i));
            } catch (Throwable ex) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println("Missing resource: " + (String) sl.get(i),
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                return;
            }
        }

        Utilities.writeLocallyJARInternalFile(HelpFiles.helpFolder
                + File.separator + "license.html");
        Utilities.writeLocallyJARInternalFile(HelpFiles.helpFolder
                + File.separator + "changes.html");
        Utilities.writeLocallyJARInternalFile(HelpFiles.helpFolder
                + File.separator + "FAQ.html");

        try {
            ApplicationFrame.getInstance().setHelpFrame(new HelpViewerWindow());
            ApplicationFrame.getInstance().getHelpFrame()
                    .setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ApplicationFrame.getInstance().getHelpFrame().setVisible(true);
        } catch (IOException ioe) {
            String errorMsg = "Unable to start the Help module.";
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(errorMsg,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(
                    "Unable to start the Help module.\n" + ioe.getMessage()
                            + "\n");
        }
    }
}