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
import java.sql.ResultSet;

import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.ResultSetTable;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class DeleteAction extends CustomAction {

    private static final long serialVersionUID = 2562827293387124219L;

    protected DeleteAction() {
        super(LanguageManager.getInstance().getString("menu.query.delete"), "delete.png", null, null);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.query.delete"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        int[] rows = ResultSetTable.getInstance().getSelectedRows();
        ResultSet resultSet = Context.getInstance().getResultSet();
        for (int i = rows.length - 1; i > -1; i--) {
            int row = rows[i];
            int origRow = ResultSetTable.getInstance().getOriginalSelectedRow(
                    row);
            resultSet.first();
            resultSet.relative(origRow);
            resultSet.deleteRow();
            ResultSetTable.getInstance().removeRow(row);
        }
    }
}
