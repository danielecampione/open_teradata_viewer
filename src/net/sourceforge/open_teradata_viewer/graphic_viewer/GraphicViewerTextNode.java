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

import java.awt.Insets;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerTextNode extends GraphicViewerNode {

    private static final long serialVersionUID = -329622213870441858L;

    public static final int ChangedLabel = 2301;
    public static final int ChangedBackground = 2302;
    public static final int ChangedTopPort = 2303;
    public static final int ChangedRightPort = 2304;
    public static final int ChangedBottomPort = 2305;
    public static final int ChangedLeftPort = 2306;
    public static final int ChangedInsets = 2307;
    public static final int ChangedAutoResize = 2308;
    private static final int flagAutoResize = 65536;
    private GraphicViewerText myLabel = null;
    private GraphicViewerObject myBack = null;
    private GraphicViewerPort myTopPort = null;
    private GraphicViewerPort myRightPort = null;
    private GraphicViewerPort myBottomPort = null;
    private GraphicViewerPort myLeftPort = null;
    private Insets myInsets = new Insets(3, 3, 2, 2);

    public GraphicViewerTextNode() {
        initCommon();
    }

    public GraphicViewerTextNode(String s1) {
        initCommon();
        init(s1);
    }

    private void initCommon() {
        setInternalFlags(getInternalFlags() & 0xffffffef | 0x10000);
    }

    private void init(String s1) {
        setInitializing(true);
        myBack = createBackground();
        myLabel = createLabel(s1);
        myTopPort = createPort(2);
        myRightPort = createPort(4);
        myBottomPort = createPort(6);
        myLeftPort = createPort(8);
        addObjectAtHead(myBack);
        addObjectAtTail(myLabel);
        addObjectAtTail(myTopPort);
        addObjectAtTail(myRightPort);
        addObjectAtTail(myBottomPort);
        addObjectAtTail(myLeftPort);
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerObject createBackground() {
        GraphicViewer3DRect graphicviewer3drect = new GraphicViewer3DRect();
        graphicviewer3drect.setSelectable(false);
        return graphicviewer3drect;
    }

    public GraphicViewerText createLabel(String s1) {
        GraphicViewerText graphicviewertext = new GraphicViewerText();
        graphicviewertext.setSelectable(false);
        graphicviewertext.setResizable(false);
        graphicviewertext.setDraggable(false);
        graphicviewertext.setMultiline(true);
        graphicviewertext.setEditable(false);
        graphicviewertext.setEditOnSingleClick(true);
        graphicviewertext.setTransparent(true);
        graphicviewertext.setText(s1);
        return graphicviewertext;
    }

    public GraphicViewerPort createPort(int i) {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        graphicviewerport.setStyle(0);
        graphicviewerport.setSize(3, 3);
        graphicviewerport.setFromSpot(i);
        graphicviewerport.setToSpot(i);
        graphicviewerport.setAutoRescale(false);
        return graphicviewerport;
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerTextNode graphicviewertextnode = (GraphicViewerTextNode) graphicviewerarea;
        graphicviewertextnode.myInsets.top = myInsets.top;
        graphicviewertextnode.myInsets.left = myInsets.left;
        graphicviewertextnode.myInsets.bottom = myInsets.bottom;
        graphicviewertextnode.myInsets.right = myInsets.right;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewertextnode.myBack = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myBack);
        graphicviewertextnode.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
        graphicviewertextnode.myTopPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myTopPort);
        graphicviewertextnode.myRightPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myRightPort);
        graphicviewertextnode.myBottomPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myBottomPort);
        graphicviewertextnode.myLeftPort = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myLeftPort);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myBack) {
            myBack = null;
        } else if (graphicviewerobject == myLabel) {
            myLabel = null;
        } else if (graphicviewerobject == myTopPort) {
            myTopPort = null;
        } else if (graphicviewerobject == myRightPort) {
            myRightPort = null;
        } else if (graphicviewerobject == myBottomPort) {
            myBottomPort = null;
        } else if (graphicviewerobject == myLeftPort) {
            myLeftPort = null;
        }
        return graphicviewerobject;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing()) {
            return;
        }
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null) {
            return;
        }
        GraphicViewerObject graphicviewerobject1 = getBackground();
        if (graphicviewerobject1 != null) {
            Insets insets = getInsets();
            if (isAutoResize()) {
                graphicviewerobject1.setBoundingRect(
                        graphicviewertext.getLeft() - insets.left,
                        graphicviewertext.getTop() - insets.top,
                        graphicviewertext.getWidth() + insets.left
                                + insets.right, graphicviewertext.getHeight()
                                + insets.top + insets.bottom);
            } else {
                int i = Math.max(graphicviewerobject1.getWidth()
                        - (insets.left + insets.right), 0);
                int j = Math.max(graphicviewerobject1.getHeight()
                        - (insets.top + insets.bottom), 0);
                graphicviewertext.setWidth(i);
                graphicviewertext.setWrappingWidth(i);
                graphicviewertext.recalcBoundingRect();
                int k = Math.min(graphicviewertext.getHeight(), j);
                int l = graphicviewerobject1.getLeft() + insets.left;
                int i1 = graphicviewerobject1.getTop() + insets.top + (j - k)
                        / 2;
                graphicviewertext.setBoundingRect(l, i1, i, k);
            }
            if (getTopPort() != null) {
                getTopPort().setSpotLocation(6, graphicviewerobject1, 2);
            }
            if (getRightPort() != null) {
                getRightPort().setSpotLocation(8, graphicviewerobject1, 4);
            }
            if (getBottomPort() != null) {
                getBottomPort().setSpotLocation(2, graphicviewerobject1, 6);
            }
            if (getLeftPort() != null) {
                getLeftPort().setSpotLocation(4, graphicviewerobject1, 8);
            }
        }
    }

    public GraphicViewerText getLabel() {
        return myLabel;
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
        GraphicViewerText graphicviewertext1 = myLabel;
        if (graphicviewertext1 != graphicviewertext) {
            copyProperties(graphicviewertext1, graphicviewertext);
            if (graphicviewertext1 != null) {
                removeObject(graphicviewertext1);
            }
            myLabel = graphicviewertext;
            if (graphicviewertext != null) {
                addObjectAtTail(graphicviewertext);
            }
            update(2301, 0, graphicviewertext1);
        }
    }

    public GraphicViewerObject getBackground() {
        return myBack;
    }

    public void setBackground(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myBack;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null) {
                removeObject(graphicviewerobject1);
            }
            myBack = graphicviewerobject;
            if (graphicviewerobject != null) {
                addObjectAtHead(graphicviewerobject);
            }
            update(2302, 0, graphicviewerobject1);
        }
    }

    public GraphicViewerPort getTopPort() {
        return myTopPort;
    }

    public void setTopPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myTopPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                removeObject(graphicviewerport1);
            }
            myTopPort = graphicviewerport;
            if (graphicviewerport != null) {
                addObjectAtTail(graphicviewerport);
            }
            update(2303, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getRightPort() {
        return myRightPort;
    }

    public void setRightPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myRightPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                removeObject(graphicviewerport1);
            }
            myRightPort = graphicviewerport;
            if (graphicviewerport != null) {
                addObjectAtTail(graphicviewerport);
            }
            update(2304, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getBottomPort() {
        return myBottomPort;
    }

    public void setBottomPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myBottomPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                removeObject(graphicviewerport1);
            }
            myBottomPort = graphicviewerport;
            if (graphicviewerport != null) {
                addObjectAtTail(graphicviewerport);
            }
            update(2305, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getLeftPort() {
        return myLeftPort;
    }

    public void setLeftPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = myLeftPort;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null) {
                removeObject(graphicviewerport1);
            }
            myLeftPort = graphicviewerport;
            if (graphicviewerport != null) {
                addObjectAtTail(graphicviewerport);
            }
            update(2306, 0, graphicviewerport1);
        }
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
            update(2307, 0, insets2);
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
            update(2308, flag2 ? 1 : 0, null);
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
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable)) {
            return ((GraphicViewerDrawable) graphicviewerobject).getPen();
        } else {
            return null;
        }
    }

    public void setPen(GraphicViewerPen graphicviewerpen) {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable)) {
            ((GraphicViewerDrawable) graphicviewerobject)
                    .setPen(graphicviewerpen);
        }
    }

    public GraphicViewerBrush getBrush() {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable)) {
            return ((GraphicViewerDrawable) graphicviewerobject).getBrush();
        } else {
            return null;
        }
    }

    public void setBrush(GraphicViewerBrush graphicviewerbrush) {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable)) {
            ((GraphicViewerDrawable) graphicviewerobject)
                    .setBrush(graphicviewerbrush);
        }
    }

    public String getText() {
        if (myLabel != null) {
            return myLabel.getText();
        } else {
            return "";
        }
    }

    public void setText(String s1) {
        if (s1 == null) {
            return;
        }
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null) {
            myLabel = createLabel(s1);
            addObjectAtTail(myLabel);
        } else {
            graphicviewertext.setText(s1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2301 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;

            case 2302 :
                graphicviewerdocumentchangededit.setNewValue(getBackground());
                return;

            case 2303 :
                graphicviewerdocumentchangededit.setNewValue(getTopPort());
                return;

            case 2304 :
                graphicviewerdocumentchangededit.setNewValue(getRightPort());
                return;

            case 2305 :
                graphicviewerdocumentchangededit.setNewValue(getBottomPort());
                return;

            case 2306 :
                graphicviewerdocumentchangededit.setNewValue(getLeftPort());
                return;

            case 2307 :
                Insets insets = getInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets.top, insets.left, insets.bottom, insets.right));
                return;

            case 2308 :
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
            case 2301 :
                setLabel((GraphicViewerText) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2302 :
                setBackground((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2303 :
                setTopPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2304 :
                setRightPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2305 :
                setBottomPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2306 :
                setLeftPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2307 :
                internalSetInsets(
                        (Insets) graphicviewerdocumentchangededit
                                .getValue(flag),
                        true);
                return;

            case 2308 :
                internalSetAutoResize(
                        graphicviewerdocumentchangededit.getValueBoolean(flag),
                        true);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGUpdateReference(String s1, Object obj) {
        super.SVGUpdateReference(s1, obj);
        if (s1.equals("textlabel")) {
            myLabel = (GraphicViewerText) obj;
        } else if (s1.equals("backdraw")) {
            myBack = (GraphicViewerObject) obj;
        } else if (s1.equals("topport")) {
            myTopPort = (GraphicViewerPort) obj;
        } else if (s1.equals("rightport")) {
            myRightPort = (GraphicViewerPort) obj;
        } else if (s1.equals("bottomport")) {
            myBottomPort = (GraphicViewerPort) obj;
        } else if (s1.equals("leftport")) {
            myLeftPort = (GraphicViewerPort) obj;
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerTextNode",
                            domelement);
            domelement1.setAttribute("insetleft",
                    Integer.toString(myInsets.left));
            domelement1.setAttribute("insetright",
                    Integer.toString(myInsets.right));
            domelement1
                    .setAttribute("insettop", Integer.toString(myInsets.top));
            domelement1.setAttribute("insetbottom",
                    Integer.toString(myInsets.bottom));
            if (myLabel != null) {
                domdoc.registerReferencingNode(domelement1, "textlabel",
                        myLabel);
            }
            if (myBack != null) {
                domdoc.registerReferencingNode(domelement1, "backdraw", myBack);
            }
            if (myTopPort != null) {
                domdoc.registerReferencingNode(domelement1, "topport",
                        myTopPort);
            }
            if (myRightPort != null) {
                domdoc.registerReferencingNode(domelement1, "rightport",
                        myRightPort);
            }
            if (myBottomPort != null) {
                domdoc.registerReferencingNode(domelement1, "bottomport",
                        myBottomPort);
            }
            if (myLeftPort != null) {
                domdoc.registerReferencingNode(domelement1, "leftport",
                        myLeftPort);
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            myInsets.left = Integer.parseInt(domelement1
                    .getAttribute("insetleft"));
            myInsets.right = Integer.parseInt(domelement1
                    .getAttribute("insetright"));
            myInsets.top = Integer.parseInt(domelement1
                    .getAttribute("insettop"));
            myInsets.bottom = Integer.parseInt(domelement1
                    .getAttribute("insetbottom"));
            String s1 = domelement1.getAttribute("textlabel");
            domdoc.registerReferencingObject(this, "textlabel", s1);
            String s2 = domelement1.getAttribute("backdraw");
            domdoc.registerReferencingObject(this, "backdraw", s2);
            String s3 = domelement1.getAttribute("topport");
            domdoc.registerReferencingObject(this, "topport", s3);
            String s4 = domelement1.getAttribute("rightport");
            domdoc.registerReferencingObject(this, "rightport", s4);
            String s5 = domelement1.getAttribute("bottomport");
            domdoc.registerReferencingObject(this, "bottomport", s5);
            String s6 = domelement1.getAttribute("leftport");
            domdoc.registerReferencingObject(this, "leftport", s6);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }
}