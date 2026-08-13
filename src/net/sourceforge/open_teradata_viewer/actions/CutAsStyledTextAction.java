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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class CutAsStyledTextAction extends CustomAction {

    private static final long serialVersionUID = -804158889626235213L;

    private Action cutAsStyledText;

    protected CutAsStyledTextAction() {
        super(LanguageManager.getInstance().getString("action.cut_styled"), "cut.png",
                KeyStroke.getKeyStroke(KeyEvent.VK_X,
                        KeyEvent.SHIFT_DOWN_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                LanguageManager.getInstance().getString("action.cut_styled.short_description"));
        // true selects RSTA's new cut variant (copies as styled text, then
        // removes the selection), as opposed to CopyAsStyledTextAction's
        // false, which only copies
        cutAsStyledText = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(true);
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.cut_styled"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("action.cut_styled.short_description"));
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        cutAsStyledText.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
    }
}