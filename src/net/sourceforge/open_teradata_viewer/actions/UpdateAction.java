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

import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 *
 *
 * @author D. Campione
 *
 */
public class UpdateAction extends CustomAction {

    private static final long serialVersionUID = -7782859550440488409L;

    protected UpdateAction() {
        super(LanguageManager.getInstance().getString("action.download_latest_version"));
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.download_latest_version"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        Utilities.openURLWithDefaultBrowser(Config.GITHUB_REPO_URL
                + "releases/latest");
    }
}
