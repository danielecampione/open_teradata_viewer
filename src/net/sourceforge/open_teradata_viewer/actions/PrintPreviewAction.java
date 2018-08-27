/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2015, D. Campione
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
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.print.PrintPreviewDialog;

/**
 * Action used to show a print preview.
 *
 * @author D. Campione
 * 
 */
class PrintPreviewAction extends CustomAction {

    private static final long serialVersionUID = -8657243264174233798L;

    /** Ctor. */
    protected PrintPreviewAction() {
        super("Print Preview");
        setEnabled(true);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.open_teradata_viewer.actions.CustomAction#performThreaded(java.awt.event.ActionEvent)
     */
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        PrintPreviewDialog printPreviewDialog = new PrintPreviewDialog(
                ApplicationFrame.getInstance(), ApplicationFrame.getInstance()
                        .getTextComponent());
        printPreviewDialog
                .setLocationRelativeTo(ApplicationFrame.getInstance());
        UISupport.showDialog(printPreviewDialog);
    }
}