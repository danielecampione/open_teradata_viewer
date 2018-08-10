/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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
public class RollbackAction extends CustomAction {

    private static final long serialVersionUID = 5040518890561368254L;

    protected RollbackAction() {
        super("Rollback", "rollback.png", null, null);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        Context.getInstance().getConnectionData().getConnection().rollback();
    }
}
