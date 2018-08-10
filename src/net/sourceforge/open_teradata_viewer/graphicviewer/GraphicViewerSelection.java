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
            GraphicViewerObjectSimpleCollection,
            Transferable,
            Serializable {

    private static final long serialVersionUID = -5123147854138539136L;

    @SuppressWarnings("rawtypes")
    public GraphicViewerSelection() {
        _fldelse = new GraphicViewerObjList();
        _fldchar = new HashMap();
        _fldcase = null;
        _fldbyte = null;
        _fldlong = GraphicViewerPen.black;
        _fldnull = null;
        _fldgoto = false;
        _fldcase = null;
    }

    @SuppressWarnings("rawtypes")
    public GraphicViewerSelection(GraphicViewerView graphicviewerview) {
        _fldelse = new GraphicViewerObjList();
        _fldchar = new HashMap();
        _fldcase = null;
        _fldbyte = null;
        _fldlong = GraphicViewerPen.black;
        _fldnull = null;
        _fldgoto = false;
        _fldcase = graphicviewerview;
    }

    public final GraphicViewerView getView() {
        return _fldcase;
    }

    public int getNumObjects() {
        return _fldelse.getNumObjects();
    }

    public boolean isEmpty() {
        return _fldelse.isEmpty();
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return _fldelse.getFirstObjectPos();
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return _fldelse.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return _fldelse.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        return _fldelse.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerObject getPrimarySelection() {
        GraphicViewerListPosition graphicviewerlistposition = _fldelse
                .getFirstObjectPos();
        if (graphicviewerlistposition == null)
            return null;
        else
            return _fldelse.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerObject selectObject(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
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
        if (graphicviewerobject == null)
            return null;
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null)
            return null;
        if (!isSelected(graphicviewerobject))
            a(graphicviewerobject);
        return graphicviewerobject;
    }

    public GraphicViewerObject toggleSelection(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null)
            return null;
        if (isSelected(graphicviewerobject))
            _mthif(graphicviewerobject);
        else
            a(graphicviewerobject);
        return graphicviewerobject;
    }

    public void clearSelection(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            clearSelection();
            return;
        }
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null)
            return;
        if (isSelected(graphicviewerobject))
            _mthif(graphicviewerobject);
    }

    public void clearSelection() {
        GraphicViewerView graphicviewerview = getView();
        boolean flag = graphicviewerview != null && getNumObjects() > 1;
        if (flag)
            graphicviewerview.fireUpdate(37, 0, null);
        for (GraphicViewerListPosition graphicviewerlistposition = _fldelse
                .getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = _fldelse
                .getFirstObjectPos()) {
            GraphicViewerObject graphicviewerobject = _fldelse
                    .getObjectAtPos(graphicviewerlistposition);
            _fldelse.removeObjectAtPos(graphicviewerlistposition);
            graphicviewerobject.lostSelection(this);
            if (graphicviewerview != null)
                graphicviewerview.fireUpdate(21, 0, graphicviewerobject);
            _fldchar.remove(graphicviewerobject);
        }

        if (flag)
            graphicviewerview.fireUpdate(38, 0, null);
    }

    public boolean isInSelection(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return false;
        graphicviewerobject = graphicviewerobject.redirectSelection();
        if (graphicviewerobject == null)
            return false;
        else
            return isSelected(graphicviewerobject);
    }

    public boolean isSelected(GraphicViewerObject graphicviewerobject) {
        return _fldchar.containsKey(graphicviewerobject);
    }

    @SuppressWarnings("unchecked")
    private void a(GraphicViewerObject graphicviewerobject) {
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null
                && graphicviewerobject.getView() != graphicviewerview
                && graphicviewerobject.getDocument() != graphicviewerview
                        .getDocument())
            return;
        _fldelse.addObjectAtTail(graphicviewerobject);
        _fldchar.put(graphicviewerobject, null);
        graphicviewerobject.gainedSelection(this);
        if (graphicviewerview != null)
            graphicviewerview.fireUpdate(20, 0, graphicviewerobject);
    }

    private void _mthif(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = getPrimarySelection();
        _fldelse.removeObject(graphicviewerobject);
        GraphicViewerView graphicviewerview = getView();
        graphicviewerobject.lostSelection(this);
        if (graphicviewerview != null)
            graphicviewerview.fireUpdate(21, 0, graphicviewerobject);
        _fldchar.remove(graphicviewerobject);
        if (graphicviewerobject1 == graphicviewerobject) {
            GraphicViewerObject graphicviewerobject2 = getPrimarySelection();
            if (graphicviewerobject2 != null) {
                graphicviewerobject2.lostSelection(this);
                if (graphicviewerview != null)
                    graphicviewerview.fireUpdate(21, 1, graphicviewerobject2);
                graphicviewerobject2.gainedSelection(this);
                if (graphicviewerview != null)
                    graphicviewerview.fireUpdate(20, 1, graphicviewerobject2);
            }
        }
    }

    public GraphicViewerObject[] toArray() {
        int i = getNumObjects();
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[i];
        int j = 0;
        for (GraphicViewerListPosition graphicviewerlistposition = _fldelse
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = _fldelse
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = _fldelse
                    .getNextObjectPos(graphicviewerlistposition);
            agraphicviewerobject[j++] = graphicviewerobject;
        }

        return agraphicviewerobject;
    }

    public void addArray(GraphicViewerObject agraphicviewerobject[]) {
        for (int i = 0; i < agraphicviewerobject.length; i++)
            extendSelection(agraphicviewerobject[i]);

    }

    @SuppressWarnings("rawtypes")
    public void addCollection(ArrayList arraylist) {
        for (int i = 0; i < arraylist.size(); i++)
            extendSelection((GraphicViewerObject) arraylist.get(i));

    }

    public void addCollection(
            GraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
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
            for (GraphicViewerListPosition graphicviewerlistposition = _fldelse
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                graphicviewerobject = _fldelse
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = _fldelse
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
                            .getLayer().isVisible()))
                graphicviewerobject.showSelectionHandles(this);
        } else {
            for (GraphicViewerListPosition graphicviewerlistposition = _fldelse
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                graphicviewerobject = _fldelse
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = _fldelse
                        .getNextObjectPos(graphicviewerlistposition);
                if (graphicviewerobject.isVisible()
                        && (graphicviewerobject.getLayer() == null || graphicviewerobject
                                .getLayer().isVisible()))
                    graphicviewerobject.showSelectionHandles(this);
                else
                    graphicviewerobject.hideSelectionHandles(this);
            }

        }
    }

    @SuppressWarnings("rawtypes")
    public void showHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = _fldchar.get(graphicviewerobject);
        if (obj == null)
            return;
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null)
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

    @SuppressWarnings("rawtypes")
    public void hideHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = _fldchar.get(graphicviewerobject);
        if (obj == null)
            return;
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null)
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

    public GraphicViewerHandle createBoundingHandle(
            GraphicViewerObject graphicviewerobject) {
        GraphicViewerView graphicviewerview = getView();
        Rectangle rectangle = graphicviewerview == null ? new Rectangle(0, 0,
                0, 0) : graphicviewerview.c();
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
            if (getPrimarySelection() == graphicviewerobject)
                color = graphicviewerview.getPrimarySelectionColor();
            else
                color = graphicviewerview.getSecondarySelectionColor();
        } else {
            color = GraphicViewerBrush.ColorBlack;
        }
        if (_fldbyte == null || !_fldbyte.getColor().equals(color))
            _fldbyte = GraphicViewerPen.make(65535, 2, color);
        graphicviewerhandle.setPen(_fldbyte);
        graphicviewerhandle.setBrush(null);
        addHandle(graphicviewerobject, graphicviewerhandle);
        return graphicviewerhandle;
    }

    public boolean isResizeHandleSizeInViewCoords() {
        return _fldgoto;
    }

    public void setResizeHandleSizeInViewCoords(boolean flag) {
        _fldgoto = flag;
    }

    public GraphicViewerHandle allocateResizeHandle(
            GraphicViewerObject graphicviewerobject, int i, int j, int k) {
        GraphicViewerView graphicviewerview = getView();
        Rectangle rectangle = graphicviewerview == null ? new Rectangle(0, 0,
                0, 0) : graphicviewerview.c();
        rectangle.width = GraphicViewerHandle.getDefaultHandleWidth();
        rectangle.height = GraphicViewerHandle.getDefaultHandleHeight();
        if (isResizeHandleSizeInViewCoords() && graphicviewerview != null
                && graphicviewerview.getScale() != 1.0D) {
            Dimension dimension = graphicviewerview._mthvoid();
            dimension.width = rectangle.width;
            dimension.height = rectangle.height;
            graphicviewerview.convertViewToDoc(dimension);
            rectangle.width = dimension.width;
            if (rectangle.width < 2)
                rectangle.width = 2;
            rectangle.height = dimension.height;
            if (rectangle.height < 2)
                rectangle.height = 2;
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
        if (k == -1)
            graphicviewerhandle.setSelectable(false);
        else
            graphicviewerhandle.setSelectable(true);
        Color color;
        if (graphicviewerview != null) {
            if (getPrimarySelection() == graphicviewerobject)
                color = graphicviewerview.getPrimarySelectionColor();
            else
                color = graphicviewerview.getSecondarySelectionColor();
        } else {
            color = GraphicViewerBrush.ColorBlack;
        }
        if (flag) {
            graphicviewerhandle.setPen(_fldlong);
            if (_fldnull == null || !_fldnull.getColor().equals(color))
                _fldnull = GraphicViewerBrush.make(65535, color);
            graphicviewerhandle.setBrush(_fldnull);
        } else {
            if (_fldbyte == null || !_fldbyte.getColor().equals(color))
                _fldbyte = GraphicViewerPen.make(65535, 2, color);
            graphicviewerhandle.setPen(_fldbyte);
            graphicviewerhandle.setBrush(null);
        }
        addHandle(graphicviewerobject, graphicviewerhandle);
        return graphicviewerhandle;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addHandle(GraphicViewerObject graphicviewerobject,
            GraphicViewerHandle graphicviewerhandle) {
        graphicviewerhandle.setHandleFor(graphicviewerobject);
        Object obj = _fldchar.get(graphicviewerobject);
        if (obj == null)
            _fldchar.put(graphicviewerobject, graphicviewerhandle);
        else if (obj instanceof ArrayList) {
            ArrayList arraylist = (ArrayList) obj;
            arraylist.add(graphicviewerhandle);
        } else {
            ArrayList arraylist1 = new ArrayList();
            arraylist1.add(obj);
            arraylist1.add(graphicviewerhandle);
            _fldchar.put(graphicviewerobject, arraylist1);
        }
        if (getView() != null)
            getView().addObjectAtHead(graphicviewerhandle);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void deleteHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = _fldchar.get(graphicviewerobject);
        if (obj == null)
            return;
        GraphicViewerView graphicviewerview = getView();
        if (graphicviewerview != null)
            if (obj instanceof ArrayList) {
                ArrayList arraylist = (ArrayList) obj;
                for (int i = 0; i < arraylist.size(); i++) {
                    GraphicViewerHandle graphicviewerhandle1 = (GraphicViewerHandle) arraylist
                            .get(i);
                    graphicviewerview.removeObject(graphicviewerhandle1);
                }

            } else {
                GraphicViewerHandle graphicviewerhandle = (GraphicViewerHandle) obj;
                graphicviewerview.removeObject(graphicviewerhandle);
            }
        _fldchar.put(graphicviewerobject, null);
    }

    @SuppressWarnings("rawtypes")
    public int getNumHandles(GraphicViewerObject graphicviewerobject) {
        Object obj = _fldchar.get(graphicviewerobject);
        if (obj == null)
            return 0;
        if (obj instanceof ArrayList) {
            ArrayList arraylist = (ArrayList) obj;
            return arraylist.size();
        } else {
            return 1;
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        GraphicViewerDocument graphicviewerdocument = a();
        if (graphicviewerdocument != null)
            return graphicviewerdocument.getTransferDataFlavors();
        else
            return new DataFlavor[0];
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor) {
        GraphicViewerDocument graphicviewerdocument = a();
        if (graphicviewerdocument != null)
            return graphicviewerdocument.isDataFlavorSupported(dataflavor);
        else
            return false;
    }

    public synchronized Object getTransferData(DataFlavor dataflavor)
            throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(dataflavor))
            return this;
        else
            throw new UnsupportedFlavorException(dataflavor);
    }

    private GraphicViewerDocument a() {
        GraphicViewerDocument graphicviewerdocument = null;
        if (getView() != null) {
            graphicviewerdocument = getView().getDocument();
        } else {
            GraphicViewerListPosition graphicviewerlistposition = _fldelse
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null)
                    break;
                GraphicViewerObject graphicviewerobject = _fldelse
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = _fldelse
                        .getNextObjectPos(graphicviewerlistposition);
                graphicviewerdocument = graphicviewerobject.getDocument();
                if (graphicviewerdocument != null)
                    break;
                GraphicViewerView graphicviewerview = graphicviewerobject
                        .getView();
                if (graphicviewerview != null)
                    graphicviewerdocument = graphicviewerview.getDocument();
            } while (graphicviewerdocument == null);
        }
        return graphicviewerdocument;
    }

    private GraphicViewerObjList _fldelse;
    @SuppressWarnings("rawtypes")
    private HashMap _fldchar;
    private transient GraphicViewerView _fldcase;
    private GraphicViewerPen _fldbyte;
    private GraphicViewerPen _fldlong;
    private GraphicViewerBrush _fldnull;
    private boolean _fldgoto;
}