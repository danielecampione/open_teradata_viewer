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
import java.sql.Connection;
import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ConnectionData.DatabaseType;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ExplainStrategyFactory;
import net.sourceforge.open_teradata_viewer.IExplainStrategy;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.WaitingDialog;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 *
 * 
 * @author D. Campione
 * 
 */
public class ExplainRequestAction extends CustomAction {

    private static final long serialVersionUID = -8555161081550563065L;

    protected ExplainRequestAction() {
        super(LanguageManager.getInstance().getString("menu.query.explain"), null, null, LanguageManager.getInstance().getString("menu.query.explain.short_description"));
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.query.explain"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("menu.query.explain.short_description"));
        });
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "explain request" command can be performed altough other
        // processes are running
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame.getInstance().getConsole().println("NOT connected.",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        String request = null;
        boolean firstIteration = true;
        while (request == null) {
            if (firstIteration) {
                request = ApplicationFrame.getInstance().getTextComponent().getSelectedText();
                firstIteration = false;
            }
            if (request == null) {
                request = ApplicationFrame.getInstance().getTextComponent().getText();
                if (request.trim().length() == 0) {
                    request = Dialog.showInputDialog("Insert the request to analyze: ");
                    if (request == null) {
                        return;
                    }
                }
            }
        }

        DatabaseType databaseType = ApplicationFrame.getInstance().getDatabaseType();
        final IExplainStrategy explainStrategy = ExplainStrategyFactory.getStrategy(databaseType);
        if (explainStrategy == null) {
            ApplicationFrame.getInstance().getConsole().println(
                    LanguageManager.getInstance().getString("message.action_not_supported_for_database"),
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }

        Connection connection = Context.getInstance().getConnectionData().getConnection();
        Runnable onCancel = () -> {
            explainStrategy.cancel();
        };
        WaitingDialog waitingDialog;
        try {
            waitingDialog = new WaitingDialog(onCancel);
        } catch (InterruptedException ie) {
            ExceptionDialog.ignoreException(ie);
            return;
        }
        waitingDialog.setText(LanguageManager.getInstance().getString("message.executing_statement"));
        try {
            List<String> executionPlan = explainStrategy.explain(connection, request);
            ApplicationFrame.getInstance().getConsole().println(
                    Utilities.LINE_SEPARATOR + "\nExplanation\n" + Utilities.LINE_SEPARATOR);
            for (String line : executionPlan) {
                ApplicationFrame.getInstance().getConsole().println(line);
            }
        } finally {
            waitingDialog.hide();
        }
    }
}