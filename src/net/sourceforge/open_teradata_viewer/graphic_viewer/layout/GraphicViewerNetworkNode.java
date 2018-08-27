/*
 * Open Teradata Viewer ( graphic viewer layout )
 * Copyright (C) 2015, D. Campione
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

import java.awt.Point;
import java.util.LinkedList;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerNetworkNode {

    IGraphicViewerAutoLayoutNodeData nodeData = null;
    private Object nodeUserData = null;
    private GraphicViewerNetwork myNetwork = null;
    private GraphicViewerObject myGoObject = null;
    private Point myCenter = new Point(0, 0);
    private LinkedList myPredLinks = new LinkedList();
    private GraphicViewerNetworkLink myPredLinksArray[] = null;
    private LinkedList mySuccLinks = new LinkedList();
    private GraphicViewerNetworkLink mySuccLinksArray[] = null;

    public GraphicViewerNetworkNode() {
    }

    public GraphicViewerNetworkNode(GraphicViewerNetwork graphicViewerNetwork,
            GraphicViewerObject graphicViewerObject) {
        setNetwork(graphicViewerNetwork);
        setGraphicViewerObject(graphicViewerObject);
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
        if (myGoObject != null) {
            Point point = myGoObject.getSpotLocation(0);
            myCenter.x = point.x;
            myCenter.y = point.y;
        }
    }

    public LinkedList getPredLinksList() {
        return myPredLinks;
    }

    public GraphicViewerNetworkLink[] getPredLinksArray() {
        if (myPredLinksArray == null) {
            myPredLinksArray = new GraphicViewerNetworkLink[myPredLinks.size()];
            myPredLinks.toArray(myPredLinksArray);
        }
        return myPredLinksArray;
    }

    public void addPredLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        int i = myPredLinks.indexOf(graphicViewerNetworkLink);
        if (i < 0) {
            myPredLinks.addLast(graphicViewerNetworkLink);
            myPredLinksArray = null;
        }
    }

    public void deletePredLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        int i = myPredLinks.indexOf(graphicViewerNetworkLink);
        if (i != -1) {
            myPredLinks.remove(i);
            myPredLinksArray = null;
        }
    }

    public LinkedList getSuccLinksList() {
        return mySuccLinks;
    }

    public GraphicViewerNetworkLink[] getSuccLinksArray() {
        if (mySuccLinksArray == null) {
            mySuccLinksArray = new GraphicViewerNetworkLink[mySuccLinks.size()];
            mySuccLinks.toArray(mySuccLinksArray);
        }
        return mySuccLinksArray;
    }

    public void addSuccLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        int i = mySuccLinks.indexOf(graphicViewerNetworkLink);
        if (i < 0) {
            mySuccLinks.addLast(graphicViewerNetworkLink);
            mySuccLinksArray = null;
        }
    }

    public void deleteSuccLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        int i = mySuccLinks.indexOf(graphicViewerNetworkLink);
        if (i != -1) {
            mySuccLinks.remove(i);
            mySuccLinksArray = null;
        }
    }

    public Point getCenter() {
        return myCenter;
    }

    public void setCenter(Point point) {
        myCenter.x = point.x;
        myCenter.y = point.y;
    }

    public void setCenter(int i, int j) {
        myCenter.x = i;
        myCenter.y = j;
    }

    public void commitPosition() {
        GraphicViewerObject graphicViewerObject = myGoObject;
        if (graphicViewerObject != null) {
            graphicViewerObject = graphicViewerObject.redirectSelection();
            if (graphicViewerObject == null) {
                graphicViewerObject = myGoObject;
            }
            graphicViewerObject.setSpotLocation(0, myCenter);
        }
    }

    public IGraphicViewerAutoLayoutNodeData getNodeData() {
        return nodeData;
    }

    public void setNodeData(
            IGraphicViewerAutoLayoutNodeData graphicViewerAutoLayoutNodeData) {
        nodeData = graphicViewerAutoLayoutNodeData;
    }

    public Object getNodeUserData() {
        return nodeUserData;
    }

    public void setNodeUserData(Object obj) {
        nodeUserData = obj;
    }
}