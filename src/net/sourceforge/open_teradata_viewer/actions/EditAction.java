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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ResultSetTable;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class EditAction extends CustomAction {

    private static final long serialVersionUID = -6208272234164147803L;

    protected EditAction() {
        super(LanguageManager.getInstance().getString("menu.query.edit"), "edit.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        boolean hasResultSet = isConnected && Context.getInstance().getResultSet() != null;
        setEnabled(hasResultSet);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.query.edit"));
        });
    }

    protected EditAction(String name, String icon) {
        super(name, icon, null, null);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ResultSet resultSet = Context.getInstance().getResultSet();
        if (resultSet == null) {
            return;
        }
        final int columnCount;
        try {
            resultSet.first();
            columnCount = resultSet.getMetaData().getColumnCount();
        } catch (Exception ex) {
            ExceptionDialog.notifyException(new Exception(
                    "Unable to retrieve ResultSet metadata. The result set may no longer be valid.", ex));
            return;
        }
        final List selectedRow = ResultSetTable.getInstance().getSelectedRowData();

        final AtomicReference<JPanel> panelRef = new AtomicReference<>();
        final AtomicReference<JTextArea[]> textAreasRef = new AtomicReference<>();
        final AtomicReference<JScrollPane> scrollPaneRef = new AtomicReference<>();

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        JPanel panel = new JPanel(new GridBagLayout());
                        JTextArea[] textAreas = new JTextArea[columnCount];
                        GridBagConstraints constraints = new GridBagConstraints(-1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);

                        for (int column = 0; column < columnCount; column++) {
                            try {
                                String columnName = resultSet.getMetaData().getColumnName(column + 1);
                                panel.add(new JLabel(columnName), constraints);
                                if (column + 1 == columnCount) {
                                    constraints.weightx = 100;
                                    constraints.weighty = 100;
                                }
                                textAreas[column] = new JTextArea();
                                panel.add(textAreas[column], constraints);
                                fillTextArea(textAreas[column], selectedRow, column);
                                if (ResultSetTable.isLob(column)) {
                                    textAreas[column].setEnabled(false);
                                }
                                if (resultSet.getConcurrency() == ResultSet.CONCUR_READ_ONLY) {
                                    textAreas[column].setEditable(false);
                                }
                                textAreas[column].setBorder(BorderFactory.createLoweredBevelBorder());
                                constraints.gridy++;
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        JScrollPane scrollPane = new JScrollPane(panel);

                        panelRef.set(panel);
                        textAreasRef.set(textAreas);
                        scrollPaneRef.set(scrollPane);
                    } catch (RuntimeException ex) {
                        throw ex;
                    }
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            throw new Exception("Error creating UI components", ex);
        }

        final JPanel panel = panelRef.get();
        final JTextArea[] textAreas = textAreasRef.get();
        final JScrollPane scrollPane = scrollPaneRef.get();
        while (true) {
            try {
                if (Dialog.OK_OPTION == Dialog.show((String) getValue(Action.NAME), scrollPane, Dialog.PLAIN_MESSAGE,
                        Dialog.OK_CANCEL_OPTION) && resultSet.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
                    position(resultSet);
                    boolean changed = false;
                    for (int i = 0; i < textAreas.length; i++) {
                        String text = textAreas[i].getText();
                        if (textAreas[i].isEnabled() && change(text, getOriginalValue(selectedRow, i))) {
                            ResultSetTable.getInstance().update(i + 1, text);
                            updateSelectedRow(selectedRow, i, text);
                            changed = true;
                        }
                    }
                    if (changed) {
                        store(resultSet);
                    }
                }
                break;
            } catch (Throwable t) {
                ExceptionDialog.showException(t);
            }
        }
    }

    protected void fillTextArea(JTextArea textArea, List selectedRow, int column) {
        textArea.setText(getOriginalValue(selectedRow, column));
    }

    private String getOriginalValue(List selectedRow, int column) {
        return selectedRow == null || selectedRow.get(column) == null ? "" : selectedRow.get(column).toString();
    }

    protected boolean change(String text, String originalText) {
        return !text.equals(originalText);
    }

    protected void position(ResultSet resultSet) throws SQLException {
        int origRow = ResultSetTable.getInstance().getOriginalSelectedRow();
        resultSet.first();
        resultSet.relative(origRow);
    }

    protected void updateSelectedRow(List selectedRow, int column, String text) {
        selectedRow.set(column, text);
    }

    protected void store(ResultSet resultSet) throws SQLException {
        resultSet.updateRow();
    }
}