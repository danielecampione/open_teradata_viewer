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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class OpenTeradataViewerRoundRect extends GraphicViewerRoundRect {

    private static final long serialVersionUID = 8212713660721103524L;

    public OpenTeradataViewerRoundRect(Point point, Dimension dimension,
            Dimension dimension1) {
        super(point, dimension, dimension1);
        updateFill();
    }

    public OpenTeradataViewerRoundRect() {
        updateFill();
    }

    public void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        updateFill();
    }

    public Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        Rectangle rectangle1 = super.handleResize(graphics2d,
                graphicviewerview, rectangle, point, i, j, k, l);
        if (j == 2)
            setBoundingRect(rectangle1);
        return null;
    }

    public void updateFill() {
        Rectangle rectangle = getBoundingRect();
        if (getBrush() != null && getBrush().getStyle() == 65535) {
            setBrush(new GraphicViewerBrush(getBrush().getPaint()));
            return;
        } else {
            setBrush(new GraphicViewerBrush(new GradientPaint(rectangle.x,
                    rectangle.y, Color.magenta, rectangle.x + rectangle.width,
                    rectangle.y + rectangle.height, Color.blue)));
            return;
        }
    }

    public String getToolTipText() {
        if (getBrush() == null)
            return "this doesn't have any brush filling in the inside";
        if (getBrush().getPaint() instanceof GradientPaint)
            return "this is filled with a color gradient";
        else
            return "this is filled with a solid color";
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        @SuppressWarnings("unused")
        DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                "net.sourceforge.open_teradata_viewer.graphicviewer.OpenTeradataViewerRoundRect", domelement);
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            updateFill();
        }
        return domelement.getNextSibling();
    }

    public void SVGUpdateReference(String s, Object obj) {
        if (!s.equals("drawablebrush"))
            super.SVGUpdateReference(s, obj);
    }
}