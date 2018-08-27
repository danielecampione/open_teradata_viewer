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

import net.sourceforge.open_teradata_viewer.Context;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class CommitAction extends CustomAction {

    private static final long serialVersionUID = -8115492758381828174L;

    protected CommitAction() {
        super("Commit", "commit.png", null, null);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        Context.getInstance().getConnectionData().getConnection().commit();
    }

}
