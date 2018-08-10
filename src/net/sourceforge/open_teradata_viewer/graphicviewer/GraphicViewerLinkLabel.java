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

import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerLinkLabel extends GraphicViewerText {

    private static final long serialVersionUID = -2997165139972895214L;

    public GraphicViewerLinkLabel() {
        eQ = null;
        D();
    }

    public GraphicViewerLinkLabel(String s) {
        super(s);
        eQ = null;
        D();
    }

    public GraphicViewerLinkLabel(Point point, int i, String s, String s1,
            boolean flag, boolean flag1, boolean flag2, int j, boolean flag3,
            boolean flag4) {
        super(point, i, s, s1, flag, flag1, flag2, j, flag3, flag4);
        eQ = null;
        D();
    }

    private final void D() {
        setTransparent(true);
        setDraggable(false);
        setResizable(false);
    }

    public GraphicViewerObject redirectSelection() {
        GraphicViewerLabeledLink graphicviewerlabeledlink = getLabeledLink();
        if (graphicviewerlabeledlink != null
                && graphicviewerlabeledlink.isGrabChildSelection())
            return graphicviewerlabeledlink;
        else
            return this;
    }

    protected void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        GraphicViewerLabeledLink graphicviewerlabeledlink = getLabeledLink();
        if (graphicviewerlabeledlink != null)
            graphicviewerlabeledlink.geometryChangeChild(this, rectangle);
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerLinkLabel",
                    domelement);
            domdoc.registerReferencingNode(domelement1, "labeledlink",
                    getLabeledLink());
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("labeledlink");
            domdoc.registerReferencingObject(this, "labeledlink", s);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("labeledlink"))
            setPartner((GraphicViewerObject) obj);
    }

    public GraphicViewerLabeledLink getLabeledLink() {
        GraphicViewerObject graphicviewerobject = getPartner();
        if (graphicviewerobject instanceof GraphicViewerLabeledLink)
            return (GraphicViewerLabeledLink) graphicviewerobject;
        else
            return null;
    }

    public GraphicViewerObject getPartner() {
        return eQ;
    }

    public void setPartner(GraphicViewerObject graphicviewerobject) {
        eQ = graphicviewerobject;
    }

    private GraphicViewerObject eQ;
}