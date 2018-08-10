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
public class GraphicViewer3DRect extends GraphicViewerRectangle {

    private static final long serialVersionUID = -7943698482448701589L;

    public GraphicViewer3DRect() {
        dR = 0;
        w();
    }

    public GraphicViewer3DRect(Rectangle rectangle) {
        super(rectangle);
        dR = 0;
        w();
    }

    public GraphicViewer3DRect(Point point, Dimension dimension) {
        super(point, dimension);
        dR = 0;
        w();
    }

    private final void w() {
        setBrush(GraphicViewerBrush.lightGray);
        setPen(GraphicViewerPen.lightGray);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewer3DRect graphicviewer3drect = (GraphicViewer3DRect) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewer3drect != null)
            graphicviewer3drect.dR = dR;
        return graphicviewer3drect;
    }

    public void setState(int i) {
        int j = dR;
        if (j != i) {
            dR = i;
            update(403, j, null);
        }
    }

    public int getState() {
        return dR;
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 403 :
                graphicviewerdocumentchangededit.setNewValueInt(getState());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 403 :
                setState(graphicviewerdocumentchangededit.getValueInt(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewer3DRect",
                    domelement);
            domelement1.setAttribute("current_state", Integer.toString(dR));
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("current_state");
            dR = Integer.parseInt(s);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        boolean flag = true;
        Rectangle rectangle = getBoundingRect();
        switch (dR) {
            case 0 : // '\0'
                flag = true;
                break;

            case 1 : // '\001'
            case 2 : // '\002'
                flag = false;
                break;
        }
        draw3DRect(graphics2d, rectangle.x, rectangle.y, rectangle.width,
                rectangle.height, flag);
    }

    public static final int StateUp = 0;
    public static final int StateDown = 1;
    public static final int StateToggled = 2;
    public static final int ChangedState = 403;
    private int dR;
}