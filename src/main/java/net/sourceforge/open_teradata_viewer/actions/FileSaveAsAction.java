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

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * Saves the text of the current document in a file chosen by the user, which
 * is always asked for.
 *
 * @author D. Campione
 *
 * @see ApplicationFrame#saveDocumentAs(net.sourceforge.open_teradata_viewer.editor.EditorDocument)
 */
public class FileSaveAsAction extends CustomAction {

    private static final long serialVersionUID = 4873016259384710652L;

    protected FileSaveAsAction() {
        super(LanguageManager.getInstance().getString("menu.file.save_as"), null, null, null);
        setEnabled(true);

        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.file.save_as"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        applicationFrame.saveDocumentAs(applicationFrame.getCurrentDocument());
    }
}
