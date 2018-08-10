/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphicviewer.layout;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerLink;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerListPosition;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerObjectSimpleCollection;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerPort;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerSelection;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Network {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    @SuppressWarnings("rawtypes")
    class a implements Comparator {

        public int compare(Object obj, Object obj1) {
            Network graphicViewerNetwork = (Network) obj;
            Network graphicViewerNetwork1 = (Network) obj1;
            if (graphicViewerNetwork != null && graphicViewerNetwork1 != null) {
                if (graphicViewerNetwork.getNodeCount() < graphicViewerNetwork1
                        .getNodeCount())
                    return 1;
                return graphicViewerNetwork.getNodeCount() != graphicViewerNetwork1
                        .getNodeCount() ? -1 : 0;
            } else {
                return 0;
            }
        }

        a() {
        }
    }

    @SuppressWarnings("rawtypes")
    public Network() {
        _fldnew = new LinkedList();
        _fldif = null;
        _fldint = new LinkedList();
        a = null;
        _fldfor = new HashMap();
        _flddo = new HashMap();
    }

    @SuppressWarnings("rawtypes")
    public Network(
            GraphicViewerObjectSimpleCollection graphicViewerObjectSimpleCollection) {
        _fldnew = new LinkedList();
        _fldif = null;
        _fldint = new LinkedList();
        a = null;
        _fldfor = new HashMap();
        _flddo = new HashMap();
        addNodesAndLinksFromCollection(graphicViewerObjectSimpleCollection);
    }

    @SuppressWarnings("unchecked")
    public void addNodesAndLinksFromCollection(
            GraphicViewerObjectSimpleCollection graphicViewerObjectSimpleCollection) {
        if (graphicViewerObjectSimpleCollection == null)
            return;
        GraphicViewerListPosition graphicViewerListPosition = graphicViewerObjectSimpleCollection
                .getFirstObjectPos();
        do {
            if (graphicViewerListPosition == null)
                break;
            GraphicViewerObject graphicViewerObject = graphicViewerObjectSimpleCollection
                    .getObjectAtPos(graphicViewerListPosition);
            graphicViewerListPosition = graphicViewerObjectSimpleCollection
                    .getNextObjectPosAtTop(graphicViewerListPosition);
            GraphicViewerObject graphicViewerObject2 = graphicViewerObject
                    .getPartner();
            if ((graphicViewerObject2 == null || !graphicViewerObject2
                    .isTopLevel())
                    && !(graphicViewerObject instanceof GraphicViewerLink)
                    && !(graphicViewerObject instanceof GraphicViewerPort)
                    && findNode(graphicViewerObject) == null) {
                NetworkNode graphicViewerNetworkNode = createNetworkNode();
                graphicViewerNetworkNode.setNetwork(this);
                graphicViewerNetworkNode
                        .setGraphicViewerObject(graphicViewerObject);
                addNode(graphicViewerNetworkNode);
                _fldfor.put(graphicViewerObject, graphicViewerNetworkNode);
            }
        } while (true);
        graphicViewerListPosition = graphicViewerObjectSimpleCollection
                .getFirstObjectPos();
        do {
            if (graphicViewerListPosition == null)
                break;
            GraphicViewerObject graphicViewerObject1 = graphicViewerObjectSimpleCollection
                    .getObjectAtPos(graphicViewerListPosition);
            graphicViewerListPosition = graphicViewerObjectSimpleCollection
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
                            .getParent());
                    Object obj1;
                    for (obj1 = graphicViewerPort1; ((GraphicViewerObject) (obj1))
                            .getParent() != null
                            && findNode(((GraphicViewerObject) (obj1))) == null; obj1 = ((GraphicViewerObject) (obj1))
                            .getParent());
                    if (obj != obj1) {
                        NetworkNode graphicViewerNetworkNode1 = findNode(((GraphicViewerObject) (obj)));
                        NetworkNode graphicViewerNetworknode2 = findNode(((GraphicViewerObject) (obj1)));
                        if (graphicViewerNetworkNode1 != null
                                && graphicViewerNetworknode2 != null) {
                            NetworkLink graphicViewerNetworkLink = linkNodes(
                                    graphicViewerNetworkNode1,
                                    graphicViewerNetworknode2,
                                    graphicViewerObject1);
                            _flddo.put(graphicViewerObject1,
                                    graphicViewerNetworkLink);
                        }
                    }
                }
            }
        } while (true);
    }

    @SuppressWarnings("rawtypes")
    public void removeAllNodesAndLinks() {
        _fldnew = new LinkedList();
        _fldif = null;
        _fldint = new LinkedList();
        a = null;
        _fldfor = new HashMap();
        _flddo = new HashMap();
    }

    public NetworkNode createNetworkNode() {
        return new NetworkNode();
    }

    @SuppressWarnings("unchecked")
    public void addNode(NetworkNode graphicViewerNetworkNode) {
        _fldnew.addLast(graphicViewerNetworkNode);
        _fldif = null;
        graphicViewerNetworkNode.setNetwork(this);
        GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                .getGraphicViewerObject();
        if (graphicViewerObject != null)
            _fldfor.put(graphicViewerObject, graphicViewerNetworkNode);
    }

    public NetworkNode addNode(GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null)
            return null;
        NetworkNode graphicViewerNetworkNode = findNode(graphicViewerObject);
        if (graphicViewerNetworkNode == null) {
            graphicViewerNetworkNode = createNetworkNode();
            graphicViewerNetworkNode.setNetwork(this);
            graphicViewerNetworkNode
                    .setGraphicViewerObject(graphicViewerObject);
            addNode(graphicViewerNetworkNode);
        }
        return graphicViewerNetworkNode;
    }

    public void deleteNode(NetworkNode graphicViewerNetworkNode) {
        if (graphicViewerNetworkNode == null)
            return;
        int i = _fldnew.indexOf(graphicViewerNetworkNode);
        if (i != -1) {
            a(graphicViewerNetworkNode, i);
            NetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getPredLinksArray();
            for (int j = aGraphicViewerNetworkLink.length - 1; j >= 0; j--) {
                NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[j];
                deleteLink(graphicViewerNetworkLink);
            }

            aGraphicViewerNetworkLink = graphicViewerNetworkNode
                    .getSuccLinksArray();
            for (int k = aGraphicViewerNetworkLink.length - 1; k >= 0; k--) {
                NetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink[k];
                deleteLink(graphicViewerNetworkLink1);
            }

        }
    }

    void a(NetworkNode graphicViewerNetworkNode, int i) {
        if (graphicViewerNetworkNode == null)
            return;
        if (i < 0)
            i = _fldnew.indexOf(graphicViewerNetworkNode);
        @SuppressWarnings("unused")
        Object obj = _fldnew.remove(i);
        _fldif = null;
        GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                .getGraphicViewerObject();
        if (graphicViewerObject != null)
            _fldfor.remove(graphicViewerObject);
        graphicViewerNetworkNode.setNetwork(null);
    }

    public void deleteNode(GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null)
            return;
        NetworkNode graphicViewerNetworkNode = findNode(graphicViewerObject);
        if (graphicViewerNetworkNode != null)
            deleteNode(graphicViewerNetworkNode);
    }

    public NetworkNode findNode(GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null)
            return null;
        Object obj = _fldfor.get(graphicViewerObject);
        if (obj != null && (obj instanceof NetworkNode))
            return (NetworkNode) obj;
        else
            return null;
    }

    public NetworkLink createNetworkLink() {
        return new NetworkLink();
    }

    @SuppressWarnings("unchecked")
    public void addLink(NetworkLink graphicViewerNetworkLink) {
        if (graphicViewerNetworkLink == null)
            return;
        _fldint.addLast(graphicViewerNetworkLink);
        a = null;
        GraphicViewerObject graphicViewerObject = graphicViewerNetworkLink
                .getGraphicViewerObject();
        if (graphicViewerObject != null
                && findLink(graphicViewerObject) == null)
            _flddo.put(graphicViewerObject, graphicViewerNetworkLink);
        NetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getToNode();
        if (graphicViewerNetworkNode != null)
            graphicViewerNetworkNode.addPredLink(graphicViewerNetworkLink);
        NetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getFromNode();
        if (graphicViewerNetworkNode1 != null)
            graphicViewerNetworkNode1.addSuccLink(graphicViewerNetworkLink);
        graphicViewerNetworkLink.setNetwork(this);
    }

    public NetworkLink addLink(GraphicViewerLink graphicViewerLink) {
        if (graphicViewerLink == null)
            return null;
        NetworkLink graphicViewerNetworkLink = findLink(graphicViewerLink);
        GraphicViewerPort graphicViewerPort = graphicViewerLink.getFromPort();
        GraphicViewerPort graphicViewerPort1 = graphicViewerLink.getToPort();
        if (graphicViewerNetworkLink == null && graphicViewerPort != null
                && graphicViewerPort1 != null) {
            Object obj;
            for (obj = graphicViewerPort; ((GraphicViewerObject) (obj))
                    .getParent() != null
                    && findNode(((GraphicViewerObject) (obj))) == null; obj = ((GraphicViewerObject) (obj))
                    .getParent());
            Object obj1;
            for (obj1 = graphicViewerPort1; ((GraphicViewerObject) (obj1))
                    .getParent() != null
                    && findNode(((GraphicViewerObject) (obj1))) == null; obj1 = ((GraphicViewerObject) (obj1))
                    .getParent());
            NetworkNode graphicViewerNetworkNode = addNode(((GraphicViewerObject) (obj)));
            NetworkNode graphicViewerNetworkNode1 = addNode(((GraphicViewerObject) (obj1)));
            graphicViewerNetworkLink = linkNodes(graphicViewerNetworkNode,
                    graphicViewerNetworkNode1, graphicViewerLink);
        }
        return graphicViewerNetworkLink;
    }

    public void deleteLink(NetworkLink graphicViewerNetworkLink) {
        NetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getToNode();
        if (graphicViewerNetworkNode != null)
            graphicViewerNetworkNode.deletePredLink(graphicViewerNetworkLink);
        NetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getFromNode();
        if (graphicViewerNetworkNode1 != null)
            graphicViewerNetworkNode1.deleteSuccLink(graphicViewerNetworkLink);
        a(graphicViewerNetworkLink);
    }

    void a(NetworkLink graphicViewerNetworkLink) {
        if (graphicViewerNetworkLink == null)
            return;
        int i = _fldint.indexOf(graphicViewerNetworkLink);
        if (i != -1) {
            @SuppressWarnings("unused")
            Object obj = _fldint.remove(i);
            a = null;
            GraphicViewerObject graphicViewerObject = graphicViewerNetworkLink
                    .getGraphicViewerObject();
            if (graphicViewerObject != null
                    && findLink(graphicViewerObject) == graphicViewerNetworkLink)
                _flddo.remove(graphicViewerObject);
            graphicViewerNetworkLink.setNetwork(null);
        }
    }

    public void deleteLink(GraphicViewerLink graphicViewerLink) {
        if (graphicViewerLink == null)
            return;
        NetworkLink graphicViewerNetworkLink = findLink(graphicViewerLink);
        if (graphicViewerNetworkLink != null)
            deleteLink(graphicViewerNetworkLink);
    }

    public NetworkLink findLink(GraphicViewerObject graphicViewerObject) {
        if (graphicViewerObject == null)
            return null;
        Object obj = _flddo.get(graphicViewerObject);
        if (obj != null && (obj instanceof NetworkLink))
            return (NetworkLink) obj;
        else
            return null;
    }

    public NetworkLink linkNodes(NetworkNode graphicViewerNetworkNode,
            NetworkNode graphicViewerNetworkNode1,
            GraphicViewerObject graphicViewerObject) {
        if (graphicViewerNetworkNode.getNetwork() == this
                && graphicViewerNetworkNode1.getNetwork() == this) {
            NetworkLink graphicViewerNetworkLink = createNetworkLink();
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

    public void reverseLink(NetworkLink graphicViewerNetworkLink) {
        NetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getFromNode();
        NetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getToNode();
        graphicViewerNetworkNode.deleteSuccLink(graphicViewerNetworkLink);
        graphicViewerNetworkNode1.deletePredLink(graphicViewerNetworkLink);
        graphicViewerNetworkLink.reverseLink();
        graphicViewerNetworkNode.addPredLink(graphicViewerNetworkLink);
        graphicViewerNetworkNode1.addSuccLink(graphicViewerNetworkLink);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void deleteSelfLinks() {
        ArrayList arraylist = new ArrayList();
        NetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int i = 0; i < aGraphicViewerNetworkLink.length; i++) {
            NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i];
            if (graphicViewerNetworkLink.getFromNode() == graphicViewerNetworkLink
                    .getToNode())
                arraylist.add(graphicViewerNetworkLink);
        }

        for (int j = 0; j < arraylist.size(); j++)
            deleteLink((NetworkLink) arraylist.get(j));

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void deleteArtificialNodes() {
        ArrayList arraylist = new ArrayList();
        NetworkNode aGraphicViewerNetworkNode[] = getNodeArray();
        for (int i = 0; i < aGraphicViewerNetworkNode.length; i++) {
            NetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
            if (graphicViewerNetworkNode.getGraphicViewerObject() == null)
                arraylist.add(graphicViewerNetworkNode);
        }

        for (int j = 0; j < arraylist.size(); j++)
            deleteNode((NetworkNode) arraylist.get(j));

        arraylist.clear();
        NetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            if (graphicViewerNetworkLink.getGraphicViewerObject() == null)
                arraylist.add(graphicViewerNetworkLink);
        }

        for (int l = 0; l < arraylist.size(); l++)
            deleteLink((NetworkLink) arraylist.get(l));

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void a() {
        ArrayList arraylist = new ArrayList();
        NetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int i = 0; i < aGraphicViewerNetworkLink.length; i++) {
            NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i];
            if (graphicViewerNetworkLink.getFromNode() == null
                    || graphicViewerNetworkLink.getToNode() == null)
                arraylist.add(graphicViewerNetworkLink);
        }

        for (int j = 0; j < arraylist.size(); j++)
            deleteLink((NetworkLink) arraylist.get(j));

    }

    boolean a(NetworkNode graphicViewerNetworkNode) {
        if (!graphicViewerNetworkNode.getPredLinksList().isEmpty())
            return false;
        return graphicViewerNetworkNode.getSuccLinksList().isEmpty();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Network[] splitIntoSubNetworks() {
        deleteArtificialNodes();
        a();
        deleteSelfLinks();
        ArrayList arraylist = new ArrayList();
        boolean flag = true;
        label0 : do {
            if (flag) {
                flag = false;
                NetworkNode aGraphicViewerNetworkNode[] = getNodeArray();
                int i = 0;
                do {
                    if (i >= aGraphicViewerNetworkNode.length)
                        continue label0;
                    NetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
                    if (!a(graphicViewerNetworkNode)) {
                        Network graphicViewerNetwork = new Network();
                        arraylist.add(graphicViewerNetwork);
                        a(graphicViewerNetwork, graphicViewerNetworkNode);
                        flag = true;
                        continue label0;
                    }
                    i++;
                } while (true);
            }
            Network aGraphicViewerNetwork[] = new Network[arraylist.size()];
            for (int j = 0; j < aGraphicViewerNetwork.length; j++)
                aGraphicViewerNetwork[j] = (Network) arraylist.get(j);

            Arrays.sort(aGraphicViewerNetwork, new a());
            return aGraphicViewerNetwork;
        } while (true);
    }

    @SuppressWarnings("rawtypes")
    private void a(Network graphicViewerNetwork,
            NetworkNode graphicViewerNetworkNode) {
        if (graphicViewerNetworkNode == null)
            return;
        if (graphicViewerNetworkNode.getNetwork() == graphicViewerNetwork)
            return;
        a(graphicViewerNetworkNode, -1);
        graphicViewerNetwork.addNode(graphicViewerNetworkNode);
        Iterator iterator = graphicViewerNetworkNode.getPredLinksList()
                .iterator();
        do {
            if (!iterator.hasNext())
                break;
            NetworkLink graphicViewerNetworkLink = (NetworkLink) iterator
                    .next();
            if (graphicViewerNetworkLink.getNetwork() != graphicViewerNetwork) {
                a(graphicViewerNetworkLink);
                graphicViewerNetwork.addLink(graphicViewerNetworkLink);
                a(graphicViewerNetwork, graphicViewerNetworkLink.getFromNode());
            }
        } while (true);
        iterator = graphicViewerNetworkNode.getSuccLinksList().iterator();
        do {
            if (!iterator.hasNext())
                break;
            NetworkLink graphicViewerNetworkLink1 = (NetworkLink) iterator
                    .next();
            if (graphicViewerNetworkLink1.getNetwork() != graphicViewerNetwork) {
                a(graphicViewerNetworkLink1);
                graphicViewerNetwork.addLink(graphicViewerNetworkLink1);
                a(graphicViewerNetwork, graphicViewerNetworkLink1.getToNode());
            }
        } while (true);
    }

    @SuppressWarnings("rawtypes")
    public GraphicViewerSelection getNodesAndLinks(
            GraphicViewerSelection graphicViewerSelection) {
        if (graphicViewerSelection == null)
            graphicViewerSelection = new GraphicViewerSelection(null);
        Iterator iterator = getNodeIterator();
        do {
            if (!iterator.hasNext())
                break;
            NetworkNode graphicViewerNetworkNode = (NetworkNode) iterator
                    .next();
            GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                    .getGraphicViewerObject();
            if (graphicViewerObject != null
                    && !graphicViewerSelection
                            .isInSelection(graphicViewerObject))
                graphicViewerSelection.extendSelection(graphicViewerObject);
        } while (true);
        iterator = getLinkIterator();
        do {
            if (!iterator.hasNext())
                break;
            NetworkLink graphicViewerNetworkLink = (NetworkLink) iterator
                    .next();
            GraphicViewerObject graphicViewerObject1 = graphicViewerNetworkLink
                    .getGraphicViewerObject();
            if (graphicViewerObject1 != null
                    && !graphicViewerSelection
                            .isInSelection(graphicViewerObject1))
                graphicViewerSelection.extendSelection(graphicViewerObject1);
        } while (true);
        return graphicViewerSelection;
    }

    public int getNodeCount() {
        return _fldnew.size();
    }

    @SuppressWarnings("rawtypes")
    public Iterator getNodeIterator() {
        return _fldnew.iterator();
    }

    @SuppressWarnings("rawtypes")
    public Iterator getNodeIterator(int i) {
        if (_fldnew.size() < i)
            return _fldnew.iterator();
        else
            return _fldnew.listIterator(i);
    }

    public int getLinkCount() {
        return _fldint.size();
    }

    @SuppressWarnings("rawtypes")
    public Iterator getLinkIterator() {
        return _fldint.iterator();
    }

    @SuppressWarnings("rawtypes")
    public Iterator getLinkIterator(int i) {
        if (i > _fldint.size())
            return _fldint.iterator();
        else
            return _fldint.listIterator(i);
    }

    public void commitNodes() {
        NetworkNode aGraphicViewerNetworkNode[] = getNodeArray();
        for (int i = 0; i < aGraphicViewerNetworkNode.length; i++) {
            NetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
            graphicViewerNetworkNode.commitPosition();
        }

    }

    public void commitLinks() {
        NetworkLink aGraphicViewerNetworkLink[] = getLinkArray();
        for (int i = 0; i < aGraphicViewerNetworkLink.length; i++) {
            NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i];
            graphicViewerNetworkLink.commitPosition();
        }

    }

    public void commitNodesAndLinks() {
        commitNodes();
        commitLinks();
    }

    @SuppressWarnings("unchecked")
    public NetworkLink[] getLinkArray() {
        if (a == null) {
            a = new NetworkLink[getLinkCount()];
            _fldint.toArray(a);
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public NetworkNode[] getNodeArray() {
        if (_fldif == null) {
            _fldif = new NetworkNode[getNodeCount()];
            _fldnew.toArray(_fldif);
        }
        return _fldif;
    }

    @SuppressWarnings("rawtypes")
    protected LinkedList getNetworkLinks() {
        return _fldint;
    }

    @SuppressWarnings("rawtypes")
    protected LinkedList getNetworkNodes() {
        return _fldnew;
    }

    @SuppressWarnings("rawtypes")
    protected HashMap getGoObjToNodeMap() {
        return _fldfor;
    }

    @SuppressWarnings("rawtypes")
    protected HashMap getGoObjToLinkMap() {
        return _flddo;
    }

    @SuppressWarnings("rawtypes")
    private LinkedList _fldnew;
    private NetworkNode _fldif[];
    @SuppressWarnings("rawtypes")
    private LinkedList _fldint;
    private NetworkLink a[];
    @SuppressWarnings("rawtypes")
    private HashMap _fldfor;
    @SuppressWarnings("rawtypes")
    private HashMap _flddo;
}