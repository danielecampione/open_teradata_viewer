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

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ExportPdfAction extends CustomAction implements PdfPageEvent {

    private static final long serialVersionUID = -2520587450971303014L;

    private PdfTemplate pdfTemplate;
    private static final Font FONT = new Font();
    private static final Font ROW_HEADER_FONT = new Font(FONT.getFamily(), 12,
            Font.BOLD);
    private static final BaseFont ROW_HEADER_BASE_FONT = ROW_HEADER_FONT
            .getCalculatedBaseFont(false);
    private static final BaseFont BASE_FONT = FONT.getCalculatedBaseFont(false);

    protected ExportPdfAction() {
        super("Export PDF", "pdf.png", null, null);
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
            Object option = Dialog.show("PDF", "Export",
                    Dialog.QUESTION_MESSAGE, new Object[]{"Everything",
                            "Selection"}, "Everything");
            if (option == null || "-1".equals(option.toString())) {
                return;
            }
            selection = "Selection".equals(option);
        }

        List<?> list = ((DefaultTableModel) table.getModel()).getDataVector();
        int columnCount = table.getColumnCount();
        PdfPTable pdfPTable = new PdfPTable(columnCount);
        pdfPTable.setWidthPercentage(100);
        pdfPTable.getDefaultCell().setPaddingBottom(4);
        int[] widths = new int[columnCount];

        // Row Header
        pdfPTable.getDefaultCell().setBorderWidth(2);
        for (int i = 0; i < columnCount; i++) {
            String columnName = table.getColumnName(i);
            pdfPTable.addCell(new Phrase(columnName, ROW_HEADER_FONT));
            widths[i] = Math.min(
                    50000,
                    Math.max(widths[i],
                            ROW_HEADER_BASE_FONT.getWidth(columnName + " ")));
        }
        pdfPTable.getDefaultCell().setBorderWidth(1);
        if (!list.isEmpty()) {
            pdfPTable.setHeaderRows(1);
        }

        // Body
        for (int i = 0; i < list.size(); i++) {
            if (!selection || table.isRowSelected(i)) {
                List<?> record = (List<?>) list.get(i);
                for (int j = 0; j < record.size(); j++) {
                    Object o = record.get(j);
                    if (o != null) {
                        if (ResultSetTable.isLob(j)) {
                            o = Context.getInstance().getColumnTypeNames()[j];
                        }
                    } else {
                        o = "";
                    }
                    PdfPCell cell = new PdfPCell(new Phrase(o.toString()));
                    cell.setPaddingBottom(4);
                    if (o instanceof Number) {
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    }
                    pdfPTable.addCell(cell);
                    widths[j] = Math.min(
                            50000,
                            Math.max(widths[j],
                                    BASE_FONT.getWidth(o.toString())));
                }
            }
        }

        // Size
        pdfPTable.setWidths(widths);
        int totalWidth = 0;
        for (int width : widths) {
            totalWidth += width;
        }
        Rectangle pageSize = PageSize.A4.rotate();
        pageSize.setRight(pageSize.getRight()
                * Math.max(1f, totalWidth / 53000f));
        pageSize.setTop(pageSize.getTop() * Math.max(1f, totalWidth / 53000f));

        // Document
        Document document = new Document(pageSize);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document,
                byteArrayOutputStream);
        document.open();
        pdfTemplate = writer.getDirectContent().createTemplate(100, 100);
        pdfTemplate.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        writer.setPageEvent(this);
        document.add(pdfPTable);
        document.close();
        FileIO.saveAndOpenFile("export.pdf",
                byteArrayOutputStream.toByteArray());
    }

    /** Print page numbers on right bottom corner. */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        String text = String.format("Page %d of ", writer.getPageNumber());
        float textSize = BASE_FONT.getWidthPoint(text, 12);
        float textBase = document.bottom() - 20;
        cb.beginText();
        cb.setFontAndSize(BASE_FONT, 12);
        float adjust = BASE_FONT.getWidthPoint("000", 12);
        cb.setTextMatrix(document.right() - textSize - adjust, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(pdfTemplate, document.right() - adjust, textBase);
    }

    /** Append total number of pages on each page after the page number. */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        pdfTemplate.beginText();
        pdfTemplate.setFontAndSize(BASE_FONT, 12);
        pdfTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
        pdfTemplate.endText();
    }

    @Override
    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
    }

    @Override
    public void onStartPage(PdfWriter pdfWriter, Document document) {
    }

    @Override
    public void onParagraph(PdfWriter pdfWriter, Document document, float v) {
    }

    @Override
    public void onParagraphEnd(PdfWriter pdfWriter, Document document, float v) {
    }

    @Override
    public void onChapter(PdfWriter pdfWriter, Document document, float v,
            Paragraph paragraph) {
    }

    @Override
    public void onChapterEnd(PdfWriter pdfWriter, Document document, float v) {
    }

    @Override
    public void onSection(PdfWriter pdfWriter, Document document, float v,
            int i, Paragraph paragraph) {
    }

    @Override
    public void onSectionEnd(PdfWriter pdfWriter, Document document, float v) {
    }

    @Override
    public void onGenericTag(PdfWriter pdfWriter, Document document,
            Rectangle rectangle, String string) {
    }
}