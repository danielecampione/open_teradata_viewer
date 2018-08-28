/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerPrintPreview extends JDialog {

    private static final long serialVersionUID = -6423601874747942546L;

    protected int m_wPage;
    protected int m_hPage;
    protected int m_orientation;
    protected Printable m_target;
    protected JComboBox m_cbScale;
    protected PreviewContainer m_preview;
    protected PageFormat m_pageFormat;
    protected int m_pageCount;

    public GraphicViewerPrintPreview(Dialog dialog, Printable printable,
            String s, PageFormat pageformat, int i) {
        super(dialog, s, true);
        initCommon(printable, pageformat, i);
    }

    public GraphicViewerPrintPreview(Frame frame, Printable printable,
            String s, PageFormat pageformat, int i) {
        super(frame, s, true);
        initCommon(printable, pageformat, i);
    }

    private void initCommon(Printable paramPrintable,
            PageFormat paramPageFormat, int paramInt) {
        m_pageFormat = paramPageFormat;
        m_pageCount = paramInt;
        setSize(600, 400);
        getContentPane().setLayout(new BorderLayout());
        m_target = paramPrintable;
        JToolBar localJToolBar = new JToolBar();
        JButton localJButton = new JButton("Print");
        Object localObject = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                Thread local1 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            PrinterJob localPrinterJob = PrinterJob
                                    .getPrinterJob();
                            Book localBook = new Book();
                            localBook
                                    .append(GraphicViewerPrintPreview.this.m_target,
                                            GraphicViewerPrintPreview.this.m_pageFormat,
                                            GraphicViewerPrintPreview.this.m_pageCount);
                            localPrinterJob.setPageable(localBook);
                            if (!localPrinterJob.printDialog()) {
                                return;
                            }
                            GraphicViewerPrintPreview.this.setCursor(Cursor
                                    .getPredefinedCursor(3));
                            localPrinterJob.print();
                            GraphicViewerPrintPreview.this.setCursor(Cursor
                                    .getPredefinedCursor(0));
                            GraphicViewerPrintPreview.this.dispose();
                        } catch (PrinterException localPrinterException) {
                            ExceptionDialog
                                    .hideException(localPrinterException);
                            System.err.println("Printing error: "
                                    + localPrinterException.toString());
                        }
                    }
                };
                local1.start();
            }
        };
        localJButton.addActionListener((ActionListener) localObject);
        localJButton.setAlignmentY(0.5F);
        localJButton.setMargin(new Insets(4, 6, 4, 6));
        localJToolBar.add(localJButton);
        localJButton = new JButton("Close");
        localObject = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                try {
                    GraphicViewerPrintPreview.this.dispose();
                } catch (Exception localException) {
                }
            }
        };
        localJButton.addActionListener((ActionListener) localObject);
        localJButton.setAlignmentY(0.5F);
        localJButton.setMargin(new Insets(4, 6, 4, 6));
        localJToolBar.add(localJButton);
        String[] arrayOfString = { "10 %", "25 %", "50 %", "100 %" };
        m_cbScale = new JComboBox(arrayOfString);
        m_cbScale.setSelectedIndex(1);
        localObject = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                Thread local1 = new Thread() {
                    @Override
                    public void run() {
                        String str = GraphicViewerPrintPreview.this.m_cbScale
                                .getSelectedItem().toString();
                        if (str.endsWith("%")) {
                            str = str.substring(0, str.length() - 1);
                        }
                        str = str.trim();
                        int i = 0;
                        try {
                            i = Integer.parseInt(str);
                        } catch (NumberFormatException localNumberFormatException) {
                            return;
                        }
                        int j = GraphicViewerPrintPreview.this.m_wPage * i
                                / 100;
                        int k = GraphicViewerPrintPreview.this.m_hPage * i
                                / 100;
                        Component[] arrayOfComponent = GraphicViewerPrintPreview.this.m_preview
                                .getComponents();
                        for (int m = 0; m < arrayOfComponent.length; m++) {
                            if ((arrayOfComponent[m] instanceof GraphicViewerPrintPreview.PagePreview)) {
                                GraphicViewerPrintPreview.PagePreview localPagePreview = (GraphicViewerPrintPreview.PagePreview) arrayOfComponent[m];
                                localPagePreview
                                        .setScaledSize(j, k, i / 100.0D);
                            }
                        }
                        GraphicViewerPrintPreview.this.m_preview.doLayout();
                        GraphicViewerPrintPreview.this.m_preview.getParent()
                                .getParent().validate();
                    }
                };
                local1.start();
            }
        };
        m_cbScale.addActionListener((ActionListener) localObject);
        m_cbScale.setMaximumSize(m_cbScale.getPreferredSize());
        m_cbScale.setEditable(true);
        localJToolBar.addSeparator();
        localJToolBar.add(m_cbScale);
        getContentPane().add(localJToolBar, "North");
        m_preview = new PreviewContainer();
        PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
        if ((m_pageFormat.getHeight() == 0.0D)
                || (m_pageFormat.getWidth() == 0.0D)) {
            System.err.println("Unable to determine default page size");
            return;
        }
        m_wPage = ((int) m_pageFormat.getWidth());
        m_hPage = ((int) m_pageFormat.getHeight());
        int i = 25;
        int j = m_wPage * i / 100;
        int k = m_hPage * i / 100;
        double d = i / 100.0D;
        for (int m = 0; m < m_pageCount; m++) {
            PagePreview localPagePreview = new PagePreview(j, k, m_target, d,
                    m_pageFormat, m);
            m_preview.add(localPagePreview);
        }
        JScrollPane localJScrollPane = new JScrollPane(m_preview);
        getContentPane().add(localJScrollPane, "Center");
        setDefaultCloseOperation(2);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class PagePreview extends JPanel {

        private static final long serialVersionUID = -4818801328635261857L;

        private int m_w;
        private int m_h;
        private Printable m_target;
        private double m_scale;
        private PageFormat m_format;
        private int m_pagenum;

        public PagePreview(int i, int j, Printable printable, double d,
                PageFormat pageformat, int k) {
            m_w = i;
            m_h = j;
            m_target = printable;
            m_scale = d;
            m_format = pageformat;
            m_pagenum = k;
            setBackground(Color.white);
            setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
        }

        public void setScaledSize(int i, int j, double d) {
            m_w = i;
            m_h = j;
            m_scale = d;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            Insets insets = getInsets();
            return new Dimension(m_w + insets.left + insets.right, m_h
                    + insets.top + insets.bottom);
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics g1 = g.create();
            if (g1 instanceof Graphics2D) {
                Graphics2D graphics2d = (Graphics2D) g1;
                try {
                    graphics2d.setColor(Color.white);
                    graphics2d.fillRect(0, 0, m_w, m_h);
                    GraphicViewerView graphicviewerview = null;
                    if (m_target instanceof GraphicViewerView) {
                        graphicviewerview = (GraphicViewerView) m_target;
                    }
                    if (graphicviewerview != null) {
                        graphicviewerview.applyRenderingHints(graphics2d);
                    }
                    graphics2d.scale(m_scale, m_scale);
                    m_target.print(graphics2d, m_format, m_pagenum);
                } catch (PrinterException pe) {
                    ExceptionDialog.hideException(pe);
                }
            }
            g1.dispose();
            paintBorder(g);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class PreviewContainer extends JPanel {

        private static final long serialVersionUID = 1854715000318478261L;

        protected int H_GAP;
        protected int V_GAP;

        PreviewContainer() {
            H_GAP = 16;
            V_GAP = 10;
        }

        @Override
        public Dimension getPreferredSize() {
            int i = getComponentCount();
            if (i == 0) {
                return new Dimension(H_GAP, V_GAP);
            }
            Component component = getComponent(0);
            Dimension dimension = component.getPreferredSize();
            int j = dimension.width;
            int k = dimension.height;
            Dimension dimension1 = getParent().getSize();
            int l = Math.max((dimension1.width - H_GAP) / (j + H_GAP), 1);
            int i1 = i / l;
            if (i1 * l < i) {
                i1++;
            }
            int j1 = l * (j + H_GAP) + H_GAP;
            int k1 = i1 * (k + V_GAP) + V_GAP;
            Insets insets = getInsets();
            return new Dimension(j1 + insets.left + insets.right, k1
                    + insets.top + insets.bottom);
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public void doLayout() {
            Insets insets = getInsets();
            int i = insets.left + H_GAP;
            int j = insets.top + V_GAP;
            int k = getComponentCount();
            if (k == 0) {
                return;
            }
            Component component = getComponent(0);
            Dimension dimension = component.getPreferredSize();
            int l = dimension.width;
            int i1 = dimension.height;
            Dimension dimension1 = getParent().getSize();
            int j1 = Math.max((dimension1.width - H_GAP) / (l + H_GAP), 1);
            int k1 = k / j1;
            if (k1 * j1 < k) {
                k1++;
            }
            int l1 = 0;
            for (int i2 = 0; i2 < k1; i2++) {
                for (int j2 = 0; j2 < j1; j2++) {
                    if (l1 >= k) {
                        return;
                    }
                    Component component1 = getComponent(l1++);
                    component1.setBounds(i, j, l, i1);
                    i += l + H_GAP;
                }

                j += i1 + V_GAP;
                i = insets.left + H_GAP;
            }
        }
    }
}