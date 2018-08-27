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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerSubGraphHandle extends GraphicViewerRectangle {

    private static final long serialVersionUID = -568644923095162681L;

    public GraphicViewerSubGraphHandle() {
        setSize(10, 10);
        setDragsNode(true);
        setSelectable(false);
        setResizable(false);
        setBrush(GraphicViewerBrush.yellow);
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        super.paint(graphics2d, graphicviewerview);
        paintHandle(graphics2d, graphicviewerview);
    }

    public void paintHandle(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview) {
        GraphicViewerSubGraph graphicviewersubgraph = (GraphicViewerSubGraph) getParent();
        if (graphicviewersubgraph == null)
            return;
        Rectangle rectangle = getBoundingRect();
        if (graphicviewersubgraph.isCollapsible()) {
            int i = rectangle.y + rectangle.height / 2;
            drawLine(graphics2d, GraphicViewerPen.black, rectangle.x
                    + rectangle.width / 4, i, rectangle.x
                    + (rectangle.width * 3) / 4, i);
            if (!graphicviewersubgraph.isExpanded()) {
                int j = rectangle.x + rectangle.width / 2;
                drawLine(graphics2d, GraphicViewerPen.black, j, rectangle.y
                        + rectangle.height / 4, j, rectangle.y
                        + (rectangle.height * 3) / 4);
            }
        } else {
            drawEllipse(graphics2d, GraphicViewerPen.black, null, rectangle.x
                    + rectangle.width / 4, rectangle.y + rectangle.height / 4,
                    rectangle.width / 2, rectangle.height / 2);
        }
    }

    public boolean doMouseClick(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        GraphicViewerSubGraph graphicviewersubgraph = (GraphicViewerSubGraph) getParent();
        if (graphicviewersubgraph == null
                || !graphicviewersubgraph.isCollapsible())
            return false;
        if (graphicviewerview != null)
            graphicviewerview.getDocument().startTransaction();
        String s = null;
        if (graphicviewersubgraph.isExpanded()) {
            graphicviewersubgraph.collapse();
            s = "Collapsed GraphicViewerSubGraph";
        } else if ((i & 2) != 0) {
            graphicviewersubgraph.expandAll();
            s = "Expanded All GraphicViewerSubGraphs";
        } else {
            graphicviewersubgraph.expand();
            s = "Expand GraphicViewerSubGraph";
        }
        if (graphicviewerview != null) {
            graphicviewerview.getDocument().endTransaction(s);
            graphicviewerview.getSelection().restoreSelectionHandles(
                    graphicviewersubgraph);
        }
        return true;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSubGraphHandle",
                    domelement);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingElement());
        return domelement.getNextSibling();
    }
}