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
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerSelection
        implements
            IGraphicViewerObjectSimpleCollection,
            Transferable,
            Serializable {

    private static final long serialVersionUID = -5123147854138539136L;

    private GraphicViewerObjList mySelectedObjects = new GraphicViewerObjList();
    private HashMap myHandles = new HashMap();
    private transient GraphicViewerView myView = null;
    private GraphicViewerPen myBoundingHandlePen = null;
    private GraphicViewerPen myResizeHandlePen = GraphicViewerPen.black;
    private GraphicViewerBrush myResizeHandleBrush = null;
    private boolean myResizeHandleSizeInViewCoords = false;

    public GraphicViewerSelection() {
        myView = null;
    }

    public GraphicViewerSelection(GraphicViewerView graphicviewerview) {
        myView = graphicviewerview;
    }

    public final GraphicViewerView getView() {
        return myView;
    }

    public int getNumObjects() {
        return mySelectedObjects.getNumObjects();
    }

    public boolean isEmpty() {
        return mySelectedObjects.isEmpty();
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return mySelectedObjects.getFirstObjectPos();
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return mySelectedObjects.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return mySelectedObjects.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        return mySelectedObjects.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerObject getPrimarySelection() {
        GraphicViewerListPosition graphicviewerlistposition = mySelectedObjects
                .getFirstObjectPos();
        if (graphicviewerlistposition == null) {
            return null;
        } else {
            return mySelectedObjects.getObjectAtPos(graphicviewerlistposition);
        }
    }

    public GraphicViewerObject selectObject(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (getPrimarySelection() == graphicviewerobject
                && getNumObjects() == 1) {
            return graphicviewerobject;
        } else {
            clearSelection();
            return extendSelection(graphicviewerobject);
        }
    }

    public GraphicViewerObject extendSelection(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null) {
            return null;
        }
        if (!isSelected(graphicviewerobject)) {
            addToSelection(graphicviewerobject);
        }
        return graphicviewerobject;
    }

    public GraphicViewerObject toggleSelection(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null) {
            return null;
        }
        if (isSelected(graphicviewerobject)) {
            removeFromSelection(graphicviewerobject);
        } else {
            addToSelection(graphicviewerobject);
        }
        return graphicviewerobject;
    }

    public void clearSelection(GraphicViewerObject paramGraphicViewerObject) {
        if (paramGraphicViewerObject == null) {
            clearSelection();
            return;
        }
        paramGraphicViewerObject = paramGraphicViewerObject.redirectSelection();
        if (paramGraphicViewerObject == null) {
            return;
        }
        if (isSelected(paramGraphicViewerObject)) {
            removeFromSelection(paramGraphicViewerObject);
        }
    }

    public void clearSelection() {
        GraphicViewerView localGraphicViewerView = getView();
        int i = (localGraphicViewerView != null) && (getNumObjects() > 1)
                ? 1
                : 0;
        if (i != 0) {
            localGraphicViewerView.fireUpdate(37, 0, null);
        }
        for (GraphicViewerListPosition localGraphicViewerListPosition = mySelectedObjects
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = mySelectedObjects
                .getFirstObjectPos()) {
            GraphicViewerObject localGraphicViewerObject = mySelectedObjects
                    .getObjectAtPos(localGraphicViewerListPosition);
            mySelectedObjects.removeObjectAtPos(localGraphicViewerListPosition);
            localGraphicViewerObject.lostSelection(this);
            if (localGraphicViewerView != null) {
                localGraphicViewerView.fireUpdate(21, 0,
                        localGraphicViewerObject);
            }
            myHandles.remove(localGraphicViewerObject);
        }
        if (i != 0) {
            localGraphicViewerView.fireUpdate(38, 0, null);
        }
    }

    public boolean isInSelection(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return false;
        }
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null) {
            return false;
        } else {
            return isSelected(graphicviewerobject);
        }
    }

    public boolean isSelected(GraphicViewerObject graphicviewerobject) {
        return myHandles.containsKey(graphicviewerobject);
    }

    private void addToSelection(GraphicViewerObject graphicviewerobject) {
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null
                && graphicviewerobject.getView() != graphicviewerview
                && graphicviewerobject.getDocument() != graphicviewerview
                        .getDocument()) {
            return;
        }
        mySelectedObjects.addObjectAtTail(graphicviewerobject);
        myHandles.put(graphicviewerobject, null);
        graphicviewerobject.gainedSelection(this);
        if (graphicviewerview != null) {
            graphicviewerview.fireUpdate(20, 0, graphicviewerobject);
        }
    }

    private void removeFromSelection(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = getPrimarySelection();
        mySelectedObjects.removeObject(graphicviewerobject);
        GraphicViewerView graphicviewerview = getView();
        graphicviewerobject.lostSelection(this);
        if (graphicviewerview != null) {
            graphicviewerview.fireUpdate(21, 0, graphicviewerobject);
        }
        myHandles.remove(graphicviewerobject);
        if (graphicviewerobject1 == graphicviewerobject) {
            GraphicViewerObject graphicviewerobject2 = getPrimarySelection();
            if (graphicviewerobject2 != null) {
                graphicviewerobject2.lostSelection(this);
                if (graphicviewerview != null) {
                    graphicviewerview.fireUpdate(21, 1, graphicviewerobject2);
                }
                graphicviewerobject2.gainedSelection(this);
                if (graphicviewerview != null) {
                    graphicviewerview.fireUpdate(20, 1, graphicviewerobject2);
                }
            }
        }
    }

    public GraphicViewerObject[] toArray() {
        int i = getNumObjects();
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[i];
        int j = 0;
        for (GraphicViewerListPosition graphicviewerlistposition = mySelectedObjects
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = mySelectedObjects
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = mySelectedObjects
                    .getNextObjectPos(graphicviewerlistposition);
            agraphicviewerobject[j++] = graphicviewerobject;
        }

        return agraphicviewerobject;
    }

    public void addArray(GraphicViewerObject agraphicviewerobject[]) {
        for (int i = 0; i < agraphicviewerobject.length; i++) {
            extendSelection(agraphicviewerobject[i]);
        }

    }

    public void addCollection(ArrayList arraylist) {
        for (int i = 0; i < arraylist.size(); i++) {
            extendSelection((GraphicViewerObject) arraylist.get(i));
        }

    }

    public void addCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPos(graphicviewerlistposition);
            extendSelection(graphicviewerobject);
        }

    }

    public void clearSelectionHandles(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject != null) {
            graphicviewerobject.hideSelectionHandles(this);
        } else {
            for (GraphicViewerListPosition graphicviewerlistposition = mySelectedObjects
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                graphicviewerobject = mySelectedObjects
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = mySelectedObjects
                        .getNextObjectPos(graphicviewerlistposition);
                graphicviewerobject.hideSelectionHandles(this);
            }

        }
    }

    public void restoreSelectionHandles(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject != null) {
            if (isSelected(graphicviewerobject)
                    && graphicviewerobject.isVisible()
                    && (graphicviewerobject.getLayer() == null || graphicviewerobject
                            .getLayer().isVisible())) {
                graphicviewerobject.showSelectionHandles(this);
            }
        } else {
            for (GraphicViewerListPosition graphicviewerlistposition = mySelectedObjects
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                graphicviewerobject = mySelectedObjects
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = mySelectedObjects
                        .getNextObjectPos(graphicviewerlistposition);
                if (graphicviewerobject.isVisible()
                        && (graphicviewerobject.getLayer() == null || graphicviewerobject
                                .getLayer().isVisible())) {
                    graphicviewerobject.showSelectionHandles(this);
                } else {
                    graphicviewerobject.hideSelectionHandles(this);
                }
            }

        }
    }

    public void showHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = myHandles.get(graphicviewerobject);
        if (obj == null) {
            return;
        }
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null) {
            if (obj instanceof ArrayList) {
                ArrayList arraylist = (ArrayList) obj;
                for (int i = 0; i < arraylist.size(); i++) {
                    GraphicViewerHandle graphicviewerhandle1 = (GraphicViewerHandle) arraylist
                            .get(i);
                    graphicviewerhandle1.setVisible(true);
                }

            } else {
                GraphicViewerHandle graphicviewerhandle = (GraphicViewerHandle) obj;
                graphicviewerhandle.setVisible(true);
            }
        }
    }

    public void hideHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = myHandles.get(graphicviewerobject);
        if (obj == null) {
            return;
        }
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null) {
            if (obj instanceof ArrayList) {
                ArrayList arraylist = (ArrayList) obj;
                for (int i = 0; i < arraylist.size(); i++) {
                    GraphicViewerHandle graphicviewerhandle1 = (GraphicViewerHandle) arraylist
                            .get(i);
                    graphicviewerhandle1.setVisible(false);
                }

            } else {
                GraphicViewerHandle graphicviewerhandle = (GraphicViewerHandle) obj;
                graphicviewerhandle.setVisible(false);
            }
        }
    }

    public GraphicViewerHandle createBoundingHandle(
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerView graphicviewerview = getView();
        Rectangle rectangle = graphicviewerview == null ? new Rectangle(0, 0,
                0, 0) : graphicviewerview.getTempRectangle();
        Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
        rectangle.x = rectangle1.x;
        rectangle.y = rectangle1.y;
        rectangle.width = rectangle1.width;
        rectangle.height = rectangle1.height;
        rectangle.x--;
        rectangle.y--;
        rectangle.width += 2;
        rectangle.height += 2;
        GraphicViewerHandle graphicviewerhandle = new GraphicViewerHandle(
                rectangle, 0);
        graphicviewerhandle.setHandleType(-1);
        graphicviewerhandle.setSelectable(false);
        Color color;
        if (graphicviewerview != null) {
            if (getPrimarySelection() == graphicviewerobject) {
                color = graphicviewerview.getPrimarySelectionColor();
            } else {
                color = graphicviewerview.getSecondarySelectionColor();
            }
        } else {
            color = GraphicViewerBrush.ColorBlack;
        }
        if (myBoundingHandlePen == null
                || !myBoundingHandlePen.getColor().equals(color)) {
            myBoundingHandlePen = GraphicViewerPen.make(65535, 2, color);
        }
        graphicviewerhandle.setPen(myBoundingHandlePen);
        graphicviewerhandle.setBrush(null);
        addHandle(graphicviewerobject, graphicviewerhandle);
        return graphicviewerhandle;
    }

    public boolean isResizeHandleSizeInViewCoords() {
        return myResizeHandleSizeInViewCoords;
    }

    public void setResizeHandleSizeInViewCoords(boolean flag) {
        myResizeHandleSizeInViewCoords = flag;
    }

    public GraphicViewerHandle allocateResizeHandle(
            GraphicViewerObject graphicviewerobject, int i, int j, int k) {
        GraphicViewerView graphicviewerview = getView();
        Rectangle rectangle = graphicviewerview == null ? new Rectangle(0, 0,
                0, 0) : graphicviewerview.getTempRectangle();
        rectangle.width = GraphicViewerHandle.getDefaultHandleWidth();
        rectangle.height = GraphicViewerHandle.getDefaultHandleHeight();
        if (isResizeHandleSizeInViewCoords() && graphicviewerview != null
                && graphicviewerview.getScale() != 1.0D) {
            Dimension dimension = graphicviewerview.getTempDimension();
            dimension.width = rectangle.width;
            dimension.height = rectangle.height;
            graphicviewerview.convertViewToDoc(dimension);
            rectangle.width = dimension.width;
            if (rectangle.width < 2) {
                rectangle.width = 2;
            }
            rectangle.height = dimension.height;
            if (rectangle.height < 2) {
                rectangle.height = 2;
            }
        }
        rectangle.x = i - rectangle.width / 2;
        rectangle.y = j - rectangle.height / 2;
        GraphicViewerHandle graphicviewerhandle = new GraphicViewerHandle(
                rectangle, 0);
        return graphicviewerhandle;
    }

    public GraphicViewerHandle createResizeHandle(
            GraphicViewerObject graphicviewerobject, int i, int j, int k,
            boolean flag) {
        GraphicViewerView graphicviewerview = getView();
        GraphicViewerHandle graphicviewerhandle = allocateResizeHandle(
                graphicviewerobject, i, j, k);
        graphicviewerhandle.setHandleType(k);
        if (k == -1) {
            graphicviewerhandle.setSelectable(false);
        } else {
            graphicviewerhandle.setSelectable(true);
        }
        Color color;
        if (graphicviewerview != null) {
            if (getPrimarySelection() == graphicviewerobject) {
                color = graphicviewerview.getPrimarySelectionColor();
            } else {
                color = graphicviewerview.getSecondarySelectionColor();
            }
        } else {
            color = GraphicViewerBrush.ColorBlack;
        }
        if (flag) {
            graphicviewerhandle.setPen(myResizeHandlePen);
            if (myResizeHandleBrush == null
                    || !myResizeHandleBrush.getColor().equals(color)) {
                myResizeHandleBrush = GraphicViewerBrush.make(65535, color);
            }
            graphicviewerhandle.setBrush(myResizeHandleBrush);
        } else {
            if (myBoundingHandlePen == null
                    || !myBoundingHandlePen.getColor().equals(color)) {
                myBoundingHandlePen = GraphicViewerPen.make(65535, 2, color);
            }
            graphicviewerhandle.setPen(myBoundingHandlePen);
            graphicviewerhandle.setBrush(null);
        }
        addHandle(graphicviewerobject, graphicviewerhandle);
        return graphicviewerhandle;
    }

    public void addHandle(GraphicViewerObject graphicviewerobject,
            GraphicViewerHandle graphicviewerhandle) {
        graphicviewerhandle.setHandleFor(graphicviewerobject);
        Object obj = myHandles.get(graphicviewerobject);
        if (obj == null) {
            myHandles.put(graphicviewerobject, graphicviewerhandle);
        } else if (obj instanceof ArrayList) {
            ArrayList arraylist = (ArrayList) obj;
            arraylist.add(graphicviewerhandle);
        } else {
            ArrayList arraylist1 = new ArrayList();
            arraylist1.add(obj);
            arraylist1.add(graphicviewerhandle);
            myHandles.put(graphicviewerobject, arraylist1);
        }
        if (getView() != null) {
            getView().addObjectAtHead(graphicviewerhandle);
        }
    }

    public void deleteHandles(GraphicViewerObject paramGraphicViewerObject) {
        Object localObject1 = myHandles.get(paramGraphicViewerObject);
        if (localObject1 == null) {
            return;
        }
        GraphicViewerView localGraphicViewerView = getView();
        if (localGraphicViewerView != null) {
            Object localObject2;
            if ((localObject1 instanceof ArrayList)) {
                localObject2 = (ArrayList) localObject1;
                for (int i = 0; i < ((ArrayList) localObject2).size(); i++) {
                    GraphicViewerHandle localGraphicViewerHandle = (GraphicViewerHandle) ((ArrayList) localObject2)
                            .get(i);
                    localGraphicViewerView
                            .removeObject(localGraphicViewerHandle);
                }
            } else {
                localObject2 = (GraphicViewerHandle) localObject1;
                localGraphicViewerView
                        .removeObject((GraphicViewerObject) localObject2);
            }
        }
        myHandles.put(paramGraphicViewerObject, null);
    }

    public int getNumHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = myHandles.get(graphicviewerobject);
        if (obj == null) {
            return 0;
        }
        if (obj instanceof ArrayList) {
            ArrayList arraylist = (ArrayList) obj;
            return arraylist.size();
        } else {
            return 1;
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            return graphicviewerdocument.getTransferDataFlavors();
        } else {
            return new DataFlavor[0];
        }
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            return graphicviewerdocument.isDataFlavorSupported(dataflavor);
        } else {
            return false;
        }
    }

    public synchronized Object getTransferData(DataFlavor dataflavor)
            throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(dataflavor)) {
            return this;
        } else {
            throw new UnsupportedFlavorException(dataflavor);
        }
    }

    private GraphicViewerDocument getDocument() {
        GraphicViewerDocument graphicviewerdocument = null;
        if (getView() != null) {
            graphicviewerdocument = getView().getDocument();
        } else {
            GraphicViewerListPosition graphicviewerlistposition = mySelectedObjects
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null) {
                    break;
                }
                GraphicViewerObject graphicviewerobject = mySelectedObjects
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = mySelectedObjects
                        .getNextObjectPos(graphicviewerlistposition);
                graphicviewerdocument = graphicviewerobject.getDocument();
                if (graphicviewerdocument != null) {
                    break;
                }
                GraphicViewerView graphicviewerview = graphicviewerobject
                        .getView();
                if (graphicviewerview != null) {
                    graphicviewerdocument = graphicviewerview.getDocument();
                }
            } while (graphicviewerdocument == null);
        }
        return graphicviewerdocument;
    }
}