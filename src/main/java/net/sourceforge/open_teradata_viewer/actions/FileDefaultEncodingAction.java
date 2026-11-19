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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.FileIO;
import net.sourceforge.open_teradata_viewer.editor.DefaultEncoding;
import net.sourceforge.open_teradata_viewer.editor.EditorDocument;
import net.sourceforge.open_teradata_viewer.editor.EncodingChoice;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * Lets the user choose the encoding that new files are saved in, and whether
 * they start with a BOM (UTF-8 with a BOM is a choice of its own), see
 * {@link DefaultEncoding}. The tabs that are still empty and have no file take
 * it at once; the other tabs and the files that are opened keep their own.
 *
 * @author D. Campione
 *
 */
public class FileDefaultEncodingAction extends CustomAction {

    private static final long serialVersionUID = 6127408539126471003L;

    protected FileDefaultEncodingAction() {
        super(LanguageManager.getInstance().getString("menu.file.default_encoding"));
        setEnabled(true);

        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.file.default_encoding"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        final EncodingChoice[] chosen = new EncodingChoice[1];
        // The components are created and shown on the event thread; this one
        // waits for the user
        SwingUtilities.invokeAndWait(() -> {
            LanguageManager langManager = LanguageManager.getInstance();
            JComboBox<FileIO.EncodingItem> encodingCombo = FileIO.createEncodingCombo(DefaultEncoding.getChoice());
            JLabel label = new JLabel(langManager.getString("dialog.encoding"));
            label.setLabelFor(encodingCombo);
            JPanel panel = new JPanel(new BorderLayout(8, 0));
            panel.add(label, BorderLayout.WEST);
            panel.add(encodingCombo, BorderLayout.CENTER);
            if (Dialog.OK_OPTION == Dialog.show(langManager.getString("dialog.default_encoding"), panel,
                    Dialog.QUESTION_MESSAGE, Dialog.OK_CANCEL_OPTION)) {
                chosen[0] = FileIO.getSelectedEncoding(encodingCombo);
            }
        });
        if (chosen[0] == null) {
            return;
        }
        // Nothing changes if the choice cannot be kept
        DefaultEncoding.save(chosen[0]);
        SwingUtilities.invokeAndWait(
                () -> ApplicationFrame.getInstance().forEachDocument(EditorDocument::adoptDefaultEncoding));
    }
}
