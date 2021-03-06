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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class DuplicateAction extends EditAction {

    private static final long serialVersionUID = 6661786810339076715L;

    protected DuplicateAction() {
        super("Duplicate", "copy.png");
    }

    @Override
    protected boolean change(String text, String originalText) {
        return text != null && !"".equals(text);
    }

    @Override
    protected void position(ResultSet resultSet) throws SQLException {
        resultSet.moveToInsertRow();
    }

    @Override
    protected void updateSelectedRow(List selectedRow, int column, String text) {
    }

    @Override
    protected void store(ResultSet resultSet) throws SQLException {
        resultSet.insertRow();
    }
}