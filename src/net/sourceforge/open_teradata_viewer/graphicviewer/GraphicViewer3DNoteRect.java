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
public class GraphicViewer3DNoteRect extends GraphicViewerRectangle {

    private static final long serialVersionUID = 4965885360423863405L;

    public GraphicViewer3DNoteRect() {
        dK = 4;
        dJ = 8;
    }

    public GraphicViewer3DNoteRect(Rectangle rectangle) {
        super(rectangle);
        dK = 4;
        dJ = 8;
    }

    public GraphicViewer3DNoteRect(Point point, Dimension dimension) {
        super(point, dimension);
        dK = 4;
        dJ = 8;
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewer3DNoteRect graphicviewer3dnoterect = (GraphicViewer3DNoteRect) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewer3dnoterect != null) {
            graphicviewer3dnoterect.dK = dK;
            graphicviewer3dnoterect.dJ = dJ;
        }
        return graphicviewer3dnoterect;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewer3DNoteRect",
                    domelement);
            domelement1.setAttribute("flapsize", Integer.toString(dJ));
            domelement1.setAttribute("shadow", Integer.toString(dK));
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            setFlapSize(Integer.parseInt(domelement1.getAttribute("flapsize")));
            setShadowSize(Integer.parseInt(domelement1.getAttribute("shadow")));
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        Rectangle rectangle = getBoundingRect();
        int i = getShadowSize();
        int j = rectangle.x;
        int k = rectangle.y;
        int l = rectangle.width - i;
        int i1 = rectangle.height - i;
        drawRect(graphics2d, j, k, l, i1);
        int ai[] = graphicviewerview._mthdo(6);
        int ai1[] = graphicviewerview._mthif(6);
        ai[0] = j + l;
        ai1[0] = k;
        ai[1] = rectangle.x + rectangle.width;
        ai1[1] = rectangle.y + i;
        ai[2] = rectangle.x + rectangle.width;
        ai1[2] = rectangle.y + rectangle.height;
        ai[3] = rectangle.x + i;
        ai1[3] = rectangle.y + rectangle.height;
        ai[4] = j;
        ai1[4] = k + i1;
        ai[5] = j + l;
        ai1[5] = k + i1;
        drawPolygon(graphics2d, null, GraphicViewerBrush.lightGray, ai, ai1, 6);
        int j1 = getFlapSize();
        ai[0] = (j + l) - j1;
        ai1[0] = k + i1;
        ai[1] = (j + l) - (j1 * 7) / 8;
        ai1[1] = (k + i1) - (j1 * 7) / 8;
        ai[2] = j + l;
        ai1[2] = (k + i1) - j1;
        drawPolygon(graphics2d, null, GraphicViewerBrush.gray, ai, ai1, 3);
    }

    public int getShadowSize() {
        return dK;
    }

    public void setShadowSize(int i) {
        int j = dK;
        if (j != i) {
            dK = Math.max(i, 0);
            update(401, j, null);
        }
    }

    public int getFlapSize() {
        return dJ;
    }

    public void setFlapSize(int i) {
        int j = dJ;
        if (j != i) {
            dJ = Math.max(i, 0);
            update(402, j, null);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 401 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getShadowSize());
                return;

            case 402 :
                graphicviewerdocumentchangededit.setNewValueInt(getFlapSize());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 401 :
                setShadowSize(graphicviewerdocumentchangededit
                        .getValueInt(flag));
                return;

            case 402 :
                setFlapSize(graphicviewerdocumentchangededit.getValueInt(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public static final int ChangedShadowSize = 401;
    public static final int ChangedFlapSize = 402;
    private int dK;
    private int dJ;
}