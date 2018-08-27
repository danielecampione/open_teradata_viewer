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
public class OTVRoundRect extends GraphicViewerRoundRect {

    private static final long serialVersionUID = 8212713660721103524L;

    public OTVRoundRect(Point loc, Dimension size, Dimension arc) {
        super(loc, size, arc);
        updateFill();
    }

    public OTVRoundRect() {
        super();
        updateFill();
    }

    // Make sure there's a color gradient going from the top-left to the bottom
    // right of the rectangle, no matter where the rectangle is or how big it is
    public void geometryChange(Rectangle prevRect) {
        super.geometryChange(prevRect);
        updateFill();
    }

    // Do resizing continuously instead of having the user manipulate an XOR'd
    // bounding rectangle
    public Rectangle handleResize(Graphics2D g, GraphicViewerView view,
            Rectangle prevRect, Point newPoint, int whichHandle, int event,
            int minWidth, int minHeight) {
        Rectangle newRect = super.handleResize(g, view, prevRect, newPoint,
                whichHandle, event, minWidth, minHeight);
        // Resize continuously (default only does setBoundingRect on MouseUp)
        if (event == GraphicViewerView.EventMouseMove) {
            setBoundingRect(newRect);
        }
        // Don't draw XOR resize rectangle
        return null;
    }

    public void updateFill() {
        Rectangle rect = getBoundingRect();
        if (getBrush() != null
                && getBrush().getStyle() == GraphicViewerBrush.SOLID) {
            setBrush(new GraphicViewerBrush(getBrush().getPaint()));
            return;
        }
        setBrush(new GraphicViewerBrush(new GradientPaint(rect.x, rect.y,
                Color.magenta, rect.x + rect.width, rect.y + rect.height,
                Color.blue)));
    }

    public String getToolTipText() {
        if (getBrush() == null) {
            return "this doesn't have any brush filling in the inside";
        } else if (getBrush().getPaint() instanceof GradientPaint) {
            return "this is filled with a color gradient";
        } else {
            return "this is filled with a solid color";
        }
    }

    public void SVGWriteObject(IDomDoc svgDoc,
            IDomElement graphicViewerElementGroup) {
        // Add OTVRoundRect element
        IDomElement graphicViewerSampleNode = svgDoc
                .createGraphicViewerClassElement(
                        "net.sourceforge.open_teradata_viewer.graphic_viewer.OTVRoundRect",
                        graphicViewerElementGroup);
        // Have superclass add to the GraphicViewerObject group
        super.SVGWriteObject(svgDoc, graphicViewerElementGroup);
    }

    public IDomNode SVGReadObject(IDomDoc svgDoc,
            GraphicViewerDocument graphicViewerDoc, IDomElement svgElement,
            IDomElement graphicViewerChildElement) {
        if (graphicViewerChildElement != null) {
            // This is a OTVRoundRect element
            super.SVGReadObject(svgDoc, graphicViewerDoc, svgElement,
                    graphicViewerChildElement
                            .getNextSiblingGraphicViewerClassElement());
            updateFill();
        }
        return svgElement.getNextSibling();
    }

    public void SVGUpdateReference(String attr, Object referencedObject) {
        if (!attr.equals("drawablebrush")) {
            super.SVGUpdateReference(attr, referencedObject);
        }
    }
}