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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerRectangle extends GraphicViewerDrawable {

    private static final long serialVersionUID = 6167033422145117738L;

    public GraphicViewerRectangle() {
    }

    public GraphicViewerRectangle(Rectangle rectangle) {
        super(rectangle);
    }

    public GraphicViewerRectangle(Point point, Dimension dimension) {
        super(point, dimension);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerRectangle graphicviewerrectangle = (GraphicViewerRectangle) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerrectangle == null)
            ;
        return graphicviewerrectangle;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerRectangle",
                    domelement);
        }
        if (domdoc.SVGOutputEnabled()) {
            IDomElement domelement2 = domdoc.createElement("rect");
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
        else if (domelement.getTagName().equalsIgnoreCase("rect"))
            SVGReadAttributes(domelement);
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
    }

    public void SVGReadAttributes(IDomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        if (s.length() > 0 && s1.length() > 0)
            setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        if (s2.length() > 0 && s3.length() > 0) {
            setWidth(Integer.parseInt(s2));
            setHeight(Integer.parseInt(s3));
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        Rectangle rectangle = getBoundingRect();
        drawRect(graphics2d, rectangle.x, rectangle.y, rectangle.width,
                rectangle.height);
    }

    public static boolean getNearestIntersectionPoint(int i, int j, int k,
            int l, int i1, int j1, int k1, int l1, Point point) {
        int i2 = i + k;
        int j2 = j + l;
        double d = 1E+021D;
        int k2 = k1;
        int l2 = l1;
        if (GraphicViewerStroke.getNearestIntersectionOnLine(i, j, i2, j, i1,
                j1, k1, l1, point)) {
            double d1 = (point.x - i1) * (point.x - i1) + (point.y - j1)
                    * (point.y - j1);
            if (d1 < d) {
                d = d1;
                k2 = point.x;
                l2 = point.y;
            }
        }
        if (GraphicViewerStroke.getNearestIntersectionOnLine(i2, j, i2, j2, i1,
                j1, k1, l1, point)) {
            double d2 = (point.x - i1) * (point.x - i1) + (point.y - j1)
                    * (point.y - j1);
            if (d2 < d) {
                d = d2;
                k2 = point.x;
                l2 = point.y;
            }
        }
        if (GraphicViewerStroke.getNearestIntersectionOnLine(i2, j2, i, j2, i1,
                j1, k1, l1, point)) {
            double d3 = (point.x - i1) * (point.x - i1) + (point.y - j1)
                    * (point.y - j1);
            if (d3 < d) {
                d = d3;
                k2 = point.x;
                l2 = point.y;
            }
        }
        if (GraphicViewerStroke.getNearestIntersectionOnLine(i, j2, i, j, i1,
                j1, k1, l1, point)) {
            double d4 = (point.x - i1) * (point.x - i1) + (point.y - j1)
                    * (point.y - j1);
            if (d4 < d) {
                d = d4;
                k2 = point.x;
                l2 = point.y;
            }
        }
        point.x = k2;
        point.y = l2;
        return d < 1E+021D;
    }
}