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
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.FileIO;
import net.sourceforge.open_teradata_viewer.ResultSetTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ExportExcelAction extends CustomAction {

    private static final long serialVersionUID = 7078532874679238724L;

    protected ExportExcelAction() {
        super("Export Excel", "spreadsheet.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        boolean hasResultSet = isConnected
                && Context.getInstance().getResultSet() != null;
        setEnabled(hasResultSet);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        JTable table = ResultSetTable.getInstance();
        if (table.getRowCount() == 0) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("No result to write.",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        boolean selection = false;
        if (table.getSelectedRowCount() > 0
                && table.getSelectedRowCount() != table.getRowCount()) {
            Object option = Dialog.show("Excel", "Export",
                    Dialog.QUESTION_MESSAGE, new Object[]{"Everything",
                            "Selection"}, "Everything");
            if (option == null || "-1".equals(option.toString())) {
                return;
            }
            selection = "Selection".equals(option);
        }

        List<?> list = ((DefaultTableModel) table.getModel()).getDataVector();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        for (int i = 0; i < table.getColumnCount(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(table.getColumnName(i)));
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, (table.getColumnModel().getColumn(i)
                    .getPreferredWidth() * 45));
        }
        int count = 1;
        for (int i = 0; i < list.size(); i++) {
            if (!selection || table.isRowSelected(i)) {
                List<?> data = (List<?>) list.get(i);
                row = sheet.createRow(count++);
                for (int j = 0; j < data.size(); j++) {
                    Object o = data.get(j);
                    HSSFCell cell = row.createCell(j);
                    if (o instanceof Number) {
                        cell.setCellValue(((Number) o).doubleValue());
                    } else if (o != null) {
                        if (ResultSetTable.isLob(j)) {
                            cell.setCellValue(new HSSFRichTextString(Context
                                    .getInstance().getColumnTypeNames()[j]));
                        } else {
                            cell.setCellValue(new HSSFRichTextString(o
                                    .toString()));
                        }
                    }
                }
            }
        }
        sheet.createFreezePane(0, 1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        FileIO.saveAndOpenFile("export.xls",
                byteArrayOutputStream.toByteArray());
    }
}