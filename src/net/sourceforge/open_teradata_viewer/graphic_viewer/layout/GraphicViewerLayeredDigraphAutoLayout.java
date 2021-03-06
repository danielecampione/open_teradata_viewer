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

import java.awt.Point;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerPort;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerLayeredDigraphAutoLayout extends
        GraphicViewerAutoLayout {

    private int layerSpacing;
    private int columnSpacing;
    private int directionOption;
    private int cycleremoveOption;
    private int layeringOption;
    private int initializeOption;
    private int iterations;
    private int aggressiveOption;
    private int packOption;
    private int maxLayer;
    private int maxIndex;
    private int maxColumn;
    private int minIndexLayer;
    private int maxIndexLayer;
    private int indices[];
    private int mySavedLayout[];
    private double myMedians[];
    private int myColumnsPS[];
    private int myCrossings[];
    private GraphicViewerNetworkNode myCachedNodeArrays[][] = new GraphicViewerNetworkNode[100][];;
    private int depthFirstSearchCycleRemovalTime;
    public static final int LD_DIRECTION_UP = 0;
    public static final int LD_DIRECTION_DOWN = 1;
    public static final int LD_DIRECTION_LEFT = 2;
    public static final int LD_DIRECTION_RIGHT = 3;
    public static final int LD_CYCLEREMOVE_GREEDY = 0;
    public static final int LD_CYCLEREMOVE_DFS = 1;
    public static final int LD_LAYERING_LONGESTPATHSINK = 0;
    public static final int LD_LAYERING_LONGESTPATHSOURCE = 1;
    public static final int LD_LAYERING_OPTIMALLINKLENGTH = 2;
    public static final int LD_INITIALIZE_NAIVE = 0;
    public static final int LD_INITIALIZE_DFSOUT = 1;
    public static final int LD_INITIALIZE_DFSIN = 2;
    public static final int LD_AGGRESSIVE_TRUE = 0;
    public static final int LD_AGGRESSIVE_FALSE = 1;
    public static final int LD_PACK_EXPAND = 1;
    public static final int LD_PACK_STRAIGHTEN = 2;
    public static final int LD_PACK_MEDIAN = 4;
    public static final int LD_PACK_ALL = 15;

    public GraphicViewerLayeredDigraphAutoLayout() {
        layerSpacing = 50;
        columnSpacing = 50;
        directionOption = 0;
        cycleremoveOption = 1;
        layeringOption = 2;
        initializeOption = 1;
        iterations = 4;
        aggressiveOption = 1;
        packOption = 7;
    }

    public GraphicViewerLayeredDigraphAutoLayout(
            GraphicViewerDocument graphicViewerDocument) {
        super(graphicViewerDocument);
        layerSpacing = 50;
        columnSpacing = 50;
        directionOption = 0;
        cycleremoveOption = 1;
        layeringOption = 2;
        initializeOption = 1;
        iterations = 4;
        aggressiveOption = 1;
        packOption = 7;
    }

    public GraphicViewerLayeredDigraphAutoLayout(
            GraphicViewerDocument graphicViewerDocument,
            GraphicViewerNetwork graphicViewerNetwork) {
        super(graphicViewerDocument, graphicViewerNetwork);
        layerSpacing = 50;
        columnSpacing = 50;
        directionOption = 0;
        cycleremoveOption = 1;
        layeringOption = 2;
        initializeOption = 1;
        iterations = 4;
        aggressiveOption = 1;
        packOption = 7;
    }

    public GraphicViewerLayeredDigraphAutoLayout(
            GraphicViewerDocument graphicViewerDocument, int k, int l, int i1,
            int j1, int k1, int l1, int i2, int j2) {
        super(graphicViewerDocument);
        layerSpacing = Math.max(k, 1);
        columnSpacing = Math.max(l, 1);
        directionOption = i1;
        cycleremoveOption = j1;
        layeringOption = k1;
        initializeOption = l1;
        iterations = i2;
        aggressiveOption = j2;
        packOption = 7;
    }

    public GraphicViewerLayeredDigraphAutoLayout(
            GraphicViewerDocument graphicViewerDocument,
            GraphicViewerNetwork graphicViewerNetwork, int k, int l, int i1,
            int j1, int k1, int l1, int i2, int j2) {
        super(graphicViewerDocument, graphicViewerNetwork);
        layerSpacing = Math.max(k, 1);
        columnSpacing = Math.max(l, 1);
        directionOption = i1;
        cycleremoveOption = j1;
        layeringOption = k1;
        initializeOption = l1;
        iterations = i2;
        aggressiveOption = j2;
        packOption = 7;
    }

    public GraphicViewerLayeredDigraphAutoLayout(
            GraphicViewerLayeredDigraphAutoLayout graphicViewerLayeredDigraphAutoLayout) {
        myCachedNodeArrays = new GraphicViewerNetworkNode[100][];
        setDocument(graphicViewerLayeredDigraphAutoLayout.getDocument());
        setNetwork(new GraphicViewerNetwork(
                graphicViewerLayeredDigraphAutoLayout.getDocument()));
        setLayerSpacing(graphicViewerLayeredDigraphAutoLayout.getLayerSpacing());
        setColumnSpacing(graphicViewerLayeredDigraphAutoLayout
                .getColumnSpacing());
        setDirectionOption(graphicViewerLayeredDigraphAutoLayout
                .getDirectionOption());
        setCycleRemoveOption(graphicViewerLayeredDigraphAutoLayout
                .getCycleRemoveOption());
        setLayeringOption(graphicViewerLayeredDigraphAutoLayout
                .getLayeringOption());
        setInitializeOption(graphicViewerLayeredDigraphAutoLayout
                .getInitializeOption());
        setIterations(graphicViewerLayeredDigraphAutoLayout.getIterations());
        setAggressiveOption(graphicViewerLayeredDigraphAutoLayout
                .getAggressiveOption());
        setPackOption(graphicViewerLayeredDigraphAutoLayout.getPackOption());
    }

    @Override
    public void performLayout() {
        if (getNetwork() == null) {
            return;
        }
        ClearCaches();
        progressUpdate(0.0D);
        if (getNetwork().getNodeCount() <= 0) {
            progressUpdate(1.0D);
            return;
        }
        getNetwork().deleteSelfLinks();
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            graphicViewerNetworkNode.nodeData = new GraphicViewerLayeredDigraphAutoLayoutNodeData();
        }

        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getNetwork()
                .getLinkArray();
        for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
            graphicViewerNetworkLink.linkData = new GraphicViewerLayeredDigraphAutoLayoutLinkData();
        }

        removeCycles();
        progressUpdate(0.10000000000000001D);
        assignLayersInternal();
        progressUpdate(0.25D);
        makeProper();
        progressUpdate(0.29999999999999999D);
        initializeIndicesInternal();
        progressUpdate(0.34999999999999998D);
        initializeColumns();
        progressUpdate(0.40000000000000002D);
        reduceCrossings();
        progressUpdate(0.59999999999999998D);
        straightenAndPack();
        progressUpdate(0.84999999999999998D);
        layoutNodesAndLinks();
        progressUpdate(1.0D);
    }

    protected int getLinkMinLength(
            GraphicViewerNetworkLink graphicViewerNetworkLink) {
        GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getFromNode();
        GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getToNode();
        int k = 0;
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                .getSuccLinksArray();
        for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkLink[l]
                    .getToNode();
            if (graphicViewerNetworkNode2 == graphicViewerNetworkNode1) {
                k++;
            }
        }

        return k <= 1 ? 1 : 2;
    }

    protected double getLinkLengthWeight(
            GraphicViewerNetworkLink graphicViewerNetworkLink) {
        return 1.0D;
    }

    protected double getLinkStraightenWeight(
            GraphicViewerNetworkLink graphicViewerNetworkLink) {
        GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                .getFromNode();
        GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                .getToNode();
        if (graphicViewerNetworkNode.getGraphicViewerObject() == null
                && graphicViewerNetworkNode1.getGraphicViewerObject() == null) {
            return 8D;
        }
        return graphicViewerNetworkNode.getGraphicViewerObject() != null
                && graphicViewerNetworkNode1.getGraphicViewerObject() != null ? 1.0D
                : 4D;
    }

    protected int getNodeMinLayerSpace(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        int k = 0;
        if (graphicViewerNetworkNode.getGraphicViewerObject() != null) {
            GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                    .getGraphicViewerObject();
            switch (directionOption) {
            case 0: // '\0'
            case 1: // '\001'
                k = graphicViewerObject.getHeight() / 2 / layerSpacing + 1;
                break;

            case 2: // '\002'
            case 3: // '\003'
                k = graphicViewerObject.getWidth() / 2 / layerSpacing + 1;
                break;

            default:
                k = graphicViewerObject.getHeight() / 2 / layerSpacing + 1;
                break;
            }
        }
        return k;
    }

    protected int getNodeMinColumnSpace(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        int k = 0;
        if (graphicViewerNetworkNode.getGraphicViewerObject() != null) {
            GraphicViewerObject graphicViewerObject = graphicViewerNetworkNode
                    .getGraphicViewerObject();
            switch (directionOption) {
            case 0: // '\0'
            case 1: // '\001'
                k = graphicViewerObject.getWidth() / 2 / columnSpacing + 1;
                break;

            case 2: // '\002'
            case 3: // '\003'
                k = graphicViewerObject.getHeight() / 2 / columnSpacing + 1;
                break;

            default:
                k = graphicViewerObject.getWidth() / 2 / columnSpacing + 1;
                break;
            }
        }
        return k;
    }

    protected int[] saveLayout() {
        if (mySavedLayout == null) {
            mySavedLayout = new int[3 * getNetwork().getNodeCount()];
        }
        int k = 0;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l];
            mySavedLayout[k] = nodeData(graphicViewerNetworkNode).layer;
            k++;
            mySavedLayout[k] = nodeData(graphicViewerNetworkNode).column;
            k++;
            mySavedLayout[k] = nodeData(graphicViewerNetworkNode).index;
            k++;
        }

        return mySavedLayout;
    }

    protected void restoreLayout(int ai[]) {
        int k = 0;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l];
            nodeData(graphicViewerNetworkNode).layer = ai[k];
            k++;
            nodeData(graphicViewerNetworkNode).column = ai[k];
            k++;
            nodeData(graphicViewerNetworkNode).index = ai[k];
            k++;
        }

    }

    protected boolean equalLayout(int ai[], int ai1[]) {
        boolean flag = true;
        int k = 0;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            flag = flag && ai[k] == ai1[k];
            k++;
            flag = flag && ai[k] == ai1[k];
            k++;
            flag = flag && ai[k] == ai1[k];
            k++;
        }

        return flag;
    }

    protected int[] crossingMatrix(int k, int l) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        if (myCrossings == null || myCrossings.length < indices[k] * indices[k]) {
            myCrossings = new int[indices[k] * indices[k]];
        }
        int ai[] = myCrossings;
        for (int i1 = 0; i1 < indices[k]; i1++) {
            int j1 = 0;
            if (l > 0 || l == 0) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = aGraphicViewerNetworkNode[i1]
                        .getPredLinksArray();
                for (int l1 = 0; l1 < aGraphicViewerNetworkLink.length; l1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l1];
                    if (!linkData(graphicViewerNetworkLink).valid
                            || nodeData(graphicViewerNetworkLink.getFromNode()).layer == k) {
                        continue;
                    }
                    int l2 = nodeData(graphicViewerNetworkLink.getFromNode()).index;
                    int j3 = linkData(graphicViewerNetworkLink).portToPos;
                    int l3 = linkData(graphicViewerNetworkLink).portFromPos;
                    for (int l4 = l1 + 1; l4 < aGraphicViewerNetworkLink.length; l4++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink4 = aGraphicViewerNetworkLink[l4];
                        if (!linkData(graphicViewerNetworkLink4).valid
                                || nodeData(graphicViewerNetworkLink4
                                        .getFromNode()).layer == k) {
                            continue;
                        }
                        int l5 = nodeData(graphicViewerNetworkLink4
                                .getFromNode()).index;
                        int l6 = linkData(graphicViewerNetworkLink4).portToPos;
                        int l7 = linkData(graphicViewerNetworkLink4).portFromPos;
                        if (j3 < l6 && (l2 > l5 || l2 == l5 && l3 > l7)) {
                            j1++;
                        }
                        if (l6 < j3 && (l5 > l2 || l5 == l2 && l7 > l3)) {
                            j1++;
                        }
                    }

                }

            }
            if (l < 0 || l == 0) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = aGraphicViewerNetworkNode[i1]
                        .getSuccLinksArray();
                for (int i2 = 0; i2 < aGraphicViewerNetworkLink1.length; i2++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[i2];
                    if (!linkData(graphicViewerNetworkLink1).valid
                            || nodeData(graphicViewerNetworkLink1.getToNode()).layer == k) {
                        continue;
                    }
                    int i3 = nodeData(graphicViewerNetworkLink1.getToNode()).index;
                    int k3 = linkData(graphicViewerNetworkLink1).portToPos;
                    int i4 = linkData(graphicViewerNetworkLink1).portFromPos;
                    for (int i5 = i2 + 1; i5 < aGraphicViewerNetworkLink1.length; i5++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink5 = aGraphicViewerNetworkLink1[i5];
                        if (!linkData(graphicViewerNetworkLink5).valid
                                || nodeData(graphicViewerNetworkLink5
                                        .getToNode()).layer == k) {
                            continue;
                        }
                        int i6 = nodeData(graphicViewerNetworkLink5.getToNode()).index;
                        int i7 = linkData(graphicViewerNetworkLink5).portToPos;
                        int i8 = linkData(graphicViewerNetworkLink5).portFromPos;
                        if (i4 < i8 && (i3 > i6 || i3 == i6 && k3 > i7)) {
                            j1++;
                        }
                        if (i8 < i4 && (i6 > i3 || i6 == i3 && i7 > k3)) {
                            j1++;
                        }
                    }

                }

            }
            ai[i1 * indices[k] + i1] = j1;
            for (int k1 = i1 + 1; k1 < indices[k]; k1++) {
                int j2 = 0;
                int k2 = 0;
                if (l > 0 || l == 0) {
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink2[] = aGraphicViewerNetworkNode[i1]
                            .getPredLinksArray();
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink4[] = aGraphicViewerNetworkNode[k1]
                            .getPredLinksArray();
                    for (int j4 = 0; j4 < aGraphicViewerNetworkLink2.length; j4++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink2 = aGraphicViewerNetworkLink2[j4];
                        if (!linkData(graphicViewerNetworkLink2).valid
                                || nodeData(graphicViewerNetworkLink2
                                        .getFromNode()).layer == k) {
                            continue;
                        }
                        int j5 = nodeData(graphicViewerNetworkLink2
                                .getFromNode()).index;
                        linkData(graphicViewerNetworkLink2);
                        int j7 = linkData(graphicViewerNetworkLink2).portFromPos;
                        for (int j8 = 0; j8 < aGraphicViewerNetworkLink4.length; j8++) {
                            GraphicViewerNetworkLink graphicViewerNetworkLink6 = aGraphicViewerNetworkLink4[j8];
                            if (!linkData(graphicViewerNetworkLink6).valid
                                    || nodeData(graphicViewerNetworkLink6
                                            .getFromNode()).layer == k) {
                                continue;
                            }
                            int l8 = nodeData(graphicViewerNetworkLink6
                                    .getFromNode()).index;
                            linkData(graphicViewerNetworkLink6);
                            int l9 = linkData(graphicViewerNetworkLink6).portFromPos;
                            if (j5 < l8 || j5 == l8 && j7 < l9) {
                                k2++;
                            }
                            if (l8 < j5 || l8 == j5 && l9 < j7) {
                                j2++;
                            }
                        }

                    }

                }
                if (l < 0 || l == 0) {
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink3[] = aGraphicViewerNetworkNode[i1]
                            .getSuccLinksArray();
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink5[] = aGraphicViewerNetworkNode[k1]
                            .getSuccLinksArray();
                    for (int k4 = 0; k4 < aGraphicViewerNetworkLink3.length; k4++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink3 = aGraphicViewerNetworkLink3[k4];
                        if (!linkData(graphicViewerNetworkLink3).valid
                                || nodeData(graphicViewerNetworkLink3
                                        .getToNode()).layer == k) {
                            continue;
                        }
                        int k5 = nodeData(graphicViewerNetworkLink3.getToNode()).index;
                        int k6 = linkData(graphicViewerNetworkLink3).portToPos;
                        linkData(graphicViewerNetworkLink3);
                        for (int k8 = 0; k8 < aGraphicViewerNetworkLink5.length; k8++) {
                            GraphicViewerNetworkLink graphicViewerNetworkLink7 = aGraphicViewerNetworkLink5[k8];
                            if (!linkData(graphicViewerNetworkLink7).valid
                                    || nodeData(graphicViewerNetworkLink7
                                            .getToNode()).layer == k) {
                                continue;
                            }
                            int i9 = nodeData(graphicViewerNetworkLink7
                                    .getToNode()).index;
                            int k9 = linkData(graphicViewerNetworkLink7).portToPos;
                            linkData(graphicViewerNetworkLink7);
                            if (k5 < i9 || k5 == i9 && k6 < k9) {
                                k2++;
                            }
                            if (i9 < k5 || i9 == k5 && k9 < k6) {
                                j2++;
                            }
                        }

                    }

                }
                ai[i1 * indices[k] + k1] = j2;
                ai[k1 * indices[k] + i1] = k2;
            }

        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        return ai;
    }

    protected int countCrossings() {
        int l = 0;
        for (int k = 0; k <= maxLayer; k++) {
            int ai[] = crossingMatrix(k, 1);
            for (int i1 = 0; i1 < indices[k]; i1++) {
                for (int j1 = i1; j1 < indices[k]; j1++) {
                    l += ai[i1 * indices[k] + j1];
                }

            }

        }

        return l;
    }

    protected double bends(int k, int l, boolean flag) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        double d1 = 0.0D;
        for (int i1 = 0; i1 < indices[k]; i1++) {
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = null;
            if (l < 0 || l == 0) {
                aGraphicViewerNetworkLink = aGraphicViewerNetworkNode[i1]
                        .getPredLinksArray();
            }
            GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = null;
            if (l > 0 || l == 0) {
                aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[i1]
                        .getSuccLinksArray();
            }
            if (aGraphicViewerNetworkLink != null) {
                for (int j1 = 0; j1 < aGraphicViewerNetworkLink.length; j1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[j1];
                    if (!linkData(graphicViewerNetworkLink).valid
                            || nodeData(graphicViewerNetworkLink.getFromNode()).layer == k) {
                        continue;
                    }
                    double d2 = nodeData(graphicViewerNetworkLink.getFromNode()).column
                            + linkData(graphicViewerNetworkLink).portFromColOffset;
                    double d4 = nodeData(graphicViewerNetworkLink.getToNode()).column
                            + linkData(graphicViewerNetworkLink).portToColOffset;
                    if (flag) {
                        d1 += Math.abs(d2 - d4)
                                * getLinkStraightenWeight(graphicViewerNetworkLink);
                    } else {
                        d1 += Math.abs(d2 - d4);
                    }
                }

            }
            if (aGraphicViewerNetworkLink1 == null) {
                continue;
            }
            for (int k1 = 0; k1 < aGraphicViewerNetworkLink1.length; k1++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[k1];
                if (!linkData(graphicViewerNetworkLink1).valid
                        || nodeData(graphicViewerNetworkLink1.getToNode()).layer == k) {
                    continue;
                }
                double d3 = nodeData(graphicViewerNetworkLink1.getFromNode()).column
                        + linkData(graphicViewerNetworkLink1).portFromColOffset;
                double d5 = nodeData(graphicViewerNetworkLink1.getToNode()).column
                        + linkData(graphicViewerNetworkLink1).portToColOffset;
                if (flag) {
                    d1 += (Math.abs(d3 - d5) + 1.0D)
                            * getLinkStraightenWeight(graphicViewerNetworkLink1);
                } else {
                    d1 += Math.abs(d3 - d5);
                }
            }

        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        return d1;
    }

    protected double countBends(boolean flag) {
        double d1 = 0.0D;
        for (int k = 0; k <= maxLayer; k++) {
            d1 += bends(k, 1, flag);
        }

        return d1;
    }

    protected void normalize() {
        int k = 0x7fffffff;
        maxColumn = -1;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i1];
            int l = getNodeMinColumnSpace(graphicViewerNetworkNode);
            k = Math.min(k, nodeData(graphicViewerNetworkNode).column - l);
            maxColumn = Math.max(maxColumn,
                    nodeData(graphicViewerNetworkNode).column + l);
        }

        for (int j1 = 0; j1 < aGraphicViewerNetworkNode.length; j1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[j1];
            nodeData(graphicViewerNetworkNode1).column -= k;
        }

        maxColumn -= k;
    }

    protected double[] barycenters(int k, int l) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        double ad[] = new double[indices[k]];
        for (int i1 = 0; i1 < indices[k]; i1++) {
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = null;
            if (l < 0 || l == 0) {
                aGraphicViewerNetworkLink = aGraphicViewerNetworkNode[i1]
                        .getPredLinksArray();
            }
            GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = null;
            if (l > 0 || l == 0) {
                aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[i1]
                        .getSuccLinksArray();
            }
            double d1 = 0.0D;
            int j1 = 0;
            if (aGraphicViewerNetworkLink != null) {
                for (int k1 = 0; k1 < aGraphicViewerNetworkLink.length; k1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k1];
                    if (linkData(graphicViewerNetworkLink).valid
                            && nodeData(graphicViewerNetworkLink.getFromNode()).layer != k) {
                        d1 += nodeData(graphicViewerNetworkLink.getFromNode()).column
                                + linkData(graphicViewerNetworkLink).portFromColOffset;
                        j1++;
                    }
                }

            }
            if (aGraphicViewerNetworkLink1 != null) {
                for (int l1 = 0; l1 < aGraphicViewerNetworkLink1.length; l1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[l1];
                    if (linkData(graphicViewerNetworkLink1).valid
                            && nodeData(graphicViewerNetworkLink1.getToNode()).layer != k) {
                        d1 += nodeData(graphicViewerNetworkLink1.getToNode()).column
                                + linkData(graphicViewerNetworkLink1).portToColOffset;
                        j1++;
                    }
                }

            }
            if (j1 == 0) {
                ad[i1] = -1D;
            } else {
                ad[i1] = d1 / (double) j1;
            }
        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        return ad;
    }

    protected double[] medians(int k, int l) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        if (myMedians == null || myMedians.length < indices[k]) {
            myMedians = new double[2 * indices[k]];
        }
        double arrayOfDouble[] = myMedians;
        for (int i1 = 0; i1 < indices[k]; i1++) {
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = null;
            if (l < 0 || l == 0) {
                aGraphicViewerNetworkLink = aGraphicViewerNetworkNode[i1]
                        .getPredLinksArray();
            }
            GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = null;
            if (l > 0 || l == 0) {
                aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[i1]
                        .getSuccLinksArray();
            }
            int j1 = 0;
            if (aGraphicViewerNetworkLink != null) {
                for (int k1 = 0; k1 < aGraphicViewerNetworkLink.length; k1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k1];
                    if (linkData(graphicViewerNetworkLink).valid
                            && nodeData(graphicViewerNetworkLink.getFromNode()).layer != k) {
                        j1++;
                    }
                }

            }
            if (aGraphicViewerNetworkLink1 != null) {
                for (int l1 = 0; l1 < aGraphicViewerNetworkLink1.length; l1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[l1];
                    if (linkData(graphicViewerNetworkLink1).valid
                            && nodeData(graphicViewerNetworkLink1.getToNode()).layer != k) {
                        j1++;
                    }
                }

            }
            if (j1 == 0) {
                arrayOfDouble[i1] = -1D;
                continue;
            }
            if (myColumnsPS == null || myColumnsPS.length < j1) {
                myColumnsPS = new int[2 * j1];
            }
            j1 = 0;
            if (aGraphicViewerNetworkLink != null) {
                for (int i2 = 0; i2 < aGraphicViewerNetworkLink.length; i2++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink2 = aGraphicViewerNetworkLink[i2];
                    if (linkData(graphicViewerNetworkLink2).valid
                            && nodeData(graphicViewerNetworkLink2.getFromNode()).layer != k) {
                        myColumnsPS[j1] = nodeData(graphicViewerNetworkLink2
                                .getFromNode()).column
                                + linkData(graphicViewerNetworkLink2).portFromColOffset;
                        j1++;
                    }
                }

            }
            if (aGraphicViewerNetworkLink1 != null) {
                for (int j2 = 0; j2 < aGraphicViewerNetworkLink1.length; j2++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink3 = aGraphicViewerNetworkLink1[j2];
                    if (linkData(graphicViewerNetworkLink3).valid
                            && nodeData(graphicViewerNetworkLink3.getToNode()).layer != k) {
                        myColumnsPS[j1] = nodeData(graphicViewerNetworkLink3
                                .getToNode()).column
                                + linkData(graphicViewerNetworkLink3).portToColOffset;
                        j1++;
                    }
                }

            }
            for (boolean flag = true; flag;) {
                flag = false;
                int k2 = 0;
                while (k2 < j1 - 1) {
                    if (myColumnsPS[k2] > myColumnsPS[k2 + 1]) {
                        flag = true;
                        int i3 = myColumnsPS[k2 + 1];
                        myColumnsPS[k2 + 1] = myColumnsPS[k2];
                        myColumnsPS[k2] = i3;
                    }
                    k2++;
                }
            }

            int l2 = j1 / 2;
            if (j1 % 2 == 1) {
                arrayOfDouble[i1] = myColumnsPS[l2];
            } else {
                arrayOfDouble[i1] = ((double) myColumnsPS[l2 - 1] + (double) myColumnsPS[l2]) / 2D;
            }
        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        return arrayOfDouble;
    }

    protected void tightComponent(
            GraphicViewerNetworkNode graphicViewerNetworkNode, int k,
            boolean flag, boolean flag1) {
        if (nodeData(graphicViewerNetworkNode).component != k) {
            nodeData(graphicViewerNetworkNode).component = k;
            if (flag) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                        .getSuccLinksArray();
                for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                            .getToNode();
                    int j1 = nodeData(graphicViewerNetworkNode).layer
                            - nodeData(graphicViewerNetworkNode1).layer;
                    int l1 = getLinkMinLength(graphicViewerNetworkLink);
                    if (j1 == l1) {
                        tightComponent(graphicViewerNetworkNode1, k, flag,
                                flag1);
                    }
                }

            }
            if (flag1) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode
                        .getPredLinksArray();
                for (int i1 = 0; i1 < aGraphicViewerNetworkLink1.length; i1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[i1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = graphicViewerNetworkLink1
                            .getFromNode();
                    int k1 = nodeData(graphicViewerNetworkNode2).layer
                            - nodeData(graphicViewerNetworkNode).layer;
                    int i2 = getLinkMinLength(graphicViewerNetworkLink1);
                    if (k1 == i2) {
                        tightComponent(graphicViewerNetworkNode2, k, flag,
                                flag1);
                    }
                }

            }
        }
    }

    protected void tightComponentUnset(
            GraphicViewerNetworkNode graphicViewerNetworkNode, int k, int l,
            boolean flag, boolean flag1) {
        if (nodeData(graphicViewerNetworkNode).component == l) {
            nodeData(graphicViewerNetworkNode).component = k;
            if (flag) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                        .getSuccLinksArray();
                for (int i1 = 0; i1 < aGraphicViewerNetworkLink.length; i1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                            .getToNode();
                    int k1 = nodeData(graphicViewerNetworkNode).layer
                            - nodeData(graphicViewerNetworkNode1).layer;
                    int i2 = getLinkMinLength(graphicViewerNetworkLink);
                    if (k1 == i2) {
                        tightComponentUnset(graphicViewerNetworkNode1, k, l,
                                flag, flag1);
                    }
                }

            }
            if (flag1) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode
                        .getPredLinksArray();
                for (int j1 = 0; j1 < aGraphicViewerNetworkLink1.length; j1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[j1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = graphicViewerNetworkLink1
                            .getFromNode();
                    int l1 = nodeData(graphicViewerNetworkNode2).layer
                            - nodeData(graphicViewerNetworkNode).layer;
                    int j2 = getLinkMinLength(graphicViewerNetworkLink1);
                    if (l1 == j2) {
                        tightComponentUnset(graphicViewerNetworkNode2, k, l,
                                flag, flag1);
                    }
                }

            }
        }
    }

    protected void component(GraphicViewerNetworkNode graphicViewerNetworkNode,
            int k, boolean flag, boolean flag1) {
        if (nodeData(graphicViewerNetworkNode).component != k) {
            nodeData(graphicViewerNetworkNode).component = k;
            if (flag) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                        .getSuccLinksArray();
                for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                            .getToNode();
                    component(graphicViewerNetworkNode1, k, flag, flag1);
                }

            }
            if (flag1) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode
                        .getPredLinksArray();
                for (int i1 = 0; i1 < aGraphicViewerNetworkLink1.length; i1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[i1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = graphicViewerNetworkLink1
                            .getFromNode();
                    component(graphicViewerNetworkNode2, k, flag, flag1);
                }

            }
        }
    }

    protected void componentUnset(
            GraphicViewerNetworkNode graphicViewerNetworkNode, int k, int l,
            boolean flag, boolean flag1) {
        if (nodeData(graphicViewerNetworkNode).component == l) {
            nodeData(graphicViewerNetworkNode).component = k;
            if (flag) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                        .getSuccLinksArray();
                for (int i1 = 0; i1 < aGraphicViewerNetworkLink.length; i1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[i1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                            .getToNode();
                    componentUnset(graphicViewerNetworkNode1, k, l, flag, flag1);
                }

            }
            if (flag1) {
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode
                        .getPredLinksArray();
                for (int j1 = 0; j1 < aGraphicViewerNetworkLink1.length; j1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[j1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = graphicViewerNetworkLink1
                            .getFromNode();
                    componentUnset(graphicViewerNetworkNode2, k, l, flag, flag1);
                }

            }
        }
    }

    protected void removeCycles() {
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getNetwork()
                .getLinkArray();
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            linkData(graphicViewerNetworkLink).rev = false;
        }

        switch (cycleremoveOption) {
        case 0: // '\0'
            greedyCycleRemoval();
            break;

        case 1: // '\001'
            depthFirstSearchCycleRemoval();
            break;

        default:
            greedyCycleRemoval();
            break;
        }
    }

    protected void greedyCycleRemoval() {
        int k = 0;
        int l = getNetwork().getNodeCount() - 1;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = new GraphicViewerNetworkNode[l + 1];
        GraphicViewerNetworkNode aGraphicViewerNetworkNode1[] = getNetwork()
                .getNodeArray();
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode1.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode3 = aGraphicViewerNetworkNode1[i1];
            nodeData(graphicViewerNetworkNode3).valid = true;
        }

        do {
            if (greedyCycleRemovalFindNode(getNetwork()) == null) {
                break;
            }
            for (GraphicViewerNetworkNode graphicViewerNetworkNode = greedyCycleRemovalFindSink(getNetwork()); graphicViewerNetworkNode != null; graphicViewerNetworkNode = greedyCycleRemovalFindSink(getNetwork())) {
                aGraphicViewerNetworkNode[l] = graphicViewerNetworkNode;
                l--;
                nodeData(graphicViewerNetworkNode).valid = false;
            }

            for (GraphicViewerNetworkNode graphicViewerNetworkNode1 = greedyCycleRemovalFindSource(getNetwork()); graphicViewerNetworkNode1 != null; graphicViewerNetworkNode1 = greedyCycleRemovalFindSource(getNetwork())) {
                aGraphicViewerNetworkNode[k] = graphicViewerNetworkNode1;
                k++;
                nodeData(graphicViewerNetworkNode1).valid = false;
            }

            GraphicViewerNetworkNode graphicViewerNetworkNode2 = greedyCycleRemovalFindNodeMaxDegDiff(getNetwork());
            if (graphicViewerNetworkNode2 != null) {
                aGraphicViewerNetworkNode[k] = graphicViewerNetworkNode2;
                k++;
                nodeData(graphicViewerNetworkNode2).valid = false;
            }
        } while (true);
        for (int j1 = 0; j1 < getNetwork().getNodeCount(); j1++) {
            nodeData(aGraphicViewerNetworkNode[j1]).index = j1;
        }

        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getNetwork()
                .getLinkArray();
        for (int k1 = 0; k1 < aGraphicViewerNetworkLink.length; k1++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k1];
            int l1 = nodeData(graphicViewerNetworkLink.getFromNode()).index;
            int i2 = nodeData(graphicViewerNetworkLink.getToNode()).index;
            if (l1 > i2) {
                getNetwork().reverseLink(graphicViewerNetworkLink);
                linkData(graphicViewerNetworkLink).rev = true;
            }
        }

    }

    protected GraphicViewerNetworkNode greedyCycleRemovalFindNode(
            GraphicViewerNetwork graphicViewerNetwork) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            if (nodeData(graphicViewerNetworkNode).valid) {
                return graphicViewerNetworkNode;
            }
        }

        return null;
    }

    protected GraphicViewerNetworkNode greedyCycleRemovalFindSink(
            GraphicViewerNetwork graphicViewerNetwork) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            if (!nodeData(graphicViewerNetworkNode).valid) {
                continue;
            }
            boolean flag = true;
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getSuccLinksArray();
            for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
                if (nodeData(graphicViewerNetworkLink.getToNode()).valid) {
                    flag = false;
                }
            }

            if (flag) {
                return graphicViewerNetworkNode;
            }
        }

        return null;
    }

    protected GraphicViewerNetworkNode greedyCycleRemovalFindSource(
            GraphicViewerNetwork graphicViewerNetwork) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            if (!nodeData(graphicViewerNetworkNode).valid) {
                continue;
            }
            boolean flag = true;
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getPredLinksArray();
            for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
                if (nodeData(graphicViewerNetworkLink.getFromNode()).valid) {
                    flag = false;
                }
            }

            if (flag) {
                return graphicViewerNetworkNode;
            }
        }

        return null;
    }

    protected GraphicViewerNetworkNode greedyCycleRemovalFindNodeMaxDegDiff(
            GraphicViewerNetwork graphicViewerNetwork) {
        GraphicViewerNetworkNode graphicViewerNetworkNode = null;
        int k = 0;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[l];
            if (!nodeData(graphicViewerNetworkNode1).valid) {
                continue;
            }
            int i1 = 0;
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode1
                    .getSuccLinksArray();
            for (int j1 = 0; j1 < aGraphicViewerNetworkLink.length; j1++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[j1];
                if (nodeData(graphicViewerNetworkLink.getToNode()).valid) {
                    i1++;
                }
            }

            int k1 = 0;
            GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode1
                    .getPredLinksArray();
            for (int l1 = 0; l1 < aGraphicViewerNetworkLink1.length; l1++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[l1];
                if (nodeData(graphicViewerNetworkLink1.getFromNode()).valid) {
                    k1++;
                }
            }

            if (graphicViewerNetworkNode == null || k < i1 - k1) {
                graphicViewerNetworkNode = graphicViewerNetworkNode1;
                k = i1 - k1;
            }
        }

        return graphicViewerNetworkNode;
    }

    protected void depthFirstSearchCycleRemoval() {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            nodeData(graphicViewerNetworkNode).discover = -1;
            nodeData(graphicViewerNetworkNode).finish = -1;
        }

        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getNetwork()
                .getLinkArray();
        for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
            linkData(graphicViewerNetworkLink).forest = false;
        }

        depthFirstSearchCycleRemovalTime = 0;
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[i1];
            if (graphicViewerNetworkNode1.getPredLinksList().isEmpty()) {
                depthFirstSearchCycleRemovalVisit(graphicViewerNetworkNode1);
            }
        }

        aGraphicViewerNetworkNode = getNetwork().getNodeArray();
        for (int j1 = 0; j1 < aGraphicViewerNetworkNode.length; j1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkNode[j1];
            if (nodeData(graphicViewerNetworkNode2).discover == -1) {
                depthFirstSearchCycleRemovalVisit(graphicViewerNetworkNode2);
            }
        }

        aGraphicViewerNetworkLink = getNetwork().getLinkArray();
        for (int k1 = 0; k1 < aGraphicViewerNetworkLink.length; k1++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink[k1];
            if (linkData(graphicViewerNetworkLink1).forest) {
                continue;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode3 = graphicViewerNetworkLink1
                    .getFromNode();
            int l1 = nodeData(graphicViewerNetworkNode3).discover;
            int i2 = nodeData(graphicViewerNetworkNode3).finish;
            GraphicViewerNetworkNode graphicViewerNetworkNode4 = graphicViewerNetworkLink1
                    .getToNode();
            int j2 = nodeData(graphicViewerNetworkNode4).discover;
            int k2 = nodeData(graphicViewerNetworkNode4).finish;
            if (j2 < l1 && i2 < k2) {
                getNetwork().reverseLink(graphicViewerNetworkLink1);
                linkData(graphicViewerNetworkLink1).rev = true;
            }
        }

    }

    protected void depthFirstSearchCycleRemovalVisit(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        nodeData(graphicViewerNetworkNode).discover = depthFirstSearchCycleRemovalTime;
        depthFirstSearchCycleRemovalTime++;
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                .getSuccLinksArray();
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                    .getToNode();
            if (nodeData(graphicViewerNetworkNode1).discover == -1) {
                linkData(graphicViewerNetworkLink).forest = true;
                depthFirstSearchCycleRemovalVisit(graphicViewerNetworkNode1);
            }
        }

        nodeData(graphicViewerNetworkNode).finish = depthFirstSearchCycleRemovalTime;
        depthFirstSearchCycleRemovalTime++;
    }

    void assignLayersInternal() {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            nodeData(graphicViewerNetworkNode).layer = -1;
        }

        maxLayer = -1;
        assignLayers();
        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[l];
            maxLayer = Math.max(maxLayer,
                    nodeData(graphicViewerNetworkNode1).layer);
        }

    }

    protected void assignLayers() {
        switch (layeringOption) {
        case 0: // '\0'
            longestPathSinkLayering();
            break;

        case 1: // '\001'
            longestPathSourceLayering();
            break;

        case 2: // '\002'
            optimalLinkLengthLayering();
            break;

        default:
            longestPathSinkLayering();
            break;
        }
    }

    protected void longestPathSinkLayering() {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            int l = longestPathSinkLayeringLength(graphicViewerNetworkNode);
            maxLayer = Math.max(l, maxLayer);
        }

    }

    protected int longestPathSinkLayeringLength(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        int k = 0;
        if (nodeData(graphicViewerNetworkNode).layer == -1) {
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getSuccLinksArray();
            for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
                int i1 = getLinkMinLength(graphicViewerNetworkLink);
                k = Math.max(
                        k,
                        longestPathSinkLayeringLength(graphicViewerNetworkLink
                                .getToNode()) + i1);
            }

            nodeData(graphicViewerNetworkNode).layer = k;
        } else {
            k = nodeData(graphicViewerNetworkNode).layer;
        }
        return k;
    }

    protected void longestPathSourceLayering() {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            int i1 = longestPathSourceLayeringLength(graphicViewerNetworkNode);
            maxLayer = Math.max(i1, maxLayer);
        }

        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[l];
            nodeData(graphicViewerNetworkNode1).layer = maxLayer
                    - nodeData(graphicViewerNetworkNode1).layer;
        }

    }

    protected int longestPathSourceLayeringLength(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        int k = 0;
        if (nodeData(graphicViewerNetworkNode).layer == -1) {
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getPredLinksArray();
            for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
                int i1 = getLinkMinLength(graphicViewerNetworkLink);
                k = Math.max(
                        k,
                        longestPathSourceLayeringLength(graphicViewerNetworkLink
                                .getFromNode()) + i1);
            }

            nodeData(graphicViewerNetworkNode).layer = k;
        } else {
            k = nodeData(graphicViewerNetworkNode).layer;
        }
        return k;
    }

    protected void optimalLinkLengthLayering() {
        longestPathSinkLayering();
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k];
            nodeData(graphicViewerNetworkNode).valid = false;
        }

        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[l];
            if (graphicViewerNetworkNode1.getPredLinksList().isEmpty()) {
                optimalLinkLengthLayeringDepthFirstSearch(graphicViewerNetworkNode1);
            }
        }

        int i1 = 0x7fffffff;
        for (int j1 = 0; j1 < aGraphicViewerNetworkNode.length; j1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkNode[j1];
            i1 = Math.min(i1, nodeData(graphicViewerNetworkNode2).layer);
        }

        maxLayer = -1;
        for (int k1 = 0; k1 < aGraphicViewerNetworkNode.length; k1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode3 = aGraphicViewerNetworkNode[k1];
            nodeData(graphicViewerNetworkNode3).layer -= i1;
            maxLayer = Math.max(maxLayer,
                    nodeData(graphicViewerNetworkNode3).layer);
        }

    }

    protected void optimalLinkLengthLayeringDepthFirstSearch(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        if (!nodeData(graphicViewerNetworkNode).valid) {
            nodeData(graphicViewerNetworkNode).valid = true;
            GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                    .getSuccLinksArray();
            for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
                GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                        .getToNode();
                optimalLinkLengthLayeringDepthFirstSearch(graphicViewerNetworkNode1);
            }

            optimalLinkLengthLayeringPull(graphicViewerNetworkNode);
            optimalLinkLengthLayeringPush(graphicViewerNetworkNode);
        }
    }

    protected void optimalLinkLengthLayeringPull(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[k];
            nodeData(graphicViewerNetworkNode1).component = -1;
        }

        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode
                .getPredLinksArray();
        for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l];
            int i1 = getLinkMinLength(graphicViewerNetworkLink);
            int k1 = nodeData(graphicViewerNetworkLink.getFromNode()).layer
                    - nodeData(graphicViewerNetworkLink.getToNode()).layer;
            if (k1 > i1) {
                tightComponentUnset(graphicViewerNetworkLink.getFromNode(), 0,
                        -1, true, false);
            }
        }

        tightComponentUnset(graphicViewerNetworkNode, 1, -1, true, true);
        while (nodeData(graphicViewerNetworkNode).component != 0) {
            double d1 = 0.0D;
            int j1 = 0x7fffffff;
            double d2 = 0.0D;
            GraphicViewerNetworkNode graphicViewerNetworkNode2 = null;
            GraphicViewerNetworkNode aGraphicViewerNetworkNode1[] = getNetwork()
                    .getNodeArray();
            for (int l1 = 0; l1 < aGraphicViewerNetworkNode1.length; l1++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode3 = aGraphicViewerNetworkNode1[l1];
                if (nodeData(graphicViewerNetworkNode3).component != 1) {
                    continue;
                }
                double d3 = 0.0D;
                boolean flag = false;
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode3
                        .getPredLinksArray();
                for (int j2 = 0; j2 < aGraphicViewerNetworkLink1.length; j2++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[j2];
                    GraphicViewerNetworkNode graphicViewerNetworkNode5 = graphicViewerNetworkLink1
                            .getFromNode();
                    d3 += getLinkLengthWeight(graphicViewerNetworkLink1);
                    if (nodeData(graphicViewerNetworkNode5).component != 1) {
                        d1 += getLinkLengthWeight(graphicViewerNetworkLink1);
                        int l2 = nodeData(graphicViewerNetworkNode5).layer
                                - nodeData(graphicViewerNetworkNode3).layer;
                        int i3 = getLinkMinLength(graphicViewerNetworkLink1);
                        j1 = Math.min(j1, l2 - i3);
                    }
                }

                GraphicViewerNetworkLink aGraphicViewerNetworkLink2[] = graphicViewerNetworkNode3
                        .getSuccLinksArray();
                for (int k2 = 0; k2 < aGraphicViewerNetworkLink2.length; k2++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink2 = aGraphicViewerNetworkLink2[k2];
                    GraphicViewerNetworkNode graphicViewerNetworkNode6 = graphicViewerNetworkLink2
                            .getToNode();
                    d3 -= getLinkLengthWeight(graphicViewerNetworkLink2);
                    if (nodeData(graphicViewerNetworkNode6).component != 1) {
                        d1 -= getLinkLengthWeight(graphicViewerNetworkLink2);
                    } else {
                        flag = true;
                    }
                }

                if ((graphicViewerNetworkNode2 == null || d3 < d2) && !flag) {
                    graphicViewerNetworkNode2 = graphicViewerNetworkNode3;
                    d2 = d3;
                }
            }

            if (d1 > 0.0D) {
                GraphicViewerNetworkNode aGraphicViewerNetworkNode2[] = getNetwork()
                        .getNodeArray();
                for (int i2 = 0; i2 < aGraphicViewerNetworkNode2.length; i2++) {
                    GraphicViewerNetworkNode graphicViewerNetworkNode4 = aGraphicViewerNetworkNode2[i2];
                    if (nodeData(graphicViewerNetworkNode4).component == 1) {
                        nodeData(graphicViewerNetworkNode4).layer += j1;
                    }
                }

                nodeData(graphicViewerNetworkNode).component = 0;
            } else {
                nodeData(graphicViewerNetworkNode2).component = 0;
            }
        }
    }

    protected void optimalLinkLengthLayeringPush(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k = 0; k < aGraphicViewerNetworkNode.length; k++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[k];
            nodeData(graphicViewerNetworkNode1).component = -1;
        }

        tightComponentUnset(graphicViewerNetworkNode, 1, -1, true, false);
        while (nodeData(graphicViewerNetworkNode).component != 0) {
            double d1 = 0.0D;
            int l = 0x7fffffff;
            double d2 = 0.0D;
            GraphicViewerNetworkNode graphicViewerNetworkNode2 = null;
            GraphicViewerNetworkNode aGraphicViewerNetworkNode1[] = getNetwork()
                    .getNodeArray();
            for (int i1 = 0; i1 < aGraphicViewerNetworkNode1.length; i1++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode3 = aGraphicViewerNetworkNode1[i1];
                if (nodeData(graphicViewerNetworkNode3).component != 1) {
                    continue;
                }
                double d3 = 0.0D;
                boolean flag = false;
                GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = graphicViewerNetworkNode3
                        .getPredLinksArray();
                for (int k1 = 0; k1 < aGraphicViewerNetworkLink.length; k1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode5 = graphicViewerNetworkLink
                            .getFromNode();
                    d3 += getLinkLengthWeight(graphicViewerNetworkLink);
                    if (nodeData(graphicViewerNetworkNode5).component != 1) {
                        d1 += getLinkLengthWeight(graphicViewerNetworkLink);
                    } else {
                        flag = true;
                    }
                }

                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode3
                        .getSuccLinksArray();
                for (int l1 = 0; l1 < aGraphicViewerNetworkLink1.length; l1++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[l1];
                    GraphicViewerNetworkNode graphicViewerNetworkNode6 = graphicViewerNetworkLink1
                            .getToNode();
                    d3 -= getLinkLengthWeight(graphicViewerNetworkLink1);
                    if (nodeData(graphicViewerNetworkNode6).component != 1) {
                        d1 -= getLinkLengthWeight(graphicViewerNetworkLink1);
                        int i2 = nodeData(graphicViewerNetworkNode3).layer
                                - nodeData(graphicViewerNetworkNode6).layer;
                        int j2 = getLinkMinLength(graphicViewerNetworkLink1);
                        l = Math.min(l, i2 - j2);
                    }
                }

                if ((graphicViewerNetworkNode2 == null || d3 > d2) && !flag) {
                    graphicViewerNetworkNode2 = graphicViewerNetworkNode3;
                    d2 = d3;
                }
            }

            if (d1 < 0.0D) {
                GraphicViewerNetworkNode aGraphicViewerNetworkNode2[] = getNetwork()
                        .getNodeArray();
                for (int j1 = 0; j1 < aGraphicViewerNetworkNode2.length; j1++) {
                    GraphicViewerNetworkNode graphicViewerNetworkNode4 = aGraphicViewerNetworkNode2[j1];
                    if (nodeData(graphicViewerNetworkNode4).component == 1) {
                        nodeData(graphicViewerNetworkNode4).layer -= l;
                    }
                }

                nodeData(graphicViewerNetworkNode).component = 0;
            } else {
                nodeData(graphicViewerNetworkNode2).component = 0;
            }
        }
    }

    protected void makeProper() {
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = new GraphicViewerNetworkLink[getNetwork()
                .getLinkCount()];
        getNetwork().getNetworkLinks().toArray(aGraphicViewerNetworkLink);
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            linkData(graphicViewerNetworkLink).valid = false;
        }

        for (int l = 0; l < aGraphicViewerNetworkLink.length; l++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink[l];
            GraphicViewerLink graphicViewerLink = null;
            if (graphicViewerNetworkLink1.getGraphicViewerObject() instanceof GraphicViewerLink) {
                graphicViewerLink = (GraphicViewerLink) graphicViewerNetworkLink1
                        .getGraphicViewerObject();
            }
            if (linkData(graphicViewerNetworkLink1).valid) {
                continue;
            }
            int j1;
            int k1;
            int l1;
            int i1 = j1 = k1 = l1 = 0;
            if (graphicViewerNetworkLink1.getGraphicViewerObject() != null) {
                Point point = graphicViewerNetworkLink1.getFromNode()
                        .getCenter();
                Point point1 = graphicViewerNetworkLink1.getToNode()
                        .getCenter();
                Point point2;
                Point point3;
                if (!linkData(graphicViewerNetworkLink1).rev) {
                    point2 = graphicViewerLink.getFromPort().getFromLinkPoint();
                    point3 = graphicViewerLink.getToPort().getToLinkPoint();
                } else {
                    point2 = graphicViewerLink.getToPort().getToLinkPoint();
                    point3 = graphicViewerLink.getFromPort().getFromLinkPoint();
                }
                switch (directionOption) {
                case 0: // '\0'
                case 1: // '\001'
                    i1 = (int) Math.round((double) (point2.x - point.x)
                            / (double) columnSpacing);
                    k1 = point2.x;
                    j1 = (int) Math.round((double) (point3.x - point1.x)
                            / (double) columnSpacing);
                    l1 = point3.x;
                    break;

                case 2: // '\002'
                case 3: // '\003'
                    i1 = (int) Math.round((double) (point2.y - point.y)
                            / (double) columnSpacing);
                    k1 = point2.y;
                    j1 = (int) Math.round((double) (point3.y - point1.y)
                            / (double) columnSpacing);
                    l1 = point3.y;
                    break;

                default:
                    i1 = (int) Math.round((double) (point2.x - point.x)
                            / (double) columnSpacing);
                    k1 = point2.x;
                    j1 = (int) Math.round((double) (point3.x - point1.x)
                            / (double) columnSpacing);
                    l1 = point3.x;
                    break;
                }
                linkData(graphicViewerNetworkLink1).portFromColOffset = i1;
                linkData(graphicViewerNetworkLink1).portFromPos = k1;
                linkData(graphicViewerNetworkLink1).portToColOffset = j1;
                linkData(graphicViewerNetworkLink1).portToPos = l1;
            } else {
                linkData(graphicViewerNetworkLink1).portFromColOffset = 0;
                linkData(graphicViewerNetworkLink1).portFromPos = 0;
                linkData(graphicViewerNetworkLink1).portToColOffset = 0;
                linkData(graphicViewerNetworkLink1).portToPos = 0;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink1
                    .getFromNode();
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink1
                    .getToNode();
            int i2 = nodeData(graphicViewerNetworkNode).layer;
            int j2 = nodeData(graphicViewerNetworkNode1).layer;
            boolean flag = graphicViewerLink != null
                    && (graphicViewerLink.isOrthogonal() || graphicViewerLink
                            .isCubic());
            int k2 = !flag || !linkData(graphicViewerNetworkLink1).rev ? 1 : 0;
            if (i2 - j2 > k2 && i2 > 0) {
                linkData(graphicViewerNetworkLink1).valid = false;
                GraphicViewerNetworkNode graphicViewerNetworkNode2 = new GraphicViewerNetworkNode(
                        getNetwork(), null);
                graphicViewerNetworkNode2.nodeData = new GraphicViewerLayeredDigraphAutoLayoutNodeData();
                nodeData(graphicViewerNetworkNode2).layer = i2 - 1;
                getNetwork().addNode(graphicViewerNetworkNode2);
                GraphicViewerNetworkLink graphicViewerNetworkLink2 = getNetwork()
                        .linkNodes(
                                graphicViewerNetworkNode,
                                graphicViewerNetworkNode2,
                                graphicViewerNetworkLink1
                                        .getGraphicViewerObject());
                graphicViewerNetworkLink2.linkData = new GraphicViewerLayeredDigraphAutoLayoutLinkData();
                linkData(graphicViewerNetworkLink2).valid = true;
                linkData(graphicViewerNetworkLink2).rev = linkData(graphicViewerNetworkLink1).rev;
                linkData(graphicViewerNetworkLink2).portFromColOffset = i1;
                linkData(graphicViewerNetworkLink2).portToColOffset = 0;
                linkData(graphicViewerNetworkLink2).portFromPos = k1;
                linkData(graphicViewerNetworkLink2).portToPos = 0;
                graphicViewerNetworkNode = graphicViewerNetworkNode2;
                for (i2--; i2 - j2 > k2 && i2 > 0; i2--) {
                    graphicViewerNetworkNode2 = new GraphicViewerNetworkNode(
                            getNetwork(), null);
                    graphicViewerNetworkNode2.nodeData = new GraphicViewerLayeredDigraphAutoLayoutNodeData();
                    nodeData(graphicViewerNetworkNode2).layer = i2 - 1;
                    getNetwork().addNode(graphicViewerNetworkNode2);
                    graphicViewerNetworkLink2 = getNetwork().linkNodes(
                            graphicViewerNetworkNode,
                            graphicViewerNetworkNode2,
                            graphicViewerNetworkLink1.getGraphicViewerObject());
                    graphicViewerNetworkLink2.linkData = new GraphicViewerLayeredDigraphAutoLayoutLinkData();
                    linkData(graphicViewerNetworkLink2).valid = true;
                    linkData(graphicViewerNetworkLink2).rev = linkData(graphicViewerNetworkLink1).rev;
                    linkData(graphicViewerNetworkLink2).portFromColOffset = 0;
                    linkData(graphicViewerNetworkLink2).portToColOffset = 0;
                    linkData(graphicViewerNetworkLink2).portFromPos = 0;
                    linkData(graphicViewerNetworkLink2).portToPos = 0;
                    graphicViewerNetworkNode = graphicViewerNetworkNode2;
                }

                graphicViewerNetworkLink2 = getNetwork().linkNodes(
                        graphicViewerNetworkNode2, graphicViewerNetworkNode1,
                        graphicViewerNetworkLink1.getGraphicViewerObject());
                graphicViewerNetworkLink2.linkData = new GraphicViewerLayeredDigraphAutoLayoutLinkData();
                linkData(graphicViewerNetworkLink2).valid = true;
                linkData(graphicViewerNetworkLink2).rev = linkData(graphicViewerNetworkLink1).rev;
                linkData(graphicViewerNetworkLink2).portFromColOffset = 0;
                linkData(graphicViewerNetworkLink2).portToColOffset = j1;
                linkData(graphicViewerNetworkLink2).portFromPos = 0;
                linkData(graphicViewerNetworkLink2).portToPos = l1;
            } else {
                linkData(graphicViewerNetworkLink1).valid = true;
            }
        }

    }

    void initializeIndicesInternal() {
        indices = new int[maxLayer + 1];
        for (int k = 0; k <= maxLayer; k++) {
            indices[k] = 0;
        }

        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i1];
            nodeData(graphicViewerNetworkNode).index = -1;
        }

        initializeIndices();
        maxIndex = -1;
        minIndexLayer = 0;
        maxIndexLayer = 0;
        for (int l = 0; l <= maxLayer; l++) {
            if (indices[l] > indices[maxIndexLayer]) {
                maxIndex = indices[l] - 1;
                maxIndexLayer = l;
            }
            if (indices[l] < indices[minIndexLayer]) {
                minIndexLayer = l;
            }
        }

    }

    protected void initializeIndices() {
        switch (initializeOption) {
        case 0: // '\0'
            naiveInitializeIndices();
            break;

        case 1: // '\001'
            depthFirstOutInitializeIndices();
            break;

        case 2: // '\002'
            depthFirstInInitializeIndices();
            break;

        default:
            naiveInitializeIndices();
            break;
        }
    }

    protected void naiveInitializeIndices() {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l];
            int k = nodeData(graphicViewerNetworkNode).layer;
            nodeData(graphicViewerNetworkNode).index = indices[k];
            indices[k]++;
        }

    }

    protected void depthFirstOutInitializeIndices() {
        for (int k = maxLayer; k >= 0; k--) {
            GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                    .getNodeArray();
            for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l];
                if (nodeData(graphicViewerNetworkNode).layer == k
                        && nodeData(graphicViewerNetworkNode).index == -1) {
                    depthFirstOutInitializeIndicesVisit(graphicViewerNetworkNode);
                }
            }
        }
    }

    protected void depthFirstOutInitializeIndicesVisit(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        int k = nodeData(graphicViewerNetworkNode).layer;
        nodeData(graphicViewerNetworkNode).index = indices[k];
        indices[k]++;
        LinkedList linkedlist = graphicViewerNetworkNode.getSuccLinksList();
        Object aobj[] = linkedlist.toArray();
        for (boolean flag = true; flag;) {
            flag = false;
            int l = 0;
            while (l < aobj.length - 1) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = (GraphicViewerNetworkLink) aobj[l];
                GraphicViewerNetworkLink graphicViewerNetworkLink2 = (GraphicViewerNetworkLink) aobj[l + 1];
                if (linkData(graphicViewerNetworkLink).portFromColOffset > linkData(graphicViewerNetworkLink2).portFromColOffset) {
                    flag = true;
                    aobj[l] = graphicViewerNetworkLink2;
                    aobj[l + 1] = graphicViewerNetworkLink;
                }
                l++;
            }
        }

        for (int i1 = 0; i1 < aobj.length; i1++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink1 = (GraphicViewerNetworkLink) aobj[i1];
            if (!linkData(graphicViewerNetworkLink1).valid) {
                continue;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink1
                    .getToNode();
            if (nodeData(graphicViewerNetworkNode1).index == -1) {
                depthFirstOutInitializeIndicesVisit(graphicViewerNetworkNode1);
            }
        }

    }

    protected void depthFirstInInitializeIndices() {
        for (int k = 0; k <= maxLayer; k++) {
            GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                    .getNodeArray();
            for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l];
                if (nodeData(graphicViewerNetworkNode).layer == k
                        && nodeData(graphicViewerNetworkNode).index == -1) {
                    depthFirstInInitializeIndicesVisit(graphicViewerNetworkNode);
                }
            }

        }

    }

    protected void depthFirstInInitializeIndicesVisit(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        int k = nodeData(graphicViewerNetworkNode).layer;
        nodeData(graphicViewerNetworkNode).index = indices[k];
        indices[k]++;
        LinkedList linkedlist = graphicViewerNetworkNode.getPredLinksList();
        Object aobj[] = linkedlist.toArray();
        for (boolean flag = true; flag;) {
            flag = false;
            int l = 0;
            while (l < aobj.length - 1) {
                GraphicViewerNetworkLink graphicViewerNetworkLink = (GraphicViewerNetworkLink) aobj[l];
                GraphicViewerNetworkLink graphicViewerNetworkLink2 = (GraphicViewerNetworkLink) aobj[l + 1];
                if (linkData(graphicViewerNetworkLink).portToColOffset > linkData(graphicViewerNetworkLink2).portToColOffset) {
                    flag = true;
                    aobj[l] = graphicViewerNetworkLink2;
                    aobj[l + 1] = graphicViewerNetworkLink;
                }
                l++;
            }
        }

        for (int i1 = 0; i1 < aobj.length; i1++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink1 = (GraphicViewerNetworkLink) aobj[i1];
            if (!linkData(graphicViewerNetworkLink1).valid) {
                continue;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink1
                    .getFromNode();
            if (nodeData(graphicViewerNetworkNode1).index == -1) {
                depthFirstInInitializeIndicesVisit(graphicViewerNetworkNode1);
            }
        }

    }

    protected void initializeColumns() {
        maxColumn = -1;
        for (int k = 0; k <= maxLayer; k++) {
            GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
            int l = 0;
            for (int i1 = 0; i1 < indices[k]; i1++) {
                int j1 = getNodeMinColumnSpace(aGraphicViewerNetworkNode[i1]);
                l += j1;
                nodeData(aGraphicViewerNetworkNode[i1]).column = l;
                l = ++l + j1;
            }

            maxColumn = Math.max(maxColumn, l - 1);
            FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        }

    }

    protected void reduceCrossings() {
        int k = countCrossings();
        int ai[] = saveLayout();
        for (int l = 0; l < iterations; l++) {
            for (int j1 = 0; j1 <= maxLayer; j1++) {
                medianBarycenterCrossingReduction(j1, 1);
                adjacentExchangeCrossingReductionBendStraighten(j1, 1, false, 1);
            }

            int j5 = countCrossings();
            if (j5 < k) {
                k = j5;
                ai = saveLayout();
            }
            for (int k1 = maxLayer; k1 >= 0; k1--) {
                medianBarycenterCrossingReduction(k1, -1);
                adjacentExchangeCrossingReductionBendStraighten(k1, -1, false,
                        -1);
            }

            j5 = countCrossings();
            if (j5 < k) {
                k = j5;
                ai = saveLayout();
            }
        }

        restoreLayout(ai);
        for (int i1 = 0; i1 < iterations; i1++) {
            for (int l1 = 0; l1 <= maxLayer; l1++) {
                medianBarycenterCrossingReduction(l1, 0);
                adjacentExchangeCrossingReductionBendStraighten(l1, 0, false, 0);
            }

            int k5 = countCrossings();
            if (k5 < k) {
                k = k5;
                ai = saveLayout();
            }
            for (int i2 = maxLayer; i2 >= 0; i2--) {
                medianBarycenterCrossingReduction(i2, 0);
                adjacentExchangeCrossingReductionBendStraighten(i2, 0, false, 0);
            }

            k5 = countCrossings();
            if (k5 < k) {
                k = k5;
                ai = saveLayout();
            }
        }

        restoreLayout(ai);
        label0: switch (aggressiveOption) {
        default:
            break;

        case 0: // '\0'
            for (int j7 = k + 1; countCrossings() < j7;) {
                j7 = countCrossings();
                int j6 = maxLayer;
                while (j6 >= 0) {
                    for (int l6 = 0; l6 <= j6; l6++) {
                        for (boolean flag = true; flag;) {
                            flag = false;
                            int j2 = j6;
                            while (j2 >= l6) {
                                flag = adjacentExchangeCrossingReductionBendStraighten(
                                        j2, -1, false, -1) || flag;
                                j2--;
                            }
                        }

                        int l5 = countCrossings();
                        if (l5 >= k) {
                            restoreLayout(ai);
                        } else {
                            k = l5;
                            ai = saveLayout();
                        }
                        for (boolean flag1 = true; flag1;) {
                            flag1 = false;
                            int k2 = j6;
                            while (k2 >= l6) {
                                flag1 = adjacentExchangeCrossingReductionBendStraighten(
                                        k2, 1, false, 1) || flag1;
                                k2--;
                            }
                        }

                        l5 = countCrossings();
                        if (l5 >= k) {
                            restoreLayout(ai);
                        } else {
                            k = l5;
                            ai = saveLayout();
                        }
                        for (boolean flag2 = true; flag2;) {
                            flag2 = false;
                            int l2 = l6;
                            while (l2 <= j6) {
                                flag2 = adjacentExchangeCrossingReductionBendStraighten(
                                        l2, 1, false, 1) || flag2;
                                l2++;
                            }
                        }

                        if (l5 >= k) {
                            restoreLayout(ai);
                        } else {
                            k = l5;
                            ai = saveLayout();
                        }
                        for (boolean flag3 = true; flag3;) {
                            flag3 = false;
                            int i3 = l6;
                            while (i3 <= j6) {
                                flag3 = adjacentExchangeCrossingReductionBendStraighten(
                                        i3, -1, false, -1) || flag3;
                                i3++;
                            }
                        }

                        if (l5 >= k) {
                            restoreLayout(ai);
                        } else {
                            k = l5;
                            ai = saveLayout();
                        }
                        for (boolean flag4 = true; flag4;) {
                            flag4 = false;
                            int j3 = j6;
                            while (j3 >= l6) {
                                flag4 = adjacentExchangeCrossingReductionBendStraighten(
                                        j3, 0, false, 0) || flag4;
                                j3--;
                            }
                        }

                        if (l5 >= k) {
                            restoreLayout(ai);
                        } else {
                            k = l5;
                            ai = saveLayout();
                        }
                        for (boolean flag5 = true; flag5;) {
                            flag5 = false;
                            int k3 = l6;
                            while (k3 <= j6) {
                                flag5 = adjacentExchangeCrossingReductionBendStraighten(
                                        k3, 0, false, 0) || flag5;
                                k3++;
                            }
                        }

                        if (l5 >= k) {
                            restoreLayout(ai);
                        } else {
                            k = l5;
                            ai = saveLayout();
                        }
                    }

                    j6--;
                }
            }

            break;

        case 1: // '\001'
            int k6 = maxLayer;
            int i7 = 0;
            int k7 = k + 1;
            do {
                if (countCrossings() >= k7) {
                    break label0;
                }
                k7 = countCrossings();
                for (boolean flag6 = true; flag6;) {
                    flag6 = false;
                    int l3 = k6;
                    while (l3 >= i7) {
                        flag6 = adjacentExchangeCrossingReductionBendStraighten(
                                l3, -1, false, -1) || flag6;
                        l3--;
                    }
                }

                int i6 = countCrossings();
                if (i6 >= k) {
                    restoreLayout(ai);
                } else {
                    k = i6;
                    ai = saveLayout();
                }
                for (boolean flag7 = true; flag7;) {
                    flag7 = false;
                    int i4 = k6;
                    while (i4 >= i7) {
                        flag7 = adjacentExchangeCrossingReductionBendStraighten(
                                i4, 1, false, 1) || flag7;
                        i4--;
                    }
                }

                i6 = countCrossings();
                if (i6 >= k) {
                    restoreLayout(ai);
                } else {
                    k = i6;
                    ai = saveLayout();
                }
                for (boolean flag8 = true; flag8;) {
                    flag8 = false;
                    int j4 = i7;
                    while (j4 <= k6) {
                        flag8 = adjacentExchangeCrossingReductionBendStraighten(
                                j4, 1, false, 1) || flag8;
                        j4++;
                    }
                }

                if (i6 >= k) {
                    restoreLayout(ai);
                } else {
                    k = i6;
                    ai = saveLayout();
                }
                for (boolean flag9 = true; flag9;) {
                    flag9 = false;
                    int k4 = i7;
                    while (k4 <= k6) {
                        flag9 = adjacentExchangeCrossingReductionBendStraighten(
                                k4, -1, false, -1) || flag9;
                        k4++;
                    }
                }

                if (i6 >= k) {
                    restoreLayout(ai);
                } else {
                    k = i6;
                    ai = saveLayout();
                }
                for (boolean flag10 = true; flag10;) {
                    flag10 = false;
                    int l4 = k6;
                    while (l4 >= i7) {
                        flag10 = adjacentExchangeCrossingReductionBendStraighten(
                                l4, 0, false, 0) || flag10;
                        l4--;
                    }
                }

                if (i6 >= k) {
                    restoreLayout(ai);
                } else {
                    k = i6;
                    ai = saveLayout();
                }
                for (boolean flag11 = true; flag11;) {
                    flag11 = false;
                    int i5 = i7;
                    while (i5 <= k6) {
                        flag11 = adjacentExchangeCrossingReductionBendStraighten(
                                i5, 0, false, 0) || flag11;
                        i5++;
                    }
                }

                if (i6 >= k) {
                    restoreLayout(ai);
                } else {
                    k = i6;
                    ai = saveLayout();
                }
            } while (true);
        }
        restoreLayout(ai);
    }

    protected boolean medianBarycenterCrossingReduction(int k, int l) {
        boolean flag = false;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        double ad[] = medians(k, l);
        double ad1[] = barycenters(k, l);
        for (int i1 = 0; i1 < indices[k]; i1++) {
            if (ad1[i1] == -1D) {
                ad1[i1] = nodeData(aGraphicViewerNetworkNode[i1]).column;
            }
            if (ad[i1] == -1D) {
                ad[i1] = nodeData(aGraphicViewerNetworkNode[i1]).column;
            }
        }

        for (boolean flag1 = true; flag1;) {
            flag1 = false;
            int j1 = 0;
            while (j1 < indices[k] - 1) {
                if (ad[j1 + 1] < ad[j1] || ad[j1 + 1] == ad[j1]
                        && ad1[j1 + 1] < ad1[j1]) {
                    flag = true;
                    flag1 = true;
                    double d1 = ad[j1];
                    ad[j1] = ad[j1 + 1];
                    ad[j1 + 1] = d1;
                    double d2 = ad1[j1];
                    ad1[j1] = ad1[j1 + 1];
                    ad1[j1 + 1] = d2;
                    GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[j1];
                    aGraphicViewerNetworkNode[j1] = aGraphicViewerNetworkNode[j1 + 1];
                    aGraphicViewerNetworkNode[j1 + 1] = graphicViewerNetworkNode;
                }
                j1++;
            }
        }

        for (int k1 = 0; k1 < indices[k]; k1++) {
            nodeData(aGraphicViewerNetworkNode[k1]).index = k1;
        }

        int i2 = 0;
        for (int l1 = 0; l1 < indices[k]; l1++) {
            int j2 = getNodeMinColumnSpace(aGraphicViewerNetworkNode[l1]);
            i2 += j2;
            nodeData(aGraphicViewerNetworkNode[l1]).column = i2;
            i2 = ++i2 + j2;
        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        return flag;
    }

    protected boolean adjacentExchangeCrossingReductionBendStraighten(int k,
            int l, boolean flag, int i1) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        int ai[] = crossingMatrix(k, l);
        double ad[] = barycenters(k, -1);
        if (!flag || i1 > 0) {
            for (int j1 = 0; j1 < indices[k]; j1++) {
                ad[j1] = -1D;
            }

        }
        double ad1[] = barycenters(k, 1);
        if (!flag || i1 < 0) {
            for (int k1 = 0; k1 < indices[k]; k1++) {
                ad1[k1] = -1D;
            }

        }
        boolean flag1 = false;
        for (boolean flag2 = true; flag2;) {
            flag2 = false;
            int l1 = 0;
            while (l1 < indices[k] - 1) {
                int j2 = ai[nodeData(aGraphicViewerNetworkNode[l1]).index
                        * indices[k]
                        + nodeData(aGraphicViewerNetworkNode[l1 + 1]).index];
                int k2 = ai[nodeData(aGraphicViewerNetworkNode[l1 + 1]).index
                        * indices[k]
                        + nodeData(aGraphicViewerNetworkNode[l1]).index];
                int l2 = 0;
                int i3 = 0;
                int j3 = nodeData(aGraphicViewerNetworkNode[l1]).column;
                int k3 = nodeData(aGraphicViewerNetworkNode[l1 + 1]).column;
                int l3 = getNodeMinColumnSpace(aGraphicViewerNetworkNode[l1]);
                int i4 = getNodeMinColumnSpace(aGraphicViewerNetworkNode[l1 + 1]);
                int j4 = (j3 - l3) + i4;
                int k4 = (k3 - l3) + i4;
                double d1 = 0.0D;
                double d2 = 0.0D;
                if (flag && (i1 < 0 || i1 == 0)) {
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = aGraphicViewerNetworkNode[l1]
                            .getPredLinksArray();
                    for (int l4 = 0; l4 < aGraphicViewerNetworkLink.length; l4++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l4];
                        if (linkData(graphicViewerNetworkLink).valid
                                && nodeData(graphicViewerNetworkLink
                                        .getFromNode()).layer != k) {
                            double d5 = getLinkStraightenWeight(graphicViewerNetworkLink);
                            int l7 = linkData(graphicViewerNetworkLink).portFromColOffset;
                            int l8 = linkData(graphicViewerNetworkLink).portToColOffset;
                            int l9 = nodeData(graphicViewerNetworkLink
                                    .getFromNode()).column;
                            d1 += (double) (Math.abs((j3 + l8) - (l9 + l7)) + 1)
                                    * d5;
                            d2 += (double) (Math.abs((k4 + l8) - (l9 + l7)) + 1)
                                    * d5;
                        }
                    }

                }
                GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = aGraphicViewerNetworkNode[l1]
                        .getPredLinksArray();
                for (int i5 = 0; i5 < aGraphicViewerNetworkLink1.length; i5++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[i5];
                    if (!linkData(graphicViewerNetworkLink1).valid
                            || nodeData(graphicViewerNetworkLink1.getFromNode()).layer != k) {
                        continue;
                    }
                    GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink1
                            .getFromNode();
                    int l6;
                    for (l6 = 0; aGraphicViewerNetworkNode[l6] != graphicViewerNetworkNode; l6++) {
                        ;
                    }
                    if (l6 < l1) {
                        l2 += 2 * (l1 - l6);
                        i3 += 2 * ((l1 + 1) - l6);
                    }
                    if (l6 == l1 + 1) {
                        l2++;
                    }
                    if (l6 > l1 + 1) {
                        l2 += 4 * (l6 - l1);
                        i3 += 4 * (l6 - (l1 + 1));
                    }
                }

                if (flag && (i1 > 0 || i1 == 0)) {
                    aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[l1]
                            .getSuccLinksArray();
                    for (int j5 = 0; j5 < aGraphicViewerNetworkLink1.length; j5++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink2 = aGraphicViewerNetworkLink1[j5];
                        if (linkData(graphicViewerNetworkLink2).valid
                                && nodeData(graphicViewerNetworkLink2
                                        .getToNode()).layer != k) {
                            double d6 = getLinkStraightenWeight(graphicViewerNetworkLink2);
                            int i8 = linkData(graphicViewerNetworkLink2).portFromColOffset;
                            int i9 = linkData(graphicViewerNetworkLink2).portToColOffset;
                            int i10 = nodeData(graphicViewerNetworkLink2
                                    .getToNode()).column;
                            d1 += (double) (Math.abs((j3 + i8) - (i10 + i9)) + 1)
                                    * d6;
                            d2 += (double) (Math.abs((k4 + i8) - (i10 + i9)) + 1)
                                    * d6;
                        }
                    }

                }
                aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[l1]
                        .getSuccLinksArray();
                for (int k5 = 0; k5 < aGraphicViewerNetworkLink1.length; k5++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink3 = aGraphicViewerNetworkLink1[k5];
                    if (!linkData(graphicViewerNetworkLink3).valid
                            || nodeData(graphicViewerNetworkLink3.getToNode()).layer != k) {
                        continue;
                    }
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink3
                            .getToNode();
                    int i7;
                    for (i7 = 0; aGraphicViewerNetworkNode[i7] != graphicViewerNetworkNode1; i7++) {
                        ;
                    }
                    if (i7 == l1 + 1) {
                        i3++;
                    }
                }

                if (flag && (i1 < 0 || i1 == 0)) {
                    aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[l1 + 1]
                            .getPredLinksArray();
                    for (int l5 = 0; l5 < aGraphicViewerNetworkLink1.length; l5++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink4 = aGraphicViewerNetworkLink1[l5];
                        if (linkData(graphicViewerNetworkLink4).valid
                                && nodeData(graphicViewerNetworkLink4
                                        .getFromNode()).layer != k) {
                            double d7 = getLinkStraightenWeight(graphicViewerNetworkLink4);
                            int j8 = linkData(graphicViewerNetworkLink4).portFromColOffset;
                            int j9 = linkData(graphicViewerNetworkLink4).portToColOffset;
                            int j10 = nodeData(graphicViewerNetworkLink4
                                    .getFromNode()).column;
                            d1 += (double) (Math.abs((k3 + j9) - (j10 + j8)) + 1)
                                    * d7;
                            d2 += (double) (Math.abs((j4 + j9) - (j10 + j8)) + 1)
                                    * d7;
                        }
                    }

                }
                aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[l1 + 1]
                        .getPredLinksArray();
                for (int i6 = 0; i6 < aGraphicViewerNetworkLink1.length; i6++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink5 = aGraphicViewerNetworkLink1[i6];
                    if (!linkData(graphicViewerNetworkLink5).valid
                            || nodeData(graphicViewerNetworkLink5.getFromNode()).layer != k) {
                        continue;
                    }
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = graphicViewerNetworkLink5
                            .getFromNode();
                    int j7;
                    for (j7 = 0; aGraphicViewerNetworkNode[j7] != graphicViewerNetworkNode2; j7++) {
                        ;
                    }
                    if (j7 < l1) {
                        l2 += 2 * ((l1 + 1) - j7);
                        i3 += 2 * (l1 - j7);
                    }
                    if (j7 == l1) {
                        i3++;
                    }
                    if (j7 > l1 + 1) {
                        l2 += 4 * (j7 - (l1 + 1));
                        i3 += 4 * (j7 - l1);
                    }
                }

                if (flag && (i1 > 0 || i1 == 0)) {
                    aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[l1 + 1]
                            .getSuccLinksArray();
                    for (int j6 = 0; j6 < aGraphicViewerNetworkLink1.length; j6++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink6 = aGraphicViewerNetworkLink1[j6];
                        if (linkData(graphicViewerNetworkLink6).valid
                                && nodeData(graphicViewerNetworkLink6
                                        .getToNode()).layer != k) {
                            double d8 = getLinkStraightenWeight(graphicViewerNetworkLink6);
                            int k8 = linkData(graphicViewerNetworkLink6).portFromColOffset;
                            int k9 = linkData(graphicViewerNetworkLink6).portToColOffset;
                            int k10 = nodeData(graphicViewerNetworkLink6
                                    .getToNode()).column;
                            d1 += (double) (Math.abs((k3 + k8) - (k10 + k9)) + 1)
                                    * d8;
                            d2 += (double) (Math.abs((j4 + k8) - (k10 + k9)) + 1)
                                    * d8;
                        }
                    }

                }
                aGraphicViewerNetworkLink1 = aGraphicViewerNetworkNode[l1 + 1]
                        .getSuccLinksArray();
                for (int k6 = 0; k6 < aGraphicViewerNetworkLink1.length; k6++) {
                    GraphicViewerNetworkLink graphicViewerNetworkLink7 = aGraphicViewerNetworkLink1[k6];
                    if (!linkData(graphicViewerNetworkLink7).valid
                            || nodeData(graphicViewerNetworkLink7.getToNode()).layer != k) {
                        continue;
                    }
                    GraphicViewerNetworkNode graphicViewerNetworkNode3 = graphicViewerNetworkLink7
                            .getToNode();
                    int k7;
                    for (k7 = 0; aGraphicViewerNetworkNode[k7] != graphicViewerNetworkNode3; k7++) {
                        ;
                    }
                    if (k7 == l1) {
                        l2++;
                    }
                }

                double d3 = 0.0D;
                double d4 = 0.0D;
                double d9 = ad[nodeData(aGraphicViewerNetworkNode[l1]).index];
                double d10 = ad1[nodeData(aGraphicViewerNetworkNode[l1]).index];
                double d11 = ad[nodeData(aGraphicViewerNetworkNode[l1 + 1]).index];
                double d12 = ad1[nodeData(aGraphicViewerNetworkNode[l1 + 1]).index];
                if (d9 != -1D) {
                    d3 += Math.abs(d9 - (double) j3);
                    d4 += Math.abs(d9 - (double) k4);
                }
                if (d10 != -1D) {
                    d3 += Math.abs(d10 - (double) j3);
                    d4 += Math.abs(d10 - (double) k4);
                }
                if (d11 != -1D) {
                    d3 += Math.abs(d11 - (double) k3);
                    d4 += Math.abs(d11 - (double) j4);
                }
                if (d12 != -1D) {
                    d3 += Math.abs(d12 - (double) k3);
                    d4 += Math.abs(d12 - (double) j4);
                }
                if (i3 < l2 || i3 == l2 && k2 < j2 || i3 == l2 && k2 == j2
                        && d2 < d1 || i3 == l2 && k2 == j2 && d2 == d1
                        && d4 < d3) {
                    flag1 = true;
                    flag2 = true;
                    nodeData(aGraphicViewerNetworkNode[l1]).column = k4;
                    nodeData(aGraphicViewerNetworkNode[l1 + 1]).column = j4;
                    GraphicViewerNetworkNode graphicViewerNetworkNode4 = aGraphicViewerNetworkNode[l1];
                    aGraphicViewerNetworkNode[l1] = aGraphicViewerNetworkNode[l1 + 1];
                    aGraphicViewerNetworkNode[l1 + 1] = graphicViewerNetworkNode4;
                }
                l1++;
            }
        }

        for (int i2 = 0; i2 < indices[k]; i2++) {
            nodeData(aGraphicViewerNetworkNode[i2]).index = i2;
        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        return flag1;
    }

    protected void straightenAndPack() {
        boolean flag2 = (getPackOption() & 1) != 0;
        boolean flag3 = (getPackOption() & 8) != 0;
        if (getNetwork().getLinkCount() > 1000 && !flag3) {
            flag2 = false;
        }
        if (flag2) {
            int ai[] = new int[maxLayer + 1];
            for (int k2 = 0; k2 <= maxLayer; k2++) {
                ai[k2] = 0;
            }

            GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                    .getNodeArray();
            for (int l2 = 0; l2 < aGraphicViewerNetworkNode.length; l2++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l2];
                int k = nodeData(graphicViewerNetworkNode).layer;
                int j3 = nodeData(graphicViewerNetworkNode).column;
                int k3 = getNodeMinColumnSpace(graphicViewerNetworkNode);
                ai[k] = Math.max(ai[k], j3 + k3);
            }

            for (int i3 = 0; i3 < aGraphicViewerNetworkNode.length; i3++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[i3];
                int l = nodeData(graphicViewerNetworkNode1).layer;
                int l3 = nodeData(graphicViewerNetworkNode1).column;
                nodeData(graphicViewerNetworkNode1).column = ((maxColumn - ai[l]) * 8)
                        / 2 + l3 * 8;
            }

            maxColumn *= 8;
        }
        if ((getPackOption() & 2) != 0) {
            for (boolean flag = true; flag; flag = bendStraighten(
                    maxIndexLayer, 0) || flag) {
                flag = false;
                for (int i1 = maxIndexLayer + 1; i1 <= maxLayer; i1++) {
                    flag = bendStraighten(i1, 1) || flag;
                }

                for (int j1 = maxIndexLayer - 1; j1 >= 0; j1--) {
                    flag = bendStraighten(j1, -1) || flag;
                }

            }

        }
        if ((getPackOption() & 4) != 0) {
            for (int k1 = maxIndexLayer + 1; k1 <= maxLayer; k1++) {
                medianStraighten(k1, 1);
            }

            for (int l1 = maxIndexLayer - 1; l1 >= 0; l1--) {
                medianStraighten(l1, -1);
            }

            medianStraighten(maxIndexLayer, 0);
        }
        if (flag2) {
            componentPack(-1);
            componentPack(1);
        }
        if ((getPackOption() & 2) != 0) {
            for (boolean flag1 = true; flag1;) {
                flag1 = false;
                flag1 = bendStraighten(maxIndexLayer, 0) || flag1;
                for (int i2 = maxIndexLayer + 1; i2 <= maxLayer; i2++) {
                    flag1 = bendStraighten(i2, 0) || flag1;
                }

                int j2 = maxIndexLayer - 1;
                while (j2 >= 0) {
                    flag1 = bendStraighten(j2, 0) || flag1;
                    j2--;
                }
            }

        }
    }

    protected boolean bendStraighten(int k, int l) {
        boolean flag;
        for (flag = false; shiftBendStraighten(k, l)
                || adjacentExchangeCrossingReductionBendStraighten(k, 0, true,
                        l); flag = true) {
            ;
        }
        return flag;
    }

    protected boolean shiftBendStraighten(int k, int l) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        double ad[] = barycenters(k, -1);
        if (l > 0) {
            for (int i1 = 0; i1 < indices[k]; i1++) {
                ad[i1] = -1D;
            }

        }
        double ad1[] = barycenters(k, 1);
        if (l < 0) {
            for (int j1 = 0; j1 < indices[k]; j1++) {
                ad1[j1] = -1D;
            }

        }
        boolean flag = false;
        for (boolean flag1 = true; flag1;) {
            flag1 = false;
            int k1 = 0;
            while (k1 < indices[k]) {
                int l1 = nodeData(aGraphicViewerNetworkNode[k1]).column;
                int i2 = getNodeMinColumnSpace(aGraphicViewerNetworkNode[k1]);
                int j2;
                if (k1 - 1 < 0
                        || l1
                                - nodeData(aGraphicViewerNetworkNode[k1 - 1]).column
                                - 1 > i2
                                + getNodeMinColumnSpace(aGraphicViewerNetworkNode[k1 - 1])) {
                    j2 = l1 - 1;
                } else {
                    j2 = l1;
                }
                int k2;
                if (k1 + 1 >= indices[k]
                        || nodeData(aGraphicViewerNetworkNode[k1 + 1]).column
                                - l1 - 1 > i2
                                + getNodeMinColumnSpace(aGraphicViewerNetworkNode[k1 + 1])) {
                    k2 = l1 + 1;
                } else {
                    k2 = l1;
                }
                double d1 = 0.0D;
                double d2 = 0.0D;
                double d3 = 0.0D;
                if (l < 0 || l == 0) {
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = aGraphicViewerNetworkNode[k1]
                            .getPredLinksArray();
                    for (int l2 = 0; l2 < aGraphicViewerNetworkLink.length; l2++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[l2];
                        if (linkData(graphicViewerNetworkLink).valid
                                && nodeData(graphicViewerNetworkLink
                                        .getFromNode()).layer != k) {
                            double d6 = getLinkStraightenWeight(graphicViewerNetworkLink);
                            int j3 = linkData(graphicViewerNetworkLink).portFromColOffset;
                            int l3 = linkData(graphicViewerNetworkLink).portToColOffset;
                            int j4 = nodeData(graphicViewerNetworkLink
                                    .getFromNode()).column;
                            d1 += (double) (Math.abs((l1 + l3) - (j4 + j3)) + 1)
                                    * d6;
                            d2 += (double) (Math.abs((j2 + l3) - (j4 + j3)) + 1)
                                    * d6;
                            d3 += (double) (Math.abs((k2 + l3) - (j4 + j3)) + 1)
                                    * d6;
                        }
                    }

                }
                if (l > 0 || l == 0) {
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = aGraphicViewerNetworkNode[k1]
                            .getSuccLinksArray();
                    for (int i3 = 0; i3 < aGraphicViewerNetworkLink1.length; i3++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[i3];
                        if (linkData(graphicViewerNetworkLink1).valid
                                && nodeData(graphicViewerNetworkLink1
                                        .getToNode()).layer != k) {
                            double d7 = getLinkStraightenWeight(graphicViewerNetworkLink1);
                            int k3 = linkData(graphicViewerNetworkLink1).portFromColOffset;
                            int i4 = linkData(graphicViewerNetworkLink1).portToColOffset;
                            int k4 = nodeData(graphicViewerNetworkLink1
                                    .getToNode()).column;
                            d1 += (double) (Math.abs((l1 + k3) - (k4 + i4)) + 1)
                                    * d7;
                            d2 += (double) (Math.abs((j2 + k3) - (k4 + i4)) + 1)
                                    * d7;
                            d3 += (double) (Math.abs((k2 + k3) - (k4 + i4)) + 1)
                                    * d7;
                        }
                    }

                }
                double d4 = 0.0D;
                double d5 = 0.0D;
                double d8 = 0.0D;
                double d9 = ad[nodeData(aGraphicViewerNetworkNode[k1]).index];
                double d10 = ad1[nodeData(aGraphicViewerNetworkNode[k1]).index];
                if (d9 != -1D) {
                    d4 += Math.abs(d9 - (double) l1);
                    d5 += Math.abs(d9 - (double) j2);
                    d8 += Math.abs(d9 - (double) k2);
                }
                if (d10 != -1D) {
                    d4 += Math.abs(d10 - (double) l1);
                    d5 += Math.abs(d10 - (double) j2);
                    d8 += Math.abs(d10 - (double) k2);
                }
                if (d2 < d1 || d2 == d1 && d5 < d4) {
                    flag = true;
                    flag1 = true;
                    nodeData(aGraphicViewerNetworkNode[k1]).column = j2;
                }
                if (d3 < d1 || d3 == d1 && d8 < d4) {
                    flag = true;
                    flag1 = true;
                    nodeData(aGraphicViewerNetworkNode[k1]).column = k2;
                }
                k1++;
            }
        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        normalize();
        return flag;
    }

    protected boolean medianStraighten(int k, int l) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = GetCachedNodeArray(k);
        double ad[] = medians(k, l);
        int ai[] = new int[indices[k]];
        for (int i1 = 0; i1 < indices[k]; i1++) {
            ai[i1] = (int) ad[i1];
        }

        boolean flag = false;
        for (boolean flag1 = true; flag1;) {
            flag1 = false;
            int j1 = 0;
            while (j1 < indices[k]) {
                int k1 = nodeData(aGraphicViewerNetworkNode[j1]).column;
                int l1 = getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1]);
                int i2 = 0;
                if (ai[j1] == -1) {
                    if (j1 == 0 && j1 == indices[k] - 1) {
                        i2 = k1;
                    } else if (j1 == 0) {
                        int j2 = nodeData(aGraphicViewerNetworkNode[j1 + 1]).column;
                        if (j2 - k1 == l1
                                + getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 + 1])) {
                            i2 = k1 - 1;
                        } else {
                            i2 = k1;
                        }
                    } else if (j1 == indices[k] - 1) {
                        int k2 = nodeData(aGraphicViewerNetworkNode[j1 - 1]).column;
                        if (k1 - k2 == l1
                                + getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 - 1])) {
                            i2 = k1 + 1;
                        } else {
                            i2 = k1;
                        }
                    } else {
                        int l2 = nodeData(aGraphicViewerNetworkNode[j1 - 1]).column;
                        int l3 = l2
                                + getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 - 1])
                                + l1 + 1;
                        int l4 = nodeData(aGraphicViewerNetworkNode[j1 + 1]).column;
                        int j5 = l4
                                - getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 + 1])
                                - l1 - 1;
                        i2 = (l3 + j5) / 2;
                    }
                } else if (j1 == 0 && j1 == indices[k] - 1) {
                    i2 = ai[j1];
                } else if (j1 == 0) {
                    int i3 = nodeData(aGraphicViewerNetworkNode[j1 + 1]).column;
                    int i4 = i3
                            - getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 + 1])
                            - l1 - 1;
                    i2 = Math.min(ai[j1], i4);
                } else if (j1 == indices[k] - 1) {
                    int j3 = nodeData(aGraphicViewerNetworkNode[j1 - 1]).column;
                    int j4 = j3
                            + getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 - 1])
                            + l1 + 1;
                    i2 = Math.max(ai[j1], j4);
                } else {
                    int k3 = nodeData(aGraphicViewerNetworkNode[j1 - 1]).column;
                    int k4 = k3
                            + getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 - 1])
                            + l1 + 1;
                    int i5 = nodeData(aGraphicViewerNetworkNode[j1 + 1]).column;
                    int k5 = i5
                            - getNodeMinColumnSpace(aGraphicViewerNetworkNode[j1 + 1])
                            - l1 - 1;
                    if (k4 < ai[j1] && ai[j1] < k5) {
                        i2 = ai[j1];
                    } else if (k4 >= ai[j1]) {
                        i2 = k4;
                    } else if (k5 <= ai[j1]) {
                        i2 = k5;
                    }
                }
                if (i2 != k1) {
                    flag = true;
                    flag1 = true;
                    nodeData(aGraphicViewerNetworkNode[j1]).column = i2;
                }
                j1++;
            }
        }

        FreeCachedNodeArray(k, aGraphicViewerNetworkNode);
        normalize();
        return flag;
    }

    protected void pack() {
        for (int k = 0; k <= maxColumn; k++) {
            while (packAux(k, 1)) {
                ;
            }
        }

        normalize();
    }

    protected boolean packAux(int k, int l) {
        boolean flag = true;
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i1];
            int l1 = getNodeMinColumnSpace(graphicViewerNetworkNode);
            if (nodeData(graphicViewerNetworkNode).column - l1 <= k
                    && nodeData(graphicViewerNetworkNode).column + l1 >= k) {
                flag = false;
            }
        }

        boolean flag1 = false;
        if (flag) {
            if (l > 0) {
                for (int j1 = 0; j1 < aGraphicViewerNetworkNode.length; j1++) {
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[j1];
                    if (nodeData(graphicViewerNetworkNode1).column > k) {
                        nodeData(graphicViewerNetworkNode1).column--;
                        flag1 = true;
                    }
                }

            }
            if (l < 0) {
                for (int k1 = 0; k1 < aGraphicViewerNetworkNode.length; k1++) {
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkNode[k1];
                    if (nodeData(graphicViewerNetworkNode2).column < k) {
                        nodeData(graphicViewerNetworkNode2).column++;
                        flag1 = true;
                    }
                }

            }
        }
        return flag1;
    }

    protected void tightPack() {
        pack();
        for (int k = 0; k < maxColumn; k++) {
            while (tightPackAux(k, 1)) {
                ;
            }
        }

        normalize();
    }

    protected boolean tightPackAux(int k, int l) {
        int i1 = k;
        if (l > 0) {
            i1 = k + 1;
        }
        if (l < 0) {
            i1 = k - 1;
        }
        boolean aflag[] = new boolean[maxLayer + 1];
        boolean aflag1[] = new boolean[maxLayer + 1];
        for (int j1 = 0; j1 <= maxLayer; j1++) {
            aflag[j1] = false;
            aflag1[j1] = false;
        }

        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int l1 = 0; l1 < aGraphicViewerNetworkNode.length; l1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[l1];
            int i2 = getNodeMinColumnSpace(graphicViewerNetworkNode);
            if (nodeData(graphicViewerNetworkNode).column - i2 <= k
                    && nodeData(graphicViewerNetworkNode).column + i2 >= k) {
                aflag[nodeData(graphicViewerNetworkNode).layer] = true;
            }
            if (nodeData(graphicViewerNetworkNode).column - i2 <= i1
                    && nodeData(graphicViewerNetworkNode).column + i2 >= i1) {
                aflag1[nodeData(graphicViewerNetworkNode).layer] = true;
            }
        }

        boolean flag = true;
        boolean flag1 = false;
        for (int k1 = 0; k1 <= maxLayer; k1++) {
            flag = flag && (!aflag[k1] || !aflag1[k1]);
        }

        if (flag) {
            if (l > 0) {
                for (int j2 = 0; j2 < aGraphicViewerNetworkNode.length; j2++) {
                    GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[j2];
                    if (nodeData(graphicViewerNetworkNode1).column > k) {
                        nodeData(graphicViewerNetworkNode1).column--;
                        flag1 = true;
                    }
                }

            }
            if (l < 0) {
                for (int k2 = 0; k2 < aGraphicViewerNetworkNode.length; k2++) {
                    GraphicViewerNetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkNode[k2];
                    if (nodeData(graphicViewerNetworkNode2).column < k) {
                        nodeData(graphicViewerNetworkNode2).column++;
                        flag1 = true;
                    }
                }

            }
        }
        return flag1;
    }

    protected void componentPack(int k) {
        tightPack();
        if (k > 0) {
            label0: for (int l = 0; l <= maxColumn; l++) {
                int ai[] = saveLayout();
                double d1 = countBends(true);
                double d3 = d1 + 1.0D;
                do {
                    if (d1 >= d3) {
                        continue label0;
                    }
                    d3 = d1;
                    componentPackAux(l, 1);
                    double d5 = countBends(true);
                    if (d5 > d1) {
                        restoreLayout(ai);
                    } else if (d5 < d1) {
                        d1 = d5;
                        ai = saveLayout();
                    }
                } while (true);
            }

        }
        if (k < 0) {
            label1: for (int i1 = maxColumn; i1 >= 0; i1--) {
                int ai1[] = saveLayout();
                double d2 = countBends(true);
                double d4 = d2 + 1.0D;
                do {
                    if (d2 >= d4) {
                        continue label1;
                    }
                    d4 = d2;
                    componentPackAux(i1, -1);
                    double d6 = countBends(true);
                    if (d6 > d2) {
                        restoreLayout(ai1);
                    } else if (d6 < d2) {
                        d2 = d6;
                        ai1 = saveLayout();
                    }
                } while (true);
            }

        }
        normalize();
    }

    protected boolean componentPackAux(int k, int l) {
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i1];
            nodeData(graphicViewerNetworkNode).component = -1;
        }

        int j1 = 0;
        if (l > 0) {
            for (int k1 = 0; k1 < aGraphicViewerNetworkNode.length; k1++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[k1];
                if (nodeData(graphicViewerNetworkNode1).column
                        - getNodeMinColumnSpace(graphicViewerNetworkNode1) <= k) {
                    nodeData(graphicViewerNetworkNode1).component = j1;
                }
            }

        }
        if (l < 0) {
            for (int l1 = 0; l1 < aGraphicViewerNetworkNode.length; l1++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkNode[l1];
                if (nodeData(graphicViewerNetworkNode2).column
                        + getNodeMinColumnSpace(graphicViewerNetworkNode2) >= k) {
                    nodeData(graphicViewerNetworkNode2).component = j1;
                }
            }

        }
        j1++;
        for (int i2 = 0; i2 < aGraphicViewerNetworkNode.length; i2++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode3 = aGraphicViewerNetworkNode[i2];
            if (nodeData(graphicViewerNetworkNode3).component == -1) {
                componentUnset(graphicViewerNetworkNode3, j1, -1, true, true);
                j1++;
            }
        }

        boolean aflag[] = new boolean[j1 * j1];
        for (int j2 = 0; j2 < j1 * j1; j2++) {
            aflag[j2] = false;
        }

        int ai[] = new int[(maxLayer + 1) * (maxColumn + 1)];
        for (int k2 = 0; k2 < (maxLayer + 1) * (maxColumn + 1); k2++) {
            ai[k2] = -1;
        }

        for (int j3 = 0; j3 < aGraphicViewerNetworkNode.length; j3++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode4 = aGraphicViewerNetworkNode[j3];
            int j4 = nodeData(graphicViewerNetworkNode4).layer;
            int k4 = getNodeMinColumnSpace(graphicViewerNetworkNode4);
            int i5 = Math.max(0, nodeData(graphicViewerNetworkNode4).column
                    - k4);
            int l5 = Math.min(maxColumn,
                    nodeData(graphicViewerNetworkNode4).column + k4);
            for (int i6 = i5; i6 <= l5; i6++) {
                ai[j4 * (maxColumn + 1) + i6] = nodeData(graphicViewerNetworkNode4).component;
            }

        }

        for (int k3 = 0; k3 <= maxLayer; k3++) {
            if (l > 0) {
                for (int l3 = 0; l3 < maxColumn; l3++) {
                    if (ai[k3 * (maxColumn + 1) + l3] != -1
                            && ai[k3 * (maxColumn + 1) + l3 + 1] != -1
                            && ai[k3 * (maxColumn + 1) + l3] != ai[k3
                                    * (maxColumn + 1) + l3 + 1]) {
                        aflag[ai[k3 * (maxColumn + 1) + l3] * j1
                                + ai[k3 * (maxColumn + 1) + l3 + 1]] = true;
                    }
                }

            }
            if (l >= 0) {
                continue;
            }
            for (int i4 = maxColumn; i4 > 0; i4--) {
                if (ai[k3 * (maxColumn + 1) + i4] != -1
                        && ai[(k3 * (maxColumn + 1) + i4) - 1] != -1
                        && ai[k3 * (maxColumn + 1) + i4] != ai[(k3
                                * (maxColumn + 1) + i4) - 1]) {
                    aflag[ai[k3 * (maxColumn + 1) + i4] * j1
                            + ai[(k3 * (maxColumn + 1) + i4) - 1]] = true;
                }
            }

        }

        boolean aflag1[] = new boolean[j1];
        for (int l2 = 0; l2 < j1; l2++) {
            aflag1[l2] = true;
        }

        LinkedList linkedlist = new LinkedList();
        linkedlist.addFirst(new Integer(0));
        boolean flag = false;
        do {
            if (flag) {
                break;
            }
            try {
                int l4 = ((Integer) linkedlist.removeLast()).intValue();
                if (aflag1[l4]) {
                    aflag1[l4] = false;
                    int i3 = 0;
                    while (i3 < j1) {
                        if (aflag[l4 * j1 + i3]) {
                            linkedlist.addFirst(new Integer(i3));
                        }
                        i3++;
                    }
                }
            } catch (NoSuchElementException nsee) {
                flag = true;
            }
        } while (true);
        boolean flag1 = false;
        if (l > 0) {
            for (int j5 = 0; j5 < aGraphicViewerNetworkNode.length; j5++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode5 = aGraphicViewerNetworkNode[j5];
                if (aflag1[nodeData(graphicViewerNetworkNode5).component]) {
                    nodeData(graphicViewerNetworkNode5).column--;
                    flag1 = true;
                }
            }

        }
        if (l < 0) {
            for (int k5 = 0; k5 < aGraphicViewerNetworkNode.length; k5++) {
                GraphicViewerNetworkNode graphicViewerNetworkNode6 = aGraphicViewerNetworkNode[k5];
                if (aflag1[nodeData(graphicViewerNetworkNode6).component]) {
                    nodeData(graphicViewerNetworkNode6).column++;
                    flag1 = true;
                }
            }

        }
        return flag1;
    }

    protected void layoutNodesAndLinks() {
        getDocument().fireForedate(218, 0, null);
        getDocument().setSuspendUpdates(true);
        layoutNodes();
        layoutLinks();
        getDocument().setSuspendUpdates(false);
        getDocument().fireUpdate(218, 0, null, 0, null);
    }

    protected void layoutNodes() {
        int ai[] = new int[maxLayer + 1];
        for (int k = 0; k <= maxLayer; k++) {
            ai[k] = 0;
        }

        GraphicViewerNetworkNode aGraphicViewerNetworkNode[] = getNetwork()
                .getNodeArray();
        for (int k1 = 0; k1 < aGraphicViewerNetworkNode.length; k1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[k1];
            int l = nodeData(graphicViewerNetworkNode).layer;
            int i2 = getNodeMinLayerSpace(graphicViewerNetworkNode);
            ai[l] = Math.max(ai[l], i2);
        }

        int ai1[] = new int[maxLayer + 1];
        int l1 = 0;
        for (int i1 = 0; i1 <= maxLayer; i1++) {
            l1 += ai[i1];
            ai1[i1] = l1;
            l1 = ++l1 + ai[i1];
        }

        for (int j2 = 0; j2 < aGraphicViewerNetworkNode.length; j2++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[j2];
            int j1 = nodeData(graphicViewerNetworkNode1).layer;
            int k2 = nodeData(graphicViewerNetworkNode1).column;
            int l2;
            int i3;
            switch (directionOption) {
            case 0: // '\0'
                l2 = columnSpacing * (k2 + 1);
                i3 = layerSpacing * (ai1[j1] + 1);
                break;

            case 1: // '\001'
                l2 = columnSpacing * (k2 + 1);
                i3 = layerSpacing * (l1 - ai1[j1]);
                break;

            case 2: // '\002'
                l2 = layerSpacing * (ai1[j1] + 1);
                i3 = columnSpacing * (k2 + 1);
                break;

            case 3: // '\003'
                l2 = layerSpacing * (l1 - ai1[j1]);
                i3 = columnSpacing * (k2 + 1);
                break;

            default:
                l2 = columnSpacing * (k2 + 1);
                i3 = layerSpacing * (ai1[j1] + 1);
                break;
            }
            graphicViewerNetworkNode1.setCenter(l2, i3);
            graphicViewerNetworkNode1.commitPosition();
        }

    }

    protected void layoutLinks() {
        Point point = new Point(0, 0);
        GraphicViewerNetworkLink aGraphicViewerNetworkLink[] = getNetwork()
                .getLinkArray();
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            GraphicViewerNetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            if (linkData(graphicViewerNetworkLink).valid) {
                continue;
            }
            GraphicViewerLink graphicViewerLink = (GraphicViewerLink) graphicViewerNetworkLink
                    .getGraphicViewerObject();
            if (graphicViewerLink == null) {
                continue;
            }
            GraphicViewerPort graphicViewerPort = graphicViewerLink
                    .getFromPort();
            GraphicViewerPort graphicViewerPort1 = graphicViewerLink
                    .getToPort();
            graphicViewerLink.removeAllPoints();
            int l = 1;
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = graphicViewerLink.isAvoidsNodes();
            graphicViewerLink.setInitializing(true);
            graphicViewerLink.setAvoidsNodes(false);
            graphicViewerLink.calculateStroke();
            graphicViewerLink.setAvoidsNodes(flag2);
            graphicViewerLink.setInitializing(false);
            l = graphicViewerLink.getFirstPickPoint() + 1;
            if (graphicViewerLink.isOrthogonal()) {
                flag = true;
                for (; graphicViewerLink.getNumPoints() > 4; graphicViewerLink
                        .removePoint(2)) {
                    ;
                }
            } else if (graphicViewerLink.isCubic()) {
                flag1 = true;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode = graphicViewerNetworkLink
                    .getFromNode();
            GraphicViewerNetworkNode graphicViewerNetworkNode1 = graphicViewerNetworkLink
                    .getToNode();
            if (!linkData(graphicViewerNetworkLink).rev) {
                GraphicViewerNetworkNode graphicViewerNetworkNode2;
                for (; graphicViewerNetworkNode != null
                        && graphicViewerNetworkNode != graphicViewerNetworkNode1; graphicViewerNetworkNode = graphicViewerNetworkNode2) {
                    graphicViewerNetworkNode2 = null;
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink1[] = graphicViewerNetworkNode
                            .getSuccLinksArray();
                    for (int i1 = 0; i1 < aGraphicViewerNetworkLink1.length; i1++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink1 = aGraphicViewerNetworkLink1[i1];
                        if (graphicViewerNetworkLink1.getGraphicViewerObject() == graphicViewerNetworkLink
                                .getGraphicViewerObject()) {
                            graphicViewerNetworkNode2 = graphicViewerNetworkLink1
                                    .getToNode();
                        }
                    }

                    if (graphicViewerNetworkNode2 == graphicViewerNetworkNode1) {
                        continue;
                    }
                    Point point7 = graphicViewerLink.getPoint(l - 1);
                    Point point9 = graphicViewerNetworkNode2.getCenter();
                    if (flag) {
                        if (getDirectionOption() == 2
                                || getDirectionOption() == 3) {
                            int k1 = (point7.x + point9.x) / 2;
                            graphicViewerLink.insertPoint(l++, k1, point7.y);
                            graphicViewerLink.insertPoint(l++, k1, point9.y);
                        } else {
                            int l1 = (point7.y + point9.y) / 2;
                            graphicViewerLink.insertPoint(l++, point7.x, l1);
                            graphicViewerLink.insertPoint(l++, point9.x, l1);
                        }
                        continue;
                    }
                    if (flag1) {
                        if (getDirectionOption() == 2
                                || getDirectionOption() == 3) {
                            byte byte2 = ((byte) (getDirectionOption() != 3 ? -30
                                    : 30));
                            graphicViewerLink.insertPoint(l++,
                                    point9.x - byte2, point9.y);
                            graphicViewerLink.insertPoint(l++, point9.x,
                                    point9.y);
                            graphicViewerLink.insertPoint(l++,
                                    point9.x + byte2, point9.y);
                        } else {
                            byte byte3 = ((byte) (getDirectionOption() != 1 ? -30
                                    : 30));
                            graphicViewerLink.insertPoint(l++, point9.x,
                                    point9.y - byte3);
                            graphicViewerLink.insertPoint(l++, point9.x,
                                    point9.y);
                            graphicViewerLink.insertPoint(l++, point9.x,
                                    point9.y + byte3);
                        }
                    } else {
                        graphicViewerLink.insertPoint(l++, point9);
                    }
                }

                if (flag) {
                    Point point1 = graphicViewerLink.getPoint(l - 1);
                    Point point4 = graphicViewerLink.getPoint(l);
                    if (getDirectionOption() == 2 || getDirectionOption() == 3) {
                        graphicViewerLink.insertPoint(l++, point4.x, point1.y);
                        graphicViewerLink.insertPoint(l++, point4);
                    } else {
                        graphicViewerLink.insertPoint(l++, point1.x, point4.y);
                        graphicViewerLink.insertPoint(l++, point4);
                    }
                }
            } else {
                GraphicViewerNetworkNode graphicViewerNetworkNode3;
                for (; graphicViewerNetworkNode1 != null
                        && graphicViewerNetworkNode != graphicViewerNetworkNode1; graphicViewerNetworkNode1 = graphicViewerNetworkNode3) {
                    graphicViewerNetworkNode3 = null;
                    GraphicViewerNetworkLink aGraphicViewerNetworkLink2[] = graphicViewerNetworkNode1
                            .getPredLinksArray();
                    for (int j1 = 0; j1 < aGraphicViewerNetworkLink2.length; j1++) {
                        GraphicViewerNetworkLink graphicViewerNetworkLink2 = aGraphicViewerNetworkLink2[j1];
                        if (graphicViewerNetworkLink2.getGraphicViewerObject() == graphicViewerNetworkLink
                                .getGraphicViewerObject()) {
                            graphicViewerNetworkNode3 = graphicViewerNetworkLink2
                                    .getFromNode();
                        }
                    }

                    if (graphicViewerNetworkNode3 == graphicViewerNetworkNode) {
                        continue;
                    }
                    Point point8 = graphicViewerLink.getPoint(l - 1);
                    Point point10 = graphicViewerNetworkNode3.getCenter();
                    if (flag) {
                        if (getDirectionOption() == 2
                                || getDirectionOption() == 3) {
                            if (l == 2) {
                                graphicViewerLink.insertPoint(l++, point8.x,
                                        point8.y);
                                graphicViewerLink.insertPoint(l++, point8.x,
                                        point10.y);
                            } else {
                                int k2 = (point8.x + point10.x) / 2;
                                graphicViewerLink
                                        .insertPoint(l++, k2, point8.y);
                                graphicViewerLink.insertPoint(l++, k2,
                                        point10.y);
                            }
                            continue;
                        }
                        if (l == 2) {
                            graphicViewerLink.insertPoint(l++, point8.x,
                                    point8.y);
                            graphicViewerLink.insertPoint(l++, point10.x,
                                    point8.y);
                        } else {
                            int l2 = (point8.y + point10.y) / 2;
                            graphicViewerLink.insertPoint(l++, point8.x, l2);
                            graphicViewerLink.insertPoint(l++, point10.x, l2);
                        }
                        continue;
                    }
                    if (flag1) {
                        if (getDirectionOption() == 2
                                || getDirectionOption() == 3) {
                            byte byte4 = ((byte) (getDirectionOption() != 3 ? 30
                                    : -30));
                            graphicViewerLink.insertPoint(l++, point10.x
                                    - byte4, point10.y);
                            graphicViewerLink.insertPoint(l++, point10.x,
                                    point10.y);
                            graphicViewerLink.insertPoint(l++, point10.x
                                    + byte4, point10.y);
                        } else {
                            byte byte5 = ((byte) (getDirectionOption() != 1 ? 30
                                    : -30));
                            graphicViewerLink.insertPoint(l++, point10.x,
                                    point10.y - byte5);
                            graphicViewerLink.insertPoint(l++, point10.x,
                                    point10.y);
                            graphicViewerLink.insertPoint(l++, point10.x,
                                    point10.y + byte5);
                        }
                    } else {
                        graphicViewerLink.insertPoint(l++, point10);
                    }
                }

                if (flag) {
                    Point point2 = graphicViewerLink.getPoint(l - 1);
                    Point point5 = graphicViewerLink.getPoint(l);
                    if (getDirectionOption() == 2 || getDirectionOption() == 3) {
                        if (point2.y == point5.y) {
                            graphicViewerLink.insertPoint(l++, point2.x,
                                    point2.y - 30);
                            graphicViewerLink.insertPoint(l++, point5.x,
                                    point2.y - 30);
                        }
                        graphicViewerLink.insertPoint(l++, point5.x, point2.y);
                        graphicViewerLink.insertPoint(l++, point5);
                    } else {
                        if (point2.x == point5.x) {
                            graphicViewerLink.insertPoint(l++, point2.x - 30,
                                    point2.y);
                            graphicViewerLink.insertPoint(l++, point2.x - 30,
                                    point5.y);
                        }
                        graphicViewerLink.insertPoint(l++, point2.x, point5.y);
                        graphicViewerLink.insertPoint(l++, point5);
                    }
                } else if (flag1) {
                    Point point3 = graphicViewerLink.getPoint(l - 1);
                    Point point6 = graphicViewerLink.getPoint(l);
                    if (getDirectionOption() == 2 || getDirectionOption() == 3) {
                        byte byte0 = ((byte) (getDirectionOption() != 3 ? 30
                                : -30));
                        if (point3.y == point6.y) {
                            graphicViewerLink.insertPoint(l++, point6.x - 2
                                    * byte0, point3.y + byte0);
                            graphicViewerLink.insertPoint(l++,
                                    point6.x - byte0, point3.y + byte0);
                            graphicViewerLink.insertPoint(l++, point6.x,
                                    point3.y + byte0);
                            graphicViewerLink.insertPoint(l++,
                                    point6.x - byte0, point3.y);
                            graphicViewerLink.insertPoint(l++, point6.x,
                                    point3.y);
                        } else {
                            graphicViewerLink.insertPoint(l++, point6.x - 2
                                    * byte0, point3.y);
                            graphicViewerLink.insertPoint(l++,
                                    point6.x - byte0, point3.y);
                            graphicViewerLink.insertPoint(l++, point6.x,
                                    point3.y);
                        }
                    } else {
                        byte byte1 = ((byte) (getDirectionOption() != 1 ? 30
                                : -30));
                        if (point3.x == point6.x) {
                            graphicViewerLink.insertPoint(l++,
                                    point3.x + byte1, point6.y - 2 * byte1);
                            graphicViewerLink.insertPoint(l++,
                                    point3.x + byte1, point6.y - byte1);
                            graphicViewerLink.insertPoint(l++,
                                    point3.x + byte1, point6.y);
                            graphicViewerLink.insertPoint(l++, point3.x,
                                    point6.y - byte1);
                            graphicViewerLink.insertPoint(l++, point3.x,
                                    point6.y);
                        } else {
                            graphicViewerLink.insertPoint(l++, point3.x,
                                    point6.y - 2 * byte1);
                            graphicViewerLink.insertPoint(l++, point3.x,
                                    point6.y - byte1);
                            graphicViewerLink.insertPoint(l++, point3.x,
                                    point6.y);
                        }
                    }
                }
            }
            graphicViewerLink.setPoint(0, graphicViewerPort.getFromLinkPoint(
                    graphicViewerLink, point));
            graphicViewerLink
                    .setPoint(graphicViewerLink.getNumPoints() - 1,
                            graphicViewerPort1.getToLinkPoint(
                                    graphicViewerLink, point));
            graphicViewerNetworkLink.commitPosition();
        }

    }

    protected GraphicViewerLayeredDigraphAutoLayoutNodeData nodeData(
            GraphicViewerNetworkNode graphicViewerNetworkNode) {
        return (GraphicViewerLayeredDigraphAutoLayoutNodeData) graphicViewerNetworkNode.nodeData;
    }

    protected GraphicViewerLayeredDigraphAutoLayoutLinkData linkData(
            GraphicViewerNetworkLink graphicViewerNetworkLink) {
        return (GraphicViewerLayeredDigraphAutoLayoutLinkData) graphicViewerNetworkLink.linkData;
    }

    protected void printNetworkData() {
        RandomAccessFile randomaccessfile = null;
        try {
            randomaccessfile = new RandomAccessFile("javaout.txt", "rw");
            randomaccessfile.seek(randomaccessfile.length());
            randomaccessfile.writeBytes("Link Data\r\n\r\n");
            GraphicViewerNetworkLink graphicViewerNetworkLink;
            for (Iterator iterator = getNetwork().getLinkIterator(); iterator
                    .hasNext(); randomaccessfile
                    .writeBytes(linkData(graphicViewerNetworkLink).portFromColOffset
                            + ","
                            + linkData(graphicViewerNetworkLink).portToColOffset
                            + "\r\n")) {
                graphicViewerNetworkLink = (GraphicViewerNetworkLink) iterator
                        .next();
                if (linkData(graphicViewerNetworkLink).valid) {
                    randomaccessfile.writeBytes("1,");
                } else {
                    randomaccessfile.writeBytes("0,");
                }
                if (linkData(graphicViewerNetworkLink).rev) {
                    randomaccessfile.writeBytes("1,");
                } else {
                    randomaccessfile.writeBytes("0,");
                }
                if (linkData(graphicViewerNetworkLink).forest) {
                    randomaccessfile.writeBytes("1,");
                } else {
                    randomaccessfile.writeBytes("0,");
                }
            }

            randomaccessfile.writeBytes("\r\n\r\nNode Data\r\n\r\n");
            GraphicViewerNetworkNode graphicViewerNetworkNode;
            for (Iterator iterator1 = getNetwork().getNodeIterator(); iterator1
                    .hasNext(); randomaccessfile
                    .writeBytes(nodeData(graphicViewerNetworkNode).discover
                            + "," + nodeData(graphicViewerNetworkNode).finish
                            + ","
                            + nodeData(graphicViewerNetworkNode).component
                            + "\r\n")) {
                graphicViewerNetworkNode = (GraphicViewerNetworkNode) iterator1
                        .next();
                randomaccessfile
                        .writeBytes(nodeData(graphicViewerNetworkNode).layer
                                + ","
                                + nodeData(graphicViewerNetworkNode).column
                                + ","
                                + nodeData(graphicViewerNetworkNode).index);
                if (nodeData(graphicViewerNetworkNode).valid) {
                    randomaccessfile.writeBytes(",1,");
                } else {
                    randomaccessfile.writeBytes(",0,");
                }
            }

            randomaccessfile.writeBytes("\r\n\r\n");
        } catch (IOException ioe) {
            ExceptionDialog.ignoreException(ioe);
        } finally {
            try {
                if (randomaccessfile != null) {
                    randomaccessfile.close();
                }
            } catch (Exception e) {
                ExceptionDialog.ignoreException(e);
            }
        }
    }

    public int getLayerSpacing() {
        return layerSpacing;
    }

    public void setLayerSpacing(int k) {
        if (k > 0) {
            layerSpacing = k;
        }
    }

    public int getColumnSpacing() {
        return columnSpacing;
    }

    public void setColumnSpacing(int k) {
        if (k > 0) {
            columnSpacing = k;
        }
    }

    public int getDirectionOption() {
        return directionOption;
    }

    public void setDirectionOption(int k) {
        directionOption = k;
    }

    public int getCycleRemoveOption() {
        return cycleremoveOption;
    }

    public void setCycleRemoveOption(int k) {
        cycleremoveOption = k;
    }

    public int getLayeringOption() {
        return layeringOption;
    }

    public void setLayeringOption(int k) {
        layeringOption = k;
    }

    public int getInitializeOption() {
        return initializeOption;
    }

    public void setInitializeOption(int k) {
        initializeOption = k;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int k) {
        iterations = Math.max(k, 0);
    }

    public int getAggressiveOption() {
        return aggressiveOption;
    }

    public void setAggressiveOption(int k) {
        aggressiveOption = k;
    }

    public int getPackOption() {
        return packOption;
    }

    public void setPackOption(int k) {
        packOption = k;
    }

    public int getMaxLayer() {
        return maxLayer;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public int getMinIndexLayer() {
        return minIndexLayer;
    }

    public int getMaxIndexLayer() {
        return maxIndexLayer;
    }

    public int[] getIndices() {
        return indices;
    }

    private void ClearCaches() {
        maxIndex = -1;
        minIndexLayer = 0;
        maxIndexLayer = 0;
        mySavedLayout = null;
        myMedians = null;
        myColumnsPS = null;
        myCrossings = null;
        for (int k = 0; k < myCachedNodeArrays.length; k++) {
            myCachedNodeArrays[k] = null;
        }

    }

    private GraphicViewerNetworkNode[] GetCachedNodeArray(int k) {
        if (indices[k] >= myCachedNodeArrays.length) {
            GraphicViewerNetworkNode aGraphicViewerNetworkNode1[][] = new GraphicViewerNetworkNode[indices[k] + 50][];
            for (int l = 0; l < myCachedNodeArrays.length; l++) {
                aGraphicViewerNetworkNode1[l] = myCachedNodeArrays[l];
            }

            myCachedNodeArrays = aGraphicViewerNetworkNode1;
        }
        GraphicViewerNetworkNode aGraphicViewerNetworkNode[];
        if (myCachedNodeArrays[indices[k]] == null) {
            aGraphicViewerNetworkNode = new GraphicViewerNetworkNode[indices[k]];
        } else {
            aGraphicViewerNetworkNode = myCachedNodeArrays[indices[k]];
            myCachedNodeArrays[indices[k]] = null;
        }
        GraphicViewerNetworkNode aGraphicViewerNetworkNode2[] = getNetwork()
                .getNodeArray();
        for (int i1 = 0; i1 < aGraphicViewerNetworkNode2.length; i1++) {
            GraphicViewerNetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode2[i1];
            if (nodeData(graphicViewerNetworkNode).layer == k) {
                aGraphicViewerNetworkNode[nodeData(graphicViewerNetworkNode).index] = graphicViewerNetworkNode;
            }
        }

        return aGraphicViewerNetworkNode;
    }

    private void FreeCachedNodeArray(int k,
            GraphicViewerNetworkNode aGraphicViewerNetworkNode[]) {
        myCachedNodeArrays[indices[k]] = aGraphicViewerNetworkNode;
    }
}