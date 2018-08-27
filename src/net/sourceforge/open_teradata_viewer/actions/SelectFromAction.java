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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.CustomSelectFromStatement;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.GenericSelectFromStatement;
import net.sourceforge.open_teradata_viewer.SelectFromStatementTemplateMethod;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SelectFromAction extends CustomAction {

    private static final long serialVersionUID = 5489248195539100092L;

    protected SelectFromAction() {
        super("SELECT * FROM ..", null, KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), null);
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        SelectFromStatementTemplateMethod selectFromStatement = isConnected ? new CustomSelectFromStatement(
                promptRelationName()) : new GenericSelectFromStatement();
        ApplicationFrame.getInstance().getTextComponent()
                .setText(selectFromStatement.returnSQLQuery());
        Actions.FORMAT_SQL.actionPerformed(new ActionEvent(this, 0, null));
    }

    private String promptRelationName() {
        String relationName = null;
        boolean firstIteration = true;
        while (relationName == null) {
            if (firstIteration) {
                relationName = ApplicationFrame.getInstance()
                        .getTextComponent().getSelectedText();
                firstIteration = false;
            }
            if (relationName == null) {
                relationName = Dialog
                        .showInputDialog("Insert the table name: ");
                if (relationName == null) {
                    return relationName;
                }
            }
            if (!Utilities.canBeATeradataObjectName(relationName)) {
                relationName = null;
            }
        }
        relationName = relationName.toUpperCase();
        return relationName;
    }
}