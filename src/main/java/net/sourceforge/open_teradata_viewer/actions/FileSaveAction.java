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
 * Saves the text of the current document: in its own file, without asking for
 * anything, if it has one and no text is selected; otherwise a file is asked
 * for, as {@link FileSaveAsAction} does.
 *
 * @author D. Campione
 *
 * @see ApplicationFrame#saveDocument(net.sourceforge.open_teradata_viewer.editor.EditorDocument)
 */
public class FileSaveAction extends CustomAction {

    private static final long serialVersionUID = -9193848167596376935L;

    protected FileSaveAction() {
        super(LanguageManager.getInstance().getString("menu.file.save"), "filesave.png", null, null);
        setEnabled(true);

        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.file.save"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        applicationFrame.saveDocument(applicationFrame.getCurrentDocument());
    }
}
