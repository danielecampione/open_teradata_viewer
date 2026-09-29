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
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.AboutDialog;
import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * Displays an "About" dialog.
 *
 * @author D. Campione
 *
 */
public class AboutAction extends CustomAction implements MouseListener {

    private static final long serialVersionUID = -4235652606704763545L;
    
    protected AboutAction() {
    	super(LanguageManager.getInstance().getString("action.about"));
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.about"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        final ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        
        try {
            SwingUtilities.invokeAndWait(() -> {
                AboutDialog ad = new AboutDialog(
                        (ApplicationFrame) SwingUtilities
                                .getWindowAncestor(applicationFrame));
                ad.setLocationRelativeTo(applicationFrame);
                ad.setVisible(true);
            });
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        } catch (java.lang.reflect.InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}