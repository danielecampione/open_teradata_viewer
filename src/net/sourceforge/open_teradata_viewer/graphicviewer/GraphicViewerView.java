/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerView extends JComponent
        implements
            GraphicViewerObjectCollection,
            Printable,
            ClipboardOwner,
            Autoscroll,
            DragGestureListener,
            DragSourceListener,
            DropTargetListener,
            GraphicViewerDocumentListener,
            KeyListener {

    private static final long serialVersionUID = -6660541999857286497L;

    GraphicViewerDocument getDoc() {
        return (GraphicViewerDocument) getDocument();
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class DragImage extends GraphicViewerImage {

        private static final long serialVersionUID = 2751464933830415091L;

        protected void gainedSelection(
                GraphicViewerSelection graphicviewerselection) {
        }

        protected void lostSelection(
                GraphicViewerSelection graphicviewerselection) {
        }

        public DragImage() {
        }

        public DragImage(Rectangle rectangle) {
            super(rectangle);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class DragRectangle extends GraphicViewerRectangle {

        private static final long serialVersionUID = -1849742512807969104L;

        protected void gainedSelection(
                GraphicViewerSelection graphicviewerselection) {
        }

        protected void lostSelection(
                GraphicViewerSelection graphicviewerselection) {
        }

        public DragRectangle() {
        }

        public DragRectangle(Rectangle rectangle) {
            super(rectangle);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class DragStroke extends GraphicViewerStroke {

        private static final long serialVersionUID = -394329196224286617L;

        protected void gainedSelection(
                GraphicViewerSelection graphicviewerselection) {
        }

        protected void lostSelection(
                GraphicViewerSelection graphicviewerselection) {
        }

        public DragStroke() {
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class GraphicViewerViewHelper
            implements
                MouseListener,
                AdjustmentListener,
                Runnable,
                FocusListener {

        public void mouseClicked(MouseEvent mouseevent) {
        }

        public void mouseReleased(MouseEvent mouseevent) {
        }

        public void mouseEntered(MouseEvent mouseevent) {
        }

        public void mouseExited(MouseEvent mouseevent) {
        }

        public void mousePressed(MouseEvent mouseevent) {
            if (!_flddo.isMouseEnabled())
                return;
            if (!_flddo.hasFocus())
                _flddo.requestFocus();
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
            _flddo.onScrollEvent(adjustmentevent);
        }

        public void run() {
            switch (_fldint) {
                case 0 : // '\0'
                    return;

                case 1 : // '\001'
                    SwingUtilities.invokeLater(_fldtry);
                    return;

                case 2 : // '\002'
                    _flddo.setCursorImmediately(_fldif);
                    return;

                case 3 : // '\003'
                    _fldnew.dropComplete(a);
                    return;

                case 4 : // '\004'
                    _flddo.a(_fldbyte);
                    return;
            }
        }

        public void focusGained(FocusEvent focusevent) {
            _flddo._mthint(_fldfor);
            _flddo.removeFocusListener(this);
        }

        public void focusLost(FocusEvent focusevent) {
        }

        int _fldint;
        Runnable _fldtry;
        GraphicViewerView _flddo;
        Cursor _fldif;
        DropTargetDropEvent _fldnew;
        boolean a;
        PrinterJob _fldbyte;
        GraphicViewerObject _fldfor;

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview) {
            _fldint = 0;
            _flddo = graphicviewerview;
        }

        GraphicViewerViewHelper(Runnable runnable) {
            _fldint = 1;
            _fldtry = runnable;
        }

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview,
                Cursor cursor) {
            _fldint = 2;
            _flddo = graphicviewerview;
            _fldif = cursor;
        }

        GraphicViewerViewHelper(DropTargetDropEvent droptargetdropevent,
                boolean flag) {
            _fldint = 3;
            _fldnew = droptargetdropevent;
            a = flag;
        }

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview,
                PrinterJob printerjob) {
            _fldint = 4;
            _flddo = graphicviewerview;
            _fldbyte = printerjob;
        }

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview,
                GraphicViewerObject graphicviewerobject) {
            _fldint = 5;
            _flddo = graphicviewerview;
            _fldfor = graphicviewerobject;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public class GraphicViewerViewCanvas extends JComponent
            implements
                Autoscroll,
                MouseListener,
                MouseMotionListener,
                DragGestureListener,
                DragSourceListener,
                DropTargetListener {
        private static final long serialVersionUID = -8951907286793166861L;

        public void setBorder(Border border) {
        }

        protected void paintComponent(Graphics g) {
            myView.onPaintComponent(g);
        }

        protected void paintBorder(Graphics g) {
        }

        public void mouseClicked(MouseEvent mouseevent) {
        }

        public void mousePressed(MouseEvent mouseevent) {
            myView.onMousePressed(mouseevent);
        }

        public void mouseReleased(MouseEvent mouseevent) {
            myView.onMouseReleased(mouseevent);
        }

        public void mouseEntered(MouseEvent mouseevent) {
        }

        public void mouseExited(MouseEvent mouseevent) {
        }

        public void mouseMoved(MouseEvent mouseevent) {
            myView.onMouseMoved(mouseevent);
        }

        public void mouseDragged(MouseEvent mouseevent) {
            myView.onMouseDragged(mouseevent);
        }

        public void dragGestureRecognized(DragGestureEvent draggestureevent) {
            myView.onDragGestureRecognized(draggestureevent);
        }

        public void dragEnter(DragSourceDragEvent dragsourcedragevent) {
            myView.onDragEnter(dragsourcedragevent);
        }

        public void dragOver(DragSourceDragEvent dragsourcedragevent) {
            myView.onDragOver(dragsourcedragevent);
        }

        public void dropActionChanged(DragSourceDragEvent dragsourcedragevent) {
            myView.onDropActionChanged(dragsourcedragevent);
        }

        public void dragExit(DragSourceEvent dragsourceevent) {
            myView.onDragExit(dragsourceevent);
        }

        public void dragDropEnd(DragSourceDropEvent dragsourcedropevent) {
            myView.onDragDropEnd(dragsourcedropevent);
        }

        public void dragEnter(DropTargetDragEvent droptargetdragevent) {
            myView.onDragEnter(droptargetdragevent);
        }

        public void dragOver(DropTargetDragEvent droptargetdragevent) {
            myView.onDragOver(droptargetdragevent);
        }

        public void dropActionChanged(DropTargetDragEvent droptargetdragevent) {
            myView.onDropActionChanged(droptargetdragevent);
        }

        public void dragExit(DropTargetEvent droptargetevent) {
            myView.onDragExit(droptargetevent);
        }

        public void drop(DropTargetDropEvent droptargetdropevent) {
            myView.onDrop(droptargetdropevent);
        }

        public Cursor getCursor() {
            return myView.getCursor();
        }

        public Insets getAutoscrollInsets() {
            return myView.getAutoscrollInsets();
        }

        public void autoscroll(Point point) {
            myView.autoscroll(point);
        }

        public JToolTip createToolTip() {
            return myView.createToolTip();
        }

        public String getToolTipText(MouseEvent mouseevent) {
            return myView.getToolTipText(mouseevent);
        }

        public boolean isOptimizedDrawingEnabled() {
            return a == 0;
        }

        protected void addImpl(Component component, Object obj, int i) {
            super.addImpl(component, obj, i);
            a++;
        }

        public void remove(int i) {
            a--;
            super.remove(i);
        }

        public void remove(Component component) {
            a--;
            super.remove(component);
        }

        public void removeAll() {
            a = 0;
            super.removeAll();
        }

        public GraphicViewerView myView;
        private int a;

        public GraphicViewerViewCanvas(GraphicViewerView graphicviewerview1) {
            a = 0;
            setOpaque(true);
            setBackground(null);
            myView = graphicviewerview1;
        }
    }

    public GraphicViewerView() {
        J = null;
        bc = new GraphicViewerObjList(true);
        u = new Point(0, 0);
        ac = 1.0D;
        r = 1.0D;
        M = false;
        ax = false;
        F = new Insets(12, 12, 12, 12);
        aH = false;
        aP = false;
        be = false;
        aO = 3;
        a5 = true;
        av = false;
        T = false;
        bf = true;
        B = false;
        ad = null;
        aC = null;
        bj = true;
        a8 = null;
        aQ = false;
        ab = 0;
        G = 50;
        H = 50;
        bd = 0;
        N = 1;
        aV = new Point(0, 0);
        bs = GraphicViewerPen.lightGray;
        aa = 0;
        aS = 0;
        _mthif(createDefaultModel());
    }

    public GraphicViewerView(GraphicViewerDocument graphicviewerdocument) {
        J = null;
        bc = new GraphicViewerObjList(true);
        u = new Point(0, 0);
        ac = 1.0D;
        r = 1.0D;
        M = false;
        ax = false;
        F = new Insets(12, 12, 12, 12);
        aH = false;
        aP = false;
        be = false;
        aO = 3;
        a5 = true;
        av = false;
        T = false;
        bf = true;
        B = false;
        ad = null;
        aC = null;
        bj = true;
        a8 = null;
        aQ = false;
        ab = 0;
        G = 50;
        H = 50;
        bd = 0;
        N = 1;
        aV = new Point(0, 0);
        bs = GraphicViewerPen.lightGray;
        aa = 0;
        aS = 0;
        _mthif(graphicviewerdocument);
    }

    @SuppressWarnings("rawtypes")
    private final void _mthif(GraphicViewerDocument graphicviewerdocument) {
        at = 0;
        au = new Point(0, 0);
        a1 = new Point(0, 0);
        bg = null;
        al = new Point(0, 0);
        ao = 0;
        br = null;
        ai = null;
        bi = null;
        aN = false;
        Y = false;
        w = null;
        aK = null;
        aj = null;
        aM = null;
        aL = new Point(0, 0);
        aU = new ArrayList();
        az = null;
        am = null;
        aT = null;
        L = null;
        aY = new Rectangle(0, 0, 0, 0);
        aI = false;
        ba = null;
        aE = new Rectangle(0, 0, 0, 0);
        a9 = new Point(0, 0);
        a7 = new Point(0, 0);
        ar = null;
        aA = new ArrayList();
        aq = false;
        I = new Dimension();
        A = new Point(0, 0);
        bq = new java.awt.geom.Rectangle2D.Double();
        V = 1;
        af = 1;
        ap = 1.0D;
        aG = 1.0D;
        Z = false;
        ay = null;
        ah = null;
        bn = null;
        Q = new int[0][];
        P = new int[0][];
        aJ = new Point(0, 0);
        bp = new Dimension();
        aX = new Rectangle(0, 0, 0, 0);
        GraphicViewerGlobal.setComponent(this);
        z = createDefaultSelection();
        setLayout(null);
        J = null;
        U = new GraphicViewerViewCanvas(this);
        add(U);
        setBackground(Color.white);
        JScrollBar jscrollbar = new JScrollBar(1);
        jscrollbar.setSize(jscrollbar.getPreferredSize());
        jscrollbar.setUnitIncrement(getGridHeight());
        setVerticalScrollBar(jscrollbar);
        JScrollBar jscrollbar1 = new JScrollBar(0);
        jscrollbar1.setSize(jscrollbar1.getPreferredSize());
        jscrollbar1.setUnitIncrement(getGridWidth());
        setHorizontalScrollBar(jscrollbar1);
        JPanel jpanel = new JPanel();
        jpanel.setSize(jscrollbar.getWidth(), jscrollbar1.getHeight());
        setCorner(jpanel);
        setPreferredSize(new Dimension(150, 150));
        validate();
        setDocument(graphicviewerdocument);
        setKeyEnabled(true);
        initializeMouseHandling();
    }

    public Frame getFrame() {
        java.awt.Container container;
        for (container = getParent(); container != null
                && !(container instanceof Applet)
                && !(container instanceof Frame); container = container
                .getParent());
        if (container != null) {
            GraphicViewerGlobal.setComponent(container);
            if (container instanceof Frame)
                return (Frame) container;
        }
        return null;
    }

    public void addNotify() {
        super.addNotify();
        Frame frame = getFrame();
        if (frame != null)
            initializeDragDropHandling();
    }

    public GraphicViewerViewCanvas getCanvas() {
        return U;
    }

    public JScrollBar getVerticalScrollBar() {
        return a3;
    }

    public void setVerticalScrollBar(JScrollBar jscrollbar) {
        if (a3 != jscrollbar) {
            if (a3 != null)
                remove(a3);
            a3 = jscrollbar;
            if (a3 != null) {
                add(a3);
                GraphicViewerViewHelper graphicviewerviewhelper = new GraphicViewerViewHelper(
                        this);
                a3.addMouseListener(graphicviewerviewhelper);
                a3.addAdjustmentListener(graphicviewerviewhelper);
            }
            validate();
        }
    }

    public JScrollBar getHorizontalScrollBar() {
        return aB;
    }

    public void setHorizontalScrollBar(JScrollBar jscrollbar) {
        if (aB != jscrollbar) {
            if (aB != null)
                remove(aB);
            aB = jscrollbar;
            if (aB != null) {
                add(aB);
                GraphicViewerViewHelper graphicviewerviewhelper = new GraphicViewerViewHelper(
                        this);
                aB.addMouseListener(graphicviewerviewhelper);
                aB.addAdjustmentListener(graphicviewerviewhelper);
            }
            validate();
        }
    }

    public JComponent getCorner() {
        return K;
    }

    public void setCorner(JComponent jcomponent) {
        if (K != jcomponent) {
            if (K != null)
                remove(K);
            K = jcomponent;
            if (K != null)
                add(K);
            validate();
        }
    }

    public Image getBackgroundImage() {
        return J;
    }

    public void setBackgroundImage(Image image) {
        Image image1 = J;
        if (image1 != image) {
            J = image;
            fireUpdate(112, 0, image1);
        }
    }

    Color _mthelse() {
        Color color = null;
        if (getDocument() != null)
            color = getDocument().getPaperColor();
        if (color == null)
            color = getBackground();
        if (color == null)
            color = GraphicViewerBrush.ColorWhite;
        return color;
    }

    public GraphicViewerDocument createDefaultModel() {
        return new GraphicViewerDocument();
    }

    public GraphicViewerDocument getDocument() {
        return bh;
    }

    public void setDocument(GraphicViewerDocument graphicviewerdocument) {
        if (graphicviewerdocument == null)
            return;
        GraphicViewerDocument graphicviewerdocument1 = bh;
        if (graphicviewerdocument != graphicviewerdocument1) {
            if (graphicviewerdocument1 != null) {
                doCancelMouse();
                doEndEdit();
                getSelection().clearSelection();
                for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = getFirstObjectPos()) {
                    @SuppressWarnings("unused")
                    GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                    removeObjectAtPos(graphicviewerlistposition);
                }

                for (int i = 0; i < aA.size(); i++) {
                    GraphicViewerControl graphicviewercontrol = (GraphicViewerControl) aA
                            .get(i);
                    JComponent jcomponent = graphicviewercontrol
                            .getComponent(this);
                    if (jcomponent != null) {
                        Rectangle rectangle = jcomponent.getBounds();
                        getCanvas().remove(jcomponent);
                        getCanvas().repaint(rectangle.x, rectangle.y,
                                rectangle.width, rectangle.height);
                    }
                }

                aA.clear();
                setViewPosition(0, 0);
                graphicviewerdocument1.removeDocumentListener(this);
            }
            bh = graphicviewerdocument;
            graphicviewerdocument.addDocumentListener(this);
            fireUpdate(1, 0, graphicviewerdocument1);
        }
    }

    public Dimension getDocumentSize() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            Dimension dimension = graphicviewerdocument.getDocumentSize();
            int i = dimension.width;
            int j = dimension.height;
            if (!isIncludingNegativeCoords()) {
                Point point = graphicviewerdocument.getDocumentTopLeft();
                if (point.x < 0)
                    i += point.x;
                if (point.y < 0)
                    j += point.y;
            }
            return new Dimension(i
                    + GraphicViewerHandle.getDefaultHandleWidth(), j
                    + GraphicViewerHandle.getDefaultHandleHeight());
        } else {
            return new Dimension();
        }
    }

    public Point getDocumentTopLeft() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null && isIncludingNegativeCoords())
            return graphicviewerdocument.getDocumentTopLeft();
        else
            return new Point(0, 0);
    }

    public boolean isIncludingNegativeCoords() {
        return M;
    }

    public void setIncludingNegativeCoords(boolean flag) {
        if (M != flag) {
            M = flag;
            fireUpdate(114, flag ? 0 : 1, null);
        }
    }

    public boolean isHidingDisabledScrollbars() {
        return ax;
    }

    public void setHidingDisabledScrollbars(boolean flag) {
        if (ax != flag) {
            ax = flag;
            fireUpdate(113, flag ? 0 : 1, null);
            invalidate();
        }
    }

    public void convertViewToDoc(Point point) {
        Point point1 = getViewPosition();
        point.x = (int) ((double) point.x / b()) + point1.x;
        point.y = (int) ((double) point.y / _mthchar()) + point1.y;
    }

    public void convertViewToDoc(Dimension dimension) {
        dimension.width = (int) Math.ceil((double) dimension.width / b());
        dimension.height = (int) Math.ceil((double) dimension.height
                / _mthchar());
    }

    public void convertViewToDoc(Rectangle rectangle) {
        Point point = getViewPosition();
        rectangle.x = (int) ((double) rectangle.x / b()) + point.x;
        rectangle.y = (int) ((double) rectangle.y / _mthchar()) + point.y;
        rectangle.width = (int) Math.ceil((double) rectangle.width / b());
        rectangle.height = (int) Math.ceil((double) rectangle.height
                / _mthchar());
    }

    public final Point viewToDocCoords(Point point) {
        Point point1 = new Point(point.x, point.y);
        convertViewToDoc(point1);
        return point1;
    }

    public final Dimension viewToDocCoords(Dimension dimension) {
        Dimension dimension1 = new Dimension(dimension);
        convertViewToDoc(dimension1);
        return dimension1;
    }

    public final Rectangle viewToDocCoords(Rectangle rectangle) {
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        convertViewToDoc(rectangle1);
        return rectangle1;
    }

    public final Rectangle viewToDocCoords(Point point, Dimension dimension) {
        Rectangle rectangle = new Rectangle(point.x, point.y, dimension.width,
                dimension.height);
        convertViewToDoc(rectangle);
        return rectangle;
    }

    public final Rectangle viewToDocCoords(int i, int j, int k, int l) {
        Rectangle rectangle = new Rectangle(i, j, k, l);
        convertViewToDoc(rectangle);
        return rectangle;
    }

    public void convertDocToView(Point point) {
        Point point1 = getViewPosition();
        point.x = (int) ((double) (point.x - point1.x) * b());
        point.y = (int) ((double) (point.y - point1.y) * _mthchar());
    }

    public void convertDocToView(Dimension dimension) {
        dimension.width = (int) Math.ceil((double) dimension.width * b());
        dimension.height = (int) Math.ceil((double) dimension.height
                * _mthchar());
    }

    public void convertDocToView(Rectangle rectangle) {
        Point point = getViewPosition();
        rectangle.x = (int) ((double) (rectangle.x - point.x) * b());
        rectangle.y = (int) ((double) (rectangle.y - point.y) * _mthchar());
        rectangle.width = (int) Math.ceil((double) rectangle.width * b());
        rectangle.height = (int) Math.ceil((double) rectangle.height
                * _mthchar());
    }

    public final Point docToViewCoords(Point point) {
        Point point1 = new Point(point.x, point.y);
        convertDocToView(point1);
        return point1;
    }

    public final Dimension docToViewCoords(Dimension dimension) {
        Dimension dimension1 = new Dimension(dimension);
        convertDocToView(dimension1);
        return dimension1;
    }

    public final Rectangle docToViewCoords(Rectangle rectangle) {
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        convertDocToView(rectangle1);
        return rectangle1;
    }

    public final Rectangle docToViewCoords(Point point, Dimension dimension) {
        Rectangle rectangle = new Rectangle(point.x, point.y, dimension.width,
                dimension.height);
        convertDocToView(rectangle);
        return rectangle;
    }

    public final Rectangle docToViewCoords(int i, int j, int k, int l) {
        Rectangle rectangle = new Rectangle(i, j, k, l);
        convertDocToView(rectangle);
        return rectangle;
    }

    public final void setViewPosition(Point point) {
        setViewPosition(point.x, point.y);
    }

    public void setViewPosition(int i, int j) {
        Point point = u;
        if (!isIncludingNegativeCoords()) {
            if (i < 0)
                i = 0;
            if (j < 0)
                j = 0;
        }
        if (point.x != i || point.y != j) {
            Point point1 = new Point(point.x, point.y);
            u.x = i;
            u.y = j;
            fireUpdate(107, 0, point1);
        }
    }

    public Point getViewPosition() {
        return u;
    }

    public Dimension getExtentSize() {
        int i = getCanvas().getWidth();
        int j = getCanvas().getHeight();
        return new Dimension((int) ((double) i / b()),
                (int) ((double) j / _mthchar()));
    }

    public Rectangle getViewRect() {
        Point point = getViewPosition();
        Dimension dimension = getExtentSize();
        return new Rectangle(point.x, point.y, dimension.width,
                dimension.height);
    }

    public void scrollRectToVisible(Rectangle rectangle) {
        Rectangle rectangle1 = getViewRect();
        if (a(rectangle1, rectangle))
            return;
        Dimension dimension = getExtentSize();
        int i;
        if (rectangle.width < dimension.width)
            i = (rectangle.x + rectangle.width / 2) - dimension.width / 2;
        else
            i = rectangle.x;
        int j;
        if (rectangle.height < dimension.height)
            j = (rectangle.y + rectangle.height / 2) - dimension.height / 2;
        else
            j = rectangle.y;
        setViewPosition(i, j);
    }

    public void setScale(double d) {
        double d1 = limitScale(d);
        if (ac != d1 || r != d1) {
            double d2 = ac;
            ac = d1;
            r = d1;
            fireUpdate(108, 0, new Double(d2));
        }
    }

    public double getScale() {
        return ac;
    }

    public double limitScale(double d) {
        if (d < 0.050000000000000003D)
            d = 0.050000000000000003D;
        if (d > 10D)
            d = 10D;
        return d;
    }

    private final double b() {
        return ac;
    }

    private final double _mthchar() {
        return r;
    }

    public GraphicViewerObject pickDocObject(Point point, boolean flag) {
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = getPrevLayer(graphicviewerlayer)) {
            GraphicViewerObject graphicviewerobject = graphicviewerlayer
                    .pickObject(point, flag);
            if (graphicviewerobject != null)
                return graphicviewerobject;
        }

        return null;
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        for (GraphicViewerListPosition graphicviewerlistposition = getLastObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getPrevObjectPos(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                    .pick(point, flag);
            if (graphicviewerobject1 != null)
                return graphicviewerobject1;
        }

        return null;
    }

    public int getNumObjects() {
        return bc.getNumObjects();
    }

    public boolean isEmpty() {
        return bc.isEmpty();
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getLayer() != null)
            return null;
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this) {
                return null;
            } else {
                bc.removeObject(graphicviewerobject);
                GraphicViewerListPosition graphicviewerlistposition = bc
                        .addObjectAtHead(graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition;
            }
        } else {
            GraphicViewerListPosition graphicviewerlistposition1 = bc
                    .addObjectAtHead(graphicviewerobject);
            graphicviewerobject.a(this, graphicviewerobject);
            return graphicviewerlistposition1;
        }
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getLayer() != null)
            return null;
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this) {
                return null;
            } else {
                bc.removeObject(graphicviewerobject);
                GraphicViewerListPosition graphicviewerlistposition = bc
                        .addObjectAtTail(graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition;
            }
        } else {
            GraphicViewerListPosition graphicviewerlistposition1 = bc
                    .addObjectAtTail(graphicviewerobject);
            graphicviewerobject.a(this, graphicviewerobject);
            return graphicviewerlistposition1;
        }
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getLayer() != null)
            return null;
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this)
                return null;
            GraphicViewerListPosition graphicviewerlistposition1 = bc
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition1 != null) {
                bc.removeObjectAtPos(graphicviewerlistposition1);
                GraphicViewerListPosition graphicviewerlistposition3 = bc
                        .insertObjectBefore(graphicviewerlistposition,
                                graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition3;
            }
        }
        GraphicViewerListPosition graphicviewerlistposition2 = bc
                .insertObjectBefore(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.a(this, graphicviewerobject);
        return graphicviewerlistposition2;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getLayer() != null)
            return null;
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this)
                return null;
            GraphicViewerListPosition graphicviewerlistposition1 = bc
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition1 != null) {
                bc.removeObjectAtPos(graphicviewerlistposition1);
                GraphicViewerListPosition graphicviewerlistposition3 = bc
                        .insertObjectAfter(graphicviewerlistposition,
                                graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition3;
            }
        }
        GraphicViewerListPosition graphicviewerlistposition2 = bc
                .insertObjectAfter(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.a(this, graphicviewerobject);
        return graphicviewerlistposition2;
    }

    public void bringObjectToFront(GraphicViewerObject graphicviewerobject) {
        addObjectAtTail(graphicviewerobject);
    }

    public void sendObjectToBack(GraphicViewerObject graphicviewerobject) {
        addObjectAtHead(graphicviewerobject);
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return;
        if (graphicviewerobject.getView() != this)
            return;
        GraphicViewerArea graphicviewerarea = graphicviewerobject.getParent();
        if (graphicviewerarea != null) {
            graphicviewerarea.removeObject(graphicviewerobject);
        } else {
            GraphicViewerListPosition graphicviewerlistposition = findObject(graphicviewerobject);
            if (graphicviewerlistposition != null)
                removeObjectAtPos(graphicviewerlistposition);
        }
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = bc
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject != null)
            graphicviewerobject.a(null, graphicviewerobject);
        return graphicviewerobject;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return bc.getFirstObjectPos();
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return bc.getLastObjectPos();
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        Object obj = graphicviewerlistposition._flddo;
        if (obj instanceof GraphicViewerObjectCollection) {
            GraphicViewerObjectCollection graphicviewerobjectcollection = (GraphicViewerObjectCollection) obj;
            if (!graphicviewerobjectcollection.isEmpty())
                return graphicviewerobjectcollection.getFirstObjectPos();
        }
        GraphicViewerListPosition graphicviewerlistposition1;
        for (graphicviewerlistposition = graphicviewerlistposition.a; graphicviewerlistposition == null; graphicviewerlistposition = graphicviewerlistposition1.a) {
            GraphicViewerArea graphicviewerarea = ((GraphicViewerObject) (obj))
                    .getParent();
            if (graphicviewerarea == null)
                return null;
            graphicviewerlistposition1 = graphicviewerarea.i();
            obj = graphicviewerarea;
        }

        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        for (Object obj = graphicviewerlistposition._flddo; ((GraphicViewerObject) (obj))
                .getParent() != null; obj = ((GraphicViewerObject) (obj))
                .getParent())
            graphicviewerlistposition = ((GraphicViewerObject) (obj))
                    .getParent().i();

        return graphicviewerlistposition.a;
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return bc.getPrevObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return bc.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject.getView() == this)
            return bc.findObject(graphicviewerobject);
        else
            return null;
    }

    public GraphicViewerSelection createDefaultSelection() {
        return new GraphicViewerSelection(this);
    }

    public GraphicViewerSelection getSelection() {
        return z;
    }

    public GraphicViewerObject selectObject(
            GraphicViewerObject graphicviewerobject) {
        return getSelection().selectObject(graphicviewerobject);
    }

    public void selectAll() {
        fireUpdate(37, 0, null);
        @SuppressWarnings("unused")
        GraphicViewerSelection graphicviewerselection = getSelection();
        label0 : for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible())
                continue;
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            do {
                GraphicViewerObject graphicviewerobject;
                do {
                    if (graphicviewerlistposition == null)
                        continue label0;
                    graphicviewerobject = graphicviewerlayer
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerlayer
                            .getNextObjectPosAtTop(graphicviewerlistposition);
                } while (!graphicviewerobject.isVisible()
                        || !graphicviewerobject.isSelectable()
                        && graphicviewerobject.redirectSelection() == graphicviewerobject);
                getSelection().extendSelection(graphicviewerobject);
            } while (true);
        }

        fireUpdate(38, 0, null);
    }

    private boolean a(Rectangle rectangle, Rectangle rectangle1) {
        int i = rectangle.width;
        int j = rectangle.height;
        int k = rectangle1.width;
        int l = rectangle1.height;
        if (i <= 0 || j <= 0 || k < 0 || l < 0) {
            return false;
        } else {
            int i1 = rectangle.x;
            int j1 = rectangle.y;
            int k1 = rectangle1.x;
            int l1 = rectangle1.y;
            return k1 >= i1 && l1 >= j1 && k1 + k <= i1 + i && l1 + l <= j1 + j;
        }
    }

    @SuppressWarnings("rawtypes")
    public void selectInBox(Rectangle rectangle) {
        ArrayList arraylist = new ArrayList();
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible())
                continue;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject = graphicviewerlayer
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerlayer
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                a(graphicviewerobject, rectangle, arraylist);
            }

        }

        for (int i = 0; i < arraylist.size(); i++) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) arraylist
                    .get(i);
            getSelection().extendSelection(graphicviewerobject1);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void a(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle, ArrayList arraylist) {
        if (!graphicviewerobject.isVisible())
            return;
        if (graphicviewerobject.isSelectable()
                && graphicviewerobject.redirectSelection() != null
                && a(rectangle, graphicviewerobject.redirectSelection()
                        .getBoundingRect()))
            arraylist.add(graphicviewerobject);
        else if (graphicviewerobject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerarea
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                a(graphicviewerobject1, rectangle, arraylist);
            }

        }
    }

    public Color getPrimarySelectionColor() {
        if (ad == null)
            return getDefaultPrimarySelectionColor();
        else
            return ad;
    }

    public void setPrimarySelectionColor(Color color) {
        Color color1 = getPrimarySelectionColor();
        if (color == null || !color1.equals(color)) {
            ad = color;
            fireUpdate(109, 0, color1);
        }
    }

    public Color getSecondarySelectionColor() {
        if (aC == null)
            return getDefaultSecondarySelectionColor();
        else
            return aC;
    }

    public void setSecondarySelectionColor(Color color) {
        Color color1 = getSecondarySelectionColor();
        if (color == null || !color1.equals(color)) {
            aC = color;
            fireUpdate(109, 1, color1);
        }
    }

    protected final void onPaintComponent(Graphics g) {
        Graphics g1 = g.create();
        if (g1 instanceof Graphics2D) {
            Graphics2D graphics2d = (Graphics2D) g1;
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            graphics2d.getClipBounds(rectangle);
            convertViewToDoc(rectangle);
            graphics2d.scale(b(), _mthchar());
            Point point = getViewPosition();
            graphics2d.translate(-point.x, -point.y);
            paintView(graphics2d, rectangle);
            Graphics2D graphics2d2 = (Graphics2D) g;
            Color color = graphics2d2.getColor();
            Font font = graphics2d2.getFont();
            graphics2d2.setXORMode(Color.white);
            graphics2d2.setFont(font);
            graphics2d2.setColor(color);
            graphics2d2.setPaintMode();
            graphics2d.dispose();
        } else {
            Dimension dimension = getCanvas().getSize();
            BufferedImage bufferedimage = new BufferedImage(dimension.width,
                    dimension.height, 1);
            Graphics2D graphics2d1 = bufferedimage.createGraphics();
            graphics2d1.scale(b(), _mthchar());
            Point point1 = getViewPosition();
            graphics2d1.translate(-point1.x, -point1.y);
            paintView(graphics2d1, getViewRect());
            graphics2d1.setXORMode(Color.white);
            graphics2d1.setColor(new Color(10, 150, 20));
            graphics2d1.setFont(new Font(GraphicViewerText
                    .getDefaultFontFaceName(), 0, 20));
            graphics2d1.dispose();
            g.drawImage(bufferedimage, 0, 0, getCanvas());
        }
    }

    public void applyRenderingHints(Graphics2D graphics2d) {
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        if (GraphicViewerGlobal.isAtLeastJavaVersion(1.3999999999999999D))
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    protected void paintView(Graphics2D graphics2d, Rectangle rectangle) {
        applyRenderingHints(graphics2d);
        paintPaperColor(graphics2d, rectangle);
        paintBackgroundDecoration(graphics2d, rectangle);
        paintDocumentObjects(graphics2d, rectangle);
        paintViewObjects(graphics2d, rectangle);
    }

    protected void paintPaperColor(Graphics2D graphics2d, Rectangle rectangle) {
        graphics2d.setColor(_mthelse());
        graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width + 5,
                rectangle.height + 5);
    }

    protected void paintBackgroundDecoration(Graphics2D graphics2d,
            Rectangle rectangle) {
        Image image = getBackgroundImage();
        if (image != null)
            graphics2d.drawImage(image, 0, 0, this);
        switch (getGridStyle()) {
            case 3 : // '\003'
                drawGridLines(graphics2d, rectangle);
                break;

            case 1 : // '\001'
                drawGridCrosses(graphics2d, 1, 1, rectangle);
                break;

            case 2 : // '\002'
                drawGridCrosses(graphics2d, 6, 6, rectangle);
                break;
        }
    }

    protected void paintDocumentObjects(Graphics2D graphics2d,
            Rectangle rectangle) {
        float f = 1.0F;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible())
                continue;
            if (graphicviewerlayer.getTransparency() != f) {
                f = graphicviewerlayer.getTransparency();
                AlphaComposite alphacomposite = AlphaComposite
                        .getInstance(3, f);
                graphics2d.setComposite(alphacomposite);
            }
            bn = graphics2d.getFontRenderContext();
            graphicviewerlayer.paint(graphics2d, this, rectangle);
        }

        if (f != 1.0F)
            graphics2d.setComposite(AlphaComposite.SrcOver);
    }

    protected void paintViewObjects(Graphics2D graphics2d, Rectangle rectangle) {
        bn = graphics2d.getFontRenderContext();
        Rectangle rectangle1 = new Rectangle(0, 0, 0, 0);
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject.isVisible()) {
                Rectangle rectangle2 = graphicviewerobject.getBoundingRect();
                rectangle1.x = rectangle2.x;
                rectangle1.y = rectangle2.y;
                rectangle1.width = rectangle2.width;
                rectangle1.height = rectangle2.height;
                graphicviewerobject.expandRectByPenWidth(rectangle1);
                if (rectangle1.intersects(rectangle))
                    graphicviewerobject.paint(graphics2d, this);
            }
        } while (true);
        for (int i = 0; i < aA.size(); i++) {
            GraphicViewerControl graphicviewercontrol = (GraphicViewerControl) aA
                    .get(i);
            graphicviewercontrol.paint(graphics2d, this);
        }

    }

    FontRenderContext _mthnull() {
        return bn;
    }

    public void updateScrollbars() {
        JScrollBar jscrollbar = getHorizontalScrollBar();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if (jscrollbar == null && jscrollbar1 == null)
            return;
        Dimension dimension = getDocumentSize();
        Point point = getDocumentTopLeft();
        Point point1 = getViewPosition();
        Insets insets = getInsets();
        int i = getHeight() - insets.top - insets.bottom;
        int j = getWidth() - insets.left - insets.right;
        int k = (int) ((double) i / _mthchar());
        int l = (int) ((double) j / b());
        boolean flag = k < dimension.height || point1.y > point.y
                || point1.y + k < point.y + dimension.height;
        boolean flag1 = l < dimension.width || point1.x > point.x
                || point1.x + l < point.x + dimension.width;
        if (jscrollbar != null && (_mthdo(jscrollbar) || flag1)) {
            i -= a(jscrollbar);
            k = (int) ((double) i / _mthchar());
        }
        if (jscrollbar1 != null && (_mthdo(jscrollbar1) || flag)) {
            j -= _mthif(jscrollbar1);
            l = (int) ((double) j / b());
        }
        flag = k < dimension.height || point1.y > point.y
                || point1.y + k < point.y + dimension.height;
        flag1 = l < dimension.width || point1.x > point.x
                || point1.x + l < point.x + dimension.width;
        aH = true;
        boolean flag2 = false;
        if (jscrollbar1 != null) {
            jscrollbar1.setValues(point1.y, k, point.y, point.y
                    + dimension.height);
            jscrollbar1.setEnabled(flag);
            if (flag && !_mthdo(jscrollbar1)) {
                jscrollbar1.setVisible(true);
                flag2 = true;
            } else if (!flag && isHidingDisabledScrollbars()
                    && _mthdo(jscrollbar1)) {
                jscrollbar1.setVisible(false);
                flag2 = true;
            }
        }
        if (jscrollbar != null) {
            jscrollbar.setValues(point1.x, l, point.x, point.x
                    + dimension.width);
            jscrollbar.setEnabled(flag1);
            if (flag1 && !_mthdo(jscrollbar)) {
                jscrollbar.setVisible(true);
                flag2 = true;
            } else if (!flag1 && isHidingDisabledScrollbars()
                    && _mthdo(jscrollbar)) {
                jscrollbar.setVisible(false);
                flag2 = true;
            }
        }
        if (flag2)
            validate();
        aH = false;
    }

    public void updateView(Rectangle rectangle) {
        GraphicViewerViewCanvas graphicviewerviewcanvas = getCanvas();
        if (rectangle.x < graphicviewerviewcanvas.getWidth()
                && rectangle.y < graphicviewerviewcanvas.getHeight()
                && rectangle.x + rectangle.width >= 0
                && rectangle.y + rectangle.height >= 0)
            graphicviewerviewcanvas.repaint(rectangle);
    }

    public void updateView() {
        Rectangle rectangle = new Rectangle(0, 0, getCanvas().getWidth(),
                getCanvas().getHeight());
        updateView(rectangle);
        updateScrollbars();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addViewListener(
            GraphicViewerViewListener graphicviewerviewlistener) {
        if (ay == null)
            ay = new ArrayList();
        if (!ay.contains(graphicviewerviewlistener))
            ay.add(graphicviewerviewlistener);
    }

    public void removeViewListener(
            GraphicViewerViewListener graphicviewerviewlistener) {
        if (ay != null)
            ay.remove(graphicviewerviewlistener);
    }

    public GraphicViewerViewListener[] getViewListeners() {
        if (ay == null)
            return null;
        Object aobj[] = ay.toArray();
        GraphicViewerViewListener agraphicviewerviewlistener[] = new GraphicViewerViewListener[aobj.length];
        for (int i = 0; i < aobj.length; i++)
            agraphicviewerviewlistener[i] = (GraphicViewerViewListener) aobj[i];

        return agraphicviewerviewlistener;
    }

    public void addDocumentListener(
            GraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (getDocument() != null)
            getDocument().addDocumentListener(graphicviewerdocumentlistener);
    }

    public void removeDocumentListener(
            GraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (getDocument() != null)
            getDocument().removeDocumentListener(graphicviewerdocumentlistener);
    }

    public GraphicViewerDocumentListener[] getDocumentListeners() {
        return getDocument().getDocumentListeners();
    }

    public final void fireUpdate(int i, int j, Object obj) {
        fireUpdate(i, j, obj, null, null, 0);
    }

    public void fireUpdate(int i, int j, Object obj, Point point, Point point1,
            int k) {
        switch (i) {
            case 1 : // '\001'
                updateView();
                break;

            case 2 : // '\002'
            case 3 : // '\003'
            case 4 : // '\004'
                GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
                Rectangle rectangle = graphicviewerobject.getBoundingRect();
                Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                        rectangle.width, rectangle.height);
                graphicviewerobject.expandRectByPenWidth(rectangle1);
                convertDocToView(rectangle1);
                rectangle1.x--;
                rectangle1.y--;
                rectangle1.width += 2;
                rectangle1.height += 2;
                updateView(rectangle1);
                break;

            case 107 : // 'k'
                updateView();
                break;

            case 108 : // 'l'
                updateView();
                break;

            case 109 : // 'm'
                updateView();
                break;

            case 112 : // 'p'
                updateView();
                break;

            case 124 : // '|'
                updateView();
                break;
        }
        if (ay != null && ay.size() > 0) {
            if (ah == null) {
                ah = new GraphicViewerViewEvent(this, i, j, obj, point, point1,
                        k);
            } else {
                ah._mthdo(i);
                ah._mthif(j);
                ah.a(obj);
                ah.a(point);
                ah._mthif(point1);
                ah.a(k);
                ah.a(false);
            }
            GraphicViewerViewEvent graphicviewerviewevent = ah;
            ah = null;
            a(graphicviewerviewevent);
            ah = graphicviewerviewevent;
            ah.a(null);
        }
    }

    void a(GraphicViewerViewEvent graphicviewerviewevent) {
        if (ay != null) {
            for (int i = 0; i < ay.size(); i++) {
                GraphicViewerViewListener graphicviewerviewlistener = (GraphicViewerViewListener) ay
                        .get(i);
                graphicviewerviewlistener.viewChanged(graphicviewerviewevent);
            }

        }
    }

    public void documentChanged(
            GraphicViewerDocumentEvent graphicviewerdocumentevent) {
        GraphicViewerObject graphicviewerobject = graphicviewerdocumentevent
                .getGraphicViewerObject();
        if (graphicviewerdocumentevent.isBeforeChanging()) {
            if (graphicviewerdocumentevent.getHint() == 203) {
                Rectangle rectangle = graphicviewerdocumentevent.a();
                Rectangle rectangle4 = graphicviewerobject.getBoundingRect();
                rectangle.x = rectangle4.x;
                rectangle.y = rectangle4.y;
                rectangle.width = rectangle4.width;
                rectangle.height = rectangle4.height;
                graphicviewerobject.expandRectByPenWidth(rectangle);
                convertDocToView(rectangle);
                rectangle.x--;
                rectangle.y--;
                rectangle.width += 2;
                rectangle.height += 2;
                updateView(rectangle);
            }
            return;
        }
        switch (graphicviewerdocumentevent.getHint()) {
            case 208 :
            case 214 :
            case 217 :
            default :
                break;

            case 203 :
                Rectangle rectangle1 = graphicviewerdocumentevent.a();
                Rectangle rectangle5 = graphicviewerobject.getBoundingRect();
                rectangle1.x = rectangle5.x;
                rectangle1.y = rectangle5.y;
                rectangle1.width = rectangle5.width;
                rectangle1.height = rectangle5.height;
                graphicviewerobject.expandRectByPenWidth(rectangle1);
                convertDocToView(rectangle1);
                rectangle1.x--;
                rectangle1.y--;
                rectangle1.width += 2;
                rectangle1.height += 2;
                updateView(rectangle1);
                if (graphicviewerdocumentevent.getFlags() == 1) {
                    Rectangle rectangle6 = (Rectangle) graphicviewerdocumentevent
                            .getPreviousValue();
                    rectangle1.x = rectangle6.x;
                    rectangle1.y = rectangle6.y;
                    rectangle1.width = rectangle6.width;
                    rectangle1.height = rectangle6.height;
                    graphicviewerobject.expandRectByPenWidth(rectangle1);
                    convertDocToView(rectangle1);
                    rectangle1.x--;
                    rectangle1.y--;
                    rectangle1.width += 2;
                    rectangle1.height += 2;
                    updateView(rectangle1);
                    GraphicViewerSelection graphicviewerselection = getSelection();
                    if (graphicviewerselection
                            .getNumHandles(graphicviewerobject) <= 0)
                        break;
                    if (graphicviewerobject.isVisible())
                        graphicviewerobject
                                .showSelectionHandles(graphicviewerselection);
                    else
                        graphicviewerobject
                                .hideSelectionHandles(graphicviewerselection);
                    break;
                }
                if (graphicviewerdocumentevent.getFlags() != 2)
                    break;
                GraphicViewerSelection graphicviewerselection1 = getSelection();
                if (!graphicviewerselection1.isSelected(graphicviewerobject))
                    break;
                if (graphicviewerobject.isVisible())
                    graphicviewerobject
                            .showSelectionHandles(graphicviewerselection1);
                else
                    graphicviewerobject
                            .hideSelectionHandles(graphicviewerselection1);
                break;

            case 202 :
                Rectangle rectangle2 = graphicviewerdocumentevent.a();
                Rectangle rectangle7 = graphicviewerobject.getBoundingRect();
                rectangle2.x = rectangle7.x;
                rectangle2.y = rectangle7.y;
                rectangle2.width = rectangle7.width;
                rectangle2.height = rectangle7.height;
                graphicviewerobject.expandRectByPenWidth(rectangle2);
                convertDocToView(rectangle2);
                rectangle2.x--;
                rectangle2.y--;
                rectangle2.width += 2;
                rectangle2.height += 2;
                updateView(rectangle2);
                break;

            case 204 :
                getSelection().clearSelection(graphicviewerobject);
                Rectangle rectangle3 = graphicviewerdocumentevent.a();
                Rectangle rectangle8 = graphicviewerobject.getBoundingRect();
                rectangle3.x = rectangle8.x;
                rectangle3.y = rectangle8.y;
                rectangle3.width = rectangle8.width;
                rectangle3.height = rectangle8.height;
                graphicviewerobject.expandRectByPenWidth(rectangle3);
                convertDocToView(rectangle3);
                rectangle3.x--;
                rectangle3.y--;
                rectangle3.width += 2;
                rectangle3.height += 2;
                updateView(rectangle3);
                break;

            case 205 :
                updateScrollbars();
                break;

            case 209 :
                updateScrollbars();
                break;

            case 100 : // 'd'
            case 108 : // 'l'
            case 110 : // 'n'
            case 206 :
            case 210 :
            case 211 :
            case 212 :
            case 213 :
            case 215 :
            case 218 :
                getSelection().restoreSelectionHandles(null);
                updateView();
                break;
        }
    }

    public void print() {
        getCanvas().setEnabled(false);
        PrinterJob printerjob = null;
        try {
            printerjob = PrinterJob.getPrinterJob();
        } catch (SecurityException securityexception) {
            getCanvas().setEnabled(true);
            return;
        }
        Thread thread = new Thread(
                new GraphicViewerViewHelper(this, printerjob));
        thread.start();
    }

    protected PageFormat printShowPageDialog(PrinterJob printerjob) {
        PageFormat pageformat = printerjob.validatePage(printerjob
                .defaultPage());
        PageFormat pageformat1 = printerjob.pageDialog(pageformat);
        if (pageformat1 != pageformat)
            return pageformat1;
        else
            return null;
    }

    private void a(PrinterJob printerjob) {
        PageFormat pageformat = printShowPageDialog(printerjob);
        if (pageformat != null) {
            printEnd(null, pageformat);
            int i = a(pageformat);
            Book book = new Book();
            book.append(this, pageformat, i);
            printerjob.setPageable(book);
            if (printerjob.printDialog()) {
                boolean flag = isDoubleBuffered();
                try {
                    setDoubleBuffered(false);
                    printerjob.print();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                setDoubleBuffered(flag);
            }
        }
    }

    protected void printBegin(Graphics2D graphics2d, PageFormat pageformat) {
        if (!aq) {
            aq = true;
            I = getPrintDocumentSize();
            A = getPrintDocumentTopLeft();
            int i = I.width;
            int j = I.height;
            bq = getPrintPageRect(graphics2d, pageformat);
            double d = bq.width;
            double d1 = bq.height;
            ap = getPrintScale(graphics2d, pageformat);
            aG = ap;
            V = (int) Math.ceil(((double) i * ap) / d);
            af = (int) Math.ceil(((double) j * aG) / d1);
        }
    }

    public Dimension getPrintDocumentSize() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        Dimension dimension = new Dimension(1, 1);
        if (graphicviewerdocument == null)
            return dimension;
        int i = 0;
        int j = 0;
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerObject graphicviewerobject = graphicviewerdocument
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerdocument
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
            rectangle.x = rectangle1.x;
            rectangle.y = rectangle1.y;
            rectangle.width = rectangle1.width;
            rectangle.height = rectangle1.height;
            graphicviewerobject.expandRectByPenWidth(rectangle);
            if (rectangle.x < i)
                i = rectangle.x;
            if (rectangle.y < j)
                j = rectangle.y;
            if (rectangle.x + rectangle.width > dimension.width)
                dimension.width = rectangle.x + rectangle.width;
            if (rectangle.y + rectangle.height > dimension.height)
                dimension.height = rectangle.y + rectangle.height;
        } while (true);
        if (isIncludingNegativeCoords()) {
            if (i < 0)
                dimension.width -= i;
            if (j < 0)
                dimension.height -= j;
        }
        return dimension;
    }

    public Point getPrintDocumentTopLeft() {
        Point point = new Point(0, 0);
        if (isIncludingNegativeCoords()) {
            GraphicViewerDocument graphicviewerdocument = getDocument();
            if (graphicviewerdocument == null)
                return point;
            int i = point.x;
            int j = point.y;
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null)
                    break;
                GraphicViewerObject graphicviewerobject = graphicviewerdocument
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerdocument
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
                rectangle.x = rectangle1.x;
                rectangle.y = rectangle1.y;
                rectangle.width = rectangle1.width;
                rectangle.height = rectangle1.height;
                graphicviewerobject.expandRectByPenWidth(rectangle);
                if (rectangle.x < i)
                    i = rectangle.x;
                if (rectangle.y < j)
                    j = rectangle.y;
            } while (true);
            point.x = i;
            point.y = j;
        }
        return point;
    }

    protected java.awt.geom.Rectangle2D.Double getPrintPageRect(
            Graphics2D graphics2d, PageFormat pageformat) {
        return new java.awt.geom.Rectangle2D.Double(pageformat.getImageableX(),
                pageformat.getImageableY(), pageformat.getImageableWidth(),
                pageformat.getImageableHeight());
    }

    protected double getPrintScale(Graphics2D graphics2d, PageFormat pageformat) {
        return 1.0D;
    }

    protected void printEnd(Graphics2D graphics2d, PageFormat pageformat) {
        aq = false;
    }

    public int print(Graphics g, PageFormat pageformat, int i) {
        Graphics2D graphics2d = (Graphics2D) g;
        printBegin(graphics2d, pageformat);
        double d = bq.x;
        double d1 = bq.y;
        double d2 = bq.width;
        double d3 = bq.height;
        if (i >= V * af) {
            printEnd(graphics2d, pageformat);
            return 1;
        }
        int j = i % V;
        int k = i / V;
        printDecoration(graphics2d, pageformat, j, k);
        Point point = getViewPosition();
        double d4 = b();
        double d5 = _mthchar();
        try {
            u = new Point((int) ((double) A.x + (double) j * (d2 / ap)),
                    (int) ((double) A.y + (double) k * (d3 / aG)));
            ac = ap;
            r = aG;
            graphics2d.clip(bq);
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            graphics2d.getClipBounds(rectangle);
            rectangle.x -= (int) d;
            rectangle.y -= (int) d1;
            convertViewToDoc(rectangle);
            graphics2d.translate(d, d1);
            graphics2d.scale(b(), _mthchar());
            Point point1 = getViewPosition();
            graphics2d.translate(-point1.x, -point1.y);
            Z = true;
            printView(graphics2d, rectangle);
        } finally {
            Z = false;
            u = point;
            ac = d4;
            r = d5;
        }
        return 0;
    }

    private int a(PageFormat pageformat) {
        java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(
                pageformat.getImageableX(), pageformat.getImageableY(),
                pageformat.getImageableWidth(), pageformat.getImageableHeight());
        Dimension dimension = getPrintDocumentSize();
        int i = dimension.width;
        int j = dimension.height;
        double d = double1.width;
        double d1 = double1.height;
        double d2 = getPrintScale(null, pageformat);
        int k = (int) Math.ceil(((double) i * d2) / d);
        int l = (int) Math.ceil(((double) j * d2) / d1);
        return k * l;
    }

    public boolean isPrinting() {
        return Z;
    }

    protected void printDecoration(Graphics2D graphics2d,
            PageFormat pageformat, int i, int j) {
        double d = bq.x;
        double d1 = bq.y;
        double d2 = bq.width;
        double d3 = bq.height;
        double d4 = d + d2;
        double d5 = d1 + d3;
        java.awt.Paint paint = graphics2d.getPaint();
        graphics2d.setPaint(Color.black);
        java.awt.Stroke stroke = graphics2d.getStroke();
        graphics2d.setStroke(new BasicStroke(0.7F));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d1, d + 10D, d1));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d1, d, d1 + 10D));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d1, d4 - 10D, d1));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d1, d4, d1 + 10D));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d5, d + 10D, d5));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d, d5, d, d5 - 10D));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d5, d4 - 10D, d5));
        graphics2d.draw(new java.awt.geom.Line2D.Double(d4, d5, d4, d5 - 10D));
        graphics2d.setPaint(paint);
        graphics2d.setStroke(stroke);
    }

    protected void printView(Graphics2D graphics2d, Rectangle rectangle) {
        applyRenderingHints(graphics2d);
        paintBackgroundDecoration(graphics2d, rectangle);
        paintDocumentObjects(graphics2d, rectangle);
    }

    public void printPreview() {
        PrinterJob printerjob = PrinterJob.getPrinterJob();
        PageFormat pageformat = printerjob.defaultPage();
        printPreview("Print Preview", pageformat);
    }

    public void printPreview(String s1, PageFormat pageformat) {
        printEnd(null, pageformat);
        int i = a(pageformat);
        Frame frame = getFrame();
        if (frame != null) {
            GraphicViewerPrintPreview graphicviewerprintpreview = new GraphicViewerPrintPreview(
                    frame, this, s1, pageformat, i);
            graphicviewerprintpreview.setVisible(true);
        }
    }

    boolean _mthdo(JScrollBar jscrollbar) {
        return jscrollbar.isVisible();
    }

    int _mthif(JScrollBar jscrollbar) {
        return jscrollbar.getWidth();
    }

    int a(JScrollBar jscrollbar) {
        return jscrollbar.getHeight();
    }

    public void doLayout() {
        super.doLayout();
        JComponent jcomponent = getCorner();
        JScrollBar jscrollbar = getHorizontalScrollBar();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        int i = getWidth();
        int j = getHeight();
        Insets insets = getInsets();
        int k = insets.left;
        int l = insets.top;
        int i1 = 0;
        int j1 = 0;
        if (jscrollbar != null && _mthdo(jscrollbar))
            j1 = jscrollbar.getHeight();
        if (jscrollbar1 != null && _mthdo(jscrollbar1))
            i1 = jscrollbar1.getWidth();
        int k1 = i - i1 - insets.right;
        int l1 = j - j1 - insets.bottom;
        int i2 = k1 - k;
        int j2 = l1 - l;
        getCanvas().setBounds(k, l, i2, j2);
        if (jscrollbar != null && _mthdo(jscrollbar)) {
            jscrollbar.setBounds(k, l1, i2, j1);
            int k2 = jscrollbar.getUnitIncrement();
            int i3 = (int) ((double) i2 / b());
            int k3 = Math.max(k2, i3 - k2);
            jscrollbar.setBlockIncrement(k3);
        }
        if (jscrollbar1 != null && _mthdo(jscrollbar1)) {
            jscrollbar1.setBounds(k1, l, i1, j2);
            int l2 = jscrollbar1.getUnitIncrement();
            int j3 = (int) ((double) j2 / _mthchar());
            int l3 = Math.max(l2, j3 - l2);
            jscrollbar1.setBlockIncrement(l3);
        }
        if (jcomponent != null)
            if (i1 != 0 && j1 != 0) {
                jcomponent.setBounds(k1, l1, i1, j1);
                jcomponent.setVisible(true);
            } else {
                jcomponent.setVisible(false);
            }
        updateScrollbars();
    }

    public void setSize(int i, int j) {
        super.setSize(i, j);
        validate();
    }

    public final void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setBounds(int i, int j, int k, int l) {
        super.setBounds(i, j, k, l);
        validate();
    }

    public final void setBounds(Rectangle rectangle) {
        setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        } else {
            Dimension dimension = getDocumentSize();
            Dimension dimension1 = new Dimension(dimension);
            convertDocToView(dimension1);
            Dimension dimension2 = getMinimumSize();
            dimension1.width = Math.max(dimension1.width, dimension2.width);
            dimension1.height = Math.max(dimension1.height, dimension2.height);
            return dimension1;
        }
    }

    public Dimension getMinimumSize() {
        if (isMinimumSizeSet())
            return super.getMinimumSize();
        else
            return new Dimension(50, 50);
    }

    protected void onScrollEvent(AdjustmentEvent adjustmentevent) {
        if (aH)
            return;
        Point point = getViewPosition();
        int i = point.x;
        int j = point.y;
        JScrollBar jscrollbar = getHorizontalScrollBar();
        if (jscrollbar != null)
            i = jscrollbar.getValue();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if (jscrollbar1 != null)
            j = jscrollbar1.getValue();
        setViewPosition(i, j);
    }

    public void setKeyEnabled(boolean flag) {
        if (flag && !B) {
            B = true;
            addKeyListener(this);
            fireUpdate(115, 0, null);
        } else if (!flag && B) {
            B = false;
            removeKeyListener(this);
            fireUpdate(115, 1, null);
        }
    }

    public boolean isKeyEnabled() {
        return B;
    }

    public void keyPressed(KeyEvent keyevent) {
        if (!isKeyEnabled()) {
            return;
        } else {
            onKeyEvent(keyevent);
            return;
        }
    }

    public void keyTyped(KeyEvent keyevent) {
    }

    public void keyReleased(KeyEvent keyevent) {
    }

    public void onKeyEvent(KeyEvent keyevent) {
        int i = keyevent.getKeyCode();
        if (i == 33 || i == 34) {
            JScrollBar jscrollbar;
            if (keyevent.isControlDown())
                jscrollbar = getHorizontalScrollBar();
            else
                jscrollbar = getVerticalScrollBar();
            if (jscrollbar == null)
                return;
            byte byte0;
            int j;
            if (i == 33) {
                byte0 = -1;
                j = jscrollbar.getMaximum();
            } else {
                byte0 = 1;
                j = jscrollbar.getMinimum();
            }
            int k = jscrollbar.getValue();
            int l = jscrollbar.getBlockIncrement(byte0);
            int i1 = k + byte0 * l;
            if (byte0 == 1)
                i1 = Math.max(i1, j);
            else
                i1 = Math.min(i1, j);
            jscrollbar.setValue(i1);
        }
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public boolean isIgnoreNextMouseDown() {
        return be;
    }

    public void setIgnoreNextMouseDown(boolean flag) {
        be = flag;
    }

    protected boolean keyToggleSelection(int i) {
        return (i & 2) != 0;
    }

    protected boolean keyExtendSelection(int i) {
        return (i & 1) != 0;
    }

    protected boolean keySingleSelection(int i) {
        return true;
    }

    protected final boolean keyMultipleSelect(int i) {
        return keyExtendSelection(i);
    }

    protected boolean keyClearSelection(int i) {
        boolean flag = (i & 1) != 0;
        boolean flag1 = (i & 2) != 0;
        return !flag && !flag1;
    }

    protected void initializeMouseHandling() {
        getCanvas().setToolTipText("");
        addMouseListener(new GraphicViewerViewHelper(this));
        setMouseEnabled(true);
    }

    public void setMouseEnabled(boolean flag) {
        if (flag && !aP) {
            aP = true;
            getCanvas().addMouseListener(getCanvas());
            getCanvas().addMouseMotionListener(getCanvas());
            fireUpdate(116, 0, null);
        } else if (!flag && aP) {
            aP = false;
            getCanvas().removeMouseListener(getCanvas());
            getCanvas().removeMouseMotionListener(getCanvas());
            fireUpdate(116, 1, null);
        }
    }

    public boolean isMouseEnabled() {
        return aP;
    }

    protected final MouseEvent getCurrentMouseEvent() {
        return bg;
    }

    Point a(MouseEvent mouseevent) {
        return mouseevent.getPoint();
    }

    int _mthif(MouseEvent mouseevent) {
        return mouseevent.getModifiers();
    }

    protected void onMousePressed(MouseEvent mouseevent) {
        if (!isMouseEnabled())
            return;
        a("onMouse Pressed", mouseevent);
        boolean flag = hasFocus();
        if (!flag) {
            JInternalFrame jinternalframe = null;
            java.awt.Container container = getParent();
            do {
                if (container == null)
                    break;
                if (container instanceof JInternalFrame) {
                    jinternalframe = (JInternalFrame) container;
                    break;
                }
                container = container.getParent();
            } while (true);
            if (jinternalframe != null
                    && GraphicViewerGlobal.isAtLeastJavaVersion(1.3D)
                    && !GraphicViewerGlobal
                            .isAtLeastJavaVersion(1.3999999999999999D))
                try {
                    jinternalframe.setSelected(false);
                    jinternalframe.setSelected(true);
                } catch (Exception exception) {
                }
            requestFocus();
            if (jinternalframe != null
                    && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
                setIgnoreNextMouseDown(false);
                return;
            }
        }
        boolean flag1 = isIgnoreNextMouseDown();
        setIgnoreNextMouseDown(false);
        if (!flag && flag1)
            return;
        bg = mouseevent;
        Point point = a(mouseevent);
        al.x = point.x;
        al.y = point.y;
        convertViewToDoc(al);
        if (mouseevent.getClickCount() == 1)
            doMouseDown(_mthif(mouseevent), al, point);
        bg = null;
    }

    protected void onMouseReleased(MouseEvent mouseevent) {
        if (!isMouseEnabled())
            return;
        a("onMouse Released", mouseevent);
        aN = false;
        if (!hasFocus() && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
            if (getState() != 0)
                doCancelMouse();
            return;
        }
        bg = mouseevent;
        Point point = a(mouseevent);
        al.x = point.x;
        al.y = point.y;
        convertViewToDoc(al);
        if (mouseevent.getClickCount() <= 1) {
            if (doMouseUp(_mthif(mouseevent), al, point))
                setState(0);
        } else {
            a("onMouse Double Click", mouseevent);
            doMouseDblClick(_mthif(mouseevent), al, point);
        }
        bg = null;
    }

    protected void onMouseMoved(MouseEvent mouseevent) {
        if (!isMouseEnabled())
            return;
        a("onMouse Moved", mouseevent);
        aN = false;
        if (!hasFocus() && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
            if (getState() != 0)
                doCancelMouse();
            setIgnoreNextMouseDown(true);
            return;
        } else {
            bg = mouseevent;
            Point point = a(mouseevent);
            al.x = point.x;
            al.y = point.y;
            convertViewToDoc(al);
            doMouseMove(_mthif(mouseevent), al, point);
            bg = null;
            return;
        }
    }

    protected void onMouseDragged(MouseEvent mouseevent) {
        if (!isMouseEnabled())
            return;
        a("onMouse Dragged", mouseevent);
        if (!hasFocus() && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
            if (getState() != 0)
                doCancelMouse();
            return;
        }
        if (!isDragEnabled() || aN) {
            bg = mouseevent;
            Point point = a(mouseevent);
            al.x = point.x;
            al.y = point.y;
            convertViewToDoc(al);
            doMouseMove(_mthif(mouseevent), al, point);
            bg = null;
        } else {
            aN = true;
        }
    }

    public boolean doMouseDown(int i, Point point, Point point1) {
        a("  doMouse  Down");
        au.x = point.x;
        au.y = point.y;
        GraphicViewerActionObject graphicvieweractionobject = pickActionObject(point);
        if (graphicvieweractionobject != null
                && startActionObject(graphicvieweractionobject, i, point,
                        point1))
            return true;
        if ((i & 0x10) != 0 && getDocument() != null
                && getDocument().isModifiable()) {
            GraphicViewerHandle graphicviewerhandle = pickHandle(point);
            if (graphicviewerhandle != null
                    && startResizing(graphicviewerhandle, point, point1))
                return true;
            GraphicViewerPort graphicviewerport = pickPort(point);
            if (graphicviewerport != null
                    && startNewLink(graphicviewerport, point))
                return true;
        }
        setCurrentObject(pickDocObject(point, true));
        if (getCurrentObject() != null)
            setState(1);
        else
            startDragBoxSelection(i, point, point1);
        return true;
    }

    public boolean doMouseMove(int i, Point point, Point point1) {
        a("  doMouse  Move");
        switch (getState()) {
            default :
                break;

            case 0 : // '\0'
                doUncapturedMouseMove(i, point, point1);
                break;

            case 1 : // '\001'
                if (getCurrentObject() == null)
                    startDragBoxSelection(i, point, point1);
                else
                    startMoveSelection(i, point, point1);
                break;

            case 2 : // '\002'
                int j = point.x - getCurrentObject().getLeft() - a1.x;
                int k = point.y - getCurrentObject().getTop() - a1.y;
                doMoveSelection(i, j, k, 2);
                break;

            case 6 : // '\006'
                Graphics2D graphics2d = getGraphicViewerGraphics();
                a7.x = au.x;
                a7.y = au.y;
                convertDocToView(a7);
                drawXORBox(graphics2d, a7.x, a7.y, point1.x, point1.y, 2);
                disposeGraphicViewerGraphics(graphics2d);
                break;

            case 5 : // '\005'
                Graphics2D graphics2d1 = getGraphicViewerGraphics();
                handleResizing(graphics2d1, point1, point, 2);
                disposeGraphicViewerGraphics(graphics2d1);
                break;

            case 3 : // '\003'
                GraphicViewerPort graphicviewerport = pickNearestPort(point);
                if (graphicviewerport != null) {
                    Point point2 = graphicviewerport.getToLinkPoint(a7);
                    am.setLocation(point2);
                } else {
                    am.setLocation(point);
                }
                break;

            case 4 : // '\004'
                GraphicViewerPort graphicviewerport1 = pickNearestPort(point);
                if (graphicviewerport1 != null) {
                    Point point3 = graphicviewerport1.getFromLinkPoint(a7);
                    am.setLocation(point3);
                } else {
                    am.setLocation(point);
                }
                break;

            case 7 : // '\007'
                handleActionObject(i, point, point1, 2);
                break;
        }
        return true;
    }

    public boolean doMouseUp(int i, Point point, Point point1) {
        a("  doMouse  Up");
        switch (getState()) {
            case 0 : // '\0'
            default :
                break;

            case 1 : // '\001'
                if (getCurrentObject() != null) {
                    if (keyExtendSelection(i))
                        setCurrentObject(getSelection().extendSelection(
                                getCurrentObject()));
                    else if (keyToggleSelection(i))
                        setCurrentObject(getSelection().toggleSelection(
                                getCurrentObject()));
                    else if (keySingleSelection(i))
                        setCurrentObject(selectObject(getCurrentObject()));
                } else if (keyClearSelection(i))
                    getSelection().clearSelection();
                doMouseClick(i, point, point1);
                break;

            case 2 : // '\002'
                int j = point.x - getCurrentObject().getLeft() - a1.x;
                int k = point.y - getCurrentObject().getTop() - a1.y;
                doMoveSelection(i, j, k, 3);
                if (bj)
                    getSelection().restoreSelectionHandles(null);
                break;

            case 6 : // '\006'
                Graphics2D graphics2d = getGraphicViewerGraphics();
                a7.x = au.x;
                a7.y = au.y;
                convertDocToView(a7);
                drawXORBox(graphics2d, a7.x, a7.y, point1.x, point1.y, 3);
                disposeGraphicViewerGraphics(graphics2d);
                if (Math.abs(point1.x - a7.x) < 3
                        && Math.abs(point1.y - a7.y) < 3) {
                    doBackgroundClick(i, point, point1);
                } else {
                    Rectangle rectangle = viewToDocCoords(aY);
                    selectInBox(rectangle);
                }
                break;

            case 5 : // '\005'
                Graphics2D graphics2d1 = getGraphicViewerGraphics();
                handleResizing(graphics2d1, point1, point, 3);
                if (bj)
                    getSelection().restoreSelectionHandles(getCurrentObject());
                disposeGraphicViewerGraphics(graphics2d1);
                break;

            case 3 : // '\003'
                GraphicViewerPort graphicviewerport = pickNearestPort(point);
                if (graphicviewerport != null) {
                    if (aT == null || aT.getLayer() == null) {
                        removeObject(aT);
                        newLink(az, graphicviewerport);
                    } else {
                        reLink(aT, az, graphicviewerport);
                        if (bj)
                            getSelection().restoreSelectionHandles(aT);
                    }
                } else {
                    GraphicViewerPort graphicviewerport2 = pickPort(point);
                    if (aT == null || aT.getLayer() == null) {
                        removeObject(aT);
                        noNewLink(az, graphicviewerport2);
                    } else {
                        noReLink(aT, az, graphicviewerport2);
                    }
                }
                aT = null;
                az = null;
                am = null;
                L = null;
                break;

            case 4 : // '\004'
                GraphicViewerPort graphicviewerport1 = pickNearestPort(point);
                if (graphicviewerport1 != null) {
                    if (aT == null || aT.getLayer() == null) {
                        removeObject(aT);
                        newLink(graphicviewerport1, az);
                    } else {
                        reLink(aT, graphicviewerport1, az);
                        if (bj)
                            getSelection().restoreSelectionHandles(aT);
                    }
                } else {
                    GraphicViewerPort graphicviewerport3 = pickPort(point);
                    if (aT == null || aT.getLayer() == null) {
                        removeObject(aT);
                        noNewLink(graphicviewerport3, az);
                    } else {
                        noReLink(aT, graphicviewerport3, az);
                    }
                }
                aT = null;
                az = null;
                am = null;
                L = null;
                break;

            case 7 : // '\007'
                handleActionObject(i, point, point1, 3);
                break;
        }
        setState(0);
        return true;
    }

    public boolean doMouseClick(int i, Point point, Point point1) {
        Object obj = pickDocObject(point, false);
        if (obj != null) {
            fireUpdate(22, 0, obj, point1, point, i);
            for (; obj != null; obj = ((GraphicViewerObject) (obj)).getParent())
                if (((GraphicViewerObject) (obj)).doMouseClick(i, point,
                        point1, this))
                    return true;

        } else {
            doBackgroundClick(i, point, point1);
        }
        return false;
    }

    public boolean doMouseDblClick(int i, Point point, Point point1) {
        Object obj = pickDocObject(point, false);
        if (obj != null) {
            fireUpdate(23, 0, obj, point1, point, i);
            for (; obj != null; obj = ((GraphicViewerObject) (obj)).getParent())
                if (((GraphicViewerObject) (obj)).doMouseDblClick(i, point,
                        point1, this))
                    return true;

        } else {
            fireUpdate(25, 0, null, point1, point, i);
        }
        return false;
    }

    public void doCancelMouse() {
        a("mouse Cancelled");
        if (!GraphicViewerGlobal.isAtLeastJavaVersion(1.3999999999999999D))
            setIgnoreNextMouseDown(true);
        switch (getState()) {
            case 0 : // '\0'
            case 1 : // '\001'
            default :
                break;

            case 2 : // '\002'
                doCancelMoveSelection(a1);
                if (bj)
                    getSelection().restoreSelectionHandles(null);
                break;

            case 6 : // '\006'
                Graphics2D graphics2d = getGraphicViewerGraphics();
                drawXORBox(graphics2d, 0, 0, 0, 0, 3);
                disposeGraphicViewerGraphics(graphics2d);
                break;

            case 5 : // '\005'
                doCancelResize(aE);
                if (getCurrentObject() != null && bj)
                    getSelection().restoreSelectionHandles(getCurrentObject());
                break;

            case 3 : // '\003'
                if (aT == null || aT.getLayer() == null) {
                    removeObject(aT);
                    noNewLink(az, null);
                } else if (L == null) {
                    noReLink(aT, az, null);
                } else {
                    aT.setToPort(L);
                    if (bj)
                        getSelection().restoreSelectionHandles(aT);
                    if (getDocument() != null)
                        getDocument().endTransaction(false);
                }
                aT = null;
                az = null;
                am = null;
                L = null;
                break;

            case 4 : // '\004'
                if (aT == null || aT.getLayer() == null) {
                    removeObject(aT);
                    noNewLink(null, az);
                } else if (L == null) {
                    noReLink(aT, null, az);
                } else {
                    aT.setFromPort(L);
                    if (bj)
                        getSelection().restoreSelectionHandles(aT);
                    if (getDocument() != null)
                        getDocument().endTransaction(false);
                }
                aT = null;
                az = null;
                am = null;
                L = null;
                break;

            case 7 : // '\007'
                if (getCurrentObject() instanceof GraphicViewerActionObject) {
                    GraphicViewerActionObject graphicvieweractionobject = (GraphicViewerActionObject) getCurrentObject();
                    graphicvieweractionobject.onActionCancelled(this);
                    graphicvieweractionobject.setActionActivated(false);
                }
                break;
        }
        setState(0);
        setCursor(getDefaultCursor());
    }

    public final int getState() {
        return at;
    }

    public void setState(int i) {
        at = i;
    }

    public final GraphicViewerObject getCurrentObject() {
        return br;
    }

    public void setCurrentObject(GraphicViewerObject graphicviewerobject) {
        br = graphicviewerobject;
    }

    public void doUncapturedMouseMove(int i, Point point, Point point1) {
        GraphicViewerObject graphicviewerobject = pickObject(point, false);
        if (graphicviewerobject != null
                && graphicviewerobject.doUncapturedMouseMove(i, point, point1,
                        this))
            return;
        for (Object obj = pickDocObject(point, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent())
            if (((GraphicViewerObject) (obj)).doUncapturedMouseMove(i, point,
                    point1, this))
                return;

        setCursor(getDefaultCursor());
    }

    public void setCursor(Cursor cursor) {
        if (getCursor() == cursor)
            return;
        if (GraphicViewerGlobal.isAtLeastJavaVersion(1.3D)) {
            setCursorImmediately(cursor);
        } else {
            Thread thread = new Thread(new GraphicViewerViewHelper(
                    new GraphicViewerViewHelper(this, cursor)));
            thread.start();
        }
    }

    public void setCursorImmediately(Cursor cursor) {
        super.setCursor(cursor);
    }

    void a(int i) {
        setCursor(Cursor.getPredefinedCursor(i));
    }

    public Cursor getDefaultCursor() {
        if (a8 == null)
            a8 = Cursor.getPredefinedCursor(0);
        return a8;
    }

    public void setDefaultCursor(Cursor cursor) {
        Cursor cursor1 = a8;
        if (cursor1 != cursor) {
            a8 = cursor;
            fireUpdate(119, 0, cursor1);
        }
    }

    public void doBackgroundClick(int i, Point point, Point point1) {
        fireUpdate(24, 0, null, point1, point, i);
    }

    public void doMoveSelection(int i, int j, int k, int l) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null
                && graphicviewerdocument.isModifiable()) {
            if (l == 1)
                graphicviewerdocument.startTransaction();
            int i1 = convertActionToModifiers(1);
            boolean flag = (i & i1) == i1;
            int j1 = getInternalMouseActions();
            boolean flag1 = (j1 & 1) != 0;
            boolean flag2 = j1 == 1;
            boolean flag3 = flag2 | flag1 & flag;
            if (l != 3) {
                if (flag3 || !isDragsRealtime()) {
                    _mthbyte();
                    moveSelection(_mthgoto(), i, j, k, l);
                } else {
                    _mthcase();
                    moveSelection(getSelection(), i, j, k, l);
                }
            } else {
                int k1 = j;
                int l1 = k;
                if (ai != null) {
                    k1 = getCurrentObject().getLeft() - bi.getLeft();
                    l1 = getCurrentObject().getTop() - bi.getTop();
                }
                _mthcase();
                if (flag3) {
                    copySelection(getSelection(), i, k1, l1, l);
                    fireUpdate(27, 0, null);
                    graphicviewerdocument
                            .endTransaction(getEditPresentationName(2));
                } else {
                    moveSelection(getSelection(), i, k1, l1, l);
                    fireUpdate(26, 0, null);
                    graphicviewerdocument
                            .endTransaction(getEditPresentationName(1));
                }
            }
        }
    }

    public boolean isDragsRealtime() {
        return bf;
    }

    public void setDragsRealtime(boolean flag) {
        boolean flag1 = bf;
        if (flag1 != flag) {
            bf = flag;
            fireUpdate(120, flag1 ? 1 : 0, null);
        }
    }

    public int getInternalMouseActions() {
        return aO;
    }

    public void setInternalMouseActions(int i) {
        int j = aO;
        if (j != i) {
            aO = i;
            fireUpdate(121, j, null);
        }
    }

    public boolean isDragsSelectionImage() {
        return a5;
    }

    public void setDragsSelectionImage(boolean flag) {
        boolean flag1 = a5;
        if (flag1 != flag) {
            a5 = flag;
            fireUpdate(122, flag1 ? 1 : 0, null);
        }
    }

    GraphicViewerSelection _mthgoto() {
        return ai;
    }

    GraphicViewerSelection _mthtry() {
        if (getCurrentObject() == null)
            return null;
        GraphicViewerSelection graphicviewerselection = new GraphicViewerSelection(
                this);
        if (isDragsSelectionImage()) {
            DragRectangle dragrectangle = new DragRectangle(getCurrentObject()
                    .getBoundingRect());
            dragrectangle.setVisible(false);
            addObjectAtTail(dragrectangle);
            graphicviewerselection.extendSelection(dragrectangle);
            setCurrentObject(dragrectangle);
            GraphicViewerSelection graphicviewerselection2 = getSelection();
            GraphicViewerObject agraphicviewerobject[] = graphicviewerselection2
                    .toArray();
            getDocument().sortByZOrder(agraphicviewerobject);
            GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                    agraphicviewerobject);
            Rectangle rectangle = GraphicViewerDocument
                    .computeBounds(graphicviewercollection);
            int i = Math.max(1, (int) Math.ceil(rectangle.width));
            int j = Math.max(1, (int) Math.ceil(rectangle.height));
            Rectangle rectangle1 = new Rectangle(0, 0, 0, 0);
            float f = 1.0F;
            BufferedImage bufferedimage = new BufferedImage(i, j, 2);
            Graphics2D graphics2d = bufferedimage.createGraphics();
            graphics2d.translate(-rectangle.x, -rectangle.y);
            applyRenderingHints(graphics2d);
            GraphicViewerListPosition graphicviewerlistposition1 = graphicviewercollection
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition1 == null)
                    break;
                GraphicViewerObject graphicviewerobject1 = graphicviewercollection
                        .getObjectAtPos(graphicviewerlistposition1);
                graphicviewerlistposition1 = graphicviewercollection
                        .getNextObjectPosAtTop(graphicviewerlistposition1);
                graphicviewerobject1 = graphicviewerobject1.getDraggingObject();
                if (graphicviewerobject1.isVisible()) {
                    GraphicViewerLayer graphicviewerlayer = graphicviewerobject1
                            .getLayer();
                    if (graphicviewerlayer != null
                            && graphicviewerlayer.isVisible()) {
                        if (graphicviewerlayer.getTransparency() != f) {
                            f = graphicviewerlayer.getTransparency();
                            AlphaComposite alphacomposite = AlphaComposite
                                    .getInstance(3, f);
                            graphics2d.setComposite(alphacomposite);
                        }
                        bn = graphics2d.getFontRenderContext();
                        Rectangle rectangle2 = graphicviewerobject1
                                .getBoundingRect();
                        rectangle1.x = rectangle2.x;
                        rectangle1.y = rectangle2.y;
                        rectangle1.width = rectangle2.width;
                        rectangle1.height = rectangle2.height;
                        graphicviewerobject1.expandRectByPenWidth(rectangle1);
                        if (rectangle1.intersects(rectangle))
                            graphicviewerobject1.paint(graphics2d, this);
                    }
                }
            } while (true);
            graphics2d.dispose();
            DragImage dragimage = new DragImage(rectangle);
            dragimage.setImage(bufferedimage);
            bufferedimage.flush();
            addObjectAtTail(dragimage);
            graphicviewerselection.extendSelection(dragimage);
        } else {
            GraphicViewerSelection graphicviewerselection1 = getSelection();
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerselection1
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null)
                    break;
                GraphicViewerObject graphicviewerobject = graphicviewerselection1
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerselection1
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                Object obj;
                if (graphicviewerobject instanceof GraphicViewerLink) {
                    DragStroke dragstroke = new DragStroke();
                    GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) graphicviewerobject;
                    for (int k = 0; k < graphicviewerstroke.getNumPoints(); k++) {
                        Point point = graphicviewerstroke.getPoint(k);
                        dragstroke.addPoint(point);
                    }

                    obj = dragstroke;
                } else {
                    obj = new DragRectangle(
                            graphicviewerobject.getBoundingRect());
                }
                addObjectAtTail(((GraphicViewerObject) (obj)));
                graphicviewerselection
                        .extendSelection(((GraphicViewerObject) (obj)));
                if (getCurrentObject() == graphicviewerobject)
                    setCurrentObject(((GraphicViewerObject) (obj)));
            } while (true);
        }
        return graphicviewerselection;
    }

    void _mthbyte() {
        if (ai == null) {
            bi = getCurrentObject();
            ai = _mthtry();
            if (ai.isEmpty()) {
                bi = null;
                ai = null;
                return;
            }
            moveSelection(getSelection(), 0, au.x - a1.x - bi.getLeft(), au.y
                    - a1.y - bi.getTop(), 2);
            if (getCurrentObject().getView() != this)
                setCurrentObject(ai.getObjectAtPos(ai.getFirstObjectPos()));
        }
    }

    void _mthcase() {
        if (ai != null) {
            for (GraphicViewerListPosition graphicviewerlistposition = ai
                    .getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = ai
                    .getNextObjectPosAtTop(graphicviewerlistposition)) {
                GraphicViewerObject graphicviewerobject = ai
                        .getObjectAtPos(graphicviewerlistposition);
                removeObject(graphicviewerobject);
            }

            ai = null;
            setCurrentObject(bi);
            bi = null;
        }
    }

    @SuppressWarnings("rawtypes")
    public void moveSelection(GraphicViewerSelection graphicviewerselection,
            int i, int j, int k, int l) {
        if (graphicviewerselection == null)
            graphicviewerselection = getSelection();
        if (graphicviewerselection.isEmpty())
            return;
        boolean flag = false;
        int i1 = getSnapMove();
        if (i1 == 1 || i1 == 2 && l == 3)
            flag = true;
        ArrayList arraylist = computeEffectiveSelection(graphicviewerselection,
                true, true);
        Point point = new Point(0, 0);
        int j1 = getGridSpot();
        GraphicViewerObject graphicviewerobject = null;
        int k1 = 0;
        do {
            if (k1 >= arraylist.size())
                break;
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) arraylist
                    .get(k1);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject = graphicviewerobject1;
                break;
            }
            k1++;
        } while (true);
        k1 = j;
        int l1 = k;
        if (graphicviewerobject != null && flag) {
            Point point1 = graphicviewerobject.getSpotLocation(j1, point);
            int k2 = point1.x;
            int l2 = point1.y;
            findNearestGridPoint(k2 + j, l2 + k, point);
            int k3 = point.x;
            int j4 = point.y;
            k1 = k3 - k2;
            l1 = j4 - l2;
        }
        for (int i2 = 0; i2 < arraylist.size(); i2++) {
            GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) arraylist
                    .get(i2);
            if (graphicviewerobject2 instanceof GraphicViewerLink) {
                int i3 = graphicviewerobject2.getLeft();
                int l3 = graphicviewerobject2.getTop();
                graphicviewerobject2.handleMove(this, i, l, 1, i3, l3, i3 + k1,
                        l3 + l1);
            }
        }

        for (int j2 = 0; j2 < arraylist.size(); j2++) {
            GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) arraylist
                    .get(j2);
            if (graphicviewerobject3 instanceof GraphicViewerLink)
                continue;
            point = graphicviewerobject3.getSpotLocation(j1, point);
            int j3 = point.x;
            int i4 = point.y;
            int k4 = j3 + j;
            int l4 = i4 + k;
            if (flag) {
                point = findNearestGridPoint(k4, l4, point);
                k4 = point.x;
                l4 = point.y;
            }
            graphicviewerobject3.handleMove(this, i, l, j1, j3, i4, k4, l4);
        }

    }

    @SuppressWarnings("rawtypes")
    public ArrayList computeEffectiveSelection(
            GraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        return computeEffectiveSelection(graphicviewerobjectsimplecollection,
                true, true);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ArrayList computeEffectiveSelection(
            GraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            boolean flag, boolean flag1) {
        HashMap hashmap = new HashMap();
        ArrayList arraylist = null;
        ArrayList arraylist1 = new ArrayList();
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                    .getDraggingObject();
            if (graphicviewerobject1 != null
                    && (!flag || graphicviewerobject1.isDraggable())
                    && !a(hashmap, graphicviewerobject1)) {
                if (arraylist != null
                        && (graphicviewerobject1 instanceof GraphicViewerArea)) {
                    for (int j = 0; j < arraylist.size();) {
                        GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) arraylist
                                .get(j);
                        if (graphicviewerobject3
                                .isChildOf(graphicviewerobject1)) {
                            hashmap.remove(graphicviewerobject3);
                            arraylist.remove(j);
                            arraylist1.remove(graphicviewerobject3);
                        } else {
                            j++;
                        }
                    }

                }
                hashmap.put(graphicviewerobject1, graphicviewerobject1);
                if (!graphicviewerobject1.isTopLevel()) {
                    if (arraylist == null)
                        arraylist = new ArrayList();
                    arraylist.add(graphicviewerobject1);
                }
                arraylist1.add(graphicviewerobject1);
            }
        } while (true);
        if (flag1) {
            for (int i = 0; i < arraylist1.size(); i++) {
                GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) arraylist1
                        .get(i);
                a(graphicviewerobject2, hashmap, arraylist1);
            }

        }
        return arraylist1;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void a(GraphicViewerObject graphicviewerobject, HashMap hashmap,
            ArrayList arraylist) {
        if (graphicviewerobject instanceof GraphicViewerPort) {
            GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerport
                    .getFirstLinkPos();
            do {
                if (graphicviewerlistposition == null)
                    break;
                GraphicViewerLink graphicviewerlink = graphicviewerport
                        .getLinkAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerport
                        .getNextLinkPos(graphicviewerlistposition);
                GraphicViewerPort graphicviewerport1 = graphicviewerlink
                        .getOtherPort(graphicviewerport);
                if (a(hashmap, ((GraphicViewerObject) (graphicviewerport1)))
                        && !a(hashmap,
                                ((GraphicViewerObject) (graphicviewerlink)))) {
                    hashmap.put(graphicviewerlink, graphicviewerlink);
                    arraylist.add(graphicviewerlink);
                }
            } while (true);
        }
        if (graphicviewerobject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition1 != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition1);
                graphicviewerlistposition1 = graphicviewerarea
                        .getNextObjectPosAtTop(graphicviewerlistposition1);
                a(graphicviewerobject1, hashmap, arraylist);
            }

        }
    }

    @SuppressWarnings("rawtypes")
    private boolean a(HashMap hashmap, GraphicViewerObject graphicviewerobject) {
        for (Object obj = graphicviewerobject; obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent())
            if (hashmap.containsKey(obj))
                return true;

        return false;
    }

    public void doCancelMoveSelection(Point point) {
        _mthcase();
        if (getCurrentObject() != null)
            moveSelection(getSelection(), 0, au.x - point.x
                    - getCurrentObject().getLeft(), au.y - point.y
                    - getCurrentObject().getTop(), 3);
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(false);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void copySelection(GraphicViewerSelection graphicviewerselection,
            int i, int j, int k, int l) {
        if (graphicviewerselection == null)
            graphicviewerselection = getSelection();
        if (graphicviewerselection.isEmpty())
            return;
        @SuppressWarnings("unused")
        boolean flag = false;
        int i1 = getSnapMove();
        if (i1 == 1 || i1 == 2 && l == 3)
            flag = true;
        GraphicViewerDocument graphicviewerdocument = getDocument();
        Point point = new Point(j, k);
        ArrayList arraylist = computeEffectiveSelection(graphicviewerselection,
                false, false);
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[arraylist
                .size()];
        for (int j1 = 0; j1 < arraylist.size(); j1++)
            agraphicviewerobject[j1] = (GraphicViewerObject) arraylist.get(j1);

        graphicviewerdocument.sortByZOrder(agraphicviewerobject);
        GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                agraphicviewerobject);
        GraphicViewerCopyEnvironment graphicviewercopyenvironment = graphicviewerdocument
                .createDefaultCopyEnvironment();
        graphicviewerdocument.copyFromCollection(graphicviewercollection,
                point, graphicviewercopyenvironment);
        ArrayList arraylist1 = aU;
        arraylist1.clear();
        Iterator iterator = graphicviewercopyenvironment.entrySet().iterator();
        do {
            if (!iterator.hasNext())
                break;
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            if (entry.getKey() instanceof GraphicViewerObject) {
                GraphicViewerObject graphicviewerobject = (GraphicViewerObject) entry
                        .getKey();
                if (graphicviewerselection.isSelected(graphicviewerobject)
                        && (entry.getValue() instanceof GraphicViewerObject)) {
                    GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) entry
                            .getValue();
                    arraylist1.add(graphicviewerobject1);
                }
            }
        } while (true);
        fireUpdate(37, 0, null);
        graphicviewerselection.clearSelection();
        for (int k1 = 0; k1 < arraylist1.size(); k1++)
            graphicviewerselection
                    .extendSelection((GraphicViewerObject) arraylist1.get(k1));

        fireUpdate(38, 0, null);
        arraylist1.clear();
    }

    protected boolean startMoveSelection(int i, Point point, Point point1) {
        if (!getSelection().isInSelection(getCurrentObject()))
            setCurrentObject(selectObject(getCurrentObject()));
        if (getCurrentObject() == null)
            return false;
        if (!getCurrentObject().isDraggable())
            return false;
        if (getCurrentObject().getLayer() != null
                && !getCurrentObject().getLayer().isModifiable())
            return false;
        if ((getInternalMouseActions() & 3) == 0)
            return false;
        if (bj)
            getSelection().clearSelectionHandles(null);
        a1.x = au.x - getCurrentObject().getLeft();
        a1.y = au.y - getCurrentObject().getTop();
        doMoveSelection(i, 1, 1, 1);
        setState(2);
        return true;
    }

    protected boolean startDragBoxSelection(int i, Point point, Point point1) {
        if (keyClearSelection(i))
            getSelection().clearSelection();
        setState(6);
        Graphics2D graphics2d = getGraphicViewerGraphics();
        drawXORBox(graphics2d, point1.x, point1.y, point1.x, point1.y, 1);
        disposeGraphicViewerGraphics(graphics2d);
        return true;
    }

    protected boolean startResizing(GraphicViewerHandle graphicviewerhandle,
            Point point, Point point1) {
        if (graphicviewerhandle == null
                || graphicviewerhandle.getHandleType() == -1)
            return false;
        GraphicViewerObject graphicviewerobject = graphicviewerhandle
                .getHandleFor();
        if (graphicviewerobject.getLayer() == null
                || graphicviewerobject.getLayer().isModifiable()) {
            if (getDocument() != null)
                getDocument().startTransaction();
            setCurrentObject(graphicviewerobject);
            ao = graphicviewerhandle.getHandleType();
            if (bj)
                getSelection().clearSelectionHandles(getCurrentObject());
            setState(5);
            Graphics2D graphics2d = getGraphicViewerGraphics();
            handleResizing(graphics2d, point1, point, 1);
            disposeGraphicViewerGraphics(graphics2d);
            return true;
        } else {
            return false;
        }
    }

    protected void handleResizing(Graphics2D graphics2d, Point point,
            Point point1, int i) {
        int j = getSnapResize();
        Point point2;
        if (j == 1 || j == 2 && i == 3) {
            point2 = findNearestGridPoint(point1.x, point1.y, null);
            @SuppressWarnings("unused")
            Point point3 = docToViewCoords(point2);
        } else {
            point2 = point1;
            @SuppressWarnings("unused")
            Point point4 = point;
        }
        if (getCurrentObject() == null || !getCurrentObject().isResizable())
            return;
        Rectangle rectangle = null;
        if (i == 1) {
            Rectangle rectangle1 = getCurrentObject().getBoundingRect();
            aE.x = rectangle1.x;
            aE.y = rectangle1.y;
            aE.width = rectangle1.width;
            aE.height = rectangle1.height;
            a9.x = point2.x;
            a9.y = point2.y;
            rectangle = new Rectangle(aE.x, aE.y, aE.width, aE.height);
        }
        Rectangle rectangle2 = getCurrentObject().handleResize(graphics2d,
                this, aE, point2, ao, i, 0, 0);
        if (rectangle2 != null)
            if (rectangle != null) {
                convertDocToView(rectangle);
                drawXORBox(graphics2d, rectangle.x, rectangle.y, rectangle.x
                        + rectangle.width, rectangle.y + rectangle.height, 1);
            } else {
                convertDocToView(rectangle2);
                drawXORBox(graphics2d, rectangle2.x, rectangle2.y, rectangle2.x
                        + rectangle2.width, rectangle2.y + rectangle2.height, i);
            }
        if (i == 3 && getState() == 5)
            fireUpdate(30, 0, getCurrentObject());
        if ((i == 3 || getState() != 5) && getDocument() != null)
            getDocument().endTransaction(getEditPresentationName(3));
    }

    public void doCancelResize(Rectangle rectangle) {
        Graphics2D graphics2d = getGraphicViewerGraphics();
        if (getCurrentObject() != null)
            getCurrentObject().handleResize(graphics2d, this, rectangle, a9,
                    ao, 2, 0, 0);
        drawXORBox(graphics2d, 0, 0, 0, 0, 3);
        disposeGraphicViewerGraphics(graphics2d);
        if (getCurrentObject() != null)
            getCurrentObject().setBoundingRect(rectangle);
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(false);
    }

    public GraphicViewerActionObject pickActionObject(Point point) {
        for (Object obj = pickDocObject(point, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            if (!(obj instanceof GraphicViewerActionObject))
                continue;
            GraphicViewerActionObject graphicvieweractionobject = (GraphicViewerActionObject) obj;
            if (graphicvieweractionobject.isActionEnabled())
                return graphicvieweractionobject;
        }

        return null;
    }

    public boolean startActionObject(
            GraphicViewerActionObject graphicvieweractionobject, int i,
            Point point, Point point1) {
        setCurrentObject((GraphicViewerObject) graphicvieweractionobject);
        graphicvieweractionobject.setActionActivated(true);
        setState(7);
        handleActionObject(i, point, point1, 1);
        return true;
    }

    public void handleActionObject(int i, Point point, Point point1, int j) {
        if (getCurrentObject() instanceof GraphicViewerActionObject) {
            GraphicViewerActionObject graphicvieweractionobject = (GraphicViewerActionObject) getCurrentObject();
            graphicvieweractionobject.onActionAdjusted(this, i, point, point1,
                    j);
            if (j == 3) {
                graphicvieweractionobject.onAction(this, i, point, point1);
                graphicvieweractionobject.setActionActivated(false);
            }
        }
    }

    public String getToolTipText(MouseEvent mouseevent) {
        if (!isMouseEnabled())
            return null;
        Point point = a(mouseevent);
        al.x = point.x;
        al.y = point.y;
        convertViewToDoc(al);
        for (Object obj = pickDocObject(al, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            String s1 = ((GraphicViewerObject) (obj)).getToolTipText();
            if (s1 != null)
                return s1;
        }

        return null;
    }

    public void setEditControl(GraphicViewerTextEdit graphicviewertextedit) {
        GraphicViewerTextEdit graphicviewertextedit1 = ar;
        if (graphicviewertextedit1 != graphicviewertextedit) {
            if (graphicviewertextedit1 != null)
                removeObject(graphicviewertextedit1);
            ar = graphicviewertextedit;
            if (graphicviewertextedit != null)
                addObjectAtTail(graphicviewertextedit);
        }
    }

    public GraphicViewerTextEdit getEditControl() {
        return ar;
    }

    public boolean isEditingTextControl() {
        return getEditControl() != null;
    }

    public void doEndEdit() {
        if (isEditingTextControl())
            getEditControl().doEndEdit();
    }

    void _mthint(GraphicViewerObject graphicviewerobject) {
        fireUpdate(33, 0, graphicviewerobject);
        if (getDocument() != null)
            getDocument().endTransaction(getEditPresentationName(7));
    }

    @SuppressWarnings("unchecked")
    void a(GraphicViewerControl graphicviewercontrol, JComponent jcomponent) {
        if (!aA.contains(graphicviewercontrol)) {
            aA.add(graphicviewercontrol);
            getCanvas().add(jcomponent);
        }
    }

    void _mthif(GraphicViewerControl graphicviewercontrol, JComponent jcomponent) {
        aA.remove(graphicviewercontrol);
        Rectangle rectangle = jcomponent.getBounds();
        getCanvas().remove(jcomponent);
        getCanvas().repaint(rectangle.x, rectangle.y, rectangle.width,
                rectangle.height);
    }

    protected void drawXORRect(Graphics2D graphics2d, int i, int j, int k, int l) {
        if (graphics2d == null) {
            return;
        } else {
            graphics2d.setXORMode(GraphicViewerBrush.ColorWhite);
            GraphicViewerDrawable.drawRect(graphics2d,
                    GraphicViewerPen.darkGray, null, i, j, k, l);
            graphics2d.setPaintMode();
            return;
        }
    }

    public boolean isDrawsXorMode() {
        return aQ;
    }

    public void setDrawsXorMode(boolean flag) {
        aQ = flag;
    }

    protected void drawXORBox(Graphics2D graphics2d, int i, int j, int k,
            int l, int i1) {
        if (aI) {
            aI = false;
            if (aY.width != 0 || aY.height != 0)
                drawXORRect(graphics2d, aY.x, aY.y, aY.width, aY.height);
        }
        if (i1 != 3) {
            aY.x = Math.min(i, k);
            aY.y = Math.min(j, l);
            aY.width = Math.abs(i - k);
            aY.height = Math.abs(j - l);
            if (isDrawsXorMode()) {
                if (aY.width != 0 || aY.height != 0)
                    drawXORRect(graphics2d, aY.x, aY.y, aY.width, aY.height);
                aI = true;
            } else {
                if (ba == null)
                    ba = new GraphicViewerRectangle();
                if (ba.getPen() != null) {
                    ba.setBrush(null);
                    GraphicViewerPen graphicviewerpen = new GraphicViewerPen(2,
                            2, GraphicViewerBrush.ColorDarkGray);
                    ba.setPen(graphicviewerpen);
                }
                ba.setBoundingRect(viewToDocCoords(aY));
                addObjectAtTail(ba);
            }
        } else if (ba != null)
            removeObject(ba);
    }

    public Graphics2D getGraphicViewerGraphics() {
        return (Graphics2D) getCanvas().getGraphics();
    }

    public void disposeGraphicViewerGraphics(Graphics2D graphics2d) {
        if (graphics2d != null)
            graphics2d.dispose();
    }

    protected GraphicViewerHandle pickHandle(Point point) {
        GraphicViewerObject graphicviewerobject = pickObject(point, true);
        if (graphicviewerobject instanceof GraphicViewerHandle)
            return (GraphicViewerHandle) graphicviewerobject;
        else
            return null;
    }

    public void newLink(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null) {
            return;
        } else {
            GraphicViewerLink graphicviewerlink = new GraphicViewerLink(
                    graphicviewerport, graphicviewerport1);
            GraphicViewerSubGraphBase.reparentToCommonSubGraph(
                    graphicviewerlink, graphicviewerport, graphicviewerport1,
                    true, graphicviewerdocument.getLinksLayer());
            fireUpdate(31, 0, graphicviewerlink);
            graphicviewerdocument.endTransaction(getEditPresentationName(4));
            return;
        }
    }

    protected void noNewLink(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        if (getDocument() != null)
            getDocument().endTransaction(false);
    }

    public void reLink(GraphicViewerLink graphicviewerlink,
            GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        graphicviewerlink.setFromPort(graphicviewerport);
        graphicviewerlink.setToPort(graphicviewerport1);
        GraphicViewerLayer graphicviewerlayer = graphicviewerlink.getLayer();
        if (graphicviewerlayer == null && getDocument() != null)
            graphicviewerlayer = getDocument().getLinksLayer();
        if (graphicviewerlayer != null)
            GraphicViewerSubGraphBase.reparentToCommonSubGraph(
                    graphicviewerlink, graphicviewerport, graphicviewerport1,
                    true, graphicviewerlayer);
        fireUpdate(32, 0, graphicviewerlink);
        if (getDocument() != null)
            getDocument().endTransaction(getEditPresentationName(5));
    }

    protected void noReLink(GraphicViewerLink graphicviewerlink,
            GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        if (graphicviewerlink.getLayer() != null) {
            GraphicViewerViewEvent graphicviewerviewevent = new GraphicViewerViewEvent(
                    this, 28, 0, graphicviewerlink, null, null, 0);
            a(graphicviewerviewevent);
            if (graphicviewerviewevent.isConsumed()) {
                doCancelMouse();
                return;
            }
            graphicviewerlink.getLayer().removeObject(graphicviewerlink);
            fireUpdate(29, 0, graphicviewerlink);
        }
        if (getDocument() != null)
            getDocument().endTransaction(getEditPresentationName(13));
    }

    private void a(boolean flag) {
        label0 : for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible())
                continue;
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            do {
                GraphicViewerObject graphicviewerobject;
                do {
                    if (graphicviewerlistposition == null)
                        continue label0;
                    graphicviewerobject = graphicviewerlayer
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerlayer
                            .getNextObjectPos(graphicviewerlistposition);
                } while (!(graphicviewerobject instanceof GraphicViewerPort));
                GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
                if (flag)
                    graphicviewerport
                            ._mthbyte(validLink(az, graphicviewerport));
                else
                    graphicviewerport
                            ._mthbyte(validLink(graphicviewerport, az));
            } while (true);
        }

    }

    public boolean validLink(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        return graphicviewerport.getLayer() != null
                && graphicviewerport.getLayer().isModifiable()
                && graphicviewerport1.getLayer() != null
                && graphicviewerport1.getLayer().isModifiable()
                && graphicviewerport.validLink(graphicviewerport1);
    }

    public boolean validSourcePort(GraphicViewerPort graphicviewerport) {
        return graphicviewerport.getLayer() != null
                && graphicviewerport.getLayer().isModifiable()
                && graphicviewerport.isValidSource();
    }

    public boolean validDestinationPort(GraphicViewerPort graphicviewerport) {
        return graphicviewerport.getLayer() != null
                && graphicviewerport.getLayer().isModifiable()
                && graphicviewerport.isValidDestination();
    }

    public int getPortGravity() {
        return getDefaultPortGravity();
    }

    public GraphicViewerPort pickNearestPort(Point point) {
        GraphicViewerPort graphicviewerport = null;
        double d = getPortGravity();
        d *= d;
        label0 : for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible())
                continue;
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            Point point1 = new Point(0, 0);
            do {
                GraphicViewerPort graphicviewerport1;
                double d3;
                do {
                    do {
                        GraphicViewerObject graphicviewerobject;
                        do {
                            if (graphicviewerlistposition == null)
                                continue label0;
                            graphicviewerobject = graphicviewerlayer
                                    .getObjectAtPos(graphicviewerlistposition);
                            graphicviewerlistposition = graphicviewerlayer
                                    .getNextObjectPos(graphicviewerlistposition);
                        } while (!(graphicviewerobject instanceof GraphicViewerPort));
                        graphicviewerport1 = (GraphicViewerPort) graphicviewerobject;
                    } while (!graphicviewerport1.isValidLink());
                    point1 = graphicviewerport1.getLinkPoint(0, point1);
                    double d1 = point.x - point1.x;
                    double d2 = point.y - point1.y;
                    d3 = d1 * d1 + d2 * d2;
                } while (d3 > d);
                graphicviewerport = graphicviewerport1;
                d = d3;
            } while (true);
        }

        return graphicviewerport;
    }

    protected GraphicViewerPort pickPort(Point point) {
        GraphicViewerObject graphicviewerobject = pickDocObject(point, false);
        if (graphicviewerobject == null)
            return null;
        if (graphicviewerobject instanceof GraphicViewerPort)
            return (GraphicViewerPort) graphicviewerobject;
        else
            return null;
    }

    public boolean startNewLink(GraphicViewerPort graphicviewerport, Point point) {
        boolean flag = validSourcePort(graphicviewerport);
        boolean flag1 = validDestinationPort(graphicviewerport);
        if (flag || flag1) {
            if (getDocument() != null)
                getDocument().startTransaction();
            a(12);
            az = graphicviewerport;
            am = createTemporaryPortForNewLink(graphicviewerport, point);
            if (flag) {
                setState(3);
                aT = createTemporaryLinkForNewLink(az, am);
                a(true);
            } else {
                setState(4);
                aT = createTemporaryLinkForNewLink(am, az);
                a(false);
            }
            addObjectAtTail(aT);
            return true;
        } else {
            return false;
        }
    }

    public boolean startReLink(GraphicViewerLink graphicviewerlink,
            GraphicViewerPort graphicviewerport, Point point) {
        if (graphicviewerlink == null
                || graphicviewerlink.getLayer() == null
                || graphicviewerport != null
                && (graphicviewerport.getLayer() == null || graphicviewerlink
                        .getDocument() != graphicviewerport.getDocument()))
            return false;
        if (graphicviewerlink.getToPort() == null)
            az = graphicviewerlink.getFromPort();
        else if (graphicviewerlink.getFromPort() == null)
            az = graphicviewerlink.getToPort();
        else
            return false;
        if (getDocument() != null)
            getDocument().startTransaction();
        a(12);
        aT = graphicviewerlink;
        L = graphicviewerport;
        am = createTemporaryPortForNewLink(az, point);
        if (graphicviewerlink.getToPort() == null) {
            setState(3);
            graphicviewerlink.setToPort(am);
            a(true);
        } else if (graphicviewerlink.getFromPort() == null) {
            setState(4);
            graphicviewerlink.setFromPort(am);
            a(false);
        }
        return true;
    }

    protected GraphicViewerPort createTemporaryPortForNewLink(
            GraphicViewerPort graphicviewerport, Point point) {
        GraphicViewerPort graphicviewerport1 = new GraphicViewerPort();
        graphicviewerport1.setLocation(point);
        graphicviewerport1.setToSpot(-1);
        graphicviewerport1.setFromSpot(-1);
        return graphicviewerport1;
    }

    protected GraphicViewerLink createTemporaryLinkForNewLink(
            GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        return new GraphicViewerLink(graphicviewerport, graphicviewerport1);
    }

    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public void copy() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.startTransaction();
        Toolkit toolkit = getToolkit();
        if (toolkit == null)
            toolkit = GraphicViewerGlobal.getToolkit();
        copyToClipboard(toolkit.getSystemClipboard());
        fireUpdate(36, 0, null);
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(getEditPresentationName(8));
    }

    @SuppressWarnings("rawtypes")
    public void copyToClipboard(Clipboard clipboard) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            try {
                Class class1 = graphicviewerdocument.getClass();
                GraphicViewerDocument graphicviewerdocument1 = (GraphicViewerDocument) class1
                        .newInstance();
                graphicviewerdocument1._mthdo(graphicviewerdocument);
                GraphicViewerObject agraphicviewerobject[] = getSelection()
                        .toArray();
                graphicviewerdocument1.sortByZOrder(agraphicviewerobject);
                GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                        agraphicviewerobject);
                graphicviewerdocument1
                        .copyFromCollection(graphicviewercollection);
                clipboard.setContents(graphicviewerdocument1, this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
    }

    public void cut() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.startTransaction();
        Toolkit toolkit = getToolkit();
        if (toolkit == null)
            toolkit = GraphicViewerGlobal.getToolkit();
        copyToClipboard(toolkit.getSystemClipboard());
        deleteSelection();
        fireUpdate(36, 0, null);
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(getEditPresentationName(9));
    }

    public void deleteSelection() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.startTransaction();
        GraphicViewerSelection graphicviewerselection = getSelection();
        GraphicViewerViewEvent graphicviewerviewevent = new GraphicViewerViewEvent(
                this, 28, 0, null, null, null, 0);
        a(graphicviewerviewevent);
        if (graphicviewerviewevent.isConsumed()) {
            if (graphicviewerdocument != null)
                graphicviewerdocument.endTransaction(false);
            return;
        }
        fireUpdate(37, 0, null);
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            if (graphicviewerobject.getLayer() != null
                    && !graphicviewerobject.getLayer().isModifiable()) {
                graphicviewerlistposition = graphicviewerselection
                        .getNextObjectPos(graphicviewerlistposition);
            } else {
                graphicviewerselection.clearSelection(graphicviewerobject);
                graphicviewerobject = graphicviewerobject.getDraggingObject();
                if (graphicviewerobject.getParent() != null)
                    graphicviewerobject.getParent().removeObject(
                            graphicviewerobject);
                else if (graphicviewerobject.getLayer() != null)
                    graphicviewerobject.getLayer().removeObject(
                            graphicviewerobject);
                else if (graphicviewerobject.getView() != null)
                    graphicviewerobject.getView().removeObject(
                            graphicviewerobject);
                graphicviewerlistposition = graphicviewerselection
                        .getFirstObjectPos();
            }
        }

        fireUpdate(38, 0, null);
        fireUpdate(29, 0, null);
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(getEditPresentationName(11));
    }

    @SuppressWarnings("rawtypes")
    public void paste() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.startTransaction();
        Toolkit toolkit = getToolkit();
        if (toolkit == null)
            toolkit = GraphicViewerGlobal.getToolkit();
        GraphicViewerCopyEnvironment graphicviewercopyenvironment = pasteFromClipboard(toolkit
                .getSystemClipboard());
        if (graphicviewercopyenvironment != null) {
            Iterator iterator = graphicviewercopyenvironment.values()
                    .iterator();
            boolean flag = false;
            fireUpdate(37, 0, null);
            GraphicViewerSelection graphicviewerselection = getSelection();
            do {
                if (!iterator.hasNext())
                    break;
                Object obj = iterator.next();
                if (obj instanceof GraphicViewerObject) {
                    GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
                    if (graphicviewerobject.isTopLevel()
                            && graphicviewerobject.getDocument() == graphicviewerdocument) {
                        if (!flag) {
                            flag = true;
                            graphicviewerselection.clearSelection();
                        }
                        graphicviewerselection
                                .extendSelection(graphicviewerobject);
                    }
                }
            } while (true);
            fireUpdate(38, 0, null);
        }
        fireUpdate(34, 0, null);
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(getEditPresentationName(10));
    }

    public boolean canPaste() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null)
            return false;
        if (!graphicviewerdocument.getDefaultLayer().isModifiable())
            return false;
        Toolkit toolkit = getToolkit();
        if (toolkit == null)
            toolkit = GraphicViewerGlobal.getToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        Transferable transferable = clipboard.getContents(this);
        java.awt.datatransfer.DataFlavor dataflavor = GraphicViewerDocument
                .getStandardDataFlavor();
        return transferable != null
                && transferable.isDataFlavorSupported(dataflavor);
    }

    public GraphicViewerCopyEnvironment pasteFromClipboard(Clipboard clipboard) {
        Transferable transferable;
        java.awt.datatransfer.DataFlavor dataflavor;
        transferable = clipboard.getContents(this);
        dataflavor = GraphicViewerDocument.getStandardDataFlavor();
        if (transferable != null
                && transferable.isDataFlavorSupported(dataflavor)) {
            GraphicViewerDocument graphicviewerdocument;
            GraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection = null;
            graphicviewerdocument = getDocument();
            if (graphicviewerdocument != null) {
                try {
                    graphicviewerobjectsimplecollection = (GraphicViewerObjectSimpleCollection) transferable
                            .getTransferData(dataflavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return graphicviewerdocument
                        .copyFromCollection(graphicviewerobjectsimplecollection);
            }
        }
        return null;
    }

    public void initializeDragDropHandling() {
        Y = false;
        aN = false;
        setDragDropEnabled(true);
    }

    public void setDragDropEnabled(boolean flag) {
        setDragEnabled(flag);
        setDropEnabled(flag);
    }

    public void setDragEnabled(boolean flag) {
        if (flag && !av) {
            av = true;
            try {
                if (aj == null) {
                    w = DragSource.getDefaultDragSource();
                    aj = w.createDefaultDragGestureRecognizer(getCanvas(), 3,
                            getCanvas());
                } else {
                    aj.addDragGestureListener(getCanvas());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            fireUpdate(117, 0, null);
        } else if (!flag && av) {
            av = false;
            if (aj != null)
                aj.removeDragGestureListener(getCanvas());
            fireUpdate(117, 1, null);
        }
    }

    public void setDropEnabled(boolean flag) {
        if (flag && !T) {
            T = true;
            try {
                if (aK == null)
                    aK = new DropTarget(getCanvas(), 3, getCanvas());
                else
                    aK.addDropTargetListener(getCanvas());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            fireUpdate(118, 0, null);
        } else if (!flag && T) {
            T = false;
            if (aK != null)
                aK.removeDropTargetListener(getCanvas());
            fireUpdate(118, 1, null);
        }
    }

    public boolean isDragDropEnabled() {
        return isDragEnabled() && isDropEnabled();
    }

    public boolean isDragEnabled() {
        return av;
    }

    public boolean isDropEnabled() {
        return T;
    }

    public boolean isInternalDragDrop() {
        return Y;
    }

    public void onDragGestureRecognized(DragGestureEvent draggestureevent) {
        if (!isDragEnabled())
            return;
        a("dragGestureRecognized", draggestureevent);
        aN = false;
        Y = true;
        if (!isDropEnabled()) {
            Point point = draggestureevent.getDragOrigin();
            aL.x = point.x;
            aL.y = point.y;
            convertViewToDoc(aL);
            doMouseMove(
                    convertActionToModifiers(draggestureevent.getDragAction()),
                    aL, point);
        }
        dragGestureRecognized(draggestureevent);
    }

    @SuppressWarnings("rawtypes")
    public void dragGestureRecognized(DragGestureEvent draggestureevent) {
        try {
            Cursor cursor = DragSource.DefaultMoveDrop;
            if (getState() == 5 || getState() == 3 || getState() == 4)
                cursor = getCursor();
            GraphicViewerDocument graphicviewerdocument = getDocument();
            Class class1 = graphicviewerdocument.getClass();
            GraphicViewerDocument graphicviewerdocument1 = (GraphicViewerDocument) class1
                    .newInstance();
            graphicviewerdocument1._mthdo(graphicviewerdocument);
            GraphicViewerObject agraphicviewerobject[] = getSelection()
                    .toArray();
            graphicviewerdocument1.sortByZOrder(agraphicviewerobject);
            GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                    agraphicviewerobject);
            graphicviewerdocument1.copyFromCollection(graphicviewercollection);
            if (DragSource.isDragImageSupported()) {
                if (aM == null)
                    aM = new BufferedImage(1, 1, 1);
                draggestureevent.startDrag(cursor, aM, new Point(0, 0),
                        graphicviewerdocument1, getCanvas());
            } else {
                draggestureevent.startDrag(cursor, graphicviewerdocument1,
                        getCanvas());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void onDragEnter(DragSourceDragEvent dragsourcedragevent) {
        if (!isDragEnabled()) {
            return;
        } else {
            dragEnter(dragsourcedragevent);
            return;
        }
    }

    public void onDragOver(DragSourceDragEvent dragsourcedragevent) {
        if (!isDragEnabled()) {
            return;
        } else {
            dragOver(dragsourcedragevent);
            return;
        }
    }

    public void onDropActionChanged(DragSourceDragEvent dragsourcedragevent) {
        if (!isDragEnabled()) {
            return;
        } else {
            a("dropActionChanged Source", dragsourcedragevent);
            dropActionChanged(dragsourcedragevent);
            return;
        }
    }

    public void onDragExit(DragSourceEvent dragsourceevent) {
        if (!isDragEnabled()) {
            return;
        } else {
            dragExit(dragsourceevent);
            return;
        }
    }

    public void onDragDropEnd(DragSourceDropEvent dragsourcedropevent) {
        if (!isDragEnabled())
            return;
        a("dragDropEnd Source", dragsourcedropevent);
        if (isInternalDragDrop()) {
            Y = false;
            doCancelMouse();
        } else {
            dragDropEnd(dragsourcedropevent);
        }
    }

    public void dragEnter(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dragOver(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dropActionChanged(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dragExit(DragSourceEvent dragsourceevent) {
    }

    public void dragDropEnd(DragSourceDropEvent dragsourcedropevent) {
    }

    public int convertActionToModifiers(int i) {
        int j = 16;
        switch (i) {
            case 2 : // '\002'
                j |= 1;
                break;

            case 1 : // '\001'
                j |= 2;
                break;

            case 1073741824 :
                j |= 3;
                break;
        }
        return j;
    }

    public void onDragEnter(DropTargetDragEvent droptargetdragevent) {
        if (!isDropEnabled())
            return;
        a("dragEnter Target", droptargetdragevent);
        if (isInternalDragDrop()) {
            Point point = droptargetdragevent.getLocation();
            aL.x = point.x;
            aL.y = point.y;
            convertViewToDoc(aL);
            doMouseMove(
                    convertActionToModifiers(droptargetdragevent
                            .getDropAction()),
                    aL, point);
        } else {
            dragEnter(droptargetdragevent);
        }
    }

    public void onDragOver(DropTargetDragEvent droptargetdragevent) {
        if (!isDropEnabled())
            return;
        a("dragOver Target", droptargetdragevent);
        if (isInternalDragDrop()) {
            Point point = droptargetdragevent.getLocation();
            aL.x = point.x;
            aL.y = point.y;
            convertViewToDoc(aL);
            doMouseMove(
                    convertActionToModifiers(droptargetdragevent
                            .getDropAction()),
                    aL, point);
        } else {
            dragOver(droptargetdragevent);
        }
    }

    public void onDropActionChanged(DropTargetDragEvent droptargetdragevent) {
        if (!isDropEnabled())
            return;
        a("dropActionChanged Target", droptargetdragevent);
        if (!isInternalDragDrop())
            dropActionChanged(droptargetdragevent);
    }

    public void onDragExit(DropTargetEvent droptargetevent) {
        if (!isDropEnabled())
            return;
        a("dragExit Target", droptargetevent);
        if (!isInternalDragDrop())
            dragExit(droptargetevent);
    }

    public void onDrop(DropTargetDropEvent droptargetdropevent) {
        if (!isDropEnabled()) {
            droptargetdropevent.rejectDrop();
            return;
        }
        a("drop Target", droptargetdropevent);
        if (isInternalDragDrop()) {
            Y = false;
            Point point = droptargetdropevent.getLocation();
            aL.x = point.x;
            aL.y = point.y;
            convertViewToDoc(aL);
            if (doMouseUp(
                    convertActionToModifiers(droptargetdropevent
                            .getDropAction()),
                    aL, point)) {
                droptargetdropevent.acceptDrop(droptargetdropevent
                        .getDropAction());
                setState(0);
                completeDrop(droptargetdropevent, true);
            } else {
                droptargetdropevent.rejectDrop();
            }
        } else {
            if (GraphicViewerGlobal.isAtLeastJavaVersion(1.3999999999999999D))
                dragExit(droptargetdropevent);
            drop(droptargetdropevent);
        }
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent) {
        if (!isDropFlavorAcceptable(droptargetdragevent)) {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if (i == 0)
            droptargetdragevent.rejectDrag();
        else
            droptargetdragevent.acceptDrag(i);
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent) {
        if (!isDropFlavorAcceptable(droptargetdragevent)) {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if (i == 0)
            droptargetdragevent.rejectDrag();
        else
            droptargetdragevent.acceptDrag(i);
    }

    public void dropActionChanged(DropTargetDragEvent droptargetdragevent) {
        if (!isDropFlavorAcceptable(droptargetdragevent)) {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if (i == 0)
            droptargetdragevent.rejectDrag();
        else
            droptargetdragevent.acceptDrag(i);
    }

    public void dragExit(DropTargetEvent droptargetevent) {
    }

    public void drop(DropTargetDropEvent droptargetdropevent) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null
                && graphicviewerdocument.getDefaultLayer().isModifiable()) {
            graphicviewerdocument.startTransaction();
            if (doDrop(droptargetdropevent, null)) {
                fireUpdate(35, 0, null);
                graphicviewerdocument
                        .endTransaction(getEditPresentationName(6));
            } else {
                droptargetdropevent.rejectDrop();
                graphicviewerdocument.endTransaction(false);
            }
        } else {
            droptargetdropevent.rejectDrop();
        }
    }

    public boolean isDropFlavorAcceptable(
            DropTargetDragEvent droptargetdragevent) {
        java.awt.datatransfer.DataFlavor dataflavor = GraphicViewerDocument
                .getStandardDataFlavor();
        return droptargetdragevent.isDataFlavorSupported(dataflavor);
    }

    public int computeAcceptableDrop(DropTargetDragEvent droptargetdragevent) {
        if (getDocument() == null
                || !getDocument().getDefaultLayer().isModifiable())
            return 0;
        if ((droptargetdragevent.getDropAction() & 3) != 0)
            return droptargetdragevent.getDropAction();
        else
            return 0;
    }

    @SuppressWarnings("rawtypes")
    public boolean doDrop(DropTargetDropEvent droptargetdropevent,
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection;
        Point point;
        java.awt.datatransfer.DataFlavor dataflavor = GraphicViewerDocument
                .getStandardDataFlavor();
        if (droptargetdropevent.isDataFlavorSupported(dataflavor)
                && (droptargetdropevent.getDropAction() & 3) != 0) {
            droptargetdropevent.acceptDrop(droptargetdropevent.getDropAction());
            Transferable transferable = droptargetdropevent.getTransferable();
            Object obj;
            try {
                obj = transferable.getTransferData(dataflavor);
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            graphicviewerobjectsimplecollection = (GraphicViewerObjectSimpleCollection) obj;
            point = new Point(0, 0);
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getFirstObjectPos();
            if (graphicviewerlistposition != null) {
                GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                        .getObjectAtPos(graphicviewerlistposition);
                point = graphicviewerobject.getLocation();
                completeDrop(droptargetdropevent, true);
                Point point1 = droptargetdropevent.getLocation();
                Point point2 = viewToDocCoords(point1);
                Point point3 = new Point(point2.x - point.x, point2.y - point.y);
                GraphicViewerDocument graphicviewerdocument = getDocument();
                if (graphicviewerdocument != null) {
                    GraphicViewerCopyEnvironment graphicviewercopyenvironment1 = graphicviewerdocument
                            .copyFromCollection(
                                    graphicviewerobjectsimplecollection,
                                    point3, graphicviewercopyenvironment);
                    GraphicViewerSelection graphicviewerselection = getSelection();
                    graphicviewerselection.clearSelection();
                    fireUpdate(37, 0, null);
                    Iterator iterator = graphicviewercopyenvironment1.values()
                            .iterator();
                    do {
                        if (!iterator.hasNext())
                            break;
                        Object obj1 = iterator.next();
                        if (obj1 instanceof GraphicViewerObject) {
                            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) obj1;
                            if (graphicviewerobject1.isTopLevel())
                                graphicviewerselection
                                        .extendSelection(graphicviewerobject1);
                        }
                    } while (true);
                    fireUpdate(38, 0, null);
                }
                completeDrop(droptargetdropevent, true);
                return true;
            }
        }
        return false;
    }

    public void completeDrop(DropTargetDropEvent droptargetdropevent,
            boolean flag) {
        if (GraphicViewerGlobal.isAtLeastJavaVersion(1.3D)) {
            droptargetdropevent.dropComplete(flag);
        } else {
            Thread thread = new Thread(new GraphicViewerViewHelper(
                    new GraphicViewerViewHelper(droptargetdropevent, flag)));
            thread.start();
        }
    }

    public Insets getAutoscrollInsets() {
        return F;
    }

    public void setAutoscrollInsets(Insets insets) {
        Insets insets1 = F;
        if (insets == null)
            insets = new Insets(0, 0, 0, 0);
        if (!insets1.equals(insets)) {
            F.top = insets.top;
            F.left = insets.left;
            F.bottom = insets.bottom;
            F.right = insets.right;
            fireUpdate(123, 0, insets1);
        }
    }

    public void autoscroll(Point point) {
        @SuppressWarnings("unused")
        Object obj = null;
        JScrollBar jscrollbar = getHorizontalScrollBar();
        Dimension dimension = getCanvas().getSize();
        Insets insets = getAutoscrollInsets();
        @SuppressWarnings("unused")
        boolean flag = false;
        @SuppressWarnings("unused")
        boolean flag1 = false;
        @SuppressWarnings("unused")
        boolean flag2 = false;
        @SuppressWarnings("unused")
        boolean flag3 = false;
        if (jscrollbar != null) {
            int i = jscrollbar.getValue();
            int j = i;
            if (point.x <= insets.left) {
                j = i - jscrollbar.getUnitIncrement();
                j = Math.max(j, jscrollbar.getMinimum());
            } else if (point.x >= dimension.width - insets.right) {
                j = i + jscrollbar.getUnitIncrement();
                j = Math.min(j, jscrollbar.getMaximum());
            }
            switch (getState()) {
                case 6 : // '\006'
                    if (isDrawsXorMode()) {
                        Graphics2D graphics2d = getGraphicViewerGraphics();
                        drawXORBox(graphics2d, 0, 0, 0, 0, 3);
                        disposeGraphicViewerGraphics(graphics2d);
                    }
                    jscrollbar.setValue(j);
                    break;

                case 5 : // '\005'
                    if (isDrawsXorMode()) {
                        Graphics2D graphics2d1 = getGraphicViewerGraphics();
                        drawXORBox(graphics2d1, 0, 0, 0, 0, 3);
                        disposeGraphicViewerGraphics(graphics2d1);
                    }
                    jscrollbar.setValue(j);
                    break;

                default :
                    jscrollbar.setValue(j);
                    break;
            }
        }
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if (jscrollbar1 != null) {
            int k = jscrollbar1.getValue();
            int l = k;
            if (point.y <= insets.top) {
                l = k - jscrollbar1.getUnitIncrement();
                l = Math.max(l, jscrollbar1.getMinimum());
            } else if (point.y >= dimension.height - insets.bottom) {
                l = k + jscrollbar1.getUnitIncrement();
                l = Math.min(l, jscrollbar1.getMaximum());
            }
            switch (getState()) {
                case 6 : // '\006'
                    if (isDrawsXorMode()) {
                        Graphics2D graphics2d2 = getGraphicViewerGraphics();
                        drawXORBox(graphics2d2, 0, 0, 0, 0, 3);
                        disposeGraphicViewerGraphics(graphics2d2);
                    }
                    jscrollbar1.setValue(l);
                    break;

                case 5 : // '\005'
                    if (isDrawsXorMode()) {
                        Graphics2D graphics2d3 = getGraphicViewerGraphics();
                        drawXORBox(graphics2d3, 0, 0, 0, 0, 3);
                        disposeGraphicViewerGraphics(graphics2d3);
                    }
                    jscrollbar1.setValue(l);
                    break;

                default :
                    jscrollbar1.setValue(l);
                    break;
            }
        }
    }

    public GraphicViewerLayer getFirstLayer() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null)
            return null;
        else
            return graphicviewerdocument.getFirstLayer();
    }

    public GraphicViewerLayer getLastLayer() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null)
            return null;
        else
            return graphicviewerdocument.getLastLayer();
    }

    public GraphicViewerLayer getNextLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null)
            return null;
        else
            return graphicviewerlayer.getNextLayer();
    }

    public GraphicViewerLayer getPrevLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null)
            return null;
        else
            return graphicviewerlayer.getPrevLayer();
    }

    public String getEditPresentationName(int i) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null
                && graphicviewerdocument.getUndoManager() != null)
            return graphicviewerdocument.getUndoManager()
                    .getViewEditPresentationName(i);
        else
            return null;
    }

    public int getDebugFlags() {
        return ab;
    }

    public void setDebugFlags(int i) {
        ab = i;
    }

    void a(String s1, EventObject eventobject) {
        if ((ab & 1) != 0) {
            String s2 = Integer.toString(getState());
            s2 = s2 + (hasFocus() ? " has focus" : " NO FOCUS");
            s2 = s2 + (isDragEnabled() ? "" : " DRAG DISABLED");
            s2 = s2 + (isDropEnabled() ? "" : " DROP DISABLED");
            s2 = s2 + (isInternalDragDrop() ? " internal drag" : "");
            s2 = s2 + (aN ? " started" : "");
            s1 = Integer.toString(ab) + " " + s1 + " " + s2;
            if (!s1.equals(bl)) {
                bl = s1;
                bb = null;
                GraphicViewerGlobal.TRACE(s1);
            }
        }
    }

    void a(String s1) {
        if ((ab & 1) != 0) {
            String s2 = Integer.toString(getState());
            s2 = s2 + (hasFocus() ? " has focus" : " NO FOCUS");
            s2 = s2 + (isDragEnabled() ? "" : " DRAG DISABLED");
            s2 = s2 + (isDropEnabled() ? "" : " DROP DISABLED");
            s2 = s2 + (isInternalDragDrop() ? " internal drag" : "");
            s2 = s2 + (aN ? " started" : "");
            s1 = Integer.toString(ab) + " " + s1 + " " + s2;
            if (!s1.equals(bb)) {
                bb = s1;
                GraphicViewerGlobal.TRACE(s1);
            }
        }
    }

    int[] _mthdo(int i) {
        if (i >= Q.length)
            Q = new int[i + 1][];
        int ai1[] = Q[i];
        if (ai1 == null) {
            ai1 = new int[i];
            Q[i] = ai1;
        }
        return ai1;
    }

    int[] _mthif(int i) {
        if (i >= P.length)
            P = new int[i + 1][];
        int ai1[] = P[i];
        if (ai1 == null) {
            ai1 = new int[i];
            P[i] = ai1;
        }
        return ai1;
    }

    Point _mthlong() {
        return aJ;
    }

    Dimension _mthvoid() {
        return bp;
    }

    Rectangle c() {
        return aX;
    }

    public static Color getDefaultPrimarySelectionColor() {
        return X;
    }

    public static void setDefaultPrimarySelectionColor(Color color) {
        X = color;
    }

    public static Color getDefaultSecondarySelectionColor() {
        return a6;
    }

    public static void setDefaultSecondarySelectionColor(Color color) {
        a6 = color;
    }

    public static int getDefaultPortGravity() {
        return aD;
    }

    public static void setDefaultPortGravity(int i) {
        aD = i;
    }

    public void setGridWidth(int i) {
        if (i != G) {
            G = i;
            if (getHorizontalScrollBar() != null)
                getHorizontalScrollBar().setUnitIncrement(getGridWidth());
            onGridChange(0);
        }
    }

    public int getGridWidth() {
        return G;
    }

    public void setGridHeight(int i) {
        if (i != H) {
            H = i;
            if (getVerticalScrollBar() != null)
                getVerticalScrollBar().setUnitIncrement(getGridHeight());
            onGridChange(0);
        }
    }

    public int getGridHeight() {
        return H;
    }

    public void setGridStyle(int i) {
        if (i != bd) {
            bd = i;
            onGridChange(1);
        }
    }

    public int getGridStyle() {
        return bd;
    }

    public void setGridOrigin(Point point) {
        if (!point.equals(aV)) {
            aV.x = point.x;
            aV.y = point.y;
            onGridChange(6);
        }
    }

    public Point getGridOrigin() {
        return aV;
    }

    public void setGridSpot(int i) {
        if (i != N) {
            N = i;
            onGridChange(2);
        }
    }

    public int getGridSpot() {
        return N;
    }

    public void setGridPen(GraphicViewerPen graphicviewerpen) {
        if (bs != graphicviewerpen) {
            bs = graphicviewerpen;
            onGridChange(5);
        }
    }

    public GraphicViewerPen getGridPen() {
        return bs;
    }

    public int getSnapMove() {
        return aa;
    }

    public void setSnapMove(int i) {
        if (aa != i) {
            aa = i;
            onGridChange(3);
        }
    }

    public int getSnapResize() {
        return aS;
    }

    public void setSnapResize(int i) {
        if (aS != i) {
            aS = i;
            onGridChange(4);
        }
    }

    public void onGridChange(int i) {
        fireUpdate(124, i, null);
    }

    protected void drawGridLines(Graphics2D graphics2d, Rectangle rectangle) {
        int i = getGridWidth();
        int j = getGridHeight();
        GraphicViewerPen graphicviewerpen = getGridPen();
        int k = rectangle.x - i;
        int l = rectangle.y - j;
        int i1 = rectangle.x + rectangle.width + i;
        int j1 = rectangle.y + rectangle.height + j;
        Point point = findNearestGridPoint(k, l, null);
        Point point1 = findNearestGridPoint(i1, j1, null);
        for (int k1 = point.x; k1 < point1.x; k1 += i)
            GraphicViewerDrawable.drawLine(graphics2d, graphicviewerpen, k1,
                    rectangle.y, k1, rectangle.y + rectangle.height);

        for (int l1 = point.y; l1 < point1.y; l1 += j)
            GraphicViewerDrawable.drawLine(graphics2d, graphicviewerpen,
                    rectangle.x, l1, rectangle.x + rectangle.width, l1);

    }

    protected void drawGridCrosses(Graphics2D graphics2d, int i, int j,
            Rectangle rectangle) {
        int k = getGridWidth();
        int l = getGridHeight();
        GraphicViewerPen graphicviewerpen = getGridPen();
        int i1 = rectangle.x - k;
        int j1 = rectangle.y - l;
        int k1 = rectangle.x + rectangle.width + k;
        int l1 = rectangle.y + rectangle.height + l;
        Point point = findNearestGridPoint(i1, j1, null);
        Point point1 = findNearestGridPoint(k1, l1, null);
        if (i < 2 && j < 2) {
            double d = getScale();
            int k2 = 1;
            if (d < 1.0D)
                k2 = (int) Math.round(1.0D / d) + 1;
            for (int l2 = point.x; l2 < point1.x; l2 += k) {
                for (int i3 = point.y; i3 < point1.y; i3 += l)
                    GraphicViewerDrawable.drawLine(graphics2d,
                            graphicviewerpen, l2, i3, l2 + k2, i3);

            }

        } else {
            for (int i2 = point.x; i2 < point1.x; i2 += k) {
                for (int j2 = point.y; j2 < point1.y; j2 += l) {
                    GraphicViewerDrawable.drawLine(graphics2d,
                            graphicviewerpen, i2, j2 - i / 2, i2, j2 + i / 2);
                    GraphicViewerDrawable.drawLine(graphics2d,
                            graphicviewerpen, i2 - j / 2, j2, i2 + j / 2, j2);
                }

            }

        }
    }

    public void snapObject(GraphicViewerObject graphicviewerobject) {
        snapObject(graphicviewerobject, getGridSpot());
    }

    public void snapObject(GraphicViewerObject graphicviewerobject, int i) {
        Point point = graphicviewerobject.getSpotLocation(i);
        Point point1 = findNearestGridPoint(point.x, point.y, point);
        graphicviewerobject.setSpotLocation(i, point1);
    }

    public void snapAllObjects() {
        snapAllObjects(getGridSpot());
    }

    public void snapAllObjects(int i) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerObject graphicviewerobject = graphicviewerdocument
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerdocument
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (!(graphicviewerobject instanceof GraphicViewerLink))
                snapObject(graphicviewerobject, i);
        } while (true);
    }

    public Point findNearestGridPoint(int i, int j, Point point) {
        Point point1 = getGridOrigin();
        int k = point1.x;
        int l = point1.y;
        int i1 = getGridWidth();
        int j1 = getGridHeight();
        int k1 = i - k;
        if (k1 < 0)
            k1 -= i1;
        k1 = (k1 / i1) * i1 + k;
        int l1 = j - l;
        if (l1 < 0)
            l1 -= j1;
        l1 = (l1 / j1) * j1 + l;
        int i2 = (i - k1) * (i - k1) + (j - l1) * (j - l1);
        int j2 = k1;
        int k2 = l1;
        int l2 = k1 + i1;
        int i3 = l1;
        int j3 = (i - l2) * (i - l2) + (j - i3) * (j - i3);
        if (j3 < i2) {
            i2 = j3;
            j2 = l2;
            k2 = i3;
        }
        int k3 = k1;
        int l3 = l1 + j1;
        int i4 = (i - k3) * (i - k3) + (j - l3) * (j - l3);
        if (i4 < i2) {
            i2 = i4;
            j2 = k3;
            k2 = l3;
        }
        int j4 = l2;
        int k4 = l3;
        int l4 = (i - j4) * (i - j4) + (j - k4) * (j - k4);
        if (l4 < i2) {
            j2 = j4;
            k2 = k4;
        }
        if (point == null) {
            return new Point(j2, k2);
        } else {
            point.x = j2;
            point.y = k2;
            return point;
        }
    }

    @SuppressWarnings("unused")
    private static int aw = 10 + (int) Math.rint(Math.random() * 30D);
    public static final int MouseStateNone = 0;
    public static final int MouseStateSelection = 1;
    public static final int MouseStateMove = 2;
    public static final int MouseStateCreateLink = 3;
    public static final int MouseStateCreateLinkFrom = 4;
    public static final int MouseStateResize = 5;
    public static final int MouseStateDragBoxSelection = 6;
    public static final int MouseStateAction = 7;
    public static final int MouseStateLast = 100;
    public static final int EventMouseDown = 1;
    public static final int EventMouseMove = 2;
    public static final int EventMouseUp = 3;
    public static final int DebugEvents = 1;
    public static final int GridInvisible = 0;
    public static final int GridDot = 1;
    public static final int GridCross = 2;
    public static final int GridLine = 3;
    public static final int NoSnap = 0;
    public static final int SnapJump = 1;
    public static final int SnapAfter = 2;
    public static final int ChangedDimensions = 0;
    public static final int ChangedStyle = 1;
    public static final int ChangedSpot = 2;
    public static final int ChangedSnapMove = 3;
    public static final int ChangedSnapResize = 4;
    public static final int ChangedPen = 5;
    public static final int ChangedOrigin = 6;
    static final int C = 33;
    static final int s = 34;
    static final int O = 10;
    static final int t = 27;
    static final int ae = 1;
    static final int D = 2;
    static final int aZ = 16;
    static final int W = 0;
    static final int aF = 1;
    static final int bo = 2;
    static final int a0 = 3;
    static final int an = 0x40000000;
    static final int as = 0;
    static final int S = 12;
    static final int ak = 3;
    static final int v = 4;
    static final int aR = 5;
    static final int aW = 6;
    static final int ag = 7;
    static final int bk = 8;
    static final int R = 9;
    static final int E = 10;
    static final int a4 = 11;
    static final int bm = 1;
    static final int a2 = 0;
    private static Color X;
    private static Color a6;
    private static int aD = 100;
    private Image J;
    private GraphicViewerViewCanvas U;
    private JScrollBar a3;
    private JScrollBar aB;
    private JComponent K;
    private GraphicViewerDocument bh;
    private GraphicViewerObjList bc;
    private Point u;
    private double ac;
    private double r;
    private boolean M;
    private boolean ax;
    private Insets F;
    private boolean aH;
    private boolean aP;
    private boolean be;
    private transient int at;
    private transient Point au;
    private transient Point a1;
    private transient MouseEvent bg;
    private transient Point al;
    private transient int ao;
    private transient GraphicViewerObject br;
    private int aO;
    private boolean a5;
    private transient GraphicViewerSelection ai;
    private transient GraphicViewerObject bi;
    private boolean av;
    private boolean T;
    private boolean bf;
    private transient boolean aN;
    private transient boolean Y;
    private transient DragSource w;
    private transient DropTarget aK;
    private transient DragGestureRecognizer aj;
    private transient BufferedImage aM;
    private transient Point aL;
    @SuppressWarnings("rawtypes")
    private transient ArrayList aU;
    private boolean B;
    private GraphicViewerSelection z;
    private Color ad;
    private Color aC;
    private boolean bj;
    private Cursor a8;
    private transient GraphicViewerPort az;
    private transient GraphicViewerPort am;
    private transient GraphicViewerLink aT;
    private transient GraphicViewerPort L;
    private boolean aQ;
    private transient Rectangle aY;
    private transient boolean aI;
    private transient GraphicViewerRectangle ba;
    private transient Rectangle aE;
    private transient Point a9;
    private transient Point a7;
    private transient GraphicViewerTextEdit ar;
    @SuppressWarnings("rawtypes")
    private transient ArrayList aA;
    private transient boolean aq;
    private transient Dimension I;
    private transient Point A;
    private transient java.awt.geom.Rectangle2D.Double bq;
    private transient int V;
    private transient int af;
    private transient double ap;
    private transient double aG;
    private transient boolean Z;
    @SuppressWarnings("rawtypes")
    private transient ArrayList ay;
    private transient GraphicViewerViewEvent ah;
    private transient FontRenderContext bn;
    private transient int Q[][];
    private transient int P[][];
    private transient Point aJ;
    private transient Dimension bp;
    private transient Rectangle aX;
    private int ab;
    private transient String bl;
    private transient String bb;
    private int G;
    private int H;
    private int bd;
    private int N;
    private Point aV;
    private GraphicViewerPen bs;
    private int aa;
    private int aS;

    static {
        X = GraphicViewerBrush.ColorGreen;
        a6 = GraphicViewerBrush.ColorCyan;
    }

}