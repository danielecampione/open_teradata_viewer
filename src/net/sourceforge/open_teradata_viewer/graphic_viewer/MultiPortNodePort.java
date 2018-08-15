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

import java.awt.Dimension;
import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MultiPortNodePort extends GraphicViewerPort {

    private static final long serialVersionUID = 7012335540736913360L;

    public MultiPortNodePort() {
        myMaxLinks = 3;
    }

    public MultiPortNodePort(boolean flag, boolean flag1, int i, Point point,
            Dimension dimension, GraphicViewerObject graphicviewerobject,
            GraphicViewerArea graphicviewerarea) {
        super(new Point(graphicviewerobject.getLeft() + point.x,
                graphicviewerobject.getTop() + point.y), dimension);
        myMaxLinks = 3;
        initialize(flag, flag1, i, point, graphicviewerobject,
                graphicviewerarea);
    }

    public void initialize(boolean flag, boolean flag1, int i, Point point,
            GraphicViewerObject graphicviewerobject,
            GraphicViewerArea graphicviewerarea) {
        setDraggable(false);
        setStyle(2);
        setPen(GraphicViewerPen.darkGray);
        setBrush(null);
        setValidDestination(flag);
        setValidSource(flag1);
        setFromSpot(i);
        setToSpot(i);
        setTopLeft(graphicviewerobject.getLeft() + point.x,
                graphicviewerobject.getTop() + point.y);
        graphicviewerarea.addObjectAtTail(this);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        MultiPortNodePort multiportnodeport = (MultiPortNodePort) super
                .copyObject(graphicviewercopyenvironment);
        if (multiportnodeport != null)
            multiportnodeport.myMaxLinks = myMaxLinks;
        return multiportnodeport;
    }

    public MultiPortNode getNode() {
        return (MultiPortNode) getParent();
    }

    public int getMaxLinks() {
        return myMaxLinks;
    }

    public void setMaxLinks(int i) {
        int j = myMaxLinks;
        if (j != i && i >= 0) {
            myMaxLinks = i;
            update(0x1277d, j, null);
        }
    }

    public int getNumValidLinks() {
        int i = 0;
        GraphicViewerListPosition graphicviewerlistposition = getFirstLinkPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerLink graphicviewerlink = getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextLinkPos(graphicviewerlistposition);
            if (graphicviewerlink.isVisible()
                    && graphicviewerlink.getDocument() != null
                    && graphicviewerlink.getFromPort().getDocument() != null
                    && graphicviewerlink.getToPort().getDocument() != null)
                i++;
        } while (true);
        return i;
    }

    public boolean isValidSource() {
        return getMaxLinks() > 0 && getNumValidLinks() < getMaxLinks()
                && super.isValidSource();
    }

    public boolean isValidDestination() {
        return getMaxLinks() > 0 && getNumValidLinks() < getMaxLinks()
                && super.isValidDestination();
    }

    public boolean validLink(GraphicViewerPort graphicviewerport) {
        return super.validLink(graphicviewerport)
                && getNumValidLinks() < getMaxLinks()
                && (!(graphicviewerport instanceof MultiPortNodePort) || ((MultiPortNodePort) graphicviewerport)
                        .getNumValidLinks() < ((MultiPortNodePort) graphicviewerport)
                        .getMaxLinks());
    }

    public void linkChange() {
        int i = getNumLinks();
        if (i <= 0)
            setBrush(EMPTY);
        else if (i < getMaxLinks())
            setBrush(OK);
        else
            setBrush(FULL);
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75645 :
                graphicviewerdocumentchangededit.setNewValueInt(getMaxLinks());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75645 :
                setMaxLinks(graphicviewerdocumentchangededit.getValueInt(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.MultiPortNodePort",
                    domelement);
            domelement1.setAttribute("maxlinks", Integer.toString(myMaxLinks));
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            myMaxLinks = Integer.parseInt(domelement1.getAttribute("maxlinks"));
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public static GraphicViewerBrush EMPTY;
    public static GraphicViewerBrush OK;
    public static GraphicViewerBrush FULL;
    public static final int ChangedMaxLinks = 0x1277d;
    private int myMaxLinks;

    static {
        EMPTY = GraphicViewerBrush.Null;
        OK = GraphicViewerBrush.green;
        FULL = GraphicViewerBrush.red;
    }
}