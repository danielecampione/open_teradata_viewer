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

import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MultiPortNode extends GraphicViewerNode {

    private static final long serialVersionUID = -1010086922666954056L;

    public MultiPortNode() {
        myLabel = null;
        myIcon = null;
    }

    public void initialize(Point point,
            GraphicViewerObject graphicviewerobject, String s) {
        setInitializing(true);
        setDraggable(true);
        setResizable(false);
        myIcon = graphicviewerobject;
        graphicviewerobject.setTopLeft(point);
        graphicviewerobject.setSelectable(false);
        addObjectAtHead(graphicviewerobject);
        if (s != null)
            myLabel = new MultiPortNodeLabel(s, graphicviewerobject, this);
        setInitializing(false);
        layoutChildren(null);
        setTopLeft(point);
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        MultiPortNode multiportnode = (MultiPortNode) graphicviewerarea;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        multiportnode.myIcon = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myIcon);
        multiportnode.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myLabel)
            myLabel = null;
        else if (graphicviewerobject == myIcon)
            myIcon = null;
        return graphicviewerobject;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.MultiPortNode",
                    domelement);
            if (myIcon != null)
                domdoc.registerReferencingNode(domelement1, "multiporticon",
                        myIcon);
            if (myLabel != null)
                domdoc.registerReferencingNode(domelement1, "multiportlabel",
                        myLabel);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("multiporticon");
            domdoc.registerReferencingObject(this, "multiporticon", s);
            String s1 = domelement1.getAttribute("multiportlabel");
            domdoc.registerReferencingObject(this, "multiportlabel", s1);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingElement());
        }
        return domelement.getNextSibling();
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("multiporticon"))
            myIcon = (GraphicViewerObject) obj;
        if (s.equals("multiportlabel"))
            myLabel = (GraphicViewerText) obj;
    }

    public GraphicViewerText getLabel() {
        return myLabel;
    }

    public GraphicViewerObject getIcon() {
        return myIcon;
    }

    protected GraphicViewerText myLabel;
    protected GraphicViewerObject myIcon;
}