/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2013, D. Campione
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
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.spell.SpellingParser;

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
        super("Toggle Spell Checking");
        isSpellCheckEnabled = true;
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        OTVSyntaxTextArea textArea = applicationFrame.getTextComponent();
        SpellingParser spellingParser = applicationFrame.getSpellingParser();

        isSpellCheckEnabled = !isSpellCheckEnabled;

        if (isSpellCheckEnabled) {
            textArea.addParser(spellingParser);
        } else {
            textArea.removeParser(spellingParser);
        }
    }
}