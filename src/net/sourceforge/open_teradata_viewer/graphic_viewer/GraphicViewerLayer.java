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

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static final class a {

        void a20091104() {
            _fldif.clear();
            _flddo = new Rectangle(0, 0, 0, 0);
        }

        GraphicViewerView _mthif() {
            return a;
        }

        ArrayList<GraphicViewerObject> _mthfor() {
            return _fldif;
        }

        Rectangle _mthdo() {
            return _flddo;
        }

        void a20091104(Rectangle rectangle) {
            _flddo.x = rectangle.x;
            _flddo.y = rectangle.y;
            _flddo.width = rectangle.width;
            _flddo.height = rectangle.height;
        }

        private transient GraphicViewerView a;
        private transient ArrayList<GraphicViewerObject> _fldif;
        private transient Rectangle _flddo;

        a(GraphicViewerView graphicviewerview) {
            a = null;
            _fldif = null;
            _flddo = null;
            a = graphicviewerview;
            _fldif = new ArrayList<GraphicViewerObject>();
            a20091104();
        }
    }

    public GraphicViewerLayer() {
        l = null;
        i = null;
        c = true;
        k = 1.0F;
        h = true;
        b = null;
        f = 0;
        j = null;
        e = new Rectangle(0, 0, 0, 0);
        d = new Rectangle(0, 0, 0, 0);
    }

    void a(GraphicViewerDocument graphicviewerdocument) {
        m = new GraphicViewerObjList(true);
        g = graphicviewerdocument;
    }

    void a(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        l = graphicviewerlayer;
        i = graphicviewerlayer1;
        if (graphicviewerlayer != null)
            graphicviewerlayer.i = this;
        if (graphicviewerlayer1 != null)
            graphicviewerlayer1.l = this;
    }

    void _mthif() {
        if (i != null)
            i.l = l;
        if (l != null)
            l.i = i;
    }

    public GraphicViewerDocument getDocument() {
        return g;
    }

    public GraphicViewerLayer getNextLayer() {
        return l;
    }

    public GraphicViewerLayer getPrevLayer() {
        return i;
    }

    public int getNumObjects() {
        return m.getNumObjects();
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getView() != null)
            return null;
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument())
                return null;
            GraphicViewerListPosition graphicviewerlistposition = m
                    .getFirstObjectPos();
            if (m.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject)
                return graphicviewerlistposition;
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition2);
            GraphicViewerObject graphicviewerobject1 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.m.removeObjectAtPos(graphicviewerlistposition2);
            GraphicViewerListPosition graphicviewerlistposition4 = m
                    .addObjectAtHead(graphicviewerobject);
            graphicviewerobject.a(this, -1, null, graphicviewerobject);
            if (graphicviewerobject1 != null)
                graphicviewerobject.update(10, 1, graphicviewerobject1);
            else
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            return graphicviewerlistposition4;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = m
                .addObjectAtHead(graphicviewerobject);
        graphicviewerobject.a(this, 6, this, graphicviewerobject);
        if (graphicviewerobject.isTopLevel())
            a(graphicviewerobject, 6, null);
        getDocument().updateDocumentSize(graphicviewerobject);
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getView() != null)
            return null;
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument())
                return null;
            GraphicViewerListPosition graphicviewerlistposition = m
                    .getLastObjectPos();
            if (m.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject)
                return graphicviewerlistposition;
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition2);
            GraphicViewerObject graphicviewerobject1 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.m.removeObjectAtPos(graphicviewerlistposition2);
            GraphicViewerListPosition graphicviewerlistposition4 = m
                    .addObjectAtTail(graphicviewerobject);
            graphicviewerobject.a(this, -1, null, graphicviewerobject);
            if (graphicviewerobject1 != null)
                graphicviewerobject.update(10, 1, graphicviewerobject1);
            else
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            return graphicviewerlistposition4;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = m
                .addObjectAtTail(graphicviewerobject);
        graphicviewerobject.a(this, 2, this, graphicviewerobject);
        if (graphicviewerobject.isTopLevel())
            a(graphicviewerobject, 2, null);
        getDocument().updateDocumentSize(graphicviewerobject);
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getView() != null)
            return null;
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument())
                return null;
            if (m.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject)
                return graphicviewerlistposition;
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition1);
            GraphicViewerObject graphicviewerobject2 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.m.removeObjectAtPos(graphicviewerlistposition1);
            GraphicViewerListPosition graphicviewerlistposition4 = m
                    .insertObjectBefore(graphicviewerlistposition,
                            graphicviewerobject);
            graphicviewerobject.a(this, -1, null, graphicviewerobject);
            if (graphicviewerobject2 != null)
                graphicviewerobject.update(10, 1, graphicviewerobject2);
            else
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            return graphicviewerlistposition4;
        }
        GraphicViewerObject graphicviewerobject1 = m
                .getObjectAtPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = m
                .insertObjectBefore(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.a(this, 8, graphicviewerobject1,
                graphicviewerobject);
        if (graphicviewerobject.isTopLevel())
            a(graphicviewerobject, 8, graphicviewerobject1);
        getDocument().updateDocumentSize(graphicviewerobject);
        return graphicviewerlistposition2;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null)
            return null;
        if (graphicviewerobject.getParent() != null)
            return null;
        if (graphicviewerobject.getView() != null)
            return null;
        if (graphicviewerobject.getDocument() != null) {
            if (graphicviewerobject.getDocument() != getDocument())
                return null;
            if (m.getObjectAtPos(graphicviewerlistposition) == graphicviewerobject)
                return graphicviewerlistposition;
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerlayer
                    .findObject(graphicviewerobject);
            GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerlayer
                    .getNextObjectPos(graphicviewerlistposition1);
            GraphicViewerObject graphicviewerobject2 = graphicviewerlayer
                    .getObjectAtPos(graphicviewerlistposition3);
            graphicviewerlayer.m.removeObjectAtPos(graphicviewerlistposition1);
            GraphicViewerListPosition graphicviewerlistposition4 = m
                    .insertObjectAfter(graphicviewerlistposition,
                            graphicviewerobject);
            graphicviewerobject.a(this, -1, null, graphicviewerobject);
            if (graphicviewerobject2 != null)
                graphicviewerobject.update(10, 1, graphicviewerobject2);
            else
                graphicviewerobject.update(10, 0, graphicviewerlayer);
            return graphicviewerlistposition4;
        }
        GraphicViewerObject graphicviewerobject1 = m
                .getObjectAtPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = m
                .insertObjectAfter(graphicviewerlistposition,
                        graphicviewerobject);
        graphicviewerobject.a(this, 4, graphicviewerobject1,
                graphicviewerobject);
        if (graphicviewerobject.isTopLevel())
            a(graphicviewerobject, 4, graphicviewerobject1);
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
        if (graphicviewerobject == null)
            return;
        if (graphicviewerobject.getLayer() != this)
            return;
        GraphicViewerArea graphicviewerarea = graphicviewerobject.getParent();
        if (graphicviewerarea != null) {
            graphicviewerarea.removeObject(graphicviewerobject);
        } else {
            GraphicViewerListPosition graphicviewerlistposition = findObject(graphicviewerobject);
            if (graphicviewerlistposition != null)
                removeObjectAtPos(graphicviewerlistposition);
        }
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerListPosition graphicviewerlistposition1 = m
                .getNextObjectPos(graphicviewerlistposition);
        GraphicViewerListPosition graphicviewerlistposition2 = m
                .getPrevObjectPos(graphicviewerlistposition);
        GraphicViewerObject graphicviewerobject = m
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject != null) {
            if (graphicviewerobject.isTopLevel())
                _mthdo(graphicviewerobject);
            GraphicViewerObject graphicviewerobject1 = m
                    .getObjectAtPos(graphicviewerlistposition1);
            if (graphicviewerobject1 == null) {
                graphicviewerobject.a(null, 2, graphicviewerobject.getLayer(),
                        graphicviewerobject);
            } else {
                GraphicViewerObject graphicviewerobject2 = m
                        .getObjectAtPos(graphicviewerlistposition2);
                if (graphicviewerobject2 == null) {
                    graphicviewerobject
                            .a(null, 6, graphicviewerobject.getLayer(),
                                    graphicviewerobject);
                } else {
                    Object aobj[] = new Object[3];
                    aobj[0] = graphicviewerobject.getLayer();
                    aobj[1] = graphicviewerobject2;
                    aobj[2] = graphicviewerobject1;
                    graphicviewerobject.a(null, 0, ((Object) (aobj)),
                            graphicviewerobject);
                }
            }
        }
        return graphicviewerobject;
    }

    public void paint(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle) {
        Rectangle rectangle1 = e;
        Rectangle rectangle2 = graphicviewerview.getViewRect();
        a a1 = a(graphicviewerview);
        if (a1 != null && a1._mthdo().equals(rectangle2)) {
            ArrayList<GraphicViewerObject> arraylist = a1._mthfor();
            int i1 = arraylist.size();
            for (int j1 = 0; j1 < i1; j1++) {
                GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) arraylist
                        .get(j1);
                if (graphicviewerobject2.isVisible()) {
                    Rectangle rectangle5 = graphicviewerobject2
                            .getBoundingRect();
                    rectangle1.x = rectangle5.x;
                    rectangle1.y = rectangle5.y;
                    rectangle1.width = rectangle5.width;
                    rectangle1.height = rectangle5.height;
                    graphicviewerobject2.expandRectByPenWidth(rectangle1);
                    if (rectangle1.intersects(rectangle))
                        graphicviewerobject2.paint(graphics2d,
                                graphicviewerview);
                }
            }

        } else if (_mthif(graphicviewerview)) {
            if (a1 == null) {
                a1 = new a(graphicviewerview);
                _mthdo().add(a1);
            } else {
                a1.a20091104();
            }
            a1.a20091104(rectangle2);
            GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null)
                    break;
                GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                Rectangle rectangle3 = graphicviewerobject.getBoundingRect();
                rectangle1.x = rectangle3.x;
                rectangle1.y = rectangle3.y;
                rectangle1.width = rectangle3.width;
                rectangle1.height = rectangle3.height;
                graphicviewerobject.expandRectByPenWidth(rectangle1);
                if (graphicviewerobject.isVisible()
                        && rectangle1.intersects(rectangle))
                    graphicviewerobject.paint(graphics2d, graphicviewerview);
                if (rectangle1.intersects(rectangle2))
                    a1._mthfor().add(graphicviewerobject);
            } while (true);
        } else {
            GraphicViewerListPosition graphicviewerlistposition1 = getFirstObjectPos();
            do {
                if (graphicviewerlistposition1 == null)
                    break;
                GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition1);
                graphicviewerlistposition1 = getNextObjectPosAtTop(graphicviewerlistposition1);
                if (graphicviewerobject1.isVisible()) {
                    Rectangle rectangle4 = graphicviewerobject1
                            .getBoundingRect();
                    rectangle1.x = rectangle4.x;
                    rectangle1.y = rectangle4.y;
                    rectangle1.width = rectangle4.width;
                    rectangle1.height = rectangle4.height;
                    graphicviewerobject1.expandRectByPenWidth(rectangle1);
                    if (rectangle1.intersects(rectangle))
                        graphicviewerobject1.paint(graphics2d,
                                graphicviewerview);
                }
            } while (true);
        }
    }

    ArrayList<a> _mthdo() {
        if (j == null)
            j = new ArrayList<a>();
        return j;
    }

    private boolean _mthif(GraphicViewerView graphicviewerview) {
        return GraphicViewerDocument.eU && !graphicviewerview.isPrinting();
    }

    private a a(GraphicViewerView graphicviewerview) {
        ArrayList<a> arraylist = _mthdo();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            a a1 = (a) arraylist.get(j1);
            if (a1._mthif() == graphicviewerview)
                return a1;
        }

        return null;
    }

    private a a(Point point) {
        a a1 = null;
        ArrayList<a> arraylist = _mthdo();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            a a2 = (a) arraylist.get(j1);
            if (a2._mthdo().contains(point)
                    && (a1 == null || a2._mthfor().size() < a1._mthfor().size()))
                a1 = a2;
        }

        return a1;
    }

    void _mthfor() {
        j = new ArrayList<a>();
    }

    void a(GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        Rectangle rectangle1 = e;
        rectangle1.x = rectangle.x;
        rectangle1.y = rectangle.y;
        rectangle1.width = rectangle.width;
        rectangle1.height = rectangle.height;
        graphicviewerobject.expandRectByPenWidth(rectangle1);
        Rectangle rectangle2 = graphicviewerobject.getBoundingRect();
        Rectangle rectangle3 = d;
        rectangle3.x = rectangle2.x;
        rectangle3.y = rectangle2.y;
        rectangle3.width = rectangle2.width;
        rectangle3.height = rectangle2.height;
        graphicviewerobject.expandRectByPenWidth(rectangle3);
        ArrayList<a> arraylist = _mthdo();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            a a1 = (a) arraylist.get(j1);
            boolean flag = a1._mthdo().intersects(rectangle1);
            boolean flag1 = a1._mthdo().intersects(rectangle3);
            if (!flag && flag1 && !a1._mthfor().contains(graphicviewerobject))
                a1._mthfor().add(graphicviewerobject);
        }

    }

    void a(GraphicViewerObject graphicviewerobject, int i1, Object obj) {
        Rectangle rectangle = graphicviewerobject.getBoundingRect();
        Rectangle rectangle1 = d;
        rectangle1.x = rectangle.x;
        rectangle1.y = rectangle.y;
        rectangle1.width = rectangle.width;
        rectangle1.height = rectangle.height;
        graphicviewerobject.expandRectByPenWidth(rectangle1);
        ArrayList<a> arraylist = _mthdo();
        int j1 = arraylist.size();
        for (int k1 = 0; k1 < j1; k1++) {
            a a1 = (a) arraylist.get(k1);
            boolean flag = a1._mthdo().intersects(rectangle1);
            if (!flag)
                continue;
            if (i1 == 2) {
                a1._mthfor().add(graphicviewerobject);
                continue;
            }
            if (i1 == 6)
                a1._mthfor().add(0, graphicviewerobject);
            else
                a1._mthfor().add(graphicviewerobject);
        }

    }

    void _mthdo(GraphicViewerObject graphicviewerobject) {
        Rectangle rectangle = graphicviewerobject.getBoundingRect();
        Rectangle rectangle1 = e;
        rectangle1.x = rectangle.x;
        rectangle1.y = rectangle.y;
        rectangle1.width = rectangle.width;
        rectangle1.height = rectangle.height;
        graphicviewerobject.expandRectByPenWidth(rectangle1);
        ArrayList<a> arraylist = _mthdo();
        int i1 = arraylist.size();
        for (int j1 = 0; j1 < i1; j1++) {
            a a1 = (a) arraylist.get(j1);
            boolean flag = a1._mthdo().intersects(rectangle1);
            if (flag)
                a1._mthfor().remove(graphicviewerobject);
        }

    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        label0 : {
            if (!isVisible())
                return null;
            a a1 = a(point);
            if (a1 != null) {
                ArrayList<GraphicViewerObject> arraylist = a1._mthfor();
                int i1 = arraylist.size();
                for (int j1 = i1 - 1; j1 >= 0; j1--) {
                    GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) arraylist
                            .get(j1);
                    GraphicViewerObject graphicviewerobject3 = graphicviewerobject2
                            .pick(point, flag);
                    if (graphicviewerobject3 != null)
                        return graphicviewerobject3;
                }

                break label0;
            }
            GraphicViewerListPosition graphicviewerlistposition = getLastObjectPos();
            GraphicViewerObject graphicviewerobject1;
            do {
                if (graphicviewerlistposition == null)
                    break label0;
                GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getPrevObjectPos(graphicviewerlistposition);
                graphicviewerobject1 = graphicviewerobject.pick(point, flag);
            } while (graphicviewerobject1 == null);
            return graphicviewerobject1;
        }
        return null;
    }

    public ArrayList<GraphicViewerObject> pickObjects(Point point,
            boolean flag, ArrayList arraylist, int i1) {
        label0 : {
            if (arraylist == null)
                arraylist = new ArrayList<GraphicViewerObject>();
            if (arraylist.size() >= i1)
                return arraylist;
            if (!isVisible())
                return arraylist;
            a a1 = a(point);
            if (a1 != null) {
                ArrayList<?> arraylist1 = a1._mthfor();
                int j1 = arraylist1.size();
                for (int k1 = j1 - 1; k1 >= 0; k1--) {
                    GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) arraylist1
                            .get(k1);
                    if (graphicviewerobject2 instanceof GraphicViewerArea) {
                        ((GraphicViewerArea) graphicviewerobject2).pickObjects(
                                point, flag, arraylist, i1);
                    } else {
                        GraphicViewerObject graphicviewerobject3 = graphicviewerobject2
                                .pick(point, flag);
                        if (graphicviewerobject3 != null) {
                            if (!arraylist.contains(graphicviewerobject2))
                                arraylist.add(graphicviewerobject2);
                            if (arraylist.size() >= i1)
                                return arraylist;
                        }
                    }
                }

                break label0;
            }
            GraphicViewerListPosition graphicviewerlistposition = getLastObjectPos();
            do {
                GraphicViewerObject graphicviewerobject;
                GraphicViewerObject graphicviewerobject1;
                do {
                    do {
                        if (graphicviewerlistposition == null)
                            break label0;
                        graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                        graphicviewerlistposition = getPrevObjectPos(graphicviewerlistposition);
                        if (!(graphicviewerobject instanceof GraphicViewerArea))
                            break;
                        ((GraphicViewerArea) graphicviewerobject).pickObjects(
                                point, flag, arraylist, i1);
                    } while (true);
                    graphicviewerobject1 = graphicviewerobject
                            .pick(point, flag);
                } while (graphicviewerobject1 == null);
                if (!arraylist.contains(graphicviewerobject))
                    arraylist.add(graphicviewerobject);
            } while (arraylist.size() < i1);
            return arraylist;
        }
        return arraylist;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return m.getFirstObjectPos();
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return m.getLastObjectPos();
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        Object obj = graphicviewerlistposition._flddo;
        if (obj instanceof IGraphicViewerObjectCollection) {
            IGraphicViewerObjectCollection graphicviewerobjectcollection = (IGraphicViewerObjectCollection) obj;
            if (!graphicviewerobjectcollection.isEmpty())
                return graphicviewerobjectcollection.getFirstObjectPos();
        }
        GraphicViewerListPosition graphicviewerlistposition1;
        for (graphicviewerlistposition = graphicviewerlistposition.a; graphicviewerlistposition == null; graphicviewerlistposition = graphicviewerlistposition1.a) {
            GraphicViewerArea graphicviewerarea = ((GraphicViewerObject) (obj))
                    .getParent();
            if (graphicviewerarea == null)
                return null;
            graphicviewerlistposition1 = graphicviewerarea.i();
            obj = graphicviewerarea;
        }

        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        for (Object obj = graphicviewerlistposition._flddo; ((GraphicViewerObject) (obj))
                .getParent() != null; obj = ((GraphicViewerObject) (obj))
                .getParent())
            graphicviewerlistposition = ((GraphicViewerObject) (obj))
                    .getParent().i();

        return graphicviewerlistposition.a;
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return m.getPrevObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return m.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject.getLayer() == this)
            return m.findObject(graphicviewerobject);
        else
            return null;
    }

    int _mthfor(GraphicViewerObject graphicviewerobject) {
        int i1 = 0;
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition)) {
            GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
            if (graphicviewerobject1 == graphicviewerobject)
                return i1;
            i1++;
        }

        return -1;
    }

    public final ArrayList<GraphicViewerObject> addCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            boolean flag, GraphicViewerLayer graphicviewerlayer) {
        ArrayList<GraphicViewerObject> arraylist = new ArrayList<GraphicViewerObject>();
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

    public ArrayList<GraphicViewerObject> addCollection(
            ArrayList<GraphicViewerObject> arraylist, boolean flag,
            GraphicViewerLayer graphicviewerlayer) {
        getDocument().fireUpdate(1902, 0, this, 0, arraylist);
        for (int i1 = 0; i1 < arraylist.size(); i1++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                    .get(i1);
            boolean flag1 = graphicviewerobject.getLayer() != null
                    && (graphicviewerobject.getLayer() != this || !graphicviewerobject
                            .isTopLevel());
            if (flag1) {
                GraphicViewerArea._mthif(graphicviewerobject, true);
                graphicviewerobject.getLayer()
                        .removeObject(graphicviewerobject);
            }
            addObjectAtTail(graphicviewerobject);
            if (flag1)
                GraphicViewerArea._mthif(graphicviewerobject, false);
        }

        getDocument().fireUpdate(1902, 0, this, 1, arraylist);
        if (flag && getDocument() != null)
            GraphicViewerSubGraphBase.reparentAllLinksToSubGraphs(arraylist,
                    true, graphicviewerlayer);
        return arraylist;
    }

    public void removeAll() {
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = getFirstObjectPos())
            removeObjectAtPos(graphicviewerlistposition);

    }

    public boolean isVisible() {
        return c;
    }

    public void setVisible(boolean flag) {
        a(flag, false);
    }

    void a(boolean flag, boolean flag1) {
        boolean flag2 = c;
        if (flag2 != flag) {
            c = flag;
            getDocument().fireUpdate(213, 0, this, flag2 ? 1 : 0, null);
            if (!flag1 && !c) {
                GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos();
                do {
                    if (graphicviewerlistposition == null)
                        break;
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
        return k;
    }

    public void setTransparency(float f1) {
        float f2 = k;
        if (f2 != f1) {
            k = f1;
            getDocument().fireUpdate(215, 0, this, 0, new Float(f2));
        }
    }

    public boolean isModifiable() {
        return h && getDocument().isModifiable();
    }

    public void setModifiable(boolean flag) {
        boolean flag1 = h;
        if (flag1 != flag) {
            h = flag;
            getDocument().fireUpdate(214, 0, this, flag1 ? 1 : 0, null);
        }
    }

    public Object getIdentifier() {
        return b;
    }

    public void setIdentifier(Object obj) {
        Object obj1 = b;
        if (obj1 != obj) {
            b = obj;
            getDocument().fireUpdate(217, 0, this, 0, obj1);
        }
    }

    static final int _fldvoid = 1902;
    private GraphicViewerObjList m;
    private GraphicViewerDocument g;
    private GraphicViewerLayer l;
    private GraphicViewerLayer i;
    boolean c;
    float k;
    boolean h;
    Object b;
    transient int f;
    private transient ArrayList<a> j;
    private Rectangle e;
    private Rectangle d;
}