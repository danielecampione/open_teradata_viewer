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
public class ListAreaRect extends GraphicViewerRectangle {

    private static final long serialVersionUID = 6380675575899988864L;

    public ListAreaRect() {
    }

    protected Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        if (getParent() == null) {
            return super.handleResize(graphics2d, graphicviewerview, rectangle,
                    point, i, j, k, l);
        } else {
            ListArea listarea = (ListArea) getParent();
            Dimension dimension = listarea.getMinimumRectSize();
            return super.handleResize(graphics2d, graphicviewerview, rectangle,
                    point, i, j, dimension.width, dimension.height);
        }
    }

    public void setBoundingRect(int i, int j, int k, int l) {
        if (getParent() == null) {
            return;
        } else {
            ListArea listarea = (ListArea) getParent();
            Dimension dimension = listarea.getMinimumRectSize();
            super.setBoundingRect(i, j, Math.max(k, dimension.width),
                    Math.max(l, dimension.height));
            return;
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.ListAreaRect",
                    domelement);
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
}