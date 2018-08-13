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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.event.MouseEvent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerOverview extends GraphicViewerView {

    private static final long serialVersionUID = 8739515364572335798L;

    private GraphicViewerView myObserved = null;
    private GraphicViewerOverviewRectangle myOverviewRect = null;

    public GraphicViewerOverview() {
        setHidingDisabledScrollbars(true);
        setInternalMouseActions(2);
        setScale(0.125D);
    }

    public void removeNotify() {
        removeListeners();
        myObserved = null;
        super.removeNotify();
    }

    private void removeListeners() {
        if (myObserved != null && myOverviewRect != null) {
            myObserved.getDocument().removeDocumentListener(this);
            myObserved.removeViewListener(myOverviewRect);
            myObserved.getCanvas().removeComponentListener(myOverviewRect);
        }
    }

    public void setObserved(GraphicViewerView graphicviewerview) {
        if (graphicviewerview instanceof GraphicViewerOverview) {
            return;
        }
        GraphicViewerView graphicviewerview1 = myObserved;
        if (graphicviewerview1 != graphicviewerview) {
            removeListeners();
            myObserved = graphicviewerview;
            if (myObserved != null) {
                if (myOverviewRect == null) {
                    myOverviewRect = new GraphicViewerOverviewRectangle(
                            myObserved.getViewPosition(),
                            myObserved.getExtentSize());
                    addObjectAtTail(myOverviewRect);
                } else {
                    myOverviewRect.setBoundingRect(myObserved.getViewRect());
                }
                myObserved.getDocument().addDocumentListener(this);
                myObserved.addViewListener(myOverviewRect);
                myObserved.getCanvas().addComponentListener(myOverviewRect);
                firePropertyChange("observed", graphicviewerview1,
                        graphicviewerview);
                updateView();
            }
        }
    }

    public GraphicViewerView getObserved() {
        return myObserved;
    }

    public boolean isDropFlavorAcceptable(
            DropTargetDragEvent droptargetdragevent) {
        return false;
    }

    public int computeAcceptableDrop(DropTargetDragEvent droptargetdragevent) {
        return 0;
    }

    public GraphicViewerOverviewRectangle getOverviewRect() {
        return myOverviewRect;
    }

    public GraphicViewerObject pickDocObject(Point point, boolean flag) {
        if (getOverviewRect() != null && getOverviewRect().isPointInObj(point)) {
            return getOverviewRect();
        } else {
            return null;
        }
    }

    public void selectInBox(Rectangle rectangle) {
    }

    public void doBackgroundClick(int i, Point point, Point point1) {
        if (getOverviewRect() != null) {
            Rectangle rectangle = getOverviewRect().getBoundingRect();
            getOverviewRect().setTopLeft(point.x - rectangle.width / 2,
                    point.y - rectangle.height / 2);
        }
    }

    public OTVDocument getDocument() {
        if (getObserved() != null) {
            return getObserved().getDocument();
        } else {
            return super.getDocument();
        }
    }

    public Dimension getDocumentSize() {
        if (getDocument() != null) {
            return getDocument().getDocumentSize();
        } else {
            return new Dimension();
        }
    }

    public boolean isIncludingNegativeCoords() {
        if (getObserved() != null) {
            return getObserved().isIncludingNegativeCoords();
        } else {
            return false;
        }
    }

    public String getToolTipText(MouseEvent mouseevent) {
        if (getObserved() == null) {
            return null;
        }
        Point point = getMouseEventPoint(mouseevent);
        convertViewToDoc(point);
        for (Object obj = getObserved().pickDocObject(point, false); obj != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            String s = ((GraphicViewerObject) (obj)).getToolTipText();
            if (s != null) {
                return s;
            }
        }

        return null;
    }
}