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
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.SQLCellRenderer;

/**
 * Toggles whether the completion window uses "fancy" rendering.
 * 
 * @author D. Campione
 * 
 */
public class FancyCellRenderingAction extends CustomAction {

    private static final long serialVersionUID = 4170710988719590178L;

    public FancyCellRenderingAction() {
        super("Fancy Cell Rendering");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "fancy cell rendering" process can be performed altough other
        // processes are running
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    /* (non-Javadoc)
     * @see net.sourceforge.open_teradata_viewer.actions.CustomAction#performThreaded(java.awt.event.ActionEvent)
     */
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        boolean fancy = (applicationFrame.getAutoCompletion()
                .getListCellRenderer() == null);
        applicationFrame.getAutoCompletion().setListCellRenderer(
                fancy ? new SQLCellRenderer() : null);
    }

    public boolean isFancyCellRenderingActivated() {
        boolean fancy = (ApplicationFrame.getInstance().getAutoCompletion()
                .getListCellRenderer() != null);
        return fancy;
    }
}