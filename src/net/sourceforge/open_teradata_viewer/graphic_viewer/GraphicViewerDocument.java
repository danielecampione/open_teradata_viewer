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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import net.sourceforge.open_teradata_viewer.graphic_viewer.svg.DefaultDocument;

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

    private transient boolean myIsLocationModifiable = true;
    private transient boolean myIsModified = false;

    private String myName = "";

    private String myLocation = "";

    // Event hints
    public static final int LOCATION_CHANGED = GraphicViewerDocumentEvent.LAST + 2;
    public static final int NAME_CHANGED = GraphicViewerDocumentEvent.LAST + 1;

    public void setModified(boolean b) {
        if (myIsModified != b) {
            myIsModified = b;
            // don't need to notify document listeners
        }
    }

    // read-only property--can the file be written?
    public boolean isLocationModifiable() {
        return myIsLocationModifiable; // just return cached value
    }

    public void setLocation(String newloc) {
        String oldLocation = myLocation;
        if (!oldLocation.equals(newloc)) {
            myLocation = newloc;
            fireUpdate(LOCATION_CHANGED, 0, null, 0, oldLocation);

            updateLocationModifiable();
        }
    }

    // There's no setLocationModifiable, because that's controlled externally
    // in the file system.  But because we're caching the writableness,
    // we need a method to update the cache.

    public void updateLocationModifiable() {
        boolean canwrite = true;
        if (!getLocation().equals("")) {
            File file = new File(getLocation());
            if (file.exists() && !file.canWrite())
                canwrite = false;
        }
        if (isLocationModifiable() != canwrite) {
            boolean oldIsModifiable = isModifiable();
            myIsLocationModifiable = canwrite;
            if (oldIsModifiable != isModifiable())
                fireUpdate(GraphicViewerDocumentEvent.MODIFIABLE_CHANGED, 0,
                        null, (oldIsModifiable ? 1 : 0), null);
        }
    }

    public String getLocation() {
        return myLocation;
    }

    public void storeSVG1(OutputStream outs, boolean genXMLExtensions,
            boolean genSVG) {
        DefaultDocument svgDomDoc = new DefaultDocument();
        svgDomDoc.setGenerateGraphicViewerXML(genXMLExtensions);
        svgDomDoc.setGenerateSVG(genSVG);
        svgDomDoc.SVGWriteDoc(outs, this);
    }

    public void updatePaperColor() {
        if (isModifiable())
            setPaperColor(new Color(255, 255, 221));
        else
            setPaperColor(new Color(0xDD, 0xDD, 0xDD));
    }

    public String getName() {
        return myName;
    }

    public void setName(String newname) {
        String oldName = myName;
        if (!oldName.equals(newname)) {
            myName = newname;
            fireUpdate(NAME_CHANGED, 0, null, 0, oldName);
        }
    }

    public GraphicViewerDocument loadSVG1(InputStream ins) {
        GraphicViewerDocument doc = new GraphicViewerDocument();
        try {
            DefaultDocument svgDomDoc = new DefaultDocument();
            svgDomDoc.SVGReadDoc(ins, doc);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
        return doc;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class a implements Comparator<Object> {

        public int compare(Object obj, Object obj1) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) obj1;
            if (graphicviewerobject == null || graphicviewerobject1 == null
                    || graphicviewerobject == graphicviewerobject1)
                return 0;
            int i = a.a(graphicviewerobject.getLayer());
            int j = a.a(graphicviewerobject1.getLayer());
            if (i < j)
                return -1;
            if (i > j)
                return 1;
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            if (graphicviewerlayer == null)
                return -1;
            int k = graphicviewerlayer._mthfor(graphicviewerobject
                    .getTopLevelObject());
            int l = graphicviewerlayer._mthfor(graphicviewerobject1
                    .getTopLevelObject());
            if (k < l)
                return -1;
            if (k > l)
                return 1;
            else
                return a20091104(graphicviewerobject.getTopLevelObject(),
                        graphicviewerobject, graphicviewerobject1);
        }

        private int a20091104(GraphicViewerObject graphicviewerobject,
                GraphicViewerObject graphicviewerobject1,
                GraphicViewerObject graphicviewerobject2) {
            label0 : {
                if (graphicviewerobject == graphicviewerobject1)
                    return -1;
                if (graphicviewerobject == graphicviewerobject2)
                    return 1;
                if (!(graphicviewerobject instanceof GraphicViewerArea))
                    break label0;
                GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
                GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                        .getFirstObjectPos();
                int i;
                do {
                    if (graphicviewerlistposition == null)
                        break label0;
                    GraphicViewerObject graphicviewerobject3 = graphicviewerarea
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerarea
                            .getNextObjectPos(graphicviewerlistposition);
                    i = a20091104(graphicviewerobject3, graphicviewerobject1,
                            graphicviewerobject2);
                } while (i == 0);
                return i;
            }
            return 0;
        }

        private GraphicViewerDocument a;

        public a(GraphicViewerDocument graphicviewerdocument) {
            a = graphicviewerdocument;
        }
    }

    public GraphicViewerDocument() {
        fa = null;
        eV = null;
        fc = null;
        e1 = null;
        fb = new Dimension(0, 0);
        eZ = new Point(0, 0);
        e3 = null;
        eY = true;
        e6 = false;
        e7 = false;
        eX = null;
        e5 = null;
        eR = null;
        eW = null;
        e8 = null;
        fd = false;
        e2 = -1;
        eS = null;
        e9 = 0;
        fa = new GraphicViewerLayer();
        fa.a(this);
        eV = fa;
        fc = fa;
        e1 = fa;
        e0 = new HashMap<GraphicViewerNode, Object>();
    }

    public Dimension getDocumentSize() {
        return fb;
    }

    public void setDocumentSize(int i, int j) {
        if (i == -23)
            if (j == -23)
                eU = true;
            else if (j == -24)
                eU = false;
        Dimension dimension = fb;
        if (i >= 0 && j >= 0 && (dimension.width != i || dimension.height != j)) {
            Dimension dimension1 = new Dimension(dimension);
            fb.width = i;
            fb.height = j;
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
        if (i < point.x || j < point.y)
            setDocumentTopLeft(i, j);
        if (i1 > dimension.width || j1 > dimension.height)
            setDocumentSize(i1, j1);
    }

    public Point getDocumentTopLeft() {
        return eZ;
    }

    public void setDocumentTopLeft(int i, int j) {
        Point point = eZ;
        if (point.x != i || point.y != j) {
            Point point1 = new Point(point.x, point.y);
            eZ.x = i;
            eZ.y = j;
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
            if (graphicviewerlistposition == null)
                break;
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
        return e3;
    }

    public void setPaperColor(Color color) {
        Color color1 = e3;
        if (color != color1 && (color == null || !color.equals(color1))) {
            e3 = color;
            fireUpdate(206, 0, null, 0, color1);
        }
    }

    public boolean isModifiable() {
        return eY && isLocationModifiable();
    }

    public void setModifiable(boolean flag) {
        boolean flag1 = eY;
        if (flag1 != flag) {
            eY = flag;
            fireUpdate(208, 0, null, flag1 ? 1 : 0, null);
        }
    }

    public int getNumObjects() {
        int i = 0;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer())
            i += graphicviewerlayer.getNumObjects();

        return i;
    }

    public boolean isEmpty() {
        return getNumObjects() == 0;
    }

    public void add(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return;
        if (graphicviewerobject instanceof GraphicViewerLink)
            getLinksLayer().addObjectAtTail(graphicviewerobject);
        else
            getDefaultLayer().addObjectAtTail(graphicviewerobject);
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
        if (graphicviewerobject == null)
            return;
        GraphicViewerLayer graphicviewerlayer = graphicviewerobject.getLayer();
        if (graphicviewerlayer == null)
            return;
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
                .getNextLayer())
            graphicviewerlayer.removeAll();

    }

    public void deleteContents() {
        for (; getLastLayer() != getFirstLayer(); removeLayer(getLastLayer()));
        getFirstLayer().removeAll();
        setDocumentSize(0, 0);
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getPrevLayer()) {
            GraphicViewerObject graphicviewerobject = graphicviewerlayer
                    .pickObject(point, flag);
            if (graphicviewerobject != null)
                return graphicviewerobject;
        }

        return null;
    }

    public ArrayList<?> pickObjects(Point point, boolean flag,
            ArrayList<?> arraylist, int i) {
        if (arraylist == null)
            arraylist = new ArrayList<Object>();
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getPrevLayer()) {
            if (arraylist.size() >= i)
                return arraylist;
            graphicviewerlayer.pickObjects(point, flag, arraylist, i);
        }

        return arraylist;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            if (graphicviewerlistposition != null)
                return graphicviewerlistposition;
        }

        return null;
    }

    public GraphicViewerListPosition getLastObjectPos() {
        for (GraphicViewerLayer graphicviewerlayer = getLastLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getPrevLayer()) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getLastObjectPos();
            if (graphicviewerlistposition != null)
                return graphicviewerlistposition;
        }

        return null;
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
            if (graphicviewerarea == null) {
                GraphicViewerLayer graphicviewerlayer = ((GraphicViewerObject) (obj))
                        .getLayer();
                for (graphicviewerlayer = graphicviewerlayer.getNextLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                        .getNextLayer()) {
                    GraphicViewerListPosition graphicviewerlistposition2 = graphicviewerlayer
                            .getFirstObjectPos();
                    if (graphicviewerlistposition2 != null)
                        return graphicviewerlistposition2;
                }

                return null;
            }
            graphicviewerlistposition1 = graphicviewerarea.i();
            obj = graphicviewerarea;
        }

        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        Object obj;
        for (obj = graphicviewerlistposition._flddo; ((GraphicViewerObject) (obj))
                .getParent() != null; obj = ((GraphicViewerObject) (obj))
                .getParent())
            graphicviewerlistposition = ((GraphicViewerObject) (obj))
                    .getParent().i();

        graphicviewerlistposition = graphicviewerlistposition.a;
        if (graphicviewerlistposition == null) {
            GraphicViewerLayer graphicviewerlayer = ((GraphicViewerObject) (obj))
                    .getLayer();
            for (graphicviewerlayer = graphicviewerlayer.getNextLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                    .getNextLayer()) {
                GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerlayer
                        .getFirstObjectPos();
                if (graphicviewerlistposition1 != null)
                    return graphicviewerlistposition1;
            }

        }
        return graphicviewerlistposition;
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        else
            return graphicviewerlistposition._fldif;
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        else
            return graphicviewerlistposition._flddo;
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .findObject(graphicviewerobject);
            if (graphicviewerlistposition != null)
                return graphicviewerlistposition;
        }

        return null;
    }

    public void addDocumentListener(
            IGraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (eX == null)
            eX = new ArrayList<IGraphicViewerDocumentListener>();
        if (!eX.contains(graphicviewerdocumentlistener))
            eX.add(graphicviewerdocumentlistener);
    }

    public void removeDocumentListener(
            IGraphicViewerDocumentListener graphicviewerdocumentlistener) {
        if (eX == null)
            return;
        int i = eX.indexOf(graphicviewerdocumentlistener);
        if (i >= 0)
            eX.remove(i);
    }

    public IGraphicViewerDocumentListener[] getDocumentListeners() {
        if (eX == null)
            return new IGraphicViewerDocumentListener[0];
        Object aobj[] = eX.toArray();
        IGraphicViewerDocumentListener agraphicviewerdocumentlistener[] = new IGraphicViewerDocumentListener[aobj.length];
        for (int i = 0; i < aobj.length; i++)
            agraphicviewerdocumentlistener[i] = (IGraphicViewerDocumentListener) aobj[i];

        return agraphicviewerdocumentlistener;
    }

    public void fireUpdate(int i, int j, Object obj, int k, Object obj1) {
        a(i, j, obj, k, obj1);
    }

    void a(int i, int j, Object obj, int k, Object obj1) {
        if (i == 203 && j == 1) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
            _mthchar(graphicviewerobject);
            if (graphicviewerobject.isTopLevel()) {
                GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                        .getLayer();
                if (graphicviewerlayer != null)
                    graphicviewerlayer.a(graphicviewerobject, (Rectangle) obj1);
            }
        } else if (i == 202) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) obj;
            if (isMaintainsPartID())
                _mthcase(graphicviewerobject1);
            _mthchar(graphicviewerobject1);
        } else if (i == 204) {
            GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) obj;
            _mthelse(graphicviewerobject2);
            _mthchar(graphicviewerobject2);
        } else if (i == 210)
            _mthchar(null);
        else if (i == 211)
            _mthchar(null);
        if (isSuspendUpdates())
            return;
        if (e5 == null) {
            e5 = new GraphicViewerDocumentEvent(this, i, j, obj, k, obj1);
        } else {
            e5._mthif(i);
            e5.a(j);
            e5.a(obj);
            e5._mthdo(k);
            e5._mthif(obj1);
        }
        GraphicViewerDocumentEvent graphicviewerdocumentevent = e5;
        e5 = null;
        ArrayList<IGraphicViewerDocumentListener> arraylist = eX;
        if (arraylist != null) {
            int l = arraylist.size();
            for (int i1 = 0; i1 < l; i1++) {
                IGraphicViewerDocumentListener graphicviewerdocumentlistener = (IGraphicViewerDocumentListener) arraylist
                        .get(i1);
                graphicviewerdocumentlistener
                        .documentChanged(graphicviewerdocumentevent);
            }

        }
        e5 = graphicviewerdocumentevent;
        e5.a(null);
    }

    public void fireForedate(int i, int j, Object obj) {
        i |= 0x8000;
        a(i, j, obj, 0, null);
    }

    public final void update() {
        fireUpdate(100, 0, null, 0, null);
    }

    public boolean isSuspendUpdates() {
        return e6;
    }

    public void setSuspendUpdates(boolean flag) {
        e6 = flag;
        if (!flag) {
            _mthchar(null);
            for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                    .getNextLayer())
                graphicviewerlayer._mthfor();

            update();
        }
    }

    public boolean isSkipsUndoManager() {
        return e7;
    }

    public void setSkipsUndoManager(boolean flag) {
        e7 = flag;
    }

    public boolean isMaintainsPartID() {
        return fd;
    }

    public void setMaintainsPartID(boolean flag) {
        _mthint(flag, false);
    }

    private void _mthint(boolean flag, boolean flag1) {
        boolean flag2 = fd;
        if (flag2 != flag) {
            fd = flag;
            fireUpdate(219, 0, null, flag2 ? 1 : 0, null);
            if (!flag1)
                if (flag)
                    ensureUniquePartID();
                else
                    eS = null;
        }
    }

    public IGraphicViewerIdentifiablePart findPart(int i) {
        if (eS != null)
            return (IGraphicViewerIdentifiablePart) eS.get(new Integer(i));
        else
            return null;
    }

    void a(IGraphicViewerIdentifiablePart graphicvieweridentifiablepart) {
        if (eS == null)
            eS = new HashMap<Integer, IGraphicViewerIdentifiablePart>(1000);
        int i = graphicvieweridentifiablepart.getPartID();
        Integer integer = null;
        if (i != -1) {
            integer = new Integer(i);
            if (eS.get(integer) == graphicvieweridentifiablepart)
                return;
            if (eS.get(integer) == null) {
                eS.put(integer, graphicvieweridentifiablepart);
                return;
            }
        }
        i = ++e2;
        for (integer = new Integer(i); eS.get(integer) != null; integer = new Integer(
                i))
            i = ++e2;

        eS.put(integer, graphicvieweridentifiablepart);
        graphicvieweridentifiablepart.setPartID(i);
    }

    void _mthif(IGraphicViewerIdentifiablePart graphicvieweridentifiablepart) {
        if (eS != null)
            eS.remove(new Integer(graphicvieweridentifiablepart.getPartID()));
    }

    public void ensureUniquePartID() {
        if (isMaintainsPartID()) {
            for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                _mthcase(graphicviewerobject);
            }

        }
    }

    void _mthcase(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject instanceof IGraphicViewerIdentifiablePart) {
            IGraphicViewerIdentifiablePart graphicvieweridentifiablepart = (IGraphicViewerIdentifiablePart) graphicviewerobject;
            a(graphicvieweridentifiablepart);
        }
        if (graphicviewerobject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerarea
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                _mthcase(graphicviewerobject1);
            }

        }
    }

    void _mthelse(GraphicViewerObject graphicviewerobject) {
        if (eS != null) {
            if (graphicviewerobject instanceof IGraphicViewerIdentifiablePart) {
                IGraphicViewerIdentifiablePart graphicvieweridentifiablepart = (IGraphicViewerIdentifiablePart) graphicviewerobject;
                _mthif(graphicvieweridentifiablepart);
            }
            if (graphicviewerobject instanceof GraphicViewerArea) {
                GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
                for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                        .getFirstObjectPos(); graphicviewerlistposition != null;) {
                    GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                            .getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerarea
                            .getNextObjectPosAtTop(graphicviewerlistposition);
                    _mthelse(graphicviewerobject1);
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
                    if (graphicviewerdocumentchangededit1 != null)
                        graphicviewerdocumentchangededit
                                .setOldValue(graphicviewerdocumentchangededit1
                                        .getNewValue());
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
                ArrayList<Serializable> arraylist = new ArrayList<Serializable>();
                int i = 0;
                for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                    GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                    arraylist.add(i++, graphicviewerobject1);
                    if (graphicviewerobject1 instanceof GraphicViewerLink) {
                        GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject1;
                        Vector<?> vector = graphicviewerlink.copyPoints();
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
                && graphicviewerdocumentchangededit.getHint() != 65536)
            throw new IllegalArgumentException(
                    "unknown GraphicViewerDocumentEvent hint: "
                            + Integer.toString(graphicviewerdocumentchangededit
                                    .getHint()));
        else
            return;
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
                    a(graphicviewerdocumentchangededit);
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
                    a(graphicviewerdocumentchangededit);
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
                            _mthdo(graphicviewerlayer7, graphicviewerlayer);
                            break;

                        case 4 : // '\004'
                            _mthif(graphicviewerlayer7, graphicviewerlayer);
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
                    if (graphicviewerlayer8 != null)
                        _mthdo(graphicviewerlayer8, graphicviewerlayer1);
                    else
                        insertLayerAfter(getLastLayer(), graphicviewerlayer1);
                } else {
                    removeLayer(graphicviewerlayer1);
                }
                return;

            case 212 :
                GraphicViewerLayer graphicviewerlayer2 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getObject();
                GraphicViewerLayer graphicviewerlayer9 = (GraphicViewerLayer) graphicviewerdocumentchangededit
                        .getValue(flag);
                if (graphicviewerlayer9 != null)
                    insertLayerBefore(graphicviewerlayer9, graphicviewerlayer2);
                else
                    insertLayerAfter(getLastLayer(), graphicviewerlayer2);
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
                ArrayList<?> arraylist = (ArrayList<?>) graphicviewerdocumentchangededit
                        .getValue(flag);
                for (int i = 0; i < arraylist.size(); i += 2) {
                    GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) arraylist
                            .get(i);
                    if (graphicviewerobject3 instanceof GraphicViewerLink) {
                        Vector<?> vector = (Vector<?>) arraylist.get(i + 1);
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
                _mthint(graphicviewerdocumentchangededit.getValueBoolean(flag),
                        true);
                return;

            case 220 :
                setValidCycle(graphicviewerdocumentchangededit
                        .getValueInt(flag));
                return;

            case 1902 :
                ArrayList<?> arraylist1 = (ArrayList<?>) graphicviewerdocumentchangededit
                        .getOldValue();
                boolean flag1 = flag
                        ? graphicviewerdocumentchangededit.getOldValueInt() == 1
                        : graphicviewerdocumentchangededit.getOldValueInt() == 0;
                for (int j = 0; j < arraylist1.size(); j++) {
                    GraphicViewerObject graphicviewerobject4 = (GraphicViewerObject) arraylist1
                            .get(j);
                    GraphicViewerArea._mthif(graphicviewerobject4, flag1);
                }

                return;
        }
        if (graphicviewerdocumentchangededit.getHint() >= 65535)
            throw new IllegalArgumentException(
                    "unknown GraphicViewerDocumentEvent hint: "
                            + Integer.toString(graphicviewerdocumentchangededit
                                    .getHint()));
        else
            return;
    }

    void a(GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
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
                if (graphicviewerobjectcollection instanceof GraphicViewerArea)
                    ((GraphicViewerArea) graphicviewerobjectcollection)._mthdo(
                            graphicviewerobject, true);
                else
                    graphicviewerobjectcollection
                            .addObjectAtTail(graphicviewerobject);
                break;

            case 6 : // '\006'
                IGraphicViewerObjectCollection graphicviewerobjectcollection1 = (IGraphicViewerObjectCollection) graphicviewerdocumentchangededit
                        .getOldValue();
                if (graphicviewerobjectcollection1 instanceof GraphicViewerArea)
                    ((GraphicViewerArea) graphicviewerobjectcollection1).a(
                            graphicviewerobject, true);
                else
                    graphicviewerobjectcollection1
                            .addObjectAtHead(graphicviewerobject);
                break;

            case 8 : // '\b'
                GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getOldValue();
                Object obj = graphicviewerobject1.getParent();
                if (obj == null)
                    obj = graphicviewerobject1.getLayer();
                if (obj == null)
                    break;
                GraphicViewerListPosition graphicviewerlistposition = ((IGraphicViewerObjectCollection) (obj))
                        .findObject(graphicviewerobject1);
                if (graphicviewerlistposition != null) {
                    if (obj instanceof GraphicViewerArea)
                        ((GraphicViewerArea) obj)._mthif(
                                graphicviewerlistposition, graphicviewerobject,
                                true);
                    else
                        ((IGraphicViewerObjectCollection) (obj))
                                .insertObjectBefore(graphicviewerlistposition,
                                        graphicviewerobject);
                    break;
                }
                if (obj instanceof GraphicViewerArea)
                    ((GraphicViewerArea) obj)._mthdo(graphicviewerobject, true);
                else
                    ((IGraphicViewerObjectCollection) (obj))
                            .addObjectAtTail(graphicviewerobject);
                break;

            case 4 : // '\004'
                GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) graphicviewerdocumentchangededit
                        .getOldValue();
                Object obj1 = graphicviewerobject2.getParent();
                if (obj1 == null)
                    obj1 = graphicviewerobject2.getLayer();
                if (obj1 == null)
                    break;
                GraphicViewerListPosition graphicviewerlistposition1 = ((IGraphicViewerObjectCollection) (obj1))
                        .findObject(graphicviewerobject2);
                if (graphicviewerlistposition1 != null) {
                    if (obj1 instanceof GraphicViewerArea)
                        ((GraphicViewerArea) obj1).a(
                                graphicviewerlistposition1,
                                graphicviewerobject, true);
                    else
                        ((IGraphicViewerObjectCollection) (obj1))
                                .insertObjectAfter(graphicviewerlistposition1,
                                        graphicviewerobject);
                    break;
                }
                if (obj1 instanceof GraphicViewerArea)
                    ((GraphicViewerArea) obj1)
                            ._mthdo(graphicviewerobject, true);
                else
                    ((IGraphicViewerObjectCollection) (obj1))
                            .addObjectAtTail(graphicviewerobject);
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
                        if (graphicviewerobjectcollection2 instanceof GraphicViewerArea)
                            ((GraphicViewerArea) graphicviewerobjectcollection2)
                                    .a(graphicviewerlistposition2,
                                            graphicviewerobject, true);
                        else
                            graphicviewerobjectcollection2.insertObjectAfter(
                                    graphicviewerlistposition2,
                                    graphicviewerobject);
                        return;
                    }
                }
                if (aobj[2] != null) {
                    GraphicViewerObject graphicviewerobject4 = (GraphicViewerObject) aobj[2];
                    GraphicViewerListPosition graphicviewerlistposition3 = graphicviewerobjectcollection2
                            .findObject(graphicviewerobject4);
                    if (graphicviewerlistposition3 != null) {
                        if (graphicviewerobjectcollection2 instanceof GraphicViewerArea)
                            ((GraphicViewerArea) graphicviewerobjectcollection2)
                                    ._mthif(graphicviewerlistposition3,
                                            graphicviewerobject, true);
                        else
                            graphicviewerobjectcollection2.insertObjectBefore(
                                    graphicviewerlistposition3,
                                    graphicviewerobject);
                        return;
                    }
                }
                if (graphicviewerobjectcollection2 instanceof GraphicViewerArea)
                    ((GraphicViewerArea) graphicviewerobjectcollection2)
                            ._mthdo(graphicviewerobject, true);
                else
                    graphicviewerobjectcollection2
                            .addObjectAtTail(graphicviewerobject);
                break;
        }
    }

    public static DataFlavor getStandardDataFlavor() {
        if (e4 == null)
            try {
                e4 = new DataFlavor(
                        Class.forName("net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSelection"),
                        "GraphicViewer Selection");
                DataFlavor adataflavor[] = {e4};
                eT = adataflavor;
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        return e4;
    }

    public DataFlavor[] getTransferDataFlavors() {
        getStandardDataFlavor();
        return eT;
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor) {
        return dataflavor.equals(getStandardDataFlavor());
    }

    public synchronized Object getTransferData(DataFlavor dataflavor)
            throws UnsupportedFlavorException, IOException {
        if (dataflavor.equals(getStandardDataFlavor()))
            return this;
        else
            throw new UnsupportedFlavorException(dataflavor);
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
        if (graphicviewercopyenvironment == null)
            graphicviewercopyenvironment = createDefaultCopyEnvironment();
        Point point1 = new Point(0, 0);
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null)
                break;
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
                if (graphicviewerlayer != null)
                    if (graphicviewerlayer.getDocument() == this)
                        graphicviewerlayer1 = graphicviewerlayer;
                    else
                        graphicviewerlayer1 = findLayer(graphicviewerlayer
                                .getIdentifier());
                if (graphicviewerlayer1 == null)
                    graphicviewerlayer1 = getDefaultLayer();
                graphicviewerlayer1.addObjectAtTail(graphicviewerobject1);
            }
        } while (true);
        graphicviewercopyenvironment.finishDelayedCopies();
        return graphicviewercopyenvironment;
    }

    public int getNumLayers() {
        int i = 0;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer())
            i++;

        return i;
    }

    public GraphicViewerLayer getFirstLayer() {
        return fc;
    }

    public GraphicViewerLayer getLastLayer() {
        return e1;
    }

    public GraphicViewerLayer getNextLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null)
            return null;
        else
            return graphicviewerlayer.getNextLayer();
    }

    public GraphicViewerLayer getPrevLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null)
            return null;
        else
            return graphicviewerlayer.getPrevLayer();
    }

    public GraphicViewerLayer getDefaultLayer() {
        return fa;
    }

    public void setDefaultLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer.getDocument() != this) {
            return;
        } else {
            fa = graphicviewerlayer;
            return;
        }
    }

    public GraphicViewerLayer getLinksLayer() {
        return eV;
    }

    public void setLinksLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer.getDocument() != this) {
            return;
        } else {
            eV = graphicviewerlayer;
            return;
        }
    }

    public GraphicViewerLayer addLayerAfter(
            GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null)
            graphicviewerlayer = getLastLayer();
        GraphicViewerLayer graphicviewerlayer1 = new GraphicViewerLayer();
        graphicviewerlayer1.a(this);
        return _mthif(graphicviewerlayer, graphicviewerlayer1);
    }

    GraphicViewerLayer _mthif(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        graphicviewerlayer1.a(graphicviewerlayer.getNextLayer(),
                graphicviewerlayer);
        if (graphicviewerlayer1.getNextLayer() == null)
            e1 = graphicviewerlayer1;
        fireUpdate(210, 0, graphicviewerlayer1, 4, graphicviewerlayer);
        return graphicviewerlayer1;
    }

    public GraphicViewerLayer addLayerBefore(
            GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer == null)
            graphicviewerlayer = getFirstLayer();
        GraphicViewerLayer graphicviewerlayer1 = new GraphicViewerLayer();
        graphicviewerlayer1.a(this);
        return _mthdo(graphicviewerlayer, graphicviewerlayer1);
    }

    GraphicViewerLayer _mthdo(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        graphicviewerlayer1.a(graphicviewerlayer,
                graphicviewerlayer.getPrevLayer());
        if (graphicviewerlayer1.getPrevLayer() == null)
            fc = graphicviewerlayer1;
        fireUpdate(210, 0, graphicviewerlayer1, 8, graphicviewerlayer);
        return graphicviewerlayer1;
    }

    public void removeLayer(GraphicViewerLayer graphicviewerlayer) {
        if (graphicviewerlayer.getDocument() != this)
            return;
        if (graphicviewerlayer == getFirstLayer()
                && graphicviewerlayer == getLastLayer())
            return;
        GraphicViewerLayer graphicviewerlayer1 = graphicviewerlayer
                .getNextLayer();
        graphicviewerlayer.removeAll();
        if (graphicviewerlayer.getPrevLayer() == null)
            fc = graphicviewerlayer1;
        if (graphicviewerlayer1 == null)
            e1 = graphicviewerlayer.getPrevLayer();
        graphicviewerlayer._mthif();
        if (graphicviewerlayer == getLinksLayer())
            if (graphicviewerlayer.getNextLayer() != null)
                setLinksLayer(graphicviewerlayer.getNextLayer());
            else if (graphicviewerlayer.getPrevLayer() != null)
                setLinksLayer(graphicviewerlayer.getPrevLayer());
            else
                setLinksLayer(getLastLayer());
        if (graphicviewerlayer == getDefaultLayer())
            if (graphicviewerlayer.getNextLayer() != null)
                setDefaultLayer(graphicviewerlayer.getNextLayer());
            else if (graphicviewerlayer.getPrevLayer() != null)
                setDefaultLayer(graphicviewerlayer.getPrevLayer());
            else
                setDefaultLayer(getLastLayer());
        fireUpdate(211, 0, graphicviewerlayer, 0, graphicviewerlayer1);
    }

    public void insertLayerAfter(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        if (graphicviewerlayer.getDocument() != this
                || graphicviewerlayer1.getDocument() != this
                || graphicviewerlayer == graphicviewerlayer1
                || graphicviewerlayer1.getPrevLayer() == graphicviewerlayer)
            return;
        GraphicViewerLayer graphicviewerlayer2 = graphicviewerlayer1
                .getNextLayer();
        if (fc == graphicviewerlayer1)
            fc = graphicviewerlayer2;
        graphicviewerlayer1._mthif();
        graphicviewerlayer1.a(graphicviewerlayer.getNextLayer(),
                graphicviewerlayer);
        if (e1 == graphicviewerlayer)
            e1 = graphicviewerlayer1;
        fireUpdate(212, 0, graphicviewerlayer1, 0, graphicviewerlayer2);
    }

    public void insertLayerBefore(GraphicViewerLayer graphicviewerlayer,
            GraphicViewerLayer graphicviewerlayer1) {
        if (graphicviewerlayer.getDocument() != this
                || graphicviewerlayer1.getDocument() != this
                || graphicviewerlayer == graphicviewerlayer1
                || graphicviewerlayer1.getNextLayer() == graphicviewerlayer)
            return;
        GraphicViewerLayer graphicviewerlayer2 = graphicviewerlayer1
                .getNextLayer();
        if (e1 == graphicviewerlayer1)
            e1 = graphicviewerlayer1.getPrevLayer();
        graphicviewerlayer1._mthif();
        graphicviewerlayer1.a(graphicviewerlayer,
                graphicviewerlayer.getPrevLayer());
        if (fc == graphicviewerlayer)
            fc = graphicviewerlayer1;
        fireUpdate(212, 0, graphicviewerlayer1, 0, graphicviewerlayer2);
    }

    public void bringLayerToFront(GraphicViewerLayer graphicviewerlayer) {
        insertLayerAfter(getLastLayer(), graphicviewerlayer);
    }

    public void sendLayerToBack(GraphicViewerLayer graphicviewerlayer) {
        insertLayerBefore(getFirstLayer(), graphicviewerlayer);
    }

    public GraphicViewerLayer findLayer(Object obj) {
        if (obj == null)
            return null;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer()) {
            Object obj1 = graphicviewerlayer.getIdentifier();
            if (obj1 != null && obj1.equals(obj))
                return graphicviewerlayer;
        }

        return null;
    }

    void _mthdo(GraphicViewerDocument graphicviewerdocument) {
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
        if (graphicviewerlayer2 != null)
            setDefaultLayer(graphicviewerlayer2);
        Object obj2 = graphicviewerdocument.getLinksLayer().getIdentifier();
        GraphicViewerLayer graphicviewerlayer3 = findLayer(obj2);
        if (graphicviewerlayer3 != null)
            setLinksLayer(graphicviewerlayer3);
    }

    public void sortByZOrder(GraphicViewerObject agraphicviewerobject[]) {
        if (agraphicviewerobject.length <= 1) {
            return;
        } else {
            F();
            a a1 = new a(this);
            Arrays.sort(agraphicviewerobject, a1);
            return;
        }
    }

    void F() {
        int i = 0;
        for (GraphicViewerLayer graphicviewerlayer = getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerlayer
                .getNextLayer())
            graphicviewerlayer.f = i++;

    }

    int a(GraphicViewerLayer graphicviewerlayer) {
        return graphicviewerlayer.f;
    }

    public GraphicViewerUndoManager getUndoManager() {
        return eR;
    }

    public void setUndoManager(GraphicViewerUndoManager graphicviewerundomanager) {
        GraphicViewerUndoManager graphicviewerundomanager1 = eR;
        if (graphicviewerundomanager1 != graphicviewerundomanager) {
            if (graphicviewerundomanager1 != null)
                removeDocumentListener(graphicviewerundomanager1);
            eR = graphicviewerundomanager;
            if (graphicviewerundomanager != null)
                addDocumentListener(graphicviewerundomanager);
        }
    }

    public boolean canUndo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null)
            return graphicviewerundomanager.canUndo();
        else
            return false;
    }

    public void undo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null)
            try {
                javax.swing.undo.UndoableEdit undoableedit = graphicviewerundomanager
                        ._mthnew();
                fireUpdate(107, 0, undoableedit, 0, null);
                graphicviewerundomanager.undo();
                fireUpdate(108, 0, undoableedit, 0, null);
            } catch (CannotUndoException cue) {
                ExceptionDialog.ignoreException(cue);
            }
    }

    public boolean canRedo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null)
            return graphicviewerundomanager.canRedo();
        else
            return false;
    }

    public void redo() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null)
            try {
                javax.swing.undo.UndoableEdit undoableedit = graphicviewerundomanager
                        ._mthint();
                fireUpdate(109, 0, undoableedit, 0, null);
                graphicviewerundomanager.redo();
                fireUpdate(110, 0, undoableedit, 0, null);
            } catch (CannotRedoException cre) {
                ExceptionDialog.ignoreException(cre);
            }
    }

    public void startTransaction() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            graphicviewerundomanager.startTransaction();
            if (graphicviewerundomanager.getTransactionLevel() == 1)
                fireUpdate(104, 0, null, 0, null);
        }
    }

    public void endTransaction(boolean flag) {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            GraphicViewerUndoManager.GraphicViewerCompoundEdit graphicviewercompoundedit = graphicviewerundomanager
                    .getCurrentEdit();
            graphicviewerundomanager.endTransaction(flag);
            if (graphicviewerundomanager.getTransactionLevel() == 0
                    && graphicviewercompoundedit != null)
                if (flag)
                    fireUpdate(105, 0, graphicviewercompoundedit, 0, null);
                else
                    fireUpdate(106, 0, null, 0, null);
        }
    }

    public void endTransaction(String s) {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null) {
            GraphicViewerUndoManager.GraphicViewerCompoundEdit graphicviewercompoundedit = graphicviewerundomanager
                    .getCurrentEdit();
            graphicviewerundomanager.endTransaction(s);
            if (graphicviewerundomanager.getTransactionLevel() == 0
                    && graphicviewercompoundedit != null)
                fireUpdate(105, 0, graphicviewercompoundedit, 0, s);
        }
    }

    public void discardAllEdits() {
        GraphicViewerUndoManager graphicviewerundomanager = getUndoManager();
        if (graphicviewerundomanager != null)
            graphicviewerundomanager.discardAllEdits();
    }

    public boolean isAvoidable(GraphicViewerObject graphicviewerobject) {
        if (!graphicviewerobject.isVisible())
            return false;
        else
            return graphicviewerobject instanceof GraphicViewerNode;
    }

    public Rectangle getAvoidableRectangle(
            GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        if (rectangle == null)
            rectangle = new Rectangle(0, 0, 0, 0);
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
        if (graphicviewerobject != e8) {
            _mthchar(null);
            e8 = graphicviewerobject;
        }
        GraphicViewerPositionArray graphicviewerpositionarray = a(false,
                graphicviewerobject);
        return graphicviewerpositionarray.isUnoccupied(rectangle.x,
                rectangle.y, rectangle.width, rectangle.height);
    }

    GraphicViewerPositionArray E() {
        return a(true, null);
    }

    GraphicViewerPositionArray a(boolean flag,
            GraphicViewerObject graphicviewerobject) {
        if (eW == null)
            eW = new GraphicViewerPositionArray();
        if (eW.isInvalid()) {
            Rectangle rectangle = computeBounds();
            rectangle.x -= 100;
            rectangle.y -= 100;
            rectangle.width += 200;
            rectangle.height += 200;
            eW.initialize(rectangle);
            for (GraphicViewerListPosition graphicviewerlistposition = getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = getNextObjectPosAtTop(graphicviewerlistposition);
                a(graphicviewerobject1, rectangle, graphicviewerobject);
            }

            eW.setInvalid(false);
        } else if (flag) {
            eW.setAllUnoccupied(0x7fffffff);
        }
        return eW;
    }

    private void a(GraphicViewerObject graphicviewerobject,
            Rectangle rectangle, GraphicViewerObject graphicviewerobject1) {
        if (graphicviewerobject == graphicviewerobject1)
            return;
        if (graphicviewerobject instanceof GraphicViewerSubGraphBase) {
            GraphicViewerSubGraphBase graphicviewersubgraphbase = (GraphicViewerSubGraphBase) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewersubgraphbase
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject2 = graphicviewersubgraphbase
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewersubgraphbase
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                a(graphicviewerobject2, rectangle, graphicviewerobject1);
            }

        } else if (isAvoidable(graphicviewerobject)) {
            rectangle = getAvoidableRectangle(graphicviewerobject, rectangle);
            int i = eW.getCellSize().width;
            int j = eW.getCellSize().height;
            for (int k = rectangle.x; k <= rectangle.x + rectangle.width; k += i) {
                for (int l = rectangle.y; l <= rectangle.y + rectangle.height; l += j)
                    eW.setDist(k, l, 0);

            }
        }
    }

    private void _mthchar(GraphicViewerObject graphicviewerobject) {
        e8 = null;
        if (eW != null
                && !eW.isInvalid()
                && (graphicviewerobject == null || isAvoidable(graphicviewerobject)))
            eW.setInvalid(true);
    }

    public void setValidCycle(int i) {
        int j = e9;
        if (j != i) {
            e9 = i;
            fireUpdate(220, 0, null, j, null);
        }
    }

    public int getValidCycle() {
        return e9;
    }

    public static boolean makesDirectedCycleFast(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1) {
        if (graphicviewernode == graphicviewernode1)
            return true;
        ArrayList<?> arraylist = graphicviewernode1.findAll(16, null);
        for (Iterator<?> iterator = arraylist.iterator(); iterator.hasNext();) {
            GraphicViewerNode graphicviewernode2 = (GraphicViewerNode) iterator
                    .next();
            if (graphicviewernode2 != graphicviewernode1
                    && makesDirectedCycleFast(graphicviewernode,
                            graphicviewernode2))
                return true;
        }

        return false;
    }

    public static boolean makesDirectedCycle(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        } else {
            e0.clear();
            e0.put(graphicviewernode, null);
            boolean flag = _mthif(graphicviewernode, graphicviewernode1, e0);
            e0.clear();
            return flag;
        }
    }

    private static boolean _mthif(GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1,
            HashMap<GraphicViewerNode, ?> hashmap) {
        if (graphicviewernode == graphicviewernode1)
            return true;
        if (hashmap.containsKey(graphicviewernode1))
            return false;
        hashmap.put(graphicviewernode1, null);
        ArrayList<?> arraylist = graphicviewernode1.findAll(16, null);
        for (Iterator<?> iterator = arraylist.iterator(); iterator.hasNext();) {
            GraphicViewerNode graphicviewernode2 = (GraphicViewerNode) iterator
                    .next();
            if (graphicviewernode2 != graphicviewernode1
                    && _mthif(graphicviewernode, graphicviewernode2, hashmap))
                return true;
        }

        return false;
    }

    public static boolean makesUndirectedCycle(
            GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1) {
        if (graphicviewernode == graphicviewernode1) {
            return true;
        } else {
            e0.clear();
            e0.put(graphicviewernode, null);
            boolean flag = a(graphicviewernode, graphicviewernode1, e0);
            e0.clear();
            return flag;
        }
    }

    private static boolean a(GraphicViewerNode graphicviewernode,
            GraphicViewerNode graphicviewernode1,
            HashMap<GraphicViewerNode, ?> hashmap) {
        if (graphicviewernode == graphicviewernode1)
            return true;
        if (hashmap.containsKey(graphicviewernode1))
            return false;
        hashmap.put(graphicviewernode1, null);
        ArrayList<?> arraylist = graphicviewernode1.findAll(24, null);
        for (Iterator<?> iterator = arraylist.iterator(); iterator.hasNext();) {
            GraphicViewerNode graphicviewernode2 = (GraphicViewerNode) iterator
                    .next();
            if (graphicviewernode2 != graphicviewernode1
                    && a(graphicviewernode, graphicviewernode2, hashmap))
                return true;
        }

        return false;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument",
                            domelement);
            domelement1.setAttribute("docwidth", Integer.toString(fb.width));
            domelement1.setAttribute("docheight", Integer.toString(fb.height));
            domelement1.setAttribute("docleft", Integer.toString(eZ.x));
            domelement1.setAttribute("doctop", Integer.toString(eZ.y));
            if (e3 != null) {
                int i = e3.getRed();
                int j = e3.getGreen();
                int k = e3.getBlue();
                int l = e3.getAlpha();
                String s = "rgbalpha(" + Integer.toString(i) + ","
                        + Integer.toString(j) + "," + Integer.toString(k) + ","
                        + Integer.toString(l) + ")";
                domelement1.setAttribute("papercolor", s);
            }
            domelement1.setAttribute("modifiable", eY ? "true" : "false");
            domdoc.registerReferencingNode(domelement1, "defaultlayer",
                    getDefaultLayer());
        }
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            fb.width = Integer.parseInt(domelement1.getAttribute("docwidth"));
            fb.height = Integer.parseInt(domelement1.getAttribute("docheight"));
            eZ.x = Integer.parseInt(domelement1.getAttribute("docleft"));
            eZ.y = Integer.parseInt(domelement1.getAttribute("doctop"));
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
            eY = domelement1.getAttribute("modifiable").equals("true");
            String s1 = domelement1.getAttribute("defaultlayer");
            domdoc.registerReferencingObject(this, "defaultlayer", s1);
        }
        return domelement.getNextSibling();
    }

    public void SVGUpdateReference(String s, Object obj) {
        if (s.equals("defaultlayer"))
            setDefaultLayer((GraphicViewerLayer) obj);
    }

    public void SVGWriteLayer(IDomDoc domdoc, IDomElement domelement,
            GraphicViewerLayer graphicviewerlayer) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLayer",
                            domelement);
            domelement1.setAttribute("visible", graphicviewerlayer.c
                    ? "true"
                    : "false");
            domelement1.setAttribute("transparency", (new Float(
                    graphicviewerlayer.k)).toString());
            domelement1.setAttribute("modifiable", graphicviewerlayer.h
                    ? "true"
                    : "false");
            if (graphicviewerlayer.b instanceof String) {
                domelement1.setAttribute("idtype", "string");
                domelement1.setAttribute("identifier",
                        (String) graphicviewerlayer.b);
            } else if (graphicviewerlayer.b instanceof Integer) {
                domelement1.setAttribute("idtype", "integer");
                domelement1.setAttribute("identifier",
                        ((Integer) graphicviewerlayer.b).toString());
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
            graphicviewerlayer.c = domelement1.getAttribute("visible").equals(
                    "true");
            graphicviewerlayer.k = (new Float(
                    domelement1.getAttribute("transparency"))).floatValue();
            graphicviewerlayer.h = domelement1.getAttribute("modifiable")
                    .equals("true");
            if (domelement1.getAttribute("idtype").equals("string"))
                graphicviewerlayer.b = domelement1.getAttribute("identifier");
            else if (domelement1.getAttribute("idtype").equals("integer"))
                graphicviewerlayer.b = new Integer(
                        domelement1.getAttribute("identifier"));
            domdoc.SVGTraverseChildren(graphicviewerdocument, domelement, null,
                    true);
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    public static final int CyclesAllowAll = 0;
    public static final int CyclesNotDirected = 1;
    public static final int CyclesNotDirectedFast = 2;
    public static final int CyclesNotUndirected = 3;
    public static final int CyclesDestinationTree = 4;
    public static final int CyclesSourceTree = 5;
    static boolean eU = false;
    private GraphicViewerLayer fa;
    private GraphicViewerLayer eV;
    private GraphicViewerLayer fc;
    private GraphicViewerLayer e1;
    private Dimension fb;
    private Point eZ;
    private Color e3;
    private boolean eY;
    private transient boolean e6;
    private transient boolean e7;
    private static transient DataFlavor e4 = null;
    private static transient DataFlavor eT[] = null;
    private transient ArrayList<IGraphicViewerDocumentListener> eX;
    private transient GraphicViewerDocumentEvent e5;
    private transient GraphicViewerUndoManager eR;
    private transient GraphicViewerPositionArray eW;
    private transient GraphicViewerObject e8;
    private boolean fd;
    private int e2;
    private transient HashMap<Integer, IGraphicViewerIdentifiablePart> eS;
    private int e9;
    private static HashMap<GraphicViewerNode, ?> e0 = null;
}