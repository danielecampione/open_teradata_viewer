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

import java.awt.Color;
import java.awt.Insets;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Comment extends GraphicViewerArea
        implements
            IGraphicViewerIdentifiablePart {
    private static final long serialVersionUID = -4074821858396012531L;

    public Comment() {
        myInsets = new Insets(2, 4, 8, 8);
        myPartID = -1;
        myLabel = null;
        myRect = null;
    }

    public Comment(String s) {
        myInsets = new Insets(2, 4, 8, 8);
        myPartID = -1;
        myLabel = null;
        myRect = null;
        initialize(s);
    }

    public void initialize(String s) {
        setResizable(false);
        myRect = new GraphicViewer3DNoteRect();
        myRect.setSelectable(false);
        myRect.setPen(GraphicViewerPen.lightGray);
        myRect.setBrush(GraphicViewerBrush.makeStockBrush(new Color(255, 255,
                204)));
        myLabel = new GraphicViewerText(s);
        myLabel.setMultiline(true);
        myLabel.setSelectable(false);
        myLabel.setResizable(false);
        myLabel.setDraggable(false);
        myLabel.setEditable(false);
        myLabel.setEditOnSingleClick(true);
        myLabel.setTransparent(true);
        addObjectAtHead(myRect);
        addObjectAtTail(myLabel);
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        Comment comment = (Comment) graphicviewerarea;
        comment.myPartID = myPartID;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        comment.myRect = (GraphicViewer3DNoteRect) graphicviewercopyenvironment
                .get(myRect);
        comment.myLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myLabel);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myRect)
            myRect = null;
        else if (graphicviewerobject == myLabel)
            myLabel = null;
        return graphicviewerobject;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            GraphicViewer3DNoteRect graphicviewer3dnoterect = getRect();
            if (graphicviewer3dnoterect != null) {
                Insets insets = getInsets();
                if (isResizable()) {
                    int i = Math.max(graphicviewer3dnoterect.getWidth()
                            - (insets.left + insets.right), 0);
                    int j = Math.max(graphicviewer3dnoterect.getHeight()
                            - (insets.top + insets.bottom), 0);
                    graphicviewertext.setClipping(true);
                    graphicviewertext.setWidth(i);
                    graphicviewertext.setWrappingWidth(i);
                    int k = Math.min(graphicviewertext.getHeight(), j);
                    int l = graphicviewer3dnoterect.getLeft() + insets.left;
                    int i1 = graphicviewer3dnoterect.getTop() + insets.top;
                    graphicviewertext.setBoundingRect(l, i1, i, k);
                } else {
                    graphicviewertext.setClipping(false);
                    graphicviewer3dnoterect.setBoundingRect(
                            graphicviewertext.getLeft() - insets.left,
                            graphicviewertext.getTop() - insets.top,
                            graphicviewertext.getWidth() + insets.left
                                    + insets.right,
                            graphicviewertext.getHeight() + insets.top
                                    + insets.bottom);
                }
            }
        }
    }

    public Insets getInsets() {
        return myInsets;
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("rectobj"))
            myRect = (GraphicViewer3DNoteRect) obj;
        else if (s.equals("label"))
            myLabel = (GraphicViewerText) obj;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.Comment", domelement);
            domelement1.setAttribute("partid", Integer.toString(myPartID));
            if (myRect != null)
                domdoc.registerReferencingNode(domelement1, "rectobj", myRect);
            if (myLabel != null)
                domdoc.registerReferencingNode(domelement1, "label", myLabel);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("partid");
            if (s.length() > 0)
                myPartID = Integer.parseInt(s);
            String s1 = domelement1.getAttribute("rectobj");
            domdoc.registerReferencingObject(this, "rectobj", s1);
            String s2 = domelement1.getAttribute("label");
            domdoc.registerReferencingObject(this, "label", s2);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public int getPartID() {
        return myPartID;
    }

    public void setPartID(int i) {
        int j = myPartID;
        if (j != i) {
            myPartID = i;
            update(0x1012c, j, null);
        }
    }

    public GraphicViewerText getLabel() {
        return myLabel;
    }

    public GraphicViewer3DNoteRect getRect() {
        return myRect;
    }

    public String getText() {
        return getLabel().getText();
    }

    public void setText(String s) {
        getLabel().setText(s);
    }

    public boolean isEditable() {
        return getLabel().isEditable();
    }

    public void setEditable(boolean flag) {
        getLabel().setEditable(flag);
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 65836 :
                graphicviewerdocumentchangededit.setNewValueInt(getPartID());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 65836 :
                setPartID(graphicviewerdocumentchangededit.getValueInt(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    private Insets myInsets;
    public static final int ChangedPartID = 0x1012c;
    private int myPartID;
    private GraphicViewerText myLabel;
    private GraphicViewer3DNoteRect myRect;
}