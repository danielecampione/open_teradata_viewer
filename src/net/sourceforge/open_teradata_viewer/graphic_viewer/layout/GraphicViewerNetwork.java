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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerListPosition;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerPort;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSelection;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IGraphicViewerObjectSimpleCollection;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerNetwork {

    private LinkedList myNetworkNodes = new LinkedList();
    private GraphicViewerNetworkNode myNetworkNodesArray[] = null;
    private LinkedList myNetworkLinks = new LinkedList();
    private GraphicViewerNetworkLink myNetworkLinksArray[] = null;
    private HashMap myMapGoObjToNode = new HashMap();
    private HashMap myMapGoObjToLink = new HashMap();

    public GraphicViewerNetwork() {
    }

    public GraphicViewerNetwork(
            IGraphicViewerObjectSimpleCollection iGraphicViewerObjectSimpleCollection) {
        addNodesAndLinksFromCollection(iGraphicViewerObjectSimpleCollection);
    }

    public void addNodesAndLinksFromCollection(
            IGraphicViewerObjectSimpleCollection iGraphicViewerObjectSimpleCollection) {
        if (iGraphicViewerObjectSimpleCollection == null) {
            return;
        }
        GraphicViewerListPosition graphicViewerListPosition = iGraphicViewerObjectSimpleCollection
                .getFirstObjectPos();
        do {
            if (graphicViewerListPosition == null) {
                break;
            }
            GraphicViewerObject graphicViewerObject = iGraphicViewerObjectSimpleCollection
                    .getObjectAtPos(graphicViewerListPosition);
            graphicViewerListPosition = iGraphicViewerObjectSimpleCollection
                    .getNextObjectPosAtTop(graphicViewerListPosition);
            GraphicViewerObject graphicViewerObject2 = graphicViewerObject
                    .getPartner();
            if ((graphicViewerObject2 == null || !graphicViewerObject2
                    .isTopLevel())
                    && !(graphicViewerObject instanceof GraphicViewerLink)
                    && !(graphicViewerObject instanceof GraphicViewerPort)
                    && findNode(graphicViewerObject) == null) {
                GraphicViewerNetworkNode graphicViewerNetworkNode = createNetworkNode();
                graphicViewerNetworkNode.setNetwork(this);
                graphicViewerNetworkNode
                        .setGraphicViewerObject(graphicViewerObject);
                addNode(graphicViewerNetworkNode);
                myMapGoObjToNode.put(graphicViewerObject,
                        graphicViewerNetworkNode);
            }
        } while (true);
        graphicViewerListPosition = iGraphicViewerObjectSimpleCollection
                .getFirstObjectPos();
        do {
            if (graphicViewerListPosition == null) {
                break;
            }
            GraphicViewerObject graphicViewerObject1 = iGraphicViewerObjectSimpleCollection
                    .getObjectAtPos(graphicViewerListPosition);
            graphicViewerListPosition = iGraphicViewerObjectSimpleCollection
                    .getNextObjectPosAtTop(graphicViewerListPosition);
            if (graphicViewerObject1 instanceof GraphicViewerLink) {
                GraphicViewerLink graphicViewerLink = (GraphicViewerLink) graphicViewerObject1;
                GraphicViewerPort graphicViewerPort = graphicViewerLink
                        .getFromPort();
                GraphicViewerPort graphicViewerPort1 = graphicViewerLink
                        .getToPort();
                if (graphicViewerPort != null && graphicViewerPort1 != null
                        && findLink(graphicViewerLink) == null) {
                    Object obj;
                    for (obj = graphicViewerPort; ((GraphicViewerObject) (obj))
                            .getParent() != null
                            && findNode(((GraphicViewerObject) (obj))) == null; obj = ((GraphicViewerObject) (obj))
                            .getParent()) {
                        ;
                    }
                    Object obj1;
                    for (obj1 = graphicViewerPort1; ((GraphicViewerObject) (obj1))
                            .getParent() != null
                            && findNode(((GraphicViewerObject) (obj1))) == null; obj1 = ((GraphicViewerObject) (obj1))
                            .getParent()) {
                        ;
                    }
                    if (obj != obj1) {
                        GraphicViewerNetworkNode graphicViewerNetworkNode1 = findNode(((GraphicViewerObject) (obj)));
                        GraphicViewerNetworkNode graphicViewerNetworknode2 = findNode(((GraphicViewerObject) (obj1)));
                        if (graphicViewerNetworkNode1 != null
                                && graphicViewerNetworknode2 != null) {
                            GraphicViewerNetworkLink graphicViewerNetworkLink = linkNodes(
                                    graphicViewerNetworkNode1,
                                    graphicViewerNetworknode2,
                                    graphicViewerObject1);
                            myMapGoObjToLink.put(graphicViewerObject1,
                                    graphicViewerNetworkLink);
                        }
                    }
                }
            }
        } while (true);
    }

    public void removeAllNodesAndLinks() {
        myNetworkNodes = new LinkedList();
        myNetworkNodesArray = null;
        myNetworkLinks = new LinkedList();
        myNetworkLinksArray = null;
        myMapGoObjToNode = new HashMap();
        myMapGoObjToLink = new HashMap();
    }

    public GraphicViewerNetworkNode createNetworkNode() {
        return new GraphicViewerNetworkNode();
    }

    public void addNode(GraphicViewerNetworkNode graphicViewerNetworkNode) {
        myNetworkNodes.addLast(graphicViewerNetworkNode);
        myNetworkNodesArray = null;
        graphicViewerNetworkNode.setNetwork(this);
        GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                .getGraphicViewerObject();
        if (graphicViewerObject != null) {
            myMapGoObjToNode.put(graphicViewerObject, graphicViewerNetworkNode);
        }
    }

    public GraphicViewerNetworkNode addNode(
            GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null) {
            return null;
        }
        GraphicViewerNetworkNode graphicViewerNetworkNode = findNode(graphicViewerObject);
        if (graphicViewerNetworkNode == null) {
            graphicViewerNetworkNode = createNetworkNode();
            graphicViewerNetworkNode.setNetwork(this);
            graphicViewerNetworkNode
                    .setGraphicViewerObject(graphicViewerObject);
            addNode(graphicViewerNetworkNode);
        }
        return graphicViewerNetworkNode;
    }

    public void deleteNode(GraphicViewerNetworkNode graphicViewerNetworkNode) {
        if (graphicViewerNetworkNode == null) {
            return;
        }
        int i = myNetworkNodes.indexOf(graphicViewerNetworkNode);
        if (i != -1) {
            removeNode(graphicViewerNetworkNode, i);
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getPredLinksArray();
            for (int j = aGraphicViewerNetworkLink.length - 1; j >= 0; j--) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[j];
                deleteLink(graphicViewerNetworkLink);
            }

            aGraphicViewerNetworkLink = graphicViewerNetworkNode
                    .getSuccLinksArray();
            for (int k = aGraphicViewerNetworkLink.length - 1; k >= 0; k--) {
                GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink[k];
                deleteLink(graphicViewerNetworkLink1);
            }

        }
    }

    void removeNode(GraphicViewerNetworkNode graphicViewerNetworkNode, int i) {
        if (graphicViewerNetworkNode == null) {
            return;
        }
        if (i < 0) {
            i = myNetworkNodes.indexOf(graphicViewerNetworkNode);
        }
        myNetworkNodes.remove(i);
        myNetworkNodesArray = null;
        GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                .getGraphicViewerObject();
        if (graphicViewerObject != null) {
            myMapGoObjToNode.remove(graphicViewerObject);
        }
        graphicViewerNetworkNode.setNetwork(null);
    }

    public void deleteNode(GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null) {
            return;
        }
        GraphicViewerNetworkNode graphicViewerNetworkNode = findNode(graphicViewerObject);
        if (graphicViewerNetworkNode != null) {
            deleteNode(graphicViewerNetworkNode);
        }
    }

    public GraphicViewerNetworkNode findNode(
            GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null) {
            return null;
        }
        Object obj = myMapGoObjToNode.get(graphicViewerObject);
        if (obj != null && (obj instanceof GraphicViewerNetworkNode)) {
            return (GraphicViewerNetworkNode) obj;
        } else {
            return null;
        }
    }

    public GraphicViewerNetworkLink createNetworkLink() {
        return new GraphicViewerNetworkLink();
    }

    public void addLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        if (graphicViewerNetworkLink == null) {
            return;
        }
        myNetworkLinks.addLast(graphicViewerNetworkLink);
        myNetworkLinksArray = null;
        GraphicViewerObject graphicViewerObject = graphicViewerNetworkLink
                .getGraphicViewerObject();
        if (graphicViewerObject != null
                && findLink(graphicViewerObject) == null) {
            myMapGoObjToLink.put(graphicViewerObject, graphicViewerNetworkLink);
        }
        GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getToNode();
        if (graphicViewerNetworkNode != null) {
            graphicViewerNetworkNode.addPredLink(graphicViewerNetworkLink);
        }
        GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getFromNode();
        if (graphicViewerNetworkNode1 != null) {
            graphicViewerNetworkNode1.addSuccLink(graphicViewerNetworkLink);
        }
        graphicViewerNetworkLink.setNetwork(this);
    }

    public GraphicViewerNetworkLink addLink(GraphicViewerLink graphicViewerLink) {
        if (graphicViewerLink == null) {
            return null;
        }
        GraphicViewerNetworkLink graphicViewerNetworkLink = findLink(graphicViewerLink);
        GraphicViewerPort graphicViewerPort = graphicViewerLink.getFromPort();
        GraphicViewerPort graphicViewerPort1 = graphicViewerLink.getToPort();
        if (graphicViewerNetworkLink == null && graphicViewerPort != null
                && graphicViewerPort1 != null) {
            Object obj;
            for (obj = graphicViewerPort; ((GraphicViewerObject) (obj))
                    .getParent() != null
                    && findNode(((GraphicViewerObject) (obj))) == null; obj = ((GraphicViewerObject) (obj))
                    .getParent()) {
                ;
            }
            Object obj1;
            for (obj1 = graphicViewerPort1; ((GraphicViewerObject) (obj1))
                    .getParent() != null
                    && findNode(((GraphicViewerObject) (obj1))) == null; obj1 = ((GraphicViewerObject) (obj1))
                    .getParent()) {
                ;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode = addNode(((GraphicViewerObject) (obj)));
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = addNode(((GraphicViewerObject) (obj1)));
            graphicViewerNetworkLink = linkNodes(graphicViewerNetworkNode,
                    graphicViewerNetworkNode1, graphicViewerLink);
        }
        return graphicViewerNetworkLink;
    }

    public void deleteLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getToNode();
        if (graphicViewerNetworkNode != null) {
            graphicViewerNetworkNode.deletePredLink(graphicViewerNetworkLink);
        }
        GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getFromNode();
        if (graphicViewerNetworkNode1 != null) {
            graphicViewerNetworkNode1.deleteSuccLink(graphicViewerNetworkLink);
        }
        removeLink(graphicViewerNetworkLink);
    }

    void removeLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        if (graphicViewerNetworkLink == null) {
            return;
        }
        int i = myNetworkLinks.indexOf(graphicViewerNetworkLink);
        if (i != -1) {
            myNetworkLinks.remove(i);
            myNetworkLinksArray = null;
            GraphicViewerObject graphicViewerObject = graphicViewerNetworkLink
                    .getGraphicViewerObject();
            if (graphicViewerObject != null
                    && findLink(graphicViewerObject) == graphicViewerNetworkLink) {
                myMapGoObjToLink.remove(graphicViewerObject);
            }
            graphicViewerNetworkLink.setNetwork(null);
        }
    }

    public void deleteLink(GraphicViewerLink graphicViewerLink) {
        if (graphicViewerLink == null) {
            return;
        }
        GraphicViewerNetworkLink graphicViewerNetworkLink = findLink(graphicViewerLink);
        if (graphicViewerNetworkLink != null) {
            deleteLink(graphicViewerNetworkLink);
        }
    }

    public GraphicViewerNetworkLink findLink(
            GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null) {
            return null;
        }
        Object obj = myMapGoObjToLink.get(graphicViewerObject);
        if (obj != null && (obj instanceof GraphicViewerNetworkLink)) {
            return (GraphicViewerNetworkLink) obj;
        } else {
            return null;
        }
    }

    public GraphicViewerNetworkLink linkNodes(
            GraphicViewerNetworkNode graphicViewerNetworkNode,
            GraphicViewerNetworkNode graphicViewerNetworkNode1,
            GraphicViewerObject graphicViewerObject) {
        if (graphicViewerNetworkNode.getNetwork() == this
                && graphicViewerNetworkNode1.getNetwork() == this) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = createNetworkLink();
            graphicViewerNetworkLink.setNetwork(this);
            graphicViewerNetworkLink
                    .setGraphicViewerObject(graphicViewerObject);
            graphicViewerNetworkLink.setFromNode(graphicViewerNetworkNode);
            graphicViewerNetworkLink.setToNode(graphicViewerNetworkNode1);
            addLink(graphicViewerNetworkLink);
            graphicViewerNetworkNode.addSuccLink(graphicViewerNetworkLink);
            graphicViewerNetworkNode1.addPredLink(graphicViewerNetworkLink);
            return graphicViewerNetworkLink;
        } else {
            return null;
        }
    }

    public void reverseLink(GraphicViewerNetworkLink graphicViewerNetworkLink) {
        GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getFromNode();
        GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getToNode();
        graphicViewerNetworkNode.deleteSuccLink(graphicViewerNetworkLink);
        graphicViewerNetworkNode1.deletePredLink(graphicViewerNetworkLink);
        graphicViewerNetworkLink.reverseLink();
        graphicViewerNetworkNode.addPredLink(graphicViewerNetworkLink);
        graphicViewerNetworkNode1.addSuccLink(graphicViewerNetworkLink);
    }

    public void deleteSelfLinks() {
        ArrayList arraylist = new ArrayList();
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int i = 0; i < aGraphicViewerNetworkLink.length; i++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i];
            if (graphicViewerNetworkLink.getFromNode() == graphicViewerNetworkLink
                    .getToNode()) {
                arraylist.add(graphicViewerNetworkLink);
            }
        }

        for (int j = 0; j < arraylist.size(); j++) {
            deleteLink((GraphicViewerNetworkLink) arraylist.get(j));
        }

    }

    public void deleteArtificialNodes() {
        ArrayList arraylist = new ArrayList();
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNodeArray();
        for (int i = 0; i < aGraphicViewerNetworkNode.length; i++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
            if (graphicViewerNetworkNode.getGraphicViewerObject() == null) {
                arraylist.add(graphicViewerNetworkNode);
            }
        }

        for (int j = 0; j < arraylist.size(); j++) {
            deleteNode((GraphicViewerNetworkNode) arraylist.get(j));
        }

        arraylist.clear();
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            if (graphicViewerNetworkLink.getGraphicViewerObject() == null) {
                arraylist.add(graphicViewerNetworkLink);
            }
        }

        for (int l = 0; l < arraylist.size(); l++) {
            deleteLink((GraphicViewerNetworkLink) arraylist.get(l));
        }

    }

    void deleteUselessLinks() {
        ArrayList arraylist = new ArrayList();
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int i = 0; i < aGraphicViewerNetworkLink.length; i++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i];
            if (graphicViewerNetworkLink.getFromNode() == null
                    || graphicViewerNetworkLink.getToNode() == null) {
                arraylist.add(graphicViewerNetworkLink);
            }
        }

        for (int j = 0; j < arraylist.size(); j++) {
            deleteLink((GraphicViewerNetworkLink) arraylist.get(j));
        }

    }

    boolean isSingleton(GraphicViewerNetworkNode graphicViewerNetworkNode) {
        if (!graphicViewerNetworkNode.getPredLinksList().isEmpty()) {
            return false;
        }
        return graphicViewerNetworkNode.getSuccLinksList().isEmpty();
    }

    public GraphicViewerNetwork[] splitIntoSubNetworks() {
        deleteArtificialNodes();
        deleteUselessLinks();
        deleteSelfLinks();
        ArrayList localArrayList = new ArrayList();
        int i = 1;
        while (i != 0) {
            i = 0;
            GraphicViewerNetworkNode[] localObject = getNodeArray();
            for (int j = 0; j < localObject.length; j++) {
                GraphicViewerNetworkNode localGraphicViewerNetworkNode = localObject[j];
                if (!isSingleton(localGraphicViewerNetworkNode)) {
                    GraphicViewerNetwork localGraphicViewerNetwork = new GraphicViewerNetwork();
                    localArrayList.add(localGraphicViewerNetwork);
                    traverseSubnet(localGraphicViewerNetwork,
                            localGraphicViewerNetworkNode);
                    i = 1;
                    break;
                }
            }
        }
        GraphicViewerNetwork[] localObject = new GraphicViewerNetwork[localArrayList
                .size()];
        for (int j = 0; j < localObject.length; j++) {
            localObject[j] = ((GraphicViewerNetwork) localArrayList.get(j));
        }
        Arrays.sort((Object[]) localObject, new NetworkCountComparer());
        return localObject;
    }

    private void traverseSubnet(GraphicViewerNetwork graphicViewerNetwork,
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        if (graphicViewerNetworkNode == null) {
            return;
        }
        if (graphicViewerNetworkNode.getNetwork() == graphicViewerNetwork) {
            return;
        }
        removeNode(graphicViewerNetworkNode, -1);
        graphicViewerNetwork.addNode(graphicViewerNetworkNode);
        Iterator iterator = graphicViewerNetworkNode.getPredLinksList()
                .iterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            GraphicViewerNetworkLink graphicViewerNetworkLink = (GraphicViewerNetworkLink) iterator
                    .next();
            if (graphicViewerNetworkLink.getNetwork() != graphicViewerNetwork) {
                removeLink(graphicViewerNetworkLink);
                graphicViewerNetwork.addLink(graphicViewerNetworkLink);
                traverseSubnet(graphicViewerNetwork,
                        graphicViewerNetworkLink.getFromNode());
            }
        } while (true);
        iterator = graphicViewerNetworkNode.getSuccLinksList().iterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            GraphicViewerNetworkLink graphicViewerNetworkLink1 = (GraphicViewerNetworkLink) iterator
                    .next();
            if (graphicViewerNetworkLink1.getNetwork() != graphicViewerNetwork) {
                removeLink(graphicViewerNetworkLink1);
                graphicViewerNetwork.addLink(graphicViewerNetworkLink1);
                traverseSubnet(graphicViewerNetwork,
                        graphicViewerNetworkLink1.getToNode());
            }
        } while (true);
    }

    public GraphicViewerSelection getNodesAndLinks(
            GraphicViewerSelection graphicViewerSelection) {
        if (graphicViewerSelection == null) {
            graphicViewerSelection = new GraphicViewerSelection(null);
        }
        Iterator iterator = getNodeIterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode = (GraphicViewerNetworkNode) iterator
                    .next();
            GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                    .getGraphicViewerObject();
            if (graphicViewerObject != null
                    && !graphicViewerSelection
                            .isInSelection(graphicViewerObject)) {
                graphicViewerSelection.extendSelection(graphicViewerObject);
            }
        } while (true);
        iterator = getLinkIterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            GraphicViewerNetworkLink graphicViewerNetworkLink = (GraphicViewerNetworkLink) iterator
                    .next();
            GraphicViewerObject graphicViewerObject1 = graphicViewerNetworkLink
                    .getGraphicViewerObject();
            if (graphicViewerObject1 != null
                    && !graphicViewerSelection
                            .isInSelection(graphicViewerObject1)) {
                graphicViewerSelection.extendSelection(graphicViewerObject1);
            }
        } while (true);
        return graphicViewerSelection;
    }

    public int getNodeCount() {
        return myNetworkNodes.size();
    }

    public Iterator getNodeIterator() {
        return myNetworkNodes.iterator();
    }

    public Iterator getNodeIterator(int i) {
        if (myNetworkNodes.size() < i) {
            return myNetworkNodes.iterator();
        } else {
            return myNetworkNodes.listIterator(i);
        }
    }

    public int getLinkCount() {
        return myNetworkLinks.size();
    }

    public Iterator getLinkIterator() {
        return myNetworkLinks.iterator();
    }

    public Iterator getLinkIterator(int i) {
        if (i > myNetworkLinks.size()) {
            return myNetworkLinks.iterator();
        } else {
            return myNetworkLinks.listIterator(i);
        }
    }

    public void commitNodes() {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNodeArray();
        for (int i = 0; i < aGraphicViewerNetworkNode.length; i++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
            graphicViewerNetworkNode.commitPosition();
        }

    }

    public void commitLinks() {
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int i = 0; i < aGraphicViewerNetworkLink.length; i++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i];
            graphicViewerNetworkLink.commitPosition();
        }

    }

    public void commitNodesAndLinks() {
        commitNodes();
        commitLinks();
    }

    public GraphicViewerNetworkLink[] getLinkArray() {
        if (myNetworkLinksArray == null) {
            myNetworkLinksArray = new GraphicViewerNetworkLink[getLinkCount()];
            myNetworkLinks.toArray(myNetworkLinksArray);
        }
        return myNetworkLinksArray;
    }

    public GraphicViewerNetworkNode[] getNodeArray() {
        if (myNetworkNodesArray == null) {
            myNetworkNodesArray = new GraphicViewerNetworkNode[getNodeCount()];
            myNetworkNodes.toArray(myNetworkNodesArray);
        }
        return myNetworkNodesArray;
    }

    protected LinkedList getNetworkLinks() {
        return myNetworkLinks;
    }

    protected LinkedList getNetworkNodes() {
        return myNetworkNodes;
    }

    protected HashMap getGoObjToNodeMap() {
        return myMapGoObjToNode;
    }

    protected HashMap getGoObjToLinkMap() {
        return myMapGoObjToLink;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class NetworkCountComparer implements Comparator {

        NetworkCountComparer() {
        }

        public int compare(Object obj, Object obj1) {
            GraphicViewerNetwork graphicViewerNetwork = (GraphicViewerNetwork) obj;
            GraphicViewerNetwork graphicViewerNetwork1 = (GraphicViewerNetwork) obj1;
            if (graphicViewerNetwork != null && graphicViewerNetwork1 != null) {
                if (graphicViewerNetwork.getNodeCount() < graphicViewerNetwork1
                        .getNodeCount()) {
                    return 1;
                }
                return graphicViewerNetwork.getNodeCount() != graphicViewerNetwork1
                        .getNodeCount() ? -1 : 0;
            } else {
                return 0;
            }
        }
    }
}