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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.spell.SpellingParser;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ToggleSpellCheckingAction extends CustomAction {

    private static final long serialVersionUID = 3697641313458928673L;

    private boolean isSpellCheckEnabled;

    public ToggleSpellCheckingAction() {
        super(LanguageManager.getInstance().getString("menu.edit.toggle_spell_checking"));
        isSpellCheckEnabled = true;
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.edit.toggle_spell_checking"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        RSyntaxTextArea textArea = applicationFrame.getTextComponent();
        SpellingParser spellingParser = applicationFrame.getSpellingParser();

        isSpellCheckEnabled = !isSpellCheckEnabled;

        if (isSpellCheckEnabled) {
            textArea.addParser(spellingParser);
        } else {
            textArea.removeParser(spellingParser);
        }
    }
}