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
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.ResultSetTable;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ExportExcelAction extends CustomAction {

    private static final long serialVersionUID = 7078532874679238724L;

    protected ExportExcelAction() {
        super(LanguageManager.getInstance().getString("action.export.excel"), "spreadsheet.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        boolean hasResultSet = isConnected
                && Context.getInstance().getResultSet() != null;
        setEnabled(hasResultSet);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.export.excel"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        JTable table = ResultSetTable.getInstance();
        if (table.getRowCount() == 0) {
            ApplicationFrame.getInstance().getConsole().println("No result to write.",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        boolean selection = false;
        if (table.getSelectedRowCount() > 0
                && table.getSelectedRowCount() != table.getRowCount()) {
        	LanguageManager langManager = LanguageManager.getInstance();
        	Object option = Dialog.show(langManager.getString("dialog.excel"),
        			langManager.getString("action.export"),
        	        Dialog.QUESTION_MESSAGE,
        	        new Object[]{"option.everything", "option.selection"},
        	        "option.everything");
        	if (option == null || "-1".equals(option.toString())) {
        	    return;
        	}
        	selection = langManager.getString("option.selection").equals(option);
        }

        List<?> list = ((DefaultTableModel) table.getModel()).getDataVector();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // XSSFWorkbook (.xlsx, OOXML) is used instead of HSSFWorkbook (.xls,
        // BIFF8): the legacy .xls format has a hard limit of 65,536 rows per
        // sheet (HSSFSheet#createRow() throws IllegalArgumentException
        // beyond that), which a Teradata-oriented export tool can hit very
        // easily. XSSF supports up to 1,048,576 rows
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            XSSFRow row = sheet.createRow(0);
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            style.setFont(font);
            for (int i = 0; i < table.getColumnCount(); i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(new XSSFRichTextString(table.getColumnName(i)));
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, (table.getColumnModel().getColumn(i).getPreferredWidth() * 45));
            }
            int count = 1;
            for (int i = 0; i < list.size(); i++) {
                if (!selection || table.isRowSelected(i)) {
                    List<?> data = (List<?>) list.get(i);
                    row = sheet.createRow(count++);
                    for (int j = 0; j < data.size(); j++) {
                        Object o = data.get(j);
                        XSSFCell cell = row.createCell(j);
                        if (o instanceof Number) {
                            cell.setCellValue(((Number) o).doubleValue());
                        } else if (o != null) {
                            if (ResultSetTable.isLob(j)) {
                                cell.setCellValue(new XSSFRichTextString(
                                        Context.getInstance().getColumnTypeNames()[j]));
                            } else {
                                cell.setCellValue(new XSSFRichTextString(o.toString()));
                            }
                        }
                    }
                }
            }
            sheet.createFreezePane(0, 1);
            workbook.write(byteArrayOutputStream);
        }
        FileIO.saveAndOpenFile("export.xlsx", byteArrayOutputStream.toByteArray());
    }
}
