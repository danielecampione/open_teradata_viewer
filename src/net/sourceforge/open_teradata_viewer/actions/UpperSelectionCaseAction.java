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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.fife.ui.rtextarea.RTextAreaEditorKit;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class UpperSelectionCaseAction extends CustomAction {

    private static final long serialVersionUID = 5694474278593302679L;

    private Action upperSelectionCase;

    protected UpperSelectionCaseAction() {
        super(LanguageManager.getInstance().getString("action.to_upper_case"), null,
                KeyStroke.getKeyStroke(KeyEvent.VK_U,
                        KeyEvent.SHIFT_DOWN_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                LanguageManager.getInstance().getString("action.to_upper_case.short_description"));
        upperSelectionCase = new RTextAreaEditorKit.UpperSelectionCaseAction();
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.to_upper_case"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("action.to_upper_case.short_description"));
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        upperSelectionCase.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}