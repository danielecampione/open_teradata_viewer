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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class SanKeyPort extends GraphicViewerPort {

    private static final long serialVersionUID = 7125783010785484468L;

    public static final int ChangedLinkPointsSpread = 333;

    private static Comparator myComparer = new EndPositionComparer();

    private int myTopBreadth;
    private int myRightBreadth;
    private int myBottomBreadth;
    private int myLeftBreadth;
    private Point myTopStart;
    private Point myRightStart;
    private Point myBottomStart;
    private Point myLeftStart;

    private GoBoxPortLinkInfo[] mySortedLinks;
    private boolean myRespreading;

    public SanKeyPort() {
        setFromSpot(Center);
        setToSpot(Center);
        setStyle(GraphicViewerPort.StyleHidden);
        // Even though the Style is Hidden, the Pen and Brush are used for
        // drawing the triangles where the links connect at the port
        setPen(GraphicViewerPen.black);
        setBrush(GraphicViewerBrush.white);
    }

    //// Outputs always come from the right side
    //public double getFromLinkDir(GraphicViewerLink link) {
    //  return 0;
    //}

    //// Inputs always go into the left side
    //public double getToLinkDir(GraphicViewerLink link) {
    //  return Math.PI;
    //}

    public Point getFromLinkPoint(GraphicViewerLink link, Point result) {
        if (link != null) {
            GoBoxPortLinkInfo[] infos = GetLinkInfos(true);
            for (int i = 0; i < infos.length; i++) {
                GoBoxPortLinkInfo info = infos[i];
                if (info.Link == link) {
                    result.x = info.LinkPoint.x;
                    result.y = info.LinkPoint.y;
                    return result;
                }
            }
        }
        return getSpotLocation(Center, result);
    }

    public Point getToLinkPoint(GraphicViewerLink link, Point result) {
        if (link != null) {
            GoBoxPortLinkInfo[] infos = GetLinkInfos(true);
            for (int i = 0; i < infos.length; i++) {
                GoBoxPortLinkInfo info = infos[i];
                if (info.Link == link) {
                    result.x = info.LinkPoint.x;
                    result.y = info.LinkPoint.y;
                    return result;
                }
            }
        }
        return getSpotLocation(Center, result);
    }

    public double GetAngle(GraphicViewerLink link) {
        if (link == null) {
            return 0;
        }
        GraphicViewerPort ip = link.getOtherPort(this);
        if (ip == null) {
            return 0;
        }
        Point pc = ip.getSpotLocation(Center);
        Point thisc = getSpotLocation(Center);
        if (link.getNumPoints() > 0) {
            if (link.getFromPort() == ip) {
                pc = link.getPoint(0);
            } else {
                pc = link.getPoint(link.getNumPoints() - 1);
            }
        }
        return -Math.atan2(pc.x - thisc.x, pc.y - thisc.y);
    }

    public double GetDirection(GraphicViewerLink link) {
        if (link == null) {
            return 0;
        }
        if (link.getFromPort() == this) {
            return getFromLinkDir(link);
        } else {
            return getToLinkDir(link);
        }
    }

    GoBoxPortLinkInfo[] GetLinkInfos(boolean sort) {
        if (mySortedLinks == null || mySortedLinks.length != getNumLinks()) {
            mySortedLinks = new GoBoxPortLinkInfo[getNumLinks()];
            sort = true;
        }
        if (sort && !myRespreading) {
            boolean oldRespreading = myRespreading;
            myRespreading = true;
            int i = 0;
            GraphicViewerListPosition pos = getFirstLinkPos();
            while (pos != null) {
                GraphicViewerLink l = getLinkAtPos(pos);
                pos = getNextLinkPos(pos);

                double dir = GetDirection(l);
                double angle = GetAngle(l);
                int side;
                if (dir == 0) {
                    side = RightCenter;
                } else if (dir == Math.PI / 2) {
                    side = BottomCenter;
                } else if (dir == Math.PI) {
                    side = LeftCenter;
                } else {
                    side = TopCenter;
                    if (angle < 0) {
                        angle += Math.PI * 2;
                    }
                }
                GoBoxPortLinkInfo info = mySortedLinks[i];
                if (info == null) {
                    mySortedLinks[i] = new GoBoxPortLinkInfo(l, angle, side);
                } else {
                    info.Link = l;
                    info.Angle = angle;
                    info.Side = side;
                }
                i++;
            }
            SortLinkInfos(mySortedLinks);
            // Now assign IndexOnSide for each set of GoBoxPortLinkInfos with
            // the same Side
            int numlinks = mySortedLinks.length;
            int currside = -1;
            int numonside = 0;
            for (i = 0; i < numlinks; i++) {
                GoBoxPortLinkInfo info = mySortedLinks[i];
                if (info.Side != currside) {
                    currside = info.Side;
                    numonside = 0;
                }
                info.IndexOnSide = numonside;
                numonside++;
            }
            // The highest IndexOnSide now also indicates how many there are
            // with the same Side--assign CountOnSide correspondingly; also
            // compute the LinkPoint
            currside = -1;
            numonside = 0;
            for (i = numlinks - 1; i >= 0; i--) { // Backwards
                GoBoxPortLinkInfo info = mySortedLinks[i];
                if (info.Side != currside) {
                    currside = info.Side;
                    numonside = info.IndexOnSide + 1;
                }
                info.CountOnSide = numonside;
            }
            AssignLinkPoints(mySortedLinks);
            AssignEndSegmentLengths(mySortedLinks);
            myRespreading = oldRespreading;
        }
        return mySortedLinks;
    }

    protected void SortLinkInfos(GoBoxPortLinkInfo[] linkinfos) {
        Arrays.sort(linkinfos, myComparer);
    }

    protected void AssignEndSegmentLengths(GoBoxPortLinkInfo[] linkinfos) {
        for (int i = 0; i < linkinfos.length; i++) {
            GoBoxPortLinkInfo info = linkinfos[i];
            info.EndSegmentLength = getEndSegmentLength();
        }
    }

    // Besides making sure the link points are placed as close next to each
    // other as their pen widths, this also remembers where the links are
    // connected as a group, for Paint to know where to draw triangles
    protected void AssignLinkPoints(GoBoxPortLinkInfo[] linkinfos) {
        // Calculate the sum of the breadth of the links on each side
        myTopBreadth = 0;
        myRightBreadth = 0;
        myBottomBreadth = 0;
        myLeftBreadth = 0;
        for (int i = 0; i < linkinfos.length; i++) {
            GoBoxPortLinkInfo info = linkinfos[i];
            GraphicViewerLink l = info.Link;
            int w = l.getPen().getWidth();
            if (l.getHighlight() != null) {
                w = Math.max(w, l.getHighlight().getWidth());
            }
            switch (info.Side) {
                case TopCenter :
                    myTopBreadth += w;
                    break;
                case RightCenter :
                    myRightBreadth += w;
                    break;
                case BottomCenter :
                    myBottomBreadth += w;
                    break;
                case LeftCenter :
                    myLeftBreadth += w;
                    break;
                default :
                    break;
            }
        }

        GraphicViewerObject pobj = getPortObject();
        if (pobj == null) {
            pobj = this;
        }
        Rectangle r = pobj.getBoundingRect();
        // Remember the link point connection starting points, clockwise
        myTopStart = new Point(r.x + r.width / 2 - myTopBreadth / 2, r.y);
        myRightStart = new Point(r.x + r.width, r.y + r.height / 2
                - myRightBreadth / 2);
        myBottomStart = new Point(r.x + r.width / 2 + myBottomBreadth / 2, r.y
                + r.height);
        myLeftStart = new Point(r.x, r.y + r.height / 2 + myLeftBreadth / 2);
        Point top = new Point(myTopStart);
        Point right = new Point(myRightStart);
        Point bottom = new Point(myBottomStart);
        Point left = new Point(myLeftStart);
        for (int i = 0; i < linkinfos.length; i++) {
            GoBoxPortLinkInfo info = linkinfos[i];
            GraphicViewerLink l = info.Link;
            int w = Math.max(1, l.getPen().getWidth());
            if (l.getHighlight() != null) {
                w = Math.max(w, l.getHighlight().getWidth());
            }
            // Asssume top and left are inputs, right and bottom are outputs
            switch (info.Side) {
                case TopCenter :
                    info.LinkPoint.x = top.x + w / 2;
                    info.LinkPoint.y = top.y - getEndSegmentLength();
                    top.x += w;
                    break;
                case RightCenter :
                    info.LinkPoint.x = right.x;
                    info.LinkPoint.y = right.y + w / 2;
                    right.y += w;
                    break;
                case BottomCenter :
                    info.LinkPoint.x = bottom.x - w / 2;
                    info.LinkPoint.y = bottom.y;
                    bottom.x -= w;
                    break;
                case LeftCenter :
                    info.LinkPoint.x = left.x - getEndSegmentLength();
                    info.LinkPoint.y = left.y - w / 2;
                    left.y -= w;
                    break;
            }
        }
    }

    public void linkChange() {
        portChange(ChangedLinkPointsSpread, 0, null); // Need to reposition
                                                      // links based on their
                                                      // widths
        update();
    }

    // Draw triangles where links are connected.
    // For these triangles to be visible, the ports/nodes need to be in front of
    // all of the links.
    // This assumes the links at the top or left are inputs and the links at the
    // right or bottom are outputs, hence draws the triangles pointing in the
    // corresponding directions
    public void paint(Graphics2D g, GraphicViewerView view) {
        super.paint(g, view);
        if (getNumLinks() == 0) {
            return;
        }
        int[] x = new int[3];
        int[] y = new int[3];
        if (myTopBreadth > 0) { // Assume an input triangle
            x[0] = myTopStart.x;
            y[0] = myTopStart.y;
            x[1] = myTopStart.x;
            y[1] = myTopStart.y;
            x[2] = myTopStart.x;
            y[2] = myTopStart.y;
            y[0] -= getEndSegmentLength();
            x[1] += myTopBreadth / 2;
            x[2] += myTopBreadth;
            y[2] -= getEndSegmentLength();
            drawPolygon(g, x, y, 3);
        }
        if (myRightBreadth > 0) { // Assume an output triangle
            x[0] = myRightStart.x;
            y[0] = myRightStart.y;
            x[1] = myRightStart.x;
            y[1] = myRightStart.y;
            x[2] = myRightStart.x;
            y[2] = myRightStart.y;
            y[1] += myRightBreadth / 2;
            x[1] += getEndSegmentLength();
            y[2] += myRightBreadth;
            drawPolygon(g, x, y, 3);
        }
        if (myBottomBreadth > 0) { // Assume an output triangle
            x[0] = myBottomStart.x;
            y[0] = myBottomStart.y;
            x[1] = myBottomStart.x;
            y[1] = myBottomStart.y;
            x[2] = myBottomStart.x;
            y[2] = myBottomStart.y;
            x[1] -= myBottomBreadth / 2;
            y[1] += getEndSegmentLength();
            x[2] -= myBottomBreadth;
            drawPolygon(g, x, y, 3);
        }
        if (myLeftBreadth > 0) { // Assume an input triangle
            x[0] = myLeftStart.x;
            y[0] = myLeftStart.y;
            x[1] = myLeftStart.x;
            y[1] = myLeftStart.y;
            x[2] = myLeftStart.x;
            y[2] = myLeftStart.y;
            x[0] -= getEndSegmentLength();
            y[1] -= myLeftBreadth / 2;
            x[2] -= getEndSegmentLength();
            y[2] -= myLeftBreadth;
            drawPolygon(g, x, y, 3);
        }
    }

    // When GraphicViewerObject.Paint paints beyond the
    // GraphicViewerObject.Bounds, we need to tell the GraphicViewerView where
    // to invalidate and eventually repaint
    public void expandRectByPenWidth(Rectangle rect) {
        super.expandRectByPenWidth(rect);
        rect.grow(getEndSegmentLength(), getEndSegmentLength());
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    static class EndPositionComparer implements Comparator { // Nested class

        EndPositionComparer() {
        }

        public int compare(Object x, Object y) {
            GoBoxPortLinkInfo a = (GoBoxPortLinkInfo) x;
            GoBoxPortLinkInfo b = (GoBoxPortLinkInfo) y;
            if (a == null || b == null) {
                return 0;
            }
            if (a.Side < b.Side) {
                return -1;
            } else if (a.Side > b.Side) {
                return 1;
            } else {
                if (a.Angle < b.Angle) {
                    return -1;
                } else if (a.Angle > b.Angle) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    public static class GoBoxPortLinkInfo implements Serializable {

        private static final long serialVersionUID = -1257454044206894738L;

        public GraphicViewerLink Link;
        public double Angle;
        public int Side;
        public int CountOnSide;
        public int IndexOnSide;
        public Point LinkPoint = new Point();
        public int EndSegmentLength;

        GoBoxPortLinkInfo(GraphicViewerLink l, double a, int s) {
            this.Link = l;
            this.Angle = a;
            this.Side = s;
        }
    }
}