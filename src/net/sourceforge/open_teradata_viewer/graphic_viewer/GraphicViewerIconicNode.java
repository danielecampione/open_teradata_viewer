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
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerIconicNode extends GraphicViewerNode {

    private static final long serialVersionUID = 6360402985276259207L;

    public GraphicViewerIconicNode() {
        c9 = null;
        c6 = null;
        c5 = null;
        c7 = new Dimension(0xfff0bdc1, 0xfff0bdc1);
        c8 = false;
        q();
    }

    public GraphicViewerIconicNode(String s) {
        c9 = null;
        c6 = null;
        c5 = null;
        c7 = new Dimension(0xfff0bdc1, 0xfff0bdc1);
        c8 = false;
        q();
        _mthif(s);
    }

    private void q() {
        _mthfor(g() & 0xffffffef | 0x20);
    }

    private void _mthif(String s) {
        setInitializing(true);
        c9 = createIcon(s);
        c6 = createLabel(s);
        c5 = createPort();
        addObjectAtHead(c9);
        addObjectAtTail(c6);
        addObjectAtTail(c5);
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerObject createIcon(String s) {
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage();
        graphicviewerimage.setSelectable(false);
        graphicviewerimage.setResizable(false);
        graphicviewerimage.setSize(32, 32);
        return graphicviewerimage;
    }

    public GraphicViewerPort createPort() {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        graphicviewerport.setStyle(0);
        graphicviewerport.setFromSpot(-1);
        graphicviewerport.setToSpot(-1);
        graphicviewerport.setSize(c4);
        graphicviewerport.setPortObject(getIcon());
        return graphicviewerport;
    }

    public GraphicViewerText createLabel(String s) {
        if (s != null) {
            GraphicViewerText graphicviewertext = new GraphicViewerText();
            graphicviewertext.setSelectable(false);
            graphicviewertext.setResizable(false);
            graphicviewertext.setDragsNode(true);
            graphicviewertext.setEditable(true);
            graphicviewertext.setEditOnSingleClick(true);
            graphicviewertext.setTransparent(true);
            graphicviewertext.setAlignment(2);
            graphicviewertext.setText(s);
            return graphicviewertext;
        } else {
            return null;
        }
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerIconicNode graphicviewericonicnode = (GraphicViewerIconicNode) graphicviewerarea;
        graphicviewericonicnode.c8 = c8;
        graphicviewericonicnode.c7 = new Dimension(c7);
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewericonicnode.c9 = (GraphicViewerObject) graphicviewercopyenvironment
                .get(c9);
        graphicviewericonicnode.c6 = (GraphicViewerText) graphicviewercopyenvironment
                .get(c6);
        graphicviewericonicnode.c5 = (GraphicViewerPort) graphicviewercopyenvironment
                .get(c5);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == c9)
            c9 = null;
        else if (graphicviewerobject == c6)
            c6 = null;
        else if (graphicviewerobject == c5)
            c5 = null;
        return graphicviewerobject;
    }

    public Point getLocation(Point point) {
        if (getIcon() != null)
            return getIcon().getSpotLocation(0, point);
        else
            return getSpotLocation(0, point);
    }

    public void setLocation(int i, int j) {
        if (getIcon() != null) {
            Rectangle rectangle = getIcon().getBoundingRect();
            int k = rectangle.x + rectangle.width / 2;
            int i1 = rectangle.y + rectangle.height / 2;
            if (i != k || j != i1) {
                int k1 = getLeft();
                int l1 = getTop();
                setTopLeft(i - (k - k1), j - (i1 - l1));
            }
        } else {
            Rectangle rectangle1 = getBoundingRect();
            int l = rectangle1.x + rectangle1.width / 2;
            int j1 = rectangle1.y + rectangle1.height / 2;
            if (i != l || j != j1)
                setTopLeft(i - rectangle1.width / 2, j - rectangle1.height / 2);
        }
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        GraphicViewerObject graphicviewerobject1 = getIcon();
        if (graphicviewerobject1 == null)
            return;
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null)
            if (isDraggableLabel() && graphicviewerobject == graphicviewertext) {
                c7.width = graphicviewertext.getLeft()
                        - graphicviewerobject1.getLeft();
                c7.height = graphicviewertext.getTop()
                        - graphicviewerobject1.getTop();
            } else if (c7.width > 0xfff0bdc1)
                graphicviewertext.setTopLeft(graphicviewerobject1.getLeft()
                        + c7.width, graphicviewerobject1.getTop() + c7.height);
            else
                graphicviewertext.setSpotLocation(2, graphicviewerobject1, 6);
        if (c5 != null)
            c5.setSpotLocation(0, graphicviewerobject1, 0);
    }

    public Dimension getLabelOffset() {
        return c7;
    }

    public void setLabelOffset(int i, int j) {
        a(i, j, false);
    }

    private void a(int i, int j, boolean flag) {
        Dimension dimension = c7;
        if (dimension.width != i || dimension.height != j) {
            dimension = new Dimension(dimension);
            c7.width = i;
            c7.height = j;
            update(2204, 0, dimension);
            if (!flag)
                layoutChildren(null);
        }
    }

    public void setLabelOffset(Dimension dimension) {
        setLabelOffset(dimension.width, dimension.height);
    }

    public boolean isDraggableLabel() {
        return c8;
    }

    public void setDraggableLabel(boolean flag) {
        _mthif(flag, false);
    }

    private void _mthif(boolean flag, boolean flag1) {
        boolean flag2 = c8;
        if (flag2 != flag) {
            c8 = flag;
            update(2205, flag2 ? 1 : 0, null);
            if (!flag1 && getLabel() != null) {
                getLabel().setSelectable(flag);
                getLabel().setDragsNode(!flag);
            }
        }
    }

    public GraphicViewerObject getIcon() {
        return c9;
    }

    public void setIcon(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = c9;
        if (graphicviewerobject1 != graphicviewerobject) {
            a(graphicviewerobject1, graphicviewerobject);
            if (graphicviewerobject1 != null)
                removeObject(graphicviewerobject1);
            c9 = graphicviewerobject;
            if (graphicviewerobject != null) {
                if (graphicviewerobject1 == null) {
                    graphicviewerobject.setSelectable(false);
                    graphicviewerobject.setResizable(false);
                }
                addObjectAtHead(graphicviewerobject);
            }
            update(2202, 0, graphicviewerobject1);
            if (getPort() != null
                    && getPort().getPortObject() == graphicviewerobject1)
                getPort().setPortObject(graphicviewerobject);
        }
    }

    public GraphicViewerText getLabel() {
        return c6;
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
        GraphicViewerText graphicviewertext1 = c6;
        if (graphicviewertext1 != graphicviewertext) {
            if (graphicviewertext1 != null)
                removeObject(graphicviewertext1);
            c6 = graphicviewertext;
            if (graphicviewertext != null)
                addObjectAtTail(graphicviewertext);
            update(2201, 0, graphicviewertext1);
        }
    }

    public GraphicViewerPort getPort() {
        return c5;
    }

    public void setPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = c5;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null)
                removeObject(graphicviewerport1);
            c5 = graphicviewerport;
            if (graphicviewerport != null)
                addObjectAtTail(graphicviewerport);
            update(2203, 0, graphicviewerport1);
            if (graphicviewerport != null
                    && graphicviewerport.getPortObject() == null)
                graphicviewerport.setPortObject(getIcon());
        }
    }

    public GraphicViewerImage getImage() {
        GraphicViewerObject graphicviewerobject = getIcon();
        if (graphicviewerobject instanceof GraphicViewerImage)
            return (GraphicViewerImage) graphicviewerobject;
        else
            return null;
    }

    public String getText() {
        if (getLabel() != null)
            return getLabel().getText();
        else
            return "";
    }

    public void setText(String s) {
        if (s == null)
            return;
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null) {
            c6 = createLabel(s);
            c7.width = 0xf423f;
            c7.height = 0xfff0bdc1;
            addObjectAtTail(c6);
        } else {
            graphicviewertext.setText(s);
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("icon"))
            c9 = (GraphicViewerObject) obj;
        else if (s.equals("label"))
            c6 = (GraphicViewerText) obj;
        else if (s.equals("port"))
            c5 = (GraphicViewerPort) obj;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerIconicNode",
                            domelement);
            domelement1.setAttribute("draggablelabel", c8 ? "true" : "false");
            domelement1
                    .setAttribute("labeloffsetx", Integer.toString(c7.width));
            domelement1.setAttribute("labeloffsety",
                    Integer.toString(c7.height));
            if (c9 != null)
                domdoc.registerReferencingNode(domelement1, "icon", c9);
            if (c6 != null)
                domdoc.registerReferencingNode(domelement1, "label", c6);
            if (c5 != null)
                domdoc.registerReferencingNode(domelement1, "port", c5);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            c8 = domelement1.getAttribute("draggablelabel").equals("true");
            String s = domelement1.getAttribute("labeloffsetx");
            if (s.length() > 0)
                c7.width = Integer.parseInt(s);
            String s1 = domelement1.getAttribute("labeloffsety");
            if (s1.length() > 0)
                c7.height = Integer.parseInt(s1);
            String s2 = domelement1.getAttribute("icon");
            domdoc.registerReferencingObject(this, "icon", s2);
            String s3 = domelement1.getAttribute("label");
            domdoc.registerReferencingObject(this, "label", s3);
            String s4 = domelement1.getAttribute("port");
            domdoc.registerReferencingObject(this, "port", s4);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2202 :
                graphicviewerdocumentchangededit.setNewValue(getIcon());
                return;

            case 2201 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;

            case 2203 :
                graphicviewerdocumentchangededit.setNewValue(getPort());
                return;

            case 2204 :
                graphicviewerdocumentchangededit.setNewValue(new Dimension(
                        getLabelOffset()));
                return;

            case 2205 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(isDraggableLabel() ? 1 : 0);
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2202 :
                setIcon((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2201 :
                setLabel((GraphicViewerText) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2203 :
                setPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2204 :
                Dimension dimension = (Dimension) graphicviewerdocumentchangededit
                        .getValue(flag);
                a(dimension.width, dimension.height, true);
                return;

            case 2205 :
                _mthif(graphicviewerdocumentchangededit.getValueInt(flag) == 1,
                        true);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    private static Dimension c4 = new Dimension(7, 7);
    public static final int ChangedLabel = 2201;
    public static final int ChangedIcon = 2202;
    public static final int ChangedPort = 2203;
    public static final int ChangedLabelOffset = 2204;
    public static final int ChangedDraggableLabel = 2205;
    private GraphicViewerObject c9;
    private GraphicViewerText c6;
    private GraphicViewerPort c5;
    private Dimension c7;
    private boolean c8;
}