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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerSubGraph extends GraphicViewerSubGraphBase {

    private static final long serialVersionUID = 8665889205874001054L;

    private static final int flagExpanded = 65536;
    private static final int flagCollapsible = 131072;
    private static final int flagExpandedResizable = 262144;
    private static final int flagWasExpanded = 524288;
    public static final int StateExpanded = 0;
    public static final int StateCollapsed = 1;
    public static final int StateExpanding = 2;
    public static final int StateCollapsing = 3;
    public static final int ChangedBackgroundColor = 2401;
    public static final int ChangedBorderPen = 2402;
    public static final int ChangedLabelSpot = 2403;
    public static final int ChangedCollapsible = 2405;
    public static final int ChangedInsets = 2406;
    public static final int ChangedOpacity = 2407;
    public static final int ChangedCollapsedLabelSpot = 2408;
    public static final int ChangedCollapsedInsets = 2409;
    public static final int ChangedCollapsedObject = 2410;
    public static final int ChangedLabel = 2411;
    public static final int ChangedPort = 2412;
    public static final int ChangedState = 2413;
    public static final int ChangedSavedBounds = 2414;
    public static final int ChangedSavedPaths = 2415;
    public static final int ChangedWasExpanded = 2416;
    public static final int ChangedExpandedResizable = 2417;
    private int myState = 0;
    private GraphicViewerSubGraphHandle myHandle = null;
    private GraphicViewerText myLabel = null;
    private GraphicViewerPort myPort = null;
    private GraphicViewerObject myCollapsedObject = null;
    private Color myBackgroundColor = new Color(200, 200, 255, 63);
    private double myOpacity = 0.2D;
    private GraphicViewerPen myBorderPen = null;
    private int myLabelSpot = 2;
    private int myCollapsedLabelSpot = 0;
    private Insets myInsets = new Insets(4, 4, 4, 4);
    private Insets myCollapsedInsets = new Insets(0, 0, 0, 0);
    private HashMap myBoundsHashtable = new HashMap();
    private HashMap myPathsHashtable = new HashMap();
    private transient Rectangle mySavedBoundsInsideMargins = null;

    public GraphicViewerSubGraph() {
        initCommon();
    }

    public GraphicViewerSubGraph(String s) {
        initCommon();
        initialize(s);
    }

    private void initCommon() {
        setInternalFlags((getInternalFlags() | 0x100000 | 0x10000 | 0x20000) & 0xffffffef & 0xfffffbff);
    }

    public void initialize(String s) {
        myCollapsedObject = createCollapsedObject();
        super.addObjectAtTail(myCollapsedObject);
        myLabel = createLabel(s);
        super.addObjectAtTail(myLabel);
        myHandle = createHandle();
        super.addObjectAtTail(myHandle);
        myPort = createPort();
        super.addObjectAtHead(myPort);
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerText createLabel(String s) {
        GraphicViewerText graphicviewertext = null;
        if (s != null) {
            graphicviewertext = new GraphicViewerText();
            graphicviewertext.setText(s);
            graphicviewertext.setSelectable(false);
            graphicviewertext.setDragsNode(true);
            graphicviewertext.setEditable(true);
            graphicviewertext.setEditOnSingleClick(true);
            graphicviewertext.setTransparent(true);
            graphicviewertext.setBold(true);
            graphicviewertext.setAlignment(2);
            graphicviewertext.setMultiline(true);
            graphicviewertext.setWrapping(true);
        }
        return graphicviewertext;
    }

    public GraphicViewerSubGraphHandle createHandle() {
        GraphicViewerSubGraphHandle graphicviewersubgraphhandle = new GraphicViewerSubGraphHandle();
        return graphicviewersubgraphhandle;
    }

    protected GraphicViewerPort createPort() {
        return null;
    }

    protected GraphicViewerObject createCollapsedObject() {
        return null;
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerSubGraph graphicviewersubgraph = (GraphicViewerSubGraph) graphicviewerarea;
        graphicviewersubgraph.myState = myState;
        graphicviewersubgraph.myBackgroundColor = myBackgroundColor;
        graphicviewersubgraph.myOpacity = myOpacity;
        graphicviewersubgraph.myBorderPen = myBorderPen;
        graphicviewersubgraph.myLabelSpot = myLabelSpot;
        graphicviewersubgraph.myCollapsedLabelSpot = myCollapsedLabelSpot;
        graphicviewersubgraph.myInsets.top = myInsets.top;
        graphicviewersubgraph.myInsets.left = myInsets.left;
        graphicviewersubgraph.myInsets.bottom = myInsets.bottom;
        graphicviewersubgraph.myInsets.right = myInsets.right;
        graphicviewersubgraph.myCollapsedInsets.top = myCollapsedInsets.top;
        graphicviewersubgraph.myCollapsedInsets.left = myCollapsedInsets.left;
        graphicviewersubgraph.myCollapsedInsets.bottom = myCollapsedInsets.bottom;
        graphicviewersubgraph.myCollapsedInsets.right = myCollapsedInsets.right;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewersubgraph.myHandle = (GraphicViewerSubGraphHandle) graphicviewercopyenvironment
                .get(myHandle);
        graphicviewersubgraph.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
        graphicviewersubgraph.myPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myPort);
        graphicviewersubgraph.myCollapsedObject = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myCollapsedObject);
        HashMap hashmap = graphicviewersubgraph.myBoundsHashtable;
        GraphicViewerObject graphicviewerobject1;
        Rectangle rectangle;
        for (Iterator iterator = myBoundsHashtable.entrySet().iterator(); iterator
                .hasNext(); hashmap.put(graphicviewerobject1, new Rectangle(
                rectangle.x, rectangle.y, rectangle.width, rectangle.height))) {
            Entry entry = (Entry) iterator.next();
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) entry
                    .getKey();
            graphicviewerobject1 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject);
            rectangle = (Rectangle) entry.getValue();
        }

        hashmap = graphicviewersubgraph.myPathsHashtable;
        Iterator iterator1 = myPathsHashtable.entrySet().iterator();
        do {
            if (!iterator1.hasNext()) {
                break;
            }
            Entry entry1 = (Entry) iterator1.next();
            GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) entry1
                    .getKey();
            GraphicViewerStroke graphicviewerstroke1 = (GraphicViewerStroke) graphicviewercopyenvironment
                    .get(graphicviewerstroke);
            if (graphicviewerstroke != null) {
                ArrayList arraylist = (ArrayList) entry1.getValue();
                ArrayList arraylist1 = new ArrayList();
                for (int i = 0; i < arraylist.size(); i++) {
                    Point point = (Point) arraylist.get(i);
                    arraylist1.add(new Point(point.x, point.y));
                }

                hashmap.put(graphicviewerstroke1, arraylist1);
            }
        } while (true);
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerListPosition graphicviewerlistposition = super
                .addObjectAtHead(graphicviewerobject);
        if (graphicviewerlistposition != null) {
            graphicviewerobject.setDragsNode(false);
        }
        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        Object obj = getLabel();
        if (obj == null) {
            obj = getHandle();
        }
        if (obj != null) {
            return insertObjectBefore(
                    findObject(((GraphicViewerObject) (obj))),
                    graphicviewerobject);
        }
        GraphicViewerListPosition graphicviewerlistposition = super
                .addObjectAtTail(graphicviewerobject);
        if (graphicviewerlistposition != null) {
            graphicviewerobject.setDragsNode(false);
        }
        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerListPosition graphicviewerlistposition1 = super
                .insertObjectBefore(graphicviewerlistposition,
                        graphicviewerobject);
        if (graphicviewerlistposition1 != null) {
            graphicviewerobject.setDragsNode(false);
        }
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerListPosition graphicviewerlistposition1 = super
                .insertObjectAfter(graphicviewerlistposition,
                        graphicviewerobject);
        if (graphicviewerlistposition1 != null) {
            graphicviewerobject.setDragsNode(false);
        }
        return graphicviewerlistposition1;
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myLabel) {
            myLabel = null;
        } else if (graphicviewerobject == myHandle) {
            myHandle = null;
        } else if (graphicviewerobject == myPort) {
            myPort = null;
        } else if (graphicviewerobject == myCollapsedObject) {
            myCollapsedObject = null;
        }
        if (getSavedBounds().containsKey(graphicviewerobject)) {
            getSavedBounds().remove(graphicviewerobject);
        }
        if (getSavedPaths().containsKey(graphicviewerobject)) {
            getSavedPaths().remove(graphicviewerobject);
        }
        return graphicviewerobject;
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        if (paintsDecoration(graphicviewerview)) {
            paintDecoration(graphics2d, graphicviewerview);
        }
        super.paint(graphics2d, graphicviewerview);
    }

    protected boolean paintsDecoration(GraphicViewerView graphicviewerview) {
        return getCollapsedObject() == null
                || !getCollapsedObject().isVisible();
    }

    protected void paintDecoration(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview) {
        Color color = getBackgroundColor();
        GraphicViewerPen graphicviewerpen = getBorderPen();
        if (color != null || graphicviewerpen != null) {
            Rectangle rectangle = computeBorder();
            if (color.getAlpha() == 255) {
                color = new Color(color.getRed(), color.getGreen(),
                        color.getBlue(), Math.max(0,
                                Math.min(255, (int) (255D * getOpacity()))));
            }
            GraphicViewerDrawable.drawRect(graphics2d, graphicviewerpen,
                    GraphicViewerBrush.makeStockBrush(color), rectangle.x,
                    rectangle.y, rectangle.width, rectangle.height);
        }
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        super.expandRectByPenWidth(rectangle);
        Insets insets;
        if (isExpanded()) {
            insets = getInsets();
        } else {
            insets = getCollapsedInsets();
        }
        int i = 1;
        GraphicViewerPen graphicviewerpen = getBorderPen();
        if (graphicviewerpen != null) {
            i = graphicviewerpen.getWidth();
        }
        rectangle.x -= i + insets.left;
        rectangle.y -= i + insets.top;
        rectangle.width += i + insets.left + insets.right;
        rectangle.height += i + insets.top + insets.bottom;
    }

    protected Rectangle computeBoundingRect() {
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        boolean flag = false;
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject.isVisible()) {
                Rectangle rectangle2 = graphicviewerobject.getBoundingRect();
                if (!flag) {
                    flag = true;
                    rectangle.x = rectangle2.x;
                    rectangle.y = rectangle2.y;
                    rectangle.width = rectangle2.width;
                    rectangle.height = rectangle2.height;
                } else {
                    rectangle.add(rectangle2);
                }
            }
        } while (true);
        if (!flag) {
            rectangle = getBoundingRect();
        }
        Rectangle rectangle1 = computeBorder();
        rectangle1.add(rectangle);
        return rectangle1;
    }

    public Rectangle computeBorder() {
        Rectangle rectangle = computeInsideMargins(null);
        if (rectangle.width > 0 && rectangle.height > 0) {
            Insets insets;
            if (isExpanded()) {
                insets = getInsets();
            } else {
                insets = getCollapsedInsets();
            }
            rectangle.x -= insets.left;
            rectangle.y -= insets.top;
            rectangle.width += insets.left + insets.right;
            rectangle.height += insets.top + insets.bottom;
        } else {
            setBoundsRect(rectangle, getBoundingRect());
        }
        return rectangle;
    }

    private static Rectangle getBoundsWithoutMargins(
            GraphicViewerSubGraph paramGraphicViewerSubGraph,
            Rectangle paramRectangle) {
        if (paramRectangle == null) {
            paramRectangle = new Rectangle(0, 0, 0, 0);
        }
        setBoundsRect(paramRectangle,
                paramGraphicViewerSubGraph.getBoundingRect());
        Insets localInsets;
        if (paramGraphicViewerSubGraph.isExpanded()) {
            localInsets = paramGraphicViewerSubGraph.getInsets();
        } else {
            localInsets = paramGraphicViewerSubGraph.getCollapsedInsets();
        }
        paramRectangle.x += localInsets.left;
        paramRectangle.y += localInsets.top;
        paramRectangle.width -= localInsets.left + localInsets.right;
        paramRectangle.height -= localInsets.top + localInsets.bottom;
        return paramRectangle;
    }

    public Rectangle computeInsideMargins(
            GraphicViewerObject graphicviewerobject) {
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        boolean flag = false;
        boolean flag1 = false;
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
            if ((graphicviewerobject == null || graphicviewerobject1 != graphicviewerobject)
                    && !computeInsideMarginsSkip(graphicviewerobject1)) {
                Rectangle rectangle1 = graphicviewerobject1.getBoundingRect();
                if (!flag) {
                    flag = true;
                    setBoundsRect(rectangle, rectangle1);
                } else {
                    flag1 = true;
                    rectangle.add(rectangle1);
                }
            }
        } while (true);
        if (!flag) {
            if (getCollapsedObject() != null) {
                setBoundsRect(rectangle, getCollapsedObject().getBoundingRect());
            } else if (mySavedBoundsInsideMargins != null) {
                setBoundsRect(rectangle, mySavedBoundsInsideMargins);
            }
        } else if (flag && !flag1) {
            if (mySavedBoundsInsideMargins == null) {
                mySavedBoundsInsideMargins = new Rectangle(0, 0, 0, 0);
            }
            setBoundsRect(mySavedBoundsInsideMargins, rectangle);
        }
        return rectangle;
    }

    protected boolean computeInsideMarginsSkip(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == getHandle()) {
            return true;
        }
        if (graphicviewerobject == getLabel()) {
            return !graphicviewerobject.isVisible();
        }
        if (graphicviewerobject instanceof GraphicViewerPort) {
            return true;
        }
        if (graphicviewerobject == getCollapsedObject()) {
            return !graphicviewerobject.isVisible();
        }
        GraphicViewerLink graphicviewerlink = null;
        if (graphicviewerobject instanceof GraphicViewerLink) {
            graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
        }
        if (graphicviewerlink != null) {
            if (!graphicviewerlink.isVisible() || !isExpanded()) {
                return true;
            }
            if (getPort() != null
                    && (graphicviewerlink.getFromPort() != null
                            && graphicviewerlink.getFromPort().getParent() == this || graphicviewerlink
                            .getToPort() != null
                            && graphicviewerlink.getToPort().getParent() == this)) {
                return true;
            }
        } else if (getCollapsedObject() != null
                && !graphicviewerobject.isVisible() && !isExpanded()) {
            return true;
        }
        return false;
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        if (!isResizable()) {
            graphicviewerselection.createBoundingHandle(this);
            return;
        }
        Rectangle rectangle = computeBorder();
        int i = rectangle.x;
        int j = rectangle.x + rectangle.width / 2;
        int k = rectangle.x + rectangle.width;
        int l = rectangle.y;
        int i1 = rectangle.y + rectangle.height / 2;
        int j1 = rectangle.y + rectangle.height;
        graphicviewerselection.createResizeHandle(this, i, l, 1, true);
        graphicviewerselection.createResizeHandle(this, k, l, 3, true);
        graphicviewerselection.createResizeHandle(this, i, j1, 7, true);
        graphicviewerselection.createResizeHandle(this, k, j1, 5, true);
        if (!is4ResizeHandles()) {
            graphicviewerselection.createResizeHandle(this, j, l, 2, true);
            graphicviewerselection.createResizeHandle(this, k, i1, 4, true);
            graphicviewerselection.createResizeHandle(this, j, j1, 6, true);
            graphicviewerselection.createResizeHandle(this, i, i1, 8, true);
        }
    }

    protected Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        Rectangle rectangle1 = computeInsideMargins(null);
        Rectangle rectangle2 = computeResize(rectangle, point, i,
                rectangle1.width, rectangle1.height);
        Insets insets = new Insets(Math.max(0, rectangle1.y - rectangle2.y),
                Math.max(0, rectangle1.x - rectangle2.x), Math.max(0,
                        (rectangle2.y + rectangle2.height)
                                - (rectangle1.y + rectangle1.height)),
                Math.max(0, (rectangle2.x + rectangle2.width)
                        - (rectangle1.x + rectangle1.width)));
        if (isExpanded()) {
            setInsets(insets);
        } else {
            setCollapsedInsets(insets);
        }
        return null;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing()) {
            return;
        }
        if (graphicviewerobject != null && graphicviewerobject == getHandle()) {
            return;
        }
        if (graphicviewerobject != null && graphicviewerobject == getPort()) {
            return;
        }
        layoutCollapsedObject();
        layoutLabel();
        layoutHandle();
        if (getHandle() != null && getLabel() != null
                && getHandle().getLeft() == getLabel().getLeft()
                && getHandle().getTop() == getLabel().getTop()) {
            layoutLabel();
        }
        layoutPort();
    }

    protected void layoutCollapsedObject() {
        GraphicViewerObject graphicviewerobject = getCollapsedObject();
        if (graphicviewerobject == null) {
            return;
        }
        Rectangle rectangle;
        if (isExpanded()) {
            rectangle = computeInsideMargins(graphicviewerobject);
        } else {
            Dimension dimension = computeCollapsedSize(true);
            rectangle = computeCollapsedRectangle(dimension);
        }
        boolean flag = isInitializing();
        setInitializing(true);
        graphicviewerobject.setTopLeft(rectangle.x, rectangle.y);
        setInitializing(flag);
    }

    protected void layoutLabel() {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null) {
            return;
        }
        Rectangle rectangle;
        int i;
        if (!isExpanded()) {
            i = getCollapsedLabelSpot();
            GraphicViewerObject graphicviewerobject = getCollapsedObject();
            if (graphicviewerobject != null) {
                rectangle = graphicviewerobject.getBoundingRect();
            } else {
                Dimension dimension = computeCollapsedSize(true);
                rectangle = computeCollapsedRectangle(dimension);
            }
        } else {
            i = getLabelSpot();
            rectangle = computeInsideMargins(graphicviewertext);
        }
        Point point = getRectangleSpotLocation(rectangle, i, null);
        boolean flag = isInitializing();
        setInitializing(true);
        positionLabel(graphicviewertext, i, point);
        setInitializing(flag);
    }

    private void positionLabel(GraphicViewerText graphicviewertext, int i,
            Point point) {
        if (i == 1) {
            graphicviewertext.setAlignment(i);
            graphicviewertext.setSpotLocation(7, point);
            if (getHandle() != null
                    && getHandle().getLeft() == graphicviewertext.getLeft()
                    && getHandle().getTop() == graphicviewertext.getTop()) {
                point.x += getHandle().getWidth() + 2;
                graphicviewertext.setSpotLocation(7, point);
            }
        } else if (i == 3) {
            graphicviewertext.setAlignment(i);
            graphicviewertext.setSpotLocation(5, point);
        } else if (i == 5) {
            graphicviewertext.setAlignment(i);
            graphicviewertext.setSpotLocation(3, point);
        } else if (i == 7) {
            graphicviewertext.setAlignment(i);
            graphicviewertext.setSpotLocation(1, point);
        } else {
            graphicviewertext.setAlignment(spotOpposite(i));
            graphicviewertext.setSpotLocation(spotOpposite(i), point);
        }
    }

    protected void layoutHandle() {
        if (!isExpanded()) {
            return;
        }
        GraphicViewerSubGraphHandle graphicviewersubgraphhandle = getHandle();
        if (graphicviewersubgraphhandle != null) {
            Rectangle rectangle = computeInsideMargins(null);
            graphicviewersubgraphhandle.setTopLeft(rectangle.x, rectangle.y);
        }
    }

    public void layoutPort() {
        GraphicViewerPort graphicviewerport = getPort();
        if (graphicviewerport != null) {
            if (getHandle() != null) {
                Rectangle rectangle = getHandle().getBoundingRect();
                graphicviewerport.setBoundingRect(rectangle);
            } else if (getLabel() != null) {
                graphicviewerport.setBoundingRect(getLabel().getBoundingRect());
            } else {
                Rectangle rectangle1 = computeInsideMargins(null);
                graphicviewerport.setTopLeft(rectangle1.x, rectangle1.y);
            }
        }
    }

    public Dimension computeCollapsedSize(boolean flag) {
        Dimension dimension = new Dimension(0, 0);
        if (flag && getCollapsedObject() != null) {
            Dimension dimension1 = getCollapsedObject().getSize();
            dimension.width = dimension1.width;
            dimension.height = dimension1.height;
        }
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
            if (!computeCollapsedSizeSkip(graphicviewerobject)) {
                Dimension dimension2 = graphicviewerobject.getSize();
                GraphicViewerSubGraph graphicviewersubgraph = null;
                if (graphicviewerobject instanceof GraphicViewerSubGraph) {
                    graphicviewersubgraph = (GraphicViewerSubGraph) graphicviewerobject;
                }
                if (graphicviewersubgraph != null) {
                    dimension2 = graphicviewersubgraph
                            .computeCollapsedSize(false);
                }
                dimension.width = Math.max(dimension.width, dimension2.width);
                dimension.height = Math
                        .max(dimension.height, dimension2.height);
            }
        } while (true);
        return dimension;
    }

    protected boolean computeCollapsedSizeSkip(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == getHandle()) {
            return true;
        }
        if (graphicviewerobject == getLabel()) {
            return true;
        }
        if (graphicviewerobject == getCollapsedObject()) {
            return true;
        }
        if (graphicviewerobject instanceof GraphicViewerPort) {
            return true;
        }
        if (graphicviewerobject instanceof GraphicViewerLink) {
            return true;
        }
        if (graphicviewerobject.getPartner() instanceof GraphicViewerLabeledLink) {
            return true;
        }
        return getCollapsedObject() != null && !graphicviewerobject.isVisible();
    }

    protected Point computeReferencePoint() {
        Point point;
        if (getHandle() != null) {
            point = getHandle().getTopLeft();
        } else {
            point = getTopLeft();
        }
        return point;
    }

    protected Rectangle computeCollapsedRectangle(Dimension dimension) {
        Point point = computeReferencePoint();
        return new Rectangle(point.x, point.y, dimension.width,
                dimension.height);
    }

    public void collapse() {
        if (getState() != 0) {
            return;
        }
        if (!isCollapsible()) {
            return;
        }
        setState(3);
        setInitializing(true);
        foredate(40);
        foredate(2415);
        foredate(2414);
        getSavedBounds().put(this, copyRect(getBoundingRect()));
        prepareCollapse();
        Dimension dimension = computeCollapsedSize(true);
        Rectangle rectangle = computeCollapsedRectangle(dimension);
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            saveChildBounds(graphicviewerobject, rectangle);
        }

        for (GraphicViewerListPosition graphicviewerlistposition1 = getFirstObjectPos(); graphicviewerlistposition1 != null;) {
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition1);
            graphicviewerlistposition1 = getNextObjectPos(graphicviewerlistposition1);
            collapseChild(graphicviewerobject1, rectangle);
        }

        finishCollapse(rectangle);
        setInitializing(false);
        setState(1);
        layoutChildren(null);
        setBoundingRectInvalid(true);
        update(2414, 0, null);
        update(2415, 0, null);
        update(40, 0, null);
    }

    protected void prepareCollapse() {
    }

    protected void saveChildBounds(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle) {
        if (graphicviewerobject == getHandle()) {
            return;
        }
        if (graphicviewerobject == getLabel()) {
            return;
        }
        if (graphicviewerobject == getCollapsedObject()) {
            return;
        }
        if (graphicviewerobject instanceof GraphicViewerPort) {
            return;
        }
        if (graphicviewerobject instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
            ArrayList arraylist = graphicviewerlink.copyPointsArray();
            for (int i = 0; i < arraylist.size(); i++) {
                Point point = (Point) arraylist.get(i);
                point.x -= rectangle.x;
                point.y -= rectangle.y;
            }

            getSavedPaths().put(graphicviewerobject, arraylist);
        } else if (!(graphicviewerobject.getPartner() instanceof GraphicViewerLabeledLink)) {
            Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
            getSavedBounds()
                    .put(graphicviewerobject,
                            new Rectangle(rectangle1.x - rectangle.x,
                                    rectangle1.y - rectangle.y,
                                    rectangle1.width, rectangle1.height));
        }
    }

    protected void collapseChild(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle) {
        if (graphicviewerobject == getHandle()) {
            return;
        }
        if (graphicviewerobject == getLabel()) {
            return;
        }
        if (graphicviewerobject == getCollapsedObject()) {
            return;
        }
        if (graphicviewerobject instanceof GraphicViewerPort) {
            return;
        }
        if (!(graphicviewerobject instanceof GraphicViewerLink)) {
            GraphicViewerSubGraph graphicviewersubgraph = null;
            if (graphicviewerobject instanceof GraphicViewerSubGraph) {
                graphicviewersubgraph = (GraphicViewerSubGraph) graphicviewerobject;
            }
            if (graphicviewersubgraph != null
                    && graphicviewersubgraph.isExpanded()) {
                graphicviewersubgraph.setWasExpanded(true);
                graphicviewersubgraph.collapse();
            }
            Point point = new Point(rectangle.x + rectangle.width / 2,
                    rectangle.y + rectangle.height / 2);
            graphicviewerobject.setSpotLocation(0, point);
        }
        graphicviewerobject.setVisible(false);
    }

    protected void finishCollapse(Rectangle rectangle) {
        if (getCollapsedObject() != null) {
            getCollapsedObject().setVisible(true);
        }
        if (isResizable()) {
            setExpandedResizable(true);
            setResizable(false);
        }
    }

    public void expand() {
        if (getState() != 1) {
            return;
        }
        if (!isCollapsible()) {
            return;
        }
        setState(2);
        setInitializing(true);
        foredate(40);
        foredate(2415);
        foredate(2414);
        prepareExpand();
        Point point = computeReferencePoint();
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject instanceof GraphicViewerLink)) {
                expandChild(graphicviewerobject, point);
            }
        } while (true);
        graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject1 instanceof GraphicViewerLink) {
                expandChild(graphicviewerobject1, point);
            }
        } while (true);
        boolean flag = getSavedBounds().size() <= 1;
        Rectangle rectangle = null;
        if (getSavedBounds().containsKey(this)) {
            rectangle = (Rectangle) getSavedBounds().get(this);
        }
        finishExpand(point);
        setInitializing(false);
        setState(0);
        layoutChildren(null);
        setBoundingRectInvalid(true);
        if (flag && rectangle != null) {
            setTopLeft(rectangle.x, rectangle.y);
        }
        update(2414, 0, null);
        update(2415, 0, null);
        update(40, 0, null);
    }

    protected void prepareExpand() {
    }

    protected void expandChild(GraphicViewerObject graphicviewerobject,
            Point point) {
        if (graphicviewerobject == getCollapsedObject()) {
            return;
        }
        graphicviewerobject.setVisible(true);
        if (graphicviewerobject instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
            if (getSavedPaths().containsKey(graphicviewerlink)) {
                ArrayList arraylist = (ArrayList) getSavedPaths().get(
                        graphicviewerlink);
                ArrayList arraylist1 = new ArrayList();
                for (int i = 0; i < arraylist.size(); i++) {
                    Point point1 = (Point) arraylist.get(i);
                    arraylist1.add(new Point(point1.x + point.x, point1.y
                            + point.y));
                }

                graphicviewerlink.setPoints(arraylist1);
            }
        } else if (getSavedBounds().containsKey(graphicviewerobject)) {
            Rectangle rectangle = (Rectangle) getSavedBounds().get(
                    graphicviewerobject);
            if (graphicviewerobject instanceof GraphicViewerSubGraph) {
                GraphicViewerSubGraph graphicviewersubgraph = (GraphicViewerSubGraph) graphicviewerobject;
                if (graphicviewersubgraph.isWasExpanded()) {
                    graphicviewersubgraph.setWasExpanded(false);
                    graphicviewersubgraph.expand();
                }
                graphicviewersubgraph.setTopLeft(point.x + rectangle.x, point.y
                        + rectangle.y);
            } else {
                graphicviewerobject.setBoundingRect(point.x + rectangle.x,
                        point.y + rectangle.y, rectangle.width,
                        rectangle.height);
            }
        }
    }

    protected void finishExpand(Point point) {
        if (getCollapsedObject() != null) {
            getCollapsedObject().setVisible(false);
        }
        if (isExpandedResizable()) {
            setExpandedResizable(false);
            setResizable(true);
        }
        getSavedBounds().clear();
        getSavedPaths().clear();
    }

    public void toggle() {
        if (getState() == 0) {
            collapse();
        } else if (getState() == 1) {
            expand();
        }
    }

    public void expandAll() {
        expand();
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerSubGraph) {
                ((GraphicViewerSubGraph) graphicviewerobject).expandAll();
            }
        } while (true);
    }

    public HashMap getSavedBounds() {
        return myBoundsHashtable;
    }

    public HashMap getSavedPaths() {
        return myPathsHashtable;
    }

    public GraphicViewerSubGraphHandle getHandle() {
        return myHandle;
    }

    public GraphicViewerText getLabel() {
        return myLabel;
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
        GraphicViewerText graphicviewertext1 = myLabel;
        if (graphicviewertext1 != graphicviewertext) {
            if (graphicviewertext1 != null) {
                removeObject(graphicviewertext1);
            }
            myLabel = graphicviewertext;
            if (graphicviewertext != null) {
                addObjectAtTail(graphicviewertext);
            }
            update(2411, 0, graphicviewertext1);
        }
    }

    public GraphicViewerPort getPort() {
        return myPort;
    }

    public void setPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                removeObject(graphicviewerport1);
            }
            myPort = graphicviewerport;
            if (graphicviewerport != null) {
                addObjectAtHead(graphicviewerport);
            }
            update(2412, 0, graphicviewerport1);
        }
    }

    public GraphicViewerObject getCollapsedObject() {
        return myCollapsedObject;
    }

    public void setCollapsedObject(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myCollapsedObject;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null) {
                removeObject(graphicviewerobject1);
            }
            myCollapsedObject = graphicviewerobject;
            if (graphicviewerobject != null) {
                addObjectAtHead(graphicviewerobject);
            }
            update(2410, 0, graphicviewerobject1);
            if (getCollapsedObject() != null && graphicviewerobject1 != null) {
                getCollapsedObject().setVisible(
                        graphicviewerobject1.isVisible());
            }
        }
    }

    public Color getBackgroundColor() {
        return myBackgroundColor;
    }

    public void setBackgroundColor(Color color) {
        Color color1 = myBackgroundColor;
        if (color1 == null && color != null || !color1.equals(color)) {
            myBackgroundColor = color;
            update(2401, 0, color1);
        }
    }

    public void setOpacity(double d) {
        double d1 = myOpacity;
        if (d1 != d && d >= 0.0D && d <= 1.0D) {
            myOpacity = d;
            update(2407, 0, new Double(d1));
        }
    }

    public double getOpacity() {
        return myOpacity;
    }

    public GraphicViewerPen getBorderPen() {
        return myBorderPen;
    }

    public void setBorderPen(GraphicViewerPen graphicviewerpen) {
        GraphicViewerPen graphicviewerpen1 = myBorderPen;
        if (graphicviewerpen1 == null && graphicviewerpen != null
                || !graphicviewerpen1.equals(graphicviewerpen)) {
            myBorderPen = graphicviewerpen;
            update(2402, 0, graphicviewerpen1);
        }
    }

    public Insets getInsets() {
        return myInsets;
    }

    public void setInsets(Insets insets) {
        Insets insets1 = myInsets;
        if (!insets1.equals(insets) && insets.top >= 0 && insets.left >= 0
                && insets.bottom >= 0 && insets.right >= 0) {
            update();
            Insets insets2 = new Insets(insets1.top, insets1.left,
                    insets1.bottom, insets1.right);
            myInsets.top = insets.top;
            myInsets.left = insets.left;
            myInsets.bottom = insets.bottom;
            myInsets.right = insets.right;
            update(2406, 0, insets2);
        }
    }

    public Insets getCollapsedInsets() {
        return myCollapsedInsets;
    }

    public void setCollapsedInsets(Insets insets) {
        Insets insets1 = myCollapsedInsets;
        if (!insets1.equals(insets) && insets.top >= 0 && insets.left >= 0
                && insets.bottom >= 0 && insets.right >= 0) {
            update();
            Insets insets2 = new Insets(insets1.top, insets1.left,
                    insets1.bottom, insets1.right);
            myCollapsedInsets.top = insets.top;
            myCollapsedInsets.left = insets.left;
            myCollapsedInsets.bottom = insets.bottom;
            myCollapsedInsets.right = insets.right;
            update(2406, 0, insets2);
        }
    }

    public int getLabelSpot() {
        return myLabelSpot;
    }

    public void setLabelSpot(int i) {
        setLabelSpot(i, false);
    }

    private void setLabelSpot(int i, boolean flag) {
        int j = myLabelSpot;
        if (j != i) {
            myLabelSpot = i;
            update(2403, j, null);
            if (!flag) {
                layoutChildren(null);
            }
        }
    }

    public int getCollapsedLabelSpot() {
        return myCollapsedLabelSpot;
    }

    public void setCollapsedLabelSpot(int i) {
        setCollapsedLabelSpot(i, false);
    }

    private void setCollapsedLabelSpot(int i, boolean flag) {
        int j = myCollapsedLabelSpot;
        if (j != i) {
            myCollapsedLabelSpot = i;
            update(2408, j, null);
            if (!flag) {
                layoutChildren(null);
            }
        }
    }

    public boolean isExpanded() {
        return getState() == 0;
    }

    protected int getState() {
        return myState;
    }

    protected void setState(int i) {
        int j = myState;
        if (j != i) {
            myState = i;
            Rectangle rectangle = getBoundingRect();
            update(2413, j, new Rectangle(rectangle.x, rectangle.y,
                    rectangle.width, rectangle.height));
        }
    }

    public void setCollapsible(boolean flag) {
        boolean flag1 = (getInternalFlags() & 0x20000) != 0;
        if (flag1 != flag) {
            if (flag) {
                setInternalFlags(getInternalFlags() | 0x20000);
            } else {
                setInternalFlags(getInternalFlags() & 0xfffdffff);
            }
            update(2405, flag1 ? 1 : 0, null);
        }
    }

    public boolean isCollapsible() {
        return (getInternalFlags() & 0x20000) != 0;
    }

    public void setWasExpanded(boolean paramBoolean) {
        boolean bool = (getInternalFlags() & 0x80000) != 0;
        if (bool != paramBoolean) {
            if (paramBoolean) {
                setInternalFlags(getInternalFlags() | 0x80000);
            } else {
                setInternalFlags(getInternalFlags() & 0xFFF7FFFF);
            }
            update(2416, bool ? 1 : 0, null);
        }
    }

    public boolean isWasExpanded() {
        return (getInternalFlags() & 0x80000) != 0;
    }

    void setExpandedResizable(boolean flag) {
        boolean flag1 = (getInternalFlags() & 0x40000) != 0;
        if (flag1 != flag) {
            if (flag) {
                setInternalFlags(getInternalFlags() | 0x40000);
            } else {
                setInternalFlags(getInternalFlags() & 0xfffbffff);
            }
            update(2417, flag1 ? 1 : 0, null);
        }
    }

    boolean isExpandedResizable() {
        return (getInternalFlags() & 0x40000) != 0;
    }

    public void copyOldValueForUndo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2414 :
            case 2415 :
                if (!graphicviewerdocumentchangededit.isBeforeChanging()) {
                    GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit1 = graphicviewerdocumentchangededit
                            .findBeforeChangingEdit();
                    if (graphicviewerdocumentchangededit1 != null) {
                        graphicviewerdocumentchangededit
                                .setOldValue(graphicviewerdocumentchangededit1
                                        .getNewValue());
                    }
                }
                return;
        }
        super.copyOldValueForUndo(graphicviewerdocumentchangededit);
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2413 :
                graphicviewerdocumentchangededit.setNewValueInt(getState());
                return;

            case 2401 :
                graphicviewerdocumentchangededit
                        .setNewValue(getBackgroundColor());
                return;

            case 2402 :
                graphicviewerdocumentchangededit.setNewValue(getBorderPen());
                return;

            case 2403 :
                graphicviewerdocumentchangededit.setNewValueInt(getLabelSpot());
                return;

            case 2405 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isCollapsible());
                return;

            case 2406 :
                Insets insets = getInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets.top, insets.left, insets.bottom, insets.right));
                return;

            case 2407 :
                graphicviewerdocumentchangededit.setNewValue(new Double(
                        getOpacity()));
                return;

            case 2408 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getCollapsedLabelSpot());
                return;

            case 2409 :
                Insets insets1 = getCollapsedInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets1.top, insets1.left, insets1.bottom,
                        insets1.right));
                return;

            case 2410 :
                graphicviewerdocumentchangededit
                        .setNewValue(getCollapsedObject());
                return;

            case 2411 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;

            case 2412 :
                graphicviewerdocumentchangededit.setNewValue(getPort());
                return;

            case 2414 :
                graphicviewerdocumentchangededit.setNewValue(getSavedBounds()
                        .clone());
                return;

            case 2415 :
                graphicviewerdocumentchangededit.setNewValue(getSavedPaths()
                        .clone());
                return;

            case 2417 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isExpandedResizable());
                return;

            case 2416 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isWasExpanded());
                return;

            case 2404 :
            default :
                super.copyNewValueForRedo(graphicviewerdocumentchangededit);
                return;
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2413 :
                setState(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 2401 :
                setBackgroundColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2402 :
                setBorderPen((GraphicViewerPen) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2403 :
                setLabelSpot(
                        graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 2405 :
                setCollapsible(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 2406 :
                setInsets((Insets) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2407 :
                setOpacity(graphicviewerdocumentchangededit
                        .getValueDouble(flag));
                return;

            case 2408 :
                setCollapsedLabelSpot(
                        graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 2409 :
                setCollapsedInsets((Insets) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2410 :
                setCollapsedObject((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2411 :
                setLabel((GraphicViewerText) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2412 :
                setPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2414 :
                HashMap hashmap = (HashMap) graphicviewerdocumentchangededit
                        .getValue(flag);
                if (hashmap != null) {
                    myBoundsHashtable = (HashMap) hashmap.clone();
                } else {
                    myBoundsHashtable.clear();
                }
                return;

            case 2415 :
                HashMap hashmap1 = (HashMap) graphicviewerdocumentchangededit
                        .getValue(flag);
                if (hashmap1 != null) {
                    myPathsHashtable = (HashMap) hashmap1.clone();
                } else {
                    myPathsHashtable.clear();
                }
                return;

            case 2417 :
                setExpandedResizable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 2416 :
                setWasExpanded(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 2404 :
            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                return;
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("subgraphlabel")) {
            myLabel = (GraphicViewerText) obj;
        } else if (s.equals("subgraphhandle")) {
            myHandle = (GraphicViewerSubGraphHandle) obj;
        } else if (s.equals("subgraphport")) {
            myPort = (GraphicViewerPort) obj;
        } else if (s.equals("collapsedobject")) {
            myCollapsedObject = (GraphicViewerObject) obj;
        } else if (s.equals("borderpen")) {
            myBorderPen = (GraphicViewerPen) obj;
        } else if (s.equals("hashtableobjs")) {
            Integer integer = null;
            Iterator iterator = getSavedBounds().entrySet().iterator();
            do {
                if (!iterator.hasNext()) {
                    break;
                }
                Entry entry = (Entry) iterator.next();
                if (entry.getKey() instanceof Integer) {
                    Integer integer1 = (Integer) entry.getKey();
                    if (integer == null
                            || integer1.intValue() < integer.intValue()) {
                        integer = integer1;
                    }
                }
            } while (true);
            if (integer != null) {
                Rectangle rectangle = (Rectangle) getSavedBounds().get(integer);
                getSavedBounds().remove(integer);
                getSavedBounds().put(obj, rectangle);
            }
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSubGraph",
                            domelement);
            domelement1
                    .setAttribute("labelspot", Integer.toString(myLabelSpot));
            domelement1.setAttribute("collapsedlabelspot",
                    Integer.toString(myCollapsedLabelSpot));
            if (myBackgroundColor != null) {
                int i = myBackgroundColor.getRed();
                int j = myBackgroundColor.getGreen();
                int k = myBackgroundColor.getBlue();
                int l = myBackgroundColor.getAlpha();
                String s4 = "rgbalpha(" + Integer.toString(i) + ","
                        + Integer.toString(j) + "," + Integer.toString(k) + ","
                        + Integer.toString(l) + ")";
                domelement1.setAttribute("backgroundcolor", s4);
            }
            domelement1.setAttribute("opacity", Double.toString(getOpacity()));
            domelement1.setAttribute("expanded", isExpanded()
                    ? "true"
                    : "false");
            domelement1.setAttribute("wasexpanded", isWasExpanded()
                    ? "true"
                    : "false");
            domelement1.setAttribute("expandedresizable", isExpandedResizable()
                    ? "true"
                    : "false");
            domelement1.setAttribute("collapsible", isCollapsible()
                    ? "true"
                    : "false");
            domelement1.setAttribute("insets_top",
                    Integer.toString(getInsets().top));
            domelement1.setAttribute("insets_left",
                    Integer.toString(getInsets().left));
            domelement1.setAttribute("insets_bottom",
                    Integer.toString(getInsets().bottom));
            domelement1.setAttribute("insets_right",
                    Integer.toString(getInsets().right));
            domelement1.setAttribute("collapsed_insets_top",
                    Integer.toString(getCollapsedInsets().top));
            domelement1.setAttribute("collapsed_insets_left",
                    Integer.toString(getCollapsedInsets().left));
            domelement1.setAttribute("collapsed_insets_bottom",
                    Integer.toString(getCollapsedInsets().bottom));
            domelement1.setAttribute("collapsed_insets_right",
                    Integer.toString(getCollapsedInsets().right));
            if (myLabel != null) {
                domdoc.registerReferencingNode(domelement1, "subgraphlabel",
                        myLabel);
            }
            if (myHandle != null) {
                domdoc.registerReferencingNode(domelement1, "subgraphhandle",
                        myHandle);
            }
            if (myPort != null) {
                domdoc.registerReferencingNode(domelement1, "subgraphport",
                        myPort);
            }
            if (myCollapsedObject != null) {
                domdoc.registerReferencingNode(domelement1, "collapsedobject",
                        myCollapsedObject);
            }
            if (myBorderPen != null) {
                if (!domdoc.isRegisteredReference(myBorderPen)) {
                    domelement1.setAttribute("embeddedborderpen", "true");
                    IDomElement domelement2 = domdoc.createElement("g");
                    domelement1.appendChild(domelement2);
                    myBorderPen.SVGWriteObject(domdoc, domelement2);
                }
                domdoc.registerReferencingNode(domelement1, "borderpen",
                        myBorderPen);
            }
            String s = "";
            String s1 = "";
            String s2 = "";
            String s3 = "";
            for (Iterator iterator = getSavedBounds().entrySet().iterator(); iterator
                    .hasNext();) {
                Entry entry = (Entry) iterator.next();
                GraphicViewerObject graphicviewerobject = (GraphicViewerObject) entry
                        .getKey();
                Rectangle rectangle = (Rectangle) entry.getValue();
                domdoc.registerReferencingNode(domelement1, "hashtableobjs",
                        graphicviewerobject);
                if (s.length() > 0) {
                    s = s + " ";
                    s1 = s1 + " ";
                    s2 = s2 + " ";
                    s3 = s3 + " ";
                }
                s = s + Integer.toString(rectangle.x);
                s1 = s1 + Integer.toString(rectangle.y);
                s2 = s2 + Integer.toString(rectangle.width);
                s3 = s3 + Integer.toString(rectangle.height);
            }

            if (s.length() > 0) {
                domelement1.setAttribute("xpoints", s);
                domelement1.setAttribute("ypoints", s1);
                domelement1.setAttribute("wdimensions", s2);
                domelement1.setAttribute("hdimensions", s3);
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            myLabelSpot = Integer.parseInt(domelement1
                    .getAttribute("labelspot"));
            String s = domelement1.getAttribute("collapsedlabelspot");
            if (s.length() > 0) {
                myCollapsedLabelSpot = Integer.parseInt(s);
            }
            String s1 = domelement1.getAttribute("backgroundcolor");
            if (s1.length() > 0 && s1.startsWith("rgbalpha")) {
                int i = s1.indexOf("(") + 1;
                int j = s1.indexOf(",", i);
                String s4 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s6 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s8 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(")", i);
                String s10 = s1.substring(i, j);
                myBackgroundColor = new Color(Integer.parseInt(s4),
                        Integer.parseInt(s6), Integer.parseInt(s8),
                        Integer.parseInt(s10));
            }
            String s2 = domelement.getAttribute("opacity");
            if (s2.length() > 0) {
                myOpacity = Double.parseDouble(s2);
            }
            String s3 = domelement.getAttribute("expanded");
            if (s3.length() > 0) {
                setState(s3.equals("true") ? 0 : 1);
            }
            s3 = domelement.getAttribute("wasexpanded");
            if (s3.length() > 0) {
                setWasExpanded(s3.equals("true"));
            }
            s3 = domelement.getAttribute("expandedresizable");
            if (s3.length() > 0) {
                setExpandedResizable(s3.equals("true"));
            }
            s3 = domelement.getAttribute("collapsible");
            if (s3.length() > 0) {
                setCollapsible(s3.equals("true"));
            }
            String s5 = domelement.getAttribute("insets_top");
            if (s5.length() > 0) {
                myInsets.top = Integer.parseInt(s5);
            }
            String s7 = domelement.getAttribute("insets_left");
            if (s7.length() > 0) {
                myInsets.left = Integer.parseInt(s7);
            }
            String s9 = domelement.getAttribute("insets_bottom");
            if (s9.length() > 0) {
                myInsets.bottom = Integer.parseInt(s9);
            }
            String s11 = domelement.getAttribute("insets_right");
            if (s11.length() > 0) {
                myInsets.right = Integer.parseInt(s11);
            }
            String s12 = domelement.getAttribute("collapsed_insets_top");
            if (s12.length() > 0) {
                myCollapsedInsets.top = Integer.parseInt(s12);
            }
            String s13 = domelement.getAttribute("collapsed_insets_left");
            if (s13.length() > 0) {
                myCollapsedInsets.left = Integer.parseInt(s13);
            }
            String s14 = domelement.getAttribute("collapsed_insets_bottom");
            if (s14.length() > 0) {
                myCollapsedInsets.bottom = Integer.parseInt(s14);
            }
            String s15 = domelement.getAttribute("collapsed_insets_right");
            if (s15.length() > 0) {
                myCollapsedInsets.right = Integer.parseInt(s15);
            }
            String s16 = domelement1.getAttribute("subgraphlabel");
            domdoc.registerReferencingObject(this, "subgraphlabel", s16);
            String s17 = domelement1.getAttribute("subgraphhandle");
            domdoc.registerReferencingObject(this, "subgraphhandle", s17);
            String s18 = domelement1.getAttribute("subgraphport");
            domdoc.registerReferencingObject(this, "subgraphport", s18);
            String s19 = domelement1.getAttribute("collapsedobject");
            domdoc.registerReferencingObject(this, "collapsedobject", s19);
            if (domelement1.getAttribute("embeddedborderpen").equals("true")) {
                domdoc.SVGTraverseChildren(graphicviewerdocument, domelement1,
                        null, false);
            }
            String s20 = domelement1.getAttribute("borderpen");
            domdoc.registerReferencingObject(this, "borderpen", s20);
            String s22;
            for (String s21 = domelement1.getAttribute("hashtableobjs"); s21
                    .length() > 0; domdoc.registerReferencingObject(this,
                    "hashtableobjs", s22)) {
                int k = s21.indexOf(" ");
                if (k == -1) {
                    k = s21.length();
                }
                s22 = s21.substring(0, k);
                if (k >= s21.length()) {
                    s21 = "";
                } else {
                    s21 = s21.substring(k + 1);
                }
            }

            int l = 0;
            String s23 = domelement1.getAttribute("xpoints");
            String s24 = domelement1.getAttribute("ypoints");
            String s25 = domelement1.getAttribute("wdimensions");
            String s26 = domelement1.getAttribute("hdimensions");
            Rectangle rectangle;
            for (; s23.length() > 0 && s24.length() > 0; getSavedBounds().put(
                    new Integer(l++), rectangle)) {
                int i1 = s23.indexOf(" ");
                if (i1 == -1) {
                    i1 = s23.length();
                }
                String s27 = s23.substring(0, i1);
                if (i1 >= s23.length()) {
                    s23 = "";
                } else {
                    s23 = s23.substring(i1 + 1);
                }
                i1 = s24.indexOf(" ");
                if (i1 == -1) {
                    i1 = s24.length();
                }
                String s28 = s24.substring(0, i1);
                if (i1 >= s24.length()) {
                    s24 = "";
                } else {
                    s24 = s24.substring(i1 + 1);
                }
                i1 = s25.indexOf(" ");
                if (i1 == -1) {
                    i1 = s25.length();
                }
                String s29 = s25.substring(0, i1);
                if (i1 >= s25.length()) {
                    s25 = "";
                } else {
                    s25 = s25.substring(i1 + 1);
                }
                i1 = s26.indexOf(" ");
                if (i1 == -1) {
                    i1 = s26.length();
                }
                String s30 = s26.substring(0, i1);
                if (i1 >= s26.length()) {
                    s26 = "";
                } else {
                    s26 = s26.substring(i1 + 1);
                }
                rectangle = new Rectangle(Integer.parseInt(s27),
                        Integer.parseInt(s28), Integer.parseInt(s29),
                        Integer.parseInt(s30));
            }

            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }
}