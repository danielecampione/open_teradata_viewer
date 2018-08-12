/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2013, D. Campione
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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Diamond extends GraphicViewerDrawable {

    private static final long serialVersionUID = 3275725626657294457L;

    public Diamond() {
        myPath = null;
    }

    public Diamond(Rectangle rectangle) {
        super(rectangle);
        myPath = null;
    }

    public Diamond(Point point, Dimension dimension) {
        super(point, dimension);
        myPath = null;
    }

    public void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        resetPath();
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        GeneralPath generalpath = getPath(graphicviewerview);
        drawPath(graphics2d, getPen(), getBrush(), generalpath);
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        GraphicViewerPen graphicviewerpen = getPen();
        if (graphicviewerpen != null) {
            int i = 5 * graphicviewerpen.getWidth();
            rectangle.x -= i;
            rectangle.y -= i;
            rectangle.width += i * 2;
            rectangle.height += i * 2;
        }
    }

    public boolean isPointInObj(Point point) {
        if (!super.isPointInObj(point)) {
            return false;
        } else {
            GeneralPath generalpath = getPath(null);
            return generalpath.contains(point.x, point.y);
        }
    }

    public boolean getNearestIntersectionPoint(int i, int j, int k, int l,
            Point point) {
        Rectangle rectangle = getBoundingRect();
        int i1 = getPen().getWidth();
        int j1 = rectangle.x + rectangle.width / 2;
        int k1 = rectangle.y - i1;
        int l1 = rectangle.x + rectangle.width + i1;
        int i2 = rectangle.y + rectangle.height / 2;
        int j2 = j1;
        int k2 = rectangle.y + rectangle.height + i1;
        int l2 = rectangle.x - i1;
        int i3 = i2;
        Point point1 = new Point(0, 0);
        double d = 1E+021D;
        if (GraphicViewerStroke.getNearestIntersectionOnLine(j1, k1, l1, i2, i,
                j, k, l, point1)) {
            double d1 = (point1.x - i) * (point1.x - i) + (point1.y - j)
                    * (point1.y - j);
            if (d1 < d) {
                d = d1;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (GraphicViewerStroke.getNearestIntersectionOnLine(l1, i2, j2, k2, i,
                j, k, l, point1)) {
            double d2 = (point1.x - i) * (point1.x - i) + (point1.y - j)
                    * (point1.y - j);
            if (d2 < d) {
                d = d2;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (GraphicViewerStroke.getNearestIntersectionOnLine(j2, k2, l2, i3, i,
                j, k, l, point1)) {
            double d3 = (point1.x - i) * (point1.x - i) + (point1.y - j)
                    * (point1.y - j);
            if (d3 < d) {
                d = d3;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (GraphicViewerStroke.getNearestIntersectionOnLine(l2, i3, j1, k1, i,
                j, k, l, point1)) {
            double d4 = (point1.x - i) * (point1.x - i) + (point1.y - j)
                    * (point1.y - j);
            if (d4 < d) {
                d = d4;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        return d < 1E+021D;
    }

    void makePath(GeneralPath generalpath, GraphicViewerView graphicviewerview) {
        Rectangle rectangle = getBoundingRect();
        int i = rectangle.x + rectangle.width / 2;
        int j = rectangle.y;
        int k = rectangle.x + rectangle.width;
        int l = rectangle.y + rectangle.height / 2;
        int i1 = i;
        int j1 = rectangle.y + rectangle.height;
        int k1 = rectangle.x;
        int l1 = l;
        generalpath.moveTo(i, j);
        generalpath.lineTo(k, l);
        generalpath.lineTo(i1, j1);
        generalpath.lineTo(k1, l1);
        generalpath.lineTo(i, j);
        generalpath.closePath();
    }

    GeneralPath getPath(GraphicViewerView graphicviewerview) {
        if (myPath == null)
            myPath = new GeneralPath(1, 8);
        if (myPath.getCurrentPoint() == null)
            makePath(myPath, graphicviewerview);
        return myPath;
    }

    void resetPath() {
        if (myPath != null)
            myPath.reset();
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        super.changeValue(graphicviewerdocumentchangededit, flag);
        if (graphicviewerdocumentchangededit.getFlags() == 1)
            resetPath();
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.Diamond",
                    domelement);
        }
        if (domdoc.SVGOutputEnabled()) {
            Rectangle rectangle = getBoundingRect();
            int ai[] = new int[4];
            int ai1[] = new int[4];
            ai[0] = rectangle.x + rectangle.width / 2;
            ai1[0] = rectangle.y;
            ai[1] = rectangle.x + rectangle.width;
            ai1[1] = rectangle.y + rectangle.height / 2;
            ai[2] = ai[0];
            ai1[2] = rectangle.y + rectangle.height;
            ai[3] = rectangle.x;
            ai1[3] = ai1[1];
            IDomElement domelement2 = domdoc.createElement("path");
            String s = "M " + Integer.toString(ai[0]) + " "
                    + Integer.toString(ai1[0]);
            s = s + " L " + Integer.toString(ai[1]) + " "
                    + Integer.toString(ai1[1]);
            s = s + " L " + Integer.toString(ai[2]) + " "
                    + Integer.toString(ai1[2]);
            s = s + " L " + Integer.toString(ai[3]) + " "
                    + Integer.toString(ai1[3]);
            s = s + " Z";
            domelement2.setAttribute("d", s);
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        return domelement.getNextSibling();
    }

    private transient GeneralPath myPath;
}