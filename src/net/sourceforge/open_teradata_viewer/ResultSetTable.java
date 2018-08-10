/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer;


import java.awt.Component;
import java.awt.Font;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.CopyCellValueAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ResultSetTable extends JTable {

    private static final long serialVersionUID = 1565094396033102066L;

    private static final ResultSetTable RESULT_SET_TABLE = new ResultSetTable();

    @SuppressWarnings("rawtypes")
    private List<Vector> originalOrder;

    private ResultSetTable() {
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setToolTipText(
                "<html>Left click: sort asc<br>Right click: sort desc</html>");
        getTableHeader().addMouseListener(new TableSorter());
        getTableHeader().setFont(
                getTableHeader().getFont().deriveFont(Font.BOLD));
        addMouseListener(Actions.LOB_EXPORT);
        getSelectionModel().addListSelectionListener(Actions.getInstance());
        getColumnModel().addColumnModelListener(Actions.getInstance());
        setModel(new ResultSetTableModel());
        setDefaultRenderer(Object.class, new ResultSetTableCellRenderer());
        getActionMap().put("copy",
                new CopyCellValueAction(getActionMap().get("copy")));
    }

    public static ResultSetTable getInstance() {
        return RESULT_SET_TABLE;
    }

    public Object getTableValue() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        return getValueAt(row, column);
    }

    public void setTableValue(Object o) {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        setValueAt(o, row, column);
    }

    @SuppressWarnings("unchecked")
    public List<String> getSelectedRowData() {
        int row = getSelectedRow();
        if (row != -1) {
            return (List<String>) ((DefaultTableModel) getModel())
                    .getDataVector().get(row);
        } else {
            return null;
        }
    }

    public int getOriginalSelectedRow() {
        int row = getSelectedRow();
        return getOriginalSelectedRow(row);
    }

    public int getOriginalSelectedRow(int selectedRow) {
        @SuppressWarnings("rawtypes")
        Vector row = (Vector) ((DefaultTableModel) getModel()).getDataVector()
                .get(selectedRow);
        return originalOrder.indexOf(row);
    }

    @SuppressWarnings("rawtypes")
    public void setDataVector(Vector<Vector> dataVector,
            Vector columnIdentifiers, String executionTime) {
        originalOrder = new ArrayList<Vector>(dataVector);
        ((DefaultTableModel) getModel()).setDataVector(dataVector,
                columnIdentifiers);
        String rows = String.format("%d %s", dataVector.size(),
                dataVector.size() != 1 ? "rows" : "row");
        JComponent scrollPane = (JComponent) getParent().getParent();
        if (dataVector.isEmpty()) {
            scrollPane.setToolTipText(String.format("%s - %s", rows,
                    executionTime));
            setToolTipText(null);
        } else {
            setToolTipText(String.format("%s - %s", rows, executionTime));
            scrollPane.setToolTipText(null);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resizeColumns();
            }
        });
    }

    public static boolean isLob(int column) {
        if (column == -1
                || column >= Context.getInstance().getColumnTypes().length) {
            return false;
        }
        int columnType = Context.getInstance().getColumnTypes()[column];
        return Types.LONGVARBINARY == columnType
                || Types.VARBINARY == columnType || Types.BLOB == columnType
                || Types.CLOB == columnType
                || 2007 /* oracle xmltype */== columnType;
    }

    protected void resizeColumns() {
        double[] widths = new double[getColumnCount()];
        TableCellRenderer defaultRenderer = getDefaultRenderer(Object.class);
        for (int i = 0; i < getModel().getRowCount(); i++) {
            for (int j = 0; j < getModel().getColumnCount(); j++) {
                Component component = defaultRenderer
                        .getTableCellRendererComponent(this, getModel()
                                .getValueAt(i, j), false, false, i, j);
                widths[j] = Math.max(widths[j], component.getPreferredSize()
                        .getWidth());
            }
        }
        defaultRenderer = getTableHeader().getDefaultRenderer();
        TableColumnModel columnModel = getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            Component component = defaultRenderer
                    .getTableCellRendererComponent(this,
                            columnModel.getColumn(i).getHeaderValue(), false,
                            false, 0, i);
            widths[i] = Math.max(widths[i], component.getPreferredSize()
                    .getWidth());
            widths[i] = Math.min(widths[i], 550);
            columnModel.getColumn(i).setPreferredWidth((int) widths[i] + 2);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class ResultSetTableModel extends DefaultTableModel {

        private static final long serialVersionUID = -6686889204709944699L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class ResultSetTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 3879463001635743124L;

        @Override
        public Component getTableCellRendererComponent(JTable componentTable,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (value != null && isLob(column)) {
                value = Context.getInstance().getColumnTypeNames()[column];
            }
            JLabel tableCellRendererComponent = (JLabel) super
                    .getTableCellRendererComponent(componentTable, value,
                            isSelected, hasFocus, row, column);
            tableCellRendererComponent
                    .setHorizontalAlignment(value instanceof Number
                            ? SwingConstants.TRAILING
                            : SwingConstants.LEADING);
            return tableCellRendererComponent;
        }
    }
}
