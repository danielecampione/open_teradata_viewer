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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.io.Serializable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class GraphicViewerObjList implements Serializable {

    private static final long serialVersionUID = 872776455730424153L;

    public GraphicViewerObjList() {
        _fldif = null;
        a = null;
        _flddo = 0;
        _fldfor = false;
    }

    public GraphicViewerObjList(boolean flag) {
        _fldif = null;
        a = null;
        _flddo = 0;
        _fldfor = false;
        _fldfor = flag;
    }

    public int getNumObjects() {
        return _flddo;
    }

    public boolean isEmpty() {
        return _fldif == null;
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        GraphicViewerListPosition graphicviewerlistposition = new GraphicViewerListPosition(
                graphicviewerobject, null, _fldif);
        if (_fldif != null)
            _fldif._fldif = graphicviewerlistposition;
        _fldif = graphicviewerlistposition;
        if (a == null)
            a = _fldif;
        _flddo++;
        if (_fldfor)
            graphicviewerobject.a(_fldif);
        return _fldif;
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        GraphicViewerListPosition graphicviewerlistposition = new GraphicViewerListPosition(
                graphicviewerobject, a, null);
        if (a != null)
            a.a = graphicviewerlistposition;
        a = graphicviewerlistposition;
        if (_fldif == null)
            _fldif = a;
        _flddo++;
        if (_fldfor)
            graphicviewerobject.a(a);
        return a;
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null)
            return null;
        GraphicViewerListPosition graphicviewerlistposition1 = new GraphicViewerListPosition(
                graphicviewerobject, graphicviewerlistposition._fldif,
                graphicviewerlistposition);
        if (graphicviewerlistposition._fldif != null)
            graphicviewerlistposition._fldif.a = graphicviewerlistposition1;
        else
            _fldif = graphicviewerlistposition1;
        graphicviewerlistposition._fldif = graphicviewerlistposition1;
        _flddo++;
        if (_fldfor)
            graphicviewerobject.a(graphicviewerlistposition1);
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null)
            return null;
        GraphicViewerListPosition graphicviewerlistposition1 = new GraphicViewerListPosition(
                graphicviewerobject, graphicviewerlistposition,
                graphicviewerlistposition.a);
        if (graphicviewerlistposition.a != null)
            graphicviewerlistposition.a._fldif = graphicviewerlistposition1;
        else
            a = graphicviewerlistposition1;
        graphicviewerlistposition.a = graphicviewerlistposition1;
        _flddo++;
        if (_fldfor)
            graphicviewerobject.a(graphicviewerlistposition1);
        return graphicviewerlistposition1;
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        GraphicViewerListPosition graphicviewerlistposition = findObject(graphicviewerobject);
        removeObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        GraphicViewerObject graphicviewerobject = graphicviewerlistposition._flddo;
        if (graphicviewerlistposition == _fldif)
            _fldif = graphicviewerlistposition.a;
        if (graphicviewerlistposition == a)
            a = graphicviewerlistposition._fldif;
        if (graphicviewerlistposition._fldif != null)
            graphicviewerlistposition._fldif.a = graphicviewerlistposition.a;
        if (graphicviewerlistposition.a != null)
            graphicviewerlistposition.a._fldif = graphicviewerlistposition._fldif;
        graphicviewerlistposition._fldif = null;
        graphicviewerlistposition.a = null;
        if (_fldfor)
            graphicviewerobject.a(null);
        _flddo--;
        return graphicviewerobject;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return _fldif;
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return a;
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null)
            return null;
        else
            return graphicviewerlistposition.a;
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
        if (_fldfor) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerobject
                    .i();
            if (graphicviewerlistposition != null)
                return graphicviewerlistposition;
        }
        for (GraphicViewerListPosition graphicviewerlistposition1 = _fldif; graphicviewerlistposition1 != null; graphicviewerlistposition1 = graphicviewerlistposition1.a)
            if (graphicviewerlistposition1._flddo == graphicviewerobject)
                return graphicviewerlistposition1;

        return null;
    }

    private GraphicViewerListPosition _fldif;
    private GraphicViewerListPosition a;
    private int _flddo;
    private boolean _fldfor;
}