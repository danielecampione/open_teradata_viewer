/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

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
public class GraphicViewerPolygon extends GraphicViewerStroke {

    private static final long serialVersionUID = 405455231135994265L;

    public GraphicViewerPolygon() {
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerPolygon graphicviewerpolygon = (GraphicViewerPolygon) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerpolygon == null)
            ;
        return graphicviewerpolygon;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        @SuppressWarnings("unused")
        DomElement domelement1;
        if (domdoc.GraphicViewerXMLOutputEnabled())
            domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerPolygon",
                    domelement);
        if (domdoc.SVGOutputEnabled()) {
            DomElement domelement2 = domdoc.createElement("path");
            String s = "";
            int i = myPoints.size();
            if (i > 0) {
                Point point = (Point) myPoints.get(0);
                s = s + "M";
                s = s + " " + Integer.toString(point.x);
                s = s + " " + Integer.toString(point.y);
                for (int j = 1; j < myPoints.size(); j++) {
                    Point point1 = (Point) myPoints.get(j);
                    s = s + " L " + Integer.toString(point1.x);
                    s = s + " " + Integer.toString(point1.y);
                }

                s = s + " Z";
                SVGWriteAttributes(domelement2);
                domelement2.setAttribute("d", s);
                domelement.appendChild(domelement2);
            }
        }
        domdoc.setDisabledDrawing(true);
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        return domelement.getNextSibling();
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        int i = getNumPoints();
        if (i <= 1)
            return;
        GeneralPath generalpath = _mthdo(graphicviewerview);
        GraphicViewerPen graphicviewerpen = getHighlight();
        if (graphicviewerpen != null)
            drawPath(graphics2d, graphicviewerpen, null, generalpath);
        drawPath(graphics2d, getPen(), getBrush(), generalpath);
    }

    public boolean isPointInObj(Point point) {
        Rectangle rectangle = getBoundingRect();
        int i = getPen() == null ? 1 : getPen().getWidth();
        if (point.x < rectangle.x - i
                || point.x > rectangle.x + rectangle.width + i
                || point.y < rectangle.y - i
                || point.y > rectangle.y + rectangle.height + i)
            return false;
        int j = getNumPoints();
        if (j <= 1) {
            return false;
        } else {
            GeneralPath generalpath = _mthdo(null);
            return generalpath.contains(point.x, point.y);
        }
    }

    void a(GeneralPath generalpath, GraphicViewerView graphicviewerview) {
        super.a(generalpath, graphicviewerview);
        generalpath.closePath();
    }
}