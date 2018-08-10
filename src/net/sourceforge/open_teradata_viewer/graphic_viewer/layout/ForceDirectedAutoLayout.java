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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

import java.awt.Point;
import java.util.Random;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSelection;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ForceDirectedAutoLayout extends AutoLayout {

    public ForceDirectedAutoLayout() {
        s = null;
        p = getDefaultMaxIterations();
    }

    public ForceDirectedAutoLayout(GraphicViewerDocument graphicViewerDocument) {
        super(graphicViewerDocument);
        s = null;
        p = getDefaultMaxIterations();
    }

    public ForceDirectedAutoLayout(GraphicViewerSelection graphicViewerSelection) {
        super(graphicViewerSelection);
        s = null;
        p = getDefaultMaxIterations();
    }

    public ForceDirectedAutoLayout(GraphicViewerDocument graphicViewerDocument,
            int i) {
        super(graphicViewerDocument);
        s = null;
        p = i;
    }

    public ForceDirectedAutoLayout(GraphicViewerDocument graphicViewerDocument,
            Network graphicViewerNetwork, int i) {
        super(graphicViewerDocument, graphicViewerNetwork);
        s = null;
        p = i;
    }

    public ForceDirectedAutoLayout(
            ForceDirectedAutoLayout graphicViewerForceDirectedAutoLayout) {
        s = null;
        setDocument(graphicViewerForceDirectedAutoLayout.getDocument());
        setNetwork(null);
        setMaxIterations(graphicViewerForceDirectedAutoLayout
                .getMaxIterations());
        setRandomNumberGenerator(graphicViewerForceDirectedAutoLayout
                .getRandomNumberGenerator());
    }

    public void performLayout() {
        if (getNetwork() == null)
            return;
        progressUpdate(0.0D);
        getNetwork().deleteSelfLinks();
        NetworkNode aGraphicViewerNetworkNode[] = getNetwork().getNodeArray();
        for (int i = 0; i < aGraphicViewerNetworkNode.length; i++) {
            NetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
            graphicViewerNetworkNode._flddo = new ForceDirectedAutoLayoutNodeData();
            nodeData(graphicViewerNetworkNode).charge = getElectricalCharge(graphicViewerNetworkNode);
            nodeData(graphicViewerNetworkNode).mass = getGravitationalMass(graphicViewerNetworkNode);
            nodeData(graphicViewerNetworkNode).changeX = 0;
            nodeData(graphicViewerNetworkNode).changeY = 0;
        }

        NetworkLink aGraphicViewerNetworkLink[] = getNetwork().getLinkArray();
        for (int j = 0; j < aGraphicViewerNetworkLink.length; j++) {
            NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[j];
            graphicViewerNetworkLink.a = new ForceDirectedAutoLayoutLinkData();
            linkData(graphicViewerNetworkLink).stiffness = getSpringStiffness(graphicViewerNetworkLink);
            linkData(graphicViewerNetworkLink).length = getSpringLength(graphicViewerNetworkLink);
        }

        r = 0;
        do {
            if (r >= p)
                break;
            r++;
            if (!updatePositions())
                break;
            layoutNodesAndLinks(false);
        } while (true);
        layoutNodesAndLinks(true);
        progressUpdate(1.0D);
    }

    protected double getNodeDistance(NetworkNode graphicViewerNetworkNode,
            NetworkNode graphicViewerNetworkNode1) {
        Point point = graphicViewerNetworkNode.getCenter();
        int i;
        int j;
        int k;
        int l;
        if (graphicViewerNetworkNode.getGraphicViewerObject() != null) {
            int i1 = graphicViewerNetworkNode.getGraphicViewerObject()
                    .getWidth() / 2;
            i = point.x - i1;
            k = 2 * i1;
            int j1 = graphicViewerNetworkNode.getGraphicViewerObject()
                    .getHeight() / 2;
            j = point.y - j1;
            l = 2 * j1;
        } else {
            i = point.x;
            k = 0;
            j = point.y;
            l = 0;
        }
        Point point1 = graphicViewerNetworkNode1.getCenter();
        int k1;
        int l1;
        int i2;
        int j2;
        if (graphicViewerNetworkNode1.getGraphicViewerObject() != null) {
            int k2 = graphicViewerNetworkNode1.getGraphicViewerObject()
                    .getWidth() / 2;
            k1 = point1.x - k2;
            i2 = 2 * k2;
            int l2 = graphicViewerNetworkNode1.getGraphicViewerObject()
                    .getHeight() / 2;
            l1 = point1.y - l2;
            j2 = 2 * l2;
        } else {
            k1 = point1.x;
            i2 = 0;
            l1 = point1.y;
            j2 = 0;
        }
        if (i + k < k1) {
            if (j > l1 + j2)
                return Math.sqrt(Math.pow((i + k) - k1, 2D)
                        + Math.pow(j - (l1 + j2), 2D));
            if (j + l < l1)
                return Math.sqrt(Math.pow((i + k) - k1, 2D)
                        + Math.pow((j + l) - l1, 2D));
            else
                return (double) Math.abs((i + k) - k1);
        }
        if (i > k1 + i2) {
            if (j > l1 + j2)
                return Math.sqrt(Math.pow(i - (k1 + i2), 2D)
                        + Math.pow(j - (l1 + j2), 2D));
            if (j + l < l1)
                return Math.sqrt(Math.pow(i - (k1 + i2), 2D)
                        + Math.pow((j + l) - l1, 2D));
            else
                return (double) Math.abs(i - (k1 + i2));
        }
        if (j > l1 + j2)
            return (double) Math.abs(j - (l1 + j2));
        if (j + l < l1)
            return (double) Math.abs((j + l) - l1);
        else
            return 0.10000000000000001D;
    }

    protected double getSpringStiffness(NetworkLink graphicViewerNetworkLink) {
        return 0.050000000000000003D;
    }

    protected double getSpringLength(NetworkLink graphicViewerNetworkLink) {
        return 50D;
    }

    protected double getElectricalCharge(NetworkNode graphicViewerNetworkNode) {
        return 150D;
    }

    protected double getElectricalFieldX(Point point) {
        return 0.0D;
    }

    protected double getElectricalFieldY(Point point) {
        return 0.0D;
    }

    protected double getGravitationalMass(NetworkNode graphicViewerNetworkNode) {
        return 0.0D;
    }

    protected double getGravitationalFieldX(Point point) {
        return 0.0D;
    }

    protected double getGravitationalFieldY(Point point) {
        return 0.0D;
    }

    protected boolean isFixed(NetworkNode graphicViewerNetworkNode) {
        return false;
    }

    protected boolean updatePositions() {
        NetworkNode aGraphicViewerNetworkNode[] = getNetwork().getNodeArray();
        for (int i = 0; i < aGraphicViewerNetworkNode.length; i++) {
            NetworkNode graphicViewerNetworkNode = aGraphicViewerNetworkNode[i];
            nodeData(graphicViewerNetworkNode).forceX = 0.0D;
            nodeData(graphicViewerNetworkNode).forceY = 0.0D;
        }

        boolean flag = false;
        for (int j = 0; j < aGraphicViewerNetworkNode.length; j++) {
            NetworkNode graphicViewerNetworkNode1 = aGraphicViewerNetworkNode[j];
            Point point = graphicViewerNetworkNode1.getCenter();
            double d4 = nodeData(graphicViewerNetworkNode1).charge
                    * getElectricalFieldX(point);
            double d8 = nodeData(graphicViewerNetworkNode1).charge
                    * getElectricalFieldY(point);
            nodeData(graphicViewerNetworkNode1).forceX += d4;
            nodeData(graphicViewerNetworkNode1).forceY += d8;
            d4 = nodeData(graphicViewerNetworkNode1).mass
                    * getGravitationalFieldX(point);
            d8 = nodeData(graphicViewerNetworkNode1).mass
                    * getGravitationalFieldY(point);
            nodeData(graphicViewerNetworkNode1).forceX += d4;
            nodeData(graphicViewerNetworkNode1).forceY += d8;
            for (int i1 = j + 1; i1 < aGraphicViewerNetworkNode.length; i1++) {
                NetworkNode graphicViewerNetworkNode4 = aGraphicViewerNetworkNode[i1];
                Point point2 = graphicViewerNetworkNode4.getCenter();
                double d11 = getNodeDistance(graphicViewerNetworkNode1,
                        graphicViewerNetworkNode4);
                double d5;
                double d9;
                if (d11 < 1.0D) {
                    if (s == null)
                        s = new Random();
                    d5 = s.nextInt(20);
                    d9 = s.nextInt(20);
                } else {
                    double d1 = (-1D * (nodeData(graphicViewerNetworkNode1).charge * nodeData(graphicViewerNetworkNode4).charge))
                            / (d11 * d11);
                    d5 = d1 * ((double) (point2.x - point.x) / d11);
                    d9 = d1 * ((double) (point2.y - point.y) / d11);
                }
                nodeData(graphicViewerNetworkNode1).forceX += d5;
                nodeData(graphicViewerNetworkNode1).forceY += d9;
                nodeData(graphicViewerNetworkNode4).forceX -= d5;
                nodeData(graphicViewerNetworkNode4).forceY -= d9;
            }

        }

        NetworkLink aGraphicViewerNetworkLink[] = getNetwork().getLinkArray();
        for (int k = 0; k < aGraphicViewerNetworkLink.length; k++) {
            NetworkLink graphicViewerNetworkLink = aGraphicViewerNetworkLink[k];
            NetworkNode graphicViewerNetworkNode3 = graphicViewerNetworkLink
                    .getFromNode();
            NetworkNode graphicViewerNetworkNode5 = graphicViewerNetworkLink
                    .getToNode();
            Point point3 = graphicViewerNetworkNode3.getCenter();
            Point point4 = graphicViewerNetworkNode5.getCenter();
            double d13 = getNodeDistance(graphicViewerNetworkNode3,
                    graphicViewerNetworkNode5);
            double d6;
            double d10;
            if (d13 < 1.0D) {
                if (s == null)
                    s = new Random();
                d6 = s.nextInt(20);
                d10 = s.nextInt(20);
            } else {
                double d2 = linkData(graphicViewerNetworkLink).stiffness
                        * (d13 - linkData(graphicViewerNetworkLink).length);
                d6 = d2 * ((double) (point4.x - point3.x) / d13);
                d10 = d2 * ((double) (point4.y - point3.y) / d13);
            }
            nodeData(graphicViewerNetworkNode3).forceX += d6;
            nodeData(graphicViewerNetworkNode3).forceY += d10;
            nodeData(graphicViewerNetworkNode5).forceX -= d6;
            nodeData(graphicViewerNetworkNode5).forceY -= d10;
        }

        for (int l = 0; l < aGraphicViewerNetworkNode.length; l++) {
            NetworkNode graphicViewerNetworkNode2 = aGraphicViewerNetworkNode[l];
            Point point1 = graphicViewerNetworkNode2.getCenter();
            if (isFixed(graphicViewerNetworkNode2))
                continue;
            double d12 = 1.0D;
            int j1;
            int k1;
            do {
                j1 = (int) Math
                        .round(nodeData(graphicViewerNetworkNode2).forceX * d12);
                k1 = (int) Math
                        .round(nodeData(graphicViewerNetworkNode2).forceY * d12);
                d12 *= 1.25D;
            } while (j1 == 0 && k1 == 0 && d12 < 256D);
            j1 = Math.min(Math.max(j1, -50), 50);
            k1 = Math.min(Math.max(k1, -50), 50);
            graphicViewerNetworkNode2.setCenter(point1.x + j1, point1.y + k1);
            nodeData(graphicViewerNetworkNode2).changeX += j1;
            nodeData(graphicViewerNetworkNode2).changeY += k1;
            if (r % 10 != 0)
                continue;
            if (Math.abs(nodeData(graphicViewerNetworkNode2).changeX) > 1
                    || Math.abs(nodeData(graphicViewerNetworkNode2).changeY) > 1)
                flag = true;
            nodeData(graphicViewerNetworkNode2).changeX = 0;
            nodeData(graphicViewerNetworkNode2).changeY = 0;
        }

        if (r % 10 != 0)
            flag = true;
        return flag;
    }

    protected void layoutNodesAndLinks(boolean flag) {
        if (r % 10 == 0 || flag) {
            getNetwork().commitNodesAndLinks();
            progressUpdate((double) r / (double) p);
        }
    }

    public int getMaxIterations() {
        return p;
    }

    public void setMaxIterations(int i) {
        p = Math.max(i, 0);
    }

    public Random getRandomNumberGenerator() {
        return s;
    }

    public void setRandomNumberGenerator(Random random) {
        s = random;
    }

    public final ForceDirectedAutoLayoutNodeData nodeData(
            NetworkNode graphicViewerNetworkNode) {
        return (ForceDirectedAutoLayoutNodeData) graphicViewerNetworkNode._flddo;
    }

    public final ForceDirectedAutoLayoutLinkData linkData(
            NetworkLink graphicViewerNetworkLink) {
        return (ForceDirectedAutoLayoutLinkData) graphicViewerNetworkLink.a;
    }

    public static int getDefaultMaxIterations() {
        return q;
    }

    public static void setDefaultMaxIterations(int i) {
        q = i;
    }

    private static int q = 1000;
    private int p;
    private int r;
    private Random s;
}