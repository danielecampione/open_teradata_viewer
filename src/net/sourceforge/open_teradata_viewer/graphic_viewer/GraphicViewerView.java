/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2015, D. Campione
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
import java.awt.geom.Rectangle2D;
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
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerView extends JComponent
        implements
            IGraphicViewerObjectCollection,
            Printable,
            ClipboardOwner,
            Autoscroll,
            DragGestureListener,
            DragSourceListener,
            DropTargetListener,
            IGraphicViewerDocumentListener,
            KeyListener {

    private static final long serialVersionUID = -6660541999857286497L;

    private static int mxy = 10 + (int) Math.rint(Math.random() * 30.0D);
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
    static final int Key_PAGE_UP = 33;
    static final int Key_PAGE_DOWN = 34;
    static final int Key_ENTER = 10;
    static final int Key_ESCAPE = 27;
    static final int Input_SHIFT_MASK = 1;
    static final int Input_CTRL_MASK = 2;
    static final int Input_BUTTON1_MASK = 16;
    static final int DnD_ACTION_NONE = 0;
    static final int DnD_ACTION_COPY = 1;
    static final int DnD_ACTION_MOVE = 2;
    static final int DnD_ACTION_COPY_OR_MOVE = 3;
    static final int DnD_ACTION_LINK = 1073741824;
    static final int Cursor_DEFAULT = 0;
    static final int Cursor_HAND = 12;
    static final int Cursor_WAIT = 3;
    static final int Cursor_SW_RESIZE = 4;
    static final int Cursor_SE_RESIZE = 5;
    static final int Cursor_NW_RESIZE = 6;
    static final int Cursor_NE_RESIZE = 7;
    static final int Cursor_N_RESIZE = 8;
    static final int Cursor_S_RESIZE = 9;
    static final int Cursor_W_RESIZE = 10;
    static final int Cursor_E_RESIZE = 11;
    static final int Printing_NO_SUCH_PAGE = 1;
    static final int Printing_PAGE_EXISTS = 0;
    private static Color myDefaultPrimarySelectionColor = GraphicViewerBrush.ColorGreen;
    private static Color myDefaultSecondarySelectionColor = GraphicViewerBrush.ColorCyan;
    private static int myDefaultPortGravity = 100;
    private Image myBackgroundImage = null;
    private GraphicViewerViewCanvas myCanvas;
    private JScrollBar myVertScroll;
    private JScrollBar myHorizScroll;
    private JComponent myCornerBox;
    private OTVDocument myDocument;
    private GraphicViewerObjList myObjects = new GraphicViewerObjList(true);
    private Point myOrigin = new Point(0, 0);
    private double myHorizScale = 1.0D;
    private double myVertScale = 1.0D;
    private boolean myIncludingNegativeCoords = false;
    private boolean myHidingDisabledScrollbars = false;
    private Insets myAutoscrollInsets = new Insets(12, 12, 12, 12);
    private boolean myUpdatingScrollbars = false;
    private boolean myMouseEnabled = false;
    private boolean myIgnoreNextMouseDown = false;
    private transient int myMouseState;
    private transient Point myMouseDownPoint;
    private transient Point myMoveOffset;
    private transient MouseEvent myMouseEvent;
    private transient Point myMouseDocPoint;
    private transient int myHandleHit;
    private transient GraphicViewerObject myCurrentObject;
    private int myMouseActions = 3;
    private boolean myDragsSelectionImage = true;
    private transient GraphicViewerSelection myDragSelection;
    private transient GraphicViewerObject myDragSelectionOrigObj;
    private boolean myDragEnabled = false;
    private boolean myDropEnabled = false;
    private boolean myDragsRealtime = true;
    private transient boolean myInternalDragStarted;
    private transient boolean myInternalDragDrop;
    private transient DragSource myDragSource;
    private transient DropTarget myDropTarget;
    private transient DragGestureRecognizer myDragGestureRecognizer;
    private transient BufferedImage myDragImage;
    private transient Point myDropDocPoint;
    private transient ArrayList myMoveArrayList;
    private boolean myKeyEnabled = false;
    private GraphicViewerSelection mySelection;
    private Color myPrimarySelectionColor = null;
    private Color mySecondarySelectionColor = null;
    private boolean myHideSelectionOnMouse = true;
    private Cursor myDefaultCursor = null;
    private transient GraphicViewerPort myTempStartPort;
    private transient GraphicViewerPort myTempEndPort;
    private transient GraphicViewerLink myTempLink;
    private transient GraphicViewerPort myOrigEndPort;
    private boolean myDrawsXorMode = false;
    private transient Rectangle myPrevXORRect;
    private transient boolean myPrevXORRectValid;
    private transient GraphicViewerRectangle myMarquee;
    private transient Rectangle myOrigResizeRect;
    private transient Point myOrigResizePoint;
    private transient Point myAnchorPoint;
    private transient GraphicViewerTextEdit myTextEdit;
    private transient ArrayList myControls;
    private transient boolean myPrintBegun;
    private transient Dimension myPrintDocSize;
    private transient Point myPrintDocTopLeft;
    private transient Rectangle2D.Double myPrintPageRect;
    private transient int myPrintNumPagesAcross;
    private transient int myPrintNumPagesDown;
    private transient double myPrintHorizScale;
    private transient double myPrintVertScale;
    private transient boolean myIsPrinting;
    private transient ArrayList myViewListeners;
    private transient GraphicViewerViewEvent myViewEvent;
    private transient FontRenderContext myFontRenderContext;
    private transient int[][] myTempArraysX;
    private transient int[][] myTempArraysY;
    private transient Point myTempPoint;
    private transient Dimension myTempDimension;
    private transient Rectangle myTempRectangle;
    private int myDebugFlags = 0;
    private transient String myLastEventMsg;
    private transient String myLastHandlerMsg;
    private int myGridWidth = 50;
    private int myGridHeight = 50;
    private int myGridStyle = 0;
    private int mySpotNumber = 1;
    private Point myGridOrigin = new Point(0, 0);
    private GraphicViewerPen myGridPen = GraphicViewerPen.lightGray;
    private int mySnapMove = 0;
    private int mySnapResize = 0;

    public GraphicViewerView() {
        init(createDefaultModel());
    }

    public GraphicViewerView(OTVDocument graphicviewerdocument) {
        init(graphicviewerdocument);
    }

    private final void init(OTVDocument graphicviewerdocument) {
        myMouseState = 0;
        myMouseDownPoint = new Point(0, 0);
        myMoveOffset = new Point(0, 0);
        myMouseEvent = null;
        myMouseDocPoint = new Point(0, 0);
        myHandleHit = 0;
        myCurrentObject = null;
        myDragSelection = null;
        myDragSelectionOrigObj = null;
        myInternalDragStarted = false;
        myInternalDragDrop = false;
        myDragSource = null;
        myDropTarget = null;
        myDragGestureRecognizer = null;
        myDragImage = null;
        myDropDocPoint = new Point(0, 0);
        myMoveArrayList = new ArrayList();
        myTempStartPort = null;
        myTempEndPort = null;
        myTempLink = null;
        myOrigEndPort = null;
        myPrevXORRect = new Rectangle(0, 0, 0, 0);
        myPrevXORRectValid = false;
        myMarquee = null;
        myOrigResizeRect = new Rectangle(0, 0, 0, 0);
        myOrigResizePoint = new Point(0, 0);
        myAnchorPoint = new Point(0, 0);
        myTextEdit = null;
        myControls = new ArrayList();
        myPrintBegun = false;
        myPrintDocSize = new Dimension();
        myPrintDocTopLeft = new Point(0, 0);
        myPrintPageRect = new java.awt.geom.Rectangle2D.Double();
        myPrintNumPagesAcross = 1;
        myPrintNumPagesDown = 1;
        myPrintHorizScale = 1.0D;
        myPrintVertScale = 1.0D;
        myIsPrinting = false;
        myViewListeners = null;
        myViewEvent = null;
        myFontRenderContext = null;
        myTempArraysX = new int[0][];
        myTempArraysY = new int[0][];
        myTempPoint = new Point(0, 0);
        myTempDimension = new Dimension();
        myTempRectangle = new Rectangle(0, 0, 0, 0);
        GraphicViewerGlobal.setComponent(this);
        mySelection = createDefaultSelection();
        setLayout(null);
        myBackgroundImage = null;
        myCanvas = new GraphicViewerViewCanvas(this);
        add(myCanvas);
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
                .getParent()) {
            ;
        }
        if (container != null) {
            GraphicViewerGlobal.setComponent(container);
            if (container instanceof Frame) {
                return (Frame) container;
            }
        }
        return null;
    }

    public void addNotify() {
        super.addNotify();
        Frame frame = getFrame();
        if (frame != null) {
            initializeDragDropHandling();
        }
    }

    public GraphicViewerViewCanvas getCanvas() {
        return myCanvas;
    }

    public JScrollBar getVerticalScrollBar() {
        return myVertScroll;
    }

    public void setVerticalScrollBar(JScrollBar jscrollbar) {
        if (myVertScroll != jscrollbar) {
            if (myVertScroll != null) {
                remove(myVertScroll);
            }
            myVertScroll = jscrollbar;
            if (myVertScroll != null) {
                add(myVertScroll);
                GraphicViewerViewHelper graphicviewerviewhelper = new GraphicViewerViewHelper(
                        this);
                myVertScroll.addMouseListener(graphicviewerviewhelper);
                myVertScroll.addAdjustmentListener(graphicviewerviewhelper);
            }
            validate();
        }
    }

    public JScrollBar getHorizontalScrollBar() {
        return myHorizScroll;
    }

    public void setHorizontalScrollBar(JScrollBar jscrollbar) {
        if (myHorizScroll != jscrollbar) {
            if (myHorizScroll != null) {
                remove(myHorizScroll);
            }
            myHorizScroll = jscrollbar;
            if (myHorizScroll != null) {
                add(myHorizScroll);
                GraphicViewerViewHelper graphicviewerviewhelper = new GraphicViewerViewHelper(
                        this);
                myHorizScroll.addMouseListener(graphicviewerviewhelper);
                myHorizScroll.addAdjustmentListener(graphicviewerviewhelper);
            }
            validate();
        }
    }

    public JComponent getCorner() {
        return myCornerBox;
    }

    public void setCorner(JComponent jcomponent) {
        if (myCornerBox != jcomponent) {
            if (myCornerBox != null) {
                remove(myCornerBox);
            }
            myCornerBox = jcomponent;
            if (myCornerBox != null) {
                add(myCornerBox);
            }
            validate();
        }
    }

    public Image getBackgroundImage() {
        return myBackgroundImage;
    }

    public void setBackgroundImage(Image image) {
        Image image1 = myBackgroundImage;
        if (image1 != image) {
            myBackgroundImage = image;
            fireUpdate(112, 0, image1);
        }
    }

    Color getEffectiveBackgroundColor() {
        Color color = null;
        if (getDocument() != null) {
            color = getDocument().getPaperColor();
        }
        if (color == null) {
            color = getBackground();
        }
        if (color == null) {
            color = GraphicViewerBrush.ColorWhite;
        }
        return color;
    }

    public OTVDocument createDefaultModel() {
        return new OTVDocument();
    }

    public OTVDocument getDocument() {
        return myDocument;
    }

    public void setDocument(OTVDocument graphicviewerdocument) {
        if (graphicviewerdocument == null) {
            return;
        }
        GraphicViewerDocument graphicviewerdocument1 = myDocument;
        if (graphicviewerdocument != graphicviewerdocument1) {
            if (graphicviewerdocument1 != null) {
                doCancelMouse();
                doEndEdit();
                getSelection().clearSelection();
                for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = getFirstObjectPos()) {
                    getObjectAtPos(graphicviewerlistposition);
                    removeObjectAtPos(graphicviewerlistposition);
                }

                for (int i = 0; i < myControls.size(); i++) {
                    GraphicViewerControl graphicviewercontrol = (GraphicViewerControl) myControls
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

                myControls.clear();
                setViewPosition(0, 0);
                graphicviewerdocument1.removeDocumentListener(this);
            }
            myDocument = graphicviewerdocument;
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
                if (point.x < 0) {
                    i += point.x;
                }
                if (point.y < 0) {
                    j += point.y;
                }
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
        if (graphicviewerdocument != null && isIncludingNegativeCoords()) {
            return graphicviewerdocument.getDocumentTopLeft();
        } else {
            return new Point(0, 0);
        }
    }

    public boolean isIncludingNegativeCoords() {
        return myIncludingNegativeCoords;
    }

    public void setIncludingNegativeCoords(boolean flag) {
        if (myIncludingNegativeCoords != flag) {
            myIncludingNegativeCoords = flag;
            fireUpdate(114, flag ? 0 : 1, null);
        }
    }

    public boolean isHidingDisabledScrollbars() {
        return myHidingDisabledScrollbars;
    }

    public void setHidingDisabledScrollbars(boolean flag) {
        if (myHidingDisabledScrollbars != flag) {
            myHidingDisabledScrollbars = flag;
            fireUpdate(113, flag ? 0 : 1, null);
            invalidate();
        }
    }

    public void convertViewToDoc(Point point) {
        Point point1 = getViewPosition();
        point.x = (int) ((double) point.x / getHorizScale()) + point1.x;
        point.y = (int) ((double) point.y / getVertScale()) + point1.y;
    }

    public void convertViewToDoc(Dimension dimension) {
        dimension.width = (int) Math.ceil((double) dimension.width
                / getHorizScale());
        dimension.height = (int) Math.ceil((double) dimension.height
                / getVertScale());
    }

    public void convertViewToDoc(Rectangle rectangle) {
        Point point = getViewPosition();
        rectangle.x = (int) ((double) rectangle.x / getHorizScale()) + point.x;
        rectangle.y = (int) ((double) rectangle.y / getVertScale()) + point.y;
        rectangle.width = (int) Math.ceil((double) rectangle.width
                / getHorizScale());
        rectangle.height = (int) Math.ceil((double) rectangle.height
                / getVertScale());
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
        point.x = (int) ((double) (point.x - point1.x) * getHorizScale());
        point.y = (int) ((double) (point.y - point1.y) * getVertScale());
    }

    public void convertDocToView(Dimension dimension) {
        dimension.width = (int) Math.ceil((double) dimension.width
                * getHorizScale());
        dimension.height = (int) Math.ceil((double) dimension.height
                * getVertScale());
    }

    public void convertDocToView(Rectangle rectangle) {
        Point point = getViewPosition();
        rectangle.x = (int) ((double) (rectangle.x - point.x) * getHorizScale());
        rectangle.y = (int) ((double) (rectangle.y - point.y) * getVertScale());
        rectangle.width = (int) Math.ceil((double) rectangle.width
                * getHorizScale());
        rectangle.height = (int) Math.ceil((double) rectangle.height
                * getVertScale());
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
        Point point = myOrigin;
        if (!isIncludingNegativeCoords()) {
            if (i < 0) {
                i = 0;
            }
            if (j < 0) {
                j = 0;
            }
        }
        if (point.x != i || point.y != j) {
            Point point1 = new Point(point.x, point.y);
            myOrigin.x = i;
            myOrigin.y = j;
            fireUpdate(107, 0, point1);
        }
    }

    public Point getViewPosition() {
        return myOrigin;
    }

    public Dimension getExtentSize() {
        int i = getCanvas().getWidth();
        int j = getCanvas().getHeight();
        return new Dimension((int) ((double) i / getHorizScale()),
                (int) ((double) j / getVertScale()));
    }

    public Rectangle getViewRect() {
        Point point = getViewPosition();
        Dimension dimension = getExtentSize();
        return new Rectangle(point.x, point.y, dimension.width,
                dimension.height);
    }

    public void scrollRectToVisible(Rectangle rectangle) {
        Rectangle rectangle1 = getViewRect();
        if (containsRect(rectangle1, rectangle)) {
            return;
        }
        Dimension dimension = getExtentSize();
        int i;
        if (rectangle.width < dimension.width) {
            i = (rectangle.x + rectangle.width / 2) - dimension.width / 2;
        } else {
            i = rectangle.x;
        }
        int j;
        if (rectangle.height < dimension.height) {
            j = (rectangle.y + rectangle.height / 2) - dimension.height / 2;
        } else {
            j = rectangle.y;
        }
        setViewPosition(i, j);
    }

    public void setScale(double d) {
        double d1 = limitScale(d);
        if (myHorizScale != d1 || myVertScale != d1) {
            double d2 = myHorizScale;
            myHorizScale = d1;
            myVertScale = d1;
            fireUpdate(108, 0, new Double(d2));
        }
    }

    public double getScale() {
        return myHorizScale;
    }

    public double limitScale(double d) {
        if (d < 0.050000000000000003D) {
            d = 0.050000000000000003D;
        }
        if (d > 10D) {
            d = 10D;
        }
        return d;
    }

    private final double getHorizScale() {
        return myHorizScale;
    }

    private final double getVertScale() {
        return myVertScale;
    }

    public GraphicViewerObject pickDocObject(Point point, boolean flag) {
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = getPrevLayer(graphicviewerlayer)) {
            GraphicViewerObject graphicviewerobject = graphicviewerlayer
                    .pickObject(point, flag);
            if (graphicviewerobject != null) {
                return graphicviewerobject;
            }
        }

        return null;
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        for (GraphicViewerListPosition graphicviewerlistposition = getLastObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getPrevObjectPos(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                    .pick(point, flag);
            if (graphicviewerobject1 != null) {
                return graphicviewerobject1;
            }
        }

        return null;
    }

    public int getNumObjects() {
        return myObjects.getNumObjects();
    }

    public boolean isEmpty() {
        return myObjects.isEmpty();
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this) {
                return null;
            } else {
                myObjects.removeObject(graphicviewerobject);
                GraphicViewerListPosition graphicviewerlistposition = myObjects
                        .addObjectAtHead(graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition;
            }
        } else {
            GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                    .addObjectAtHead(graphicviewerobject);
            graphicviewerobject.setView(this, graphicviewerobject);
            return graphicviewerlistposition1;
        }
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this) {
                return null;
            } else {
                myObjects.removeObject(graphicviewerobject);
                GraphicViewerListPosition graphicviewerlistposition = myObjects
                        .addObjectAtTail(graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition;
            }
        } else {
            GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                    .addObjectAtTail(graphicviewerobject);
            graphicviewerobject.setView(this, graphicviewerobject);
            return graphicviewerlistposition1;
        }
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition1 != null) {
                myObjects.removeObjectAtPos(graphicviewerlistposition1);
                GraphicViewerListPosition graphicviewerlistposition3 = myObjects
                        .insertObjectBefore(graphicviewerlistposition,
                                graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition3;
            }
        }
        GraphicViewerListPosition graphicviewerlistposition2 = myObjects
                .insertObjectBefore(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.setView(this, graphicviewerobject);
        return graphicviewerlistposition2;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        if (graphicviewerobject.getView() != null) {
            if (graphicviewerobject.getView() != this) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition1 != null) {
                myObjects.removeObjectAtPos(graphicviewerlistposition1);
                GraphicViewerListPosition graphicviewerlistposition3 = myObjects
                        .insertObjectAfter(graphicviewerlistposition,
                                graphicviewerobject);
                graphicviewerobject.update(10, 0, null);
                return graphicviewerlistposition3;
            }
        }
        GraphicViewerListPosition graphicviewerlistposition2 = myObjects
                .insertObjectAfter(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.setView(this, graphicviewerobject);
        return graphicviewerlistposition2;
    }

    public void bringObjectToFront(GraphicViewerObject graphicviewerobject) {
        addObjectAtTail(graphicviewerobject);
    }

    public void sendObjectToBack(GraphicViewerObject graphicviewerobject) {
        addObjectAtHead(graphicviewerobject);
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return;
        }
        if (graphicviewerobject.getView() != this) {
            return;
        }
        GraphicViewerArea graphicviewerarea = graphicviewerobject.getParent();
        if (graphicviewerarea != null) {
            graphicviewerarea.removeObject(graphicviewerobject);
        } else {
            GraphicViewerListPosition graphicviewerlistposition = findObject(graphicviewerobject);
            if (graphicviewerlistposition != null) {
                removeObjectAtPos(graphicviewerlistposition);
            }
        }
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = myObjects
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject != null) {
            graphicviewerobject.setView(null, graphicviewerobject);
        }
        return graphicviewerobject;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return myObjects.getFirstObjectPos();
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return myObjects.getLastObjectPos();
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        }
        Object obj = graphicviewerlistposition.obj;
        if (obj instanceof IGraphicViewerObjectCollection) {
            IGraphicViewerObjectCollection graphicviewerobjectcollection = (IGraphicViewerObjectCollection) obj;
            if (!graphicviewerobjectcollection.isEmpty()) {
                return graphicviewerobjectcollection.getFirstObjectPos();
            }
        }
        GraphicViewerListPosition graphicviewerlistposition1;
        for (graphicviewerlistposition = graphicviewerlistposition.next; graphicviewerlistposition == null; graphicviewerlistposition = graphicviewerlistposition1.next) {
            GraphicViewerArea graphicviewerarea = ((GraphicViewerObject) (obj))
                    .getParent();
            if (graphicviewerarea == null) {
                return null;
            }
            graphicviewerlistposition1 = graphicviewerarea
                    .getCurrentListPosition();
            obj = graphicviewerarea;
        }

        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        }
        for (Object obj = graphicviewerlistposition.obj; ((GraphicViewerObject) (obj))
                .getParent() != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            graphicviewerlistposition = ((GraphicViewerObject) (obj))
                    .getParent().getCurrentListPosition();
        }

        return graphicviewerlistposition.next;
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.getPrevObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject.getView() == this) {
            return myObjects.findObject(graphicviewerobject);
        } else {
            return null;
        }
    }

    public GraphicViewerSelection createDefaultSelection() {
        return new GraphicViewerSelection(this);
    }

    public GraphicViewerSelection getSelection() {
        return mySelection;
    }

    public GraphicViewerObject selectObject(
            GraphicViewerObject graphicviewerobject) {
        return getSelection().selectObject(graphicviewerobject);
    }

    public void selectAll() {
        fireUpdate(37, 0, null);
        getSelection();
        label0 : for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible()) {
                continue;
            }
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            do {
                GraphicViewerObject graphicviewerobject;
                do {
                    if (graphicviewerlistposition == null) {
                        continue label0;
                    }
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

    private boolean containsRect(Rectangle rectangle, Rectangle rectangle1) {
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

    public void selectInBox(Rectangle rectangle) {
        ArrayList arraylist = new ArrayList();
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible()) {
                continue;
            }
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject = graphicviewerlayer
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerlayer
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                selectObjectInBox(graphicviewerobject, rectangle, arraylist);
            }

        }

        for (int i = 0; i < arraylist.size(); i++) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) arraylist
                    .get(i);
            getSelection().extendSelection(graphicviewerobject1);
        }

    }

    private void selectObjectInBox(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle, ArrayList arraylist) {
        if (!graphicviewerobject.isVisible()) {
            return;
        }
        if (graphicviewerobject.isSelectable()
                && graphicviewerobject.redirectSelection() != null
                && containsRect(rectangle, graphicviewerobject
                        .redirectSelection().getBoundingRect())) {
            arraylist.add(graphicviewerobject);
        } else if (graphicviewerobject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerarea
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                selectObjectInBox(graphicviewerobject1, rectangle, arraylist);
            }

        }
    }

    public Color getPrimarySelectionColor() {
        if (myPrimarySelectionColor == null) {
            return getDefaultPrimarySelectionColor();
        } else {
            return myPrimarySelectionColor;
        }
    }

    public void setPrimarySelectionColor(Color color) {
        Color color1 = getPrimarySelectionColor();
        if (color == null || !color1.equals(color)) {
            myPrimarySelectionColor = color;
            fireUpdate(109, 0, color1);
        }
    }

    public Color getSecondarySelectionColor() {
        if (mySecondarySelectionColor == null) {
            return getDefaultSecondarySelectionColor();
        } else {
            return mySecondarySelectionColor;
        }
    }

    public void setSecondarySelectionColor(Color color) {
        Color color1 = getSecondarySelectionColor();
        if (color == null || !color1.equals(color)) {
            mySecondarySelectionColor = color;
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
            graphics2d.scale(getHorizScale(), getVertScale());
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
            graphics2d1.scale(getHorizScale(), getVertScale());
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
        if (GraphicViewerGlobal.isAtLeastJavaVersion(1.3999999999999999D)) {
            graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
    }

    protected void paintView(Graphics2D graphics2d, Rectangle rectangle) {
        applyRenderingHints(graphics2d);
        paintPaperColor(graphics2d, rectangle);
        paintBackgroundDecoration(graphics2d, rectangle);
        paintDocumentObjects(graphics2d, rectangle);
        paintViewObjects(graphics2d, rectangle);
    }

    protected void paintPaperColor(Graphics2D graphics2d, Rectangle rectangle) {
        graphics2d.setColor(getEffectiveBackgroundColor());
        graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width + 5,
                rectangle.height + 5);
    }

    protected void paintBackgroundDecoration(Graphics2D graphics2d,
            Rectangle rectangle) {
        Image image = getBackgroundImage();
        if (image != null) {
            graphics2d.drawImage(image, 0, 0, this);
        }
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
            if (!graphicviewerlayer.isVisible()) {
                continue;
            }
            if (graphicviewerlayer.getTransparency() != f) {
                f = graphicviewerlayer.getTransparency();
                AlphaComposite alphacomposite = AlphaComposite
                        .getInstance(3, f);
                graphics2d.setComposite(alphacomposite);
            }
            myFontRenderContext = graphics2d.getFontRenderContext();
            graphicviewerlayer.paint(graphics2d, this, rectangle);
        }

        if (f != 1.0F) {
            graphics2d.setComposite(AlphaComposite.SrcOver);
        }
    }

    protected void paintViewObjects(Graphics2D graphics2d, Rectangle rectangle) {
        myFontRenderContext = graphics2d.getFontRenderContext();
        Rectangle rectangle1 = new Rectangle(0, 0, 0, 0);
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject.isVisible()) {
                Rectangle rectangle2 = graphicviewerobject.getBoundingRect();
                rectangle1.x = rectangle2.x;
                rectangle1.y = rectangle2.y;
                rectangle1.width = rectangle2.width;
                rectangle1.height = rectangle2.height;
                graphicviewerobject.expandRectByPenWidth(rectangle1);
                if (rectangle1.intersects(rectangle)) {
                    graphicviewerobject.paint(graphics2d, this);
                }
            }
        } while (true);
        for (int i = 0; i < myControls.size(); i++) {
            GraphicViewerControl graphicviewercontrol = (GraphicViewerControl) myControls
                    .get(i);
            graphicviewercontrol.paint(graphics2d, this);
        }

    }

    FontRenderContext getFontRenderContext() {
        return myFontRenderContext;
    }

    public void updateScrollbars() {
        JScrollBar jscrollbar = getHorizontalScrollBar();
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if (jscrollbar == null && jscrollbar1 == null) {
            return;
        }
        Dimension dimension = getDocumentSize();
        Point point = getDocumentTopLeft();
        Point point1 = getViewPosition();
        Insets insets = getInsets();
        int i = getHeight() - insets.top - insets.bottom;
        int j = getWidth() - insets.left - insets.right;
        int k = (int) ((double) i / getVertScale());
        int l = (int) ((double) j / getHorizScale());
        boolean flag = k < dimension.height || point1.y > point.y
                || point1.y + k < point.y + dimension.height;
        boolean flag1 = l < dimension.width || point1.x > point.x
                || point1.x + l < point.x + dimension.width;
        if (jscrollbar != null && (isScrollbarVisible(jscrollbar) || flag1)) {
            i -= getScrollbarHeight(jscrollbar);
            k = (int) ((double) i / getVertScale());
        }
        if (jscrollbar1 != null && (isScrollbarVisible(jscrollbar1) || flag)) {
            j -= getScrollbarWidth(jscrollbar1);
            l = (int) ((double) j / getHorizScale());
        }
        flag = k < dimension.height || point1.y > point.y
                || point1.y + k < point.y + dimension.height;
        flag1 = l < dimension.width || point1.x > point.x
                || point1.x + l < point.x + dimension.width;
        myUpdatingScrollbars = true;
        boolean flag2 = false;
        if (jscrollbar1 != null) {
            jscrollbar1.setValues(point1.y, k, point.y, point.y
                    + dimension.height);
            jscrollbar1.setEnabled(flag);
            if (flag && !isScrollbarVisible(jscrollbar1)) {
                jscrollbar1.setVisible(true);
                flag2 = true;
            } else if (!flag && isHidingDisabledScrollbars()
                    && isScrollbarVisible(jscrollbar1)) {
                jscrollbar1.setVisible(false);
                flag2 = true;
            }
        }
        if (jscrollbar != null) {
            jscrollbar.setValues(point1.x, l, point.x, point.x
                    + dimension.width);
            jscrollbar.setEnabled(flag1);
            if (flag1 && !isScrollbarVisible(jscrollbar)) {
                jscrollbar.setVisible(true);
                flag2 = true;
            } else if (!flag1 && isHidingDisabledScrollbars()
                    && isScrollbarVisible(jscrollbar)) {
                jscrollbar.setVisible(false);
                flag2 = true;
            }
        }
        if (flag2) {
            validate();
        }
        myUpdatingScrollbars = false;
    }

    public void updateView(Rectangle rectangle) {
        GraphicViewerViewCanvas graphicviewerviewcanvas = getCanvas();
        if (rectangle.x < graphicviewerviewcanvas.getWidth()
                && rectangle.y < graphicviewerviewcanvas.getHeight()
                && rectangle.x + rectangle.width >= 0
                && rectangle.y + rectangle.height >= 0) {
            graphicviewerviewcanvas.repaint(rectangle);
        }
    }

    public void updateView() {
        Rectangle rectangle = new Rectangle(0, 0, getCanvas().getWidth(),
                getCanvas().getHeight());
        updateView(rectangle);
        updateScrollbars();
    }

    public void addViewListener(
            IGraphicViewerViewListener graphicviewerviewlistener) {
        if (myViewListeners == null) {
            myViewListeners = new ArrayList();
        }
        if (!myViewListeners.contains(graphicviewerviewlistener)) {
            myViewListeners.add(graphicviewerviewlistener);
        }
    }

    public void removeViewListener(
            IGraphicViewerViewListener graphicviewerviewlistener) {
        if (myViewListeners != null) {
            myViewListeners.remove(graphicviewerviewlistener);
        }
    }

    public IGraphicViewerViewListener[] getViewListeners() {
        if (myViewListeners == null) {
            return null;
        }
        Object aobj[] = myViewListeners.toArray();
        IGraphicViewerViewListener agraphicviewerviewlistener[] = new IGraphicViewerViewListener[aobj.length];
        for (int i = 0; i < aobj.length; i++) {
            agraphicviewerviewlistener[i] = (IGraphicViewerViewListener) aobj[i];
        }

        return agraphicviewerviewlistener;
    }

    public void addDocumentListener(
            IGraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (getDocument() != null) {
            getDocument().addDocumentListener(graphicviewerdocumentlistener);
        }
    }

    public void removeDocumentListener(
            IGraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (getDocument() != null) {
            getDocument().removeDocumentListener(graphicviewerdocumentlistener);
        }
    }

    public IGraphicViewerDocumentListener[] getDocumentListeners() {
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
        if (myViewListeners != null && myViewListeners.size() > 0) {
            if (myViewEvent == null) {
                myViewEvent = new GraphicViewerViewEvent(this, i, j, obj,
                        point, point1, k);
            } else {
                myViewEvent.setHint(i);
                myViewEvent.setFlags(j);
                myViewEvent.setObject(obj);
                myViewEvent.setPointViewCoords(point);
                myViewEvent.setPointDocCoords(point1);
                myViewEvent.setModifiers(k);
                myViewEvent.setConsumed(false);
            }
            GraphicViewerViewEvent graphicviewerviewevent = myViewEvent;
            myViewEvent = null;
            invokeViewListeners(graphicviewerviewevent);
            myViewEvent = graphicviewerviewevent;
            myViewEvent.setObject(null);
        }
    }

    void invokeViewListeners(GraphicViewerViewEvent graphicviewerviewevent) {
        if (myViewListeners != null) {
            for (int i = 0; i < myViewListeners.size(); i++) {
                IGraphicViewerViewListener graphicviewerviewlistener = (IGraphicViewerViewListener) myViewListeners
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
                Rectangle rectangle = graphicviewerdocumentevent
                        .getTempRectangle();
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
                Rectangle rectangle1 = graphicviewerdocumentevent
                        .getTempRectangle();
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
                            .getNumHandles(graphicviewerobject) <= 0) {
                        break;
                    }
                    if (graphicviewerobject.isVisible()) {
                        graphicviewerobject
                                .showSelectionHandles(graphicviewerselection);
                    } else {
                        graphicviewerobject
                                .hideSelectionHandles(graphicviewerselection);
                    }
                    break;
                }
                if (graphicviewerdocumentevent.getFlags() != 2) {
                    break;
                }
                GraphicViewerSelection graphicviewerselection1 = getSelection();
                if (!graphicviewerselection1.isSelected(graphicviewerobject)) {
                    break;
                }
                if (graphicviewerobject.isVisible()) {
                    graphicviewerobject
                            .showSelectionHandles(graphicviewerselection1);
                } else {
                    graphicviewerobject
                            .hideSelectionHandles(graphicviewerselection1);
                }
                break;

            case 202 :
                Rectangle rectangle2 = graphicviewerdocumentevent
                        .getTempRectangle();
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
                Rectangle rectangle3 = graphicviewerdocumentevent
                        .getTempRectangle();
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
        } catch (SecurityException se) {
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
        if (pageformat1 != pageformat) {
            return pageformat1;
        } else {
            return null;
        }
    }

    private void internalPrint(PrinterJob printerjob) {
        PageFormat pageformat = printShowPageDialog(printerjob);
        if (pageformat != null) {
            printEnd(null, pageformat);
            int i = calculatePageCount(pageformat);
            Book book = new Book();
            book.append(this, pageformat, i);
            printerjob.setPageable(book);
            if (printerjob.printDialog()) {
                boolean flag = isDoubleBuffered();
                try {
                    setDoubleBuffered(false);
                    printerjob.print();
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
                setDoubleBuffered(flag);
            }
        }
    }

    protected void printBegin(Graphics2D graphics2d, PageFormat pageformat) {
        if (!myPrintBegun) {
            myPrintBegun = true;
            myPrintDocSize = getPrintDocumentSize();
            myPrintDocTopLeft = getPrintDocumentTopLeft();
            int i = myPrintDocSize.width;
            int j = myPrintDocSize.height;
            myPrintPageRect = getPrintPageRect(graphics2d, pageformat);
            double d = myPrintPageRect.width;
            double d1 = myPrintPageRect.height;
            myPrintHorizScale = getPrintScale(graphics2d, pageformat);
            myPrintVertScale = myPrintHorizScale;
            myPrintNumPagesAcross = (int) Math
                    .ceil(((double) i * myPrintHorizScale) / d);
            myPrintNumPagesDown = (int) Math
                    .ceil(((double) j * myPrintVertScale) / d1);
        }
    }

    public Dimension getPrintDocumentSize() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        Dimension dimension = new Dimension(1, 1);
        if (graphicviewerdocument == null) {
            return dimension;
        }
        int i = 0;
        int j = 0;
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
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
            if (rectangle.x < i) {
                i = rectangle.x;
            }
            if (rectangle.y < j) {
                j = rectangle.y;
            }
            if (rectangle.x + rectangle.width > dimension.width) {
                dimension.width = rectangle.x + rectangle.width;
            }
            if (rectangle.y + rectangle.height > dimension.height) {
                dimension.height = rectangle.y + rectangle.height;
            }
        } while (true);
        if (isIncludingNegativeCoords()) {
            if (i < 0) {
                dimension.width -= i;
            }
            if (j < 0) {
                dimension.height -= j;
            }
        }
        return dimension;
    }

    public Point getPrintDocumentTopLeft() {
        Point point = new Point(0, 0);
        if (isIncludingNegativeCoords()) {
            GraphicViewerDocument graphicviewerdocument = getDocument();
            if (graphicviewerdocument == null) {
                return point;
            }
            int i = point.x;
            int j = point.y;
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null) {
                    break;
                }
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
                if (rectangle.x < i) {
                    i = rectangle.x;
                }
                if (rectangle.y < j) {
                    j = rectangle.y;
                }
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
        myPrintBegun = false;
    }

    public int print(Graphics g, PageFormat pageformat, int i) {
        Graphics2D graphics2d = (Graphics2D) g;
        printBegin(graphics2d, pageformat);
        double d = myPrintPageRect.x;
        double d1 = myPrintPageRect.y;
        double d2 = myPrintPageRect.width;
        double d3 = myPrintPageRect.height;
        if (i >= myPrintNumPagesAcross * myPrintNumPagesDown) {
            printEnd(graphics2d, pageformat);
            return 1;
        }
        int j = i % myPrintNumPagesAcross;
        int k = i / myPrintNumPagesAcross;
        printDecoration(graphics2d, pageformat, j, k);
        Point point = getViewPosition();
        double d4 = getHorizScale();
        double d5 = getVertScale();
        try {
            myOrigin = new Point(
                    (int) ((double) myPrintDocTopLeft.x + (double) j
                            * (d2 / myPrintHorizScale)),
                    (int) ((double) myPrintDocTopLeft.y + (double) k
                            * (d3 / myPrintVertScale)));
            myHorizScale = myPrintHorizScale;
            myVertScale = myPrintVertScale;
            graphics2d.clip(myPrintPageRect);
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            graphics2d.getClipBounds(rectangle);
            rectangle.x -= (int) d;
            rectangle.y -= (int) d1;
            convertViewToDoc(rectangle);
            graphics2d.translate(d, d1);
            graphics2d.scale(getHorizScale(), getVertScale());
            Point point1 = getViewPosition();
            graphics2d.translate(-point1.x, -point1.y);
            myIsPrinting = true;
            printView(graphics2d, rectangle);
        } finally {
            myIsPrinting = false;
            myOrigin = point;
            myHorizScale = d4;
            myVertScale = d5;
        }
        return 0;
    }

    private int calculatePageCount(PageFormat pageformat) {
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
        return myIsPrinting;
    }

    protected void printDecoration(Graphics2D graphics2d,
            PageFormat pageformat, int i, int j) {
        double d = myPrintPageRect.x;
        double d1 = myPrintPageRect.y;
        double d2 = myPrintPageRect.width;
        double d3 = myPrintPageRect.height;
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
        int i = calculatePageCount(pageformat);
        Frame frame = getFrame();
        if (frame != null) {
            GraphicViewerPrintPreview graphicviewerprintpreview = new GraphicViewerPrintPreview(
                    frame, this, s1, pageformat, i);
            graphicviewerprintpreview.setVisible(true);
        }
    }

    boolean isScrollbarVisible(JScrollBar jscrollbar) {
        return jscrollbar.isVisible();
    }

    int getScrollbarWidth(JScrollBar jscrollbar) {
        return jscrollbar.getWidth();
    }

    int getScrollbarHeight(JScrollBar jscrollbar) {
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
        if (jscrollbar != null && isScrollbarVisible(jscrollbar)) {
            j1 = jscrollbar.getHeight();
        }
        if (jscrollbar1 != null && isScrollbarVisible(jscrollbar1)) {
            i1 = jscrollbar1.getWidth();
        }
        int k1 = i - i1 - insets.right;
        int l1 = j - j1 - insets.bottom;
        int i2 = k1 - k;
        int j2 = l1 - l;
        getCanvas().setBounds(k, l, i2, j2);
        if (jscrollbar != null && isScrollbarVisible(jscrollbar)) {
            jscrollbar.setBounds(k, l1, i2, j1);
            int k2 = jscrollbar.getUnitIncrement();
            int i3 = (int) ((double) i2 / getHorizScale());
            int k3 = Math.max(k2, i3 - k2);
            jscrollbar.setBlockIncrement(k3);
        }
        if (jscrollbar1 != null && isScrollbarVisible(jscrollbar1)) {
            jscrollbar1.setBounds(k1, l, i1, j2);
            int l2 = jscrollbar1.getUnitIncrement();
            int j3 = (int) ((double) j2 / getVertScale());
            int l3 = Math.max(l2, j3 - l2);
            jscrollbar1.setBlockIncrement(l3);
        }
        if (jcomponent != null) {
            if (i1 != 0 && j1 != 0) {
                jcomponent.setBounds(k1, l1, i1, j1);
                jcomponent.setVisible(true);
            } else {
                jcomponent.setVisible(false);
            }
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
        if (isMinimumSizeSet()) {
            return super.getMinimumSize();
        } else {
            return new Dimension(50, 50);
        }
    }

    protected void onScrollEvent(AdjustmentEvent adjustmentevent) {
        if (myUpdatingScrollbars) {
            return;
        }
        Point point = getViewPosition();
        int i = point.x;
        int j = point.y;
        JScrollBar jscrollbar = getHorizontalScrollBar();
        if (jscrollbar != null) {
            i = jscrollbar.getValue();
        }
        JScrollBar jscrollbar1 = getVerticalScrollBar();
        if (jscrollbar1 != null) {
            j = jscrollbar1.getValue();
        }
        setViewPosition(i, j);
    }

    public void setKeyEnabled(boolean flag) {
        if (flag && !myKeyEnabled) {
            myKeyEnabled = true;
            addKeyListener(this);
            fireUpdate(115, 0, null);
        } else if (!flag && myKeyEnabled) {
            myKeyEnabled = false;
            removeKeyListener(this);
            fireUpdate(115, 1, null);
        }
    }

    public boolean isKeyEnabled() {
        return myKeyEnabled;
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
            if (keyevent.isControlDown()) {
                jscrollbar = getHorizontalScrollBar();
            } else {
                jscrollbar = getVerticalScrollBar();
            }
            if (jscrollbar == null) {
                return;
            }
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
            if (byte0 == 1) {
                i1 = Math.max(i1, j);
            } else {
                i1 = Math.min(i1, j);
            }
            jscrollbar.setValue(i1);
        }
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public boolean isIgnoreNextMouseDown() {
        return myIgnoreNextMouseDown;
    }

    public void setIgnoreNextMouseDown(boolean flag) {
        myIgnoreNextMouseDown = flag;
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

    /** @deprecated */
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
        if (flag && !myMouseEnabled) {
            myMouseEnabled = true;
            getCanvas().addMouseListener(getCanvas());
            getCanvas().addMouseMotionListener(getCanvas());
            fireUpdate(116, 0, null);
        } else if (!flag && myMouseEnabled) {
            myMouseEnabled = false;
            getCanvas().removeMouseListener(getCanvas());
            getCanvas().removeMouseMotionListener(getCanvas());
            fireUpdate(116, 1, null);
        }
    }

    public boolean isMouseEnabled() {
        return myMouseEnabled;
    }

    protected final MouseEvent getCurrentMouseEvent() {
        return myMouseEvent;
    }

    Point getMouseEventPoint(MouseEvent mouseevent) {
        return mouseevent.getPoint();
    }

    int getMouseEventModifiers(MouseEvent mouseevent) {
        return mouseevent.getModifiers();
    }

    protected void onMousePressed(MouseEvent mouseevent) {
        if (!isMouseEnabled()) {
            return;
        }
        TRACE("onMouse Pressed", mouseevent);
        boolean flag = hasFocus();
        if (!flag) {
            JInternalFrame jinternalframe = null;
            java.awt.Container container = getParent();
            do {
                if (container == null) {
                    break;
                }
                if (container instanceof JInternalFrame) {
                    jinternalframe = (JInternalFrame) container;
                    break;
                }
                container = container.getParent();
            } while (true);
            if (jinternalframe != null
                    && GraphicViewerGlobal.isAtLeastJavaVersion(1.3D)
                    && !GraphicViewerGlobal
                            .isAtLeastJavaVersion(1.3999999999999999D)) {
                try {
                    jinternalframe.setSelected(false);
                    jinternalframe.setSelected(true);
                } catch (Exception e) {
                    ExceptionDialog.ignoreException(e);
                }
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
        if (!flag && flag1) {
            return;
        }
        myMouseEvent = mouseevent;
        Point point = getMouseEventPoint(mouseevent);
        myMouseDocPoint.x = point.x;
        myMouseDocPoint.y = point.y;
        convertViewToDoc(myMouseDocPoint);
        if (mouseevent.getClickCount() == 1) {
            doMouseDown(getMouseEventModifiers(mouseevent), myMouseDocPoint,
                    point);
        }
        myMouseEvent = null;
    }

    protected void onMouseReleased(MouseEvent mouseevent) {
        if (!isMouseEnabled()) {
            return;
        }
        TRACE("onMouse Released", mouseevent);
        myInternalDragStarted = false;
        if (!hasFocus() && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
            if (getState() != 0) {
                doCancelMouse();
            }
            return;
        }
        myMouseEvent = mouseevent;
        Point point = getMouseEventPoint(mouseevent);
        myMouseDocPoint.x = point.x;
        myMouseDocPoint.y = point.y;
        convertViewToDoc(myMouseDocPoint);
        if (mouseevent.getClickCount() <= 1) {
            if (doMouseUp(getMouseEventModifiers(mouseevent), myMouseDocPoint,
                    point)) {
                setState(0);
            }
        } else {
            TRACE("onMouse Double Click", mouseevent);
            doMouseDblClick(getMouseEventModifiers(mouseevent),
                    myMouseDocPoint, point);
        }
        myMouseEvent = null;
    }

    protected void onMouseMoved(MouseEvent mouseevent) {
        if (!isMouseEnabled()) {
            return;
        }
        TRACE("onMouse Moved", mouseevent);
        myInternalDragStarted = false;
        if (!hasFocus() && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
            if (getState() != 0) {
                doCancelMouse();
            }
            setIgnoreNextMouseDown(true);
            return;
        } else {
            myMouseEvent = mouseevent;
            Point point = getMouseEventPoint(mouseevent);
            myMouseDocPoint.x = point.x;
            myMouseDocPoint.y = point.y;
            convertViewToDoc(myMouseDocPoint);
            doMouseMove(getMouseEventModifiers(mouseevent), myMouseDocPoint,
                    point);
            myMouseEvent = null;
            return;
        }
    }

    protected void onMouseDragged(MouseEvent mouseevent) {
        if (!isMouseEnabled()) {
            return;
        }
        TRACE("onMouse Dragged", mouseevent);
        if (!hasFocus() && !GraphicViewerGlobal.isAtLeastJavaVersion(1.22D)) {
            if (getState() != 0) {
                doCancelMouse();
            }
            return;
        }
        if (!isDragEnabled() || myInternalDragStarted) {
            myMouseEvent = mouseevent;
            Point point = getMouseEventPoint(mouseevent);
            myMouseDocPoint.x = point.x;
            myMouseDocPoint.y = point.y;
            convertViewToDoc(myMouseDocPoint);
            doMouseMove(getMouseEventModifiers(mouseevent), myMouseDocPoint,
                    point);
            myMouseEvent = null;
        } else {
            myInternalDragStarted = true;
        }
    }

    public boolean doMouseDown(int i, Point point, Point point1) {
        TRACE("  doMouse  Down");
        myMouseDownPoint.x = point.x;
        myMouseDownPoint.y = point.y;
        IGraphicViewerActionObject graphicvieweractionobject = pickActionObject(point);
        if (graphicvieweractionobject != null
                && startActionObject(graphicvieweractionobject, i, point,
                        point1)) {
            return true;
        }
        if ((i & 0x10) != 0 && getDocument() != null
                && getDocument().isModifiable()) {
            GraphicViewerHandle graphicviewerhandle = pickHandle(point);
            if (graphicviewerhandle != null
                    && startResizing(graphicviewerhandle, point, point1)) {
                return true;
            }
            GraphicViewerPort graphicviewerport = pickPort(point);
            if (graphicviewerport != null
                    && startNewLink(graphicviewerport, point)) {
                return true;
            }
        }
        setCurrentObject(pickDocObject(point, true));
        if (getCurrentObject() != null) {
            setState(1);
        } else {
            startDragBoxSelection(i, point, point1);
        }
        return true;
    }

    public boolean doMouseMove(int i, Point point, Point point1) {
        TRACE("  doMouse  Move");
        switch (getState()) {
            default :
                break;

            case 0 : // '\0'
                doUncapturedMouseMove(i, point, point1);
                break;

            case 1 : // '\001'
                if (getCurrentObject() == null) {
                    startDragBoxSelection(i, point, point1);
                } else {
                    startMoveSelection(i, point, point1);
                }
                break;

            case 2 : // '\002'
                int j = point.x - getCurrentObject().getLeft() - myMoveOffset.x;
                int k = point.y - getCurrentObject().getTop() - myMoveOffset.y;
                doMoveSelection(i, j, k, 2);
                break;

            case 6 : // '\006'
                Graphics2D graphics2d = getGraphicViewerGraphics();
                myAnchorPoint.x = myMouseDownPoint.x;
                myAnchorPoint.y = myMouseDownPoint.y;
                convertDocToView(myAnchorPoint);
                drawXORBox(graphics2d, myAnchorPoint.x, myAnchorPoint.y,
                        point1.x, point1.y, 2);
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
                    Point point2 = graphicviewerport
                            .getToLinkPoint(myAnchorPoint);
                    myTempEndPort.setLocation(point2);
                } else {
                    myTempEndPort.setLocation(point);
                }
                break;

            case 4 : // '\004'
                GraphicViewerPort graphicviewerport1 = pickNearestPort(point);
                if (graphicviewerport1 != null) {
                    Point point3 = graphicviewerport1
                            .getFromLinkPoint(myAnchorPoint);
                    myTempEndPort.setLocation(point3);
                } else {
                    myTempEndPort.setLocation(point);
                }
                break;

            case 7 : // '\007'
                handleActionObject(i, point, point1, 2);
                break;
        }
        return true;
    }

    public boolean doMouseUp(int i, Point point, Point point1) {
        TRACE("  doMouse  Up");
        switch (getState()) {
            case 0 : // '\0'
            default :
                break;

            case 1 : // '\001'
                if (getCurrentObject() != null) {
                    if (keyExtendSelection(i)) {
                        setCurrentObject(getSelection().extendSelection(
                                getCurrentObject()));
                    } else if (keyToggleSelection(i)) {
                        setCurrentObject(getSelection().toggleSelection(
                                getCurrentObject()));
                    } else if (keySingleSelection(i)) {
                        setCurrentObject(selectObject(getCurrentObject()));
                    }
                } else if (keyClearSelection(i)) {
                    getSelection().clearSelection();
                }
                doMouseClick(i, point, point1);
                break;

            case 2 : // '\002'
                int j = point.x - getCurrentObject().getLeft() - myMoveOffset.x;
                int k = point.y - getCurrentObject().getTop() - myMoveOffset.y;
                doMoveSelection(i, j, k, 3);
                if (myHideSelectionOnMouse) {
                    getSelection().restoreSelectionHandles(null);
                }
                break;

            case 6 : // '\006'
                Graphics2D graphics2d = getGraphicViewerGraphics();
                myAnchorPoint.x = myMouseDownPoint.x;
                myAnchorPoint.y = myMouseDownPoint.y;
                convertDocToView(myAnchorPoint);
                drawXORBox(graphics2d, myAnchorPoint.x, myAnchorPoint.y,
                        point1.x, point1.y, 3);
                disposeGraphicViewerGraphics(graphics2d);
                if (Math.abs(point1.x - myAnchorPoint.x) < 3
                        && Math.abs(point1.y - myAnchorPoint.y) < 3) {
                    doBackgroundClick(i, point, point1);
                } else {
                    Rectangle rectangle = viewToDocCoords(myPrevXORRect);
                    selectInBox(rectangle);
                }
                break;

            case 5 : // '\005'
                Graphics2D graphics2d1 = getGraphicViewerGraphics();
                handleResizing(graphics2d1, point1, point, 3);
                if (myHideSelectionOnMouse) {
                    getSelection().restoreSelectionHandles(getCurrentObject());
                }
                disposeGraphicViewerGraphics(graphics2d1);
                break;

            case 3 : // '\003'
                GraphicViewerPort graphicviewerport = pickNearestPort(point);
                if (graphicviewerport != null) {
                    if (myTempLink == null || myTempLink.getLayer() == null) {
                        removeObject(myTempLink);
                        newLink(myTempStartPort, graphicviewerport);
                    } else {
                        reLink(myTempLink, myTempStartPort, graphicviewerport);
                        if (myHideSelectionOnMouse) {
                            getSelection().restoreSelectionHandles(myTempLink);
                        }
                    }
                } else {
                    GraphicViewerPort graphicviewerport2 = pickPort(point);
                    if (myTempLink == null || myTempLink.getLayer() == null) {
                        removeObject(myTempLink);
                        noNewLink(myTempStartPort, graphicviewerport2);
                    } else {
                        noReLink(myTempLink, myTempStartPort,
                                graphicviewerport2);
                    }
                }
                myTempLink = null;
                myTempStartPort = null;
                myTempEndPort = null;
                myOrigEndPort = null;
                break;

            case 4 : // '\004'
                GraphicViewerPort graphicviewerport1 = pickNearestPort(point);
                if (graphicviewerport1 != null) {
                    if (myTempLink == null || myTempLink.getLayer() == null) {
                        removeObject(myTempLink);
                        newLink(graphicviewerport1, myTempStartPort);
                    } else {
                        reLink(myTempLink, graphicviewerport1, myTempStartPort);
                        if (myHideSelectionOnMouse) {
                            getSelection().restoreSelectionHandles(myTempLink);
                        }
                    }
                } else {
                    GraphicViewerPort graphicviewerport3 = pickPort(point);
                    if (myTempLink == null || myTempLink.getLayer() == null) {
                        removeObject(myTempLink);
                        noNewLink(graphicviewerport3, myTempStartPort);
                    } else {
                        noReLink(myTempLink, graphicviewerport3,
                                myTempStartPort);
                    }
                }
                myTempLink = null;
                myTempStartPort = null;
                myTempEndPort = null;
                myOrigEndPort = null;
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
            for (; obj != null; obj = ((GraphicViewerObject) (obj)).getParent()) {
                if (((GraphicViewerObject) (obj)).doMouseClick(i, point,
                        point1, this)) {
                    return true;
                }
            }

        } else {
            doBackgroundClick(i, point, point1);
        }
        return false;
    }

    public boolean doMouseDblClick(int i, Point point, Point point1) {
        Object obj = pickDocObject(point, false);
        if (obj != null) {
            fireUpdate(23, 0, obj, point1, point, i);
            for (; obj != null; obj = ((GraphicViewerObject) (obj)).getParent()) {
                if (((GraphicViewerObject) (obj)).doMouseDblClick(i, point,
                        point1, this)) {
                    return true;
                }
            }

        } else {
            fireUpdate(25, 0, null, point1, point, i);
        }
        return false;
    }

    public void doCancelMouse() {
        TRACE("mouse Cancelled");
        if (!GraphicViewerGlobal.isAtLeastJavaVersion(1.3999999999999999D)) {
            setIgnoreNextMouseDown(true);
        }
        switch (getState()) {
            case 0 : // '\0'
            case 1 : // '\001'
            default :
                break;

            case 2 : // '\002'
                doCancelMoveSelection(myMoveOffset);
                if (myHideSelectionOnMouse) {
                    getSelection().restoreSelectionHandles(null);
                }
                break;

            case 6 : // '\006'
                Graphics2D graphics2d = getGraphicViewerGraphics();
                drawXORBox(graphics2d, 0, 0, 0, 0, 3);
                disposeGraphicViewerGraphics(graphics2d);
                break;

            case 5 : // '\005'
                doCancelResize(myOrigResizeRect);
                if (getCurrentObject() != null && myHideSelectionOnMouse) {
                    getSelection().restoreSelectionHandles(getCurrentObject());
                }
                break;

            case 3 : // '\003'
                if (myTempLink == null || myTempLink.getLayer() == null) {
                    removeObject(myTempLink);
                    noNewLink(myTempStartPort, null);
                } else if (myOrigEndPort == null) {
                    noReLink(myTempLink, myTempStartPort, null);
                } else {
                    myTempLink.setToPort(myOrigEndPort);
                    if (myHideSelectionOnMouse) {
                        getSelection().restoreSelectionHandles(myTempLink);
                    }
                    if (getDocument() != null) {
                        getDocument().endTransaction(false);
                    }
                }
                myTempLink = null;
                myTempStartPort = null;
                myTempEndPort = null;
                myOrigEndPort = null;
                break;

            case 4 : // '\004'
                if (myTempLink == null || myTempLink.getLayer() == null) {
                    removeObject(myTempLink);
                    noNewLink(null, myTempStartPort);
                } else if (myOrigEndPort == null) {
                    noReLink(myTempLink, null, myTempStartPort);
                } else {
                    myTempLink.setFromPort(myOrigEndPort);
                    if (myHideSelectionOnMouse) {
                        getSelection().restoreSelectionHandles(myTempLink);
                    }
                    if (getDocument() != null) {
                        getDocument().endTransaction(false);
                    }
                }
                myTempLink = null;
                myTempStartPort = null;
                myTempEndPort = null;
                myOrigEndPort = null;
                break;

            case 7 : // '\007'
                if (getCurrentObject() instanceof IGraphicViewerActionObject) {
                    IGraphicViewerActionObject graphicvieweractionobject = (IGraphicViewerActionObject) getCurrentObject();
                    graphicvieweractionobject.onActionCancelled(this);
                    graphicvieweractionobject.setActionActivated(false);
                }
                break;
        }
        setState(0);
        setCursor(getDefaultCursor());
    }

    public final int getState() {
        return myMouseState;
    }

    public void setState(int i) {
        myMouseState = i;
    }

    public final GraphicViewerObject getCurrentObject() {
        return myCurrentObject;
    }

    public void setCurrentObject(GraphicViewerObject graphicviewerobject) {
        myCurrentObject = graphicviewerobject;
    }

    public void doUncapturedMouseMove(int i, Point point, Point point1) {
        GraphicViewerObject graphicviewerobject = pickObject(point, false);
        if (graphicviewerobject != null
                && graphicviewerobject.doUncapturedMouseMove(i, point, point1,
                        this)) {
            return;
        }
        for (Object obj = pickDocObject(point, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            if (((GraphicViewerObject) (obj)).doUncapturedMouseMove(i, point,
                    point1, this)) {
                return;
            }
        }

        setCursor(getDefaultCursor());
    }

    public void setCursor(Cursor cursor) {
        if (getCursor() == cursor) {
            return;
        }
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

    void setCursorType(int i) {
        setCursor(Cursor.getPredefinedCursor(i));
    }

    public Cursor getDefaultCursor() {
        if (myDefaultCursor == null) {
            myDefaultCursor = Cursor.getPredefinedCursor(0);
        }
        return myDefaultCursor;
    }

    public void setDefaultCursor(Cursor cursor) {
        Cursor cursor1 = myDefaultCursor;
        if (cursor1 != cursor) {
            myDefaultCursor = cursor;
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
            if (l == 1) {
                graphicviewerdocument.startTransaction();
            }
            int i1 = convertActionToModifiers(1);
            boolean flag = (i & i1) == i1;
            int j1 = getInternalMouseActions();
            boolean flag1 = (j1 & 1) != 0;
            boolean flag2 = j1 == 1;
            boolean flag3 = flag2 | flag1 & flag;
            if (l != 3) {
                if (flag3 || !isDragsRealtime()) {
                    makeDragSelection();
                    moveSelection(getDragSelection(), i, j, k, l);
                } else {
                    clearDragSelection();
                    moveSelection(getSelection(), i, j, k, l);
                }
            } else {
                int k1 = j;
                int l1 = k;
                if (myDragSelection != null) {
                    k1 = getCurrentObject().getLeft()
                            - myDragSelectionOrigObj.getLeft();
                    l1 = getCurrentObject().getTop()
                            - myDragSelectionOrigObj.getTop();
                }
                clearDragSelection();
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
        return myDragsRealtime;
    }

    public void setDragsRealtime(boolean flag) {
        boolean flag1 = myDragsRealtime;
        if (flag1 != flag) {
            myDragsRealtime = flag;
            fireUpdate(120, flag1 ? 1 : 0, null);
        }
    }

    public int getInternalMouseActions() {
        return myMouseActions;
    }

    public void setInternalMouseActions(int i) {
        int j = myMouseActions;
        if (j != i) {
            myMouseActions = i;
            fireUpdate(121, j, null);
        }
    }

    public boolean isDragsSelectionImage() {
        return myDragsSelectionImage;
    }

    public void setDragsSelectionImage(boolean flag) {
        boolean flag1 = myDragsSelectionImage;
        if (flag1 != flag) {
            myDragsSelectionImage = flag;
            fireUpdate(122, flag1 ? 1 : 0, null);
        }
    }

    GraphicViewerSelection getDragSelection() {
        return myDragSelection;
    }

    GraphicViewerSelection createDragSelection() {
        if (getCurrentObject() == null) {
            return null;
        }
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
                if (graphicviewerlistposition1 == null) {
                    break;
                }
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
                        myFontRenderContext = graphics2d.getFontRenderContext();
                        Rectangle rectangle2 = graphicviewerobject1
                                .getBoundingRect();
                        rectangle1.x = rectangle2.x;
                        rectangle1.y = rectangle2.y;
                        rectangle1.width = rectangle2.width;
                        rectangle1.height = rectangle2.height;
                        graphicviewerobject1.expandRectByPenWidth(rectangle1);
                        if (rectangle1.intersects(rectangle)) {
                            graphicviewerobject1.paint(graphics2d, this);
                        }
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
                if (graphicviewerlistposition == null) {
                    break;
                }
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
                if (getCurrentObject() == graphicviewerobject) {
                    setCurrentObject(((GraphicViewerObject) (obj)));
                }
            } while (true);
        }
        return graphicviewerselection;
    }

    void makeDragSelection() {
        if (myDragSelection == null) {
            myDragSelectionOrigObj = getCurrentObject();
            myDragSelection = createDragSelection();
            if (myDragSelection.isEmpty()) {
                myDragSelectionOrigObj = null;
                myDragSelection = null;
                return;
            }
            moveSelection(getSelection(), 0, myMouseDownPoint.x
                    - myMoveOffset.x - myDragSelectionOrigObj.getLeft(),
                    myMouseDownPoint.y - myMoveOffset.y
                            - myDragSelectionOrigObj.getTop(), 2);
            if (getCurrentObject().getView() != this) {
                setCurrentObject(myDragSelection.getObjectAtPos(myDragSelection
                        .getFirstObjectPos()));
            }
        }
    }

    void clearDragSelection() {
        if (myDragSelection != null) {
            for (GraphicViewerListPosition graphicviewerlistposition = myDragSelection
                    .getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = myDragSelection
                    .getNextObjectPosAtTop(graphicviewerlistposition)) {
                GraphicViewerObject graphicviewerobject = myDragSelection
                        .getObjectAtPos(graphicviewerlistposition);
                removeObject(graphicviewerobject);
            }

            myDragSelection = null;
            setCurrentObject(myDragSelectionOrigObj);
            myDragSelectionOrigObj = null;
        }
    }

    public void moveSelection(GraphicViewerSelection graphicviewerselection,
            int i, int j, int k, int l) {
        if (graphicviewerselection == null) {
            graphicviewerselection = getSelection();
        }
        if (graphicviewerselection.isEmpty()) {
            return;
        }
        boolean flag = false;
        int i1 = getSnapMove();
        if (i1 == 1 || i1 == 2 && l == 3) {
            flag = true;
        }
        ArrayList arraylist = computeEffectiveSelection(graphicviewerselection,
                true, true);
        Point point = new Point(0, 0);
        int j1 = getGridSpot();
        GraphicViewerObject graphicviewerobject = null;
        int k1 = 0;
        do {
            if (k1 >= arraylist.size()) {
                break;
            }
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
            if (graphicviewerobject3 instanceof GraphicViewerLink) {
                continue;
            }
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

    public ArrayList computeEffectiveSelection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        return computeEffectiveSelection(graphicviewerobjectsimplecollection,
                true, true);
    }

    public ArrayList computeEffectiveSelection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            boolean flag, boolean flag1) {
        HashMap hashmap = new HashMap();
        ArrayList arraylist = null;
        ArrayList arraylist1 = new ArrayList();
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                    .getDraggingObject();
            if (graphicviewerobject1 != null
                    && (!flag || graphicviewerobject1.isDraggable())
                    && !alreadyMoved(hashmap, graphicviewerobject1)) {
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
                    if (arraylist == null) {
                        arraylist = new ArrayList();
                    }
                    arraylist.add(graphicviewerobject1);
                }
                arraylist1.add(graphicviewerobject1);
            }
        } while (true);
        if (flag1) {
            for (int i = 0; i < arraylist1.size(); i++) {
                GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) arraylist1
                        .get(i);
                searchForLinks(graphicviewerobject2, hashmap, arraylist1);
            }

        }
        return arraylist1;
    }

    private void searchForLinks(GraphicViewerObject graphicviewerobject,
            HashMap hashmap, ArrayList arraylist) {
        if (graphicviewerobject instanceof GraphicViewerPort) {
            GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerport
                    .getFirstLinkPos();
            do {
                if (graphicviewerlistposition == null) {
                    break;
                }
                GraphicViewerLink graphicviewerlink = graphicviewerport
                        .getLinkAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerport
                        .getNextLinkPos(graphicviewerlistposition);
                GraphicViewerPort graphicviewerport1 = graphicviewerlink
                        .getOtherPort(graphicviewerport);
                if (alreadyMoved(hashmap,
                        ((GraphicViewerObject) (graphicviewerport1)))
                        && !alreadyMoved(hashmap,
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
                searchForLinks(graphicviewerobject1, hashmap, arraylist);
            }

        }
    }

    private boolean alreadyMoved(HashMap hashmap,
            GraphicViewerObject graphicviewerobject) {
        for (Object obj = graphicviewerobject; obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            if (hashmap.containsKey(obj)) {
                return true;
            }
        }

        return false;
    }

    public void doCancelMoveSelection(Point point) {
        clearDragSelection();
        if (getCurrentObject() != null) {
            moveSelection(getSelection(), 0, myMouseDownPoint.x - point.x
                    - getCurrentObject().getLeft(), myMouseDownPoint.y
                    - point.y - getCurrentObject().getTop(), 3);
        }
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(false);
        }
    }

    public void copySelection(GraphicViewerSelection graphicviewerselection,
            int i, int j, int k, int l) {
        if (graphicviewerselection == null) {
            graphicviewerselection = getSelection();
        }
        if (graphicviewerselection.isEmpty()) {
            return;
        }
        getSnapMove();
        GraphicViewerDocument graphicviewerdocument = getDocument();
        Point point = new Point(j, k);
        ArrayList arraylist = computeEffectiveSelection(graphicviewerselection,
                false, false);
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[arraylist
                .size()];
        for (int j1 = 0; j1 < arraylist.size(); j1++) {
            agraphicviewerobject[j1] = (GraphicViewerObject) arraylist.get(j1);
        }

        graphicviewerdocument.sortByZOrder(agraphicviewerobject);
        GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                agraphicviewerobject);
        IGraphicViewerCopyEnvironment graphicviewercopyenvironment = graphicviewerdocument
                .createDefaultCopyEnvironment();
        graphicviewerdocument.copyFromCollection(graphicviewercollection,
                point, graphicviewercopyenvironment);
        ArrayList arraylist1 = myMoveArrayList;
        arraylist1.clear();
        Iterator iterator = graphicviewercopyenvironment.entrySet().iterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            Entry entry = (Entry) iterator.next();
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
        for (int k1 = 0; k1 < arraylist1.size(); k1++) {
            graphicviewerselection
                    .extendSelection((GraphicViewerObject) arraylist1.get(k1));
        }

        fireUpdate(38, 0, null);
        arraylist1.clear();
    }

    protected boolean startMoveSelection(int i, Point point, Point point1) {
        if (!getSelection().isInSelection(getCurrentObject())) {
            setCurrentObject(selectObject(getCurrentObject()));
        }
        if (getCurrentObject() == null) {
            return false;
        }
        if (!getCurrentObject().isDraggable()) {
            return false;
        }
        if (getCurrentObject().getLayer() != null
                && !getCurrentObject().getLayer().isModifiable()) {
            return false;
        }
        if ((getInternalMouseActions() & 3) == 0) {
            return false;
        }
        if (myHideSelectionOnMouse) {
            getSelection().clearSelectionHandles(null);
        }
        myMoveOffset.x = myMouseDownPoint.x - getCurrentObject().getLeft();
        myMoveOffset.y = myMouseDownPoint.y - getCurrentObject().getTop();
        doMoveSelection(i, 1, 1, 1);
        setState(2);
        return true;
    }

    protected boolean startDragBoxSelection(int i, Point point, Point point1) {
        if (keyClearSelection(i)) {
            getSelection().clearSelection();
        }
        setState(6);
        Graphics2D graphics2d = getGraphicViewerGraphics();
        drawXORBox(graphics2d, point1.x, point1.y, point1.x, point1.y, 1);
        disposeGraphicViewerGraphics(graphics2d);
        return true;
    }

    protected boolean startResizing(GraphicViewerHandle graphicviewerhandle,
            Point point, Point point1) {
        if (graphicviewerhandle == null
                || graphicviewerhandle.getHandleType() == -1) {
            return false;
        }
        GraphicViewerObject graphicviewerobject = graphicviewerhandle
                .getHandleFor();
        if (graphicviewerobject.getLayer() == null
                || graphicviewerobject.getLayer().isModifiable()) {
            if (getDocument() != null) {
                getDocument().startTransaction();
            }
            setCurrentObject(graphicviewerobject);
            myHandleHit = graphicviewerhandle.getHandleType();
            if (myHideSelectionOnMouse) {
                getSelection().clearSelectionHandles(getCurrentObject());
            }
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
            docToViewCoords(point2);
        } else {
            point2 = point1;
        }
        if (getCurrentObject() == null || !getCurrentObject().isResizable()) {
            return;
        }
        Rectangle rectangle = null;
        if (i == 1) {
            Rectangle rectangle1 = getCurrentObject().getBoundingRect();
            myOrigResizeRect.x = rectangle1.x;
            myOrigResizeRect.y = rectangle1.y;
            myOrigResizeRect.width = rectangle1.width;
            myOrigResizeRect.height = rectangle1.height;
            myOrigResizePoint.x = point2.x;
            myOrigResizePoint.y = point2.y;
            rectangle = new Rectangle(myOrigResizeRect.x, myOrigResizeRect.y,
                    myOrigResizeRect.width, myOrigResizeRect.height);
        }
        Rectangle rectangle2 = getCurrentObject().handleResize(graphics2d,
                this, myOrigResizeRect, point2, myHandleHit, i, 0, 0);
        if (rectangle2 != null) {
            if (rectangle != null) {
                convertDocToView(rectangle);
                drawXORBox(graphics2d, rectangle.x, rectangle.y, rectangle.x
                        + rectangle.width, rectangle.y + rectangle.height, 1);
            } else {
                convertDocToView(rectangle2);
                drawXORBox(graphics2d, rectangle2.x, rectangle2.y, rectangle2.x
                        + rectangle2.width, rectangle2.y + rectangle2.height, i);
            }
        }
        if (i == 3 && getState() == 5) {
            fireUpdate(30, 0, getCurrentObject());
        }
        if ((i == 3 || getState() != 5) && getDocument() != null) {
            getDocument().endTransaction(getEditPresentationName(3));
        }
    }

    public void doCancelResize(Rectangle rectangle) {
        Graphics2D graphics2d = getGraphicViewerGraphics();
        if (getCurrentObject() != null) {
            getCurrentObject().handleResize(graphics2d, this, rectangle,
                    myOrigResizePoint, myHandleHit, 2, 0, 0);
        }
        drawXORBox(graphics2d, 0, 0, 0, 0, 3);
        disposeGraphicViewerGraphics(graphics2d);
        if (getCurrentObject() != null) {
            getCurrentObject().setBoundingRect(rectangle);
        }
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(false);
        }
    }

    public IGraphicViewerActionObject pickActionObject(Point point) {
        for (Object obj = pickDocObject(point, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            if (!(obj instanceof IGraphicViewerActionObject)) {
                continue;
            }
            IGraphicViewerActionObject graphicvieweractionobject = (IGraphicViewerActionObject) obj;
            if (graphicvieweractionobject.isActionEnabled()) {
                return graphicvieweractionobject;
            }
        }

        return null;
    }

    public boolean startActionObject(
            IGraphicViewerActionObject graphicvieweractionobject, int i,
            Point point, Point point1) {
        setCurrentObject((GraphicViewerObject) graphicvieweractionobject);
        graphicvieweractionobject.setActionActivated(true);
        setState(7);
        handleActionObject(i, point, point1, 1);
        return true;
    }

    public void handleActionObject(int i, Point point, Point point1, int j) {
        if (getCurrentObject() instanceof IGraphicViewerActionObject) {
            IGraphicViewerActionObject graphicvieweractionobject = (IGraphicViewerActionObject) getCurrentObject();
            graphicvieweractionobject.onActionAdjusted(this, i, point, point1,
                    j);
            if (j == 3) {
                graphicvieweractionobject.onAction(this, i, point, point1);
                graphicvieweractionobject.setActionActivated(false);
            }
        }
    }

    public String getToolTipText(MouseEvent mouseevent) {
        if (!isMouseEnabled()) {
            return null;
        }
        Point point = getMouseEventPoint(mouseevent);
        myMouseDocPoint.x = point.x;
        myMouseDocPoint.y = point.y;
        convertViewToDoc(myMouseDocPoint);
        for (Object obj = pickDocObject(myMouseDocPoint, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            String s1 = ((GraphicViewerObject) (obj)).getToolTipText();
            if (s1 != null) {
                return s1;
            }
        }

        return null;
    }

    public void setEditControl(GraphicViewerTextEdit graphicviewertextedit) {
        GraphicViewerTextEdit graphicviewertextedit1 = myTextEdit;
        if (graphicviewertextedit1 != graphicviewertextedit) {
            if (graphicviewertextedit1 != null) {
                removeObject(graphicviewertextedit1);
            }
            myTextEdit = graphicviewertextedit;
            if (graphicviewertextedit != null) {
                addObjectAtTail(graphicviewertextedit);
            }
        }
    }

    public GraphicViewerTextEdit getEditControl() {
        return myTextEdit;
    }

    public boolean isEditingTextControl() {
        return getEditControl() != null;
    }

    public void doEndEdit() {
        if (isEditingTextControl()) {
            getEditControl().doEndEdit();
        }
    }

    void internalFinishedEdit(GraphicViewerObject graphicviewerobject) {
        fireUpdate(33, 0, graphicviewerobject);
        if (getDocument() != null) {
            getDocument().endTransaction(getEditPresentationName(7));
        }
    }

    void addControl(GraphicViewerControl graphicviewercontrol,
            JComponent jcomponent) {
        if (!myControls.contains(graphicviewercontrol)) {
            myControls.add(graphicviewercontrol);
            getCanvas().add(jcomponent);
        }
    }

    void removeControl(GraphicViewerControl graphicviewercontrol,
            JComponent jcomponent) {
        myControls.remove(graphicviewercontrol);
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
        return myDrawsXorMode;
    }

    public void setDrawsXorMode(boolean flag) {
        myDrawsXorMode = flag;
    }

    protected void drawXORBox(Graphics2D graphics2d, int i, int j, int k,
            int l, int i1) {
        if (myPrevXORRectValid) {
            myPrevXORRectValid = false;
            if (myPrevXORRect.width != 0 || myPrevXORRect.height != 0) {
                drawXORRect(graphics2d, myPrevXORRect.x, myPrevXORRect.y,
                        myPrevXORRect.width, myPrevXORRect.height);
            }
        }
        if (i1 != 3) {
            myPrevXORRect.x = Math.min(i, k);
            myPrevXORRect.y = Math.min(j, l);
            myPrevXORRect.width = Math.abs(i - k);
            myPrevXORRect.height = Math.abs(j - l);
            if (isDrawsXorMode()) {
                if (myPrevXORRect.width != 0 || myPrevXORRect.height != 0) {
                    drawXORRect(graphics2d, myPrevXORRect.x, myPrevXORRect.y,
                            myPrevXORRect.width, myPrevXORRect.height);
                }
                myPrevXORRectValid = true;
            } else {
                if (myMarquee == null) {
                    myMarquee = new GraphicViewerRectangle();
                }
                if (myMarquee.getPen() != null) {
                    myMarquee.setBrush(null);
                    GraphicViewerPen graphicviewerpen = new GraphicViewerPen(2,
                            2, GraphicViewerBrush.ColorDarkGray);
                    myMarquee.setPen(graphicviewerpen);
                }
                myMarquee.setBoundingRect(viewToDocCoords(myPrevXORRect));
                addObjectAtTail(myMarquee);
            }
        } else if (myMarquee != null) {
            removeObject(myMarquee);
        }
    }

    public Graphics2D getGraphicViewerGraphics() {
        return (Graphics2D) getCanvas().getGraphics();
    }

    public void disposeGraphicViewerGraphics(Graphics2D graphics2d) {
        if (graphics2d != null) {
            graphics2d.dispose();
        }
    }

    protected GraphicViewerHandle pickHandle(Point point) {
        GraphicViewerObject graphicviewerobject = pickObject(point, true);
        if (graphicviewerobject instanceof GraphicViewerHandle) {
            return (GraphicViewerHandle) graphicviewerobject;
        } else {
            return null;
        }
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
        if (getDocument() != null) {
            getDocument().endTransaction(false);
        }
    }

    public void reLink(GraphicViewerLink graphicviewerlink,
            GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        graphicviewerlink.setFromPort(graphicviewerport);
        graphicviewerlink.setToPort(graphicviewerport1);
        GraphicViewerLayer graphicviewerlayer = graphicviewerlink.getLayer();
        if (graphicviewerlayer == null && getDocument() != null) {
            graphicviewerlayer = getDocument().getLinksLayer();
        }
        if (graphicviewerlayer != null) {
            GraphicViewerSubGraphBase.reparentToCommonSubGraph(
                    graphicviewerlink, graphicviewerport, graphicviewerport1,
                    true, graphicviewerlayer);
        }
        fireUpdate(32, 0, graphicviewerlink);
        if (getDocument() != null) {
            getDocument().endTransaction(getEditPresentationName(5));
        }
    }

    protected void noReLink(GraphicViewerLink graphicviewerlink,
            GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        if (graphicviewerlink.getLayer() != null) {
            GraphicViewerViewEvent graphicviewerviewevent = new GraphicViewerViewEvent(
                    this, 28, 0, graphicviewerlink, null, null, 0);
            invokeViewListeners(graphicviewerviewevent);
            if (graphicviewerviewevent.isConsumed()) {
                doCancelMouse();
                return;
            }
            graphicviewerlink.getLayer().removeObject(graphicviewerlink);
            fireUpdate(29, 0, graphicviewerlink);
        }
        if (getDocument() != null) {
            getDocument().endTransaction(getEditPresentationName(13));
        }
    }

    private void cacheValidLinks(boolean flag) {
        label0 : for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible()) {
                continue;
            }
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            do {
                GraphicViewerObject graphicviewerobject;
                do {
                    if (graphicviewerlistposition == null) {
                        continue label0;
                    }
                    graphicviewerobject = graphicviewerlayer
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerlayer
                            .getNextObjectPos(graphicviewerlistposition);
                } while (!(graphicviewerobject instanceof GraphicViewerPort));
                GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
                if (flag) {
                    graphicviewerport.setValidLink(validLink(myTempStartPort,
                            graphicviewerport));
                } else {
                    graphicviewerport.setValidLink(validLink(graphicviewerport,
                            myTempStartPort));
                }
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
            if (!graphicviewerlayer.isVisible()) {
                continue;
            }
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
                            if (graphicviewerlistposition == null) {
                                continue label0;
                            }
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
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject instanceof GraphicViewerPort) {
            return (GraphicViewerPort) graphicviewerobject;
        } else {
            return null;
        }
    }

    public boolean startNewLink(GraphicViewerPort graphicviewerport, Point point) {
        boolean flag = validSourcePort(graphicviewerport);
        boolean flag1 = validDestinationPort(graphicviewerport);
        if (flag || flag1) {
            if (getDocument() != null) {
                getDocument().startTransaction();
            }
            setCursorType(12);
            myTempStartPort = graphicviewerport;
            myTempEndPort = createTemporaryPortForNewLink(graphicviewerport,
                    point);
            if (flag) {
                setState(3);
                myTempLink = createTemporaryLinkForNewLink(myTempStartPort,
                        myTempEndPort);
                cacheValidLinks(true);
            } else {
                setState(4);
                myTempLink = createTemporaryLinkForNewLink(myTempEndPort,
                        myTempStartPort);
                cacheValidLinks(false);
            }
            addObjectAtTail(myTempLink);
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
                        .getDocument() != graphicviewerport.getDocument())) {
            return false;
        }
        if (graphicviewerlink.getToPort() == null) {
            myTempStartPort = graphicviewerlink.getFromPort();
        } else if (graphicviewerlink.getFromPort() == null) {
            myTempStartPort = graphicviewerlink.getToPort();
        } else {
            return false;
        }
        if (getDocument() != null) {
            getDocument().startTransaction();
        }
        setCursorType(12);
        myTempLink = graphicviewerlink;
        myOrigEndPort = graphicviewerport;
        myTempEndPort = createTemporaryPortForNewLink(myTempStartPort, point);
        if (graphicviewerlink.getToPort() == null) {
            setState(3);
            graphicviewerlink.setToPort(myTempEndPort);
            cacheValidLinks(true);
        } else if (graphicviewerlink.getFromPort() == null) {
            setState(4);
            graphicviewerlink.setFromPort(myTempEndPort);
            cacheValidLinks(false);
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
        if (graphicviewerdocument != null) {
            graphicviewerdocument.startTransaction();
        }
        Toolkit toolkit = getToolkit();
        if (toolkit == null) {
            toolkit = GraphicViewerGlobal.getToolkit();
        }
        copyToClipboard(toolkit.getSystemClipboard());
        fireUpdate(36, 0, null);
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(getEditPresentationName(8));
        }
    }

    public void copyToClipboard(Clipboard clipboard) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            try {
                Class class1 = graphicviewerdocument.getClass();
                GraphicViewerDocument graphicviewerdocument1 = (GraphicViewerDocument) class1
                        .newInstance();
                graphicviewerdocument1.copyLayersFrom(graphicviewerdocument);
                GraphicViewerObject agraphicviewerobject[] = getSelection()
                        .toArray();
                graphicviewerdocument1.sortByZOrder(agraphicviewerobject);
                GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                        agraphicviewerobject);
                graphicviewerdocument1
                        .copyFromCollection(graphicviewercollection);
                clipboard.setContents(graphicviewerdocument1, this);
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
    }

    public void cut() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.startTransaction();
        }
        Toolkit toolkit = getToolkit();
        if (toolkit == null) {
            toolkit = GraphicViewerGlobal.getToolkit();
        }
        copyToClipboard(toolkit.getSystemClipboard());
        deleteSelection();
        fireUpdate(36, 0, null);
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(getEditPresentationName(9));
        }
    }

    public void deleteSelection() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.startTransaction();
        }
        GraphicViewerSelection graphicviewerselection = getSelection();
        GraphicViewerViewEvent graphicviewerviewevent = new GraphicViewerViewEvent(
                this, 28, 0, null, null, null, 0);
        invokeViewListeners(graphicviewerviewevent);
        if (graphicviewerviewevent.isConsumed()) {
            if (graphicviewerdocument != null) {
                graphicviewerdocument.endTransaction(false);
            }
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
                if (graphicviewerobject.getParent() != null) {
                    graphicviewerobject.getParent().removeObject(
                            graphicviewerobject);
                } else if (graphicviewerobject.getLayer() != null) {
                    graphicviewerobject.getLayer().removeObject(
                            graphicviewerobject);
                } else if (graphicviewerobject.getView() != null) {
                    graphicviewerobject.getView().removeObject(
                            graphicviewerobject);
                }
                graphicviewerlistposition = graphicviewerselection
                        .getFirstObjectPos();
            }
        }

        fireUpdate(38, 0, null);
        fireUpdate(29, 0, null);
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(getEditPresentationName(11));
        }
    }

    public void paste() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.startTransaction();
        }
        Toolkit toolkit = getToolkit();
        if (toolkit == null) {
            toolkit = GraphicViewerGlobal.getToolkit();
        }
        IGraphicViewerCopyEnvironment graphicviewercopyenvironment = pasteFromClipboard(toolkit
                .getSystemClipboard());
        if (graphicviewercopyenvironment != null) {
            Iterator iterator = graphicviewercopyenvironment.values()
                    .iterator();
            boolean flag = false;
            fireUpdate(37, 0, null);
            GraphicViewerSelection graphicviewerselection = getSelection();
            do {
                if (!iterator.hasNext()) {
                    break;
                }
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
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(getEditPresentationName(10));
        }
    }

    public boolean canPaste() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null) {
            return false;
        }
        if (!graphicviewerdocument.getDefaultLayer().isModifiable()) {
            return false;
        }
        Toolkit toolkit = getToolkit();
        if (toolkit == null) {
            toolkit = GraphicViewerGlobal.getToolkit();
        }
        Clipboard clipboard = toolkit.getSystemClipboard();
        Transferable transferable = clipboard.getContents(this);
        java.awt.datatransfer.DataFlavor dataflavor = GraphicViewerDocument
                .getStandardDataFlavor();
        return transferable != null
                && transferable.isDataFlavorSupported(dataflavor);
    }

    public IGraphicViewerCopyEnvironment pasteFromClipboard(Clipboard clipboard) {
        Transferable transferable;
        java.awt.datatransfer.DataFlavor dataflavor;
        transferable = clipboard.getContents(this);
        dataflavor = GraphicViewerDocument.getStandardDataFlavor();
        if (transferable != null
                && transferable.isDataFlavorSupported(dataflavor)) {
            GraphicViewerDocument graphicviewerdocument;
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection = null;
            graphicviewerdocument = getDocument();
            if (graphicviewerdocument != null) {
                try {
                    graphicviewerobjectsimplecollection = (IGraphicViewerObjectSimpleCollection) transferable
                            .getTransferData(dataflavor);
                } catch (UnsupportedFlavorException ufe) {
                    ExceptionDialog.hideException(ufe);
                    return null;
                } catch (IOException ioe) {
                    ExceptionDialog.hideException(ioe);
                    return null;
                }
                return graphicviewerdocument
                        .copyFromCollection(graphicviewerobjectsimplecollection);
            }
        }
        return null;
    }

    public void initializeDragDropHandling() {
        myInternalDragDrop = false;
        myInternalDragStarted = false;
        setDragDropEnabled(true);
    }

    public void setDragDropEnabled(boolean flag) {
        setDragEnabled(flag);
        setDropEnabled(flag);
    }

    public void setDragEnabled(boolean flag) {
        if (flag && !myDragEnabled) {
            myDragEnabled = true;
            try {
                if (myDragGestureRecognizer == null) {
                    myDragSource = DragSource.getDefaultDragSource();
                    myDragGestureRecognizer = myDragSource
                            .createDefaultDragGestureRecognizer(getCanvas(), 3,
                                    getCanvas());
                } else {
                    myDragGestureRecognizer.addDragGestureListener(getCanvas());
                }
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
            fireUpdate(117, 0, null);
        } else if (!flag && myDragEnabled) {
            myDragEnabled = false;
            if (myDragGestureRecognizer != null) {
                myDragGestureRecognizer.removeDragGestureListener(getCanvas());
            }
            fireUpdate(117, 1, null);
        }
    }

    public void setDropEnabled(boolean flag) {
        if (flag && !myDropEnabled) {
            myDropEnabled = true;
            try {
                if (myDropTarget == null) {
                    myDropTarget = new DropTarget(getCanvas(), 3, getCanvas());
                } else {
                    myDropTarget.addDropTargetListener(getCanvas());
                }
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
            fireUpdate(118, 0, null);
        } else if (!flag && myDropEnabled) {
            myDropEnabled = false;
            if (myDropTarget != null) {
                myDropTarget.removeDropTargetListener(getCanvas());
            }
            fireUpdate(118, 1, null);
        }
    }

    public boolean isDragDropEnabled() {
        return isDragEnabled() && isDropEnabled();
    }

    public boolean isDragEnabled() {
        return myDragEnabled;
    }

    public boolean isDropEnabled() {
        return myDropEnabled;
    }

    public boolean isInternalDragDrop() {
        return myInternalDragDrop;
    }

    public void onDragGestureRecognized(DragGestureEvent draggestureevent) {
        if (!isDragEnabled()) {
            return;
        }
        TRACE("dragGestureRecognized", draggestureevent);
        myInternalDragStarted = false;
        myInternalDragDrop = true;
        if (!isDropEnabled()) {
            Point point = draggestureevent.getDragOrigin();
            myDropDocPoint.x = point.x;
            myDropDocPoint.y = point.y;
            convertViewToDoc(myDropDocPoint);
            doMouseMove(
                    convertActionToModifiers(draggestureevent.getDragAction()),
                    myDropDocPoint, point);
        }
        dragGestureRecognized(draggestureevent);
    }

    public void dragGestureRecognized(DragGestureEvent draggestureevent) {
        try {
            Cursor cursor = DragSource.DefaultMoveDrop;
            if (getState() == 5 || getState() == 3 || getState() == 4) {
                cursor = getCursor();
            }
            GraphicViewerDocument graphicviewerdocument = getDocument();
            Class class1 = graphicviewerdocument.getClass();
            GraphicViewerDocument graphicviewerdocument1 = (GraphicViewerDocument) class1
                    .newInstance();
            graphicviewerdocument1.copyLayersFrom(graphicviewerdocument);
            GraphicViewerObject agraphicviewerobject[] = getSelection()
                    .toArray();
            graphicviewerdocument1.sortByZOrder(agraphicviewerobject);
            GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection(
                    agraphicviewerobject);
            graphicviewerdocument1.copyFromCollection(graphicviewercollection);
            if (DragSource.isDragImageSupported()) {
                if (myDragImage == null) {
                    myDragImage = new BufferedImage(1, 1, 1);
                }
                draggestureevent.startDrag(cursor, myDragImage,
                        new Point(0, 0), graphicviewerdocument1, getCanvas());
            } else {
                draggestureevent.startDrag(cursor, graphicviewerdocument1,
                        getCanvas());
            }
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
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
            TRACE("dropActionChanged Source", dragsourcedragevent);
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
        if (!isDragEnabled()) {
            return;
        }
        TRACE("dragDropEnd Source", dragsourcedropevent);
        if (isInternalDragDrop()) {
            myInternalDragDrop = false;
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
        if (!isDropEnabled()) {
            return;
        }
        TRACE("dragEnter Target", droptargetdragevent);
        if (isInternalDragDrop()) {
            Point point = droptargetdragevent.getLocation();
            myDropDocPoint.x = point.x;
            myDropDocPoint.y = point.y;
            convertViewToDoc(myDropDocPoint);
            doMouseMove(
                    convertActionToModifiers(droptargetdragevent
                            .getDropAction()),
                    myDropDocPoint, point);
        } else {
            dragEnter(droptargetdragevent);
        }
    }

    public void onDragOver(DropTargetDragEvent droptargetdragevent) {
        if (!isDropEnabled()) {
            return;
        }
        TRACE("dragOver Target", droptargetdragevent);
        if (isInternalDragDrop()) {
            Point point = droptargetdragevent.getLocation();
            myDropDocPoint.x = point.x;
            myDropDocPoint.y = point.y;
            convertViewToDoc(myDropDocPoint);
            doMouseMove(
                    convertActionToModifiers(droptargetdragevent
                            .getDropAction()),
                    myDropDocPoint, point);
        } else {
            dragOver(droptargetdragevent);
        }
    }

    public void onDropActionChanged(DropTargetDragEvent droptargetdragevent) {
        if (!isDropEnabled()) {
            return;
        }
        TRACE("dropActionChanged Target", droptargetdragevent);
        if (!isInternalDragDrop()) {
            dropActionChanged(droptargetdragevent);
        }
    }

    public void onDragExit(DropTargetEvent droptargetevent) {
        if (!isDropEnabled()) {
            return;
        }
        TRACE("dragExit Target", droptargetevent);
        if (!isInternalDragDrop()) {
            dragExit(droptargetevent);
        }
    }

    public void onDrop(DropTargetDropEvent droptargetdropevent) {
        if (!isDropEnabled()) {
            droptargetdropevent.rejectDrop();
            return;
        }
        TRACE("drop Target", droptargetdropevent);
        if (isInternalDragDrop()) {
            myInternalDragDrop = false;
            Point point = droptargetdropevent.getLocation();
            myDropDocPoint.x = point.x;
            myDropDocPoint.y = point.y;
            convertViewToDoc(myDropDocPoint);
            if (doMouseUp(
                    convertActionToModifiers(droptargetdropevent
                            .getDropAction()),
                    myDropDocPoint, point)) {
                droptargetdropevent.acceptDrop(droptargetdropevent
                        .getDropAction());
                setState(0);
                completeDrop(droptargetdropevent, true);
            } else {
                droptargetdropevent.rejectDrop();
            }
        } else {
            if (GraphicViewerGlobal.isAtLeastJavaVersion(1.3999999999999999D)) {
                dragExit(droptargetdropevent);
            }
            drop(droptargetdropevent);
        }
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent) {
        if (!isDropFlavorAcceptable(droptargetdragevent)) {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if (i == 0) {
            droptargetdragevent.rejectDrag();
        } else {
            droptargetdragevent.acceptDrag(i);
        }
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent) {
        if (!isDropFlavorAcceptable(droptargetdragevent)) {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if (i == 0) {
            droptargetdragevent.rejectDrag();
        } else {
            droptargetdragevent.acceptDrag(i);
        }
    }

    public void dropActionChanged(DropTargetDragEvent droptargetdragevent) {
        if (!isDropFlavorAcceptable(droptargetdragevent)) {
            droptargetdragevent.rejectDrag();
            return;
        }
        int i = computeAcceptableDrop(droptargetdragevent);
        if (i == 0) {
            droptargetdragevent.rejectDrag();
        } else {
            droptargetdragevent.acceptDrag(i);
        }
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
                || !getDocument().getDefaultLayer().isModifiable()) {
            return 0;
        }
        if ((droptargetdragevent.getDropAction() & 3) != 0) {
            return droptargetdragevent.getDropAction();
        } else {
            return 0;
        }
    }

    public boolean doDrop(DropTargetDropEvent droptargetdropevent,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection;
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
            } catch (UnsupportedFlavorException ufe) {
                ExceptionDialog.hideException(ufe);
                return false;
            } catch (IOException ioe) {
                ExceptionDialog.hideException(ioe);
                return false;
            }
            graphicviewerobjectsimplecollection = (IGraphicViewerObjectSimpleCollection) obj;
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
                    IGraphicViewerCopyEnvironment graphicviewercopyenvironment1 = graphicviewerdocument
                            .copyFromCollection(
                                    graphicviewerobjectsimplecollection,
                                    point3, graphicviewercopyenvironment);
                    GraphicViewerSelection graphicviewerselection = getSelection();
                    graphicviewerselection.clearSelection();
                    fireUpdate(37, 0, null);
                    Iterator iterator = graphicviewercopyenvironment1.values()
                            .iterator();
                    do {
                        if (!iterator.hasNext()) {
                            break;
                        }
                        Object obj1 = iterator.next();
                        if (obj1 instanceof GraphicViewerObject) {
                            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) obj1;
                            if (graphicviewerobject1.isTopLevel()) {
                                graphicviewerselection
                                        .extendSelection(graphicviewerobject1);
                            }
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
        return myAutoscrollInsets;
    }

    public void setAutoscrollInsets(Insets insets) {
        Insets insets1 = myAutoscrollInsets;
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        if (!insets1.equals(insets)) {
            myAutoscrollInsets.top = insets.top;
            myAutoscrollInsets.left = insets.left;
            myAutoscrollInsets.bottom = insets.bottom;
            myAutoscrollInsets.right = insets.right;
            fireUpdate(123, 0, insets1);
        }
    }

    public void autoscroll(Point point) {
        JScrollBar jscrollbar = getHorizontalScrollBar();
        Dimension dimension = getCanvas().getSize();
        Insets insets = getAutoscrollInsets();
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
        if (graphicviewerdocument == null) {
            return null;
        } else {
            return graphicviewerdocument.getFirstLayer();
        }
    }

    public GraphicViewerLayer getLastLayer() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null) {
            return null;
        } else {
            return graphicviewerdocument.getLastLayer();
        }
    }

    public GraphicViewerLayer getNextLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null) {
            return null;
        } else {
            return graphicviewerlayer.getNextLayer();
        }
    }

    public GraphicViewerLayer getPrevLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null) {
            return null;
        } else {
            return graphicviewerlayer.getPrevLayer();
        }
    }

    public String getEditPresentationName(int i) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null
                && graphicviewerdocument.getUndoManager() != null) {
            return graphicviewerdocument.getUndoManager()
                    .getViewEditPresentationName(i);
        } else {
            return null;
        }
    }

    public int getDebugFlags() {
        return myDebugFlags;
    }

    public void setDebugFlags(int i) {
        myDebugFlags = i;
    }

    void TRACE(String paramString, EventObject paramEventObject) {
        if ((myDebugFlags & 0x1) != 0) {
            String str = Integer.toString(getState());
            str = new StringBuilder().append(str)
                    .append(hasFocus() ? " has focus" : " NO FOCUS").toString();
            str = new StringBuilder().append(str)
                    .append(isDragEnabled() ? "" : " DRAG DISABLED").toString();
            str = new StringBuilder().append(str)
                    .append(isDropEnabled() ? "" : " DROP DISABLED").toString();
            str = new StringBuilder().append(str)
                    .append(isInternalDragDrop() ? " internal drag" : "")
                    .toString();
            str = new StringBuilder().append(str)
                    .append(myInternalDragStarted ? " started" : "").toString();
            paramString = new StringBuilder()
                    .append(Integer.toString(myDebugFlags)).append(" ")
                    .append(paramString).append(" ").append(str).toString();
            if (!paramString.equals(myLastEventMsg)) {
                myLastEventMsg = paramString;
                myLastHandlerMsg = null;
                GraphicViewerGlobal.TRACE(paramString);
            }
        }
    }

    void TRACE(String paramString) {
        if ((myDebugFlags & 0x1) != 0) {
            String str = Integer.toString(getState());
            str = new StringBuilder().append(str)
                    .append(hasFocus() ? " has focus" : " NO FOCUS").toString();
            str = new StringBuilder().append(str)
                    .append(isDragEnabled() ? "" : " DRAG DISABLED").toString();
            str = new StringBuilder().append(str)
                    .append(isDropEnabled() ? "" : " DROP DISABLED").toString();
            str = new StringBuilder().append(str)
                    .append(isInternalDragDrop() ? " internal drag" : "")
                    .toString();
            str = new StringBuilder().append(str)
                    .append(myInternalDragStarted ? " started" : "").toString();
            paramString = new StringBuilder()
                    .append(Integer.toString(myDebugFlags)).append(" ")
                    .append(paramString).append(" ").append(str).toString();
            if (!paramString.equals(myLastHandlerMsg)) {
                myLastHandlerMsg = paramString;
                GraphicViewerGlobal.TRACE(paramString);
            }
        }
    }

    int[] getTempXs(int i) {
        if (i >= myTempArraysX.length) {
            myTempArraysX = new int[i + 1][];
        }
        int ai1[] = myTempArraysX[i];
        if (ai1 == null) {
            ai1 = new int[i];
            myTempArraysX[i] = ai1;
        }
        return ai1;
    }

    int[] getTempYs(int i) {
        if (i >= myTempArraysY.length) {
            myTempArraysY = new int[i + 1][];
        }
        int ai1[] = myTempArraysY[i];
        if (ai1 == null) {
            ai1 = new int[i];
            myTempArraysY[i] = ai1;
        }
        return ai1;
    }

    Point getTempPoint() {
        return myTempPoint;
    }

    Dimension getTempDimension() {
        return myTempDimension;
    }

    Rectangle getTempRectangle() {
        return myTempRectangle;
    }

    public static Color getDefaultPrimarySelectionColor() {
        return myDefaultPrimarySelectionColor;
    }

    public static void setDefaultPrimarySelectionColor(Color color) {
        myDefaultPrimarySelectionColor = color;
    }

    public static Color getDefaultSecondarySelectionColor() {
        return myDefaultSecondarySelectionColor;
    }

    public static void setDefaultSecondarySelectionColor(Color color) {
        myDefaultSecondarySelectionColor = color;
    }

    public static int getDefaultPortGravity() {
        return myDefaultPortGravity;
    }

    public static void setDefaultPortGravity(int i) {
        myDefaultPortGravity = i;
    }

    public void setGridWidth(int i) {
        if (i != myGridWidth) {
            myGridWidth = i;
            if (getHorizontalScrollBar() != null) {
                getHorizontalScrollBar().setUnitIncrement(getGridWidth());
            }
            onGridChange(0);
        }
    }

    public int getGridWidth() {
        return myGridWidth;
    }

    public void setGridHeight(int i) {
        if (i != myGridHeight) {
            myGridHeight = i;
            if (getVerticalScrollBar() != null) {
                getVerticalScrollBar().setUnitIncrement(getGridHeight());
            }
            onGridChange(0);
        }
    }

    public int getGridHeight() {
        return myGridHeight;
    }

    public void setGridStyle(int i) {
        if (i != myGridStyle) {
            myGridStyle = i;
            onGridChange(1);
        }
    }

    public int getGridStyle() {
        return myGridStyle;
    }

    public void setGridOrigin(Point point) {
        if (!point.equals(myGridOrigin)) {
            myGridOrigin.x = point.x;
            myGridOrigin.y = point.y;
            onGridChange(6);
        }
    }

    public Point getGridOrigin() {
        return myGridOrigin;
    }

    public void setGridSpot(int i) {
        if (i != mySpotNumber) {
            mySpotNumber = i;
            onGridChange(2);
        }
    }

    public int getGridSpot() {
        return mySpotNumber;
    }

    public void setGridPen(GraphicViewerPen graphicviewerpen) {
        if (myGridPen != graphicviewerpen) {
            myGridPen = graphicviewerpen;
            onGridChange(5);
        }
    }

    public GraphicViewerPen getGridPen() {
        return myGridPen;
    }

    public int getSnapMove() {
        return mySnapMove;
    }

    public void setSnapMove(int i) {
        if (mySnapMove != i) {
            mySnapMove = i;
            onGridChange(3);
        }
    }

    public int getSnapResize() {
        return mySnapResize;
    }

    public void setSnapResize(int i) {
        if (mySnapResize != i) {
            mySnapResize = i;
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
        for (int k1 = point.x; k1 < point1.x; k1 += i) {
            GraphicViewerDrawable.drawLine(graphics2d, graphicviewerpen, k1,
                    rectangle.y, k1, rectangle.y + rectangle.height);
        }

        for (int l1 = point.y; l1 < point1.y; l1 += j) {
            GraphicViewerDrawable.drawLine(graphics2d, graphicviewerpen,
                    rectangle.x, l1, rectangle.x + rectangle.width, l1);
        }

    }

    protected void drawGridCrosses(Graphics2D paramGraphics2D, int paramInt1,
            int paramInt2, Rectangle paramRectangle) {
        int i = getGridWidth();
        int j = getGridHeight();
        GraphicViewerPen localGraphicViewerPen = getGridPen();
        int k = paramRectangle.x - i;
        int m = paramRectangle.y - j;
        int n = paramRectangle.x + paramRectangle.width + i;
        int i1 = paramRectangle.y + paramRectangle.height + j;
        Point localPoint1 = findNearestGridPoint(k, m, null);
        Point localPoint2 = findNearestGridPoint(n, i1, null);
        if ((paramInt1 < 2) && (paramInt2 < 2)) {
            double d = getScale();
            int i4 = 1;
            if (d < 1.0D) {
                i4 = (int) Math.round(1.0D / d) + 1;
            }
            int i5 = localPoint1.x;
            while (i5 < localPoint2.x) {
                int i6 = localPoint1.y;
                while (i6 < localPoint2.y) {
                    GraphicViewerDrawable.drawLine(paramGraphics2D,
                            localGraphicViewerPen, i5, i6, i5 + i4, i6);
                    i6 += j;
                }
                i5 += i;
            }
        } else {
            int i2 = localPoint1.x;
            while (i2 < localPoint2.x) {
                int i3 = localPoint1.y;
                while (i3 < localPoint2.y) {
                    GraphicViewerDrawable.drawLine(paramGraphics2D,
                            localGraphicViewerPen, i2, i3 - paramInt1 / 2, i2,
                            i3 + paramInt1 / 2);
                    GraphicViewerDrawable.drawLine(paramGraphics2D,
                            localGraphicViewerPen, i2 - paramInt2 / 2, i3, i2
                                    + paramInt2 / 2, i3);
                    i3 += j;
                }
                i2 += i;
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
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = graphicviewerdocument
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerdocument
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (!(graphicviewerobject instanceof GraphicViewerLink)) {
                snapObject(graphicviewerobject, i);
            }
        } while (true);
    }

    public Point findNearestGridPoint(int i, int j, Point point) {
        Point point1 = getGridOrigin();
        int k = point1.x;
        int l = point1.y;
        int i1 = getGridWidth();
        int j1 = getGridHeight();
        int k1 = i - k;
        if (k1 < 0) {
            k1 -= i1;
        }
        k1 = (k1 / i1) * i1 + k;
        int l1 = j - l;
        if (l1 < 0) {
            l1 -= j1;
        }
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

        int myKind;
        Runnable myRunLater;
        GraphicViewerView myView;
        Cursor myCursor;
        DropTargetDropEvent mydtde;
        boolean mySucc;
        PrinterJob myPrinterJob;
        GraphicViewerObject myObject;

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview) {
            myKind = 0;
            myView = graphicviewerview;
        }

        GraphicViewerViewHelper(Runnable runnable) {
            myKind = 1;
            myRunLater = runnable;
        }

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview,
                Cursor cursor) {
            myKind = 2;
            myView = graphicviewerview;
            myCursor = cursor;
        }

        GraphicViewerViewHelper(DropTargetDropEvent droptargetdropevent,
                boolean flag) {
            myKind = 3;
            mydtde = droptargetdropevent;
            mySucc = flag;
        }

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview,
                PrinterJob printerjob) {
            myKind = 4;
            myView = graphicviewerview;
            myPrinterJob = printerjob;
        }

        GraphicViewerViewHelper(GraphicViewerView graphicviewerview,
                GraphicViewerObject graphicviewerobject) {
            myKind = 5;
            myView = graphicviewerview;
            myObject = graphicviewerobject;
        }

        public void mouseClicked(MouseEvent mouseevent) {
        }

        public void mouseReleased(MouseEvent mouseevent) {
        }

        public void mouseEntered(MouseEvent mouseevent) {
        }

        public void mouseExited(MouseEvent mouseevent) {
        }

        public void mousePressed(MouseEvent mouseevent) {
            if (!myView.isMouseEnabled()) {
                return;
            }
            if (!myView.hasFocus()) {
                myView.requestFocus();
            }
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
            myView.onScrollEvent(adjustmentevent);
        }

        public void run() {
            switch (myKind) {
                case 0 : // '\0'
                    return;

                case 1 : // '\001'
                    SwingUtilities.invokeLater(myRunLater);
                    return;

                case 2 : // '\002'
                    myView.setCursorImmediately(myCursor);
                    return;

                case 3 : // '\003'
                    mydtde.dropComplete(mySucc);
                    return;

                case 4 : // '\004'
                    myView.internalPrint(myPrinterJob);
                    return;
            }
        }

        public void focusGained(FocusEvent focusevent) {
            myView.internalFinishedEdit(myObject);
            myView.removeFocusListener(this);
        }

        public void focusLost(FocusEvent focusevent) {
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

        public GraphicViewerView myView;
        private int myNumControls = 0;

        public GraphicViewerViewCanvas(GraphicViewerView arg2) {
            setOpaque(true);
            setBackground(null);
            Object localObject;
            myView = arg2;
        }

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
            return myNumControls == 0;
        }

        protected void addImpl(Component component, Object obj, int i) {
            super.addImpl(component, obj, i);
            myNumControls++;
        }

        public void remove(int i) {
            myNumControls--;
            super.remove(i);
        }

        public void remove(Component component) {
            myNumControls--;
            super.remove(component);
        }

        public void removeAll() {
            myNumControls = 0;
            super.removeAll();
        }
    }
}