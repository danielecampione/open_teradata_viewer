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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerArea;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerCollection;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerListPosition;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerNode;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerPort;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IGraphicViewerObjectCollection;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IGraphicViewerObjectSimpleCollection;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GraphicViewerTreeNetwork {

    private GraphicViewerNodeArrayList myNetworkNodes = new GraphicViewerNodeArrayList();
    private GraphicViewerLinkArrayList myNetworkLinks = new GraphicViewerLinkArrayList();
    private HashMap myMapGoObjToNode = new HashMap();
    private HashMap myMapGoObjToLink = new HashMap();
    private GraphicViewerTreeAutoLayout myLayout;

    public GraphicViewerTreeNetwork() {
    }

    public GraphicViewerTreeNetwork(
            IGraphicViewerObjectSimpleCollection paramGraphicViewerObjectSimpleCollection) {
        AddNodesAndLinksFromCollection(
                paramGraphicViewerObjectSimpleCollection, true);
    }

    public GraphicViewerTreeNetworkNode CreateNetworkNode() {
        return new GraphicViewerTreeNetworkNode();
    }

    public GraphicViewerTreeNetworkLink CreateNetworkLink() {
        return new GraphicViewerTreeNetworkLink();
    }

    public void AddNodesAndLinksFromCollection(
            IGraphicViewerObjectSimpleCollection paramGraphicViewerObjectSimpleCollection,
            boolean paramBoolean) {
        if (paramGraphicViewerObjectSimpleCollection == null) {
            return;
        }
        GraphicViewerObject localGraphicViewerObject;
        Object localObject;
        for (GraphicViewerListPosition localGraphicViewerListPosition = paramGraphicViewerObjectSimpleCollection
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = paramGraphicViewerObjectSimpleCollection
                .getNextObjectPosAtTop(localGraphicViewerListPosition)) {
            localGraphicViewerObject = paramGraphicViewerObjectSimpleCollection
                    .getObjectAtPos(localGraphicViewerListPosition);
            if ((!(localGraphicViewerObject instanceof GraphicViewerLink))
                    && ((!paramBoolean) || ((localGraphicViewerObject instanceof GraphicViewerNode)))
                    && (FindNode(localGraphicViewerObject) == null)) {
                localObject = CreateNetworkNode();
                ((GraphicViewerTreeNetworkNode) localObject).setNetwork(this);
                ((GraphicViewerTreeNetworkNode) localObject)
                        .setGraphicViewerObject(localGraphicViewerObject);
                AddNode((GraphicViewerTreeNetworkNode) localObject);
            }
        }
        GraphicViewerListPosition localGraphicViewerListPosition = paramGraphicViewerObjectSimpleCollection
                .getFirstObjectPos();
        while (localGraphicViewerListPosition != null) {
            localGraphicViewerObject = paramGraphicViewerObjectSimpleCollection
                    .getObjectAtPos(localGraphicViewerListPosition);
            localGraphicViewerListPosition = paramGraphicViewerObjectSimpleCollection
                    .getNextObjectPosAtTop(localGraphicViewerListPosition);
            localObject = null;
            if ((localGraphicViewerObject instanceof GraphicViewerLink)) {
                localObject = (GraphicViewerLink) localGraphicViewerObject;
            }
            if (localObject != null) {
                GraphicViewerPort localGraphicViewerPort1 = ((GraphicViewerLink) localObject)
                        .getFromPort();
                GraphicViewerPort localGraphicViewerPort2 = ((GraphicViewerLink) localObject)
                        .getToPort();
                if ((localGraphicViewerPort1 != null)
                        && (localGraphicViewerPort2 != null)
                        && (FindLink(localGraphicViewerObject) == null)) {
                    GraphicViewerArea localGraphicViewerArea1;
                    for (localGraphicViewerArea1 = localGraphicViewerPort1
                            .getParent(); (localGraphicViewerArea1.getParent() != null)
                            && (FindNode(localGraphicViewerArea1) == null); localGraphicViewerArea1 = localGraphicViewerArea1
                            .getParent()) {
                        ;
                    }
                    GraphicViewerArea localGraphicViewerArea2;
                    for (localGraphicViewerArea2 = localGraphicViewerPort2
                            .getParent(); (localGraphicViewerArea2.getParent() != null)
                            && (FindNode(localGraphicViewerArea2) == null); localGraphicViewerArea2 = localGraphicViewerArea2
                            .getParent()) {
                        ;
                    }
                    if (localGraphicViewerArea1 != localGraphicViewerArea2) {
                        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = FindNode(localGraphicViewerArea1);
                        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = FindNode(localGraphicViewerArea2);
                        if ((localGraphicViewerTreeNetworkNode1 != null)
                                && (localGraphicViewerTreeNetworkNode2 != null)) {
                            LinkNodes(localGraphicViewerTreeNetworkNode1,
                                    localGraphicViewerTreeNetworkNode2,
                                    localGraphicViewerObject);
                        }
                    }
                }
            }
        }
    }

    public void RemoveAllNodesAndLinks() {
        myNetworkNodes = new GraphicViewerNodeArrayList();
        myNetworkLinks = new GraphicViewerLinkArrayList();
        myMapGoObjToNode = new HashMap();
        myMapGoObjToLink = new HashMap();
    }

    public void AddNode(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        myNetworkNodes.Add(paramGraphicViewerTreeNetworkNode);
        GraphicViewerObject localGraphicViewerObject = paramGraphicViewerTreeNetworkNode
                .getGraphicViewerObject();
        if (localGraphicViewerObject != null) {
            myMapGoObjToNode.put(localGraphicViewerObject,
                    paramGraphicViewerTreeNetworkNode);
        }
        paramGraphicViewerTreeNetworkNode.setNetwork(this);
    }

    public GraphicViewerTreeNetworkNode AddNode(
            GraphicViewerObject paramGraphicViewerObject) {
        if (paramGraphicViewerObject == null) {
            return null;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = FindNode(paramGraphicViewerObject);
        if (localGraphicViewerTreeNetworkNode == null) {
            localGraphicViewerTreeNetworkNode = CreateNetworkNode();
            localGraphicViewerTreeNetworkNode
                    .setGraphicViewerObject(paramGraphicViewerObject);
            AddNode(localGraphicViewerTreeNetworkNode);
        }
        return localGraphicViewerTreeNetworkNode;
    }

    public void DeleteNode(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        int i = myNetworkNodes.IndexOf(paramGraphicViewerTreeNetworkNode);
        if (i != -1) {
            RemoveNode(paramGraphicViewerTreeNetworkNode, i);
            GraphicViewerLinkArrayList localGraphicViewerLinkArrayList = paramGraphicViewerTreeNetworkNode
                    .getSourceLinksList();
            GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink;
            for (int j = localGraphicViewerLinkArrayList.getCount() - 1; j >= 0; j--) {
                localGraphicViewerTreeNetworkLink = localGraphicViewerLinkArrayList
                        .get(j);
                DeleteLink(localGraphicViewerTreeNetworkLink);
            }
            localGraphicViewerLinkArrayList = paramGraphicViewerTreeNetworkNode
                    .getDestinationLinksList();
            for (int j = localGraphicViewerLinkArrayList.getCount() - 1; j >= 0; j--) {
                localGraphicViewerTreeNetworkLink = localGraphicViewerLinkArrayList
                        .get(j);
                DeleteLink(localGraphicViewerTreeNetworkLink);
            }
        }
    }

    void RemoveNode(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode,
            int paramInt) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        if (paramInt < 0) {
            paramInt = myNetworkNodes
                    .IndexOf(paramGraphicViewerTreeNetworkNode);
        }
        myNetworkNodes.RemoveAt(paramInt);
        GraphicViewerObject localGraphicViewerObject = paramGraphicViewerTreeNetworkNode
                .getGraphicViewerObject();
        if (localGraphicViewerObject != null) {
            myMapGoObjToNode.remove(localGraphicViewerObject);
        }
        paramGraphicViewerTreeNetworkNode.setNetwork(null);
    }

    public void DeleteNode(GraphicViewerObject paramGraphicViewerObject) {
        if (paramGraphicViewerObject == null) {
            return;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = FindNode(paramGraphicViewerObject);
        if (localGraphicViewerTreeNetworkNode != null) {
            DeleteNode(localGraphicViewerTreeNetworkNode);
        }
    }

    public GraphicViewerTreeNetworkNode FindNode(
            GraphicViewerObject paramGraphicViewerObject) {
        if (paramGraphicViewerObject == null) {
            return null;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = null;
        if ((myMapGoObjToNode.get(paramGraphicViewerObject) instanceof GraphicViewerTreeNetworkNode)) {
            localGraphicViewerTreeNetworkNode = (GraphicViewerTreeNetworkNode) myMapGoObjToNode
                    .get(paramGraphicViewerObject);
        }
        return localGraphicViewerTreeNetworkNode;
    }

    public void AddLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        if (paramGraphicViewerTreeNetworkLink == null) {
            return;
        }
        myNetworkLinks.Add(paramGraphicViewerTreeNetworkLink);
        GraphicViewerObject localGraphicViewerObject = paramGraphicViewerTreeNetworkLink
                .getGraphicViewerObject();
        if ((localGraphicViewerObject != null)
                && (FindLink(localGraphicViewerObject) == null)) {
            myMapGoObjToLink.put(localGraphicViewerObject,
                    paramGraphicViewerTreeNetworkLink);
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkLink
                .getToNode();
        if (localGraphicViewerTreeNetworkNode1 != null) {
            localGraphicViewerTreeNetworkNode1
                    .AddSourceLink(paramGraphicViewerTreeNetworkLink);
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = paramGraphicViewerTreeNetworkLink
                .getFromNode();
        if (localGraphicViewerTreeNetworkNode2 != null) {
            localGraphicViewerTreeNetworkNode2
                    .AddDestinationLink(paramGraphicViewerTreeNetworkLink);
        }
        paramGraphicViewerTreeNetworkLink.setNetwork(this);
    }

    public GraphicViewerTreeNetworkLink AddLink(
            GraphicViewerLink paramGraphicViewerLink) {
        if (paramGraphicViewerLink == null) {
            return null;
        }
        GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = FindLink(paramGraphicViewerLink);
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode;
        if (localGraphicViewerTreeNetworkLink == null) {
            localGraphicViewerTreeNetworkLink = CreateNetworkLink();
            localGraphicViewerTreeNetworkLink
                    .setGraphicViewerObject(paramGraphicViewerLink);
            if (paramGraphicViewerLink.getFromPort().getParentNode() != null) {
                localGraphicViewerTreeNetworkNode = AddNode(paramGraphicViewerLink
                        .getFromPort().getParentGraphicViewerNode());
                localGraphicViewerTreeNetworkLink
                        .setFromNode(localGraphicViewerTreeNetworkNode);
            }
            if (paramGraphicViewerLink.getToPort().getParentNode() != null) {
                localGraphicViewerTreeNetworkNode = AddNode(paramGraphicViewerLink
                        .getToPort().getParentGraphicViewerNode());
                localGraphicViewerTreeNetworkLink
                        .setToNode(localGraphicViewerTreeNetworkNode);
            }
            AddLink(localGraphicViewerTreeNetworkLink);
        } else {
            if (paramGraphicViewerLink.getFromPort().getParentNode() != null) {
                localGraphicViewerTreeNetworkNode = AddNode(paramGraphicViewerLink
                        .getFromPort().getParentGraphicViewerNode());
                localGraphicViewerTreeNetworkLink
                        .setFromNode(localGraphicViewerTreeNetworkNode);
            } else {
                localGraphicViewerTreeNetworkLink.setFromNode(null);
            }
            if (paramGraphicViewerLink.getToPort().getParentNode() != null) {
                localGraphicViewerTreeNetworkNode = AddNode(paramGraphicViewerLink
                        .getToPort().getParentGraphicViewerNode());
                localGraphicViewerTreeNetworkLink
                        .setToNode(localGraphicViewerTreeNetworkNode);
            } else {
                localGraphicViewerTreeNetworkLink.setToNode(null);
            }
        }
        return localGraphicViewerTreeNetworkLink;
    }

    public void DeleteLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        if (paramGraphicViewerTreeNetworkLink == null) {
            return;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkLink
                .getToNode();
        if (localGraphicViewerTreeNetworkNode1 != null) {
            localGraphicViewerTreeNetworkNode1
                    .DeleteSourceLink(paramGraphicViewerTreeNetworkLink);
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = paramGraphicViewerTreeNetworkLink
                .getFromNode();
        if (localGraphicViewerTreeNetworkNode2 != null) {
            localGraphicViewerTreeNetworkNode2
                    .DeleteDestinationLink(paramGraphicViewerTreeNetworkLink);
        }
        RemoveLink(paramGraphicViewerTreeNetworkLink);
    }

    void RemoveLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        if (paramGraphicViewerTreeNetworkLink == null) {
            return;
        }
        int i = myNetworkLinks.IndexOf(paramGraphicViewerTreeNetworkLink);
        if (i != -1) {
            myNetworkLinks.RemoveAt(i);
            GraphicViewerObject localGraphicViewerObject = paramGraphicViewerTreeNetworkLink
                    .getGraphicViewerObject();
            if ((localGraphicViewerObject != null)
                    && (FindLink(localGraphicViewerObject) == paramGraphicViewerTreeNetworkLink)) {
                myMapGoObjToLink.remove(localGraphicViewerObject);
            }
            paramGraphicViewerTreeNetworkLink.setNetwork(null);
        }
    }

    public void DeleteLink(GraphicViewerLink paramGraphicViewerLink) {
        if (paramGraphicViewerLink == null) {
            return;
        }
        GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = FindLink(paramGraphicViewerLink);
        if (localGraphicViewerTreeNetworkLink != null) {
            DeleteLink(localGraphicViewerTreeNetworkLink);
        }
    }

    public GraphicViewerTreeNetworkLink FindLink(
            GraphicViewerObject paramGraphicViewerObject) {
        if (paramGraphicViewerObject == null) {
            return null;
        }
        GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = null;
        if ((myMapGoObjToLink.get(paramGraphicViewerObject) instanceof GraphicViewerTreeNetworkLink)) {
            localGraphicViewerTreeNetworkLink = (GraphicViewerTreeNetworkLink) myMapGoObjToLink
                    .get(paramGraphicViewerObject);
        }
        return localGraphicViewerTreeNetworkLink;
    }

    public GraphicViewerTreeNetworkLink LinkNodes(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode1,
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode2,
            GraphicViewerObject paramGraphicViewerObject) {
        if ((paramGraphicViewerTreeNetworkNode1 == null)
                || (paramGraphicViewerTreeNetworkNode2 == null)) {
            return null;
        }
        if ((paramGraphicViewerTreeNetworkNode1.getNetwork() == this)
                && (paramGraphicViewerTreeNetworkNode2.getNetwork() == this)) {
            GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = CreateNetworkLink();
            localGraphicViewerTreeNetworkLink
                    .setGraphicViewerObject(paramGraphicViewerObject);
            localGraphicViewerTreeNetworkLink
                    .setFromNode(paramGraphicViewerTreeNetworkNode1);
            localGraphicViewerTreeNetworkLink
                    .setToNode(paramGraphicViewerTreeNetworkNode2);
            AddLink(localGraphicViewerTreeNetworkLink);
            return localGraphicViewerTreeNetworkLink;
        }
        return null;
    }

    public void ReverseLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        if (paramGraphicViewerTreeNetworkLink == null) {
            return;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkLink
                .getFromNode();
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = paramGraphicViewerTreeNetworkLink
                .getToNode();
        if ((localGraphicViewerTreeNetworkNode1 == null)
                || (localGraphicViewerTreeNetworkNode2 == null)) {
            return;
        }
        localGraphicViewerTreeNetworkNode1
                .DeleteDestinationLink(paramGraphicViewerTreeNetworkLink);
        localGraphicViewerTreeNetworkNode2
                .DeleteSourceLink(paramGraphicViewerTreeNetworkLink);
        paramGraphicViewerTreeNetworkLink.ReverseLink();
        localGraphicViewerTreeNetworkNode1
                .AddSourceLink(paramGraphicViewerTreeNetworkLink);
        localGraphicViewerTreeNetworkNode2
                .AddDestinationLink(paramGraphicViewerTreeNetworkLink);
    }

    public void DeleteSelfLinks() {
        GraphicViewerLinkArrayList localGraphicViewerLinkArrayList = new GraphicViewerLinkArrayList();
        for (int i = 0; i < getLinks().getCount(); i++) {
            GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = getLinks()
                    .get(i);
            if ((localGraphicViewerTreeNetworkLink != null)
                    && (localGraphicViewerTreeNetworkLink.getFromNode() == localGraphicViewerTreeNetworkLink
                            .getToNode())) {
                localGraphicViewerLinkArrayList
                        .Add(localGraphicViewerTreeNetworkLink);
            }
            for (int j = 0; i < localGraphicViewerLinkArrayList.getCount(); j++) {
                DeleteLink(localGraphicViewerLinkArrayList.get(j));
            }
        }
    }

    public void DeleteArtificialNodes() {
        GraphicViewerNodeArrayList localGraphicViewerNodeArrayList = new GraphicViewerNodeArrayList();
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode;
        for (int i = 0; i < getNodes().getCount(); i++) {
            localGraphicViewerTreeNetworkNode = getNodes().get(i);
            if (localGraphicViewerTreeNetworkNode.getGraphicViewerObject() == null) {
                localGraphicViewerNodeArrayList
                        .Add(localGraphicViewerTreeNetworkNode);
            }
        }
        for (int i = 0; i < localGraphicViewerNodeArrayList.getCount(); i++) {
            localGraphicViewerTreeNetworkNode = localGraphicViewerNodeArrayList
                    .get(i);
            DeleteNode(localGraphicViewerTreeNetworkNode);
        }
        GraphicViewerLinkArrayList localGraphicViewerLinkArrayList = new GraphicViewerLinkArrayList();
        GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink;
        for (int j = 0; j < getLinks().getCount(); j++) {
            localGraphicViewerTreeNetworkLink = getLinks().get(j);
            if (localGraphicViewerTreeNetworkLink.getGraphicViewerObject() == null) {
                localGraphicViewerLinkArrayList
                        .Add(localGraphicViewerTreeNetworkLink);
            }
        }
        for (int j = 0; j < localGraphicViewerLinkArrayList.getCount(); j++) {
            localGraphicViewerTreeNetworkLink = localGraphicViewerLinkArrayList
                    .get(j);
            DeleteLink(localGraphicViewerTreeNetworkLink);
        }
    }

    void DeleteUselessLinks() {
        GraphicViewerLinkArrayList localGraphicViewerLinkArrayList = new GraphicViewerLinkArrayList();
        for (int i = 0; i < getLinks().getCount(); i++) {
            GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = getLinks()
                    .get(i);
            if ((localGraphicViewerTreeNetworkLink.getFromNode() == null)
                    || (localGraphicViewerTreeNetworkLink.getToNode() == null)) {
                localGraphicViewerLinkArrayList
                        .Add(localGraphicViewerTreeNetworkLink);
            }
        }
        for (int i = 0; i < localGraphicViewerLinkArrayList.getCount(); i++) {
            DeleteLink(localGraphicViewerLinkArrayList.get(i));
        }
    }

    static boolean IsSingleton(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode.getSourceLinksList().getCount() > 0) {
            return false;
        }
        return paramGraphicViewerTreeNetworkNode.getDestinationLinksList()
                .getCount() <= 0;
    }

    public ArrayList SplitIntoSubNetworks() {
        DeleteArtificialNodes();
        DeleteUselessLinks();
        DeleteSelfLinks();
        ArrayList localArrayList1 = new ArrayList();
        int i = 1;
        Object localObject;
        while (i != 0) {
            i = 0;
            for (int j = 0; j < getNodes().getCount(); j++) {
                GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = getNodes()
                        .get(j);
                if (!IsSingleton(localGraphicViewerTreeNetworkNode)) {
                    localObject = new GraphicViewerTreeNetwork();
                    localArrayList1.add(localObject);
                    TraverseSubnet((GraphicViewerTreeNetwork) localObject,
                            localGraphicViewerTreeNetworkNode);
                    i = 1;
                    break;
                }
            }
        }
        ArrayList localArrayList2 = new ArrayList();
        while (localArrayList1.listIterator().hasNext()) {
            int k = -1;
            localObject = null;
            ListIterator localListIterator1 = localArrayList1.listIterator();
            ListIterator localListIterator2 = localListIterator1;
            while (localListIterator1.hasNext()) {
                GraphicViewerTreeNetwork localGraphicViewerTreeNetwork = (GraphicViewerTreeNetwork) localListIterator1
                        .next();
                if (localGraphicViewerTreeNetwork.getNodes().getCount() > k) {
                    k = localGraphicViewerTreeNetwork.getNodes().getCount();
                    localObject = localGraphicViewerTreeNetwork;
                }
            }
            localArrayList2.add(localObject);
            localListIterator1.remove();
        }
        return localArrayList2;
    }

    private void TraverseSubnet(
            GraphicViewerTreeNetwork paramGraphicViewerTreeNetwork,
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        if (paramGraphicViewerTreeNetworkNode.getNetwork() == paramGraphicViewerTreeNetwork) {
            return;
        }
        RemoveNode(paramGraphicViewerTreeNetworkNode, -1);
        paramGraphicViewerTreeNetwork
                .AddNode(paramGraphicViewerTreeNetworkNode);
        GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink;
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode
                .getSourceLinksList().getCount(); i++) {
            localGraphicViewerTreeNetworkLink = paramGraphicViewerTreeNetworkNode
                    .getSourceLinksList().get(i);
            if (localGraphicViewerTreeNetworkLink.getNetwork() != paramGraphicViewerTreeNetwork) {
                RemoveLink(localGraphicViewerTreeNetworkLink);
                paramGraphicViewerTreeNetwork
                        .AddLink(localGraphicViewerTreeNetworkLink);
                TraverseSubnet(paramGraphicViewerTreeNetwork,
                        localGraphicViewerTreeNetworkLink.getFromNode());
            }
        }
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode
                .getDestinationLinksList().getCount(); i++) {
            localGraphicViewerTreeNetworkLink = paramGraphicViewerTreeNetworkNode
                    .getDestinationLinksList().get(i);
            RemoveLink(localGraphicViewerTreeNetworkLink);
            paramGraphicViewerTreeNetwork
                    .AddLink(localGraphicViewerTreeNetworkLink);
            TraverseSubnet(paramGraphicViewerTreeNetwork,
                    localGraphicViewerTreeNetworkLink.getToNode());
        }
    }

    public IGraphicViewerObjectCollection GetNodesAndLinks(
            IGraphicViewerObjectCollection paramGraphicViewerObjectCollection) {
        if (paramGraphicViewerObjectCollection == null) {
            paramGraphicViewerObjectCollection = new GraphicViewerCollection();
        }
        Object localObject;
        for (int i = 0; i < getNodes().getCount(); i++) {
            localObject = getNodes().get(i);
            paramGraphicViewerObjectCollection
                    .addObjectAtTail(((GraphicViewerTreeNetworkNode) localObject)
                            .getGraphicViewerObject());
        }
        for (int i = 0; i < getLinks().getCount(); i++) {
            localObject = getLinks().get(i);
            paramGraphicViewerObjectCollection
                    .addObjectAtTail(((GraphicViewerTreeNetworkLink) localObject)
                            .getGraphicViewerObject());
        }
        return paramGraphicViewerObjectCollection;
    }

    public int getNodeCount() {
        return myNetworkNodes.getCount();
    }

    public int getLinkCount() {
        return myNetworkLinks.getCount();
    }

    public GraphicViewerTreeAutoLayout getLayout() {
        return myLayout;
    }

    public GraphicViewerNodeArrayList getNodes() {
        return myNetworkNodes;
    }

    public GraphicViewerLinkArrayList getLinks() {
        return myNetworkLinks;
    }

    public void setLayout(
            GraphicViewerTreeAutoLayout paramGraphicViewerTreeAutoLayout) {
        myLayout = paramGraphicViewerTreeAutoLayout;
    }
}