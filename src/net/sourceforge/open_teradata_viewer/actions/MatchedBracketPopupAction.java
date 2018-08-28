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
import net.sourceforge.open_teradata_viewer.ThreadedAction;

/**
 * Toggles the matched bracket popup window.
 * 
 * @author D. Campione
 * 
 */
public class MatchedBracketPopupAction extends CustomAction {

	private static final long serialVersionUID = 1054202978350180816L;

	public MatchedBracketPopupAction() {
        super("Matched Bracket Popup");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "matched bracket popup" process can be performed altough
        // other processes are running
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
        boolean enabled = !applicationFrame.getTextComponent()
                .getShowMatchedBracketPopup();
        applicationFrame.getTextComponent().setShowMatchedBracketPopup(enabled);
    }

    public boolean isMatchedBracketPopupEnabled() {
        boolean enabled = ApplicationFrame.getInstance().getTextComponent()
                .getShowMatchedBracketPopup();
        return enabled;
    }
}