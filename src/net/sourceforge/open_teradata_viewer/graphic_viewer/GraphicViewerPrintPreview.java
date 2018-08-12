/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2013, D. Campione
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

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class PagePreview extends JPanel {

        private static final long serialVersionUID = -4818801328635261857L;

        public void setScaledSize(int i, int j, double d) {
            _fldint = i;
            a = j;
            _fldif = d;
            repaint();
        }

        public Dimension getPreferredSize() {
            Insets insets = getInsets();
            return new Dimension(_fldint + insets.left + insets.right, a
                    + insets.top + insets.bottom);
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public void paintComponent(Graphics g) {
            Graphics g1 = g.create();
            if (g1 instanceof Graphics2D) {
                Graphics2D graphics2d = (Graphics2D) g1;
                try {
                    graphics2d.setColor(Color.white);
                    graphics2d.fillRect(0, 0, _fldint, a);
                    GraphicViewerView graphicviewerview = null;
                    if (_fldfor instanceof GraphicViewerView)
                        graphicviewerview = (GraphicViewerView) _fldfor;
                    if (graphicviewerview != null)
                        graphicviewerview.applyRenderingHints(graphics2d);
                    graphics2d.scale(_fldif, _fldif);
                    _fldfor.print(graphics2d, _flddo, _fldnew);
                } catch (PrinterException pe) {
                    ExceptionDialog.hideException(pe);
                }
            }
            g1.dispose();
            paintBorder(g);
        }

        private int _fldint;
        private int a;
        private Printable _fldfor;
        private double _fldif;
        private PageFormat _flddo;
        private int _fldnew;

        public PagePreview(int i, int j, Printable printable, double d,
                PageFormat pageformat, int k) {
            _fldint = i;
            a = j;
            _fldfor = printable;
            _fldif = d;
            _flddo = pageformat;
            _fldnew = k;
            setBackground(Color.white);
            setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
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

        public Dimension getPreferredSize() {
            int i = getComponentCount();
            if (i == 0)
                return new Dimension(H_GAP, V_GAP);
            Component component = getComponent(0);
            Dimension dimension = component.getPreferredSize();
            int j = dimension.width;
            int k = dimension.height;
            Dimension dimension1 = getParent().getSize();
            int l = Math.max((dimension1.width - H_GAP) / (j + H_GAP), 1);
            int i1 = i / l;
            if (i1 * l < i)
                i1++;
            int j1 = l * (j + H_GAP) + H_GAP;
            int k1 = i1 * (k + V_GAP) + V_GAP;
            Insets insets = getInsets();
            return new Dimension(j1 + insets.left + insets.right, k1
                    + insets.top + insets.bottom);
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public void doLayout() {
            Insets insets = getInsets();
            int i = insets.left + H_GAP;
            int j = insets.top + V_GAP;
            int k = getComponentCount();
            if (k == 0)
                return;
            Component component = getComponent(0);
            Dimension dimension = component.getPreferredSize();
            int l = dimension.width;
            int i1 = dimension.height;
            Dimension dimension1 = getParent().getSize();
            int j1 = Math.max((dimension1.width - H_GAP) / (l + H_GAP), 1);
            int k1 = k / j1;
            if (k1 * j1 < k)
                k1++;
            int l1 = 0;
            for (int i2 = 0; i2 < k1; i2++) {
                for (int j2 = 0; j2 < j1; j2++) {
                    if (l1 >= k)
                        return;
                    Component component1 = getComponent(l1++);
                    component1.setBounds(i, j, l, i1);
                    i += l + H_GAP;
                }

                j += i1 + V_GAP;
                i = insets.left + H_GAP;
            }

        }

        protected int H_GAP;
        protected int V_GAP;

        PreviewContainer() {
            H_GAP = 16;
            V_GAP = 10;
        }
    }

    public GraphicViewerPrintPreview(Dialog dialog, Printable printable,
            String s, PageFormat pageformat, int i) {
        super(dialog, s, true);
        a(printable, pageformat, i);
    }

    public GraphicViewerPrintPreview(Frame frame, Printable printable,
            String s, PageFormat pageformat, int i) {
        super(frame, s, true);
        a(printable, pageformat, i);
    }

    private void a(Printable printable, PageFormat pageformat, int i) {
        m_pageFormat = pageformat;
        m_pageCount = i;
        setSize(600, 400);
        getContentPane().setLayout(new BorderLayout());
        m_target = printable;
        JToolBar jtoolbar = new JToolBar();
        JButton jbutton = new JButton("Print");
        ActionListener actionlistener = new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                Thread thread = new Thread() {

                    public void run() {
                        PrinterJob printerjob1;
                        printerjob1 = PrinterJob.getPrinterJob();
                        Book book = new Book();
                        book.append(m_target, m_pageFormat, m_pageCount);
                        printerjob1.setPageable(book);
                        if (!printerjob1.printDialog())
                            return;
                        try {
                            setCursor(Cursor.getPredefinedCursor(3));
                            printerjob1.print();
                            setCursor(Cursor.getPredefinedCursor(0));
                            dispose();
                        } catch (PrinterException pe) {
                            ExceptionDialog.hideException(pe);
                            System.err.println("Printing error: "
                                    + pe.toString());
                        }
                        return;
                    }
                };
                thread.start();
            }
        };
        jbutton.addActionListener(actionlistener);
        jbutton.setAlignmentY(0.5F);
        jbutton.setMargin(new Insets(4, 6, 4, 6));
        jtoolbar.add(jbutton);
        jbutton = new JButton("Close");
        actionlistener = new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                try {
                    dispose();
                } catch (Exception e) {
                    ExceptionDialog.ignoreException(e);
                }
            }

        };
        jbutton.addActionListener(actionlistener);
        jbutton.setAlignmentY(0.5F);
        jbutton.setMargin(new Insets(4, 6, 4, 6));
        jtoolbar.add(jbutton);
        String as[] = {"10 %", "25 %", "50 %", "100 %"};
        m_cbScale = new JComboBox<Object>(as);
        m_cbScale.setSelectedIndex(1);
        actionlistener = new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                Thread thread = new Thread() {

                    public void run() {
                        String s = m_cbScale.getSelectedItem().toString();
                        if (s.endsWith("%"))
                            s = s.substring(0, s.length() - 1);
                        s = s.trim();
                        int j1 = 0;
                        try {
                            j1 = Integer.parseInt(s);
                        } catch (NumberFormatException nfe) {
                            return;
                        }
                        int k1 = (m_wPage * j1) / 100;
                        int l1 = (m_hPage * j1) / 100;
                        Component acomponent[] = m_preview.getComponents();
                        for (int i2 = 0; i2 < acomponent.length; i2++)
                            if (acomponent[i2] instanceof PagePreview) {
                                PagePreview pagepreview1 = (PagePreview) acomponent[i2];
                                pagepreview1.setScaledSize(k1, l1,
                                        (double) j1 / 100D);
                            }

                        m_preview.doLayout();
                        m_preview.getParent().getParent().validate();
                    }

                };
                thread.start();
            }

        };
        m_cbScale.addActionListener(actionlistener);
        m_cbScale.setMaximumSize(m_cbScale.getPreferredSize());
        m_cbScale.setEditable(true);
        jtoolbar.addSeparator();
        jtoolbar.add(m_cbScale);
        getContentPane().add(jtoolbar, "North");
        m_preview = new PreviewContainer();
        PrinterJob.getPrinterJob();
        if (m_pageFormat.getHeight() == 0.0D || m_pageFormat.getWidth() == 0.0D) {
            System.err.println("Unable to determine default page size");
            return;
        }
        m_wPage = (int) m_pageFormat.getWidth();
        m_hPage = (int) m_pageFormat.getHeight();
        int j = 25;
        int k = (m_wPage * j) / 100;
        int l = (m_hPage * j) / 100;
        double d = (double) j / 100D;
        for (int i1 = 0; i1 < m_pageCount; i1++) {
            PagePreview pagepreview = new PagePreview(k, l, m_target, d,
                    m_pageFormat, i1);
            m_preview.add(pagepreview);
        }

        JScrollPane jscrollpane = new JScrollPane(m_preview);
        getContentPane().add(jscrollpane, "Center");
        setDefaultCloseOperation(2);
    }

    protected int m_wPage;
    protected int m_hPage;
    protected int m_orientation;
    protected Printable m_target;
    protected JComboBox<?> m_cbScale;
    protected PreviewContainer m_preview;
    protected PageFormat m_pageFormat;
    protected int m_pageCount;
}