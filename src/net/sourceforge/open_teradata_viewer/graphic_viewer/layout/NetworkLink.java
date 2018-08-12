/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLabeledLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NetworkLink {

    public NetworkLink() {
        a = null;
        _fldnew = null;
        _flddo = null;
        _fldint = null;
        _fldif = null;
        _fldfor = null;
    }

    public NetworkLink(Network graphicViewerNetwork,
            GraphicViewerObject graphicViewerObject,
            NetworkNode graphicViewerNetworkNode,
            NetworkNode graphicViewerNetworkNode1) {
        a = null;
        _fldnew = null;
        _flddo = null;
        _fldint = null;
        _fldif = null;
        _fldfor = null;
        setNetwork(graphicViewerNetwork);
        setGraphicViewerObject(graphicViewerObject);
        setFromNode(graphicViewerNetworkNode);
        setToNode(graphicViewerNetworkNode1);
    }

    public Network getNetwork() {
        return _flddo;
    }

    public void setNetwork(Network graphicViewerNetwork) {
        _flddo = graphicViewerNetwork;
    }

    public GraphicViewerObject getGraphicViewerObject() {
        return _fldint;
    }

    public void setGraphicViewerObject(GraphicViewerObject graphicViewerObject) {
        _fldint = graphicViewerObject;
    }

    public NetworkNode getFromNode() {
        return _fldif;
    }

    public void setFromNode(NetworkNode graphicViewerNetworkNode) {
        _fldif = graphicViewerNetworkNode;
    }

    public NetworkNode getToNode() {
        return _fldfor;
    }

    public void setToNode(NetworkNode graphicViewerNetworkNode) {
        _fldfor = graphicViewerNetworkNode;
    }

    public void reverseLink() {
        NetworkNode graphicViewerNetworkNode = _fldif;
        _fldif = _fldfor;
        _fldfor = graphicViewerNetworkNode;
    }

    public void commitPosition() {
        if (getGraphicViewerObject() instanceof GraphicViewerLabeledLink) {
            GraphicViewerLabeledLink graphicViewerLabeledLink = (GraphicViewerLabeledLink) getGraphicViewerObject();
            graphicViewerLabeledLink.positionLabels();
        }
    }

    public IAutoLayoutLinkData getLinkData() {
        return a;
    }

    public void setLinkData(IAutoLayoutLinkData graphicViewerAutoLayoutLinkData) {
        a = graphicViewerAutoLayoutLinkData;
    }

    public Object getLinkUserData() {
        return _fldnew;
    }

    public void setLinkUserData(Object obj) {
        _fldnew = obj;
    }

    IAutoLayoutLinkData a;
    private Object _fldnew;
    private Network _flddo;
    private GraphicViewerObject _fldint;
    private NetworkNode _fldif;
    private NetworkNode _fldfor;
}