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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ResultSetTable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class EditAction extends CustomAction {

    private static final long serialVersionUID = -6208272234164147803L;

    protected EditAction() {
        super("Edit..", "edit.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        boolean hasResultSet = isConnected
                && Context.getInstance().getResultSet() != null;
        setEnabled(hasResultSet);
    }

    protected EditAction(String name, String icon) {
        super(name, icon, null, null);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        JTable table = ResultSetTable.getInstance();
        if (table.getRowCount() == 0) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("No result to view.",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        ResultSet resultSet = Context.getInstance().getResultSet();
        JPanel panel = new JPanel(new GridBagLayout());
        JTextArea[] textAreas = new JTextArea[resultSet.getMetaData()
                .getColumnCount()];
        GridBagConstraints constraints = new GridBagConstraints(-1, 0, 1, 1, 0,
                0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0);
        List selectedRow = ResultSetTable.getInstance().getSelectedRowData();
        for (int column = 0; column < resultSet.getMetaData().getColumnCount(); column++) {
            String columnName = resultSet.getMetaData().getColumnName(
                    column + 1);
            panel.add(new JLabel(columnName), constraints);
            if (column + 1 == resultSet.getMetaData().getColumnCount()) {
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
            textAreas[column].setBorder(BorderFactory
                    .createLoweredBevelBorder());
            constraints.gridy++;
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        while (true) {
            try {
                if (Dialog.OK_OPTION == Dialog.show(
                        (String) getValue(Action.NAME), scrollPane,
                        Dialog.PLAIN_MESSAGE, Dialog.OK_CANCEL_OPTION)
                        && resultSet.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
                    position(resultSet);
                    boolean changed = false;
                    for (int i = 0; i < textAreas.length; i++) {
                        String text = textAreas[i].getText();
                        if (textAreas[i].isEnabled()
                                && change(text,
                                        getOriginalValue(selectedRow, i))) {
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
        return selectedRow == null || selectedRow.get(column) == null
                ? ""
                : selectedRow.get(column).toString();
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