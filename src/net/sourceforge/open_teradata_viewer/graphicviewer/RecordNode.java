/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2012, D. Campione
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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RecordNode extends GraphicViewerNode {

    private static final long serialVersionUID = -3214786918557761085L;

    public RecordNode() {
        myListArea = null;
        myHeader = null;
        myFooter = null;
    }

    public void initialize() {
        myListArea = new ListArea();
        myListArea.initialize();
        myListArea.setVertical(true);
        myListArea.setSelectable(false);
        myListArea.getRect().setSelectable(false);
        addObjectAtHead(myListArea);
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        RecordNode recordnode = (RecordNode) graphicviewerarea;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        recordnode.myListArea = (ListArea) graphicviewercopyenvironment
                .get(myListArea);
        recordnode.myHeader = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myHeader);
        recordnode.myFooter = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myFooter);
    }

    public ListArea getListArea() {
        return myListArea;
    }

    public GraphicViewerObject getHeader() {
        return myHeader;
    }

    public void setHeader(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myHeader;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null)
                removeObject(graphicviewerobject1);
            myHeader = graphicviewerobject;
            if (graphicviewerobject != null) {
                graphicviewerobject.setSelectable(false);
                addObjectAtHead(graphicviewerobject);
            }
            layoutChildren(graphicviewerobject);
            update(0x1273f, 0, graphicviewerobject1);
        }
    }

    public GraphicViewerObject getFooter() {
        return myFooter;
    }

    public void setFooter(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myFooter;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null)
                removeObject(graphicviewerobject1);
            myFooter = graphicviewerobject;
            if (graphicviewerobject != null) {
                graphicviewerobject.setSelectable(false);
                addObjectAtHead(graphicviewerobject);
            }
            layoutChildren(graphicviewerobject);
            update(0x12740, 0, graphicviewerobject1);
        }
    }

    protected Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        Dimension dimension = myListArea.getMinimumSize();
        if (getHeader() != null)
            dimension.height += getHeader().getHeight();
        if (getFooter() != null)
            dimension.height += getFooter().getHeight();
        return super.handleResize(graphics2d, graphicviewerview, rectangle,
                point, i, j, dimension.width, dimension.height);
    }

    public void setBoundingRect(int i, int j, int k, int l) {
        Dimension dimension = myListArea.getMinimumSize();
        if (getHeader() != null)
            dimension.height += getHeader().getHeight();
        if (getFooter() != null)
            dimension.height += getFooter().getHeight();
        super.setBoundingRect(i, j, Math.max(k, dimension.width),
                Math.max(l, dimension.height));
    }

    public void setVisible(boolean flag) {
        if (myListArea != null)
            myListArea.setVisible(flag);
        super.setVisible(flag);
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing() || myListArea == null)
            return;
        setInitializing(true);
        int i = getLeft();
        int j = getTop();
        int k = getWidth();
        int l = getHeight();
        int i1 = 0;
        if (getHeader() != null)
            i1 = getHeader().getHeight();
        int j1 = 0;
        if (getFooter() != null)
            j1 = getFooter().getHeight();
        myListArea.setBoundingRect(i, j + i1, k, l - i1 - j1);
        GraphicViewerRectangle graphicviewerrectangle = myListArea.getRect();
        int k1 = graphicviewerrectangle.getLeft() - myListArea.getLeft();
        @SuppressWarnings("unused")
        int l1 = graphicviewerrectangle.getTop() - myListArea.getTop();
        int i2 = graphicviewerrectangle.getWidth();
        int j2 = graphicviewerrectangle.getHeight();
        @SuppressWarnings("unused")
        int k2 = myListArea.getWidth() - i2;
        @SuppressWarnings("unused")
        int l2 = myListArea.getHeight() - j2;
        if (getHeader() != null)
            getHeader().setBoundingRect(i + k1, j, i2, i1);
        if (getFooter() != null)
            getFooter().setBoundingRect(i + k1, (j + l) - j1, i2, j1);
        setInitializing(false);
    }

    protected boolean geometryChangeChild(
            GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        if (isInitializing())
            return false;
        if (graphicviewerobject == myListArea
                && graphicviewerobject.getWidth() != rectangle.width) {
            int i = myListArea.getRect().getWidth();
            if (getHeader() != null)
                getHeader().setWidth(i);
            if (getFooter() != null)
                getFooter().setWidth(i);
            setWidth(i);
            return true;
        } else {
            return super.geometryChangeChild(graphicviewerobject, rectangle);
        }
    }

    public void addItem(GraphicViewerObject graphicviewerobject,
            GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1,
            GraphicViewerObject graphicviewerobject1) {
        if (graphicviewerport != null) {
            graphicviewerport.setValidSource(false);
            graphicviewerport.setValidDestination(true);
            if (myListArea.isVertical()) {
                graphicviewerport.setFromSpot(8);
                graphicviewerport.setToSpot(8);
            } else {
                graphicviewerport.setFromSpot(2);
                graphicviewerport.setToSpot(2);
            }
        }
        if (graphicviewerport1 != null) {
            graphicviewerport1.setValidSource(true);
            graphicviewerport1.setValidDestination(false);
            if (myListArea.isVertical()) {
                graphicviewerport1.setFromSpot(4);
                graphicviewerport1.setToSpot(4);
            } else {
                graphicviewerport1.setFromSpot(6);
                graphicviewerport1.setToSpot(6);
            }
        }
        myListArea.insertItem(getNumItems(), graphicviewerobject,
                graphicviewerport, graphicviewerport1, graphicviewerobject1);
    }

    public GraphicViewerPort getLeftPort(int i) {
        return (GraphicViewerPort) myListArea.getLeftPort(i);
    }

    public void setLeftPort(int i, GraphicViewerPort graphicviewerport) {
        if (graphicviewerport != null)
            if (myListArea.isVertical()) {
                graphicviewerport.setFromSpot(8);
                graphicviewerport.setToSpot(8);
            } else {
                graphicviewerport.setFromSpot(2);
                graphicviewerport.setToSpot(2);
            }
        myListArea.setLeftPort(i, graphicviewerport);
    }

    public GraphicViewerPort getRightPort(int i) {
        return (GraphicViewerPort) myListArea.getRightPort(i);
    }

    public void setRightPort(int i, GraphicViewerPort graphicviewerport) {
        if (graphicviewerport != null)
            if (myListArea.isVertical()) {
                graphicviewerport.setFromSpot(4);
                graphicviewerport.setToSpot(4);
            } else {
                graphicviewerport.setFromSpot(6);
                graphicviewerport.setToSpot(6);
            }
        myListArea.setRightPort(i, graphicviewerport);
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("footer"))
            myFooter = (GraphicViewerObject) obj;
        else if (s.equals("header"))
            myHeader = (GraphicViewerObject) obj;
        else if (s.equals("listarea"))
            myListArea = (ListArea) obj;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.RecordNode", domelement);
            if (myFooter != null)
                domdoc.registerReferencingNode(domelement1, "footer", myFooter);
            if (myHeader != null)
                domdoc.registerReferencingNode(domelement1, "header", myHeader);
            if (myListArea != null)
                domdoc.registerReferencingNode(domelement1, "listarea",
                        myListArea);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("footer");
            domdoc.registerReferencingObject(this, "footer", s);
            String s1 = domelement1.getAttribute("header");
            domdoc.registerReferencingObject(this, "header", s1);
            String s2 = domelement1.getAttribute("listarea");
            domdoc.registerReferencingObject(this, "listarea", s2);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public int getNumItems() {
        return myListArea.getNumItems();
    }

    public GraphicViewerObject getItem(int i) {
        return myListArea.getItem(i);
    }

    public void setItem(int i, GraphicViewerObject graphicviewerobject) {
        myListArea.setItem(i, graphicviewerobject);
    }

    public int findItem(GraphicViewerObject graphicviewerobject) {
        return myListArea.findItem(graphicviewerobject);
    }

    public void removeItem(int i) {
        myListArea.removeItem(i);
    }

    public GraphicViewerObject getIcon(int i) {
        return myListArea.getIcon(i);
    }

    public void setIcon(int i, GraphicViewerObject graphicviewerobject) {
        myListArea.setIcon(i, graphicviewerobject);
    }

    public boolean isVertical() {
        return myListArea.isVertical();
    }

    public void setVertical(boolean flag) {
        myListArea.setVertical(flag);
    }

    public boolean isScrollBarOnRight() {
        return myListArea.isScrollBarOnRight();
    }

    public void setScrollBarOnRight(boolean flag) {
        myListArea.setScrollBarOnRight(flag);
    }

    public GraphicViewerPen getLinePen() {
        return myListArea.getLinePen();
    }

    public void setLinePen(GraphicViewerPen graphicviewerpen) {
        myListArea.setLinePen(graphicviewerpen);
    }

    public Insets getInsets() {
        return myListArea.getInsets();
    }

    public void setInsets(Insets insets) {
        myListArea.setInsets(insets);
    }

    public int getSpacing() {
        return myListArea.getSpacing();
    }

    public void setSpacing(int i) {
        myListArea.setSpacing(i);
    }

    public int getAlignment() {
        return myListArea.getAlignment();
    }

    public void setAlignment(int i) {
        myListArea.setAlignment(i);
    }

    public int getIconAlignment() {
        return myListArea.getIconAlignment();
    }

    public int getIconSpacing() {
        return myListArea.getIconSpacing();
    }

    public void setIconSpacing(int i) {
        myListArea.setIconSpacing(i);
    }

    public void setIconAlignment(int i) {
        myListArea.setIconAlignment(i);
    }

    public int getFirstVisibleIndex() {
        return myListArea.getFirstVisibleIndex();
    }

    public void setFirstVisibleIndex(int i) {
        myListArea.setFirstVisibleIndex(i);
    }

    public int getLastVisibleIndex() {
        return myListArea.getLastVisibleIndex();
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75583 :
                graphicviewerdocumentchangededit.setNewValue(getHeader());
                return;

            case 75584 :
                graphicviewerdocumentchangededit.setNewValue(getFooter());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75583 :
                setHeader((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 75584 :
                setFooter((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public static final int HeaderChanged = 0x1273f;
    public static final int FooterChanged = 0x12740;
    private ListArea myListArea;
    private GraphicViewerObject myHeader;
    private GraphicViewerObject myFooter;
}