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
public class GraphicViewerRoundRect extends GraphicViewerDrawable {

    private static final long serialVersionUID = 3411129146763651958L;

    public GraphicViewerRoundRect() {
        dS = new Dimension();
        _mthif(5, 5);
    }

    public GraphicViewerRoundRect(Dimension dimension) {
        dS = new Dimension();
        _mthif(dimension.width, dimension.height);
    }

    public GraphicViewerRoundRect(Rectangle rectangle, Dimension dimension) {
        super(rectangle);
        dS = new Dimension();
        _mthif(dimension.width, dimension.height);
    }

    public GraphicViewerRoundRect(Point point, Dimension dimension,
            Dimension dimension1) {
        super(point, dimension);
        dS = new Dimension();
        _mthif(dimension1.width, dimension1.height);
    }

    private final void _mthif(int i, int j) {
        dS.width = i;
        dS.height = j;
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerRoundRect graphicviewerroundrect = (GraphicViewerRoundRect) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerroundrect != null) {
            graphicviewerroundrect.dS.width = dS.width;
            graphicviewerroundrect.dS.height = dS.height;
        }
        return graphicviewerroundrect;
    }

    public Dimension getArcDimension() {
        return dS;
    }

    public void setArcDimension(Dimension dimension) {
        Dimension dimension1 = dS;
        if (dimension1.width != dimension.width
                || dimension1.height != dimension.height) {
            Dimension dimension2 = new Dimension(dimension1);
            dS.width = dimension.width;
            dS.height = dimension.height;
            update(404, 0, dimension2);
        }
    }

    public void copyOldValueForUndo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 404 :
                graphicviewerdocumentchangededit.setOldValue(new Dimension(
                        (Dimension) graphicviewerdocumentchangededit
                                .getOldValue()));
                return;
        }
        super.copyOldValueForUndo(graphicviewerdocumentchangededit);
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 404 :
                graphicviewerdocumentchangededit.setNewValue(new Dimension(
                        getArcDimension()));
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 404 :
                setArcDimension((Dimension) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerRoundRect",
                    domelement);
            domelement1.setAttribute("arcwidth", Integer.toString(dS.width));
            domelement1.setAttribute("archeight", Integer.toString(dS.height));
        }
        if (domdoc.SVGOutputEnabled()) {
            DomElement domelement2 = domdoc.createElement("rect");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            int i = Integer.parseInt(domelement1.getAttribute("arcwidth"));
            int j = Integer.parseInt(domelement1.getAttribute("archeight"));
            setArcDimension(new Dimension(i, j));
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(DomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
        domelement.setAttribute("rx",
                Double.toString(getArcDimension().width / 2));
        domelement.setAttribute("ry",
                Double.toString(getArcDimension().height / 2));
    }

    public void SVGReadAttributes(DomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        setWidth(Integer.parseInt(s2));
        setHeight(Integer.parseInt(s3));
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        Rectangle rectangle = getBoundingRect();
        drawRoundRect(graphics2d, rectangle.x, rectangle.y, rectangle.width,
                rectangle.height, dS.width, dS.height);
    }

    public static final int ChangedArcDimension = 404;
    private Dimension dS;
}