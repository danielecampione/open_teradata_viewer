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
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerLayer
        implements
            IGraphicViewerObjectCollection,
            Serializable {

    private static final long serialVersionUID = -7585755566426110456L;

    static final int ChangedNoClear = 1902;
    private GraphicViewerObjList myObjects;
    private GraphicViewerDocument myDocument;
    private GraphicViewerLayer myNextLayer = null;
    private GraphicViewerLayer myPrevLayer = null;
    boolean myVisible = true;
    float myTransparency = 1.0F;
    boolean myModifiable = true;
    Object myIdentifier = null;
    transient int myLayerIndex = 0;
    private transient ArrayList myCaches = null;
    private Rectangle myTempRect1 = new Rectangle(0, 0, 0, 0);
    private Rectangle myTempRect2 = new Rectangle(0, 0, 0, 0);

    void init(GraphicViewerDocument graphicviewerdocument) {
        myObjects = new GraphicViewerObjList(true);
        myDocument = graphicviewerdocument;
    }

    void insert(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        myNextLayer = graphicviewerlayer;
        myPrevLayer = graphicviewerlayer1;
        if (graphicviewerlayer != null) {
            graphicviewerlayer.myPrevLayer = this;
        }
        if (graphicviewerlayer1 != null) {
            graphicviewerlayer1.myNextLayer = this;
        }
    }

    void extract() {
        if (myPrevLayer != null) {
            myPrevLayer.myNextLayer = myNextLayer;
        }
        if (myNextLayer != null) {
            myNextLayer.myPrevLayer = myPrevLayer;
        }
    }

    public GraphicViewerDocument getDocument() {
        return myDocument;
    }

    public GraphicViewerLayer getNextLayer() {
        return myNextLayer;
    }

    public GraphicViewerLayer getPrevLayer() {
        return myPrevLayer;
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
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument()) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition = myObjects
                    .getFirstObjectPos();
            if (myObjects.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject) {
                return graphicviewerlistposition;
            }
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition2);
            GraphicViewerObject graphicviewerobject1 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.myObjects
                    .removeObjectAtPos(graphicviewerlistposition2);
            GraphicViewerListPosition graphicviewerlistposition4 = myObjects
                    .addObjectAtHead(graphicviewerobject);
            graphicviewerobject.setLayer(this, -1, null, graphicviewerobject);
            if (graphicviewerobject1 != null) {
                graphicviewerobject.update(10, 1, graphicviewerobject1);
            } else {
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            }
            return graphicviewerlistposition4;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                .addObjectAtHead(graphicviewerobject);
        graphicviewerobject.setLayer(this, 6, this, graphicviewerobject);
        if (graphicviewerobject.isTopLevel()) {
            insertIntoCache(graphicviewerobject, 6, null);
        }
        getDocument().updateDocumentSize(graphicviewerobject);
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        if (graphicviewerobject.getParent() != null) {
            return null;
        }
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument()) {
                return null;
            }
            GraphicViewerListPosition graphicviewerlistposition = myObjects
                    .getLastObjectPos();
            if (myObjects.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject) {
                return graphicviewerlistposition;
            }
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition2);
            GraphicViewerObject graphicviewerobject1 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.myObjects
                    .removeObjectAtPos(graphicviewerlistposition2);
            GraphicViewerListPosition graphicviewerlistposition4 = myObjects
                    .addObjectAtTail(graphicviewerobject);
            graphicviewerobject.setLayer(this, -1, null, graphicviewerobject);
            if (graphicviewerobject1 != null) {
                graphicviewerobject.update(10, 1, graphicviewerobject1);
            } else {
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            }
            return graphicviewerlistposition4;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                .addObjectAtTail(graphicviewerobject);
        graphicviewerobject.setLayer(this, 2, this, graphicviewerobject);
        if (graphicviewerobject.isTopLevel()) {
            insertIntoCache(graphicviewerobject, 2, null);
        }
        getDocument().updateDocumentSize(graphicviewerobject);
        return graphicviewerlistposition1;
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
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument()) {
                return null;
            }
            if (myObjects.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject) {
                return graphicviewerlistposition;
            }
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition1);
            GraphicViewerObject graphicviewerobject2 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.myObjects
                    .removeObjectAtPos(graphicviewerlistposition1);
            GraphicViewerListPosition graphicviewerlistposition4 = myObjects
                    .insertObjectBefore(graphicviewerlistposition,
                            graphicviewerobject);
            graphicviewerobject.setLayer(this, -1, null, graphicviewerobject);
            if (graphicviewerobject2 != null) {
                graphicviewerobject.update(10, 1, graphicviewerobject2);
            } else {
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            }
            return graphicviewerlistposition4;
        }
        GraphicViewerObject graphicviewerobject1 = myObjects
                .getObjectAtPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = myObjects
                .insertObjectBefore(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.setLayer(this, 8, graphicviewerobject1,
                graphicviewerobject);
        if (graphicviewerobject.isTopLevel()) {
            insertIntoCache(graphicviewerobject, 8, graphicviewerobject1);
        }
        getDocument().updateDocumentSize(graphicviewerobject);
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
        if (graphicviewerobject.getView() != null) {
            return null;
        }
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument()) {
                return null;
            }
            if (myObjects.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject) {
                return graphicviewerlistposition;
            }
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition1);
            GraphicViewerObject graphicviewerobject2 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.myObjects
                    .removeObjectAtPos(graphicviewerlistposition1);
            GraphicViewerListPosition graphicviewerlistposition4 = myObjects
                    .insertObjectAfter(graphicviewerlistposition,
                            graphicviewerobject);
            graphicviewerobject.setLayer(this, -1, null, graphicviewerobject);
            if (graphicviewerobject2 != null) {
                graphicviewerobject.update(10, 1, graphicviewerobject2);
            } else {
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            }
            return graphicviewerlistposition4;
        }
        GraphicViewerObject graphicviewerobject1 = myObjects
                .getObjectAtPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = myObjects
                .insertObjectAfter(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.setLayer(this, 4, graphicviewerobject1,
                graphicviewerobject);
        if (graphicviewerobject.isTopLevel()) {
            insertIntoCache(graphicviewerobject, 4, graphicviewerobject1);
        }
        getDocument().updateDocumentSize(graphicviewerobject);
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
        if (graphicviewerobject.getLayer() != this) {
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
        GraphicViewerListPosition graphicviewerlistposition1 = myObjects
                .getNextObjectPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = myObjects
                .getPrevObjectPos(graphicviewerlistposition);
        GraphicViewerObject graphicviewerobject = myObjects
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject != null) {
            if (graphicviewerobject.isTopLevel()) {
                removeFromCache(graphicviewerobject);
            }
            GraphicViewerObject graphicviewerobject1 = myObjects
                    .getObjectAtPos(graphicviewerlistposition1);
            if (graphicviewerobject1 == null) {
                graphicviewerobject.setLayer(null, 2,
                        graphicviewerobject.getLayer(), graphicviewerobject);
            } else {
                GraphicViewerObject graphicviewerobject2 = myObjects
                        .getObjectAtPos(graphicviewerlistposition2);
                if (graphicviewerobject2 == null) {
                    graphicviewerobject
                            .setLayer(null, 6, graphicviewerobject.getLayer(),
                                    graphicviewerobject);
                } else {
                    Object aobj[] = new Object[3];
                    aobj[0] = graphicviewerobject.getLayer();
                    aobj[1] = graphicviewerobject2;
                    aobj[2] = graphicviewerobject1;
                    graphicviewerobject.setLayer(null, 0, ((Object) (aobj)),
                            graphicviewerobject);
                }
            }
        }
        return graphicviewerobject;
    }

    public void paint(Graphics2D paramGraphics2D,
            GraphicViewerView paramGraphicViewerView, Rectangle paramRectangle) {
        Rectangle localRectangle1 = myTempRect1;
        Rectangle localRectangle2 = paramGraphicViewerView.getViewRect();
        GraphicViewerLayerCache localGraphicViewerLayerCache = findCache(paramGraphicViewerView);
        Object localObject;
        if ((localGraphicViewerLayerCache != null)
                && (localGraphicViewerLayerCache.getRect()
                        .equals(localRectangle2))) {
            localObject = localGraphicViewerLayerCache.getObjects();
            int i = ((ArrayList) localObject).size();
            for (int j = 0; j < i; j++) {
                GraphicViewerObject localGraphicViewerObject2 = (GraphicViewerObject) ((ArrayList) localObject)
                        .get(j);
                if (localGraphicViewerObject2.isVisible()) {
                    Rectangle localRectangle4 = localGraphicViewerObject2
                            .getBoundingRect();
                    localRectangle1.x = localRectangle4.x;
                    localRectangle1.y = localRectangle4.y;
                    localRectangle1.width = localRectangle4.width;
                    localRectangle1.height = localRectangle4.height;
                    localGraphicViewerObject2
                            .expandRectByPenWidth(localRectangle1);
                    if (localRectangle1.intersects(paramRectangle)) {
                        localGraphicViewerObject2.paint(paramGraphics2D,
                                paramGraphicViewerView);
                    }
                }
            }
        } else {
            GraphicViewerObject localGraphicViewerObject1;
            Rectangle localRectangle3;
            if (cacheWanted(paramGraphicViewerView)) {
                if (localGraphicViewerLayerCache == null) {
                    localGraphicViewerLayerCache = new GraphicViewerLayerCache(
                            paramGraphicViewerView);
                    getCaches().add(localGraphicViewerLayerCache);
                } else {
                    localGraphicViewerLayerCache.reset();
                }
                localGraphicViewerLayerCache.setRect(localRectangle2);
                localObject = getFirstObjectPos();
                while (localObject != null) {
                    localGraphicViewerObject1 = getObjectAtPos((GraphicViewerListPosition) localObject);
                    localObject = getNextObjectPosAtTop((GraphicViewerListPosition) localObject);
                    localRectangle3 = localGraphicViewerObject1
                            .getBoundingRect();
                    localRectangle1.x = localRectangle3.x;
                    localRectangle1.y = localRectangle3.y;
                    localRectangle1.width = localRectangle3.width;
                    localRectangle1.height = localRectangle3.height;
                    localGraphicViewerObject1
                            .expandRectByPenWidth(localRectangle1);
                    if ((localGraphicViewerObject1.isVisible())
                            && (localRectangle1.intersects(paramRectangle))) {
                        localGraphicViewerObject1.paint(paramGraphics2D,
                                paramGraphicViewerView);
                    }
                    if (localRectangle1.intersects(localRectangle2)) {
                        localGraphicViewerLayerCache.getObjects().add(
                                localGraphicViewerObject1);
                    }
                }
            } else {
                localObject = getFirstObjectPos();
                while (localObject != null) {
                    localGraphicViewerObject1 = getObjectAtPos((GraphicViewerListPosition) localObject);
                    localObject = getNextObjectPosAtTop((GraphicViewerListPosition) localObject);
                    if (localGraphicViewerObject1.isVisible()) {
                        localRectangle3 = localGraphicViewerObject1
                                .getBoundingRect();
                        localRectangle1.x = localRectangle3.x;
                        localRectangle1.y = localRectangle3.y;
                        localRectangle1.width = localRectangle3.width;
                        localRectangle1.height = localRectangle3.height;
                        localGraphicViewerObject1
                                .expandRectByPenWidth(localRectangle1);
                        if (localRectangle1.intersects(paramRectangle)) {
                            localGraphicViewerObject1.paint(paramGraphics2D,
                                    paramGraphicViewerView);
                        }
                    }
                }
            }
        }
    }

    ArrayList getCaches() {
        if (myCaches == null) {
            myCaches = new ArrayList();
        }
        return myCaches;
    }

    private boolean cacheWanted(GraphicViewerView graphicviewerview) {
        return GraphicViewerDocument.myCaching
                && !graphicviewerview.isPrinting();
    }

    private GraphicViewerLayerCache findCache(
            GraphicViewerView graphicviewerview) {
        ArrayList arraylist = getCaches();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            GraphicViewerLayerCache localGraphicViewerLayerCache = (GraphicViewerLayerCache) arraylist
                    .get(j1);
            if (localGraphicViewerLayerCache.getView() == graphicviewerview) {
                return localGraphicViewerLayerCache;
            }
        }

        return null;
    }

    private GraphicViewerLayerCache findCache(Point point) {
        GraphicViewerLayerCache localObject = null;
        ArrayList arraylist = getCaches();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            GraphicViewerLayerCache localGraphicViewerLayerCache = (GraphicViewerLayerCache) arraylist
                    .get(j1);
            if (localGraphicViewerLayerCache.getRect().contains(point)
                    && (localObject == null || localGraphicViewerLayerCache
                            .getObjects().size() < localObject.getObjects()
                            .size())) {
                localObject = localGraphicViewerLayerCache;
            }
        }

        return localObject;
    }

    void resetCache() {
        myCaches = new ArrayList();
    }

    void updateCache(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle) {
        Rectangle rectangle1 = myTempRect1;
        rectangle1.x = rectangle.x;
        rectangle1.y = rectangle.y;
        rectangle1.width = rectangle.width;
        rectangle1.height = rectangle.height;
        graphicviewerobject.expandRectByPenWidth(rectangle1);
        Rectangle rectangle2 = graphicviewerobject.getBoundingRect();
        Rectangle rectangle3 = myTempRect2;
        rectangle3.x = rectangle2.x;
        rectangle3.y = rectangle2.y;
        rectangle3.width = rectangle2.width;
        rectangle3.height = rectangle2.height;
        graphicviewerobject.expandRectByPenWidth(rectangle3);
        ArrayList arraylist = getCaches();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            GraphicViewerLayerCache localGraphicViewerLayerCache = (GraphicViewerLayerCache) arraylist
                    .get(j1);
            boolean flag = localGraphicViewerLayerCache.getRect().intersects(
                    rectangle1);
            boolean flag1 = localGraphicViewerLayerCache.getRect().intersects(
                    rectangle3);
            if (!flag
                    && flag1
                    && !localGraphicViewerLayerCache.getObjects().contains(
                            graphicviewerobject)) {
                localGraphicViewerLayerCache.getObjects().add(
                        graphicviewerobject);
            }
        }
    }

    void insertIntoCache(GraphicViewerObject graphicviewerobject, int i1,
            Object obj) {
        Rectangle rectangle = graphicviewerobject.getBoundingRect();
        Rectangle rectangle1 = myTempRect2;
        rectangle1.x = rectangle.x;
        rectangle1.y = rectangle.y;
        rectangle1.width = rectangle.width;
        rectangle1.height = rectangle.height;
        graphicviewerobject.expandRectByPenWidth(rectangle1);
        ArrayList arraylist = getCaches();
        int j1 = arraylist.size();
        for (int k1 = 0; k1 < j1; k1++) {
            GraphicViewerLayerCache localGraphicViewerLayerCache = (GraphicViewerLayerCache) arraylist
                    .get(k1);
            boolean flag = localGraphicViewerLayerCache.getRect().intersects(
                    rectangle1);
            if (!flag) {
                continue;
            }
            if (i1 == 2) {
                localGraphicViewerLayerCache.getObjects().add(
                        graphicviewerobject);
                continue;
            }
            if (i1 == 6) {
                localGraphicViewerLayerCache.getObjects().add(0,
                        graphicviewerobject);
            } else {
                localGraphicViewerLayerCache.getObjects().add(
                        graphicviewerobject);
            }
        }
    }

    void removeFromCache(GraphicViewerObject graphicviewerobject) {
        Rectangle rectangle = graphicviewerobject.getBoundingRect();
        Rectangle rectangle1 = myTempRect1;
        rectangle1.x = rectangle.x;
        rectangle1.y = rectangle.y;
        rectangle1.width = rectangle.width;
        rectangle1.height = rectangle.height;
        graphicviewerobject.expandRectByPenWidth(rectangle1);
        ArrayList arraylist = getCaches();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            GraphicViewerLayerCache localGraphicViewerLayerCache = (GraphicViewerLayerCache) arraylist
                    .get(j1);
            boolean flag = localGraphicViewerLayerCache.getRect().intersects(
                    rectangle1);
            if (flag) {
                localGraphicViewerLayerCache.getObjects().remove(
                        graphicviewerobject);
            }
        }

    }

    public GraphicViewerObject pickObject(Point paramPoint, boolean paramBoolean) {
        if (!isVisible()) {
            return null;
        }
        GraphicViewerLayerCache localGraphicViewerLayerCache = findCache(paramPoint);
        Object localObject;
        if (localGraphicViewerLayerCache != null) {
            localObject = localGraphicViewerLayerCache.getObjects();
            int i = ((ArrayList) localObject).size();
            for (int j = i - 1; j >= 0; j--) {
                GraphicViewerObject localGraphicViewerObject3 = (GraphicViewerObject) ((ArrayList) localObject)
                        .get(j);
                GraphicViewerObject localGraphicViewerObject4 = localGraphicViewerObject3
                        .pick(paramPoint, paramBoolean);
                if (localGraphicViewerObject4 != null) {
                    return localGraphicViewerObject4;
                }
            }
        } else {
            localObject = getLastObjectPos();
            while (localObject != null) {
                GraphicViewerObject localGraphicViewerObject1 = getObjectAtPos((GraphicViewerListPosition) localObject);
                localObject = getPrevObjectPos((GraphicViewerListPosition) localObject);
                GraphicViewerObject localGraphicViewerObject2 = localGraphicViewerObject1
                        .pick(paramPoint, paramBoolean);
                if (localGraphicViewerObject2 != null) {
                    return localGraphicViewerObject2;
                }
            }
        }
        return null;
    }

    public ArrayList pickObjects(Point paramPoint, boolean paramBoolean,
            ArrayList paramArrayList, int paramInt) {
        if (paramArrayList == null) {
            paramArrayList = new ArrayList();
        }
        if (paramArrayList.size() >= paramInt) {
            return paramArrayList;
        }
        if (!isVisible()) {
            return paramArrayList;
        }
        GraphicViewerLayerCache localGraphicViewerLayerCache = findCache(paramPoint);
        Object localObject;
        if (localGraphicViewerLayerCache != null) {
            localObject = localGraphicViewerLayerCache.getObjects();
            int i = ((ArrayList) localObject).size();
            for (int j = i - 1; j >= 0; j--) {
                GraphicViewerObject localGraphicViewerObject3 = (GraphicViewerObject) ((ArrayList) localObject)
                        .get(j);
                if ((localGraphicViewerObject3 instanceof GraphicViewerArea)) {
                    ((GraphicViewerArea) localGraphicViewerObject3)
                            .pickObjects(paramPoint, paramBoolean,
                                    paramArrayList, paramInt);
                } else {
                    GraphicViewerObject localGraphicViewerObject4 = localGraphicViewerObject3
                            .pick(paramPoint, paramBoolean);
                    if (localGraphicViewerObject4 != null) {
                        if (!paramArrayList.contains(localGraphicViewerObject3)) {
                            paramArrayList.add(localGraphicViewerObject3);
                        }
                        if (paramArrayList.size() >= paramInt) {
                            return paramArrayList;
                        }
                    }
                }
            }
        } else {
            localObject = getLastObjectPos();
            while (localObject != null) {
                GraphicViewerObject localGraphicViewerObject1 = getObjectAtPos((GraphicViewerListPosition) localObject);
                localObject = getPrevObjectPos((GraphicViewerListPosition) localObject);
                if ((localGraphicViewerObject1 instanceof GraphicViewerArea)) {
                    ((GraphicViewerArea) localGraphicViewerObject1)
                            .pickObjects(paramPoint, paramBoolean,
                                    paramArrayList, paramInt);
                } else {
                    GraphicViewerObject localGraphicViewerObject2 = localGraphicViewerObject1
                            .pick(paramPoint, paramBoolean);
                    if (localGraphicViewerObject2 != null) {
                        if (!paramArrayList.contains(localGraphicViewerObject1)) {
                            paramArrayList.add(localGraphicViewerObject1);
                        }
                        if (paramArrayList.size() >= paramInt) {
                            return paramArrayList;
                        }
                    }
                }
            }
        }
        return paramArrayList;
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
        if (graphicviewerobject.getLayer() == this) {
            return myObjects.findObject(graphicviewerobject);
        } else {
            return null;
        }
    }

    int getIndexOf(GraphicViewerObject graphicviewerobject) {
        int i1 = 0;
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition)) {
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            if (graphicviewerobject1 == graphicviewerobject) {
                return i1;
            }
            i1++;
        }

        return -1;
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
        getDocument().fireUpdate(1902, 0, this, 0, arraylist);
        for (int i1 = 0; i1 < arraylist.size(); i1++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                    .get(i1);
            boolean flag1 = graphicviewerobject.getLayer() != null
                    && (graphicviewerobject.getLayer() != this || !graphicviewerobject
                            .isTopLevel());
            if (flag1) {
                GraphicViewerArea.setAllNoClear(graphicviewerobject, true);
                graphicviewerobject.getLayer()
                        .removeObject(graphicviewerobject);
            }
            addObjectAtTail(graphicviewerobject);
            if (flag1) {
                GraphicViewerArea.setAllNoClear(graphicviewerobject, false);
            }
        }

        getDocument().fireUpdate(1902, 0, this, 1, arraylist);
        if (flag && getDocument() != null) {
            GraphicViewerSubGraphBase.reparentAllLinksToSubGraphs(arraylist,
                    true, graphicviewerlayer);
        }
        return arraylist;
    }

    public void removeAll() {
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = getFirstObjectPos()) {
            removeObjectAtPos(graphicviewerlistposition);
        }

    }

    public boolean isVisible() {
        return myVisible;
    }

    public void setVisible(boolean flag) {
        setVisibleInternal(flag, false);
    }

    void setVisibleInternal(boolean flag, boolean flag1) {
        boolean flag2 = myVisible;
        if (flag2 != flag) {
            myVisible = flag;
            getDocument().fireUpdate(213, 0, this, flag2 ? 1 : 0, null);
            if (!flag1 && !myVisible) {
                GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
                do {
                    if (graphicviewerlistposition == null) {
                        break;
                    }
                    GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = getNextObjectPos(graphicviewerlistposition);
                    if (graphicviewerobject instanceof GraphicViewerControl) {
                        GraphicViewerControl graphicviewercontrol = (GraphicViewerControl) graphicviewerobject;
                        graphicviewercontrol.ownerChange(getDocument(), null,
                                null);
                    }
                } while (true);
            }
        }
    }

    public float getTransparency() {
        return myTransparency;
    }

    public void setTransparency(float f1) {
        float f2 = myTransparency;
        if (f2 != f1) {
            myTransparency = f1;
            getDocument().fireUpdate(215, 0, this, 0, new Float(f2));
        }
    }

    public boolean isModifiable() {
        return myModifiable && getDocument().isModifiable();
    }

    public void setModifiable(boolean flag) {
        boolean flag1 = myModifiable;
        if (flag1 != flag) {
            myModifiable = flag;
            getDocument().fireUpdate(214, 0, this, flag1 ? 1 : 0, null);
        }
    }

    public Object getIdentifier() {
        return myIdentifier;
    }

    public void setIdentifier(Object obj) {
        Object obj1 = myIdentifier;
        if (obj1 != obj) {
            myIdentifier = obj;
            getDocument().fireUpdate(217, 0, this, 0, obj1);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static final class GraphicViewerLayerCache {

        private transient GraphicViewerView myView = null;
        private transient ArrayList myObjects = null;
        private transient Rectangle myRect = null;

        GraphicViewerLayerCache(GraphicViewerView paramGraphicViewerView) {
            myView = paramGraphicViewerView;
            myObjects = new ArrayList();
            reset();
        }

        void reset() {
            myObjects.clear();
            myRect = new Rectangle(0, 0, 0, 0);
        }

        GraphicViewerView getView() {
            return myView;
        }

        ArrayList getObjects() {
            return myObjects;
        }

        Rectangle getRect() {
            return myRect;
        }

        void setRect(Rectangle rectangle) {
            myRect.x = rectangle.x;
            myRect.y = rectangle.y;
            myRect.width = rectangle.width;
            myRect.height = rectangle.height;
        }
    }
}