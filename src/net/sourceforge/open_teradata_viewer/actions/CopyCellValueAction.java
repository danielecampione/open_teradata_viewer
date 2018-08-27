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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;

import net.sourceforge.open_teradata_viewer.ResultSetTable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CopyCellValueAction extends AbstractAction {

    private static final long serialVersionUID = 8416058336022772049L;

    private Action defaultAction;

    public CopyCellValueAction(Action defaultAction) {
        this.defaultAction = defaultAction;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        JTable table = (JTable) ae.getSource();
        if (table.getSelectedRowCount() == 1) {
            Object value = ResultSetTable.getInstance().getTableValue();
            if (value != null) {
                Toolkit.getDefaultToolkit()
                        .getSystemClipboard()
                        .setContents(new StringSelection(value.toString()),
                                null);
            }
        } else {
            defaultAction.actionPerformed(ae);
        }
    }
}
