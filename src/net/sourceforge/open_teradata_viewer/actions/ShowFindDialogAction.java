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

import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ShowFindDialogAction extends CustomAction {

    private static final long serialVersionUID = 4122174099874841385L;

    public ShowFindDialogAction() {
        super("Find..", "find.png", null, null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "show find dialog" process can be performed altough other
        // processes are running. No ThreadAction object must be instantiated
        // because the focus must go to the find dialog
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        FindDialog findDialog = applicationFrame.getFindDialog();
        ReplaceDialog replaceDialog = applicationFrame.getReplaceDialog();
        CollapsibleSectionPanel csp = applicationFrame
                .getCollapsibleSectionPanel();

        csp.hideBottomComponent();
        if (replaceDialog.isVisible()) {
            replaceDialog.setVisible(false);
        }
        UISupport.showDialog(findDialog);
    }
}