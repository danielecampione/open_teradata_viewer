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

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FetchLimitAction extends CustomAction {

    private static final long serialVersionUID = -4703116641594116984L;

    protected FetchLimitAction() {
        super(getFormattedName(), "fetchlimit.png", null, null);
        setEnabled(true);
        
        // Add language change listener to update the action name when language
        // changes.
        // The listener that recalculates the entire string with the new language
        // and the current value
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            // Ensure UI property updates are safely dispatched to the Event
        	// Dispatch Thread
            SwingUtilities.invokeLater(() -> putValue(NAME, getFormattedName()));
        });
    }
    
    // Helper method to avoid code duplication between constructor, listener and
    // performThreaded
    private static String getFormattedName() {
        int limit = Context.getInstance().getFetchLimit();
        String limitString = (limit == 0) 
            ? LanguageManager.getInstance().getString("label.unlimited") 
            : String.valueOf(limit);
            
        return String.format(LanguageManager.getInstance().getString("action.fetch_limit"), limitString);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        // Substance requires all Swing components to be created and manipulated on
    	// the EDT.
        // invokeAndWait is used to block the current background thread until the
    	// user interacts with the dialog
        SwingUtilities.invokeAndWait(() -> {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                    Context.getInstance().getFetchLimit(), 0, 999999, 1));
            
            String dialogTitle = LanguageManager.getInstance().getString("dialog.fetch_limit");
            
            if (Dialog.OK_OPTION == Dialog.show(dialogTitle, spinner, Dialog.QUESTION_MESSAGE, Dialog.OK_CANCEL_OPTION)) {
                Context.getInstance().setFetchLimit(((Number) spinner.getValue()).intValue());
                
                // Update the action name using the helper method to avoid duplicate formatting logic
                putValue(NAME, getFormattedName());
            }
        });
    }
}