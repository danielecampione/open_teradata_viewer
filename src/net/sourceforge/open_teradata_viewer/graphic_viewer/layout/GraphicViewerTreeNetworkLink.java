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

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GraphicViewerTreeNetworkLink {

    private GraphicViewerObject myGraphicViewerObject;
    private GraphicViewerTreeNetwork myNetwork;
    private GraphicViewerTreeNetworkNode myFromNode;
    private GraphicViewerTreeNetworkNode myToNode;
    private Point myRelativePoint = new Point(0, 0);
    private Object myLinkUserData;
    private int myFlags;

    public GraphicViewerObject getGraphicViewerObject() {
        return this.myGraphicViewerObject;
    }

    public void setGraphicViewerObject(
            GraphicViewerObject paramGraphicViewerObject) {
        this.myGraphicViewerObject = paramGraphicViewerObject;
    }

    public GraphicViewerTreeNetwork getNetwork() {
        return this.myNetwork;
    }

    public void setNetwork(
            GraphicViewerTreeNetwork paramGraphicViewerTreeNetwork) {
        this.myNetwork = paramGraphicViewerTreeNetwork;
    }

    public GraphicViewerTreeNetworkNode getFromNode() {
        return this.myFromNode;
    }

    public void setFromNode(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        this.myFromNode = paramGraphicViewerTreeNetworkNode;
    }

    public GraphicViewerTreeNetworkNode getToNode() {
        return this.myToNode;
    }

    public void setToNode(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        this.myToNode = paramGraphicViewerTreeNetworkNode;
    }

    public void ReverseLink() {
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = this.myFromNode;
        this.myFromNode = this.myToNode;
        this.myToNode = localGraphicViewerTreeNetworkNode;
    }

    public void CommitPosition() {
        GraphicViewerLink localGraphicViewerLink1 = (GraphicViewerLink) getGraphicViewerObject();
        if (localGraphicViewerLink1 == null) {
            return;
        }
        GraphicViewerTreeAutoLayout localGraphicViewerTreeAutoLayout = getNetwork()
                .getLayout();
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = null;
        switch (localGraphicViewerTreeAutoLayout.getPath()) {
            case 1 :
                localGraphicViewerTreeNetworkNode = getFromNode();
                break;
            case 2 :
                localGraphicViewerTreeNetworkNode = getToNode();
        }
        if (localGraphicViewerTreeNetworkNode == null) {
            return;
        }
        Point localPoint1 = new Point(getRelativePoint().x,
                getRelativePoint().y);
        if ((localPoint1.x == 0) && (localPoint1.y == 0)
                && (!localGraphicViewerTreeNetworkNode.getRouteFirstRow())) {
            return;
        }
        int i = (localPoint1.x == 0) && (localPoint1.y == 0)
                && (localGraphicViewerTreeNetworkNode.getRouteFirstRow())
                ? 1
                : 0;
        GraphicViewerObject localGraphicViewerObject = localGraphicViewerTreeNetworkNode
                .getGraphicViewerObject();
        int j = localGraphicViewerObject.getBoundingRect().y;
        int k = localGraphicViewerObject.getBoundingRect().y
                + localGraphicViewerObject.getBoundingRect().height;
        int m = localGraphicViewerObject.getBoundingRect().x;
        int n = localGraphicViewerObject.getBoundingRect().x
                + localGraphicViewerObject.getBoundingRect().width;
        int i1 = localGraphicViewerTreeNetworkNode.getLayerSpacing();
        int i2 = localGraphicViewerTreeNetworkNode.getRowSpacing();
        GraphicViewerLink localGraphicViewerLink2 = localGraphicViewerLink1;
        if ((localGraphicViewerLink2 != null)
                && (localGraphicViewerLink2.isAvoidsNodes())) {
            return;
        }
        int i3 = (localGraphicViewerLink2 != null)
                && (localGraphicViewerLink2.isCubic()) ? 1 : 0;
        int i4 = (localGraphicViewerLink2 != null)
                && (localGraphicViewerLink2.isOrthogonal()) ? 1 : 0;
        int i5 = localGraphicViewerLink1.getNumPoints() / 2;
        Point localPoint2;
        Point localPoint3;
        if ((i4 != 0) || (i3 != 0)) {
            i5 = 2;
            while (localGraphicViewerLink1.getNumPoints() > 4) {
                localGraphicViewerLink1.removePoint(2);
            }
            localPoint2 = localGraphicViewerLink1.getPoint(1);
            localPoint3 = localGraphicViewerLink1.getPoint(2);
        } else {
            localPoint2 = localGraphicViewerLink1.getPoint(0);
            localPoint3 = localGraphicViewerLink1
                    .getPoint(localGraphicViewerLink1.getNumPoints() - 1);
        }
        Point localPoint4 = localGraphicViewerLink1
                .getPoint(localGraphicViewerLink1.getNumPoints() - 1);
        float f = GraphicViewerTreeAutoLayout
                .OrthoAngle(localGraphicViewerTreeNetworkNode);
        int i6;
        if (f == 0.0F) {
            if (localGraphicViewerTreeNetworkNode.getAlignment() == 4) {
                i6 = k + localPoint1.y;
                if ((localPoint1.y == 0)
                        && (localPoint2.y > localPoint4.y
                                + localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.min(
                            i6,
                            Math.max(
                                    localPoint2.y,
                                    i6
                                            - localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if (localGraphicViewerTreeNetworkNode.getAlignment() == 3) {
                i6 = j + localPoint1.y;
                if ((localPoint1.y == 0)
                        && (localPoint2.y < localPoint4.y
                                - localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.max(
                            i6,
                            Math.min(
                                    localPoint2.y,
                                    i6
                                            + localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if ((localGraphicViewerTreeNetworkNode
                    .getRouteAroundCentered())
                    || ((localGraphicViewerTreeNetworkNode
                            .getRouteAroundLastParent()) && (localGraphicViewerTreeNetworkNode
                            .getMaxGenerationCount() == 1))) {
                i6 = j
                        - localGraphicViewerTreeNetworkNode.getSubtreeOffset().height
                        + localPoint1.y;
            } else {
                i6 = j + localGraphicViewerObject.getBoundingRect().height / 2
                        + localPoint1.y;
            }
            if (i3 != 0) {
                if (i == 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(
                            localPoint2.x, i6));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(n + i1,
                            i6));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(n + i1
                            + (localPoint1.x - i2) / 3, i6));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(n + i1
                            + (localPoint1.x - i2) * 2 / 3, i6));
                    i5++;
                } else {
                    localGraphicViewerLink1.insertPoint(i5, new Point(n + i1
                            + (localPoint1.x - i2), i6));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(n + i1
                        + (localPoint1.x - i2), i6));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(
                        localPoint3.x, i6));
                i5++;
            } else {
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(n + i1
                            / 2, localPoint2.y));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(n + i1 / 2,
                        i6));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(n + i1
                        + localPoint1.x - (i4 != 0 ? i2 / 2 : i2), i6));
                i5++;
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(
                            localGraphicViewerLink1.getPoint(i5 - 1).x,
                            localPoint3.y));
                    i5++;
                }
            }
        } else if (f == 90.0F) {
            if (localGraphicViewerTreeNetworkNode.getAlignment() == 4) {
                i6 = n + localPoint1.x;
                if ((localPoint1.x == 0)
                        && (localPoint2.x > localPoint4.x
                                + localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.min(
                            i6,
                            Math.max(
                                    localPoint2.x,
                                    i6
                                            - localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if (localGraphicViewerTreeNetworkNode.getAlignment() == 3) {
                i6 = m + localPoint1.x;
                if ((localPoint1.x == 0)
                        && (localPoint2.x < localPoint4.x
                                - localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.max(
                            i6,
                            Math.min(
                                    localPoint2.x,
                                    i6
                                            + localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if ((localGraphicViewerTreeNetworkNode
                    .getRouteAroundCentered())
                    || ((localGraphicViewerTreeNetworkNode
                            .getRouteAroundLastParent()) && (localGraphicViewerTreeNetworkNode
                            .getMaxGenerationCount() == 1))) {
                i6 = m = localGraphicViewerTreeNetworkNode.getSubtreeOffset().width = localPoint1.x;
            } else {
                i6 = localGraphicViewerObject.getBoundingRect().x
                        + localGraphicViewerObject.getBoundingRect().width / 2
                        + localPoint1.x;
            }
            if (i3 != 0) {
                if (i == 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6,
                            localPoint2.y));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, k
                            + i1));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, k
                            + i1 + (localPoint1.y - i2) / 3));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, k
                            + i1 + (localPoint1.y - i2) * 2 / 3));
                    i5++;
                } else {
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, k
                            + i1 + (localPoint1.y - i2)));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(i6, k + i1
                        + (localPoint1.y - i2)));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(i6,
                        localPoint3.y));
                i5++;
            } else {
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(
                            localPoint2.x, k + i1 / 2));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(i6, k + i1
                        / 2));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(i6, k + i1
                        + localPoint1.y - (i4 != 0 ? i2 / 2 : i2)));
                i5++;
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(
                            i5,
                            new Point(localPoint3.x, localGraphicViewerLink1
                                    .getPoint(i5 - 1).y));
                    i5++;
                }
            }
        } else if (f == 180.0F) {
            if (localGraphicViewerTreeNetworkNode.getAlignment() == 4) {
                i6 = k + localPoint1.y;
                if ((localPoint1.y == 0)
                        && (localPoint2.y > localPoint4.y
                                + localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.min(
                            i6,
                            Math.max(
                                    localPoint2.y,
                                    i6
                                            - localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if (localGraphicViewerTreeNetworkNode.getAlignment() == 3) {
                i6 = j + localPoint1.y;
                if ((localPoint1.y == 0)
                        && (localPoint2.y < localPoint4.y
                                - localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.max(
                            i6,
                            Math.min(
                                    localPoint2.y,
                                    i6
                                            + localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if ((localGraphicViewerTreeNetworkNode
                    .getRouteAroundCentered())
                    || ((localGraphicViewerTreeNetworkNode
                            .getRouteAroundLastParent()) && (localGraphicViewerTreeNetworkNode
                            .getMaxGenerationCount() == 1))) {
                i6 = j
                        - localGraphicViewerTreeNetworkNode.getSubtreeOffset().height
                        + localPoint1.y;
            } else {
                i6 = j + localGraphicViewerObject.getHeight() / 2
                        + localPoint1.y;
            }
            if (i3 != 0) {
                if (i == 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(
                            localPoint2.x, i6));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(m - i1,
                            i6));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(m - i1
                            + (localPoint1.x + i2) / 3, i6));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(m - i1
                            + (localPoint1.x + i2) * 2 / 3, i6));
                    i5++;
                } else {
                    localGraphicViewerLink1.insertPoint(i5, new Point(m - i1
                            + (localPoint1.x + i2), i6));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(m - i1
                        + (localPoint1.x + i2), i6));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(
                        localPoint3.x, i6));
                i5++;
            } else {
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(m - i1
                            / 2, localPoint2.y));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(m - i1 / 2,
                        i6));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(m - i1
                        + localPoint1.x + (i4 != 0 ? i2 / 2 : i2), i6));
                i5++;
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(
                            localGraphicViewerLink1.getPoint(i5 - 1).x,
                            localPoint3.y));
                    i5++;
                }
            }
        } else if (f == 270.0F) {
            if (localGraphicViewerTreeNetworkNode.getAlignment() == 4) {
                i6 = n + localPoint1.x;
                if ((localPoint1.x == 0)
                        && (localPoint2.x > localPoint4.x
                                + localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.min(
                            i6,
                            Math.max(
                                    localPoint2.x,
                                    i6
                                            - localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if (localGraphicViewerTreeNetworkNode.getAlignment() == 3) {
                i6 = m + localPoint1.x;
                if ((localPoint1.x == 0)
                        && (localPoint2.x < localPoint4.x
                                - localGraphicViewerTreeNetworkNode
                                        .getRowIndent())) {
                    i6 = Math.max(
                            i6,
                            Math.min(
                                    localPoint2.x,
                                    i6
                                            + localGraphicViewerTreeNetworkNode
                                                    .getNodeIndent()));
                }
            } else if ((localGraphicViewerTreeNetworkNode
                    .getRouteAroundCentered())
                    || ((localGraphicViewerTreeNetworkNode
                            .getRouteAroundLastParent()) && (localGraphicViewerTreeNetworkNode
                            .getMaxGenerationCount() == 1))) {
                i6 = m
                        - localGraphicViewerTreeNetworkNode.getSubtreeOffset().width
                        + localPoint1.x;
            } else {
                i6 = localGraphicViewerObject.getLeft()
                        + localGraphicViewerObject.getWidth() / 2
                        + localPoint1.x;
            }
            if (i3 != 0) {
                if (i == 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6,
                            localPoint2.y));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, j
                            - i1));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, j
                            - i1 + (localPoint1.y + i2 / 3)));
                    i5++;
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, j
                            - i1 + (localPoint1.y + i2 * 2 / 3)));
                    i5++;
                } else {
                    localGraphicViewerLink1.insertPoint(i5, new Point(i6, j
                            - i1 + (localPoint1.y + i2)));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(i6, j - i1
                        + (localPoint1.y + i2)));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(i6,
                        localPoint3.y));
                i5++;
            } else {
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(i5, new Point(
                            localPoint2.x, j - i1 / 2));
                    i5++;
                }
                localGraphicViewerLink1.insertPoint(i5, new Point(i6, j - i1
                        / 2));
                i5++;
                localGraphicViewerLink1.insertPoint(i5, new Point(i6, j - i1
                        + localPoint1.y + (i4 != 0 ? i2 / 2 : i2)));
                i5++;
                if (i4 != 0) {
                    localGraphicViewerLink1.insertPoint(
                            i5,
                            new Point(localPoint3.x, localGraphicViewerLink1
                                    .getPoint(i5 - 1).y));
                    i5++;
                }
            }
        }
    }

    public Object getUserObject() {
        return this.myLinkUserData;
    }

    public void setUserObject(Object paramObject) {
        this.myLinkUserData = paramObject;
    }

    public int getUserFlags() {
        return this.myFlags;
    }

    public void setUserFlags(int paramInt) {
        this.myFlags = paramInt;
    }

    public Point getRelativePoint() {
        return this.myRelativePoint;
    }

    public void setRelativePoint(Point paramPoint) {
        this.myRelativePoint = paramPoint;
    }
}