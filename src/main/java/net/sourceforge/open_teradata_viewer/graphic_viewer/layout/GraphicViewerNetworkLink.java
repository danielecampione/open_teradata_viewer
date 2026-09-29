/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLabeledLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerNetworkLink {

    IGraphicViewerAutoLayoutLinkData linkData = null;
    private Object linkUserData = null;
    private GraphicViewerNetwork myNetwork = null;
    private GraphicViewerObject myGoObject = null;
    private GraphicViewerNetworkNode myFromNode = null;
    private GraphicViewerNetworkNode myToNode = null;

    public GraphicViewerNetworkLink() {
    }

    public GraphicViewerNetworkLink(GraphicViewerNetwork graphicViewerNetwork,
            GraphicViewerObject graphicViewerObject,
            GraphicViewerNetworkNode graphicViewerNetworkNode,
            GraphicViewerNetworkNode graphicViewerNetworkNode1) {
        setNetwork(graphicViewerNetwork);
        setGraphicViewerObject(graphicViewerObject);
        setFromNode(graphicViewerNetworkNode);
        setToNode(graphicViewerNetworkNode1);
    }

    public GraphicViewerNetwork getNetwork() {
        return myNetwork;
    }

    public void setNetwork(GraphicViewerNetwork graphicViewerNetwork) {
        myNetwork = graphicViewerNetwork;
    }

    public GraphicViewerObject getGraphicViewerObject() {
        return myGoObject;
    }

    public void setGraphicViewerObject(GraphicViewerObject graphicViewerObject) {
        myGoObject = graphicViewerObject;
    }

    public GraphicViewerNetworkNode getFromNode() {
        return myFromNode;
    }

    public void setFromNode(GraphicViewerNetworkNode graphicViewerNetworkNode) {
        myFromNode = graphicViewerNetworkNode;
    }

    public GraphicViewerNetworkNode getToNode() {
        return myToNode;
    }

    public void setToNode(GraphicViewerNetworkNode graphicViewerNetworkNode) {
        myToNode = graphicViewerNetworkNode;
    }

    public void reverseLink() {
        GraphicViewerNetworkNode graphicViewerNetworkNode = myFromNode;
        myFromNode = myToNode;
        myToNode = graphicViewerNetworkNode;
    }

    public void commitPosition() {
        if (getGraphicViewerObject() instanceof GraphicViewerLabeledLink) {
            GraphicViewerLabeledLink graphicViewerLabeledLink = (GraphicViewerLabeledLink) getGraphicViewerObject();
            graphicViewerLabeledLink.positionLabels();
        }
    }

    public IGraphicViewerAutoLayoutLinkData getLinkData() {
        return linkData;
    }

    public void setLinkData(
            IGraphicViewerAutoLayoutLinkData graphicViewerAutoLayoutLinkData) {
        linkData = graphicViewerAutoLayoutLinkData;
    }

    public Object getLinkUserData() {
        return linkUserData;
    }

    public void setLinkUserData(Object obj) {
        linkUserData = obj;
    }
}