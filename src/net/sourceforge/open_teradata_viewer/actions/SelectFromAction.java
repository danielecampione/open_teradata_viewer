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
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
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
        super(LanguageManager.getInstance().getString("action.select_all"), null, KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), null);
        setEnabled(true);

        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.select_all"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        SelectFromStatementTemplateMethod selectFromStatement = isConnected
                ? new CustomSelectFromStatement(promptRelationName())
                : new GenericSelectFromStatement();
        ApplicationFrame.getInstance().getTextComponent()
                .setText(selectFromStatement.returnSQLQuery());
        Actions.FORMAT_SQL.actionPerformed(new ActionEvent(this, 0, null));
    }

    private String promptRelationName() throws InterruptedException,
            java.lang.reflect.InvocationTargetException {
        String relationName = null;
        boolean firstIteration = true;
        while (relationName == null) {
            if (firstIteration) {
                relationName = ApplicationFrame.getInstance()
                        .getTextComponent().getSelectedText();
                firstIteration = false;
            }
            if (relationName == null) {
                // Dialog interaction must happen on the Event Dispatch Thread
                // to avoid Substance state-tracking violations
                final String[] result = new String[1];
                javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        result[0] = Dialog.showInputDialog("Insert the table name: ");
                    }
                });
                relationName = result[0];
                if (relationName == null) {
                    return null;
                }
            }
            if (!Utilities.canBeAValidObjectName(relationName)) {
                relationName = null;
            }
        }
        relationName = relationName.toUpperCase();
        return relationName;
    }
}