/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.Arrays;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerLink extends GraphicViewerStroke
        implements
            IGraphicViewerIdentifiablePart {

    private static final long serialVersionUID = -16146961797393251L;

    public static final int AdjustingStyleCalculate = 0;
    public static final int AdjustingStyleScale = 1;
    public static final int AdjustingStyleStretch = 2;
    public static final int AdjustingStyleEnd = 3;
    public static final int RelinkableFromHandle = 91;
    public static final int RelinkableToHandle = 92;
    public static final int ChangedFromPort = 201;
    public static final int ChangedToPort = 202;
    public static final int ChangedOrthogonal = 203;
    public static final int ChangedRelinkable = 204;
    public static final int ChangedJumpsOver = 205;
    public static final int ChangedAvoidsNodes = 206;
    public static final int ChangedCurviness = 207;
    public static final int ChangedAdjustingStyle = 208;
    public static final int ChangedUserObject = 209;
    public static final int ChangedPartID = 210;
    public static final int ChangedRoundedCorners = 211;
    private static final int flagAdjustingStyle = 15;
    private static final int flagLinkOrtho = 8192;
    private static final int flagLinkRelinkable = 65536;
    private static final int flagLinkJumpsOver = 131072;
    private static final int flagLinkAvoidsNodes = 262144;
    private static final int flagNoClearPorts = 524288;
    private static final int flagRoundedCorners = 1048576;
    private static boolean myResizingModifiesExistingLink = true;
    private int myPartID = -1;
    private Object myUserObject = null;
    private int myLinkFlags = 0;
    private GraphicViewerPort myFromPort = null;
    private GraphicViewerPort myToPort = null;
    private int myCurviness = 10;

    public GraphicViewerLink() {
        init();
    }

    public GraphicViewerLink(GraphicViewerPort paramGraphicViewerPort1,
            GraphicViewerPort paramGraphicViewerPort2) {
        init();
        myFromPort = paramGraphicViewerPort1;
        myToPort = paramGraphicViewerPort2;
        if (myFromPort != null) {
            myFromPort.addLink(this);
        }
        if (myToPort != null) {
            myToPort.addLink(this);
        }
        calculateStroke();
    }

    private final void init() {
        setInternalFlags(getInternalFlags() & -9 & 0xfffffbff);
        myLinkFlags = 0x10000;
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerLink graphicviewerlink = (GraphicViewerLink) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerlink != null) {
            graphicviewerlink.myPartID = myPartID;
            graphicviewerlink.myUserObject = myUserObject;
            graphicviewerlink.myLinkFlags = myLinkFlags;
            graphicviewercopyenvironment.delay(this);
            graphicviewerlink.myFromPort = null;
            graphicviewerlink.myToPort = null;
        }
        return graphicviewerlink;
    }

    public void copyObjectDelayed(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment,
            GraphicViewerObject graphicviewerobject) {
        if (!(graphicviewerobject instanceof GraphicViewerLink)) {
            return;
        }
        GraphicViewerLink graphicviewerlink = this;
        GraphicViewerLink graphicviewerlink1 = (GraphicViewerLink) graphicviewerobject;
        GraphicViewerPort graphicviewerport = graphicviewerlink.getFromPort();
        GraphicViewerPort graphicviewerport1 = graphicviewerlink.getToPort();
        Object obj = graphicviewercopyenvironment.get(graphicviewerport);
        Object obj1 = graphicviewercopyenvironment.get(graphicviewerport1);
        if ((obj instanceof GraphicViewerPort)
                && (obj1 instanceof GraphicViewerPort)) {
            GraphicViewerPort graphicviewerport2 = (GraphicViewerPort) obj;
            GraphicViewerPort graphicviewerport3 = (GraphicViewerPort) obj1;
            graphicviewerlink1.myFromPort = graphicviewerport2;
            graphicviewerlink1.myToPort = graphicviewerport3;
            graphicviewerport2.addLink(graphicviewerlink1);
            graphicviewerport3.addLink(graphicviewerlink1);
        } else if (graphicviewerlink1.getLayer() != null) {
            graphicviewerlink1.getLayer().removeObject(graphicviewerlink1);
        }
    }

    public void unlink() {
        GraphicViewerLayer graphicviewerlayer = getLayer();
        if (graphicviewerlayer != null) {
            GraphicViewerPort graphicviewerport = getFromPort();
            if (graphicviewerport != null) {
                graphicviewerport.removeLink(this);
            }
            graphicviewerport = getToPort();
            if (graphicviewerport != null) {
                graphicviewerport.removeLink(this);
            }
            graphicviewerlayer.removeObject(this);
        } else {
            GraphicViewerView graphicviewerview = getView();
            if (graphicviewerview != null) {
                GraphicViewerPort graphicviewerport1 = getFromPort();
                if (graphicviewerport1 != null) {
                    graphicviewerport1.removeLink(this);
                }
                graphicviewerport1 = getToPort();
                if (graphicviewerport1 != null) {
                    graphicviewerport1.removeLink(this);
                }
                graphicviewerview.removeObject(this);
            }
        }
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        if (!isResizable()) {
            super.gainedSelection(graphicviewerselection);
        } else {
            int i = getFirstPickPoint();
            int j = getLastPickPoint();
            boolean flag = isRelinkable();
            for (int i1 = i + 1; i1 <= j - 1; i1++) {
                Point point1 = getPoint(i1);
                if (point1 == null) {
                    continue;
                }
                int j1 = 100 + i1;
                if (isOrthogonal()) {
                    if (getNumPoints() < 6) {
                        j1 = -1;
                    } else if (i1 == i + 1) {
                        Point point2 = getPoint(i);
                        if (point2.y == point1.y && point2.x != point1.x) {
                            j1 = 8;
                        } else if (point2.x == point1.x && point2.y != point1.y) {
                            j1 = 2;
                        } else if (point2.x == point1.x && point2.y == point1.y
                                && i + 2 <= j) {
                            Point point4 = getPoint(i + 2);
                            if (point4.y == point1.y && point4.x != point1.x) {
                                j1 = 2;
                            } else if (point4.x == point1.x
                                    && point4.y != point1.y) {
                                j1 = 8;
                            }
                        }
                    } else if (i1 == j - 1) {
                        Point point3 = getPoint(j);
                        if (point1.y == point3.y && point1.x != point3.x) {
                            j1 = 4;
                        } else if (point1.x == point3.x && point1.y != point3.y) {
                            j1 = 6;
                        } else if (point3.x == point1.x && point3.y == point1.y
                                && j - 2 >= i) {
                            Point point5 = getPoint(j - 2);
                            if (point5.y == point1.y && point5.x != point1.x) {
                                j1 = 6;
                            } else if (point5.x == point1.x
                                    && point5.y != point1.y) {
                                j1 = 4;
                            }
                        }
                    }
                }
                graphicviewerselection.createResizeHandle(this, point1.x,
                        point1.y, j1, j1 != -1);
            }

            Point point = getPoint(i);
            graphicviewerselection.createResizeHandle(this, point.x, point.y,
                    flag ? 91 : -1, flag);
            point = getPoint(j);
            graphicviewerselection.createResizeHandle(this, point.x, point.y,
                    flag ? 92 : -1, flag);
        }
    }

    public Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int i1, int j1) {
        GraphicViewerPort graphicviewerport = null;
        GraphicViewerPort graphicviewerport1 = null;
        if (i == 91) {
            graphicviewerport = getToPort();
            graphicviewerport1 = getFromPort();
        } else if (i == 92) {
            graphicviewerport = getFromPort();
            graphicviewerport1 = getToPort();
        }
        if (graphicviewerport != null) {
            if (isDefaultResizingRelinks()) {
                GraphicViewerLink graphicviewerlink = this;
                if (graphicviewerlink.getFromPort() == graphicviewerport) {
                    graphicviewerlink.setToPort(null);
                } else if (graphicviewerlink.getToPort() == graphicviewerport) {
                    graphicviewerlink.setFromPort(null);
                }
                graphicviewerview.startReLink(graphicviewerlink,
                        graphicviewerport1, point);
            } else {
                unlink();
                graphicviewerview.startNewLink(graphicviewerport, point);
            }
        } else {
            int k1 = getFirstPickPoint() + 1;
            int l1 = getLastPickPoint() - 1;
            switch (i) {
                case 2 : // '\002'
                    setPoint(k1, getPoint(k1 - 1).x, point.y);
                    setPoint(k1 + 1, getPoint(k1 + 2).x, point.y);
                    break;

                case 8 : // '\b'
                    setPoint(k1, point.x, getPoint(k1 - 1).y);
                    setPoint(k1 + 1, point.x, getPoint(k1 + 2).y);
                    break;

                case 6 : // '\006'
                    setPoint(l1 - 1, getPoint(l1 - 2).x, point.y);
                    setPoint(l1, getPoint(l1 + 1).x, point.y);
                    break;

                case 4 : // '\004'
                    setPoint(l1 - 1, point.x, getPoint(l1 - 2).y);
                    setPoint(l1, point.x, getPoint(l1 + 1).y);
                    break;

                case 3 : // '\003'
                case 5 : // '\005'
                case 7 : // '\007'
                default :
                    if (i < 100) {
                        break;
                    }
                    int i2 = i - 100;
                    Point point1 = getPoint(i2);
                    if (isOrthogonal()) {
                        Point point2 = getPoint(i2 - 1);
                        Point point3 = getPoint(i2 + 1);
                        if (point2.x == point1.x && point1.y == point3.y) {
                            setPoint(i2 - 1, point.x, point2.y);
                            setPoint(i2 + 1, point3.x, point.y);
                        } else if (point2.y == point1.y && point1.x == point3.x) {
                            setPoint(i2 - 1, point2.x, point.y);
                            setPoint(i2 + 1, point.x, point3.y);
                        } else if (point2.x == point1.x && point1.x == point3.x) {
                            setPoint(i2 - 1, point.x, point2.y);
                            setPoint(i2 + 1, point.x, point3.y);
                        } else if (point2.y == point1.y && point1.y == point3.y) {
                            setPoint(i2 - 1, point2.x, point.y);
                            setPoint(i2 + 1, point3.x, point.y);
                        }
                    }
                    setPoint(i2, point);
                    int j2 = getNumPoints();
                    if (j2 < 3) {
                        break;
                    }
                    if (i2 == 1 && getFromPort() != null) {
                        Point point4 = new Point(0, 0);
                        setPoint(
                                0,
                                getFromPort().getLinkPointFromPoint(point.x,
                                        point.y, point4));
                    }
                    if (i2 == j2 - 2 && getToPort() != null) {
                        Point point5 = new Point(0, 0);
                        setPoint(
                                j2 - 1,
                                getToPort().getLinkPointFromPoint(point.x,
                                        point.y, point5));
                    }
                    break;
            }
        }
        return null;
    }

    protected void ownerChange(
            IGraphicViewerObjectCollection graphicviewerobjectcollection,
            IGraphicViewerObjectCollection graphicviewerobjectcollection1,
            GraphicViewerObject graphicviewerobject) {
        super.ownerChange(graphicviewerobjectcollection,
                graphicviewerobjectcollection1, graphicviewerobject);
        if (graphicviewerobjectcollection1 == null && !isNoClearPorts()
                && !isChildOf(graphicviewerobject)) {
            GraphicViewerPort graphicviewerport = getFromPort();
            if (graphicviewerport != null) {
                graphicviewerport.removeLink(this);
            }
            graphicviewerport = getToPort();
            if (graphicviewerport != null) {
                graphicviewerport.removeLink(this);
            }
        }
    }

    void setNoClearPorts(boolean flag) {
        int i = myLinkFlags;
        if (flag) {
            i |= 0x80000;
        } else {
            i &= 0xfff7ffff;
        }
        myLinkFlags = i;
    }

    boolean isNoClearPorts() {
        return (myLinkFlags & 0x80000) != 0;
    }

    public void portChange(GraphicViewerPort graphicviewerport, int i, int j,
            Object obj) {
        if (i != 301 && i != 302 && i != 0) {
            if (graphicviewerport != null && graphicviewerport == getFromPort()
                    && getNumPoints() > 0) {
                Point point = new Point(0, 0);
                point = graphicviewerport.getFromLinkPoint(this, point);
                Point point2 = getPoint(0);
                if (Math.abs(point.x - point2.x) > 1
                        || Math.abs(point.y - point2.y) > 1) {
                    calculateStroke();
                }
            } else if (graphicviewerport != null
                    && graphicviewerport == getToPort() && getNumPoints() >= 2) {
                Point point1 = new Point(0, 0);
                point1 = graphicviewerport.getToLinkPoint(this, point1);
                Point point3 = getPoint(getNumPoints() - 1);
                if (Math.abs(point1.x - point3.x) > 1
                        || Math.abs(point1.y - point3.y) > 1) {
                    calculateStroke();
                }
            } else {
                calculateStroke();
            }
        }
    }

    public int getFirstPickPoint() {
        GraphicViewerPort graphicviewerport = getFromPort();
        if (graphicviewerport == null) {
            return 0;
        }
        int i = getNumPoints();
        if (i <= 2) {
            return 0;
        }
        return graphicviewerport.getFromSpot() == -1 && !isOrthogonal() ? 0 : 1;
    }

    public int getLastPickPoint() {
        int i = getNumPoints();
        if (i == 0) {
            return 0;
        }
        GraphicViewerPort graphicviewerport = getToPort();
        if (graphicviewerport == null) {
            return i - 1;
        }
        if (i <= 2) {
            return i - 1;
        }
        if (graphicviewerport.getToSpot() != -1 || isOrthogonal()) {
            return i - 2;
        } else {
            return i - 1;
        }
    }

    public GraphicViewerPort getFromPort() {
        return myFromPort;
    }

    public GraphicViewerPort getToPort() {
        return myToPort;
    }

    public void setFromPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myFromPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                graphicviewerport1.removeLink(this);
            }
            myFromPort = graphicviewerport;
            if (myFromPort != null) {
                myFromPort.addLink(this);
            }
            removeAllPoints();
            calculateStroke();
            update(201, 0, graphicviewerport1);
        }
    }

    private void setFromPortNoCalc(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myFromPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                graphicviewerport1.removeLink(this);
            }
            myFromPort = graphicviewerport;
            if (myFromPort != null) {
                myFromPort.addLink(this);
            }
            update(201, 0, graphicviewerport1);
        }
    }

    public void setToPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myToPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                graphicviewerport1.removeLink(this);
            }
            myToPort = graphicviewerport;
            if (myToPort != null) {
                myToPort.addLink(this);
            }
            removeAllPoints();
            calculateStroke();
            update(202, 0, graphicviewerport1);
        }
    }

    private void setToPortNoCalc(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myToPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                graphicviewerport1.removeLink(this);
            }
            myToPort = graphicviewerport;
            if (myToPort != null) {
                myToPort.addLink(this);
            }
            update(202, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getOtherPort(GraphicViewerPort graphicviewerport) {
        if (getFromPort() == graphicviewerport) {
            return getToPort();
        }
        if (getToPort() == graphicviewerport) {
            return getFromPort();
        } else {
            return null;
        }
    }

    public boolean isCubic() {
        if (isSelfLoop() && !isOrthogonal()) {
            return true;
        } else {
            return super.isCubic();
        }
    }

    public boolean isSelfLoop() {
        return getFromPort() == getToPort() && getFromPort() != null;
    }

    public int getCurviness() {
        return myCurviness;
    }

    public void setCurviness(int i) {
        int j = myCurviness;
        if (j != i) {
            myCurviness = i;
            update(207, i, null);
        }
    }

    public void calculateStroke() {
        GraphicViewerPort graphicviewerport = getFromPort();
        GraphicViewerPort graphicviewerport1 = getToPort();
        if (graphicviewerport == null) {
            return;
        }
        if (graphicviewerport1 == null) {
            return;
        }
        int i = getNumPoints();
        int j = graphicviewerport.getFromSpot();
        int i1 = graphicviewerport1.getToSpot();
        boolean flag = isSelfLoop();
        boolean flag1 = isOrthogonal();
        boolean flag2 = isCubic();
        boolean flag3 = getAdjustingStyle() == 0;
        int j1 = getCurviness();
        boolean flag4 = isSuspendUpdates();
        if (!flag4) {
            foredate(110);
        }
        setSuspendUpdates(true);
        if (!flag1 && j == -1 && i1 == -1 && !flag) {
            boolean flag5 = false;
            if (!flag3 && i >= 3) {
                Point point1 = new Point(0, 0);
                Point point2 = getPoint(1);
                point1 = graphicviewerport.getLinkPointFromPoint(point2.x,
                        point2.y, point1);
                int i2 = point1.x;
                int j2 = point1.y;
                point2 = getPoint(i - 2);
                point1 = graphicviewerport1.getLinkPointFromPoint(point2.x,
                        point2.y, point1);
                int k2 = point1.x;
                int i3 = point1.y;
                flag5 = adjustPoints(0, i2, j2, i - 1, k2, i3);
            }
            if (!flag5) {
                if (flag2) {
                    calculateBezierNoSpot(graphicviewerport, graphicviewerport1);
                } else {
                    calculateLineNoSpot(graphicviewerport, graphicviewerport1);
                }
            }
        } else {
            Point point = new Point(0, 0);
            point = graphicviewerport.getFromLinkPoint(this, point);
            int k1 = 0;
            int l1 = 0;
            double d = 0.0D;
            if (flag1 || j != -1 || flag) {
                int l2 = graphicviewerport.getEndSegmentLength();
                d = graphicviewerport.getFromLinkDir(this);
                if (flag) {
                    if (flag1) {
                        if (d == 0.0D) {
                            d = 4.7123889803846897D;
                        } else if (d == 1.5707963267948966D) {
                            d = 0.0D;
                        } else if (d == 3.1415926535897931D) {
                            d = 1.5707963267948966D;
                        } else if (d == 4.7123889803846897D) {
                            d = 3.1415926535897931D;
                        }
                    } else {
                        d -= 0.5D;
                        if (j1 < 0) {
                            d -= 3.1415926535897931D;
                        }
                    }
                }
                if (flag2 && i >= 4) {
                    l2 += 15;
                    if (flag) {
                        l2 += Math.abs(j1);
                    }
                }
                if (d == 0.0D) {
                    k1 = l2;
                } else if (d == 1.5707963267948966D) {
                    l1 = l2;
                } else if (d == 3.1415926535897931D) {
                    k1 = -l2;
                } else if (d == 4.7123889803846897D) {
                    l1 = -l2;
                } else {
                    k1 = (int) Math.rint((double) l2 * Math.cos(d));
                    l1 = (int) Math.rint((double) l2 * Math.sin(d));
                }
                if (j == -1 && flag) {
                    point = graphicviewerport.getLinkPointFromPoint(point.x
                            + k1 * 1000, point.y + l1 * 1000, point);
                }
            }
            Point point3 = new Point(0, 0);
            point3 = graphicviewerport1.getToLinkPoint(this, point3);
            int j3 = 0;
            int k3 = 0;
            double d1 = -1D;
            if (flag1 || i1 != -1 || flag) {
                int l3 = graphicviewerport1.getEndSegmentLength();
                d1 = graphicviewerport1.getToLinkDir(this);
                if (flag && !flag1) {
                    d1 += 0.5D;
                    if (j1 < 0) {
                        d1 += 3.1415926535897931D;
                    }
                }
                if (flag2 && i >= 4) {
                    l3 += 15;
                    if (flag) {
                        l3 += Math.abs(j1);
                    }
                }
                if (d1 == 0.0D) {
                    j3 = l3;
                } else if (d1 == 1.5707963267948966D) {
                    k3 = l3;
                } else if (d1 == 3.1415926535897931D) {
                    j3 = -l3;
                } else if (d1 == 4.7123889803846897D) {
                    k3 = -l3;
                } else {
                    j3 = (int) Math.rint((double) l3 * Math.cos(d1));
                    k3 = (int) Math.rint((double) l3 * Math.sin(d1));
                }
                if (i1 == -1 && flag) {
                    point3 = graphicviewerport1.getLinkPointFromPoint(point3.x
                            + j3 * 1000, point3.y + k3 * 1000, point3);
                }
            }
            if (flag1 || flag) {
                point = graphicviewerport.getLinkPointFromPoint(point.x + k1
                        * 1000, point.y + l1 * 1000, point);
            } else if (j == -1) {
                point = graphicviewerport.getLinkPointFromPoint(point3.x + j3,
                        point3.y + k3, point);
            }
            if (flag1 || flag) {
                point3 = graphicviewerport1.getLinkPointFromPoint(point3.x + j3
                        * 1000, point3.y + k3 * 1000, point3);
            } else if (i1 == -1) {
                point3 = graphicviewerport1.getLinkPointFromPoint(point.x + k1,
                        point.y + l1, point3);
            }
            int i4 = point.x;
            int j4 = point.y;
            if (flag1 || j != -1 || flag) {
                i4 += k1;
                j4 += l1;
            }
            int k4 = point3.x;
            int l4 = point3.y;
            if (flag1 || i1 != -1 || flag) {
                k4 += j3;
                l4 += k3;
            }
            if (!flag3 && !flag1 && j == -1 && i > 3
                    && adjustPoints(0, point.x, point.y, i - 2, k4, l4)) {
                setPoint(i - 1, point3);
            } else if (!flag3 && !flag1 && i1 == -1 && i > 3
                    && adjustPoints(1, i4, j4, i - 1, point3.x, point3.y)) {
                setPoint(0, point);
            } else if (!flag3 && !flag1 && i > 4
                    && adjustPoints(1, i4, j4, i - 2, k4, l4)) {
                setPoint(0, point);
                setPoint(i - 1, point3);
            } else if (!flag3 && flag1 && i >= 6 && !isAvoidsNodes()
                    && adjustPoints(1, i4, j4, i - 2, k4, l4)) {
                setPoint(0, point);
                setPoint(i - 1, point3);
            } else {
                removeAllPoints();
                addPoint(point);
                if (flag1 || j != -1 || flag) {
                    addPoint(i4, j4);
                }
                if (flag1) {
                    addOrthoPoints(i4, j4, d, k4, l4, d1);
                }
                if (flag1 || i1 != -1 || flag) {
                    addPoint(k4, l4);
                }
                addPoint(point3);
            }
        }
        setBoundingRectInvalid(true);
        setSuspendUpdates(flag4);
        if (!flag4) {
            update(110, 0, null);
        }
    }

    private void calculateLineNoSpot(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        removeAllPoints();
        Point point = new Point(0, 0);
        point = graphicviewerport.getLinkPointFromPoint(
                graphicviewerport1.getLeft() + graphicviewerport1.getWidth()
                        / 2,
                graphicviewerport1.getTop() + graphicviewerport1.getHeight()
                        / 2, point);
        int i = point.x;
        int j = point.y;
        point = graphicviewerport1.getLinkPointFromPoint(
                graphicviewerport.getLeft() + graphicviewerport.getWidth() / 2,
                graphicviewerport.getTop() + graphicviewerport.getHeight() / 2,
                point);
        int i1 = point.x;
        int j1 = point.y;
        addPoint(i, j);
        addPoint(i1, j1);
    }

    private void calculateBezierNoSpot(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        removeAllPoints();
        Point point = new Point(0, 0);
        point = graphicviewerport.getLinkPointFromPoint(
                graphicviewerport1.getLeft() + graphicviewerport1.getWidth()
                        / 2,
                graphicviewerport1.getTop() + graphicviewerport1.getHeight()
                        / 2, point);
        int i = point.x;
        int j = point.y;
        point = graphicviewerport1.getLinkPointFromPoint(
                graphicviewerport.getLeft() + graphicviewerport.getWidth() / 2,
                graphicviewerport.getTop() + graphicviewerport.getHeight() / 2,
                point);
        int i1 = point.x;
        int j1 = point.y;
        double d = i1 - i;
        double d1 = j1 - j;
        double d3 = getCurviness();
        double d4 = Math.abs(d3);
        if (d3 < 0.0D) {
            d4 = -d4;
        }
        double d5 = 0.0D;
        double d6 = 0.0D;
        double d7 = (double) i + d / 3D;
        double d8 = (double) j + d1 / 3D;
        double d9 = d7;
        double d10 = d8;
        if (Math.abs(d1) < 1.0D) {
            if (d > 0.0D) {
                d10 -= d4;
            } else {
                d10 += d4;
            }
        } else {
            d5 = -d / d1;
            d6 = Math.sqrt((d4 * d4) / (d5 * d5 + 1.0D));
            if (d3 < 0.0D) {
                d6 = -d6;
            }
            d9 = (double) (d1 >= 0.0D ? 1 : -1) * d6 + d7;
            d10 = d5 * (d9 - d7) + d8;
        }
        d7 = (double) i + (2D * d) / 3D;
        d8 = (double) j + (2D * d1) / 3D;
        double d11 = d7;
        double d12 = d8;
        if (Math.abs(d1) < 1.0D) {
            if (d > 0.0D) {
                d12 -= d4;
            } else {
                d12 += d4;
            }
        } else {
            d11 = (double) (d1 >= 0.0D ? 1 : -1) * d6 + d7;
            d12 = d5 * (d11 - d7) + d8;
        }
        addPoint(i, j);
        int k1 = (int) Math.rint(d9);
        int l1 = (int) Math.rint(d10);
        addPoint(k1, l1);
        int i2 = (int) Math.rint(d11);
        int j2 = (int) Math.rint(d12);
        addPoint(i2, j2);
        addPoint(i1, j1);
        setPoint(0, graphicviewerport.getLinkPointFromPoint(k1, l1, point));
        setPoint(3, graphicviewerport1.getLinkPointFromPoint(i2, j2, point));
    }

    protected boolean adjustPoints(int i, int j, int i1, int j1, int k1, int l1) {
        int i2 = getAdjustingStyle();
        if (isOrthogonal()) {
            if (i2 == 1) {
                return false;
            }
            if (i2 == 2) {
                i2 = 3;
            }
        }
        switch (i2) {
            case 1 : // '\001'
                return rescalePoints(i, j, i1, j1, k1, l1);

            case 2 : // '\002'
                return stretchPoints(i, j, i1, j1, k1, l1);

            case 3 : // '\003'
                return modifyEndPoints(i, j, i1, j1, k1, l1);
        }
        return false;
    }

    protected boolean rescalePoints(int i, int j, int i1, int j1, int k1, int l1) {
        Point point = getPoint(i);
        Point point1 = getPoint(j1);
        if (point.x == j && point.y == i1 && point1.x == k1 && point1.y == l1) {
            return true;
        }
        double d = point.x;
        double d1 = point.y;
        double d2 = point1.x;
        double d3 = point1.y;
        double d4 = d2 - d;
        double d6 = d3 - d1;
        double d8 = Math.sqrt(d4 * d4 + d6 * d6);
        if (d8 < 1.0D) {
            d8 = 1.0D;
        }
        double d9;
        if (d4 == 0.0D) {
            if (d6 < 0.0D) {
                d9 = -1.5707963267948966D;
            } else {
                d9 = 1.5707963267948966D;
            }
        } else {
            d9 = Math.atan(d6 / Math.abs(d4));
            if (d4 < 0.0D) {
                d9 = 3.1415926535897931D - d9;
            }
        }
        double d10 = j;
        double d11 = i1;
        double d12 = k1;
        double d13 = l1;
        double d14 = d12 - d10;
        double d15 = d13 - d11;
        double d16 = Math.sqrt(d14 * d14 + d15 * d15);
        double d17;
        if (d14 == 0.0D) {
            if (d15 < 0.0D) {
                d17 = -1.5707963267948966D;
            } else {
                d17 = 1.5707963267948966D;
            }
        } else {
            d17 = Math.atan(d15 / Math.abs(d14));
            if (d14 < 0.0D) {
                d17 = 3.1415926535897931D - d17;
            }
        }
        double d18 = d16 / d8;
        double d19 = d17 - d9;
        setPoint(i, j, i1);
        for (int i2 = i + 1; i2 < j1; i2++) {
            Point point2 = getPoint(i2);
            double d5 = (double) point2.x - d;
            double d7 = (double) point2.y - d1;
            double d20 = Math.sqrt(d5 * d5 + d7 * d7);
            if (d20 < 1.0D) {
                d20 = 1.0D;
            }
            double d21;
            if (d5 == 0.0D) {
                if (d7 < 0.0D) {
                    d21 = -1.5707963267948966D;
                } else {
                    d21 = 1.5707963267948966D;
                }
            } else {
                d21 = Math.atan(d7 / Math.abs(d5));
                if (d5 < 0.0D) {
                    d21 = 3.1415926535897931D - d21;
                }
            }
            double d22 = d21 + d19;
            double d23 = d20 * d18;
            double d24 = d10 + d23 * Math.cos(d22);
            double d25 = d11 + d23 * Math.sin(d22);
            setPoint(i2, (int) Math.rint(d24), (int) Math.rint(d25));
        }

        setPoint(j1, k1, l1);
        return true;
    }

    protected boolean stretchPoints(int i, int j, int i1, int j1, int k1, int l1) {
        Point point = getPoint(i);
        Point point1 = getPoint(j1);
        if (point.x == j && point.y == i1 && point1.x == k1 && point1.y == l1) {
            return true;
        }
        double d = point.x;
        double d1 = point.y;
        double d2 = point1.x;
        double d3 = point1.y;
        double d4 = (d2 - d) * (d2 - d) + (d3 - d1) * (d3 - d1);
        double d5 = j;
        double d6 = i1;
        double d7 = k1;
        double d8 = l1;
        double d9 = 0.0D;
        double d10 = 1.0D;
        if (d7 - d5 != 0.0D) {
            d9 = (d8 - d6) / (d7 - d5);
        } else {
            d9 = 9900000000D;
        }
        if (d9 != 0.0D) {
            d10 = Math.sqrt(1.0D + 1.0D / (d9 * d9));
        }
        setPoint(i, j, i1);
        for (int i2 = i + 1; i2 < j1; i2++) {
            Point point2 = getPoint(i2);
            double d11 = point2.x;
            double d12 = point2.y;
            double d13 = 0.5D;
            if (d4 != 0.0D) {
                d13 = ((d - d11) * (d - d2) + (d1 - d12) * (d1 - d3)) / d4;
            }
            double d14 = d + d13 * (d2 - d);
            double d15 = d1 + d13 * (d3 - d1);
            double d16 = Math.sqrt((d11 - d14) * (d11 - d14) + (d12 - d15)
                    * (d12 - d15));
            if (d12 < d9 * (d11 - d14) + d15) {
                d16 = -d16;
            }
            if (d9 > 0.0D) {
                d16 = -d16;
            }
            double d17 = d5 + d13 * (d7 - d5);
            double d18 = d6 + d13 * (d8 - d6);
            if (d9 != 0.0D) {
                double d19 = d17 + d16 / d10;
                double d20 = d18 - (d19 - d17) / d9;
                setPoint(i2, (int) Math.rint(d19), (int) Math.rint(d20));
            } else {
                setPoint(i2, (int) Math.rint(d17), (int) Math.rint(d18 + d16));
            }
        }

        setPoint(j1, k1, l1);
        return true;
    }

    protected boolean modifyEndPoints(int i, int j, int i1, int j1, int k1,
            int l1) {
        if (isOrthogonal()) {
            Point point = getPoint(i + 1);
            Point point1 = getPoint(i + 2);
            if (point.x == point1.x && point.y != point1.y) {
                setPoint(i + 1, point.x, i1);
            } else if (point.y == point1.y) {
                setPoint(i + 1, j, point.y);
            }
            point = getPoint(j1 - 1);
            point1 = getPoint(j1 - 2);
            if (point.x == point1.x && point.y != point1.y) {
                setPoint(j1 - 1, point.x, l1);
            } else if (point.y == point1.y) {
                setPoint(j1 - 1, k1, point.y);
            }
        }
        setPoint(i, j, i1);
        setPoint(j1, k1, l1);
        return true;
    }

    protected void addOrthoPoints(int i, int j, double d, int i1, int j1,
            double d1) {
        if (d != 0.0D && d != 1.5707963267948966D && d != 3.1415926535897931D
                && d != 4.7123889803846897D) {
            return;
        }
        if (d1 != 0.0D && d1 != 1.5707963267948966D
                && d1 != 3.1415926535897931D && d1 != 4.7123889803846897D) {
            return;
        }
        int k1 = i;
        int l1 = j;
        int i2 = i1;
        int j2 = j1;
        int k2 = getPen() == null ? 1 : getPen().getWidth() + 1;
        GraphicViewerObject graphicviewerobject = getFromPort()
                .getTopLevelObject();
        Rectangle rectangle = graphicviewerobject.getBoundingRect();
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        rectangle1.x -= k2;
        rectangle1.y -= k2;
        rectangle1.width += k2 * 2;
        rectangle1.height += k2 * 2;
        GraphicViewerObject graphicviewerobject1 = getToPort()
                .getTopLevelObject();
        rectangle = graphicviewerobject1.getBoundingRect();
        Rectangle rectangle2 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        rectangle2.x -= k2;
        rectangle2.y -= k2;
        rectangle2.width += k2 * 2;
        rectangle2.height += k2 * 2;
        if (isAvoidsNodes() && getDocument() != null) {
            GraphicViewerPositionArray graphicviewerpositionarray = getDocument()
                    .getPositions();
            int i3 = Math.min(rectangle1.x, rectangle2.x);
            int k3 = Math.min(rectangle1.y, rectangle2.y);
            int i4 = Math.max(rectangle1.x + rectangle1.width, rectangle2.x
                    + rectangle2.width);
            int k4 = Math.max(rectangle1.y + rectangle1.height, rectangle2.y
                    + rectangle2.height);
            graphicviewerpositionarray.propagate(i, j, d, i1, j1, d1, i3 - 20,
                    k3 - 20, i4 + 20, k4 + 20);
            int l4 = graphicviewerpositionarray.getDist(i1, j1);
            if (l4 >= 0x7fffffff) {
                graphicviewerpositionarray.setAllUnoccupied(0x7fffffff);
                Rectangle rectangle3 = graphicviewerpositionarray.getBounds();
                graphicviewerpositionarray.propagate(i, j, d, i1, j1, d1,
                        rectangle3.x, rectangle3.y, rectangle3.x
                                + rectangle3.width, rectangle3.y
                                + rectangle3.height);
                l4 = graphicviewerpositionarray.getDist(i1, j1);
            }
            if (l4 < 0x7fffffff
                    && !graphicviewerpositionarray.isOccupied(i1, j1)) {
                traversePositions(graphicviewerpositionarray, i1, j1, d1, true);
                Point point = getPoint(2);
                if (getNumPoints() < 4) {
                    if (d == 0.0D || d == 3.1415926535897931D) {
                        point.x = i;
                        point.y = j1;
                    } else {
                        point.x = i1;
                        point.y = j;
                    }
                    setPoint(2, point);
                    insertPoint(3, point);
                } else {
                    Point point1 = getPoint(3);
                    if (d == 0.0D || d == 3.1415926535897931D) {
                        if (point.x == point1.x) {
                            int i5 = d != 0.0D ? Math.min(point.x, i) : Math
                                    .max(point.x, i);
                            setPoint(2, i5, j);
                            setPoint(3, i5, point1.y);
                        } else if (point.y == point1.y) {
                            if (Math.abs(j - point.y) <= graphicviewerpositionarray
                                    .getCellSize().height / 2) {
                                setPoint(2, point.x, j);
                                setPoint(3, point1.x, j);
                            }
                            insertPoint(2, point.x, j);
                        } else {
                            setPoint(2, i, point.y);
                        }
                    } else if (d == 1.5707963267948966D
                            || d == 4.7123889803846897D) {
                        if (point.y == point1.y) {
                            int j5 = d != 1.5707963267948966D ? Math.min(
                                    point.y, j) : Math.max(point.y, j);
                            setPoint(2, i, j5);
                            setPoint(3, point1.x, j5);
                        } else if (point.x == point1.x) {
                            if (Math.abs(i - point.x) <= graphicviewerpositionarray
                                    .getCellSize().width / 2) {
                                setPoint(2, i, point.y);
                                setPoint(3, i, point1.y);
                            }
                            insertPoint(2, i, point.y);
                        } else {
                            setPoint(2, point.x, j);
                        }
                    }
                }
                return;
            }
        }
        int l2;
        int j3;
        int l3;
        int j4;
        if (d == 0.0D) {
            if (i2 > k1 || d1 == 4.7123889803846897D && j2 < l1
                    && rectangle2.x + rectangle2.width > k1
                    || d1 == 1.5707963267948966D && j2 > l1
                    && rectangle2.x + rectangle2.width > k1) {
                l2 = i2;
                j3 = l1;
                l3 = i2;
                j4 = (l1 + j2) / 2;
                if (d1 == 3.1415926535897931D) {
                    l2 = getMidOrthoPosition(k1, i2, false);
                    l3 = l2;
                    j4 = j2;
                } else if (d1 == 4.7123889803846897D && j2 < l1
                        || d1 == 1.5707963267948966D && j2 >= l1) {
                    if (k1 < rectangle2.x) {
                        l2 = getMidOrthoPosition(k1, rectangle2.x, false);
                    } else if (k1 < i2 && l1 < rectangle2.y + rectangle2.height) {
                        l2 = getMidOrthoPosition(k1, i2, false);
                    } else {
                        l2 = rectangle2.x + rectangle2.width;
                    }
                    l3 = l2;
                    j4 = j2;
                } else if (d1 == 0.0D && j3 > rectangle2.y
                        && j3 < rectangle2.y + rectangle2.height) {
                    l2 = k1;
                    if (l1 < j2) {
                        j3 = Math.min(j2, rectangle2.y);
                    } else {
                        j3 = Math.max(j2, rectangle2.y + rectangle2.height);
                    }
                    j4 = j3;
                }
            } else {
                l2 = k1;
                j3 = j2;
                l3 = (k1 + i2) / 2;
                j4 = j2;
                if (d1 == 3.1415926535897931D || d1 == 1.5707963267948966D
                        && j2 < rectangle1.y || d1 == 4.7123889803846897D
                        && j2 > rectangle1.y + rectangle1.height) {
                    if (j2 < l1
                            && (d1 == 3.1415926535897931D || d1 == 1.5707963267948966D)) {
                        j3 = getMidOrthoPosition(rectangle1.y,
                                Math.max(j2, rectangle2.y + rectangle2.height),
                                true);
                    } else if (j2 >= l1
                            && (d1 == 3.1415926535897931D || d1 == 4.7123889803846897D)) {
                        j3 = getMidOrthoPosition(rectangle1.y
                                + rectangle1.height,
                                Math.min(j2, rectangle2.y), true);
                    }
                    l3 = i2;
                    j4 = j3;
                }
                if (j3 > rectangle1.y && j3 < rectangle1.y + rectangle1.height) {
                    if (i2 >= rectangle1.x && i2 <= k1
                            || k1 <= rectangle2.x + rectangle2.width
                            && k1 >= i2) {
                        if (d1 == 0.0D || d1 == 3.1415926535897931D) {
                            l2 = k1;
                            j3 = (l1 + j2) / 2;
                            l3 = i2;
                            j4 = j3;
                        } else {
                            l2 = Math.max((k1 + i2) / 2, k1);
                            j3 = l1;
                            l3 = l2;
                            j4 = j2;
                        }
                    } else {
                        l3 = i2;
                        if (d1 == 4.7123889803846897D
                                || (d1 == 0.0D || d1 == 3.1415926535897931D)
                                && j2 < l1) {
                            j3 = Math.min(j2,
                                    Math.min(rectangle1.y, rectangle2.y));
                        } else {
                            j3 = Math.max(j2, Math.max(rectangle1.y
                                    + rectangle1.height, rectangle2.y
                                    + rectangle2.height));
                        }
                        j4 = j3;
                    }
                }
            }
        } else if (d == 3.1415926535897931D) {
            if (i2 <= k1 || d1 == 4.7123889803846897D && j2 < l1
                    && rectangle2.x < k1 || d1 == 1.5707963267948966D
                    && j2 > l1 && rectangle2.x < k1) {
                l2 = i2;
                j3 = l1;
                l3 = i2;
                j4 = (l1 + j2) / 2;
                if (d1 == 0.0D) {
                    l2 = getMidOrthoPosition(k1, i2, false);
                    l3 = l2;
                    j4 = j2;
                } else if (d1 == 4.7123889803846897D && j2 < l1
                        || d1 == 1.5707963267948966D && j2 >= l1) {
                    if (k1 > rectangle2.x + rectangle2.width) {
                        l2 = getMidOrthoPosition(k1, rectangle2.x
                                + rectangle2.width, false);
                    } else if (k1 > i2 && l1 < rectangle2.y + rectangle2.height) {
                        l2 = getMidOrthoPosition(k1, i2, false);
                    } else {
                        l2 = rectangle2.x;
                    }
                    l3 = l2;
                    j4 = j2;
                }
                if (d1 == 3.1415926535897931D && j3 > rectangle2.y
                        && j3 < rectangle2.y + rectangle2.height) {
                    l2 = k1;
                    if (l1 < j2) {
                        j3 = Math.min(j2, rectangle2.y);
                    } else {
                        j3 = Math.max(j2, rectangle2.y + rectangle2.height);
                    }
                    j4 = j3;
                }
            } else {
                l2 = k1;
                j3 = j2;
                l3 = (k1 + i2) / 2;
                j4 = j2;
                if (d1 == 0.0D || d1 == 1.5707963267948966D
                        && j2 < rectangle1.y || d1 == 4.7123889803846897D
                        && j2 > rectangle1.y + rectangle1.height) {
                    if (j2 < l1 && (d1 == 0.0D || d1 == 1.5707963267948966D)) {
                        j3 = getMidOrthoPosition(rectangle1.y,
                                Math.max(j2, rectangle2.y + rectangle2.height),
                                true);
                    } else if (j2 >= l1
                            && (d1 == 0.0D || d1 == 4.7123889803846897D)) {
                        j3 = getMidOrthoPosition(rectangle1.y
                                + rectangle1.height,
                                Math.min(j2, rectangle2.y), true);
                    }
                    l3 = i2;
                    j4 = j3;
                }
                if (j3 > rectangle1.y && j3 < rectangle1.y + rectangle1.height) {
                    if (i2 >= rectangle1.x && i2 <= k1
                            || k1 <= rectangle2.x + rectangle2.width
                            && k1 >= i2) {
                        if (d1 == 0.0D || d1 == 3.1415926535897931D) {
                            l2 = k1;
                            j3 = (l1 + j2) / 2;
                            l3 = i2;
                            j4 = j3;
                        } else {
                            l2 = Math.min((k1 + i2) / 2, k1);
                            j3 = l1;
                            l3 = l2;
                            j4 = j2;
                        }
                    } else {
                        l3 = i2;
                        if (d1 == 4.7123889803846897D
                                || (d1 == 0.0D || d1 == 3.1415926535897931D)
                                && j2 < l1) {
                            j3 = Math.min(j2,
                                    Math.min(rectangle1.y, rectangle2.y));
                        } else {
                            j3 = Math.max(j2, Math.max(rectangle1.y
                                    + rectangle1.height, rectangle2.y
                                    + rectangle2.height));
                        }
                        j4 = j3;
                    }
                }
            }
        } else if (d == 1.5707963267948966D) {
            if (j2 > l1 || d1 == 3.1415926535897931D && i2 < k1
                    && rectangle2.y + rectangle2.height > l1 || d1 == 0.0D
                    && i2 > k1 && rectangle2.y + rectangle2.height > l1) {
                l2 = k1;
                j3 = j2;
                l3 = (k1 + i2) / 2;
                j4 = j2;
                if (d1 == 4.7123889803846897D) {
                    j3 = getMidOrthoPosition(l1, j2, true);
                    l3 = i2;
                    j4 = j3;
                } else if (d1 == 3.1415926535897931D && i2 < k1 || d1 == 0.0D
                        && i2 >= k1) {
                    if (l1 < rectangle2.y) {
                        j3 = getMidOrthoPosition(l1, rectangle2.y, true);
                    } else if (l1 < j2 && k1 < rectangle2.x + rectangle2.width) {
                        j3 = getMidOrthoPosition(l1, j2, true);
                    } else {
                        j3 = rectangle2.y + rectangle2.height;
                    }
                    l3 = i2;
                    j4 = j3;
                }
                if (d1 == 1.5707963267948966D && l2 > rectangle2.x
                        && l2 < rectangle2.x + rectangle2.width) {
                    if (k1 < i2) {
                        l2 = Math.min(i2, rectangle2.x);
                    } else {
                        l2 = Math.max(i2, rectangle2.x + rectangle2.width);
                    }
                    j3 = l1;
                    l3 = l2;
                }
            } else {
                l2 = i2;
                j3 = l1;
                l3 = i2;
                j4 = (l1 + j2) / 2;
                if (d1 == 4.7123889803846897D || d1 == 0.0D
                        && i2 < rectangle1.x || d1 == 3.1415926535897931D
                        && i2 > rectangle1.x + rectangle1.width) {
                    if (i2 < k1 && (d1 == 4.7123889803846897D || d1 == 0.0D)) {
                        l2 = getMidOrthoPosition(rectangle1.x,
                                Math.max(i2, rectangle2.x + rectangle2.width),
                                false);
                    } else if (i2 >= k1
                            && (d1 == 4.7123889803846897D || d1 == 3.1415926535897931D)) {
                        l2 = getMidOrthoPosition(rectangle1.x
                                + rectangle1.width, Math.min(i2, rectangle2.x),
                                false);
                    }
                    l3 = l2;
                    j4 = j2;
                }
                if (l2 > rectangle1.x && l2 < rectangle1.x + rectangle1.width) {
                    if (j2 >= rectangle1.y && j2 <= l1
                            || l1 <= rectangle2.y + rectangle2.height
                            && l1 >= j2) {
                        if (d1 == 0.0D || d1 == 3.1415926535897931D) {
                            l2 = k1;
                            j3 = Math.max((l1 + j2) / 2, l1);
                            l3 = i2;
                            j4 = j3;
                        } else {
                            l2 = (k1 + i2) / 2;
                            j3 = l1;
                            l3 = l2;
                            j4 = j2;
                        }
                    } else {
                        if (d1 == 3.1415926535897931D
                                || (d1 == 1.5707963267948966D || d1 == 4.7123889803846897D)
                                && i2 < k1) {
                            l2 = Math.min(i2,
                                    Math.min(rectangle1.x, rectangle2.x));
                        } else {
                            l2 = Math.max(i2, Math.max(rectangle1.x
                                    + rectangle1.width, rectangle2.x
                                    + rectangle2.width));
                        }
                        l3 = l2;
                        j4 = j2;
                    }
                }
            }
        } else if (j2 <= l1 || d1 == 3.1415926535897931D && i2 < k1
                && rectangle2.y < l1 || d1 == 0.0D && i2 > k1
                && rectangle2.y < l1) {
            l2 = k1;
            j3 = j2;
            l3 = (k1 + i2) / 2;
            j4 = j2;
            if (d1 == 1.5707963267948966D) {
                j3 = getMidOrthoPosition(l1, j2, true);
                l3 = i2;
                j4 = j3;
            } else if (d1 == 3.1415926535897931D && i2 < k1 || d1 == 0.0D
                    && i2 >= k1) {
                if (l1 > rectangle2.y + rectangle2.height) {
                    j3 = getMidOrthoPosition(l1, rectangle2.y
                            + rectangle2.height, true);
                } else if (l1 > j2 && k1 < rectangle2.x + rectangle2.width) {
                    j3 = getMidOrthoPosition(l1, j2, true);
                } else {
                    j3 = rectangle2.y;
                }
                l3 = i2;
                j4 = j3;
            }
            if (d1 == 4.7123889803846897D && l2 > rectangle2.x
                    && l2 < rectangle2.x + rectangle2.width) {
                if (k1 < i2) {
                    l2 = Math.min(i2, rectangle2.x);
                } else {
                    l2 = Math.max(i2, rectangle2.x + rectangle2.width);
                }
                j3 = l1;
                l3 = l2;
            }
        } else {
            l2 = i2;
            j3 = l1;
            l3 = i2;
            j4 = (l1 + j2) / 2;
            if (d1 == 1.5707963267948966D || d1 == 0.0D && i2 < rectangle1.x
                    || d1 == 3.1415926535897931D
                    && i2 > rectangle1.x + rectangle1.width) {
                if (i2 < k1 && (d1 == 1.5707963267948966D || d1 == 0.0D)) {
                    l2 = getMidOrthoPosition(rectangle1.x,
                            Math.max(i2, rectangle2.x + rectangle2.width),
                            false);
                } else if (i2 >= k1
                        && (d1 == 1.5707963267948966D || d1 == 3.1415926535897931D)) {
                    l2 = getMidOrthoPosition(rectangle1.x + rectangle1.width,
                            Math.min(i2, rectangle2.x), false);
                }
                l3 = l2;
                j4 = j2;
            }
            if (l2 > rectangle1.x && l2 < rectangle1.x + rectangle1.width) {
                if (j2 >= rectangle1.y && j2 <= l1
                        || l1 <= rectangle2.y + rectangle2.height && l1 >= j2) {
                    if (d1 == 0.0D || d1 == 3.1415926535897931D) {
                        l2 = k1;
                        j3 = Math.min((l1 + j2) / 2, l1);
                        l3 = i2;
                        j4 = j3;
                    } else {
                        l2 = (k1 + i2) / 2;
                        j3 = l1;
                        l3 = l2;
                        j4 = j2;
                    }
                } else {
                    if (d1 == 3.1415926535897931D
                            || (d1 == 1.5707963267948966D || d1 == 4.7123889803846897D)
                            && i2 < k1) {
                        l2 = Math.min(i2, Math.min(rectangle1.x, rectangle2.x));
                    } else {
                        l2 = Math.max(i2, Math.max(rectangle1.x
                                + rectangle1.width, rectangle2.x
                                + rectangle2.width));
                    }
                    l3 = l2;
                    j4 = j2;
                }
            }
        }
        addPoint(l2, j3);
        addPoint(l3, j4);
    }

    protected int getMidOrthoPosition(int i, int j, boolean flag) {
        return (i + j) / 2;
    }

    private void traversePositions(
            GraphicViewerPositionArray graphicviewerpositionarray, int i,
            int j, double d, boolean flag) {
        Dimension dimension = graphicviewerpositionarray.getCellSize();
        int i1 = graphicviewerpositionarray.getDist(i, j);
        int j1 = i;
        int k1 = j;
        int l1 = j1;
        int i2 = k1;
        if (d == 0.0D) {
            l1 += dimension.width;
        } else if (d == 1.5707963267948966D) {
            i2 += dimension.height;
        } else if (d == 3.1415926535897931D) {
            l1 -= dimension.width;
        } else {
            i2 -= dimension.height;
        }
        for (; i1 > 1 && graphicviewerpositionarray.getDist(l1, i2) == i1 - 1; i1--) {
            j1 = l1;
            k1 = i2;
            if (d == 0.0D) {
                l1 += dimension.width;
                continue;
            }
            if (d == 1.5707963267948966D) {
                i2 += dimension.height;
                continue;
            }
            if (d == 3.1415926535897931D) {
                l1 -= dimension.width;
            } else {
                i2 -= dimension.height;
            }
        }

        if (flag) {
            if (i1 > 1) {
                if (d == 3.1415926535897931D || d == 0.0D) {
                    j1 = (int) Math.floor((double) j1
                            / (double) dimension.width)
                            * dimension.width + dimension.width / 2;
                } else {
                    k1 = (int) Math.floor((double) k1
                            / (double) dimension.height)
                            * dimension.height + dimension.height / 2;
                }
            }
        } else {
            j1 = (int) Math.floor((double) j1 / (double) dimension.width)
                    * dimension.width + dimension.width / 2;
            k1 = (int) Math.floor((double) k1 / (double) dimension.height)
                    * dimension.height + dimension.height / 2;
        }
        if (i1 > 1) {
            double d1 = d;
            int j2 = j1;
            int k2 = k1;
            if (d == 0.0D) {
                d1 = 1.5707963267948966D;
                k2 += dimension.height;
            } else if (d == 1.5707963267948966D) {
                d1 = 3.1415926535897931D;
                j2 -= dimension.width;
            } else if (d == 3.1415926535897931D) {
                d1 = 4.7123889803846897D;
                k2 -= dimension.height;
            } else if (d == 4.7123889803846897D) {
                d1 = 0.0D;
                j2 += dimension.width;
            }
            if (graphicviewerpositionarray.getDist(j2, k2) == i1 - 1) {
                traversePositions(graphicviewerpositionarray, j2, k2, d1, false);
            } else {
                int l2 = j1;
                int i3 = k1;
                if (d == 0.0D) {
                    d1 = 4.7123889803846897D;
                    i3 -= dimension.height;
                } else if (d == 1.5707963267948966D) {
                    d1 = 0.0D;
                    l2 += dimension.width;
                } else if (d == 3.1415926535897931D) {
                    d1 = 1.5707963267948966D;
                    i3 += dimension.height;
                } else if (d == 4.7123889803846897D) {
                    d1 = 3.1415926535897931D;
                    l2 -= dimension.width;
                }
                if (graphicviewerpositionarray.getDist(l2, i3) == i1 - 1) {
                    traversePositions(graphicviewerpositionarray, l2, i3, d1,
                            false);
                }
            }
        }
        addPoint(j1, k1);
    }

    public boolean isOrthogonal() {
        return (getInternalFlags() & 0x2000) != 0;
    }

    public void setOrthogonal(boolean flag) {
        boolean flag1 = (getInternalFlags() & 0x2000) != 0;
        if (flag1 != flag) {
            int i = getInternalFlags();
            if (flag) {
                i |= 0x2000;
            } else {
                i &= 0xffffdfff;
            }
            setInternalFlags(i);
            update(203, flag1 ? 1 : 0, null);
            if (!isInitializing() && flag) {
                removeAllPoints();
                calculateStroke();
            }
        }
    }

    public void setJumpsOver(boolean flag) {
        boolean flag1 = (myLinkFlags & 0x20000) != 0;
        if (flag1 != flag) {
            int i = myLinkFlags;
            if (flag) {
                i |= 0x20000;
            } else {
                i &= 0xfffdffff;
            }
            myLinkFlags = i;
            resetPath();
            update(205, flag1 ? 1 : 0, null);
        }
    }

    public boolean isJumpsOver() {
        return (myLinkFlags & 0x20000) != 0;
    }

    public void setRoundedCorners(boolean flag) {
        boolean flag1 = (myLinkFlags & 0x100000) != 0;
        if (flag1 != flag) {
            int i = myLinkFlags;
            if (flag) {
                i |= 0x100000;
            } else {
                i &= 0xffefffff;
            }
            myLinkFlags = i;
            resetPath();
            update(211, flag1 ? 1 : 0, null);
        }
    }

    public boolean isRoundedCorners() {
        return (myLinkFlags & 0x100000) != 0;
    }

    public void setAvoidsNodes(boolean flag) {
        boolean flag1 = (myLinkFlags & 0x40000) != 0;
        if (flag1 != flag) {
            int i = myLinkFlags;
            if (flag) {
                i |= 0x40000;
            } else {
                i &= 0xfffbffff;
            }
            myLinkFlags = i;
            update(206, flag1 ? 1 : 0, null);
            if (!isInitializing() && flag) {
                removeAllPoints();
                calculateStroke();
            }
        }
    }

    public boolean isAvoidsNodes() {
        return (myLinkFlags & 0x40000) != 0;
    }

    public void setAdjustingStyle(int i) {
        int j = myLinkFlags & 0xf;
        if (j != i) {
            int i1 = myLinkFlags;
            i1 &= 0xfffffff0;
            i1 |= i;
            myLinkFlags = i1;
            update(208, j, null);
        }
    }

    public int getAdjustingStyle() {
        return myLinkFlags & 0xf;
    }

    public void setRelinkable(boolean flag) {
        boolean flag1 = (myLinkFlags & 0x10000) != 0;
        if (flag1 != flag) {
            int i = myLinkFlags;
            if (flag) {
                i |= 0x10000;
            } else {
                i &= 0xfffeffff;
            }
            myLinkFlags = i;
            update(204, flag1 ? 1 : 0, null);
        }
    }

    public boolean isRelinkable() {
        return (myLinkFlags & 0x10000) != 0;
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        if (graphicviewerview != null
                && graphicviewerview.getScale() > 0.29999999999999999D
                && isJumpsOver() && isOrthogonal() && !isCubic()) {
            resetPath();
        }
        super.paint(graphics2d, graphicviewerview);
    }

    void makePath(GeneralPath paramGeneralPath, GraphicViewerView paramGraphicViewerView) {
        int i = getNumPoints();
        if ((i >= 2) && (paramGraphicViewerView != null) && (isOrthogonal())
                && (!isCubic()) && ((isRoundedCorners()) || (isJumpsOver()))) {
            Point localPoint1 = new Point(0, 0);
            Point localPoint2 = getPoint(0);
            paramGeneralPath.moveTo(localPoint2.x, localPoint2.y);
            int k;
            for (int j = 1; j < i; j = k) {
                j = furthestPoint(localPoint2.x, localPoint2.y, j, j > 1);
                Point localPoint3 = getPoint(j);
                if (j >= i - 1) {
                    if ((localPoint2.x == localPoint3.x)
                            && (localPoint2.y == localPoint3.y)) {
                        break;
                    }
                    addLine(paramGeneralPath, localPoint2.x, localPoint2.y,
                            localPoint3.x, localPoint3.y, paramGraphicViewerView);
                    break;
                }
                k = furthestPoint(localPoint3.x, localPoint3.y, j + 1,
                        j < i - 3);
                Point localPoint4 = getPoint(k);
                addLineAndCorner(paramGeneralPath, localPoint2.x,
                        localPoint2.y, localPoint3.x, localPoint3.y,
                        localPoint4.x, localPoint4.y, paramGraphicViewerView, localPoint1);
                localPoint2 = localPoint1;
            }
        } else {
            super.makePath(paramGeneralPath, paramGraphicViewerView);
        }
    }

    int furthestPoint(int i, int j, int i1, boolean flag) {
        int j1 = getNumPoints();
        int k1 = i;
        int l1;
        Point point;
        for (l1 = j; i == k1 && j == l1; l1 = point.y) {
            if (i1 >= j1) {
                return j1 - 1;
            }
            point = getPoint(i1++);
            k1 = point.x;
        }

        if (i != k1 && j != l1) {
            return i1 - 1;
        }
        int i2 = k1;
        Point point1;
        for (int j2 = l1; i == k1 && k1 == i2
                && (!flag || (j < l1 ? l1 <= j2 : l1 >= j2)) || j == l1
                && l1 == j2 && (!flag || (i < k1 ? k1 <= i2 : k1 >= i2)); j2 = point1.y) {
            if (i1 >= j1) {
                return j1 - 1;
            }
            point1 = getPoint(i1++);
            i2 = point1.x;
        }

        return i1 - 2;
    }

    private void addLineAndCorner(GeneralPath generalpath, int i, int j,
            int i1, int j1, int k1, int l1,
            GraphicViewerView graphicviewerview, Point point) {
        if (j == j1 && i1 == k1) {
            int i2 = isRoundedCorners() ? Math.abs(getCurviness()) : 0;
            int k2 = Math.min(i2, Math.abs(i1 - i) / 2);
            int i3 = Math.min(k2, Math.abs(l1 - j1) / 2);
            k2 = i3;
            if (k2 < 1 || i3 < 1) {
                addLine(generalpath, i, j, i1, j1, graphicviewerview);
                point.x = i1;
                point.y = j1;
                return;
            }
            int k3 = i1;
            int i4 = j1;
            int k4 = i1;
            int i5 = j1;
            if (i1 > i) {
                k3 -= k2;
                if (l1 > j1) {
                    i5 += i3;
                } else {
                    i5 -= i3;
                }
            } else {
                k3 += k2;
                if (l1 > j1) {
                    i5 += i3;
                } else {
                    i5 -= i3;
                }
            }
            addLine(generalpath, i, j, k3, i4, graphicviewerview);
            generalpath.quadTo(i1, j1, k4, i5);
            point.x = k4;
            point.y = i5;
        } else if (i == i1 && j1 == l1) {
            int j2 = isRoundedCorners() ? Math.abs(getCurviness()) : 0;
            int l2 = Math.min(j2, Math.abs(j1 - j) / 2);
            int j3 = Math.min(l2, Math.abs(k1 - i1) / 2);
            l2 = j3;
            if (j3 < 1 || l2 < 1) {
                addLine(generalpath, i, j, i1, j1, graphicviewerview);
                point.x = i1;
                point.y = j1;
                return;
            }
            int l3 = i1;
            int j4 = j1;
            int l4 = i1;
            int j5 = j1;
            if (j1 > j) {
                j4 -= l2;
                if (k1 > i1) {
                    l4 += j3;
                } else {
                    l4 -= j3;
                }
            } else {
                j4 += l2;
                if (k1 > i1) {
                    l4 += j3;
                } else {
                    l4 -= j3;
                }
            }
            addLine(generalpath, i, j, l3, j4, graphicviewerview);
            generalpath.quadTo(i1, j1, l4, j5);
            point.x = l4;
            point.y = j5;
        } else {
            addLine(generalpath, i, j, i1, j1, graphicviewerview);
            addLine(generalpath, i1, j1, k1, l1, graphicviewerview);
            point.x = k1;
            point.y = l1;
        }
    }

    void addLine(GeneralPath paramGeneralPath, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4,
            GraphicViewerView paramGraphicViewerView) {
        int i = 10;
        int j = i / 2;
        int[] arrayOfInt = paramGraphicViewerView.getTempXs(100);
        Point localPoint = paramGraphicViewerView.getTempPoint();
        int k = getIntersections(paramInt1, paramInt2, paramInt3, paramInt4,
                arrayOfInt, localPoint);
        if (k > 0) {
            int m;
            int n;
            int i1;
            int i2;
            if (paramInt2 == paramInt4) {
                if (paramInt1 < paramInt3) {
                    m = 0;
                    while (m < k) {
                        n = Math.max(paramInt1,
                                Math.min(arrayOfInt[(m++)] - j, paramInt3 - i));
                        paramGeneralPath.lineTo(n, paramInt4);
                        for (i1 = Math.min(n + i, paramInt3); m < k; i1 = Math
                                .min(i2 + j, paramInt3)) {
                            i2 = arrayOfInt[m];
                            if (i2 >= i1 + i) {
                                break;
                            }
                            m++;
                        }
                        paramGeneralPath.quadTo((n + i1) / 2, paramInt4 - i,
                                i1, paramInt4);
                    }
                } else {
                    m = k - 1;
                    while (m >= 0) {
                        n = Math.min(paramInt1,
                                Math.max(arrayOfInt[(m--)] + j, paramInt3 + i));
                        paramGeneralPath.lineTo(n, paramInt4);
                        for (i1 = Math.max(n - i, paramInt3); m >= 0; i1 = Math
                                .max(i2 - j, paramInt3)) {
                            i2 = arrayOfInt[m];
                            if (i2 <= i1 - i) {
                                break;
                            }
                            m--;
                        }
                        paramGeneralPath.quadTo((n + i1) / 2, paramInt4 - i,
                                i1, paramInt4);
                    }
                }
            } else if (paramInt1 == paramInt3) {
                if (paramInt2 < paramInt4) {
                    m = 0;
                    while (m < k) {
                        n = Math.max(paramInt2,
                                Math.min(arrayOfInt[(m++)] - j, paramInt4 - i));
                        paramGeneralPath.lineTo(paramInt3, n);
                        for (i1 = Math.min(n + i, paramInt4); m < k; i1 = Math
                                .min(i2 + j, paramInt4)) {
                            i2 = arrayOfInt[m];
                            if (i2 >= i1 + i) {
                                break;
                            }
                            m++;
                        }
                        paramGeneralPath.quadTo(paramInt3 - i, (n + i1) / 2,
                                paramInt3, i1);
                    }
                } else {
                    m = k - 1;
                    while (m >= 0) {
                        n = Math.min(paramInt2,
                                Math.max(arrayOfInt[(m--)] + j, paramInt4 + i));
                        paramGeneralPath.lineTo(paramInt3, n);
                        for (i1 = Math.max(n - i, paramInt4); m >= 0; i1 = Math
                                .max(i2 - j, paramInt4)) {
                            i2 = arrayOfInt[m];
                            if (i2 <= i1 - i) {
                                break;
                            }
                            m--;
                        }
                        paramGeneralPath.quadTo(paramInt3 - i, (n + i1) / 2,
                                paramInt3, i1);
                    }
                }
            }
        }
        paramGeneralPath.lineTo(paramInt3, paramInt4);
    }

    int getIntersections(int i, int j, int i1, int j1, int ai[], Point point) {
        int k1 = 0;
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null) {
            return 0;
        }
        label0 : for (GraphicViewerLayer graphicviewerlayer = graphicviewerdocument
                .getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicviewerdocument
                .getNextLayer(graphicviewerlayer)) {
            if (!graphicviewerlayer.isVisible()) {
                continue;
            }
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerlayer
                    .getFirstObjectPos();
            do {
                GraphicViewerLink graphicviewerlink;
                do {
                    GraphicViewerObject graphicviewerobject;
                    do {
                        if (graphicviewerlistposition == null) {
                            continue label0;
                        }
                        graphicviewerobject = graphicviewerlayer
                                .getObjectAtPos(graphicviewerlistposition);
                        graphicviewerlistposition = graphicviewerlayer
                                .getNextObjectPos(graphicviewerlistposition);
                        if (graphicviewerobject == this) {
                            Arrays.sort(ai, 0, k1);
                            return k1;
                        }
                    } while (!graphicviewerobject.isVisible()
                            || !(graphicviewerobject instanceof GraphicViewerLink));
                    graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
                } while (!graphicviewerlink.isOrthogonal()
                        || !graphicviewerlink.isJumpsOver()
                        || graphicviewerlink.isCubic());
                int l1 = graphicviewerlink.getNumPoints();
                int i2 = 1;
                while (i2 < l1) {
                    Point point1 = graphicviewerlink.getPoint(i2 - 1);
                    Point point2 = graphicviewerlink.getPoint(i2);
                    if (getOrthoSegmentIntersection(i, j, i1, j1, point1,
                            point2, point) && k1 < ai.length) {
                        if (j == j1) {
                            ai[k1++] = point.x;
                        } else {
                            ai[k1++] = point.y;
                        }
                    }
                    i2++;
                }
            } while (true);
        }

        Arrays.sort(ai, 0, k1);
        return k1;
    }

    boolean getOrthoSegmentIntersection(int i, int j, int i1, int j1,
            Point point, Point point1, Point point2) {
        if (i != i1) {
            if (point.x == point1.x && Math.min(i, i1) < point.x
                    && Math.max(i, i1) > point.x
                    && Math.min(point.y, point1.y) < j
                    && Math.max(point.y, point1.y) > j) {
                point2.x = point.x;
                point2.y = j;
                return true;
            }
        } else if (point.y == point1.y && Math.min(j, j1) < point.y
                && Math.max(j, j1) > point.y && Math.min(point.x, point1.x) < i
                && Math.max(point.x, point1.x) > i) {
            point2.x = i;
            point2.y = point.y;
            return true;
        }
        return false;
    }

    public int getPartID() {
        return myPartID;
    }

    public void setPartID(int i) {
        int j = myPartID;
        if (j != i) {
            myPartID = i;
            update(210, j, null);
        }
    }

    public Object getUserObject() {
        return myUserObject;
    }

    public void setUserObject(Object obj) {
        Object obj1 = myUserObject;
        if (obj1 != obj) {
            myUserObject = obj;
            update(209, 0, obj1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 201 :
                graphicviewerdocumentchangededit.setNewValue(getFromPort());
                return;

            case 202 :
                graphicviewerdocumentchangededit.setNewValue(getToPort());
                return;

            case 203 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isOrthogonal());
                return;

            case 204 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isRelinkable());
                return;

            case 205 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isJumpsOver());
                return;

            case 206 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isAvoidsNodes());
                return;

            case 207 :
                graphicviewerdocumentchangededit.setNewValueInt(getCurviness());
                return;

            case 208 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getAdjustingStyle());
                return;

            case 209 :
                graphicviewerdocumentchangededit.setNewValue(getUserObject());
                return;

            case 210 :
                graphicviewerdocumentchangededit.setNewValueInt(getPartID());
                return;

            case 211 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isRoundedCorners());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 201 :
                myFromPort = (GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag);
                return;

            case 202 :
                myToPort = (GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag);
                return;

            case 203 :
                boolean flag1 = isInitializing();
                setInitializing(true);
                setOrthogonal(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(flag1);
                return;

            case 204 :
                setRelinkable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 205 :
                setJumpsOver(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 206 :
                boolean flag2 = isInitializing();
                setInitializing(true);
                setAvoidsNodes(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(flag2);
                return;

            case 207 :
                setCurviness(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 208 :
                setAdjustingStyle(graphicviewerdocumentchangededit
                        .getValueInt(flag));
                return;

            case 209 :
                setUserObject(graphicviewerdocumentchangededit.getValue(flag));
                return;

            case 210 :
                setPartID(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 211 :
                setRoundedCorners(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLink",
                            domelement);
            domelement1.setAttribute("partid", Integer.toString(myPartID));
            domdoc.registerReferencingNode(domelement1, "fromport",
                    getFromPort());
            domdoc.registerReferencingNode(domelement1, "toport", getToPort());
            domelement1
                    .setAttribute("linkflags", Integer.toString(myLinkFlags));
            domelement1
                    .setAttribute("curviness", Integer.toString(myCurviness));
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("partid");
            if (s.length() > 0) {
                myPartID = Integer.parseInt(s);
            }
            String s1 = domelement1.getAttribute("fromport");
            String s2 = domelement1.getAttribute("toport");
            domdoc.registerReferencingObject(this, "fromport", s1);
            domdoc.registerReferencingObject(this, "toport", s2);
            myLinkFlags = Integer.parseInt(domelement1
                    .getAttribute("linkflags"));
            String s3 = domelement1.getAttribute("curviness");
            if (s3.length() > 0) {
                setCurviness(Integer.parseInt(s3));
            }
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("fromport")) {
            setFromPortNoCalc((GraphicViewerPort) obj);
            if (isSelfLoop()) {
                calculateStroke();
            }
        } else if (s.equals("toport")) {
            setToPortNoCalc((GraphicViewerPort) obj);
            if (isSelfLoop()) {
                calculateStroke();
            }
        }
    }

    public static void setDefaultResizingRelinks(boolean flag) {
        myResizingModifiesExistingLink = flag;
    }

    public static boolean isDefaultResizingRelinks() {
        return myResizingModifiesExistingLink;
    }
}