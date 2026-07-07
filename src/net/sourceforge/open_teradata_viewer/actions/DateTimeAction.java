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

import javax.swing.Action;

import org.fife.ui.rtextarea.RTextAreaEditorKit;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class DateTimeAction extends CustomAction {

    private static final long serialVersionUID = 3002564219607149147L;

    private Action dateTime;
    
    protected DateTimeAction() {
        super(LanguageManager.getInstance().getString("action.date_time"), "clock.png", null,
        		LanguageManager.getInstance().getString("action.date_time.short_description"));
        dateTime = new RTextAreaEditorKit.TimeDateAction(); 
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.date_time"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("action.date_time.short_description"));
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        dateTime.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}