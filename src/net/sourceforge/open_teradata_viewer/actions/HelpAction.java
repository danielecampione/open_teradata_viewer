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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.help.HelpFiles;
import net.sourceforge.open_teradata_viewer.help.HelpViewerWindow;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * Action that opens the Help viewer and manages resource extraction.
 * 
 * @author D. Campione
 */
public class HelpAction extends CustomAction {

    private static final long serialVersionUID = 1572333979959917847L;

    public HelpAction() {
        super(LanguageManager.getInstance().getString("menu.help.help"), "help.png", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
        		LanguageManager.getInstance().getString("menu.help.help.short_description"));
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.help.help"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("menu.help.help.short_description"));
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        }
    }

    protected void performThreaded(final ActionEvent e) {
        // 1. Singleton check: avoid multiple windows
        HelpViewerWindow helpFrame = ApplicationFrame.getInstance().getHelpFrame();
        if (helpFrame != null && helpFrame.isVisible()) {
            helpFrame.toFront();
            return;
        }

        // 2. Comprehensive Resource extraction (HTML, CSS and all Images)
        extractHelpResourcesIfNeeded();

        try {
            // 3. UI Initialization
            helpFrame = new HelpViewerWindow();
            ApplicationFrame.getInstance().setHelpFrame(helpFrame);

            // 4. Cleanup on close
            helpFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    ApplicationFrame.getInstance().setHelpFrame(null);
                }
            });

            helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            helpFrame.setVisible(true);

        } catch (IOException ioe) {
            String errorMsg = "Unable to start the Help module.";
            ApplicationFrame.getInstance().getConsole().println(errorMsg,
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            UISupport.getDialogs().showErrorMessage(errorMsg + "\n" + ioe.getMessage());
        }
    }
    
    /**
     * Extracts internal JAR resources to the local temp folder only if they are missing.
     * Includes all images required for the manual and FAQ.
     */
    private void extractHelpResourcesIfNeeded() {
        String tempDir = Utilities.normalizePath(System.getProperty("java.io.tmpdir"));
        String helpFolderPath = tempDir + HelpFiles.helpFolder + File.separator;
        
        // The complete list of files to be extracted
        String[] resources = {
            // Document files
            "manual.html", "style.css", "license.html", "changes.html",
            "FAQ.html", "style_groovy_macros.css", "groovy_macros.html",
            "style_js_macros.css", "js_macros.html",
            
            // Image files
            "images/add.png", "images/back.png", "images/commit.png", 
            "images/connect.png", "images/connection.jpg", "images/connection_manager.jpg",
            "images/copy.png", "images/delete.png", "images/disconnect.png", 
            "images/edit.png", "images/export.png", "images/favorites.png", 
            "images/fetchlimit.png", "images/fileopen.png", "images/filesave.png", 
            "images/format.png", "images/import.png", "images/logo.png", 
            "images/next.png", "images/paste.png", "images/pdf.png", 
            "images/rollback.png", "images/run.png", "images/schema.png", 
            "images/script.png", "images/source.png", "images/spreadsheet.png", 
            "images/text.png", "images/textarea_clipboard_history.png", 
            "images/textarea_contextual_menu.png", "images/textarea_matched_bracket_popup.png"
        };

        for (String fileName : resources) {
            File targetFile = new File(helpFolderPath, fileName);
            
            // We only write if the file is not already there
            if (!targetFile.exists()) {
                try {
                    // This method handles the creation of the "images" subfolder automatically
                    Utilities.writeLocallyJARInternalFile(HelpFiles.helpFolder + File.separator + fileName);
                } catch (Exception ex) {
                    ExceptionDialog.ignoreException(ex);
                }
            }
        }
    }
}