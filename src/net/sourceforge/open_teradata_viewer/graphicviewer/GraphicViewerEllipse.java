/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

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
public class GraphicViewerEllipse extends GraphicViewerDrawable {

    private static final long serialVersionUID = -5231810073802115833L;

    public GraphicViewerEllipse() {
    }

    public GraphicViewerEllipse(Rectangle rectangle) {
        super(rectangle);
    }

    public GraphicViewerEllipse(Point point, Dimension dimension) {
        super(point, dimension);
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerEllipse graphicviewerellipse = (GraphicViewerEllipse) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerellipse == null)
            ;
        return graphicviewerellipse;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        @SuppressWarnings("unused")
        DomElement domelement1;
        if (domdoc.GraphicViewerXMLOutputEnabled())
            domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerEllipse",
                    domelement);
        if (domdoc.SVGOutputEnabled()) {
            DomElement domelement2 = domdoc.createElement("ellipse");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        else if (domelement.getTagName().equalsIgnoreCase("ellipse"))
            SVGReadAttributes(domelement);
        else if (domelement.getTagName().equalsIgnoreCase("circle"))
            SVGReadAttributes(domelement);
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(DomElement domelement) {
        super.SVGWriteAttributes(domelement);
        int i = getTopLeft().x + getWidth() / 2;
        int j = getTopLeft().y + getHeight() / 2;
        int k = getWidth() / 2;
        int l = getHeight() / 2;
        domelement.setAttribute("rx", Integer.toString(k));
        domelement.setAttribute("ry", Integer.toString(l));
        domelement.setAttribute("cx", Integer.toString(i));
        domelement.setAttribute("cy", Integer.toString(j));
    }

    public void SVGReadAttributes(DomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("cx");
        String s1 = domelement.getAttribute("cy");
        String s2 = domelement.getAttribute("rx");
        String s3 = domelement.getAttribute("ry");
        String s4 = domelement.getAttribute("r");
        if (s4.length() > 0) {
            s2 = s4;
            s3 = s4;
        }
        int i = (new Double(s)).intValue() - (new Double(s2)).intValue();
        int j = (new Double(s1)).intValue() - (new Double(s3)).intValue();
        int k = (new Double(s2)).intValue() * 2;
        int l = (new Double(s3)).intValue() * 2;
        setTopLeft(new Point(i, j));
        setWidth(k);
        setHeight(l);
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        drawEllipse(graphics2d, getBoundingRect());
    }

    public boolean isPointInObj(Point point) {
        if (!super.isPointInObj(point)) {
            return false;
        } else {
            double d = (double) getWidth() / 2D;
            double d1 = (double) point.x - ((double) getLeft() + d);
            double d2 = (double) getHeight() / 2D;
            double d3 = (double) point.y - ((double) getTop() + d2);
            return (d1 * d1) / (d * d) + (d3 * d3) / (d2 * d2) <= 1.0D;
        }
    }

    public static boolean getNearestIntersectionPoint(int i, int j, int k,
            int l, int i1, int j1, Point point) {
        double d = (double) k / 2D;
        double d1 = (double) l / 2D;
        double d2 = (double) i + d;
        double d3 = (double) j + d1;
        double d4 = (double) i1 - d2;
        double d5 = (double) j1 - d3;
        double d6 = d * d;
        double d7 = d1 * d1;
        if (-0.01D < d4 && d4 < 0.01D)
            d4 = 0.01D;
        if (-0.01D < d5 && d5 < 0.01D)
            d5 = 0.01D;
        double d8 = (d5 * d5) / (d4 * d4);
        double d9 = Math.sqrt((d6 * d7) / (d7 + d6 * d8));
        double d10 = Math.abs((d9 * d5) / d4);
        point.x = (int) Math.rint(d4 <= 0.0D ? d2 - d9 : d2 + d9);
        point.y = (int) Math.rint(d5 <= 0.0D ? d3 - d10 : d3 + d10);
        return true;
    }

    public static boolean getNearestIntersectionPoint(int i, int j, int k,
            int l, int i1, int j1, int k1, int l1, Point point) {
        if (k == 0)
            return GraphicViewerStroke.getNearestIntersectionOnLine(i, j, i, j
                    + l, i1, j1, k1, l1, point);
        if (l == 0)
            return GraphicViewerStroke.getNearestIntersectionOnLine(i, j,
                    i + k, j, i1, j1, k1, l1, point);
        double d = i1;
        double d1 = j1;
        double d2 = k1;
        double d3 = l1;
        double d4 = k / 2;
        double d5 = l / 2;
        double d6 = (double) i + d4;
        double d7 = (double) j + d5;
        if (d != d2) {
            double d8;
            if (d > d2)
                d8 = (d1 - d3) / (d - d2);
            else
                d8 = (d3 - d1) / (d2 - d);
            double d10 = d1 - d7 - d8 * (d - d6);
            if ((d4 * d4 * (d8 * d8) + d5 * d5) - d10 * d10 < 0.0D) {
                point.x = 0;
                point.y = 0;
                return false;
            }
            double d12 = Math.sqrt((d4 * d4 * (d8 * d8) + d5 * d5) - d10 * d10);
            double d14 = (-(d4 * d4 * d8 * d10) + d4 * d5 * d12)
                    / (d5 * d5 + d4 * d4 * (d8 * d8)) + d6;
            double d16 = (-(d4 * d4 * d8 * d10) - d4 * d5 * d12)
                    / (d5 * d5 + d4 * d4 * (d8 * d8)) + d6;
            double d18 = d8 * (d14 - d6) + d10 + d7;
            double d19 = d8 * (d16 - d6) + d10 + d7;
            double d20 = Math.abs((d - d14) * (d - d14))
                    + Math.abs((d1 - d18) * (d1 - d18));
            double d21 = Math.abs((d - d16) * (d - d16))
                    + Math.abs((d1 - d19) * (d1 - d19));
            if (d20 < d21) {
                point.x = Math.round((float) d14);
                point.y = Math.round((float) d18);
            } else {
                point.x = Math.round((float) d16);
                point.y = Math.round((float) d19);
            }
        } else {
            double d9 = Math.sqrt(d5 * d5 - ((d5 * d5) / (d4 * d4))
                    * ((d - d6) * (d - d6)));
            double d11 = d7 + d9;
            double d13 = d7 - d9;
            double d15 = Math.abs(d11 - d1);
            double d17 = Math.abs(d13 - d1);
            if (d15 < d17) {
                point.x = i1;
                point.y = Math.round((float) d11);
            } else {
                point.x = i1;
                point.y = Math.round((float) d13);
            }
        }
        return true;
    }

    public boolean getNearestIntersectionPoint(int i, int j, int k, int l,
            Point point) {
        Rectangle rectangle = getBoundingRect();
        int i1 = 1;
        if (getPen() != null)
            i1 = getPen().getWidth();
        return getNearestIntersectionPoint(rectangle.x - i1, rectangle.y - i1,
                rectangle.width + 2 * i1, rectangle.height + 2 * i1, i, j, k,
                l, point);
    }
}