/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C), D. Campione
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
public class SimpleNode extends GraphicViewerNode {

    private static final long serialVersionUID = -4314447468625559972L;

    public SimpleNode() {
        myLabel = null;
        myIcon = null;
        myInputPort = null;
        myOutputPort = null;
    }

    public void initialize(Point point, Dimension dimension,
            GraphicViewerObject graphicviewerobject, String s, boolean flag,
            boolean flag1) {
        setInitializing(true);
        setDraggable(true);
        setResizable(false);
        set4ResizeHandles(true);
        myIcon = graphicviewerobject;
        if (myIcon != null) {
            myIcon.setBoundingRect(point, dimension);
            myIcon.setSelectable(false);
            addObjectAtHead(myIcon);
        }
        if (s != null)
            myLabel = new SimpleNodeLabel(s, this);
        if (flag)
            myInputPort = createPort(true);
        if (flag1)
            myOutputPort = createPort(false);
        setInitializing(false);
        layoutChildren(null);
        setTopLeft(point);
    }

    protected GraphicViewerPort createPort(boolean flag) {
        return new SimpleNodePort(flag, this);
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        SimpleNode simplenode = (SimpleNode) graphicviewerarea;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        simplenode.myIcon = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myIcon);
        simplenode.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
        simplenode.myInputPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myInputPort);
        simplenode.myOutputPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myOutputPort);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myIcon)
            myIcon = null;
        else if (graphicviewerobject == myLabel)
            myLabel = null;
        else if (graphicviewerobject == myInputPort)
            myInputPort = null;
        else if (graphicviewerobject == myOutputPort)
            myOutputPort = null;
        return graphicviewerobject;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        setInitializing(true);
        GraphicViewerObject graphicviewerobject1 = getIcon();
        GraphicViewerText graphicviewertext = getLabel();
        GraphicViewerPort graphicviewerport = getInputPort();
        GraphicViewerPort graphicviewerport1 = getOutputPort();
        if (graphicviewertext != null)
            if (graphicviewerobject1 != null)
                graphicviewertext.setSpotLocation(2, graphicviewerobject1, 6);
            else
                graphicviewertext.setSpotLocation(6, this, 6);
        if (graphicviewerport != null)
            if (graphicviewerobject1 != null)
                graphicviewerport.setSpotLocation(4, graphicviewerobject1, 8);
            else
                graphicviewerport.setSpotLocation(8, this, 8);
        if (graphicviewerport1 != null)
            if (graphicviewerobject1 != null)
                graphicviewerport1.setSpotLocation(8, graphicviewerobject1, 4);
            else
                graphicviewerport1.setSpotLocation(4, this, 4);
        setInitializing(false);
    }

    public void rescaleChildren(Rectangle rectangle) {
        if (myIcon != null) {
            int i = myIcon.getWidth();
            int j = myIcon.getHeight();
            if (i <= 0)
                i = 1;
            double d = (double) j / (double) i;
            int k = getWidth();
            int l = getHeight();
            if (myInputPort != null)
                k -= myInputPort.getWidth();
            if (myOutputPort != null)
                k -= myOutputPort.getWidth();
            if (myLabel != null)
                l -= myLabel.getHeight();
            double d1 = (double) l / (double) k;
            if (d < d1)
                l = (int) Math.rint(d * (double) k);
            else
                k = (int) Math.rint((double) l / d);
            myIcon.setSize(k, l);
        }
    }

    public Dimension getMinimumIconSize() {
        return new Dimension(20, 20);
    }

    public Dimension getMinimumSize() {
        int i = 0;
        int j = 0;
        if (myInputPort != null)
            i += myInputPort.getWidth();
        if (myOutputPort != null)
            i += myOutputPort.getWidth();
        Dimension dimension = getMinimumIconSize();
        i += dimension.width;
        j += dimension.height;
        if (myLabel != null) {
            i = Math.max(i, myLabel.getWidth());
            j += myLabel.getHeight();
        }
        return new Dimension(i, j);
    }

    public void setBoundingRect(int i, int j, int k, int l) {
        Dimension dimension = getMinimumSize();
        super.setBoundingRect(i, j, Math.max(k, dimension.width),
                Math.max(l, dimension.height));
    }

    protected Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        Dimension dimension = getMinimumSize();
        Rectangle rectangle1 = super.handleResize(graphics2d,
                graphicviewerview, rectangle, point, i, j,
                Math.max(k, dimension.width), Math.max(l, dimension.height));
        if (j == 2)
            setBoundingRect(rectangle1);
        return null;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.SimpleNode", domelement);
            if (myIcon != null)
                domdoc.registerReferencingNode(domelement1, "simpleicon",
                        myIcon);
            if (myInputPort != null)
                domdoc.registerReferencingNode(domelement1, "simpleinputport",
                        myInputPort);
            if (myOutputPort != null)
                domdoc.registerReferencingNode(domelement1, "simpleoutputport",
                        myOutputPort);
            if (myLabel != null)
                domdoc.registerReferencingNode(domelement1, "simplelabel",
                        myLabel);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("simpleicon");
            domdoc.registerReferencingObject(this, "simpleicon", s);
            String s1 = domelement1.getAttribute("simpleinputport");
            domdoc.registerReferencingObject(this, "simpleinputport", s1);
            String s2 = domelement1.getAttribute("simpleoutputport");
            domdoc.registerReferencingObject(this, "simpleoutputport", s2);
            String s3 = domelement1.getAttribute("simplelabel");
            domdoc.registerReferencingObject(this, "simplelabel", s3);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingElement());
        }
        return domelement.getNextSibling();
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("simpleicon"))
            myIcon = (GraphicViewerObject) obj;
        if (s.equals("simpleinputport"))
            myInputPort = (GraphicViewerPort) obj;
        if (s.equals("simpleoutputport"))
            myOutputPort = (GraphicViewerPort) obj;
        if (s.equals("simplelabel"))
            myLabel = (GraphicViewerText) obj;
    }

    public GraphicViewerText getLabel() {
        return myLabel;
    }

    public GraphicViewerObject getIcon() {
        return myIcon;
    }

    public GraphicViewerPort getInputPort() {
        return myInputPort;
    }

    public GraphicViewerPort getOutputPort() {
        return myOutputPort;
    }

    protected GraphicViewerText myLabel;
    protected GraphicViewerObject myIcon;
    protected GraphicViewerPort myInputPort;
    protected GraphicViewerPort myOutputPort;
}