/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2014, D. Campione
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

import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SimpleNodePort extends GraphicViewerPort {

    private static final long serialVersionUID = -8414352632275407696L;

    public SimpleNodePort() {
    }

    public SimpleNodePort(boolean flag, GraphicViewerArea graphicviewerarea) {
        super(TriangleRect());
        initialize(flag, graphicviewerarea);
    }

    public void initialize(boolean flag, GraphicViewerArea graphicviewerarea) {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setStyle(3);
        setPen(GraphicViewerPen.darkGray);
        setBrush(GraphicViewerBrush.lightGray);
        if (flag) {
            setValidSource(false);
            setValidDestination(true);
            setToSpot(8);
        } else {
            setValidSource(true);
            setValidDestination(false);
            setFromSpot(4);
        }
        setTopLeft(graphicviewerarea.getLeft(), graphicviewerarea.getTop());
        graphicviewerarea.addObjectAtTail(this);
    }

    public final boolean isInput() {
        return isValidDestination();
    }

    public final boolean isOutput() {
        return isValidSource();
    }

    public boolean validLink(GraphicViewerPort graphicviewerport) {
        return super.validLink(graphicviewerport) && isOutput()
                && (graphicviewerport instanceof SimpleNodePort)
                && ((SimpleNodePort) graphicviewerport).isInput();
    }

    public SimpleNode getNode() {
        return (SimpleNode) getParent();
    }

    public static Rectangle TriangleRect() {
        return myTriangleRect;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.SimpleNodePort",
                    domelement);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingElement());
        return domelement.getNextSibling();
    }

    private static Rectangle myTriangleRect = new Rectangle(0, 0, 8, 8);
}