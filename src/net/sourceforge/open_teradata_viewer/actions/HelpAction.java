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
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.Tools;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.help.HelpFiles;
import net.sourceforge.open_teradata_viewer.help.HelpViewerWindow;
import net.sourceforge.open_teradata_viewer.util.StreamUtil;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;
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
        // The help command can be performed altough other processes are
        // running. No ThreadAction object must be instantiated because the
        // focus must still remains on the guidance frame.
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(t);
            ExceptionDialog.showException(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        if (ApplicationFrame.getInstance().helpFrame != null
                && SwingUtil
                        .isVisible(ApplicationFrame.getInstance().helpFrame)) {
            return;
        }
        Tools.writeLocallyJARInternalFile("license.txt");
        Tools.writeLocallyJARInternalFile("changes.txt");

        // guidance files
        Tools.writeLocallyJARInternalFile(HelpFiles.helpFolder + File.separator
                + "manual.html");

        StringList sl = new StringList();
        sl.setText(StreamUtil.stream2String(getClass().getResourceAsStream(
                "/res/help_files.list")));
        for (int i = 0; i < sl.size(); i++) {
            if (StringUtil.isEmpty((String) sl.get(i))) {
                continue;
            }
            try {
                Tools.writeLocallyJARInternalFile(HelpFiles.helpFolder
                        + File.separator + "manual_file" + File.separator
                        + (String) sl.get(i));
            } catch (Throwable ex) {
                ApplicationFrame.getInstance().changeLog.append(
                        "Missing resource: " + (String) sl.get(i),
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                return;
            }
        }

        Tools.writeLocallyJARInternalFile(HelpFiles.helpFolder + File.separator
                + "license.html");
        Tools.writeLocallyJARInternalFile(HelpFiles.helpFolder + File.separator
                + "changes.html");
        Tools.writeLocallyJARInternalFile(HelpFiles.helpFolder + File.separator
                + "FAQ.html");

        try {
            ApplicationFrame.getInstance().helpFrame = new HelpViewerWindow();
            ApplicationFrame.getInstance().helpFrame
                    .setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ApplicationFrame.getInstance().helpFrame.setVisible(true);
        } catch (IOException ioe) {
            String errorMsg = "Unable to start the Help module.\n";
            ApplicationFrame.getInstance().changeLog.append(errorMsg,
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(
                    "Unable to start the Help module.\n" + ioe.getMessage()
                            + "\n");
        }
    }
}
