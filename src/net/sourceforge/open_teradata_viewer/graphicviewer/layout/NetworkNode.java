/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphicviewer.layout;


import java.awt.Point;
import java.util.LinkedList;

import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NetworkNode {

    @SuppressWarnings("rawtypes")
    public NetworkNode() {
        _flddo = null;
        _fldif = null;
        _fldint = null;
        a = null;
        _fldfor = new Point(0, 0);
        _fldnew = new LinkedList();
        _fldtry = null;
        _fldcase = new LinkedList();
        _fldbyte = null;
    }

    @SuppressWarnings("rawtypes")
    public NetworkNode(Network graphicViewerNetwork,
            GraphicViewerObject graphicViewerObject) {
        _flddo = null;
        _fldif = null;
        _fldint = null;
        a = null;
        _fldfor = new Point(0, 0);
        _fldnew = new LinkedList();
        _fldtry = null;
        _fldcase = new LinkedList();
        _fldbyte = null;
        setNetwork(graphicViewerNetwork);
        setGraphicViewerObject(graphicViewerObject);
    }

    public Network getNetwork() {
        return _fldint;
    }

    public void setNetwork(Network graphicViewerNetwork) {
        _fldint = graphicViewerNetwork;
    }

    public GraphicViewerObject getGraphicViewerObject() {
        return a;
    }

    public void setGraphicViewerObject(GraphicViewerObject graphicViewerObject) {
        a = graphicViewerObject;
        if (a != null) {
            Point point = a.getSpotLocation(0);
            _fldfor.x = point.x;
            _fldfor.y = point.y;
        }
    }

    @SuppressWarnings("rawtypes")
    public LinkedList getPredLinksList() {
        return _fldnew;
    }

    @SuppressWarnings("unchecked")
    public NetworkLink[] getPredLinksArray() {
        if (_fldtry == null) {
            _fldtry = new NetworkLink[_fldnew.size()];
            _fldnew.toArray(_fldtry);
        }
        return _fldtry;
    }

    @SuppressWarnings("unchecked")
    public void addPredLink(NetworkLink graphicViewerNetworkLink) {
        int i = _fldnew.indexOf(graphicViewerNetworkLink);
        if (i < 0) {
            _fldnew.addLast(graphicViewerNetworkLink);
            _fldtry = null;
        }
    }

    public void deletePredLink(NetworkLink graphicViewerNetworkLink) {
        int i = _fldnew.indexOf(graphicViewerNetworkLink);
        if (i != -1) {
            _fldnew.remove(i);
            _fldtry = null;
        }
    }

    @SuppressWarnings("rawtypes")
    public LinkedList getSuccLinksList() {
        return _fldcase;
    }

    @SuppressWarnings("unchecked")
    public NetworkLink[] getSuccLinksArray() {
        if (_fldbyte == null) {
            _fldbyte = new NetworkLink[_fldcase.size()];
            _fldcase.toArray(_fldbyte);
        }
        return _fldbyte;
    }

    @SuppressWarnings("unchecked")
    public void addSuccLink(NetworkLink graphicViewerNetworkLink) {
        int i = _fldcase.indexOf(graphicViewerNetworkLink);
        if (i < 0) {
            _fldcase.addLast(graphicViewerNetworkLink);
            _fldbyte = null;
        }
    }

    public void deleteSuccLink(NetworkLink graphicViewerNetworkLink) {
        int i = _fldcase.indexOf(graphicViewerNetworkLink);
        if (i != -1) {
            _fldcase.remove(i);
            _fldbyte = null;
        }
    }

    public Point getCenter() {
        return _fldfor;
    }

    public void setCenter(Point point) {
        _fldfor.x = point.x;
        _fldfor.y = point.y;
    }

    public void setCenter(int i, int j) {
        _fldfor.x = i;
        _fldfor.y = j;
    }

    public void commitPosition() {
        GraphicViewerObject graphicViewerObject = a;
        if (graphicViewerObject != null) {
            graphicViewerObject = graphicViewerObject.redirectSelection();
            if (graphicViewerObject == null)
                graphicViewerObject = a;
            graphicViewerObject.setSpotLocation(0, _fldfor);
        }
    }

    public AutoLayoutNodeData getNodeData() {
        return _flddo;
    }

    public void setNodeData(AutoLayoutNodeData graphicViewerAutoLayoutNodeData) {
        _flddo = graphicViewerAutoLayoutNodeData;
    }

    public Object getNodeUserData() {
        return _fldif;
    }

    public void setNodeUserData(Object obj) {
        _fldif = obj;
    }

    AutoLayoutNodeData _flddo;
    private Object _fldif;
    private Network _fldint;
    private GraphicViewerObject a;
    private Point _fldfor;
    @SuppressWarnings("rawtypes")
    private LinkedList _fldnew;
    private NetworkLink _fldtry[];
    @SuppressWarnings("rawtypes")
    private LinkedList _fldcase;
    private NetworkLink _fldbyte[];
}