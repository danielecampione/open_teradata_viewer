/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2013, D. Campione
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

    private static Dimension DefaultPortSize = new Dimension(7, 7);
    public static final int ChangedLabel = 2201;
    public static final int ChangedIcon = 2202;
    public static final int ChangedPort = 2203;
    public static final int ChangedLabelOffset = 2204;
    public static final int ChangedDraggableLabel = 2205;
    private GraphicViewerObject myIcon = null;
    private GraphicViewerText myLabel = null;
    private GraphicViewerPort myPort = null;
    private Dimension myLabelOffset = new Dimension(-999999, -999999);
    private boolean myDraggableLabel = false;

    public GraphicViewerIconicNode() {
        initCommon();
    }

    public GraphicViewerIconicNode(String s) {
        initCommon();
        init(s);
    }

    private void initCommon() {
        setInternalFlags(getInternalFlags() & 0xffffffef | 0x20);
    }

    private void init(String s) {
        setInitializing(true);
        myIcon = createIcon(s);
        myLabel = createLabel(s);
        myPort = createPort();
        addObjectAtHead(myIcon);
        addObjectAtTail(myLabel);
        addObjectAtTail(myPort);
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
        graphicviewerport.setSize(DefaultPortSize);
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
        graphicviewericonicnode.myDraggableLabel = myDraggableLabel;
        graphicviewericonicnode.myLabelOffset = new Dimension(myLabelOffset);
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewericonicnode.myIcon = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myIcon);
        graphicviewericonicnode.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
        graphicviewericonicnode.myPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myPort);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myIcon) {
            myIcon = null;
        } else if (graphicviewerobject == myLabel) {
            myLabel = null;
        } else if (graphicviewerobject == myPort) {
            myPort = null;
        }
        return graphicviewerobject;
    }

    public Point getLocation(Point point) {
        if (getIcon() != null) {
            return getIcon().getSpotLocation(0, point);
        } else {
            return getSpotLocation(0, point);
        }
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
            if (i != l || j != j1) {
                setTopLeft(i - rectangle1.width / 2, j - rectangle1.height / 2);
            }
        }
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing()) {
            return;
        }
        GraphicViewerObject graphicviewerobject1 = getIcon();
        if (graphicviewerobject1 == null) {
            return;
        }
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            if (isDraggableLabel() && graphicviewerobject == graphicviewertext) {
                myLabelOffset.width = graphicviewertext.getLeft()
                        - graphicviewerobject1.getLeft();
                myLabelOffset.height = graphicviewertext.getTop()
                        - graphicviewerobject1.getTop();
            } else if (myLabelOffset.width > 0xfff0bdc1) {
                graphicviewertext.setTopLeft(graphicviewerobject1.getLeft()
                        + myLabelOffset.width, graphicviewerobject1.getTop()
                        + myLabelOffset.height);
            } else {
                graphicviewertext.setSpotLocation(2, graphicviewerobject1, 6);
            }
        }
        if (myPort != null) {
            myPort.setSpotLocation(0, graphicviewerobject1, 0);
        }
    }

    public Dimension getLabelOffset() {
        return myLabelOffset;
    }

    public void setLabelOffset(int i, int j) {
        internalSetLabelOffset(i, j, false);
    }

    private void internalSetLabelOffset(int i, int j, boolean flag) {
        Dimension dimension = myLabelOffset;
        if (dimension.width != i || dimension.height != j) {
            dimension = new Dimension(dimension);
            myLabelOffset.width = i;
            myLabelOffset.height = j;
            update(2204, 0, dimension);
            if (!flag) {
                layoutChildren(null);
            }
        }
    }

    public void setLabelOffset(Dimension dimension) {
        setLabelOffset(dimension.width, dimension.height);
    }

    public boolean isDraggableLabel() {
        return myDraggableLabel;
    }

    public void setDraggableLabel(boolean flag) {
        internalSetDraggableLabel(flag, false);
    }

    private void internalSetDraggableLabel(boolean flag, boolean flag1) {
        boolean flag2 = myDraggableLabel;
        if (flag2 != flag) {
            myDraggableLabel = flag;
            update(2205, flag2 ? 1 : 0, null);
            if (!flag1 && getLabel() != null) {
                getLabel().setSelectable(flag);
                getLabel().setDragsNode(!flag);
            }
        }
    }

    public GraphicViewerObject getIcon() {
        return myIcon;
    }

    public void setIcon(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myIcon;
        if (graphicviewerobject1 != graphicviewerobject) {
            copyProperties(graphicviewerobject1, graphicviewerobject);
            if (graphicviewerobject1 != null) {
                removeObject(graphicviewerobject1);
            }
            myIcon = graphicviewerobject;
            if (graphicviewerobject != null) {
                if (graphicviewerobject1 == null) {
                    graphicviewerobject.setSelectable(false);
                    graphicviewerobject.setResizable(false);
                }
                addObjectAtHead(graphicviewerobject);
            }
            update(2202, 0, graphicviewerobject1);
            if (getPort() != null
                    && getPort().getPortObject() == graphicviewerobject1) {
                getPort().setPortObject(graphicviewerobject);
            }
        }
    }

    public GraphicViewerText getLabel() {
        return myLabel;
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
        GraphicViewerText graphicviewertext1 = myLabel;
        if (graphicviewertext1 != graphicviewertext) {
            if (graphicviewertext1 != null) {
                removeObject(graphicviewertext1);
            }
            myLabel = graphicviewertext;
            if (graphicviewertext != null) {
                addObjectAtTail(graphicviewertext);
            }
            update(2201, 0, graphicviewertext1);
        }
    }

    public GraphicViewerPort getPort() {
        return myPort;
    }

    public void setPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                removeObject(graphicviewerport1);
            }
            myPort = graphicviewerport;
            if (graphicviewerport != null) {
                addObjectAtTail(graphicviewerport);
            }
            update(2203, 0, graphicviewerport1);
            if (graphicviewerport != null
                    && graphicviewerport.getPortObject() == null) {
                graphicviewerport.setPortObject(getIcon());
            }
        }
    }

    public GraphicViewerImage getImage() {
        GraphicViewerObject graphicviewerobject = getIcon();
        if (graphicviewerobject instanceof GraphicViewerImage) {
            return (GraphicViewerImage) graphicviewerobject;
        } else {
            return null;
        }
    }

    public String getText() {
        if (getLabel() != null) {
            return getLabel().getText();
        } else {
            return "";
        }
    }

    public void setText(String s) {
        if (s == null) {
            return;
        }
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null) {
            myLabel = createLabel(s);
            myLabelOffset.width = 0xf423f;
            myLabelOffset.height = 0xfff0bdc1;
            addObjectAtTail(myLabel);
        } else {
            graphicviewertext.setText(s);
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("icon")) {
            myIcon = (GraphicViewerObject) obj;
        } else if (s.equals("label")) {
            myLabel = (GraphicViewerText) obj;
        } else if (s.equals("port")) {
            myPort = (GraphicViewerPort) obj;
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerIconicNode",
                            domelement);
            domelement1.setAttribute("draggablelabel", myDraggableLabel
                    ? "true"
                    : "false");
            domelement1.setAttribute("labeloffsetx",
                    Integer.toString(myLabelOffset.width));
            domelement1.setAttribute("labeloffsety",
                    Integer.toString(myLabelOffset.height));
            if (myIcon != null) {
                domdoc.registerReferencingNode(domelement1, "icon", myIcon);
            }
            if (myLabel != null) {
                domdoc.registerReferencingNode(domelement1, "label", myLabel);
            }
            if (myPort != null) {
                domdoc.registerReferencingNode(domelement1, "port", myPort);
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            myDraggableLabel = domelement1.getAttribute("draggablelabel")
                    .equals("true");
            String s = domelement1.getAttribute("labeloffsetx");
            if (s.length() > 0) {
                myLabelOffset.width = Integer.parseInt(s);
            }
            String s1 = domelement1.getAttribute("labeloffsety");
            if (s1.length() > 0) {
                myLabelOffset.height = Integer.parseInt(s1);
            }
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
                internalSetLabelOffset(dimension.width, dimension.height, true);
                return;

            case 2205 :
                internalSetDraggableLabel(
                        graphicviewerdocumentchangededit.getValueInt(flag) == 1,
                        true);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }
}