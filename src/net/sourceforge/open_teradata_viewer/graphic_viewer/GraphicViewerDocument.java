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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerDocument
        implements
            IGraphicViewerObjectCollection,
            Transferable,
            Serializable,
            IGraphicViewerXMLSaveRestore {

    private static final long serialVersionUID = -3038095570271130435L;

    public static final int CyclesAllowAll = 0;
    public static final int CyclesNotDirected = 1;
    public static final int CyclesNotDirectedFast = 2;
    public static final int CyclesNotUndirected = 3;
    public static final int CyclesDestinationTree = 4;
    public static final int CyclesSourceTree = 5;
    static boolean myCaching = false;
    private GraphicViewerLayer myDefaultLayer;
    private GraphicViewerLayer myLinksLayer;
    private GraphicViewerLayer myFirstLayer;
    private GraphicViewerLayer myLastLayer;
    private Dimension myDocumentSize;
    private Point myDocumentTopLeft;
    private Color myPaperColor;
    private boolean myModifiable;
    private transient boolean mySuspendUpdates;
    private transient boolean mySkipsUndoManager;
    private static transient DataFlavor myStandardFlavor = null;
    private static transient DataFlavor myFlavors[] = null;
    private transient ArrayList myDocumentListeners;
    private transient GraphicViewerDocumentEvent myDocumentEvent;
    private transient GraphicViewerUndoManager myUndoManager;
    private transient GraphicViewerPositionArray myPositions;
    private transient GraphicViewerObject mySkippedAvoidable;
    private boolean myMaintainsPartID;
    private int myLastPartID;
    private transient HashMap myParts;
    private int myValidCycle;
    private static HashMap myCycleMap = null;

    public GraphicViewerDocument() {
        myDefaultLayer = null;
        myLinksLayer = null;
        myFirstLayer = null;
        myLastLayer = null;
        myDocumentSize = new Dimension(0, 0);
        myDocumentTopLeft = new Point(0, 0);
        myPaperColor = null;
        myModifiable = true;
        mySuspendUpdates = false;
        mySkipsUndoManager = false;
        myDocumentListeners = null;
        myDocumentEvent = null;
        myUndoManager = null;
        myPositions = null;
        mySkippedAvoidable = null;
        myMaintainsPartID = false;
        myLastPartID = -1;
        myParts = null;
        myValidCycle = 0;
        myDefaultLayer = new GraphicViewerLayer();
        myDefaultLayer.init(this);
        myLinksLayer = myDefaultLayer;
        myFirstLayer = myDefaultLayer;
        myLastLayer = myDefaultLayer;
        myCycleMap = new HashMap();
    }

    public Dimension getDocumentSize() {
        return myDocumentSize;
    }

    public void setDocumentSize(int i, int j) {
        if (i == -23) {
            if (j == -23) {
                myCaching = true;
            } else if (j == -24) {
                myCaching = false;
            }
        }
        Dimension dimension = myDocumentSize;
        if (i >= 0 && j >= 0 && (dimension.width != i || dimension.height != j)) {
            Dimension dimension1 = new Dimension(dimension);
            myDocumentSize.width = i;
            myDocumentSize.height = j;
            fireUpdate(205, 0, null, 0, dimension1);
        }
    }

    public final void setDocumentSize(Dimension dimension) {
        setDocumentSize(dimension.width, dimension.height);
    }

    protected void updateDocumentSize(GraphicViewerObject graphicviewerobject) {
        Dimension dimension = getDocumentSize();
        Point point = getDocumentTopLeft();
        Rectangle rectangle = graphicviewerobject.getBoundingRect();
        int i = Math.min(point.x, rectangle.x);
        int j = Math.min(point.y, rectangle.y);
        int k = Math.max(point.x + dimension.width, rectangle.x
                + rectangle.width);
        int l = Math.max(point.y + dimension.height, rectangle.y
                + rectangle.height);
        int i1 = k - i;
        int j1 = l - j;
        if (i < point.x || j < point.y) {
            setDocumentTopLeft(i, j);
        }
        if (i1 > dimension.width || j1 > dimension.height) {
            setDocumentSize(i1, j1);
        }
    }

    public Point getDocumentTopLeft() {
        return myDocumentTopLeft;
    }

    public void setDocumentTopLeft(int i, int j) {
        Point point = myDocumentTopLeft;
        if (point.x != i || point.y != j) {
            Point point1 = new Point(point.x, point.y);
            myDocumentTopLeft.x = i;
            myDocumentTopLeft.y = j;
            fireUpdate(209, 0, null, 0, point1);
        }
    }

    public final void setDocumentTopLeft(Point point) {
        setDocumentTopLeft(point.x, point.y);
    }

    public static Rectangle computeBounds(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        boolean flag = false;
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
            if (graphicviewerobject.isVisible()) {
                Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
                rectangle.x = rectangle1.x;
                rectangle.y = rectangle1.y;
                rectangle.width = rectangle1.width;
                rectangle.height = rectangle1.height;
                graphicviewerobject.expandRectByPenWidth(rectangle);
                if (!flag) {
                    flag = true;
                    i = rectangle.x;
                    j = rectangle.y;
                    k = rectangle.x + rectangle.width;
                    l = rectangle.y + rectangle.height;
                } else {
                    i = Math.min(i, rectangle.x);
                    j = Math.min(j, rectangle.y);
                    k = Math.max(k, rectangle.x + rectangle.width);
                    l = Math.max(l, rectangle.y + rectangle.height);
                }
            }
        } while (true);
        rectangle.x = i;
        rectangle.y = j;
        rectangle.width = k - i;
        rectangle.height = l - j;
        return rectangle;
    }

    public Rectangle computeBounds() {
        return computeBounds(((IGraphicViewerObjectSimpleCollection) (this)));
    }

    public Color getPaperColor() {
        return myPaperColor;
    }

    public void setPaperColor(Color color) {
        Color color1 = myPaperColor;
        if (color != color1 && (color == null || !color.equals(color1))) {
            myPaperColor = color;
            fireUpdate(206, 0, null, 0, color1);
        }
    }

    public boolean isModifiable() {
        return myModifiable;
    }

    public void setModifiable(boolean flag) {
        boolean flag1 = myModifiable;
        if (flag1 != flag) {
            myModifiable = flag;
            fireUpdate(208, 0, null, flag1 ? 1 : 0, null);
        }
    }

    public int getNumObjects() {
        int i = 0;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            i += graphicviewerlayer.getNumObjects();
        }

        return i;
    }

    public boolean isEmpty() {
        return getNumObjects() == 0;
    }

    public void add(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return;
        }
        if (graphicviewerobject instanceof GraphicViewerLink) {
            getLinksLayer().addObjectAtTail(graphicviewerobject);
        } else {
            getDefaultLayer().addObjectAtTail(graphicviewerobject);
        }
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        return getFirstLayer().addObjectAtHead(graphicviewerobject);
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        return getLastLayer().addObjectAtTail(graphicviewerobject);
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        } else {
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            return graphicviewerobject1.getLayer().insertObjectBefore(
                    graphicviewerlistposition, graphicviewerobject);
        }
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        } else {
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            return graphicviewerobject1.getLayer().insertObjectAfter(
                    graphicviewerlistposition, graphicviewerobject);
        }
    }

    public void bringObjectToFront(GraphicViewerObject graphicviewerobject) {
        getLastLayer().bringObjectToFront(graphicviewerobject);
    }

    public void sendObjectToBack(GraphicViewerObject graphicviewerobject) {
        getFirstLayer().sendObjectToBack(graphicviewerobject);
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return;
        }
        GraphicViewerLayer graphicviewerlayer = graphicviewerobject.getLayer();
        if (graphicviewerlayer == null) {
            return;
        }
        if (graphicviewerlayer.getDocument() != this) {
            return;
        } else {
            graphicviewerlayer.removeObject(graphicviewerobject);
            return;
        }
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
        return graphicviewerobject.getLayer().removeObjectAtPos(
                graphicviewerlistposition);
    }

    public void removeAll() {
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            graphicviewerlayer.removeAll();
        }
    }

    public void deleteContents() {
        for (; getLastLayer() != getFirstLayer(); removeLayer(getLastLayer())) {
            ;
        }
        getFirstLayer().removeAll();
        setDocumentSize(0, 0);
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getPrevLayer()) {
            GraphicViewerObject graphicviewerobject = graphicviewerlayer
                    .pickObject(point, flag);
            if (graphicviewerobject != null) {
                return graphicviewerobject;
            }
        }

        return null;
    }

    public ArrayList pickObjects(Point point, boolean flag,
            ArrayList arraylist, int i) {
        if (arraylist == null) {
            arraylist = new ArrayList();
        }
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getPrevLayer()) {
            if (arraylist.size() >= i) {
                return arraylist;
            }
            graphicviewerlayer.pickObjects(point, flag, arraylist, i);
        }

        return arraylist;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            if (graphicviewerlistposition != null) {
                return graphicviewerlistposition;
            }
        }

        return null;
    }

    public GraphicViewerListPosition getLastObjectPos() {
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getPrevLayer()) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getLastObjectPos();
            if (graphicviewerlistposition != null) {
                return graphicviewerlistposition;
            }
        }

        return null;
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
                GraphicViewerLayer graphicviewerlayer = ((GraphicViewerObject) (obj))
                        .getLayer();
                for (graphicviewerlayer = graphicviewerlayer.getNextLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                        .getNextLayer()) {
                    GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerlayer
                            .getFirstObjectPos();
                    if (graphicviewerlistposition2 != null) {
                        return graphicviewerlistposition2;
                    }
                }

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
        Object obj;
        for (obj = graphicviewerlistposition.obj; ((GraphicViewerObject) (obj))
                .getParent() != null; obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            graphicviewerlistposition = ((GraphicViewerObject) (obj))
                    .getParent().getCurrentListPosition();
        }

        graphicviewerlistposition = graphicviewerlistposition.next;
        if (graphicviewerlistposition == null) {
            GraphicViewerLayer graphicviewerlayer = ((GraphicViewerObject) (obj))
                    .getLayer();
            for (graphicviewerlayer = graphicviewerlayer.getNextLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                    .getNextLayer()) {
                GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerlayer
                        .getFirstObjectPos();
                if (graphicviewerlistposition1 != null) {
                    return graphicviewerlistposition1;
                }
            }

        }
        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        } else {
            return graphicviewerlistposition.prev;
        }
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        } else {
            return graphicviewerlistposition.obj;
        }
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition != null) {
                return graphicviewerlistposition;
            }
        }

        return null;
    }

    public void addDocumentListener(
            IGraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (myDocumentListeners == null) {
            myDocumentListeners = new ArrayList();
        }
        if (!myDocumentListeners.contains(graphicviewerdocumentlistener)) {
            myDocumentListeners.add(graphicviewerdocumentlistener);
        }
    }

    public void removeDocumentListener(
            IGraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (myDocumentListeners == null) {
            return;
        }
        int i = myDocumentListeners.indexOf(graphicviewerdocumentlistener);
        if (i >= 0) {
            myDocumentListeners.remove(i);
        }
    }

    public IGraphicViewerDocumentListener[] getDocumentListeners() {
        if (myDocumentListeners == null) {
            return new IGraphicViewerDocumentListener[0];
        }
        Object aobj[] = myDocumentListeners.toArray();
        IGraphicViewerDocumentListener agraphicviewerdocumentlistener[] = new IGraphicViewerDocumentListener[aobj.length];
        for (int i = 0; i < aobj.length; i++) {
            agraphicviewerdocumentlistener[i] = (IGraphicViewerDocumentListener) aobj[i];
        }

        return agraphicviewerdocumentlistener;
    }

    public void fireUpdate(int i, int j, Object obj, int k, Object obj1) {
        invokeListeners(i, j, obj, k, obj1);
    }

    void invokeListeners(int i, int j, Object obj, int k, Object obj1) {
        if (i == 203 && j == 1) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
            invalidatePositionArray(graphicviewerobject);
            if (graphicviewerobject.isTopLevel()) {
                GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                        .getLayer();
                if (graphicviewerlayer != null) {
                    graphicviewerlayer.updateCache(graphicviewerobject,
                            (Rectangle) obj1);
                }
            }
        } else if (i == 202) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) obj;
            if (isMaintainsPartID()) {
                addAllParts(graphicviewerobject1);
            }
            invalidatePositionArray(graphicviewerobject1);
        } else if (i == 204) {
            GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) obj;
            removeAllParts(graphicviewerobject2);
            invalidatePositionArray(graphicviewerobject2);
        } else if (i == 210) {
            invalidatePositionArray(null);
        } else if (i == 211) {
            invalidatePositionArray(null);
        }
        if (isSuspendUpdates()) {
            return;
        }
        if (myDocumentEvent == null) {
            myDocumentEvent = new GraphicViewerDocumentEvent(this, i, j, obj,
                    k, obj1);
        } else {
            myDocumentEvent.setHint(i);
            myDocumentEvent.setFlags(j);
            myDocumentEvent.setObject(obj);
            myDocumentEvent.setPreviousValueInt(k);
            myDocumentEvent.setPreviousValue(obj1);
        }
        GraphicViewerDocumentEvent graphicviewerdocumentevent = myDocumentEvent;
        myDocumentEvent = null;
        ArrayList arraylist = myDocumentListeners;
        if (arraylist != null) {
            int l = arraylist.size();
            for (int i1 = 0; i1 < l; i1++) {
                IGraphicViewerDocumentListener graphicviewerdocumentlistener = (IGraphicViewerDocumentListener) arraylist
                        .get(i1);
                graphicviewerdocumentlistener
                        .documentChanged(graphicviewerdocumentevent);
            }

        }
        myDocumentEvent = graphicviewerdocumentevent;
        myDocumentEvent.setObject(null);
    }

    public void fireForedate(int i, int j, Object obj) {
        i |= 0x8000;
        invokeListeners(i, j, obj, 0, null);
    }

    public final void update() {
        fireUpdate(100, 0, null, 0, null);
    }

    public boolean isSuspendUpdates() {
        return mySuspendUpdates;
    }

    public void setSuspendUpdates(boolean flag) {
        mySuspendUpdates = flag;
        if (!flag) {
            invalidatePositionArray(null);
            for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                    .getNextLayer()) {
                graphicviewerlayer.resetCache();
            }

            update();
        }
    }

    public boolean isSkipsUndoManager() {
        return mySkipsUndoManager;
    }

    public void setSkipsUndoManager(boolean flag) {
        mySkipsUndoManager = flag;
    }

    public boolean isMaintainsPartID() {
        return myMaintainsPartID;
    }

    public void setMaintainsPartID(boolean flag) {
        internalSetMaintainsPartID(flag, false);
    }

    private void internalSetMaintainsPartID(boolean flag, boolean flag1) {
        boolean flag2 = myMaintainsPartID;
        if (flag2 != flag) {
            myMaintainsPartID = flag;
            fireUpdate(219, 0, null, flag2 ? 1 : 0, null);
            if (!flag1) {
                if (flag) {
                    ensureUniquePartID();
                } else {
                    myParts = null;
                }
            }
        }
    }

    public IGraphicViewerIdentifiablePart findPart(int i) {
        if (myParts != null) {
            return (IGraphicViewerIdentifiablePart) myParts.get(new Integer(i));
        } else {
            return null;
        }
    }

    void addPart(IGraphicViewerIdentifiablePart graphicvieweridentifiablepart) {
        if (myParts == null) {
            myParts = new HashMap(1000);
        }
        int i = graphicvieweridentifiablepart.getPartID();
        Integer integer = null;
        if (i != -1) {
            integer = new Integer(i);
            if (myParts.get(integer) == graphicvieweridentifiablepart) {
                return;
            }
            if (myParts.get(integer) == null) {
                myParts.put(integer, graphicvieweridentifiablepart);
                return;
            }
        }
        i = ++myLastPartID;
        for (integer = new Integer(i); myParts.get(integer) != null; integer = new Integer(
                i)) {
            i = ++myLastPartID;
        }

        myParts.put(integer, graphicvieweridentifiablepart);
        graphicvieweridentifiablepart.setPartID(i);
    }

    void removePart(IGraphicViewerIdentifiablePart graphicvieweridentifiablepart) {
        if (myParts != null) {
            myParts.remove(new Integer(graphicvieweridentifiablepart
                    .getPartID()));
        }
    }

    public void ensureUniquePartID() {
        if (isMaintainsPartID()) {
            for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                addAllParts(graphicviewerobject);
            }
        }
    }

    void addAllParts(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject instanceof IGraphicViewerIdentifiablePart) {
            IGraphicViewerIdentifiablePart graphicvieweridentifiablepart = (IGraphicViewerIdentifiablePart) graphicviewerobject;
            addPart(graphicvieweridentifiablepart);
        }
        if (graphicviewerobject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerarea
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                addAllParts(graphicviewerobject1);
            }
        }
    }

    void removeAllParts(GraphicViewerObject graphicviewerobject) {
        if (myParts != null) {
            if (graphicviewerobject instanceof IGraphicViewerIdentifiablePart) {
                IGraphicViewerIdentifiablePart graphicvieweridentifiablepart = (IGraphicViewerIdentifiablePart) graphicviewerobject;
                removePart(graphicvieweridentifiablepart);
            }
            if (graphicviewerobject instanceof GraphicViewerArea) {
                GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
                for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                        .getFirstObjectPos(); graphicviewerlistposition != null;) {
                    GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerarea
                            .getNextObjectPosAtTop(graphicviewerlistposition);
                    removeAllParts(graphicviewerobject1);
                }
            }
        }
    }

    public void copyOldValueForUndo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getHint()) {
            case 202 :
                return;

            case 203 :
                GraphicViewerObject graphicviewerobject = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerobject
                        .copyOldValueForUndo(graphicviewerdocumentchangededit);
                return;

            case 204 :
                return;

            case 205 :
                graphicviewerdocumentchangededit.setOldValue(new Dimension(
                        (Dimension) graphicviewerdocumentchangededit
                                .getOldValue()));
                return;

            case 206 :
                return;

            case 208 :
                return;

            case 209 :
                Point point = (Point) graphicviewerdocumentchangededit
                        .getOldValue();
                graphicviewerdocumentchangededit.setOldValue(new Point(point.x,
                        point.y));
                return;

            case 210 :
                return;

            case 211 :
                return;

            case 212 :
                return;

            case 213 :
            case 214 :
            case 215 :
            case 217 :
            case 219 :
                return;

            case 218 :
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

            case 207 :
            case 216 :
            default :
                return;
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getHint()) {
            case 202 :
                return;

            case 203 :
                GraphicViewerObject graphicviewerobject = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerobject
                        .copyNewValueForRedo(graphicviewerdocumentchangededit);
                return;

            case 204 :
                return;

            case 205 :
                graphicviewerdocumentchangededit.setNewValue(new Dimension(
                        getDocumentSize()));
                return;

            case 206 :
                graphicviewerdocumentchangededit.setNewValue(getPaperColor());
                return;

            case 208 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isModifiable());
                return;

            case 209 :
                Point point = getDocumentTopLeft();
                graphicviewerdocumentchangededit.setNewValue(new Point(point.x,
                        point.y));
                return;

            case 210 :
                return;

            case 211 :
                return;

            case 212 :
                GraphicViewerLayer graphicviewerlayer = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerdocumentchangededit.setNewValue(graphicviewerlayer
                        .getNextLayer());
                return;

            case 213 :
                GraphicViewerLayer graphicviewerlayer1 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(graphicviewerlayer1.isVisible());
                return;

            case 214 :
                GraphicViewerLayer graphicviewerlayer2 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(graphicviewerlayer2.isModifiable());
                return;

            case 215 :
                GraphicViewerLayer graphicviewerlayer3 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerdocumentchangededit.setNewValue(new Float(
                        graphicviewerlayer3.getTransparency()));
                return;

            case 217 :
                GraphicViewerLayer graphicviewerlayer4 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerdocumentchangededit
                        .setNewValue(graphicviewerlayer4.getIdentifier());
                return;

            case 218 :
                ArrayList arraylist = new ArrayList();
                int i = 0;
                for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                    GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                    arraylist.add(i++, graphicviewerobject1);
                    if (graphicviewerobject1 instanceof GraphicViewerLink) {
                        GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject1;
                        Vector vector = graphicviewerlink.copyPoints();
                        arraylist.add(i++, vector);
                    } else {
                        Rectangle rectangle = graphicviewerobject1
                                .getBoundingRect();
                        arraylist
                                .add(i++, new Rectangle(rectangle.x,
                                        rectangle.y, rectangle.width,
                                        rectangle.height));
                    }
                }

                graphicviewerdocumentchangededit.setNewValue(arraylist);
                return;

            case 219 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isMaintainsPartID());
                return;

            case 220 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getValidCycle());
                return;

            case 1902 :
                return;
        }
        if (graphicviewerdocumentchangededit.getHint() >= 65535
                && graphicviewerdocumentchangededit.getHint() != 65537
                && graphicviewerdocumentchangededit.getHint() != 65536) {
            throw new IllegalArgumentException(
                    "unknown GraphicViewerDocumentEvent hint: "
                            + Integer.toString(graphicviewerdocumentchangededit
                                    .getHint()));
        } else {
            return;
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getHint()) {
            case 202 :
                if (flag) {
                    GraphicViewerObject graphicviewerobject = (GraphicViewerObject) graphicviewerdocumentchangededit
                            .getObject();
                    removeObject(graphicviewerobject);
                } else {
                    reinsertObject(graphicviewerdocumentchangededit);
                }
                return;

            case 203 :
                GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerobject1.changeValue(
                        graphicviewerdocumentchangededit, flag);
                return;

            case 204 :
                if (flag) {
                    reinsertObject(graphicviewerdocumentchangededit);
                } else {
                    GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) graphicviewerdocumentchangededit
                            .getObject();
                    removeObject(graphicviewerobject2);
                }
                return;

            case 205 :
                setDocumentSize((Dimension) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 206 :
                setPaperColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 208 :
                setModifiable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 209 :
                setDocumentTopLeft((Point) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 210 :
                GraphicViewerLayer graphicviewerlayer = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                if (flag) {
                    removeLayer(graphicviewerlayer);
                } else {
                    GraphicViewerLayer graphicviewerlayer7 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                            .getOldValue();
                    switch (graphicviewerdocumentchangededit.getOldValueInt()) {
                        case 8 : // '\b'
                            insertBefore(graphicviewerlayer7,
                                    graphicviewerlayer);
                            break;

                        case 4 : // '\004'
                            insertAfter(graphicviewerlayer7, graphicviewerlayer);
                            break;
                    }
                }
                return;

            case 211 :
                GraphicViewerLayer graphicviewerlayer1 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                if (flag) {
                    GraphicViewerLayer graphicviewerlayer8 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                            .getOldValue();
                    if (graphicviewerlayer8 != null) {
                        insertBefore(graphicviewerlayer8, graphicviewerlayer1);
                    } else {
                        insertLayerAfter(getLastLayer(), graphicviewerlayer1);
                    }
                } else {
                    removeLayer(graphicviewerlayer1);
                }
                return;

            case 212 :
                GraphicViewerLayer graphicviewerlayer2 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                GraphicViewerLayer graphicviewerlayer9 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getValue(flag);
                if (graphicviewerlayer9 != null) {
                    insertLayerBefore(graphicviewerlayer9, graphicviewerlayer2);
                } else {
                    insertLayerAfter(getLastLayer(), graphicviewerlayer2);
                }
                return;

            case 213 :
                GraphicViewerLayer graphicviewerlayer3 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerlayer3.setVisible(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 214 :
                GraphicViewerLayer graphicviewerlayer4 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerlayer4
                        .setModifiable(graphicviewerdocumentchangededit
                                .getValueBoolean(flag));
                return;

            case 215 :
                GraphicViewerLayer graphicviewerlayer5 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                Float float1 = (Float) graphicviewerdocumentchangededit
                        .getValue(flag);
                graphicviewerlayer5.setTransparency(float1.floatValue());
                return;

            case 217 :
                GraphicViewerLayer graphicviewerlayer6 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                graphicviewerlayer6
                        .setIdentifier(graphicviewerdocumentchangededit
                                .getValue(flag));
                return;

            case 218 :
                ArrayList arraylist = (ArrayList) graphicviewerdocumentchangededit
                        .getValue(flag);
                for (int i = 0; i < arraylist.size(); i += 2) {
                    GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) arraylist
                            .get(i);
                    if (graphicviewerobject3 instanceof GraphicViewerLink) {
                        Vector vector = (Vector) arraylist.get(i + 1);
                        GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject3;
                        graphicviewerlink.setPoints(vector);
                    } else {
                        Rectangle rectangle = (Rectangle) arraylist.get(i + 1);
                        graphicviewerobject3.setBoundingRect(rectangle);
                    }
                }

                update();
                return;

            case 219 :
                internalSetMaintainsPartID(
                        graphicviewerdocumentchangededit.getValueBoolean(flag),
                        true);
                return;

            case 220 :
                setValidCycle(graphicviewerdocumentchangededit
                        .getValueInt(flag));
                return;

            case 1902 :
                ArrayList arraylist1 = (ArrayList) graphicviewerdocumentchangededit
                        .getOldValue();
                boolean flag1 = flag
                        ? graphicviewerdocumentchangededit.getOldValueInt() == 1
                        : graphicviewerdocumentchangededit.getOldValueInt() == 0;
                for (int j = 0; j < arraylist1.size(); j++) {
                    GraphicViewerObject graphicviewerobject4 = (GraphicViewerObject) arraylist1
                            .get(j);
                    GraphicViewerArea
                            .setAllNoClear(graphicviewerobject4, flag1);
                }

                return;
        }
        if (graphicviewerdocumentchangededit.getHint() >= 65535) {
            throw new IllegalArgumentException(
                    "unknown GraphicViewerDocumentEvent hint: "
                            + Integer.toString(graphicviewerdocumentchangededit
                                    .getHint()));
        } else {
            return;
        }
    }

    void reinsertObject(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        GraphicViewerObject graphicviewerobject = (GraphicViewerObject) graphicviewerdocumentchangededit
                .getObject();
        switch (graphicviewerdocumentchangededit.getOldValueInt()) {
            case 1 : // '\001'
            case 3 : // '\003'
            case 5 : // '\005'
            case 7 : // '\007'
            default :
                break;

            case 2 : // '\002'
                IGraphicViewerObjectCollection graphicviewerobjectcollection = (IGraphicViewerObjectCollection) graphicviewerdocumentchangededit
                        .getOldValue();
                if (graphicviewerobjectcollection instanceof GraphicViewerArea) {
                    ((GraphicViewerArea) graphicviewerobjectcollection)
                            .addObjectAtTailInternal(graphicviewerobject, true);
                } else {
                    graphicviewerobjectcollection
                            .addObjectAtTail(graphicviewerobject);
                }
                break;

            case 6 : // '\006'
                IGraphicViewerObjectCollection graphicviewerobjectcollection1 = (IGraphicViewerObjectCollection) graphicviewerdocumentchangededit
                        .getOldValue();
                if (graphicviewerobjectcollection1 instanceof GraphicViewerArea) {
                    ((GraphicViewerArea) graphicviewerobjectcollection1)
                            .addObjectAtHeadInternal(graphicviewerobject, true);
                } else {
                    graphicviewerobjectcollection1
                            .addObjectAtHead(graphicviewerobject);
                }
                break;

            case 8 : // '\b'
                GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getOldValue();
                Object obj = graphicviewerobject1.getParent();
                if (obj == null) {
                    obj = graphicviewerobject1.getLayer();
                }
                if (obj == null) {
                    break;
                }
                GraphicViewerListPosition graphicviewerlistposition = ((IGraphicViewerObjectCollection) (obj))
                        .findObject(graphicviewerobject1);
                if (graphicviewerlistposition != null) {
                    if (obj instanceof GraphicViewerArea) {
                        ((GraphicViewerArea) obj).insertObjectBeforeInternal(
                                graphicviewerlistposition, graphicviewerobject,
                                true);
                    } else {
                        ((IGraphicViewerObjectCollection) (obj))
                                .insertObjectBefore(graphicviewerlistposition,
                                        graphicviewerobject);
                    }
                    break;
                }
                if (obj instanceof GraphicViewerArea) {
                    ((GraphicViewerArea) obj).addObjectAtTailInternal(
                            graphicviewerobject, true);
                } else {
                    ((IGraphicViewerObjectCollection) (obj))
                            .addObjectAtTail(graphicviewerobject);
                }
                break;

            case 4 : // '\004'
                GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getOldValue();
                Object obj1 = graphicviewerobject2.getParent();
                if (obj1 == null) {
                    obj1 = graphicviewerobject2.getLayer();
                }
                if (obj1 == null) {
                    break;
                }
                GraphicViewerListPosition graphicviewerlistposition1 = ((IGraphicViewerObjectCollection) (obj1))
                        .findObject(graphicviewerobject2);
                if (graphicviewerlistposition1 != null) {
                    if (obj1 instanceof GraphicViewerArea) {
                        ((GraphicViewerArea) obj1).insertObjectAfterInternal(
                                graphicviewerlistposition1,
                                graphicviewerobject, true);
                    } else {
                        ((IGraphicViewerObjectCollection) (obj1))
                                .insertObjectAfter(graphicviewerlistposition1,
                                        graphicviewerobject);
                    }
                    break;
                }
                if (obj1 instanceof GraphicViewerArea) {
                    ((GraphicViewerArea) obj1).addObjectAtTailInternal(
                            graphicviewerobject, true);
                } else {
                    ((IGraphicViewerObjectCollection) (obj1))
                            .addObjectAtTail(graphicviewerobject);
                }
                break;

            case 0 : // '\0'
                Object aobj[] = (Object[]) graphicviewerdocumentchangededit
                        .getOldValue();
                IGraphicViewerObjectCollection graphicviewerobjectcollection2 = (IGraphicViewerObjectCollection) aobj[0];
                if (aobj[1] != null) {
                    GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) aobj[1];
                    GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerobjectcollection2
                            .findObject(graphicviewerobject3);
                    if (graphicviewerlistposition2 != null) {
                        if (graphicviewerobjectcollection2 instanceof GraphicViewerArea) {
                            ((GraphicViewerArea) graphicviewerobjectcollection2)
                                    .insertObjectAfterInternal(
                                            graphicviewerlistposition2,
                                            graphicviewerobject, true);
                        } else {
                            graphicviewerobjectcollection2.insertObjectAfter(
                                    graphicviewerlistposition2,
                                    graphicviewerobject);
                        }
                        return;
                    }
                }
                if (aobj[2] != null) {
                    GraphicViewerObject graphicviewerobject4 = (GraphicViewerObject) aobj[2];
                    GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerobjectcollection2
                            .findObject(graphicviewerobject4);
                    if (graphicviewerlistposition3 != null) {
                        if (graphicviewerobjectcollection2 instanceof GraphicViewerArea) {
                            ((GraphicViewerArea) graphicviewerobjectcollection2)
                                    .insertObjectBeforeInternal(
                                            graphicviewerlistposition3,
                                            graphicviewerobject, true);
                        } else {
                            graphicviewerobjectcollection2.insertObjectBefore(
                                    graphicviewerlistposition3,
                                    graphicviewerobject);
                        }
                        return;
                    }
                }
                if (graphicviewerobjectcollection2 instanceof GraphicViewerArea) {
                    ((GraphicViewerArea) graphicviewerobjectcollection2)
                            .addObjectAtTailInternal(graphicviewerobject, true);
                } else {
                    graphicviewerobjectcollection2
                            .addObjectAtTail(graphicviewerobject);
                }
                break;
        }
    }

    public static DataFlavor getStandardDataFlavor() {
        if (myStandardFlavor == null) {
            try {
                myStandardFlavor = new DataFlavor(
                        Class.forName("net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSelection"),
                        "GraphicViewer Selection");
                DataFlavor adataflavor[] = {myStandardFlavor};
                myFlavors = adataflavor;
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
        return myStandardFlavor;
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor) {
        return dataflavor.equals(getStandardDataFlavor());
    }

    public synchronized Object getTransferData(DataFlavor dataflavor)
            throws UnsupportedFlavorException, IOException {
        if (dataflavor.equals(getStandardDataFlavor())) {
            return this;
        } else {
            throw new UnsupportedFlavorException(dataflavor);
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        getStandardDataFlavor();
        return myFlavors;
    }

    public IGraphicViewerCopyEnvironment createDefaultCopyEnvironment() {
        return new GraphicViewerCopyMap();
    }

    public GraphicViewerObject addCopy(GraphicViewerObject graphicviewerobject,
            Point point) {
        Point point1 = new Point(0, 0);
        Point point2 = graphicviewerobject.getLocation(point1);
        point1.x = point.x - point2.x;
        point1.y = point.y - point2.y;
        GraphicViewerCollection graphicviewercollection = new GraphicViewerCollection();
        graphicviewercollection.addObjectAtTail(graphicviewerobject);
        IGraphicViewerCopyEnvironment graphicviewercopyenvironment = copyFromCollection(
                graphicviewercollection, point1, null);
        return (GraphicViewerObject) graphicviewercopyenvironment
                .get(graphicviewerobject);
    }

    public IGraphicViewerCopyEnvironment copyFromCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        return copyFromCollection(graphicviewerobjectsimplecollection,
                new Point(0, 0), null);
    }

    public IGraphicViewerCopyEnvironment copyFromCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            Point point,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        if (graphicviewercopyenvironment == null) {
            graphicviewercopyenvironment = createDefaultCopyEnvironment();
        }
        Point point1 = new Point(0, 0);
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
            graphicviewerobject = graphicviewerobject.getDraggingObject();
            GraphicViewerObject graphicviewerobject1 = graphicviewercopyenvironment
                    .copy(graphicviewerobject);
            if (graphicviewerobject1 != null) {
                point1 = graphicviewerobject1.getLocation(point1);
                Point point2 = graphicviewerobject1.computeMove(point1.x,
                        point1.y, point1.x + point.x, point1.y + point.y,
                        point1);
                graphicviewerobject1.setLocation(point2.x, point2.y);
                GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                        .getLayer();
                GraphicViewerLayer graphicviewerlayer1 = null;
                if (graphicviewerlayer != null) {
                    if (graphicviewerlayer.getDocument() == this) {
                        graphicviewerlayer1 = graphicviewerlayer;
                    } else {
                        graphicviewerlayer1 = findLayer(graphicviewerlayer
                                .getIdentifier());
                    }
                }
                if (graphicviewerlayer1 == null) {
                    graphicviewerlayer1 = getDefaultLayer();
                }
                graphicviewerlayer1.addObjectAtTail(graphicviewerobject1);
            }
        } while (true);
        graphicviewercopyenvironment.finishDelayedCopies();
        return graphicviewercopyenvironment;
    }

    public int getNumLayers() {
        int i = 0;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            i++;
        }

        return i;
    }

    public GraphicViewerLayer getFirstLayer() {
        return myFirstLayer;
    }

    public GraphicViewerLayer getLastLayer() {
        return myLastLayer;
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

    public GraphicViewerLayer getDefaultLayer() {
        return myDefaultLayer;
    }

    public void setDefaultLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer.getDocument() != this) {
            return;
        } else {
            myDefaultLayer = graphicviewerlayer;
            return;
        }
    }

    public GraphicViewerLayer getLinksLayer() {
        return myLinksLayer;
    }

    public void setLinksLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer.getDocument() != this) {
            return;
        } else {
            myLinksLayer = graphicviewerlayer;
            return;
        }
    }

    public GraphicViewerLayer addLayerAfter(
            GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null) {
            graphicviewerlayer = getLastLayer();
        }
        GraphicViewerLayer graphicviewerlayer1 = new GraphicViewerLayer();
        graphicviewerlayer1.init(this);
        return insertAfter(graphicviewerlayer, graphicviewerlayer1);
    }

    GraphicViewerLayer insertAfter(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        graphicviewerlayer1.insert(graphicviewerlayer.getNextLayer(),
                graphicviewerlayer);
        if (graphicviewerlayer1.getNextLayer() == null) {
            myLastLayer = graphicviewerlayer1;
        }
        fireUpdate(210, 0, graphicviewerlayer1, 4, graphicviewerlayer);
        return graphicviewerlayer1;
    }

    public GraphicViewerLayer addLayerBefore(
            GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null) {
            graphicviewerlayer = getFirstLayer();
        }
        GraphicViewerLayer graphicviewerlayer1 = new GraphicViewerLayer();
        graphicviewerlayer1.init(this);
        return insertBefore(graphicviewerlayer, graphicviewerlayer1);
    }

    GraphicViewerLayer insertBefore(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        graphicviewerlayer1.insert(graphicviewerlayer,
                graphicviewerlayer.getPrevLayer());
        if (graphicviewerlayer1.getPrevLayer() == null) {
            myFirstLayer = graphicviewerlayer1;
        }
        fireUpdate(210, 0, graphicviewerlayer1, 8, graphicviewerlayer);
        return graphicviewerlayer1;
    }

    public void removeLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer.getDocument() != this) {
            return;
        }
        if (graphicviewerlayer == getFirstLayer()
                && graphicviewerlayer == getLastLayer()) {
            return;
        }
        GraphicViewerLayer graphicviewerlayer1 = graphicviewerlayer
                .getNextLayer();
        graphicviewerlayer.removeAll();
        if (graphicviewerlayer.getPrevLayer() == null) {
            myFirstLayer = graphicviewerlayer1;
        }
        if (graphicviewerlayer1 == null) {
            myLastLayer = graphicviewerlayer.getPrevLayer();
        }
        graphicviewerlayer.extract();
        if (graphicviewerlayer == getLinksLayer()) {
            if (graphicviewerlayer.getNextLayer() != null) {
                setLinksLayer(graphicviewerlayer.getNextLayer());
            } else if (graphicviewerlayer.getPrevLayer() != null) {
                setLinksLayer(graphicviewerlayer.getPrevLayer());
            } else {
                setLinksLayer(getLastLayer());
            }
        }
        if (graphicviewerlayer == getDefaultLayer()) {
            if (graphicviewerlayer.getNextLayer() != null) {
                setDefaultLayer(graphicviewerlayer.getNextLayer());
            } else if (graphicviewerlayer.getPrevLayer() != null) {
                setDefaultLayer(graphicviewerlayer.getPrevLayer());
            } else {
                setDefaultLayer(getLastLayer());
            }
        }
        fireUpdate(211, 0, graphicviewerlayer, 0, graphicviewerlayer1);
    }

    public void insertLayerAfter(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        if (graphicviewerlayer.getDocument() != this
                || graphicviewerlayer1.getDocument() != this
                || graphicviewerlayer == graphicviewerlayer1
                || graphicviewerlayer1.getPrevLayer() == graphicviewerlayer) {
            return;
        }
        GraphicViewerLayer graphicviewerlayer2 = graphicviewerlayer1
                .getNextLayer();
        if (myFirstLayer == graphicviewerlayer1) {
            myFirstLayer = graphicviewerlayer2;
        }
        graphicviewerlayer1.extract();
        graphicviewerlayer1.insert(graphicviewerlayer.getNextLayer(),
                graphicviewerlayer);
        if (myLastLayer == graphicviewerlayer) {
            myLastLayer = graphicviewerlayer1;
        }
        fireUpdate(212, 0, graphicviewerlayer1, 0, graphicviewerlayer2);
    }

    public void insertLayerBefore(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        if (graphicviewerlayer.getDocument() != this
                || graphicviewerlayer1.getDocument() != this
                || graphicviewerlayer == graphicviewerlayer1
                || graphicviewerlayer1.getNextLayer() == graphicviewerlayer) {
            return;
        }
        GraphicViewerLayer graphicviewerlayer2 = graphicviewerlayer1
                .getNextLayer();
        if (myLastLayer == graphicviewerlayer1) {
            myLastLayer = graphicviewerlayer1.getPrevLayer();
        }
        graphicviewerlayer1.extract();
        graphicviewerlayer1.insert(graphicviewerlayer,
                graphicviewerlayer.getPrevLayer());
        if (myFirstLayer == graphicviewerlayer) {
            myFirstLayer = graphicviewerlayer1;
        }
        fireUpdate(212, 0, graphicviewerlayer1, 0, graphicviewerlayer2);
    }

    public void bringLayerToFront(GraphicViewerLayer graphicviewerlayer) {
        insertLayerAfter(getLastLayer(), graphicviewerlayer);
    }

    public void sendLayerToBack(GraphicViewerLayer graphicviewerlayer) {
        insertLayerBefore(getFirstLayer(), graphicviewerlayer);
    }

    public GraphicViewerLayer findLayer(Object obj) {
        if (obj == null) {
            return null;
        }
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            Object obj1 = graphicviewerlayer.getIdentifier();
            if (obj1 != null && obj1.equals(obj)) {
                return graphicviewerlayer;
            }
        }

        return null;
    }

    void copyLayersFrom(GraphicViewerDocument graphicviewerdocument) {
        for (GraphicViewerLayer graphicviewerlayer = graphicviewerdocument
                .getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerdocument
                .getNextLayer(graphicviewerlayer)) {
            Object obj = graphicviewerlayer.getIdentifier();
            if (obj != null && findLayer(obj) == null) {
                GraphicViewerLayer graphicviewerlayer1 = addLayerAfter(getLastLayer());
                graphicviewerlayer1.setIdentifier(obj);
            }
        }

        Object obj1 = graphicviewerdocument.getDefaultLayer().getIdentifier();
        GraphicViewerLayer graphicviewerlayer2 = findLayer(obj1);
        if (graphicviewerlayer2 != null) {
            setDefaultLayer(graphicviewerlayer2);
        }
        Object obj2 = graphicviewerdocument.getLinksLayer().getIdentifier();
        GraphicViewerLayer graphicviewerlayer3 = findLayer(obj2);
        if (graphicviewerlayer3 != null) {
            setLinksLayer(graphicviewerlayer3);
        }
    }

    public void sortByZOrder(GraphicViewerObject agraphicviewerobject[]) {
        if (agraphicviewerobject.length <= 1) {
            return;
        } else {
            initLayerIndices();
            ZOrderComparer localZOrderComparer = new ZOrderComparer(this);
            Arrays.sort(agraphicviewerobject, localZOrderComparer);
            return;
        }
    }

    void initLayerIndices() {
        int i = 0;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            graphicviewerlayer.myLayerIndex = i++;
        }
    }

    int getLayerIndexOf(GraphicViewerLayer graphicviewerlayer) {
        return graphicviewerlayer.myLayerIndex;
    }

    public GraphicViewerUndoManager getUndoManager() {
        return myUndoManager;
    }

    public void setUndoManager(GraphicViewerUndoManager graphicviewerundomanager) {
        GraphicViewerUndoManager graphicviewerundomanager1 = myUndoManager;
        if (graphicviewerundomanager1 != graphicviewerundomanager) {
            if (graphicviewerundomanager1 != null) {
                removeDocumentListener(graphicviewerundomanager1);
            }
            myUndoManager = graphicviewerundomanager;
            if (graphicviewerundomanager != null) {
                addDocumentListener(graphicviewerundomanager);
            }
        }
    }

    public boolean canUndo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            return graphicviewerundomanager.canUndo();
        } else {
            return false;
        }
    }

    public void undo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            try {
                javax.swing.undo.UndoableEdit undoableedit = graphicviewerundomanager
                        .getEditToUndo();
                fireUpdate(107, 0, undoableedit, 0, null);
                graphicviewerundomanager.undo();
                fireUpdate(108, 0, undoableedit, 0, null);
            } catch (CannotUndoException cue) {
                ExceptionDialog.ignoreException(cue);
            }
        }
    }

    public boolean canRedo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            return graphicviewerundomanager.canRedo();
        } else {
            return false;
        }
    }

    public void redo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            try {
                javax.swing.undo.UndoableEdit undoableedit = graphicviewerundomanager
                        .getEditToRedo();
                fireUpdate(109, 0, undoableedit, 0, null);
                graphicviewerundomanager.redo();
                fireUpdate(110, 0, undoableedit, 0, null);
            } catch (CannotRedoException cre) {
                ExceptionDialog.ignoreException(cre);
            }
        }
    }

    public void startTransaction() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            graphicviewerundomanager.startTransaction();
            if (graphicviewerundomanager.getTransactionLevel() == 1) {
                fireUpdate(104, 0, null, 0, null);
            }
        }
    }

    public void endTransaction(boolean flag) {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            GraphicViewerUndoManager.GraphicViewerCompoundEdit graphicviewercompoundedit = graphicviewerundomanager
                    .getCurrentEdit();
            graphicviewerundomanager.endTransaction(flag);
            if (graphicviewerundomanager.getTransactionLevel() == 0
                    && graphicviewercompoundedit != null) {
                if (flag) {
                    fireUpdate(105, 0, graphicviewercompoundedit, 0, null);
                } else {
                    fireUpdate(106, 0, null, 0, null);
                }
            }
        }
    }

    public void endTransaction(String s) {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            GraphicViewerUndoManager.GraphicViewerCompoundEdit graphicviewercompoundedit = graphicviewerundomanager
                    .getCurrentEdit();
            graphicviewerundomanager.endTransaction(s);
            if (graphicviewerundomanager.getTransactionLevel() == 0
                    && graphicviewercompoundedit != null) {
                fireUpdate(105, 0, graphicviewercompoundedit, 0, s);
            }
        }
    }

    public void discardAllEdits() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            graphicviewerundomanager.discardAllEdits();
        }
    }

    public boolean isAvoidable(GraphicViewerObject graphicviewerobject) {
        if (!graphicviewerobject.isVisible()) {
            return false;
        } else {
            return graphicviewerobject instanceof GraphicViewerNode;
        }
    }

    public Rectangle getAvoidableRectangle(
            GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        if (rectangle == null) {
            rectangle = new Rectangle(0, 0, 0, 0);
        }
        Rectangle rectangle1 = graphicviewerobject.getBoundingRect();
        rectangle.x = rectangle1.x;
        rectangle.y = rectangle1.y;
        rectangle.width = rectangle1.width;
        rectangle.height = rectangle1.height;
        graphicviewerobject.expandRectByPenWidth(rectangle);
        return rectangle;
    }

    public boolean isUnoccupied(Rectangle rectangle,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject != mySkippedAvoidable) {
            invalidatePositionArray(null);
            mySkippedAvoidable = graphicviewerobject;
        }
        GraphicViewerPositionArray graphicviewerpositionarray = getPositions(
                false, graphicviewerobject);
        return graphicviewerpositionarray.isUnoccupied(rectangle.x,
                rectangle.y, rectangle.width, rectangle.height);
    }

    GraphicViewerPositionArray getPositions() {
        return getPositions(true, null);
    }

    GraphicViewerPositionArray getPositions(boolean flag,
            GraphicViewerObject graphicviewerobject) {
        if (myPositions == null) {
            myPositions = new GraphicViewerPositionArray();
        }
        if (myPositions.isInvalid()) {
            Rectangle rectangle = computeBounds();
            rectangle.x -= 100;
            rectangle.y -= 100;
            rectangle.width += 200;
            rectangle.height += 200;
            myPositions.initialize(rectangle);
            for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                getPositions1(graphicviewerobject1, rectangle,
                        graphicviewerobject);
            }

            myPositions.setInvalid(false);
        } else if (flag) {
            myPositions.setAllUnoccupied(0x7fffffff);
        }
        return myPositions;
    }

    private void getPositions1(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle, GraphicViewerObject graphicviewerobject1) {
        if (graphicviewerobject == graphicviewerobject1) {
            return;
        }
        if (graphicviewerobject instanceof GraphicViewerSubGraphBase) {
            GraphicViewerSubGraphBase graphicviewersubgraphbase = (GraphicViewerSubGraphBase) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewersubgraphbase
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject2 = graphicviewersubgraphbase
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewersubgraphbase
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                getPositions1(graphicviewerobject2, rectangle,
                        graphicviewerobject1);
            }

        } else if (isAvoidable(graphicviewerobject)) {
            rectangle = getAvoidableRectangle(graphicviewerobject, rectangle);
            int i = myPositions.getCellSize().width;
            int j = myPositions.getCellSize().height;
            for (int k = rectangle.x; k <= rectangle.x + rectangle.width; k += i) {
                for (int l = rectangle.y; l <= rectangle.y + rectangle.height; l += j) {
                    myPositions.setDist(k, l, 0);
                }
            }
        }
    }

    private void invalidatePositionArray(GraphicViewerObject graphicviewerobject) {
        mySkippedAvoidable = null;
        if (myPositions != null
                && !myPositions.isInvalid()
                && (graphicviewerobject == null || isAvoidable(graphicviewerobject))) {
            myPositions.setInvalid(true);
        }
    }

    public void setValidCycle(int i) {
        int j = myValidCycle;
        if (j != i) {
            myValidCycle = i;
            fireUpdate(220, 0, null, j, null);
        }
    }

    public int getValidCycle() {
        return myValidCycle;
    }

    public static boolean makesDirectedCycleFast(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        }
        ArrayList arraylist = graphicviewernode1.findAll(16, null);
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext();) {
            GraphicViewerNode graphicviewernode2 = (GraphicViewerNode) iterator
                    .next();
            if (graphicviewernode2 != graphicviewernode1
                    && makesDirectedCycleFast(graphicviewernode,
                            graphicviewernode2)) {
                return true;
            }
        }

        return false;
    }

    public static boolean makesDirectedCycle(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        } else {
            myCycleMap.clear();
            myCycleMap.put(graphicviewernode, null);
            boolean flag = makesDirectedCycle1(graphicviewernode,
                    graphicviewernode1, myCycleMap);
            myCycleMap.clear();
            return flag;
        }
    }

    private static boolean makesDirectedCycle1(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1, HashMap hashmap) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        }
        if (hashmap.containsKey(graphicviewernode1)) {
            return false;
        }
        hashmap.put(graphicviewernode1, null);
        ArrayList arraylist = graphicviewernode1.findAll(16, null);
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext();) {
            GraphicViewerNode graphicviewernode2 = (GraphicViewerNode) iterator
                    .next();
            if (graphicviewernode2 != graphicviewernode1
                    && makesDirectedCycle1(graphicviewernode,
                            graphicviewernode2, hashmap)) {
                return true;
            }
        }

        return false;
    }

    public static boolean makesUndirectedCycle(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        } else {
            myCycleMap.clear();
            myCycleMap.put(graphicviewernode, null);
            boolean flag = makesUndirectedCycle1(graphicviewernode,
                    graphicviewernode1, myCycleMap);
            myCycleMap.clear();
            return flag;
        }
    }

    private static boolean makesUndirectedCycle1(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1, HashMap hashmap) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        }
        if (hashmap.containsKey(graphicviewernode1)) {
            return false;
        }
        hashmap.put(graphicviewernode1, null);
        ArrayList arraylist = graphicviewernode1.findAll(24, null);
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext();) {
            GraphicViewerNode graphicviewernode2 = (GraphicViewerNode) iterator
                    .next();
            if (graphicviewernode2 != graphicviewernode1
                    && makesUndirectedCycle1(graphicviewernode,
                            graphicviewernode2, hashmap)) {
                return true;
            }
        }

        return false;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument",
                            domelement);
            domelement1.setAttribute("docwidth",
                    Integer.toString(myDocumentSize.width));
            domelement1.setAttribute("docheight",
                    Integer.toString(myDocumentSize.height));
            domelement1.setAttribute("docleft",
                    Integer.toString(myDocumentTopLeft.x));
            domelement1.setAttribute("doctop",
                    Integer.toString(myDocumentTopLeft.y));
            if (myPaperColor != null) {
                int i = myPaperColor.getRed();
                int j = myPaperColor.getGreen();
                int k = myPaperColor.getBlue();
                int l = myPaperColor.getAlpha();
                String s = "rgbalpha(" + Integer.toString(i) + ","
                        + Integer.toString(j) + "," + Integer.toString(k) + ","
                        + Integer.toString(l) + ")";
                domelement1.setAttribute("papercolor", s);
            }
            domelement1.setAttribute("modifiable", myModifiable
                    ? "true"
                    : "false");
            domdoc.registerReferencingNode(domelement1, "defaultlayer",
                    getDefaultLayer());
        }
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            myDocumentSize.width = Integer.parseInt(domelement1
                    .getAttribute("docwidth"));
            myDocumentSize.height = Integer.parseInt(domelement1
                    .getAttribute("docheight"));
            myDocumentTopLeft.x = Integer.parseInt(domelement1
                    .getAttribute("docleft"));
            myDocumentTopLeft.y = Integer.parseInt(domelement1
                    .getAttribute("doctop"));
            String s = domelement1.getAttribute("papercolor");
            if (s.length() > 0 && s.startsWith("rgbalpha")) {
                int i = s.indexOf("(") + 1;
                int j = s.indexOf(",", i);
                String s2 = s.substring(i, j);
                i = j + 1;
                j = s.indexOf(",", i);
                String s3 = s.substring(i, j);
                i = j + 1;
                j = s.indexOf(",", i);
                String s4 = s.substring(i, j);
                i = j + 1;
                j = s.indexOf(")", i);
                String s5 = s.substring(i, j);
                graphicviewerdocument.setPaperColor(new Color(Integer
                        .parseInt(s2), Integer.parseInt(s3), Integer
                        .parseInt(s4), Integer.parseInt(s5)));
            }
            myModifiable = domelement1.getAttribute("modifiable")
                    .equals("true");
            String s1 = domelement1.getAttribute("defaultlayer");
            domdoc.registerReferencingObject(this, "defaultlayer", s1);
        }
        return domelement.getNextSibling();
    }

    public void SVGUpdateReference(String s, Object obj) {
        if (s.equals("defaultlayer")) {
            setDefaultLayer((GraphicViewerLayer) obj);
        }
    }

    public void SVGWriteLayer(IDomDoc domdoc, IDomElement domelement,
            GraphicViewerLayer graphicviewerlayer) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLayer",
                            domelement);
            domelement1.setAttribute("visible", graphicviewerlayer.myVisible
                    ? "true"
                    : "false");
            domelement1.setAttribute("transparency", (new Float(
                    graphicviewerlayer.myTransparency)).toString());
            domelement1.setAttribute("modifiable",
                    graphicviewerlayer.myModifiable ? "true" : "false");
            if (graphicviewerlayer.myIdentifier instanceof String) {
                domelement1.setAttribute("idtype", "string");
                domelement1.setAttribute("identifier",
                        (String) graphicviewerlayer.myIdentifier);
            } else if (graphicviewerlayer.myIdentifier instanceof Integer) {
                domelement1.setAttribute("idtype", "integer");
                domelement1.setAttribute("identifier",
                        ((Integer) graphicviewerlayer.myIdentifier).toString());
            }
            domdoc.registerObject(this, domelement1);
        }
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerlayer
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            try {
                domdoc.setDisabledDrawing(false);
                IDomElement domelement2 = domelement;
                if (domdoc.GraphicViewerXMLOutputEnabled()
                        || domdoc.SVGOutputEnabled()) {
                    domelement2 = domdoc.createElement("g");
                    domelement.appendChild(domelement2);
                }
                graphicviewerobject.SVGWriteObject(domdoc, domelement2);
            } catch (Exception e) {
                ExceptionDialog.ignoreException(e);
            }
        }
    }

    public IDomNode SVGReadLayer(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1,
            GraphicViewerLayer graphicviewerlayer) {
        if (domelement1 != null) {
            graphicviewerlayer.myVisible = domelement1.getAttribute("visible")
                    .equals("true");
            graphicviewerlayer.myTransparency = (new Float(
                    domelement1.getAttribute("transparency"))).floatValue();
            graphicviewerlayer.myModifiable = domelement1.getAttribute(
                    "modifiable").equals("true");
            if (domelement1.getAttribute("idtype").equals("string")) {
                graphicviewerlayer.myIdentifier = domelement1
                        .getAttribute("identifier");
            } else if (domelement1.getAttribute("idtype").equals("integer")) {
                graphicviewerlayer.myIdentifier = new Integer(
                        domelement1.getAttribute("identifier"));
            }
            domdoc.SVGTraverseChildren(graphicviewerdocument, domelement, null,
                    true);
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class ZOrderComparer implements Comparator {

        private GraphicViewerDocument myDocument;

        public ZOrderComparer(GraphicViewerDocument graphicviewerdocument) {
            myDocument = graphicviewerdocument;
        }

        public int compare(Object obj, Object obj1) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) obj1;
            if (graphicviewerobject == null || graphicviewerobject1 == null
                    || graphicviewerobject == graphicviewerobject1) {
                return 0;
            }
            int i = myDocument.getLayerIndexOf(graphicviewerobject.getLayer());
            int j = myDocument.getLayerIndexOf(graphicviewerobject1.getLayer());
            if (i < j) {
                return -1;
            }
            if (i > j) {
                return 1;
            }
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            if (graphicviewerlayer == null) {
                return -1;
            }
            int k = graphicviewerlayer.getIndexOf(graphicviewerobject
                    .getTopLevelObject());
            int l = graphicviewerlayer.getIndexOf(graphicviewerobject1
                    .getTopLevelObject());
            if (k < l) {
                return -1;
            }
            if (k > l) {
                return 1;
            } else {
                return AFirst(graphicviewerobject.getTopLevelObject(),
                        graphicviewerobject, graphicviewerobject1);
            }
        }

        private int AFirst(GraphicViewerObject graphicviewerobject,
                GraphicViewerObject graphicviewerobject1,
                GraphicViewerObject graphicviewerobject2) {
            label0 : {
                if (graphicviewerobject == graphicviewerobject1) {
                    return -1;
                }
                if (graphicviewerobject == graphicviewerobject2) {
                    return 1;
                }
                if (!(graphicviewerobject instanceof GraphicViewerArea)) {
                    break label0;
                }
                GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
                GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                        .getFirstObjectPos();
                int i;
                do {
                    if (graphicviewerlistposition == null) {
                        break label0;
                    }
                    GraphicViewerObject graphicviewerobject3 = graphicviewerarea
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerarea
                            .getNextObjectPos(graphicviewerlistposition);
                    i = AFirst(graphicviewerobject3, graphicviewerobject1,
                            graphicviewerobject2);
                } while (i == 0);
                return i;
            }
            return 0;
        }
    }
}