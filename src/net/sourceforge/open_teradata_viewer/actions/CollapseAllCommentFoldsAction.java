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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * Action to collapse all comment folds.
 * 
 * @author D. Campione
 */
public class CollapseAllCommentFoldsAction extends CustomAction {

    private static final long serialVersionUID = 1L;

    private Action collapseAllCommentFolds;

    public CollapseAllCommentFoldsAction() {
        super(LanguageManager.getInstance().getString("action.collapse_all_comment_folds"));
        collapseAllCommentFolds = new RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction();
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.collapse_all_comment_folds"));
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        collapseAllCommentFolds.actionPerformed(e);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        // Not needed for this action
    }
}