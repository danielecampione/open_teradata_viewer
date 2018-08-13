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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class GraphicViewerObject
        implements
            Serializable,
            IGraphicViewerXMLSaveRestore {

    private static final long serialVersionUID = -1845625064807221883L;

    public static final int NoSpot = -1;
    public static final int NoHandle = -1;
    public static final int Center = 0;
    public static final int TopLeft = 1;
    public static final int TopCenter = 2;
    public static final int TopMiddle = 2;
    public static final int Top = 2;
    public static final int TopRight = 3;
    public static final int RightCenter = 4;
    public static final int CenterRight = 4;
    public static final int SideRight = 4;
    public static final int Right = 4;
    public static final int BottomRight = 5;
    public static final int BottomCenter = 6;
    public static final int BottomMiddle = 6;
    public static final int Bottom = 6;
    public static final int BottomLeft = 7;
    public static final int LeftCenter = 8;
    public static final int CenterLeft = 8;
    public static final int SideLeft = 8;
    public static final int Left = 8;
    public static final int NumReservedHandles = 100;
    public static final int RepaintAll = 0;
    public static final int ChangedGeometry = 1;
    public static final int ChangedVisible = 2;
    public static final int ChangedSelectable = 3;
    public static final int ChangedDraggable = 4;
    public static final int ChangedResizable = 5;
    public static final int Changed4ResizeHandles = 6;

    /** @deprecated */
    public static final int ChangedGrabChildSelection = 9;
    public static final int ChangedZOrder = 10;
    public static final int ChangedPen = 11;
    public static final int ChangedBrush = 12;
    public static final int ChangedDragsNode = 13;
    public static final int ChangedUpdatePartner = 14;
    public static final int ChangedAutoRescale = 15;
    public static final int ChangedInitializing = 40;
    public static final int LastChangedHint = 65535;
    static final int flagVisible = 2;
    static final int flagSelectable = 4;
    static final int flagDraggable = 8;
    static final int flagResizable = 16;
    static final int flag4ResizeHandles = 32;
    static final int flagSuspendUpdates = 64;
    static final int flagSuspendChildUpdates = 128;
    static final int flagBoundingRectInvalid = 256;
    static final int flagGrabChildSelection = 512;
    static final int flagDragsNode = 1024;
    static final int flagSkipsUndoManager = 2048;
    static final int flagObject4 = 4096;
    static final int flagObject3 = 8192;
    static final int flagObject2 = 16384;
    static final int flagObject1 = 32768;
    static final int flagObject5 = 65536;
    static final int flagObject6 = 131072;
    static final int flagObject7 = 262144;
    static final int flagObject8 = 524288;
    static final int flagInitializing = 1048576;
    static final int flagSkipsBoundingRectChanged = 2097152;
    static final int flagAutoRescale = 4194304;
    static final int flagUpdatePartner = 8388608;
    private GraphicViewerArea myParentArea = null;
    private GraphicViewerLayer myLayer = null;
    private transient GraphicViewerView myView = null;
    private Rectangle myBoundingRect = new Rectangle(0, 0, 0, 0);
    private int myInternalFlags = 0;
    private int myExternalFlags = 0;

    public GraphicViewerObject() {
        init();
    }

    public GraphicViewerObject(Rectangle rectangle) {
        init();
        myBoundingRect.x = rectangle.x;
        myBoundingRect.y = rectangle.y;
        myBoundingRect.width = rectangle.width;
        myBoundingRect.height = rectangle.height;
    }

    public GraphicViewerObject(Point point, Dimension dimension) {
        init();
        myBoundingRect.x = point.x;
        myBoundingRect.y = point.y;
        myBoundingRect.width = dimension.width;
        myBoundingRect.height = dimension.height;
    }

    private final void init() {
        setInternalFlags(4195358);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerObject graphicviewerobject = (GraphicViewerObject) graphicviewercopyenvironment
                .get(this);
        if (graphicviewerobject != null) {
            return null;
        }
        try {
            Class class1 = getClass();
            graphicviewerobject = (GraphicViewerObject) class1.newInstance();
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
        if (graphicviewerobject != null) {
            graphicviewercopyenvironment.put(this, graphicviewerobject);
            graphicviewerobject.myParentArea = null;
            graphicviewerobject.myLayer = null;
            graphicviewerobject.myView = null;
            graphicviewerobject.myBoundingRect.x = myBoundingRect.x;
            graphicviewerobject.myBoundingRect.y = myBoundingRect.y;
            graphicviewerobject.myBoundingRect.width = myBoundingRect.width;
            graphicviewerobject.myBoundingRect.height = myBoundingRect.height;
            graphicviewerobject.myInternalFlags = myInternalFlags;
            graphicviewerobject.myExternalFlags = myExternalFlags;
        }
        return graphicviewerobject;
    }

    public void copyObjectDelayed(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment,
            GraphicViewerObject graphicviewerobject) {
    }

    public GraphicViewerObject copy() {
        GraphicViewerCopyMap graphicviewercopymap = new GraphicViewerCopyMap();
        return graphicviewercopymap.copyComplete(this);
    }

    public Rectangle getBoundingRect() {
        if (isBoundingRectInvalid() && !isSkipsBoundingRectChanged()) {
            setBoundingRectInvalid(false);
            setSkipsBoundingRectChanged(true);
            Rectangle rectangle = computeBoundingRect();
            if (rectangle != null) {
                setBoundingRect(rectangle);
            }
            setSkipsBoundingRectChanged(false);
        }
        return myBoundingRect;
    }

    public void setBoundingRect(int j, int k, int l, int i1) {
        if (l >= 0
                && i1 >= 0
                && (myBoundingRect.x != j || myBoundingRect.y != k
                        || myBoundingRect.width != l || myBoundingRect.height != i1)) {
            Rectangle rectangle = new Rectangle(myBoundingRect.x,
                    myBoundingRect.y, myBoundingRect.width,
                    myBoundingRect.height);
            myBoundingRect.x = j;
            myBoundingRect.y = k;
            myBoundingRect.width = l;
            myBoundingRect.height = i1;
            update(1, 0, rectangle);
            if (!isSkipsBoundingRectChanged()) {
                setSkipsBoundingRectChanged(true);
                geometryChange(rectangle);
                if (isBoundingRectInvalid()) {
                    setBoundingRectInvalid(false);
                    Rectangle rectangle1 = computeBoundingRect();
                    if (rectangle1 != null) {
                        setBoundingRect(rectangle1);
                    }
                }
            }
            setSkipsBoundingRectChanged(false);
            GraphicViewerArea graphicviewerarea = getParent();
            if (graphicviewerarea != null
                    && !graphicviewerarea.isSkipsBoundingRectChanged()) {
                graphicviewerarea.setSkipsBoundingRectChanged(true);
                graphicviewerarea.geometryChangeChild(this, rectangle);
                if (graphicviewerarea.isBoundingRectInvalid()) {
                    graphicviewerarea.setBoundingRectInvalid(false);
                    Rectangle rectangle2 = graphicviewerarea
                            .computeBoundingRect();
                    if (rectangle2 != null) {
                        graphicviewerarea.setBoundingRect(rectangle2);
                    }
                }
                graphicviewerarea.setSkipsBoundingRectChanged(false);
            }
            GraphicViewerDocument graphicviewerdocument = getDocument();
            if (graphicviewerdocument != null) {
                graphicviewerdocument.updateDocumentSize(this);
            }
        }
    }

    public final void setBoundingRect(Point point, Dimension dimension) {
        setBoundingRect(point.x, point.y, dimension.width, dimension.height);
    }

    public final void setBoundingRect(Rectangle rectangle) {
        setBoundingRect(rectangle.x, rectangle.y, rectangle.width,
                rectangle.height);
    }

    /** @deprecated */
    protected final boolean setBoundingRectForce(int j, int k, int l, int i1) {
        if (myBoundingRect.x == j && myBoundingRect.y == k
                && myBoundingRect.width == l && myBoundingRect.height == i1) {
            return false;
        } else {
            setBoundingRect(j, k, l, i1);
            return true;
        }
    }

    /** @deprecated */
    public final boolean setBoundingRectForce(Rectangle rectangle) {
        return setBoundingRectForce(rectangle.x, rectangle.y, rectangle.width,
                rectangle.height);
    }

    protected Rectangle computeBoundingRect() {
        return myBoundingRect;
    }

    public final Dimension getSize() {
        return getSize(null);
    }

    public final Dimension getSize(Dimension dimension) {
        if (dimension == null) {
            dimension = new Dimension();
        }
        dimension.width = getWidth();
        dimension.height = getHeight();
        return dimension;
    }

    public final void setSize(Dimension dimension) {
        setBoundingRect(getLeft(), getTop(), dimension.width, dimension.height);
    }

    public final void setSize(int j, int k) {
        setBoundingRect(getLeft(), getTop(), j, k);
    }

    public final int getWidth() {
        return getBoundingRect().width;
    }

    public final void setWidth(int j) {
        setBoundingRect(getLeft(), getTop(), j, getHeight());
    }

    public final int getHeight() {
        return getBoundingRect().height;
    }

    public final void setHeight(int j) {
        setBoundingRect(getLeft(), getTop(), getWidth(), j);
    }

    public final int getLeft() {
        return getBoundingRect().x;
    }

    public final void setLeft(int j) {
        setBoundingRect(j, getTop(), getWidth(), getHeight());
    }

    public final int getTop() {
        return getBoundingRect().y;
    }

    public final void setTop(int j) {
        setBoundingRect(getLeft(), j, getWidth(), getHeight());
    }

    public final Point getTopLeft() {
        return getTopLeft(null);
    }

    public final Point getTopLeft(Point point) {
        if (point == null) {
            point = new Point(0, 0);
        }
        point.x = getLeft();
        point.y = getTop();
        return point;
    }

    public final void setTopLeft(Point point) {
        setBoundingRect(point.x, point.y, getWidth(), getHeight());
    }

    public final void setTopLeft(int j, int k) {
        setBoundingRect(j, k, getWidth(), getHeight());
    }

    public Point getLocation(Point point) {
        return getTopLeft(point);
    }

    public final Point getLocation() {
        return getLocation(null);
    }

    public void setLocation(int j, int k) {
        setTopLeft(j, k);
    }

    public final void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    public void setSizeKeepingLocation(int j, int k) {
        setSize(j, k);
    }

    public final void setLocationOffset(int j, int k, int l, int i1) {
        setLocation(j + l, k + i1);
    }

    public final void setLocationOffset(Point point, Point point1) {
        setLocation(point.x + point1.x, point.y + point1.y);
    }

    public static int spotOpposite(int j) {
        switch (j) {
            default :
                return j;

            case 0 : // '\0'
                return 0;

            case 1 : // '\001'
                return 5;

            case 2 : // '\002'
                return 6;

            case 3 : // '\003'
                return 7;

            case 4 : // '\004'
                return 8;

            case 5 : // '\005'
                return 1;

            case 6 : // '\006'
                return 2;

            case 7 : // '\007'
                return 3;

            case 8 : // '\b'
                return 4;
        }
    }

    public Point getRectangleSpotLocation(Rectangle rectangle, int j,
            Point point) {
        Point point1 = point;
        if (point1 == null) {
            point1 = new Point(rectangle.x, rectangle.y);
        } else {
            point1.x = rectangle.x;
            point1.y = rectangle.y;
        }
        switch (j) {
            case 0 : // '\0'
                point1.x += rectangle.width / 2;
                point1.y += rectangle.height / 2;
                break;

            case 2 : // '\002'
                point1.x += rectangle.width / 2;
                break;

            case 3 : // '\003'
                point1.x += rectangle.width;
                break;

            case 4 : // '\004'
                point1.x += rectangle.width;
                point1.y += rectangle.height / 2;
                break;

            case 5 : // '\005'
                point1.x += rectangle.width;
                point1.y += rectangle.height;
                break;

            case 6 : // '\006'
                point1.x += rectangle.width / 2;
                point1.y += rectangle.height;
                break;

            case 7 : // '\007'
                point1.y += rectangle.height;
                break;

            case 8 : // '\b'
                point1.y += rectangle.height / 2;
                break;
        }
        return point1;
    }

    public Rectangle setRectangleSpotLocation(Rectangle rectangle, int j,
            int k, int l, Rectangle rectangle1) {
        Rectangle rectangle2 = rectangle1;
        if (rectangle2 == null) {
            rectangle2 = new Rectangle(rectangle.x, rectangle.y,
                    rectangle.width, rectangle.height);
        } else {
            rectangle2.width = rectangle.width;
            rectangle2.height = rectangle.height;
        }
        switch (j) {
            case 0 : // '\0'
                rectangle2.x = k - rectangle2.width / 2;
                rectangle2.y = l - rectangle2.height / 2;
                break;

            case 1 : // '\001'
            default :
                rectangle2.x = k;
                rectangle2.y = l;
                break;

            case 2 : // '\002'
                rectangle2.x = k - rectangle2.width / 2;
                rectangle2.y = l;
                break;

            case 3 : // '\003'
                rectangle2.x = k - rectangle2.width;
                rectangle2.y = l;
                break;

            case 4 : // '\004'
                rectangle2.x = k - rectangle2.width;
                rectangle2.y = l - rectangle2.height / 2;
                break;

            case 5 : // '\005'
                rectangle2.x = k - rectangle2.width;
                rectangle2.y = l - rectangle2.height;
                break;

            case 6 : // '\006'
                rectangle2.x = k - rectangle2.width / 2;
                rectangle2.y = l - rectangle2.height;
                break;

            case 7 : // '\007'
                rectangle2.x = k;
                rectangle2.y = l - rectangle2.height;
                break;

            case 8 : // '\b'
                rectangle2.x = k;
                rectangle2.y = l - rectangle2.height / 2;
                break;
        }
        return rectangle2;
    }

    public Point getSpotLocation(int j, Point point) {
        Rectangle rectangle = getBoundingRect();
        return getRectangleSpotLocation(rectangle, j, point);
    }

    public final Point getSpotLocation(int j) {
        return getSpotLocation(j, null);
    }

    public void setSpotLocation(int j, int k, int l) {
        Rectangle rectangle = getBoundingRect();
        setBoundingRect(setRectangleSpotLocation(rectangle, j, k, l, null));
    }

    public final void setSpotLocation(int j, Point point) {
        setSpotLocation(j, point.x, point.y);
    }

    public final void setSpotLocation(int j,
            GraphicViewerObject graphicviewerobject, int k) {
        Point point = graphicviewerobject.getSpotLocation(k);
        setSpotLocation(j, point.x, point.y);
    }

    public final void setSpotLocationOffset(int j, int k, int l, int i1, int j1) {
        setSpotLocation(j, k + i1, l + j1);
    }

    public final void update() {
        update(0, 0, null);
    }

    public void update(int j, int k, Object obj) {
        if (isSuspendUpdates()) {
            return;
        }
        if (isBoundingRectInvalid()) {
            getBoundingRect();
        }
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.fireUpdate(203, j, this, k, obj);
        } else {
            GraphicViewerView graphicviewerview = getView();
            if (graphicviewerview != null) {
                graphicviewerview.fireUpdate(3, j, this);
                if (j == 1) {
                    Rectangle rectangle = graphicviewerview.getTempRectangle();
                    Rectangle rectangle1 = (Rectangle) obj;
                    rectangle.x = rectangle1.x;
                    rectangle.y = rectangle1.y;
                    rectangle.width = rectangle1.width;
                    rectangle.height = rectangle1.height;
                    expandRectByPenWidth(rectangle);
                    graphicviewerview.convertDocToView(rectangle);
                    rectangle.x--;
                    rectangle.y--;
                    rectangle.width += 2;
                    rectangle.height += 2;
                    graphicviewerview.updateView(rectangle);
                }
            }
        }
        if (isUpdatePartner()) {
            Object obj1 = getPartner();
            if (obj1 == null) {
                obj1 = getParent();
            }
            if (obj1 != null) {
                ((GraphicViewerObject) (obj1)).partnerUpdate(this, j, k, obj);
            }
        }
    }

    public void foredate(int j) {
        if (isSuspendUpdates()) {
            return;
        }
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.fireForedate(203, j, this);
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        Rectangle rectangle = getBoundingRect();
        GraphicViewerDrawable.drawRect(graphics2d, GraphicViewerPen.black,
                null, rectangle.x, rectangle.y, rectangle.width,
                rectangle.height);
        GraphicViewerDrawable.drawLine(graphics2d, GraphicViewerPen.black,
                rectangle.x, rectangle.y, rectangle.x + rectangle.width,
                rectangle.y + rectangle.height);
        GraphicViewerDrawable.drawLine(graphics2d, GraphicViewerPen.black,
                rectangle.x, rectangle.y + rectangle.height, rectangle.x
                        + rectangle.width, rectangle.y);
    }

    public boolean isPointInObj(Point point) {
        return getBoundingRect().contains(point.x, point.y);
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
    }

    public boolean getNearestIntersectionPoint(int j, int k, int l, int i1,
            Point point) {
        Rectangle rectangle = getBoundingRect();
        return GraphicViewerRectangle.getNearestIntersectionPoint(rectangle.x,
                rectangle.y, rectangle.width, rectangle.height, j, k, l, i1,
                point);
    }

    public GraphicViewerObject pick(Point point, boolean flag) {
        if (!isVisible()) {
            return null;
        }
        if (!isPointInObj(point)) {
            return null;
        }
        if (!flag) {
            return this;
        }
        if (isSelectable()) {
            return this;
        }
        for (GraphicViewerArea graphicviewerarea = getParent(); graphicviewerarea != null; graphicviewerarea = graphicviewerarea
                .getParent()) {
            if (graphicviewerarea.isSelectable()) {
                return graphicviewerarea;
            }
        }

        return null;
    }

    public boolean doMouseClick(int j, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        return false;
    }

    public boolean doMouseDblClick(int j, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        return false;
    }

    public boolean doUncapturedMouseMove(int j, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        return false;
    }

    public String getToolTipText() {
        return null;
    }

    protected void ownerChange(
            IGraphicViewerObjectCollection graphicviewerobjectcollection,
            IGraphicViewerObjectCollection graphicviewerobjectcollection1,
            GraphicViewerObject graphicviewerobject) {
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        if (!isResizable()) {
            graphicviewerselection.createBoundingHandle(this);
            return;
        }
        Rectangle rectangle = getBoundingRect();
        int j = rectangle.x;
        int k = rectangle.x + rectangle.width / 2;
        int l = rectangle.x + rectangle.width;
        int i1 = rectangle.y;
        int j1 = rectangle.y + rectangle.height / 2;
        int k1 = rectangle.y + rectangle.height;
        graphicviewerselection.createResizeHandle(this, j, i1, 1, true);
        graphicviewerselection.createResizeHandle(this, l, i1, 3, true);
        graphicviewerselection.createResizeHandle(this, j, k1, 7, true);
        graphicviewerselection.createResizeHandle(this, l, k1, 5, true);
        if (!is4ResizeHandles()) {
            graphicviewerselection.createResizeHandle(this, k, i1, 2, true);
            graphicviewerselection.createResizeHandle(this, l, j1, 4, true);
            graphicviewerselection.createResizeHandle(this, k, k1, 6, true);
            graphicviewerselection.createResizeHandle(this, j, j1, 8, true);
        }
    }

    protected void lostSelection(GraphicViewerSelection graphicviewerselection) {
        graphicviewerselection.deleteHandles(this);
    }

    public GraphicViewerObject redirectSelection() {
        return this;
    }

    public GraphicViewerObject getDraggingObject() {
        if (isDragsNode()) {
            for (GraphicViewerArea graphicviewerarea = getParent(); graphicviewerarea != null; graphicviewerarea = graphicviewerarea
                    .getParent()) {
                if (!graphicviewerarea.isDragsNode()) {
                    return graphicviewerarea;
                }
                if (graphicviewerarea.getParent() == null) {
                    return graphicviewerarea;
                }
            }

        }
        return this;
    }

    public void handleMove(GraphicViewerView graphicviewerview, int j, int k,
            int l, int i1, int j1, int k1, int l1) {
        Point point = computeMove(i1, j1, k1, l1, null);
        setSpotLocation(l, point.x, point.y);
    }

    public Point computeMove(int j, int k, int l, int i1, Point point) {
        if (point == null) {
            point = new Point(0, 0);
        }
        point.x = l;
        point.y = i1;
        return point;
    }

    protected Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int j, int k, int l, int i1) {
        Rectangle rectangle1 = computeResize(rectangle, point, j, l, i1);
        if (k == 3) {
            setBoundingRect(rectangle1);
        }
        return rectangle1;
    }

    protected Rectangle computeResize(Rectangle rectangle, Point point, int j,
            int k, int l) {
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        switch (j) {
            case 1 : // '\001'
                rectangle1.x = Math.min(point.x,
                        (rectangle.x + rectangle.width) - k);
                rectangle1.y = Math.min(point.y,
                        (rectangle.y + rectangle.height) - l);
                rectangle1.width += rectangle.x - rectangle1.x;
                rectangle1.height += rectangle.y - rectangle1.y;
                break;

            case 2 : // '\002'
                rectangle1.y = Math.min(point.y,
                        (rectangle.y + rectangle.height) - l);
                rectangle1.height += rectangle.y - rectangle1.y;
                break;

            case 8 : // '\b'
                rectangle1.x = Math.min(point.x,
                        (rectangle.x + rectangle.width) - k);
                rectangle1.width += rectangle.x - rectangle1.x;
                break;

            case 3 : // '\003'
                rectangle1.y = Math.min(point.y,
                        (rectangle.y + rectangle.height) - l);
                rectangle1.width = Math.max(point.x - rectangle.x, k);
                rectangle1.height += rectangle.y - rectangle1.y;
                break;

            case 7 : // '\007'
                rectangle1.x = Math.min(point.x,
                        (rectangle.x + rectangle.width) - k);
                rectangle1.width += rectangle.x - rectangle1.x;
                rectangle1.height = Math.max(point.y - rectangle.y, l);
                break;

            case 4 : // '\004'
                rectangle1.width = Math.max(point.x - rectangle.x, k);
                break;

            case 6 : // '\006'
                rectangle1.height = Math.max(point.y - rectangle.y, l);
                break;

            case 5 : // '\005'
                rectangle1.width = Math.max(point.x - rectangle.x, k);
                rectangle1.height = Math.max(point.y - rectangle.y, l);
                break;
        }
        return rectangle1;
    }

    protected void geometryChange(Rectangle rectangle) {
    }

    protected boolean geometryChangeChild(
            GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        return false;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject",
                            domelement);
            domelement1.setAttribute("obj_flags",
                    Integer.toString(myInternalFlags));
            domelement1.setAttribute("user_flags",
                    Integer.toString(myExternalFlags));
            domelement1
                    .setAttribute("objx", Integer.toString(myBoundingRect.x));
            domelement1
                    .setAttribute("objy", Integer.toString(myBoundingRect.y));
            domelement1.setAttribute("objwidth",
                    Integer.toString(myBoundingRect.width));
            domelement1.setAttribute("objheight",
                    Integer.toString(myBoundingRect.height));
            domdoc.registerObject(this, domelement1);
        }
        if (domdoc.SVGOutputEnabled() && getToolTipText() != null) {
            IDomElement domelement2 = domdoc.createElement("title");
            domelement2.setAttribute("content", "structured text");
            domelement.appendChild(domelement2);
            IDomText domtext = domdoc.createText(getToolTipText());
            domelement2.appendChild(domtext);
        }
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("obj_flags");
            if (s.length() > 0) {
                myInternalFlags = Integer.parseInt(s);
                if ((this instanceof GraphicViewerArea)
                        && (myInternalFlags & 0x200) != 0) {
                    myInternalFlags &= 0xfffffdff;
                    if ((myInternalFlags & 4) != 0) {
                        myInternalFlags |= 0x8000;
                    }
                    myInternalFlags |= 4;
                }
            }
            String s1 = domelement1.getAttribute("user_flags");
            if (s1.length() > 0) {
                myExternalFlags = Integer.parseInt(s1);
            } else {
                myExternalFlags = myInternalFlags;
            }
            String s2 = domelement1.getAttribute("objx");
            String s3 = domelement1.getAttribute("objy");
            String s4 = domelement1.getAttribute("objwidth");
            String s5 = domelement1.getAttribute("objheight");
            if (s2.length() > 0 && s3.length() > 0 && s4.length() > 0
                    && s5.length() > 0) {
                myBoundingRect.x = Integer.parseInt(s2);
            }
            myBoundingRect.y = Integer.parseInt(s3);
            myBoundingRect.width = Integer.parseInt(s4);
            myBoundingRect.height = Integer.parseInt(s5);
            String s6 = domelement1.getAttribute("id");
            if (s6.length() > 0) {
                domdoc.registerTag(s6, this);
            }
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
    }

    public void SVGReadAttributes(IDomElement domelement) {
    }

    public void SVGUpdateReference(String s, Object obj) {
    }

    public GraphicViewerArea getParent() {
        return myParentArea;
    }

    protected void setParent(GraphicViewerArea graphicviewerarea) {
        myParentArea = graphicviewerarea;
    }

    public final boolean isTopLevel() {
        return getParent() == null;
    }

    public GraphicViewerObject getTopLevelObject() {
        Object obj;
        for (obj = this; !((GraphicViewerObject) (obj)).isTopLevel(); obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            ;
        }
        return ((GraphicViewerObject) (obj));
    }

    public GraphicViewerObject getParentNode() {
        Object obj;
        for (obj = this; ((GraphicViewerObject) (obj)).getParent() != null
                && !(((GraphicViewerObject) (obj)).getParent() instanceof GraphicViewerSubGraphBase); obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            ;
        }
        return ((GraphicViewerObject) (obj));
    }

    public GraphicViewerNode getParentGraphicViewerNode() {
        for (Object obj = this; ((GraphicViewerObject) (obj)).getParent() != null;) {
            obj = ((GraphicViewerObject) (obj)).getParent();
            if (obj instanceof GraphicViewerNode) {
                return (GraphicViewerNode) obj;
            }
        }

        return null;
    }

    public boolean isChildOf(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject instanceof GraphicViewerArea) {
            for (GraphicViewerArea graphicviewerarea = getParent(); graphicviewerarea != null; graphicviewerarea = graphicviewerarea
                    .getParent()) {
                if (graphicviewerarea == graphicviewerobject) {
                    return true;
                }
            }

        }
        return false;
    }

    public static GraphicViewerObject findCommonParent(
            GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1) {
        if (graphicviewerobject == graphicviewerobject1) {
            return graphicviewerobject;
        }
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() == graphicviewerobject1) {
            return graphicviewerobject1;
        }
        if (graphicviewerobject1 == null) {
            return null;
        }
        if (graphicviewerobject1.getParent() == graphicviewerobject) {
            return graphicviewerobject;
        }
        if (graphicviewerobject1.getParent() == null) {
            for (Object obj = graphicviewerobject; obj != null; obj = ((GraphicViewerObject) (obj))
                    .getParent()) {
                if (obj == graphicviewerobject1) {
                    return graphicviewerobject1;
                }
            }

        } else if (graphicviewerobject.getParent() == null) {
            for (Object obj1 = graphicviewerobject1; obj1 != null; obj1 = ((GraphicViewerObject) (obj1))
                    .getParent()) {
                if (obj1 == graphicviewerobject) {
                    return graphicviewerobject;
                }
            }

        } else {
            for (Object obj2 = graphicviewerobject; obj2 != null; obj2 = ((GraphicViewerObject) (obj2))
                    .getParent()) {
                for (Object obj3 = graphicviewerobject1; obj3 != null; obj3 = ((GraphicViewerObject) (obj3))
                        .getParent()) {
                    if (obj3 == obj2) {
                        return ((GraphicViewerObject) (obj3));
                    }
                }

            }

        }
        return null;
    }

    public void remove() {
        GraphicViewerArea graphicviewerarea = getParent();
        if (graphicviewerarea != null) {
            graphicviewerarea.removeObject(this);
        } else {
            GraphicViewerLayer graphicviewerlayer = getLayer();
            if (graphicviewerlayer != null) {
                graphicviewerlayer.removeObject(this);
            } else {
                GraphicViewerView graphicviewerview = getView();
                if (graphicviewerview != null) {
                    graphicviewerview.removeObject(this);
                }
            }
        }
    }

    public GraphicViewerDocument getDocument() {
        if (myLayer == null) {
            return null;
        } else {
            return myLayer.getDocument();
        }
    }

    public GraphicViewerLayer getLayer() {
        return myLayer;
    }

    void setLayer(GraphicViewerLayer graphicviewerlayer, int j, Object obj,
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerlayer != null) {
            myLayer = graphicviewerlayer;
            GraphicViewerDocument graphicviewerdocument1 = getDocument();
            if (graphicviewerdocument != graphicviewerdocument1) {
                ownerChange(graphicviewerdocument, graphicviewerdocument1,
                        graphicviewerobject);
                if (j >= 0) {
                    graphicviewerdocument1.fireUpdate(202, 0, this, j, obj);
                }
            }
        } else {
            if (graphicviewerdocument != null) {
                ownerChange(graphicviewerdocument, null, graphicviewerobject);
                if (j >= 0) {
                    graphicviewerdocument.fireUpdate(204, 0, this, j, obj);
                }
            }
            myLayer = null;
        }
    }

    public GraphicViewerView getView() {
        return myView;
    }

    void setView(GraphicViewerView graphicviewerview,
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerView graphicviewerview1 = myView;
        if (graphicviewerview != null) {
            myView = graphicviewerview;
            if (graphicviewerview1 != myView) {
                ownerChange(graphicviewerview1, myView, graphicviewerobject);
                graphicviewerview.fireUpdate(2, 0, this);
            }
        } else {
            if (graphicviewerview1 != null) {
                ownerChange(graphicviewerview1, null, graphicviewerobject);
                graphicviewerview1.fireUpdate(4, 0, this);
            }
            myView = null;
        }
    }

    public void showSelectionHandles(
            GraphicViewerSelection graphicviewerselection) {
        lostSelection(graphicviewerselection);
        gainedSelection(graphicviewerselection);
    }

    public void hideSelectionHandles(
            GraphicViewerSelection graphicviewerselection) {
        lostSelection(graphicviewerselection);
    }

    public GraphicViewerObject getPartner() {
        return null;
    }

    public void setPartner(GraphicViewerObject graphicviewerobject) {
    }

    public void setUpdatePartner(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x800000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x800000;
            } else {
                myInternalFlags &= 0xff7fffff;
            }
            update(14, flag1 ? 1 : 0, null);
        }
    }

    public boolean isUpdatePartner() {
        return (myInternalFlags & 0x800000) != 0;
    }

    protected void partnerUpdate(GraphicViewerObject graphicviewerobject,
            int j, int k, Object obj) {
    }

    void setCurrentListPosition(
            GraphicViewerListPosition graphicviewerlistposition) {
    }

    GraphicViewerListPosition getCurrentListPosition() {
        return null;
    }

    public void copyOldValueForUndo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1 : // '\001'
                Rectangle rectangle = getBoundingRect();
                graphicviewerdocumentchangededit.setNewValue(new Rectangle(
                        rectangle.x, rectangle.y, rectangle.width,
                        rectangle.height));
                return;

            case 2 : // '\002'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isVisible());
                return;

            case 3 : // '\003'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isSelectable());
                return;

            case 4 : // '\004'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isDraggable());
                return;

            case 5 : // '\005'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isResizable());
                return;

            case 6 : // '\006'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(is4ResizeHandles());
                return;

            case 10 : // '\n'
                Object obj = getParent();
                if (obj == null) {
                    obj = getLayer();
                }
                if (obj != null) {
                    GraphicViewerListPosition graphicviewerlistposition = ((IGraphicViewerObjectCollection) (obj))
                            .findObject(this);
                    GraphicViewerListPosition graphicviewerlistposition1 = ((IGraphicViewerObjectCollection) (obj))
                            .getNextObjectPos(graphicviewerlistposition);
                    GraphicViewerObject graphicviewerobject = ((IGraphicViewerObjectCollection) (obj))
                            .getObjectAtPos(graphicviewerlistposition1);
                    if (graphicviewerobject != null) {
                        graphicviewerdocumentchangededit.setNewValueInt(1);
                        graphicviewerdocumentchangededit
                                .setNewValue(graphicviewerobject);
                    } else {
                        graphicviewerdocumentchangededit.setNewValueInt(0);
                        graphicviewerdocumentchangededit.setNewValue(obj);
                    }
                }
                return;

            case 13 : // '\r'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isDragsNode());
                return;

            case 14 : // '\016'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isUpdatePartner());
                return;

            case 15 : // '\017'
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isAutoRescale());
                return;

            case 40 : // '('
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isInitializing());
                return;

            case 0 : // '\0'
                return;

            case 7 : // '\007'
            case 8 : // '\b'
            case 9 : // '\t'
            case 11 : // '\013'
            case 12 : // '\f'
            case 16 : // '\020'
            case 17 : // '\021'
            case 18 : // '\022'
            case 19 : // '\023'
            case 20 : // '\024'
            case 21 : // '\025'
            case 22 : // '\026'
            case 23 : // '\027'
            case 24 : // '\030'
            case 25 : // '\031'
            case 26 : // '\032'
            case 27 : // '\033'
            case 28 : // '\034'
            case 29 : // '\035'
            case 30 : // '\036'
            case 31 : // '\037'
            case 32 : // ' '
            case 33 : // '!'
            case 34 : // '"'
            case 35 : // '#'
            case 36 : // '$'
            case 37 : // '%'
            case 38 : // '&'
            case 39 : // '\''
            default :
                throw new IllegalArgumentException("unknown CHANGED sub-hint: "
                        + Integer.toString(graphicviewerdocumentchangededit
                                .getFlags()));
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1 : // '\001'
                Rectangle rectangle = (Rectangle) graphicviewerdocumentchangededit
                        .getValue(flag);
                myBoundingRect.x = rectangle.x;
                myBoundingRect.y = rectangle.y;
                myBoundingRect.width = rectangle.width;
                myBoundingRect.height = rectangle.height;
                update();
                return;

            case 2 : // '\002'
                setVisible(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 3 : // '\003'
                setSelectable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 4 : // '\004'
                setDraggable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 5 : // '\005'
                setResizable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 6 : // '\006'
                set4ResizeHandles(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 10 : // '\n'
                int j = graphicviewerdocumentchangededit.getValueInt(flag);
                if (j == 1) {
                    GraphicViewerObject graphicviewerobject = (GraphicViewerObject) graphicviewerdocumentchangededit
                            .getValue(flag);
                    if (graphicviewerobject != null) {
                        Object obj = graphicviewerobject.getParent();
                        if (obj == null) {
                            obj = graphicviewerobject.getLayer();
                        }
                        if (obj != null) {
                            GraphicViewerListPosition graphicviewerlistposition = ((IGraphicViewerObjectCollection) (obj))
                                    .findObject(graphicviewerobject);
                            if (graphicviewerlistposition == null) {
                                ((IGraphicViewerObjectCollection) (obj))
                                        .addObjectAtTail(this);
                            } else {
                                ((IGraphicViewerObjectCollection) (obj))
                                        .insertObjectBefore(
                                                graphicviewerlistposition, this);
                            }
                        }
                    }
                } else {
                    IGraphicViewerObjectCollection graphicviewerobjectcollection = (IGraphicViewerObjectCollection) graphicviewerdocumentchangededit
                            .getValue(flag);
                    graphicviewerobjectcollection.addObjectAtTail(this);
                }
                return;

            case 13 : // '\r'
                setDragsNode(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 14 : // '\016'
                setUpdatePartner(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 15 : // '\017'
                setAutoRescale(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 40 : // '('
                setInitializing(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 0 : // '\0'
                return;

            case 7 : // '\007'
            case 8 : // '\b'
            case 9 : // '\t'
            case 11 : // '\013'
            case 12 : // '\f'
            case 16 : // '\020'
            case 17 : // '\021'
            case 18 : // '\022'
            case 19 : // '\023'
            case 20 : // '\024'
            case 21 : // '\025'
            case 22 : // '\026'
            case 23 : // '\027'
            case 24 : // '\030'
            case 25 : // '\031'
            case 26 : // '\032'
            case 27 : // '\033'
            case 28 : // '\034'
            case 29 : // '\035'
            case 30 : // '\036'
            case 31 : // '\037'
            case 32 : // ' '
            case 33 : // '!'
            case 34 : // '"'
            case 35 : // '#'
            case 36 : // '$'
            case 37 : // '%'
            case 38 : // '&'
            case 39 : // '\''
            default :
                throw new IllegalArgumentException("unknown CHANGED sub-hint: "
                        + Integer.toString(graphicviewerdocumentchangededit
                                .getFlags()));
        }
    }

    public final void setFlags(int j) {
        myExternalFlags = j;
    }

    public final int getFlags() {
        return myExternalFlags;
    }

    final void setInternalFlags(int j) {
        myInternalFlags = j;
    }

    final int getInternalFlags() {
        return myInternalFlags;
    }

    public void setVisible(boolean flag) {
        boolean flag1 = (myInternalFlags & 2) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 2;
            } else {
                myInternalFlags &= -3;
            }
            update(2, flag1 ? 1 : 0, null);
        }
    }

    public boolean isVisible() {
        return (myInternalFlags & 2) != 0;
    }

    public boolean canView() {
        for (Object obj = this; obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            if (!((GraphicViewerObject) (obj)).isVisible()) {
                return false;
            }
        }

        GraphicViewerLayer graphicviewerlayer = getLayer();
        if (graphicviewerlayer != null) {
            return graphicviewerlayer.isVisible();
        } else {
            return true;
        }
    }

    public void setSelectable(boolean flag) {
        boolean flag1 = (myInternalFlags & 4) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 4;
            } else {
                myInternalFlags &= -5;
            }
            update(3, flag1 ? 1 : 0, null);
        }
    }

    public boolean isSelectable() {
        return (myInternalFlags & 4) != 0;
    }

    public void setDraggable(boolean flag) {
        boolean flag1 = (myInternalFlags & 8) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 8;
            } else {
                myInternalFlags &= -9;
            }
            update(4, flag1 ? 1 : 0, null);
        }
    }

    public boolean isDraggable() {
        return (myInternalFlags & 8) != 0;
    }

    public void setResizable(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x10) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x10;
            } else {
                myInternalFlags &= 0xffffffef;
            }
            update(5, flag1 ? 1 : 0, null);
        }
    }

    public boolean isResizable() {
        return (myInternalFlags & 0x10) != 0;
    }

    public void set4ResizeHandles(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x20) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x20;
            } else {
                myInternalFlags &= 0xffffffdf;
            }
            update(6, flag1 ? 1 : 0, null);
        }
    }

    public boolean is4ResizeHandles() {
        return (myInternalFlags & 0x20) != 0;
    }

    public void setAutoRescale(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x400000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x400000;
            } else {
                myInternalFlags &= 0xffbfffff;
            }
            update(15, flag1 ? 1 : 0, null);
        }
    }

    public boolean isAutoRescale() {
        return (myInternalFlags & 0x400000) != 0;
    }

    public void setDragsNode(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x400) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x400;
            } else {
                myInternalFlags &= 0xfffffbff;
            }
            update(13, flag1 ? 1 : 0, null);
        }
    }

    public boolean isDragsNode() {
        return (myInternalFlags & 0x400) != 0;
    }

    public void setSuspendUpdates(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x40) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x40;
            } else {
                myInternalFlags &= 0xffffffbf;
            }
            update(0, 1, null);
        }
    }

    public boolean isSuspendUpdates() {
        return (myInternalFlags & 0x40) != 0;
    }

    public void setSkipsUndoManager(boolean flag) {
        boolean flag1 = (myInternalFlags & 0x800) != 0;
        if (flag1 != flag) {
            if (flag) {
                myInternalFlags |= 0x800;
            } else {
                myInternalFlags &= 0xfffff7ff;
            }
        }
    }

    public boolean isSkipsUndoManager() {
        return (myInternalFlags & 0x800) != 0;
    }

    /** @deprecated */
    public void setSuspendChildUpdates(boolean flag) {
        if (((myInternalFlags & 0x80) != 0) != flag) {
            if (flag) {
                myInternalFlags |= 0x80;
            } else {
                myInternalFlags &= 0xffffff7f;
            }
        }
    }

    /** @deprecated */
    public boolean isSuspendChildUpdates() {
        return (myInternalFlags & 0x80) != 0;
    }

    protected void setBoundingRectInvalid(boolean flag) {
        if (flag) {
            myInternalFlags |= 0x100;
        } else {
            myInternalFlags &= 0xfffffeff;
        }
    }

    protected boolean isBoundingRectInvalid() {
        return (myInternalFlags & 0x100) != 0;
    }

    public void setInitializing(boolean flag) {
        if (flag) {
            myInternalFlags |= 0x100000;
        } else {
            myInternalFlags &= 0xffefffff;
        }
    }

    public boolean isInitializing() {
        return (myInternalFlags & 0x100000) != 0;
    }

    void setSkipsBoundingRectChanged(boolean flag) {
        if (flag) {
            myInternalFlags |= 0x200000;
        } else {
            myInternalFlags &= 0xffdfffff;
        }
    }

    boolean isSkipsBoundingRectChanged() {
        return (myInternalFlags & 0x200000) != 0;
    }

    public static void setBoundsRect(Rectangle rectangle, Rectangle rectangle1) {
        rectangle.x = rectangle1.x;
        rectangle.y = rectangle1.y;
        rectangle.width = rectangle1.width;
        rectangle.height = rectangle1.height;
    }

    public static Rectangle copyRect(Rectangle rectangle) {
        return new Rectangle(rectangle.x, rectangle.y, rectangle.width,
                rectangle.height);
    }

    public static void growRect(Rectangle rectangle, int j, int k) {
        rectangle.x -= j;
        rectangle.width += 2 * j;
        rectangle.y -= k;
        rectangle.height += 2 * k;
    }
}