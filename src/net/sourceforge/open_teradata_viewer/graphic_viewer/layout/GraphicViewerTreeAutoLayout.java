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

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerCollection;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerListPosition;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerNode;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerPort;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSelection;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IGraphicViewerObjectCollection;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GraphicViewerTreeAutoLayout extends GraphicViewerAutoLayout {

    private IGraphicViewerObjectCollection myRoots = new GraphicViewerCollection();
    private GraphicViewerTreeNetworkNode myRootDefaults;
    private GraphicViewerTreeNetworkNode myAlternateDefaults;
    public static final int PathDestination = 1;
    public static final int PathSource = 2;
    private int myPath = 1;
    public static final int StyleLayered = 1;
    public static final int StyleLastParents = 2;
    public static final int StyleAlternating = 3;
    private int myStyle = 1;
    public static final int ArrangementVertical = 1;
    public static final int ArrangementHorizontal = 2;
    public static final int ArrangementFixedRoots = 3;
    private int myArrangement = 1;
    private Point myArrangementOrigin = new Point(50, 50);
    private Dimension myArrangementSpacing = new Dimension(10, 10);
    private Comparator myAlphabeticNodeTextComparer = new GraphicViewerAlphaComparer();
    private GraphicViewerTreeNetwork myNetwork = null;
    private GraphicViewerDocument myDocument = null;
    private Point[][][] myTempArrays;

    public GraphicViewerTreeAutoLayout() {
        init();
    }

    public GraphicViewerTreeAutoLayout(
            GraphicViewerDocument paramGraphicViewerDocument) {
        init();
        setDocument(paramGraphicViewerDocument);
        setNetwork(new GraphicViewerTreeNetwork(paramGraphicViewerDocument));
    }

    public GraphicViewerTreeAutoLayout(
            GraphicViewerSelection paramGraphicViewerSelection) {
        init();
        setDocument(paramGraphicViewerSelection.getView().getDocument());
        setNetwork(new GraphicViewerTreeNetwork(paramGraphicViewerSelection));
    }

    private void init() {
        myRootDefaults = new GraphicViewerTreeNetworkNode();
        myAlternateDefaults = new GraphicViewerTreeNetworkNode();
    }

    public GraphicViewerTreeNetwork getTreeNetwork() {
        return myNetwork;
    }

    private void setNetwork(
            GraphicViewerTreeNetwork paramGraphicViewerTreeNetwork) {
        myNetwork = paramGraphicViewerTreeNetwork;
    }

    public GraphicViewerNetwork getNetwork() {
        return null;
    }

    public GraphicViewerDocument getDocument() {
        return myDocument;
    }

    public void setDocument(GraphicViewerDocument paramGraphicViewerDocument) {
        myDocument = paramGraphicViewerDocument;
        if (getNetwork() == null) {
            setNetwork(new GraphicViewerTreeNetwork(paramGraphicViewerDocument));
        }
    }

    public void performLayout() {
        if (getDocument() == null) {
            return;
        }
        if (getTreeNetwork() == null) {
            setNetwork(new GraphicViewerTreeNetwork());
            getTreeNetwork().setLayout(this);
            getNetwork().addNodesAndLinksFromCollection(getDocument());
        }
        if (getTreeNetwork().getLayout() == null) {
            getTreeNetwork().setLayout(this);
        }
        progressUpdate(0.0D);
        CreateTrees();
        progressUpdate(0.1000000014901161D);
        InitializeAll();
        progressUpdate(0.1500000059604645D);
        AssignAll();
        progressUpdate(0.2000000029802322D);
        SortAll();
        progressUpdate(0.300000011920929D);
        progressUpdate(0.4D);
        LayoutAll();
        progressUpdate(0.6000000238418579D);
        ArrangeTrees();
        progressUpdate(0.699999988079071D);
        LayoutNodesAndLinks();
        progressUpdate(1.0D);
    }

    public IGraphicViewerObjectCollection getRoots() {
        return myRoots;
    }

    public void setRoots(
            IGraphicViewerObjectCollection paramGraphicViewerObjectCollection) {
        myRoots = paramGraphicViewerObjectCollection;
    }

    public int getPath() {
        return myPath;
    }

    public void setPath(int paramInt) {
        myPath = paramInt;
    }

    protected void CreateTrees() {
        GraphicViewerTreeNetwork localGraphicViewerTreeNetwork = getTreeNetwork();
        localGraphicViewerTreeNetwork.DeleteSelfLinks();
        for (int i = 0; i < localGraphicViewerTreeNetwork.getNodes().getCount(); i++) {
            GraphicViewerTreeNetworkNode localObject = localGraphicViewerTreeNetwork
                    .getNodes().get(i);
            ((GraphicViewerTreeNetworkNode) localObject).setInitialized(false);
            ((GraphicViewerTreeNetworkNode) localObject).setLevel(0);
            ((GraphicViewerTreeNetworkNode) localObject).setParent(null);
            ((GraphicViewerTreeNetworkNode) localObject)
                    .setChildren(GraphicViewerTreeNetworkNode.getNoChildren());
        }
        GraphicViewerCollection localGraphicViewerCollection = new GraphicViewerCollection();
        localGraphicViewerCollection.addCollection(getRoots());
        GraphicViewerObject localGraphicViewerObject;
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode;
        for (Object localObject = localGraphicViewerCollection
                .getFirstObjectPos(); localObject != null; localObject = localGraphicViewerCollection
                .getNextObjectPos((GraphicViewerListPosition) localObject)) {
            localGraphicViewerObject = localGraphicViewerCollection
                    .getObjectAtPos((GraphicViewerListPosition) localObject);
            localGraphicViewerTreeNetworkNode = getTreeNetwork().FindNode(
                    localGraphicViewerObject);
            if (localGraphicViewerTreeNetworkNode == null) {
                getRoots().removeObject(localGraphicViewerObject);
            }
        }
        if (getRoots().isEmpty()) {
            FindRoots();
        }
        localGraphicViewerCollection = new GraphicViewerCollection();
        localGraphicViewerCollection.addCollection(getRoots());
        for (GraphicViewerListPosition localObject = localGraphicViewerCollection
                .getFirstObjectPos(); localObject != null; localObject = localGraphicViewerCollection
                .getNextObjectPos((GraphicViewerListPosition) localObject)) {
            localGraphicViewerObject = localGraphicViewerCollection
                    .getObjectAtPos((GraphicViewerListPosition) localObject);
            localGraphicViewerTreeNetworkNode = getTreeNetwork().FindNode(
                    localGraphicViewerObject);
            if ((localGraphicViewerTreeNetworkNode != null)
                    && (!localGraphicViewerTreeNetworkNode.getInitialized())) {
                localGraphicViewerTreeNetworkNode.setInitialized(true);
                WalkTree(localGraphicViewerTreeNetworkNode);
            }
        }
    }

    public void FindRoots() {
        GraphicViewerNodeArrayList localGraphicViewerNodeArrayList = getTreeNetwork()
                .getNodes();
        for (int i = 0; i < localGraphicViewerNodeArrayList.getCount(); i++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = localGraphicViewerNodeArrayList
                    .get(i);
            switch (getPath()) {
                case 1 :
                    if (localGraphicViewerTreeNetworkNode.getSourceLinksList()
                            .getCount() == 0) {
                        getRoots().addObjectAtTail(
                                localGraphicViewerTreeNetworkNode
                                        .getGraphicViewerObject());
                    }
                    break;
                case 2 :
                    if (localGraphicViewerTreeNetworkNode
                            .getDestinationLinksList().getCount() == 0) {
                        getRoots().addObjectAtTail(
                                localGraphicViewerTreeNetworkNode
                                        .getGraphicViewerObject());
                    }
                    break;
                default :
                    return;
            }
        }
        if (getRoots().isEmpty()) {
            ChooseRoot();
        }
    }

    private void ChooseRoot() {
        int i = 999999;
        GraphicViewerTreeNetworkNode localObject = null;
        for (int j = 0; j < getTreeNetwork().getNodes().getCount(); j++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = getTreeNetwork()
                    .getNodes().get(j);
            switch (getPath()) {
                case 1 :
                    if (localGraphicViewerTreeNetworkNode.getSourceLinksList()
                            .getCount() < i) {
                        i = localGraphicViewerTreeNetworkNode
                                .getSourceLinksList().getCount();
                        localObject = localGraphicViewerTreeNetworkNode;
                    }
                    break;
                case 2 :
                    if (localGraphicViewerTreeNetworkNode
                            .getDestinationLinksList().getCount() < i) {
                        i = localGraphicViewerTreeNetworkNode
                                .getDestinationLinksList().getCount();
                        localObject = localGraphicViewerTreeNetworkNode;
                    }
                    break;
                default :
                    return;
            }
        }
        if (localObject != null) {
            getRoots().addObjectAtTail(localObject.getGraphicViewerObject());
        }
    }

    protected void WalkTree(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        GraphicViewerNodeArrayList localGraphicViewerNodeArrayList;
        int j;
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2;
        switch (getPath()) {
            case 1 :
                if (paramGraphicViewerTreeNetworkNode.getDestinationLinksList()
                        .getCount() > 0) {
                    localGraphicViewerNodeArrayList = new GraphicViewerNodeArrayList();
                    for (j = 0; j < paramGraphicViewerTreeNetworkNode
                            .getDestinations().getCount(); j++) {
                        localGraphicViewerTreeNetworkNode2 = paramGraphicViewerTreeNetworkNode
                                .getDestinations().get(j);
                        if (WalkOK(paramGraphicViewerTreeNetworkNode,
                                localGraphicViewerTreeNetworkNode2)) {
                            localGraphicViewerNodeArrayList
                                    .Add(localGraphicViewerTreeNetworkNode2);
                        }
                    }
                    if (localGraphicViewerNodeArrayList.getCount() > 0) {
                        paramGraphicViewerTreeNetworkNode
                                .setChildren(localGraphicViewerNodeArrayList
                                        .ToArray());
                    }
                }
                break;
            case 2 :
                if (paramGraphicViewerTreeNetworkNode.getSourceLinksList()
                        .getCount() > 0) {
                    localGraphicViewerNodeArrayList = new GraphicViewerNodeArrayList();
                    for (j = 0; j < paramGraphicViewerTreeNetworkNode
                            .getSources().getCount(); j++) {
                        localGraphicViewerTreeNetworkNode2 = paramGraphicViewerTreeNetworkNode
                                .getSources().get(j);
                        if (WalkOK(paramGraphicViewerTreeNetworkNode,
                                localGraphicViewerTreeNetworkNode2)) {
                            localGraphicViewerNodeArrayList
                                    .Add(localGraphicViewerTreeNetworkNode2);
                        }
                    }
                    if (localGraphicViewerNodeArrayList.getCount() > 0) {
                        paramGraphicViewerTreeNetworkNode
                                .setChildren(localGraphicViewerNodeArrayList
                                        .ToArray());
                    }
                }
                break;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1;
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode.getChildren().length; i++) {
            localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i];
            localGraphicViewerTreeNetworkNode1.setInitialized(true);
            localGraphicViewerTreeNetworkNode1
                    .setLevel(paramGraphicViewerTreeNetworkNode.getLevel() + 1);
            localGraphicViewerTreeNetworkNode1
                    .setParent(paramGraphicViewerTreeNetworkNode);
            getRoots()
                    .removeObject(
                            localGraphicViewerTreeNetworkNode1
                                    .getGraphicViewerObject());
        }
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode.getChildren().length; i++) {
            localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i];
            WalkTree(localGraphicViewerTreeNetworkNode1);
        }
    }

    private boolean WalkOK(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode1,
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode2) {
        if (paramGraphicViewerTreeNetworkNode2.getInitialized()) {
            if (IsAncestor(paramGraphicViewerTreeNetworkNode2,
                    paramGraphicViewerTreeNetworkNode1)) {
                return false;
            }
            if (paramGraphicViewerTreeNetworkNode2.getLevel() > paramGraphicViewerTreeNetworkNode1
                    .getLevel()) {
                return false;
            }
            RemoveChild(paramGraphicViewerTreeNetworkNode2.getParent(),
                    paramGraphicViewerTreeNetworkNode2);
            return true;
        }
        return true;
    }

    private boolean IsAncestor(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode1,
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode2) {
        if (paramGraphicViewerTreeNetworkNode2 == null) {
            return false;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode2
                .getParent();
        if (localGraphicViewerTreeNetworkNode == paramGraphicViewerTreeNetworkNode1) {
            return true;
        }
        return IsAncestor(paramGraphicViewerTreeNetworkNode1,
                localGraphicViewerTreeNetworkNode);
    }

    private void RemoveChild(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode1,
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode2) {
        if (paramGraphicViewerTreeNetworkNode1 == null) {
            return;
        }
        if (paramGraphicViewerTreeNetworkNode2 == null) {
            return;
        }
        GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkNode1
                .getChildren();
        int i = 0;
        for (int j = 0; j < arrayOfGraphicViewerTreeNetworkNode1.length; j++) {
            if (arrayOfGraphicViewerTreeNetworkNode1[j] == paramGraphicViewerTreeNetworkNode2) {
                i++;
            }
        }
        if (i > 0) {
            GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode2 = new GraphicViewerTreeNetworkNode[arrayOfGraphicViewerTreeNetworkNode1.length
                    - i];
            int k = 0;
            for (int m = 0; m < arrayOfGraphicViewerTreeNetworkNode1.length; m++) {
                if (arrayOfGraphicViewerTreeNetworkNode1[m] != paramGraphicViewerTreeNetworkNode2) {
                    arrayOfGraphicViewerTreeNetworkNode2[(k++)] = arrayOfGraphicViewerTreeNetworkNode1[m];
                }
            }
            paramGraphicViewerTreeNetworkNode1
                    .setChildren(arrayOfGraphicViewerTreeNetworkNode2);
        }
    }

    public int getStyle() {
        return myStyle;
    }

    public void setStyle(int paramInt) {
        myStyle = paramInt;
    }

    private void InitializeAll() {
        for (GraphicViewerListPosition localGraphicViewerListPosition = getRoots()
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = getRoots()
                .getNextObjectPos(localGraphicViewerListPosition)) {
            GraphicViewerObject localGraphicViewerObject = getRoots()
                    .getObjectAtPos(localGraphicViewerListPosition);
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = getTreeNetwork()
                    .FindNode(localGraphicViewerObject);
            InitializeTree(localGraphicViewerTreeNetworkNode);
        }
    }

    private void InitializeTree(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        InitializeTreeNodeValues(paramGraphicViewerTreeNetworkNode);
        int i = 0;
        int j = paramGraphicViewerTreeNetworkNode.getChildrenCount();
        int k = 0;
        for (int m = 0; m < paramGraphicViewerTreeNetworkNode
                .getChildrenCount(); m++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                    .getChildren()[m];
            InitializeTree(localGraphicViewerTreeNetworkNode);
            i += localGraphicViewerTreeNetworkNode.getDescendentCount() + 1;
            j = Math.max(j,
                    localGraphicViewerTreeNetworkNode.getMaxChildrenCount());
            k = Math.max(k,
                    localGraphicViewerTreeNetworkNode.getMaxGenerationCount());
        }
        paramGraphicViewerTreeNetworkNode.setDescendentCount(i);
        paramGraphicViewerTreeNetworkNode.setMaxChildrenCount(j);
        paramGraphicViewerTreeNetworkNode.setMaxGenerationCount(j > 0
                ? k + 1
                : 0);
    }

    private GraphicViewerTreeNetworkNode Mom(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        switch (getStyle()) {
            case 1 :
            default :
                if (paramGraphicViewerTreeNetworkNode.getParent() != null) {
                    return paramGraphicViewerTreeNetworkNode.getParent();
                }
                return getRootDefaults();
            case 3 :
                if (paramGraphicViewerTreeNetworkNode.getParent() != null) {
                    if (paramGraphicViewerTreeNetworkNode.getParent()
                            .getParent() != null) {
                        return paramGraphicViewerTreeNetworkNode.getParent()
                                .getParent();
                    }
                    return getAlternateDefaults();
                }
                return getRootDefaults();
            case 2 :
        }
        int i = 1;
        if (paramGraphicViewerTreeNetworkNode.getChildrenCount() == 0) {
            i = 0;
        } else {
            for (int j = 0; j < paramGraphicViewerTreeNetworkNode
                    .getChildrenCount(); j++) {
                GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                        .getChildren()[j];
                if (localGraphicViewerTreeNetworkNode.getChildrenCount() > 0) {
                    i = 0;
                    break;
                }
            }
        }
        if ((i != 0) && (paramGraphicViewerTreeNetworkNode.getParent() != null)) {
            return getAlternateDefaults();
        }
        if (paramGraphicViewerTreeNetworkNode.getParent() != null) {
            return paramGraphicViewerTreeNetworkNode.getParent();
        }
        return getRootDefaults();
    }

    protected void InitializeTreeNodeValues(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = Mom(paramGraphicViewerTreeNetworkNode);
        paramGraphicViewerTreeNetworkNode
                .CopyInheritedPropertiesFrom(localGraphicViewerTreeNetworkNode);
        paramGraphicViewerTreeNetworkNode.setInitialized(true);
    }

    private void AssignAll() {
        for (GraphicViewerListPosition localGraphicViewerListPosition = getRoots()
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = getRoots()
                .getNextObjectPos(localGraphicViewerListPosition)) {
            GraphicViewerObject localGraphicViewerObject = getRoots()
                    .getObjectAtPos(localGraphicViewerListPosition);
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = getTreeNetwork()
                    .FindNode(localGraphicViewerObject);
            AssignTree(localGraphicViewerTreeNetworkNode);
        }
    }

    private void AssignTree(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        AssignTreeNodeValues(paramGraphicViewerTreeNetworkNode);
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode.getChildren().length; i++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i];
            AssignTree(localGraphicViewerTreeNetworkNode);
        }
    }

    protected void AssignTreeNodeValues(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
    }

    public void SortAll() {
        for (GraphicViewerListPosition localGraphicViewerListPosition = getRoots()
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = getRoots()
                .getNextObjectPos(localGraphicViewerListPosition)) {
            GraphicViewerObject localGraphicViewerObject = getRoots()
                    .getObjectAtPos(localGraphicViewerListPosition);
            SortTree(getTreeNetwork().FindNode(localGraphicViewerObject));
        }
    }

    public void SortTree(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        SortTreeNodeChildren(paramGraphicViewerTreeNetworkNode);
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode.getChildren().length; i++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i];
            SortTree(localGraphicViewerTreeNetworkNode);
        }
    }

    protected void SortTreeNodeChildren(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        switch (paramGraphicViewerTreeNetworkNode.getSorting()) {
            case 1 :
                break;
            case 2 :
                paramGraphicViewerTreeNetworkNode
                        .setChildren(Reverse(paramGraphicViewerTreeNetworkNode
                                .getChildren()));
                break;
            case 3 :
                Arrays.sort(paramGraphicViewerTreeNetworkNode.getChildren(),
                        getAlphabeticNodeTextComparer());
                break;
            case 4 :
                Arrays.sort(paramGraphicViewerTreeNetworkNode.getChildren(),
                        getAlphabeticNodeTextComparer());
                paramGraphicViewerTreeNetworkNode
                        .setChildren(Reverse(paramGraphicViewerTreeNetworkNode
                                .getChildren()));
                break;
        }
    }

    private GraphicViewerTreeNetworkNode[] Reverse(
            GraphicViewerTreeNetworkNode[] paramArrayOfGraphicViewerTreeNetworkNode) {
        GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode = new GraphicViewerTreeNetworkNode[paramArrayOfGraphicViewerTreeNetworkNode.length];
        for (int i = 0; i < paramArrayOfGraphicViewerTreeNetworkNode.length; i++) {
            arrayOfGraphicViewerTreeNetworkNode[(paramArrayOfGraphicViewerTreeNetworkNode.length - 1 - i)] = paramArrayOfGraphicViewerTreeNetworkNode[i];
        }
        return arrayOfGraphicViewerTreeNetworkNode;
    }

    private void LayoutAll() {
        for (GraphicViewerListPosition localGraphicViewerListPosition = getRoots()
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = getRoots()
                .getNextObjectPos(localGraphicViewerListPosition)) {
            GraphicViewerObject localGraphicViewerObject = getRoots()
                    .getObjectAtPos(localGraphicViewerListPosition);
            LayoutTree(getTreeNetwork().FindNode(localGraphicViewerObject));
        }
    }

    private void LayoutTree(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        switch (getCompaction()) {
            case 1 :
                LayoutTreeNone(paramGraphicViewerTreeNetworkNode);
                break;
            case 2 :
                LayoutTreeBlock(paramGraphicViewerTreeNetworkNode);
                break;
        }
    }

    private void LayoutTreeNone(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        if (paramGraphicViewerTreeNetworkNode.getChildrenCount() == 0) {
            paramGraphicViewerTreeNetworkNode.setRelativePosition(new Point(0,
                    0));
            paramGraphicViewerTreeNetworkNode
                    .setSubtreeSize(paramGraphicViewerTreeNetworkNode.getSize());
            paramGraphicViewerTreeNetworkNode.setSubtreeOffset(new Dimension(0,
                    0));
            return;
        }
        float f = OrthoAngle(paramGraphicViewerTreeNetworkNode);
        int i = (f == 90.0F) || (f == 270.0F) ? 1 : 0;
        int j = 0;
        for (int k = 0; k < paramGraphicViewerTreeNetworkNode.getChildren().length; k++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkNode
                    .getChildren()[k];
            LayoutTree(localGraphicViewerTreeNetworkNode1);
            j = Math.max(
                    j,
                    i != 0
                            ? localGraphicViewerTreeNetworkNode1
                                    .getSubtreeSize().width
                            : localGraphicViewerTreeNetworkNode1
                                    .getSubtreeSize().height);
        }
        int k = paramGraphicViewerTreeNetworkNode.getAlignment();
        int m = k == 3 ? 1 : 0;
        int n = k == 4 ? 1 : 0;
        int i1 = Math.max(0,
                paramGraphicViewerTreeNetworkNode.getBreadthLimit());
        int i2 = paramGraphicViewerTreeNetworkNode.getLayerSpacing();
        if (i2 < (i != 0
                ? -paramGraphicViewerTreeNetworkNode.getHeight()
                : -paramGraphicViewerTreeNetworkNode.getWidth())) {
            i2 = i != 0
                    ? -paramGraphicViewerTreeNetworkNode.getHeight()
                    : -paramGraphicViewerTreeNetworkNode.getWidth();
        }
        int i3 = paramGraphicViewerTreeNetworkNode.getNodeSpacing();
        int i4 = Math.max(0, paramGraphicViewerTreeNetworkNode.getNodeIndent());
        int i5 = n != 0 ? 0 : m != 0 ? i4 : i4 / 2;
        int i6 = paramGraphicViewerTreeNetworkNode.getRowSpacing();
        int i7 = 0;
        if ((m != 0)
                || (n != 0)
                || (paramGraphicViewerTreeNetworkNode.getRouteAroundCentered())
                || ((paramGraphicViewerTreeNetworkNode
                        .getRouteAroundLastParent()) && (paramGraphicViewerTreeNetworkNode
                        .getMaxGenerationCount() == 1))) {
            i7 = Math.max(0, paramGraphicViewerTreeNetworkNode.getRowIndent());
        }
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                .getChildren();
        int i18;
        for (int i16 = 0; i16 < arrayOfGraphicViewerTreeNetworkNode.length; i16++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = arrayOfGraphicViewerTreeNetworkNode[i16];
            if (i != 0) {
                if ((i1 > 0)
                        && (i14 > 0)
                        && (i11
                                + i3
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().width > i1)) {
                    if (i11 < j) {
                        ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                                new Dimension(j - i11, 0), i15, i16 - 1);
                    }
                    i13++;
                    i14 = 0;
                    i15 = i16;
                    i10 = i9;
                    i11 = 0;
                    i12 = f > 135.0F ? -i9 - i6 : i9 + i6;
                }
                i18 = i14 == 0 ? i5 : i3;
                RecordMidPoints(localGraphicViewerTreeNetworkNode2, new Point(
                        0, i12));
                localGraphicViewerTreeNetworkNode2
                        .setRelativePosition(new Point(i11 + i18, i12));
                i8 = Math.max(
                        i8,
                        i11
                                + i18
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().width);
                i9 = Math.max(
                        i9,
                        i10
                                + (i13 == 0 ? 0 : i6)
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().height);
                i11 += i18
                        + localGraphicViewerTreeNetworkNode2.getSubtreeSize().width;
            } else {
                if ((i1 > 0)
                        && (i14 > 0)
                        && (i12
                                + i3
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().height > i1)) {
                    if (i12 < j) {
                        ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                                new Dimension(0, j - i12), i15, i16 - 1);
                    }
                    i13++;
                    i14 = 0;
                    i15 = i16;
                    i10 = i8;
                    i12 = 0;
                    i11 = f > 135.0F ? -i8 - i6 : i8 + i6;
                }
                i18 = i14 == 0 ? i5 : i3;
                RecordMidPoints(localGraphicViewerTreeNetworkNode2, new Point(
                        i11, 0));
                localGraphicViewerTreeNetworkNode2
                        .setRelativePosition(new Point(i11, i12 + i18));
                i9 = Math.max(
                        i9,
                        i12
                                + i18
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().height);
                i8 = Math.max(
                        i8,
                        i10
                                + (i13 == 0 ? 0 : i6)
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().width);
                i12 += i18
                        + localGraphicViewerTreeNetworkNode2.getSubtreeSize().height;
            }
            i14++;
        }
        if (i13 > 0) {
            if (i != 0) {
                i9 += Math.max(0, i2);
                if (i11 < i8) {
                    ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                            new Dimension(i8 - i11, 0), i15,
                            arrayOfGraphicViewerTreeNetworkNode.length - 1);
                }
                if (i7 > 0) {
                    if (n == 0) {
                        ShiftRelPos(paramGraphicViewerTreeNetworkNode,
                                new Dimension(i7, 0), 0,
                                arrayOfGraphicViewerTreeNetworkNode.length - 1);
                    }
                    i8 += i7;
                }
            } else {
                i8 += Math.max(0, i2);
                if (i12 < i9) {
                    ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                            new Dimension(0, i9 - i12), i15,
                            arrayOfGraphicViewerTreeNetworkNode.length - 1);
                }
                if (i7 > 0) {
                    if (n == 0) {
                        ShiftRelPos(paramGraphicViewerTreeNetworkNode,
                                new Dimension(0, i7), 0,
                                arrayOfGraphicViewerTreeNetworkNode.length - 1);
                    }
                    i9 += i7;
                }
            }
        }
        Dimension localDimension = new Dimension(0, 0);
        switch (k) {
            case 1 :
                if (i != 0) {
                    localDimension.width += (i8 - paramGraphicViewerTreeNetworkNode
                            .getWidth()) / 2;
                } else {
                    localDimension.height += (i9 - paramGraphicViewerTreeNetworkNode
                            .getHeight()) / 2;
                }
                break;
            case 2 :
                if (i13 > 0) {
                    if (i != 0) {
                        localDimension.width += (i8 - paramGraphicViewerTreeNetworkNode
                                .getWidth()) / 2;
                    } else {
                        localDimension.height += (i9 - paramGraphicViewerTreeNetworkNode
                                .getHeight()) / 2;
                    }
                } else {
                    int i17 = paramGraphicViewerTreeNetworkNode
                            .getChildrenCount();
                    int i19;
                    if (i != 0) {
                        i18 = paramGraphicViewerTreeNetworkNode.getChildren()[0]
                                .getSubtreeOffset().width;
                        i19 = paramGraphicViewerTreeNetworkNode.getChildren()[(i17 - 1)]
                                .getRelativePosition().x
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i17 - 1)]
                                        .getSubtreeOffset().width
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i17 - 1)].getWidth();
                        localDimension.width += i18
                                + (i19 - i18 - paramGraphicViewerTreeNetworkNode
                                        .getWidth()) / 2;
                    } else {
                        i18 = paramGraphicViewerTreeNetworkNode.getChildren()[0]
                                .getSubtreeOffset().height;
                        i19 = paramGraphicViewerTreeNetworkNode.getChildren()[(i17 - 1)]
                                .getRelativePosition().y
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i17 - 1)]
                                        .getSubtreeOffset().height
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i17 - 1)].getHeight();
                        localDimension.height += i18
                                + (i19 - i18 - paramGraphicViewerTreeNetworkNode
                                        .getHeight()) / 2;
                    }
                }
                break;
            case 3 :
                break;
            case 4 :
                if (i != 0) {
                    i8 += i4;
                    localDimension.width += i8
                            - paramGraphicViewerTreeNetworkNode.getWidth();
                } else {
                    i9 += i4;
                    localDimension.height += i9
                            - paramGraphicViewerTreeNetworkNode.getHeight();
                }
                break;
        }
        for (int i17 = 0; i17 < paramGraphicViewerTreeNetworkNode.getChildren().length; i17++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode3 = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i17];
            if (i != 0) {
                localGraphicViewerTreeNetworkNode3
                        .setRelativePosition(new Point(
                                localGraphicViewerTreeNetworkNode3
                                        .getRelativePosition().x
                                        + localGraphicViewerTreeNetworkNode3
                                                .getSubtreeOffset().width
                                        - localDimension.width,
                                localGraphicViewerTreeNetworkNode3
                                        .getRelativePosition().y
                                        + (f > 135.0F
                                                ? -localGraphicViewerTreeNetworkNode3
                                                        .getSubtreeSize().height
                                                        + localGraphicViewerTreeNetworkNode3
                                                                .getSubtreeOffset().height
                                                        - i2
                                                : paramGraphicViewerTreeNetworkNode
                                                        .getHeight()
                                                        + i2
                                                        + localGraphicViewerTreeNetworkNode3
                                                                .getSubtreeOffset().height)));
            } else {
                localGraphicViewerTreeNetworkNode3
                        .setRelativePosition(new Point(
                                localGraphicViewerTreeNetworkNode3
                                        .getRelativePosition().x
                                        + (f > 135.0F
                                                ? -localGraphicViewerTreeNetworkNode3
                                                        .getSubtreeSize().width
                                                        + localGraphicViewerTreeNetworkNode3
                                                                .getSubtreeOffset().width
                                                        - i2
                                                : paramGraphicViewerTreeNetworkNode
                                                        .getWidth()
                                                        + i2
                                                        + localGraphicViewerTreeNetworkNode3
                                                                .getSubtreeOffset().width),
                                localGraphicViewerTreeNetworkNode3
                                        .getRelativePosition().y
                                        + localGraphicViewerTreeNetworkNode3
                                                .getSubtreeOffset().height
                                        - localDimension.height));
            }
        }
        if (i != 0) {
            if (paramGraphicViewerTreeNetworkNode.getWidth() > i8) {
                i8 = paramGraphicViewerTreeNetworkNode.getWidth();
                localDimension.width = 0;
            }
            if (f > 135.0F) {
                localDimension.height += i9 + i2;
            }
            i9 = Math
                    .max(Math.max(i9,
                            paramGraphicViewerTreeNetworkNode.getHeight()), i9
                            + paramGraphicViewerTreeNetworkNode.getHeight()
                            + i2);
        } else {
            if (f > 135.0F) {
                localDimension.width += i8 + i2;
            }
            i8 = Math.max(
                    Math.max(i8, paramGraphicViewerTreeNetworkNode.getWidth()),
                    i8 + paramGraphicViewerTreeNetworkNode.getWidth() + i2);
            if (paramGraphicViewerTreeNetworkNode.getHeight() > i9) {
                i9 = paramGraphicViewerTreeNetworkNode.getHeight();
                localDimension.height = 0;
            }
        }
        paramGraphicViewerTreeNetworkNode.setSubtreeOffset(localDimension);
        paramGraphicViewerTreeNetworkNode.setSubtreeSize(new Dimension(i8, i9));
    }

    private static Dimension AlignOffset(int paramInt, Dimension paramDimension) {
        switch (paramInt) {
            case 1 :
                paramDimension.width /= 2;
                paramDimension.height /= 2;
                break;
            case 2 :
                paramDimension.width /= 2;
                paramDimension.height /= 2;
                break;
            case 3 :
                paramDimension.width = 0;
                paramDimension.height = 0;
                break;
            case 4 :
                break;
        }
        return paramDimension;
    }

    private void ShiftRelPosAlign(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode,
            int paramInt1, Dimension paramDimension, int paramInt2,
            int paramInt3) {
        Dimension localDimension = AlignOffset(paramInt1, paramDimension);
        ShiftRelPos(paramGraphicViewerTreeNetworkNode, localDimension,
                paramInt2, paramInt3);
    }

    private void ShiftRelPos(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode,
            Dimension paramDimension, int paramInt1, int paramInt2) {
        if ((paramDimension.width == 0) && (paramDimension.height == 0)) {
            return;
        }
        GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                .getChildren();
        for (int i = paramInt1; i <= paramInt2; i++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = arrayOfGraphicViewerTreeNetworkNode[i];
            Point localPoint = localGraphicViewerTreeNetworkNode
                    .getRelativePosition();
            localPoint.x += paramDimension.width;
            localPoint.y += paramDimension.height;
            localGraphicViewerTreeNetworkNode.setRelativePosition(localPoint);
        }
    }

    private void RecordMidPoints(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode,
            Point paramPoint) {
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                .getParent();
        int i;
        GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink;
        switch (getPath()) {
            case 1 :
                for (i = 0; i < paramGraphicViewerTreeNetworkNode
                        .getSourceLinksList().getCount(); i++) {
                    localGraphicViewerTreeNetworkLink = paramGraphicViewerTreeNetworkNode
                            .getSourceLinksList().get(i);
                    if (localGraphicViewerTreeNetworkLink.getFromNode() == localGraphicViewerTreeNetworkNode) {
                        localGraphicViewerTreeNetworkLink
                                .setRelativePoint(paramPoint);
                    }
                }
                break;
            case 2 :
                for (i = 0; i < paramGraphicViewerTreeNetworkNode
                        .getDestinationLinksList().getCount(); i++) {
                    localGraphicViewerTreeNetworkLink = paramGraphicViewerTreeNetworkNode
                            .getDestinationLinksList().get(i);
                    if (localGraphicViewerTreeNetworkLink.getToNode() == localGraphicViewerTreeNetworkNode) {
                        localGraphicViewerTreeNetworkLink
                                .setRelativePoint(paramPoint);
                    }
                }
        }
    }

    private void LayoutTreeBlock(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        if (paramGraphicViewerTreeNetworkNode.getChildrenCount() == 0) {
            paramGraphicViewerTreeNetworkNode.setRelativePosition(new Point(0,
                    0));
            paramGraphicViewerTreeNetworkNode
                    .setSubtreeSize(paramGraphicViewerTreeNetworkNode.getSize());
            paramGraphicViewerTreeNetworkNode.setSubtreeOffset(new Dimension(0,
                    0));
            paramGraphicViewerTreeNetworkNode.LeftFringe = null;
            paramGraphicViewerTreeNetworkNode.RightFringe = null;
            return;
        }
        float f = OrthoAngle(paramGraphicViewerTreeNetworkNode);
        int i = (f == 90.0F) || (f == 270.0F) ? 1 : 0;
        int j = 0;
        for (int k = 0; k < paramGraphicViewerTreeNetworkNode
                .getChildrenCount(); k++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = paramGraphicViewerTreeNetworkNode
                    .getChildren()[k];
            LayoutTree(localGraphicViewerTreeNetworkNode1);
            j = Math.max(
                    j,
                    i != 0
                            ? localGraphicViewerTreeNetworkNode1
                                    .getSubtreeSize().width
                            : localGraphicViewerTreeNetworkNode1
                                    .getSubtreeSize().height);
        }
        int k = paramGraphicViewerTreeNetworkNode.getAlignment();
        int m = k == 3 ? 1 : 0;
        int n = k == 4 ? 1 : 0;
        int i1 = Math.max(0,
                paramGraphicViewerTreeNetworkNode.getBreadthLimit());
        int i2 = paramGraphicViewerTreeNetworkNode.getLayerSpacing();
        if (i2 < (i != 0
                ? -paramGraphicViewerTreeNetworkNode.getHeight()
                : -paramGraphicViewerTreeNetworkNode.getWidth())) {
            i2 = i != 0
                    ? -paramGraphicViewerTreeNetworkNode.getHeight()
                    : -paramGraphicViewerTreeNetworkNode.getWidth();
        }
        int i3 = paramGraphicViewerTreeNetworkNode.getNodeSpacing();
        int i4 = Math.max(0, paramGraphicViewerTreeNetworkNode.getNodeIndent());
        int i5 = paramGraphicViewerTreeNetworkNode.getRowSpacing();
        int i6 = 0;
        if ((m != 0)
                || (n != 0)
                || (paramGraphicViewerTreeNetworkNode.getRouteAroundCentered())
                || ((paramGraphicViewerTreeNetworkNode
                        .getRouteAroundLastParent()) && (paramGraphicViewerTreeNetworkNode
                        .getMaxGenerationCount() == 1))) {
            i6 = Math.max(0, paramGraphicViewerTreeNetworkNode.getRowIndent());
        }
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        Point[] arrayOfPoint1 = null;
        Point[] arrayOfPoint2 = null;
        Dimension localDimension1 = new Dimension(0, 0);
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                .getChildren();
        int i17;
        Object localObject2;
        for (int i15 = 0; i15 < arrayOfGraphicViewerTreeNetworkNode.length; i15++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = arrayOfGraphicViewerTreeNetworkNode[i15];
            FringeRef localFringeRef1;
            FringeRef localFringeRef2;
            if (i != 0) {
                if ((i1 > 0)
                        && (i13 > 0)
                        && (i10
                                + i3
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().width > i1)) {
                    if (i10 < j) {
                        ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                                new Dimension(j - i10, 0), i14, i15 - 1);
                    }
                    i12++;
                    i13 = 0;
                    i14 = i15;
                    i9 = i8;
                    i10 = 0;
                    i11 = f > 135.0F ? -i8 - i5 : i8 + i5;
                }
                RecordMidPoints(localGraphicViewerTreeNetworkNode2, new Point(
                        0, i11));
                i17 = 0;
                if (i13 == 0) {
                    arrayOfPoint1 = localGraphicViewerTreeNetworkNode2.LeftFringe;
                    arrayOfPoint2 = localGraphicViewerTreeNetworkNode2.RightFringe;
                    localDimension1 = localGraphicViewerTreeNetworkNode2
                            .getSubtreeSize();
                    if ((arrayOfPoint1 == null)
                            || (arrayOfPoint2 == null)
                            || (f != OrthoAngle(localGraphicViewerTreeNetworkNode2))) {
                        arrayOfPoint1 = AllocTempPointArray(2);
                        arrayOfPoint2 = AllocTempPointArray(2);
                        arrayOfPoint1[0] = new Point(0, 0);
                        arrayOfPoint1[1] = new Point(0, localDimension1.height);
                        arrayOfPoint2[0] = new Point(localDimension1.width, 0);
                        arrayOfPoint2[1] = new Point(localDimension1.width,
                                localDimension1.height);
                    }
                } else {
                    localFringeRef1 = new FringeRef(arrayOfPoint1);
                    localFringeRef2 = new FringeRef(arrayOfPoint2);
                    localObject2 = new SizeRef(localDimension1);
                    i17 = MergeFringes(paramGraphicViewerTreeNetworkNode,
                            localGraphicViewerTreeNetworkNode2,
                            localFringeRef1, localFringeRef2,
                            (SizeRef) localObject2);
                    localDimension1 = ((SizeRef) localObject2).getSize();
                    arrayOfPoint1 = localFringeRef1.getFringe();
                    arrayOfPoint2 = localFringeRef2.getFringe();
                    if ((i10 < localGraphicViewerTreeNetworkNode2
                            .getSubtreeSize().width) && (i17 < 0)) {
                        ShiftRelPos(paramGraphicViewerTreeNetworkNode,
                                new Dimension(-i17, 0), i14, i15 - 1);
                        ShiftFringe(arrayOfPoint1, new Dimension(-i17, 0));
                        ShiftFringe(arrayOfPoint2, new Dimension(-i17, 0));
                        i17 = 0;
                    }
                }
                localGraphicViewerTreeNetworkNode2
                        .setRelativePosition(new Point(i17, i11));
                i7 = Math.max(i7, localDimension1.width);
                i8 = Math.max(
                        i8,
                        i9
                                + (i12 == 0 ? 0 : i5)
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().height);
                i10 = localDimension1.width;
            } else {
                if ((i1 > 0)
                        && (i13 > 0)
                        && (i11
                                + i3
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().height > i1)) {
                    if (i11 < j) {
                        ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                                new Dimension(0, j - i11), i14, i15 - 1);
                    }
                    i12++;
                    i13 = 0;
                    i14 = i15;
                    i9 = i7;
                    i11 = 0;
                    i10 = f > 135.0F ? -i7 - i5 : i7 + i5;
                }
                RecordMidPoints(localGraphicViewerTreeNetworkNode2, new Point(
                        i10, 0));
                i17 = 0;
                if (i13 == 0) {
                    arrayOfPoint1 = localGraphicViewerTreeNetworkNode2.LeftFringe;
                    arrayOfPoint2 = localGraphicViewerTreeNetworkNode2.RightFringe;
                    localDimension1 = localGraphicViewerTreeNetworkNode2
                            .getSubtreeSize();
                    if ((arrayOfPoint1 == null)
                            || (arrayOfPoint2 == null)
                            || (f != OrthoAngle(localGraphicViewerTreeNetworkNode2))) {
                        arrayOfPoint1 = AllocTempPointArray(2);
                        arrayOfPoint2 = AllocTempPointArray(2);
                        arrayOfPoint1[0] = new Point(0, 0);
                        arrayOfPoint1[1] = new Point(localDimension1.width, 0);
                        arrayOfPoint2[0] = new Point(0, localDimension1.height);
                        arrayOfPoint2[1] = new Point(localDimension1.width,
                                localDimension1.height);
                    }
                } else {
                    localFringeRef1 = new FringeRef(arrayOfPoint1);
                    localFringeRef2 = new FringeRef(arrayOfPoint2);
                    localObject2 = new SizeRef(localDimension1);
                    i17 = MergeFringes(paramGraphicViewerTreeNetworkNode,
                            localGraphicViewerTreeNetworkNode2,
                            localFringeRef1, localFringeRef2,
                            (SizeRef) localObject2);
                    arrayOfPoint1 = localFringeRef1.getFringe();
                    arrayOfPoint2 = localFringeRef2.getFringe();
                    localDimension1 = ((SizeRef) localObject2).getSize();
                    if ((i11 < localGraphicViewerTreeNetworkNode2
                            .getSubtreeSize().height) && (i17 < 0)) {
                        ShiftRelPos(paramGraphicViewerTreeNetworkNode,
                                new Dimension(0, -i17), i14, i15 - 1);
                        ShiftFringe(arrayOfPoint1, new Dimension(0, -i17));
                        ShiftFringe(arrayOfPoint2, new Dimension(0, -i17));
                        i17 = 0;
                    }
                }
                localGraphicViewerTreeNetworkNode2
                        .setRelativePosition(new Point(i10, i17));
                i8 = Math.max(i8, localDimension1.height);
                i7 = Math.max(
                        i7,
                        i9
                                + (i12 == 0 ? 0 : i5)
                                + localGraphicViewerTreeNetworkNode2
                                        .getSubtreeSize().width);
                i11 = localDimension1.height;
            }
            i13++;
        }
        if (i12 > 0) {
            if (i != 0) {
                i8 += Math.max(0, i2);
                if (i10 < i7) {
                    ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                            new Dimension(i7 - i10, 0), i14,
                            arrayOfGraphicViewerTreeNetworkNode.length - 1);
                }
                if (i6 > 0) {
                    if (n == 0) {
                        ShiftRelPos(paramGraphicViewerTreeNetworkNode,
                                new Dimension(i6, 0), 0,
                                arrayOfGraphicViewerTreeNetworkNode.length - 1);
                    }
                    i7 += i6;
                }
            } else {
                i7 += Math.max(0, i2);
                if (i11 < i8) {
                    ShiftRelPosAlign(paramGraphicViewerTreeNetworkNode, k,
                            new Dimension(0, i8 - i11), i14,
                            arrayOfGraphicViewerTreeNetworkNode.length - 1);
                }
                if (i6 > 0) {
                    if (n == 0) {
                        ShiftRelPos(paramGraphicViewerTreeNetworkNode,
                                new Dimension(0, i6), 0,
                                arrayOfGraphicViewerTreeNetworkNode.length - 1);
                    }
                    i8 += i6;
                }
            }
        }
        Dimension localDimension2 = new Dimension(0, 0);
        switch (k) {
            case 1 :
                if (i != 0) {
                    localDimension2.width += (i7
                            - paramGraphicViewerTreeNetworkNode.getWidth() - i4) / 2;
                } else {
                    localDimension2.height += (i8
                            - paramGraphicViewerTreeNetworkNode.getHeight() - i4) / 2;
                }
                break;
            case 2 :
                if (i12 > 0) {
                    if (i != 0) {
                        localDimension2.width += (i7
                                - paramGraphicViewerTreeNetworkNode.getWidth() - i4) / 2;
                    } else {
                        localDimension2.height += (i8
                                - paramGraphicViewerTreeNetworkNode.getHeight() - i4) / 2;
                    }
                } else {
                    int i16 = paramGraphicViewerTreeNetworkNode
                            .getChildrenCount();
                    int i18;
                    if (i != 0) {
                        i17 = paramGraphicViewerTreeNetworkNode.getChildren()[0]
                                .getRelativePosition().x
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[0].getSubtreeOffset().width;
                        i18 = paramGraphicViewerTreeNetworkNode.getChildren()[(i16 - 1)]
                                .getRelativePosition().x
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i16 - 1)]
                                        .getSubtreeOffset().width
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i16 - 1)].getWidth();
                        localDimension2.width += i17
                                + (i18
                                        - i17
                                        - paramGraphicViewerTreeNetworkNode
                                                .getWidth() - i4) / 2;
                    } else {
                        i17 = paramGraphicViewerTreeNetworkNode.getChildren()[0]
                                .getRelativePosition().y
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[0].getSubtreeOffset().height;
                        i18 = paramGraphicViewerTreeNetworkNode.getChildren()[(i16 - 1)]
                                .getRelativePosition().y
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i16 - 1)]
                                        .getSubtreeOffset().height
                                + paramGraphicViewerTreeNetworkNode
                                        .getChildren()[(i16 - 1)].getHeight();
                        localDimension2.height += i17
                                + (i18
                                        - i17
                                        - paramGraphicViewerTreeNetworkNode
                                                .getHeight() - i4) / 2;
                    }
                }
                break;
            case 3 :
                if (i != 0) {
                    localDimension2.width -= i4;
                    i7 += i4;
                } else {
                    localDimension2.height -= i4;
                    i8 += i4;
                }
                break;
            case 4 :
                if (i != 0) {
                    localDimension2.width += i7
                            - paramGraphicViewerTreeNetworkNode.getWidth() + i4;
                    i7 += i4;
                } else {
                    localDimension2.height += i8
                            - paramGraphicViewerTreeNetworkNode.getHeight()
                            + i4;
                    i8 += i4;
                }
                break;
            default :
                return;
        }
        for (int i16 = 0; i16 < paramGraphicViewerTreeNetworkNode.getChildren().length; i16++) {
            GraphicViewerTreeNetworkNode localObject1 = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i16];
            if (i != 0) {
                ((GraphicViewerTreeNetworkNode) localObject1)
                        .setRelativePosition(new Point(
                                ((GraphicViewerTreeNetworkNode) localObject1)
                                        .getRelativePosition().x
                                        + ((GraphicViewerTreeNetworkNode) localObject1)
                                                .getSubtreeOffset().width
                                        - localDimension2.width,
                                ((GraphicViewerTreeNetworkNode) localObject1)
                                        .getRelativePosition().y
                                        + (f > 135.0F
                                                ? -((GraphicViewerTreeNetworkNode) localObject1)
                                                        .getSubtreeSize().height
                                                        + ((GraphicViewerTreeNetworkNode) localObject1)
                                                                .getSubtreeOffset().height
                                                        - i2
                                                : paramGraphicViewerTreeNetworkNode
                                                        .getHeight()
                                                        + i2
                                                        + ((GraphicViewerTreeNetworkNode) localObject1)
                                                                .getSubtreeOffset().height)));
            } else {
                ((GraphicViewerTreeNetworkNode) localObject1)
                        .setRelativePosition(new Point(
                                ((GraphicViewerTreeNetworkNode) localObject1)
                                        .getRelativePosition().x
                                        + (f > 135.0F
                                                ? -((GraphicViewerTreeNetworkNode) localObject1)
                                                        .getSubtreeSize().width
                                                        + ((GraphicViewerTreeNetworkNode) localObject1)
                                                                .getSubtreeOffset().width
                                                        - i2
                                                : paramGraphicViewerTreeNetworkNode
                                                        .getWidth()
                                                        + i2
                                                        + ((GraphicViewerTreeNetworkNode) localObject1)
                                                                .getSubtreeOffset().width),
                                ((GraphicViewerTreeNetworkNode) localObject1)
                                        .getRelativePosition().y
                                        + ((GraphicViewerTreeNetworkNode) localObject1)
                                                .getSubtreeOffset().height
                                        - localDimension2.height));
            }
        }
        Dimension localDimension3 = new Dimension(0, 0);
        if (i != 0) {
            if (paramGraphicViewerTreeNetworkNode.getWidth() > i7) {
                localDimension3 = AlignOffset(k, new Dimension(
                        paramGraphicViewerTreeNetworkNode.getWidth() - i7, 0));
                i7 = paramGraphicViewerTreeNetworkNode.getWidth();
                localDimension2.width = 0;
            }
            if (localDimension2.width < 0) {
                localDimension3.width -= localDimension2.width;
                localDimension2.width = 0;
            }
            if (f > 135.0F) {
                localDimension2.height += i8 + i2;
            }
            i8 = Math
                    .max(Math.max(i8,
                            paramGraphicViewerTreeNetworkNode.getHeight()), i8
                            + paramGraphicViewerTreeNetworkNode.getHeight()
                            + i2);
            localDimension3.height += paramGraphicViewerTreeNetworkNode
                    .getHeight() + i2;
        } else {
            if (f > 135.0F) {
                localDimension2.width += i7 + i2;
            }
            i7 = Math.max(
                    Math.max(i7, paramGraphicViewerTreeNetworkNode.getWidth()),
                    i7 + paramGraphicViewerTreeNetworkNode.getWidth() + i2);
            if (paramGraphicViewerTreeNetworkNode.getHeight() > i8) {
                localDimension3 = AlignOffset(k, new Dimension(0,
                        paramGraphicViewerTreeNetworkNode.getHeight() - i8));
                i8 = paramGraphicViewerTreeNetworkNode.getHeight();
                localDimension2.height = 0;
            }
            if (localDimension2.height < 0) {
                localDimension3.height -= localDimension2.height;
                localDimension2.height = 0;
            }
            localDimension3.width += paramGraphicViewerTreeNetworkNode
                    .getWidth() + i2;
        }
        Point[] arrayOfPoint3;
        if (i12 > 0) {
            Point[] localObject1 = AllocTempPointArray(4);
            arrayOfPoint3 = AllocTempPointArray(4);
            if (i != 0) {
                localObject1[2] = new Point(0,
                        paramGraphicViewerTreeNetworkNode.getHeight() + i2);
                localObject1[3] = new Point(localObject1[2].x, i8);
                arrayOfPoint3[2] = new Point(i7, localObject1[2].y);
                arrayOfPoint3[3] = new Point(arrayOfPoint3[2].x,
                        localObject1[3].y);
            } else {
                localObject1[2] = new Point(
                        paramGraphicViewerTreeNetworkNode.getWidth() + i2, 0);
                localObject1[3] = new Point(i7, localObject1[2].y);
                arrayOfPoint3[2] = new Point(localObject1[2].x, i8);
                arrayOfPoint3[3] = new Point(localObject1[3].x,
                        arrayOfPoint3[2].y);
            }
        } else {
            Point[] localObject1 = AllocTempPointArray(arrayOfPoint1.length + 2);
            arrayOfPoint3 = AllocTempPointArray(arrayOfPoint2.length + 2);
            for (int i19 = 0; i19 < arrayOfPoint1.length; i19++) {
                localObject2 = arrayOfPoint1[i19];
                localObject1[(i19 + 2)] = new Point(((Point) localObject2).x
                        + localDimension3.width, ((Point) localObject2).y
                        + localDimension3.height);
            }
            for (int i19 = 0; i19 < arrayOfPoint2.length; i19++) {
                localObject2 = arrayOfPoint2[i19];
                arrayOfPoint3[(i19 + 2)] = new Point(((Point) localObject2).x
                        + localDimension3.width, ((Point) localObject2).y
                        + localDimension3.height);
            }
        }
        Point localPoint;
        Point[] localObject1 = new Point[4];
        if (i != 0) {
            localObject1 = new Point[4];
            localObject1[0] = new Point(localDimension2.width, 0);
            localObject1[1] = new Point(localObject1[0].x,
                    paramGraphicViewerTreeNetworkNode.getHeight());
            if ((localObject1[2].y < paramGraphicViewerTreeNetworkNode
                    .getHeight()) && (localObject1[2].x > localObject1[0].x)) {
                localObject1[2].y = paramGraphicViewerTreeNetworkNode
                        .getHeight();
            }
            if ((localObject1[3].y < paramGraphicViewerTreeNetworkNode
                    .getHeight()) && (localObject1[3].x > localObject1[0].x)) {
                localObject1[3].y = paramGraphicViewerTreeNetworkNode
                        .getHeight();
            }
            arrayOfPoint3[0] = new Point(localDimension2.width
                    + paramGraphicViewerTreeNetworkNode.getWidth(), 0);
            arrayOfPoint3[1] = new Point(arrayOfPoint3[0].x,
                    paramGraphicViewerTreeNetworkNode.getHeight());
            localPoint = arrayOfPoint3[1];
            if ((arrayOfPoint3[2].y < paramGraphicViewerTreeNetworkNode
                    .getHeight()) && (arrayOfPoint3[2].x > arrayOfPoint3[0].x)) {
                arrayOfPoint3[1] = arrayOfPoint3[2];
            }
            if ((arrayOfPoint3[3].y < paramGraphicViewerTreeNetworkNode
                    .getHeight()) && (arrayOfPoint3[3].x > arrayOfPoint3[0].x)) {
                arrayOfPoint3[2] = arrayOfPoint3[3];
                arrayOfPoint3[3] = localPoint;
            }
        } else {
            localObject1 = new Point[4];
            localObject1[0] = new Point(0, localDimension2.height);
            localObject1[1] = new Point(
                    paramGraphicViewerTreeNetworkNode.getWidth(),
                    localObject1[0].y);
            if ((localObject1[2].x < paramGraphicViewerTreeNetworkNode
                    .getWidth()) && (localObject1[2].y > localObject1[0].y)) {
                localObject1[2].x = paramGraphicViewerTreeNetworkNode
                        .getWidth();
            }
            if ((localObject1[3].x < paramGraphicViewerTreeNetworkNode
                    .getWidth()) && (localObject1[3].y > localObject1[0].y)) {
                localObject1[3].x = paramGraphicViewerTreeNetworkNode
                        .getWidth();
            }
            arrayOfPoint3[0] = new Point(0, localDimension2.height
                    + paramGraphicViewerTreeNetworkNode.getHeight());
            arrayOfPoint3[1] = new Point(
                    paramGraphicViewerTreeNetworkNode.getWidth(),
                    arrayOfPoint3[0].y);
            localPoint = arrayOfPoint3[1];
            if ((arrayOfPoint3[2].x < paramGraphicViewerTreeNetworkNode
                    .getWidth()) && (arrayOfPoint3[2].y > arrayOfPoint3[0].y)) {
                arrayOfPoint3[1] = arrayOfPoint3[2];
            }
            if ((arrayOfPoint3[3].x < paramGraphicViewerTreeNetworkNode
                    .getWidth()) && (arrayOfPoint3[3].y > arrayOfPoint3[0].y)) {
                arrayOfPoint3[2] = arrayOfPoint3[3];
                arrayOfPoint3[3] = localPoint;
            }
        }
        FreeTempPointArray(arrayOfPoint1);
        FreeTempPointArray(arrayOfPoint2);
        paramGraphicViewerTreeNetworkNode.LeftFringe = ((Point[]) localObject1);
        paramGraphicViewerTreeNetworkNode.RightFringe = arrayOfPoint3;
        paramGraphicViewerTreeNetworkNode.setSubtreeOffset(localDimension2);
        paramGraphicViewerTreeNetworkNode.setSubtreeSize(new Dimension(i7, i8));
    }

    static void ShiftFringe(Point[] paramArrayOfPoint, Dimension paramDimension) {
        for (int i = 0; i < paramArrayOfPoint.length; i++) {
            Point localPoint = paramArrayOfPoint[i];
            localPoint.x += paramDimension.width;
            localPoint.y += paramDimension.height;
            paramArrayOfPoint[i] = localPoint;
        }
    }

    private int MergeFringes(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode1,
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode2,
            FringeRef paramFringeRef1, FringeRef paramFringeRef2,
            SizeRef paramSizeRef) {
        float f = OrthoAngle(paramGraphicViewerTreeNetworkNode1);
        int i = (f == 90.0F) || (f == 270.0F) ? 1 : 0;
        int j = paramGraphicViewerTreeNetworkNode1.getNodeSpacing();
        Point[] arrayOfPoint1 = paramFringeRef1.getFringe();
        Point[] arrayOfPoint2 = paramFringeRef2.getFringe();
        Dimension localDimension1 = paramSizeRef.getSize();
        Point[] arrayOfPoint3 = paramGraphicViewerTreeNetworkNode2.LeftFringe;
        Point[] arrayOfPoint4 = paramGraphicViewerTreeNetworkNode2.RightFringe;
        Dimension localDimension2 = paramGraphicViewerTreeNetworkNode2
                .getSubtreeSize();
        int k = i != 0 ? Math.max(localDimension1.height,
                localDimension2.height) : Math.max(localDimension1.width,
                localDimension2.width);
        if ((arrayOfPoint3 == null)
                || (f != OrthoAngle(paramGraphicViewerTreeNetworkNode2))) {
            arrayOfPoint3 = AllocTempPointArray(2);
            arrayOfPoint4 = AllocTempPointArray(2);
            if (i != 0) {
                arrayOfPoint3[0] = new Point(0, 0);
                arrayOfPoint3[1] = new Point(0, localDimension2.height);
                arrayOfPoint4[0] = new Point(localDimension2.width, 0);
                arrayOfPoint4[1] = new Point(arrayOfPoint4[0].x,
                        arrayOfPoint3[1].y);
            } else {
                arrayOfPoint3[0] = new Point(0, 0);
                arrayOfPoint3[1] = new Point(localDimension2.width, 0);
                arrayOfPoint4[0] = new Point(0, localDimension2.height);
                arrayOfPoint4[1] = new Point(arrayOfPoint3[1].x,
                        arrayOfPoint4[0].y);
            }
        }
        if (i != 0) {
            int m = localDimension1.width;
            int n = m - FringeDistanceX(arrayOfPoint2, arrayOfPoint3, m);
            n += j;
            paramFringeRef1.setFringe(FringeUnionLeftX(arrayOfPoint1,
                    arrayOfPoint3, n));
            paramFringeRef2.setFringe(FringeUnionRightX(arrayOfPoint2,
                    arrayOfPoint4, n));
            paramSizeRef.setSize(new Dimension(Math.max(0, n)
                    + localDimension2.width, k));
            FreeTempPointArray(arrayOfPoint1);
            FreeTempPointArray(arrayOfPoint3);
            FreeTempPointArray(arrayOfPoint2);
            FreeTempPointArray(arrayOfPoint4);
            return n;
        }
        int m = localDimension1.height;
        int n = m - FringeDistanceY(arrayOfPoint2, arrayOfPoint3, m);
        n += j;
        paramFringeRef1.setFringe(FringeUnionLeftY(arrayOfPoint1,
                arrayOfPoint3, n));
        paramFringeRef2.setFringe(FringeUnionRightY(arrayOfPoint2,
                arrayOfPoint4, n));
        paramSizeRef.setSize(new Dimension(k, Math.max(0, n)
                + localDimension2.height));
        FreeTempPointArray(arrayOfPoint1);
        FreeTempPointArray(arrayOfPoint3);
        FreeTempPointArray(arrayOfPoint2);
        FreeTempPointArray(arrayOfPoint4);
        return n;
    }

    Point[] FringeUnionLeftY(Point[] paramArrayOfPoint1,
            Point[] paramArrayOfPoint2, int paramInt) {
        if ((paramArrayOfPoint1 == null) || (paramArrayOfPoint1.length < 2)
                || (paramArrayOfPoint2 == null)
                || (paramArrayOfPoint2.length < 2)) {
            return null;
        }
        Point[] arrayOfPoint1 = AllocTempPointArray(paramArrayOfPoint1.length
                + paramArrayOfPoint2.length);
        int i = 0;
        int j = 0;
        int k = 0;
        Point localPoint;
        while ((j < paramArrayOfPoint2.length)
                && (paramArrayOfPoint2[j].x < paramArrayOfPoint1[0].x)) {
            localPoint = newPoint(paramArrayOfPoint2[(j++)]);
            localPoint.y += paramInt;
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        while (i < paramArrayOfPoint1.length) {
            localPoint = newPoint(paramArrayOfPoint1[(i++)]);
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        int m = paramArrayOfPoint1[(paramArrayOfPoint1.length - 1)].x;
        while ((j < paramArrayOfPoint2.length)
                && (paramArrayOfPoint2[j].x <= m)) {
            j++;
        }
        while ((j < paramArrayOfPoint2.length) && (paramArrayOfPoint2[j].x > m)) {
            localPoint = newPoint(paramArrayOfPoint2[(j++)]);
            localPoint.y += paramInt;
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        Point[] arrayOfPoint2 = AllocTempPointArray(k);
        for (int n = 0; n < k; n++) {
            arrayOfPoint2[n] = newPoint(arrayOfPoint1[n]);
        }
        FreeTempPointArray(arrayOfPoint1);
        return arrayOfPoint2;
    }

    Point[] FringeUnionLeftX(Point[] paramArrayOfPoint1,
            Point[] paramArrayOfPoint2, int paramInt) {
        if ((paramArrayOfPoint1 == null) || (paramArrayOfPoint1.length < 2)
                || (paramArrayOfPoint2 == null)
                || (paramArrayOfPoint2.length < 2)) {
            return null;
        }
        Point[] arrayOfPoint1 = AllocTempPointArray(paramArrayOfPoint1.length
                + paramArrayOfPoint2.length);
        int i = 0;
        int j = 0;
        int k = 0;
        Point localPoint;
        while ((j < paramArrayOfPoint2.length)
                && (paramArrayOfPoint2[j].y < paramArrayOfPoint1[0].y)) {
            localPoint = newPoint(paramArrayOfPoint2[(j++)]);
            localPoint.x += paramInt;
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        while (i < paramArrayOfPoint1.length) {
            localPoint = newPoint(paramArrayOfPoint1[(i++)]);
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        int m = paramArrayOfPoint1[(paramArrayOfPoint1.length - 1)].y;
        while ((j < paramArrayOfPoint2.length)
                && (paramArrayOfPoint2[j].y <= m)) {
            j++;
        }
        while ((j < paramArrayOfPoint2.length) && (paramArrayOfPoint2[j].y > m)) {
            localPoint = newPoint(paramArrayOfPoint2[(j++)]);
            localPoint.x += paramInt;
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        Point[] arrayOfPoint2 = AllocTempPointArray(k);
        for (int n = 0; n < k; n++) {
            arrayOfPoint2[n] = newPoint(arrayOfPoint1[n]);
        }
        FreeTempPointArray(arrayOfPoint1);
        return arrayOfPoint2;
    }

    Point[] FringeUnionRightY(Point[] paramArrayOfPoint1,
            Point[] paramArrayOfPoint2, int paramInt) {
        if ((paramArrayOfPoint1 == null) || (paramArrayOfPoint1.length < 2)
                || (paramArrayOfPoint2 == null)
                || (paramArrayOfPoint2.length < 2)) {
            return null;
        }
        Point[] arrayOfPoint1 = AllocTempPointArray(paramArrayOfPoint1.length
                + paramArrayOfPoint2.length);
        int i = 0;
        int j = 0;
        int k = 0;
        Point localPoint;
        while ((i < paramArrayOfPoint1.length)
                && (paramArrayOfPoint1[i].x < paramArrayOfPoint2[0].x)) {
            localPoint = newPoint(paramArrayOfPoint1[(i++)]);
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        while (j < paramArrayOfPoint2.length) {
            localPoint = newPoint(paramArrayOfPoint2[(j++)]);
            localPoint.y += paramInt;
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        int m = paramArrayOfPoint2[(paramArrayOfPoint2.length - 1)].x;
        while ((i < paramArrayOfPoint1.length)
                && (paramArrayOfPoint1[1].x <= m)) {
            i++;
        }
        while ((i < paramArrayOfPoint1.length) && (paramArrayOfPoint1[i].x > m)) {
            localPoint = newPoint(paramArrayOfPoint1[(i++)]);
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        Point[] arrayOfPoint2 = AllocTempPointArray(k);
        for (int n = 0; n < k; n++) {
            arrayOfPoint2[n] = newPoint(arrayOfPoint1[n]);
        }
        FreeTempPointArray(arrayOfPoint1);
        return arrayOfPoint2;
    }

    Point[] FringeUnionRightX(Point[] paramArrayOfPoint1,
            Point[] paramArrayOfPoint2, int paramInt) {
        if ((paramArrayOfPoint1 == null) || (paramArrayOfPoint1.length < 2)
                || (paramArrayOfPoint2 == null)
                || (paramArrayOfPoint2.length < 2)) {
            return null;
        }
        Point[] arrayOfPoint1 = AllocTempPointArray(paramArrayOfPoint1.length
                + paramArrayOfPoint2.length);
        int i = 0;
        int j = 0;
        int k = 0;
        Point localPoint;
        while ((i < paramArrayOfPoint1.length)
                && (paramArrayOfPoint1[i].y < paramArrayOfPoint2[0].y)) {
            localPoint = newPoint(paramArrayOfPoint1[(i++)]);
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        while (j < paramArrayOfPoint2.length) {
            localPoint = newPoint(paramArrayOfPoint2[(j++)]);
            localPoint.x += paramInt;
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        int m = paramArrayOfPoint2[(paramArrayOfPoint2.length - 1)].y;
        while ((i < paramArrayOfPoint1.length)
                && (paramArrayOfPoint1[i].y <= m)) {
            i++;
        }
        while ((i < paramArrayOfPoint1.length) && (paramArrayOfPoint1[i].y > m)) {
            localPoint = newPoint(paramArrayOfPoint1[(i++)]);
            arrayOfPoint1[(k++)] = newPoint(localPoint);
        }
        Point[] arrayOfPoint2 = AllocTempPointArray(k);
        for (int n = 0; n < k; n++) {
            arrayOfPoint2[n] = newPoint(arrayOfPoint1[n]);
        }
        FreeTempPointArray(arrayOfPoint1);
        return arrayOfPoint2;
    }

    static int FringeDistanceY(Point[] paramArrayOfPoint1,
            Point[] paramArrayOfPoint2, int paramInt) {
        int i = 9999999;
        if ((paramArrayOfPoint1 == null) || (paramArrayOfPoint1.length < 2)
                || (paramArrayOfPoint2 == null)
                || (paramArrayOfPoint2.length < 2)) {
            return i;
        }
        int j = 0;
        int k = 0;
        while ((j < paramArrayOfPoint1.length)
                && (k < paramArrayOfPoint2.length)) {
            Point localPoint1 = newPoint(paramArrayOfPoint1[j]);
            Point localPoint2 = newPoint(paramArrayOfPoint2[k]);
            localPoint2.y += paramInt;
            Point localPoint3 = newPoint(localPoint1);
            if (j + 1 < paramArrayOfPoint1.length) {
                localPoint3 = newPoint(paramArrayOfPoint1[(j + 1)]);
            }
            Point localPoint4 = newPoint(localPoint2);
            if (k + 1 < paramArrayOfPoint2.length) {
                localPoint4 = newPoint(paramArrayOfPoint2[(k + 1)]);
                localPoint4.y += paramInt;
            }
            int m = i;
            if (localPoint1.x == localPoint2.x) {
                m = localPoint2.y - localPoint1.y;
            } else if ((localPoint1.x > localPoint2.x)
                    && (localPoint1.x < localPoint4.x)) {
                m = localPoint2.y + (localPoint1.x - localPoint2.x)
                        / (localPoint4.x - localPoint2.x)
                        * (localPoint4.y - localPoint2.y) - localPoint1.y;
            } else if ((localPoint2.x > localPoint1.x)
                    && (localPoint2.x < localPoint3.x)) {
                m = localPoint2.y
                        - (localPoint1.y + (localPoint2.x - localPoint1.x)
                                / (localPoint3.x - localPoint1.x)
                                * (localPoint3.y - localPoint1.y));
            }
            if (m < i) {
                i = m;
            }
            if (localPoint3.x <= -localPoint1.x) {
                j++;
            } else if (localPoint4.x <= localPoint2.x) {
                k++;
            } else {
                if (localPoint3.x <= localPoint4.x) {
                    j++;
                }
                if (localPoint4.x <= localPoint3.x) {
                    k++;
                }
            }
        }
        return i;
    }

    static int FringeDistanceX(Point[] paramArrayOfPoint1,
            Point[] paramArrayOfPoint2, int paramInt) {
        int i = 9999999;
        if ((paramArrayOfPoint1 == null) || (paramArrayOfPoint1.length < 2)
                || (paramArrayOfPoint2 == null)
                || (paramArrayOfPoint2.length < 2)) {
            return i;
        }
        int j = 0;
        int k = 0;
        while ((j < paramArrayOfPoint1.length)
                && (k < paramArrayOfPoint2.length)) {
            Point localPoint1 = newPoint(paramArrayOfPoint1[j]);
            Point localPoint2 = newPoint(paramArrayOfPoint2[k]);
            localPoint2.x += paramInt;
            Point localPoint3 = newPoint(localPoint1);
            if (j + 1 < paramArrayOfPoint1.length) {
                localPoint3 = newPoint(paramArrayOfPoint1[(j + 1)]);
            }
            Point localPoint4 = newPoint(localPoint2);
            if (k + 1 < paramArrayOfPoint2.length) {
                localPoint4 = newPoint(paramArrayOfPoint2[(k + 1)]);
                localPoint4.x += paramInt;
            }
            int m = i;
            if (localPoint1.y == localPoint2.y) {
                m = localPoint2.x - localPoint1.x;
            } else if ((localPoint1.y > localPoint2.y)
                    && (localPoint1.y < localPoint4.y)) {
                m = localPoint2.x + (localPoint1.y - localPoint2.y)
                        / (localPoint4.y - localPoint2.y)
                        * (localPoint4.x - localPoint2.x) - localPoint1.x;
            } else if ((localPoint2.y > localPoint1.y)
                    && (localPoint2.y < localPoint3.y)) {
                m = localPoint2.x
                        - (localPoint1.x + (localPoint2.y - localPoint1.y)
                                / (localPoint3.y - localPoint1.y)
                                * (localPoint3.x - localPoint1.x));
            }
            if (m < i) {
                i = m;
            }
            if (localPoint3.y <= localPoint1.y) {
                j++;
            } else if (localPoint4.y <= localPoint2.y) {
                k++;
            } else {
                if (localPoint3.y <= localPoint4.y) {
                    j++;
                }
                if (localPoint4.y <= localPoint3.y) {
                    k++;
                }
            }
        }
        return i;
    }

    Point[] AllocTempPointArray(int paramInt) {
        if (myTempArrays == null) {
            myTempArrays = new Point[10][][];
        }
        for (int i = 0; i < myTempArrays.length; i++) {
            Object[] localObject = myTempArrays[i];
            if ((localObject == null) || (paramInt >= localObject.length)) {
                localObject = new Point[Math.max(paramInt + 1, 40)][];
                myTempArrays[i] = (Point[][]) localObject;
            }
            Point[] arrayOfPoint = (Point[]) localObject[paramInt];
            if (arrayOfPoint != null) {
                localObject[paramInt] = null;
                return arrayOfPoint;
            }
        }
        return new Point[paramInt];
    }

    void FreeTempPointArray(Point[] paramArrayOfPoint) {
        if (myTempArrays == null) {
            return;
        }
        int i = paramArrayOfPoint.length;
        for (int j = 0; j < myTempArrays.length; j++) {
            Point[][] arrayOfPoint = myTempArrays[j];
            if ((arrayOfPoint != null) && (i < arrayOfPoint.length)
                    && (arrayOfPoint[i] == null)) {
                arrayOfPoint[i] = paramArrayOfPoint;
                return;
            }
        }
    }

    public int getArrangement() {
        return myArrangement;
    }

    public void setArrangement(int paramInt) {
        myArrangement = paramInt;
    }

    public Point getArrangementOrigin() {
        return myArrangementOrigin;
    }

    public void setArrangementOrigin(Point paramPoint) {
        myArrangementOrigin = paramPoint;
    }

    public Dimension getArrangementSpacing() {
        return myArrangementSpacing;
    }

    public void setArrangementSpacing(Dimension paramDimension) {
        myArrangementSpacing = paramDimension;
    }

    protected void ArrangeTrees() {
        Object localObject1;
        Object localObject2;
        Object localObject3;
        if (getArrangement() == 3) {
            for (localObject1 = getRoots().getFirstObjectPos(); localObject1 != null; localObject1 = getRoots()
                    .getNextObjectPos((GraphicViewerListPosition) localObject1)) {
                localObject2 = getRoots().getObjectAtPos(
                        (GraphicViewerListPosition) localObject1);
                localObject3 = getTreeNetwork().FindNode(
                        (GraphicViewerObject) localObject2);
                AssignAbsolutePositions(
                        (GraphicViewerTreeNetworkNode) localObject3,
                        ((GraphicViewerObject) localObject2).getLocation());
            }
        } else {
            localObject1 = getArrangementOrigin();
            for (localObject2 = getRoots().getFirstObjectPos(); localObject2 != null; localObject2 = getRoots()
                    .getNextObjectPos((GraphicViewerListPosition) localObject2)) {
                localObject3 = getRoots().getObjectAtPos(
                        (GraphicViewerListPosition) localObject2);
                GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = getTreeNetwork()
                        .FindNode((GraphicViewerObject) localObject3);
                if (localGraphicViewerTreeNetworkNode != null) {
                    Point localPoint = newPoint((Point) localObject1);
                    localPoint.x += localGraphicViewerTreeNetworkNode
                            .getSubtreeOffset().width;
                    localPoint.y += localGraphicViewerTreeNetworkNode
                            .getSubtreeOffset().height;
                    AssignAbsolutePositions(localGraphicViewerTreeNetworkNode,
                            localPoint);
                    switch (getArrangement()) {
                        case 1 :
                            ((Point) localObject1).y += localGraphicViewerTreeNetworkNode
                                    .getSubtreeSize().height
                                    + getArrangementSpacing().height;
                            break;
                        case 2 :
                            ((Point) localObject1).x += localGraphicViewerTreeNetworkNode
                                    .getSubtreeSize().width
                                    + getArrangementSpacing().width;
                    }
                }
            }
        }
    }

    protected void LayoutNodesAndLinks() {
        GraphicViewerDocument localGraphicViewerDocument = getDocument();
        localGraphicViewerDocument.fireForedate(218, 0, null);
        localGraphicViewerDocument.setSuspendUpdates(true);
        SetPortSpotsAll();
        Object localObject;
        for (int i = 0; i < getTreeNetwork().getNodes().getCount(); i++) {
            localObject = getTreeNetwork().getNodes().get(i);
            ((GraphicViewerTreeNetworkNode) localObject).CommitPosition();
        }
        for (int i = 0; i < getTreeNetwork().getNodes().getCount(); i++) {
            localObject = getTreeNetwork().getNodes().get(i);
        }
        localGraphicViewerDocument.setSuspendUpdates(false);
        for (int i = 0; i < getTreeNetwork().getLinks().getCount(); i++) {
            localObject = getTreeNetwork().getLinks().get(i);
            ((GraphicViewerTreeNetworkLink) localObject).CommitPosition();
        }
    }

    private void SetPortSpotsAll() {
        for (GraphicViewerListPosition localGraphicViewerListPosition = getRoots()
                .getFirstObjectPos(); localGraphicViewerListPosition != null; localGraphicViewerListPosition = getRoots()
                .getNextObjectPos(localGraphicViewerListPosition)) {
            GraphicViewerObject localGraphicViewerObject = getRoots()
                    .getObjectAtPos(localGraphicViewerListPosition);
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = getTreeNetwork()
                    .FindNode(localGraphicViewerObject);
            SetPortSpotsTree(localGraphicViewerTreeNetworkNode);
        }
    }

    private void SetPortSpotsTree(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        SetPortSpots(paramGraphicViewerTreeNetworkNode);
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode.getChildren().length; i++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i];
            SetPortSpotsTree(localGraphicViewerTreeNetworkNode);
        }
    }

    protected void SetPortSpots(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        float f = OrthoAngle(paramGraphicViewerTreeNetworkNode);
        int i;
        Object localObject1;
        Object localObject2;
        Object localObject3;
        Object localObject4;
        if (getPath() == 1) {
            for (i = 0; i < paramGraphicViewerTreeNetworkNode
                    .getDestinationLinksList().getCount(); i++) {
                localObject1 = (GraphicViewerLink) paramGraphicViewerTreeNetworkNode
                        .getDestinationLinksList().get(i)
                        .getGraphicViewerObject();
                if (localObject1 != null) {
                    if (paramGraphicViewerTreeNetworkNode.getSetsPortSpot()) {
                        localObject2 = ((GraphicViewerLink) localObject1)
                                .getFromPort();
                        if (localObject2 != null) {
                            localObject3 = ((GraphicViewerPort) localObject2)
                                    .getParentGraphicViewerNode();
                            localObject4 = null;
                            if (localObject3 != null) {
                                localObject4 = ((GraphicViewerNode) localObject3)
                                        .findAll(1, null);
                            }
                            if ((localObject3 != null)
                                    && (localObject4 != null)
                                    && (((ArrayList) localObject4).size() == 1)) {
                                if ((paramGraphicViewerTreeNetworkNode
                                        .getPortSpot() == -1)
                                        && (((GraphicViewerPort) localObject2)
                                                .getPortObject() == null)) {
                                    if (f == 0.0F) {
                                        ((GraphicViewerPort) localObject2)
                                                .setFromSpot(4);
                                    } else if (f == 90.0F) {
                                        ((GraphicViewerPort) localObject2)
                                                .setFromSpot(6);
                                    } else if (f == 180.0F) {
                                        ((GraphicViewerPort) localObject2)
                                                .setFromSpot(8);
                                    } else {
                                        ((GraphicViewerPort) localObject2)
                                                .setFromSpot(2);
                                    }
                                } else {
                                    ((GraphicViewerPort) localObject2)
                                            .setFromSpot(paramGraphicViewerTreeNetworkNode
                                                    .getPortSpot());
                                }
                            }
                        }
                    }
                    if (paramGraphicViewerTreeNetworkNode
                            .getSetsChildPortSpot()) {
                        localObject2 = ((GraphicViewerLink) localObject1)
                                .getToPort();
                        if (localObject2 != null) {
                            localObject3 = ((GraphicViewerPort) localObject2)
                                    .getParentGraphicViewerNode();
                            localObject4 = null;
                            if (localObject3 != null) {
                                localObject4 = ((GraphicViewerNode) localObject3)
                                        .findAll(1, null);
                            }
                            if ((localObject3 != null)
                                    && (localObject4 != null)
                                    && (((ArrayList) localObject4).size() == 1)) {
                                if ((paramGraphicViewerTreeNetworkNode
                                        .getChildPortSpot() == -1)
                                        && (((GraphicViewerPort) localObject2)
                                                .getPortObject() == null)) {
                                    if (f == 0.0F) {
                                        ((GraphicViewerPort) localObject2)
                                                .setToSpot(8);
                                    } else if (f == 90.0F) {
                                        ((GraphicViewerPort) localObject2)
                                                .setToSpot(2);
                                    } else if (f == 180.0F) {
                                        ((GraphicViewerPort) localObject2)
                                                .setToSpot(4);
                                    } else {
                                        ((GraphicViewerPort) localObject2)
                                                .setToSpot(6);
                                    }
                                } else {
                                    ((GraphicViewerPort) localObject2)
                                            .setToSpot(paramGraphicViewerTreeNetworkNode
                                                    .getChildPortSpot());
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (i = 0; i < paramGraphicViewerTreeNetworkNode
                    .getSourceLinksList().getCount(); i++) {
                localObject1 = paramGraphicViewerTreeNetworkNode
                        .getSourceLinksList().get(i);
                localObject2 = (GraphicViewerLink) ((GraphicViewerTreeNetworkLink) localObject1)
                        .getGraphicViewerObject();
                if (localObject2 != null) {
                    ArrayList localArrayList;
                    if (paramGraphicViewerTreeNetworkNode.getSetsPortSpot()) {
                        localObject3 = ((GraphicViewerLink) localObject2)
                                .getToPort();
                        if (localObject3 != null) {
                            localObject4 = ((GraphicViewerPort) localObject3)
                                    .getParentGraphicViewerNode();
                            localArrayList = null;
                            if (localObject4 != null) {
                                localArrayList = ((GraphicViewerNode) localObject4)
                                        .findAll(1, null);
                            }
                            if ((localObject4 != null)
                                    && (localArrayList != null)
                                    && (localArrayList.size() == 1)) {
                                if (paramGraphicViewerTreeNetworkNode
                                        .getPortSpot() == -1) {
                                    if (f == 0.0F) {
                                        ((GraphicViewerPort) localObject3)
                                                .setToSpot(4);
                                    } else if (f == 90.0F) {
                                        ((GraphicViewerPort) localObject3)
                                                .setToSpot(6);
                                    } else if (f == 180.0F) {
                                        ((GraphicViewerPort) localObject3)
                                                .setToSpot(8);
                                    } else {
                                        ((GraphicViewerPort) localObject3)
                                                .setToSpot(2);
                                    }
                                } else {
                                    ((GraphicViewerPort) localObject3)
                                            .setToSpot(paramGraphicViewerTreeNetworkNode
                                                    .getPortSpot());
                                }
                            }
                        }
                    }
                    if (paramGraphicViewerTreeNetworkNode
                            .getSetsChildPortSpot()) {
                        localObject3 = ((GraphicViewerLink) localObject2)
                                .getFromPort();
                        if (localObject3 != null) {
                            localObject4 = ((GraphicViewerPort) localObject3)
                                    .getParentGraphicViewerNode();
                            localArrayList = null;
                            if (localObject4 != null) {
                                localArrayList = ((GraphicViewerNode) localObject4)
                                        .findAll(1, null);
                            }
                            if ((localObject4 != null)
                                    && (localArrayList != null)
                                    && (localArrayList.size() == 1)) {
                                if (paramGraphicViewerTreeNetworkNode
                                        .getChildPortSpot() == -1) {
                                    if (f == 0.0F) {
                                        ((GraphicViewerPort) localObject3)
                                                .setFromSpot(8);
                                    } else if (f == 90.0F) {
                                        ((GraphicViewerPort) localObject3)
                                                .setFromSpot(2);
                                    } else if (f == 180.0F) {
                                        ((GraphicViewerPort) localObject3)
                                                .setFromSpot(4);
                                    } else {
                                        ((GraphicViewerPort) localObject3)
                                                .setFromSpot(6);
                                    }
                                } else {
                                    ((GraphicViewerPort) localObject3)
                                            .setFromSpot(paramGraphicViewerTreeNetworkNode
                                                    .getChildPortSpot());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static float OrthoAngle(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (paramGraphicViewerTreeNetworkNode.getAngle() <= 45.0F) {
            return 0.0F;
        }
        if (paramGraphicViewerTreeNetworkNode.getAngle() <= 135.0F) {
            return 90.0F;
        }
        if (paramGraphicViewerTreeNetworkNode.getAngle() <= 225.0F) {
            return 180.0F;
        }
        if (paramGraphicViewerTreeNetworkNode.getAngle() <= 315.0F) {
            return 270.0F;
        }
        return 0.0F;
    }

    public GraphicViewerTreeNetworkNode getRootDefaults() {
        return myRootDefaults;
    }

    public GraphicViewerTreeNetworkNode getAlternateDefaults() {
        return myAlternateDefaults;
    }

    public int getSorting() {
        return getRootDefaults().getSorting();
    }

    public void setSorting(int paramInt) {
        getRootDefaults().setSorting(paramInt);
    }

    public Comparator getComparer() {
        return getRootDefaults().getComparer();
    }

    public void setComparer(Comparator paramComparator) {
        getRootDefaults().setComparer(paramComparator);
    }

    public Comparator getAlphabeticNodeTextComparer() {
        return myAlphabeticNodeTextComparer;
    }

    public float getAngle() {
        return getRootDefaults().getAngle();
    }

    public void setAngle(float paramFloat) {
        getRootDefaults().setAngle(paramFloat);
    }

    public int getAlignment() {
        return getRootDefaults().getAlignment();
    }

    public void setAlignment(int paramInt) {
        getRootDefaults().setAlignment(paramInt);
    }

    public int getNodeIndent() {
        return getRootDefaults().getNodeIndent();
    }

    public void setNodeIndent(int paramInt) {
        getRootDefaults().setNodeIndent(paramInt);
    }

    public int getNodeSpacing() {
        return getRootDefaults().getNodeSpacing();
    }

    public void setNodeSpacing(int paramInt) {
        getRootDefaults().setNodeSpacing(paramInt);
    }

    public int getLayerSpacing() {
        return getRootDefaults().getLayerSpacing();
    }

    public void setLayerSpacing(int paramInt) {
        getRootDefaults().setLayerSpacing(paramInt);
    }

    public int getCompaction() {
        return getRootDefaults().getCompaction();
    }

    public void setCompaction(int paramInt) {
        getRootDefaults().setCompaction(paramInt);
    }

    public int getBreadthLimit() {
        return getRootDefaults().getBreadthLimit();
    }

    public void setBreadthLimit(int paramInt) {
        getRootDefaults().setBreadthLimit(paramInt);
    }

    public int getRowSpacing() {
        return getRootDefaults().getRowSpacing();
    }

    public void setRowSpacing(int paramInt) {
        getRootDefaults().setRowSpacing(paramInt);
    }

    public int getRowIndent() {
        return getRootDefaults().getRowIndent();
    }

    public void setRowIndent(int paramInt) {
        getRootDefaults().setRowIndent(paramInt);
    }

    public boolean getSetsPortSpot() {
        return getRootDefaults().getSetsPortSpot();
    }

    public void setSetsPortSpot(boolean paramBoolean) {
        getRootDefaults().setSetsPortSport(paramBoolean);
    }

    public int getPortSpot() {
        return getRootDefaults().getPortSpot();
    }

    public void setPortSpot(int paramInt) {
        getRootDefaults().setPortSpot(paramInt);
    }

    public boolean getSetsChildPortSpot() {
        return getRootDefaults().getSetsChildPortSpot();
    }

    public void setSetsChildPortSpot(boolean paramBoolean) {
        getRootDefaults().setSetsChildPortSpot(paramBoolean);
    }

    public int getChildPortSpot() {
        return getRootDefaults().getChildPortSpot();
    }

    public void setChildPortSpot(int paramInt) {
        getRootDefaults().setChildPortSpot(paramInt);
    }

    void AssignAbsolutePositions(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode,
            Point paramPoint) {
        if (paramGraphicViewerTreeNetworkNode == null) {
            return;
        }
        paramGraphicViewerTreeNetworkNode.setPosition(paramPoint);
        for (int i = 0; i < paramGraphicViewerTreeNetworkNode
                .getChildrenCount(); i++) {
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = paramGraphicViewerTreeNetworkNode
                    .getChildren()[i];
            AssignAbsolutePositions(
                    localGraphicViewerTreeNetworkNode,
                    new Point(paramPoint.x
                            + localGraphicViewerTreeNetworkNode
                                    .getRelativePosition().x, paramPoint.y
                            + localGraphicViewerTreeNetworkNode
                                    .getRelativePosition().y));
        }
    }

    static Point newPoint(Point paramPoint) {
        return new Point(paramPoint);
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class FringeRef {
        Point[] myFringe;

        FringeRef(Point[] arg2) {
            myFringe = arg2;
        }

        Point[] getFringe() {
            return myFringe;
        }

        void setFringe(Point[] paramArrayOfPoint) {
            myFringe = paramArrayOfPoint;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class SizeRef {
        Dimension mySize = null;

        SizeRef(Dimension arg2) {
            mySize = arg2;
        }

        Dimension getSize() {
            return mySize;
        }

        void setSize(Dimension paramDimension) {
            mySize = paramDimension;
        }
    }
}