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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerArea extends GraphicViewerObject
        implements
            IGraphicViewerObjectCollection {

    private static final long serialVersionUID = 6303819593066819843L;

    public static final int ChangedPickableBackground = 1900;
    private static final int ChangedNoClear = 1901;
    static final int flagPickableBackground = 32768;
    private GraphicViewerObjList myChildren = new GraphicViewerObjList(true);
    private GraphicViewerListPosition myCurrentListPosition = null;

    public GraphicViewerArea() {
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerArea graphicviewerarea = (GraphicViewerArea) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerarea != null) {
            boolean flag = graphicviewerarea.isInitializing();
            graphicviewerarea.setInitializing(true);
            copyChildren(graphicviewerarea, graphicviewercopyenvironment);
            graphicviewerarea.setInitializing(flag);
        }
        return graphicviewerarea;
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            graphicviewerarea.addObjectAtTailInternal(
                    graphicviewercopyenvironment.copy(graphicviewerobject),
                    true);
        }
    }

    void setLayer(GraphicViewerLayer graphicviewerlayer, int j, Object obj,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlayer != null) {
            super.setLayer(graphicviewerlayer, j, obj, graphicviewerobject);
            for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
                graphicviewerobject1.setLayer(graphicviewerlayer, -1, null,
                        graphicviewerobject);
            }
        } else {
            for (GraphicViewerListPosition graphicviewerlistposition1 = getFirstObjectPos(); graphicviewerlistposition1 != null;) {
                GraphicViewerObject graphicviewerobject2 = getObjectAtPos(graphicviewerlistposition1);
                graphicviewerlistposition1 = getNextObjectPos(graphicviewerlistposition1);
                graphicviewerobject2.setLayer(null, -1, null,
                        graphicviewerobject);
            }

            super.setLayer(null, j, obj, graphicviewerobject);
        }
    }

    void setView(GraphicViewerView graphicviewerview,
            GraphicViewerObject graphicviewerobject) {
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            graphicviewerobject1
                    .setView(graphicviewerview, graphicviewerobject);
        }

        super.setView(graphicviewerview, graphicviewerobject);
    }

    public void setPickableBackground(boolean flag) {
        boolean flag1 = (getInternalFlags() & 0x8000) != 0;
        if (flag1 != flag) {
            if (flag) {
                setInternalFlags(getInternalFlags() | 0x8000);
            } else {
                setInternalFlags(getInternalFlags() & 0xffff7fff);
            }
            update(1900, flag1 ? 1 : 0, null);
        }
    }

    public boolean isPickableBackground() {
        return (getInternalFlags() & 0x8000) != 0;
    }

    public GraphicViewerObject pick(Point point, boolean flag) {
        if (!isVisible()) {
            return null;
        }
        if (!getBoundingRect().contains(point.x, point.y)) {
            return null;
        }
        GraphicViewerObject graphicviewerobject = pickObject(point, flag);
        if (graphicviewerobject != null) {
            return graphicviewerobject;
        }
        if (isPickableBackground()) {
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
        }
        return null;
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        if (!getBoundingRect().contains(point.x, point.y)) {
            return null;
        }
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

    public ArrayList pickObjects(Point point, boolean flag,
            ArrayList arraylist, int j) {
        if (arraylist == null) {
            arraylist = new ArrayList();
        }
        if (arraylist.size() >= j) {
            return arraylist;
        }
        if (!isVisible()) {
            return arraylist;
        }
        GraphicViewerObject graphicviewerobject = pickObject(point, flag);
        if (graphicviewerobject != null
                && !arraylist.contains(graphicviewerobject)) {
            arraylist.add(graphicviewerobject);
        }
        return arraylist;
    }

    public boolean getNearestIntersectionPoint(int j, int k, int l, int i1,
            Point point) {
        double d = 1E+021D;
        int j1 = l;
        int k1 = i1;
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject.isVisible()
                    && graphicviewerobject.getNearestIntersectionPoint(j, k, l,
                            i1, point)) {
                double d1 = (point.x - j) * (point.x - j) + (point.y - k)
                        * (point.y - k);
                if (d1 < d) {
                    d = d1;
                    j1 = point.x;
                    k1 = point.y;
                }
            }
        } while (true);
        point.x = j1;
        point.y = k1;
        return d < 1E+021D;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        IDomElement domelement1;
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerArea",
                            domelement);
        }
        domelement1 = domelement;
        if (domdoc.GraphicViewerXMLOutputEnabled() || domdoc.SVGOutputEnabled()) {
            domelement1 = domdoc.createElement("g");
            domelement.appendChild(domelement1);
        }
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        IDomElement domelement2 = domelement1;
        while (graphicviewerlistposition != null) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (domdoc.GraphicViewerXMLOutputEnabled()
                    || domdoc.SVGOutputEnabled()) {
                domelement2 = domdoc.createElement("g");
                domelement1.appendChild(domelement2);
            }
            boolean flag = domdoc.isGenerateSVG();
            if (!graphicviewerobject.isVisible()) {
                domdoc.setGenerateSVG(false);
            }
            graphicviewerobject.SVGWriteObject(domdoc, domelement2);
            domdoc.setGenerateSVG(flag);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            IDomElement domelement2 = domelement1.getNextSiblingElement();
            domdoc.SVGTraverseChildren(graphicviewerdocument, domelement2,
                    this, true);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement2.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject.isVisible()) {
                graphicviewerobject.paint(graphics2d, graphicviewerview);
            }
        } while (true);
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        int j = rectangle.x;
        int k = rectangle.y;
        int l = j + rectangle.width;
        int i1 = k + rectangle.height;
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
            rectangle.x = rectangle1.x;
            rectangle.y = rectangle1.y;
            rectangle.width = rectangle1.width;
            rectangle.height = rectangle1.height;
            graphicviewerobject.expandRectByPenWidth(rectangle);
            j = Math.min(j, rectangle.x);
            k = Math.min(k, rectangle.y);
            l = Math.max(l, rectangle.x + rectangle.width);
            i1 = Math.max(i1, rectangle.y + rectangle.height);
        }

        rectangle.x = j;
        rectangle.y = k;
        rectangle.width = l - j;
        rectangle.height = i1 - k;
    }

    private boolean mightShrink(Rectangle rectangle, int j, int k, int l, int i1) {
        Rectangle rectangle1 = getBoundingRect();
        if (rectangle1.x == rectangle.x && j > rectangle1.x) {
            return true;
        }
        if (rectangle1.y == rectangle.y && k > rectangle1.y) {
            return true;
        }
        int j1 = rectangle1.x + rectangle1.width;
        int k1 = rectangle1.y + rectangle1.height;
        int l1 = j + l;
        int i2 = k + i1;
        if (j1 == rectangle.x + rectangle.width && l1 < j1) {
            return true;
        }
        return k1 == rectangle.y + rectangle.height && i2 < k1;
    }

    protected Rectangle computeBoundingRect() {
        Rectangle rectangle = null;
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (!graphicviewerobject.isBoundingRectInvalid()) {
                if (rectangle == null) {
                    Rectangle rectangle1 = graphicviewerobject
                            .getBoundingRect();
                    rectangle = new Rectangle(rectangle1.x, rectangle1.y,
                            rectangle1.width, rectangle1.height);
                } else {
                    rectangle.add(graphicviewerobject.getBoundingRect());
                }
            }
        } while (true);
        return rectangle;
    }

    protected void geometryChange(Rectangle rectangle) {
        Rectangle rectangle1 = getBoundingRect();
        if (rectangle.width == rectangle1.width
                && rectangle.height == rectangle1.height) {
            moveChildren(rectangle);
        } else {
            rescaleChildren(rectangle);
            layoutChildren(null);
            setBoundingRectInvalid(true);
        }
    }

    protected boolean geometryChangeChild(
            GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        layoutChildren(graphicviewerobject);
        setBoundingRectInvalid(true);
        return true;
    }

    protected void moveChildren(Rectangle rectangle) {
        Rectangle rectangle1 = getBoundingRect();
        if (rectangle.equals(rectangle1)) {
            return;
        }
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerLink) {
                int j = graphicviewerobject.getLeft() - rectangle.x;
                int l = graphicviewerobject.getTop() - rectangle.y;
                graphicviewerobject.setBoundingRect(rectangle1.x + j,
                        rectangle1.y + l, graphicviewerobject.getWidth(),
                        graphicviewerobject.getHeight());
            }
        } while (true);
        graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)
                    && !(graphicviewerobject1.getPartner() instanceof GraphicViewerLabeledLink)) {
                int k = graphicviewerobject1.getLeft() - rectangle.x;
                int i1 = graphicviewerobject1.getTop() - rectangle.y;
                graphicviewerobject1.setBoundingRect(rectangle1.x + k,
                        rectangle1.y + i1, graphicviewerobject1.getWidth(),
                        graphicviewerobject1.getHeight());
            }
        } while (true);
    }

    protected void rescaleChildren(Rectangle rectangle) {
        Rectangle rectangle1 = getBoundingRect();
        if (rectangle.equals(rectangle1)) {
            return;
        }
        double d = 1.0D;
        if (rectangle.width != 0) {
            d = (double) rectangle1.width / (double) rectangle.width;
        }
        double d1 = 1.0D;
        if (rectangle.height != 0) {
            d1 = (double) rectangle1.height / (double) rectangle.height;
        }
        GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject.isAutoRescale()
                    && (graphicviewerobject instanceof GraphicViewerLink)) {
                Rectangle rectangle2 = graphicviewerobject.getBoundingRect();
                int j = rectangle1.x
                        + (int) Math.rint((double) (rectangle2.x - rectangle.x)
                                * d);
                int l = rectangle1.y
                        + (int) Math.rint((double) (rectangle2.y - rectangle.y)
                                * d1);
                int j1 = (int) Math.rint((double) rectangle2.width * d);
                int l1 = (int) Math.rint((double) rectangle2.height * d1);
                graphicviewerobject.setBoundingRect(j, l, j1, l1);
            }
        } while (true);
        graphicviewerlistposition = getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject1.isAutoRescale()
                    && !(graphicviewerobject1 instanceof GraphicViewerLink)) {
                Rectangle rectangle3 = graphicviewerobject1.getBoundingRect();
                int k = rectangle1.x
                        + (int) Math.rint((double) (rectangle3.x - rectangle.x)
                                * d);
                int i1 = rectangle1.y
                        + (int) Math.rint((double) (rectangle3.y - rectangle.y)
                                * d1);
                int k1 = (int) Math.rint((double) rectangle3.width * d);
                int i2 = (int) Math.rint((double) rectangle3.height * d1);
                graphicviewerobject1.setBoundingRect(k, i1, k1, i2);
            }
        } while (true);
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
    }

    public int getNumObjects() {
        return myChildren.getNumObjects();
    }

    public boolean isEmpty() {
        return myChildren.isEmpty();
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        return addObjectAtHeadInternal(graphicviewerobject, false);
    }

    GraphicViewerListPosition addObjectAtHeadInternal(
            GraphicViewerObject graphicviewerobject, boolean flag) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            if (graphicviewerobject.getParent() != this) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition = myChildren
                    .getFirstObjectPos();
            if (myChildren.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject) {
                return graphicviewerlistposition;
            }
            graphicviewerlistposition = myChildren
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition2 = myChildren
                    .getNextObjectPos(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = myChildren
                    .getObjectAtPos(graphicviewerlistposition2);
            myChildren.removeObjectAtPos(graphicviewerlistposition);
            GraphicViewerListPosition graphicviewerlistposition3 = myChildren
                    .addObjectAtHead(graphicviewerobject);
            if (graphicviewerobject1 != null) {
                graphicviewerobject.update(10, 1, graphicviewerobject1);
            } else {
                graphicviewerobject.update(10, 0, this);
            }
            return graphicviewerlistposition3;
        }
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = myChildren
                .addObjectAtHead(graphicviewerobject);
        graphicviewerobject.setParent(this);
        GraphicViewerLayer graphicviewerlayer = getLayer();
        if (graphicviewerlayer != null) {
            graphicviewerobject.setLayer(graphicviewerlayer, 6, this,
                    graphicviewerobject);
        } else {
            GraphicViewerView graphicviewerview = getView();
            if (graphicviewerview != null) {
                graphicviewerobject.setView(graphicviewerview,
                        graphicviewerobject);
            }
        }
        if (!flag) {
            geometryChangeChild(graphicviewerobject,
                    graphicviewerobject.getBoundingRect());
            setBoundingRectInvalid(true);
        }
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        return addObjectAtTailInternal(graphicviewerobject, false);
    }

    GraphicViewerListPosition addObjectAtTailInternal(
            GraphicViewerObject graphicviewerobject, boolean flag) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            if (graphicviewerobject.getParent() != this) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition = myChildren
                    .getLastObjectPos();
            if (myChildren.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject) {
                return graphicviewerlistposition;
            }
            graphicviewerlistposition = myChildren
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition2 = myChildren
                    .getNextObjectPos(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = myChildren
                    .getObjectAtPos(graphicviewerlistposition2);
            myChildren.removeObjectAtPos(graphicviewerlistposition);
            GraphicViewerListPosition graphicviewerlistposition3 = myChildren
                    .addObjectAtTail(graphicviewerobject);
            if (graphicviewerobject1 != null) {
                graphicviewerobject.update(10, 1, graphicviewerobject1);
            } else {
                graphicviewerobject.update(10, 0, this);
            }
            return graphicviewerlistposition3;
        }
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = myChildren
                .addObjectAtTail(graphicviewerobject);
        graphicviewerobject.setParent(this);
        GraphicViewerLayer graphicviewerlayer = getLayer();
        if (graphicviewerlayer != null) {
            graphicviewerobject.setLayer(graphicviewerlayer, 2, this,
                    graphicviewerobject);
        } else {
            GraphicViewerView graphicviewerview = getView();
            if (graphicviewerview != null) {
                graphicviewerobject.setView(graphicviewerview,
                        graphicviewerobject);
            }
        }
        if (!flag) {
            geometryChangeChild(graphicviewerobject,
                    graphicviewerobject.getBoundingRect());
            setBoundingRectInvalid(true);
        }
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        return insertObjectBeforeInternal(graphicviewerlistposition,
                graphicviewerobject, false);
    }

    GraphicViewerListPosition insertObjectBeforeInternal(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject, boolean flag) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            if (graphicviewerobject.getParent() != this) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition1 = myChildren
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition1 != null) {
                GraphicViewerListPosition graphicviewerlistposition3 = myChildren
                        .getNextObjectPos(graphicviewerlistposition1);
                GraphicViewerObject graphicviewerobject1 = myChildren
                        .getObjectAtPos(graphicviewerlistposition3);
                myChildren.removeObjectAtPos(graphicviewerlistposition1);
                GraphicViewerListPosition graphicviewerlistposition4 = myChildren
                        .insertObjectBefore(graphicviewerlistposition,
                                graphicviewerobject);
                if (graphicviewerobject1 != null) {
                    graphicviewerobject.update(10, 1, graphicviewerobject1);
                } else {
                    graphicviewerobject.update(10, 0, this);
                }
                return graphicviewerlistposition4;
            }
        }
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition2 = myChildren
                .insertObjectBefore(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.setParent(this);
        GraphicViewerLayer graphicviewerlayer = getLayer();
        if (graphicviewerlayer != null) {
            GraphicViewerObject graphicviewerobject2 = myChildren
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerobject.setLayer(graphicviewerlayer, 8,
                    graphicviewerobject2, graphicviewerobject);
        } else {
            GraphicViewerView graphicviewerview = getView();
            if (graphicviewerview != null) {
                graphicviewerobject.setView(graphicviewerview,
                        graphicviewerobject);
            }
        }
        if (!flag) {
            geometryChangeChild(graphicviewerobject,
                    graphicviewerobject.getBoundingRect());
            setBoundingRectInvalid(true);
        }
        return graphicviewerlistposition2;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        return insertObjectAfterInternal(graphicviewerlistposition,
                graphicviewerobject, false);
    }

    GraphicViewerListPosition insertObjectAfterInternal(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject, boolean flag) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            if (graphicviewerobject.getParent() != this) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition1 = myChildren
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition1 != null) {
                GraphicViewerListPosition graphicviewerlistposition3 = myChildren
                        .getNextObjectPos(graphicviewerlistposition1);
                GraphicViewerObject graphicviewerobject1 = myChildren
                        .getObjectAtPos(graphicviewerlistposition3);
                myChildren.removeObjectAtPos(graphicviewerlistposition1);
                GraphicViewerListPosition graphicviewerlistposition4 = myChildren
                        .insertObjectAfter(graphicviewerlistposition,
                                graphicviewerobject);
                if (graphicviewerobject1 != null) {
                    graphicviewerobject.update(10, 1, graphicviewerobject1);
                } else {
                    graphicviewerobject.update(10, 0, this);
                }
                return graphicviewerlistposition4;
            }
        }
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getLayer() != null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition2 = myChildren
                .insertObjectAfter(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.setParent(this);
        GraphicViewerLayer graphicviewerlayer = getLayer();
        if (graphicviewerlayer != null) {
            GraphicViewerObject graphicviewerobject2 = myChildren
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerobject.setLayer(graphicviewerlayer, 4,
                    graphicviewerobject2, graphicviewerobject);
        } else {
            GraphicViewerView graphicviewerview = getView();
            if (graphicviewerview != null) {
                graphicviewerobject.setView(graphicviewerview,
                        graphicviewerobject);
            }
        }
        if (!flag) {
            geometryChangeChild(graphicviewerobject,
                    graphicviewerobject.getBoundingRect());
            setBoundingRectInvalid(true);
        }
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
        if (graphicviewerobject.getParent() != this) {
            return;
        }
        GraphicViewerListPosition graphicviewerlistposition = myChildren
                .findObject(graphicviewerobject);
        if (graphicviewerlistposition != null) {
            removeObjectAtPos(graphicviewerlistposition);
        }
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerListPosition graphicviewerlistposition1 = myChildren
                .getNextObjectPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = myChildren
                .getPrevObjectPos(graphicviewerlistposition);
        GraphicViewerObject graphicviewerobject = myChildren
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject != null) {
            if (!isEmpty()) {
                Rectangle rectangle = getBoundingRect();
                if (mightShrink(rectangle, (rectangle.x + rectangle.width) / 2,
                        (rectangle.y + rectangle.height) / 2, 0, 0)) {
                    setBoundingRectInvalid(true);
                }
            }
            if (graphicviewerobject.getLayer() != null) {
                GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition1);
                if (graphicviewerobject1 == null) {
                    graphicviewerobject.setLayer(null, 2, this,
                            graphicviewerobject);
                } else {
                    GraphicViewerObject graphicviewerobject2 = getObjectAtPos(graphicviewerlistposition2);
                    if (graphicviewerobject2 == null) {
                        graphicviewerobject.setLayer(null, 6, this,
                                graphicviewerobject);
                    } else {
                        Object aobj[] = new Object[3];
                        aobj[0] = this;
                        aobj[1] = graphicviewerobject2;
                        aobj[2] = graphicviewerobject1;
                        graphicviewerobject.setLayer(null, 0,
                                ((Object) (aobj)), graphicviewerobject);
                    }
                }
            } else if (graphicviewerobject.getView() != null) {
                graphicviewerobject.setView(null, graphicviewerobject);
            }
            geometryChangeChild(graphicviewerobject,
                    graphicviewerobject.getBoundingRect());
            setBoundingRectInvalid(true);
            graphicviewerobject.setParent(null);
        }
        return graphicviewerobject;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return myChildren.getFirstObjectPos();
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return myChildren.getLastObjectPos();
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myChildren.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myChildren.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myChildren.getPrevObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myChildren.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() == this) {
            return myChildren.findObject(graphicviewerobject);
        } else {
            return null;
        }
    }

    void setCurrentListPosition(
            GraphicViewerListPosition graphicviewerlistposition) {
        myCurrentListPosition = graphicviewerlistposition;
    }

    GraphicViewerListPosition getCurrentListPosition() {
        return myCurrentListPosition;
    }

    public final ArrayList addCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            boolean flag, GraphicViewerLayer graphicviewerlayer) {
        ArrayList arraylist = new ArrayList();
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            arraylist.add(graphicviewerobject);
        }

        return addCollection(arraylist, flag, graphicviewerlayer);
    }

    public ArrayList addCollection(ArrayList arraylist, boolean flag,
            GraphicViewerLayer graphicviewerlayer) {
        update(1901, 0, arraylist);
        for (int j = 0; j < arraylist.size(); j++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                    .get(j);
            if (isChildOf(graphicviewerobject) || this == graphicviewerobject) {
                continue;
            }
            boolean flag1 = graphicviewerobject.getLayer() != null
                    && graphicviewerobject.getParent() != this;
            if (flag1) {
                setAllNoClear(graphicviewerobject, true);
                graphicviewerobject.getLayer()
                        .removeObject(graphicviewerobject);
            }
            addObjectAtTail(graphicviewerobject);
            if (flag1) {
                setAllNoClear(graphicviewerobject, false);
            }
        }

        update(1901, 1, arraylist);
        if (flag && getDocument() != null) {
            GraphicViewerSubGraphBase.reparentAllLinksToSubGraphs(arraylist,
                    true, graphicviewerlayer);
        }
        return arraylist;
    }

    static void setAllNoClear(GraphicViewerObject graphicviewerobject,
            boolean flag) {
        if (graphicviewerobject instanceof GraphicViewerPort) {
            GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
            graphicviewerport.setNoClearLinks(flag);
        } else if (graphicviewerobject instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
            graphicviewerlink.setNoClearPorts(flag);
        } else if (graphicviewerobject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerarea
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                setAllNoClear(graphicviewerobject1, flag);
            }

        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1900 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isPickableBackground());
                return;

            case 1901 :
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1900 :
                setPickableBackground(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 1901 :
                ArrayList arraylist = (ArrayList) graphicviewerdocumentchangededit
                        .getOldValue();
                boolean flag1 = flag
                        ? graphicviewerdocumentchangededit.getOldValueInt() == 1
                        : graphicviewerdocumentchangededit.getOldValueInt() == 0;
                for (int j = 0; j < arraylist.size(); j++) {
                    GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                            .get(j);
                    setAllNoClear(graphicviewerobject, flag1);
                }

                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }
}