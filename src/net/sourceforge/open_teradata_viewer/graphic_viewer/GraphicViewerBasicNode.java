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
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerBasicNode extends GraphicViewerNode {

    private static final long serialVersionUID = 6233692699831637870L;

    private static Dimension DefaultPortSize = new Dimension(7, 7);
    private static Dimension DefaultDrawableMargin = new Dimension(7, 7);
    public static final int ChangedLabelSpot = 2101;
    public static final int ChangedDrawable = 2102;
    public static final int ChangedLabel = 2103;
    public static final int ChangedPort = 2104;
    public static final int ChangedInsets = 2105;
    public static final int ChangedAutoResize = 2106;
    private static final int flagAutoResize = 65536;
    private GraphicViewerDrawable myDrawable = null;
    private GraphicViewerText myLabel = null;
    private GraphicViewerPort myPort = null;
    private int myLabelSpot = 2;
    private Insets myInsets = new Insets(5, 10, 5, 10);

    public GraphicViewerBasicNode() {
        initCommon();
    }

    public GraphicViewerBasicNode(String s) {
        initCommon();
        init(s);
    }

    private void initCommon() {
        setInternalFlags(getInternalFlags() & 0xffffffef | 0x10000);
    }

    private void init(String s) {
        setInitializing(true);
        myPort = createPort();
        myDrawable = createDrawable(myPort);
        myLabel = createLabel(s);
        addObjectAtHead(myDrawable);
        addObjectAtTail(myPort);
        addObjectAtTail(myLabel);
        if (myPort != null) {
            myPort.setPortObject(myDrawable);
        }
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerPort createPort() {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        if (getLabelSpot() == 0) {
            graphicviewerport.setStyle(0);
        } else {
            graphicviewerport.setStyle(2);
        }
        graphicviewerport.setFromSpot(-1);
        graphicviewerport.setToSpot(-1);
        graphicviewerport.setSize(DefaultPortSize);
        return graphicviewerport;
    }

    public GraphicViewerDrawable createDrawable(
            GraphicViewerPort graphicviewerport) {
        GraphicViewerEllipse graphicviewerellipse = new GraphicViewerEllipse();
        Dimension dimension = DefaultDrawableMargin;
        graphicviewerellipse.setSize(graphicviewerport.getWidth() + 2
                * dimension.width, graphicviewerport.getHeight() + 2
                * dimension.height);
        graphicviewerellipse.setSelectable(false);
        graphicviewerellipse.setResizable(false);
        graphicviewerellipse.setBrush(GraphicViewerBrush.white);
        return graphicviewerellipse;
    }

    public GraphicViewerText createLabel(String s) {
        if (s != null) {
            GraphicViewerText graphicviewertext = new GraphicViewerText(s);
            graphicviewertext.setSelectable(false);
            graphicviewertext.setDraggable(false);
            graphicviewertext.setEditable(false);
            graphicviewertext.setEditOnSingleClick(true);
            graphicviewertext.setTransparent(true);
            return graphicviewertext;
        } else {
            return null;
        }
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerBasicNode graphicviewerbasicnode = (GraphicViewerBasicNode) graphicviewerarea;
        graphicviewerbasicnode.myLabelSpot = myLabelSpot;
        graphicviewerbasicnode.myInsets = new Insets(myInsets.top,
                myInsets.left, myInsets.bottom, myInsets.right);
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewerbasicnode.myDrawable = (GraphicViewerDrawable) graphicviewercopyenvironment
                .get(myDrawable);
        graphicviewerbasicnode.myPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myPort);
        graphicviewerbasicnode.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myDrawable) {
            myDrawable = null;
        } else if (graphicviewerobject == myLabel) {
            myLabel = null;
        } else if (graphicviewerobject == myPort) {
            myPort = null;
        }
        return graphicviewerobject;
    }

    public Point getLocation(Point point) {
        if (getDrawable() != null) {
            return getDrawable().getSpotLocation(0, point);
        } else {
            return getSpotLocation(0, point);
        }
    }

    public void setLocation(int i, int j) {
        if (getDrawable() != null) {
            Rectangle rectangle = getDrawable().getBoundingRect();
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
        GraphicViewerDrawable graphicviewerdrawable = getDrawable();
        if (graphicviewerdrawable == null) {
            return;
        }
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            int i = getLabelSpot();
            if (i == 0) {
                int j = graphicviewerdrawable.getLeft()
                        + graphicviewerdrawable.getWidth() / 2;
                int k = graphicviewerdrawable.getTop()
                        + graphicviewerdrawable.getHeight() / 2;
                Insets insets = getInsets();
                if (isAutoResize()) {
                    int l = graphicviewertext.getWidth() + insets.left
                            + insets.right;
                    int j1 = graphicviewertext.getHeight() + insets.top
                            + insets.bottom;
                    graphicviewerdrawable.setBoundingRect(
                            j - graphicviewertext.getWidth() / 2 - insets.left,
                            k - graphicviewertext.getHeight() / 2 - insets.top,
                            l, j1);
                } else {
                    int i1 = Math.max(graphicviewerdrawable.getWidth()
                            - (insets.left + insets.right), 0);
                    int k1 = Math.max(graphicviewerdrawable.getHeight()
                            - (insets.top + insets.bottom), 0);
                    graphicviewertext.setWidth(i1);
                    graphicviewertext.setWrappingWidth(i1);
                    graphicviewertext.recalcBoundingRect();
                    int l1 = Math.min(graphicviewertext.getHeight(), k1);
                    int i2 = graphicviewerdrawable.getLeft() + insets.left;
                    int j2 = graphicviewerdrawable.getTop() + insets.top
                            + (k1 - l1) / 2;
                    graphicviewertext.setBoundingRect(i2, j2, i1, l1);
                }
                graphicviewertext.setAlignment(0);
                graphicviewertext.setSpotLocation(0, j, k);
                if (getPort() != null) {
                    getPort().setBoundingRect(
                            graphicviewerdrawable.getBoundingRect());
                }
            } else {
                graphicviewertext.setAlignment(spotOpposite(i));
                graphicviewertext.setSpotLocation(spotOpposite(i),
                        graphicviewerdrawable, i);
            }
        }
        if (getPort() != null) {
            getPort().setSpotLocation(0, graphicviewerdrawable, 0);
        }
    }

    public int getLabelSpot() {
        return myLabelSpot;
    }

    public void setLabelSpot(int i) {
        internalSetLabelSpot(i, false);
    }

    private void internalSetLabelSpot(int i, boolean flag) {
        int j = myLabelSpot;
        if (j != i) {
            myLabelSpot = i;
            update(2101, j, null);
            if (!flag) {
                labelSpotChanged(j);
            }
        }
    }

    public void labelSpotChanged(int paramInt) {
        GraphicViewerPort localGraphicViewerPort = getPort();
        if (localGraphicViewerPort != null) {
            if (getLabelSpot() == 0) {
                localGraphicViewerPort.setStyle(0);
                setResizable(false);
            } else if (paramInt == 0) {
                localGraphicViewerPort.setStyle(2);
                GraphicViewerDrawable localGraphicViewerDrawable = getDrawable();
                int i = localGraphicViewerDrawable.getLeft()
                        + localGraphicViewerDrawable.getWidth() / 2;
                int j = localGraphicViewerDrawable.getTop()
                        + localGraphicViewerDrawable.getHeight() / 2;
                Rectangle localRectangle = new Rectangle(i
                        - DefaultPortSize.width / 2, j - DefaultPortSize.height
                        / 2, DefaultPortSize.width, DefaultPortSize.height);
                localGraphicViewerDrawable.setBoundingRect(i
                        - localRectangle.width / 2
                        - DefaultDrawableMargin.width, j
                        - localRectangle.height / 2
                        - DefaultDrawableMargin.height, localRectangle.width
                        + 2 * DefaultDrawableMargin.width,
                        localRectangle.height + 2
                                * DefaultDrawableMargin.height);
                localGraphicViewerPort.setBoundingRect(localRectangle);
            }
        }
        layoutChildren(getLabel());
    }

    public Insets getInsets() {
        return myInsets;
    }

    public void setInsets(Insets insets) {
        internalSetInsets(insets, false);
    }

    private void internalSetInsets(Insets insets, boolean flag) {
        Insets insets1 = myInsets;
        if (!insets1.equals(insets)) {
            Insets insets2 = new Insets(insets1.top, insets1.left,
                    insets1.bottom, insets1.right);
            myInsets.top = insets.top;
            myInsets.left = insets.left;
            myInsets.bottom = insets.bottom;
            myInsets.right = insets.right;
            update(2105, 0, insets2);
            if (!flag) {
                layoutChildren(null);
            }
        }
    }

    public void setAutoResize(boolean flag) {
        internalSetAutoResize(flag, false);
    }

    private void internalSetAutoResize(boolean flag, boolean flag1) {
        boolean flag2 = (getInternalFlags() & 0x10000) != 0;
        if (flag2 != flag) {
            if (flag) {
                setInternalFlags(getInternalFlags() | 0x10000);
            } else {
                setInternalFlags(getInternalFlags() & 0xfffeffff);
            }
            update(2106, flag2 ? 1 : 0, null);
            if (!flag1) {
                onAutoResizeChanged(flag2);
            }
        }
    }

    public boolean isAutoResize() {
        return (getInternalFlags() & 0x10000) != 0;
    }

    public void onAutoResizeChanged(boolean flag) {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            graphicviewertext.setMultiline(flag);
            graphicviewertext.setWrapping(flag);
            graphicviewertext.setClipping(flag);
        }
    }

    public GraphicViewerPen getPen() {
        return getDrawable().getPen();
    }

    public void setPen(GraphicViewerPen graphicviewerpen) {
        getDrawable().setPen(graphicviewerpen);
    }

    public GraphicViewerBrush getBrush() {
        return getDrawable().getBrush();
    }

    public void setBrush(GraphicViewerBrush graphicviewerbrush) {
        getDrawable().setBrush(graphicviewerbrush);
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
            removeObject(myLabel);
        } else if (getLabel() == null) {
            setLabel(createLabel(s));
        } else {
            getLabel().setText(s);
        }
    }

    public GraphicViewerDrawable getDrawable() {
        return myDrawable;
    }

    public void setDrawable(GraphicViewerDrawable graphicviewerdrawable) {
        GraphicViewerDrawable graphicviewerdrawable1 = myDrawable;
        if (graphicviewerdrawable1 != graphicviewerdrawable) {
            copyProperties(graphicviewerdrawable1, graphicviewerdrawable);
            if (graphicviewerdrawable1 != null) {
                removeObject(graphicviewerdrawable1);
            }
            myDrawable = graphicviewerdrawable;
            if (graphicviewerdrawable != null) {
                if (graphicviewerdrawable1 == null) {
                    graphicviewerdrawable.setSelectable(false);
                    graphicviewerdrawable.setResizable(false);
                }
                addObjectAtHead(graphicviewerdrawable);
            }
            update(2102, 0, graphicviewerdrawable1);
            if (getPort() != null
                    && getPort().getPortObject() == graphicviewerdrawable1) {
                getPort().setPortObject(graphicviewerdrawable);
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
            update(2103, 0, graphicviewertext1);
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
            update(2104, 0, graphicviewerport1);
            if (graphicviewerport != null
                    && graphicviewerport.getPortObject() == null) {
                graphicviewerport.setPortObject(getDrawable());
            }
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("drawable")) {
            myDrawable = (GraphicViewerDrawable) obj;
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
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerBasicNode",
                            domelement);
            domelement1
                    .setAttribute("labelspot", Integer.toString(myLabelSpot));
            domelement1.setAttribute("insets_top",
                    Integer.toString(myInsets.top));
            domelement1.setAttribute("insets_right",
                    Integer.toString(myInsets.right));
            domelement1.setAttribute("insets_bottom",
                    Integer.toString(myInsets.bottom));
            domelement1.setAttribute("insets_left",
                    Integer.toString(myInsets.left));
            if (myDrawable != null) {
                domdoc.registerReferencingNode(domelement1, "drawable",
                        myDrawable);
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
            myLabelSpot = Integer.parseInt(domelement1
                    .getAttribute("labelspot"));
            String s = domelement1.getAttribute("insets_top");
            if (s.length() > 0) {
                myInsets.top = Integer.parseInt(s);
            }
            String s1 = domelement1.getAttribute("insets_right");
            if (s1.length() > 0) {
                myInsets.right = Integer.parseInt(s1);
            }
            String s2 = domelement1.getAttribute("insets_bottom");
            if (s2.length() > 0) {
                myInsets.bottom = Integer.parseInt(s2);
            }
            String s3 = domelement1.getAttribute("insets_left");
            if (s3.length() > 0) {
                myInsets.left = Integer.parseInt(s3);
            }
            String s4 = domelement1.getAttribute("drawable");
            domdoc.registerReferencingObject(this, "drawable", s4);
            String s5 = domelement1.getAttribute("label");
            domdoc.registerReferencingObject(this, "label", s5);
            String s6 = domelement1.getAttribute("port");
            domdoc.registerReferencingObject(this, "port", s6);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2101 :
                graphicviewerdocumentchangededit.setNewValueInt(getLabelSpot());
                return;

            case 2102 :
                graphicviewerdocumentchangededit.setNewValue(getDrawable());
                return;

            case 2103 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;

            case 2104 :
                graphicviewerdocumentchangededit.setNewValue(getPort());
                return;

            case 2105 :
                Insets insets = getInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets.top, insets.left, insets.bottom, insets.right));
                return;

            case 2106 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isAutoResize());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2101 :
                internalSetLabelSpot(
                        graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 2102 :
                setDrawable((GraphicViewerDrawable) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2103 :
                setLabel((GraphicViewerText) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2104 :
                setPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2105 :
                internalSetInsets(
                        (Insets) graphicviewerdocumentchangededit
                                .getValue(flag),
                        true);
                return;

            case 2106 :
                internalSetAutoResize(
                        graphicviewerdocumentchangededit.getValueBoolean(flag),
                        true);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }
}