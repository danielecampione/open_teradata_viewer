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
import net.sourceforge.open_teradata_viewer.SwingUtil;
import net.sourceforge.open_teradata_viewer.Tools;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.help.ApplicationFiles;
import net.sourceforge.open_teradata_viewer.help.HelpViewerWindow;

/**
 * 
 * @author D. Campione
 * 
 */
public class HelpAction extends CustomAction {

    private static final long serialVersionUID = 1572333979959917847L;

    public HelpAction() {
        super("Help", "help.png", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
                "Shows the user manual.");
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
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual.html");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "colorschememapping.xml");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "filelist.xml");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image001.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image003.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image005.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image007.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image009.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image011.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image013.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image015.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image017.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image019.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image021.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image023.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image025.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image027.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image029.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image031.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image033.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image035.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image037.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image039.jpg");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image040.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image041.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image042.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image043.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image044.png");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image045.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image046.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image047.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image048.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image049.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image050.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image051.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image052.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image053.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image054.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image055.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image056.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image057.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image058.gif");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image059.jpg");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image060.jpg");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "image061.jpg");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "item0044.xml");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "props0045.xml");
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "manual_file" + File.separator
                + "themedata.thmx");

        // FAQ
        Tools.writeLocallyJARInternalFile(ApplicationFiles.helpFolder
                + File.separator + "FAQ.html");

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
